package com.wishare.contract.apps.remote.fo.proquery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:48
 */
@Data
@ApiModel(value = "流程创建请求数据实体")
public class ProcessQueryDataF {

    @ApiModelProperty(value = "流程请求id")
    private String requestId;


    @ApiModelProperty(value = "表单唯一id")
    private String formdataid;

}
