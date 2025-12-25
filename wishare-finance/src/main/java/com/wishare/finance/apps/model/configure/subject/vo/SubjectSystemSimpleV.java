package com.wishare.finance.apps.model.configure.subject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目体系简单返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目体系简单返回信息")
public class SubjectSystemSimpleV {

    @ApiModelProperty("科目体系id")
    private Long id;

    @ApiModelProperty("科目体系名称")
    private String name;

    @ApiModelProperty("科目体系编码")
    private String code;

    @ApiModelProperty("是否启用")
    private Integer disabled;

}
