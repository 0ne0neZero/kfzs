package com.wishare.finance.infrastructure.remote.fo.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 数据字典列表
 * @author: pgq
 * @since: 2023/1/2 19:01
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("数据项")
public class DictionaryItemF {

    @ApiModelProperty("cfg_dictionary_type表code")
    private String dictionaryCode;

    @ApiModelProperty("数据项code")
    private String code;
}
