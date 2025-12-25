package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 合同发票和收据记录(用于流水认领)
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("合同发票和收据记录")
public class ContractInvoiceAndReceiptV extends InvoiceAndReceiptV{

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

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("成本中心")
    private String costCenterName;
}
