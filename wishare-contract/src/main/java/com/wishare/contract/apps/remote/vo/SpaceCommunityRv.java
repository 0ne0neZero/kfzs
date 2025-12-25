package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author wangrui
 */
@Getter
@ToString
@Setter
@Accessors(chain = true)
public class SpaceCommunityRv {

    @ApiModelProperty("项目id")
    private String id;
    @ApiModelProperty("项目名")
    private String name;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否禁用：0：否，1：是")
    private Byte disabled;
    @ApiModelProperty("组织id")
    private Long orgId;
    @ApiModelProperty("PJ码")
    private String serialNumber;
    @ApiModelProperty("项目简称编码")
    private String projectNameNumber;
    @ApiModelProperty("省份名称")
    private String province;
    @ApiModelProperty("项目类型")
    private Long type;
}
