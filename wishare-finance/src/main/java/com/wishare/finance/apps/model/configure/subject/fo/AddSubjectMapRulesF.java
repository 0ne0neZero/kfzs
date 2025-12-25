package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.chargeitem.consts.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubMapTypeEnum;
import com.wishare.finance.infrastructure.conts.VoucherSysEnum;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("创建科目类型数据")
public class AddSubjectMapRulesF {

    @ApiModelProperty(value = "映射规则名称",required = true)
    @NotBlank(message = "映射规则名称不能为空")
    private String subMapName;

    @ApiModelProperty(value = "科目体系id",required = true)
    @NotNull(message = "科目体系id不能为空")
    private Long subSysId;

    @ApiModelProperty(value = "科目体系名称",required = true)
    @NotBlank(message = "科目体系名称不能为空")
    private String subSysName;

    @ApiModelProperty(value = "凭证系统 1 用友ncc",required = true)
    @NotNull(message = "凭证系统不能为空")
    private Integer voucherSys;

    @ApiModelProperty(value = "映射单元类型（1 费项 2 辅助核算）",required = true)
    @NotNull(message = "映射单元类型不能为空")
    private Integer subMapType;

    @ApiModelProperty("费项属性： 1收入,2支出 3代收代付及其他")
    private Integer chargeItemAttribute;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用",required = true)
    @NotNull(message = "是否禁用不能为空")
    private Integer disabled;

    @ApiModelProperty("一级科目列表")
    private List<SubjectLevelJson> subjectLevelJson;


    /**
     *
     */
    public void check() {
        checkVoucherSys();
        checkSubMapType();
        checkChargeItemAttribute();
    }

    /**
     *  校检映射单元类型
     */
    private void checkSubMapType() {
        SubMapTypeEnum subMapTypeEnum = SubMapTypeEnum.valueOfByCode(this.getSubMapType());
        if (subMapTypeEnum == null) {
            throw BizException.throw400("映射单元类型不存在");
        }
    }

    /**
     * 校检费项属性
     */
    private void checkChargeItemAttribute() {
        ChargeItemAttributeEnum chargeItemAttributeEnum = ChargeItemAttributeEnum.valueOfByCode(this.getChargeItemAttribute());
        if (chargeItemAttributeEnum == null) {
            throw BizException.throw400("该费项属性不存在");
        }
    }

    /**
     * 校检凭证系统
     */
    private void checkVoucherSys() {
        VoucherSysEnum voucherSysEnum = VoucherSysEnum.valueOfByCode(this.getVoucherSys());
        if (voucherSysEnum == null) {
            throw BizException.throw400("该凭证系统不存在");
        }
    }
}
