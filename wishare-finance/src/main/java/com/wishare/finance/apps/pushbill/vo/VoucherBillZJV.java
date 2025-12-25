package com.wishare.finance.apps.pushbill.vo;


import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value="中交汇总单据信息")
public class VoucherBillZJV {
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
    @ApiModelProperty(value = "是报账单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "业务单元code")
    private String businessUnitCode;
    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;
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
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty(value = "单据类型 1 收入确认")
    private Integer billEventType;
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
    @ApiModelProperty(value = "流转状态  1成功  2 失败 3流转中")
    private Integer wanderAboutState;
    @ApiModelProperty(value = "财务云单据id")
    private String financeId;
    @ApiModelProperty(value = "财务云单据编号")
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
    @ApiModelProperty("业务类型")
    private String businessType = "服务类业务";

    @ApiModelProperty("供应商名称 债权人")
    private String supplier;
    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("是否签约 0 否 1 是 默认1")
    private String sign = "1";

    @ApiModelProperty("计税方式")
    private String taxType = "一般计税";

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("核算组织名称")
    private String organizationName;

    @ApiModelProperty(value = "审批流程id")
    private String procInstId;

    @ApiModelProperty("对接审批流审核状态 0 已审核 1 审核中 2已驳回")
    private Integer approveState;

    @ApiModelProperty(value = "单据备注")
    private String receiptRemark;

    @ApiModelProperty(value = "到期日期")
    private LocalDateTime gmtExpire;

    @ApiModelProperty(value = "是否显示重新推送按钮")
    private Boolean showPushAgainBtn;

    @ApiModelProperty(value = "是否含有影像附件")
    private Boolean isHaveFile;

    private String payAppId;

    private Boolean isShowApprovel = Boolean.FALSE;
    //是否展示上传按钮
    private Boolean isShowUpload ;

    @ApiModelProperty(value = "项目id")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
    @ApiModelProperty(value = "影像附件列表")
    private List<VoucherBillZJFileSV> fileList;
    //计税方式（1.一般计税，2.简单计税）
    private Integer calculationMethod;
}
