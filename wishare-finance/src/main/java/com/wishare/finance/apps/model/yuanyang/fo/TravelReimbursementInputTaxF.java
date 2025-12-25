package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 差旅费报销进项税额明细
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("差旅费报销进项税额明细")
public class TravelReimbursementInputTaxF {

    @ApiModelProperty(value = "税率说明", required = true)
    @NotNull(message = "税率说明不能为空")
    private String taxRateDes;

    @ApiModelProperty(value = "价税合计金额（单位：分）")
    @NotNull(message = "价税合计金额不能为空")
    private Long taxPriceAmount;

    @ApiModelProperty(value = "税后金额（单位：分）")
    private Long excTaxAmount;

    @ApiModelProperty(value = "税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "税率, 格式为数值+ '%'， 例： 13%")
    @NotBlank(message = "税率不能为空")
    private String taxRate;

}
