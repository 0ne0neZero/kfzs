package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 流水撤销认领参数
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("流水撤销认领参数")
public class RevokedFlowF {

    @ApiModelProperty("认领记录id集合")
    @NotEmpty(message = "认领记录id集合不能为空")
    private List<Long> flowClaimIdList;

    public RevokedFlowF() {
    }

    public RevokedFlowF(List<Long> flowClaimIdList) {
        this.flowClaimIdList = flowClaimIdList;
    }
}
