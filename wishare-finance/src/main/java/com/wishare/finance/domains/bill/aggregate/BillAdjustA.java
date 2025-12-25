package com.wishare.finance.domains.bill.aggregate;

import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * 调整聚合
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class BillAdjustA<B extends Bill> extends BillAdjustE {

    /**
     * 账单信息
     */
    private B bill;

    /**
     * 调高创建的新账单
     */
    public B newBill;

    /**
     * 调低创建新的预收账单
     */
    public AdvanceBill advanceBill;

    /**
     * 账单审核记录
     */
    private BillApproveE billApprove;

    /**
     * 退款记录
     */
    public BillRefundE billRefundE;

    /**
     * 货款聚合
     */
    private BillRefundA<B> refundA;

    public BillAdjustA() {
    }

    public BillAdjustA(B bill) {
        this.bill = bill;
    }

    public BillAdjustA(B bill, BillApproveE billApprove) {
        this.bill = bill;
        this.billApprove = billApprove;
    }

    public BillAdjustA(B bill, BillApproveE billApprove, BillAdjustE billAdjustE) {
        this.bill = bill;
        this.billApprove = billApprove;
        if (Objects.nonNull(billAdjustE)) {
            Global.mapperFacade.map(billAdjustE, this);
        }
    }

    /**
     * 调整
     *
     * @param type 1 预调整 2 审核成功调整
     * @return
     */
    public boolean adjust(int type) {
        //1.调整校验
        checkAdjust();
        switch (BillAdjustWayEnum.valueOfByCode(getAdjustWay())) {
            case RECEIVABLE_PRICE:
                //调整单价审核对单价进行重新设值
                resetPrice();
                doAdjust(BillAdjustTypeEnum.valueOfByCode(getAdjustType()), type);
                break;
            case ADJUST_CHARGE_ITEM:
                resetChargeItem();
                break;
            case ADJUST_NUM:
                resetNum();
                doAdjust(BillAdjustTypeEnum.valueOfByCode(getAdjustType()), type);
                break;
            case RECEIVABLE_AREA:
                resetArea();
            case RECEIVABLE_AMOUNT:
            case PAYMENT_ON_BEHALF:
                doAdjust(BillAdjustTypeEnum.valueOfByCode(getAdjustType()), type);
                break;
            case ADJUST_TAX_RATE:
                resetTaxRate();
                break;
            case ADJUST_OVERDUE_RATE:
                resetOverdueRate();
                break;
            case ADJUST_OVERDUE_BEGIN_DATE:
                restOverdueBeginDate();
                break;
            case ADJUST_RECEIVABLE_DATE:
                restReceivableDate();
                break;
            case ACTUAL_REDUCE:
            case ACTUAL_COUPON:
            case ACTUAL_VACANT_HOUSE_DISCOUNT:
            case LIQUIDATED_DAMAGES:
            case ADJUST_ODD:
            case ACTUAL_OTHER:
                doDerate(getDeductionMethod(), type);
                break;
            default:
                break;
        }
        if (Objects.nonNull(billApprove)) {
            setState(AdjustStateEnum.valueOfApproveState(BillApproveStateEnum.valueOfByCode(billApprove.getApprovedState())).getCode());
            setAdjustTime(billApprove.getGmtModify());
        } else {
            setState(AdjustStateEnum.已生效.getCode());
            setAdjustTime(LocalDateTime.now());
        }
        //设置收费对象
        if (Objects.nonNull(getPayerId())) {
            bill.changePayer(getPayerId(), getPayerName(), getPayerType(),getPayerPhone(), getPayerLabel());
        }
        return true;
    }

    public void verifyOperate(){
        ErrorAssertUtil.isFalseThrow402(BillRefundStateEnum.退款中.equalsByCode(bill.getRefundState()),
                ErrorMessage.BILL_IS_OPERATING, BillRefundStateEnum.退款中.getCode());
        ErrorAssertUtil.isFalseThrow402(BillCarryoverStateEnum.待结转.equalsByCode(bill.getCarriedState()),
                ErrorMessage.BILL_IS_OPERATING, BillCarryoverStateEnum.待结转.getValue());
        ErrorAssertUtil.isFalseThrow402(BillReverseStateEnum.已冲销.equalsByCode(bill.getReversed() == null ?
                BillReverseStateEnum.未冲销.getCode() :
                bill.getReversed()), ErrorMessage.BILL_IS_OPERATING, BillReverseStateEnum.已冲销.getValue());
        ErrorAssertUtil.isTrueThrow402(BillStateEnum.正常.equalsByCode(bill.getState()),
                ErrorMessage.BILL_IS_OPERATING, BillStateEnum.valueOfByCode(bill.getState()));
    }

    /**
     * 校验调整
     */
    public void checkAdjust() {
        verifyOperate();

        if (
                !BillAdjustWayEnum.IS_NO_AMOUNT_ADJUST.test(BillAdjustWayEnum.valueOfByCode(getAdjustWay()))
        ) {
            ErrorAssertUtil.notNullThrow300(getAdjustAmount(), ErrorMessage.BILL_ADJUST_AMOUNT_NOT_NULL);
        }
        ErrorAssertUtil.isFalseThrow402(BillVerifyStateEnum.已核销.equalsByCode(bill.getVerifyState()), ErrorMessage.BILL_ADJUST_STATE_ERROR, BillVerifyStateEnum.已核销.getValue());
        //BillSettleStateEnum billSettleStateEnum = BillSettleStateEnum.valueOfByCode(bill.getSettleState().intValue());
        //BillAdjustWayEnum adjustWay = BillAdjustWayEnum.valueOfByCode(getAdjustWay());
        //应收只能调整未结算账单，实收只能调整已结算账单（去除限制，都可以操作）
        /*ErrorAssertUtil.isFalseThrow402((
                BillSettleStateEnum.已结算 == billSettleStateEnum
                        && (adjustWay == BillAdjustWayEnum.RECEIVABLE_PRICE
                        || adjustWay == BillAdjustWayEnum.RECEIVABLE_AMOUNT
                        || adjustWay == BillAdjustWayEnum.RECEIVABLE_AREA)
        ) || (
                BillSettleStateEnum.未结算 == billSettleStateEnum
                        && (adjustWay == BillAdjustWayEnum.ACTUAL_PRICE
                        || adjustWay == BillAdjustWayEnum.ACTUAL_COUPON
                        || adjustWay == BillAdjustWayEnum.ACTUAL_REDUCE
                        || adjustWay == BillAdjustWayEnum.ACTUAL_VACANT_HOUSE_DISCOUNT
                        || adjustWay == BillAdjustWayEnum.ACTUAL_OTHER)
        ), ErrorMessage.BILL_ADJUST_WAY_ERROR, billSettleStateEnum.getValue(), adjustWay.getValue());*/
    }

    /**
     * 减免判断是否要走转预收逻辑
     *
     * @return true 走转预收逻辑， false 不走
     */
    private boolean shouldDoAdvance() {
        // 判断可支付金额是否小于减免金额，如果小于则会走超出金额处理逻辑（退款或转预收）
        boolean adjustFlag = BillAdjustTypeEnum.减免.equalsByCode(this.getAdjustType()) ||
                BillAdjustTypeEnum.调低.equalsByCode(this.getAdjustType());
        boolean outAmount = bill.getRemainingSettleAmount() + getAdjustAmount() < 0;
        boolean partSettleFlag = BillSettleStateEnum.部分结算.equalsByCode(bill.getSettleState()) && outAmount;
        return BillApproveStateEnum.已审核.equalsByCode(billApprove.getLastApproveState())
                && (BillSettleStateEnum.已结算.equalsByCode(bill.getSettleState()) || partSettleFlag) && adjustFlag;
    }

    /**
     * 执行减免
     *
     * @param way 1.应收减免；2实收减免
     */
    private void doDerate(int way, int type) {
        if (shouldDoAdvance() && 2 == type) {
            // 支付溢出金额处理
            doAdvance();
        }
        // 减免账单金额
        bill.derate(Math.abs(getAdjustAmount()), way);
    }

    /**
     * 支付溢出金额处理
     */
    private void doAdvance() {
        // 减免金额大于可支付金额部分退还逻辑（默认走转预收逻辑）
        if (BillApprovedActionEnum.退款.equalsByCode(billApprove.getApprovedAction())) {
            BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(bill.getType());
            adjustRefundAction(billTypeEnum);
        } else if (BillApprovedActionEnum.转预收.equalsByCode(billApprove.getApprovedAction())) {
            ErrorAssertUtil.notNullThrow300(billApprove.getChargeItemId(), ErrorMessage.BILL_ADJUST_CHARGE_ERROR);
            ErrorAssertUtil.notNullThrow300(billApprove.getChargeItemName(), ErrorMessage.BILL_ADJUST_CHARGE_ERROR);
            //创建同房号、同项目的新账单
            this.advanceBill = createAdvanceBill(AdvanceBill.class);
            handleActualLostAmount(advanceBill);
        }
        bill.setAdjusted(BillAdjustStateEnum.已调整.getCode());
    }

    private void handleActualLostAmount(AdvanceBill advanceBill) {
        Long actualLostAmount = getActualLostAmount();
        if (Objects.nonNull(actualLostAmount) && Objects.nonNull(advanceBill)) {
            setActualLostAmount(actualLostAmount - advanceBill.getTotalAmount());
        }
    }

    /**
     * 触发监听
     *
     * @param adjustType 调整类型
     */
    private void doAdjust(BillAdjustTypeEnum adjustType, Integer type) {
        if (shouldDoAdvance() && type == 2) {
            // 支付溢出金额处理
            doAdvance();
        }
        bill.adjust(Math.abs(getAdjustAmount()), adjustType, type);
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            receivableBill.setIsExact(getIsExact());
            if (receivableBill.getOverdue() == 1) {
                receivableBill.setExtField1("1");
            }
        }else if (bill instanceof TemporaryChargeBill) {
            ((TemporaryChargeBill) bill).setIsExact(getIsExact());
        }
    }

    /**
     * 重置数量
     */
    private void resetNum() {
        Long num = getExtField1();
        ErrorAssertUtil.notNullThrow300(num, ErrorMessage.BILL_ADJUST_NUM_NOT_NULL);
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            receivableBill.setChargingCount(Integer.valueOf(num.toString()));
        }else if (bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill receivableBill = (TemporaryChargeBill) bill;
            receivableBill.setChargingCount(Integer.valueOf(num.toString()));
        }
    }

    /**
     * 重置面积
     */
    private void resetArea() {
        BigDecimal chargingArea = getChargingArea();
        ErrorAssertUtil.notNullThrow300(chargingArea, ErrorMessage.BILL_ADJUST_AREA_NOT_NULL);
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            receivableBill.setChargingArea(chargingArea);
        } else if (bill instanceof AdvanceBill) {
            AdvanceBill advanceBill = (AdvanceBill) bill;
            advanceBill.setChargingArea(chargingArea);
        } else if (bill instanceof PayableBill) {
            PayableBill payableBill = (PayableBill) bill;
            payableBill.setChargingArea(chargingArea);
        }else if(bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill receivableBill = (TemporaryChargeBill) bill;
            receivableBill.setChargingArea(chargingArea);
        }
    }

    private void resetTaxRate() {
        BigDecimal adjustTaxRate = getAdjustTaxRate();
        ErrorAssertUtil.notNullThrow300(adjustTaxRate, ErrorMessage.BILL_ADJUST_TAX_RATE_NOT_NULL);
        bill.setTaxRate(adjustTaxRate.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
        bill.setTaxRateId(getAdjustTaxRateId());
    }

    private void resetOverdueRate() {
        BigDecimal adjustOverdueRate = getOverdueRate();
        ErrorAssertUtil.notNullThrow300(adjustOverdueRate, ErrorMessage.BILL_ADJUST_OVERDUE_RATE_NOT_NULL);
        bill.setExtField5(adjustOverdueRate.toString());
    }

    private void restReceivableDate() {
        if (!(bill instanceof ReceivableBill)) {
            return;
        }
        ReceivableBill receivableBill = (ReceivableBill) bill;
        LocalDate receivableDate = getReceivableDate();
        ErrorAssertUtil.notNullThrow300(receivableDate, ErrorMessage.BILL_RECEIVABLE_DATE);
        receivableBill.setReceivableDate(receivableDate);
        receivableBill.setReceivableDay(receivableDate.getDayOfMonth());
        if (TenantUtil.bf8()) {
            receivableBill.setExtField8(receivableDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (!Optional.ofNullable(getIsReceivableDateBreakContractStartMethod()).orElse(false)) {
            return;
        }
        Integer lateDays = getLateDays();
        ErrorAssertUtil.notNullThrow300(lateDays, ErrorMessage.BILL_BREAK_CONTRACT_START_METHOD);
        LocalDateTime overdueBeginDate = receivableDate.plusDays(lateDays + 1).atStartOfDay();
        receivableBill.setExtField2(overdueBeginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void restOverdueBeginDate() {
        if (!(bill instanceof ReceivableBill)) {
            return;
        }
        ReceivableBill receivableBill = (ReceivableBill) bill;
        Integer breakContractStartMethod = getBreakContractStartMethod();
        Integer lateDays = getLateDays();
        ErrorAssertUtil.notNullThrow300(breakContractStartMethod, ErrorMessage.BILL_BREAK_CONTRACT_START_METHOD);
        ErrorAssertUtil.notNullThrow300(lateDays, ErrorMessage.BILL_BREAK_CONTRACT_START_METHOD);
        LocalDateTime overdueBeginDate ;
        if (breakContractStartMethod.equals(1)) {
            LocalDateTime endTime = receivableBill.getEndTime();
            ErrorAssertUtil.notNullThrow300(endTime, ErrorMessage.BILL_END_TIME);
            overdueBeginDate = endTime.plusSeconds(1L).plusDays(lateDays);
        } else {
            LocalDate receivableDate = receivableBill.getReceivableDate();
            ErrorAssertUtil.notNullThrow300(receivableDate, ErrorMessage.BILL_RECEIVABLE_DATE);
            overdueBeginDate = receivableDate.plusDays(lateDays + 1).atStartOfDay();
        }
        receivableBill.setExtField2(overdueBeginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void resetChargeItem() {
        Long changeChargeItemId = getChangeChargeItemId();
        bill.setChargeItemId(changeChargeItemId);
        ChargeItemRepository chargeItemRepository = Global.ac.getBean(ChargeItemRepository.class);
        ChargeItemE chargeItemInfo = chargeItemRepository.getById(changeChargeItemId);
        ErrorAssertUtil.isTrueThrow300(Objects.nonNull(chargeItemInfo),
                ErrorMessage.CHARGE_NOT_EXIST);
        bill.setChargeItemName(chargeItemInfo.getName());
    }

    /**
     * 重置单价
     */
    private void resetPrice() {
        BigDecimal unitPrice = getUnitPrice().abs();
        ErrorAssertUtil.notNullThrow300(unitPrice, ErrorMessage.BILL_ADJUST_UNIT_PRICE_NOT_NULL);
        //调整后的单价金额不能小于0    a.compareTo(BigDecimal.ZERO) > 0
        ErrorAssertUtil.isFalseThrow300(unitPrice.compareTo(BigDecimal.ZERO) < 0,
                ErrorMessage.BILL_ADJUST_UNIT_PRICE_ERROR);

        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            receivableBill.setUnitPrice(unitPrice);
        }else if(bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill receivableBill = (TemporaryChargeBill) bill;
            receivableBill.setUnitPrice(unitPrice);
        }
    }

    /**
     * 创建退款记录
     *
     * @return BillRefundE
     */
    private BillRefundE createBillRefund() {
        BillRefundE billRefundE = new BillRefundE();
        billRefundE.setId(IdentifierFactory.getInstance().generateLongIdentifier("bill_refund_id"));
        billRefundE.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefundE.setBillId(bill.getId());
        billRefundE.setRefundAmount(Math.abs(getAdjustAmount()));
        billRefundE.setRefundChannel(SettleChannelEnum.其他.getCode());
        billRefundE.setRefundWay(SettleWayEnum.线上.getCode());
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setState(BillRefundStateEnum.已退款.getCode());
        return billRefundE;
    }

    /**
     * 调整审核通过退款动作
     *
     * @param billTypeEnum 账单类型
     */
    private void adjustRefundAction(BillTypeEnum billTypeEnum) {
        BillRefundE billRefund = createBillRefund();
        billRefund.setBillType(bill.getType());
        billRefund.setBillApproveId(billRefund.getId());
        switch (billTypeEnum) {
            case 应收账单:
                ReceivableBill receivableBill = Global.mapperFacade.map(bill, ReceivableBill.class);
                billRefund.setRefunderType(receivableBill.getPayerType());
                billRefund.setChargeStartTime(receivableBill.getStartTime());
                billRefund.setChargeEndTime(receivableBill.getEndTime());
                billRefund.setRefunderName(receivableBill.getPayerName());
                billRefund.setRemark("应收账单调整退款");
                break;
            case 预收账单:
                AdvanceBill advanceBill = Global.mapperFacade.map(bill, AdvanceBill.class);
                billRefund.setRefunderType(advanceBill.getPayerType());
                billRefund.setChargeStartTime(advanceBill.getStartTime());
                billRefund.setChargeEndTime(advanceBill.getEndTime());
                billRefund.setRefunderName(advanceBill.getPayerName());
                billRefund.setRemark("预收账单调整退款");
                break;
            case 临时收费账单:
                TemporaryChargeBill temporaryChargeBill = Global.mapperFacade.map(bill, TemporaryChargeBill.class);
                billRefund.setRefunderName(temporaryChargeBill.getPayerName());
                billRefund.setRemark("临时账单调整退款");
                break;
            default:
        }
        //发起退款
        BillRefundA<B> billRefundA = new BillRefundA<>(bill);
        Global.mapperFacade.map(billRefund, billRefundA);
        billRefundA.refund();
        this.billRefundE = billRefundA;
    }

    /**
     * 复制当前账单信息作为新账单
     */
    private <T extends Bill> T createAdvanceBill(Class<T> tClass) {
        T b = Global.mapperFacade.map(bill, tClass);
        b.setId(null);
        b.setBillNo(null);
        b.init();
        b.resetState();
        // 转预收金额
        b.resetAmount(Math.abs(bill.getRemainingSettleAmount() + getAdjustAmount()));
        b.resetOperatorInfo();
        if(EnvConst.ZHONGJIAO.equals(EnvData.config)){
            b.setTaxRate(new BigDecimal("0.00"));
            b.setTaxRateId(136157205772010L);
        }
        return b;
    }

    public B getBill() {
        return bill;
    }
}
