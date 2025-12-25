package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Linitly
 * @date: 2023/8/7 17:00
 * @descrption:
 */
@Mapper
public interface ReceiptTemplateMapper extends BaseMapper<ReceiptTemplateE> {

    Page<ReceiptTemplateE> pageList(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
}
