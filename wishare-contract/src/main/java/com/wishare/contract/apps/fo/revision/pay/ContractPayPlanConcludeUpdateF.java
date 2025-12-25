package com.wishare.contract.apps.fo.revision.pay;


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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 支出合同订立信息表 更新请求参数 不会跟着表结构更新而更新
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同付款计划信息表更新请求参数", description = "支出合同付款计划信息表")
public class ContractPayPlanConcludeUpdateF {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("期数")
    private Integer termDate;

    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("计划收付款金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("金额比例")
    @NotBlank(message = "金额比例不能为空")
    private BigDecimal ratioAmount;

    @ApiModelProperty("拆分方式")
    @NotNull(message = "拆分方式不能为空")
    private Integer splitMode;

    @ApiModelProperty("服务类型")
    @NotBlank(message = "服务类型不能为空")
    private Integer serviceType;

    @ApiModelProperty("费项")
    @NotBlank(message = "费项不能为空")
    private String chargeItem;

    @ApiModelProperty("费项ID")
    @NotBlank(message = "费项ID不能为空")
    private String chargeItemId;

    @ApiModelProperty("税率")
    @NotBlank(message = "税率不能为空")
    private String taxRate;

    @ApiModelProperty("不含税金额")
    @NotNull(message = "不含税金额不能为空")
    private BigDecimal noTaxAmount;

    @ApiModelProperty("税额")
    @NotNull(message = "税额不能为空")
    private BigDecimal taxAmount;


    @ApiModelProperty("第几批")
    private Integer howorder;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("保存类型1暂存 2提交")
    @NotBlank(message = "保存类型不能为空")
    private String saveType;

    @ApiModelProperty("供应商")
    @NotBlank(message = "供应商不能为空")
    private String merchant;

    @ApiModelProperty("供应商")
    @NotBlank(message = "供应商不能为空")
    private String merchantName;



}
