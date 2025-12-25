package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description:
 */
@Getter
@Setter
@ApiModel("根据条件查询票本列表入参")
public class InvoiceBookListF {

    @ApiModelProperty("票本编号")
    private String invoiceBookNumber;

    @ApiModelProperty("票本状态：1.未领用 2.部分领用 3.全部领用")
    private List<Integer> states;
}
