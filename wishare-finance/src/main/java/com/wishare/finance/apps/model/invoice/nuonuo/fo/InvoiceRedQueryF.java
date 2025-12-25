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
@ApiModel("红字专用发票信息表查询接口入参")
public class InvoiceRedQueryF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty("24 位申请单号:12位金税盘编号+12位该红字信息表请求时间 (YYMMDDHHMMSS)")
    private String billNo;

    @ApiModelProperty(value = "销方税号", required = true)
    private String sellerTaxNo;

    @ApiModelProperty("信息表编号")
    private String billInfoNo;

    @ApiModelProperty("购方税号")
    private String buyerTaxNo;

    @ApiModelProperty(value = "填开起始时间，申请单号或信息表编号有值时，可为空，允许最大查询范围为60天 格式：20200526", example = "20220809")
    private String billTimeBegin;

    @ApiModelProperty(value = "填开结束时间，申请单号或信息表编号有值时，可为空，允许最大查询范围为60天格式：20200526", example = "20220810")
    private String billTimeEnd;
}
