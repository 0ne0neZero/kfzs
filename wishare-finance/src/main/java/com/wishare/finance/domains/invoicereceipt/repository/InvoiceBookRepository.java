package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBookListF;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceBookMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Service
public class InvoiceBookRepository extends ServiceImpl<InvoiceBookMapper, InvoiceBookE> {

    @Autowired
    private InvoiceBookMapper invoiceBookMapper;

    /**
     * 分页查询票本列表
     *
     * @param form
     * @return
     */
    public Page<InvoiceBookE> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        return invoiceBookMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 查询票本列表
     *
     * @param form
     * @return
     */
    public List<InvoiceBookE> querylist(InvoiceBookListF form) {
        LambdaQueryWrapper<InvoiceBookE> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(form.getInvoiceBookNumber()), InvoiceBookE::getInvoiceBookNumber, form.getInvoiceBookNumber());
        wrapper.in(CollectionUtils.isNotEmpty(form.getStates()), InvoiceBookE::getState, form.getStates());
        return invoiceBookMapper.selectList(wrapper);
    }

    /**
     * 根据发票号码获取票本信息
     *
     * @param invoiceNo
     * @param invoiceType
     * @return
     */
    public InvoiceBookE getInvoiceReceiptCode(String invoiceNo, Integer invoiceType) {
        return invoiceBookMapper.getInvoiceReceiptCode(invoiceNo,invoiceType);
    }
}
