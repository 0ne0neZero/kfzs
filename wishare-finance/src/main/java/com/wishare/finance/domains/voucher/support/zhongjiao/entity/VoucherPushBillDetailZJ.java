package com.wishare.finance.domains.voucher.support.zhongjiao.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL_DETAIL_ZJ, autoResultMap = true)
public class VoucherPushBillDetailZJ extends BaseEntity {
    @ApiModelProperty(value = "流水id")
    private Long gatherBillDetailId;
    @ApiModelProperty(value = "收款单编号")
    private String gatherBillNo;
    @ApiModelProperty(value = "收款单编号")
    private String gatherBillId;
    @ApiModelProperty(value = "单据明细id")
    private Long id;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "账单编号")
    private String billNo;
    @ApiModelProperty(value = "单据编号")
    private String voucherBillDetailNo;
    @ApiModelProperty(value = "汇总单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "账单类型 1:应收账单，2:预收账单，3:临时收费账单")
    private Integer billType;
    @ApiModelProperty(value = "项目id")
    private String communityId;
    @ApiModelProperty(value = "所属项目")
    private String communityName;
    @ApiModelProperty(value = "资产名称")
    private String roomName;
    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;
    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;
    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;
    @ApiModelProperty(value = "客户")
    private String customName;
    @ApiModelProperty(value = "客户id")
    private String customId;
    @ApiModelProperty(value = "收款方ID")
    private String payeeId;
    @ApiModelProperty(value = "收款方名称")
    private String payeeName;
    @ApiModelProperty(value = "付款方ID")
    private String payerId;
    @ApiModelProperty(value = "付款方类型")
    private Integer payerType;
    @ApiModelProperty(value = "付款方名称")
    private String payerName;
    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;
    @ApiModelProperty(value = "税率id")
    private Long taxRateId;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "含税金额")
    private Long taxIncludAmount;
    @ApiModelProperty(value = "不含税金额")
    private Long taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private Long taxAmount;
    @ApiModelProperty(value = "业务单元id")
    private Long businessUnitId;
    @ApiModelProperty(value = "房号ID")
    private String roomId;
    @ApiModelProperty(value = "结算方式  ALIPAY：支付宝，WECHATPAY:微信支付， CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE:支票，OTHER:其他，COMPLEX：组合支付'")
    private String payChannel;

    @ApiModelProperty(value = "触发事件类型：1 收入确认 ")
    private Integer billEventType;
    @ApiModelProperty(value = "推送状态  1待推送 2推送成功 3 推送失败")
    private Integer pushBillState;
    @ApiModelProperty(value = "是否删除：0否，1是")
    private Integer deleted;
    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "合同ID")
    private String contractId;
    @ApiModelProperty(value = "退款金额")
    private Long refundAmount;
    @ApiModelProperty(value = "业务科目id")
    private Long subjectId;
    @ApiModelProperty(value = "业务科目名称")
    private String subjectName;
    @ApiModelProperty(value = "现金流量项目")
    private String cashFlowItem;
    @ApiModelProperty(value = "业务科目来源id")
    private String subjectExtId;
    @ApiModelProperty(value = "现金流项目来源id")
    private String cashFlowItemExtId;

    private Integer reverseFlag;

    private Long sceneId;

    @ApiModelProperty("变动类型编码")
    private String changeCode;

    @ApiModelProperty("变动类型名称")
    private String changeName;

    @ApiModelProperty("内部合同ID")
    private String innerContractId;

    @ApiModelProperty("内部合同编号")
    private String innerContractNo;

    public String groupForPushKxmx(){
        return (Objects.isNull(payerType) || payerType != 99 ? "0-" : "1-") + getSubjectExtId();
    }

    public String groupForPushSrmx(){
        return Objects.isNull(payerType) || payerType != 99 ? "0-" : "1-";
    }

}
