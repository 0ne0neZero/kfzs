package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Mapper
public interface InvoiceBookMapper extends BaseMapper<InvoiceBookE> {

    /**
     * 分页查询票本列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<InvoiceBookE> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 根据发票号码获取票本信息
     *
     * @param invoiceNo
     * @return
     */
    InvoiceBookE getInvoiceReceiptCode(@Param("invoiceNo") String invoiceNo,@Param("type") Integer type);
}
