package com.wishare.contract.apps.fo.contractset;


import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 工程类合同计提信息表 更新请求参数
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractEngineeringPlanUpdateF {

    @ApiModelProperty("主键")
    @NotNull(message = "主键不可为空")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("损益计划id")
    private Long profitLossId;
    @ApiModelProperty("计提编号")
    private String accrualCode;
    @ApiModelProperty("上次工程完成百分比")
    private BigDecimal lastPercent;
    @ApiModelProperty("上次工程总金额")
    private BigDecimal lastAmount;
    @ApiModelProperty("本次工程百分比")
    private BigDecimal thisTimePercent;
    @ApiModelProperty("本次工程总金额")
    private BigDecimal thisTimeAmount;
    @ApiModelProperty("本次计提金额")
    private BigDecimal accrualAmount;
    @ApiModelProperty("计提资料")
    private String accrualData;
    @ApiModelProperty("计提资料FileVo")
    private FileVo accrualDataFileVo;
    @ApiModelProperty("备注")
    private String remarks;
}
