package com.wishare.finance.apps.model.configure.businessunit.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yyx
 * @date 2023/6/17 15:17
 * 业务单元关联法定单位及银行账户信息
 */
@Getter
@Setter
public class BusinessUnitStatutoryV {

    @ApiModelProperty(value = "业务单元Id")
    private Long businessUnitId;

    @ApiModelProperty(value = "业务单元编码")
    private String businessUnitCode;

    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;

    @ApiModelProperty("关联收款账户信息")
    private List<BusinessUnitAccountBankV> accountList;


}
