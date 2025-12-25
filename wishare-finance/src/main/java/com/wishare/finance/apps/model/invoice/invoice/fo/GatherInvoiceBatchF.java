package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dp
 * @date 2024/1/3
 * @Description:
 */
@Getter
@Setter
@ApiModel("收款明细批量开票")
public class GatherInvoiceBatchF {

    @ApiModelProperty("收款单id")
    private Long gatherBillId;

    @ApiModelProperty("收款单号")
    private String gatherBillNo;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty("收款单明细id")
    private Long gatherDetailBillId;

    @ApiModelProperty("是否免税：0不免税，1免税， 默认不免税")
    private Integer freeTax;

    @ApiModelProperty("价税合计（开票金额含税）")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "付款人id")
    private String payerId;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

}
