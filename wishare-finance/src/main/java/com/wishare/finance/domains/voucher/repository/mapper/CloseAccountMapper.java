package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.CloseAccount;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface CloseAccountMapper extends BaseMapper<CloseAccount> {

    /**
     * 查询交账列表
     * @param convertMPPage
     * @param wrapper
     * @return
     */
    Page<CloseAccount> selectPageBySearch(Page<SearchF<?>> convertMPPage, @Param(Constants.WRAPPER)QueryWrapper<?> wrapper);

    void saveBatchCloseAccounts(@Param("closeAccounts") Collection<CloseAccount> closeAccounts);

}
