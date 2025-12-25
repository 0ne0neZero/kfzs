package com.wishare.finance.infrastructure.remote.fo.spacePermission;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author: zhengHui
 * @createTime: 2022-09-28  11:42
 * @description:
 */
@Data
public class SpacePermissionF {
    @ApiModelProperty("组织id集合")
    private List<String> orgIds;

    @ApiModelProperty("关联id，项目id或空间id")
    private List<String> spaceIds;

    @ApiModelProperty("空间类型分类，1：房号相关，2：空间区域相关，3：车位相关")
    private List<String> spaceTypeClassify;

    @ApiModelProperty("空间类型标识名称")
    private List<String> spaceTypeNameFlag;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("是否全部返回")
    private Boolean allReturn;

    @ApiModelProperty("项目id")
    private String communityId;
}
