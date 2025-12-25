package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 财务组织返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("财务组织返回信息")
public class FinanceOrganizationV {

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

    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
}
