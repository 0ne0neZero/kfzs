package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 工程类合同计提信息表 保存请求参数
* </p>
*
* @author wangrui
* @since 2022-11-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractEngineeringPlanSaveF {

    @ApiModelProperty("合同id")
    @NotNull(message = "合同id不可为空")
    private Long contractId;
    @ApiModelProperty("租户id")
    private String tenantId;
//    @ApiModelProperty("损益计划id")
//    private Long profitLossId;
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
    @ApiModelProperty("计提资料名称")
    private String accrualDataName;
    @ApiModelProperty("计提资料FileVo")
    private List<FileVo> accrualDataFileVo;
    @ApiModelProperty("备注")
    private String remarks;

}
