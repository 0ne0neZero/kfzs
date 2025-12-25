package com.wishare.finance.infrastructure.remote.vo.org;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 财务组织-成本返回参数
 * </p>
 *
 * @author wangrui
 * @since 2022-08-15
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgFinanceCostRv {

    @ApiModelProperty("主键（成本id）")
    private Long id;
    @ApiModelProperty("组织中文名称")
    private String nameCn;
    @ApiModelProperty("组织英文名称")
    private String nameEn;
    @ApiModelProperty("财务组织pid")
    private Long pid;
    @ApiModelProperty("组织类型 1 法定单位 2 成本中心")
    private String type;
    @ApiModelProperty("成本中心编码")
    private String costCode;
    @ApiModelProperty("关联组织-企业组织 org_org主键")
    private Long orgId;
    @ApiModelProperty("企业组织父级数组")
    private List<Long> orgIds;
    @ApiModelProperty("关联组织名称")
    private String orgName;
    @ApiModelProperty("关联项目id")
    private String communityId;
    @ApiModelProperty("是否与主数据库同步：0否 1是")
    private Integer dataSyn;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否启用  0 开启 1 禁用")
    private Integer disabled;
    @ApiModelProperty("成本中心形式 1 项目 2行政组织")
    private Integer costForm;
    @ApiModelProperty("商户号")
    private String unionPayMerchantNo;
}
