package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同订立信息拓展表支付方信息
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractZffxxF {
    @ApiModelProperty("支出方名称")
    private String drawee;

    @ApiModelProperty("支出方编码")
    private String draweeid;

    @ApiModelProperty("应付合同金额")
    private String draweepayamt;

    @ApiModelProperty("实际付款人")
    private String truedrawee;

    @ApiModelProperty("实际付款人编码")
    private String truedraweeid;

    @ApiModelProperty("支出方信息前端展示")
    private List<ContractZffxxDraweeExtendF> draweeName;

    @ApiModelProperty("实际支出方信息前端展示")
    private List<ContractZffxxTrueDraweeExtendF> truedraweeName;

}
