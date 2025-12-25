package com.wishare.finance.apps.service.bill;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishare.finance.apps.pushbill.fo.VoucherBillRecMdm63F;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.Mdm11Handler;
import com.wishare.finance.apps.scheduler.mdm.Mdm73Handler;
import com.wishare.finance.apps.scheduler.mdm.Mdm97Handler;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.apps.service.pushbill.mdm63.Mdm63Service;
import com.wishare.finance.domains.mdm.entity.Mdm11E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm11Mapper;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm97Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.FinancialCloudClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.common.FinanceV;
import com.wishare.finance.infrastructure.remote.vo.common.WhereConditionV;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgDetailsV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class FinanceCloudService {

    private final FinancialCloudClient financialCloudClient;
    ObjectMapper objectMapper = new ObjectMapper();
    private final ZJBillProperties zjBillProperties;
    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";
    static final String QUERYCAPITEL_PLAN_URL = "/ESB/API/ZJFW/YXDMC/QueryCapitelPlan";
    static final String CAPITE_URL = "/ESB/API/ZJFW/YXDMC/QueryCapitelPlan";
    private static final String CONTENTTYPE = "Content-Type";
    private final ConfigClient configClient;
    private final SpaceClient spaceClient;
    private final OrgClient orgClient;

    private final Mdm11Handler mdm11Handler;
    private final Mdm11Mapper mdm11Mapper;
    private final Mdm73Handler mdm73Handler;
    private final Mdm97Handler mdm97Handler;
    private final Mdm97Mapper mdm97Mapper;
    private final Mdm63Mapper mdm63Mapper;

    public ZjDictionaryResponse<PaymentMethodV> queryPaymentMethod(Integer pageNum) {
        if (StrUtil.isNotBlank(zjBillProperties.getPaymentMethodResult())) {
            // 开发环境无法通过代理服务器连接中交uat地址,直接返回默认值
            return JSON.parseObject(zjBillProperties.getPaymentMethodResult(), new TypeReference<>() {});
        }
        if (null == pageNum || 0 == pageNum) {
            pageNum =1;
        }
        LocalDate nowTime = LocalDate.now();
        String nowTimeUtcStr = convertLocalDateToUtcString(nowTime.minusYears(10));
        String endTimeUtcStr = convertLocalDateToUtcString(nowTime);
        FinanceV financeV = FinanceV.builder()
                .appInstanceCode("10000")
                .dicCode("MDM27")
                .unitCode("MDM")
                .whereCondition(
                        WhereConditionV.builder().starttime(nowTimeUtcStr).endtime(endTimeUtcStr).acctUnitID("").PageNum(1).build()
                )
                .sourceSystem("CCCG-DMC")
                .build();

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(financeV);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return null;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        ZjDictionaryResponse<PaymentMethodV> response = JSON.parseObject(result, new TypeReference<>() {
        });
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("无MDM27数据");
            return null;
        }
        response.setData(response.getData().stream().filter(d->"1".equals(d.getSTATE_ISENABLED())).collect(Collectors.toList()));
        return response;
    }

    public List<CashFlowV> queryCashFlow() {
        List<Mdm11E> mdm11EList = mdm11Mapper.selectCashFlowItems();
        if (CollectionUtils.isEmpty(mdm11EList)){
            return Collections.emptyList();
        }
        return mdm11EList.stream().map(CashFlowV::transferByMdm11).collect(Collectors.toList());
    }

    public ZjDictionaryResponse<PaymentAccountV> queryPaymentAccount(Integer pageNum) {
        if (null == pageNum || 0 == pageNum) {
            pageNum =1;
        }
        LocalDate nowTime = LocalDate.now();
        String nowTimeUtcStr = convertLocalDateToUtcString(nowTime.minusDays(29));
        String endTimeUtcStr = convertLocalDateToUtcString(nowTime);
        FinanceV financeV = FinanceV.builder()
                .appInstanceCode("10000")
                .dicCode("MDM06")
                .unitCode("MDM")
                .whereCondition(
                        WhereConditionV.builder().starttime(nowTimeUtcStr).endtime(endTimeUtcStr).acctUnitID("").PageNum(pageNum).build()
                )
                .sourceSystem("CCCG-DMC")
                .build();

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(financeV);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return null;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        ZjDictionaryResponse<PaymentAccountV> response = JSON.parseObject(result, new TypeReference<>() {
        });
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("无MDM06数据");
            return null;
        }
        response.setData(response.getData().stream().filter(d->"2".equals(d.getAccountStatus())).collect(Collectors.toList()));
        return response;
    }

    public static String convertLocalDateToUtcString(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        DateTimeFormatter customFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SS")
                .appendOffset("+HH:MM", "+00:00")
                .toFormatter();

        return offsetDateTime.format(customFormatter);
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

    public ZjDictionaryResponse<PayPlanCodeV> payCode(PayPlanCodeReqV payPlanCodeReqV) {
        String url = zjBillProperties.getUrl();
        if (StrUtil.isNotBlank(zjBillProperties.getPayPlanDefaultResult())) {
            // 开发环境无法通过代理服务器连接中交uat地址,直接返回默认值
            return JSON.parseObject(zjBillProperties.getPayPlanDefaultResult(), new TypeReference<>() {});
        }
        if (payPlanCodeReqV.getTestOrNot()) {
            payPlanCodeReqV.setBZDW("101235178");
            payPlanCodeReqV.setLYXT("SCM");
            payPlanCodeReqV.setQJNM("20230101");
        }else{
            /*List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", payPlanCodeReqV.getCommunity());
            List<String> collect = community.stream().map(CfgExternalDataV::getExternalDataType).collect(Collectors.toList());
            if (!collect.contains("department")){
                throw new BizException(400, "未维护行政部门");
            }
            if (!collect.contains("org")){
                throw new BizException(400, "未维护行政组织");
            }
            String org = "";
            for (CfgExternalDataV cfgExternalDataV : community) {
                if("org".equals(cfgExternalDataV.getExternalDataType())){
                    org = cfgExternalDataV.getDataCode();
                    payPlanCodeReqV.setBZDW(org);
                }
            }*/
            if(StringUtils.isEmpty(payPlanCodeReqV.getExternalDepartmentCode())){
                throw new BizException(400, "部门code不允许为空");
            }
            payPlanCodeReqV.setBZDW(payPlanCodeReqV.getExternalDepartmentCode());
            payPlanCodeReqV.setLYXT("CCCG-DMC");
            payPlanCodeReqV.setQJNM(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        }
        payPlanCodeReqV.setDJLX("BizPaymentRequest");
        payPlanCodeReqV.setType("QUERY");


        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(payPlanCodeReqV);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return null;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(url+QUERYCAPITEL_PLAN_URL, jsonString);
        ZjDictionaryResponse<PayPlanCodeV> response = JSON.parseObject(result, new TypeReference<>() {
        });
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("无MDM06数据");
            return response;
        }
        return response;
    }

    public ZjDictionaryResponse<BusinessSubjectV> businessSubjects(Integer pageNum, Integer direct) {
        if (null == pageNum || 0 == pageNum) {
            pageNum =1;
        }
        if (direct == null) {
            direct = 1;
        }
        LocalDate nowTime = LocalDate.now();
        String nowTimeUtcStr = convertLocalDateToUtcString(nowTime.minusDays(28));
        String endTimeUtcStr = convertLocalDateToUtcString(nowTime);
        FinanceV financeV = FinanceV.builder()
                .appInstanceCode("10000")
                .dicCode("MDM17")
                .unitCode("MDM")
                .whereCondition(
                        WhereConditionV.builder().starttime(nowTimeUtcStr).endtime(endTimeUtcStr).acctUnitID("").PageNum(pageNum).build()
                )
                .sourceSystem("CCCG-DMC")
                .build();

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(financeV);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return null;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        ZjDictionaryResponse<BusinessSubjectV> response = JSON.parseObject(result, new TypeReference<>() {
        });
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("无MDM17数据");
            return null;
        }
        Integer finalDirect = direct;
        response.setData(response.getData().stream().filter(d->null != d.getBALANCEDIR() && finalDirect.equals(d.getBALANCEDIR())).collect(Collectors.toList()));
        return response;
    }

    public ZjDictionaryResponse<BankAccountV> unitBankInfo(BankAccountReqV bankAccountReqV) {
        if (StrUtil.isNotBlank(zjBillProperties.getUnitBankInfo())) {
            // 开发环境无法通过代理服务器连接中交uat地址,直接返回默认值
            return JSON.parseObject(zjBillProperties.getUnitBankInfo(), new TypeReference<>() {});
        }
        LocalDate nowTime = LocalDate.now();
        String nowTimeUtcStr = convertLocalDateToUtcString(nowTime.minusYears(5));
        String endTimeUtcStr = convertLocalDateToUtcString(nowTime);
        FinanceV financeV = FinanceV.builder()
                .appInstanceCode("10000")
                .dicCode("MDM62")
                .unitCode("MDM")
                .whereCondition(
                        WhereConditionV.builder().starttime(nowTimeUtcStr).endtime(endTimeUtcStr).WLDWBH(bankAccountReqV.getWldwbh()).PageNum(1).build()
                )
                .sourceSystem("CCCG-DMC")
                .build();

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(financeV);
        } catch (JsonProcessingException e) {
            log.error("objectMapper error:{}", e.getMessage());
            return null;
        }
        log.info("dicRequest:{}", jsonString);
        //组装查询对象
        String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
        ZjDictionaryResponse<BankAccountV> response = JSON.parseObject(result, new TypeReference<>() {
        });
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getData())){
            log.info("无MDM62数据");
            return null;
        }
        return response;
    }

    public List<BankAccountV> payBankInfo(BankAccountReqV bankAccountReqV) {
        if(Objects.nonNull(bankAccountReqV.getUnitCode())){
            return mdm97Mapper.selectValidBankByOid(bankAccountReqV.getUnitCode());
        }else{
            SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(bankAccountReqV.getCommunityId());
            if (Objects.isNull(spaceCommunityShortVS)){
                throw new OwlBizException("项目不存在，请检查");
            }
            OrgDetailsV orgInfo = orgClient.getOrgInfo(spaceCommunityShortVS.getOrgId());
            if (Objects.isNull(orgInfo) || StringUtils.isBlank(orgInfo.getOid())){
                throw new OwlBizException("项目不存在或组织oid不存在,请检查");
            }
            String oid = orgInfo.getOid();

            return mdm97Mapper.selectValidBankByOid(oid);

        }
    }


    public void doSyncMdm11() {
        mdm11Handler.sync();
    }


    public void syncMdm73() {
        mdm73Handler.sync(null);
    }

    public void syncMdm97(String oid) {
        mdm97Handler.sync(oid);
    }

    //核销应付清单列表筛选
    public PageV<Mdm63FrontV> queryMdm63Page(PageF<VoucherBillRecMdm63F> pageF) {
        VoucherBillRecMdm63F mdm63F = pageF.getConditions();
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(mdm63F.getCommunityId());
        if (Objects.isNull(spaceCommunityShortVS)){
            throw new OwlBizException("项目编号不存在,请检查");
        }
        String projectId = spaceCommunityShortVS.getSerialNumber();
        //合同编码
        String contractNo = "9999999999";
        //主数据往来单位编码
        String contractPayer = "BP01505472";
        Page<Mdm63FrontV> mdm63FrontVPage = mdm63Mapper.queryMdm63Page(RepositoryUtil.convertMPPage(pageF),
                projectId,
                contractPayer,
                contractNo,
                Mdm63Service.PAYABLE_TYPE
                );
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(mdm63FrontVPage.getRecords())){
            return PageV.of(pageF.getPageNum(),pageF.getPageSize(),0,Collections.emptyList());
        }
        for (Mdm63FrontV record : mdm63FrontVPage.getRecords()) {
            record.setProjectInfoName(spaceCommunityShortVS.getName());
            record.setPartnerName(mdm63F.getContractPayerName());
        }
        return PageV.of(mdm63FrontVPage.getCurrent(), mdm63FrontVPage.getSize(),
                mdm63FrontVPage.getTotal(), mdm63FrontVPage.getRecords());
    }
}
