package com.wishare.finance.domains.ncc.repository.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Mapper
public interface NcTaskMapper extends BaseMapper<InvoiceBookE> {

    @InterceptorIgnore(tenantLine = "on")
    Page<JSONObject> pageSupplierAndCustomer(Page<?> page);

    @InterceptorIgnore(tenantLine = "on")
    Page<JSONObject> pageSpaceCommunity(Page<JSONObject> page);

    @InterceptorIgnore(tenantLine = "on")
    List<JSONObject> pageSpaceCommunityByName(@Param("name") String name);

    @InterceptorIgnore(tenantLine = "on")
    Page<JSONObject> pageOrgFinance(Page<JSONObject> page);

    @InterceptorIgnore(tenantLine = "on")
    List<JSONObject> getOrgFinanceByName(@Param("name") String name);
}
