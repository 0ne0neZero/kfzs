package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherSchemeMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 凭证核算方案表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherSchemeRepository extends ServiceImpl<VoucherSchemeMapper, VoucherScheme> {


    public boolean updateByDisabled(Long voucherSchemeId, Integer disabled) {
        return update(new LambdaUpdateWrapper<VoucherScheme>().set(VoucherScheme::getDisabled, disabled).eq(VoucherScheme::getId, voucherSchemeId));
    }


    public Page<VoucherScheme> getPage(PageF<SearchF<?>> searchFPageF) {
       return baseMapper.selectPageBySearchF(RepositoryUtil.convertMPPage(searchFPageF), RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

}
