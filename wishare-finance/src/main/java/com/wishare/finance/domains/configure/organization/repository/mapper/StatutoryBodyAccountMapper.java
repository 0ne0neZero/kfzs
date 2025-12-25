package com.wishare.finance.domains.configure.organization.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountPageF;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Mapper
public interface StatutoryBodyAccountMapper extends BaseMapper<StatutoryBodyAccountE> {

    /**
     * 分页查询银行账户
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<StatutoryBodyAccountE> queryPage(Page<?> of,
        @Param("ew") QueryWrapper<?> queryModel);

    Boolean insertBatch(@Param("entities") List<StatutoryBodyAccountE> statutoryBodyAccountEList);

    @InterceptorIgnore(tenantLine = "on")
    Page<StatutoryBodyAccountE> queryPageWithoutTenantId(Page<?> of,
        @Param("ew") LambdaQueryWrapper<StatutoryBodyAccountE> wrapper);

    /**
     * 根据名称和编码获取银行账户信息
     * @param code 银行账户编码
     * @param name 银行账户名称
     * @return 银行账户信息
     */
    List<StatutoryBodyAccountE> selectByCodeAndName(@Param("code") String code, @Param("name")String name);
}
