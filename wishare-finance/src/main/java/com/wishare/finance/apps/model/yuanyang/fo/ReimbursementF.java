package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.PayeeF;
import com.wishare.finance.apps.model.bill.fo.PayerF;
import com.wishare.finance.apps.model.bill.fo.SceneF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报销流程入参
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("报销流程入参")
public class ReimbursementF {

    @ApiModelProperty(value = "业务系统交易单号", required = true)
    @Length(max = 64, message = "业务系统交易单号格式不正确")
    private String bizTransactionNo;

    @ApiModelProperty(value = "商户订单标题", required = true)
    
    private String transactionTitle;

    @ApiModelProperty(value = "收款人信息")
    @Valid
    private PayeeF payee;

    @ApiModelProperty(value = "付款人信息")
    @Valid
    private PayerF payer;

    @ApiModelProperty(value = "支付场景信息")
    @Valid
    private SceneF scene;

    @ApiModelProperty(value = "请求超时时间 格式：yyyy-MM-dd HH:mm:ss，默认为15分钟")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "支付渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CBS: 招商银企直连，OTHER: 其他）",required = true)
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "支付方式(0线上，1线下)， 默认：线上",required = true)
    private Integer payWay = 0;

    @ApiModelProperty(value = "支付扩展信息，支付通知原样返回")
    @Length(max = 256, message = "支付扩展信息格式不正确")
    private String attachParam;

    @ApiModelProperty(value = "消息通知地址")
    @Length(max = 256, message = "消息通知地址格式不正确")
    private String notifyUrl;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "法定单位编码")
    //@NotBlank(message = "法定单位编码不能为空")
    private String statutoryBodyCode;

    @ApiModelProperty(value = "报销金额", required = true)
    @NotNull(message = "报销金额不能为空")
    private Long reimburseAmount;

    @ApiModelProperty(value = "应付金额", required = true)
    @NotNull(message = "应付金额不能为空")
    private Long payableAmount;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "推凭标识：0不推凭，1推凭")
    private Integer voucherFlag;

    @ApiModelProperty(value = "账簿编码", required = true)
    @NotBlank(message = "账簿编码不能为空")
    private String accountBookCode;

    @ApiModelProperty(value = "账单归属年", required = true)
    @NotNull(message = "账单归属年不能为空")
    private Integer accountYear;

    @ApiModelProperty(value = "账单归属月", required = true)
    @NotNull(message = "账单归属月不能为空")
    private LocalDate accountDate;

    @ApiModelProperty(value = "账单开始时间", required = true)
    @NotNull(message = "账单开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间", required = true)
    @NotNull(message = "账单结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "申请人id")
    @NotNull(message = "申请人id不能为空")
    private String applicantId;

    @ApiModelProperty(value = "申请人名称")
    @NotNull(message = "申请人名称不能为空")
    private String applicantName;

    @ApiModelProperty(value = "制单人唯一标识，推凭必传，NCC系统人员信息保持一致", required = true)
    @NotBlank(message = "制单人唯一标识不能为空")
    private String makerId;

    @ApiModelProperty(value = "制单人名称")
    @NotBlank(message = "制单人名称不能为空")
    private String makerName;

    @ApiModelProperty(value = "报销明细", required = true)
    @NotNull(message = "报销明细不能为空")
    @Size(min = 1, max = 200, message = "报销明细数量仅允许1-200条")
    @Valid
    private List<ReimbursementDetailF> reimbursementDetails;

    @ApiModelProperty(value = "发票明细", required = true)
    //@NotNull(message = "发票明细不能为空")
    //@Size(min = 1, max = 200, message = "发票明细数量仅允许1-200条")
    @Valid
    private List<ReimbursementInvoiceF> invoices;

    @ApiModelProperty(value = "报销进项税额明细")
    @Size(max = 200, message = "报销进项税额明细数量仅允许1-200条")
    @Valid
    private List<ReimbursementInputTaxF> inputTaxDetails;

    @ApiModelProperty(value = "是否分摊")
    private boolean share;

    @ApiModelProperty(value = "备用金明细", required = true)
    @Valid
    private ReimbursementPettyCashF pettyCash;

    @ApiModelProperty(value = "员工报销信息", required = true)
    @Valid
    @NotNull(message = "员工报销信息不能为空")
    private ReimbursementPersonF reimbursePerson;

    @ApiModelProperty(value = "银行存款信息", required = true)
    @Valid
    @NotNull(message = "银行存款信息不能为空")
    private ReimbursementBankF reimburseBank;

}
