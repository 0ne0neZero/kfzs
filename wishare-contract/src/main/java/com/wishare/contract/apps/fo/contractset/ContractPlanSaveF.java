package com.wishare.contract.apps.fo.contractset;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
* <p>
* 合同收款计划信息 保存请求参数
* </p>
*
* @author wangrui
* @since 2022-11-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPlanSaveF {

    @ApiModelProperty("收付款计划")
    List<ContractCollectionPlanSaveF> contractCollectionPlanF;
    @ApiModelProperty("合同Id")
    private  Long contractId;
    @ApiModelProperty("是否自动生成损益（工程计提情况下）0 否 1 是")
    private  Integer isNotProfit;
}
