package com.wishare.finance.apps.model.invoice.receipttemplate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/7 20:05
 * @descrption:
 */
@Data
@ApiModel(value = "票据模板类型")
@AllArgsConstructor
public class TemplateTypeV {

    @ApiModelProperty(value = "模板类型编码")
    private Integer templateTypeCode;

    @ApiModelProperty(value = "模板类型名称")
    private String templateTypeName;
}
