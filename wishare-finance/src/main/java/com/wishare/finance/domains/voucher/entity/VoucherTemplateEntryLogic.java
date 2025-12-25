package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 凭证模板逻辑
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
public class VoucherTemplateEntryLogic {

    @ApiModelProperty(value = "凭证模板逻辑类型：code运算实体, method运算方式, bracket括号, number数字, point小数点, entryAmount分录金额")
    @NotBlank(message = "凭证模板逻辑类型不能为空")
    private String type;

    @ApiModelProperty(value = "凭证模板逻辑值")
    @NotBlank(message = "凭证模板逻辑值不能为空")
    private String value;

    @ApiModelProperty(value = "凭证模板逻辑标题")
    private String label;

    public VoucherTemplateEntryLogic() {
    }

    public VoucherTemplateEntryLogic(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
