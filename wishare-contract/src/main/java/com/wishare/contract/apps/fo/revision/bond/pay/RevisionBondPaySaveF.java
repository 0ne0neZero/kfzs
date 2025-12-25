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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
* <p>
* 保证金改版-缴纳类保证金 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-缴纳类保证金新增请求参数", description = "保证金改版-缴纳类保证金新增请求参数")
public class RevisionBondPaySaveF {

    /**
    * 计划编号 不可为空
    */
    @ApiModelProperty(value = "计划编号",required = true)
    @NotBlank(message = "计划编号不可为空")
    @Length(message = "计划编号不可超过 50 个字符",max = 50)
    private String code;
    /**
    * 保证金类型Code 不可为空
    */
    @ApiModelProperty(value = "保证金类型Code",required = true)
    @NotBlank(message = "保证金类型Code不可为空")
    @Length(message = "保证金类型Code不可超过 40 个字符",max = 40)
    private String typeCode;
    /**
    * 保证金类型名称
    */
    @ApiModelProperty("保证金类型名称")
    @Length(message = "保证金类型名称不可超过 50 个字符",max = 50)
    private String type;
    /**
    * 客户ID 不可为空
    */
    @ApiModelProperty(value = "客户ID",required = true)
    @NotBlank(message = "客户ID不可为空")
    @Length(message = "客户ID不可超过 40 个字符",max = 40)
    private String customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    @Length(message = "客户名称不可超过 50 个字符",max = 50)
    private String customer;
    /**
    * 合同ID
    */
    @ApiModelProperty("合同ID")
    @Length(message = "合同ID不可超过 40 个字符",max = 40)
    private String contractId;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 40 个字符",max = 40)
    private String contractCode;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 50 个字符",max = 50)
    private String contractName;
    /**
    * 成本中心ID
    */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
    * 成本中心名称
    */
    @ApiModelProperty("成本中心名称")
    @Length(message = "成本中心名称不可超过 50 个字符",max = 50)
    private String costCenterName;
    /**
    * 所属项目ID
    */
    @ApiModelProperty("所属项目ID")
    @Length(message = "所属项目ID不可超过 40 个字符",max = 40)
    private String communityId;
    /**
    * 所属项目名称
    */
    @ApiModelProperty("所属项目名称")
    @Length(message = "所属项目名称不可超过 50 个字符",max = 50)
    private String communityName;
    /**
    * 保证金总额 不可为空
    */
    @ApiModelProperty(value = "保证金总额",required = true)
    @Digits(integer = 18,fraction =2,message = "保证金总额不正确")
    @NotNull(message = "保证金总额不可为空")
    private BigDecimal bondAmount;
    /**
    * 计划付款日期
    */
    @ApiModelProperty("计划付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate plannedPayDate;
    /**
    * 计划付款金额 不可为空
    */
    @ApiModelProperty(value = "计划付款金额",required = true)
    @Digits(integer = 18,fraction =2,message = "计划付款金额不正确")
    @NotNull(message = "计划付款金额不可为空")
    private BigDecimal plannedPayAmount;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 40 个字符",max = 40)
    private String orgId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
    * 负责人ID
    */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 40 个字符",max = 40)
    private String chargeManId;
    /**
    * 负责人
    */
    @ApiModelProperty("负责人")
    @Length(message = "负责人不可超过 50 个字符",max = 50)
    private String chargeMan;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 500 个字符",max = 500)
    private String remark;
    /**
    * 状态（0 待提交   3 未完成   5 已完成）
    */
    @ApiModelProperty("状态（0 待提交   3 未完成   5 已完成）")
    private Integer status;
    /**
    * 已付款金额
    */
    @ApiModelProperty("已付款金额")
    @Digits(integer = 18,fraction =2,message = "已付款金额不正确")
    private BigDecimal payAmount;
    /**
    * 已收款金额
    */
    @ApiModelProperty("已收款金额")
    @Digits(integer = 18,fraction =2,message = "已收款金额不正确")
    private BigDecimal collectAmount;
    /**
    * 已开收据金额
    */
    @ApiModelProperty("已开收据金额")
    @Digits(integer = 18,fraction =2,message = "已开收据金额不正确")
    private BigDecimal receiptAmount;
    /**
    * 已结转金额
    */
    @ApiModelProperty("已结转金额")
    @Digits(integer = 18,fraction =2,message = "已结转金额不正确")
    private BigDecimal settleTransferAmount;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * 中台临时账单id
    */
    @ApiModelProperty("中台临时账单id")
    private Long billId;
    /**
    * 中台临时账单编号（招投标保证金才有）
    */
    @ApiModelProperty("中台临时账单编号（招投标保证金才有）")
    @Length(message = "中台临时账单编号（招投标保证金才有）不可超过 40 个字符",max = 40)
    private String billNo;
    /**
    * 开户行
    */
    @ApiModelProperty("开户行")
    @Length(message = "开户行不可超过 20 个字符",max = 20)
    private String bankName;
    /**
    * 银行账户
    */
    @ApiModelProperty("银行账户")
    @Length(message = "银行账户不可超过 50 个字符",max = 50)
    private String bankAccount;

}
