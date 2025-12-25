package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 应收账单结转入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("应收账单结转入参")
public class ReceivableBillFreezeF{
    @ApiModelProperty("账单ID列表")
    @NotEmpty(message = "账单ID列表不能为空!")
    private List<Long> billIds;

    @ApiModelProperty("上级收费单元ID")
    private String supCpUnitId;
}
