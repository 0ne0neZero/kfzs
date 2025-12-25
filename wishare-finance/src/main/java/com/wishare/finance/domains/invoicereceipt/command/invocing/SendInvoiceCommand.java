package com.wishare.finance.domains.invoicereceipt.command.invocing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 发送票据信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/27
 */
@Setter
@Getter
public class SendInvoiceCommand {

    @ApiModelProperty(value = "收款单id")
    @NotNull(message = "收款单id不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "账单类型: 1应收账单,2预收账单, 3临时收费账单, 4应付账单, 5退款账单, 6付款单, 7收款单")
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushModes;

    @ApiModelProperty(value = "购方手机（pushMode为1或2时，此项为必填）")
    @Pattern(regexp = "1[3-9]\\d{9}")
    private String buyerPhone;

    @ApiModelProperty(value = "推送邮箱（pushMode为0或2时，此项为必填）")
    @Pattern(regexp = "\\w{1,30}@[a-zA-Z0-9]{2,20}(\\.[a-zA-Z0-9]{2,20}){1,2}")
    private String email;

}
