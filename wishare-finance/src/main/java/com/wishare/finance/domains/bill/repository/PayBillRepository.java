package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.PayBillDto;
import com.wishare.finance.domains.bill.entity.PayBill;
import com.wishare.finance.domains.bill.repository.mapper.PayBillMapper;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayBillRepository extends ServiceImpl<PayBillMapper, PayBill> {

    /**
     * 分页查询已审核付款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    public IPage<PayBillDto> queryPage(Page<Object> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.queryPage(page,queryWrapper);
    }

    /**
     * 统计付款单信息
     *
     * @param wrapper 统计条件
     * @return BillTotalDto
     */
    public BillTotalDto queryTotal(QueryWrapper<?> wrapper) {
        return baseMapper.queryTotal(wrapper);
    }

    /**
     * 根据id获取付款单
     *
     * @param payBillId 付款单id
     * @return PayBillDto
     */
    public PayBillDto queryById(Long payBillId) {
        return baseMapper.queryById(payBillId);
    }

    /**
     * 根据id获取付款单
     *
     * @param payBillIdList 付款单id
     * @return PayBillDto
     */
    public List<PayBillDto> queryByIdList(List<Long> payBillIdList) {
        return baseMapper.queryByIdList(payBillIdList);
    }

    /**
     * 获取结算账单推凭信息
     * @param page
     * @param queryModel
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceInfo(PageF<SearchF<BillInferenceV>> page, QueryWrapper<BillInferenceV> queryModel) {
        return baseMapper.pageBillInferenceInfo(Page.of(page.getPageNum(), page.getPageSize()), queryModel);
    }

    /**
     * 获取冲销作废账单推凭信息
     * @param page
     * @param queryModel
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceOffInfo(PageF<SearchF<BillInferenceV>> page, QueryWrapper<BillInferenceV> queryModel) {
        return baseMapper.pageBillInferenceOffInfo(Page.of(page.getPageNum(), page.getPageSize()), queryModel);
    }

    /**
     * 查询凭证付款结算业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherPayBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherPayBillByQuery(wrapper,
                VoucherEventTypeEnum.付款结算.getCode(),
                VoucherBusinessBillTypeEnum.付款单.getCode(),tableName);
    }

    /**
     * 修改收款账户ID
     * @param sbAccountId
     * @param idList
     */
    public void updateAccountId(Long sbAccountId, Long pnAccountId, List<Long> idList) {
        baseMapper.updateAccountId(sbAccountId, pnAccountId, idList);
    }
}
