package com.wishare.finance.domains.voucher.support.zhongjiao.entity;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import com.wishare.finance.domains.voucher.support.ListUploadLinkZJHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL_DX_ZJ, autoResultMap = true)
public class VoucherBillDxZJ extends BaseEntity {

    @ApiModelProperty(value = "汇总单据id")
    @TableId
    private Long id;
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    @ApiModelProperty(value = "报账类型")
    private String ruleName;
    @ApiModelProperty(value = "推送状态  1待推送 2已推送 3 推送失败 4 推送中  5 已驳回 6 财务云审批驳回")
    private Integer pushState;
    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;
    @ApiModelProperty(value = "报账单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "报账总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "推送方式枚举(1手动推送、2按日推送)")
    private Integer pushMethod;

    @ApiModelProperty(value = "流转状态  1成功  2 失败")
    private Integer wanderAboutState;
    @ApiModelProperty(value = "财务云单据id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String financeId;
    @ApiModelProperty(value = "财务云单据编号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String financeNo;

    @ApiModelProperty(value = "3 对下结算-计提  4 对下结算-实签")
    private Integer billEventType;
    @ApiModelProperty(value = "备注")
    @TableField(typeHandler = JSONTypeHandler.class)
    private String remark;
    @ApiModelProperty(value = "明细内码")
    @TableField(typeHandler = JSONTypeHandler.class)
    private String detailNumber;
    @ApiModelProperty(value = "是否删除：0否，1是")
    private Integer deleted;

    @ApiModelProperty(value = "是否上传附件  0 是 1否")
    private Integer upload;

    @ApiModelProperty(value = "附件数量")
    private Integer uploadNum;

    @ApiModelProperty(value = "附件上传链接")
    @TableField(typeHandler= ListUploadLinkZJHandler.class)
    private List<UploadLinkZJ> uploadLink;

    @ApiModelProperty("流水认领记录id")
    private String  recordIdList;

    @ApiModelProperty("对接审批流审核状态 0 已审核 1 审核中 2已驳回")
    private Integer approveState;
    @ApiModelProperty(value = "业务类型编码")
    private String businessType;
    @ApiModelProperty(value = "业务类型id")
    private String businessTypeCode;
    @ApiModelProperty(value = "业务类型名称")
    private String businessTypeName;

    @ApiModelProperty(value = "审批流程id")
    private String procInstId;


    @ApiModelProperty(value = "单据备注")
    private String receiptRemark;

    @ApiModelProperty(value = "到期日期")
    private LocalDateTime gmtExpire;

    @ApiModelProperty(value = "审核通过时间")
    private LocalDateTime gmtApprove;

    @ApiModelProperty(value = "结算/确收单id")
    private String settlementId;

    @ApiModelProperty(value = "财务云口径计税方式 1一般计税 2简单计税 3不适用")
    private Integer taxType;


    private String payAppId;

    @ApiModelProperty(value = "项目id")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
    @ApiModelProperty(value = "计税方式（1.一般计税，2.简单计税）")
    private Integer calculationMethod;
}
