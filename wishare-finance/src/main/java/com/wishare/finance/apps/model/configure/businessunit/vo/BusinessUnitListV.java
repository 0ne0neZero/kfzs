package com.wishare.finance.apps.model.configure.businessunit.vo;

import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountV;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitDetailE;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgInfoRv;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务单元返回信息
 *
 * @author
 */
@Getter
@Setter
@ApiModel("业务单元返回信息")
public class BusinessUnitListV {

    @ApiModelProperty(value = "业务单元id")
    private Long id;

    @ApiModelProperty(value = "父业务单元id")
    private Long parentId;

    @ApiModelProperty(value = "父业务单元名称")
    private String parentName;

    @ApiModelProperty(value = "父业务单元编码")
    private String parentCode;

    @ApiModelProperty("业务单元编码")
    private String code;

    @ApiModelProperty("业务单元名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("关联详情")
    private List<BusinessUnitDetailE> businessUnitDetailEList;

    @ApiModelProperty("法定单位id")
    private List<Long> legalIds;

    @ApiModelProperty("成本中心id")
    private List<Long> costIds;

    @ApiModelProperty("行政组织id")
    private List<Long> orgIds;

    @ApiModelProperty("银行账号id")
    private List<Long> statutoryBodyAccountId;

    @ApiModelProperty("法定单位")
    private List<StatutoryBodyRv> legalArray=new ArrayList<>();

    @ApiModelProperty("成本中心")
    private List<StatutoryBodyRv> costArray=new ArrayList<>();

    @ApiModelProperty("行政组织")
    private List<OrgInfoRv> orgArray=new ArrayList<>();

    @ApiModelProperty("银行账户")
    private List<StatutoryBodyAccountV> accountArray=new ArrayList<>();

}
