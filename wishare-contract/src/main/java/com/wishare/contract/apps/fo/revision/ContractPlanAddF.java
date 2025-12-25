package com.wishare.contract.apps.fo.revision;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.List;

/**
 * 该父类仅做属性复用，不能承载具体的数据传输功能，若要使用对应的属性，请使用子类
 *
 * @author 龙江锋
 * @date 2023/8/17 13:54
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPlanAddF {
    @ApiModelProperty("合同id")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    @ApiModelProperty("收付款计划编号")
    private String payNotecode;

    @ApiModelProperty("合同编号")
    @NotBlank(message = "合同编号不能为空")
    private String contractNo;

    @ApiModelProperty("合同名称")
    @NotBlank(message = "合同名称不能为空")
    private String contractName;

    @ApiModelProperty("期数")
    @NotNull(message = "期数不能为空")
    private Integer termDate;

    @ApiModelProperty("计划收付款金额")
    @NotNull(message = "计划收付款金额不能为空")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("计划收付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "计划收付款日期不能为空")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("结算金额")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal settlementAmount;

    @ApiModelProperty("结算状态 未结算:1 未完成:2 已完成:3")
    private Integer paymentStatus;

    @ApiModelProperty("开票/收票金额")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal invoiceApplyAmount;

    @ApiModelProperty("开票/收票状态 未完成:1 已完成:2")
    private Integer invoiceStatus;

    @ApiModelProperty("收付款金额")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal paymentAmount;

    @ApiModelProperty("计划状态 待提交:1 已完成:2 未完成:3")
    private Integer planStatus;

    @ApiModelProperty("合同金额(本币)")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal contractAmount;

    @ApiModelProperty("未计划金额,点击新增后的那个")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal noPlanAmount;

    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate contractStartTime;

    @ApiModelProperty("合同到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate contractEndTime;

    @ApiModelProperty("拆分方式")
    @NotNull(message = "拆分方式不能为空")
    private Integer splitMode;

    @ApiModelProperty("计划总金额")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    @NotNull(message = "计划总金额不能为空")
    private BigDecimal planAllAmount;

    @ApiModelProperty("金额比例")
    @NotBlank(message = "金额比例不能为空")
    private BigDecimal ratioAmount;

    @ApiModelProperty("服务类型")
    @NotBlank(message = "服务类型不能为空")
    private Integer serviceType;

    @ApiModelProperty("费项")
    @NotBlank(message = "费项不能为空")
    private String chargeItem;

    @ApiModelProperty("费项ID数组")
    private String chargeItemId;

    @ApiModelProperty("费项ID数组")
    private List<Long> chargeItemIdList;

    @ApiModelProperty("税率")
    @NotBlank(message = "税率不能为空")
    private String taxRate;

    @ApiModelProperty("税率")
    @NotBlank(message = "税率不能为空")
    private String taxRateId;

    @ApiModelProperty("不含税金额")
    @NotNull(message = "不含税金额不能为空")
    private BigDecimal noTaxAmount;

    @ApiModelProperty("税额")
    @NotNull(message = "税额不能为空")
    private BigDecimal taxAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("保存类型1暂存 2提交")
    @NotBlank(message = "保存类型不能为空")
    private String saveType;

    @ApiModelProperty("未付金额")
    private BigDecimal noPayAmount;
}
