package com.wishare.contract.apps.remote.vo.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hhb
 * @describe
 * @date 2025/7/26 12:03
 */
@Data
public class CfgExternalDeportData {

    @ApiModelProperty("项目ID")
    private String communityId;
    @ApiModelProperty("数据编号")
    private String dataCode;
    @ApiModelProperty("数据名称")
    private String dataName;
    @ApiModelProperty("是否默认推送（ 0. 否； 1. 是）")
    private Integer isDefaultPush;
}
