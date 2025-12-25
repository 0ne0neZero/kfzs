package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量开票参数
 *
 * @author ljx
 */
@ApiModel("批量开票参数")
@Data
public class ContractInvoiceF {

    @ApiModelProperty("合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty("合同名称")
    @NotNull
    private String contractName;

    @ApiModelProperty("开票人id")
    @NotNull
    private String userId;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("开票人姓名")
    @NotNull
    private String userName;

    @ApiModelProperty("货品名称")
    private String productName;

    @ApiModelProperty("开票说明")
    private String remark;

    @ApiModelProperty("开票时间")
    @NotNull
    private LocalDateTime invoiceApplyTime;

    @ApiModelProperty(value = "甲方税号")
    private String buyerTaxNum;

    @ApiModelProperty(value = "甲方电话")
    private String buyerTel;

    @ApiModelProperty(value = "甲方地址")
    private String buyerAddress;

    @ApiModelProperty("甲方银行开户行")
    private String buyerBank;

    @ApiModelProperty("甲方银行账号")
    private String buyerAccount;

    @ApiModelProperty(value = "乙方税号")
    private String sellerTaxNum;

    @ApiModelProperty(value = "乙方电话")
    private String sellerTel;

    @ApiModelProperty(value = "乙方地址")
    private String sellerAddress;

    @ApiModelProperty("乙方银行开户行")
    private String sellerBank;

    @ApiModelProperty("乙方银行账号")
    private String sellerAccount;

    @ApiModelProperty("甲方名称")
    private String partyAName;

    @ApiModelProperty("乙方名称")
    private String partyBName;

    @ApiModelProperty("收款计划id及金额")
    private List<CollectionAmountF> collectionAmountFList;
}
