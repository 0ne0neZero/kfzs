package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.FlowTempDetailE;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowTempDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class FlowTempDetailRepository extends ServiceImpl<FlowTempDetailMapper, FlowTempDetailE> {
}
