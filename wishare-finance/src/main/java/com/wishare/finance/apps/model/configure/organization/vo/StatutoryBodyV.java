package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("法定单位反参")
public class StatutoryBodyV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("法定单位编码")
    private String code;

    @ApiModelProperty("法人代表名称")
    private String corporatorName;

    @ApiModelProperty("法定单位名称中文")
    private String nameCn;

    @ApiModelProperty("法定单位名称英文")
    private String nameEn;

    @ApiModelProperty("纳税人类别：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关")
    private Integer taxpayerType;

    @ApiModelProperty("纳税人识别号")
    private String taxpayerNo;

    @ApiModelProperty("营业地址")
    private String address;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("开户行名称")
    private String bankName;

    @ApiModelProperty("开户行账号")
    private String bankAccount;

    @ApiModelProperty("主管税务机关编码")
    private String taxAuthorityCode;

    @ApiModelProperty("主管税务机关名称")
    private String taxAuthority;

    @ApiModelProperty("关联组织")
    private Long orgId;

    @ApiModelProperty("关联组织名称")
    private String orgName;

    @ApiModelProperty("是否与主数据库同步：0否 1是")
    private Integer dataSyn;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;
}
