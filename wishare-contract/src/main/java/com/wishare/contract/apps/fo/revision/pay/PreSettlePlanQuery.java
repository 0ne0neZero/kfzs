package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class PreSettlePlanQuery {

    @ApiModelProperty(value = "合同id")
    @NotNull(message = "合同id不可为空")
    private String contractId;

    @ApiModelProperty("拆分方式")
    @NotNull(message = "拆分方式不可为空")
    private Integer splitMode;

    @ApiModelProperty(hidden = true)
    private Boolean edit = false;

    @ApiModelProperty(value = "已经存在的合同清单",hidden = true)
    private List<String> existContractPayFundIdList;

}
