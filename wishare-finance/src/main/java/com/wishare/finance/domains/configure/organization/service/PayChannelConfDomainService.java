package com.wishare.finance.domains.configure.organization.service;

import com.wishare.finance.apps.model.configure.organization.fo.AddPayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.fo.QueryPaymentChannelF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdatePayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.vo.PayChannelConfV;
import com.wishare.finance.domains.configure.organization.facade.PaymentFacade;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentChannelRv;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/15
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayChannelConfDomainService {

    private final PaymentFacade paymentFacade;

    /**
     * 新增支付渠道
     *
     * @param form
     * @return
     */
    public Long addPayChannelConf(AddPayChannelConfF form) {
        return paymentFacade.addPayChannelConf(form);
    }

    /**
     * 修改支付渠道
     *
     * @param form
     * @return
     */
    public Boolean updatePayChannelConf(UpdatePayChannelConfF form) {
        return paymentFacade.updatePayChannelConf(form);
    }

    /**
     * 删除支付渠道
     *
     * @param id
     * @return
     */
    public Boolean deletePayChannelConf(Long id) {
        return paymentFacade.deletePayChannelConf(id);
    }

    /**
     * 根据id获取支付渠道详情
     *
     * @param id
     * @return
     */
    public PaymentChannelRv detailPayChannelConf(Long id) {
        PaymentChannelRv paymentChannelRv = paymentFacade.detailPayChannelConf(id);
        if (null == paymentChannelRv) {
            throw BizException.throw400("为找到对应的支付渠道配置");
        }
        return paymentChannelRv;
    }

    /**
     * 根据id启用或禁用支付渠道
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        return paymentFacade.enable(id, disableState);
    }

    /**
     * 分页查询支付渠道
     *
     * @param form
     * @return
     */
    public PageV<PaymentChannelRv> payChannelConfPage(PageF<SearchF<?>> form) {
        return paymentFacade.payChannelConfPage(form);
    }

    public List<PayChannelConfV> payChannelConfVList(QueryPaymentChannelF queryPaymentChannelF){
        return  paymentFacade.payChannelConfVList(queryPaymentChannelF);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public FileVo fileUpload(MultipartFile file) {
        return paymentFacade.fileUpload(file);
    }

}
