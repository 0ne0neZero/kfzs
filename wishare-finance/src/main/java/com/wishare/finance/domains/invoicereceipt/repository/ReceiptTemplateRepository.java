package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Linitly
 * @date: 2023/8/7 17:13
 * @descrption:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiptTemplateRepository extends ServiceImpl<ReceiptTemplateMapper, ReceiptTemplateE> {

    private final ReceiptTemplateMapper receiptTemplateMapper;

    public Page<ReceiptTemplateE> pageList(Page<?> page, QueryWrapper<?> queryWrapper) {
        return receiptTemplateMapper.pageList(page, queryWrapper);
    }
}
