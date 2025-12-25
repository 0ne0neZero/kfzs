package com.wishare.finance.apps.model.configure.subject.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 * @author dongpeng
 * @since 2023-7-27 10:30:09
 */
@Getter
@Setter
@TableName("科目映射规则明细表")
public class SubjectMapUnitDetailV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("科目映射规则id")
    private Long subMapRuleId;

    @ApiModelProperty("映射单元类型（1 费项 2 辅助核算）")
    private Integer subMapType;

    @ApiModelProperty("映射类别： 1科目，2现金流量")
    private Integer mapType;

    @ApiModelProperty("映射单元id")
    private Long subMapUnitId;

    @ApiModelProperty("一级科目id")
    private Long subjectLevelOneId;

    @ApiModelProperty("一级科目名称")
    private String subjectLevelOneName;

    @ApiModelProperty("末级科目id")
    private Long subjectLevelLastId;

    @ApiModelProperty("末级科目名称")
    private String subjectLevelLastName;


}

