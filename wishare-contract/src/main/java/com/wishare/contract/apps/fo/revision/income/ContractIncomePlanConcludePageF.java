package com.wishare.contract.apps.fo.revision.income;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.time.LocalDate;

/**
 * <p>
 * 支出合同订立信息表 分页请求参数
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同付款计划信息表分页请求参数", description = "支出合同付款计划信息表")
public class ContractIncomePlanConcludePageF {
    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("期数")
    private Integer termDate;

    @ApiModelProperty("计划收付款金额")
    @Digits(integer = 10, fraction = 2, message = "计划收付款金额不正确")
    private Integer plannedCollectionAmount;

    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;
}
