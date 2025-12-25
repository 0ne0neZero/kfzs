package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字专用发票信息表下载入参")
public class DownloadInvoiceRedApplyF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "发票种类: s增值税专用发票", required = true)
    @NotBlank(message = "发票种类不能为空")
    private String invoiceLine;

    @ApiModelProperty("填开起始时间,信息表编号有值时，可为空 格式：20220809")
    private String billTimeBegin;

    @ApiModelProperty("填开结束时间,信息表编号有值时，可为空 格式：20220810")
    private String billTimeEnd;

    @ApiModelProperty("购方税号")
    private String buyerTaxNo;

    @ApiModelProperty("信息表编号")
    private String billInfoNo;
}
