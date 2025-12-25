package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillBatchApplyUpdateF
 * @date 2023.12.25  17:00
 * @description 账单批量审核记录更新
 */
@Getter
@Setter
@ApiModel("账单批量审核记录更新")
public class BillBatchApplyUpdateF {

    @ApiModelProperty(value = "账单审核id列表",required = true)
    @NotEmpty(message = "账单审核id列表不能为空")
    private List<Long> billApproveIds;

    @ApiModelProperty("外部审批标识iD")
    private String outApproveId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
