package com.wishare.contract.apps.remote.vo.bpm;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hhb
 * @describe
 * @date 2025/10/28 18:30
 */
@Getter
@Setter
public class OrgUser {

    @ApiModelProperty("用户头像")
    private FileVo avatar;

    @ApiModelProperty("部门ID/用户ID")
    private String id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("部门名/用户名")
    private String name;

    @ApiModelProperty("用户性别")
    private boolean sex;

    @ApiModelProperty("类型，user=用户 dept=部门")
    private String type;


}