package com.wishare.finance.apps.model.configure.subject.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目类型树信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目类型树信息")
public class SubjectCategoryTreeV extends Tree<SubjectCategoryTreeV, Long> {

    @ApiModelProperty(value = "科目体系id")
    private String pertainId;

    @ApiModelProperty(value = "科目类型名称")
    private String categoryName;

}
