package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.consts.enums.BillAdjustTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;

import java.util.Objects;

/**
 * 账单接口
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
public interface IBill {

    /**
     * 获取id
     * @return
     */
    Long getIdentifier();

    /**
     * 设置id
     */
    void setIdentifier(Long id);

    /**
     * 初始化id
     */
    default void generateIdentifier(){
        if (Objects.isNull(getIdentifier())){
            setIdentifier(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        }
    }

    /**
     * 审核
     * @param approveState
     * @return
     */
    boolean approve(BillApproveStateEnum approveState);

    /**
     * 申请审核
     * @return
     */
    boolean apply();

    /**
     * 申请结转
     * @return
     */
    boolean applyCarryover();

    /**
     * 申请调整
     * @return
     */
    boolean applyAdjust();

    /**
     * 申请减免
     * @return
     */
    boolean applyDeduction();

    /**
     * 申请退款
     * @return
     */
    boolean applyRefund();

    /**
     * 退款
     * @param amount    退款额度
     * @return
     */
    boolean refund(Long amount);

    /**
     * 调整
     * @param adjustAmount  调整的金额
     * @param adjustType    调整类型
     * @return
     */
    boolean adjust(Long adjustAmount, BillAdjustTypeEnum adjustType, Integer type);

    /**
     * 减免
     *
     * @param adjustAmount 减免金额
     * @param way 调整方式 1.应收减免；2实收减免
     * @return
     */
    boolean derate(Long adjustAmount, int way);

    /**
     * 调整
     *
     * @param adjustAmount 调整的金额
     * @param adjustType 调整类型
     * @param way
     * @param deductionMethod 减免类型
     * @return
     */
    boolean adjust2(Long adjustAmount, BillAdjustTypeEnum adjustType, Integer deductionMethod);


    /**
     * 结转
     * @return
     */
    boolean carryover();

    /**
     * 结算
     * @param settleAmount
     * @return
     */
    boolean settle(long settleAmount,long discountAmount);

    long canOverFlowSettle(long settleAmount,long discountAmount);

    /**
     * 删除
     * @return
     */
    boolean delete();

    /**
     * 反审核
     * @return
     */
    boolean deapprove();

    /**
     * 冻结
     * @return
     */
    boolean freeze();

    /**
     * 冻结
     * @return
     */
    boolean freezeBatchAddReason(Integer type);
    /**
     * 解冻
     * @return
     */
    boolean unfreeze();

    /**
     * 交账
     * @return
     */
    boolean handAccount();

    /**
     * 反交账
     * @return
     */
    boolean handReversal();

    /**
     * 挂账
     * @return
     */
    boolean onAccount();

    /**
     * 自动挂账
     *
     * @return
     */
    boolean onAutoAccount();

    /**
     * 自动未挂账
     * @return
     */
    boolean offAccount();

    /**
     * 销账
     */
    boolean writeOff();

    /**
     * 发起开票
     * @return
     */
    boolean invoice();


    /**
     * 根据入参来修改开票状态
     * @return
     */
    boolean invoice(Integer status);

    /**
     * 完成开票
     * @return
     */
    boolean finishInvoice(Long invoiceAmount, boolean success);

    /**
     * 根据金额批量作废，红冲账单发票
     *
     * @param invoiceAmount
     * @return
     */
    boolean voidBatch(Long invoiceAmount);

    /**
     * 冲销
     * @return
     */
    boolean reverse();

    /**
     * 作废
     * @return
     */
    boolean invalid();

    /**
     * 对账
     * @param result 对账结果
     * @return
     */
    boolean reconcile(boolean result);

    /**
     * 推凭
     * @return
     */
    boolean infer();


}
