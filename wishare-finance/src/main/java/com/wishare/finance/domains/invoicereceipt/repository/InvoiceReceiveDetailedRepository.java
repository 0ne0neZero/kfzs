package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiveDetailedStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveDetailedE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceReceiveDetailedMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Service
public class InvoiceReceiveDetailedRepository extends ServiceImpl<InvoiceReceiveDetailedMapper, InvoiceReceiveDetailedE> {


    /**
     * 根据票据领用记录id获取票据编号
     *
     * @param invoiceReceiptIds
     * @param pageSize
     * @param type
     * @return
     */
    public List<String> getInvoiceReceiptNo(List<Long> invoiceReceiptIds, Long pageSize, Integer type) {
        return baseMapper.getInvoiceReceiptNo(invoiceReceiptIds, InvoiceReceiveDetailedStateEnum.待使用.getCode(), pageSize,type);
    }

    /**
     * 票据领用明细表(invoice_receive_detailed) state = 2 已使用
     * @param receiptNo
     * @param type
     */
    public Integer useByInvoiceNo(Long receiptNo, Integer type) {
        if(Objects.isNull(receiptNo)){return 0;}
        InvoiceReceiveDetailedE invoiceReceiveDetailedE = new InvoiceReceiveDetailedE();
        invoiceReceiveDetailedE.setState(InvoiceReceiveDetailedStateEnum.已使用.getCode());
        LambdaQueryWrapper<InvoiceReceiveDetailedE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceReceiveDetailedE::getInvoiceNum, receiptNo);
        wrapper.eq(InvoiceReceiveDetailedE::getInvoiceType, type);
        return baseMapper.update(invoiceReceiveDetailedE, wrapper);
    }
}
