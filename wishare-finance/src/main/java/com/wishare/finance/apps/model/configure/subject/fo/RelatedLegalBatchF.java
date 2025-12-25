package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 关联法定单位数据
 * @author yancao
 */
@Getter
@Setter
@ApiModel("批量关联法定单位数据")
public class RelatedLegalBatchF {

    @ApiModelProperty(value = "科目体系id", required = true)
    @NotNull(message = "科目体系id不能为空")
    private Long subjectSystemId;

    @ApiModelProperty(value = "法定单位id集合", required = true)
    @NotEmpty(message = "法定单位id集合不能为空")
    private List<Long> statutoryBodyIdList;

}
