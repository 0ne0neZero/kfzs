package com.wishare.finance.domains.voucher.support.fangyuan.entity;

import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 过滤条件
 *
 * @author zmh
 */
@Getter
@Setter
public class VoucherBillRuleConditionOBV {

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
