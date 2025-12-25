package com.wishare.finance.apps.model.configure.subject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目简单返回信息")
public class SubjectSimpleV {

    @ApiModelProperty("科目id")
    private Long id;

    @ApiModelProperty("科目编码")
    private String subjectCode;

    @ApiModelProperty("科目名称")
    private String subjectName;

    @ApiModelProperty("科目类型id")
    private Long categoryId;

    @ApiModelProperty("科目类型")
    private String categoryName;

}
