package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/6 9:49
 * @version: 1.0.0
 */
@Data
@ApiModel("增值税税率")
public class RateRv {

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 名称-税率
     */
    @ApiModelProperty("名称-税率")
    private String name;

    private String pk_org;

    private String pk_group;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String state;
}
