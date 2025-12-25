package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.FlowClaimFilesV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormPayMx;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationPayMxMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentApplicationPayMxRepository extends ServiceImpl<PaymentApplicationPayMxMapper, PaymentApplicationFormPayMx> {
    private final   PaymentApplicationPayMxMapper paymentApplicationPayMxMapper;


    public void removeByAppId(Long id) {
        this.remove(new LambdaQueryWrapper<PaymentApplicationFormPayMx>().eq(PaymentApplicationFormPayMx::getPayAppId,id));
    }


    public List<PaymentApplicationFormPayMx> queryPayDetail(Long id) {
        LambdaQueryWrapper<PaymentApplicationFormPayMx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentApplicationFormPayMx::getPayAppId, id);
        wrapper.eq(PaymentApplicationFormPayMx::getDeleted, 0);
        return this.list(wrapper);
    }
}
