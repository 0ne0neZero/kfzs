package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author yyx
 * @date 2023/5/9 13:55
 */
@Getter
@Setter
@ApiModel("账单拆分子账单拆分内容")
public class SplitDetailF {

    @ApiModelProperty("拆分账单收费对象类型")
    @NotNull(message = "收费对象类型不能为空")
    private Integer customerType;

    @ApiModelProperty("账单金额")
    @NotNull(message = "账单金额百分比不能为空")
    private BigDecimal totalAmountPercentage;

    @ApiModelProperty("尾差余数分配")
    @NotNull(message = "尾差余数分配不能为空")
    private Boolean isRemainderSplit;

    @ApiModelProperty("备注信息")
    private String remark;


}
