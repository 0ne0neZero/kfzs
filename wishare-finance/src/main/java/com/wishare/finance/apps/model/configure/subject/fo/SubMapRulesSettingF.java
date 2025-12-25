package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2023/1/6
 * @Description:
 */
@Getter
@Setter
@ApiModel("科目映射批量设置入参")
public class SubMapRulesSettingF {

    @ApiModelProperty("科目映射单元列表")
    private List<Long> subMapUnitIds;

    @ApiModelProperty("科目映射单元对象数组")
    private List<BatchSettingF> batchSettingFList;

}
