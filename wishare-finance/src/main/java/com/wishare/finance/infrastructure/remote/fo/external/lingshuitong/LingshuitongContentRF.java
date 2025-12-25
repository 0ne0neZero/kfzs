package com.wishare.finance.infrastructure.remote.fo.external.lingshuitong;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("灵税通入账凭证")
public class LingshuitongContentRF {

    @ApiModelProperty(value = "租户id不能为空",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "0取消凭证 1入账凭证",required = true)
    @NotBlank(message = "0取消凭证 1入账凭证 不能为空")
    private String voucherType;

    @ApiModelProperty(value = "发票内容",required = true)
    @NotNull(message = "发票内容不能为空")
    private List<LingshuitongInvoiceRF> invoices;
}
