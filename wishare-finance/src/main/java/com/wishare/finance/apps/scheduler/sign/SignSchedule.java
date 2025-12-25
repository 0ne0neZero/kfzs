package com.wishare.finance.apps.scheduler.sign;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wishare.finance.apis.invoicereceipt.InvoiceApi;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillAllDetailV;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchF;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.apps.service.invoicereceipt.ReceiptAppService;
import com.wishare.finance.apps.service.strategy.ReceiptTenant;
import com.wishare.finance.apps.service.strategy.receipt.ReceiptTenantZJ;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.InvoiceFlowMonitorStepTypeEnum;
import com.wishare.finance.domains.bill.entity.InvoiceFlowMonitorE;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.service.InvoiceFlowMonitorService;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理外部签章回调结果 ,例如签章
 */
@Component
@Slf4j
public class SignSchedule {


    @Setter(onMethod_ = @Autowired)
    private ReceiptDomainService receiptDomainService;

    @Setter(onMethod_ = @Autowired)
    private ReceiptRepository receiptRepository;

    @Setter(onMethod_ = @Autowired)
    private InvoiceReceiptRepository invoiceReceiptRepository;

    @Setter(onMethod_ = @Autowired)
    private InvoiceFlowMonitorService invoiceFlowMonitorService;




    /**
     * 对票据下发 只处理1个小时内的数据
     * todo 日志打印可以使用代理 打印方法前后问题
     *
     */
    @XxlJob("signResultHandler")
    public void signResultHandler() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        log.info("signResultHandler is start");
        LocalDateTime previousHour = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);
//        signResult(previousHour);
//        voidResult(previousHour);
//        voidApply(previousHour);
        //签署-下发(结果)
        executor.execute(() -> this.signResult(previousHour));
        //作废-下发(结果)
        executor.execute(() -> this.voidResult(previousHour));
        //其他异常处理（例如，远洋内部异常导致pdf未生成,则数据回滚[修改账单开票状态为原状态，收据表记录删除]）
        executor.execute(this::doExt);
        executor.shutdown();
        log.info("signResultHandler is end ");
    }


    /**
     *
     * 可迁移到远洋的文件里处理
     */
    private void doExt(){
        if(!TenantUtil.bf4()){
            return;
        }
        //获取XxlJob的参数
        String jobParam = XxlJobHelper.getJobParam();
        Map map = JSON.parseObject(jobParam, Map.class);
        String tenantId = (String) map.get("tenantId");
        log.info("doExt tenantId【{}】",tenantId);
        ErrorAssertUtils.isFalseThrow400(StringUtils.isBlank(tenantId),"请于xxjob配置租户信息");
        //补充身份标识
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //只有远洋处理
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<ReceiptE> between = new LambdaQueryWrapper<ReceiptE>()
                .isNull(ReceiptE::getReceiptUrl)
                .eq(ReceiptE::getSignStatus, 0)
                .between(ReceiptE::getGmtCreate, now.minusHours(1), now.minusMinutes(8));
        //获取当前时间往前推8分钟到推1小时内的数据
        List<ReceiptE> errorReceiptUrlList = receiptRepository.list(between);
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        for(ReceiptE r:errorReceiptUrlList){
            try {
                //修改数据信息
                Global.ac.getBean(SignSchedule.class).doResult(r,receiptTenant);
            }catch (Exception e){
                log.error("处理状态结果异常",e);
            }

        }
    }


    /**
     * invoice_receipt -- state 开票失败3
     * 账单|收款单|收款明细 开票状态复原
     * @param r
     */
    @Transactional
    public void doResult(ReceiptE r,ReceiptTenant receiptTenant){
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(r.getId());
        //未知异常
        receiptE.setSignSealStatus(8);
        receiptRepository.updateById(receiptE);
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setState(3);
        invoiceReceiptE.setId(r.getInvoiceReceiptId());
        Global.ac.getBean(InvoiceReceiptRepository.class).updateById(invoiceReceiptE);
        receiptTenant.signError(r.getInvoiceReceiptId(),"pdf生成失败");
    }








        /**
         * 作废回调处理
         *
         * @param previousHour
         */
    private void voidResult(LocalDateTime previousHour) {
        try {
            //获取1个小时内作废中的文件以及作废的文件
            List<ReceiptE> receiptES = receiptRepository.getBaseMapper().signingVoidProgress(List.of(1, 2), previousHour);
            receiptES.stream().forEach(e -> {
                //增加一层 确保当批次数据一条存在问题导致整批次数据都失败
                try {
                    //2、[签署中&&(调用签署结果接口获取签署成功)||签署成功]
                    if (e.getSignVoidStatus().equals(2)||
                            (e.getSignVoidStatus().equals(1) && this.saveReceiptVoid(e.getInvoiceReceiptId(), e.getId(), e.getVoidSignApplyNo(), e.getTenantId()))) {
                        ReceiptVDto receiptVDto = receiptDomainService.queryByInvoiceReceiptId(e.getInvoiceReceiptId());
                        //作废成功之后,流程记录
                        this.flowRecordVoidSuccAfter(receiptVDto);
                        //需要发
                        if (ObjectUtils.isNotEmpty(receiptVDto.getVoidSendStatus()) && receiptVDto.getVoidSendStatus().equals(1)) {
                            int way = receiptDomainService.receiptSend(List.of(1), receiptVDto, new HashMap<>() {{
                                put("voidNo", receiptVDto.getInvoiceReceiptNo());
                            }});
                            //4、付款方通知之后 进行部分数据变更
                            receiptDomainService.afterReceiptSendVoid(e.getId(), way, receiptVDto);
                        }
                    }
                }catch (Exception e1){
                    log.error("voidResult1 error:{}", e1);
                }
            });
        } catch (Exception e) {
            log.error("voidResult2 error:{}", e);
        }
    }


    /**
     * 处理签署中的
     *
     * @param previousHour
     */
    private void signResult(LocalDateTime previousHour) {
        try {
            //获取1个小时内已签署和正在签署中的数据
            List<ReceiptE> receiptES = receiptRepository.getBaseMapper().signingInProgress(List.of(1, 2), previousHour);
            receiptES.stream().forEach(e -> {
                //补充身份标识
                IdentityInfo identityInfo = new IdentityInfo();
                identityInfo.setTenantId(e.getTenantId());
                ThreadLocalUtil.set("IdentityInfo", identityInfo);
                //增加一层 确保当批次数据一条存在问题导致整批次数据都失败
                try {
                    //2、[签署中&&(调用签署结果接口获取签署成功)||签署成功]
                    if ((e.getSignSealStatus().equals(1) && this.saveReceipt(e.getId(), e.getSignApplyNo(), e.getTenantId(),e.getInvoiceReceiptId())) ||
                            e.getSignSealStatus().equals(2)) {
                        //获取核心数据
                        ReceiptVDto receiptVDto = receiptDomainService.queryByInvoiceReceiptId(e.getInvoiceReceiptId());
                        //签署成功之后,流程记录
                        Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName(),ReceiptTenant.class).flowRecordSignSuccAfter(receiptVDto);
                        //需要发送短信
                        if (ObjectUtils.isEmpty(receiptVDto.getSendStatus()) || receiptVDto.getSendStatus().equals(1)) {
                            final List<Integer> pushMode = receiptVDto.getPushMode();
                            if(CollectionUtils.isEmpty(pushMode)||pushMode.contains(-1)){
                                //无需发送直接修改状态为4
                                this.updateReceipt(e.getId(),receiptVDto.getTenantId());
                                return;
                            }
                            //3、签署成功 付款方通知 并且记录推送记录【receipt_send_log】
                            int way = receiptDomainService.receiptSend(pushMode, receiptVDto, new HashMap<>() {{
                                put("email", receiptVDto.getEmail());
                            }});
                            //4、付款方通知之后 部分数据修改(推送状态等信息)[invoice_receipt、receipt]
                            receiptDomainService.afterReceiptSend(e.getId(), way, receiptVDto);
                        }
                    }
                }catch (Exception e1){
                    log.error("signResult1 error:", e1);
                }
            });
        } catch (Exception e) {
            log.error("signResult error:", e);
        }
    }



    /**
     * 作废成功之后进行流程节点记录，当前中交需要流程记录
     * @param receiptVDto
     */
    private void flowRecordVoidSuccAfter(ReceiptVDto receiptVDto){
        if(TenantUtil.bf2()){
            InvoiceFlowMonitorE invoiceFlowMonitorE = invoiceFlowMonitorService.getBaseMapper()
                    .selectOne(new LambdaQueryWrapper<InvoiceFlowMonitorE>()
                            .eq(InvoiceFlowMonitorE::getReceiptId, receiptVDto.getInvoiceReceiptId())
                            .ne(InvoiceFlowMonitorE::getStepType,InvoiceFlowMonitorStepTypeEnum.STEP_END.getCode()));
            if(Objects.isNull(invoiceFlowMonitorE)){return;}
            //节点记录 [invoice_flow_monitor]
            invoiceFlowMonitorService.updateById(InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorE.getId())
                    .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_WAKE_UP_INVOICING.getCode())
                    .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_WAKE_UP_INVOICING.getValue()).build());
            String invoiceParameters = invoiceFlowMonitorE.getInvoiceParameters();
            String invoiceReceiptDetailEList = invoiceFlowMonitorE.getInvoiceReceiptDetailEList();
            ObjectMapper objectMapper = new ObjectMapper();
            // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
            objectMapper.registerModule(new JavaTimeModule());
            InvoiceBatchF invoiceBatchF;
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEs;
            try {
                // key-billids value-开票状态
                Map<String, Map<Long, Integer>> billIdsMap = objectMapper.readValue(invoiceFlowMonitorE.getBillIds(), new TypeReference<Map<String, Map<Long, Integer>>>() {});

                invoiceBatchF = objectMapper.readValue(invoiceParameters, InvoiceBatchF.class);
                // 注册Java 8日期/时间模块 处理java.time.LocalDateTime
                objectMapper.registerModule(new JavaTimeModule());
                invoiceReceiptDetailEs = objectMapper.readValue(invoiceReceiptDetailEList, new TypeReference<>() {});
                /**true:开票不校验是否开具了*/
                //【{doFlow}】：【{(定时任务内置)唤起开票}】开票完成 需要前置处理去处理账单开票状态为原状态
                //修改账单状态为原状态
                Global.ac.getBean(BillFacade.class).invoiceBatch(invoiceReceiptDetailEs, receiptVDto.getCommunityId(),billIdsMap);
                /**处理发送短信的手机号*/
                this.doBuyerPhone(invoiceBatchF,invoiceReceiptDetailEs.get(0).getBillId());
                Long invoiceId = Global.ac.getBean(InvoiceAppService.class).invoiceBatch(invoiceBatchF);
                //节点记录 [invoice_flow_monitor]
                invoiceFlowMonitorService.updateById(InvoiceFlowMonitorE.builder().id(invoiceFlowMonitorE.getId())
                        .invoiceId(invoiceId)
                        .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_END.getCode())
                        .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_END.getValue()).build());
            } catch (Exception e) {
                log.error("flowRecordVoidSuccAfter：error",e);
            }
        }
    }


    /**
     * 处理发短信的手机号 如果没有手机号则不发
     * @param invoiceBatchF
     * @param billId
     */
    private void doBuyerPhone(InvoiceBatchF invoiceBatchF,Long billId){
        if(CollectionUtils.isNotEmpty(invoiceBatchF.getPushMode())&&invoiceBatchF.getPushMode().contains(1)){
            //如果没有手机号则赋值一个手机号
            if(StringUtils.isNotBlank(invoiceBatchF.getBuyerPhone())){
                return;
            }
            //获取第一个账单的信息
            final ReceivableBillAllDetailV allDetail = Global.ac.getBean(ReceivableBillAppService.class)
                    .getAllDetail(billId, ReceivableBillAllDetailV.class, invoiceBatchF.getSupCpUnitId());
            /**增加业主手机号*/
            invoiceBatchF.setBuyerPhone(allDetail.getPayerPhone());
            if(StringUtils.isBlank(invoiceBatchF.getBuyerPhone())){
                /** 不发送标志 */
                invoiceBatchF.setPushMode(List.of(-1));
            }
        }
    }











    /**
     * 更新
     * @param id
     */
    private void updateReceipt(Long id,String tenantId){
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(id);
        receiptE.setSendStatus(4);
        receiptRepository.updateById(receiptE);
    }
    /**
     * 更新
     * @param id
     */
    private void updateReceiptVoid(Long id,String tenantId){
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        ReceiptE receiptE = new ReceiptE();
        receiptE.setId(id);
        receiptE.setVoidSendStatus(4);
        receiptRepository.updateById(receiptE);
    }


    /**
     * 如果返回true 需要继续往下执行，否则停止执行
     * true：签署成功 false:不需要签署或者签署失败等其他情况
     * todo 获取签章 需要处理 如果是多签约来源 需要提取增加防腐层
     *
     * @param receiptEId
     * @param signApplyNo
     * @return
     */
    private boolean saveReceipt(Long receiptEId, String signApplyNo, String tenantId,Long invoiceReceiptId) {
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        //发起签署
        EsignResult esignResult = receiptTenant.signResult(signApplyNo);
        //签署中
        if (StringUtils.equals(esignResult.getStatus(), "1")) {
            return false;
        }
        //补充身份标识
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //组装参数
        ReceiptE receiptE = receiptTenant.signResultAfter(receiptEId, esignResult);
        receiptRepository.updateById(receiptE);
        //签署结果 true:成功 false其他失败
        boolean flag = StringUtils.equals(esignResult.getStatus(), "2") ? true : false;
        //如果不等于零，签署失败账单信息置回去
        if(!flag){
            ReceiptE byId = Global.ac.getBean(ReceiptRepository.class).getById(receiptE.getId());
            InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
            invoiceReceiptE.setState(3);
            invoiceReceiptE.setId(byId.getInvoiceReceiptId());
            Global.ac.getBean(InvoiceReceiptRepository.class).updateById(invoiceReceiptE);
            receiptTenant.signError(invoiceReceiptId,esignResult.getMsg());
        }
        return flag;
    }
    /**
     * 如果返回true 需要继续往下执行，否则停止执行
     * true：签署成功 false:不需要签署或者签署失败等其他情况
     * todo 获取签章 需要处理 如果是多签约来源 需要提取增加防腐层
     *
     * @param receiptEId
     * @param signApplyNo
     * @return
     */
    private boolean saveReceiptVoid(Long invoiceReceiptId,Long receiptEId, String signApplyNo, String tenantId) {
        ReceiptTenant receiptTenant = (ReceiptTenant) Global.ac.getBean(TenantUtil.curTagDevReceiptBeanName());
        //获取签署结果
        EsignResult esignResult = receiptTenant.voidResult(signApplyNo);
        //签署中
        if (Objects.isNull(esignResult)||StringUtils.equals(esignResult.getStatus(), "1")) {
            return false;
        }
        //补充身份标识
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //组装参数
        ReceiptE receiptE = receiptTenant.voidResultAfter(receiptEId, esignResult);
        //修改[receipt]
        receiptRepository.updateById(receiptE);
        //修改作废标志[invoice_receipt]
        InvoiceReceiptE invoiceReceiptE = new InvoiceReceiptE();
        invoiceReceiptE.setId(invoiceReceiptId);
        invoiceReceiptE.setState(6);
        invoiceReceiptRepository.updateById(invoiceReceiptE);
        return StringUtils.equals(esignResult.getStatus(), "2") ? true : false;
    }


}
