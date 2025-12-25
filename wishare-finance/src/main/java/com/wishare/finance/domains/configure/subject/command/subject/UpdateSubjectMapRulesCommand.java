package com.wishare.finance.domains.configure.subject.command.subject;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.configure.subject.fo.SubjectLevelJson;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapRulesE;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("修改科目映射规则")
public class UpdateSubjectMapRulesCommand {

    @ApiModelProperty("科目映射规则id")
    private Long id;

    @ApiModelProperty("映射规则名称")
    private String subMapName;

    @ApiModelProperty("科目体系id")
    private Long subSysId;

    @ApiModelProperty("科目体系名称")
    private String subSysName;

    @ApiModelProperty("凭证系统 1 用友ncc")
    private Integer voucherSys;

    @ApiModelProperty("映射单元类型（1 费项 2 辅助核算）")
    private Integer subMapType;

    @ApiModelProperty("费项属性： 1收入,2支出 3代收代付及其他")
    private Integer chargeItemAttribute;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("一级科目列表")
    private List<SubjectLevelJson> subjectLevelJson;

    /**
     * 修改科目映射规则
     *
     * @return
     */
    public SubjectMapRulesE getSubjectMapRules() {
        SubjectMapRulesE subjectMapRulesE = Global.mapperFacade.map(this, SubjectMapRulesE.class);
        subjectMapRulesE.setSubjectLevelJson(JSON.toJSONString(this.getSubjectLevelJson()));
        return subjectMapRulesE;
    }
}
