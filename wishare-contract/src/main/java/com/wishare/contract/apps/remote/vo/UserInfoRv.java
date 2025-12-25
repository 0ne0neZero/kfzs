package com.wishare.contract.apps.remote.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 通用用户信息展示
 * </p>
 *
 * @author wishare
 * @since 2022-04-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户通用信息展示对象", description = "用户通用信息展示对象")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoRv {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("登录账号名称")
    private String account;
    @ApiModelProperty("角色Ids")
    private List<String> roleIds;
    @ApiModelProperty(value = "数据库原始角色字符",hidden = true)
    @JsonIgnore
    @JSONField(serialize = false)
    private String roleIdsString;
    @ApiModelProperty("姓名")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty("组织ID")
    private List<Long> orgIds;
    @ApiModelProperty(value = "数据库原始角色字符",hidden = true)
    @JsonIgnore
    @JSONField(serialize = false)
    private String orgIdsString;
    @ApiModelProperty("手机号")
    private String mobileNum;
    @ApiModelProperty("账号状态 0 正常 1 禁用")
    private Integer state;
    @ApiModelProperty("邮箱")
    private String email;
}

