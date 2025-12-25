package com.wishare.finance.domains.invoicereceipt.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.OtherAmountDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDtoV1;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.EditInvoiceReceiptDetailF;
import com.wishare.finance.apps.model.invoice.invoice.fo.EditReceiptF;
import com.wishare.finance.apps.model.invoice.invoice.fo.ReceiptDetailF;
import com.wishare.finance.apps.model.invoice.invoice.fo.ReceiptVoidF;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.model.signature.EsignResultV;
import com.wishare.finance.apps.service.strategy.ReceiptTenant;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.dto.BillDetailQueryDto;
import com.wishare.finance.domains.bill.dto.SendEmailDto;
import com.wishare.finance.domains.bill.dto.SmsDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.facade.MsgFacade;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.invoicereceipt.aggregate.ReceiptA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.PushModeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.PushStateEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiveDetailedRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptSendLogRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptTemplateRepository;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.RedisConst;
import com.wishare.finance.infrastructure.conts.TextContentEnum;
import com.wishare.finance.infrastructure.pdf.PDFUtil;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.NhwReceiptNoUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.TextContentUtil;
import com.wishare.finance.infrastructure.utils.ZipUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Service
@Slf4j
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiptDomainService implements ApiBase {

    private final ReceiptRepository receiptRepository;
    private final InvoiceRepository invoiceRepository;
    /**
     * 票据下发日志
     */
    private final ReceiptSendLogRepository receiptSendLogRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;
    //开票明细表
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    //票本的领用明细表
    private final InvoiceReceiveDetailedRepository invoiceReceiveDetailedRepository;
    private final AdvanceBillRepository advanceBillRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final ReceiptTemplateRepository receiptTemplateRepository;
    private final BillFacade billFacade;
    private final MsgFacade msgFacade;
    private final FileStorage fileStorage;
    private final GatherDetailRepository gatherDetailRepository;
    private final GatherBillRepository gatherBillRepository;


    @Value("${wishare.file.host}")
    private String fileHost;

    /**
     *
     * @param invoiceReceiptId
     * @return
     */
    public ReceiptVDto queryByInvoiceReceiptId(Long invoiceReceiptId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ir.id", invoiceReceiptId);
        return receiptRepository.queryByElement(queryWrapper);
    }




    /**
     * 新增收据command
     *
     * @param command 入参
     * @param map     暂存参数针对业务场景特殊处理()
     * @param receiptTenant 租户信息
     * @return
     */
    public Long invoiceBatch(AddReceiptCommand command, ReceiptTenant receiptTenant, Map<String, Object> map) {
        /*校检该收据号是否被使用*/
        receiptTenant.verifyReceiptNoCanUse(command.getReceiptNo());
        /**校验票据模板 变更command(模板信息id、name)*/
        receiptTenant.verifyTemplate(command);
        AddInvoiceCommand addInvoiceCommand = Global.mapperFacade.map(command, AddInvoiceCommand.class);
        //根据账单id获取账单信息
        List<BillDetailMoreV> billDetailMoreVList = Global.ac.getBean(InvoiceDomainService.class).getBillDetailMoreVList(addInvoiceCommand);
        /** 账单ids */
        command.setBillIds(billDetailMoreVList.stream().map(BillDetailMoreV::getId).distinct().collect(
                Collectors.toList()));
        /*校验相同法定单位，收费对象，账单来源，项目/成本中心*/
        receiptTenant.verifyConsistency(billDetailMoreVList);
        /*校验 账单开票状态*/
        receiptTenant.verifyInvoiceState(billDetailMoreVList, map);

        if (EnvConst.NIANHUAWAN.equals(EnvData.config)) {
            // nhw单独修改编号规则
            String tenantId = ThreadLocalUtil.curIdentityInfo().getTenantId();
            String key = "finance:receiptNo:" + tenantId + ":";
            String receiptNo = RedisHelper.get(key);
            // 每个租户下只会存最后一个最大的一个收据编号值
            if (StringUtils.isNotBlank(receiptNo)) {
                receiptNo = NhwReceiptNoUtil.incrementNumberAndKeepLeadingZeros(receiptNo);
                RedisHelper.set(key, receiptNo);
            } else {
                // 没取到则从数据库里取
                receiptNo = invoiceReceiptRepository.getLastReceiptNo();
                if (StringUtils.isNotBlank(receiptNo) && StringUtils.isNumeric(receiptNo)) {
                    receiptNo = NhwReceiptNoUtil.incrementNumberAndKeepLeadingZeros(receiptNo);
                    RedisHelper.set(key, receiptNo);
                } else {
                    // Redis和数据库都没有编号，认为是当前租户的第一个编号
                    receiptNo = NhwReceiptNoUtil.getFirstNo();
                    RedisHelper.set(key, receiptNo);
                }
            }
            log.info("nhw电子收据编号：{}",receiptNo);
            command.setInvoiceReceiptNo(receiptNo);
        }
        /**初步构建收据聚合对象(数据对象)*/
        ReceiptA receiptA = new ReceiptA(command, billDetailMoreVList);
        /**部分数据补充(数据赋值invoiceReceiptE)*/
        receiptTenant.increaseInvoiceReceiptE(command, receiptA, billDetailMoreVList,map);
        //收集需要处理的账单、收款单、明细开票的原状态
        Map<String, Map<Long, Integer>> stringMapMap = this.doOriginalState(billDetailMoreVList,command.getSupCpUnitId(),map);
        // doInvoiceBatch 发票数据进行入库【发票收据主表】【收据表】【开票明细表】[票本的领用明细表] ...
        final Long invoiceReceiptEId = Global.ac.getBean(ReceiptDomainService.class).doInvoiceBatch(receiptA,command,receiptTenant,stringMapMap,map);
        //记日志
        this.bizLog(receiptA);
        //返回 发票收据主表 id
        return invoiceReceiptEId;
    }


    /**
     * 收集需要处理的账单、收款单、明细开票的原状态
     * 在中交开收据作废收据再开票流程的唤起开票流程需要把状态置换回去
     * @param billDetailMoreVList
     * @param map
     * @return
     */
    private Map<String,Map<Long, Integer>> doOriginalState(List<BillDetailMoreV> billDetailMoreVList, String supCpUnitId, Map<String, Object> map){
        //跳过
        if(Objects.nonNull(map)&&Objects.nonNull(map.get("skip"))){
            return null;
        }
        //账单开票状态
        Map<Long, Integer> billIdsMap = billDetailMoreVList.stream()
                .collect(Collectors.toMap(BillDetailMoreV::getReceiptBillId, BillDetailMoreV::getReceiptBillInvoiceState, (state1, state2) -> state1));
        //明细开票状态
        Map<Long, Integer> gatherDetailBillIdsMap = billDetailMoreVList.stream()
                .filter(billDetailMoreV -> billDetailMoreV.getReceiptGatherDetailBillId() != null)
                .collect(Collectors.toMap(BillDetailMoreV::getReceiptGatherDetailBillId, BillDetailMoreV::getReceiptGatherDetailInvoiceState, (state1, state2) -> state1));
        List<Long> gatherBillIds = billDetailMoreVList.stream().filter(billDetailMoreV -> billDetailMoreV.getGatherBillId() != null).map(BillDetailMoreV::getGatherBillId).distinct().collect(Collectors.toList());
        Map<Long, Integer> gatherBillIdsMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(gatherBillIds)){
            List<GatherBill> gatherBills = gatherBillRepository.list(new LambdaQueryWrapper<GatherBill>().in(GatherBill::getId, gatherBillIds).eq(GatherBill::getSupCpUnitId, supCpUnitId));
            //收款开票状态
            gatherBillIdsMap = gatherBills.stream()
                    .collect(Collectors.toMap(GatherBill::getId, GatherBill::getInvoiceState, (state1, state2) -> state1));
        }
        return new HashMap<>(Map.of(
                "billIdsMap", billIdsMap,
                "gatherDetailBillIdsMap", gatherDetailBillIdsMap,
                "gatherBillIdsMap", gatherBillIdsMap
        ));
    }


    /**
     * 日志记录
     *
     * @param receiptA
     */
    private void bizLog(ReceiptA receiptA) {
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = receiptA.getInvoiceReceiptDetailEList();
        if (CollectionUtils.isNotEmpty(invoiceReceiptDetailEList)) {
            Map<Long, List<InvoiceReceiptDetailE>> detailMap = invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
            String invoiceReceiptNo = receiptA.getInvoiceReceiptE().getInvoiceReceiptNo();
            detailMap.forEach((billId, details) -> {
                long invoiceAmount = details.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
                BizLog.initiate(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.开收据,
                        new Content().option(new ContentOption(new PlainTextDataItem("收据号：" + (Objects.isNull(invoiceReceiptNo) ? "" : invoiceReceiptNo), true)))
                                .option(new ContentOption(new PlainTextDataItem("收据金额：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(invoiceAmount), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            });
        }
    }


    /**
     * 数据入库，数据更新的条件字段是从command 赋值到receiptA 出现空需要排查是否成功赋值
     *
     * @param receiptA
     * @param receiptTenant
     * @param stringMapMap key-billId,value-开票状态
     */
    @Transactional
    public Long doInvoiceBatch(ReceiptA receiptA,AddReceiptCommand command,ReceiptTenant receiptTenant,Map<String, Map<Long, Integer>> stringMapMap,Map<String, Object> map) {
        final ReceiptE receiptE = receiptA.getReceiptE();
        final InvoiceReceiptE invoiceReceiptE = receiptA.getInvoiceReceiptE();
        final List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = receiptA.getInvoiceReceiptDetailEList();
        //发票收据主表(invoice_receipt)
        invoiceReceiptRepository.save(invoiceReceiptE);
        //收据表(receipt)
        receiptRepository.save(receiptE);
        //开票明细表(invoice_receipt_detail)
        invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailEList);
        //票本的领用明细表(invoice_receive_detailed) -- 修改状态已使用
        invoiceReceiveDetailedRepository.useByInvoiceNo(receiptE.getReceiptNo(), invoiceReceiptE.getType());
        //设置bill状态为开票中
        receiptTenant.invoiceBatch(invoiceReceiptE,invoiceReceiptDetailEList,command,stringMapMap);
        //开具成功(签章只是增加开具的可信度和法律效力)
        receiptTenant.handleBillStateFinishInvoice(invoiceReceiptE, invoiceReceiptDetailEList, true,
                command.getInvoiceFlowMonitorId(),map,receiptE.getSignStatus());
        return invoiceReceiptE.getId();
    }


    /**
     * 变更收款人以及收款方式
     *
     * @param command
     * @param invoiceReceiptE
     */
    private void updatePayMsg(AddReceiptCommand command, InvoiceReceiptE invoiceReceiptE) {
        if (CollectionUtil.isNotEmpty(command.getBillIds())) {
            //取账单最新的收款人以及收款方式 收款单明细(gather_detail)
            List<GatherDetail> list = gatherDetailRepository.getNewPaymentList(command.getBillIds(), command.getSupCpUnitId());
            String payeeName = "";
            String payChannel = "";
            if (CollectionUtil.isNotEmpty(list)) {
                payeeName = list.get(0).getPayeeName();
                payChannel = SettleChannelEnum.valueOfByCode(list.get(0).getPayChannel()).getValue();
            }
            invoiceReceiptE.setPayChannel(payChannel);
            invoiceReceiptE.setPayeeName(payeeName);
        }
    }


    /**
     * 收据推送信息
     *
     * @param invoiceReceiptEId
     * @param supCpUnitId
     * @param receiptTenant
     * @param map
     */
    public AfterPdf doPdf(Long invoiceReceiptEId, String supCpUnitId, ReceiptTenant receiptTenant,Map<String, Object> map){
        ReceiptDetailF receiptDetailF = new ReceiptDetailF();
        receiptDetailF.setInvoiceReceiptId(invoiceReceiptEId);
        receiptDetailF.setSupCpUnitId(supCpUnitId);
        //获取收据详情
        ReceiptDetailDtoV1 detail = this.detailV1(receiptDetailF);
        ReceiptE receiptE = detail.getReceiptA().getReceiptE();
        //模板可能为null(收费系统等等推过来的数据是不存在模板信息)
        ReceiptTemplateE receiptTemplateE = detail.getReceiptTemplateE();
        //是否需要签章
        final Integer signStatus = receiptE.getSignStatus();
        /* receiptTemplateE修改其签章状态 */
        receiptTenant.signOnPdf(receiptTemplateE, signStatus,map);
        detail.setSignStatus(Objects.nonNull(receiptTemplateE)?receiptTemplateE.getEnableElectSign():signStatus);
        //是否需要展示开票人
        detail.setClerkStatus(map.containsKey("clerkStatus")?"clerkStatus":null);
        //获取pdf生成之后的流
        final byte[] byteData = pdfByteGenerate(receiptTemplateE, detail,receiptTenant);
        //pdf上传获取文件地址信息
        FileVo fileVo = fileStorage.formalSaveWithInfo(byteData, detail.getReceiptNo() + ".pdf", FormalF.builder().tenantId(getTenantId().get())
                .serverName("receipt").clazz(ReceiptDomainService.class).businessId(UUID.randomUUID().toString()).build());
        /* pdf原件入库[receipt] */
        this.updateReceipt(receiptE.getId(), fileHost + fileVo.getFileKey(), List.of(fileVo));
        return AfterPdf.builder().fileVo(fileVo).detail(detail).signStatus(signStatus).build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static public class AfterPdf{
        @ApiModelProperty("收据源文件")
        FileVo fileVo;

        ReceiptDetailDtoV1 detail;

        @ApiModelProperty("是否需要签章")
        Integer signStatus;
    }

    /**
     * 收据推送信息
     *
     * @param invoiceReceiptEId
     * @param supCpUnitId
     * @param receiptTenant
     * @param map
     */
    @Async
    public void pushMessage(Long invoiceReceiptEId, String supCpUnitId, ReceiptTenant receiptTenant,Map<String, Object> map) {
        //制作pdf
        AfterPdf afterPdf = this.doPdf(invoiceReceiptEId,supCpUnitId,receiptTenant,map);
        //map[endPdf]:生成pdf并且入库，流程结束
        if(Objects.nonNull(map)&&Objects.nonNull(map.get("endPdf"))){
            return;
        }
        //进行三方签章[receipt]
        receiptTenant.signExternalSeal(SignExternalSealVo.builder().signStatus(afterPdf.getSignStatus()).fileVo(afterPdf.getFileVo())
                .fileHost(fileHost).receiptE(afterPdf.getDetail().getReceiptA().getReceiptE()).invoiceReceiptE(afterPdf.getDetail().getReceiptA().getInvoiceReceiptE()).map(map)
                .build());
        ReceiptVDto receiptVDto = this.queryByInvoiceReceiptId(invoiceReceiptEId);
        //处理信息下发付款方【receiptSendLog】（推送消息）
        int way = receiptTenant.receiptSend(receiptVDto);
        //下发后逻辑处理[receipt、invoiceReceipt]
        receiptTenant.afterReceiptSend(receiptVDto, way);
    }





    /**
     * 保存pdf
     * @return
     */
    private FileVo savePdf(Long invoiceReceiptEId,byte[] byteData){

        MultipartFile fileTo = null;
        try {
            fileTo = new MockMultipartFile(invoiceReceiptEId.toString(),invoiceReceiptEId.toString()+".pdf", ContentType.APPLICATION_OCTET_STREAM.toString(), new ByteArrayInputStream(byteData));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("savePdf start:{}",System.currentTimeMillis());
        FileVo fileVo = fileStorage.tmpSave(fileTo, Optional.ofNullable(ThreadLocalUtil.curIdentityInfo()).map(IdentityInfo::getTenantId).get());
        log.info("savePdf end:{}",System.currentTimeMillis());
        return fileVo;
    }






    /**
     * 收据表pdf原件入库
     *
     * @param id
     * @param pdfUrl
     */
    private void updateReceipt(Long id, String pdfUrl, List<FileVo> signFileVos) {
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(id);
        receiptE1.setReceiptUrl(pdfUrl);
        //原文件集合
        receiptE1.setScriptFileVos(signFileVos);
        receiptRepository.updateById(receiptE1);
    }


    /**
     * 票据下发之后进行数据变更
     *
     * @param receiptId
     * @param way         发送结果字段
     * @param receiptVDto
     */
    public void afterReceiptSend(Long receiptId, Integer way, ReceiptVDto receiptVDto) {
        if (way == 0) {
            return;
        }
        //补充身份标识
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(receiptVDto.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

        //true：推送失败 false:推送成功（起码推送成功一个）
        boolean pushFlag = way == -1;
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(receiptId);
        receiptE1.setSendStatus(pushFlag ? 3 : 2);
        receiptE1.setLastPushTime(LocalDateTime.now());

        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setId(receiptVDto.getInvoiceReceiptId());
        invoiceReceiptE.setPushState(pushFlag ? PushStateEnum.推送失败.getCode() : PushStateEnum.已推送.getCode());

        receiptRepository.updateById(receiptE1);
        invoiceReceiptRepository.updateById(invoiceReceiptE);

    }


    /**
     * 票据下发之后进行数据变更
     *
     * @param receiptId
     * @param way         发送结果字段
     * @param receiptVDto
     */
    public void afterReceiptSendVoid(Long receiptId, Integer way, ReceiptVDto receiptVDto) {
        if (way == 0) {
            return;
        }
        //补充身份标识
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(receiptVDto.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

        //true：推送失败 false:推送成功（起码推送成功一个）
        boolean pushFlag = way == -1;
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(receiptId);
        receiptE1.setVoidSendStatus(pushFlag ? 3 : 2);

        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setId(receiptVDto.getInvoiceReceiptId());
        invoiceReceiptE.setVoidPushState(pushFlag ? PushStateEnum.推送失败.getCode() : PushStateEnum.已推送.getCode());

        receiptRepository.updateById(receiptE1);
        invoiceReceiptRepository.updateById(invoiceReceiptE);

    }


    /**
     * pdf生成
     *
     * @param templateE
     * @param detail
     * @return
     */
    private byte[] pdfByteGenerate(ReceiptTemplateE templateE, ReceiptDetailDto detail,ReceiptTenant receiptTenant) {
        byte[] byteData;
        try {
            // 模板路径
            String templatePath = receiptTenant.getReceiptTemplatePath(templateE);
            //数据对象集
            Map<String,   Object> receiptTemplateData = getReceiptTemplateData(detail, templateE);
            //生成pdf数据流
            byteData = PDFUtil.getByteData(templatePath, receiptTemplateData);
//            本地pdf测试
//            try(FileOutputStream f = new FileOutputStream("out.pdf")){f.write(byteData);}catch (Exception e){log.error("生成pdf异常",e);}
//            int i = 1/0;
            return byteData;
        } catch (Exception e) {
            log.error("pdfByteGenerate-->", e);
            ErrorAssertUtils.isThrow400("生成pdf异常");
        }
        //走不到的逻辑代码
        return null;
    }


    /**
     * 0无需推送 -1 推送失败 其他推送成功（部分成功or全部成功）
     * 只做推送以及推送日志记录
     *
     * @param pushModes
     * @param receiptVDto
     * @param map
     * @return
     */
    public int receiptSend(List<Integer> pushModes, ReceiptVDto receiptVDto, Map<String, Object> map) {
        int way = 0;
        //为空或者-1 则不进行推送
        if (CollectionUtils.isEmpty(pushModes) || pushModes.contains(-1)) {
            return way;
        }
        for (Integer mode : pushModes) {
            //发送状态 0:发送成功 1：发送失败
            int status = 0;
            String msg = "";
            switch (PushModeEnum.valueOfByCode(mode)) {
                case 邮箱:
                    SendEmailDto sendEmailDto = SendEmailDto.builder().destEmailAddress(map.get("email").toString()).build().doContent(receiptVDto).doSubject(receiptVDto.getStatutoryBodyName());
                    try {
                        msgFacade.sendEmail(sendEmailDto.getDestEmailAddress(),
                                sendEmailDto.getSubject(),
                                sendEmailDto.getContent(),
                                sendEmailDto.getFiles());
                        way++;
                    } catch (Exception e) {
                        log.error("receiptSend sendEmail error,{}", receiptVDto.getId(), e);
                        msg = e.getMessage();
                        status = 1;
                    }
                    receiptSendLogRepository.insert(receiptVDto, mode, status, msg);
                    break;
                case 手机:
                    if (EnvData.msgFlag) {
                        //手机发送 ：作废 ，申请
                        SmsDto smsDto = SmsDto.builder().pdfUrl(receiptVDto.getUrl()).roomName(receiptVDto.getRoomName()).build().doPhone(receiptVDto.getCommunityId())
                                .doTenantShortName(receiptVDto.getTenantId()).doMobileNum(receiptVDto).doVoidNo(map.get("voidNo"));
                        try {
                            if (StringUtils.isEmpty(smsDto.getVoidNo())) {
                                msgFacade.smsReceipt(smsDto.getMobileNum(), smsDto.getTenantShortName(), smsDto.getRoomName(), smsDto.getPdfUrl(), smsDto.getPhone());
                            } else {
                                msgFacade.smsReceiptVoid(smsDto.getMobileNum(), smsDto.getTenantShortName(), smsDto.getRoomName(), smsDto.getVoidNo(), smsDto.getPhone());
                            }

                            way++;
                        } catch (Exception e) {
                            log.error("receiptSend phone error:[{}],{}", receiptVDto.getId(), e);
                            msg = e.getMessage();
                            status = 1;
                        }
                        receiptSendLogRepository.insert(receiptVDto, mode, status, msg);
                    } else {
                        log.info("ReceiptDomainService.receiptSend:{}", "不发送短信收据");
                    }
                    break;
            }
        }
        return way == 0 ? -1 : way;
    }



    /**
     * 设置推送消息成功
     *
     * @param invoiceReceiptId
     * @param invoiceReceiptNo
     * @param pdfUrl
     */
    @Transactional(rollbackFor = Exception.class)
    public void setPushSuccess(Long invoiceReceiptId, String invoiceReceiptNo, String pdfUrl) {
        invoiceReceiptRepository.setPushState(invoiceReceiptNo, PushStateEnum.已推送);
        if (StringUtils.isNotBlank(pdfUrl)) {
            receiptRepository.setPdfUrl(invoiceReceiptId, pdfUrl);
        }
    }


    /**
     * 生成邮件内容
     *
     * @param roomName        房号
     * @param tenantShortName 租户简称
     * @param url             下载链接
     * @param communityName   项目名称
     * @return
     */
    private String handleEmailContent(String roomName, String tenantShortName, String url, String communityName) {
        Object[] data = new Object[]{roomName, tenantShortName == null ? "" : tenantShortName, url, communityName};
        return TextContentUtil.getEmailContent(TextContentEnum.电子收据, data);
    }

    /**
     * 生成邮件主题
     *
     * @param statutoryBodyName 法定单位
     * @return
     */
    private String handleEmailSubject(String statutoryBodyName) {
        return TextContentUtil.getEmailSubject(TextContentEnum.电子收据, new Object[]{statutoryBodyName});
    }

    /**
     * 组装收据pdf模板参数
     *
     * @param detail
     * @return
     */
    public Map<String, Object> getReceiptTemplateData(ReceiptDetailDto detail, ReceiptTemplateE templateE) {
        // 启用电子签章：0:不启用;1:启用
        Integer enableElectSign = 0;
        String signPictureUrl = "";
        // 签章类型
        Integer electSignType = 0;
        if (Objects.nonNull(templateE)) {
            enableElectSign = templateE.getEnableElectSign();
            electSignType = templateE.getElectSignType();
            signPictureUrl = Objects.isNull(templateE.getSignPictureUrl()) ? "" : fileHost + templateE.getSignPictureUrl().getFileKey();
        }

        HashMap<String, Object> templateData = new HashMap<>();
        templateData.put("communityName", detail.getCommunityName());
        templateData.put("receiptNo", detail.getReceiptNo());
        templateData.put("settleWayChannelStr", detail.getSettleWayChannelStr());
        templateData.put("roomName", detail.getRoomName());
        templateData.put("customerName", detail.getCustomerName()); // 缴纳人
        //票据明细
        templateData.put("invoiceReceiptDetails", detail.getInvoiceReceiptDetail());
        templateData.put("invoiceAmountTotalUppercase", detail.getInvoiceAmountTotalUppercase());
        templateData.put("invoiceAmountTotal", detail.getInvoiceAmountTotal());
        List<OtherAmountDto> otherAmountDto = detail.getOtherAmountDto();
        if (otherAmountDto != null && otherAmountDto.size() > 0) {
            otherAmountDto.forEach(otherAmount -> {
                otherAmount.setPrice(AmountUtils.toScale(otherAmount.getPrice()));
                otherAmount.setOtherAmount(AmountUtils.toScale(otherAmount.getOtherAmount()));
            });
        }
        templateData.put("otherAmounts", detail.getOtherAmountDto());
        templateData.put("remark", detail.getRemark());
        templateData.put("billingTime", DateTimeUtil.formatMonth(detail.getBillingTime().toLocalDate()));
        templateData.put("payTime", detail.getPayTime() == null ? "     " : DateTimeUtil.formatMonth(detail.getPayTime().toLocalDate()));
        templateData.put("statutoryBodyName", detail.getStatutoryBodyName());
        //是否需要签章（走外围签章）
        templateData.put("signStatus", detail.getSignStatus());
        templateData.put("clerk", detail.getClerk());
        templateData.put("clerkStatus", detail.getClerkStatus());
        templateData.put("signPictureUrl", signPictureUrl);
        templateData.put("enableElectSign", enableElectSign);
        templateData.put("electSignType", electSignType);
        templateData.put("payeeName", detail.getPayeeName());
        templateData.put("payChannel", detail.getPayChannel());
        templateData.put("receivableAmountSum", detail.getReceivableAmountSum());
        templateData.put("actualPayAmountSum", detail.getActualPayAmountSum());

        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        /*templateData logo*/
        receiptTenant.pdfLogo(fileHost, templateData);
        return templateData;
    }

    /**
     * 分页查询收据列表
     *
     * @param form
     * @return
     */
    public Page<ReceiptDto> queryPage(PageF<SearchF<?>> form) {
        return receiptRepository.queryPage(form);
    }

    /**
     * 统计收据信息
     *
     * @param form
     * @return
     */
    public ReceiptStatisticsDto statistics(PageF<SearchF<?>> form) {
        return receiptRepository.statistics(form);
    }

    /**
     * 作废收据
     *
     * @param invoiceReceiptId
     * @return
     */
    @Transactional
    public Boolean voidReceipt(Long invoiceReceiptId, String supCpUnitId) {
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        if (null == invoiceReceiptE) {
            throw BizException.throw400("收据不存在");
        }
        if (InvoiceReceiptStateEnum.已作废.getCode().equals(invoiceReceiptE.getState())) {
            throw BizException.throw400("收据已作废");
        }
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptId));
        receiptRepository.voidReceipt(invoiceReceiptE);
        return billFacade.invoiceVoidBatch(invoiceReceiptDetailES, supCpUnitId);

    }


    /**
     * 作废收据 目前针对中交e签宝
     *
     * @param form
     * @return
     */
    public Boolean voidReceiptV(ReceiptVoidF form) {
        ReceiptVDto receiptVDto = this.queryByInvoiceReceiptId(form.getInvoiceReceiptId());
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(receiptVDto), "收据不存在");
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        //作废收据
        return receiptTenant.voidReceiptV(receiptVDto);
    }


    /**
     * 根据账单id获取收据明细
     *
     * @param billId
     * @return
     */
    public List<InvoiceReceiptE> getByBillId(Long billId) {
        return invoiceReceiptRepository.getByBillId(billId, Lists.newArrayList(InvoiceLineEnum.收据.getCode(), InvoiceLineEnum.纸质收据.getCode(), InvoiceLineEnum.电子收据.getCode()));
    }

    /**
     * 短信邮件在定时任务一并发送 这里不进行发送
     *
     * @param invoiceReceiptId
     * @return
     */
    public EsignResultV getByInvoiceReceiptId(Long invoiceReceiptId) {
        ReceiptE receiptE = receiptRepository.getByInvoiceReceiptId(invoiceReceiptId);
        String signApplyNo = receiptE.getSignApplyNo();
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        if (StringUtils.isEmpty(signApplyNo)) {
            //正在处理pdf生成，以及部分异常会进来，调用签署签章接口出现异常情况
            String s = RedisHelper.get(RedisConst.SIGN + invoiceReceiptId);
            if (StringUtils.isNotBlank(s)) {
                //异常修改开票状态
                receiptTenant.signError(invoiceReceiptId,s);
                return EsignResultV.builder().status("9").msg(s).build();
            }
            return EsignResultV.builder().status("0").build();
        }
        //已经申请

        //签署
        EsignResult esignResult = receiptTenant.signResult(signApplyNo);
        //正在签署
        if (StringUtils.equals(esignResult.getStatus(), "1")) {
            return EsignResultV.builder().status("1").build();
        }
        //签署之后 数据保存
        final ReceiptE receiptE1 = receiptTenant.signResultAfter(receiptE.getId(), esignResult);
        //修改签署状态以及签章之后的pdf文件(中交返回的信息在list里 方圆的没有批处理)
        receiptRepository.updateById(receiptE1);
        return EsignResultV.builder()
                .reusltUrl(receiptE1.getSignReceiptUrl())
                .status(esignResult.getStatus())
                .build();
    }


    /**
     * 作废
     * @param invoiceReceiptId
     * @return
     */
    public EsignResultV getByInvoiceReceiptIdVoid(Long invoiceReceiptId) {
        ReceiptE receiptE = receiptRepository.getByInvoiceReceiptId(invoiceReceiptId);
        final String voidSignApplyNo = receiptE.getVoidSignApplyNo();
        ErrorAssertUtils.isFalseThrow400(StringUtils.isEmpty(voidSignApplyNo), "当前收据不能作废");
        //已经申请作废签章
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        EsignResult esignResult = receiptTenant.voidResult(voidSignApplyNo);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(esignResult), "查询作废结果异常");
        String status = esignResult.getStatus();
        //作废中
        if (StringUtils.equals(status, "1")) {
            return EsignResultV.builder().status("1").build();
        }
        ReceiptE receiptE1 = receiptTenant.voidResultAfter(receiptE.getId(), esignResult);
        //修改签署状态以及签章之后的pdf文件
        receiptRepository.updateById(receiptE1);
        return EsignResultV.builder()
                .reusltUrl(receiptE1.getVoidPdf())
                .status(esignResult.getStatus())
                .build();
    }





    /**
     * 根据收款单ids获取收据发票信息
     * @param gatherDetailIds
     * @param supCpUnitId
     * @return
     */
    public InvoiceReceiptDto getReceiptByGatherDetailIds(List<Long> gatherDetailIds,String gatherDetailName) {
        //收据发票主表
        final List<InvoiceBillDetailDto> invoiceBillDetailDtos = invoiceReceiptDetailRepository.getReceiptByGatherDetailIds(
                gatherDetailIds, gatherDetailName);
        if(CollectionUtils.isEmpty(invoiceBillDetailDtos)){
            return null;
        }
        //收据ids,发票ids
        final List<Long> receiptIds = Lists.newArrayList();
        final List<Long> invoiceIds = Lists.newArrayList();
        //map<收据发票id,收据发票主表对象>
        final Map<Long,InvoiceBillDetailDto> kIdvObj = Maps.newHashMap();
        //解析数据源
        this.doInvoiceBillDetailDtos(invoiceBillDetailDtos,receiptIds,invoiceIds,kIdvObj);
        //收据处理 map<收款明细id,收据Obj>
        Map<Long, ReceiptDto> longReceiptDtoMap = this.doReceiptE(receiptIds, kIdvObj);
        //发票处理 map<收款明细id,发票Obj>
        Map<Long, InvoiceDto> longInvoiceDtoMap = this.doInvoiceE(invoiceIds, kIdvObj);
        return InvoiceReceiptDto.builder().longInvoiceDtoMap(longInvoiceDtoMap).longReceiptDtoMap(longReceiptDtoMap).build();

    }

    /**
     * 发票处理
     * @param invoiceIds
     * @param kIdvObj
     */
    private Map<Long, InvoiceDto> doInvoiceE(final List<Long> invoiceIds, final Map<Long, InvoiceBillDetailDto> kIdvObj) {
        if(CollectionUtils.isEmpty(invoiceIds)){
            return null;
        }
        //根据收据发票ids 获取对应的收据发票、收据信息
        LambdaQueryWrapper<InvoiceE> invoiceEWrapper = new LambdaQueryWrapper<InvoiceE>()
                .in(InvoiceE::getInvoiceReceiptId,invoiceIds)
                .eq(InvoiceE::getDeleted,0);
        List<InvoiceE> listByWrapper = invoiceRepository.getListByWrapper(invoiceEWrapper);
        //map<收款单id,收据obj>
        Map<Long,InvoiceDto> mapReceiptDto = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(listByWrapper)){
            listByWrapper.forEach(e->{
                InvoiceDto invoiceDtoTmp = Global.mapperFacade.map(e, InvoiceDto.class);
                //每个收款单只保留对应一个最新的发票，结果：map<收款单id,发票Obj>，map<发票收据id,收款单id> ，收据对象根据发票id-->对应 收款id
                InvoiceBillDetailDto invoiceBillDetailDto = kIdvObj.get(invoiceDtoTmp.getInvoiceReceiptId());
                Long gatherDetailId = invoiceBillDetailDto.getGatherDetailId();
                /** 收款明细id */
                invoiceDtoTmp.setGatherDetailId(gatherDetailId);
                invoiceDtoTmp.setInvoiceReceiptNo(invoiceBillDetailDto.getInvoiceNo());
                if(mapReceiptDto.containsKey(gatherDetailId)){
                    //存在 则比较时间 只保留最新的一份
                    InvoiceDto invoiceDto = mapReceiptDto.get(gatherDetailId);
                    if (invoiceDtoTmp.getGmtModify().isAfter(invoiceDto.getGmtModify())) {
                        mapReceiptDto.put(gatherDetailId, invoiceDtoTmp);
                        return;
                    }
                }
                mapReceiptDto.put(gatherDetailId,invoiceDtoTmp);
            });
        }
        return mapReceiptDto;


    }


    /**
     * 根据收集需要的集合
     * @param invoiceBillDetailDtos
     * @param receiptIds
     * @param invoiceIds
     * @param kIdvObj
     */
    private void doInvoiceBillDetailDtos(List<InvoiceBillDetailDto> invoiceBillDetailDtos, List<Long> receiptIds, List<Long> invoiceIds, Map<Long, InvoiceBillDetailDto> kIdvObj) {
        for(InvoiceBillDetailDto dto:invoiceBillDetailDtos){
            //开票类型
            Integer invoiceType = dto.getInvoiceType();
            if(Objects.nonNull(invoiceType)){
                //收据
                if(invoiceType == 6||invoiceType == 7){
                    receiptIds.add(dto.getId());
                }else{
                    invoiceIds.add(dto.getId());
                }
            }
            kIdvObj.put(dto.getId(),dto);

        }
    }


    /**
     * 收据处理
     * @param listReceiptEByWrapper
     * @param kIdvObj
     * @return
     */
    private Map<Long,ReceiptDto> doReceiptE(final List<Long> receiptIds, final Map<Long,InvoiceBillDetailDto> kIdvObj){
        if(CollectionUtils.isEmpty(receiptIds)){
            return null;
        }
        //根据收据发票ids 获取对应的收据发票、收据信息
        LambdaQueryWrapper<ReceiptE> receiptWrapper = new LambdaQueryWrapper<ReceiptE>()
                .in(ReceiptE::getInvoiceReceiptId,receiptIds)
                .eq(ReceiptE::getDeleted,0);
        List<ReceiptE> listReceiptEByWrapper = receiptRepository.getListByWrapper(receiptWrapper);
        //map<收款单id,收据obj>
        Map<Long,ReceiptDto> mapReceiptDto = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(listReceiptEByWrapper)){
            listReceiptEByWrapper.forEach(e->{
                ReceiptDto receiptTmp = Global.mapperFacade.map(e, ReceiptDto.class);
                //每个收款单只保留对应一个最新的收据，结果：map<收款单id,收据Obj>，map<收据id,收款单id> ，收据对象根据收据id-->对应 收款id
                InvoiceBillDetailDto invoiceBillDetailDto = kIdvObj.get(receiptTmp.getInvoiceReceiptId());
                Long gatherDetailId = invoiceBillDetailDto.getGatherDetailId();
                /** 收款明细id */
                receiptTmp.setGatherDetailId(gatherDetailId);
                receiptTmp.setInvoiceReceiptNo(invoiceBillDetailDto.getInvoiceNo());
                if(mapReceiptDto.containsKey(gatherDetailId)){
                    //存在 则比较时间 只保留最新的一份
                    ReceiptDto receiptE = mapReceiptDto.get(gatherDetailId);
                    if (receiptTmp.getGmtModify().isAfter(receiptE.getGmtModify())) {
                        mapReceiptDto.put(gatherDetailId, receiptTmp);
                        return;
                    }
                }
                mapReceiptDto.put(gatherDetailId,receiptTmp);
            });
        }
        return mapReceiptDto;
    }





    /**
     * @param form
     * @return
     */
    public ReceiptDetailDtoV1 detailV1(ReceiptDetailF form) {
        ReceiptA receiptA = getReceiptAggregate(form.getInvoiceReceiptNo(), form.getInvoiceReceiptId());
        TemplateV templateV = null;
        ReceiptTemplateE receiptTemplateE = null;
        if (Objects.nonNull(receiptA.getInvoiceReceiptE().getReceiptTemplateId())) {
            receiptTemplateE = receiptTemplateRepository.getById(receiptA.getInvoiceReceiptE().getReceiptTemplateId());
            templateV = Global.mapperFacade.map(receiptTemplateE, TemplateV.class);
        }
        //根据在账单id获取账单详情
        List<BillDetailQueryDto> billDetailQueryDtos = receiptA.getInvoiceReceiptDetailEList().stream()
                .map(detail -> new BillDetailQueryDto(detail.getBillId(), detail.getBillType())).distinct().collect(Collectors.toList());
        List<BillDetailMoreV> billDetailMoreVList = billFacade.getAlldetailList(billDetailQueryDtos, form.getSupCpUnitId());
        ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto()
                .general(receiptA.getInvoiceReceiptE()
                        , billDetailMoreVList
                        , receiptA.getInvoiceReceiptDetailEList()
                        , receiptA.getReceiptE(), templateV, receiptTemplateE);
        ReceiptDetailDtoV1 receiptDetailDtoV1 = Global.mapperFacade.map(receiptDetailDto, ReceiptDetailDtoV1.class);
        receiptDetailDtoV1.setReceiptA(receiptA);
        receiptDetailDtoV1.setReceiptTemplateE(receiptTemplateE);
        return receiptDetailDtoV1;
    }


    /**
     * 获取收据详情
     *
     * @param form
     * @return
     */
    public ReceiptDetailDto detail(ReceiptDetailF form) {
        ReceiptA receiptA = getReceiptAggregate(form.getInvoiceReceiptNo(), form.getInvoiceReceiptId());
        TemplateV templateV = null;
        ReceiptTemplateE receiptTemplateE = null;
        if (Objects.nonNull(receiptA.getInvoiceReceiptE().getReceiptTemplateId())) {
            receiptTemplateE = receiptTemplateRepository.getById(receiptA.getInvoiceReceiptE().getReceiptTemplateId());
            templateV = Global.mapperFacade.map(receiptTemplateE, TemplateV.class);
        }
        //根据在账单id获取账单详情
        List<BillDetailQueryDto> billDetailQueryDtos = receiptA.getInvoiceReceiptDetailEList().stream()
                .map(detail -> new BillDetailQueryDto(detail.getBillId(), detail.getBillType())).distinct().collect(Collectors.toList());
        List<BillDetailMoreV> billDetailMoreVList = billFacade.getAlldetailList(billDetailQueryDtos, form.getSupCpUnitId());
        return new ReceiptDetailDto()
                .general(receiptA.getInvoiceReceiptE()
                        , billDetailMoreVList
                        , receiptA.getInvoiceReceiptDetailEList()
                        , receiptA.getReceiptE(), templateV, receiptTemplateE);
    }

    /**
     * 获取收据最新缴费时间
     *
     * @param invoiceReceiptDetailES
     * @return
     */
    public LocalDateTime getReceiptNewPayTime(List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        InvoiceReceiptDetailE firstInvoiceReceiptDetail = invoiceReceiptDetailES.get(0);
        LocalDateTime newPayTime = getBillPayTime(firstInvoiceReceiptDetail.getBillType(), firstInvoiceReceiptDetail.getBillNo());
        for (int i = 1; i < invoiceReceiptDetailES.size(); i++) {
            InvoiceReceiptDetailE detail = invoiceReceiptDetailES.get(i);
            LocalDateTime payTime = getBillPayTime(detail.getBillType(), detail.getBillNo());
            if (newPayTime == null || (payTime != null && newPayTime.compareTo(payTime) < 0)) {
                newPayTime = payTime;
            }
        }
        return newPayTime;
    }

    /**
     * 获取账单缴费
     *
     * @param billType
     * @param billNo
     * @return
     */
    public LocalDateTime getBillPayTime(int billType, String billNo) {
        switch (BillTypeEnum.valueOfByCode(billType)) {
            case 预收账单:
                AdvanceBill advanceBill = advanceBillRepository.queryByBillNo(billNo);
                return advanceBill.getPayTime();
            case 应收账单:
            case 临时收费账单:
                ReceivableBill receivableBill = receivableBillRepository.queryByBillNo(billNo);
                return receivableBill.getChargeTime();
            default:
                return null;
        }
    }

    /**
     * 获取收据聚合
     *
     * @return
     */
    private ReceiptA getReceiptAggregate(String invoiceReceiptNo, Long invoiceReceiptId) {
        //1.获取收据主表的信息
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getByInvoiceReceiptNo(invoiceReceiptNo, invoiceReceiptId);
        //获取收据表信息
        ReceiptE receiptE = receiptRepository.getByInvoiceReceiptId(invoiceReceiptE.getId());
        //获取收据明细信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.getBillIdsByInvoiceReceiptId(invoiceReceiptE.getId());
        return new ReceiptA(invoiceReceiptE, receiptE, invoiceReceiptDetailES);
    }

    /**
     * 校检该收据号是否被使用
     *
     * @param receiptNo
     * @param type
     */
    private void checkReceiptNo(Long receiptNo, Integer type) {
        if (null != receiptNo) {
            ReceiptE receiptE = receiptRepository.getByReceiptNo(receiptNo);
            if (null != receiptE) {
                throw BizException.throw400("该收据号已被使用");
            }
        }
    }

    /**
     * 根据账单ids查询是否含有收据
     *
     * @param billIds
     */
    public Integer getByBillIds(List<Long> billIds) {
        return receiptRepository.getByBillIds(billIds);
    }

    /**
     * 编辑收据
     *
     * @param form
     * @return
     */
    public Boolean editReceipt(EditReceiptF form) {
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(form.getInvoiceReceiptId());
        if (null == invoiceReceiptE) {
            throw BizException.throw400("该收据不存在");
        }
        List<Long> invoiceReceiptDetailIds = form.getEditInvoiceReceiptDetailFList().stream().map(EditInvoiceReceiptDetailF::getInvoiceReceiptDetailId).collect(Collectors.toList());
        //根据发票明细表更新备注
        List<InvoiceReceiptDetailE> detailEList = invoiceReceiptDetailRepository.listByIds(invoiceReceiptDetailIds);

        Map<Long, List<EditInvoiceReceiptDetailF>> editInvoiceReceiptDetailMap = form.getEditInvoiceReceiptDetailFList().stream().collect(Collectors.groupingBy(EditInvoiceReceiptDetailF::getInvoiceReceiptDetailId));
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : detailEList) {
            EditInvoiceReceiptDetailF editInvoiceReceiptDetailF = editInvoiceReceiptDetailMap.get(invoiceReceiptDetailE.getId()).get(0);
            invoiceReceiptDetailE.setRemark(editInvoiceReceiptDetailF.getRemark());
            invoiceReceiptDetailE.setRemarkNew(editInvoiceReceiptDetailF.getRemarkNew());
        }
        return invoiceReceiptDetailRepository.updateBatchById(detailEList);
    }

    /**
     * 修改中交2024-01-15到2024-01-31期间的备注
     *
     * @return
     */
    public Boolean repairReceipt() {

        QueryWrapper<InvoiceReceiptDetailE> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("gmt_create",
                LocalDateTime.of(2024, 1, 15, 0, 0, 0),
                LocalDateTime.of(2024, 1, 31, 23, 59, 59))
                .eq("deleted", 0);
        List<InvoiceReceiptDetailE> list = invoiceReceiptDetailRepository.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        log.info("repairReceipt开始执行");
        Map<Long, List<InvoiceReceiptDetailE>> map = list.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getInvoiceReceiptId));
        int i = 0;
        for (Map.Entry<Long, List<InvoiceReceiptDetailE>> longListEntry : map.entrySet()) {
            InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.queryById(longListEntry.getKey());
            if (ObjectUtil.isNull(invoiceReceiptE)) {
                continue;
            }
            if (StringUtils.isNotBlank(invoiceReceiptE.getRemark())
                    && invoiceReceiptE.getRemark().startsWith("【") && invoiceReceiptE.getRemark().contains("】元")) {
                QueryWrapper<GatherDetail> gatherDetailWrapper = new QueryWrapper<>();
                gatherDetailWrapper.eq("rec_bill_id", longListEntry.getValue().get(0).getBillId())
                        .eq("sup_cp_unit_id", invoiceReceiptE.getCommunityId())
                        .orderByDesc("gmt_create");
                List<GatherDetail> gatherDetails = gatherDetailRepository.list(gatherDetailWrapper);
                String remark = "";
                if (CollectionUtils.isNotEmpty(gatherDetails)) {
                    remark = gatherDetails.get(0).getRemark();
                    log.info(invoiceReceiptE.getId() + "这个的备注修改了:修改后备注:" + remark);
                }
                LambdaUpdateWrapper<InvoiceReceiptE> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(InvoiceReceiptE::getRemark, remark).
                        eq(InvoiceReceiptE::getId, invoiceReceiptE.getId());
                //通过日志简易做个记录
                log.info(invoiceReceiptE.getId() + "修改前备注:" + invoiceReceiptE.getRemark() + ",修改后备注:" + remark);
                invoiceReceiptRepository.update(wrapper);
            }
            i++;

        }
        log.info("repairReceipt修复收据错乱数据总共处理了:" + i + "个数据");
        return true;
    }

    public Boolean updateBillPayTime() {
        List<InvoiceE> invoiceES = invoiceReceiptRepository.listInvoices();
        List<Long> invoiceReceiptIds = invoiceES.stream().map(InvoiceE::getInvoiceReceiptId).collect(Collectors.toList());
        LambdaQueryWrapper<InvoiceReceiptDetailE> invoiceReceiptDetailWrapper = new LambdaQueryWrapper<>();
        invoiceReceiptDetailWrapper.in(InvoiceReceiptDetailE::getInvoiceReceiptId, invoiceReceiptIds);
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.list(invoiceReceiptDetailWrapper);
        invoiceReceiptDetailES.forEach(invoiceReceiptDetailE -> {
            InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.queryById(invoiceReceiptDetailE.getInvoiceReceiptId());
            LambdaQueryWrapper<GatherDetail> gatherDetailWrapper = new LambdaQueryWrapper<>();
            gatherDetailWrapper.eq(GatherDetail::getRecBillId, invoiceReceiptDetailE.getBillId()).eq(GatherDetail::getSupCpUnitId, invoiceReceiptE.getCommunityId());
            List<GatherDetail> gatherDetails = gatherDetailRepository.list(gatherDetailWrapper);
            List<LocalDateTime> billPayTimes = new ArrayList<>();
            gatherDetails.forEach(gatherDetail -> {
                if(invoiceReceiptDetailE.getGmtCreate().isAfter(gatherDetail.getPayTime())){
                    billPayTimes.add(gatherDetail.getPayTime());
                }
            });
            if (CollectionUtils.isNotEmpty(billPayTimes)){
                LocalDateTime maxBillPayTime = Collections.max(billPayTimes);
                LambdaUpdateWrapper<InvoiceReceiptDetailE> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(InvoiceReceiptDetailE::getBillPayTime, maxBillPayTime).
                        eq(InvoiceReceiptDetailE::getId, invoiceReceiptDetailE.getId());
                invoiceReceiptDetailRepository.update(wrapper);
            }
        });
        return true;
    }

    private String handleSettleWayChannelStr(List<GatherDetail> gatherDetails, LocalDateTime gmtCreate) {
        String setttleWayChannelStr = "";
            List<String> setttleWayChannelList = Lists.newArrayList();
                for (GatherDetail gatherDetail : gatherDetails) {
                    if(gmtCreate.isAfter(gatherDetail.getGmtCreate())){
                        SettleWayChannelEnum settleWayChannelEnum = SettleWayChannelEnum.valueOfByCode(gatherDetail.getPayChannel());
                        setttleWayChannelList.add(settleWayChannelEnum.getValue());
                    }
                }
            //去重
            List<String> setttleWayChannelStrList = setttleWayChannelList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(setttleWayChannelStrList)) {
                for (String str : setttleWayChannelStrList) {
                    setttleWayChannelStr = setttleWayChannelStr + str + ",";
                }
                if (setttleWayChannelStr.contains(",")){
                    return setttleWayChannelStr.substring(0,setttleWayChannelStr.length()-1);
                }
            }
        return setttleWayChannelStr;
    }

    public void batchDownloadZip(PageF<SearchF<?>> form, HttpServletResponse response) {
        String fileName = "大有秋收据" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
        Page<ReceiptDto> receiptDtoPage = receiptRepository.queryPage(form);
        List<ReceiptDto> receiptDtoPageRecords = receiptDtoPage.getRecords();
        log.info("批量下载大有秋收据，查询数量：{}",receiptDtoPageRecords.size());
        long start = System.currentTimeMillis();
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            for (ReceiptDto receiptDto : receiptDtoPageRecords) {
                String url = receiptDto.getReceiptUrl();
                String name = receiptDto.getInvoiceReceiptNo();
                log.info("url is :{},name is: {}",url,name);
                if (StringUtils.isBlank(url)) {
                    continue;
                }

                ZipUtils.doWrite(url, name, zipOut);
            }
            long timeConsume = System.currentTimeMillis() - start;
            log.info("批量下载大有秋收据，下载完成，耗时：{}毫秒",timeConsume);
            zipOut.finish(); // 确保所有数据都已写入响应中并关闭流

        } catch (IOException e) {
            // 处理ZIP创建或写入错误
            e.printStackTrace();
        }
    }


}
