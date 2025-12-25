package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 查询对账分组信息
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("查询对账分组信息")
public class QueryReconcileGroupF {

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

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空!")
    private String supCpUnitId;

    @Setter
    @Getter
    public static class DimensionRule{

        @ApiModelProperty(value = "是否单个分组")
        private boolean group = true;

        @ApiModelProperty(value = "分组值")
        private List<String> values;
    }

}
