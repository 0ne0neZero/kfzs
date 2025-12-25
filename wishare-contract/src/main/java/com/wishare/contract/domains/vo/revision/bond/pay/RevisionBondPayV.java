package com.wishare.contract.domains.vo.revision.bond.pay;

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
* 保证金改版-缴纳类保证金视图对象
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "保证金改版-缴纳类保证金视图对象", description = "保证金改版-缴纳类保证金视图对象")
public class RevisionBondPayV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 计划编号
    */
    @ApiModelProperty("计划编号")
    private String code;
    /**
    * 保证金类型Code
    */
    @ApiModelProperty("保证金类型Code")
    private String typeCode;
    /**
    * 保证金类型名称
    */
    @ApiModelProperty("保证金类型名称")
    private String type;
    /**
    * 客户ID
    */
    @ApiModelProperty("客户ID")
    private String customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String customer;
    /**
    * 合同ID
    */
    @ApiModelProperty("合同ID")
    private String contractId;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    private String contractCode;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String contractName;
    /**
    * 成本中心ID
    */
    @ApiModelProperty("成本中心ID")
    private String costCenterId;
    /**
    * 成本中心名称
    */
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    /**
    * 所属项目ID
    */
    @ApiModelProperty("所属项目ID")
    private String communityId;
    /**
    * 所属项目名称
    */
    @ApiModelProperty("所属项目名称")
    private String communityName;
    /**
    * 保证金总额
    */
    @ApiModelProperty("保证金总额")
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
    * 计划付款金额
    */
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedPayAmount;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    private String orgId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    private String orgName;
    /**
    * 负责人ID
    */
    @ApiModelProperty("负责人ID")
    private String chargeManId;
    /**
    * 负责人
    */
    @ApiModelProperty("负责人")
    private String chargeMan;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
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
    private BigDecimal payAmount;
    /**
    * 已收款金额
    */
    @ApiModelProperty("已收款金额")
    private BigDecimal collectAmount;
    /**
    * 已开收据金额
    */
    @ApiModelProperty("已开收据金额")
    private BigDecimal receiptAmount;
    /**
    * 已结转金额
    */
    @ApiModelProperty("已结转金额")
    private BigDecimal settleTransferAmount;
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
    * 中台临时账单id
    */
    @ApiModelProperty("中台临时账单id")
    private Long billId;
    /**
    * 中台临时账单编号（招投标保证金才有）
    */
    @ApiModelProperty("中台临时账单编号（招投标保证金才有）")
    private String billNo;
    /**
    * 开户行
    */
    @ApiModelProperty("开户行")
    private String bankName;
    /**
    * 银行账户
    */
    @ApiModelProperty("银行账户")
    private String bankAccount;

}
