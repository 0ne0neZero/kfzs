package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yyx
 * @date 2023/6/17 15:26
 */
@Setter
@Getter
public class BusinessUnitAccountDto {

    @ApiModelProperty(value = "业务单元Id")
    private Long businessUnitId;

    @ApiModelProperty(value = "业务单元编码")
    private String businessUnitCode;

    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;

    @ApiModelProperty("关联收款账户信息")
    private List<BusinessUnitAccountBankDto> accountList;
}
