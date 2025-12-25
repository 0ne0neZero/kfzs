package com.wishare.finance.infrastructure.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 法定单位
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("法定单位返回信息")
public class StatutoryBodyRv {

    @ApiModelProperty("财务组织id")
    private Long id;
    @ApiModelProperty("组织中文名称")
    private String nameCn;
    @ApiModelProperty("组织英文名称")
    private String nameEn;
    @ApiModelProperty("财务组织pid")
    private Long pid;
    @ApiModelProperty("组织类型 1 法定单位 2 成本中心")
    private String type;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("关联组织-企业组织 org_org主键")
    private Long orgId;
    @ApiModelProperty("企业组织父级数组")
    private List<Long> orgIds;
    @ApiModelProperty("关联组织名称")
    private String orgName;
    @ApiModelProperty("法人代表名称")
    private String corporatorName;
    @ApiModelProperty("纳税人类别：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关")
    private Integer taxpayerType;
    @ApiModelProperty("纳税人识别号")
    private String taxpayerNo;
    @ApiModelProperty("营业地址")
    private String address;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("主管税务机关编码")
    private String taxAuthorityCode;
    @ApiModelProperty("主管税务机关名称")
    private String taxAuthority;
    @ApiModelProperty("是否与主数据库同步：0否 1是")
    private Integer dataSyn;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;
}
