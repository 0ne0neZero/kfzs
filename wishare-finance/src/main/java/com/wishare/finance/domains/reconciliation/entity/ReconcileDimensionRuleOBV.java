package com.wishare.finance.domains.reconciliation.entity;

import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
public class ReconcileDimensionRuleOBV {

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

    @ApiModelProperty("支付方式维度")
    private DimensionRule payWay;


    /**
     * 判断没有设置规则
     * @return
     */
    public boolean empty(){
        return Objects.isNull(community) && Objects.isNull(statutoryBody);
    }

    /**
     * 判断有设置规则
     * @return
     */
    public boolean notEmpty(){
        return !empty();
    }


    @Setter
    @Getter
    public static class DimensionRule{

        @ApiModelProperty(value = "是否单个分组")
        private boolean group;

        @ApiModelProperty(value = "分组值")
        private List<String> values;
    }





}
