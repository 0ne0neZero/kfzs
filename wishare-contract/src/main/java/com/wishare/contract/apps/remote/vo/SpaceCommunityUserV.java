package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/6/5 15:33
 */
@Getter
@ToString
@Setter
@Accessors(chain = true)
@ApiModel(value = "项目对应项目经理信息", description = "")
public class SpaceCommunityUserV {
    //项目ID
    private String communityId;
    //项目经理人员列表
    private List<UserInfoRv> userList;
}
