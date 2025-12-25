package com.wishare.finance.domains.voucher.entity;

import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 过滤条件
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
public class VoucherRuleConditionOBV {

    /**
     * {@linkplain VoucherRuleConditionTypeEnum}
     */
    @ApiModelProperty(value = "过滤条件类型")
    private Integer type;

    @ApiModelProperty(value = "过滤方式： 1:包含， 2不包含")
    @NotNull(message = "过滤方式不能为空")
    private Integer method;

    @ApiModelProperty(value = "过滤值")
    private List<VoucherRuleConditionOptionOBV> values;

    @ApiModelProperty(value = "扩展字段")
    private String attach;

}
