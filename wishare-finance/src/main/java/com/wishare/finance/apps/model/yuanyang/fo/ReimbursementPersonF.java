package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工报销信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
public class ReimbursementPersonF {

    @ApiModelProperty(value = "辅助核算项明细, 凭证NCC推凭需要，涉及到的科目的辅助核算必传", required = true)
    @Size(min = 1, max = 200, message = "辅助核算项数量仅允许1-200条")
    @Valid
    private List<ReimbursementAssisteItemF> assisteItems;

}
