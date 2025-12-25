package com.wishare.finance.apps.model.configure.subject.fo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.subject.command.subject.UpdateSubjectCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建科目入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("更新科目数据")
public class UpdateSubjectF {

    @ApiModelProperty(value = "科目id", required = true)
    @NotNull(message = "科目id不能为空")
    private Long id;

    @ApiModelProperty(value = "父科目id")
    private Long parentId;

    @ApiModelProperty("科目编码")
    private String subjectCode;

    @ApiModelProperty("科目名称")
    private String subjectName;

    @ApiModelProperty("科目类型id")
    private Long categoryId;

    @ApiModelProperty("科目体系id")
    private Long subjectSystemId;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("现金类别： 0无，1现金，2银行，3现金等价物")
    private Integer cashType;

    @ApiModelProperty(value = "科目现金流量信息")
    private List<SubjectCashFlowF> cashFlows;

    @ApiModelProperty("辅助核算")
    private List<String> auxiliaryCountList;

    @ApiModelProperty("是否税费科目 0否 1是 默认0")
    private Integer existTax;

    public UpdateSubjectCommand getUpdateSubjectCommand(IdentityInfo identityInfo){
        UpdateSubjectCommand command = Global.mapperFacade.map(this, UpdateSubjectCommand.class);
        command.setAuxiliaryCount(JSON.toJSONString(auxiliaryCountList));
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
