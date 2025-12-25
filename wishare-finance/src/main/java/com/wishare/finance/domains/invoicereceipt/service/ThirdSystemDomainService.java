package com.wishare.finance.domains.invoicereceipt.service;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.enums.LingshuitongInvoiceKindEnum;
import com.wishare.finance.infrastructure.remote.enums.LingshuitongVoucherTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongContentRF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongInvoiceRF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongLoginRf;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/11/28
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ThirdSystemDomainService implements ApiBase {

    private final ExternalClient externalClient;

    /**
     * 进项发票系统登录
     *
     * @param userName
     * @return
     */
    public String lingshuitonglogin(String userName) {
        LingshuitongLoginRf lingshuitongLoginRf = new LingshuitongLoginRf();
        lingshuitongLoginRf.setUserName(userName);
        lingshuitongLoginRf.setTenantId(getTenantId().get());
        return externalClient.receivableLogin(lingshuitongLoginRf);
    }


    /**
     * 灵税通入账凭证
     *
     * @param voucher
     * @param invoiceCode
     * @param invoiceNo
     * @param type
     */
    public void lingshuitongVoucherInvoice(String voucher, String invoiceCode, String invoiceNo,Integer type) {
        LingshuitongContentRF lingshuitongContentRF = new LingshuitongContentRF();
        lingshuitongContentRF.setTenantId(getTenantId().get());
        lingshuitongContentRF.setVoucherType(LingshuitongVoucherTypeEnum.入账凭证.getCodeStr());
        lingshuitongContentRF.setInvoices(generalInvoices(voucher,invoiceCode,invoiceNo,type));
        externalClient.invoiceVoucherInvoice(lingshuitongContentRF);
    }


    /**
     * 构建灵税通明细
     *
     * @param voucher
     * @param invoiceCode
     * @param invoiceNo
     * @param type
     * @return
     */
    private List<LingshuitongInvoiceRF> generalInvoices(String voucher, String invoiceCode, String invoiceNo, Integer type) {
        LingshuitongInvoiceRF invoiceRF = new LingshuitongInvoiceRF();
        invoiceRF.setInvoiceCode(invoiceCode);
        invoiceRF.setInvoiceNo(invoiceNo);
        invoiceRF.setInvoiceKind(LingshuitongInvoiceKindEnum.valueOfByCode(type).getDes());
        invoiceRF.setVoucherNumber(voucher);
        return Lists.newArrayList(invoiceRF);
    }

}
