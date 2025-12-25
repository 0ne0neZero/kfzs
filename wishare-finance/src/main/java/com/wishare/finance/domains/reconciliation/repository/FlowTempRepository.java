package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.FlowTempE;
import com.wishare.finance.domains.reconciliation.repository.mapper.FlowTempMapper;
import org.springframework.stereotype.Service;

@Service
public class FlowTempRepository  extends ServiceImpl<FlowTempMapper, FlowTempE> {
}
