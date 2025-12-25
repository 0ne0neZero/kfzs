package com.wishare.finance.domains.mdm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.fo.VoucherBillRecMdm63F;
import com.wishare.finance.apps.pushbill.vo.Mdm63FrontV;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.starter.beans.PageF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface Mdm63Mapper extends BaseMapper<Mdm63E> {

    int deleteByCtCode(@Param("ctCode") String ctCode);

    void deleteByCtCodeAndPartnerCodeInCertainPeriod(@Param("ctCode") String ctCode,
                                                     @Param("partnerCode") String partnerCode,
                                                     @Param("start") String start,
                                                     @Param("end") String end,
                                                     @Param("projectId") String projectId);


    void insertBatch(@Param("list") List<Mdm63E> list);

    List<Mdm63E> selectByCondition(@Param("ctCode") String conMainCode, @Param("idExt") String idExt);

    /**
     * 匹配指定范围的应收/应付
     **/
    List<Mdm63E> queryOnContract(@Param("ctCode") String ctCode,
                                 @Param("paymentId") String paymentId,
                                 @Param("apArType") String apArType,
                                 @Param("sourceBillEventType") Integer sourceBillEventType);
    /**
     * 匹配指定范围的应收/应付
     **/
    List<Mdm63E> queryOnContractByFtId(@Param("voucherBillNo") String voucherBillNo);

    List<Mdm63E> queryOnContract2(@Param("ctCode") String ctCode,
                                 @Param("paymentId") String paymentId,
                                 @Param("apArType") String apArType,
                                 @Param("sourceBillEventType") Integer sourceBillEventType);

    List<Mdm63E> queryOnSettlementIds(@Param("settlementIds") List<String> settlementIds,
                                      @Param("apArType") String apArType,
                                      @Param("sourceBillEventType") Integer sourceBillEventType);

    List<Mdm63E> queryOnPaymentAppId(@Param("payAppId") Long payAppId,
                                     @Param("apArType") String apArType,
                                     @Param("sourceBillEventType") int sourceBillEventType);

    List<Mdm63E> queryOnSettlementIdAndPaymentId(@Param("settlementId") String settlementId,
                                                 @Param("paymentId") String paymentId,
                                                 @Param("apArType") String apArType,
                                                 @Param("sourceBillEventType") int sourceBillEventType);

    Page<Mdm63FrontV> queryPageOnRec(Page<?> pageF,
                                     @Param("arapModule") String arapModule,
                                     @Param("projectId") String projectId,
                                     @Param("partnerCode") String partnerCode,
                                     @Param("billNum") String billNum,
                                     @Param("bizDate") Date bizDate,
                                     @Param("contractNo") String contractNo,
                                     @Param("fundsPropId") String fundsPropId,
                                     @Param("queryVoucherBillNo") String queryVoucherBillNo,
                                     @Param("fromContract") boolean fromContract);

    List<Mdm63E> queryForReceiptAutoMatch(@Param("arapModule") String arapModule,
                                          @Param("fromContract") boolean fromContract,
                                          @Param("contractNo") String contractNo,
                                          @Param("projectId") String projectId,
                                          @Param("paymentId") String paymentId,
                                          @Param("partnerCode") String partnerCode);

    List<Mdm63E> selectByFtIds(@Param("ftIds") List<String> ftIds);

    Page<Mdm63FrontV> queryMdm63Page(Page<?> pageF,
            @Param("projectId") String projectId,
            @Param("partnerCode") String partnerCode,
            @Param("contractNo") String contractNo,
            @Param("arapModule") String arapModule
            );

    Mdm63E getMdm63ByFtId(@Param("ftId") String ftId);
}
