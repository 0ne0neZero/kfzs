package com.wishare.contract.apps.fo.revision.pay;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 合同成本损益表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-10-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同成本损益表原始请求参数", description = "合同成本损益表原始请求参数，会跟着表重新生成")
public class ContractPayConcludeProfitLossRawF {

    /**
    * 关联合同ID 不可为空
    */
    @ApiModelProperty(value = "关联合同ID",required = true)
    @NotBlank(message = "关联合同ID不可为空")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 50 个字符",max = 50)
    private String contractNo;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 50 个字符",max = 50)
    private String contractName;
    /**
    * 供应商
    */
    @ApiModelProperty("供应商")
    @Length(message = "供应商不可超过 50 个字符",max = 50)
    private String merchant;
    /**
    * 供应商名称
    */
    @ApiModelProperty("供应商名称")
    @Length(message = "供应商名称不可超过 50 个字符",max = 50)
    private String merchantName;
    /**
    * 付款计划编号
    */
    @ApiModelProperty("付款计划编号")
    @Length(message = "付款计划编号不可超过 40 个字符",max = 40)
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
    private LocalDateTime plannedCollectionTime;
    /**
    * 计划收付款金额
    */
    @ApiModelProperty("计划收付款金额")
    @Digits(integer = 15,fraction =6,message = "计划收付款金额不正确")
    private BigDecimal plannedCollectionAmount;
    /**
    * 结算金额
    */
    @ApiModelProperty("结算金额")
    @Digits(integer = 15,fraction =6,message = "结算金额不正确")
    private BigDecimal settlementAmount;
    /**
    * 扣款金额
    */
    @ApiModelProperty("扣款金额")
    @Digits(integer = 15,fraction =6,message = "扣款金额不正确")
    private BigDecimal deductionAmount;
    /**
    * 收票金额
    */
    @ApiModelProperty("收票金额")
    @Digits(integer = 15,fraction =6,message = "收票金额不正确")
    private BigDecimal invoiceApplyAmount;
    /**
    * 付款金额
    */
    @ApiModelProperty("付款金额")
    @Digits(integer = 15,fraction =6,message = "付款金额不正确")
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
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * 费项ID
    */
    @ApiModelProperty("费项ID")
    @Length(message = "费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    @Length(message = "税率不可超过 10 个字符",max = 10)
    private String taxRate;
    /**
    * 税率ID
    */
    @ApiModelProperty("税率ID")
    @Length(message = "税率ID不可超过 50 个字符",max = 50)
    private String taxRateId;
    /**
    * 不含税金额
    */
    @ApiModelProperty("不含税金额")
    @Digits(integer = 15,fraction =6,message = "不含税金额不正确")
    private BigDecimal noTaxAmount;
    /**
    * 税额
    */
    @ApiModelProperty("税额")
    @Digits(integer = 15,fraction =6,message = "税额不正确")
    private BigDecimal taxAmount;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 1,000 个字符",max = 1000)
    private String remark;
    /**
    * 未付金额
    */
    @ApiModelProperty("未付金额")
    @Digits(integer = 15,fraction =6,message = "未付金额不正确")
    private BigDecimal noPayAmount;
    /**
    * 金额比例
    */
    @ApiModelProperty("金额比例")
    @Digits(integer = 10,fraction =2,message = "金额比例不正确")
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
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * 收入状态 0-未确收 1-已确收
    */
    @ApiModelProperty("收入状态 0-未确收 1-已确收")
    private Boolean acceptStatus;
    /**
    * 收入确认金额(元)
    */
    @ApiModelProperty("收入确认金额(元)")
    @Digits(integer = 15,fraction =6,message = "收入确认金额(元)不正确")
    private BigDecimal acceptAmount;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"contractNo\",\"contractName\",\"merchant\",\"merchantName\",\"payNotecode\",\"termDate\",\"plannedCollectionTime\",\"plannedCollectionAmount\",\"settlementAmount\",\"deductionAmount\",\"invoiceApplyAmount\",\"paymentAmount\",\"planStatus\",\"paymentStatus\",\"invoiceStatus\",\"reviewStatus\",\"splitMode\",\"chargeItem\",\"chargeItemId\",\"taxRate\",\"taxRateId\",\"noTaxAmount\",\"taxAmount\",\"remark\",\"noPayAmount\",\"ratioAmount\",\"howOrder\",\"serviceType\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\",\"acceptStatus\",\"acceptAmount\"]"
        + "id 主键ID"
        + "contractId 关联合同ID"
        + "contractNo 合同编号"
        + "contractName 合同名称"
        + "merchant 供应商"
        + "merchantName 供应商名称"
        + "payNotecode 付款计划编号"
        + "termDate 期数"
        + "plannedCollectionTime 计划收付款时间"
        + "plannedCollectionAmount 计划收付款金额"
        + "settlementAmount 结算金额"
        + "deductionAmount 扣款金额"
        + "invoiceApplyAmount 收票金额"
        + "paymentAmount 付款金额"
        + "planStatus 计划状态 0待提交  1未完成  2已完成"
        + "paymentStatus 结算状态 0未结算  1未完成  2已完成"
        + "invoiceStatus 收票状态 0未完成  1已完成"
        + "reviewStatus 审核状态0待提交1审批中2已通过3已拒绝"
        + "splitMode 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)"
        + "chargeItem 费项"
        + "chargeItemId 费项ID"
        + "taxRate 税率"
        + "taxRateId 税率ID"
        + "noTaxAmount 不含税金额"
        + "taxAmount 税额"
        + "remark 备注"
        + "noPayAmount 未付金额"
        + "ratioAmount 金额比例"
        + "howOrder 次"
        + "serviceType 服务类型"
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "deleted 是否删除  0 正常 1 删除"
        + "acceptStatus 收入状态 0-未确收 1-已确收"
        + "acceptAmount 收入确认金额(元)")
    private List<String> fields;


}
