package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiveDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Mapper
public interface InvoiceReceiveMapper extends BaseMapper<InvoiceReceiveE> {

    /**
     * 查询该票本全部领取数量
     *
     * @param receiveInvoiceBookId
     * @return
     */
    Long getReceiveNumberTotal(@Param("receiveInvoiceBookId") Long receiveInvoiceBookId);

    /**
     * 分页查询发票领用列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<InvoiceReceiveDto> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 根据项目id获取领用记录
     *
     * @param communityId
     * @param type
     * @return
     */
    List<InvoiceReceiveE> getIdByCommunity(@Param("communityId") String communityId,@Param("type") Integer type);

}
