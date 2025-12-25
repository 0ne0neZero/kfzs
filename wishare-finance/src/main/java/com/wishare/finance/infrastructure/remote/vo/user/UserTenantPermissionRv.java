package com.wishare.finance.infrastructure.remote.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author yancao
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "租户用户权限展示对象", description = "租户用户权限展示对象")
public class UserTenantPermissionRv {

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("数据类型 参见数据字典USER_DATA_PERMISSION_LEVEL")
    private Integer dataType;

    @ApiModelProperty("数据ID")
    private String dataId;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("使用状态")
    private Boolean disabled;
}