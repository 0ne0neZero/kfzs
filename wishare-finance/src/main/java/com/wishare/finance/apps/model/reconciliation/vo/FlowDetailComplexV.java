package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FlowDetailComplexV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("流水号")
    private String serialNumber;

    @ApiModelProperty("缴费金额")
    private Long settleAmount;

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("对方账户")
    private String oppositeAccount;

    @ApiModelProperty("对方名称")
    private String oppositeName;

    @ApiModelProperty("对方开户行")
    private String oppositeBank;

    @ApiModelProperty("本方账户")
    private String ourAccount;

    @ApiModelProperty("本方名称")
    private String ourName;

    @ApiModelProperty("本方开户行")
    private String ourBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("资金用途")
    private String fundPurpose;

    @ApiModelProperty("交易平台")
    private String tradingPlatform;

    @ApiModelProperty("交易方式")
    private String transactionMode;

    @ApiModelProperty("认领状态：0未认领，1已认领 2 挂起 3 审核中")
    private Integer claimStatus;

    @ApiModelProperty("流水类型：1收入 2退款")
    private Integer type;

    @ApiModelProperty("是否为同步数据（0否，1是）")
    private Integer syncData;

    @ApiModelProperty("是否为同步数据（0否，1是）")
    private String receiptUrl;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("修改人姓名")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("是否已使用（用于判断流水金额和开票金额明细）")
    private Boolean dealFlag = false;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("数据来源id")
    private String idExt;

    @ApiModelProperty("流水认领记录id")
    private Long flowClaimRecordId;

    @ApiModelProperty("认领批次号")
    private String flowClaimRecordSerialNumber;

    @ApiModelProperty("报账单id")
    private Long voucherBillId;

    @ApiModelProperty("报账单号")
    private String voucherBillNo;

    @ApiModelProperty("付款方式(计算方式)")
    private Integer payChannelType;

    @ApiModelProperty("付款方式(计算方式)")
    private Long statutoryBodyId;

    private Long costOrgId;

}
