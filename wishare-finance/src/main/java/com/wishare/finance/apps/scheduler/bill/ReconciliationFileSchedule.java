package com.wishare.finance.apps.scheduler.bill;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.organization.facade.PaymentFacade;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationYinlianE;
import com.wishare.finance.domains.reconciliation.service.ReconciliationYinlianDomainService;
import com.wishare.finance.infrastructure.conts.CacheEnum;
import com.wishare.finance.infrastructure.remote.enums.ChannelTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.payment.ReconciliationYinlianV;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.wishare.finance.infrastructure.utils.DateTimeUtil.DATE_FORMAT;

/**
 * @author xujian
 * @date 2023/2/19
 * @Description:
 */
@Slf4j
@Component
public class ReconciliationFileSchedule {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private ReconciliationYinlianDomainService reconciliationYinlianDomainService;

    /**
     * 远洋sftp下载文件路径
     */
    private final static String download = "/download/";
    @Value("${reconciliation.channelCode:3}")
    private Integer channelCode;

    /**
     * 远洋租户id
     */
    @Value("${reconciliation.tenantId:108314314140208}")
    private String tenantId;

    @XxlJob("reconciliationFileHandler")
    public void reconciliationHandler() throws IOException {
        log.info("拉取账单数据定时任务开始---------------->");
        //获取对账日期,前一天
        LocalDate recDate = LocalDate.now().minusDays(1);
        //1.构造对账文件名称（商户号_日期）数据唯一性
        String fileName = /*mid + "_" +*/ DateTimeUtil.dateNocPattern(recDate);
        //2.查询该对账文件是否已经获取过
        String fileNameExit = RedisHelper.get(CacheEnum.YINLIAN_RECONCILIATION_FILE.getCacheKey(fileName));
        log.info("fileNameExit:" + fileNameExit);
        //处理商户和租户的映射关系
        handleMidToTenant();
        List<ReconciliationYinlianE> docList = new ArrayList<>();
        if (StringUtils.isBlank(fileNameExit)) {
            // 银联账单拉取
            if ("银联".equals(ChannelTypeEnum.valueOfByCode(channelCode).getDes())){
                byte[] reconciliationDownload = paymentFacade.reconciliationDownload(download, DateTimeUtil.dateNocPattern(recDate), ChannelTypeEnum.valueOfByCode(channelCode).getDes());
                log.info(String.valueOf(reconciliationDownload));
                String reconciliationString = new String(reconciliationDownload, "GBK");
                log.info("银联账单文件解析前原始字符串:" + reconciliationString);
                docList =  readString(reconciliationString, fileName);
                log.info("银联账单数据条数docList:" + docList.size());
                reconciliationYinlianDomainService.addBatch(docList);
                RedisHelper.setAtExpire(CacheEnum.YINLIAN_RECONCILIATION_FILE.getCacheKey(fileName), 30*24*60*60, fileName);
            }else if ("招商银行".equals(ChannelTypeEnum.valueOfByCode(channelCode).getDes())){
                List<ReconciliationYinlianV> reconciliationYinlianVS = paymentFacade.reconciliationDownloadHttp(download, recDate.format(DATE_FORMAT), ChannelTypeEnum.招商银行.getCode().toString());
                List<ReconciliationYinlianE> reconciliationYinlianES = Global.mapperFacade.mapAsList(reconciliationYinlianVS, ReconciliationYinlianE.class);
                reconciliationYinlianDomainService.addBatch(reconciliationYinlianES);
                //6.存入redis30天
                RedisHelper.setAtExpire(CacheEnum.YINLIAN_RECONCILIATION_FILE.getCacheKey(fileName), 30*24*60*60, fileName);
            }else {
                List<ReconciliationYinlianV> reconciliationYinlianVS = paymentFacade.reconciliationDownloadHttp(download, DateTimeUtil.dateNocPattern(recDate), ChannelTypeEnum.valueOfByCode(channelCode).getDes());
                List<ReconciliationYinlianE> reconciliationYinlianES = Global.mapperFacade.mapAsList(reconciliationYinlianVS, ReconciliationYinlianE.class);
                reconciliationYinlianDomainService.addBatch(reconciliationYinlianES);
                //6.存入redis30天
                RedisHelper.setAtExpire(CacheEnum.YINLIAN_RECONCILIATION_FILE.getCacheKey(fileName), 30*24*60*60, fileName);
            }
        }
        log.info("拉取账单数据定时任务结束---------------->");
    }

    /**
     * 处理商户和租户的映射关系
     */
    private void handleMidToTenant() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
    }

    private List<ReconciliationYinlianE> readAllInPayZip(File file, String fileName) throws IOException {
        List<ReconciliationYinlianE> dataList = Lists.newArrayList();
        ZipFile zipFile  = null;
        String destDirPath = file.getParent();
        File targetFile = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            zipFile  = new ZipFile(fileName);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                // 压缩包文件
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                targetFile = new File(destDirPath + "/" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                if(!targetFile.getParentFile().exists()){
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
                // 将压缩文件内容写入到这个文件中
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
            }
            XSSFWorkbook workbook = new XSSFWorkbook(targetFile);
            for(int numSheet = 1; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if(sheet == null) {
                    continue;
                }
                for(int numRow = 10; numRow <= sheet.getLastRowNum(); numRow++) {
                    Row row = sheet.getRow(numRow);
                    if(row == null) {
                        continue;
                    }
                    if ( "小计".equals(row.getCell(1).toString())){
                        break;
                    }
                    ReconciliationYinlianE entity = new ReconciliationYinlianE();
                    entity.setFileName(file.getName());
                    entity.setClearDate(row.getCell(11).toString().trim().replace("-",""));//清算日期
                    entity.setMid("");//商户号
                    entity.setTid("");//终端号
                    entity.setMsgType("");//消息类型
                    entity.setHandleCode("");//处理码
                    entity.setPaymentCode(row.getCell(6).toString());//卡号/付款码
                    entity.setTradeAmount(row.getCell(9).toString());//交易金额
                    entity.setSeqId(row.getCell(13).toString());//交易流水号
                    entity.setTradeDate(row.getCell(11).toString());//交易日期时间
                    entity.setServicePointConditionCode("");//服务点条件码
                    entity.setServiceCode("");//服务码
                    entity.setIssuerIdent("");//发卡机构标识
                    entity.setSearchReferenceNo("");//检索参考号
                    entity.setOldSeqId("");//原交易流水号
                    entity.setOldTradeDate("");//原交易日期
                    entity.setOldTradeReferenceNo(row.getCell(5).toString());//原交易参考号
                    entity.setCommission(row.getCell(10).toString());//商户手续费
                    entity.setOrderNumber(row.getCell(12).toString());//订单号
                    entity.setTradeName("");//交易名称（中文）
                    entity.setCardType("");//卡类型标识
                    entity.setExtCardUseToIn("");//外卡内用标识
                    entity.setTradeChannel("");//交易渠道
                    entity.setExtField1("");//备用字段1
                    entity.setExtField2("");//备用字段2
                    entity.setExtField3("");//备用字段3
                    dataList.add(entity);
                }
            }
        } catch (Exception e) {
            log.error("解析账单异常:"+ e);
        } finally {
            // 关流顺序，先打开的后关闭
            fos.close();
            is.close();
        }
        return dataList;
    }

    private List<ReconciliationYinlianE> readString(String reconciliationString,String fileName){
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

    /**
     * 解析获取到的txt文件
     *
     * @param fileName 文件名称（每天唯一）
     * @return
     */
    private List<ReconciliationYinlianE> readPremiumTxt(File file, String fileName) {
        try {
            InputStream inputStream = new FileInputStream(file);
            //list接收数据
            List<ReconciliationYinlianE> dataList = Lists.newArrayList();
            //接受传入的流 改为gbk编码
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            // 读第一行(暂时不知道有没有头行)
            String lineTxt = "";
            while ((lineTxt = br.readLine()) != null) {
                String[] arrStrings = lineTxt.split("\\|");
                if (arrStrings.length != 25) {
                    return null;
                }
                //对账信息实体
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
            br.close();
            return dataList;
        } catch (Exception e) {
            log.info("对账文件读取异常:{}", e);
            return null;
        }
    }

}
