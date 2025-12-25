package com.wishare.finance.apps.model.reconciliation.fo.external;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(name = "ZINSTID")
    private String ZINSTID;

    @ApiModelProperty(value = "请求时间")
    @JSONField(name = "ZZREQTIME")
    private String ZZREQTIME;

    @ApiModelProperty(value = "来源系统代码")
    @JSONField(name = "ZZSRC_SYS")
    private String ZZSRC_SYS;

    @ApiModelProperty(value = "备用字段1")
    @JSONField(name = "ZZATTR1")
    private String ZZATTR1;

    @ApiModelProperty(value = "备用字段2")
    @JSONField(name = "ZZATTR2")
    private String ZZATTR2;

    @ApiModelProperty(value = "备用字段3")
    @JSONField(name = "ZZATTR3")
    private String ZZATTR3;

}
