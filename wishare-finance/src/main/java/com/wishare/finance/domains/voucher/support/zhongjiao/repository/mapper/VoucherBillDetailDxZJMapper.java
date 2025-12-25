package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxCostDetailsV;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxPaymentDetailsV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherBillDetailDxZJMapper extends BaseMapper<VoucherPushBillDetailDxZJ> {

    Page<VoucherPushBillDetailDxZJ> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    List<VoucherPushBillDetailDxZJ> selectVoucherPushBillDetailDxZJ(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    VoucherBillZJDetailMoneyV queryMoney(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    VoucherBillZJConvertMoneyV queryConvertDetailMoney(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    Page<VoucherBillZJFlowDetailV>  queryFlowDetailPage(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    Page<VoucherBillZJFlowDetailV>  queryFlowDetailPageNew(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);


    List<FlowClaimFilesV> queryFlowClaimFilesV(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    List<FlowClaimFilesV> queryFlowClaimFilesVNew(@Param("ids") List<Long> ids);

    Page<VoucherBillZJConvertDetailV> queryConvertDetailPage(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);


    Page<VoucherBillZJRecDetailV> queryRecDetailPage(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    Page<VoucherBillZJRecCWYDetailV> queryRecCWYDetailPage(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    Page<VoucherBillZJCashFlowV> queryCashFlowPage(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    Page<VoucherBillCostInfoV> queryVoucherBillCostInfo(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);


    List<VoucherBillZJRecCWYDetailV> queryRecCWYDetail(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);


    void delete(String voucherBillNo);

    @InterceptorIgnore(tenantLine = "on")
    void updatePushStateByVoucherBIllNo(@Param("voucherBillNo")String voucherBillNo, @Param("pushState") Integer pushState);

    List<DxPaymentDetailsV> getPaymentDetailsVByVoucherBillNoOnPay(@Param("voucherBillNo") String voucherBillNo,
                                                                   @Param("billEventType") Integer billEventType,
                                                                   @Param("communityId") String communityId);

    List<DxPaymentDetailsV> getPaymentDetailsVByVoucherBillNoOnIncome(@Param("voucherBillNo") String voucherBillNo,
                                                                      @Param("billEventType") Integer billEventType,
                                                                      @Param("communityId") String communityId);

    List<DxCostDetailsV> getCostDetailsVByVoucherBillNoOnPay(@Param("voucherBillNo") String voucherBillNo,
                                                             @Param("billEventType") Integer billEventType,
                                                             @Param("communityId") String communityId);

    List<String> selectContractIdForMdm63OnPay();

    List<String> selectContractIdForMdm63OnIncome();


    Page<PaymentApplicationBZDetailV> selectBZDBySearch(Page<SearchF<?>> searchFPage,@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    //根据报账单号获取去重后封装数据
    List<VoucherPushBillDetailDxZJ> getbillDetailDxZjList(@Param("voucherBillNoList") List<String> voucherBillNoList);

    void deleteByVoucherBIllNo(@Param("voucherBillNo")String voucherBillNo);
}
