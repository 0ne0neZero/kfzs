package com.wishare.finance.domains.expensereport.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportDetailE;
import com.wishare.finance.domains.expensereport.repository.mapper.ExpenseReportDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseReportDetailRepository extends ServiceImpl<ExpenseReportDetailMapper, ExpenseReportDetailE> {

}
