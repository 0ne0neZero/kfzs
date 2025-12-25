package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormItem {
    @ApiModelProperty("唯一标识ID")
    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("组件名称")
    private String name;

    @ApiModelProperty("扩展属性")
    private Props props;
}

