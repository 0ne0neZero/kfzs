package com.wishare.finance.domains.configure.subject.command.subject;

import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description:
 */
@Getter
@Setter
@ApiModel("批量设置")
public class BatchSettingCommand {

    @ApiModelProperty("科目映射规则id")
    private Long subjectMapRuleId;

    @ApiModelProperty("科目映射单元id")
    private Long subMapUnitId;

    @ApiModelProperty("一级科目id")
    private Long subjectLevelOneId;

    @ApiModelProperty("一级科目名称")
    private String subjectLevelOneName;

    @ApiModelProperty("末级科目id")
    private Long subjectLevelLastId;

    @ApiModelProperty("末级科目名称")
    private String subjectLevelLastName;

    @ApiModelProperty("映射类别： 1科目，2现金流量")
    private Integer mapType;

    @ApiModelProperty("现金流方向类型(1=现金流入、2=现金流出)")
    private Integer cashFlowType;


    /**
     * 构建科目映射单元明细
     *
     * @return
     */
    public SubjectMapUnitDetailE getSubjectMapUnitDetail(Integer subMapType) {
        SubjectMapUnitDetailE detailE = Global.mapperFacade.map(this, SubjectMapUnitDetailE.class);
        detailE.setId(IdentifierFactory.getInstance().generateLongIdentifier("subjectMapUnitDetailEId"));
        detailE.setSubMapRuleId(this.getSubjectMapRuleId());
        detailE.setSubMapType(subMapType);
        return detailE;
    }

    public SubjectMapUnitDetailE getUpdateSubjectMapUnitDetail(SubjectMapUnitDetailE subjectMapUnitDetailE) {
        SubjectMapUnitDetailE detailE = Global.mapperFacade.map(this, SubjectMapUnitDetailE.class);
        detailE.setId(subjectMapUnitDetailE.getId());
        detailE.setSubMapRuleId(subjectMapUnitDetailE.getSubMapRuleId());
        detailE.setSubMapType(subjectMapUnitDetailE.getSubMapType());
        return detailE;
    }
}
