package com.wishare.finance.infrastructure.remote.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 财务组织树返回参数
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
public class OrgFinanceTreeRv extends Tree<OrgFinanceTreeRv, Long> {

    @ApiModelProperty("组织中文名称")
    private String nameCn;
    @ApiModelProperty("组织英文名称")
    private String nameEn;
    @ApiModelProperty("组织类型 1 法定单位 2 成本中心")
    private String type;
    @ApiModelProperty("组织集合 查询多种类型财务组织")
    private List<String> types;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("关联组织-企业组织 org_org主键")
    private Long orgId;
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
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof OrgFinanceTreeRv){
            OrgFinanceTreeRv o = (OrgFinanceTreeRv) obj;
            return o.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
