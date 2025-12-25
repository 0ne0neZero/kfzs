package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:44
 */

@Data
@ApiModel(value = "公共请求头部")
public class CommonHeadF {

    @ApiModelProperty(value = "接口请求唯一ID")
    private String ZINSTID;

    @ApiModelProperty(value = "请求时间")
    private String ZZREQTIME;

    @ApiModelProperty(value = "来源系统代码")
    private String ZZSRC_SYS;

    @ApiModelProperty(value = "备用字段1")
    private String ZZATTR1;

    @ApiModelProperty(value = "备用字段2")
    private String ZZATTR2;

    @ApiModelProperty(value = "备用字段3")
    private String ZZATTR3;

}
