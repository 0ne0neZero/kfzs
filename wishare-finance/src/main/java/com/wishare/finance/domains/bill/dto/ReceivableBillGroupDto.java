package com.wishare.finance.domains.bill.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ReceivableBillGroupDto extends BillGroupDto{

    /**
     * 计费方式
     */
    private Integer billMethod;

    /**
     * 计费面积(单位：m²)
     */
    private BigDecimal chargingArea;

}
