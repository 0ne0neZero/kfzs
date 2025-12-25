package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工程类合同计提信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractEngineeringPlanF {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("计提编号")
    private String accrualCode;

}
