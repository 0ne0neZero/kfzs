package com.wishare.contract.apps.remote.fo.procreate;

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
public class ProcessCreateDataF {

    @ApiModelProperty(value = "通用数据对象")
    private ProcessCreateBaseInfoF BASEINFO;


    @ApiModelProperty(value = "业务数据对象")
    private ProcessCreateBusinessInfoF BUSINESSINFO;

}
