package com.wishare.contract.domains.dto.contractset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

/**
* <p>
* 工程类合同计提信息表
* </p>
*
* @author wangrui
* @since 2022-11-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_engineering_plan请求对象", description = "工程类合同计提信息表")
public class ContractEngineeringPlanD {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
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
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
