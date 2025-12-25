package com.wishare.finance.infrastructure.remote.vo.external.oa;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/15:38
 */
@Data
@ApiModel(value = "审批意见实体类-V2")
public class OpinionApprovalV2 {

    @ApiModelProperty(value = "请求响应信息")
    private OpinionApprovalReturnV ES_RETURN;


    @ApiModelProperty(value = "请求响应结果")
    private List<OpinionApprovalResultV2> ET_RESULT;

}
