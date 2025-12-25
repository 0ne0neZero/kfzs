package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.SKMX2Data;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 资金收款单 （应收款明细）说明
 * @author longhuadmin
 */
@Getter
@Setter
@TableName(value = "voucher_bill_zj_rec_detail", autoResultMap = true)
public class VoucherBillZJRecDetailE extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "应收款明细-内码")
    private String innerRecCode;

    @ApiModelProperty(value = "单据内码")
    private String innerSheetCode;


    @ApiModelProperty(value = "核销应收id")
    private String ftId;

    @ApiModelProperty(value = "核销应收编号")
    private String ftNo;

    @ApiModelProperty(value = "核销应收摘要")
    private String ftSummary;


    @ApiModelProperty(value = "价税合计（本币）")
    private BigDecimal totalAmountOnCurrency;

    @ApiModelProperty(value = "价税合计（原币）")
    private BigDecimal totalAmountOnOrigin;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "财务云税率id")
    private String financialTaxRateId;


    @ApiModelProperty(value = "税额(本币)")
    private BigDecimal taxAmountOnCurrency;


    @ApiModelProperty(value = "税额(原币)")
    private BigDecimal taxAmountOnOrigin;


    @ApiModelProperty(value = "不含税金额(原币)")
    private BigDecimal noTaxAmountOnOrigin;

    @ApiModelProperty(value = "不含税金额(本币)")
    private BigDecimal noTaxAmountOnCurrency;


    @ApiModelProperty(value = "业务科目id")
    private String paymentId;

    @ApiModelProperty(value = "业务科目名称")
    private String paymentName;


    @ApiModelProperty(value = "项目id")
    private String projectId;


    @ApiModelProperty(value = "合同编号")
    private String contractNo;


    @ApiModelProperty(value = "计税方式 1:一般计税 2:简易计税")
    private String taxType;


    @ApiModelProperty(value = "核销金额(原币)")
    private BigDecimal offsetAmountOnOrigin;


    @ApiModelProperty(value = "到期日期")
    private String dueDate;


    @ApiModelProperty(value = "合同付款人-主数据编码")
    private String contractPayer;

    @ApiModelProperty(value = "合同付款人-名称")
    private String contractPayerName;

}
