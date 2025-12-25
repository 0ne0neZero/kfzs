package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("批量清单合并参数")
public class MergePayFundPidF {

    @ApiModelProperty("批量清单合并pids")
    private List<String> pids;

}
