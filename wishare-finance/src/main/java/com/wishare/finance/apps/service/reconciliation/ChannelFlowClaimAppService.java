package com.wishare.finance.apps.service.reconciliation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.fo.ChannelClaimF;
import com.wishare.finance.apps.model.reconciliation.fo.ChannelClaimFlowF;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimStatisticsV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.ReconciliationBillDto;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.BillReconciliationRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.reconciliation.dto.ChannelFlowClaimRecordDto;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileRunStateEnum;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationYinlianRepository;
import com.wishare.finance.domains.reconciliation.service.ReconcileRuleDomainService;
import com.wishare.finance.domains.reconciliation.service.ReconciliationDomainService;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.payment.PaymentReconcileClient;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.payment.ChannelConfigV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class ChannelFlowClaimAppService {

    private final PaymentReconcileClient paymentReconcileClient;
    private final GatherBillRepository gatherBillRepository;
    private final SharedBillAppService sharedBillAppService;
    private final ReconciliationYinlianRepository reconciliationYinlianRepository;
    private final ReconciliationRepository reconciliationRepository;
    private final ReconciliationDetailRepository reconciliationDetailRepository;
    private final ReconcileRuleDomainService reconcileRuleDomainService;
    private final OrgClient orgClient;
    private final BillReconciliationRepository billReconciliationRepository;
    private final ReconciliationDomainService reconciliationDomainService;
    private final GatherDetailRepository gatherDetailRepository;
    private final ReceivableBillRepository receivableBillRepository;


    @Value("${reconciliation.startTime:2024-09-15 00:00:00}")
    private String reconciliationStartTime;

    public PageV<ChannelFlowClaimRecordPageV> pageRecord(PageF<SearchF<?>> queryF) {
        //  根据项目id 查询商户号
        String supCpUnitId = null;
        List<Field> fields = queryF.getConditions().getFields();
        for (Field field : fields) {
            if (field.getName().equals("business_id")) {
                supCpUnitId = field.getValue().toString();
            }
        }
        List<String> list = new ArrayList<>();
        List<ChannelConfigV> channelByBusinessId = paymentReconcileClient.getChannelByBusinessId(supCpUnitId);
        channelByBusinessId.forEach(s -> {
            JSONObject channelParams = s.getChannelParams();
            JSONArray mchInfos = channelParams.getJSONArray("mchInfo");
            if (null != mchInfos && mchInfos.size() > 0) {
                for (int i = 0; i < mchInfos.size(); i++) {
                    log.info("mchInfos.get(i).toString()" + mchInfos.get(i).toString());
                    Object json = JSON.toJSON(mchInfos.get(i));
                    log.info("JSON.toJSON(mchInfos.get(i))" + json.toString());
                    JSONObject jsonObject = JSONObject.parseObject(json.toString());
                    list.add(jsonObject.getString("orgId"));
                }
            }
        });
        if (null == list || list.size() == 0){
            return  null;
        }
        RepositoryUtil.channelFlowClaimConvertSearchType(queryF.getConditions());
        QueryWrapper<?> queryModel = queryF.getConditions().getQueryModel();
        queryModel.in("mid", list);
        Page<?> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        //  根据商户号查询支付流水数据\收款单号
        IPage<ChannelFlowClaimRecordDto> channelFlowClaimRecordDtoIPage = reconciliationYinlianRepository.pageRecord(sharedBillAppService.getShareTableName(supCpUnitId, BillSharedingColumn.收款账单.getTableName())
                , page, queryModel);

        List<ChannelFlowClaimRecordPageV> pageDataList = Global.mapperFacade.mapAsList(channelFlowClaimRecordDtoIPage.getRecords(), ChannelFlowClaimRecordPageV.class);
        Map<String, ChannelFlowClaimRecordDto> map = pageRecordMap(pageDataList);
        final ChannelFlowClaimRecordDto df = new ChannelFlowClaimRecordDto();
        for (int i = 0; i < channelFlowClaimRecordDtoIPage.getRecords().size(); i++) {
            String tradeNo = channelFlowClaimRecordDtoIPage.getRecords().get(i).getTradeNo();
            String billNo = map.getOrDefault(tradeNo, df).getBillNo();
            pageDataList.get(i).setBillNo(billNo);
            pageDataList.get(i).setReconcileTime(map.getOrDefault(tradeNo,df).getReconcileTime());
            if ( StringUtils.isNotBlank(billNo)){
                pageDataList.get(i).setBillNoList(StrUtil.split(billNo,','));
            }

        }
        return PageV.of(channelFlowClaimRecordDtoIPage.getCurrent(), channelFlowClaimRecordDtoIPage.getSize(), channelFlowClaimRecordDtoIPage.getTotal(), pageDataList);
    }


    private Map<String,ChannelFlowClaimRecordDto> pageRecordMap(List<ChannelFlowClaimRecordPageV> pageDataList){
        if (CollUtil.isEmpty(pageDataList)){
            return Collections.emptyMap();
        }
     List<ChannelFlowClaimRecordDto>  list =  reconciliationYinlianRepository.pageRecordMap(pageDataList.stream()
             .map(ChannelFlowClaimRecordPageV::getTradeNo).collect(Collectors.toSet()));
        return list.stream().collect(Collectors.toMap(ChannelFlowClaimRecordDto::getTradeNo, v->v, (a,b)->a));
    }

    private Map<String,ChannelFlowClaimRecordDto> pageRecordMapYY(List<ChannelFlowClaimRecordPageV> pageDataList){
        if (CollUtil.isEmpty(pageDataList)){
            return Collections.emptyMap();
        }
        List<ChannelFlowClaimRecordDto>  list =  reconciliationYinlianRepository.pageRecordMapYY(pageDataList.stream()
                .map(ChannelFlowClaimRecordPageV::getTradeNo).collect(Collectors.toSet()));
        return list.stream().collect(Collectors.toMap(ChannelFlowClaimRecordDto::getTradeNo, v->v, (a,b)->a));
    }


    public ChannelFlowClaimStatisticsV getStatistics(PageF<SearchF<?>> queryF) {
        //  根据项目id 查询商户号
        String supCpUnitId = null;
        List<Field> fields = queryF.getConditions().getFields();
        for (Field field : fields) {
            if (field.getName().equals("business_id")) {
                supCpUnitId = field.getValue().toString();
            }
        }
        List<String> list = new ArrayList<>();
        List<ChannelConfigV> channelByBusinessId = paymentReconcileClient.getChannelByBusinessId(supCpUnitId);
        channelByBusinessId.forEach(s -> {
            JSONObject channelParams = s.getChannelParams();
            JSONArray mchInfos = channelParams.getJSONArray("mchInfo");
            if (null != mchInfos && mchInfos.size() > 0) {
                for (int i = 0; i < mchInfos.size(); i++) {
                    log.info("mchInfos.get(i).toString()" + mchInfos.get(i).toString());
                    Object json = JSON.toJSON(mchInfos.get(i));
                    log.info("JSON.toJSON(mchInfos.get(i))" + json.toString());
                    JSONObject jsonObject = JSONObject.parseObject(json.toString());
                    list.add(jsonObject.getString("orgId"));
                }
            }
        });
        if (null == list || list.size() == 0){
            return new ChannelFlowClaimStatisticsV();
        }
        RepositoryUtil.channelFlowClaimConvertSearchType(queryF.getConditions());
        QueryWrapper<?> queryModel = queryF.getConditions().getQueryModel();
        queryModel.in("mid", list);
        Page<?> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, BillSharedingColumn.收款账单.getTableName());
        ChannelFlowClaimStatisticsV statistics = reconciliationYinlianRepository.getStatistics(shareTableName,queryModel);
        return statistics;
    }

    @Transactional
    public Boolean claim(ChannelClaimFlowF claimFlowF) {
        List<ReconciliationYinlianE> reconciliationYinlianBySeqId;
        // 查询支付流水金额
        if (EnvConst.YUANYANG.equals(EnvData.config)){
            reconciliationYinlianBySeqId =  reconciliationYinlianRepository.getReconciliationYinlianByRefNo(claimFlowF.getTradeNoList());
        } else {
            reconciliationYinlianBySeqId = reconciliationYinlianRepository.getReconciliationYinlianBySeqId(claimFlowF.getTradeNoList());
        }
        if (null != reconciliationYinlianBySeqId.get(0).getState() && 1 == reconciliationYinlianBySeqId.get(0).getState()){
            throw BizException.throw402("该流水已认领!");
        }
        // 查询账单金额
        List<GatherBill> gatherBillByBillNo = gatherBillRepository.getGatherBillByBillNo(claimFlowF.getBillNoList(), claimFlowF.getSupCpUnitId());
        // 判断账单法定单位是否一致
        GatherBill gatherBill = null;
        if (CollUtil.isNotEmpty(gatherBillByBillNo)) {
             gatherBill = gatherBillByBillNo.get(0);
            for (GatherBill bill : gatherBillByBillNo) {
                if (!gatherBill.getStatutoryBodyId().equals(bill.getStatutoryBodyId())) {
                    throw BizException.throw402("收款单法定单位需要一致!");
                }
                if (null != bill.getMcReconcileState() && 2 == bill.getMcReconcileState()){
                    throw BizException.throw402("该收款单已对账!");
                }
            }

        }
        // 判断金额是否一致
        String tradeAmount = reconciliationYinlianBySeqId.get(0).getTradeAmount();
        BigDecimal bigDecimal = new BigDecimal(tradeAmount);
        log.info("支付流水金额:" + bigDecimal.multiply(new BigDecimal("100")));
        long billSum = gatherBillByBillNo.stream().mapToLong(GatherBill::getTotalAmount).sum();
        log.info("账单金额:" + billSum);
        int i = bigDecimal.multiply(new BigDecimal("100")).compareTo(new BigDecimal(String.valueOf(billSum)));
        log.info("判断金额是否相等:" + i);
        // 判断金额是否相等
        if (bigDecimal.multiply(new BigDecimal("100")).compareTo(new BigDecimal(String.valueOf(billSum))) != 0) {
            throw BizException.throw402("认领金额不一致!");
        }

        ReconcileRuleE byMode = reconcileRuleDomainService.getByMode(0);
        // 更新流水数据
        ReconciliationYinlianE reconciliationYinlianE = reconciliationYinlianBySeqId.get(0);
        reconciliationYinlianE.setState(1);
        reconciliationYinlianRepository.updateById(reconciliationYinlianE);
        gatherBillRepository.updateById(gatherBillByBillNo.get(0));
        // 创建对账记录
        ReconciliationE reconciliationE = new ReconciliationE();
        reconciliationE = ReconciliationE.builder(byMode)
                .statutoryBodyId(gatherBill.getStatutoryBodyId())
                .statutoryBodyName(gatherBill.getStatutoryBodyName())
                .communityId(gatherBill.getSupCpUnitId())
                .communityName(gatherBill.getSupCpUnitName())
                .reconcileTime(LocalDateTime.now())
                .costCenterId("")
                .costCenterName("")
                .sbAccountId(new Long(0))
                .state(ReconcileRunStateEnum.已完成.getCode())
                .result(ReconcileResultEnum.已核对.getCode())
                .reconcileRuleId(byMode.getId())
                .reconcileMode(ReconcileModeEnum.商户清分对账.getCode())
                .build();
        log.info("对账记录信息:" + JSON.toJSON(reconciliationE));
        reconciliationE.setBalanceCount(1L);
        reconciliationE.setBillCount(1L);
        reconciliationE.setStatutoryBodyId(gatherBill.getStatutoryBodyId());
        reconciliationE.setStatutoryBodyName(gatherBill.getStatutoryBodyName());
        reconciliationE.setActualTotal(billSum);
        reconciliationE.setFlowClaimTotal(billSum);
        reconciliationRepository.save(reconciliationE);
        // 创建对账明细
        ReconciliationDetailE detailE = new ReconciliationDetailE();
        detailE.setChannelTradeAmount(AmountUtils.toLong(reconciliationYinlianE.getTradeAmount()));
        detailE.setStatutoryBodyId(gatherBill.getStatutoryBodyId());
        detailE.setStatutoryBodyName(gatherBill.getStatutoryBodyName());
        detailE.setCostCenterId("0");
        detailE.setCostCenterName("");
        detailE.setBillId(gatherBill.getId());
        detailE.setBillNo(gatherBill.getBillNo());
        detailE.setBillType(7);
        detailE.setReconciliationId(reconciliationE.getId());
        detailE.setReconcileMode(ReconcileModeEnum.商户清分对账.getCode());
        detailE.setReceivableAmount(billSum);
        detailE.setActualAmount(billSum);
        detailE.setFlowClaimAmount(billSum);
        detailE.doReconcileResult();
        detailE.setCommission(AmountUtils.toLong(reconciliationYinlianE.getCommission()));
        if (EnvConst.YUANYANG.equals(EnvData.config)){
            detailE.setChannelSeqId(reconciliationYinlianE.getSearchReferenceNo());
        } else {
            detailE.setChannelSeqId(reconciliationYinlianE.getSeqId());
        }
        detailE.setSysSource(gatherBill.getSysSource());
        detailE.setChannelMid(reconciliationYinlianE.getMid());
        detailE.setReconcileTime(LocalDateTime.now());

        ArrayList<ReconciliationDetailE> list = new ArrayList<>(gatherBillByBillNo.size());
        for (GatherBill bill : gatherBillByBillNo){
            detailE.setBillNo(bill.getBillNo());
            
            ReconciliationDetailE a = new ReconciliationDetailE();
            BeanUtil.copyProperties(detailE,a,true);
            list.add(a);
        }
        log.info("对账明细信息:" + JSON.toJSON(list));
        reconciliationDetailRepository.saveBatch(list);

        // 插入对账记录，插入对账明细
        for (GatherBill bill : gatherBillByBillNo) {
            bill.setMcReconcileState(2);
            gatherBillRepository.updateById(bill);
        }
        if (EnvConst.YUANYANG.equals(EnvData.config)){
            gatherBill.setBankFlowNo(reconciliationYinlianE.getSearchReferenceNo());
        } else {
            gatherBill.setTradeNo(reconciliationYinlianE.getSeqId());
        }
        gatherBillRepository.updateById(gatherBill);

        return true;
    }


    public  Page<?> creatPage(PageF<SearchF<?>> queryF){
        Page<?> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        return page;
    }

    public OrgFinanceCostRv getOrgFinanceCostRv(Long id) {
        OrgFinanceCostRv orgFinanceCostById = orgClient.getOrgFinanceCostById(id);
        return orgFinanceCostById;
    }

    public  QueryWrapper<?> creatWrapper(PageF<SearchF<?>> queryF) {
        List<Field> fields = queryF.getConditions().getFields();
        String costId = "";
        Integer flag = 1;
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                costId = field.getValue().toString();
            }
            if (field.getName().equals("flag")) {
                flag = Integer.parseInt(field.getValue().toString());
            }
        }
        OrgFinanceCostRv orgFinanceCostRv = getOrgFinanceCostRv(Long.valueOf(costId));
        RepositoryUtil.channelFlowClaimConvertSearchTypeForYY(queryF.getConditions());
        QueryWrapper<?> queryModel = queryF.getConditions().getQueryModel();
        String unionPayMerchantNo = orgFinanceCostRv.getUnionPayMerchantNo();
        if(StringUtils.isEmpty(unionPayMerchantNo)){
            return null;
        }
        List<String> list = new ArrayList<>();
        String[] splits = unionPayMerchantNo.split(",");
        for (int i = 0; i < splits.length; i++) {
            list.add(splits[i]) ;
        }
        queryModel.in("ry.mid", list);
        queryModel.orderByDesc("ry.clear_date");
        queryModel.orderByDesc("ry.trade_date");
        // 添加标识， 判断是线上还是线下
        if (!flag.equals(0)) {
            queryModel.and(wrapper -> wrapper.notLike("ry.order_number", "348M").notLike("ry.order_number", "7459"));
        } else {
            queryModel.and(wrapper -> wrapper.likeRight("ry.order_number", "348M").or().likeRight("ry.order_number", "7459"));
        }
        return queryModel;
    }

    public PageV<ChannelFlowClaimRecordPageV> getPageRecordYY(PageF<SearchF<?>> queryF) {
        List<Field> fields = queryF.getConditions().getFields();
        String costId = "";
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                costId = field.getValue().toString();
            }
        }
        Page<?> page = creatPage(queryF);
        QueryWrapper<?> queryWrapper = creatWrapper(queryF);
        if (null == queryWrapper) {
            return null;
        }
        OrgFinanceCostRv orgFinanceCostRv = getOrgFinanceCostRv(Long.valueOf(costId));
        if (StringUtils.isBlank(orgFinanceCostRv.getCommunityId())){
            return null;
        }
        String supCpUnitId = orgFinanceCostRv.getCommunityId();
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, BillSharedingColumn.收款账单.getTableName());
        //  根据商户号查询支付流水数据\收款单号
        IPage<ChannelFlowClaimRecordDto> channelFlowClaimRecordDtoIPage = reconciliationYinlianRepository.pageRecordForYY(shareTableName, page, queryWrapper);
        List<ChannelFlowClaimRecordPageV> pageDataList = Global.mapperFacade.mapAsList(channelFlowClaimRecordDtoIPage.getRecords(), ChannelFlowClaimRecordPageV.class);

        Map<String, ChannelFlowClaimRecordDto> mapYY = pageRecordMapYY(pageDataList);
        final ChannelFlowClaimRecordDto df = new ChannelFlowClaimRecordDto();
        for (int i = 0; i < channelFlowClaimRecordDtoIPage.getRecords().size(); i++) {
            String payTime = channelFlowClaimRecordDtoIPage.getRecords().get(i).getPayTime();
            // 201912031203150242
            String outputString = removeChars(payTime, 4, 4);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = inputFormat.parse(outputString);
                String outputDate = outputFormat.format(date);
                pageDataList.get(i).setPayTime(outputDate);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String tradeNo = channelFlowClaimRecordDtoIPage.getRecords().get(i).getTradeNo();
            String billNo = mapYY.getOrDefault(tradeNo, df).getBillNo();
            pageDataList.get(i).setBillNo(billNo);
            pageDataList.get(i).setReconcileTime(mapYY.getOrDefault(tradeNo,df).getReconcileTime());
            if ( StringUtils.isNotBlank(billNo)){
                pageDataList.get(i).setBillNoList(StrUtil.split(billNo,','));
            }
        }
        return PageV.of(channelFlowClaimRecordDtoIPage.getCurrent(), channelFlowClaimRecordDtoIPage.getSize(), channelFlowClaimRecordDtoIPage.getTotal(), pageDataList);
    }
    public  String removeChars(String input, int startIndex, int count) {
        if (startIndex < 0 || startIndex >= input.length()) {
            return input;
        }

        int endIndex = Math.min(startIndex + count, input.length());
        return input.substring(0, startIndex) + input.substring(endIndex);
    }

    public ChannelFlowClaimStatisticsV getYYStatistics(PageF<SearchF<?>> queryF) {
        List<Field> fields = queryF.getConditions().getFields();
        String costId = "";
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                costId = field.getValue().toString();
            }
        }
        QueryWrapper<?> queryWrapper = creatWrapper(queryF);
        if (null == queryWrapper) {
            return null;
        }
        OrgFinanceCostRv orgFinanceCostRv = getOrgFinanceCostRv(Long.valueOf(costId));
        if (StringUtils.isBlank(orgFinanceCostRv.getCommunityId())){
            return null;
        }
        String supCpUnitId = orgFinanceCostRv.getCommunityId();
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, BillSharedingColumn.收款账单.getTableName());
        ChannelFlowClaimStatisticsV statistics = reconciliationYinlianRepository.getYYStatistics(shareTableName,queryWrapper);
        return statistics;
    }



    @Async
    public Boolean channelClaimOffLine(ChannelClaimF channelClaimF) {
        List<Long> costCenterIdList = channelClaimF.getCostCenterIdList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (Long costCenterId : costCenterIdList) {
            // 通过成本中心查询项目信息
            OrgFinanceCostRv financeCostRv = orgClient.getOrgFinanceCostById(costCenterId);
            if (Objects.nonNull(financeCostRv) && StringUtils.isNotEmpty(financeCostRv.getCommunityId())){
                // 查询符合对账条件的收款单数据
                List<ReconciliationBillDto> gatherBillForOffLine = billReconciliationRepository.getGatherBillForOffLine(financeCostRv.getCommunityId(),
                        channelClaimF.getStartTime() ==  null ? reconciliationStartTime : DateTimeUtil.formatDateTime(channelClaimF.getStartTime()),
                        channelClaimF.getStartTime() ==  null ?  DateTimeUtil.formatDateTime(LocalDate.now().atStartOfDay()) : DateTimeUtil.formatDateTime(channelClaimF.getStartTime()),
                        costCenterId);

                // 判断是否有符合条件的收款单
                if(CollectionUtils.isNotEmpty(gatherBillForOffLine)) {
                    // 构造对账记录主表数据
                    ReconciliationE reconciliationE = new ReconciliationE();
                    ReconcileRuleE reconcileRuleE = reconcileRuleDomainService.getByMode(1);
                    reconciliationE.builder(reconcileRuleE)
                            .billCount((long) gatherBillForOffLine.size())
                            .costCenterId(String.valueOf(costCenterId))
                            .communityId(financeCostRv.getCommunityId())
                            .state(ReconcileRunStateEnum.运行中.getCode())
                            .result(ReconcileResultEnum.未核对.getCode())
                            .reconcileRuleId(reconcileRuleE.getId())
                            .reconcileMode(1).build();
                    // 定义已核对数量
                    AtomicInteger balanceCount = new AtomicInteger();
                    // 对账记录主表数据写入
                    reconciliationDomainService.addReconciliation(reconciliationE);
                    // 定义对账明细列表
                    ArrayList<ReconciliationDetailE> reconciliationDetailES = new ArrayList<>();

                    // 符合对账条件的收款单循环对比第三方账单
                    gatherBillForOffLine.forEach( s -> {
                        String format = s.getTradeTime().format(formatter);
                        String string = String.valueOf(new BigDecimal(s.getActualAmount()).multiply(new BigDecimal("0.01")).setScale(2,  RoundingMode.HALF_UP));
                        List<ReconciliationYinlianE> reconciliationYinlianES = reconciliationYinlianRepository.list(new LambdaQueryWrapper<ReconciliationYinlianE>()
                                .eq(ReconciliationYinlianE::getTradeAmount, string)
                                .eq(ReconciliationYinlianE::getClearDate, format)
                                .eq(ReconciliationYinlianE::getState, 0)
                                .like(ReconciliationYinlianE::getSearchReferenceNo, s.getBankFlowNo()));

                        // 定义对账明细数据
                        ReconciliationDetailE reconciliationDetailE = new ReconciliationDetailE();
                        // 查询对应的收款单
                        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id", s.getId())
                                .eq("sup_cp_unit_id", s.getSupCpUnitId()));

                        List<GatherDetail> gatherDetails = gatherDetailRepository.list(new LambdaQueryWrapper<GatherDetail>().eq(GatherDetail::getGatherBillId, s.getId())
                                .eq(GatherDetail::getSupCpUnitId, financeCostRv.getCommunityId()));
                        // 查询应收账单
                        List<ReceivableBill> receivableBills = receivableBillRepository.list(new LambdaQueryWrapper<ReceivableBill>().eq(Bill::getSupCpUnitId, financeCostRv.getCommunityId())
                                .in(Bill::getId, gatherDetails.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList())));
                        // 判断是否存在符合规则的数据
                        if (CollectionUtils.isNotEmpty(reconciliationYinlianES) && reconciliationYinlianES.size() == 1) {
                            // 如果规则查出来只有一条, 则认为符合条件
                            balanceCount.set(balanceCount.get() + 1);
                            reconciliationDetailE.setBankSeqId(reconciliationYinlianES.get(0).getSearchReferenceNo());
                            reconciliationDetailE.setResult(2);
                            // 更新应收账单对应状态
                            for (ReconciliationYinlianE reconciliationYinlianE : reconciliationYinlianES) {
                                reconciliationYinlianE.setState(1);
                            }
                            reconciliationDetailE.setCommission(AmountUtils.toLong(reconciliationYinlianES.get(0).getCommission()));
                            reconciliationDetailE.setChannelTradeAmount(AmountUtils.toLong(reconciliationYinlianES.get(0).getTradeAmount()));
                            reconciliationYinlianRepository.saveOrUpdateBatch(reconciliationYinlianES);
                            gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",s.getId()).set("reconcile_state",2)
                                    .set("bank_flow_no", reconciliationYinlianES.get(0).getSearchReferenceNo())
                                    .eq("sup_cp_unit_id",s.getSupCpUnitId()));
                            receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                    .in("id", receivableBills.stream().map(Bill::getId).collect(Collectors.toList()))
                                    .eq("sup_cp_unit_id",s.getSupCpUnitId())
                                    .set("mc_reconcile_state", 1));
                        } else {
                            reconciliationDetailE.setResult(3);
                            gatherBill.setReconcileState(3);
                            reconciliationDetailE.setCommission(0L);
                            gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",s.getId()).set("reconcile_state",3)
                                    .eq("sup_cp_unit_id",s.getSupCpUnitId()));
                            receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                    .in("id", receivableBills.stream().map(Bill::getId).collect(Collectors.toList()))
                                    .eq("sup_cp_unit_id",s.getSupCpUnitId())
                                    .set("mc_reconcile_state", 3));
                        }
                        //更新应收账单

                        reconciliationDetailE.setReconciliationId(reconciliationE.getId());
                        reconciliationDetailE.setStatutoryBodyId(gatherBill.getStatutoryBodyId());
                        reconciliationDetailE.setStatutoryBodyName(gatherBill.getStatutoryBodyName());
                        reconciliationDetailE.setCostCenterId("0");
                        reconciliationDetailE.setCostCenterName("");
                        reconciliationDetailE.setBillId(gatherBill.getId());
                        reconciliationDetailE.setBillNo(gatherBill.getBillNo());
                        reconciliationDetailE.setBillType(7);
                        reconciliationDetailE.setReconciliationId(reconciliationE.getId());
                        reconciliationDetailE.setReconcileMode(ReconcileModeEnum.商户清分对账.getCode());
                        reconciliationDetailE.setReceivableAmount(gatherBill.getTotalAmount());
                        reconciliationDetailE.setActualAmount(gatherBill.getTotalAmount());
                        reconciliationDetailE.setFlowClaimAmount(gatherBill.getTotalAmount());
                        reconciliationDetailE.setReconDetNo(IdentifierFactory.getInstance().serialNumber("reconciliation_detail", "DZ", 22));
                        reconciliationDetailES.add(reconciliationDetailE);
                        // 更新收款单数据、覆盖不完整收款单号

                    });
                    // 更新对账主表 插入批量明细
                    reconciliationDetailRepository.saveBatch(reconciliationDetailES);
                    reconciliationE.setBalanceCount(Long.valueOf(String.valueOf(balanceCount)));
                    reconciliationE.setCommunityName(gatherBillForOffLine.get(0).getSupCpUnitName());
                    reconciliationE.setStatutoryBodyId(gatherBillForOffLine.get(0).getStatutoryBodyId());
                    reconciliationE.setStatutoryBodyName(gatherBillForOffLine.get(0).getStatutoryBodyName());
                    reconciliationE.setState(ReconcileRunStateEnum.已完成.getCode());
                    reconciliationE.setBillCount((long) gatherBillForOffLine.size());
                    reconciliationE.setBalanceCount(Long.valueOf(balanceCount.toString()));
                    reconciliationE.setReconcileTime(LocalDateTime.now());
                    reconciliationE.setFlowClaimTotal(reconciliationDetailES.stream().mapToLong(ReconciliationDetailE::getFlowClaimAmount).sum());
                    reconciliationE.setActualTotal(gatherBillForOffLine.stream().mapToLong(ReconciliationBillDto::getActualAmount).sum());
                    if (reconciliationE.getBalanceCount().equals(reconciliationE.getBillCount())){
                        reconciliationE.setResult(ReconcileResultEnum.已核对.getCode());
                    } else {
                        reconciliationE.setResult(ReconcileResultEnum.部分核对.getCode());
                    }
                    reconciliationE.setReconcileMode(1);
                    reconciliationE.setCommission(reconciliationDetailES.stream().mapToLong(ReconciliationDetailE::getCommission).sum());
                    reconciliationRepository.saveOrUpdate(reconciliationE);
                }
            }

        }
        return true;
    }



}
