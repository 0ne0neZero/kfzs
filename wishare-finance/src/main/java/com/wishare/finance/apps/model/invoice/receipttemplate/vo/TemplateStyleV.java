package com.wishare.finance.apps.model.invoice.receipttemplate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/7 20:34
 * @descrption:
 */
@Data
@ApiModel(value = "票据模板样式")
@AllArgsConstructor
public class TemplateStyleV {

    @ApiModelProperty(value = "票据模板样式编码")
    private Integer templateStyleCode;

    @ApiModelProperty(value = "票据模板样式名称")
    private String templateStyleName;
}
