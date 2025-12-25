package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Mapper
public interface InvoiceReceiptMapper extends BaseMapper<InvoiceReceiptE> {

    /**
     * 根据账单id获取发票收据信息
     *
     * @param billId
     * @return
     */
    List<InvoiceReceiptE> getByBillId(@Param("billId") Long billId,@Param("types")List<Integer> types);

    /**
     * 分页查询发票和收据列表
     *
     * @param page page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<InvoiceAndReceiptDto> queryDetailPage(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper, @Param("receivableBillName")String receivableBillName);

    /**
     * 根据状态查询发票和收据合计金额(用于流水领用)
     *
     * @param sysSource 系统来源
     * @param idList 发票id集合
     * @return Long
     */
    InvoiceAndReceiptStatisticsDto queryAmount(@Param("idList") List<Long> idList,@Param("sysSource") Integer sysSource,@Param("receivableBillName")String receivableBillName);

    /**
     * 根据状态查询发票和收据合计金额(用于流水领用)
     *
     * @return Long
     */
    InvoiceAndReceiptStatisticsDto queryAmount2( @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper, @Param("table") String oneTable);

    /**
     * 根据账单id批量获取发票数据
     * @param billIdList 账单id
     * @param billType
     * @return 账单列表
     */
    List<InvoiceBillDto> getBillInvoiceList(@Param("billIdList") List<Long> billIdList, @Param("billType") Integer billType);

    /**
     * 根据账单id获取开票金额
     *
     * @param billIdList
     * @return
     */
    Long invoiceReceiptAmount(@Param("billIdList")List<Long> billIdList,@Param("invoiceReceiptStateList")List<Integer> invoiceReceiptStateList);

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    @Select("select * from invoice_receipt where id = #{id} and deleted = 0")
    InvoiceReceiptE queryById(Long id);

    @Delete("DELETE FROM invoice_receipt WHERE id = #{invoiceReceiptId}")
    int deletePhysically(@Param("invoiceReceiptId") Long invoiceReceiptId);

    @Select("SELECT r.id, r.invoice_receipt_id, r.deleted, r.tenant_id, r.creator_name, r.creator, r.gmt_create, r.operator_name, r.operator, r.gmt_modify FROM invoice_receipt ir INNER JOIN invoice r ON ir.id = r.invoice_receipt_id WHERE ir.type = 6;")
    List<InvoiceE> listInvoices();

    long countByInvoiceNo(@Param("invoiceNo") String invoiceNo, @Param("state") List<Integer> state);

    List<InvoiceDto> queryInvoiceDetailByGatherBillIds(@Param("gatherBillIds") List<Long> gatherBillIds);

    String getLastReceiptNo();

    @InterceptorIgnore(tenantLine = "on")
    int queryByRedInvoiceReceipt(@Param("invoiceReceiptNo") String invoiceReceiptNo);
}
