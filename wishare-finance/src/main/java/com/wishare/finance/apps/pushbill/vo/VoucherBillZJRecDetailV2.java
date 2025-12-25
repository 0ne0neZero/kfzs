package com.wishare.finance.apps.pushbill.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.SKMX2Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 资金收款单 （应收款明细）说明
 */
@Data
@ApiModel("资金收款单下的应收款明细V2")
public class VoucherBillZJRecDetailV2 {

    @ApiModelProperty(value = "id")
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

    @ApiModelProperty(value = "是否代收代付属性费项的其他应收款")
    private Boolean isTPPReceivebles;
    @ApiModelProperty(value = "是否入库")
    private Boolean isSaveData = Boolean.FALSE;




    public SKMX2Data transfer(){
        SKMX2Data skmx2Data = new SKMX2Data();
        skmx2Data.setYSMXNM(innerRecCode);
        skmx2Data.setDJNM(innerSheetCode);
        skmx2Data.setHXYSBH(ftId);
        skmx2Data.setWHXJEYB("");
        skmx2Data.setYG("");
        skmx2Data.setZJJH("");
        skmx2Data.setJSHJBB(totalAmountOnCurrency.doubleValue());
        skmx2Data.setJSHJYB(totalAmountOnOrigin.doubleValue());
        skmx2Data.setSL(financialTaxRateId);
        skmx2Data.setSEBB(taxAmountOnCurrency.doubleValue());
        skmx2Data.setSEYB(taxAmountOnOrigin.doubleValue());
        skmx2Data.setBHSJEYB(noTaxAmountOnOrigin.doubleValue());
        skmx2Data.setBHSJEBB(noTaxAmountOnCurrency.doubleValue());
        skmx2Data.setZXXM("");
        skmx2Data.setYWKM(paymentId);
        skmx2Data.setXMID(projectId);
        skmx2Data.setHTBH(contractNo);
        skmx2Data.setJSFS(taxType);
        if (StringUtils.isNotBlank(ftId)){
            skmx2Data.setHXJEYB(totalAmountOnCurrency.doubleValue());
        }
        skmx2Data.setDQRQ(dueDate);
        skmx2Data.setHTFKR(contractPayer);
        skmx2Data.setBZ("156");
        return skmx2Data;
    }

}
