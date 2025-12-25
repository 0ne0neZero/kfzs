package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 关联法定单位数据
 * @author yancao
 */
@Getter
@Setter
@ApiModel("关联法定单位数据")
public class RelatedLegalF {

    @ApiModelProperty(value = "科目体系id", required = true)
    @NotNull(message = "科目体系id不能为空")
    private Long subjectSystemId;

    @ApiModelProperty(value = "法定单位id", required = true)
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;


}
