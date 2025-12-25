package com.wishare.contract.domains.dto.revision.bond;

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
* 保证金改版-收取类保证金
* </p>
*
* @author chenglong
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "revision_bond_collect请求对象", description = "保证金改版-收取类保证金")
public class RevisionBondCollectD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("计划编号")
    private String code;
    @ApiModelProperty("保证金类型Code")
    private String typeCode;
    @ApiModelProperty("保证金类型名称")
    private String type;
    @ApiModelProperty("供应商ID")
    private String supplierId;
    @ApiModelProperty("供应商名称")
    private String supplier;
    @ApiModelProperty("合同ID")
    private String contractId;
    @ApiModelProperty("合同编号")
    private String contractCode;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("成本中心ID")
    private String costCenterId;
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    @ApiModelProperty("所属项目ID")
    private String communityId;
    @ApiModelProperty("所属项目名称")
    private String communityName;
    @ApiModelProperty("保证金总额")
    private BigDecimal bondAmount;
    @ApiModelProperty("计划收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionDate;
    @ApiModelProperty("计划收款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("所属部门ID")
    private String orgId;
    @ApiModelProperty("所属部门名称")
    private String orgName;
    @ApiModelProperty("负责人ID")
    private String chargeManId;
    @ApiModelProperty("负责人")
    private String chargeMan;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("状态（0 待提交   3 未完成   5 已完成）")
    private Integer status;
    @ApiModelProperty("已收款金额")
    private BigDecimal collectAmount;
    @ApiModelProperty("已退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("已开收据金额")
    private BigDecimal receiptAmount;
    @ApiModelProperty("已结转金额")
    private BigDecimal settleTransferAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("中台临时账单id")
    private Long billId;
    @ApiModelProperty("中台临时账单编号（招投标保证金才有）")
    private String billNo;
    @ApiModelProperty("开户行")
    private String bankName;
    @ApiModelProperty("银行账户")
    private String bankAccount;

}
