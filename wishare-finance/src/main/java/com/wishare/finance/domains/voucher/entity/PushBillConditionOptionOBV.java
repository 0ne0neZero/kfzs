package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class PushBillConditionOptionOBV {

    @ApiModelProperty(value = "过滤条件值id", required = true)
    @NotBlank(message = "过滤条件值id不能为空")
    private String id;

    @ApiModelProperty(value = "过滤条件值编码，有则传")
    private String code;

    @ApiModelProperty(value = "过滤条件值名称", required = true)
    @NotBlank(message = "过滤条件值名称不能为空")
    private String name;

    public PushBillConditionOptionOBV() {
    }

    public PushBillConditionOptionOBV(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
