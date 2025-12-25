package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("批量删除结果信息")
public class BillDeleteBatchResultDto {

    @ApiModelProperty("成功数量")
    private int successCount = 0;

    @ApiModelProperty(value = "失败数量")
    private int failCount = 0;
}
