package com.wishare.finance.apps.model.configure.subject.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目体系树信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目体系树信息")
public class SubjectSystemTreeV extends Tree<SubjectSystemTreeV, Long> {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

}
