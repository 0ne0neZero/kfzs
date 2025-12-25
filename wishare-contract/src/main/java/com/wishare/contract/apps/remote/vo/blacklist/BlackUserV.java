package com.wishare.contract.apps.remote.vo.blacklist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 功能解释
 *
 * @author long
 * @date 2023/8/2 14:43
 */
@Data
public class BlackUserV {
    /**
     * 成功则为0 失败则为0
     */
    @ApiModelProperty("响应码")
    private String code;

    /**
     * 提示信息
     */
    @ApiModelProperty("提示信息")
    private String message;

    /**
     * 该用户是否是黑名单用户
     */
    @ApiModelProperty("是否是黑名单用户")
    private boolean isBlack;
}
