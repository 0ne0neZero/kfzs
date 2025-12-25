package com.wishare.finance.apps.model.configure.subject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目体系关联法定单位返回值
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目体系关联法定单位返回值")
public class SubjectSystemRelatedV {

    @ApiModelProperty(value = "科目体系id")
    private Long subjectSystemId;

    @ApiModelProperty("法定单位id")
    private Long id;

    @ApiModelProperty("关联id")
    private Long relatedId;

    @ApiModelProperty("法定单位名称")
    private String nameCn;

    @ApiModelProperty("法定单位编码")
    private String code;

    @ApiModelProperty(value = "是否关联：0未关联，1已关联")
    private Integer related;
}
