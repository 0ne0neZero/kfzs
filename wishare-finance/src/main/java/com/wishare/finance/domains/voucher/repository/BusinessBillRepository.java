package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.BusinessBill;
import com.wishare.finance.domains.voucher.repository.mapper.BusinessBillMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Service
public class BusinessBillRepository extends ServiceImpl<BusinessBillMapper, BusinessBill> {

    public BusinessBill getByVoucherId(String voucherId) {
        return baseMapper.getByVoucherId(voucherId);
    }

}
