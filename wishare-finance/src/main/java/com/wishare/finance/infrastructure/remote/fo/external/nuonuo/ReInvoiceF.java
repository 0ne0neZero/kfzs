package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/9/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票重试入参")
public class ReInvoiceF {

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty("发票流水号，流水号和订单号两字段二选一，同时存在以流水号为准")
    private String fpqqlsh;

    @ApiModelProperty("订单号")
    private String orderno;
}
