package com.wishare.finance.domains.configure.organization.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.organization.entity.StatutoryInvoiceConfE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Mapper
public interface StatutoryInvoiceConfMapper extends BaseMapper<StatutoryInvoiceConfE> {

    /**
     * 分页查询电子开票设置
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<StatutoryInvoiceConfE> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);
}
