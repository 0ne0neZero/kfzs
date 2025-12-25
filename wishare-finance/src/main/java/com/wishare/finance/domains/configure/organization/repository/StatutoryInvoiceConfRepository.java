package com.wishare.finance.domains.configure.organization.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryInvoiceConfListF;
import com.wishare.finance.domains.configure.organization.entity.StatutoryInvoiceConfE;
import com.wishare.finance.domains.configure.organization.repository.mapper.StatutoryInvoiceConfMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Service
public class StatutoryInvoiceConfRepository extends ServiceImpl<StatutoryInvoiceConfMapper, StatutoryInvoiceConfE> {

    @Autowired
    private StatutoryInvoiceConfMapper statutoryInvoiceConfMapper;

    /**
     * 分页查询电子开票设置
     *
     * @param form
     * @return
     */
    public Page<StatutoryInvoiceConfE> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        return  statutoryInvoiceConfMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 列表查询电子开票设置
     *
     * @param form
     * @return
     */
    public List<StatutoryInvoiceConfE> queryList(StatutoryInvoiceConfListF form) {
        LambdaQueryWrapper<StatutoryInvoiceConfE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatutoryInvoiceConfE::getStatutoryBodyId, form.getStatutoryBodyId());
        wrapper.orderByDesc(StatutoryInvoiceConfE::getGmtCreate);
        return statutoryInvoiceConfMapper.selectList(wrapper);
    }

    /**
     * 根据机器编码获取相关电票配置
     *
     * @param machineCode
     * @return
     */
    public StatutoryInvoiceConfE getByMachineCode(String machineCode) {
        LambdaQueryWrapper<StatutoryInvoiceConfE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatutoryInvoiceConfE::getMachineCode, machineCode);
        return  statutoryInvoiceConfMapper.selectOne(wrapper);
    }
}
