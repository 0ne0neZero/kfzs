package com.wishare.finance.domains.ncc.repository;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.ncc.repository.mapper.NcTaskMapper;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2023/1/31 17:42
 * @version: 1.0.0
 */
@Service
public class NcTaskRepository extends ServiceImpl<NcTaskMapper, InvoiceBookE> {

    /**
     * 获取客商信息
     *
     * @param page
     * @return
     */
    public Page<JSONObject> pageSupplierAndCustomer(Page<?> page) {
        return baseMapper.pageSupplierAndCustomer(page);
    }

    /**
     * 获取项目信息
     *
     * @param page
     * @return
     */
    public Page<JSONObject> pageSpaceCommunity(Page<JSONObject> page) {
        return baseMapper.pageSpaceCommunity(page);
    }

    /**
     * 获取项目信息 根据名称
     *
     * @param name
     * @return
     */
    public List<JSONObject> pageSpaceCommunityByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = null;
        }
        return baseMapper.pageSpaceCommunityByName(name);
    }

    /**
     * 获取成本中心
     * @param page
     * @return
     */
    public Page<JSONObject> pageOrgFinance(Page<JSONObject> page) {
        return baseMapper.pageOrgFinance(page);
    }

    /**
     * 根据名称获取成本中心
     * @param name
     * @return
     */
    public List<JSONObject> getOrgFinanceByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = null;
        }
        return baseMapper.getOrgFinanceByName(name);
    }
}
