package com.wishare.contract.domains.vo.revision;

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
 * @since： 2023/8/14  10:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "ContractNumShow合同页面金额展示", description = "ContractNumShow合同页面金额展示")
public class ContractNumShow {

    /**
     * 合同金额
     */
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;

    /**
     * 已收款金额
     */
    @ApiModelProperty("已收款金额")
    private BigDecimal collectAmount;

    /**
     * 未收款金额
     */
    @ApiModelProperty("未收款金额")
    private BigDecimal unCollectAmount;

    /**
     * 已付款金额
     */
    @ApiModelProperty("已付款金额")
    private BigDecimal payAmount;

    /**
     * 未付款金额
     */
    @ApiModelProperty("未付款金额")
    private BigDecimal unPayAmount;

}
