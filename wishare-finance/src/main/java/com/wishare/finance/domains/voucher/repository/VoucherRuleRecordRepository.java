package com.wishare.finance.domains.voucher.repository;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleStateEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherRuleRecordMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

/**
 * 凭证规则运行记录资源库
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Service
public class VoucherRuleRecordRepository extends ServiceImpl<VoucherRuleRecordMapper, VoucherRuleRecord> {


    /**
     * 分页查询运行记录
     * @param searchPageF 查询条件
     * @return 分页记录
     */
    public Page<VoucherRuleRecord> pageBySearch(PageF<SearchF<?>> searchPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchPageF), RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()));
    }

    /**
     * 设置运行记录为失败
     * @param voucherRuleRecordId 运行记录
     */
    public void setFail(Long voucherRuleRecordId, String failMessage) {
        VoucherRuleRecord record = baseMapper.selectOne(
                new LambdaUpdateWrapper<VoucherRuleRecord>()
                        .eq(VoucherRuleRecord::getId, voucherRuleRecordId));
        if (ObjectUtil.isNotNull(record)){
            record.setState(VoucherRuleStateEnum.处理失败.getCode());
            record.setRemark(failMessage);
            baseMapper.updateById(record);
        }

    }
}
