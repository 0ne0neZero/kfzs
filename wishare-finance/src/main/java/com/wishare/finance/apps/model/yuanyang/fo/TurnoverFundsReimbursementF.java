package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.PayeeF;
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
 * 资金上缴流程入参
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("资金上缴流程入参")
public class TurnoverFundsReimbursementF {

    @ApiModelProperty(value = "业务系统交易单号", required = true)
    @Length(max = 64, message = "业务系统交易单号格式不正确")
    private String bizTransactionNo;

    @ApiModelProperty(value = "商户订单标题", required = true)
    
    private String transactionTitle;

    @ApiModelProperty(value = "资金收款人信息")
    @Valid
    private PayeeF payee;

    @ApiModelProperty(value = "结算中心编码", required = true)
    @NotBlank(message = "结算中心编码不能为空")
    private String settleCostCode;

    @ApiModelProperty(value = "结算中心名称", required = true)
    @NotBlank(message = "结算中心名称不能为空")
    private String settleCostName;

    @ApiModelProperty(value = "结算中心账簿编码", required = true)
    @NotBlank(message = "结算中心账簿编码不能为空")
    private String settleBookCode;

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

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "账单归属年", required = true)
    @NotNull(message = "账单归属年不能为空")
    private Integer accountYear;

    @ApiModelProperty(value = "账单归属月", required = true)
    @NotNull(message = "账单归属月不能为空")
    private LocalDate accountDate;

    @ApiModelProperty(value = "资金审核人唯一标识，NCC系统人员信息保持一致", required = true)
    @NotBlank(message = "资金审核人唯一标识不能为空")
    private String cashReviewerId;

    @ApiModelProperty(value = "资金审核人名称")
    @NotBlank(message = "资金审核人名称不能为空")
    private String cashReviewerName;

    @ApiModelProperty(value = "资金上缴明细", required = true)
    @NotNull(message = "资金上缴明细不能为空")
    @Size(min = 1, max = 200, message = "资金上缴明细数量仅允许1-200条")
    @Valid
    private List<TurnoverDetail> turnoverDetails;

}
