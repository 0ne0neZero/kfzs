package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单审核记录更新")
public class BillApplyUpdateF {

    @ApiModelProperty(value = "账单审核id",required = true)
    @NotNull(message = "账单审核id不能为空")
    private Long billApproveId;

    @ApiModelProperty("外部审批标识iD")
    private String outApproveId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
