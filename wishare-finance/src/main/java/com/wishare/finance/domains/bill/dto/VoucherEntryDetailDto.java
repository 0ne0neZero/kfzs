package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分录详情
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
public class VoucherEntryDetailDto {

    @ApiModelProperty(value = "借贷类型： debit借方， icredit贷方")
    private String type;

    @ApiModelProperty(value = "借贷类型名称")
    private String typeName;

    @ApiModelProperty(value = "借贷金额（单位：分）")
    private String amount;

    @ApiModelProperty(value = "规则描述")
    private String ruleRemark;

    @ApiModelProperty(value = "科目代码")
    private String subjectCode;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    @ApiModelProperty(value = "币种名称 （人民币）")
    private String current;

    @ApiModelProperty(value = "辅助核算详情信息")
    private String supItemName;

    @ApiModelProperty(value = "辅助编码集合")
    private String auxiliaryCount;

}
