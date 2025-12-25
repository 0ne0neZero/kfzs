package com.wishare.finance.apps.service.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.AddPayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.fo.QueryPaymentChannelF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdatePayChannelConfF;
import com.wishare.finance.apps.model.configure.organization.vo.PayChannelConfV;
import com.wishare.finance.domains.configure.organization.service.PayChannelConfDomainService;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentChannelRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/15
 * @Description:
 */
@Service
public class PayChannelConfAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private PayChannelConfDomainService payChannelConfDomainService;

    /**
     * 新增支付渠道
     *
     * @param form
     * @return
     */
    public Long addPayChannelConf(AddPayChannelConfF form) {
        return payChannelConfDomainService.addPayChannelConf(form);
    }

    /**
     * 修改支付设置
     *
     * @param form
     * @return
     */
    public Boolean updatePayChannelConf(UpdatePayChannelConfF form) {
        return payChannelConfDomainService.updatePayChannelConf(form);
    }

    /**
     * 删除支付设置
     *
     * @param id
     * @return
     */
    public Boolean deletePayChannelConf(Long id) {
        return payChannelConfDomainService.deletePayChannelConf(id);
    }

    /**
     * 根据id获取支付渠道详情
     *
     * @param id
     * @return
     */
    public PayChannelConfV detailPayChannelConf(Long id) {
        PaymentChannelRv paymentChannelRv = payChannelConfDomainService.detailPayChannelConf(id);
        return Global.mapperFacade.map(paymentChannelRv, PayChannelConfV.class);
    }

    /**
     * 根据id启用或禁用银行账户
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        return payChannelConfDomainService.enable(id, disableState);
    }

    /**
     * 分页获取支付设置
     *
     * @param form
     * @return
     */
    public PageV<PayChannelConfV> payChannelConfPage(PageF<SearchF<?>> form) {
       // PageV<PaymentChannelRv> paymentChannelRvPageV = payChannelConfDomainService.payChannelConfPage(form);
        PageV<PaymentChannelRv> paymentChannelRvPageV = new PageV<PaymentChannelRv>();
        paymentChannelRvPageV.setRecords(new ArrayList<PaymentChannelRv>());
        return PageV.of(form, paymentChannelRvPageV.getTotal(), Global.mapperFacade.mapAsList(paymentChannelRvPageV.getRecords(), PayChannelConfV.class));
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public FileVo fileUpload(MultipartFile file) {
        return payChannelConfDomainService.fileUpload(file);
    }


    public List<PayChannelConfV> payChannelConfVList(QueryPaymentChannelF queryPaymentChannelF){
        return  payChannelConfDomainService.payChannelConfVList(queryPaymentChannelF);
    }
}
