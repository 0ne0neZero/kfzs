package com.wishare.finance.apps.service.scheduler;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.reconciliationFile.fo.ReconcileAccountF;
import com.wishare.finance.domains.configure.organization.facade.PaymentFacade;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.service.ReconciliationYinlianDomainService;
import com.wishare.finance.infrastructure.remote.enums.ChannelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReconciliationFileService {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private ReconciliationYinlianDomainService reconciliationYinlianDomainService;

    public boolean getChannelAccount(ReconcileAccountF reconcileAccountF) {
        List<ReconciliationYinlianE> docList = new ArrayList<>();
        String fileName = reconcileAccountF.getRecDate();
        ReconciliationYinlianE reconciliationYinlianEByFileName = reconciliationYinlianDomainService.getReconciliationYinlianEByFileName(fileName);
        if (null != reconciliationYinlianEByFileName){
            return false;
        }
        try {
            // 判断账单日期
            byte[] reconciliationDownload = paymentFacade.reconciliationDownload(reconcileAccountF.getDownload(), reconcileAccountF.getRecDate(),
                    ChannelTypeEnum.valueOfByCode(reconcileAccountF.getChannelCode()).getDes());

            String reconciliationString = new String(reconciliationDownload, "GBK");
            log.info("银联账单文件解析前原始字符串:" + reconciliationString);
            docList = readString(reconciliationString, fileName);
            log.info("银联账单数据条数docList:" + docList.size());
            reconciliationYinlianDomainService.addBatch(docList);
            return true;
        } catch (Exception e) {
            log.error("拉取账单异常:" + e);
        }
        return false;
    }

    private List<ReconciliationYinlianE> readString(String reconciliationString, String fileName) {
        //list接收数据
        List<ReconciliationYinlianE> dataList = Lists.newArrayList();
        String[] split = reconciliationString.split(";\\|");
        for (String s1 : split) {
            String[] arrStrings = s1.split("\\|");
            if (arrStrings.length != 25) {
                continue;
            }
            ReconciliationYinlianE entity = new ReconciliationYinlianE();
            entity.setFileName(fileName);
            entity.setClearDate(arrStrings[0].trim());//清算日期
            entity.setMid(arrStrings[1].trim());//商户号
            entity.setTid(arrStrings[2].trim());//终端号
            entity.setMsgType(arrStrings[3].trim());//消息类型
            entity.setHandleCode(arrStrings[4].trim());//处理码
            entity.setPaymentCode(arrStrings[5].trim());//卡号/付款码
            entity.setTradeAmount(arrStrings[6].trim());//交易金额
            entity.setSeqId(arrStrings[7].trim());//交易流水号
            entity.setTradeDate(arrStrings[8].trim());//交易日期时间
            entity.setServicePointConditionCode(arrStrings[9].trim());//服务点条件码
            entity.setServiceCode(arrStrings[10].trim());//服务码
            entity.setIssuerIdent(arrStrings[11].trim());//发卡机构标识
            entity.setSearchReferenceNo(arrStrings[12].trim());//检索参考号
            entity.setOldSeqId(arrStrings[13].trim());//原交易流水号
            entity.setOldTradeDate(arrStrings[14].trim());//原交易日期
            entity.setOldTradeReferenceNo(arrStrings[15].trim());//原交易参考号
            entity.setCommission(arrStrings[16].trim());//商户手续费
            entity.setOrderNumber(arrStrings[17].trim());//订单号
            entity.setTradeName(arrStrings[18].trim());//交易名称（中文）
            entity.setCardType(arrStrings[19].trim());//卡类型标识
            entity.setExtCardUseToIn(arrStrings[20].trim());//外卡内用标识
            entity.setTradeChannel(arrStrings[21].trim());//交易渠道
            entity.setExtField1(arrStrings[22].trim());//备用字段1
            entity.setExtField2(arrStrings[23].trim());//备用字段2
            entity.setExtField3(arrStrings[24].trim());//备用字段3
            dataList.add(entity);
        }
        return dataList;
    }
}
