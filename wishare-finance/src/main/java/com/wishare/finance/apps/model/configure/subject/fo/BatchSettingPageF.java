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
@ApiModel("批量设置分页查询")
public class BatchSettingPageF {

    @ApiModelProperty("科目映射规则id")
    private Long subjectMapRuleId;
}
