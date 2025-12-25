package com.wishare.finance.apps.model.configure.subject.vo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.configure.subject.fo.SubjectLevelJson;
import com.wishare.finance.domains.configure.chargeitem.consts.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapRulesE;
import com.wishare.finance.infrastructure.conts.VoucherSysEnum;
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
@ApiModel("科目映射规则Vo")
public class SubjectMapRulesV {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("映射规则名称")
    private String subMapName;

    @ApiModelProperty("科目体系id")
    private Long subSysId;

    @ApiModelProperty("科目体系名称")
    private String subSysName;

    @ApiModelProperty("凭证系统")
    private Integer voucherSys;
    @ApiModelProperty("凭证系统描述")
    private String voucherSysStr;

    @ApiModelProperty("映射单元类型（1 费项 2 辅助核算）")
    private Integer subMapType;
    @ApiModelProperty("映射单元类型描述")
    private String subMapTypeStr;

    @ApiModelProperty("费项属性： 1收入,2支出 3代收代付及其他")
    private Integer chargeItemAttribute;
    @ApiModelProperty("费项属性描述")
    private String chargeItemAttributeStr;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("一级科目json")
    private List<SubjectLevelJson> subjectLevelJson;

    public String getSubMapTypeStr() {
        if (this.getSubMapType() != null) {
            SubMapTypeEnum subMapTypeEnum = SubMapTypeEnum.valueOfByCode(this.getSubMapType());
            if (subMapTypeEnum != null) {
                return subMapTypeEnum.getDes();
            }
        }
        return subMapTypeStr;
    }

    public String getChargeItemAttributeStr() {
        if (this.getChargeItemAttribute() != null) {
            ChargeItemAttributeEnum chargeItemAttributeEnum = ChargeItemAttributeEnum.valueOfByCode(this.getChargeItemAttribute());
            if (chargeItemAttributeEnum != null) {
                return chargeItemAttributeEnum.getDes();
            }
        }
        return chargeItemAttributeStr;
    }

    public String getVoucherSysStr() {
        if (this.getVoucherSys() != null) {
            VoucherSysEnum voucherSysEnum = VoucherSysEnum.valueOfByCode(this.getVoucherSys());
            if (voucherSysEnum != null) {
                return voucherSysEnum.getDes();
            }
        }
        return voucherSysStr;
    }

    public SubjectMapRulesV general(SubjectMapRulesE subjectMapRulesE) {
        SubjectMapRulesV subjectMapRulesV = new SubjectMapRulesV();
        subjectMapRulesV.setId(subjectMapRulesE.getId());
        subjectMapRulesV.setSubMapName(subjectMapRulesE.getSubMapName());
        subjectMapRulesV.setSubMapType(subjectMapRulesE.getSubMapType());
        subjectMapRulesV.setDisabled(subjectMapRulesE.getDisabled());
        subjectMapRulesV.setChargeItemAttribute(subjectMapRulesE.getChargeItemAttribute());
        subjectMapRulesV.setVoucherSys(subjectMapRulesE.getVoucherSys());
        subjectMapRulesV.setSubSysId(subjectMapRulesE.getSubSysId());
        subjectMapRulesV.setSubSysName(subjectMapRulesE.getSubSysName());
        subjectMapRulesV.setSubjectLevelJson(JSON.parseArray(subjectMapRulesE.getSubjectLevelJson(), SubjectLevelJson.class));
        return subjectMapRulesV;
    }
}
