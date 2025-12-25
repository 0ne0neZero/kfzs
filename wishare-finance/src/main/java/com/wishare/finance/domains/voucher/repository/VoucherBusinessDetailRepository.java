package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherBusinessDetailMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 20:05
 * @version: 1.0.0
 */
@Service
public class VoucherBusinessDetailRepository extends ServiceImpl<VoucherBusinessDetailMapper, VoucherBusinessDetail> {

    public List<VoucherBusinessDetail> findByBillIdAndBillType(Long billId, BillTypeEnum billTypeEnum) {
        if (billId == null || billTypeEnum == null) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().eq(VoucherBusinessDetail::getBusinessBillId, billId)
                .eq(VoucherBusinessDetail::getBusinessBillType, billTypeEnum.getCode()).list();
    }

}
