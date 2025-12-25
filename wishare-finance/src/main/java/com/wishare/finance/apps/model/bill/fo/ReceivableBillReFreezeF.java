package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 应收账单结转入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("应收账单结转入参")
public class ReceivableBillReFreezeF {

    @ApiModelProperty("冻结记录id")
    private Long id;

    @ApiModelProperty("账单编号列表")
    private List<String> billNos;

    @ApiModelProperty("上级收费单元ID")
    private String supCpUnitId;
}
