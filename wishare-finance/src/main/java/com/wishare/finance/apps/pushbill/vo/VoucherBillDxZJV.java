package com.wishare.finance.apps.pushbill.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel(value="中交汇总单据信息-对下结算单")
public class VoucherBillDxZJV extends BaseEntity {
    @ApiModelProperty(value = "汇总单据id")
    private Long id;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    @ApiModelProperty(value = "报账类型")
    private String ruleName;
    @ApiModelProperty(value = "推送状态  1待推送 2已推送 3 推送失败 4 已驳回 5推送中")
    private Integer pushState;
    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;
    @ApiModelProperty(value = "【基本信息-报账单号】")
    private String voucherBillNo;
    @ApiModelProperty(value = "业务单元code")
    private String businessUnitCode;
    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;
    @ApiModelProperty(value = "错误信息")
    private String remark;
    @ApiModelProperty(value = "报账总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "推送方式枚举(1手动推送、2按日推送)")
    private Integer pushMethod;
    @ApiModelProperty(value = "是否删除：0否，1是")
    private Integer deleted;
    @ApiModelProperty(value = "[基本信息-创建日期】")
    private LocalDateTime gmtCreate;
    @ApiModelProperty(value = "[计量信息-期望凭证日期】")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private LocalDateTime expectedVoucherDate;
    @ApiModelProperty(value = "单据类型 1 收入确认")
    private Integer billEventType;
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
    @ApiModelProperty(value = "项目Id")
    private String communityId;
    @ApiModelProperty(value = "流转状态  1成功  2 失败 3流转中")
    private Integer wanderAboutState;
    @ApiModelProperty(value = "财务云单据id")
    private String financeId;
    @ApiModelProperty(value = "财务云单据编号【基本信息-单据编号】")
    private String financeNo;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;
    @ApiModelProperty(value = "推单明细")
    private List<VoucherBillZJDetailV> voucherBillDetailVS;
    @ApiModelProperty(value = "是否上传附件  0 是 1否")
    private Integer upload;
    @ApiModelProperty(value = "附件上传链接")
    private List<UploadLinkZJ> uploadLink;
    @ApiModelProperty("实际付款人")
    private String payer = "其他";
    @ApiModelProperty("银行账户")
    private String bankAccount;
    @ApiModelProperty("业务事由")
    private String businessReasons;
    @ApiModelProperty("【基本信息-业务类型】")
    private String businessType = "服务类业务";

    @ApiModelProperty(value = "业务类型id")
    private String businessTypeCode;
    @ApiModelProperty(value = "业务类型名称【计量信息-业务类型】")
    private String businessTypeName;

    @ApiModelProperty("供应商名称 债权人")
    private String supplier;
    @ApiModelProperty(value = "合同Id")
    private String contractId;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("合同编号")
    private String contractName;
    @ApiModelProperty("合同收款人")
    private String recipientName;

    @ApiModelProperty(value = "期望付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expectPayDate;

    @ApiModelProperty(value = "应付金额-业务信息")
    private BigDecimal totalPaymentAmount;

    @ApiModelProperty("计税方式")
    private String taxType = "一般计税";

    @ApiModelProperty("【基本信息-单位名称】")
    private String unitName;

    @ApiModelProperty("【基本信息-单位编码】")
    private String unitCode;

    @ApiModelProperty("【基本信息、成本明细-部门名称】")
    private String departmentName;

    @ApiModelProperty("【基本信息-部门编码】")
    private String departmentCode;

    @ApiModelProperty("基本信息-到期日期】逻辑内容-合同结束日期+一个顺推结算周期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime expireNextEndDate;

    @ApiModelProperty("核算组织名称")
    private String organizationName;

    @ApiModelProperty(value = "审批流程id")
    private String procInstId;

    @ApiModelProperty("对接审批流审核状态 0 已审核 1 审核中 2已驳回")
    private Integer approveState;

    @ApiModelProperty(value = "单据备注 【计量信息-业务事由】")
    private String receiptRemark;

    @ApiModelProperty(value = "是否显示重新推送按钮")
    private Boolean showPushAgainBtn;

    @ApiModelProperty(value = "报账单创建人-【基本信息-经办人】")
    private String creatorName;

    @ApiModelProperty(value = "【基本信息-来源系统】")
    private String sourceSystem;

    @ApiModelProperty(value = "审核通过时间【基础信息-单据日期】")
    private LocalDateTime gmtApprove;

    @ApiModelProperty(value = "【计量信息-是否签认】0:否 1:是，默认为：是")
    private Integer isSign = 0;

    @ApiModelProperty(value = "【计量信息-合同编号】")
    private String conmainCode;

    @ApiModelProperty("对方单位id [计量信息-债权人Id]")
    private String oppositeOneId;

    @ApiModelProperty("对方单位名称 [计量信息-债权人]")
    private String oppositeOne;

    @ApiModelProperty("V2.12-结算类型 0中期结算 1最终结算 [计量信息-结算类型]")
    private Integer settlementType;

    @ApiModelProperty("本位币币种编码")
    private String BWBBZ = "156";

    @ApiModelProperty("本位币币种名称 [计量信息-本位币币种]")
    private String BWBBZName = "CNY-人民币";

    @ApiModelProperty(" [计量信息-附件张数]")
    private Integer fileSize;

    @ApiModelProperty("项目id")
    private String projectId;

    @ApiModelProperty("【计量信息-项目名称】")
    private String projectName;

    @ApiModelProperty("【计量信息-纳税人类型】：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关")
    private Integer taxpayerType;

    @ApiModelProperty("【计量信息-结算单号】")
    private String payFundNumber;

    @ApiModelProperty("【计量信息-结算开始时间】")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date settleStartDate;

    @ApiModelProperty("【计量信息-结算截止日期】")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date settleEndDate;

    @ApiModelProperty(value = "[计量信息-本期结算含税金额]")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty(value = "[计量信息-累计结算含税金额]")
    private BigDecimal totalSettlementAmount;

    @ApiModelProperty("【收款信息-实际收款人】【合同收款人】")
    private String truepayee;

    @ApiModelProperty("实际收款人编码")
    private String truepayeeid;

    @ApiModelProperty("实际收款人账户-开户行【收款信息-开户行】")
    private String truepayeeaccounbank;

    @ApiModelProperty("实际收款人账户-账户id ")
    private String truepayeeaccountid;

    @ApiModelProperty("实际收款人账户-账户名称  【收款信息-收款账户名称】")
    private String truepayeeaccounname;

    @ApiModelProperty("实际收款人账户-账号   【收款信息-收款银行账号】")
    private String truepayeeaccounnumber;

    @ApiModelProperty("结算审批单、确收审批单Id")
    private String settleId;

    @ApiModelProperty("支付申请单Id")
    private String payAppId;
    @ApiModelProperty(value = "计税方式（1.一般计税，2.简单计税）")
    private Integer calculationMethod;

}
