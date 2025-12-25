package com.wishare.finance.infrastructure.remote.vo.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 数据项
 * @author: pgq
 * @since: 2023/1/2 19:05
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("数据项")
public class DictionaryItemRV {

    @ApiModelProperty("数据项code")
    private String code;

    @ApiModelProperty("cfg_dictionary_type表code")
    private String dictionaryCode;

    @ApiModelProperty("是否禁用 true 禁用 false 开启")
    private boolean disabled;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据项级别")
    private Integer level;

    @ApiModelProperty("数据项名称")
    private String name;

    @ApiModelProperty("数据用途描述")
    private String remarks;

    @ApiModelProperty("数据项父级ID")
    private String parentId;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("数据项排序")
    private Integer sort;
}
