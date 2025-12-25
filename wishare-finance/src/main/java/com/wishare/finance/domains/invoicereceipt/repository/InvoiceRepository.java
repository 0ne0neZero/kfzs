package com.wishare.finance.domains.invoicereceipt.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.dto.*;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceListF;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.enums.InvoiceReceiptSourceEnum;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.InvoiceMapper;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceReceiptDetailQuery;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.consts.Const;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Service
@Slf4j
public class InvoiceRepository extends ServiceImpl<InvoiceMapper, InvoiceE> {

    @Autowired
    private InvoiceReceiptRepository invoiceReceiptRepository;

    @Autowired
    private InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    @Value("${invoice.supplier}")
    private String supplier;

    /**
     * 分页查询开票列表
     *
     * @param form
     * @return
     */
    public Page<InvoiceDto> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.orderByDesc("ir.gmt_create");
        Page<InvoiceDto> invoiceDtoPage = baseMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
        handleDetail(invoiceDtoPage.getRecords());
        return invoiceDtoPage;
    }

    /**
     * 根据票据id获取账票据信息
     * @param invoiceId
     * @return
     */
    public InvoiceDto getByInvoiceId(Long invoiceId){
        return baseMapper.getByInvoiceId(invoiceId);
    }

    /**
     * 获取数据信息
     * @param wrapper
     * @return
     */
    public List<InvoiceE> getListByWrapper(LambdaQueryWrapper<InvoiceE> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据发票号码查询发票数量
     * @param invoiceNo
     * @return
     */
    public long countByInvoiceNo(String invoiceNo) {
        return baseMapper.selectCount(new LambdaUpdateWrapper<InvoiceE>().eq(InvoiceE::getInvoiceNo, invoiceNo));
    }

    /**
     * 根据发票id获取发票信息以及发票明细列表
     * @param invoiceId
     * @return
     */
    public InvoiceAndDetailListDto getByIdForEntityAndDetail(Long invoiceId,String tenantId){
        //处理支付回调拿不到租户id的问题
        IdentityInfo newIdentityInfo = new IdentityInfo();
        newIdentityInfo.setTenantId(tenantId);
        newIdentityInfo.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        // 组织权限和角色权限不能为空
        newIdentityInfo.setOrgIds(List.of(0L));
        newIdentityInfo.setRoleIds(List.of("0"));
        ThreadLocalUtil.set(Const.IDENTITY_INFO, newIdentityInfo);

        InvoiceAndDetailListDto invoiceDto = baseMapper.getByInvoiceIdForMessage(invoiceId);
        if (ObjectUtils.isEmpty(invoiceDto)){
            throw BizException.throw400("该发票不存在");
        }
        if (ObjectUtils.isNotEmpty(invoiceDto.getInvoiceReceiptId())){
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = invoiceReceiptDetailRepository.getBillIdsByInvoiceReceiptId(invoiceDto.getInvoiceReceiptId());

            String rateString = "";
            if (CollectionUtils.isNotEmpty(invoiceReceiptDetailEList)){
                List<InvoiceReceiptDetailForMessageDto> invoiceReceiptDetailForMessageDtos = Global.mapperFacade.mapAsList(invoiceReceiptDetailEList, InvoiceReceiptDetailForMessageDto.class);
//                rateString = invoiceReceiptDetailForMessageDtos.get(0).getTaxRate();
                rateString = String.format("%.2f", Double.valueOf((invoiceReceiptDetailForMessageDtos.get(0).getTaxRate()))*100)+"%";
                if (invoiceReceiptDetailForMessageDtos.size()>1){
                    for (int i = 1; i < invoiceReceiptDetailForMessageDtos.size(); i++) {
//                        rateString = rateString + "，" + invoiceReceiptDetailForMessageDtos.get(i).getTaxRate();
                        rateString = rateString + "，" + String.format("%.2f", Double.valueOf((invoiceReceiptDetailForMessageDtos.get(i).getTaxRate()))*100)+"%";
                    }
                }
            }
            invoiceDto.setTaxRateString(rateString);
        }

        invoiceDto.setInvoicePrice(String.format("%.2f", Double.valueOf((invoiceDto.getPriceTaxAmount() / 100))));
        return invoiceDto;
    }

    /**
     * 处理票据明细的账单相关信息
     *
     * @param records
     */
    private void handleDetail(List<InvoiceDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> invoiceReceiptIds = records.stream().map(InvoiceDto::getId).collect(Collectors.toList());
            InvoiceReceiptDetailQuery invoiceReceiptDetailQuery = new InvoiceReceiptDetailQuery(invoiceReceiptDetailRepository, invoiceReceiptIds);
            records.forEach(receiptDto -> {
                receiptDto.setTypeName(InvoiceLineEnum.valueOfByCode(receiptDto.getType()).getDes());
                receiptDto.setStateName(InvoiceReceiptStateEnum.valueOfByCode(receiptDto.getState()).getDes());
                receiptDto.setSysSourceName(SysSourceEnum.valueOfByCode(receiptDto.getSysSource()).getDes());
                receiptDto.setSourceName(InvoiceReceiptSourceEnum.valueOfByCode(receiptDto.getSource()).getName());
                List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailQuery.get(receiptDto.getId());
                if (CollectionUtils.isNotEmpty(invoiceReceiptDetailES)) {
                    receiptDto.setInvoiceReceiptDetailVS(Global.mapperFacade.mapAsList(invoiceReceiptDetailES, InvoiceReceiptDetailV.class));
                    receiptDto.setBillNoStr(invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillNo).map(String::valueOf).collect(Collectors.joining(",")));
                    receiptDto.setPayerType(invoiceReceiptDetailES.stream()
                            .map(InvoiceReceiptDetailE::getPayerType)
                            .filter(Objects::nonNull)
                            .distinct()
                            .map(VoucherBillCustomerTypeEnum::getNameByCode)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining(",")));
                }
            });
        }
    }

    /**
     * 根据条件查询开票列表
     *
     * @param form
     * @return
     */
    public List<BillInvoiceDto> listByBillIds(InvoiceListF form) {
        return baseMapper.listByBillIds(form);
    }


    /**
     * 根据开票状态查询开票数据
     *
     * @param invoicingStates
     * @return
     */
    public List<InvoiceE> getInvoiceByState(List<Integer> invoicingStates, Date beginDate) {
        return baseMapper.getInvoiceByState(invoicingStates, beginDate);
    }

    /**
     * 统计发票信息
     *
     * @param form
     * @return
     */
    public InvoiceStatisticsDto statistics(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("i.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.in("ir.state", Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode(), InvoiceReceiptStateEnum.已红冲.getCode(), InvoiceReceiptStateEnum.部分红冲.getCode()));
        return baseMapper.statistics(queryModel);
    }

    /**
     * 作废发票
     *
     * @param invoiceReceiptE
     */
    public Boolean voidReceipt(InvoiceReceiptE invoiceReceiptE) {
        invoiceReceiptE.setState(InvoiceReceiptStateEnum.已作废.getCode());
        return invoiceReceiptRepository.updateById(invoiceReceiptE);
    }

    /**
     * 根据发票收据主表获取发票信息
     *
     * @param invoiceReceiptId
     * @return
     */
    public InvoiceE getByInvoiceReceiptId(Long invoiceReceiptId) {
        LambdaQueryWrapper<InvoiceE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceE::getInvoiceReceiptId, invoiceReceiptId);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据发票号查询
     *
     * @param invoiceNo
     * @return
     */
    public InvoiceE getByInvoiceNo(String invoiceNo) {
        LambdaQueryWrapper<InvoiceE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceE::getInvoiceNo, invoiceNo);
        wrapper.eq(InvoiceE::getDeleted, 0);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 更新开票结果
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param invoiceState
     * @param invoiceResultRVList
     * @param invoiceUrl
     */
    public void updateInvoiceInfo(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, Integer invoiceState, List<QueryInvoiceResultV> invoiceResultRVList, String invoiceUrl) {
        if (invoiceResultRVList.size() == 1) {
            QueryInvoiceResultV invoiceRes = invoiceResultRVList.get(0);
            invoiceE.setInvoiceCode(invoiceRes.getInvoiceCode());
            invoiceE.setInvoiceNo(invoiceRes.getInvoiceNo());
            invoiceE.setMachineCode(invoiceRes.getMachineCode());
            invoiceE.setInvoiceUrl(invoiceUrl);
            invoiceE.setNuonuoUrl(invoiceRes.getPdfUrl());
            invoiceE.setFailReason(invoiceRes.getFailCause());
            invoiceE.setInvoiceUrl(invoiceUrl);
            invoiceE.setSalerName(invoiceRes.getSaleName());
            invoiceE.setThridReturnParameter(JSON.toJSONString(invoiceRes));
            if(StringUtils.isNotBlank(invoiceRes.getSaleName())){
                invoiceE.setSalerName(invoiceRes.getSaleName());
            }
            invoiceReceiptE.setClerk(invoiceRes.getClerk());
            invoiceReceiptE.setState(invoiceState);
            invoiceReceiptE.setBillingTime(millisecondsToLocalDateTime(invoiceRes.getInvoiceTime()));
            invoiceReceiptE.setInvoiceReceiptNo(invoiceRes.getInvoiceNo());
            // 百望设置为预览地址
            if ("baiwang".equals(supplier)) {
                invoiceE.setNuonuoUrl(invoiceRes.getPictureUrl());
            }
        } else {
            List<String> invoiceCodeList = Lists.newArrayList();
            List<String> invoiceNoList = Lists.newArrayList();
            List<String> pdfUrlList = Lists.newArrayList();
            List<String> nuonuoUrlList = Lists.newArrayList();
            List<String> failReasonList = Lists.newArrayList();
            for (QueryInvoiceResultV invoiceResultRV : invoiceResultRVList) {
                invoiceCodeList.add(invoiceResultRV.getInvoiceCode());
                invoiceNoList.add(invoiceResultRV.getInvoiceNo());
                pdfUrlList.add(invoiceResultRV.getPdfUrl());
                failReasonList.add(invoiceResultRV.getFailCause());
                // 百望设置为预览地址
                if ("baiwang".equals(supplier)) {
                    nuonuoUrlList.add(invoiceResultRV.getPictureUrl());
                } else {
                    nuonuoUrlList.add(invoiceResultRV.getPdfUrl());
                }
            }
            if(StringUtils.isNotBlank(invoiceResultRVList.get(0).getSaleName())){
                invoiceE.setSalerName(invoiceResultRVList.get(0).getSaleName());
            }
            invoiceE.setInvoiceCode(StringUtils.join(invoiceCodeList, ","));
            invoiceE.setInvoiceNo(StringUtils.join(invoiceNoList, ","));
            invoiceE.setInvoiceUrl(invoiceUrl);
            invoiceE.setNuonuoUrl(StringUtils.join(nuonuoUrlList, ","));
            invoiceE.setFailReason(StringUtils.join(failReasonList, ","));
            invoiceE.setInvoiceUrl(invoiceUrl);
            invoiceE.setSalerName(invoiceResultRVList.get(0).getSaleName());
            invoiceE.setMachineCode(invoiceResultRVList.get(0).getMachineCode());
            invoiceReceiptE.setClerk(invoiceResultRVList.get(0).getClerk());
            invoiceReceiptE.setState(invoiceState);
            invoiceReceiptE.setBillingTime(millisecondsToLocalDateTime(invoiceResultRVList.get(0).getInvoiceTime()));
            invoiceReceiptE.setInvoiceReceiptNo(StringUtils.join(invoiceNoList, ","));
        }
        baseMapper.updateById(invoiceE);
        invoiceReceiptRepository.updateById(invoiceReceiptE);
    }

    /**
     * 时间戳(毫秒) 转 LocalDateTime
     *
     * @param milliseconds
     * @return
     */
    private LocalDateTime millisecondsToLocalDateTime(String milliseconds) {
        if (StringUtils.isNotBlank(milliseconds)) {
            Long aLong = Long.valueOf(milliseconds);
            LocalDateTime localDateTime = Instant.ofEpochMilli(aLong).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            return localDateTime;
        }
        return null;
    }


    /**
     * 根据蓝票id获取原蓝票数据
     *
     * @param blueInvoiceReceiptId
     * @return
     */
    public List<InvoiceE> getByBlueInvoiceReceiptId(Long blueInvoiceReceiptId) {
        LambdaQueryWrapper<InvoiceE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceE::getInvoiceReceiptId, blueInvoiceReceiptId);
        wrapper.orderByDesc(InvoiceE::getGmtCreate);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 根据蓝票id获取红票数据
     *
     * @param blueInvoiceReceiptId
     * @return
     */
    public List<InvoiceInfoDto> getRedInvoiceInfo(Long blueInvoiceReceiptId) {
        return baseMapper.getInvoiceInfo(blueInvoiceReceiptId, null,
                null, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode()));
    }

    /**
     * 根据蓝票id获取红票数据
     *
     * @param blueInvoiceReceiptId
     * @return
     */
    public List<InvoiceInfoDto> getRedInvoiceInfo(Long blueInvoiceReceiptId, Long billId) {
        return baseMapper.getInvoiceInfo(blueInvoiceReceiptId, null,
                billId, Lists.newArrayList(InvoiceReceiptStateEnum.开票成功.getCode()));
    }

    /**
     * 根据蓝票信息获取红票信息
     * @param blueInvoiceReceiptId 蓝票id集合
     * @return List
     */
    public List<InvoiceAndReceiptDto> getRedInvoiceInfoByBlueInvoiceReceiptId(List<Long> blueInvoiceReceiptId) {
        return baseMapper.getRedInvoiceInfoByBlueInvoiceReceiptId(blueInvoiceReceiptId);
    }

    /**
     * 根据账单id列表
     * @param billIds
     * @return
     */
    public List<InvoiceBillDetailDto> getInvoiceBillDetails(List<Long> billIds) {
        return CollectionUtils.isNotEmpty(billIds) ? baseMapper.queryInvoiceBillDetails(billIds) : new ArrayList<>();
    }


    /**
     * 根据invoiceReceiptId获取发票推送所需信息
     * @param invoiceReceiptIds
     * @return
     */
    public List<InvoiceMessageDto> getInvoiceMessages(List<Long> invoiceReceiptIds) {
        return CollectionUtils.isNotEmpty(invoiceReceiptIds) ? baseMapper.getInvoiceMessages(invoiceReceiptIds) : new ArrayList<>();
    }

    /**
     * 根据票据id列表获取账票据信息
     * @param invoiceIds 票据id列表
     * @return
     */
    public List<InvoiceDto> getByInvoiceIds(List<Long> invoiceIds) {
        return baseMapper.getByInvoiceIds(invoiceIds);
    }

    /**
     * 根据发票流水号获取发票信息
     *
     * @param invoiceSerialNum
     * @return
     */
    public List<InvoiceE> listByInvoiceSerialNums(String invoiceSerialNum) {
        return baseMapper.listByInvoiceSerialNums(invoiceSerialNum);
    }

    /**
     * 物理删除
     * @param invoiceReceiptId
     * @return
     */
    public void deletePhysically(Long invoiceReceiptId) {
        baseMapper.deletePhysically(invoiceReceiptId);
    }

    public List<InvoiceDto> queryQuotaListByCommunityId(String communityId) {
        // 获取发票数据
        List<InvoiceDto>invoiceDtoList=baseMapper.queryQuotaListByCommunityId(communityId);

        return invoiceDtoList;
    }
}
