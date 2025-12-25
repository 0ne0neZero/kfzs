package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 撤销认领返回信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("撤销认领返回信息")
public class FlowDetailRevokedV {

    @ApiModelProperty("流水明细数量")
    private Integer flowDetailSum;

    @ApiModelProperty("发票明细数量")
    private Integer invoiceDetailSum;

}
