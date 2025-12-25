package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("认领发票")
public class ClaimInvoiceF {

    @NotBlank(message = "项目id不能为空")
    @ApiModelProperty(value = "项目id")
    public String communityId;

    @NotNull(message = "票据id不能为空")
    @ApiModelProperty(value = "票据id")
    public Long invoiceReceiptId;

    @NotEmpty(message = "单据id不能为空")
    @ApiModelProperty(value = "单据id")
    public List<Long> billIds;

    @NotNull(message = "账单类型不能为空")
    @ApiModelProperty(value = "账单类型")
    public Integer billType;

    @ApiModelProperty(value = "购方名称", required = false)
    private String buyerName;
    @ApiModelProperty(value = "发票类型", required = false)
    private Integer invoiceType;

}
