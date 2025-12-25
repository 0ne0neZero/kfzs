package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Hello
 */
@Getter
@Setter
@ApiModel("开票收款单信息")
public class InvoiceGatherBillListV {

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;

    @ApiModelProperty(value = "房号")
    private String cpUnitName;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "开票金额（单位：元）")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "账单开始日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private LocalDateTime endTime;

    @ApiModelProperty("收款明细")
    protected List<InvoiceGatherBillDetailListV> gatherDetails;

}
