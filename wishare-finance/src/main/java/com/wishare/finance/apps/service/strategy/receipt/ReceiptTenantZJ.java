package com.wishare.finance.apps.service.strategy.receipt;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.ElectronStampZjF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.mq.vo.TicketContent;
import com.wishare.finance.apps.service.acl.AclSignService;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.notice.NoticeService;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.InvoiceFlowMonitorStepTypeEnum;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.InvoiceFlowMonitorE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.GatherDetailMapper;
import com.wishare.finance.domains.bill.service.InvoiceFlowMonitorService;
import com.wishare.finance.domains.invoicereceipt.aggregate.ReceiptA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.infrastructure.configs.TenantConfigProperties;
import com.wishare.finance.infrastructure.conts.RedisConst;
import com.wishare.finance.infrastructure.conts.enumTable.receipt.ReceiptSendStatusEnum;
import com.wishare.finance.infrastructure.conts.enumTable.receipt.ReceiptSignStatusEnum;
import com.wishare.finance.infrastructure.pdf.PDFConst;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @see com.wishare.finance.infrastructure.conts.EnvConstEnum
 */
@Component
@Lazy
@Slf4j
public class ReceiptTenantZJ extends AbReceiptTenant {

    @Value("${wishare.finance.profile.env:dev}")
    private String env;


    /**
     * 收据模板路劲
     * @param templateE
     * @return
     */
    @Override
    public String getReceiptTemplatePath(ReceiptTemplateE templateE) {
        return ReceiptTemplateStyleEnum.中交电子收据模板样式.getTemplatePath();
    }

    /**
     * 如果是
     *
     * @param billDetailMoreVList
     * @param map
     */
    @Override
    public void verifyInvoiceState(List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map) {
        //skip 为null 跳过校验
        if (!org.springframework.util.CollectionUtils.isEmpty(map) && Objects.nonNull(map.get(SKIP))) {
            return;
        }
        //开票状态校验
        super.verifyInvoiceState(billDetailMoreVList, map);
    }

    @Override
    public void pdfLogo(String path, HashMap<String, Object> templateData) {
        templateData.put("zhongjiaoLogo", path + PDFConst.ZHONGJIAO_LOGO_PATH);
    }

    /**
     * 收据作废
     *
     * @param receiptVDto
     * @return
     */
    @Override
    public boolean voidReceiptV(ReceiptVDto receiptVDto) {
        //没开启外部签章（e签宝不需要作废）
        if(receiptVDto.getSignStatus() != 0){
            return true;
        }
        //需要发送短信，并且存在作废收据号，则修改发送状态
        if(Objects.nonNull(receiptVDto.getVoidSignApplyNo())){
            final Integer voidSendStatus = receiptVDto.getVoidSendStatus();
            if(Objects.nonNull(voidSendStatus)&&voidSendStatus.equals(1)){
                ReceiptE receiptE = new ReceiptE();
                receiptE.setId(receiptVDto.getId());
                //需要发送短信
                receiptE.setVoidSendStatus(ReceiptSendStatusEnum.need.getCode());
                Global.ac.getBean(ReceiptRepository.class).updateById(receiptE);
                return true;
            }
        }
        //开启签章,进行签章（定时任务跑获取签章状态信息）获取到签章之后再走推送逻辑
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(receiptVDto.getId());
        //获取作废编号
        String stamp = this.baffleCutting(receiptVDto);
        //作废状态
        receiptE.setSignVoidStatus(StringUtils.isEmpty(stamp)? ReceiptSignStatusEnum.unKnow.getCode(): ReceiptSignStatusEnum.signing.getCode());
        //失败增加发消息队列通知
        this.messagesSendVo(StringUtils.isEmpty(stamp),receiptVDto);
        //作废申请编号
        receiptE.setVoidSignApplyNo(stamp);
        //是否需要发送作废短信
        receiptE.setVoidSendStatus(receiptVDto.getVoidSendStatus());
        //更新数据
        Global.ac.getBean(ReceiptRepository.class).updateById(receiptE);
        //不需要发短信等后续处理,定时任务处理
        return false;
    }

    /**
     * 作废
     *
     * @param
     * @param receiptVDto
     * @return
     */
    public String baffleCutting(ReceiptVDto receiptVDto) {
        String stamp;
        //挡板
        if (Global.ac.getBean(TenantConfigProperties.class).getBaffleCutting().equals("on")) {
            stamp = UUID.randomUUID().toString();
        } else {
            //获取作废请求编号
            stamp = Global.ac.getBean(AclSignService.class).stamp(ElectronStampZjF.builder()
                    .fileVos(receiptVDto.getSignFileVos())
                    .keyword("Sign作废")
                    .orgIDCardNum(receiptVDto.getStatutoryBodyId().toString())
                    .sealType("ZFZ")
                    .build(), receiptVDto.getInvoiceReceiptId(), RedisConst.ZF_SIGN);
        }
        return stamp;
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
            stamp = Global.ac.getBean(AclSignService.class).stamp(ElectronStampZjF.builder()
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
     * 如果开启e签宝签章 则不进行后续信息下发客户，定时任务处理
     * 对收据表的状态以及签章批次号做更新或者记录
     *
     * @param vo
     * @return
     */
    @Override
    public boolean signExternalSeal(SignExternalSealVo vo) {
        Map<String, Object> map = vo.getMap();
        if(Objects.nonNull(map)&&Objects.nonNull(map.get(SKIP))){
            return true;
        }
        Integer signStatus = vo.getSignStatus();
        if (!(ObjectUtils.isNotEmpty(signStatus) && signStatus == 0)) {
            //未开启外部签署
            return true;
        }
        //开启签章,进行签章（定时任务跑获取签章状态信息）获取到签章之后再走推送逻辑
        String stamp = "123测试";
        if (!Global.ac.getEnvironment().getProperty("spring.profiles.active").equals(env)) {
            stamp = this.baffleCuttingQS(vo);
        }
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(vo.getReceiptE().getId());
        /* 为了唤起 定时任务 定时任务的重新处理签署文件信息 根据创建时间获取数据 */
        receiptE.setGmtCreate(LocalDateTime.now());
        /** 签署申请编号 */
        receiptE.setSignApplyNo(stamp);
        boolean emptyStamp = StringUtils.isEmpty(stamp);
        /** 签署状态 */
        receiptE.setSignSealStatus(emptyStamp ? 8 : 1);
        //失败增加发消息队列通知
        if (!Global.ac.getEnvironment().getProperty("spring.profiles.active").equals(env)) {
            this.messagesSend(emptyStamp, vo);
        }
        //更新数据[receipt]
        Global.ac.getBean(ReceiptRepository.class).updateById(receiptE);
        //不需要发短信等后续处理,定时任务处理
        return false;
    }


    /**
     *
     */
    private void messagesSendVo(boolean emptyStamp,ReceiptVDto receiptVDto){
        if(!emptyStamp){
            return;
        }
        Long invoiceReceiptId = receiptVDto.getInvoiceReceiptId();
        String roomName = receiptVDto.getRoomName();
        String errMsg = RedisHelper.get(RedisConst.ZF_SIGN + invoiceReceiptId);
        String userId = Optional.ofNullable(ThreadLocalUtil.curIdentityInfo())
                .map(IdentityInfo::getUserId).get();
        String msg = "收款单号【{no}】，{roomName}，开具的收据调用E签宝签章失败，失败原因：${errMsg}。";
        String resultMsg = msg.replace("{no}", receiptVDto.getGatherBillNo())
                .replace("{roomName}", roomName)
                .replace("${errMsg}", errMsg);

        NoticeBusinessD noticeBusinessD = new NoticeBusinessD();
        noticeBusinessD.setTitle("收据签章失败");

        noticeBusinessD.setWbsMessage("1");
        noticeBusinessD.setContent(resultMsg);

        noticeBusinessD.setUserId(List.of(userId));
        noticeBusinessD.setNoticeTime(LocalDateTime.now());
        noticeBusinessD.setModelCode("sign");
        noticeBusinessD.setModelCodeName("收据签署模板");
        noticeBusinessD.setNoticeCardType(1);
        noticeBusinessD.setNoticeCardTypeName("文章消息卡片");
        TicketContent build = TicketContent.builder()
                .id(UUID.randomUUID().toString())
                .communityId(receiptVDto.getCommunityId())
                .title("收据签章失败")
                .bodyMsg(noticeBusinessD.getContent())
                .urlFlag(true)
                .urlName("点击重新作废签署文件")
                .url("/finance/receipt/eSignReceipt/2/" + invoiceReceiptId)
                .build();
        Global.ac.getBean(NoticeService.class).messagesSend(noticeBusinessD,userId,"sign",
                JSONObject.toJSONString(build));
    }




    /**
     *
     */
    private void messagesSend(boolean emptyStamp,SignExternalSealVo vo){
        if(!emptyStamp){
            return;
        }
        Long invoiceReceiptId = vo.getInvoiceReceiptE().getId();
        ReceiptVDto receiptVDto = Global.ac.getBean(ReceiptDomainService.class).queryByInvoiceReceiptId(invoiceReceiptId);
        String roomName = receiptVDto.getRoomName();
        String errMsg = RedisHelper.get(RedisConst.SIGN + vo.getInvoiceReceiptE().getId());
        String userId = Optional.ofNullable(ThreadLocalUtil.curIdentityInfo())
                .map(IdentityInfo::getUserId).get();
        String msg = "收款单号【{no}】，{roomName}，开具的收据调用E签宝签章失败，失败原因：${errMsg}。";
        String resultMsg = msg.replace("{no}", receiptVDto.getGatherBillNo())
                .replace("{roomName}", roomName)
                .replace("${errMsg}", errMsg);

        NoticeBusinessD noticeBusinessD = new NoticeBusinessD();
        noticeBusinessD.setTitle("收据签章失败");

        noticeBusinessD.setWbsMessage("1");
        noticeBusinessD.setContent(resultMsg);

        noticeBusinessD.setUserId(List.of(userId));
        noticeBusinessD.setNoticeTime(LocalDateTime.now());
        noticeBusinessD.setModelCode("sign");
        noticeBusinessD.setModelCodeName("收据签署模板");
        noticeBusinessD.setNoticeCardType(1);
        noticeBusinessD.setNoticeCardTypeName("文章消息卡片");
        TicketContent build = TicketContent.builder()
                .id(UUID.randomUUID().toString())
                .communityId(vo.getInvoiceReceiptE().getCommunityId())
                .title("收据签章失败")
                .bodyMsg(noticeBusinessD.getContent())
                .urlFlag(true)
                .urlName("点击重新签章")
                .url("/finance/receipt/eSignReceipt/1/" + invoiceReceiptId)
                .build();
        Global.ac.getBean(NoticeService.class).messagesSend(noticeBusinessD,userId,"sign",
                JSONObject.toJSONString(build));
    }


    /**
     *
     * @param gatherBillNo 收款单号
     * @param roomName
     * @param errMsg
     */
    public void messagesSendInvoice(String communityId,String gatherBillNo,String roomName,String errMsg){
        String userId = Optional.ofNullable(ThreadLocalUtil.curIdentityInfo())
                .map(IdentityInfo::getUserId).get();
        String msg = "收款单号【{no}】，{roomName}，申请开具的全电普票开票失败，失败原因：${errMsg}。";
        String resultMsg = msg.replace("{no}", gatherBillNo)
                .replace("{roomName}", roomName)
                .replace("${errMsg}", errMsg);

        NoticeBusinessD noticeBusinessD = new NoticeBusinessD();
        noticeBusinessD.setTitle("开票失败");

        noticeBusinessD.setWbsMessage("1");
        noticeBusinessD.setContent(resultMsg);

        noticeBusinessD.setUserId(List.of(userId));
        noticeBusinessD.setNoticeTime(LocalDateTime.now());
        noticeBusinessD.setModelCode("kp");
        noticeBusinessD.setModelCodeName("开票模板");
        noticeBusinessD.setNoticeCardType(1);
        noticeBusinessD.setNoticeCardTypeName("文章消息卡片");
        TicketContent build = TicketContent.builder()
                .id(UUID.randomUUID().toString())
                .communityId(communityId)
                .title("开票失败")
                .bodyMsg(resultMsg)
                .urlFlag(false)
                // .urlName("点击重新签章")
                // .url("/finance/receipt/eSignReceipt/" + invoiceReceiptId)
                .build();
        Global.ac.getBean(NoticeService.class).messagesSend(noticeBusinessD,userId,"sign",
                JSONObject.toJSONString(build));
    }








    /**
     * 获取签署结果
     */
    @Override
    public EsignResult signResult(String signFlowId) {
        EsignResult esignResult = this.baffleCuttingGetResult(signFlowId);
        if(Objects.isNull(esignResult)){
            log.error("获取签署结果为空");
            esignResult = new EsignResult();
            //签署中
            esignResult.setStatus("1");
        }
        // ErrorAssertUtils.isFalseThrow400(Objects.isNull(esignResult),"获取签署结果异常");
        final List<String> resultUrls = esignResult.getResultUrls();
        if (!CollectionUtils.isEmpty(resultUrls)) {
            esignResult.setReusltUrl(resultUrls.get(0));
        }
        return esignResult;
    }

    @Override
    public EsignResult voidResult(String signFlowId) {
        return signResult(signFlowId);
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
            esignResult = Global.ac.getBean(AclSignService.class).querySignResultZj(signFlowId);
        }
        return esignResult;
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
     * 获取签署结果 之后的操作
     * @param receiptEId
     * @param esignResult
     * @return
     */
    @Override
    public ReceiptE voidResultAfter(Long receiptEId, EsignResult esignResult) {
        ReceiptE receiptE1 = new ReceiptE();
        receiptE1.setId(receiptEId);
        receiptE1.setVoidFileVos(esignResult.getFileVos());
        receiptE1.setSignVoidStatus(Integer.valueOf(esignResult.getStatus()));
        final List<String> resultUrls = esignResult.getResultUrls();
        receiptE1.setVoidPdf(CollectionUtils.isEmpty(resultUrls)?null:esignResult.getResultUrls().get(0));
        return receiptE1;
    }



    /**
     * @param receiptTemplateE
     * @param signStatus       是否需要签章：0 - 是 1 - 否
     */
    @Override
    public void signOnPdf(ReceiptTemplateE receiptTemplateE, Integer signStatus,Map<String, Object> map) {
        if(Objects.nonNull(map)&&Objects.nonNull(map.get(SKIP))){
            return;
        }
        //存在 需要盖章&&电子收据
        if (Objects.nonNull(receiptTemplateE)&& ObjectUtils.isNotEmpty(signStatus) && signStatus.equals(0) && receiptTemplateE.getTemplateType() == 6) {
            //启用电子签章：0:不启用;1:启用;(关闭内部章，用外部章比如 e签宝)
            receiptTemplateE.setEnableElectSign(0);
        }
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
     * 中交需要重新置换账单状态为开票中
     *
     * 额外描述
     * *完成开票
     * * *修改 收款单表[gather_bill] 开票金额、开票状态
     * * *修改 账单表 开票状态、挂账状态、开票金额）
     *
     * @param invoiceReceiptDetailEList
     * @param command
     */
    @Override
    public void replaceAgainInvoiceState(List<InvoiceReceiptDetailE> invoiceReceiptDetailEList,
            AddReceiptCommand command) {
        //空,非流程
        Long invoiceFlowMonitorId = command.getInvoiceFlowMonitorId();
        if(Objects.isNull(invoiceFlowMonitorId)){return;}
        //中交e签宝流程状态 true:流程
        List<Long> billIds = invoiceReceiptDetailEList.stream().map(InvoiceReceiptDetailE::getBillId)
                .distinct().collect(Collectors.toList());
        ReceivableBill receivableBill = new ReceivableBill();
        receivableBill.setInvoiceState(BillInvoiceStateEnum.开票中.getCode());
        //修改账单表开票状态为开票中
        Global.ac.getBean(ReceivableBillRepository.class)
                .update(receivableBill,
                        new UpdateWrapper<ReceivableBill>().in("id", billIds)
                                .eq("sup_cp_unit_id", command.getCommunityId()));
        //增加被置换开票状态的账单
        Global.ac.getBean(InvoiceFlowMonitorService.class).updateById(
                InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorId).billIds(new Gson().toJson(billIds)).build());
    }


    // /**
    //  * 修改账单状态为开票中
    //  * @param invoiceReceiptE
    //  * @param invoiceReceiptDetailEList
    //  * @param command
    //  */
    // @Override
    // public void invoiceBatch(InvoiceReceiptE invoiceReceiptE,
    //         List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, AddReceiptCommand command,Map<String,Map<Long, Integer>> billIdsMap) {
    //     //流程
    //     if(Objects.nonNull(command.getInvoiceFlowMonitorId())){
    //         //修改账单状态为开票中
    //         Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEList, command.getSupCpUnitId());
    //         // 将Map<Long, Integer>对象转换为JSON字符串
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         String json = null;
    //         String invoiceReceiptDetailEListJson = null;
    //         try {
    //             json = objectMapper.writeValueAsString(billIdsMap);
    //             // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
    //             objectMapper.registerModule(new JavaTimeModule());
    //             invoiceReceiptDetailEListJson = objectMapper.writeValueAsString(invoiceReceiptDetailEList);
    //         } catch (JsonProcessingException e) {
    //             e.printStackTrace();
    //             log.error("invoiceBatch:{}",e);
    //         }
    //         //数据留存，修改的账单以及原开票状态
    //         Global.ac.getBean(InvoiceFlowMonitorService.class).updateById(
    //                 InvoiceFlowMonitorE.builder().id(command.getInvoiceFlowMonitorId()).billIds(json).invoiceReceiptDetailEList(invoiceReceiptDetailEListJson).build());
    //         return;
    //     }
    //     // super.invoiceBatch(invoiceReceiptE,invoiceReceiptDetailEList,command);
    //
    // }

    /**
     * 开具成功\开票调用财务中心完成开票
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param status 开票\开具状态 true：成功 false：失败
     * @param invoiceFlowMonitorId 流程id
     * @param signStatus 是否需要eSign
     * BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), invoiceReceiptDetailEList, true, invoiceReceiptE.getCommunityId()
     */
    @Override
    public void handleBillStateFinishInvoice(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, boolean status,
            Long invoiceFlowMonitorId,Map<String, Object> map,Integer signStatus) {
        //流程
        if(Objects.nonNull(map)&&Objects.nonNull(map.get(SKIP))){
            return;
        }
        //开票成功
        super.handleBillStateFinishInvoice(invoiceReceiptE, invoiceReceiptDetailEList, status,
                invoiceFlowMonitorId,map,signStatus);
    }
    @Override
    public void increaseInvoiceReceiptE(AddReceiptCommand command, ReceiptA receiptA, List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map) {
        if(Objects.nonNull(map)&&Objects.nonNull(map.get(SKIP))){
            /**已作废*/
            receiptA.getInvoiceReceiptE().setState(6);
        }
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            return;
        }
        super.increaseInvoiceReceiptE(command, receiptA, billDetailMoreVList,map);
        InvoiceReceiptE invoiceReceiptE = receiptA.getInvoiceReceiptE();
        List<GatherDetail> gatherDetails = Global.ac.getBean(GatherDetailMapper.class).selectList(
                new LambdaQueryWrapper<GatherDetail>()
                        .in(GatherDetail::getRecBillId, billIds)
                        .eq(GatherDetail::getDeleted, 0)
                        .eq(GatherDetail::getSupCpUnitId, command.getSupCpUnitId())
                        .isNotNull(GatherDetail::getRemark)
                        .orderByDesc(GatherDetail::getPayTime));
        String remark = "";
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            remark = gatherDetails.get(0).getRemark();
        }
        invoiceReceiptE.setRemark(remark);

    }


}
