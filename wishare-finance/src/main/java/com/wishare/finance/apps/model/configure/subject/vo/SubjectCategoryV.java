package com.wishare.finance.apps.model.configure.subject.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 科目类型返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目类型返回信息")
public class SubjectCategoryV {

    @ApiModelProperty("科目类型id")
    private Long id;

    @ApiModelProperty("科目类型编码")
    private String categoryCode;

    @ApiModelProperty("科目类型名称")
    private String categoryName;

    @ApiModelProperty("层级")
    private String level;

    @ApiModelProperty("父科目类型id")
    private Long parentId;

    @ApiModelProperty("体系id")
    private Long pertainId;

    @ApiModelProperty("是否叶子节点（0否，1是）")
    private Integer leaf;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新人id")
    private String operator;

    @ApiModelProperty("更新人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
