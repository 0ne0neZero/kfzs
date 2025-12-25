package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取银联对账文件入参")
public class GetFileRF {

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("模糊匹配名称")
    private String queryName;

    @ApiModelProperty("支付渠道")
    private String channel;
}
