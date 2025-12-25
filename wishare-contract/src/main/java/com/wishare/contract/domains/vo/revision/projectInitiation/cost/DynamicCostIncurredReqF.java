package com.wishare.contract.domains.vo.revision.projectInitiation.cost;


import cn.hutool.core.util.NumberUtil;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态成本校验及占用接口入参
 */
@Data
@Accessors(chain = true)
public class DynamicCostIncurredReqF {

    @ApiModelProperty("单据ID")
    private String billGuid;

    @ApiModelProperty("单据编码")
    private String billCode;

    @ApiModelProperty("单据名称")
    private String billName;

    @ApiModelProperty("合同ID")
    private String contractId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    /**
     * @see com.wishare.contract.domains.enums.BillTypeEnum
     */
    @ApiModelProperty("单据类型名称;合同发起、合同变更/补充协议、合同驳回、合同结算、立项发起、立项驳回、立项金额确定等")
    private String billTypeName;

    @ApiModelProperty("单据类型枚举：1，2，3，4，5，6，7")
    private Integer billTypeEnum;

    /**
     * 注:立项/合同，负数金额--调用方审批通过时占用，撤回/驳回时释放-为负数
     */
    @ApiModelProperty("操作类型：10校验20占用30释放")
    private Integer operationType;

    @ApiModelProperty("触点;占用OR释放")
    private String contactor;

    @ApiModelProperty("地区公司GUID")
    private String buGuid;

    @ApiModelProperty("项目GUID")
    private String projectGuid;

    @ApiModelProperty("业务线GUID")
    private String businessGuid = "4875602536615936";

    @ApiModelProperty("业务单元编号")
    private String businessUnitCode = "14";

    @ApiModelProperty("分摊金额")
    private BigDecimal incurredAmount;

    @ApiModelProperty("来源系统（1合同 2E采 3能源  4奖金平台）")
    private Integer systemType;

    @ApiModelProperty("分摊明细")
    private List<IncurredDataListDTO> incurredDataList = new ArrayList<>();

    @NoArgsConstructor
    @Data
    public static class IncurredDataListDTO {

        @ApiModelProperty("成本费项全码")
        private String accountItemFullCode;

        @ApiModelProperty("动态成本GUID")
        private String dynamicCostGuid;

        @ApiModelProperty("分摊年度（格式：yyyy）")
        private String shareYear;

        @ApiModelProperty("年度分摊金额")
        private BigDecimal yearShareAmount;

        @ApiModelProperty("一月分摊金额")
        private BigDecimal janShareAmount;

        @ApiModelProperty("二月分摊金额")
        private BigDecimal febShareAmount;

        @ApiModelProperty("三月分摊金额")
        private BigDecimal marShareAmount;

        @ApiModelProperty("四月分摊金额")
        private BigDecimal aprShareAmount;

        @ApiModelProperty("五月分摊金额")
        private BigDecimal mayShareAmount;

        @ApiModelProperty("六月分摊金额")
        private BigDecimal junShareAmount;

        @ApiModelProperty("七月分摊金额")
        private BigDecimal julShareAmount;

        @ApiModelProperty("八月分摊金额")
        private BigDecimal augShareAmount;

        @ApiModelProperty("九月分摊金额")
        private BigDecimal sepShareAmount;

        @ApiModelProperty("十月分摊金额")
        private BigDecimal octShareAmount;

        @ApiModelProperty("十一月分摊金额")
        private BigDecimal novShareAmount;

        @ApiModelProperty("十二月分摊金额")
        private BigDecimal decShareAmount;

        /**
         * 成本占用
         * @param monthlyAllocation
         * @return
         */
        public void occupiedDynamicCostIncurred(IncurredDataListDTO incurredDataDTO, ContractProjectPlanMonthlyAllocationV monthlyAllocation) {
            incurredDataDTO.setDynamicCostGuid(monthlyAllocation.getDynamicCostGuid())
                    .setShareYear(monthlyAllocation.getYear())
                    .setYearShareAmount(monthlyAllocation.getYearSurplus())
                    .setJanShareAmount(monthlyAllocation.getJanSurplus())
                    .setFebShareAmount(monthlyAllocation.getFebSurplus())
                    .setMarShareAmount(monthlyAllocation.getMarSurplus())
                    .setAprShareAmount(monthlyAllocation.getAprSurplus())
                    .setMayShareAmount(monthlyAllocation.getMaySurplus())
                    .setJunShareAmount(monthlyAllocation.getJunSurplus())
                    .setJulShareAmount(monthlyAllocation.getJulSurplus())
                    .setAugShareAmount(monthlyAllocation.getAugSurplus())
                    .setSepShareAmount(monthlyAllocation.getSepSurplus())
                    .setOctShareAmount(monthlyAllocation.getOctSurplus())
                    .setNovShareAmount(monthlyAllocation.getNovSurplus())
                    .setDecShareAmount(monthlyAllocation.getDecSurplus());
        }

        /**
         * 成本释放
         * @param monthlyAllocation
         * @return
         */
        public void freeDynamicCostIncurred(IncurredDataListDTO incurredDataDTO, ContractProjectPlanMonthlyAllocationV monthlyAllocation) {
            incurredDataDTO.setDynamicCostGuid(monthlyAllocation.getDynamicCostGuid())
                    .setShareYear(monthlyAllocation.getYear())
                    .setYearShareAmount(this.negate(monthlyAllocation.getYearSurplus()))
                    .setJanShareAmount(this.negate(monthlyAllocation.getJanSurplus()))
                    .setFebShareAmount(this.negate(monthlyAllocation.getFebSurplus()))
                    .setMarShareAmount(this.negate(monthlyAllocation.getMarSurplus()))
                    .setAprShareAmount(this.negate(monthlyAllocation.getAprSurplus()))
                    .setMayShareAmount(this.negate(monthlyAllocation.getMaySurplus()))
                    .setJunShareAmount(this.negate(monthlyAllocation.getJunSurplus()))
                    .setJulShareAmount(this.negate(monthlyAllocation.getJulSurplus()))
                    .setAugShareAmount(this.negate(monthlyAllocation.getAugSurplus()))
                    .setSepShareAmount(this.negate(monthlyAllocation.getSepSurplus()))
                    .setOctShareAmount(this.negate(monthlyAllocation.getOctSurplus()))
                    .setNovShareAmount(this.negate(monthlyAllocation.getNovSurplus()))
                    .setDecShareAmount(this.negate(monthlyAllocation.getDecSurplus()));
        }

        /**
         * 成本确认金额计算
         *
         * @param incurredDataDTO
         * @param type1
         * @param type3
         */
        public void freeDynamicCostIncurred(IncurredDataListDTO incurredDataDTO, ContractProjectPlanMonthlyAllocationV type1, ContractProjectPlanMonthlyAllocationV type3) {
            incurredDataDTO
                    .setDynamicCostGuid(type1.getDynamicCostGuid())
                    .setShareYear(type1.getYear())
                    .setYearShareAmount(NumberUtil.sub(type1.getYearSurplus(), type3.getYearSurplus()))
                    .setJanShareAmount(NumberUtil.sub(type1.getJanSurplus(), type3.getJanSurplus()))
                    .setFebShareAmount(NumberUtil.sub(type1.getFebSurplus(), type3.getFebSurplus()))
                    .setMarShareAmount(NumberUtil.sub(type1.getMarSurplus(), type3.getMarSurplus()))
                    .setAprShareAmount(NumberUtil.sub(type1.getAprSurplus(), type3.getAprSurplus()))
                    .setMayShareAmount(NumberUtil.sub(type1.getMaySurplus(), type3.getMaySurplus()))
                    .setJunShareAmount(NumberUtil.sub(type1.getJunSurplus(), type3.getJunSurplus()))
                    .setJulShareAmount(NumberUtil.sub(type1.getJulSurplus(), type3.getJulSurplus()))
                    .setAugShareAmount(NumberUtil.sub(type1.getAugSurplus(), type3.getAugSurplus()))
                    .setSepShareAmount(NumberUtil.sub(type1.getSepSurplus(), type3.getSepSurplus()))
                    .setOctShareAmount(NumberUtil.sub(type1.getOctSurplus(), type3.getOctSurplus()))
                    .setNovShareAmount(NumberUtil.sub(type1.getNovSurplus(), type3.getNovSurplus()))
                    .setDecShareAmount(NumberUtil.sub(type1.getDecSurplus(), type3.getDecSurplus()));
        }

        private BigDecimal negate(BigDecimal value) {
            return value != null ? value.negate() : null;
        }
    }
}
