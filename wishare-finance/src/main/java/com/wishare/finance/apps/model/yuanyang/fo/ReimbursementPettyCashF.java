package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 备用金明细
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("备用金明细")
public class ReimbursementPettyCashF {

    @ApiModelProperty(value = "备用金现金还款金额（单位：分）")
    @NotNull(message = "备用金金额不能为空")
    private Long amount;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "辅助核算项明细, 凭证NCC推凭需要，涉及到的科目的辅助核算必传", required = true)
    @Size(min = 1, max = 200, message = "辅助核算项数量仅允许1-200条")
    @NotNull(message = "辅助核算不能为空")
    @Valid
    private List<ReimbursementAssisteItemF> assisteItems;

}
