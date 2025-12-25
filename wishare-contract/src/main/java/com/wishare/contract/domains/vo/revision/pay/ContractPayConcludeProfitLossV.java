package com.wishare.contract.domains.vo.revision.pay;

import java.math.BigDecimal;
import java.time.LocalDate;
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
* 合同成本损益表视图对象
* </p>
*
* @author chenglong
* @since 2023-10-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同成本损益表视图对象", description = "合同成本损益表视图对象")
public class ContractPayConcludeProfitLossV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联合同ID
    */
    @ApiModelProperty("关联合同ID")
    private String contractId;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    private String contractNo;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String contractName;
    /**
    * 供应商
    */
    @ApiModelProperty("供应商")
    private String merchant;
    /**
    * 供应商名称
    */
    @ApiModelProperty("供应商名称")
    private String merchantName;
    /**
    * 付款计划编号
    */
    @ApiModelProperty("付款计划编号")
    private String payNotecode;
    /**
    * 期数
    */
    @ApiModelProperty("期数")
    private Boolean termDate;
    /**
    * 计划收付款时间
    */
    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDate plannedCollectionTime;
    /**
    * 计划收付款金额
    */
    @ApiModelProperty("计划收付款金额")
    private BigDecimal plannedCollectionAmount;
    /**
    * 结算金额
    */
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    /**
    * 扣款金额
    */
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    /**
    * 收票金额
    */
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    /**
    * 付款金额
    */
    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;
    /**
    * 计划状态 0待提交  1未完成  2已完成
    */
    @ApiModelProperty("计划状态 0待提交  1未完成  2已完成")
    private Boolean planStatus;
    /**
    * 结算状态 0未结算  1未完成  2已完成
    */
    @ApiModelProperty("结算状态 0未结算  1未完成  2已完成")
    private Boolean paymentStatus;
    /**
    * 收票状态 0未完成  1已完成
    */
    @ApiModelProperty("收票状态 0未完成  1已完成")
    private Boolean invoiceStatus;
    /**
    * 审核状态0待提交1审批中2已通过3已拒绝
    */
    @ApiModelProperty("审核状态0待提交1审批中2已通过3已拒绝")
    private Boolean reviewStatus;
    /**
    * 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
    */
    @ApiModelProperty("拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)")
    private Boolean splitMode;
    /**
    * 费项
    */
    @ApiModelProperty("费项")
    private String chargeItem;
    /**
    * 费项ID
    */
    @ApiModelProperty("费项ID")
    private String chargeItemId;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private String taxRate;
    /**
    * 税率ID
    */
    @ApiModelProperty("税率ID")
    private String taxRateId;
    /**
    * 不含税金额
    */
    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;
    /**
    * 税额
    */
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    private String remark;
    /**
    * 未付金额
    */
    @ApiModelProperty("未付金额")
    private BigDecimal noPayAmount;
    /**
    * 金额比例
    */
    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;
    /**
    * 次
    */
    @ApiModelProperty("次")
    private Boolean howOrder;
    /**
    * 服务类型
    */
    @ApiModelProperty("服务类型")
    private Boolean serviceType;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * 收入状态 0-未确收 1-已确收
    */
    @ApiModelProperty("收入状态 0-未确收 1-已确收")
    private Boolean acceptStatus;
    /**
    * 收入确认金额(元)
    */
    @ApiModelProperty("收入确认金额(元)")
    private BigDecimal acceptAmount;

    @ApiModelProperty("所属部门")
    private String departName;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

}
