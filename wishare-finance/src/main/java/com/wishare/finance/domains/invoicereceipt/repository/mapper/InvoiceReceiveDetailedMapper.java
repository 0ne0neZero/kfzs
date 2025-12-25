package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveDetailedE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Mapper
public interface InvoiceReceiveDetailedMapper extends BaseMapper<InvoiceReceiveDetailedE> {


    /**
     * 根据票据领用记录id获取票据编号
     *
     * @param invoiceReceiptIds
     * @param pageSize
     * @param type
     * @return
     */
    List<String> getInvoiceReceiptNo(@Param("invoiceReceiptIds") List<Long> invoiceReceiptIds, @Param("state")Integer state, @Param("pageSize") Long pageSize, @Param("invoiceType")Integer type);
}
