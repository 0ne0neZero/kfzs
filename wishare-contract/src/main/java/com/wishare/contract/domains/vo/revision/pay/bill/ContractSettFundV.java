package com.wishare.contract.domains.vo.revision.pay.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/30/19:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单收付款视图对象", description = "结算单收付款视图对象")
public class ContractSettFundV {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("结算单id")
    private String settlementId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同id")
    private String payNotecode;

    @ApiModelProperty("收付款方式")
    private Integer fundType;

    @ApiModelProperty("收付款方式前端")
    private String fundTypeName;

    @ApiModelProperty("实际收付款金额")
    private BigDecimal amount;

    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("收付款类型 收款 0 付款 1")
    private Integer type;

    @ApiModelProperty("收款状态")
    private Integer planStatus;

    @ApiModelProperty("收款状态名称")
    private String planStatusName;

    @ApiModelProperty("收付款日期")
    private LocalDate collectTime;

    @ApiModelProperty("备注")
    private String remark;
}
