package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础凭证核算方案规则信息
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@ApiModel(value="基础凭证核算方案规则信息")
public class BaseVoucherSchemeRuleV {

    @ApiModelProperty(value = "核算方案id")
    private Long id;

    @ApiModelProperty(value = "方案名称")
    private String name;

    public BaseVoucherSchemeRuleV() {
    }

    public BaseVoucherSchemeRuleV(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
