package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-09
 */
@Data
public class SettlementF {

    @ApiModelProperty("合同系统-结算单id")
    @NotEmpty(message = "结算单id不能为空")
    private String settlementId;

    @ApiModelProperty("合同系统-结算单名称")
    @NotEmpty(message = "结算单名称不能为空")
    private String settlementName;
}
