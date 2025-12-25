package com.wishare.finance.domains.expensereport.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportE;
import com.wishare.finance.domains.expensereport.repository.mapper.ExpenseReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseReportRepository extends ServiceImpl<ExpenseReportMapper, ExpenseReportE> {

}
