package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 批量添加结果信息
 *
 * @author yancao
 */
@Setter
@Getter
@AllArgsConstructor
@ApiModel("批量添加结果信息")
public class BillAddBatchResultDto {

    @ApiModelProperty("失败信息")
    private String errMsg;

    @ApiModelProperty(value = "失败行号")
    private Long rowNumber;
}
