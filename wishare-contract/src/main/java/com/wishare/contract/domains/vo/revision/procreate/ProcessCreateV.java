package com.wishare.contract.domains.vo.revision.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author long
 * @date 2023年7月19日18:04:05
 */
@Data
@ApiModel(value = "流程创建实体类")
public class ProcessCreateV {
    @ApiModelProperty(value = "请求响应信息")
    private ProcessCreateReturnV ES_RETURN;

    @ApiModelProperty(value = "请求响应结果")
    private ProcessCreateResultV ET_RESULT;
}
