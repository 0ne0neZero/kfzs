package com.wishare.contract.domains.vo.revision.projectInitiation.cost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目下费项可用金额明细(合约规划明细)
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "动态成本信息")
public class DynamicCostSurplusInfo {

    @ApiModelProperty(name = "项目GUID", notes = "项目GUID")
    private String projectGuid;
    @ApiModelProperty(name = "地区公司ID", notes = "地区公司ID")
    private String buGuid;
    @ApiModelProperty(name = "业务线GUID", notes = "业务线GUID")
    private String businessGuid;
    @ApiModelProperty(name = "业务单元编码", notes = "业务单元编码")
    private String businessUnitCode;


    @ApiModelProperty(name = "费项全码", notes = "费项全码")
    private String accountItemFullCode;
    @ApiModelProperty(name = "费项全称", notes = "费项全称")
    private String accountItemFullName;
    @ApiModelProperty(name = "费项编码", notes = "费项编码")
    private String accountItemCode;
    @ApiModelProperty(name = "费项名称", notes = "费项名称")
    private String accountItemName;
    @ApiModelProperty(name = "费项层级", notes = "费项层级")
    private Integer accountItemLevel;

    @ApiModelProperty(name = "合约规划目标金额", notes = "合约规划目标金额")
    private BigDecimal summaryTargetAmount;
    @ApiModelProperty(name = "合约规划占用金额", notes = "合约规划占用金额")
    private BigDecimal summaryIncurredAmount;
    @ApiModelProperty(name = "合约规划可用金额", notes = "合约规划可用金额")
    private BigDecimal summarySurplusAmount;

    /************************************当前费项数据********************************************************************/

    @ApiModelProperty(name = "本月目标金额", notes = "本月目标金额")
    private BigDecimal monthTargetAmount;
    @ApiModelProperty(name = "本月已发生金额", notes = "本月已发生金额")
    private BigDecimal monthIncurredAmount;
    @ApiModelProperty(name = "本月已发生占比", notes = "本月已发生占比")
    private BigDecimal monthRate;
    @ApiModelProperty(name = "本年目标金额", notes = "本年目标金额")
    private BigDecimal yearTargetAmount;
    @ApiModelProperty(name = "本年已发生金额", notes = "本年已发生金额")
    private BigDecimal yearIncurredAmount;
    @ApiModelProperty(name = "本年已发生占比", notes = "本年已发生占比")
    private BigDecimal yearRate;

    /************************************二级费项数据************************************************************************/
    @ApiModelProperty(name = "费项全码", notes = "费项全码")
    private String twoLevelFullCode;
    @ApiModelProperty(name = "费项全称", notes = "费项全称")
    private String twoLevelFullName;
    @ApiModelProperty(name = "费项编码", notes = "费项编码")
    private String twoLevelItemCode;
    @ApiModelProperty(name = "费项名称", notes = "费项名称")
    private String twoLevelItemName;
    @ApiModelProperty(name = "二级费项本月目标金额", notes = "二级费项本月目标金额")
    private BigDecimal monthTwoLevelTargetAmount;
    @ApiModelProperty(name = "二级费项本月已发生金额", notes = "二级费项本月已发生金额")
    private BigDecimal monthTwoLeveIncurredAmount;
    @ApiModelProperty(name = "二级费项本月已发生占比", notes = "二级费项本月已发生占比")
    private BigDecimal monthTwoLeveRate;
    @ApiModelProperty(name = "二级费项本年目标金额", notes = "二级费项本年目标金额")
    private BigDecimal yearTwoLeveTargetAmount;
    @ApiModelProperty(name = "二级费项本年已发生金额", notes = "二级费项本年已发生金额")
    private BigDecimal yearTwoLeveIncurredAmount;
    @ApiModelProperty(name = "二级费项本年已发生占比", notes = "二级费项本年已发生占比")
    private BigDecimal yearTwoLeveRate;

    @ApiModelProperty("分摊金额明细")
    private List<DynamicCostSurplusDetailDTO> list;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "分摊金额明细")
    public static class DynamicCostSurplusDetailDTO {

        @ApiModelProperty(name = "动态成本GUID", notes = "动态成本GUID")
        private String dynamicCostGuid;
        @ApiModelProperty(name = "成本管控方式名称", notes = "成本管控方式名称")
        private String costControlTypeName;
        @ApiModelProperty(name = "成本管控方式枚举", notes = "成本管控方式枚举")
        private Integer costControlTypeEnum;
        @ApiModelProperty(name = "预算编制年份", notes = "预算编制年份")
        private String budgetYear;
        @ApiModelProperty(name = "分摊可用金额", notes = "动态成本可用金额")
        private DynamicCostSurplusDTO DynamicCostSurplus;

    }

    /**
     * 分摊可用金额
     */
    @Data
    @Accessors(chain = true)
    @ApiModel(description = "分摊可用金额")
    public static class DynamicCostSurplusDTO {

        @ApiModelProperty(name = "年度余额", notes = "年度余额")
        private BigDecimal yearSurplus;
        @ApiModelProperty(name = "一月余额", notes = "一月余额")
        private BigDecimal janSurplus;
        @ApiModelProperty(name = "二月余额", notes = "二月余额")
        private BigDecimal febSurplus;
        @ApiModelProperty(name = "三月余额", notes = "三月余额")
        private BigDecimal marSurplus;
        @ApiModelProperty(name = "四月余额", notes = "四月余额")
        private BigDecimal aprSurplus;
        @ApiModelProperty(name = "五月余额", notes = "五月余额")
        private BigDecimal maySurplus;
        @ApiModelProperty(name = "六月余额", notes = "六月余额")
        private BigDecimal junSurplus;
        @ApiModelProperty(name = "七月余额", notes = "七月余额")
        private BigDecimal julSurplus;
        @ApiModelProperty(name = "八月余额", notes = "八月余额")
        private BigDecimal augSurplus;
        @ApiModelProperty(name = "九月余额", notes = "九月余额")
        private BigDecimal sepSurplus;
        @ApiModelProperty(name = "十月余额", notes = "十月余额")
        private BigDecimal octSurplus;
        @ApiModelProperty(name = "十一月余额", notes = "十一月余额")
        private BigDecimal novSurplus;
        @ApiModelProperty(name = "十二月余额", notes = "十二月余额")
        private BigDecimal decSurplus;
    }

}
