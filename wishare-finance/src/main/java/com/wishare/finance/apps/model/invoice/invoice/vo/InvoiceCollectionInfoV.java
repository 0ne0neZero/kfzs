package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收票信息
 * @author dxclay
 * @since  2023/2/21
 * @version 1.0
 */
@Getter
@Setter
@ApiModel("收票信息")
public class InvoiceCollectionInfoV {

    @ApiModelProperty(value = "收票id", required = true)
    private String collectId;

    @ApiModelProperty(value = "收票状态: 1收票中, 2收票成功, 3收票失败", required = true)
    private Integer state;

    @ApiModelProperty(value = "收票详情信息")
    private List<InvoiceCollectionDetailV> data;

}

