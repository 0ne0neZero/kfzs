package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceZoningE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvoiceZoningMapper extends BaseMapper<InvoiceZoningE> {
    @InterceptorIgnore(tenantLine = "on")
    InvoiceZoningE getByAreaName(@Param("areaName") String areaName);
    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceZoningE> getInvoiceZoningBySuperiorCode(@Param("superiorCode")String superiorCode);

    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceZoningE> getTree();

}
