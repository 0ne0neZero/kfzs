package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单相关金额")
public class BillAmountDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("实际应缴金额(待收款金额)")
    private Long actualUnpayAmount;

    @ApiModelProperty("欠缴金额")
    private Long amountOwed;

    @ApiModelProperty("账单ids")//账单id,法定单位id,账单金额（可缴纳金额）
    private List<BillAmountExtendDto> billGroupDtoList;
}
