package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/11/4
 * @Description:
 */
@Getter
@Setter
@ApiModel("批量结算")
public class BatchSettleF {

    @ApiModelProperty("账单ids")
    private List<Long> billIds;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "付款方id",required = true)
    @NotBlank(message = "付款方id不能为空")
    private String payerId;

    @ApiModelProperty(value = "付款方名称",required = true)
    @NotBlank(message = "付款方名称不能为空")
    private String payerName;

    /*@ApiModelProperty(value = "开户行名称",required = true)
    @NotBlank(message = "开户行名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "开户行账号",required = true)
    @NotBlank(message = "开户行账号不能为空")
    private String bankAccount;
*/
    @ApiModelProperty(value = "结算金额（单位：分）",required = true)
    @NotNull(message = "结算金额不能为空")
    private Long settleAmount;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）",required = true)
    @NotBlank(message = "结算渠道不能为空")
    private String settleChannel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;
}
