package com.wishare.contract.apps.remote.fo.proquery;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:46
 */
@Data
@ApiModel(value = "流程创建请求实体")
public class ProcessQueryF {


    @ApiModelProperty(value = "请求头部信息")
    private ProcessQueryHeadF IS_REQ_HEAD;


    @ApiModelProperty(value = "业务数据")
    private ProcessQueryDataF IT_DATA;

}
