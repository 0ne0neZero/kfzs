package com.wishare.finance.apps.model.bill.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BillOverdueStatisticsV {

    private Long roomCount;

    private Long overdueCount;

    private BigDecimal overdueAmount;
}
