package com.wishare.finance.domains.bill.command;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/11/4
 * @Description: 批量结算command
 */
@Getter
@Setter
public class BatchSettleCommand {

    /**
     * 账单ids
     */
    private List<Long> billIds;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）
     */
    private Integer billType;

    /**
     * 付款方id
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 开户行账号
     */
    private String bankAccount;

    /**
     * 结算金额（单位：分）
     */
    private Long settleAmount;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;

    /**
     * 备注
     */
    private String remark;

    private String supCpUnitId;

    /**
     * 构建结算记录（全部结算）
     *
     * @return
     */
    public List<AddBillSettleF> generalBillSettleRf(List<BillDetailMoreV> detailMoreVList, IdentityInfo identityInfo) {
        //校检结算金额
        long actualUnpayAmountSum = detailMoreVList.stream().mapToLong(BillDetailMoreV::getActualUnpayAmount).sum();
        if (this.getSettleAmount().longValue() != actualUnpayAmountSum) {
            throw BizException.throw400("可结算金额为：" + actualUnpayAmountSum/100 + " 实际结算金额为:" + this.getSettleAmount()/100);
        }
        //构造结算记录
        List<AddBillSettleF> addBillSettleRFList = Lists.newArrayList();
        for (BillDetailMoreV detailMoreV : detailMoreVList) {
            AddBillSettleF settleRF = new AddBillSettleF();
            settleRF.setSupCpUnitId(detailMoreV.getSupCpUnitId());
            settleRF.setBillId(detailMoreV.getBillId());
            settleRF.setPayeeId(identityInfo.getUserId());
            settleRF.setPayeeName(identityInfo.getUserName());
            settleRF.setPayerType(detailMoreV.getPayerType());
            settleRF.setPayerId(this.getPayerId());
            settleRF.setPayerName(this.getPayerName());
            settleRF.setRemark(this.getRemark());
            settleRF.setSettleChannel(SettleWayChannelEnum.valueOfByCode(this.getSettleChannel()).getCode());
            settleRF.setSettleWay(SettleWayChannelEnum.valueOfByCode(this.getSettleChannel()).getType());
            settleRF.setBankName(this.getBankName());
            settleRF.setBankAccount(this.getBankAccount());
            settleRF.setSettleAmount(detailMoreV.getActualUnpayAmount());
            settleRF.setPayAmount(this.getSettleAmount());
            settleRF.setSettleTime(LocalDateTime.now());
            addBillSettleRFList.add(settleRF);
        }
        return addBillSettleRFList;
    }
}
