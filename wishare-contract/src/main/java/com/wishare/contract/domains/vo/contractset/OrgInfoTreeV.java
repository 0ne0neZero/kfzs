package com.wishare.contract.domains.vo.contractset;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 组织信息表
 * </p>
 *
 * @author wishare
 * @since 2022-04-13
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel
@ToString
public class OrgInfoTreeV extends Tree<OrgInfoTreeV,Long> {

    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("归属公司")
    private String companyId;
    @ApiModelProperty("组织排序")
    private Integer sort;
    @ApiModelProperty("组织类型 1 公司 2 部门")
    private Integer orgType;
    @ApiModelProperty("简称")
    private String shortName;
    @ApiModelProperty("英文简称")
    private String englishName;
    @ApiModelProperty("组织类型id")
    private Long typeId;

    @ApiModelProperty("组织路径")
    private String standardOrgPath;
//    @ApiModelProperty(value = "是否禁用 false 否 true是",hidden = true)
//    private Boolean disabled;
    @ApiModelProperty("是否禁用 false 否 true是")
    private Boolean unable;

    @ApiModelProperty("是否有权限勾选 true 是 false 否")
    private Boolean havAuth;
    @ApiModelProperty("是否可选 false 否 true是")
    private Boolean disabled;

    @ApiModelProperty("是否默认创建 false 否 true是")
    private Boolean defaultCreate;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
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
    @ApiModelProperty("该组织类型的icon Url")
    private String iconUrl;
    @ApiModelProperty("老saas 0 ，基座创建 1，中交4A：2")
    private Integer sourceSys;

    @ApiModelProperty("4A主数据状态")
    private String stateOf4A;

    @ApiModelProperty("简称编码")
    private String abbrCode;

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof OrgInfoTreeV){
            OrgInfoTreeV o = (OrgInfoTreeV) obj;
            return o.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
