package com.wishare.contract.apps.fo.revision.bond;


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
* 保证金改版-收取类保证金 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-收取类保证金更新请求参数", description = "保证金改版-收取类保证金")
public class RevisionBondCollectUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 40 个字符",max = 40)
    private String id;
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
    * supplierId
    */
    @ApiModelProperty("供应商ID")
    @Length(message = "供应商ID不可超过 40 个字符",max = 40)
    private String supplierId;
    /**
    * supplier
    */
    @ApiModelProperty("供应商名称")
    @Length(message = "供应商名称不可超过 50 个字符",max = 50)
    private String supplier;
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
    * plannedCollectionDate
    */
    @ApiModelProperty("计划收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionDate;
    /**
    * plannedCollectionAmount
    */
    @ApiModelProperty("计划收款金额")
    @Digits(integer = 18,fraction =2,message = "计划收款金额不正确")
    private BigDecimal plannedCollectionAmount;
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
    * collectAmount
    */
    @ApiModelProperty("已收款金额")
    @Digits(integer = 18,fraction =2,message = "已收款金额不正确")
    private BigDecimal collectAmount;
    /**
    * refundAmount
    */
    @ApiModelProperty("已退款金额")
    @Digits(integer = 18,fraction =2,message = "已退款金额不正确")
    private BigDecimal refundAmount;
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
    * deductionAmount
    */
    @ApiModelProperty("扣款金额")
    @Digits(integer = 18,fraction =2,message = "扣款金额不正确")
    private BigDecimal deductionAmount;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
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

}
