package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/12/3 14:38
 * @version: 1.0.0
 */
@ApiModel("返回结果")
@Getter
@Setter
public class Result {

    /**
     * 001成功 002失败
     */
    @ApiModelProperty("001成功 002失败")
    private String code;

    /**
     * 数据主键
     */
    @ApiModelProperty("主键")
    private String pkCode;

    /**
     * 信息
     */
    @ApiModelProperty("信息")
    private String msg;
}
