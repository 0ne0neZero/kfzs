package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/25 14:42
 * @descrption:
 */
@Data
@ApiModel(value = "发票附加信息返回结果")
public class AttachInfoResV {

    @ApiModelProperty(value = "版式推送状态")
    private String formatPushStatus;
}
