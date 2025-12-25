package com.wishare.finance.domains.invoicereceipt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceZoningE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceZoningMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InvoiceZoningRepository extends ServiceImpl<InvoiceZoningMapper, InvoiceZoningE> {
    public InvoiceZoningE getByAreaName(String areaName){
        return baseMapper.getByAreaName(areaName);
    }

    /**
     *
     * @param superiorCode
     * @return
     */
    public List<InvoiceZoningE> getInvoiceZoningBySuperiorCode(String superiorCode) {
        return baseMapper.getInvoiceZoningBySuperiorCode(superiorCode);
    }

    public List<InvoiceZoningE> getTree(){

        List<InvoiceZoningE> regionList = baseMapper.getTree();
        List<InvoiceZoningE> collect = regionList.stream().filter(area -> Objects.equals(area.getAreaLevel()    , "1"))
                .peek(menu -> menu.setChildren(getChildren(menu, regionList))).collect(Collectors.toList());
        return collect;
    }


    private List<InvoiceZoningE> getChildren(InvoiceZoningE menu, List<InvoiceZoningE> all) {
        return all.stream().filter(cate -> cate.getSuperiorCode().equals(menu.getAreaCode()))
                .peek(cate -> cate.setChildren(getChildren(cate, all))).collect(Collectors.toList());
    }
}
