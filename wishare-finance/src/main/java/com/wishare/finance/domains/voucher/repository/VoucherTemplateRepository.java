package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherTemplateMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 凭证模板表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Service
public class VoucherTemplateRepository extends ServiceImpl<VoucherTemplateMapper, VoucherTemplate> {


    public Page<VoucherTemplate> getPage(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchFPageF), RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public boolean updateByDisabled(Long voucherTemplateId, Integer disabled) {
        return update(new LambdaUpdateWrapper<VoucherTemplate>().set(VoucherTemplate::getDisabled, disabled).eq(VoucherTemplate::getId, voucherTemplateId));
    }
}
