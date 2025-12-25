package com.wishare.finance.domains.bill.aggregate;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemV;
import com.wishare.finance.apps.service.configure.chargeitem.ChargeItemAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.invoicereceipt.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.infrastructure.conts.CarryoverConst;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 调整聚合
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
@Getter
@Setter
@Slf4j
public class BillCarryoverA<B extends Bill,T extends Bill> extends BillCarryoverE {

    /**
     * 账单信息
     */
    private B bill;
    private BillApproveE billApprove;
    private GatherBill gatherBill;

    /**
     * 被结转的账单
     */
    private List<T> targetBills;
    private List<GatherDetail> targetSettles;

    /**
     * 结转剩余未抵扣的金额
     */
    private AdvanceBill advanceBill;
    private GatherDetail advanceBillSettle;

    public BillCarryoverA() {
    }

    public BillCarryoverA(B bill, BillApproveE billApprove, BillCarryoverE billCarryoverE, List<T> targetBills) {
        this.bill = bill;
        this.billApprove = billApprove;
        this.targetBills  = targetBills;
        this.targetSettles = new ArrayList<>();
        if (Objects.nonNull(billCarryoverE)) {
            Global.mapperFacade.map(billCarryoverE, this);
        }
    }

    /**
     * 结转
     * @return
     */
    public boolean carryover() {
        setCarryoverTime(LocalDateTime.now());
        setState(CarryoverStateEnum.已生效.getCode());
        setBillType(bill.getType());
        CarryoverTypeEnum carryoverTypeEnum = CarryoverTypeEnum.valueOfByCode(getCarryoverType());
        switch (carryoverTypeEnum){
            case 抵扣:
                ErrorAssertUtil.isFalseThrow300(org.springframework.util.CollectionUtils.isEmpty(targetBills),ErrorMessage.CARRYOVER_BILL_NOT_EXIST);
                doDiscount();
                break;
            case 结转预收:
                doCarryoverAdvance();
                break;
            default: return false;
        }
        bill.setCarriedAmount(bill.getCarriedAmount() + getCarryoverAmount());
        bill.resetCarriedState();
        return true;
    }

    /**
     * 抵扣
     */
    private void doDiscount(){
        //生成新的收款单
        if (gatherBill == null) {
            gatherBill = createBillSettle();
        }
        // 获取优惠赠送可结转金额
        Long deductibleAmount = bill.getDeductibleAmount();
        // 实收可结转金额
        Long canCarriedAmount = bill.getActualSettleAmount();

        Map<Long, List<CarryoverDetail>> targetBillMap = getCarryoverDetail().stream().collect(Collectors.groupingBy(CarryoverDetail::getTargetBillId));
        CarryoverDetail carryoverDetail;
        List<CarryoverDetail> carryoverDetails;

        for (T targetBill : targetBills) {
            carryoverDetails = targetBillMap.get(targetBill.getId());
            if (CollectionUtils.isNotEmpty(carryoverDetails)){
                carryoverDetail = carryoverDetails.get(0);
                carryoverDetail.setTargetBillNo(targetBill.getBillNo());
                // 如果需结转金额不超过实收可用结转部分 则直接用其即可
                if (canCarriedAmount>=carryoverDetail.getCarryoverAmount()){
                    long actualUnpayAmount = Optional.of(targetBill.getActualUnpayAmount()).orElse(0L);
                    carryoverDetail.setActualCarryoverAmount(Math.min(actualUnpayAmount, carryoverDetail.getCarryoverAmount()));
                    targetSettles.add(doSettleByReceive(targetBill, carryoverDetail));
                    canCarriedAmount = canCarriedAmount - carryoverDetail.getCarryoverAmount();
                }else {
                    // 如果需要用到优惠金额 则进行结转减免明细处理
                    if (canCarriedAmount != 0){
                        // 将剩余的实收可结转金额用掉
                        long actualUnpayAmount = Optional.of(targetBill.getActualUnpayAmount()).orElse(0L);
                        carryoverDetail.setActualCarryoverAmount(Math.min(actualUnpayAmount, Math.min(canCarriedAmount,carryoverDetail.getCarryoverAmount())));
                        targetSettles.add(doSettleByReceive(targetBill, carryoverDetail));
                    }
                    // 需要优惠部分补充的添加减免记录
                    long presentAmount = carryoverDetail.getCarryoverAmount() - canCarriedAmount;
                    applyPresentAmount(bill,presentAmount,targetBill);
                    deductibleAmount = deductibleAmount - presentAmount;
                    canCarriedAmount = 0L;
                }
            }
        }

        // 重新计算实际结转金额
        reSetCarryoverAmount();
    }

    /**
     * 优惠赠送金额减免结转
     *
     * @param advanceBill   预收账单
     * @param presentAmount 优惠金额
     * @param targetBill    目标账单
     */
    private void applyPresentAmount(B advanceBill, long presentAmount, T targetBill){
        if (!(advanceBill instanceof AdvanceBill)){return;}
        // 原账单添加一条正数应收减免记录
        List<BillAdjustE> result = new ArrayList<>();
        BillAdjustE billAdjustE = new BillAdjustE();
        billAdjustE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_ADJUST));
        billAdjustE.setBillId(advanceBill.getId());
        billAdjustE.setBillType(BillTypeEnum.预收账单.getCode());
        billAdjustE.setAdjustWay(BillAdjustWayEnum.ADJUST_CARRYOVER.getCode());
        billAdjustE.setDeductionMethod(DeductionMethodEnum.应收减免.getCode());
        billAdjustE.setContent("因:结转:使用预存优惠赠送金额:通过:结转"+ BigDecimal.valueOf(presentAmount).divide(Const.BIG_DECIMAL_HUNDRED,2, RoundingMode.HALF_UP) +"元");
        billAdjustE.setReason(99);
        billAdjustE.setBillAmount(advanceBill.getTotalAmount());
        billAdjustE.setAdjustAmount(presentAmount);
        billAdjustE.setAdjustType(BillAdjustTypeEnum.减免.getCode());
        billAdjustE.setState(Const.State._2);
        billAdjustE.setApproveTime(LocalDateTime.now());
        billAdjustE.setAdjustTime(LocalDateTime.now());
        billAdjustE.setFileVos(new ArrayList<>());
        billAdjustE.setInferenceState(Const.State._0);
        billAdjustE.setIsExact(Const.State._2);
        billAdjustE.setRemark("结转给账单"+targetBill.getBillNo());
        advanceBill.setDeductibleAmount(advanceBill.getDeductibleAmount()-presentAmount);
        advanceBill.refresh();
        Global.ac.getBean(AdvanceBillRepository.class).update((AdvanceBill) advanceBill,new UpdateWrapper<AdvanceBill>()
                .eq("id",targetBill.getId()));
        // 结转账单新增一条负数减免
        BillAdjustE adjustE = new BillAdjustE();
        adjustE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_ADJUST));
        adjustE.setBillId(targetBill.getId());
        adjustE.setBillType(targetBill.getType());
        adjustE.setAdjustWay(BillAdjustWayEnum.ADJUST_CARRYOVER.getCode());
        adjustE.setDeductionMethod(DeductionMethodEnum.应收减免.getCode());
        adjustE.setContent("因:结转:使用预存优惠赠送金额:通过:减免了"+ BigDecimal.valueOf(presentAmount).divide(Const.BIG_DECIMAL_HUNDRED,2, RoundingMode.HALF_UP) +"元");
        adjustE.setReason(99);
        adjustE.setBillAmount(targetBill.getTotalAmount());
        adjustE.setAdjustAmount(-presentAmount);
        adjustE.setAdjustType(BillAdjustTypeEnum.减免.getCode());
        adjustE.setState(Const.State._2);
        adjustE.setApproveTime(LocalDateTime.now());
        adjustE.setAdjustTime(LocalDateTime.now());
        adjustE.setFileVos(new ArrayList<>());
        adjustE.setInferenceState(Const.State._0);
        adjustE.setIsExact(Const.State._2);
        adjustE.setRemark("由账单"+advanceBill.getBillNo()+"结转而来");
        result.add(adjustE);
        result.add(billAdjustE);
        targetBill.setDeductibleAmount(targetBill.getDeductibleAmount()+presentAmount);
        targetBill.refresh();
        BillRepository<T> billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(targetBill.getType()));
        billRepository.update(targetBill,new UpdateWrapper<T>()
                .eq("id",targetBill.getId()).eq("sup_cp_unit_id",targetBill.getSupCpUnitId()));
        Global.ac.getBean(BillAdjustRepository.class).saveBatch(result);
    }


    /**
     * 结转预收
     */
    private void doCarryoverAdvance(){
        //生成新的收款单
        if (gatherBill == null) {
            log.info("处理结转预收，生成新的收款单");
            gatherBill = createBillSettle();
        }
        AdvanceBill advanceBill = Global.mapperFacade.map(bill, AdvanceBill.class);
        if (!CarryoverConst.BILL_ESTIMATED_INCOME.equals(
                Optional.ofNullable(this.billApprove).orElse(new BillApproveE()).getExtField1())
        ) {
            advanceBill.setStartTime(null);
            advanceBill.setEndTime(null);
        }
        advanceBill.resetState();
        advanceBill.resetAmount(getCarryoverAmount());
        advanceBill.approve(BillApproveStateEnum.已审核);
        advanceBill.setPayTime(getCarryoverTime());
        advanceBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+"结转生成预收账单");
        advanceBill.setSource("财务中心-结转预收");
        advanceBill.setPayWay(SettleWayEnum.结转.getCode());
        advanceBill.setPayChannel(SettleChannelEnum.结转.getCode());
        advanceBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        advanceBill.setSettleState(BillSettleStateEnum.已结算.getCode());
        advanceBill.setGmtCreate(LocalDateTime.now());
        advanceBill.setId(null);
        advanceBill.setBillNo(null);
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            advanceBill.setTaxRate(new BigDecimal("0.00"));
            advanceBill.setTaxRateId(136157205772010L);
        }
        advanceBill.init();
        setAdvanceBill(advanceBill);
        getCarryoverDetail().forEach(carryoverDetail ->{
            carryoverDetail.setTargetBillId(advanceBill.getId());
            carryoverDetail.setTargetBillNo(advanceBill.getBillNo());
            advanceBill.setChargeItemId(carryoverDetail.getChargeItemId());
            advanceBill.setChargeItemName(carryoverDetail.getChargeItemName());
            setAdvanceBillSettle(doSettleByAdvance(getAdvanceBill(), carryoverDetail, gatherBill));
        });
    }

    /**
     * 应收账单结转结算
     * @param targetBill    账单
     * @param carryoverDetail   结转明细
     * @return BillSettleA
     */
    private GatherDetail doSettleByReceive(T targetBill, CarryoverDetail carryoverDetail){
        AddBillSettleCommand addBillSettleCommand = new AddBillSettleCommand();
        addBillSettleCommand.setPayeeId(targetBill.getPayeeId());
        addBillSettleCommand.setPayeeName(targetBill.getPayeeName());
        addBillSettleCommand.setPayerId(bill.getPayerId());
        addBillSettleCommand.setPayerName(bill.getPayerName());
        addBillSettleCommand.setPayerType(bill.getPayerType());
        addBillSettleCommand.setPayAmount(carryoverDetail.getActualCarryoverAmount());
        addBillSettleCommand.setSettleAmount(carryoverDetail.getActualCarryoverAmount());
        addBillSettleCommand.setSettleWay(SettleWayEnum.结转.getCode());
        addBillSettleCommand.setSettleChannel(SettleChannelEnum.结转.getCode());
        // 如果押金收费类型的账单结转，则对应被结转的收款明细为押金结转收款渠道
        ChargeItemV chargeItemV = Global.ac.getBean(ChargeItemAppService.class).getById(bill.getChargeItemId());
        if (ObjectUtils.allNotNull(chargeItemV,chargeItemV.getType()) && chargeItemV.getType().equals(Const.State._3) && TenantUtil.bf24()){
            addBillSettleCommand.setSettleChannel(SettleChannelEnum.押金结转.getCode());
        }
        addBillSettleCommand.setCarriedBillPayChannel(getCarriedPayChannel(bill));
        addBillSettleCommand.setSettleTime(LocalDateTime.now());
        addBillSettleCommand.setChargeStartTime(carryoverDetail.getChargeStartTime());
        addBillSettleCommand.setChargeEndTime(carryoverDetail.getChargeEndTime());
        BillGatherDetailA<T> billGatherDetailA = new BillGatherDetailA<>(targetBill, gatherBill);
        billGatherDetailA.gather(addBillSettleCommand);
        billGatherDetailA.getGatherDetail().setBillCarryoverId(getId());
        // 刷新账单状态
        targetBill.refresh();
        return billGatherDetailA.getGatherDetail();
    }

    /**
     * 获取结转账单的收款方式
     * @param bill
     * @return
     */
    private String getCarriedPayChannel(B bill) {
        //收款明细资源库
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        //获取结转账单的收款单
        List<GatherDetail> gatherDetailList = Optional
                .ofNullable(gatherDetailRepository.getListByRecBillId(bill.getId(), bill.getSupCpUnitId()))
                .orElse(new ArrayList<>())
                .stream().filter(e -> e.getRemainingCarriedAmount() > 0)
                .collect(Collectors.toList());
        // 处理普通缴费溢出情况
        if (CollectionUtils.isEmpty(gatherDetailList)){
            return null;
        }
        if (SettleChannelEnum.结转.getCode().equals(gatherDetailList.get(0).getPayChannel())){
            return Optional.ofNullable(gatherDetailList.get(0).getCarriedBillPayChannel()).orElse(SettleChannelEnum.其他.getCode());
        }
        return Optional.ofNullable(gatherDetailList.get(0).getPayChannel()).orElse(SettleChannelEnum.其他.getCode());
    }

    /**
     * 预收账单结转结算
     * @param advanceBill       应收账单
     * @param carryoverDetail   结转明细
     * @return GatherDetail
     */
    private GatherDetail doSettleByAdvance(AdvanceBill advanceBill, CarryoverDetail carryoverDetail, GatherBill gatherBill){
        carryoverDetail.setActualCarryoverAmount(getCarryoverAmount());
        advanceBill.settle(getCarryoverAmount(),0L);
        return createAdvanceBillSettle(advanceBill, gatherBill);
    }

    /**
     * 创建预收单
     * @param advanceAmount  预收金额
     */
    private void createAdvance(long advanceAmount){
        if (advanceAmount < 1){
            return;
        }
        //生成新的收款单
        if (gatherBill == null) {
            gatherBill = createBillSettle();
        }
        AdvanceBill advanceBill = Global.mapperFacade.map(bill, AdvanceBill.class);
        advanceBill.resetState();
        advanceBill.resetAmount(advanceAmount);
        advanceBill.approve(BillApproveStateEnum.已审核);
        advanceBill.setPayTime(getCarryoverTime());
        advanceBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+"结转生成预收账单");
        advanceBill.setSource("财务中心-应收结转");
        advanceBill.setPayWay(SettleWayEnum.结转.getCode());
        advanceBill.setPayChannel(SettleChannelEnum.结转.getCode());
        advanceBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        advanceBill.setGmtCreate(LocalDateTime.now());
        advanceBill.setId(null);
        advanceBill.setBillNo(null);
        advanceBill.init();
        advanceBill.settle(advanceAmount,0L);
        GatherDetail advanceBillSettle = createAdvanceBillSettle(advanceBill, this.gatherBill);
        this.advanceBill = advanceBill;
        this.advanceBillSettle = advanceBillSettle;
    }

    /**
     * 创建结算记录数据
     *
     * @return BillSettleE
     */
    private GatherBill createBillSettle() {
        AddBillSettleCommand addBillSettleCommand = new AddBillSettleCommand();
        addBillSettleCommand.setPayeeId(ThreadLocalUtil.curIdentityInfo().getUserId());
        addBillSettleCommand.setPayeeName(ThreadLocalUtil.curIdentityInfo().getUserName());
        addBillSettleCommand.setPayerId(bill.getPayerId());
        addBillSettleCommand.setPayerName(bill.getPayerName());
        addBillSettleCommand.setPayAmount(getCarryoverAmount());
        addBillSettleCommand.setSettleAmount(getCarryoverAmount());
        addBillSettleCommand.setSettleWay(SettleWayEnum.结转.getCode());
        addBillSettleCommand.setSettleChannel(SettleChannelEnum.结转.getCode());
        // 如果押金收费类型的账单结转，则对应被结转的收款明细为押金结转收款渠道
        ChargeItemV chargeItemV = Global.ac.getBean(ChargeItemAppService.class).getById(bill.getChargeItemId());
        if (ObjectUtils.allNotNull(chargeItemV,chargeItemV.getType()) && chargeItemV.getType().equals(Const.State._3) && TenantUtil.bf24()){
            addBillSettleCommand.setSettleChannel(SettleChannelEnum.押金结转.getCode());
        }
        addBillSettleCommand.setSettleTime(LocalDateTime.now());
        addBillSettleCommand.setSupCpUnitId(bill.getSupCpUnitId());
        if(bill instanceof ReceivableBill){
            ReceivableBill receivableBill = (ReceivableBill) bill;
            addBillSettleCommand.setChargeStartTime(receivableBill.getStartTime());
            addBillSettleCommand.setChargeEndTime(receivableBill.getEndTime());
        }else if(bill instanceof TemporaryChargeBill){
            TemporaryChargeBill temporaryChargeBill = (TemporaryChargeBill) bill;
            addBillSettleCommand.setChargeStartTime(temporaryChargeBill.getStartTime());
            addBillSettleCommand.setChargeEndTime(temporaryChargeBill.getEndTime());
        }
        BillGatherA<B> billGatherA = new BillGatherA(List.of(bill),List.of(addBillSettleCommand));
        return billGatherA.getGatherBill();
    }

    /**
     * 创建预收账单结算记录数据
     *
     * @param bill 预收账单
     * @return BillSettleE
     */
    private GatherDetail createAdvanceBillSettle(AdvanceBill bill, GatherBill gatherBill) {
        GatherDetail billSettleE = new GatherDetail()
                .setGatherBillId(gatherBill.getId())
                .setGatherBillNo(gatherBill.getBillNo())
                .setRecBillId(bill.getId())
                .setRecBillNo(bill.getBillNo())
                .setChargeItemId(bill.getChargeItemId())
                .setChargeItemName(bill.getChargeItemName())
                .setCostCenterId(bill.getCostCenterId())
                .setCostCenterName(bill.getCostCenterName())
                .setSupCpUnitId(bill.getCommunityId())
                .setSupCpUnitName(bill.getCommunityName())
                .setCpUnitId(bill.getRoomId())
                .setCpUnitName(bill.getRoomName())
                .setRecPayAmount(bill.getSettleAmount())
                .setPayChannel(SettleChannelEnum.结转.getCode())
                .setPayWay(SettleWayEnum.结转.getCode())
                .setPayAmount(bill.getSettleAmount())
                .setPayTime(Objects.nonNull(bill.getPayTime()) ? bill.getPayTime() : LocalDateTime.now())
                .setChargeStartTime(bill.getStartTime())
                .setChargeEndTime(bill.getEndTime())
                .setPayerType(bill.getPayerType())
                .setPayerId(bill.getPayerId())
                .setPayerName(bill.getPayerName())
                .setPayeeId(bill.getPayeeId())
                .setPayeeName(bill.getPayeeName())
                .setChargeStartTime(bill.getStartTime())
                .setChargeEndTime(bill.getEndTime())
                .setRemark(gatherBill.getRemark())
                .setGatherType(GatherTypeEnum.预收.getCode());
        log.info("初始化预收账单结算记录数据:{}", JSONObject.toJSONString(billSettleE));
        // 如果押金收费类型的账单结转，则对应被结转的收款明细为押金结转收款渠道
        ChargeItemV chargeItemV = Global.ac.getBean(ChargeItemAppService.class).getById(this.bill.getChargeItemId());
        if (ObjectUtils.allNotNull(chargeItemV,chargeItemV.getType()) && chargeItemV.getType().equals(Const.State._3) && TenantUtil.bf24()){
            billSettleE.setPayChannel(SettleChannelEnum.押金结转.getCode());
        }
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            billSettleE.setCarriedBillPayChannel(getCarriedPayChannel(this.bill));
        }
        billSettleE.init();
        log.info("填充预收账单结算记录数据:{}", JSONObject.toJSONString(billSettleE));
        if (BillInvoiceStateEnum.已开票.getCode().equals(gatherBill.getInvoiceState())) {
            // 如果其中有未开票的收款单明细，修改收款单为部分开票
            gatherBill.setInvoiceState(BillInvoiceStateEnum.部分开票.getCode());
            log.info("收款单明细中还有未开票账单，收款单状态为已开票，状态不一致,更新收款单状态：{}", gatherBill.getInvoiceState());
        }
        return billSettleE;
    }
}
