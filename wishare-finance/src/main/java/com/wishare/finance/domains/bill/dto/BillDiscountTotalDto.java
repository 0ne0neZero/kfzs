package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 账单减免合计数据信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单减免合计数据信息")
public class BillDiscountTotalDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "应收减免总金额", required = true)
    private Long totalAmount = 0L;

    @ApiModelProperty(value = "应收减免总金额", required = true)
    private Long deductibleAmount = 0L;

    @ApiModelProperty(value = "实收减免总金额", required = true)
    private Long discountAmount = 0L;

    @ApiModelProperty(value = "应收减免总金额", required = true)
    private Long settleAmount = 0L;


    public void add(BillDiscountTotalDto billDiscountTotalDto){
        if (Objects.nonNull(billDiscountTotalDto)){
            totalAmount += billDiscountTotalDto.getTotalAmount();
            deductibleAmount += billDiscountTotalDto.getDeductibleAmount();
            discountAmount += billDiscountTotalDto.getDiscountAmount();
            settleAmount += billDiscountTotalDto.getSettleAmount();
        }
    }

}
