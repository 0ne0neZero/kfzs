package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.domains.bill.entity.BillDeductionE;
import com.wishare.finance.domains.invoicereceipt.dto.ReceiptMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Mapper
public interface BillDeductionMapper extends BaseMapper<BillDeductionE> {

    /**
     * 分页查询收据列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<BillDeductionE> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 通过账单ids查询收据
     *
     * @param
     * @param
     */
    Integer getByBillIds(@Param("billIds") List<Long> billIds);

    /**
     * 根据参数查询列表
     * @param
     * @return
     */
    List<BillDeductionE> getList(@Param("ew") QueryWrapper<?> queryModel);
}
