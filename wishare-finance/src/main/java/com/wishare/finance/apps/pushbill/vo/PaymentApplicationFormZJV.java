package com.wishare.finance.apps.pushbill.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value="支付申请单分页查询")
public class PaymentApplicationFormZJV {


    @ApiModelProperty("支付申请单id")
    private Long id;

    @ApiModelProperty("区域")
    private String region;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("项目名称")
    private String communityId;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同管理类别")
    private String conmanagetypename;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("结算信息")
    private String settlementInfo;

    @ApiModelProperty("结算信息列表")
    private List<SettlementInfoV> settlementInfoList;

    @ApiModelProperty(value = "供应商")
    private String oppositeOne;

    @ApiModelProperty("合同金额")
    private String contractAmountOriginalRate;

    @ApiModelProperty(value = "支付申请单编号")
    private String payApplyCode;

    @ApiModelProperty(value = "应付金额-业务信息")
    private BigDecimal totalPaymentAmount;

    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;

    @ApiModelProperty(value = "支付状态 1:未支付 2:已支付")
    private String payStatusName;

    @ApiModelProperty("单位code")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "部门名称")
    private String departName;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "期望付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expectPayDate;

    /**
     * 收款人名称(往来单位对应账户ID)
     */
    private String recipient;
    /**
     * 往来单位主数据编号BP开头
     */
    private String recipientCode;

    @ApiModelProperty("实际收款人")
    private String recipientName;

    /**
     * 收款账户id(往来单位对应账户ID)
     */
    private String account;

    /**
     * 往来单位主数据编号BP开头(账户)
     */
    private String accountCode;

    @ApiModelProperty("收款账户名称")
    private String nameOfReceivingAccount;

    @ApiModelProperty("收款银行账号")
    private String bankAccountNumber;

    @ApiModelProperty("收款开户行")
    private String openingBank;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("业务事由")
    private String businessReasons;

    @ApiModelProperty("经办人")
    private String handledBy;

    @ApiModelProperty("核算组织")
    private String org;

    @ApiModelProperty("附件张数")
    private Integer attachmentNum;

    @ApiModelProperty("附件信息")
    List<VoucherBillFileZJ> voucherBillFileS;

    @ApiModelProperty(value = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("现金流量-初审支付明细")
    private String cashFlow;

    private String cashFlowName;

    @ApiModelProperty("结算方式-初审支付明细")
    private String paymentMethod;


    @ApiModelProperty("付款账户名称-初审支付明细")
    private String nameOfPayAccount;

    @ApiModelProperty("付款银行账号-初审支付明细")
    private String payBankAccountNumber;

    @ApiModelProperty("付款开户行-初审支付明细")
    private String payOpeningBank;

    @ApiModelProperty("票据支付方式-初审支付明细")
    private String paymentMethodForBills;

    @ApiModelProperty("票据数量-初审支付明细")
    private String billsNumbers;

    @ApiModelProperty("付款详细说明-初审支付明细")
    private String payDesc;

    @ApiModelProperty("转账附言-初审支付明细")
    private String transferRemarks;

    @ApiModelProperty("备注")
    private String remarks;


    @ApiModelProperty("合同服务类型,0:其它 1:四保一服")
    private Integer contractServeType;

    @ApiModelProperty("收款银行名称")
    private String beneficiaryBank;

    @ApiModelProperty("结算方式名称")
    private String paymentMethodName;

    @ApiModelProperty("审批状态")
    private Integer approvalStatus;

    @ApiModelProperty("审批状态 审批状态 1:草稿 2:待审批 3:完成审批 ")
    private String approvalStatusName;

    @ApiModelProperty("款项明细")
    private List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS;

    @ApiModelProperty("支付明细")
    private List<PaymentApplicationZFDetailV> paymentApplicationZFDetailVS;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;


}
