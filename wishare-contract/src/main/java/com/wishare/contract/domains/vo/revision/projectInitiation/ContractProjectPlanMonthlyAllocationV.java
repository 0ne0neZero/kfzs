package com.wishare.contract.domains.vo.revision.projectInitiation;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "立项关联合约规划月度分摊")
public class ContractProjectPlanMonthlyAllocationV {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty("关联的立项ID")
    private String projectInitiationId;

    @ApiModelProperty("合约规划id")
    private String contractProjectPlanId;

    @ApiModelProperty("月度分摊类型 0 合约规划, 1 成本确认")
    private Integer monthlyAllocationType;

    @ApiModelProperty("分摊类型 0 可用金额, 1 分摊金额, 2 分摊后剩余, 3 上次分摊金额")
    private Integer type;

    @ApiModelProperty(name = "动态成本GUID", notes = "动态成本GUID")
    private String dynamicCostGuid;

    @ApiModelProperty(name = "成本管控方式名称", notes = "成本管控方式名称")
    private String costControlTypeName;

    @ApiModelProperty(name = "成本管控方式枚举", notes = "成本管控方式枚举")
    private Integer costControlTypeEnum;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty(name = "当年合计", notes = "年度")
    private BigDecimal yearSurplus;

    @ApiModelProperty(name = "一月", notes = "一月")
    private BigDecimal janSurplus;

    @ApiModelProperty(name = "二月", notes = "二月")
    private BigDecimal febSurplus;

    @ApiModelProperty(name = "三月", notes = "三月")
    private BigDecimal marSurplus;

    @ApiModelProperty(name = "四月", notes = "四月")
    private BigDecimal aprSurplus;

    @ApiModelProperty(name = "五月", notes = "五月")
    private BigDecimal maySurplus;

    @ApiModelProperty(name = "六月", notes = "六月")
    private BigDecimal junSurplus;

    @ApiModelProperty(name = "七月", notes = "七月")
    private BigDecimal julSurplus;

    @ApiModelProperty(name = "八月", notes = "八月")
    private BigDecimal augSurplus;

    @ApiModelProperty(name = "九月", notes = "九月")
    private BigDecimal sepSurplus;

    @ApiModelProperty(name = "十月", notes = "十月")
    private BigDecimal octSurplus;

    @ApiModelProperty(name = "十一月", notes = "十一月")
    private BigDecimal novSurplus;

    @ApiModelProperty(name = "十二月", notes = "十二月")
    private BigDecimal decSurplus;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新人")
    private String operator;

    @ApiModelProperty(value = "更新人")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    /**
     * 根据月份数字设置对应的金额
     * @param month 月份 (1-12)
     * @param amount 金额
     */
    public void setMonthSurplus(int month, BigDecimal amount) {
        // 确保金额保留两位小数 (标准货币精度)
        BigDecimal finalSurplus = amount.setScale(2, RoundingMode.HALF_UP);
        switch (month) {
            case 1: janSurplus = finalSurplus; break;
            case 2: febSurplus = finalSurplus; break;
            case 3: marSurplus = finalSurplus; break;
            case 4: aprSurplus = finalSurplus; break;
            case 5: maySurplus = finalSurplus; break;
            case 6: junSurplus = finalSurplus; break;
            case 7: julSurplus = finalSurplus; break;
            case 8: augSurplus = finalSurplus; break;
            case 9: sepSurplus = finalSurplus; break;
            case 10: octSurplus = finalSurplus; break;
            case 11: novSurplus = finalSurplus; break;
            case 12: decSurplus = finalSurplus; break;
            default: throw new IllegalArgumentException("无效的月份: " + month);
        }
    }

    /**
     * 计算月度分摊 分摊后剩余
     *
     * @param monthlyAllocationType0 可用金额
     * @param monthlyAllocationType1 分摊金额
     * @return
     */
    public ContractProjectPlanMonthlyAllocationV calcMonthlyAllocationCalculation(ContractProjectPlanMonthlyAllocationV monthlyAllocationType0, ContractProjectPlanMonthlyAllocationV monthlyAllocationType1) {
        return this.setYearSurplus(NumberUtil.sub(monthlyAllocationType0.getYearSurplus(), monthlyAllocationType1.getYearSurplus()))
                .setJanSurplus(NumberUtil.sub(monthlyAllocationType0.getJanSurplus(), monthlyAllocationType1.getJanSurplus()))
                .setFebSurplus(NumberUtil.sub(monthlyAllocationType0.getFebSurplus(), monthlyAllocationType1.getFebSurplus()))
                .setMarSurplus(NumberUtil.sub(monthlyAllocationType0.getMarSurplus(), monthlyAllocationType1.getMarSurplus()))
                .setAprSurplus(NumberUtil.sub(monthlyAllocationType0.getAprSurplus(), monthlyAllocationType1.getAprSurplus()))
                .setMaySurplus(NumberUtil.sub(monthlyAllocationType0.getMaySurplus(), monthlyAllocationType1.getMaySurplus()))
                .setJunSurplus(NumberUtil.sub(monthlyAllocationType0.getJunSurplus(), monthlyAllocationType1.getJunSurplus()))
                .setJulSurplus(NumberUtil.sub(monthlyAllocationType0.getJulSurplus(), monthlyAllocationType1.getJulSurplus()))
                .setAugSurplus(NumberUtil.sub(monthlyAllocationType0.getAugSurplus(), monthlyAllocationType1.getAugSurplus()))
                .setSepSurplus(NumberUtil.sub(monthlyAllocationType0.getSepSurplus(), monthlyAllocationType1.getSepSurplus()))
                .setOctSurplus(NumberUtil.sub(monthlyAllocationType0.getOctSurplus(), monthlyAllocationType1.getOctSurplus()))
                .setNovSurplus(NumberUtil.sub(monthlyAllocationType0.getNovSurplus(), monthlyAllocationType1.getNovSurplus()))
                .setDecSurplus(NumberUtil.sub(monthlyAllocationType0.getDecSurplus(), monthlyAllocationType1.getDecSurplus()));
    }
}