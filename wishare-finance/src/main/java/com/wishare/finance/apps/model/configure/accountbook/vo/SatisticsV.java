package com.wishare.finance.apps.model.configure.accountbook.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Getter
@Setter
@ApiModel("统计反参")
public class SatisticsV {

    @ApiModelProperty("资产金额")
    private BigDecimal assetsAmount;

    @ApiModelProperty("费用金额")
    private BigDecimal costAmount;

    @ApiModelProperty("负责金额")
    private BigDecimal liabilitiesAmount;

    @ApiModelProperty("所有者权益金额")
    private BigDecimal equitiesAmount;

    @ApiModelProperty("收入金额")
    private BigDecimal incomeAmount;
}
