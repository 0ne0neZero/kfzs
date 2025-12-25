package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Mapper
public interface VoucherBillDetailZJMapper extends BaseMapper<VoucherPushBillDetailZJ> {

    Page<VoucherPushBillDetailZJ> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
    List<VoucherPushBillDetailZJ> selectVoucherPushBillDetailZJ(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
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

    List<VoucherBillZJRecV> queryRecList(@Param("voucherBillNo") String voucherBillNo);


    void delete(String voucherBillNo);

    @InterceptorIgnore(tenantLine = "on")
    void updatePushStateByVoucherBIllNo(@Param("voucherBillNo")String voucherBillNo, @Param("pushState") Integer pushState);

    List<String> queryAllPropertyMainCode();

}
