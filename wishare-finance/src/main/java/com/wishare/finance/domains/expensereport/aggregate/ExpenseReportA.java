package com.wishare.finance.domains.expensereport.aggregate;

import java.util.List;

import com.wishare.finance.domains.expensereport.entity.ExpenseReportDetailE;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportE;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenseReportA {

    private ExpenseReportE expenseReportE;

    private List<ExpenseReportDetailE> expenseReportDetails;


}
