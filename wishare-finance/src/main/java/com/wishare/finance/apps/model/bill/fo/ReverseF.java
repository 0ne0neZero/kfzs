package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/10/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单冲销")
public class ReverseF {

    @ApiModelProperty(value = "账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty("冲销时如果需要生成新的应收传入：ReversedInitBill）")
    private String extField1;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;
}
