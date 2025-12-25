package com.wishare.contract.apps.fo.revision.bond.pay;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 保证金改版-缴纳类保证金 更新请求参数
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-缴纳类保证金下拉列表请求参数", description = "保证金改版-缴纳类保证金")
public class RevisionBondPayListF {

    /**
    * code
    */
    @ApiModelProperty("计划编号")
    @Length(message = "计划编号不可超过 50 个字符",max = 50)
    private String code;
    /**
    * typeCode
    */
    @ApiModelProperty("保证金类型Code")
    @Length(message = "保证金类型Code不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
    * type
    */
    @ApiModelProperty("保证金类型名称")
    @Length(message = "保证金类型名称不可超过 50 个字符",max = 50)
    private String type;
    /**
    * customerId
    */
    @ApiModelProperty("客户ID")
    @Length(message = "客户ID不可超过 40 个字符",max = 40)
    private String customerId;
    /**
    * customer
    */
    @ApiModelProperty("客户名称")
    @Length(message = "客户名称不可超过 50 个字符",max = 50)
    private String customer;
    /**
    * contractId
    */
    @ApiModelProperty("合同ID")
    @Length(message = "合同ID不可超过 40 个字符",max = 40)
    private String contractId;
    /**
    * contractCode
    */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 40 个字符",max = 40)
    private String contractCode;
    /**
    * contractName
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 50 个字符",max = 50)
    private String contractName;
    /**
    * costCenterId
    */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
    * costCenterName
    */
    @ApiModelProperty("成本中心名称")
    @Length(message = "成本中心名称不可超过 50 个字符",max = 50)
    private String costCenterName;
    /**
    * communityId
    */
    @ApiModelProperty("所属项目ID")
    @Length(message = "所属项目ID不可超过 40 个字符",max = 40)
    private String communityId;
    /**
    * communityName
    */
    @ApiModelProperty("所属项目名称")
    @Length(message = "所属项目名称不可超过 50 个字符",max = 50)
    private String communityName;
    /**
    * bondAmount
    */
    @ApiModelProperty("保证金总额")
    @Digits(integer = 18,fraction =2,message = "保证金总额不正确")
    private BigDecimal bondAmount;
    /**
    * plannedPayDate
    */
    @ApiModelProperty("计划付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedPayDate;
    /**
    * plannedPayAmount
    */
    @ApiModelProperty("计划付款金额")
    @Digits(integer = 18,fraction =2,message = "计划付款金额不正确")
    private BigDecimal plannedPayAmount;
    /**
    * orgId
    */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 40 个字符",max = 40)
    private String orgId;
    /**
    * orgName
    */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
    * chargeManId
    */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 40 个字符",max = 40)
    private String chargeManId;
    /**
    * chargeMan
    */
    @ApiModelProperty("负责人")
    @Length(message = "负责人不可超过 50 个字符",max = 50)
    private String chargeMan;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
    * status
    */
    @ApiModelProperty("状态（0 待提交   3 未完成   5 已完成）")
    private Integer status;
    /**
    * payAmount
    */
    @ApiModelProperty("已付款金额")
    @Digits(integer = 18,fraction =2,message = "已付款金额不正确")
    private BigDecimal payAmount;
    /**
    * collectAmount
    */
    @ApiModelProperty("已收款金额")
    @Digits(integer = 18,fraction =2,message = "已收款金额不正确")
    private BigDecimal collectAmount;
    /**
    * receiptAmount
    */
    @ApiModelProperty("已开收据金额")
    @Digits(integer = 18,fraction =2,message = "已开收据金额不正确")
    private BigDecimal receiptAmount;
    /**
    * settleTransferAmount
    */
    @ApiModelProperty("已结转金额")
    @Digits(integer = 18,fraction =2,message = "已结转金额不正确")
    private BigDecimal settleTransferAmount;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人")
    @Length(message = "创建人不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人名称")
    @Length(message = "创建人名称不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * operator
    */
    @ApiModelProperty("操作人")
    @Length(message = "操作人不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人名称")
    @Length(message = "操作人名称不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * billId
    */
    @ApiModelProperty("中台临时账单id")
    private Long billId;
    /**
    * billNo
    */
    @ApiModelProperty("中台临时账单编号（招投标保证金才有）")
    @Length(message = "中台临时账单编号（招投标保证金才有）不可超过 40 个字符",max = 40)
    private String billNo;
    /**
    * bankName
    */
    @ApiModelProperty("开户行")
    @Length(message = "开户行不可超过 20 个字符",max = 20)
    private String bankName;
    /**
    * bankAccount
    */
    @ApiModelProperty("银行账户")
    @Length(message = "银行账户不可超过 50 个字符",max = 50)
    private String bankAccount;
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private String indexId;
}
