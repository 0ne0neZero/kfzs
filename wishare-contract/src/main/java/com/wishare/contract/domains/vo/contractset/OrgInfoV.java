package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author hhb
 * @describe
 * @date 2025/11/15 15:12
 */
@Data
@Accessors(chain = true)
public class OrgInfoV {

    @ApiModelProperty("组织ID 主键")
    private Long id;
    @ApiModelProperty("组织名称")
    private String orgName;
    @ApiModelProperty("标准组织路径")
    private String standardOrgPath;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("归属公司")
    private String companyId;
    @ApiModelProperty("父级ID")
    private Long pid;
    @ApiModelProperty("组织排序")
    private Integer sort;
    @ApiModelProperty("组织类型 1 公司 2 部门")
    private Integer orgType;
    @ApiModelProperty("是否禁用 false 否 true是")
    private Boolean disabled;
    @ApiModelProperty("是否默认创建 false 否 true是")
    private Boolean defaultCreate;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("4A组织id")
    private String oid;
    @ApiModelProperty("4A组织名称")
    private String oidName;
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

    @ApiModelProperty("老saas 0 ，基座创建 1，中交4A：2")
    private Integer sourceSys;

    @ApiModelProperty("组织类型id")
    private Long typeId;

    @ApiModelProperty("组织类型名称")
    private String typeName;


    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof OrgInfoV){
            OrgInfoV o = (OrgInfoV) obj;
            return o.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


}
