package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 报销明细入参
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("报销明细入参")
public class ReimbursementDetailF {

    @ApiModelProperty(value = "费项编码", required = true)
    @NotNull(message = "费项编码不能为空")
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "科目编码", required = true)
    @NotNull(message = "科目编码不能为空")
    private String subjectCode;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    @ApiModelProperty(value = "报销明细金额（单位：分）", required = true)
    @NotNull(message = "报销明细金额不能为空")
    private Long reimburseAmount;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "税率说明", required = true)
    @NotNull(message = "税率说明不能为空")
    private String taxRateDes;

    @ApiModelProperty(value = "不含税金额（单位：分）",required = true)
    @NotNull(message = "不含税金额不能为空")
    private Long excTaxAmount;

    @ApiModelProperty(value = "税额（单位：分）", required = true)
    @NotNull(message = "税额不能为空")
    private Long taxAmount;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "辅助核算项明细, 凭证NCC推凭需要，涉及到的科目的辅助核算必传", required = true)
    @Size(min = 1, max = 200, message = "辅助核算项数量仅允许1-200条")
    @Valid
    private List<ReimbursementAssisteItemF> assisteItems;

}
