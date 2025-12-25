package com.wishare.contract.domains.vo.revision.proquery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/11:31
 */
@Data
@ApiModel(value = "请求响应信息")
public class ProcessQueryResultV {

    @ApiModelProperty(value = "流程状态名称 草稿、审批中、审批结束、人工结束、删除、不存在")
    private String PROCESSSTATENAME;

    @ApiModelProperty(value = "流程状态ID 0：草稿、1：审批中、2：审批结束、3：人工结束、4：删除、5：不存在")
    private String PROCESSSTATUS;


}
