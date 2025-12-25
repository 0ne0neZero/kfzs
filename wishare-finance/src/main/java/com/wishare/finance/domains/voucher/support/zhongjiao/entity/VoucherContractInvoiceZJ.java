package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 *
 */
@Getter
@Setter
@TableName("voucher_contract_zj")
public class VoucherContractInvoiceZJ extends BaseEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票类型")
    private String invoiceType;

    @ApiModelProperty("发票日期")
    private LocalDate invoiceDate;

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("税额")
    private Long taxAmount;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同id")
    private String contractId;

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
    private String writeOffInfo;

    @ApiModelProperty("报账单主表Id")
    private Long voucherBillId;

    @ApiModelProperty("结算单编号")
    private String settlementNo;
}
