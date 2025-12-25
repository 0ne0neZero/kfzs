package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.entity.BillReverseE;
import com.wishare.finance.domains.bill.repository.mapper.BillReverseMapper;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description:
 */
@Service
public class BillReverseRepository extends ServiceImpl<BillReverseMapper, BillReverseE> {

    /**
     * 根据账单id获取最新数据
     *
     * @param billId
     * @return
     */
    public BillReverseE getOneByBillIdDescTime(Long billId) {
        LambdaQueryWrapper<BillReverseE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillReverseE::getBillId, billId);
        wrapper.orderByDesc(BillReverseE::getGmtCreate);
        return baseMapper.selectOne(wrapper);
    }
}
