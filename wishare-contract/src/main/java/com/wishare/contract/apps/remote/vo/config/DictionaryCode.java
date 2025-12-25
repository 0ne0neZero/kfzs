package com.wishare.contract.apps.remote.vo.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lvyang
 * @date 2023/2/26 19:48
 * @Description:
 */
@Data
public class DictionaryCode {
    @ApiModelProperty("数据字典主键ID")
    private String id;

    @ApiModelProperty("数据字典值")
    private String name;

    @ApiModelProperty("数据项值")
    private String code;
}
