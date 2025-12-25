package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.FlowTempRecordE;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowTempRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class FlowTempRecordRepository extends ServiceImpl<FlowTempRecordMapper, FlowTempRecordE> {
}
