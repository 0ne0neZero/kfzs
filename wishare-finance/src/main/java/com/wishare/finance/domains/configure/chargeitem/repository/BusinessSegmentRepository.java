package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessSegmentE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.BusinessSegmentMapper;
import org.springframework.stereotype.Service;

@Service
public class BusinessSegmentRepository extends ServiceImpl<BusinessSegmentMapper, BusinessSegmentE> {

}
