package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.domains.bill.entity.BillDeductionE;
import com.wishare.finance.domains.bill.repository.mapper.BillDeductionMapper;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.dto.ReceiptMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptMapper;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceReceiptDetailQuery;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
public class BillDeductionRepository extends ServiceImpl<BillDeductionMapper, BillDeductionE> {

    /**
     *
     * @param
     * @return
     */
    public Integer getByBillIds(List<Long> billIds) {
        return baseMapper.getByBillIds(billIds);
    }

    /**
     *
     * @param billId
     * @return
     */
    public List<BillDeductionE> getList(Long billId) {
        List<BillDeductionE> list = baseMapper.selectList(new QueryWrapper<BillDeductionE>().eq("bill_id", billId));
        return list;
    }
}
