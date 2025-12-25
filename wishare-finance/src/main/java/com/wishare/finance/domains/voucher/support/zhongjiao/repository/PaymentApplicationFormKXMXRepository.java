package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormKxmx;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormPayMx;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationKXMXMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentApplicationFormKXMXRepository extends ServiceImpl<PaymentApplicationKXMXMapper, PaymentApplicationFormKxmx> {

    public void removeByAppId(Long id) {
        this.remove(new LambdaQueryWrapper<PaymentApplicationFormKxmx>().eq(PaymentApplicationFormKxmx::getPayAppId,id));
    }

    public List<PaymentApplicationFormKxmx> queryKXMXDetail(Long id) {
        LambdaQueryWrapper<PaymentApplicationFormKxmx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentApplicationFormKxmx::getPayAppId, id);
        wrapper.eq(PaymentApplicationFormKxmx::getDeleted, 0);
        return this.list(wrapper);
    }
}
