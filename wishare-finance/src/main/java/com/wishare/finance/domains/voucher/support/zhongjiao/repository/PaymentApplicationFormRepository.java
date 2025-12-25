package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationFormZJV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentApplicationFormRepository extends ServiceImpl<PaymentApplicationZJMapper, PaymentApplicationFormZJ> {
    private final  PaymentApplicationZJMapper paymentApplicationZJMapper;


    public Page<PaymentApplicationFormZJ> pageBySearch(PageF<SearchF<?>> form) {
        return paymentApplicationZJMapper.selectBySearch(RepositoryUtil.convertMPPage(form), RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
    }

    public PaymentApplicationFormZJ queryById(String id) {
        return this.lambdaQuery().eq(PaymentApplicationFormZJ::getId,id).eq(PaymentApplicationFormZJ::getDeleted,0).one();
    }


    public PaymentApplicationFormZJV getDetailById(String id) {

        return paymentApplicationZJMapper.getDetailById(id);
    }

    //根据信息台ID获取数据
    public PaymentApplicationFormZJ getDetailByBpmProcessId(String bpmProcessId) {
        return paymentApplicationZJMapper.getDetailByBpmProcessId(bpmProcessId);
    }
}
