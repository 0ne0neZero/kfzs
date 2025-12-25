package com.wishare.finance.domains.bill.aggregate.approve;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.ChargeOverdueBatchDeleteF;
import com.wishare.finance.apps.model.bill.fo.ReceivableBillInvalidF;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.BillAdjustA;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillCarryoverDetailE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.finance.domains.bill.entity.ChargeOverdueE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.event.BillAdjustMqDetail;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.repository.BillRefundRepository;
import com.wishare.finance.domains.bill.repository.BillRepository;
import com.wishare.finance.domains.bill.repository.BillRepositoryFactory;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.ChargeOverdueMapper;
import com.wishare.finance.domains.bill.service.ChargeOverdueService;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.OverdueStateEnum;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.FinanceConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.TenantInfo;
import com.wishare.starter.consts.CacheConst;
import com.wishare.starter.consts.Const;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.bill.repository.BillRepository.FANG_YUAN;

/**
 * 调整审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
@Slf4j
public class AdjustApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        BillAdjustRepository billAdjustRepository = Global.ac.getBean(BillAdjustRepository.class);
        BillAdjustE billAdjustE = billAdjustRepository.getByApproveId(billApprove.getId());
        BillAdjustA<B> billAdjustA = new BillAdjustA<>(bill, billApprove, billAdjustE);
        billAdjustA.adjust(2);
        saveBillInfo(bill, billApprove.getLastApproveState(), billAdjustA, billApprove);
        //更新调整状态
        billAdjustRepository.updateById(billAdjustA);
        BillAdjustMqDetail detail = new BillAdjustMqDetail(billAdjustA);
        log.info("当前环境为：{}", EnvData.config);
        if (!FANG_YUAN.equals(EnvData.config)) {
            log.info("MQ发送账单调整：{}",bill.getId());
            EventLifecycle.push(EventMessage.builder()
                    .headers("action", BillAction.ADJUSTED)
                    .payload(BillActionEvent.adjust(bill.getId(), bill.getType(), detail, bill.getSupCpUnitId()))
                    .build());
        }
        String value = BillAdjustWayEnum.valueOfByCode(billAdjustA.getAdjustWay()).getValue();
        //存在当前信息增加描述
        if(billAdjustA.getContent().contains("账单收费对象由")&&billAdjustA.getAdjustWay()!=10){
            value = value+"、"+ BillAdjustWayEnum.CHARGE_OBJECT.getValue();
        }


        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption<>(new PlainTextDataItem("审核内容：", true)))
                        .option(new ContentOption<>(new PlainTextDataItem(billAdjustA.getContent(), true)))
                        .option(new ContentOption<>(new PlainTextDataItem("调整方式: ", false)))
                        .option(new ContentOption<>(new PlainTextDataItem(value, true), OptionStyle.normal())));
    }


    /**
     * 调整拒绝 修改 bill_adjust state调整状态[未生效]、 remark[原因]
     * @param bill         账单
     * @param billApprove  审核信息
     * @param reason       原因
     */
    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        BillAdjustRepository billAdjustRepository = Global.ac.getBean(BillAdjustRepository.class);
        BillAdjustE billAdjustE = billAdjustRepository.getByApproveId(billApprove.getId());
        /* 调整状态：0待审核，1审核中,2已生效，3未生效 */
        billAdjustE.setState(AdjustStateEnum.未生效.getCode());
        /* 备注 */
        billAdjustE.setRemark(reason);
        //更新调整状态
        billAdjustRepository.updateById(billAdjustE);
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
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
                        // 中交环境调低生成预收账单的费用 固定为 [预存账户]
                        if (TenantUtil.bf2()){
                            billApprove.setChargeItemId(168197507609306L);
                            billApprove.setChargeItemName("预收账户");
                        }
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

                        // 账单自身收款单需结转出去
                        carryiedOwnBill(bill, advanceBill);
                        carryoverAdvanceBill(bill, advanceBill);
                        //创建新的结算记录
                        GatherBill gatherBill = createAdvanceGatherBill(advanceBill);
                        GatherDetail gatherDetail = createAdvanceGatherDetail(advanceBill,gatherBill);
                        gatherDetail.setChargeStartTime(advanceBill.getStartTime());
                        gatherDetail.setChargeEndTime(advanceBill.getEndTime());
                        //设置调整记录中关联的新账单
                        billAdjustA.setSeparateBillNo(advanceBill.getBillNo());
                        bill.setCarriedAmount(bill.getCarriedAmount() + advanceBill.getSettleAmount());
                        Global.ac.getBean(GatherBillRepository.class).save(gatherBill);
                        Global.ac.getBean(GatherDetailRepository.class).save(gatherDetail);
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
                            if(StringUtils.isBlank(bill.getSupCpUnitId())) {
                                throw new IllegalArgumentException("上级收费单元ID不能为空!");
                            }
                            gatherDetailList = gatherDetailRepository.queryByGatherBillId(bill.getId(), bill.getSupCpUnitId());
                        }else if(BillTypeEnum.应收账单.equalsByCode(bill.getType()) || BillTypeEnum.临时收费账单.equalsByCode(bill.getType())){
                            gatherDetailList = gatherDetailRepository.listByRecBillId(bill.getId(), billApprove.getSupCpUnitId());
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

        // 如果违约金相关调整重新计算违约金金额
        if (
                BillApproveStateEnum.已审核.equalsByCode(approveState) &&
                BillAdjustWayEnum.IS_ADJUST_OVERDUE.test(BillAdjustWayEnum.valueOfByCode(billAdjustA.getAdjustWay()))
        ) {
            if (
                    BillAdjustWayEnum.ADJUST_RECEIVABLE_DATE.equalsByCode(billAdjustA.getAdjustWay()) &&
                    !Optional.ofNullable(billAdjustA.getIsReceivableDateBreakContractStartMethod()).orElse(false)
            ) {
                return;
            }
            doOverdueAdjust(bill);
        }

        if (
                BillApproveStateEnum.已审核.equalsByCode(approveState) &&
                        BillAdjustWayEnum.RECEIVABLE_AMOUNT.equals(
                                BillAdjustWayEnum.valueOfByCode(billAdjustA.getAdjustWay())
                        )
        ) {

            overdueAdjustAmount(bill);
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
                newBill.setPayerLabel(2);
                newBill.setPayerName(billAdjustA.getDeveloperName());
                newBill.setPayerId(billAdjustA.getDeveloperId());
                newBill.setCustomerId(billAdjustA.getDeveloperId());
                newBill.setCustomerName(billAdjustA.getDeveloperName());
                newBill.setCustomerType(1);
                newBill.init();
                newBill.setDescription("开发代付");
                newBill.setIsExact(2);
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
                newTemporaryBill.setCustomerId(billAdjustA.getDeveloperId());
                newTemporaryBill.setCustomerName(billAdjustA.getDeveloperName());
                newTemporaryBill.setCustomerType(1);
                newTemporaryBill.init();
                newTemporaryBill.setIsExact(2);
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

    private void overdueAdjustAmount(B bill) {
        if (!(bill instanceof ReceivableBill)) {
            return;
        }
        ReceivableBill recBill = (ReceivableBill) bill;
        if (OverdueStateEnum.无违约金.equalsByCode(recBill.getOverdue())) {
            return;
        }
        // 获取违约金管理数据
        ChargeOverdueService chargeOverdueService = Global.ac.getBean(ChargeOverdueService.class);
        ChargeOverdueE overdue = chargeOverdueService.getOverdueByBillId(bill.getId());
        if (Objects.isNull(overdue)) {
            return;
        }
        overdue.setOverdueAmount(bill.getTotalAmount());
        chargeOverdueService.updateById(overdue);
    }

    private void doOverdueAdjust(B bill) {
        if (!(bill instanceof ReceivableBill)) {
            return;
        }
        ReceivableBill recBill = (ReceivableBill) bill;
        Long overdueAmount = recBill.calculateOverdue();
        // 如果违约金金额为null 表示丢失违约金计算的相关数据
        if (Objects.isNull(overdueAmount)) {
            return;
        }
        // 获取违约金管理数据
        ChargeOverdueService chargeOverdueService = Global.ac.getBean(ChargeOverdueService.class);
        ReceivableBillAppService receivableBillAppService = Global.ac.getBean(ReceivableBillAppService.class);
        ChargeOverdueE overdue = chargeOverdueService.getOverdueByRefBillId(recBill.getId());
        if (Objects.isNull(overdue)) {
            return;
        }
        // 删除原违约金账单
        if (Objects.nonNull(overdue.getBillId())) {
            ReceivableBillInvalidF receivableBillInvalidF = new ReceivableBillInvalidF();
            receivableBillInvalidF.setBillId(overdue.getBillId());
            receivableBillInvalidF.setSupCpUnitId(bill.getSupCpUnitId());
            receivableBillInvalidF.setReason("原账单调整违约相关信息，原违约账单作废！");
            receivableBillAppService.invalid(receivableBillInvalidF);
        }
        // 如果违约金计算为0 删除违约金管理数据
        if (overdueAmount == 0) {
            chargeOverdueService.delete(overdue);
            return;
        }
        // 新生成
        if (Objects.nonNull(overdue.getBillId())) {
            ReceivableBill receivableBill = newOverdueBill(recBill, overdueAmount);
            ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
            receivableBillRepository.save(receivableBill);
            overdue.setBillId(receivableBill.getId());
            overdue.setBillNo(receivableBill.getBillNo());
        }
        // 修改违约金管理数据
        overdue.setBillSettleState(BillSettleStateEnum.未结算.getCode());
        overdue.setOverdueBeginDate(recBill.getOverdueBeginDate());
        overdue.setOverdueAmount(overdueAmount);
        overdue.setOverdueRate(new BigDecimal(Optional.ofNullable(recBill.getExtField5()).orElse("0")));
        chargeOverdueService.updateById(overdue);
    }

    private ReceivableBill newOverdueBill(ReceivableBill recBill, Long overdueAmount) {
        ReceivableBill newBill = Global.mapperFacade.map(recBill, ReceivableBill.class);
        newBill.copyReset();
        newBill.resetAmount(Math.abs(overdueAmount));
        newBill.init();
        newBill.setDescription("原账单调整生成");
        newBill.setIsExact(2);
        newBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(newBill.getRoomId()));
        newBill.setOverdue(Const.State._1);
        newBill.setBillMethod(BillMethodEnum.PRICE_OVERDUE.getType());
        newBill.setStartTime(recBill.getOverdueBeginDate().toLocalDate().atStartOfDay());
        newBill.setEndTime(LocalDate.now().atTime(23, 59, 59));
        return newBill;
    }

    private void carryoverAdvanceBill(B bill,  AdvanceBill advanceBill) {
        BillCarryoverE billCarryoverE = new BillCarryoverE();
        billCarryoverE.setCarriedBillId(bill.getId());
        billCarryoverE.setCarriedBillNo(bill.getBillNo());
        billCarryoverE.setCarryoverAmount(advanceBill.getTotalAmount());
        billCarryoverE.setCarryoverType(2);
        billCarryoverE.setApproveTime(LocalDateTime.now());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        CarryoverDetail carryoverDetail = new CarryoverDetail();
        carryoverDetail.setTargetBillId(advanceBill.getId());
        carryoverDetail.setTargetBillNo(advanceBill.getBillNo());
        carryoverDetail.setCarryoverAmount(advanceBill.getTotalAmount());
        carryoverDetail.setActualCarryoverAmount(advanceBill.getTotalAmount());
        carryoverDetail.setChargeStartTime(advanceBill.getStartTime());
        carryoverDetail.setChargeEndTime(advanceBill.getEndTime());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        billCarryoverE.setCarryoverDetail(Lists.newArrayList(carryoverDetail));
        billCarryoverE.setAdvanceCarried(1);
        billCarryoverE.setState(2);
        billCarryoverE.setRemark(StringUtils.isBlank(advanceBill.getRemark()) ? "" :
                advanceBill.getRemark());
        billCarryoverE.setBillType(bill.getType());
        BillCarryoverRepository carryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        carryoverRepository.save(billCarryoverE);
        saveCarryoverDetail(billCarryoverE);
    }

    /**
     * 添加结转详情数据
     *
     * @param billCarryoverA            结转单
     */
    private void saveCarryoverDetail(BillCarryoverE billCarryoverA) {
        List<BillCarryoverDetailE> resultList = Lists.newArrayList();
        for (CarryoverDetail carryoverDetail : billCarryoverA.getCarryoverDetail()) {
            BillCarryoverDetailE billCarryoverDetailE = new BillCarryoverDetailE();
            billCarryoverDetailE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CARRYOVER_DETAIL));
            billCarryoverDetailE.setBillType(billCarryoverA.getBillType());
            billCarryoverDetailE.setCarriedBillId(billCarryoverA.getCarriedBillId());
            billCarryoverDetailE.setCarriedBillNo(billCarryoverA.getCarriedBillNo());
            billCarryoverDetailE.setTargetBillId(carryoverDetail.getTargetBillId());
            billCarryoverDetailE.setTargetBillNo(carryoverDetail.getTargetBillNo());
            billCarryoverDetailE.setCarryoverType(billCarryoverA.getCarryoverType());
            billCarryoverDetailE.setCarryoverAmount(carryoverDetail.getActualCarryoverAmount());
            billCarryoverDetailE.setCarryoverTime(billCarryoverA.getCarryoverTime());
            billCarryoverDetailE.setBillCarryoverId(billCarryoverA.getId());
            billCarryoverDetailE.setRemark(billCarryoverA.getRemark());
            resultList.add(billCarryoverDetailE);
        }
        BillCarryoverDetailRepository carryoverDetailRepository
                = Global.ac.getBean(BillCarryoverDetailRepository.class);
        carryoverDetailRepository.saveBatch(resultList);
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
     * @param bill       预收账单
     * @param gatherBill  收款单
     * @return BillSettleE
     */
    private GatherDetail createAdvanceGatherDetail(AdvanceBill bill, GatherBill gatherBill) {
        GatherDetail billSettleE = new GatherDetail()
                .setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_DETAIL))
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
                .setPayChannel(gatherBill.getPayChannel())
                .setPayWay(gatherBill.getPayWay())
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
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            billSettleE.setCarriedBillPayChannel(SettleChannelEnum.预缴折扣.getCode());
        }
        billSettleE.init();
        return billSettleE;
    }

    /**
     * 结转账单金额 涉及自己账单收款单调整
     * @param bill 账单
     * @param advanceBill 结转金额
     */
    private void carryiedOwnBill(B bill, AdvanceBill advanceBill) {
        Long carryiedAmount = advanceBill.getSettleAmount();
        // 获取自身收款单可用金额
        List<GatherDetail> gatherDetailList = Optional
                .ofNullable(Global.ac.getBean(GatherDetailRepository.class).getListByRecBillId(bill.getId(),
                        bill.getSupCpUnitId()))
                .orElse(new ArrayList<>())
                .stream().filter(e -> e.getRemainingCarriedAmount() > 0)
                .collect(Collectors.toList());
        // 搜集每笔结转单可用金额
        for (GatherDetail gatherDetail : gatherDetailList) {
            Long amount = gatherDetail.getRemainingCarriedAmount();
            GatherBill one =  Global.ac.getBean(GatherBillRepository.class).getOne(new QueryWrapper<GatherBill>()
                    .eq("id", gatherDetail.getGatherBillId())
                    .eq("sup_cp_unit_id", bill.getSupCpUnitId()).eq("deleted", DataDeletedEnum.NORMAL.getCode()));
            if (carryiedAmount <= 0){
                break;
            }
            if (amount >= carryiedAmount){
                gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount()+carryiedAmount);
                gatherDetail.setCarriedBillId(advanceBill.getId());
                gatherDetail.setCarriedBillNo(advanceBill.getBillNo());
                gatherDetail.setCarriedBillType(BillTypeEnum.预收账单.getCode());
                // 更新gatherbill结转金额
                one.setCarriedAmount(one.getCarriedAmount() + carryiedAmount);
                Global.ac.getBean(GatherBillRepository.class).update(one,new UpdateWrapper<GatherBill>().eq("id",one.getId())
                        .eq("sup_cp_unit_id", bill.getSupCpUnitId()));
                break;
            }else {
                gatherDetail.setCarriedAmount(amount);
                gatherDetail.setCarriedBillId(advanceBill.getId());
                gatherDetail.setCarriedBillNo(advanceBill.getBillNo());
                gatherDetail.setCarriedBillType(BillTypeEnum.预收账单.getCode());
                // 更新gatherbill结转金额
                one.setCarriedAmount(one.getCarriedAmount() + amount);
                Global.ac.getBean(GatherBillRepository.class).update(one,new UpdateWrapper<GatherBill>().eq("id",one.getId())
                        .eq("sup_cp_unit_id", bill.getSupCpUnitId()));
                carryiedAmount = carryiedAmount - amount;
            }
        }
        // 更新收款详情单数据
        for (GatherDetail gatherDetail : gatherDetailList) {
            Global.ac.getBean(GatherDetailRepository.class).update(gatherDetail,new QueryWrapper<GatherDetail>()
                    .eq("id",gatherDetail.getId())
                    .eq("sup_cp_unit_id",gatherDetail.getSupCpUnitId()));
        }
    }

    /**
     * 创建预收收款单
     * @param advanceBill
     * @return
     */
    private GatherBill createAdvanceGatherBill(AdvanceBill advanceBill) {
        GatherBill billSettleE = new GatherBill()
                .setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_BILL))
                .setBillNo(BillSerialNumberFactory.getInstance().serialNumber())
                .setStatutoryBodyId(advanceBill.getStatutoryBodyId())
                .setStatutoryBodyName(advanceBill.getStatutoryBodyName())
                .setSbAccountId(advanceBill.getSbAccountId())
                .setSupCpUnitId(advanceBill.getSupCpUnitId())
                .setSupCpUnitName(advanceBill.getSupCpUnitName())
                .setPayTime(Objects.nonNull(advanceBill.getPayTime()) ? advanceBill.getPayTime() : LocalDateTime.now())
                .setPayChannel(SettleChannelEnum.结转.getCode())
                .setPayWay(SettleWayEnum.结转.getCode())
                .setTotalAmount(advanceBill.getSettleAmount())
                .setPayeeId(ThreadLocalUtil.curIdentityInfo().getUserId())
                .setPayeeName(ThreadLocalUtil.curIdentityInfo().getUserName())
                .setPayerId(advanceBill.getPayerId())
                .setPayerName(advanceBill.getPayerName())
                .setSysSource(advanceBill.getSysSource())
                .setApprovedState(BillApproveStateEnum.已审核.getCode())
                .setPaySource(advanceBill.getPaySource());
        billSettleE.init();
        return billSettleE;
    }

}
