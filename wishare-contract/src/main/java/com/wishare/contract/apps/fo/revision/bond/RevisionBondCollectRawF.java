package com.wishare.contract.apps.fo.revision.bond;


import java.math.BigDecimal;
import java.time.LocalDate;
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
* 保证金改版-收取类保证金 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-收取类保证金原始请求参数", description = "保证金改版-收取类保证金原始请求参数，会跟着表重新生成")
public class RevisionBondCollectRawF {

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
    * 供应商ID 不可为空
    */
    @ApiModelProperty(value = "供应商ID",required = true)
    @NotBlank(message = "供应商ID不可为空")
    @Length(message = "供应商ID不可超过 40 个字符",max = 40)
    private String supplierId;
    /**
    * 供应商名称
    */
    @ApiModelProperty("供应商名称")
    @Length(message = "供应商名称不可超过 50 个字符",max = 50)
    private String supplier;
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
    * 计划收款日期
    */
    @ApiModelProperty("计划收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionDate;
    /**
    * 计划收款金额 不可为空
    */
    @ApiModelProperty(value = "计划收款金额",required = true)
    @Digits(integer = 18,fraction =2,message = "计划收款金额不正确")
    @NotNull(message = "计划收款金额不可为空")
    private BigDecimal plannedCollectionAmount;
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
    * 已收款金额
    */
    @ApiModelProperty("已收款金额")
    @Digits(integer = 18,fraction =2,message = "已收款金额不正确")
    private BigDecimal collectAmount;
    /**
    * 已退款金额
    */
    @ApiModelProperty("已退款金额")
    @Digits(integer = 18,fraction =2,message = "已退款金额不正确")
    private BigDecimal refundAmount;
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
    * 扣款金额
    */
    @ApiModelProperty("扣款金额")
    @Digits(integer = 18,fraction =2,message = "扣款金额不正确")
    private BigDecimal deductionAmount;
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

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"code\",\"typeCode\",\"type\",\"supplierId\",\"supplier\",\"contractId\",\"contractCode\",\"contractName\",\"costCenterId\",\"costCenterName\",\"communityId\",\"communityName\",\"bondAmount\",\"plannedCollectionDate\",\"plannedCollectionAmount\",\"orgId\",\"orgName\",\"chargeManId\",\"chargeMan\",\"remark\",\"status\",\"collectAmount\",\"refundAmount\",\"receiptAmount\",\"settleTransferAmount\",\"deductionAmount\",\"deleted\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"billId\",\"billNo\",\"bankName\",\"bankAccount\"]"
        + "id 主键ID"
        + "code 计划编号"
        + "typeCode 保证金类型Code"
        + "type 保证金类型名称"
        + "supplierId 供应商ID"
        + "supplier 供应商名称"
        + "contractId 合同ID"
        + "contractCode 合同编号"
        + "contractName 合同名称"
        + "costCenterId 成本中心ID"
        + "costCenterName 成本中心名称"
        + "communityId 所属项目ID"
        + "communityName 所属项目名称"
        + "bondAmount 保证金总额"
        + "plannedCollectionDate 计划收款日期"
        + "plannedCollectionAmount 计划收款金额"
        + "orgId 所属部门ID"
        + "orgName 所属部门名称"
        + "chargeManId 负责人ID"
        + "chargeMan 负责人"
        + "remark 备注"
        + "status 状态（0 待提交   3 未完成   5 已完成）"
        + "collectAmount 已收款金额"
        + "refundAmount 已退款金额"
        + "receiptAmount 已开收据金额"
        + "settleTransferAmount 已结转金额"
        + "deductionAmount 扣款金额"
        + "deleted 是否删除  0 正常 1 删除"
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "billId 中台临时账单id"
        + "billNo 中台临时账单编号（招投标保证金才有）"
        + "bankName 开户行"
        + "bankAccount 银行账户")
    private List<String> fields;


}
