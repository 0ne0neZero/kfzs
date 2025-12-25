package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 交账结果数据
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/13
 */
@Setter
@Getter
@Accessors(chain = true)
@ApiModel("交账结果数据")
public class HandAccountDto {

    @ApiModelProperty(value = "交账结果： true: 成功 false: 失败", required = true)
    private Boolean success;

    @ApiModelProperty(value = "失败类型： 0少收， 1多收")
    private Integer errType;

    @ApiModelProperty(value = "差额")
    private Long differenceAmount;

}
