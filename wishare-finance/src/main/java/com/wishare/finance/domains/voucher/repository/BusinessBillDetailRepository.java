package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.BusinessBillDetail;
import com.wishare.finance.domains.voucher.repository.mapper.BusinessBillDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class BusinessBillDetailRepository extends ServiceImpl<BusinessBillDetailMapper, BusinessBillDetail> {
}
