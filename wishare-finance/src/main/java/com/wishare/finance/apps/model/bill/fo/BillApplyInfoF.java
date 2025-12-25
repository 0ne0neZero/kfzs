package com.wishare.finance.apps.model.bill.fo;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillApplyInfoF
 * @date 2024.04.08  17:34
 * @description 账单审核记录查询
 */
@Getter
@Setter
@ApiModel("账单审核记录查询")
public class BillApplyInfoF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "审核状态")
    private List<Integer> approveStatus;

}
