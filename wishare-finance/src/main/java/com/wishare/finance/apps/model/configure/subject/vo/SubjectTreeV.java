package com.wishare.finance.apps.model.configure.subject.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 科目树返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("科目树返回信息")
public class SubjectTreeV extends Tree<SubjectTreeV, Long> {

    @ApiModelProperty("科目名称")
    private String subjectName;

    @ApiModelProperty("辅助核算")
    private List<String> auxiliaryCountList;
}
