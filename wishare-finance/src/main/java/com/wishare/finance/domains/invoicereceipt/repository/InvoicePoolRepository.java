package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoicePoolE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoicePoolMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Service
public class InvoicePoolRepository extends ServiceImpl<InvoicePoolMapper, InvoicePoolE> {

    /**
     * 根据发票号删除票池
     *
     * @param invoiceNums
     */
    public void deletByInvoiceNum(List<Long> invoiceNums) {
        LambdaQueryWrapper<InvoicePoolE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoicePoolE::getInvoiceNum, invoiceNums);
        baseMapper.delete(wrapper);
    }

    /**
     * 删除票本id
     *
     * @param invoiceBookId
     */
    public void deleteByInvoiceBookId(Long invoiceBookId) {
        LambdaQueryWrapper<InvoicePoolE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoicePoolE::getInvoiceBookId, invoiceBookId);
        baseMapper.delete(wrapper);
    }

    /**
     * 根据发票编号获取发票
     *
     * @param invoiceNums
     * @param invoiceType
     * @return
     */
    public Long getByInvoiceNums(List<Long> invoiceNums, Integer invoiceType) {
        LambdaQueryWrapper<InvoicePoolE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoicePoolE::getInvoiceNum, invoiceNums);
        wrapper.eq(InvoicePoolE::getInvoiceType, invoiceType);
        return baseMapper.selectCount(wrapper);
    }
}
