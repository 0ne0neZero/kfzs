package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("一级科目")
public class SubjectLevelJson {

    @ApiModelProperty("科目id")
    private Long subjectId;

    @ApiModelProperty("科目名称")
    private String subjectName;

    @ApiModelProperty("现金流量项目类型列表 1现金流入，2=现金流出")
    private String itemType;

    @ApiModelProperty("现金流量编码")
    private String code;

    @ApiModelProperty("现金流量名称")
    private String cashFlowName;

    @ApiModelProperty("现金流量ID")
    private Long cashFlowId;

}
