package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceChildDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceChildE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceChildMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/3
 * @Description:
 */
@Service
public class InvoiceChildRepository extends ServiceImpl<InvoiceChildMapper, InvoiceChildE> {

    /**
     * 根据发票主表id获取发票子表信息
     *
     * @param invoiceReceiptId
     * @return
     */
    public List<InvoiceChildE> invoiceChildList(Long invoiceReceiptId) {
        LambdaQueryWrapper<InvoiceChildE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceChildE::getInvoiceReceiptId, invoiceReceiptId);
        wrapper.orderByDesc(InvoiceChildE::getGmtCreate);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据发票主表id获取发票子表信息
     *
     * @param invoiceReceiptId
     * @return
     */
    public List<InvoiceChildDto> getByInvoiceReceiptId(Long invoiceReceiptId) {
        return baseMapper.getByInvoiceReceiptId(invoiceReceiptId);
    }
}
