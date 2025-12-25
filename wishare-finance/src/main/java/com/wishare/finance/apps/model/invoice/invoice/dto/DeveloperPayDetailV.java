package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author szh
 * @date 2024/5/13 16:44
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperPayDetailV {
    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("单价（单位：分）")
    private String price;

    @ApiModelProperty("数量描述（计量）")
    private String numStr;

    @ApiModelProperty("代付小计")
    private String total;
}
