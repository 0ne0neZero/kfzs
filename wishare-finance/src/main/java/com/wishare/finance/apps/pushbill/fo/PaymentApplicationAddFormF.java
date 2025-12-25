package com.wishare.finance.apps.pushbill.fo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationFormPayMxV;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationKXDetailV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel("创建支付申请单")
public class PaymentApplicationAddFormF {

/**-----------------------------业务信息---------------------------------*/
    @ApiModelProperty("支付申请单id")
    private String id;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("CT码")
    @NotEmpty(message = "CT码不能为空")
    private String conmaincode;

    @ApiModelProperty("合同名称")
    @NotEmpty(message = "合同名称不能为空")
    private String contractName;

    @ApiModelProperty("合同系统-结算单信息")
    @NotNull(message = "结算单信息不能为空")
    private List<SettlementF> settlementList;

    @ApiModelProperty("期望付款日期")
    @NotNull(message = "期望付款日期不能为空")
    private LocalDate expectPayDate;

    @ApiModelProperty("支付申请单应付金额")
    private BigDecimal totalPaymentAmount;

    @ApiModelProperty("业务类型 默认：其它业务")
    @NotNull(message = "业务类型不能为空")
    private String businessType = "其它业务";

    @ApiModelProperty("业务事由")
    @NotEmpty(message = "业务事由不能为空")
    private String businessReasons;

    @ApiModelProperty("备注")
    private String remarks;

    /**-----------------------------收款信息---------------------------------*/


    @ApiModelProperty("收款人id 往来单位对应账户ID (单位)")
    private String recipient;

    @ApiModelProperty("收款人名称 (单位)")
    private String recipientName;

    @ApiModelProperty("往来单位主数据编号BP开头(单位)")
    private String recipientCode;


    @ApiModelProperty("收款账户id(往来单位对应账户ID)(账户)")
    private String account;

    @ApiModelProperty("往来单位主数据编号BP开头(账户)")
    private String accountCode;

    @ApiModelProperty("收款账户名称(账户)")
    private String nameOfReceivingAccount;

    @ApiModelProperty("银行账号")
    private String bankAccountNumber;

    @ApiModelProperty("开户行")
    private String openingBank;


    @ApiModelProperty("收款银行名称")
    private String beneficiaryBank;

    /**-----------------------------基本信息---------------------------------*/

    @ApiModelProperty("单位code")
    private String unitCode;
    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门名称")
    private String departName;

    @ApiModelProperty("单据日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime billDate;

    @ApiModelProperty("核算组织")
    private String org;

    @ApiModelProperty("附件张数")
    private Integer attachmentNum;

    @ApiModelProperty("审批状态")
    private Integer approvalStatus;

    @ApiModelProperty("经办人")
    private String handledBy;

    /**-------------------------------------------------------款项明细*/
    @ApiModelProperty("款项明细")
    private List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS;

    @ApiModelProperty("附件信息")
    private UpLoadFileF upLoadFileF;

    @ApiModelProperty("1:草稿 2:提交财务初审")
    @NotNull(message = "提交类型不能为空")
    private Integer submitType;


    @ApiModelProperty("支付明细")
    private List<PaymentApplicationFormPayMxV> paymentApplicationFormPayMxVS;

    /**-----收款账单-退款时使用-------*/
    @ApiModelProperty(" 项目id")
    private String communityId;
    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty("区域")
    private String region;


    @ApiModelProperty("推送部门code")
    private String externalDepartmentCode;


}
