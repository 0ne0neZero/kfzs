package com.wishare.finance.infrastructure.remote.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 *
 * @author: PengAn
 * @create: 2022-08-23
 * @description:
 **/
@Data
public class UserInfoRv {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("登录账号名称")
    private String account;
    @ApiModelProperty("角色Ids")
    private List<String> roleIds;
    @ApiModelProperty("角色名称列表")
    private List<String> roleNames;
    @ApiModelProperty("姓名")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("用户头像uri")
    private FileVo headPicUri;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty(value = "组织ID列表")
    private List<Long> orgIds;
    @ApiModelProperty(value = "组织名称列表")
    private List<String> orgNames;
    @ApiModelProperty("手机号")
    private String mobileNum;
    @ApiModelProperty("账号状态 0 正常 1 禁用")
    private Integer state;
    @ApiModelProperty("性别，1代表男，2代表女")
    private Integer gender;
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("修改人名称")
    private String operatorName;
    @ApiModelProperty("修改时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("数据权限列表")
    private List<String> dataPermissionIds;
}
