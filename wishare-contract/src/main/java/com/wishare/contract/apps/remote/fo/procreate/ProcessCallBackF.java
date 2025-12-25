package com.wishare.contract.apps.remote.fo.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 功能解释
 *
 * @author long
 * @date 2023/7/18 10:49
 */
@Getter
@Setter
@ApiModel("流程创建回调参数")
public class ProcessCallBackF<T> {
    @ApiModelProperty(value = "回调具体实体类")
    private T t;

    @ApiModelProperty("类型（1合同订立支出2合同订立收入）")
    private Integer type;
}
