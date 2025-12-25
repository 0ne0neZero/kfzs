package com.wishare.finance.apps.model.configure.subject.fo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.subject.command.subject.CreateSubjectCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
@ApiModel("创建科目数据")
public class CreateSubjectF {

    @ApiModelProperty(value = "科目名称", required = true)
    @NotBlank(message = "科目名称不能为空")
    private String subjectName;

    @ApiModelProperty(value = "科目编码", required = true)
    @NotBlank(message = "科目编码不能为空")
    private String subjectCode;

    @ApiModelProperty(value = "科目类型id", required = true)
    @NotNull(message = "科目类型不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "科目体系id", required = true)
    @NotNull(message = "科目体系不能为空")
    private Long subjectSystemId;

    @ApiModelProperty("父科目id")
    private Long parentId;

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

    public CreateSubjectCommand getCreateSubjectCommand(IdentityInfo identityInfo){
        CreateSubjectCommand command = Global.mapperFacade.map(this, CreateSubjectCommand.class);
        LocalDateTime now = LocalDateTime.now();
        command.setAuxiliaryCount(JSON.toJSONString(auxiliaryCountList));
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setGmtCreate(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("subjectId"));
        return command;
    }
}
