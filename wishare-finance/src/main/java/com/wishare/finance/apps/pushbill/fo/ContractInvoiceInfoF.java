package com.wishare.finance.apps.pushbill.fo;


import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ApiModel("合同收票生产对下结算单 接口入参")
public class ContractInvoiceInfoF {

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("税额")
    private Long taxAmount;

    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("变动类型")
    private String changeType;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String supplier;

    @ApiModelProperty("款项名称")
    private String paymentName;

    @ApiModelProperty("款项id")
    private String paymentId;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;
    /**
     * 附件资料信息  数据库直接存json
     */
    @ApiModelProperty("附件信息")
    private String files;

    @ApiModelProperty("核销信息")
    private JSONObject writeOffInfo;

    @ApiModelProperty("计量明细信息")
    private List<MeasurementDetailF> detailFList;

    @ApiModelProperty("发票信息")
    private List<InvoiceZJF> invoiceZJFList;
    /**
     * 审批中(0, "审批中"),
     * 无需审批(1, "无需审批"),
     */
    @ApiModelProperty("1,无需审批, 0 需审批")
    private Integer approveState;

    // liucchengid
    /**
     * 审批流程,对应bpm的formId
     */
    @ApiModelProperty("审批流程,对应bpm的formId")
    private String approveRule;

    @ApiModelProperty("结算单编号")
    private String settlementNo;
}
