package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description:
 */
@Getter
@Setter
@ApiModel("批量设置入参")
public class BatchSettingF {

    @ApiModelProperty("科目映射规则id")
    private Long subjectMapRuleId;

    @ApiModelProperty("科目映射单元id")
    private Long subMapUnitId;

    @ApiModelProperty("一级科目id")
    private Long subjectLevelOneId;

    @ApiModelProperty("一级科目名称")
    private String subjectLevelOneName;

    @ApiModelProperty("末级科目id")
    private Long subjectLevelLastId;

    @ApiModelProperty("末级科目名称")
    private String subjectLevelLastName;

    @ApiModelProperty("映射类别： 1科目，2现金流量")
    private Integer mapType;

}
