package com.wishare.finance.apps.model.configure.subject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 科目返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目体系返回信息")
public class SubjectSystemV {

    @ApiModelProperty("科目体系id")
    private Long id;

    @ApiModelProperty("科目体系名称")
    private String name;

    @ApiModelProperty("科目体系编码")
    private String code;

    @ApiModelProperty("法定单位id")
    private Long pertainId;

    @ApiModelProperty("是否启用")
    private Integer disabled;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新人id")
    private String operator;

    @ApiModelProperty("更新人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("科目类型")
    private List<SubjectCategoryV> subjectCategoryList;

}
