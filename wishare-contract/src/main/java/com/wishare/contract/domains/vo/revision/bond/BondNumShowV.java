package com.wishare.contract.domains.vo.revision.bond;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/8/11  13:51
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "BondNumShowV", description = "BondNumShowV")
public class BondNumShowV {

    /**
     * 计划  收/付  款金额
     */
    @ApiModelProperty("计划  收/付  款金额")
    private BigDecimal planAmount;

    /**
     * 实际 已 收/付 金额
     */
    @ApiModelProperty("实际 已 收/付 金额")
    private BigDecimal amount;

    /**
     * 保证金余额
     */
    @ApiModelProperty("保证金余额")
    private BigDecimal resAmount;

}
