package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description:  开票结果查询入参
 */
@Getter
@Setter
public class QueryInvoiceResultF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty("发票流水号，两字段二选一，同时存在以流水号为准（最多查50个订单号）")
    private List<String> serialNos;

    @ApiModelProperty("订单编号（最多查50个订单号）")
    private List<String>  orderNos;

    @ApiModelProperty("是否需要提供明细 1-是, 0-否(不填默认 0)")
    private String isOfferInvoiceDetail;
}
