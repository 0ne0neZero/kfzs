package com.wishare.finance.domains.voucher.support.zhongjiao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJRecDetailE;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillZJRecDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author longhuadmin
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SuppressWarnings("all")
public class VoucherBillZJRecDetailRepository extends ServiceImpl<VoucherBillZJRecDetailMapper, VoucherBillZJRecDetailE> {

    private final VoucherBillZJRecDetailMapper voucherBillZJRecDetailMapper;

    public void deleteByVoucherBillNo(String voucherBillNo) {
        voucherBillZJRecDetailMapper.deleteByVoucherBillNo(voucherBillNo);
    }
}
