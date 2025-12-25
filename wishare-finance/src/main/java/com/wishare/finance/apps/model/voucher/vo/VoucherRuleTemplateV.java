package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 凭证规则凭证模板信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证规则凭证模板信息")
public class VoucherRuleTemplateV {

    @ApiModelProperty(value = "凭证模板id")
    private Long voucherRuleId;

    @ApiModelProperty(value = "凭证模板id")
    private Long voucherTemplateId;


}
