package com.wishare.finance.domains.bill.aggregate.approve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.BillAdjustA;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.AdjustStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustWayEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApprovedActionEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.BillRefundRepository;
import com.wishare.finance.domains.bill.repository.BillRepository;
import com.wishare.finance.domains.bill.repository.BillRepositoryFactory;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.aggregate.data.BillJumpA;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillJumpE;
import com.wishare.finance.domains.bill.repository.BillJumpRepository;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 调整审核监听器
 *
 * @Author zhenghui
 * @Date 2023/7/21
 * @Version 1.0
 */
@Slf4j
public class JumpApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        BillJumpRepository billJumpRepository = Global.ac.getBean(BillJumpRepository.class);
        BillJumpE billJumpE = billJumpRepository.getByApproveId(billApprove.getId());
        billJumpE.setState(1);
        BillJumpA<B> billJumpA = new BillJumpA<>(bill, billApprove, billJumpE);

        //更新调整状态
        billJumpRepository.updateById(billJumpA);

        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption<>(new PlainTextDataItem("审核内容：", false)))
                .option(new ContentOption<>(new PlainTextDataItem("bpm审核通过", true), OptionStyle.normal())));
    }

    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        //设置调整状态
        BillJumpRepository billJumpRepository = Global.ac.getBean(BillJumpRepository.class);
        BillJumpE billJumpE = billJumpRepository.getByApproveId(billApprove.getId());
        billJumpE.setState(2);
        billJumpE.setRemark(reason);
        BillJumpA<B> billJumpA = new BillJumpA<>(bill, billApprove, billJumpE);
        //更新调整状态
        billJumpRepository.updateById(billJumpA);
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝,
                new Content().option(new ContentOption<>(new PlainTextDataItem("拒绝原因：", false)))
                        .option(new ContentOption<>(new PlainTextDataItem(StringUtils.isBlank(reason) ? "bpm跳收申请拒绝" : reason, true), OptionStyle.normal())));
    }

    /**
     * 调整账单后账单资源库操作
     *
     * @param bill 当前账单
     * @param approveState 审核状态
     * @param billAdjustA 调整聚合
     */
    public void saveBillInfo(B bill, Integer approveState, BillAdjustA<B> billAdjustA, BillApproveE billApprove) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(bill.getType());
        Long billId = null;
        if(BillApproveStateEnum.已审核.equalsByCode(approveState)
                && (BillSettleStateEnum.已结算.equalsByCode(bill.getSettleState()) || BillSettleStateEnum.部分结算.equalsByCode(bill.getSettleState()))){
            if (Objects.nonNull(billAdjustA.getAdjustType()) && BillAdjustTypeEnum.调高 == BillAdjustTypeEnum.valueOfByCode(billAdjustA.getAdjustType()) && Objects.nonNull(billAdjustA.newBill)) {
                //调高创建新账单
                BillRepository billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(bill.getType()));
                switch (billTypeEnum) {
                    case 应收账单:
                        //应收账单创建新账单
                        ReceivableBill receivableBill = (ReceivableBill) billAdjustA.newBill;
                        receivableBill.approve(BillApproveStateEnum.已审核);
                        receivableBill.setSettleState(BillSettleStateEnum.已结算.getCode());
                        receivableBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+"调高生成应收账单");

                        //创建新的结算记录
                        BillGatherDetailA<B> receivableBillSettleE = createBillSettle(billAdjustA.newBill);
                        receivableBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(receivableBill.getRoomId()));
                        billRepository.save(receivableBill);
                        billId = receivableBill.getId();
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(receivableBill.getBillNo());
                        Global.ac.getBean(GatherDetailRepository.class).save(receivableBillSettleE.getGatherDetail());
                        Global.ac.getBean(GatherBillRepository.class).save(receivableBillSettleE.getGatherBill());
                        break;
                    case 预收账单:
                        //预收账单创建新账单
                        AdvanceBill advanceBill = (AdvanceBill) billAdjustA.newBill;
                        if(advanceBill.getPayTime() == null){
                            advanceBill.setPayTime(LocalDateTime.now());
                        }
                        advanceBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                        advanceBill.setSettleState(BillSettleStateEnum.已结算.getCode());
                        advanceBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+"调高生成预收账单");

                        //创建新的结算记录
                        BillGatherDetailA<B> advanceBillSettleE = createBillSettle(billAdjustA.newBill);
                        advanceBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(advanceBill.getRoomId()));
                        billRepository.save(advanceBill);
                        billId = advanceBill.getId();
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(advanceBill.getBillNo());
                        Global.ac.getBean(GatherDetailRepository.class).save(advanceBillSettleE.getGatherDetail());
                        break;
                    case 临时收费账单:
                        //临时账单创建新账单
                        TemporaryChargeBill temporaryChargeBill = (TemporaryChargeBill) billAdjustA.newBill;
                        temporaryChargeBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                        temporaryChargeBill.setSettleState(BillSettleStateEnum.已结算.getCode());
                        temporaryChargeBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+"调高生成临时收费账单");
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(temporaryChargeBill.getBillNo());

                        //创建新的结算记录
                        BillGatherDetailA<B> temporaryChargeBillSettleE = createBillSettle(billAdjustA.newBill);
                        temporaryChargeBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(temporaryChargeBill.getRoomId()));
                        billRepository.save(temporaryChargeBill);
                        billId = temporaryChargeBill.getId();
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(temporaryChargeBill.getBillNo());
                        Global.ac.getBean(GatherDetailRepository.class).save(temporaryChargeBillSettleE.getGatherDetail());
                        Global.ac.getBean(GatherBillRepository.class).save(temporaryChargeBillSettleE.getGatherBill());
                        break;
                    default:
                        break;
                }
                //记录日志
                if (Objects.nonNull(billId)){
                    BizLog.normal(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.创建,
                            new Content().option(new ContentOption(new PlainTextDataItem("调高生成", true))));
                }
            }else{
                if(Objects.nonNull(billApprove.getApprovedAction())){
                    if(BillApprovedActionEnum.退款.equalsByCode(billApprove.getApprovedAction())){
                        //部分结算应收调整没有退款，需要进行判空
                        BillRefundE billRefundE = billAdjustA.billRefundE;
                        if(Objects.nonNull(billRefundE)){
                            //退款保存退款记录
                            Global.ac.getBean(BillRefundRepository.class).save(billRefundE);
                        }
                    }else if(BillApprovedActionEnum.转预收.equalsByCode(billApprove.getApprovedAction()) && Objects.nonNull(billAdjustA.advanceBill)){
                        AdvanceBillRepository billRepository = Global.ac.getBean(AdvanceBillRepository.class);
                        //预收账单创建新账单
                        AdvanceBill advanceBill = billAdjustA.advanceBill;
                        advanceBill.setChargeItemId(billApprove.getChargeItemId());
                        advanceBill.setChargeItemName(billApprove.getChargeItemName());
                        advanceBill.setSettleAmount(advanceBill.getTotalAmount());
                        advanceBill.resetBillCycle();
                        if(advanceBill.getPayTime() == null){
                            advanceBill.setPayTime(LocalDateTime.now());
                        }
                        advanceBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                        advanceBill.setSettleState(BillSettleStateEnum.已结算.getCode());
                        advanceBill.setDescription(BillTypeEnum.valueOfByCode(bill.getType()).getValue()+bill.getBillNo()+BillAdjustTypeEnum.valueOfByCode(billAdjustA.getAdjustType()).getValue()+"生成预收账单");
                        advanceBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(advanceBill.getRoomId()));
                        billRepository.save(advanceBill);
                        billId = advanceBill.getId();
                        //创建新的结算记录
                        GatherDetail advanceBillSettleE = createAdvanceBillSettle(advanceBill);
                        advanceBillSettleE.setChargeStartTime(advanceBill.getStartTime());
                        advanceBillSettleE.setChargeEndTime(advanceBill.getEndTime());
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(advanceBill.getBillNo());
                        bill.setCarriedAmount(bill.getCarriedAmount() + advanceBill.getSettleAmount());
                        Global.ac.getBean(GatherDetailRepository.class).save(advanceBillSettleE);
                        //记录日志
                        if (Objects.nonNull(billId)){
                            BizLog.normal(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.创建,
                                    new Content().option(new ContentOption(new PlainTextDataItem("调整转预收生成", true))));
                        }
                    }
                }
            }
        }else{
            //未审核已结算状态调整修改结算记录
            GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
            if( BillSettleStateEnum.已结算.equalsByCode(bill.getSettleState())){
                switch (BillAdjustWayEnum.valueOfByCode(billAdjustA.getAdjustWay())){
                    case ACTUAL_REDUCE:
                    case ACTUAL_COUPON:
                    case ACTUAL_VACANT_HOUSE_DISCOUNT:
                    case ACTUAL_OTHER:
                        //初始审核已结算调低和减免，更新结算记录
                        List<GatherDetail> gatherDetailList = new ArrayList<>();
                        if(BillTypeEnum.预收账单.equalsByCode(bill.getType())){
                            gatherDetailList = gatherDetailRepository.queryByGatherBillId(bill.getId(), bill.getSupCpUnitId());
                        }else if(BillTypeEnum.应收账单.equalsByCode(bill.getType()) || BillTypeEnum.临时收费账单.equalsByCode(bill.getType())){
                            gatherDetailList = gatherDetailRepository.listByRecBillId(bill.getId(), bill.getSupCpUnitId());
                        }
                        if(CollectionUtils.isNotEmpty(gatherDetailList) && gatherDetailList.size() == 1){
                            GatherDetail billSettleE = gatherDetailList.get(0);
                            billSettleE.setPayAmount(bill.getSettleAmount());
                            billSettleE.setPayTime(LocalDateTime.now());
                            gatherDetailRepository.update(billSettleE, new QueryWrapper<GatherDetail>()
                                .eq("id", billSettleE.getId())
                                .eq("sup_cp_unit_id", billSettleE.getSupCpUnitId()));
                        }
                        break;
                    default: break;
                }
            }
        }

        // 调整-开发代付生成一个应收账单
        if (BillApproveStateEnum.已审核.equalsByCode(approveState) &&
                BillAdjustWayEnum.PAYMENT_ON_BEHALF.equalsByCode(billAdjustA.getAdjustWay())) {
            //调整-开发代付创建新账单
            BillRepository billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(bill.getType()));
            Long newBillId = null;
            if (bill instanceof ReceivableBill) {
                //应收账单创建新账单
                ReceivableBill receivableBill = (ReceivableBill) bill;
                ReceivableBill newBill = Global.mapperFacade.map(receivableBill, ReceivableBill.class);
                newBill.copyReset();
                newBill.resetAmount(Math.abs(billAdjustA.getAdjustAmount()));
                newBill.setPayerPhone(null);
                newBill.setPayInfos(null);
                newBill.setPayerType(1);
                newBill.setPayerName(billAdjustA.getDeveloperName());
                newBill.setPayerId(billAdjustA.getDeveloperId());
                newBill.init();
                newBill.setDescription("开发代付");
                newBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(newBill.getRoomId()));
                billRepository.save(newBill);
                billAdjustA.setSeparateBillNo(newBill.getBillNo());
                newBillId = newBill.getId();
            }else if (bill instanceof TemporaryChargeBill) {
                //临时账单创建新账单
                TemporaryChargeBill temporaryChargeBill = (TemporaryChargeBill) bill;
                TemporaryChargeBill newTemporaryBill = Global.mapperFacade.map(temporaryChargeBill,
                        TemporaryChargeBill.class);
                newTemporaryBill.copyReset();
                newTemporaryBill.resetAmount(Math.abs(billAdjustA.getAdjustAmount()));
                newTemporaryBill.setPayerPhone(null);
                newTemporaryBill.setPayInfos(null);
                newTemporaryBill.setPayerType(1);
                newTemporaryBill.setPayerName(billAdjustA.getDeveloperName());
                newTemporaryBill.setPayerId(billAdjustA.getDeveloperId());
                newTemporaryBill.setDescription("开发代付");
                newTemporaryBill.init();
                newTemporaryBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(newTemporaryBill.getRoomId()));
                billRepository.save(newTemporaryBill);
                billAdjustA.setSeparateBillNo(newTemporaryBill.getBillNo());
                newBillId = newTemporaryBill.getId();
            }
            //记录日志
            if (Objects.nonNull(newBillId)){
                BizLog.normal(String.valueOf(newBillId), LogContext.getOperator(), LogObject.账单, LogAction.创建,
                        new Content().option(new ContentOption(new PlainTextDataItem("开发代付生成", true))));
            }
        }
    }

    /**
     * 创建结算记录数据
     *
     * @param bill 账单
     * @return BillSettleE
     */
    private BillGatherDetailA<B> createBillSettle(B bill) {
        AddBillSettleCommand addBillSettleCommand = new AddBillSettleCommand();
        addBillSettleCommand.setPayeeId(ThreadLocalUtil.curIdentityInfo().getUserId());
        addBillSettleCommand.setPayeeName(ThreadLocalUtil.curIdentityInfo().getUserName());
        addBillSettleCommand.setPayerId(bill.getPayerId());
        addBillSettleCommand.setPayerName(bill.getPayerName());
        addBillSettleCommand.setPayAmount(bill.getReceivableAmount());
        addBillSettleCommand.setSettleAmount(bill.getReceivableAmount());
        addBillSettleCommand.setSettleWay(SettleWayEnum.线上.getCode());
        addBillSettleCommand.setSettleChannel(SettleChannelEnum.其他.getCode());
        addBillSettleCommand.setSettleTime(LocalDateTime.now());
        BillGatherDetailA<B> billSettleA = new BillGatherDetailA<>(bill);
        if(bill instanceof ReceivableBill){
            ReceivableBill receivableBill = (ReceivableBill) bill;
            addBillSettleCommand.setChargeStartTime(receivableBill.getStartTime());
            addBillSettleCommand.setChargeEndTime(receivableBill.getEndTime());
            BillGatherA<B> billGatherA = new BillGatherA(List.of(bill),List.of(addBillSettleCommand));
            billSettleA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
            billSettleA.gather(addBillSettleCommand);
        }else if(bill instanceof TemporaryChargeBill){
            TemporaryChargeBill temporaryChargeBill = (TemporaryChargeBill) bill;
            addBillSettleCommand.setChargeStartTime(temporaryChargeBill.getStartTime());
            addBillSettleCommand.setChargeEndTime(temporaryChargeBill.getEndTime());
            BillGatherA<B> billGatherA = new BillGatherA(List.of(bill),List.of(addBillSettleCommand));
            billSettleA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
            billSettleA.gather(addBillSettleCommand);
        }else if(bill instanceof AdvanceBill){
            AdvanceBill advanceBill = (AdvanceBill) bill;
            addBillSettleCommand.setChargeStartTime(advanceBill.getStartTime());
            addBillSettleCommand.setChargeEndTime(advanceBill.getEndTime());
            GatherBill gatherBill = new GatherBill();
            gatherBill.setId(advanceBill.getId());
            gatherBill.setBillNo(advanceBill.getBillNo());
            billSettleA = new BillGatherDetailA<>(bill, gatherBill);
            billSettleA.gather(addBillSettleCommand);
        }
        return billSettleA;
    }

    /**
     * 创建结算记录数据
     *
     * @param bill 预收账单
     * @return BillSettleE
     */
    private GatherDetail createAdvanceBillSettle(AdvanceBill bill) {
        GatherDetail billSettleE = new GatherDetail()
                .setGatherBillId(bill.getId())
                .setGatherBillNo(bill.getBillNo())
                .setChargeItemId(bill.getChargeItemId())
                .setChargeItemName(bill.getChargeItemName())
                .setCostCenterId(bill.getCostCenterId())
                .setCostCenterName(bill.getCostCenterName())
                .setSupCpUnitId(bill.getCommunityId())
                .setSupCpUnitName(bill.getCommunityName())
                .setCpUnitId(bill.getRoomId())
                .setCpUnitName(bill.getRoomName())
                .setRecPayAmount(bill.getSettleAmount())
                .setPayChannel(StringUtils.isEmpty(bill.getSettleChannel()) ? SettleChannelEnum.其他.getCode() : bill.getSettleChannel())
                .setPayWay(Objects.isNull(bill.getSettleWay()) ? SettleWayEnum.线上.getCode() : bill.getSettleWay())
                .setPayAmount(bill.getSettleAmount())
                .setPayTime(Objects.nonNull(bill.getPayTime()) ? bill.getPayTime() : LocalDateTime.now())
                .setChargeStartTime(bill.getStartTime())
                .setChargeEndTime(bill.getEndTime())
                .setPayerType(bill.getPayerType())
                .setPayerId(bill.getPayerId())
                .setPayerName(bill.getPayerName())
                .setPayeeId(ThreadLocalUtil.curIdentityInfo().getUserId())
                .setPayeeName(ThreadLocalUtil.curIdentityInfo().getUserName())
                .setGatherType(GatherTypeEnum.预收.getCode());
        billSettleE.init();
        return billSettleE;
    }
}
