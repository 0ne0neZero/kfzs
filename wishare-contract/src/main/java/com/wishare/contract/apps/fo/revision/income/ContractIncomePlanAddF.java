package com.wishare.contract.apps.fo.revision.income;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.ContractPlanAddF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:22
/**
 * 收入合同付款计划信息表新增参数AddF
 *
 * @author zhangfuyu
 * @Date 2023/7/6/11:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同付款计划信息表新增参数AddF", description = "收入合同付款计划信息表新增参数AddF")
public class ContractIncomePlanAddF extends ContractPlanAddF {


    @ApiModelProperty("计划id")
    @NotBlank(message = "计划id")
    private String id;

    @ApiModelProperty("客户")
    @NotBlank(message = "客户不能为空")
    private String customer;

    @ApiModelProperty("客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    @ApiModelProperty("所属部门")
    private String departName;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("计划收付款金额")
    @Digits(integer = 10,fraction =2,message = "计划收付款金额不正确")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("结算周期")
    private List<String> timeRange;

    @ApiModelProperty("保存类型1暂存 2提交")
    private String saveType;

    @ApiModelProperty("是否枫行梦 1-是 0-否")
    private String isfxm;

    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "结算计划分组")
    private String settlePlanGroup;

    @ApiModelProperty(value = "成本预估编码")
    private String costEstimationCode;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;
}
