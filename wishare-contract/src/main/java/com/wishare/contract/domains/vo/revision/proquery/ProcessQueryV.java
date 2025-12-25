package com.wishare.contract.domains.vo.revision.proquery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/11:26
 */
@Data
@ApiModel(value = "流程查询实体类")
public class ProcessQueryV {

    @ApiModelProperty(value = "请求响应信息")
    private ProcessQueryReturnV ES_RETURN;

    @ApiModelProperty(value = "请求响应结果")
    private ProcessQueryResultV ET_RESULT;
}
