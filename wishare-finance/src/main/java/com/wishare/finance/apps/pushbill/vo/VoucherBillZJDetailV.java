package com.wishare.finance.apps.pushbill.vo;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("中交汇总单据明细信息")
public class VoucherBillZJDetailV {
    
    @ApiModelProperty(value = "单据明细id")
    private Long id;
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
    private String accountDate;
    @ApiModelProperty(value = "客户")
    private String custom;
    @ApiModelProperty(value = "客户id")
    private String customId;
    @ApiModelProperty(value = "收款方ID")
    private String payeeId;
    @ApiModelProperty(value = "收款方名称")
    private String payeeName;
    @ApiModelProperty(value = "付款方ID")
    private String payerId;
    @ApiModelProperty(value = "付款方名称")
    private String payerName;
    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;
    @ApiModelProperty(value = "税率id")
    private Long taxRateId;
    @ApiModelProperty(value = "税率")
    private String taxRate;
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxIncludAmount;
    @ApiModelProperty(value = "不含税金额")
    private BigDecimal taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "账单编号")
    private String billNo;
    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "合同ID")
    private String contractId;
    @ApiModelProperty(value = "收款单编号")
    private String gatherBillNo;
    @ApiModelProperty(value = "收款单id")
    private String gatherBillId;
}
