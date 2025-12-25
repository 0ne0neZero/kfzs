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
@ApiModel("获取科目映射规则列表")
public class ListSubjectMapRulesF {

    @ApiModelProperty("科目映射规则名称")
    private String subMapName;
}
