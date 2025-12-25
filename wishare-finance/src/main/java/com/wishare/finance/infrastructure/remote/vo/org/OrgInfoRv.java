package com.wishare.finance.infrastructure.remote.vo.org;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author
 * @date
 * @Description: 根据id查询财务组织详情
 */
@Getter
@Setter
public class OrgInfoRv {

    @ApiModelProperty("组织id")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("标准组织路径")
    private String standardOrgPath;
    @ApiModelProperty("租户id")
    private Long tenantId;
    @ApiModelProperty("归属公司")
    private Long companyId;
    @ApiModelProperty("父id")
    private Long pid;
    @ApiModelProperty("组织排序")
    private Integer sort;
    @ApiModelProperty("组织类型")
    private Integer orgType;
    @ApiModelProperty("是否禁用")
    private Boolean disabled;
    @ApiModelProperty("是否默认创建")
    private Boolean defaultCreate;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    private String gmtCreate;
    @ApiModelProperty("修改人")
    private String operator;
    @ApiModelProperty("修改时间")
    private String gmtModify;

}
