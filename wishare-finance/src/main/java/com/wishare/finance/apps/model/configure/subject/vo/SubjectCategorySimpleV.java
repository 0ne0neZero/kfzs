package com.wishare.finance.apps.model.configure.subject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目类型返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目类型返回信息")
public class SubjectCategorySimpleV {

    @ApiModelProperty(value = "科目类型id")
    private Long id;

    @ApiModelProperty(value = "科目类型名称")
    private String categoryName;

}
