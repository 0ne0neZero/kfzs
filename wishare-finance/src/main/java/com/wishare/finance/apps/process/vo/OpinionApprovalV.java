package com.wishare.finance.apps.process.vo;


import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalReturnV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "审批意见实体类")
public class OpinionApprovalV {

    @ApiModelProperty(value = "请求响应信息")
    private OpinionApprovalReturnV ES_RETURN;


    @ApiModelProperty(value = "请求响应结果")
    private List<OpinionApprovalResultV> ET_RESULT;

}
