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
@ApiModel("科目详情")
public class SubjectDetailV {

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

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("父科目id")
    private Long parentId;

    @ApiModelProperty("科目路径")
    private List<Long> subjectPath;

    @ApiModelProperty("科目体系id")
    private Long subjectSystemId;

    @ApiModelProperty("是否叶子节点（0否，1是）")
    private Integer leaf;

    @ApiModelProperty("是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty("辅助核算")
    private List<String> auxiliaryCountList;

    @ApiModelProperty("现金类别： 0无，1现金，2银行，3现金等价物")
    private Integer cashType;

    @ApiModelProperty("现金现金流量值")
    private List<SubjectCashFlowV> cashFlows;

    @ApiModelProperty("是否税费科目 0否 1是 默认0")
    private Integer existTax;

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

}
