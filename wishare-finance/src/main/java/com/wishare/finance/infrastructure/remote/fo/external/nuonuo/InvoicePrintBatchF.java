package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
public class InvoicePrintBatchF {

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "诺诺请求体")
    private List<OutInvoicePrintRequestsF> outInvoicePrintRequests;
}
