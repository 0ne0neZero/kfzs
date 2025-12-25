package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/5/8/10:20
 */
@Data
@ApiModel(value = "请求响应信息")
public class EsbRetrunInfoV {

    /**
     * 0：该票据已存在
     * 1：该票据不存在
     * 2：发票号码未填写或未传入发票号码列
     */
    @ApiModelProperty(value = "返回编号")
    private String code;

    /**
     * 0：该票据已存在
     * 1：该票据不存在
     * 2：发票号码未填写或未传入发票号码列
     */
    @ApiModelProperty(value = "返回信息")
    private String message;
}
