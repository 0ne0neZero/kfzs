package com.wishare.finance.apps.model.configure.subject.fo;

import com.wishare.finance.domains.configure.chargeitem.consts.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubMapTypeEnum;
import com.wishare.finance.infrastructure.conts.VoucherSysEnum;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/12/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("修改科目映射规则入参")
public class UpdateSubjectMapRulesF {

    @ApiModelProperty(value = "科目映射规则id",required = true)
    @NotNull(message = "科目映射规则id不能为空")
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

    public void check() {
        checkVoucherSys();
        checkSubMapType();
        checkChargeItemAttribute();
    }

    /**
     *  校检映射单元类型
     */
    private void checkSubMapType() {
        if (this.getSubMapType() != null) {
            SubMapTypeEnum subMapTypeEnum = SubMapTypeEnum.valueOfByCode(this.getSubMapType());
            if (subMapTypeEnum == null) {
                throw BizException.throw400("映射单元类型不存在");
            }
        }

    }

    /**
     * 校检费项属性
     */
    private void checkChargeItemAttribute() {
        if (this.getChargeItemAttribute() != null) {
            ChargeItemAttributeEnum chargeItemAttributeEnum = ChargeItemAttributeEnum.valueOfByCode(this.getChargeItemAttribute());
            if (chargeItemAttributeEnum == null) {
                throw BizException.throw400("该费项属性不存在");
            }
        }
    }

    /**
     * 校检凭证系统
     */
    private void checkVoucherSys() {
        if (this.getVoucherSys() != null) {
            VoucherSysEnum voucherSysEnum = VoucherSysEnum.valueOfByCode(this.getVoucherSys());
            if (voucherSysEnum == null) {
                throw BizException.throw400("该凭证系统不存在");
            }
        }
    }
}
