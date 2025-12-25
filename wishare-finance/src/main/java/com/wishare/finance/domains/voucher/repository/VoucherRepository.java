package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/24 20:05
 * @version: 1.0.0
 */
@Service
public class VoucherRepository extends ServiceImpl<VoucherMapper, VoucherE> {

    @Autowired
    private VoucherMapper voucherMapper;

    public Page<VoucherE> queryPage(PageF<SearchF<?>> form) {

        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.orderByDesc("gmt_create");
        return voucherMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    public Page<VoucherE> queryPageByRecordId(PageF<SearchF<?>> form, List<Long> voucherIds) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.in("id", voucherIds);
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.orderByDesc("gmt_create");
        return voucherMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    public Long staticVoucherAmount(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        return voucherMapper.staticVoucherAmount(queryModel);
    }

    public void updateVoucherNoByIds(List<Long> ids, String reason, boolean success) {
        voucherMapper.updateVoucherNoByIds(ids, reason, success ? 1 : 2);
    }

    public List<VoucherE> listByBillId(Long billId, BillTypeEnum billTypeEnum) {
        LambdaQueryWrapper<VoucherE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VoucherE::getBillId, billId);
        queryWrapper.eq(VoucherE::getBillType, billTypeEnum.getCode());
        queryWrapper.eq(VoucherE::getInferenceState, 1);
        queryWrapper.eq(VoucherE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        return list(queryWrapper);
    }

    public void updateVoucherNoInferStateByIds(List<Long> ids, int inferState) {
        voucherMapper.updateVoucherNoByIds(ids, "", inferState);
    }
}
