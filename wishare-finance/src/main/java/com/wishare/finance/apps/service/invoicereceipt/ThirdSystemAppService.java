package com.wishare.finance.apps.service.invoicereceipt;

import com.wishare.finance.apps.model.invoice.invoice.fo.LingshuitongLoginF;
import com.wishare.finance.domains.invoicereceipt.service.ThirdSystemDomainService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/11/28
 * @Description:
 */
@Service
public class ThirdSystemAppService {

    @Setter(onMethod_ = {@Autowired})
    private ThirdSystemDomainService thirdSystemDomainService;

    /**
     * 进项发票系统登录
     *
     * @param form
     * @return
     */
    public String lingshuitonglogin(LingshuitongLoginF form) {
        return thirdSystemDomainService.lingshuitonglogin(form.getUserName());
    }

}
