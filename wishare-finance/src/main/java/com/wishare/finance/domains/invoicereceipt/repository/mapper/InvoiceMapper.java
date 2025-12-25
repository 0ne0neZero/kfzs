package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.invoice.dto.*;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceListF;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<InvoiceE> {

    /**
     * 分页查询开票列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<InvoiceDto> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 根据条件查询开票列表
     *
     * @param form
     * @return
     */
    List<BillInvoiceDto> listByBillIds(@Param("form")  InvoiceListF form);

    /**
     * 根据发票状态查询开票数据
     *
     * @param invoieStates
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceE> getInvoiceByState(@Param("invoieStates") List<Integer> invoieStates, @Param("beginDate") Date beginDate);


    /**
     * 根据发票代码和发票号码更新发票主表状态
     *
     * @param invoiceCode
     * @param invoiceNo
     * @param state
     */
    @InterceptorIgnore(tenantLine = "on")
    void updateRed(@Param("invoiceCode") String invoiceCode, @Param("invoiceNo") String invoiceNo, @Param("state") Integer state);

    /**
     * 统计发票信息
     *
     * @param queryModel
     * @return
     */
    InvoiceStatisticsDto statistics(@Param("ew") QueryWrapper<?> queryModel);

    /**
     * 发票收据主表id获取票
     * @param invoiceReceiptId
     * @return
     */
    @Select("select * from invoice where invoice_receipt_id = #{invoiceReceiptId} deleted = 0 order by gmt_modify desc limit 1")
    InvoiceE selectByInvoiceReceiptId(@Param("invoiceReceiptId") Long invoiceReceiptId);


    List<InvoiceInfoDto> getInvoiceInfo(@Param("blueInvoiceReceiptId") Long blueInvoiceReceiptId,
                                        @Param("invoiceReceiptId") Long invoiceReceiptId,
                                        @Param("billId")Long billId,@Param("invoiceReceiptState") List<Integer> invoiceReceiptState);

    /**
     * 根据蓝票信息获取红票信息
     * @param blueInvoiceReceiptIdList 蓝票id集合
     * @return List
     */
    List<InvoiceAndReceiptDto> getRedInvoiceInfoByBlueInvoiceReceiptId(@Param("blueInvoiceReceiptIdList") List<Long> blueInvoiceReceiptIdList);

    /**
     * 根据账单id获取发票账单相关信息
     * @param billIds
     * @return
     */
    List<InvoiceBillDetailDto> queryInvoiceBillDetails(@Param("billIds") List<Long> billIds);

    /**
     * 根据invoiceReceiptId获取发票推送所需信息
     * @param invoiceReceiptIds
     * @return
     */
    List<InvoiceMessageDto> getInvoiceMessages(@Param("invoiceReceiptIds") List<Long> invoiceReceiptIds);

    /**
     * 根据票据id获取账票据信息
     * @param invoiceId
     * @return
     */
    InvoiceDto getByInvoiceId(@Param("invoiceId") Long invoiceId);

    /**
     * 根据票据id获取账票据信息
     * @param invoiceId
     * @return
     */
    InvoiceAndDetailListDto getByInvoiceIdForMessage(@Param("invoiceId") Long invoiceId);

    /**
     * 根据票据id列表获取账票据信息
     * @param invoiceIds
     * @return
     */
    List<InvoiceDto> getByInvoiceIds(@Param("invoiceIds") List<Long> invoiceIds);

    /**
     * 根据发票流水号获取发票信息
     *
     * @param invoiceSerialNum
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    @Select("SELECT * FROM invoice where invoice_serial_num = #{invoiceSerialNum} AND deleted = 0")
    List<InvoiceE> listByInvoiceSerialNums(@Param("invoiceSerialNum") String invoiceSerialNum);

    @Delete("DELETE FROM invoice WHERE invoice_receipt_id = #{invoiceReceiptId}")
    int deletePhysically(@Param("invoiceReceiptId") Long invoiceReceiptId);

    /** 根据项目id查询定额发票列表
     * @param communityId
     * @return
     */
    List<InvoiceDto> queryQuotaListByCommunityId(@Param("communityId") String communityId);

    /** 根据发票编号查询发票信息
     * @param invoiceNo
     * @return
     */
    List<InvoiceDto> queryInvoiceByInvoiceNo(@Param("invoiceNo") String invoiceNo);
}
