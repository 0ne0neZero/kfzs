package com.wishare.finance.apps.scheduler.mdm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishare.finance.apps.scheduler.mdm.vo.DictionaryF;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM11F;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm11Response;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.domains.mdm.entity.Mdm11E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm11Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Mdm11Handler {

    private final ZJBillProperties zjBillProperties;
    private final Mdm11Mapper mdm11Mapper;

    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CONTENTTYPE = "Content-Type";


    @XxlJob("mdm11SyncHandler")
    @Transactional(rollbackFor = Exception.class)
    public void sync(){
        log.info("=====开始同步MDM11数据==============");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        // 2020-01-01到当前时间,按照28天间隔拆分
        List<String> dateList = splitDatesByInterval("2021-06-29", 28);
        for (int i = 0; i < dateList.size()-1; i++) {
            String start = dateList.get(i);
            String end = dateList.get(i+1);
            doSyncMdm11(start,end);
        }

    }

    public void doSyncMdm11(String start,String end) {
        DictionaryF<MDM11F> dicRequest = buildDictionaryFOnMdm11(start, end);
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(dicRequest);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return;
        }
//        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        //数据转换
        ZjDictionaryResponse<Mdm11Response> response = null;
        response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm11Response>>() {});
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("开始{} 结束{} 无MDM11数据",start,end);
            return;
        }
        List<Mdm11Response> responseList = response.getData();
        List<Mdm11E> mdm11EList = responseList.stream().map(Mdm11Response::transfer2Mdm11E).collect(Collectors.toList());

        // 数据异常的参数请求
        Mdm11E errPojo = mdm11EList.stream()
                .filter(e -> !StringUtils.equals(e.getCusitemCategory(), "2CA6E6DDF4B8486D96595863A8A5C170"))
                .findFirst().orElse(null);
        if (Objects.nonNull(errPojo)){
            System.out.println("异常请求数据"+jsonString);
        }

        // 提取id
        List<String> idList = mdm11EList.stream().map(Mdm11E::getId).collect(Collectors.toList());
        mdm11Mapper.deleteBatchIds(idList);
        mdm11Mapper.insertBatch(mdm11EList);
    }

    private DictionaryF<MDM11F> buildDictionaryFOnMdm11(String start, String end) {
        String year = start.substring(0, 4);
        String startUtcStr = start+"T00:00:00.00+00:00";
        String endUtcStr = end+"T00:00:00.00+00:00";
        DictionaryF<MDM11F> dictionaryF = new DictionaryF<>();
        MDM11F mdm11F = MDM11F.builder()
                .starttime(startUtcStr)
                .endtime(endUtcStr)
                .year(year)
                .CusItemCategory("01")
                .pageNum(1)
                .endtime(endUtcStr).build();

        dictionaryF.setWhereCondition(mdm11F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM11");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }


    public static List<String> splitDatesByInterval(String startDateStr, int intervalDays) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, inputFormatter);
        LocalDate currentDate = LocalDate.now();

        List<String> dateList = new ArrayList<>();
        LocalDate currentDateIter = startDate;

        while (!currentDateIter.isAfter(currentDate)) {
            dateList.add(currentDateIter.format(outputFormatter));
            currentDateIter = currentDateIter.plusDays(intervalDays);
        }

        // 如果最后一个日期超过了当前时间，则移除它并添加当前时间（格式化为 yyyy-MM-dd）
        if (!dateList.isEmpty() && !currentDateIter.minusDays(intervalDays).isBefore(currentDate)) {
            dateList.remove(dateList.size() - 1);
            dateList.add(currentDate.format(outputFormatter));
        }
        // 检查并添加当前日期+1（如果最后一个日期不是当前日期）
        if (!dateList.isEmpty()) {
            LocalDate lastDate = LocalDate.parse(dateList.get(dateList.size() - 1), inputFormatter);
            if (!lastDate.isEqual(currentDate)) {
                dateList.add(currentDate.plusDays(1).format(outputFormatter));
            }
        }
        return dateList;
    }

    public static List<String> splitDatesByInterval(String startDateStr, String endDateStr, int intervalDays) {
        if (intervalDays <= 0) {
            throw new IllegalArgumentException("间隔天数必须大于0");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        List<String> dateList = new ArrayList<>();
        LocalDate current = startDate;

        dateList.add(current.format(formatter));

        while (current.plusDays(intervalDays).isBefore(endDate)) {
            current = current.plusDays(intervalDays);
            dateList.add(current.format(formatter));
        }

        if (!current.equals(endDate)) {
            dateList.add(endDate.format(formatter));
        }
        return dateList;
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
