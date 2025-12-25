package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 凭证模板分录摘要信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
@ApiModel("凭证模板分录摘要信息")
public class VoucherTemplateEntrySummary {

    @ApiModelProperty(value = "分录摘要类型: Text普通文本, AccountMonth归属月, GmtModify操作日期, CostCenter成本中心, StatutoryBody法定单位", required = true)
    @NotBlank(message = "分录摘要类型不能为空")
    private String type;

    @ApiModelProperty(value = "分录摘要类型值", required = true)
    @NotBlank(message = "分录摘要类型值不能为空")
    private String value;

    @ApiModelProperty(value = "分录摘要类型标题", required = true)
    @NotBlank(message = "分录摘要类型标题不能为空")
    private String label;

}
