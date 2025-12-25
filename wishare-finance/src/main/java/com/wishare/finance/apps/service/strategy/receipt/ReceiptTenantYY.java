package com.wishare.finance.apps.service.strategy.receipt;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchF;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.ElectronStampYyF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.service.acl.AclSignService;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.InvoiceFlowMonitorStepTypeEnum;
import com.wishare.finance.domains.bill.entity.InvoiceFlowMonitorE;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.service.InvoiceFlowMonitorService;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.infrastructure.configs.TenantConfigProperties;
import com.wishare.finance.infrastructure.conts.RedisConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @see com.wishare.finance.infrastructure.conts.EnvConstEnum
 */
@Component
@Lazy
@Slf4j
public class ReceiptTenantYY extends AbReceiptTenant {

    /**
     * @param receiptTemplateE
     * @param signStatus       是否需要签章：0 - 是 1 - 否
     */
    @Override
    public void signOnPdf(ReceiptTemplateE receiptTemplateE, Integer signStatus, Map<String, Object> map) {
        //存在 存在模板&&需要盖章&&电子收据
        if (Objects.nonNull(receiptTemplateE) && ObjectUtils.isNotEmpty(signStatus) && signStatus.equals(0) && receiptTemplateE.getTemplateType() == 6) {
            //启用电子签章：0:不启用;1:启用;(关闭内部章，用外部章比如 e签宝)
            receiptTemplateE.setEnableElectSign(0);
        }
    }

    /**
     * 如果开启e签宝签章 则不进行后续信息下发客户，定时任务处理
     * 对收据表的状态以及签章批次号做更新或者记录
     *
     * @param vo
     * @return
     */
    @Override
    public boolean signExternalSeal(SignExternalSealVo vo) {
        Integer signStatus = vo.getSignStatus();
        if (!(ObjectUtils.isNotEmpty(signStatus) && signStatus == 0)) {
            //未开启外部签署
            return true;
        }
        //开启签章,进行签章（定时任务跑获取签章状态信息）获取到签章之后再走推送逻辑
        String stamp = this.baffleCuttingQS(vo);

        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(vo.getReceiptE().getId());
        /** 签署申请编号 */
        receiptE.setSignApplyNo(stamp);
        /** 签署状态 */
        receiptE.setSignSealStatus(StringUtils.isEmpty(stamp) ? 8 : 1);
        //若当前节点就失败了 则处理为失败
        if(StringUtils.isEmpty(stamp)){
            Long id = vo.getInvoiceReceiptE().getId();
            InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
            invoiceReceiptE.setState(3);
            invoiceReceiptE.setId(id);
            Global.ac.getBean(InvoiceReceiptRepository.class).updateById(invoiceReceiptE);
            ObjectMapper objectMapper = new ObjectMapper();
            // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
            objectMapper.registerModule(new JavaTimeModule());
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEs;
            InvoiceFlowMonitorE invoiceFlowMonitorE = Global.ac.getBean(InvoiceFlowMonitorService.class).getBaseMapper()
                    .selectOne(new LambdaQueryWrapper<InvoiceFlowMonitorE>()
                            .eq(InvoiceFlowMonitorE::getReceiptId, id));
            String invoiceReceiptDetailEList = invoiceFlowMonitorE.getInvoiceReceiptDetailEList();
            try {
                invoiceReceiptDetailEs = objectMapper.readValue(invoiceReceiptDetailEList, new TypeReference<>() {});
                Map<String, Map<Long, Integer>> billIdsMap = objectMapper.readValue(invoiceFlowMonitorE.getBillIds(), new TypeReference<>() {});
                //置换开票状态为原状态
                Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEs, vo.getInvoiceReceiptE().getCommunityId(),billIdsMap);
            }catch (Exception e){
                log.error("signExternalSeal:[{}]",e);
            }
        }
        //更新数据[receipt]
        Global.ac.getBean(ReceiptRepository.class).updateById(receiptE);
        //不需要发短信等后续处理,定时任务处理
        return false;
    }

    /**
     * 发起签署
     *
     * @param vo
     * @return
     */
    public String baffleCuttingQS(SignExternalSealVo vo) {
        String stamp;
        //挡板
        if (Global.ac.getBean(TenantConfigProperties.class).getBaffleCutting().equals("on")) {
            stamp = UUID.randomUUID().toString();
        } else {
            //获取签署请求编号
            stamp = Global.ac.getBean(AclSignService.class).stamp(ElectronStampYyF.builder()
                    .companyName(vo.getInvoiceReceiptE().getStatutoryBodyName())
                    .fileVos(List.of(vo.getFileVo()))
                    //同步模板 增加Sign唯一标识
                    .keyword("熊仔王盖章点")
                    .orgIDCardNum(vo.getInvoiceReceiptE().getStatutoryBodyId().toString())
                    .sealType("SFZYZ")
                    .build(), vo.getInvoiceReceiptE().getId(), RedisConst.SIGN);
        }
        return stamp;
    }


    /**
     * 获取签署结果
     */
    @Override
    public EsignResult signResult(String signFlowId) {
        EsignResult esignResult = this.baffleCuttingGetResult(signFlowId);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(esignResult),"获取签署结果异常");
        final List<String> resultUrls = esignResult.getResultUrls();
        if (!CollectionUtils.isEmpty(resultUrls)) {
            esignResult.setReusltUrl(resultUrls.get(0));
        }
        return esignResult;
    }

    @Override
    public EsignResult voidResult(String signFlowId) {
        return this.signResult(signFlowId);
    }

    @Override
    public ReceiptE signResultAfter(Long receiptEId, EsignResult esignResult) {
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(receiptEId);
        final List<String> resultUrls = esignResult.getResultUrls();
        receiptE1.setSignReceiptUrl(CollectionUtils.isEmpty(resultUrls)?null:resultUrls.get(0));
        receiptE1.setSignFileVos(esignResult.getFileVos());
        receiptE1.setSignSealStatus(Integer.valueOf(esignResult.getStatus()));
        return receiptE1;
    }



    /**
     * 获取签署结果
     * @param signFlowId
     * @return
     */
    public EsignResult baffleCuttingGetResult(String signFlowId) {
        EsignResult esignResult;
        //挡板
        if (Global.ac.getBean(TenantConfigProperties.class).getBaffleCutting().equals("on")) {
            FileVo fileVo = new FileVo();
            fileVo.setFileKey("/114964435430504/receipt/ReceiptDomainService/024d3c4b-7bb1-42b9-8959-e50149261591/20231029/1698554005833101.pdf");
            return EsignResult.builder()
                    .resultUrls(List.of("https://saasdev.wisharetec.com/files/114964435430504/receipt/ReceiptDomainService/024d3c4b-7bb1-42b9-8959-e50149261591/20231029/1698554005833101.pdf"))
                    .fileVos(List.of(fileVo))
                    .status("2")
                    .build();
        } else {
            esignResult = Global.ac.getBean(AclSignService.class).querySignResultYy(signFlowId);
        }
        return esignResult;
    }


    /**
     * @param receiptVDto 数据对象集
     * @return
     */
    @Override
    public int receiptSend(ReceiptVDto receiptVDto) {
        //是否需要签章：0 - 是 1 - 否
        final Integer signStatus = receiptVDto.getSignStatus();
        //e签宝
        if (ObjectUtils.isNotEmpty(signStatus) && signStatus.equals(0)) {
            return 0;
        }
        return super.receiptSend(receiptVDto);
    }

    /**
     * 获取文件地址
     * @param no
     * @return
     */
    @Override
    public String eSignUrl(String no) {
        ReceiptE receiptE = Global.ac.getBean(ReceiptRepository.class)
                        .getBaseMapper().queryBySignApplyNo(no);
        if (Objects.isNull(receiptE)) {
            return null;
        }
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(receiptE.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        InvoiceReceiptE invoiceReceiptE = Global.ac.getBean(InvoiceReceiptRepository.class)
                .getById(receiptE.getInvoiceReceiptId());
        if (Objects.isNull(invoiceReceiptE)) {
            return null;
        }
        if (invoiceReceiptE.getState() == 2) {
            return receiptE.getSignReceiptUrl();
        }
        return null;

    }

    /**
     * 修改账单状态为开票中
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param command
     */
    @Override
    public void invoiceBatch(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, AddReceiptCommand command,Map<String,Map<Long, Integer>> billIdsMap) {
        Integer signStatus = command.getSignStatus();
        //不走eSign不需要修改开票状态
        if(Objects.isNull(signStatus) || signStatus.equals(1)){
            return;
        }
        //修改账单状态为开票中
        Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEList, command.getSupCpUnitId());
        // 将Map<Long, Integer>对象转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        String json = null;
        String invoiceReceiptDetailEListJson = null;
        try {
            json = objectMapper.writeValueAsString(billIdsMap);
            invoiceReceiptDetailEListJson = objectMapper.writeValueAsString(invoiceReceiptDetailEList);
        } catch (JsonProcessingException e) {
            log.error("invoiceBatch:[{}]",e);
        }
        //数据留存，修改的账单以及原开票状态 作为初始流程状态
        Global.ac.getBean(InvoiceFlowMonitorService.class).save(
                InvoiceFlowMonitorE.builder()
                        .id(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.INVOICE_FLOW_MONITOR))
                        .receiptId(invoiceReceiptE.getId()).billIds(json)
                        .invoiceReceiptDetailEList(invoiceReceiptDetailEListJson).build());
    }

    /**
     * 远洋eSign进行
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param status 开票\开具状态 true：成功 false：失败
     * @param invoiceFlowMonitorId 流程id
     *
     */
    @Override
    public void handleBillStateFinishInvoice(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, boolean status,
            Long invoiceFlowMonitorId, Map<String, Object> map, Integer signStatus) {
        //远洋eSign获取结果才去开票 在之前则只修改账单为开票中
        if(Objects.nonNull(signStatus) && signStatus.equals(0)){
            return;
        }
        //开票成功
        super.handleBillStateFinishInvoice(invoiceReceiptE, invoiceReceiptDetailEList, status,
                invoiceFlowMonitorId,map,signStatus);
    }


    /**
     * eSign失败需要修改状态为原状态
     * @param invoiceReceiptId
     * @param errorMsg
     */
    @Override
    public void signError(Long invoiceReceiptId,String errorMsg){
        if(!RedisHelper.setNotExists(RedisConst.YY_SIGN_RESULT + invoiceReceiptId,errorMsg)){
            return;
        }
        //第一个处理异常
        InvoiceFlowMonitorE invoiceFlowMonitorE = Global.ac.getBean(InvoiceFlowMonitorService.class).getBaseMapper()
                .selectOne(new LambdaQueryWrapper<InvoiceFlowMonitorE>()
                        .eq(InvoiceFlowMonitorE::getReceiptId, invoiceReceiptId));
        String invoiceReceiptDetailEList = invoiceFlowMonitorE.getInvoiceReceiptDetailEList();
        ObjectMapper objectMapper = new ObjectMapper();
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEs;
        try {
            // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
            objectMapper.registerModule(new JavaTimeModule());
            // key-billids value-开票状态
            Map<String, Map<Long, Integer>> billIdsMap = objectMapper.readValue(invoiceFlowMonitorE.getBillIds(), new TypeReference<>() {});
            invoiceReceiptDetailEs = objectMapper.readValue(invoiceReceiptDetailEList, new TypeReference<>() {});
            ReceiptVDto receiptVDto = Global.ac.getBean(ReceiptDomainService.class).queryByInvoiceReceiptId(invoiceReceiptId);
            //修改账单状态为原状态
            Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEs, receiptVDto.getCommunityId(),billIdsMap);
            //节点记录 [invoice_flow_monitor]
            Global.ac.getBean(InvoiceFlowMonitorService.class).updateById(
                    InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorE.getId())
                            .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_SIGN_RECEIPT_ERROR.getCode())
                            .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_SIGN_RECEIPT_ERROR.getValue())
                            .remark(JSONObject.toJSONString(errorMsg)).build());
        } catch (Exception e) {
            log.error("signError：error ",e);
        }
    }











    /**
     * 签署成功后，才算eSign收据开具成功
     * @param receiptVDto
     */
    @Override
    public void flowRecordSignSuccAfter(ReceiptVDto receiptVDto){
        InvoiceFlowMonitorE invoiceFlowMonitorE = Global.ac.getBean(InvoiceFlowMonitorService.class).getBaseMapper()
                .selectOne(new LambdaQueryWrapper<InvoiceFlowMonitorE>()
                        .eq(InvoiceFlowMonitorE::getReceiptId, receiptVDto.getInvoiceReceiptId()));
        if(Objects.isNull(invoiceFlowMonitorE)){return;}
        //节点记录 [invoice_flow_monitor]
        Global.ac.getBean(InvoiceFlowMonitorService.class).updateById(
                InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorE.getId())
                        .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_WAKE_UP_RECEIPT.getCode())
                        .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_WAKE_UP_RECEIPT.getValue()).build());
        String invoiceReceiptDetailEList = invoiceFlowMonitorE.getInvoiceReceiptDetailEList();
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEs;
        try {
            // key-billids value-开票状态
            Map<String, Map<Long, Integer>> billIdsMap = objectMapper.readValue(invoiceFlowMonitorE.getBillIds(), new TypeReference<Map<String, Map<Long, Integer>>>() {});
            invoiceReceiptDetailEs = objectMapper.readValue(invoiceReceiptDetailEList, new TypeReference<>() {});
            //修改账单状态为原状态
            Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEs, receiptVDto.getCommunityId(),billIdsMap);
            //继续开收据
            Global.ac.getBean(BillFacade.class).handleBillStateFinishInvoice(invoiceReceiptDetailEs,true,receiptVDto.getCommunityId());
            //节点记录 [invoice_flow_monitor]
            Global.ac.getBean(InvoiceFlowMonitorService.class).updateById(InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorE.getId())
                    .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_END.getCode())
                    .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_END.getValue()).build());
        } catch (Exception e) {
            log.error("flowRecordVoidSuccAfter：error",e);
        }
    }






}
