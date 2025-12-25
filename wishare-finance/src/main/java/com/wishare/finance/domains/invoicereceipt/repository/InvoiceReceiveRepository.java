package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiveDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceReceiveMapper;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceReceiveOriginMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Service
public class InvoiceReceiveRepository extends ServiceImpl<InvoiceReceiveMapper, InvoiceReceiveE> {

    @Autowired
    private InvoiceReceiveMapper invoiceReceiveMapper;

    @Autowired
    private InvoiceReceiveOriginMapper invoiceReceiveOriginMapper;

    /**
     * 查询该票本全部领取数量
     *
     * @param receiveInvoiceBookId
     * @return
     */
    public Long getReceiveNumberTotal(Long receiveInvoiceBookId) {
        return invoiceReceiveMapper.getReceiveNumberTotal(receiveInvoiceBookId);
    }

    /**
     * 分页查询发票领用列表
     *
     * @param form
     * @return
     */
    public Page<InvoiceReceiveDto> receiveQueryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.orderByDesc("ir.receive_time");
        queryModel.groupBy("ird.invoice_receive_id");
        return invoiceReceiveMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 根据项目id获取领用记录
     *
     * @param communityId
     * @param type
     * @return
     */
    public List<InvoiceReceiveE> getIdByCommunity(String communityId,  Integer type) {
        return invoiceReceiveOriginMapper.getIdByCommunity(communityId,type);
    }
}
