package com.wishare.finance.infrastructure.remote.vo.space;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: fxl
 * @Date: 2023/8/9
 * @Description:
 **/
@Getter
@Setter
public class UserInfoRawV {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("登录账号名称")
    private String account;
    @ApiModelProperty("姓名")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("手机号")
    private String mobileNum;
    @ApiModelProperty("座机号码")
    private String telephone;
    @ApiModelProperty(value = "组织ID列表")
    private List<Long> orgIds;
    @ApiModelProperty(value = "组织名称列表")
    private List<String> orgNames;
}
