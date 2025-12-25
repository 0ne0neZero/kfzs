package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 对账维度规则值对象
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("对账维度规则信息")
public class ReconcileDimensionRuleQuery {

    @ApiModelProperty(value = "项目维度")
    private DimensionRule community;

    @ApiModelProperty(value = "法定范围维度")
    private DimensionRule statutoryBody;

    @ApiModelProperty(value = "收款账号维度")
    private DimensionRule statutoryBodyAccount;

    @ApiModelProperty("成本中心维度")
    private DimensionRule costCenter;

    @ApiModelProperty("支付渠道维度")
    private DimensionRule payChannel;

    @Setter
    @Getter
    public static class DimensionRule{

        @ApiModelProperty(value = "是否单个分组")
        private boolean group;

        @ApiModelProperty(value = "分组值")
        private List<String> values;

        public boolean notEmpty(){
            return group || (Objects.nonNull(values) && !values.isEmpty());
        }
    }





}
