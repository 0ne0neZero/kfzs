package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 组织信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrgInfoTreeF {

    @ApiModelProperty("组织ID 主键")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("归属公司id")
    private String companyId;
    @ApiModelProperty("父级ID")
    private Long pid;
    @ApiModelProperty("是否禁用 false 否 true是")
    private Boolean disabled;
    @ApiModelProperty("组织类型 1公司 2 部门")
    private Integer orgType;
    @ApiModelProperty("组织类型id")
    private Long typeId;

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("组织ID集合 主键")
    private List<Long> ids;

    @ApiModelProperty("过滤组织架构")
    private Integer filterOrg;

    @ApiModelProperty("权限范围内的组织id集合")
    private List<Long> permissionOrgIds;

    public boolean isAllOrgInfo() {
        return StringUtils.isAllBlank(this.orgName, this.companyId) &&
                Objects.isNull(this.id) && Objects.isNull(this.pid) &&
                Objects.isNull(this.orgType) && Objects.isNull(this.typeId)
                && Objects.isNull(this.filterOrg);
    }
}
