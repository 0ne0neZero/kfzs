package com.wishare.contract.apps.remote.vo.org;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
public class OrgDetailsRV {

    @ApiModelProperty("组织ID 主键")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("归属公司")
    private String companyId;
    @ApiModelProperty("父级ID")
    private Long pid;
    @ApiModelProperty("组织类型 1 公司 2 部门")
    private Integer orgType;
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
}
