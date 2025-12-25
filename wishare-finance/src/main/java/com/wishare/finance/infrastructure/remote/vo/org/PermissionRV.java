package com.wishare.finance.infrastructure.remote.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author light
 * @since 2023/2/19
 */
@Data
@ApiModel("组织权限")
public class PermissionRV {

    @ApiModelProperty("用户拥有权限的所有确切的节点，即有该节点则拥有权限，没有则没有权限")
    private List<Long> allNodes;

    @ApiModelProperty("权限类型， 1  全部权限 0 没有任何权限 3 部分权限（此时返回所有有权限的节点）")
    private Integer type;
}
