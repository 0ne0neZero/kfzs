package com.wishare.finance.infrastructure.remote.vo.org;


import com.fasterxml.jackson.annotation.JsonFormat;
//import com.wishare.org.domains.vo.tenant.TenantNatureV;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 组织信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-04-14
 */
@Data
@Accessors(chain = true)
public class OrgDetailsV {

    @ApiModelProperty("组织ID 主键")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("组织标准名称")
    private String standardOrgName;
    @ApiModelProperty("标准组织路径")
    private String standardOrgPath;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("归属公司")
    private String companyId;
    @ApiModelProperty("父级ID")
    private Long pid;
    @ApiModelProperty("组织类型 1 公司 2 部门")
    private Integer orgType;
    @ApiModelProperty(value = "部门负责人ID")
    private String deptUserId;
    @ApiModelProperty(value = "部门负责人",hidden = true)
    private String deptContacts;
    @ApiModelProperty(value = "部门联系人手机号",hidden = true)
    private String deptPhone;
    @ApiModelProperty("组织描述")
    private String remarks;
    @ApiModelProperty("营业执照")
    private String license;
    @ApiModelProperty("上传营业执照的FileVo")
    private FileVo licenseFileVo;
    @ApiModelProperty("联系邮箱")
    private String mail;
    @ApiModelProperty("联系电话")
    private String telephone;
    @ApiModelProperty("公司负责人手机号")
    private String phone;
    @ApiModelProperty("logo")
    private String logo;
    @ApiModelProperty("logo文件上传")
    private FileVo logoFileVo;
    @ApiModelProperty("负责人名字")
    private String contacts;
    @ApiModelProperty("统一社会信用代码")
    private String uscc;
    @ApiModelProperty("客户简称")
    private String shortName;
    @ApiModelProperty("客户英文简称")
    private String englishName;
    @ApiModelProperty("公司地址")
    private String address;
    @ApiModelProperty(value = "公司属性",hidden = true)
    private String nature;
    @ApiModelProperty("公司属性code列表")
    private List<String> natures;
//    @ApiModelProperty("公司属性名称列表")
//    private List<TenantNatureV> natureList;
    @ApiModelProperty("是否禁用 false 否 true是")
    private Boolean disabled;
    @ApiModelProperty("是否默认创建 false 否 true是")
    private Boolean defaultCreate;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("组织类型id")
    private Long typeId;
    @ApiModelProperty("4A主数据id")
    private String oid;
    @ApiModelProperty("4A主数据名字")
    private String oidName;
    @ApiModelProperty("数据来源 默认 基座 1,老Saas 0,中交4A 2")
    private Integer sourceSys;
    @ApiModelProperty("4A主数据状态")
    private String stateOf4A;
    @ApiModelProperty("组织简称编码")
    private String abbrCode;
}
