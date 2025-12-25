package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title GatherApplyUpdateF
 * @date 2023.11.18  13:37
 * @description 收款单审核失败更新入参
 */
@Getter
@Setter
@ApiModel("收款单审核失败更新入参")
public class GatherApplyUpdateF {

    @ApiModelProperty("收款单ID列表")
    @NotNull(message = "收款单ID不能为空")
    private List<Long> gatherBillIds;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("审批记录ID")
    private Long applyId;

    @ApiModelProperty("操作类型")
    private Integer oprType;
}
