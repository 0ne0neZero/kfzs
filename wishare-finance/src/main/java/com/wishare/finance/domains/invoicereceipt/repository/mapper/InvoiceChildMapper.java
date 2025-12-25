package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceChildDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceChildE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/3
 * @Description:
 */
@Mapper
public interface InvoiceChildMapper extends BaseMapper<InvoiceChildE> {
    /**
     * 根据发票主表id获取发票子表信息
     *
     * @param invoiceReceiptId
     * @return
     */
    List<InvoiceChildDto> getByInvoiceReceiptId(@Param("invoiceReceiptId") Long invoiceReceiptId);
}
