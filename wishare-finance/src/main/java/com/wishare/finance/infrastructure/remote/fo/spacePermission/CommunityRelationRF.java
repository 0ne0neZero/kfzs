package com.wishare.finance.infrastructure.remote.fo.spacePermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/11/3
 * @Description:
 */
@Getter
@Setter
@ApiModel("关联关系列表查询")
public class CommunityRelationRF {

    @ApiModelProperty("标识码(例nuonuo)")
    private String code;

    @ApiModelProperty("项目ID列表")
    private List<String> communityIds;
}
