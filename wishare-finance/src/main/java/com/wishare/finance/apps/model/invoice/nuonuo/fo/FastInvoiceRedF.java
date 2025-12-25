package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/11/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("红票快捷冲红接口")
public class FastInvoiceRedF {

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;


    @ApiModelProperty(value = "订单号,每个企业唯一",required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("销方企业税号（需要校验与开放平台头部报文中的税号一致）")
    private String taxNum;

    @ApiModelProperty("对应蓝票发票代码")
    private String invoiceCode;

    @ApiModelProperty("对应蓝票发票号码,全电红票时为20位")
    private String invoiceNumber;

    @ApiModelProperty("对应蓝票发票流水号")
    private String invoiceId;

    @ApiModelProperty(value = "红字确认单编号,全电红票必传；且必须在\n" +
            "备注中注明“被冲红蓝字全电发票号码：Y\n" +
            "YYYYYYYY 红字发票信息确认单编号：\n" +
            "XXXXXXXXX”字样，其中“Y”为全\n" +
            "电蓝票的号码，“X”对应的红字确认单编\n" +
            "号（由接口自动拼接）",required = true)
    @NotBlank(message = "红字确认单编号不能为空")
    private String billNo;

    @ApiModelProperty(value = "红字确认单uuid",required = true)
    @NotBlank(message = "红字确认单uuid不能为空")
    private String billUuid;

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public void setTaxnum(String taxnum) {
        this.taxnum = taxnum;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public String getTaxnum() {
        return taxnum;
    }
}
