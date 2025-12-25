package com.wishare.finance.apps.model.configure.subject.vo;

import com.wishare.finance.apps.model.configure.subject.fo.SubjectLevelJson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description:
 */
@Getter
@Setter
@ApiModel("科目映射单元设置分页反参")
public class SubjectMapUnitDetailPageV {

    @ApiModelProperty("映射单元id")
    private Long subMapUnitId;

    @ApiModelProperty("二级费项")
    private String subMapUnitName;

    @ApiModelProperty("二级费项编码")
    private String subMapUnitCode;

    @ApiModelProperty("一级费项")
    private String subMapUnitParentName;

    @ApiModelProperty("二级科目键值对")
    private Map<Long, SubjectLevelJson> subjectLevelLastMap;


}
