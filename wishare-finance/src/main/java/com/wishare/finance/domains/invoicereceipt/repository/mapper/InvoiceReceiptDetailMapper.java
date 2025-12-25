package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceDetailAndReceiptV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.domains.invoicereceipt.command.invocing.GatherInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.PayInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.ReceivableInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Mapper
public interface InvoiceReceiptDetailMapper extends BaseMapper<InvoiceReceiptDetailE> {

    /**
     * 根据票据主表id获取账单ids
     *
     * @param invoiceReceiptId
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceReceiptDetailE> getByInvoiceReceiptId(@Param("invoiceReceiptId") Long invoiceReceiptId);

    /**
     * 根据票据类型查询账单ids
     * @param queryModel
     * @return
     */
    List<InvoiceReceiptDetailV> getBillIdsByType(@Param("qm") QueryWrapper<InvoiceReceiptE> queryModel);

    /**
     * 根据账单id获取发票账单相关信息
     * @param billIds
     * @return
     */
    List<InvoiceBillDetailDto> getInvoiceBillDetails(@Param("billIds") List<Long> billIds, @Param("recIds") List<Long> recIds);

    /**
     * 查询账单对应红冲的票据
     * @param invoiceReceiptIds
     * @param billId
     * @return
     */
    List<InvoiceBillDto> listPartRedInvoices(@Param("invoiceReceiptIds") List<Long> invoiceReceiptIds, @Param("billId") Long billId);

    /**
     * 根据收款单信息查询票据信息
     * @param query 收款单信息
     * @return
     */
    List<InvoiceBillDetailDto> getByGatherBillIds(@Param("giQuery") GatherInvoiceQuery query, @Param("gatherDetailName") String gatherDetailName);


    /**
     * 根据收款明细ids获取收据开票主表信息
     * @param billIds
     * @return
     */
    List<InvoiceBillDetailDto> getReceiptByGatherDetailIds(@Param("gatherBillIds") List<Long> gatherBillIds);



    List<InvoiceBillDetailDto> getInvoiceByRecBillIds(@Param("giQuery") ReceivableInvoiceQuery query);


    /**
     * 根据开票单元id获取开票信息
     * @param invRecUnitId
     * @return
     */
    List<InvoiceBillDetailDto> getInvoiceByInvRecUnitId(@Param("invRecUnitId") String invRecUnitId);

    /**
     * 根据付款单信息查询票据信息
     * @param query 收款单信息
     * @return
     */
    List<InvoiceBillDetailDto> getByPayBillIds(@Param("payQuery") PayInvoiceQuery query);

    /**
     * 更新账单开票时间
     */
    void updateBillPayTime(@Param("billType") Integer billType, @Param("billId") Long billId,
                           @Param("billPayTime")LocalDateTime billPayTime, @Param("afterPayment") Integer afterPayment);

    @Delete("DELETE FROM invoice_receipt_detail WHERE invoice_receipt_id = #{invoiceReceiptId}")
    int deletePhysically(@Param("invoiceReceiptId") Long invoiceReceiptId);


    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceDetailAndReceiptV> queryInvoiceDetailList(@Param("supCpUnitId") String supCpUnitId ,@Param("billId") Long billId);

    InvoiceDetailAndReceiptV queryEmptyInvoiceDetail(@Param("supCpUnitId") String supCpUnitId ,@Param("billId") Long billId);

    List<Long> getValidInvoiceReceiptIds(@Param("gatherBillId")Long gatherBillId, @Param("supCpUnitId")String supCpUnitId);
    List<Long> getValidInvoiceIdsByGatherBillIds(@Param("gatherBillId")Long gatherBillId);

    @InterceptorIgnore(tenantLine = "on")
    List<String> findOldCommunityToRefresh();

    List<Long> getValidInvoiceIdsByBillId(Long billId);
}
