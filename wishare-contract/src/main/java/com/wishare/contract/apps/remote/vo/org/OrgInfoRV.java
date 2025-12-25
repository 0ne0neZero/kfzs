package com.wishare.contract.apps.remote.vo.org;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
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
public class OrgInfoRV {

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof OrgInfoRV){
            OrgInfoRV o = (OrgInfoRV) obj;
            return o.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


}
