package com.wishare.finance.infrastructure.remote.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
* <p>
* 用户档案基本信息表
* </p>
*
* @author light
* @since 2022-12-22
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "用户档案基本信息表视图对象", description = "用户档案基本信息表")
public class ArchivesUserV {

    @ApiModelProperty("档案用户头像")
    private FileVo headPicUrl;
    @ApiModelProperty("用户档案ID")
    private String archivesId;
    @ApiModelProperty("档案使用的用户ID，主动生成")
    private String bindUserId;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("档案人员姓名")
    private String name;
    @ApiModelProperty("档案人员昵称")
    private String nickName;
    @ApiModelProperty("是否老人/儿童 0 否 1 老人 2 儿童")
    private Integer agedOrChild;
    @ApiModelProperty("联系电话")
    private String mobileNum;
    @ApiModelProperty("证件类型")
    private String certificatesType;
    @ApiModelProperty("证件号码")
    private String certificatesNumber;
    @ApiModelProperty("上传证件 数组形式")
    private String certificates;
    @ApiModelProperty("数据来源 0 注册用户1 导入用户")
    private Integer dataSources;
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @ApiModelProperty("性别 0 女 1 男")
    private Integer gender;
    @ApiModelProperty("民族")
    private String nation;
    @ApiModelProperty("政治面貌")
    private String politicalStatus;
    @ApiModelProperty("婚姻状态")
    private String maritalStatus;
    @ApiModelProperty("学历")
    private String education;
    @ApiModelProperty("职业")
    private String job;
    @ApiModelProperty("紧急联系人")
    private String emergencyContact;
    @ApiModelProperty("紧急联系人电话")
    private String emergencyContactNumber;
    @ApiModelProperty("是否实名 0 是未实名 1 是实名")
    private Integer validatedPerson = 0;
    @ApiModelProperty("座机电话")
    private String telephone;

}
