package com.wishare.finance.apps.service.invoicereceipt;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.invoice.fo.GetInvoiceReceiptNoF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBookListF;
import com.wishare.finance.apps.model.invoice.invoicebook.fo.AddInvoiceBookF;
import com.wishare.finance.apps.model.invoice.invoicebook.fo.InvoiceReceiveF;
import com.wishare.finance.apps.model.invoice.invoicebook.vo.InvoiceBookV;
import com.wishare.finance.apps.model.invoice.invoicebook.dto.ReceiveDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiveDto;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceBookCommand;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceReceiveCommand;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceBookDomainService;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Service
public class InvoiceBookAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private InvoiceBookDomainService invoiceBookDomainService;

    @Setter(onMethod_ = {@Autowired})
    private AccountOrgFacade accountOrgFacade;

    /**
     * 新增票本
     *
     * @param form
     * @return
     */
    public Long addInvoiceBook(AddInvoiceBookF form) {
        OrgTenantRv orgTenantRv = accountOrgFacade.tenantGetById(getTenantId().get());
        AddInvoiceBookCommand command = form.getAddInvoiceBookCommand(orgTenantRv.getEnglishName());
        return invoiceBookDomainService.addInvoiceBook(command);
    }

    /**
     * 票本领用
     *
     * @param form
     * @return
     */
    public Boolean receive(InvoiceReceiveF form) {
        AddInvoiceReceiveCommand command = form.getInvoiceReceive();
        return invoiceBookDomainService.receive(command);
    }

    /**
     * 分页查询票本
     *
     * @param form
     * @return
     */
    public PageV<InvoiceBookV> queryPage(PageF<SearchF<?>> form) {
        Page<InvoiceBookE> pageResult = invoiceBookDomainService.queryPage(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), InvoiceBookV.class));
    }

    /**
     * 查询票本列表
     *
     * @param form
     * @return
     */
    public List<InvoiceBookV> querylist(InvoiceBookListF form) {
        List<InvoiceBookE> invoiceBookEList =invoiceBookDomainService.querylist(form);
        return Global.mapperFacade.mapAsList(invoiceBookEList, InvoiceBookV.class);
    }

    /**
     * 删除票本
     *
     * @param id
     * @return
     */
    public Boolean deleteInvoiceBook(Long id) {
        return invoiceBookDomainService.deleteInvoiceBook(id);
    }

    /**
     * 根据id获取票本详情
     *
     * @param id
     * @return
     */
    public InvoiceBookV detailInvoiceBook(Long id) {
        InvoiceBookE invoiceBookE = invoiceBookDomainService.detailInvoiceBook(id);
        return Global.mapperFacade.map(invoiceBookE, InvoiceBookV.class);
    }

    /**
     * 分页查询发票领用列表
     *
     * @param form
     * @return
     */
    public PageV<InvoiceReceiveDto> receiveQueryPage(PageF<SearchF<?>> form) {
        Page<InvoiceReceiveDto> pageResult = invoiceBookDomainService.receiveQueryPage(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), InvoiceReceiveDto.class));
    }

    /**
     * 根据id获取票本领用详情
     *
     * @param id
     * @return
     */
    public ReceiveDetailDto receiveDetail(Long id) {
        return invoiceBookDomainService.receiveDetail(id);
    }


    /**
     * 获取可领用发票号
     *
     * @param form
     * @return
     */
    public List<String> getInvoiceReceiptNo(GetInvoiceReceiptNoF form) {
        return invoiceBookDomainService.getInvoiceReceiptNo(form);
    }
}
