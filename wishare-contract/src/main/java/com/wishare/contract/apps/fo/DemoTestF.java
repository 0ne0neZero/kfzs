package com.wishare.contract.apps.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Author 永遇乐 yeoman<76164451@.qq.com>
 * @line --------------------------------
 * @Date 2022/04/21
 */
@ApiModel
@Getter
@Setter
public class DemoTestF {
    @ApiModelProperty(value = "文本值", required = true)
    @NotBlank(message = "文本值不可为空")
    private String text;
    @ApiModelProperty(hidden = true)
    protected String creator;
    @ApiModelProperty(hidden = true)
    protected String operator;
}
