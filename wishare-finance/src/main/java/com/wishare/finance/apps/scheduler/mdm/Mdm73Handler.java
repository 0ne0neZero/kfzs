package com.wishare.finance.apps.scheduler.mdm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.scheduler.mdm.vo.DictionaryF;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM73F;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm73Response;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.domains.mdm.entity.Mdm73E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm73Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Mdm73Handler {

    private final ZJBillProperties zjBillProperties;
    private final OrgClient orgClient;
    private final Mdm73Mapper mdm73Mapper;

    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CONTENTTYPE = "Content-Type";


    @XxlJob("mdm73SyncHandler")
    public void sync(String oid){
        log.info("=========开始同步MDM73数据==============");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        // 当前时间减10年
        LocalDate dateTenYearsAgo = LocalDate.now().minus(10, ChronoUnit.YEARS);
        String start = dateTenYearsAgo.toString();
        String end = LocalDate.now().toString();
        if (StringUtils.isNotBlank(oid)){
            doSync(oid,start,end);
        } else {
            List<String> oidList = orgClient.getAllValidOidList();
            for (String oid1 : oidList) {
                doSync(oid1,start,end);
            }
        }
    }





    @Transactional(rollbackFor = Exception.class)
    public void doSync(String oid,String start,String end){
        if (StringUtils.isBlank(oid)){
            return;
        }
        Integer pageNum = 1;
        List<Mdm73Response> mdm73ResponseList = Lists.newArrayList();
        while(true){
            DictionaryF<MDM73F> dicRequest = buildDictionaryFOnMdm73(oid, start, end, pageNum);
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
            ZjDictionaryResponse<Mdm73Response> response = null;
            response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm73Response>>() {});
            if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
                log.info("oid{} 无MDM73数据",oid);
                mdm73Mapper.deleteByOid(oid);
//            mdm63Mapper.deleteByCtCode(ctCode);
                return;
            }
            mdm73ResponseList.addAll(response.getData());
            if (response.getTotalPage() == pageNum){
                break;
            }
            pageNum++;
        }
        log.info("oid{} MDM73数据{}",oid,JSON.toJSONString(mdm73ResponseList));
        mdm73Mapper.deleteByOid(oid);
        List<Mdm73E> mdm73EList = mdm73ResponseList.stream().map(Mdm73Response::transferToMdm73E).collect(Collectors.toList());
        mdm73Mapper.insertBatch(mdm73EList);
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

    private DictionaryF<MDM73F> buildDictionaryFOnMdm73(String oid, String start, String end, Integer pageNum) {
        String startUtcStr = start+"T00:00:00.00+00:00";
        String endUtcStr = end+"T00:00:00.00+00:00";
        DictionaryF<MDM73F> dictionaryF = new DictionaryF<>();
        MDM73F mdm73F = MDM73F.builder()
                .acctUnitID(oid)
                .starttime(startUtcStr)
                .PageNum(pageNum)
                .endtime(endUtcStr).build();
        dictionaryF.setWhereCondition(mdm73F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM73");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }


}
