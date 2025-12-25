package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoucherTemplateEntryChargeItem {

    @ApiModelProperty(value = "费项id")
    private Long id;

    @ApiModelProperty(value = "费项名称")
    private String name;

}
