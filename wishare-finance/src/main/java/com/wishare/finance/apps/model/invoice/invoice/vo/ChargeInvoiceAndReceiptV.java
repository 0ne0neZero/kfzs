package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 收费发票和收据记录(用于流水认领)
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("收费发票和收据记录")
public class ChargeInvoiceAndReceiptV extends InvoiceAndReceiptV{

    @ApiModelProperty("账单开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime billStartTime;

    @ApiModelProperty("账单结束时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime billEndTime;

    @ApiModelProperty("实收金额")
    private Long settleAmount;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("项目名称")
    private String communityName;
}
