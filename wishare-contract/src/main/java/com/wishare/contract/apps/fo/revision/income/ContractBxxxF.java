package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表保险信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractBxxxF {
    @ApiModelProperty("险种名称")
    private String insurancename;

    @ApiModelProperty("保险金额")
    private String insuranceamt;

    @ApiModelProperty("免赔额")
    private String deductible;
}
