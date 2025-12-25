package com.wishare.finance.domains.invoicereceipt.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.dto.ReceiptMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptMapper;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceReceiptDetailQuery;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
public class ReceiptRepository extends ServiceImpl<ReceiptMapper, ReceiptE> {

    @Autowired
    private InvoiceReceiptRepository invoiceReceiptRepository;

    @Autowired
    private InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;


    /**
     * 分页查询收据列表
     *
     * @param form
     * @return
     */
    public Page<ReceiptDto> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("r.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.orderByDesc("ir.gmt_create");
        Page<ReceiptDto> receiptDtoPage = baseMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
        handleDetail(receiptDtoPage.getRecords());
        return receiptDtoPage;
    }


    /**
     *
     * @param queryModel
     * @return
     */
    public ReceiptVDto queryByElement(QueryWrapper<?> queryModel){

        final List<ReceiptVDto> receiptVDtos = baseMapper.queryByElement(queryModel);
        if(CollectionUtils.isEmpty(receiptVDtos)){
            return null;
        }
        //跨房间开收据获取的数据会查出来多条
        final ReceiptVDto receiptVDto = receiptVDtos.get(0);
        receiptVDto.setRoomName(receiptVDtos.stream().map(ReceiptVDto::getRoomName).distinct().collect(Collectors.joining(",")));
        return receiptVDto;
    }


    /**
     * 处理票据明细的账单相关信息
     *
     * @param records
     */
    private void handleDetail(List<ReceiptDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> invoiceReceiptIds = records.stream().map(ReceiptDto::getId).collect(Collectors.toList());
            InvoiceReceiptDetailQuery invoiceReceiptDetailQuery = new InvoiceReceiptDetailQuery(invoiceReceiptDetailRepository, invoiceReceiptIds);
            records.forEach(receiptDto -> {
                List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailQuery.get(receiptDto.getId());
                if (CollectionUtils.isNotEmpty(invoiceReceiptDetailES)) {
                    String billNo = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillNo).filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                    receiptDto.setInvoiceReceiptDetailVS(Global.mapperFacade.mapAsList(invoiceReceiptDetailES, InvoiceReceiptDetailV.class));
                    // 计算缴费日期
                    LocalDateTime billPayTime = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillPayTime).filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null);
                    receiptDto.setBillPayTime(billPayTime);
                    receiptDto.setBillNos(billNo);
                }
            });
        }
    }

    /**
     * 统计收据信息
     *
     * @param form
     * @return
     */
    public ReceiptStatisticsDto statistics(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("r.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.in("ir.state", Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(),InvoiceReceiptStateEnum.已红冲.getCode(),InvoiceReceiptStateEnum.部分红冲.getCode()));
        return baseMapper.statistics(queryModel);
    }

    /**
     * 作废收据
     *
     * @param invoiceReceiptE
     * @return
     */
    public Boolean voidReceipt(InvoiceReceiptE invoiceReceiptE) {
        invoiceReceiptE.setState(InvoiceReceiptStateEnum.已作废.getCode());
        return invoiceReceiptRepository.updateById(invoiceReceiptE);
    }

    /**
     * 根据InvoiceReceiptId获取信息
     *
     * @param invoiceReceiptId
     * @return
     */
    public ReceiptE getByInvoiceReceiptId(Long invoiceReceiptId) {
        LambdaQueryWrapper<ReceiptE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReceiptE::getInvoiceReceiptId, invoiceReceiptId);
        return baseMapper.selectOne(wrapper);
    }


    /**
     * 获取数据信息
     * @param wrapper
     * @return
     */
    public List<ReceiptE> getListByWrapper(LambdaQueryWrapper<ReceiptE> wrapper) {
        return baseMapper.selectList(wrapper);
    }



    /**
     * 根据收据号查询收据
     *
     * @param receiptNo
     * @return
     */
    public ReceiptE getByReceiptNo(Long receiptNo) {
        LambdaQueryWrapper<ReceiptE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReceiptE::getReceiptNo, receiptNo);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 通过账单ids查询是否已开收据
     *
     * @param billIds
     */
    public Integer getByBillIds(List<Long> billIds) {
        return baseMapper.getByBillIds(billIds, InvoiceReceiptStateEnum.开票成功.getCode());
    }

    /**
     * 通过发票收据主表id设置收据表pdf url
     * @param invoiceReceiptId
     * @param receiptUrl
     */
    public void setPdfUrl(Long invoiceReceiptId, String receiptUrl) {
        LambdaUpdateWrapper<ReceiptE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ReceiptE::getReceiptUrl, receiptUrl);
        updateWrapper.eq(ReceiptE::getInvoiceReceiptId, invoiceReceiptId);
        baseMapper.update(new ReceiptE(), updateWrapper);
    }

    /**
     * 根据invoiceReceiptId获取收据推送所需信息
     * @param invoiceReceiptIds
     * @return
     */
    public List<ReceiptMessageDto> getReceiptMessages(List<Long> invoiceReceiptIds) {
        return CollectionUtils.isNotEmpty(invoiceReceiptIds) ? baseMapper.getReceiptMessages(invoiceReceiptIds) : new ArrayList<>();
    }
}
