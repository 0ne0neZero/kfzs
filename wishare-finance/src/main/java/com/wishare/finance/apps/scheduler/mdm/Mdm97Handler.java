package com.wishare.finance.apps.scheduler.mdm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.scheduler.mdm.vo.DictionaryF;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM11F;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm11Response;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm97F;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm97Response;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.domains.mdm.entity.Mdm97E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm11Mapper;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm97Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Mdm97Handler {

    private final ZJBillProperties zjBillProperties;
    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";

    private final OrgClient orgClient;
    private final Mdm97Mapper mdm97Mapper;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CONTENTTYPE = "Content-Type";

    @XxlJob("mdm97SyncHandler")
    @Transactional(rollbackFor = Exception.class)
    public void sync(String oid){
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        // 2020-01-01到当前时间,按照28天间隔拆分
        List<String> dateList = Mdm11Handler.splitDatesByInterval("2021-09-15", 28);
        if (StringUtils.isNotBlank(oid)){
            doSync(oid,dateList);
        } else {
            List<String> oidList = orgClient.getAllValidOidList();
            for (String oid1 : oidList) {
                doSync(oid1,dateList);
            }
        }
    }

    public void doSync(String oid, List<String> dateList) {
        List<Mdm97Response> mdm97ResponseList = Lists.newArrayList();
        Mdm97F mdm97F = Mdm97F.builder().AUTHORIZEDUNIT(oid).PageNum(1).build();
        DictionaryF<Mdm97F> dicRequest = baseDictionaryFOnMdm97();
        for (int i = 0; i < dateList.size()-1; i++) {
            String start = dateList.get(i);
            String end = dateList.get(i+1);
            mdm97F.setStarttime(start+"T00:00:00.00+00:00");
            mdm97F.setEndtime(end+"T00:00:00.00+00:00");
            dicRequest.setWhereCondition(mdm97F);

            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(dicRequest);
            } catch (JsonProcessingException e) {
                log.error("objectMapper error:{}", e.getMessage());
                return;
            }
            //组装查询对象
            String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
            //数据转换
            ZjDictionaryResponse<Mdm97Response> response = null;
            response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm97Response>>() {});
            if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
                log.info("oid:{} 开始{} 结束{} 无MDM97数据",oid, start,end);
                continue;
            }
            mdm97ResponseList.addAll(response.getData());
        }
        if (CollectionUtils.isEmpty(mdm97ResponseList)){
            return;
        }
        List<String> ids = mdm97ResponseList.stream().map(Mdm97Response::getID).collect(Collectors.toList());
        mdm97Mapper.deleteBatchIds(ids);
        List<Mdm97E> mdm97List = mdm97ResponseList.stream().map(Mdm97Response::transfer).collect(Collectors.toList());
        mdm97Mapper.batchInsert(mdm97List);
    }

    private static DictionaryF<Mdm97F> baseDictionaryFOnMdm97(){
        DictionaryF<Mdm97F> dictionaryF = new DictionaryF<>();
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM97");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
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
                log.error("postRequest:{}", e.getMessage());
            } finally {
                response.close();
            }
        } catch (IOException e) {
            log.error("postRequest:{}", e.getMessage());
        }
        throw BizException.throw400("警告！！！postRequest");
    }

}
