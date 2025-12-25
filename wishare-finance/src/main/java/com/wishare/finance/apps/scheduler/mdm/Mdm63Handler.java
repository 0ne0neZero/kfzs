package com.wishare.finance.apps.scheduler.mdm;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.scheduler.mdm.vo.DictionaryF;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM63F;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM63F2;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63Response;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63CertainF;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailZJMapper;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Mdm63Handler {

    private final ZJBillProperties zjBillProperties;
    private final Mdm63Mapper mdm63Mapper;
    private final VoucherBillDetailDxZJMapper dxMapper;
    private final ContractClient contractClient;
    private final VoucherBillDetailZJMapper voucherBillDetailZJMapper;

    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CONTENTTYPE = "Content-Type";


    @XxlJob("mdm63SyncHandler")
    public void sync(List<Mdm63CertainF> mdm63CertainFList){
        log.info("======开始同步MDM63数据===========");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        if (CollectionUtils.isNotEmpty(mdm63CertainFList)){
            mdm63CertainFList.forEach(mdm63CertainF -> doSync(mdm63CertainF.getCtCode(), mdm63CertainF.getStart(), mdm63CertainF.getEnd()));
        } else {
            log.info("======开始支出MDM63数据===========");
            syncOnPay();
            log.info("======开始收入MDM63数据===========");
            syncOnIncome();
        }
    }

    private void syncOnIncome() {
        List<String> contractIdList = dxMapper.selectContractIdForMdm63OnIncome();
        // 100个一组,进行处理
        if (CollectionUtils.isEmpty(contractIdList)){
            return;
        }
        List<List<String>> partition = Lists.partition(contractIdList, 100);
        for (List<String> part : partition) {
            List<ContractPayPlanInnerInfoV> innerInfos = contractClient.getInnerInfoByContractIdOnIncome(part);
            if (CollectionUtils.isEmpty(innerInfos)){
                continue;
            }
            innerInfos.forEach(e -> {
                doSync(e.getConMainCode(), e.getGmtExpireStart(), e.getGmtExpireEnd());
            });
        }
    }

    private void syncOnPay() {
        List<String> contractIdList = dxMapper.selectContractIdForMdm63OnPay();
        // 100个一组,进行处理
        if (CollectionUtils.isEmpty(contractIdList)){
            return;
        }
        List<List<String>> partition = Lists.partition(contractIdList, 100);
        for (List<String> part : partition) {
            List<ContractPayPlanInnerInfoV> innerInfos = contractClient.getInnerInfoByContractIdOnPay(part);
            if (CollectionUtils.isEmpty(innerInfos)){
                continue;
            }
            innerInfos.forEach(e -> {
                doSync(e.getConMainCode(), e.getGmtExpireStart(), e.getGmtExpireEnd());
            });
        }
    }

    @XxlJob("mdm63SyncHandler2")
    public void sync2(String start){
        log.info("========开始同步小业主MDM63数据===========");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        // 获取当前年份并减去一年得到上一年
        Year lastYear = Year.now().minusYears(1);
        // 构建上一年1月1日的开始时间
        LocalDateTime startOfYear = lastYear.atMonth(1).atDay(1).atStartOfDay();
        Date calStart = Date.from(startOfYear.atZone(ZoneId.systemDefault()).toInstant());

        // 小业主的数据同步
        String startStr = StringUtils.isBlank(start) ? DateUtil.format(calStart, "yyyy-MM-dd") : start;
        String endDateStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<String> dateList = Mdm11Handler.splitDatesByInterval(startStr, endDateStr,28);
        for (int i = 0; i < dateList.size()-1; i++) {
            String curStart = dateList.get(i);
            String curEnd = dateList.get(i+1);
            doSync2("9999999999", curStart, curEnd);
        }
    }

    /**
     * @param partnerId 往来单位BP码
     **/
    @XxlJob("mdm63SyncHandler3")
    public void sync3(String partnerId){
        log.info("========开始同步大业主MDM63数据===========");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        // 获取当前年份并减去一年得到上一年
        Year lastYear = Year.now().minusYears(1);
        // 构建上一年1月1日的开始时间
        LocalDateTime startOfYear = lastYear.atMonth(1).atDay(1).atStartOfDay();
        Date calStart = Date.from(startOfYear.atZone(ZoneId.systemDefault()).toInstant());

        // 小业主的数据同步
        String startStr = DateUtil.format(calStart, "yyyy-MM-dd");
        DateTime calEnd = DateUtil.offsetDay(new Date(), 1);
        String endDateStr = DateUtil.format(calEnd, "yyyy-MM-dd");
        List<String> partnerIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(partnerId)){
            partnerIdList.add(partnerId);
        }
        if (CollectionUtils.isEmpty(partnerIdList)){
            // 获取所有报账明细的
            partnerIdList = voucherBillDetailZJMapper.queryAllPropertyMainCode();
        }
        if (CollectionUtils.isEmpty(partnerIdList)){
            return;
        }
        for (String curPartnerId : partnerIdList) {
            doSync3("9999999999", curPartnerId, startStr, endDateStr);
        }
    }

    /**
     * 外部调用异步,内部调用同步
     **/
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void doSync(String ctCode, Date start, Date end){
        if (ObjectUtils.anyNull(start, end, ctCode)){
            return;
        }
        DictionaryF<MDM63F> dicRequest = buildDictionaryFOnMdm16(ctCode, start, end);
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(dicRequest);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        //数据转换
        ZjDictionaryResponse<Mdm63Response> response = null;
        response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm63Response>>() {});
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("合同{} 无MDM63数据",ctCode);
            mdm63Mapper.deleteByCtCode(ctCode);
            return;
        }

        mdm63Mapper.deleteByCtCode(ctCode);
        List<Mdm63E> list = response.getData().stream().map(Mdm63Response::transferToMdm63E).collect(Collectors.toList());
        mdm63Mapper.insertBatch(list);
    }

    /**
     * 小业主MDM63数据同步
     * lastmodifiedtime 理论上会在start和end之间
     **/
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void doSync2(String ctCode, String start, String end){
        if (ObjectUtils.anyNull(start, end, ctCode)){
            return;
        }

        int pageNum = 1;
        List<Mdm63Response> responseDataList = Lists.newArrayList();
        while (true){
            DictionaryF<MDM63F2> dicRequest = buildDictionaryFOnMdm16_2(ctCode, start, end, pageNum);
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(dicRequest);
            } catch (JsonProcessingException e) {
                log.error("objectMapper error:{}", e.getMessage());
                return;
            }
            log.info("dicRequest:{}", jsonString);
            //组装查询对象
            String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
            //数据转换
            ZjDictionaryResponse<Mdm63Response> response = null;
            response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm63Response>>() {});
            if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
                log.info("小业主{} 开始 {} 结束 {} 页码 {} 无MDM63数据", ctCode, start, end, pageNum);
                break;
            }
            responseDataList.addAll(response.getData());

            if (response.getTotalPage() > pageNum){
                pageNum++;
            }else {
                break;
            }
        }

        if (CollectionUtils.isEmpty(responseDataList)){
            mdm63Mapper.deleteByCtCodeAndPartnerCodeInCertainPeriod(ctCode, "BP01505472", start, end, null);
            return;
        }
        // 根据partnerCode contractCode 最后更新时间在start-end的进行强制更新
        mdm63Mapper.deleteByCtCodeAndPartnerCodeInCertainPeriod(ctCode, "BP01505472", start, end, null);
        // 插入新数据
        List<Mdm63E> list = responseDataList.stream().map(Mdm63Response::transferToMdm63E).collect(Collectors.toList());
        // 防止list过大,拆分200个一次进行插入
        List<List<Mdm63E>> partition = Lists.partition(list, 200);
        partition.forEach(mdm63Mapper::insertBatch);
    }

    /**
     * 大业主MDM63数据同步
     * lastmodifiedtime 理论上会在start和end之间
     **/
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void doSync3(String ctCode, String partnerId, String start, String end){
        if (ObjectUtils.anyNull(start, end, ctCode)){
            return;
        }

        int pageNum = 1;
        List<Mdm63Response> responseDataList = Lists.newArrayList();
        while (true){
            DictionaryF<MDM63F2> dicRequest = buildDictionaryFOnMdm16_3(ctCode, partnerId, start, end, pageNum);
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(dicRequest);
            } catch (JsonProcessingException e) {
                log.error("objectMapper error:{}", e.getMessage());
                return;
            }
            log.info("dicRequest:{}", jsonString);
            //组装查询对象
            String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
            log.info("大业主{} 开始 {} 结束 {} 页码 {} MDM63数据:{}", partnerId, start, end, pageNum, result);
            //数据转换
            ZjDictionaryResponse<Mdm63Response> response = null;
            response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm63Response>>() {});
            if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
                log.info("大业主{} 开始 {} 结束 {} 页码 {} 无MDM63数据", partnerId, start, end, pageNum);
                break;
            }
            responseDataList.addAll(response.getData());

            if (response.getTotalPage() > pageNum){
                pageNum++;
            }else {
                break;
            }
        }

        if (CollectionUtils.isEmpty(responseDataList)){
            mdm63Mapper.deleteByCtCodeAndPartnerCodeInCertainPeriod(ctCode, partnerId, start, end, null);
            return;
        }
        // 根据partnerCode contractCode 最后更新时间在start-end的进行强制更新
        mdm63Mapper.deleteByCtCodeAndPartnerCodeInCertainPeriod(ctCode, partnerId, start, end, null);
        // 插入新数据
        List<Mdm63E> list = responseDataList.stream().map(Mdm63Response::transferToMdm63E).collect(Collectors.toList());
        // 防止list过大,拆分200个一次进行插入
        Map<String, Mdm63E> map = list.stream().collect(Collectors.toMap(Mdm63E::getFtId, e -> e, (oldVal, newVal) -> oldVal));
        list = Lists.newArrayList(map.values());
        List<List<Mdm63E>> partition = Lists.partition(list, 200);
        partition.forEach(mdm63Mapper::insertBatch);
    }


    static public String postRequestDefault(String url, String jsonString) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(CONTENTTYPE, "application/json");
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        return post(httpPost);
    }

    static public String post(HttpPost httpPost) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                String responseBody = EntityUtils.toString(response.getEntity());
                return responseBody;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("postRequest:{}", e.getMessage());
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("postRequest:{}", e.getMessage());
        }
        throw BizException.throw400("警告！！！postRequest");
    }

    private DictionaryF<MDM63F> buildDictionaryFOnMdm16(String ctCode, Date start, Date end) {
        String startUtcStr = DateUtil.format(start, "yyyy-MM-dd")+"T00:00:00.00+00:00";
        String endUtcStr = DateUtil.format(addOneYear(end), "yyyy-MM-dd")+"T00:00:00.00+00:00";
        DictionaryF<MDM63F> dictionaryF = new DictionaryF<>();
        MDM63F mdm16F = MDM63F.builder()
                .contractCode(ctCode)
                .starttime(startUtcStr)
                .pageNum(1)
                .endtime(endUtcStr).build();
        dictionaryF.setWhereCondition(mdm16F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM63");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }

    private DictionaryF<MDM63F2> buildDictionaryFOnMdm16_2(String ctCode, String start, String end, Integer pageNum) {
        String startUtcStr = start+"T00:00:00.00+00:00";
        String endUtcStr = end+"T00:00:00.00+00:00";
        DictionaryF<MDM63F2> dictionaryF = new DictionaryF<>();
        MDM63F2 mdm16F = MDM63F2.builder()
                .contractCode(ctCode)
                .starttime(startUtcStr)
                .PARTNERID("BP01505472")
                .pageNum(pageNum)
                .endtime(endUtcStr).build();
        dictionaryF.setWhereCondition(mdm16F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM63");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }

    private DictionaryF<MDM63F2> buildDictionaryFOnMdm16_3(String ctCode, String partnerId, String start, String end, Integer pageNum) {
        String startUtcStr = start+"T00:00:00.00+00:00";
        String endUtcStr = end+"T00:00:00.00+00:00";
        DictionaryF<MDM63F2> dictionaryF = new DictionaryF<>();
        MDM63F2 mdm16F = MDM63F2.builder()
                .contractCode(ctCode)
                .starttime(startUtcStr)
                .PARTNERID(partnerId)
                .pageNum(pageNum)
                .endtime(endUtcStr).build();
        dictionaryF.setWhereCondition(mdm16F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM63");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }


    public static Date addOneYear(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneOffset.UTC);
        ZonedDateTime newZdt = zdt.plus(1, ChronoUnit.YEARS);
        return Date.from(newZdt.toInstant());
    }
}
