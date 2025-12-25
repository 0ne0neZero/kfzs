package com.wishare.finance.apps.scheduler.bill;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.organization.facade.PaymentFacade;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.service.ReconciliationYinlianDomainService;
import com.wishare.finance.infrastructure.conts.CacheEnum;
import com.wishare.finance.infrastructure.remote.clients.payment.PaymentReconcileClient;
import com.wishare.finance.infrastructure.remote.enums.Channel;
import com.wishare.finance.infrastructure.remote.vo.payment.TLMchInfo;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 周英健
 * @date 2023/08/03
 * @Description:
 */
@Slf4j
@Component
public class TLReconciliationFileSchedule {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private ReconciliationYinlianDomainService reconciliationYinlianDomainService;

    @Setter(onMethod_ = {@Autowired})
    private PaymentReconcileClient paymentReconcileClient;
    /**
     * 方圆租户id
     */
    @Value("${fang-yuan.sign.tenantId:125322827237001}")
    private String tenantId;

    /**
     * 方圆对接通联签约代扣商户号
     */
    @Value("${fang-yuan.sign.merchantId:66059506513199D}")
    private String merchantId;

    /**
     * 方圆对接通联签约代扣用户名
     */
    @Value("${fang-yuan.sign.userName:66059506513199D04}")
    private String userName;

    @XxlJob("accountCheckingTLFileHandler")
    public void accountCheckingTLFileHandler() throws IOException {
        List<String> tlCommunityList = paymentReconcileClient.getTLCommunityList();

        if (CollectionUtils.isEmpty(tlCommunityList)){
            return;
        }
        String fileName = null;
        for (int i = 0; i < tlCommunityList.size(); i++) {
            TLMchInfo info = paymentReconcileClient.getTLMchInfoByBusinessId(tlCommunityList.get(i), Channel.通联支付.getChannelCode());

            if (ObjectUtils.isEmpty(info)){
                continue;
            }else {
                log.info("项目对应支付商户管理配置信息为{}---------------->",JSON.toJSONString(info));
            }
            log.info("下载通联对账文件并录入数据库定时任务开始---------------->");
            //获取对账日期,前一天
            LocalDate recDate = LocalDate.now().minusDays(1);
            //1.构造对账文件名称（商户号_日期）数据唯一性
            fileName = "TL_" + DateTimeUtil.dateNocPattern(recDate);
            //2.查询该对账文件是否已经获取过
            String fileNameExit = RedisHelper.get(CacheEnum.TONGLIAN_RECONCILIATION_FILE.getCacheKey(fileName));
            log.info("fileNameExit:" + fileNameExit);
            //todo 处理商户和租户的映射关系
            handleMidToTenant();
            List<ReconciliationYinlianE> docList = new ArrayList<>();
            if (StringUtils.isBlank(fileNameExit)) {
                //下载通联对账文件
                byte[] reconciliationDownload = paymentFacade.reconciliationDownloadFromTL(info, tlCommunityList.get(i));
                log.info(JSON.toJSONString(reconciliationDownload));
                String reconciliationString = new String(reconciliationDownload, "GBK");
                log.info("通联账单文件解析前原始字符串:" + reconciliationString);
                docList = readStringForTL(reconciliationString, fileName,info);
                log.info("通联账单数据条数docList:" + docList.size());
                reconciliationYinlianDomainService.addBatch(docList);
            }
            log.info("下载通联对账文件并录入数据库定时任务结束---------------->");
        }
        RedisHelper.setAtExpire(CacheEnum.TONGLIAN_RECONCILIATION_FILE.getCacheKey(fileName), 30 * 24 * 60 * 60, fileName);

    }

    /**
     * 处理商户和租户的映射关系
     */
    private void handleMidToTenant() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
    }


    private List<ReconciliationYinlianE> readStringForTL(String reconciliationString, String fileName,TLMchInfo info) {
        //list接收数据
        List<ReconciliationYinlianE> dataList = Lists.newArrayList();
        String[] split = reconciliationString.split("\\r\\n");
        for (String s1 : split) {
            String[] arrStrings = s1.split(" ");
            if (arrStrings.length == 13 || arrStrings.length == 12) {
                ReconciliationYinlianE entity = new ReconciliationYinlianE();
                entity.setFileName(fileName);
                entity.setClearDate(arrStrings[7].trim());//清算日期
                entity.setMid(info.getMerchantId());//商户号
//            entity.setTid(arrStrings[2].trim());//终端号
//            entity.setMsgType(arrStrings[3].trim());//消息类型
//            entity.setHandleCode(arrStrings[4].trim());//处理码
                entity.setPaymentCode(arrStrings[5].trim());//卡号/付款码
                entity.setTradeAmount(String.valueOf((Double.valueOf(arrStrings[4].trim()) / 100)));//交易金额
                entity.setSeqId(arrStrings[0].trim());//交易流水号
                entity.setTradeDate(arrStrings[6].trim().substring(arrStrings[6].trim().length() - 10));//交易日期时间
//            entity.setServicePointConditionCode(arrStrings[9].trim());//服务点条件码
//            entity.setServiceCode(arrStrings[10].trim());//服务码
//            entity.setIssuerIdent(arrStrings[11].trim());//发卡机构标识
//            entity.setSearchReferenceNo(arrStrings[12].trim());//检索参考号
//            entity.setOldSeqId(arrStrings[1].trim());//原交易流水号
//            entity.setOldTradeDate(arrStrings[7].trim());//原交易日期
//            entity.setOldTradeReferenceNo(arrStrings[15].trim());//原交易参考号
                entity.setCommission(arrStrings[9].trim());//商户手续费
//            entity.setOrderNumber(arrStrings[17].trim());//订单号
                entity.setTradeName("通联代扣");//交易名称（中文）
//            entity.setCardType(arrStrings[19].trim());//卡类型标识
//            entity.setExtCardUseToIn(arrStrings[20].trim());//外卡内用标识
                entity.setTradeChannel("TL");//交易渠道
//            entity.setExtField1(arrStrings[22].trim());//备用字段1
//            entity.setExtField2(arrStrings[23].trim());//备用字段2
//            entity.setExtField3(arrStrings[24].trim());//备用字段3
                dataList.add(entity);
            }

        }
        return dataList;
    }


}
