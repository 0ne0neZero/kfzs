package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceRedApplyE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/15
 */
@Mapper
public interface InvoiceRedApplyMapper extends BaseMapper<InvoiceRedApplyE> {

    /**
     * 根据申请单状态查询
     * 不带租户
     *
     * @param statusList
     * @param beginDate
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InvoiceRedApplyE> getApplyByStatus(@Param("statusList") List<String> statusList, @Param("beginDate") Date beginDate);
}
