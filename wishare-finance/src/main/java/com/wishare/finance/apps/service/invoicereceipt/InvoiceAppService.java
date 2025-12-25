package com.wishare.finance.apps.service.invoicereceipt;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wisahre.multitask.models.fo.MultiTaskF;
import com.wisahre.multitask.models.msg.MultiTaskDetailMsgD;
import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryInvoiceConfListF;
import com.wishare.finance.apps.model.invoice.invoice.dto.*;
import com.wishare.finance.apps.model.invoice.invoice.fo.*;
import com.wishare.finance.apps.model.invoice.invoice.vo.*;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.ContentF;
import com.wishare.finance.apps.model.invoice.nuonuo.fo.InvoiceCallbackF;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoiceCallbackResultV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.scheduler.invoicereceipt.InvoiceSchedule;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.bill.prepay.BillPrepayInfoAppService;
import com.wishare.finance.apps.service.expensereport.ExpenseReportAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.apps.service.strategy.receipt.ReceiptTenantZJ;
import com.wishare.finance.apps.service.voucher.IVoucherInferenceAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum;
import com.wishare.finance.domains.bill.dto.BillRemarkDTO;
import com.wishare.finance.domains.bill.dto.UnInvoiceGatherBillDto;
import com.wishare.finance.domains.bill.dto.UnInvoiceReceivableBillDto;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.domains.configure.organization.entity.StatutoryInvoiceConfE;
import com.wishare.finance.domains.configure.organization.repository.StatutoryInvoiceConfRepository;
import com.wishare.finance.domains.expensereport.aggregate.ExpenseReportA;
import com.wishare.finance.domains.expensereport.enums.KingDeePushStateEnum;
import com.wishare.finance.domains.expensereport.service.ExpenseReportDomainService;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceA;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.EnterInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.SendInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.*;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceSendDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.*;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceZoningRepository;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.enums.NuonuoInvoiceStatusEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoPushModeEnum;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.BuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.vo.invoice.ValidReverseGatherBillRV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.space.ArchivesEnterpriseDetailV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceV;
import com.wishare.finance.infrastructure.remote.vo.user.EnterpriseBaseInfoRV;
import com.wishare.finance.infrastructure.remote.vo.user.UserInfoRv;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.support.lock.LockHelper;
import com.wishare.finance.infrastructure.support.lock.Locker;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.finance.infrastructure.support.thread.AppRunnable;
import com.wishare.finance.infrastructure.support.thread.AppThreadManager;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.finance.infrastructure.utils.MapperFacadeUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.ZipUtils;
import com.wishare.multitask.mq.producer.MultiTaskDetailProducer;
import com.wishare.multitask.services.MultiTaskService;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const.State;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Service
public class InvoiceAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private InvoiceAppService invoiceAppService;

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceDomainService invoiceDomainService;

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private SpacePermissionAppService spacePermissionAppService;

    @Setter(onMethod_ = {@Autowired})
    private BillPrepayInfoAppService prepayPaymentService;

    @Setter(onMethod_ = {@Autowired})
    private ReceiptDomainService receiptDomainService;

    @Setter(onMethod_ = {@Autowired})
    private ReceiptAppService receiptAppService;

    @Setter(onMethod_ = {@Autowired})
    private ReceivableBillDomainService receivableBillDomainService;

    @Setter(onMethod_ = {@Autowired})
    private GatherBillDomainService gatherBillDomainService;

    @Setter(onMethod_ = {@Autowired})
    private GatherDetailRepository gatherDetailRepository;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceSchedule invoiceSchedule;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceReceiptRepository invoiceReceiptRepository;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceRepository invoiceRepository;

    @Value("${wishare.file.host:}")
    private String fileHost;

    @Setter(onMethod_ = {@Autowired})
    private  BillFacade billFacade;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    @Setter(onMethod_ = {@Autowired})
    private UserClient userClient;

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;

    @Setter(onMethod_ = {@Autowired})
    private MultiTaskService multiTaskService;

    @Setter(onMethod_ = {@Autowired})
    private MultiTaskDetailProducer multiTaskDetailProducer;

    @Setter(onMethod_ = {@Autowired})
    private StatutoryInvoiceConfRepository statutoryInvoiceConfRepository;

    @Setter(onMethod_ = {@Autowired})
    private ReceivableBillAppService receivableBillAppService;
    @Setter(onMethod_ = {@Autowired})
    private InvoiceZoningRepository invoiceZoningRepository;
    @Setter(onMethod_ = {@Autowired})
    private ExpenseReportDomainService expenseReportDomainService;
    /**
     * 分页查询开票列表
     *
     * @param form
     * @return
     */
    public PageV<InvoicePageV> queryPage(PageF<SearchF<?>> form) {
        // 处理项目树参数
        if (TenantUtil.bf20()) {
            receiptAppService.spaceQueryOpr(form);
        }
        Page<InvoiceDto> pageResult = invoiceDomainService.queryPage(form);
        return PageV.of(form, pageResult.getTotal(), MapperFacadeUtil.getMoneyMapperFacade().mapAsList(pageResult.getRecords(), InvoicePageV.class));
    }

    /**
     * 根据条件查询开票列表
     *
     * @param form
     * @return
     */
    public List<BillInvoiceDto> listByBillIds(InvoiceListF form) {
        List<BillInvoiceDto> invoiceDtos = invoiceDomainService.listByBillIds(form);
        return invoiceDtos;
    }

    /**
     * 批量开票
     *
     * @param form
     * @return
     */
    /*public Long invoiceBatch(InvoiceBatchF form) {
        //前置校验
        prepayPaymentService.checkPrepay(form.getBillIds(),form.getSupCpUnitId());
        form.checkParam();
        //只处理单发票的重提交问题
        String messageKey = String.valueOf(JSON.toJSONString(form.getBillIds()).hashCode());
        if (!RedisHelper.setNotExists(messageKey, "1")) {
            throw BizException.throw400("同一张发票10秒内不允许重复开具");
        }
        RedisHelper.expire(messageKey, 10);
        try {
            //校检价税合计（有区分单条和多条）
            BillSimpleInfoV billSimpleInfoV = billDomainServiceOld.getBillSimpleInfoV(form.getBillIds(), form.getBillType(), form.getSupCpUnitId());
            AddInvoiceCommand command = Global.mapperFacade.map(form, AddInvoiceCommand.class);
            //检查开票金额
            command.checkInvoice(billSimpleInfoV.getCanInvoiceAmount());
            //设置开票人
            if (StringUtils.isBlank(form.getClerk())) {
                getUserName().ifPresentOrElse(command::setClerk, () -> command.setClerk(form.getClerk()));
            }
            //校检账单是否已开收据(receipt、invoice_receipt、invoice_receipt_detail)
            Integer receiptNum = receiptDomainService.getByBillIds(form.getBillIds());
            if (receiptNum != null && receiptNum != 0 && !form.isCheckReceiptFlag()) {
                throw BizException.throw400("该批次账单存在已开收据，请先回收收据");
            }

            return invoiceDomainService.invoiceBatch(command, getTenantId().get());
        }catch (Exception e){
            RedisHelper.delete(messageKey);
            log.error("开票异常：{}", e.getMessage(), e);
            throw e;
        }

    }*/


    /**
     * 批量开票
     *
     * @param form
     * @return
     */
    public Long invoiceBatch(InvoiceBatchF form) {
        //只处理单发票的重提交问题
        String messageKey = getInvoiceRedisKey(form);
        if (!RedisHelper.setNotExists(messageKey, "1")) {
            throw BizException.throw400("同一张发票10秒内不允许重复开具");
        }
        RedisHelper.expire(messageKey, 10);
        List<BillDetailMoreV> billDetailMoreVList = null;
        try {
            /*前置校验*/
            prepayPaymentService.checkPrepay(form.getBillIds(),form.getSupCpUnitId());
            /*校检开票入参*/
            form.checkParam();
            AddInvoiceCommand command = Global.mapperFacade.map(form, AddInvoiceCommand.class);
            //获取收款单明细(额外收集账单ids)
            billDetailMoreVList = filterInvoiceBillDetails(this.getBillDetailMoreVList(command));
            if (InvoiceLineEnum.定额发票.getCode().equals(command.getType())) {
                return quotaInvoiceBatch(command,billDetailMoreVList);
            }
            billDetailMoreVList = invoiceDomainService.customizedInvoiceAmount(billDetailMoreVList, command);
            //设置开票人
            if (StringUtils.isBlank(form.getClerk())) {
                getUserName().ifPresentOrElse(command::setClerk, () -> command.setClerk(form.getClerk()));
            }
            /*检查开票金额*/
            command.checkInvoice(this.getCanInvoiceAmount(billDetailMoreVList));
            /*校检账单是否已开收据(receipt、invoice_receipt、invoice_receipt_detail)*/
//            this.checkReceipt(command.getBillIds());
            /*校检开票中,已经开票 相同法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)*/
            billFacade.checkBillDetail(billDetailMoreVList,command);
            return invoiceDomainService.invoiceBatch(command, billDetailMoreVList);
        }catch (Exception e){
            RedisHelper.delete(messageKey);
            log.error("开票异常：{}", e.getMessage(), e);
            if(TenantUtil.bf2()){
                if(CollectionUtils.isNotEmpty(billDetailMoreVList)){
                    BillDetailMoreV billDetailMoreV = billDetailMoreVList.get(0);
                    String gatherBillNo = billDetailMoreV.getGatherBillNo();
                    if(StringUtils.isNotBlank(gatherBillNo)){
                        Global.ac.getBean(ReceiptTenantZJ.class).messagesSendInvoice(billDetailMoreV.getCommunityId(),billDetailMoreV.getGatherBillNo(),billDetailMoreV.getRoomName(),e.getMessage());
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 中交环境开发票过滤预收账单
     * @param billDetailMoreVList
     * @return
     */
    private List<BillDetailMoreV> filterInvoiceBillDetails(List<BillDetailMoreV> billDetailMoreVList) {
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            return billDetailMoreVList.stream()
                    .filter(billInfo ->
                            !BillTypeEnum.预收账单.equalsByCode(Integer.parseInt(billInfo.getBillType())))
                    .collect(Collectors.toList());
        }
        return billDetailMoreVList;
    }

 /*   public static void main(String[] args) {
        InvoiceBatchF invoiceBatchF = JSON.parseObject("{\n"
                + "    \"billIds\": [\n"
                + "      167826109588173,\n"
                + "      167826109589174,\n"
                + "      167826109590175,\n"
                + "      167826109590176,\n"
                + "      167826109591177,\n"
                + "      167826109591178,\n"
                + "      167826109592179,\n"
                + "      167826109592180,\n"
                + "      167826109593181,\n"
                + "      167826109594182,\n"
                + "      167826109594183,\n"
                + "      167826109595184,\n"
                + "      167826109595185\n"
                + "    ],\n"
                + "    \"billType\": 3,\n"
                + "    \"buyerAccount\": \"\",\n"
                + "    \"buyerName\": \"55号车位租赁费\",\n"
                + "    \"clerk\": \"卢晓娜\",\n"
                + "    \"freeTax\": 0,\n"
                + "    \"invSource\": 1,\n"
                + "    \"invoiceTitleType\": 1,\n"
                + "    \"invoiceType\": 1,\n"
                + "    \"isPushOwner\": 0,\n"
                + "    \"priceTaxAmount\": 110000,\n"
                + "    \"pushMode\": [\n"
                + "      -1\n"
                + "    ],\n"
                + "    \"signStatus\": 1,\n"
                + "    \"supCpUnitId\": \"f0d7493d22a463cb7915797f3ced08d4\",\n"
                + "    \"sysSource\": 1,\n"
                + "    \"type\": 8\n"
                + "  }", InvoiceBatchF.class);
        System.out.println(String.valueOf(JSON.toJSONString(invoiceBatchF.getBillIds()).hashCode()));
    }*/

    /**
     * 获取发票幂等性校验的key
     * @param form
     * @return
     */
    private String getInvoiceRedisKey(InvoiceBatchF form) {
        if (Objects.isNull(form.getGatherBillType())) {
            // 账单开票
            return String.valueOf(JSON.toJSONString(form.getBillIds()).hashCode());
        } else {
            // 收款单或收款明细开票
            if (form.getGatherBillType() == 0) {
                return String.valueOf(JSON.toJSONString(form.getGatherBillIds()).hashCode());
            } else {
                return String.valueOf(JSON.toJSONString(form.getGatherDetailBillIds()).hashCode());
            }
        }
    }

    /**
     * 异步批量开票
     *
     * @param form
     * @return
     */
    public void asyncInvoiceBatch(InvoiceBatchF form) {
        ExecutorService singleExecutor = Executors.newFixedThreadPool(1);
        singleExecutor.execute(() -> {
            //按收款单分组
            Map<Long, List<GatherInvoiceBatchF>> BatchListMap = form.getGatherInvoiceBatchFList().stream().collect(Collectors.groupingBy(GatherInvoiceBatchF::getGatherBillId));
            String taskId = this.createMultiTask(form.getTaskName(), form.getTaskCode(), BatchListMap.size());
            List<MultiTaskDetailMsgD> multiTaskDetailMsgDList = new ArrayList<>();
            for (Map.Entry<Long, List<GatherInvoiceBatchF>> entry : BatchListMap.entrySet()){
                MultiTaskDetailMsgD multiTaskDetailMsgD = new MultiTaskDetailMsgD();
                try {
                    //获取付款人手机号
                    getPayerPhone(form, entry.getValue().get(0).getPayerId());
                    //设置法定单位所绑定分机号机器编码等信息
                    setStatutoryInvoiceConf(form, entry);
                    List<GatherInvoiceBatchF> invoiceBatchFS = entry.getValue();
                    form.setGatherInvoiceBatchFList(invoiceBatchFS);
                    form.setGatherDetailBillIds(invoiceBatchFS.stream().map(GatherInvoiceBatchF::getGatherDetailBillId).collect(Collectors.toList()));
                    //生成异步日志信息
                    generateMultiTaskDetailMsgD(entry, multiTaskDetailMsgD, taskId, form.getPriceTaxAmount());
                    if(InvoiceLineEnum.电子收据.getCode() == form.getInvoiceType()){
                        ReceiptBatchF receiptBatchF = Global.mapperFacade.map(form, ReceiptBatchF.class);
                        receiptAppService.invoiceBatch(receiptBatchF);
                    }else {
                        this.invoiceBatch(form);
                    }
                    multiTaskDetailMsgDList.add(multiTaskDetailMsgD);
                } catch (Exception e) {
                    multiTaskDetailMsgD.fail();
                    multiTaskDetailMsgD.setHandlerDesc(e.getMessage());
                    multiTaskDetailMsgDList.add(multiTaskDetailMsgD);
                    log.error("开票异常：{}", e.getMessage(), e);
                }
                if(multiTaskDetailMsgDList.size() == 10){
                    multiTaskDetailProducer.sendMessageToMultiTaskWithIentity(multiTaskDetailMsgDList, getIdentityInfo().get());
                    multiTaskDetailMsgDList.clear();
                }
            }
        });
        singleExecutor.shutdown();
    }

    /**
     * 设置法定单位所绑定分机号机器编码等信息
     * @param form
     * @param entry
     */
    private void setStatutoryInvoiceConf(InvoiceBatchF form, Map.Entry<Long, List<GatherInvoiceBatchF>> entry) {
        if(InvoiceLineEnum.全电普票.getCode() == form.getType() || InvoiceLineEnum.全电专票.getCode() == form.getType()){
            StatutoryInvoiceConfListF statutoryInvoiceConfListF = new StatutoryInvoiceConfListF();
            statutoryInvoiceConfListF.setStatutoryBodyId(entry.getValue().get(0).getStatutoryBodyId());
            List<StatutoryInvoiceConfE> statutoryInvoiceConfES = statutoryInvoiceConfRepository.queryList(statutoryInvoiceConfListF);
            if (CollectionUtils.isEmpty(statutoryInvoiceConfES)) {
                throw BizException.throw400("开票失败:该收款单法定单位下查不到机器编码、分机号等信息" );
            }
            form.setMachineCode(statutoryInvoiceConfES.get(0).getMachineCode());
            form.setExtensionNumber(statutoryInvoiceConfES.get(0).getExtensionNumber());
            form.setTerminalNumber(statutoryInvoiceConfES.get(0).getTerminalNumber());
        }
    }

    /**
    * 轮询获取付款人手机号
     */
    private void getPayerPhone(InvoiceBatchF form, String payerId) {
        if (!Objects.equals(NuonuoPushModeEnum.手机.getCode(), form.getPushMode())) {
            return;
        }
        if (Objects.equals(IsPushOwnerEnum.不推送.getCode(), form.getIsPushOwner())) {
            return;
        }
        EnterpriseBaseInfoRV enterpriseInfo = userClient.getEnterpriseInfo(Long.parseLong(payerId), null);
        if (Objects.nonNull(enterpriseInfo) && StringUtils.isNotBlank(enterpriseInfo.getContactorMobileNum())) {
            form.setBuyerPhone(enterpriseInfo.getContactorMobileNum());
            return;
        }
        UserInfoRv userInfo = userClient.getUserInfo(payerId, null);
        if (Objects.nonNull(userInfo) && StringUtils.isNotBlank(userInfo.getMobileNum())) {
            form.setBuyerPhone(userInfo.getMobileNum());
            return;
        }
        ArchivesEnterpriseDetailV enterpriseDetail = spaceClient.getEnterpriseDetail(Long.parseLong(payerId));
        if (Objects.nonNull(enterpriseDetail) && Objects.nonNull(enterpriseDetail.getArchivesEnterpriseBaseV()) && StringUtils.isNotBlank(enterpriseDetail.getArchivesEnterpriseBaseV().getContactorMobileNum())) {
            form.setBuyerPhone(enterpriseDetail.getArchivesEnterpriseBaseV().getContactorMobileNum());
        }
    }

    /**
     * 生成多任务详情--生成异步日志信息
     * @param entry
     * @param multiTaskDetailMsgD
     * @param taskId
     */
    private void generateMultiTaskDetailMsgD(Map.Entry<Long, List<GatherInvoiceBatchF>> entry, MultiTaskDetailMsgD multiTaskDetailMsgD, String taskId, Long priceTaxAmount) {
        multiTaskDetailMsgD.setTaskId(taskId);
        multiTaskDetailMsgD.setBizId(String.valueOf(entry.getKey()));
        multiTaskDetailMsgD.setTenantId(getIdentityInfo().get().getTenantId());
        multiTaskDetailMsgD.success();
        List<GatherInvoiceBatchF> invoiceBatchFS = entry.getValue();
        InvoiceGatherBillV invoiceGatherBillV = new InvoiceGatherBillV();
        invoiceGatherBillV.setSupCpUnitName(invoiceBatchFS.get(0).getSupCpUnitName());
        invoiceGatherBillV.setGatherBillNo(invoiceBatchFS.get(0).getGatherBillNo());
        invoiceGatherBillV.setCpUnitName(invoiceBatchFS.get(0).getCpUnitName());
        invoiceGatherBillV.setChargeItemName(invoiceBatchFS.get(0).getChargeItemName());
        invoiceGatherBillV.setExpensePeriod(assembleMergeStartEndTimeDate(invoiceBatchFS));
        invoiceGatherBillV.setInvoiceAmount(new BigDecimal(priceTaxAmount).multiply(new BigDecimal(100)));
        multiTaskDetailMsgD.setFormData(JSON.parseObject(JSON.toJSONString(invoiceGatherBillV), new TypeReference<>() {}));
    }

    /**
     * 组装合并开始结束时间
     * @param invoiceBatchFS
     * @return
     */
    private String assembleMergeStartEndTimeDate(List<GatherInvoiceBatchF> invoiceBatchFS) {
        invoiceBatchFS.sort((o1, o2) -> {
            if (o1.getChargeStartTime().isAfter(o2.getChargeEndTime())) {
                return 1;
            } else if (o1.getChargeStartTime().isBefore(o2.getChargeEndTime())) {
                return -1;
            }
            return 0;
        });
        LocalDateTime startTime = invoiceBatchFS.get(0).getChargeStartTime();
        LocalDateTime endTime = invoiceBatchFS.get(0).getChargeEndTime();
        Iterator<GatherInvoiceBatchF> iterator = invoiceBatchFS.iterator();
        GatherInvoiceBatchF gatherInvoiceBatchF = iterator.next();
        // a1
        while (iterator.hasNext()) {
            GatherInvoiceBatchF nextGatherInvoiceBatchF = iterator.next();
            LocalDateTime firstStartTime = gatherInvoiceBatchF.getChargeStartTime();
            LocalDateTime firstEndTime = gatherInvoiceBatchF.getChargeEndTime();
            LocalDateTime secondStartTime = nextGatherInvoiceBatchF.getChargeStartTime();
            LocalDateTime secondEndTime = nextGatherInvoiceBatchF.getChargeEndTime();
            // 判断是否需要合并
            if (DateTimeUtil.daysBetween(firstEndTime, secondStartTime) <= 1) {
                if (DateTimeUtil.daysBetween(firstEndTime, secondEndTime) >= 0) {
                    if(DateTimeUtil.daysBetween(firstStartTime,secondStartTime) <= 1){
                        startTime = secondStartTime;
                    }
                    endTime = secondEndTime;
                    iterator.remove();
                }
            }
        }
        return startTime +"至"+ endTime;
    }

    /**
     * 校检账单是否已开收据
     * @param billIds
     */
    private void checkReceipt(List<Long> billIds) {
        Integer receiptNum = receiptDomainService.getByBillIds(billIds);
        if (receiptNum != null && receiptNum != 0) {
            throw BizException.throw400("该批次账单存在已开收据，请先回收收据");
        }
    }

    /**
     * 获取可以开发票金额
     * @param billDetailMoreVList
     * @return
     */
    private Long getCanInvoiceAmount(List<BillDetailMoreV> billDetailMoreVList) {
        Long canInvoiceAmount = 0L;
        for (BillDetailMoreV billDetail : billDetailMoreVList) {
            canInvoiceAmount += billDetail.getCanInvoiceAmount();
        }
        return canInvoiceAmount;
    }

    /**
     * 获取收款单明细(额外收集账单ids)
     * @param command
     * @return
     */
    public List<BillDetailMoreV> getBillDetailMoreVList(AddInvoiceCommand command) {
        return  invoiceDomainService.getBillDetailMoreVList(command);
    }

    /**
     * 发票红冲
     *
     * @param form
     * @return
     */
    public Boolean invoiceBatchRed(InvoiceBatchRedF form) {
        return invoiceDomainService.invoiceBatchRed(form.getInvoiceReceiptId(),form.getInvoiceReceiptNo());
    }

    /**
     * 统计发票信息
     *
     * @param form
     * @return
     */
    public InvoiceStatisticsDto statistics(PageF<SearchF<?>> form) {
        if (TenantUtil.bf20()) {
            receiptAppService.spaceQueryOpr(form);
        }
        return invoiceDomainService.statistics(form);
    }

    /**
     * 分页查询发票和收据列表
     *
     * @param form form
     * @param type 1收费 2合同
     * @return PageV
     */
    public <T> PageV<T> queryDetailPage(PageF<SearchF<?>> form, Integer type, Class<T> tClass) {
        Page<InvoiceAndReceiptDto> pageResult = invoiceDomainService.queryDetailPage(form, type);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), tClass));
    }

    /**
     * 分页查询发票和收据列表-新模式
     *
     * @param form form
     * @return PageV
     */
    public <T> PageV<T> queryDetailPageNew(PageF<SearchF<?>> form, Class<T> tClass) {
        Page<InvoiceAndReceiptDto> pageResult = invoiceDomainService.queryDetailPageNew(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), tClass));
    }

    /**
     * 查询发票和收据合计金额(用于流水领用)
     *
     * @return Long
     */
    public InvoiceAndReceiptStatisticsV statisticsAmount(PageF<SearchF<?>> form) {
        InvoiceAndReceiptStatisticsDto invoiceAndReceiptStatisticsDto = invoiceDomainService.statisticsAmount2(form);
        return Global.mapperFacade.map(invoiceAndReceiptStatisticsDto, InvoiceAndReceiptStatisticsV.class);
    }

    /**
     * 根据账单id批量获取发票数据
     *
     * @param advanceBillIds
     * @param billType
     * @return
     */
    public Optional<Map<Long, List<InvoiceBillDto>>> getBillInvoiceMap(List<Long> advanceBillIds, Integer billType) {
        return invoiceDomainService.getBillInvoiceMap(advanceBillIds, billType);
    }

    /**
     * 作废发票
     *
     * @param invoiceReceiptId
     * @return
     */
    public Boolean voidInvoice(Long invoiceReceiptId) {
        return invoiceDomainService.voidInvoice(invoiceReceiptId);
    }

    /**
     * 获取发票子表数据
     *
     * @param form
     * @return
     */
    public List<InvoiceChildDto> invoiceChildList(InvoiceChildF form) {
        return invoiceDomainService.invoiceChildList(form.getInvoiceReceiptId());
    }

    /**
     * 根据发票主表id获取发票url地址
     * @param invoiceReceiptId
     * @return
     */
    public NuonuoInvoiceInfoDto getNuonuoInvoiceUrl(Long invoiceReceiptId) {
        return  invoiceDomainService.getNuonuoInvoiceUrl(invoiceReceiptId);
    }

    /**
     *  开具蓝票(无校检)
     *
     * @param form
     * @return
     */
    public Long invoiceBatchBlue(InvoiceBatchBlueF form) {
        return invoiceDomainService.invoiceBatchBlue(form);
    }

    @Autowired
    @Qualifier("receiptVoucherInferenceAppService")
    private IVoucherInferenceAppService iVoucherInferenceAppService;

    /**
     * 录入进项发票
     *
     * @param form
     * @return
     */
    public Boolean enterInvoice(EnterInvoiceF form) {
        List<Long> billIds = form.getEntryInvoiceBillFList().stream().map(EntryInvoiceBillF::getBillId).collect(Collectors.toList());

        List<String> sendresultVS = iVoucherInferenceAppService.batchSingleInference(billIds,
            BillTypeEnum.valueOfByCode(form.getType()), ActionEventEnum.收票, form.getPriceTaxAmount(),
            true, form.getSupCpUnitId());
        if (CollectionUtils.isEmpty(sendresultVS)) {
            throw BizException.throw400("未匹配到对应规则");
        }
        String voucher = sendresultVS.get(0);
        EnterInvoiceCommand command = Global.mapperFacade.map(form, EnterInvoiceCommand.class);
        Boolean res = invoiceDomainService.enterInvoice(command, billIds, voucher);
        return res;
    }


    /**
     * 统一收票
     * @param invoiceCollectionF 收票
     * @return api
     */
    @Transactional
    public boolean collectInvoice(InvoiceCollectionF invoiceCollectionF) {
        ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(invoiceCollectionF.getStatutoryBodyId()) || StringUtils.isNotBlank(invoiceCollectionF.getStatutoryBodyCode()), ErrorMessage.PAYMENT_SB_NOT_NULL);
        OrgFinanceRv orgFinance = orgClient.getSbByCode(invoiceCollectionF.getStatutoryBodyCode());
        ErrorAssertUtil.notNullThrow403(orgFinance, ErrorMessage.STATUTORY_BODY_NO_EXISTS);
        //1.录入发票
        List<InvoiceA> invoiceAS = new ArrayList<>();
        List<InvoiceInfoF> invoiceInfos = invoiceCollectionF.getInvoiceInfos();
        for (InvoiceInfoF invoiceInfoF : invoiceInfos) {
            InvoiceReceiptE invoiceReceipt = new InvoiceReceiptE();
            invoiceReceipt.setInvoiceReceiptNo(invoiceInfoF.getInvoiceNo());
            invoiceReceipt.setType(invoiceInfoF.getType());
            invoiceReceipt.setApplyTime(invoiceInfoF.getApplyTime());
            invoiceReceipt.setBillingTime(invoiceInfoF.getBillingTime());
            invoiceReceipt.setPriceTaxAmount(invoiceInfoF.getPriceTaxAmount());
            invoiceReceipt.setState(InvoiceReceiptStateEnum.开票成功.getCode());
            invoiceReceipt.setSysSource(invoiceInfoF.getSysSource());
            invoiceReceipt.setInvSource(InvSourceEnum.收入的发票.getCode());
            invoiceReceipt.setClaimStatus(InvoiceClaimStatusEnum.未认领.getCode());
            invoiceReceipt.setCommunityId(invoiceInfoF.getInvRecUnitId());
            invoiceReceipt.setCommunityName(invoiceInfoF.getInvRecUnitName());
            invoiceReceipt.setStatutoryBodyId(orgFinance.getId());
            invoiceReceipt.setStatutoryBodyName(orgFinance.getNameCn());
            invoiceReceipt.setCostCenterId(invoiceInfoF.getCostCenterId());
            invoiceReceipt.setCostCenterName(invoiceInfoF.getCostCenterName());
            invoiceReceipt.setClerk(invoiceInfoF.getClerk());
            InvoiceE invoice = new InvoiceE();
            invoice.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
            invoice.setInvoiceType(InvoiceTypeEnum.蓝票.getCode());
            invoice.setInvoiceReceiptId(invoiceReceipt.getId());
            invoice.setInvoiceTitleType(invoiceInfoF.getInvoiceTitleType());
            invoice.setInvoiceCode(invoiceInfoF.getInvoiceCode());
            invoice.setInvoiceNo(invoiceInfoF.getInvoiceNo());
            invoice.setNuonuoUrl(invoiceInfoF.getOutInvoiceUrl());
            List<InvoiceReceiptDetailE> invoiceReceiptDetails = new ArrayList<>();
            for (InvoiceCollectionDetailF invoiceDetail : invoiceInfoF.getInvoiceDetails()) {
                InvoiceReceiptDetailE invoiceReceiptDetail = new InvoiceReceiptDetailE();
                invoiceReceiptDetail.setInvoiceReceiptId(invoiceReceipt.getId());
                invoiceReceiptDetail.setBillId(invoiceDetail.getBill().getBillId());
                invoiceReceiptDetail.setBillNo(invoiceDetail.getBill().getBillNo());
                invoiceReceiptDetail.setGoodsName(invoiceDetail.getGoodsName());
                invoiceReceiptDetail.setNum(invoiceDetail.getNum());
                invoiceReceiptDetail.setTaxRate(invoiceDetail.getTaxRate());
                invoiceReceiptDetail.setUnit(invoiceDetail.getUnit());
                invoiceReceiptDetail.setWithTaxFlag(invoiceDetail.getWithTaxFlag());
                invoiceReceiptDetail.setPrice(invoiceDetail.getPrice());
                invoiceReceiptDetail.setSpectype(invoiceDetail.getSpecType());
                invoiceReceiptDetail.setBillType(invoiceDetail.getBill().getBillType());
                invoiceReceiptDetail.setInvoiceAmount(invoiceDetail.getTaxIncludedAmount());
                invoiceReceiptDetail.setPriceTaxAmount(invoiceReceipt.getPriceTaxAmount());
                invoiceReceiptDetails.add(invoiceReceiptDetail);
            }
            invoiceAS.add(new InvoiceA(invoiceReceipt, invoice, invoiceReceiptDetails));
        }
        //收票
        invoiceDomainService.collectBatchInvoice(invoiceAS);
        return true;
    }

    /**
     * 根据账单信息推送发票
     * @param invoiceSendF
     * @return
     */
    public List<InvoiceSendDto> sendInvoice(InvoiceSendF invoiceSendF) {
        //校验参数
        List<Integer> pushMode = invoiceSendF.getPushModes();

        for (Integer mode : pushMode) {
            PushModeEnum modeEnum = PushModeEnum.valueOfByCode(mode);
            switch (modeEnum) {
                case 手机:
                    ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(invoiceSendF.getBuyerPhone()), ErrorMessage.INVOICE_PUSH_PHONE_ERROR);
                    break;
                case 邮箱:
                    ErrorAssertUtil.isTrueThrow403(StringUtils.isNotBlank(invoiceSendF.getEmail()), ErrorMessage.INVOICE_PUSH_EMAIL_ERROR);
                    break;
            }
        }
        return invoiceDomainService.sendInvoiceByBillId(Global.mapperFacade.map(invoiceSendF, SendInvoiceCommand.class));
    }

    /**
     * 重新处理更新发票状态
     * @param reHandleInvoiceF
     */
    public void reHandleInvoice(ReHandleInvoiceF reHandleInvoiceF) {
        InvoiceE invoiceE = invoiceDomainService.getByInvoiceSerialNum(reHandleInvoiceF.getInvoiceSerialNum());
        if (Objects.isNull(invoiceE)) {
            throw BizException.throw400("未查询到对应发票");
        }
        if(invoiceE.getInvoiceType() == InvoiceTypeEnum.红票.getCode()){
            InvoiceReceiptE invoiceReceiptE = invoiceDomainService.getInvoiceReceipt(invoiceE.getBlueInvoiceReceiptId());
            if (InvoiceReceiptStateEnum.已红冲.equalsByCode(invoiceReceiptE.getState())) {
                throw BizException.throw400("此发票已红冲");
            }
        }
        invoiceSchedule.reHandleInvoice(invoiceE);
    }


    /**
     * 诺诺开票回调
     * @param invoiceCallbackF
     */
    @Transactional(rollbackFor = Exception.class)
    public InvoiceCallbackResultV scan(InvoiceCallbackF invoiceCallbackF) {
        List<QueryInvoiceResultV> invoiceResultRVS = new ArrayList<>();
        log.info("诺诺开票回调传参： {}", JSON.toJSONString(invoiceCallbackF));
        if("callback".equals(invoiceCallbackF.getOperater())){
            String content = invoiceCallbackF.getContent();
            if(StringUtils.isNotBlank(content)){
                content = ApiData.API.urlDecode(content);
                ContentF contentF = JSON.parseObject(content, ContentF.class);
                QueryInvoiceResultV resultV = new QueryInvoiceResultV();
                resultV.setSerialNo(contentF.getC_fpqqlsh());// 发票请求流水号
                resultV.setOrderNo(contentF.getC_orderno());//订单编号
                if(InvoiceCallbackStateEnum.开票完成.getCode().toString().equals(contentF.getC_status())){
                    resultV.setStatus(InvoiceReceiptStateEnum.开票成功.getCode().toString());//发票状态
                }else if(InvoiceCallbackStateEnum.开票成功签章失败.getCode().toString().equals(contentF.getC_status())){
                    resultV.setStatus(InvoiceReceiptStateEnum.开票成功签章失败.getCode().toString());//发票状态
                }else {
                    resultV.setStatus(InvoiceReceiptStateEnum.开票失败.getCode().toString());//发票状态
                }
                resultV.setFailCause(contentF.getC_errorMessage());//失败原因
                resultV.setPdfUrl(contentF.getC_url());//发票pdf地址
                resultV.setPictureUrl(contentF.getC_jpg_url());//发票图片地址
                resultV.setInvoiceTime(contentF.getC_kprq());//开票时间
                resultV.setInvoiceCode(contentF.getC_fpdm());// 发票代码
                resultV.setInvoiceNo(contentF.getC_fphm());//发票号码
                resultV.setExTaxAmount(contentF.getC_bhsje());//不含税金额
                resultV.setTaxAmount(contentF.getC_hjse());//合计税额
                resultV.setPayerName(contentF.getBuyername());//购方名称
                resultV.setPayerTaxNo(contentF.getTaxnum());//购方税号
                resultV.setAddress(contentF.getAddress());//购方地址
                resultV.setTelephone(contentF.getTelephone());//购方电话
                resultV.setBankAccount(contentF.getBankAccount());//购方开户行及账号
                resultV.setInvoiceKind(contentF.getC_invoice_line());//发票种类
                resultV.setCheckCode(contentF.getCheckCode());//校验码
                resultV.setQrCode(contentF.getQrCode());//二维码
                resultV.setOfdUrl(contentF.getC_ofd_url());//发票ofd地址
                resultV.setClerk(contentF.getC_clerk());//开票员
                resultV.setPayee(contentF.getC_payee());//收款人
                resultV.setChecker(contentF.getC_checker());//复核人
                resultV.setSalerAccount(contentF.getC_salerAccount());//销方银行账号
                resultV.setSalerTel(contentF.getC_salerTel());//销方电话
                resultV.setSalerAddress(contentF.getC_salerAddress());//销方地址
                resultV.setSalerTaxNum(contentF.getC_saletaxnum());//销方税号
                resultV.setSaleName(contentF.getC_salerName());//销方名称
                resultV.setRemark(contentF.getC_remark());//备注
                resultV.setProxyInvoiceFlag(contentF.getProductOilFlag());//成品油标志
                resultV.setImgUrls(contentF.getC_imgUrls());//图片地址
                resultV.setExtensionNumber(contentF.getExtensionNumber());//分机号
                resultV.setTerminalNumber(contentF.getTerminalNumber());//终端号
                resultV.setOldInvoiceCode(contentF.getC_yfpdm());//对应蓝票发票代码
                resultV.setOldInvoiceNo(contentF.getC_yfphm());//对应蓝票发票号码
                resultV.setListFlag(contentF.getC_qdbz());//清单标志
                resultV.setListName(contentF.getC_qdxmmc());//清单项目名称
                resultV.setPhone(contentF.getPhone());//购方手机
                resultV.setVehicleFlag(contentF.getVehicleFlag());//是否机动车类专票
                resultV.setRedReason(contentF.getRedReason());//冲红原因
                resultV.setSpecificFactor(contentF.getSpecificFactor());//发票特定要素
                resultV.setBuyerManagerName(contentF.getBuyerManagerName());//购买方经办人姓名
                resultV.setManagerCardType(contentF.getManagerCardType());//经办人证件类型
                resultV.setManagerCardNo(contentF.getManagerCardNo());//经办人证件号码
                resultV.setCipherText(contentF.getCipherText());//发票密文
                resultV.setInvoiceItems(contentF.getInvoiceItems());//发票明细集合
                invoiceResultRVS.add(resultV);
                List<InvoiceE> invoiceES = invoiceRepository.listByInvoiceSerialNums(contentF.getC_fpqqlsh());
                if(!invoiceES.isEmpty()){
                    InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.queryById(invoiceES.get(0).getInvoiceReceiptId());
                    //补充身份标识
                    IdentityInfo identityInfo = new IdentityInfo();
                    identityInfo.setTenantId(invoiceES.get(0).getTenantId());
                    ThreadLocalUtil.set("IdentityInfo", identityInfo);
                    //根据开票结果更新数据库信息
                    updateInvoiceState(invoiceES.get(0), invoiceResultRVS, invoiceReceiptE);
                }
            }
        }
        return new InvoiceCallbackResultV("0000","同步成功");
    }


    public String getInvoiceRemarkInfo(InvoiceRemarkF form) {
        List<BillDetailMoreV> billDetailMoreVList = billFacade.getAllDetails(form.getBillIds(), BillTypeEnum.valueOfByCode(form.getBillType()), form.getSupCpUnitId());
        if (EnvConst.FANGYUAN.equals(EnvData.config) || EnvConst.LINGANG.equals(EnvData.config)) {
            return this.getRemarkStr(form, billDetailMoreVList);
        }
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA();
        return invoiceBlueA.handleRemark("", SysSourceEnum.收费系统.getCode(),  billDetailMoreVList, form.getType());
    }

    private String getRemarkStr(InvoiceRemarkF form, List<BillDetailMoreV> billDetailMoreVList) {
        SysSourceEnum sysSourceEnum = SysSourceEnum.valueOfByCode(form.getSysSource());
        String remark = "";
        switch (sysSourceEnum) {//电子发票备注中，只用保留项目名称、房号、账单周期，去除收费方式。
            case 合同系统:
                break;
            default:
                Map<String, List<BillDetailMoreV>> billDetailMoreVMap = billDetailMoreVList.stream().collect(Collectors.groupingBy(BillDetailMoreV::getRoomId));
                for (Map.Entry<String, List<BillDetailMoreV>> entry : billDetailMoreVMap.entrySet()) {
                    List<BillDetailMoreV> value = entry.getValue();
                    remark = handeRemark(remark,value);
                }
        }
        if (remark.length() >= 100) {
            remark = remark.substring(0, 97) + "...";
        }
        return remark;
    }

    /**
     * 根据收款明细id从账单中获取备注信息
     * @param form
     * @return
     */
    public String getGatherInvoiceRemarkInfo(InvoiceRemarkF form) {
        LambdaQueryWrapper<GatherDetail> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(form.getGatherBillId())){
            List<GatherDetail> gatherDetails = gatherDetailRepository.getByGatherBillIds(
                    Lists.newArrayList(form.getGatherBillId()), form.getSupCpUnitId());
            Assert.validate(()->CollectionUtils.isNotEmpty(gatherDetails),()->BizException.throw400("该收款单明细缺失"));
            form.setGatherDetailIds(gatherDetails.stream().distinct().map(GatherDetail::getId).collect(
                    Collectors.toList()));
        }
        queryWrapper.in(GatherDetail::getId, form.getGatherDetailIds());
        queryWrapper.eq(GatherDetail::getSupCpUnitId, form.getSupCpUnitId());
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(queryWrapper);
        Map<Integer, List<GatherDetail>> typeGatherDetails = gatherDetails.stream().collect(Collectors.groupingBy(GatherDetail::getGatherType));
        List<BillDetailMoreV> billDetailMoreVList = new ArrayList<>();
        typeGatherDetails.forEach((key, value) -> {
            GatherTypeEnum gatherTypeEnum = GatherTypeEnum.valueOfByCode(key);
            List<Long> billIds = value.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
            form.setBillIds(billIds);
            switch (gatherTypeEnum) {
                case 应收:
                    form.setBillType(BillTypeEnum.应收账单.getCode());
                    List<BillDetailMoreV> receivableDetails = billFacade.getAllDetails(form.getBillIds(), BillTypeEnum.valueOfByCode(form.getBillType()), form.getSupCpUnitId());
                    billDetailMoreVList.addAll(receivableDetails);
                    break;
                case 预收:
                    form.setBillType(BillTypeEnum.预收账单.getCode());
                    List<BillDetailMoreV> advanceDetails = billFacade.getAllDetails(form.getBillIds(), BillTypeEnum.valueOfByCode(form.getBillType()), form.getSupCpUnitId());
                    billDetailMoreVList.addAll(advanceDetails);
                    break;
            }
        });
        if (EnvConst.FANGYUAN.equals(EnvData.config) || EnvConst.LINGANG.equals(EnvData.config)) {
            return this.getRemarkStr(form, billDetailMoreVList);
        }
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA();
        return invoiceBlueA.handleRemark(null, SysSourceEnum.收费系统.getCode(),  billDetailMoreVList, form.getType());
    }

    private String handeRemark(String remark, List<BillDetailMoreV> billDetailMoreVList) {
        List<String> communityNames = Lists.newArrayList();
        List<String> roomNames = Lists.newArrayList();
        List<String> chargeItemName = Lists.newArrayList();
        List<BillRemarkDTO> startEndTimeDateTime = Lists.newArrayList();
        for (BillDetailMoreV detailMoreV : billDetailMoreVList) {
            //收费系统的开票备注为“项目名称”、“房号”、费项名称 “费用期间”.
            // 计算每个费项的计费周期 合并

            //计费周期分组去重体现
            BillRemarkDTO remarkDTO = new BillRemarkDTO();
            if (Objects.nonNull(detailMoreV.getStartTime()) && Objects.nonNull(detailMoreV.getEndTime())) {
                remarkDTO.setStartTime(detailMoreV.getStartTime());
                remarkDTO.setEndTime(detailMoreV.getEndTime());
                remarkDTO.setChargeItemName(detailMoreV.getChargeItemName());
                remarkDTO.setChargeTime(detailMoreV.getChargeTime());
                remarkDTO.setPayTime(detailMoreV.getPayTime());
                remarkDTO.setBillMethod(detailMoreV.getBillMethod());
                startEndTimeDateTime.add(remarkDTO);
            }

            communityNames.add(detailMoreV.getCommunityName());
            roomNames.add(detailMoreV.getRoomName());
            chargeItemName.add(detailMoreV.getChargeItemName());
        }

        // startEndTimeDateTime list按照 费项分组
        // 定义变量，备注信息 费项名称+周期 拼接在最后
        StringBuilder chargeRemark = new StringBuilder();
        Map<String, List<BillRemarkDTO>> collect = startEndTimeDateTime.stream().collect(Collectors.groupingBy(BillRemarkDTO::getChargeItemName));
        for (Map.Entry<String, List<BillRemarkDTO>> entry : collect.entrySet()) {
            List<BillRemarkDTO> value = entry.getValue();
            List<BillRemarkDTO> billRemarkDTOS = handleAndMergeStartEndTimeDate(value);
            if (CollectionUtils.isNotEmpty(billRemarkDTOS)) {
                chargeRemark.append("费用期间：")
                        .append(getBillingCycleRemark(billRemarkDTOS))
                        .append("(")
                        .append(entry.getKey())
                        .append(");");
            }
        }
        if (CollectionUtils.isNotEmpty(communityNames)) {
            communityNames = communityNames.stream().distinct().collect(Collectors.toList());
            String communityNameRemark = StringUtils.join(communityNames, ",");
            remark = remark  + communityNameRemark + ";";
        }
        if (CollectionUtils.isNotEmpty(roomNames)) {
            roomNames = roomNames.stream().distinct().collect(Collectors.toList());
            String roomNameStr = StringUtils.join(roomNames, ",");
            remark = remark  + roomNameStr + ";";
        }
        if (StringUtils.isNotEmpty(chargeRemark)) {
            remark = remark  + chargeRemark;
        }

        return remark;
    }

    /**
     * 处理计费周期,合并相邻的计费周期
     * 若周期中有部分缴费，该周期不合并
     * @param list
     * @return
     */
    private List<BillRemarkDTO> handleAndMergeStartEndTimeDate(List<BillRemarkDTO> list) {
        list.sort((o1, o2) -> {
            if (o1.getStartTime().isAfter(o2.getEndTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getEndTime())) {
                return -1;
            }
            return 0;
        });
        Iterator<BillRemarkDTO> iterator = list.iterator();
        BillRemarkDTO timeDTO = iterator.next();
        // a1
        while (iterator.hasNext()) {
            BillRemarkDTO nextTimeDTO = iterator.next();
            LocalDateTime firstStartTime = timeDTO.getStartTime();
            LocalDateTime firstEndTime = timeDTO.getEndTime();
            LocalDateTime firstChargeTime = timeDTO.getChargeTime();
            // 包含时间计费方式时才进行合并
            if (timeDTO.hasTimeBilling()) {
                if (firstChargeTime != null && DateTimeUtil.daysBetween(firstChargeTime, firstEndTime) > 0) {
                    // 账单已缴时间小于账单结束时间的，不进行合并，单独展示
                    timeDTO = nextTimeDTO;
                    continue;
                }
            }
            LocalDateTime secondStartTime = nextTimeDTO.getStartTime();
            LocalDateTime secondEndTime = nextTimeDTO.getEndTime();
            LocalDateTime secondChargeTime = nextTimeDTO.getChargeTime();
            // 包含时间计费方式时才进行合并
            if (timeDTO.hasTimeBilling()) {
                if (secondChargeTime != null && DateTimeUtil.daysBetween(secondChargeTime, secondEndTime) > 0) {
                    // 账单已缴时间小于账单结束时间的，不进行合并，单独展示
                    timeDTO = nextTimeDTO;
                    continue;
                }
            }
            // 判断是否需要合并
            if (DateTimeUtil.daysBetween(firstEndTime, secondStartTime) <= 1) {
                if (DateTimeUtil.daysBetween(firstEndTime, secondEndTime) >= 0) {
                    if(DateTimeUtil.daysBetween(firstStartTime,secondStartTime) <= 1){
                        timeDTO.setStartTime(secondStartTime);
                    }
                    timeDTO.setEndTime(secondEndTime);
                    timeDTO.setChargeTime(nextTimeDTO.getChargeTime());
                    iterator.remove();
                }
            }
        }
        return list;
    }

    private String getBillingCycleRemark(List<BillRemarkDTO> billRemarkDTOS) {
        StringBuilder sb = new StringBuilder();
        for (BillRemarkDTO timeDTO : billRemarkDTOS) {
            sb.append(timeDTO.getStartTime().toLocalDate()).append("-").append(timeDTO.getEndTime().toLocalDate());
            // 计费方式包含时间时
            if (timeDTO.hasTimeBilling()) {
                // 缴费时间在账单结束时间之前时
                if (timeDTO.getChargeTime() != null && DateTimeUtil.daysBetween(timeDTO.getChargeTime(), timeDTO.getEndTime()) > 1) {
                    sb.append(",缴至").append(timeDTO.getChargeTime().toLocalDate());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 根据开票结果更新账单状态  超过限额
     *
     * @param invoiceE
     * @param invoiceRes
     * @param invoiceReceiptE
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateInvoiceState(InvoiceE invoiceE, List<QueryInvoiceResultV> invoiceRes, InvoiceReceiptE invoiceReceiptE) {
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.INVOICE_CALLBACK_LOCK, JSON.toJSONString(invoiceE.getInvoiceReceiptId())))) {
            InvoiceReceiptE receiptE = invoiceReceiptRepository.queryById(invoiceE.getInvoiceReceiptId());
            if (Objects.equals(InvoiceReceiptStateEnum.开票成功.getCode(), receiptE.getState())
                    || InvoiceReceiptStateEnum.已红冲.getCode() == receiptE.getState()) {
                throw BizException.throw400("此发票已开票成功，不允许重复开票");
            }
            //判断开票状态
            Integer invoiceState = NuonuoInvoiceStatusEnum.valueOfByCode(Integer.valueOf(invoiceRes.get(0).getStatus())).getInvoicingState();
            if (invoiceState == InvoiceReceiptStateEnum.开票成功.getCode() || invoiceState == InvoiceReceiptStateEnum.开票失败.getCode()) {
                //处理票的状态
                if (invoiceE.getInvoiceType() == InvoiceTypeEnum.蓝票.getCode()) {
                    invoiceSchedule.handleBlueInvoice(invoiceE, invoiceReceiptE, invoiceRes, invoiceState);
                } else if (invoiceE.getInvoiceType() == InvoiceTypeEnum.红票.getCode()) {
                    invoiceSchedule.handleRedInvoice(invoiceE, invoiceReceiptE, invoiceRes, invoiceState);
                }
                //通知财务中心
                List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = invoiceReceiptDetailRepository.getBillIdsByInvoiceReceiptId(invoiceE.getInvoiceReceiptId());
                invoiceSchedule.handleResToAmpFinance(invoiceReceiptE, invoiceReceiptDetailEList, invoiceState);
                //推送开票信息
                invoiceSchedule.pushModeMessage(invoiceState, invoiceE, invoiceReceiptE, invoiceReceiptDetailEList, invoiceRes);
                //异步推送开票信息
                invoiceSchedule.asynPushCallBack(invoiceReceiptE, invoiceE, invoiceReceiptDetailEList);
                asyncPushRedInvoice(invoiceE);
            }
        }
    }

    public void asyncPushRedInvoice(InvoiceE invoiceE) {
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
            if (EnvConst.NIANHUAWAN.equals(EnvData.config)
                    && InvoiceTypeEnum.红票.equalsByCode(invoiceE.getInvoiceType())) {
                InvoiceReceiptE blueInvoiceReceipt = invoiceReceiptRepository.getById(
                        invoiceE.getBlueInvoiceReceiptId());
                if (KingDeePushStateEnum.已推送.equalsByCode(blueInvoiceReceipt.getExtendFieldTwo())) {
                    InvoiceExpenseReportBatchF invoiceExpenseReportBatchF = new InvoiceExpenseReportBatchF();
                    invoiceExpenseReportBatchF.setInvoiceReceiptIds(List.of(invoiceE.getInvoiceReceiptId()));
                    expenseReportBatch(invoiceExpenseReportBatchF);
                }
            }
            }
        });

    }

    /**
     * 补录发票认领分页查询
     * @param form
     * @return
     */
    public PageV<InvoiceClaimPageDto> claimPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        List<Field> fields = conditions.getFields();
        Iterator<Field> iterator = fields.iterator();
        Integer billType = 0;
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (StringUtils.equals(field.getName(), "b.bill_type")) {
                billType = (Integer) field.getValue();
                iterator.remove();
            }
            if (StringUtils.equals(field.getName(), "b.start_time")) {
                field.setValue(field.getValue() + " 00:00:00");
            }
            if (StringUtils.equals(field.getName(), "b.end_time")) {
                field.setValue(field.getValue() + " 23:59:59");
            }

        }
        if (BillTypeEnum.收款单.equalsByCode(billType)) {
            fields.stream().filter(field -> StringUtils.equals(field.getName(), "settleAmount")).forEach(field -> {
                field.setName("(b.total_amount - b.refund_amount - b.carried_amount)");
                String amount = String.valueOf(field.getValue());
                field.setValue(AmountUtils.toLong(amount));
            });
            IPage<UnInvoiceGatherBillDto> unInvoiceGatherBillDtoIPage = gatherBillDomainService.unInvoiceGatherBillPage(form);
            return PageV.of(unInvoiceGatherBillDtoIPage.getCurrent(), unInvoiceGatherBillDtoIPage.getSize(),
                    unInvoiceGatherBillDtoIPage.getTotal(), Global.mapperFacade.mapAsList(unInvoiceGatherBillDtoIPage.getRecords(), InvoiceClaimPageDto.class));
        } else if (BillTypeEnum.应收账单.equalsByCode(billType)) {
            fields.stream().filter(field -> StringUtils.equals(field.getName(), "settleAmount")).forEach(field -> {
                field.setName("(b.receivable_amount - b.discount_amount)");
                String amount = String.valueOf(field.getValue());
                field.setValue(AmountUtils.toLong(amount));
            });
            IPage<UnInvoiceReceivableBillDto> unInvoiceGatherBillDtoIPage = receivableBillDomainService.unInvoiceBillPage(form);
            return PageV.of(unInvoiceGatherBillDtoIPage.getCurrent(), unInvoiceGatherBillDtoIPage.getSize(),
                    unInvoiceGatherBillDtoIPage.getTotal(), Global.mapperFacade.mapAsList(unInvoiceGatherBillDtoIPage.getRecords(), InvoiceClaimPageDto.class));
        } else {
            throw BizException.throw300("不支持的账单类型");
        }
    }

    /**
     * 补录发票认领
     * @param claimInvoiceF
     * @return
     */
    public Boolean claimInvoice(ClaimInvoiceF claimInvoiceF) {
        Long invoiceReceiptId = claimInvoiceF.getInvoiceReceiptId();
        InvoiceBlueA invoiceA = invoiceDomainService.getByInvoiceReceiptId(invoiceReceiptId);
        List<InvoiceReceiptDetailE> invoiceReceiptDetails;
        List<BillDetailMoreV> billDetailMoreVList;
        AddInvoiceCommand command = new AddInvoiceCommand();
        command.setSupCpUnitId(claimInvoiceF.getCommunityId());
        if (BillTypeEnum.收款单.equalsByCode(claimInvoiceF.getBillType())) {
            command.setGatherBillIds(claimInvoiceF.getBillIds());
            command.setBillType(BillTypeEnum.收款单.getCode());
            command.setBillIds(claimInvoiceF.getBillIds());
            command.setGatherBillType(0);
            billDetailMoreVList = this.getBillDetailMoreVList(command);
            invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetail(command, invoiceReceiptId, billDetailMoreVList);
        } else if (BillTypeEnum.应收账单.equalsByCode(claimInvoiceF.getBillType())) {
            command.setBillType(BillTypeEnum.应收账单.getCode());
            command.setBillIds(claimInvoiceF.getBillIds());
            billDetailMoreVList = this.getBillDetailMoreVList(command);
            invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetail(command, invoiceReceiptId, billDetailMoreVList);
        } else {
            throw BizException.throw300("不支持收款单和应收单外的账单认领发票");
        }
        log.info("发票认领-获取收款单数据：{}",JSONObject.toJSONString(invoiceReceiptDetails));
        invoiceA.setInvoiceReceiptDetailEList(invoiceReceiptDetails);
        amountCheck(invoiceA);

        log.info("发票认领-更新发票部分字段，billDetailMoreVList：{}",JSONObject.toJSONString(billDetailMoreVList));
        invoiceA.reRecord(billDetailMoreVList);

        String buyerName = claimInvoiceF.getBuyerName();
        InvoiceReceiptE invoiceReceiptE = invoiceA.getInvoiceReceiptE();
        invoiceReceiptE.setSysSource(SysSourceEnum.收费系统.getCode());
        invoiceReceiptE.setCustomerName(buyerName);
        // D6937 定额发票绑定时 未开票并且没有发票时间的数据需要更新字段
        if (invoiceReceiptE.getType() != null && invoiceReceiptE.getType().equals(InvoiceLineEnum.定额发票.getCode())) {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票成功.getCode());
            if (invoiceReceiptE.getBillingTime() == null) {
                invoiceReceiptE.setBillingTime(LocalDateTime.now());
            }
            if (StringUtils.isBlank(invoiceReceiptE.getClerk())) {
                invoiceReceiptE.setClerk(ApiData.API.getUserName().orElse("系统默认"));
            }
            // 定额发票没走开票 走认领 需要特殊处理
            if (StringUtils.isNotBlank(buyerName)) {
                InvoiceE invoiceE = invoiceA.getInvoiceE();
                invoiceRepository.update(Wrappers.<InvoiceE>lambdaUpdate().eq(InvoiceE::getId, invoiceE.getId()).set(InvoiceE::getBuyerName, buyerName));
            }

        }

        invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetails);
        invoiceReceiptRepository.updateById(invoiceReceiptE);

        billFacade.handleBillStateFinishInvoice(invoiceReceiptDetails, true, invoiceReceiptE.getCommunityId());

        invoiceDomainService.invoiceBatchLog(invoiceReceiptDetails, invoiceReceiptE);
        return true;
    }


    /**
     * 定额发票认领
     *
     * @param claimInvoiceF
     * @param command
     * @param item
     * @return
     */
    public Boolean claimQuotaInvoice(ClaimInvoiceF claimInvoiceF, AddInvoiceCommand command, List<BillDetailMoreV> billDetailMoreVList, MatchedInvoiceAndGatherDetail item) {
        Long invoiceReceiptId = item.getInvoiceReceiptId();
        InvoiceBlueA invoiceA = invoiceDomainService.getByInvoiceReceiptId(invoiceReceiptId);
        List<InvoiceReceiptDetailE> invoiceReceiptDetails;
        invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetail(command, invoiceReceiptId, billDetailMoreVList);

        log.info("定额发票认领-获取收款单数据：{}",JSONObject.toJSONString(invoiceReceiptDetails));
        invoiceA.setInvoiceReceiptDetailEList(invoiceReceiptDetails);
//        amountCheck(invoiceA);

        log.info("定额发票认领-更新发票部分字段，billDetailMoreVList：{}",JSONObject.toJSONString(billDetailMoreVList));
        invoiceA.reRecord(billDetailMoreVList);

        String buyerName = claimInvoiceF.getBuyerName();
        InvoiceReceiptE invoiceReceiptE = invoiceA.getInvoiceReceiptE();
        invoiceReceiptE.setSysSource(SysSourceEnum.收费系统.getCode());
        invoiceReceiptE.setCustomerName(buyerName);
        // D6937 定额发票绑定时 未开票并且没有发票时间的数据需要更新字段
        if (invoiceReceiptE.getType() != null && invoiceReceiptE.getType().equals(InvoiceLineEnum.定额发票.getCode())) {
            invoiceReceiptE.setState(InvoiceReceiptStateEnum.开票成功.getCode());
            if (invoiceReceiptE.getBillingTime() == null) {
                invoiceReceiptE.setBillingTime(LocalDateTime.now());
            }
            if (StringUtils.isBlank(invoiceReceiptE.getClerk())) {
                invoiceReceiptE.setClerk(ApiData.API.getUserName().orElse("系统默认"));
            }
            // 定额发票没走开票 走认领 需要特殊处理
            if (StringUtils.isNotBlank(buyerName)) {
                InvoiceE invoiceE = invoiceA.getInvoiceE();
                invoiceRepository.update(Wrappers.<InvoiceE>lambdaUpdate().eq(InvoiceE::getId, invoiceE.getId()).set(InvoiceE::getBuyerName, buyerName));
            }

        }

        invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetails);
        invoiceReceiptRepository.updateById(invoiceReceiptE);

        billFacade.handleBillStateFinishInvoice(invoiceReceiptDetails, true, invoiceReceiptE.getCommunityId());

        invoiceDomainService.invoiceBatchLog(invoiceReceiptDetails, invoiceReceiptE);
        return true;
    }

    /**
     * 认领发票金额校验
     * @param invoiceA
     */
    public void amountCheck(InvoiceBlueA invoiceA) {
        InvoiceE invoiceE = invoiceA.getInvoiceE();
        InvoiceReceiptE invoiceReceiptE = invoiceA.getInvoiceReceiptE();
        List<InvoiceReceiptDetailE> invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetailEList();
        Long priceTaxAmount = invoiceReceiptE.getPriceTaxAmount();
        Long totalInvoiceAmount = invoiceReceiptDetails.stream().map(InvoiceReceiptDetailE::getInvoiceAmount).reduce(Long::sum).get();
        if (priceTaxAmount.compareTo(totalInvoiceAmount) != 0) {
            throw BizException.throw300("选中单据金额总和为" + AmountUtils.toStringAmount(totalInvoiceAmount) + ",与发票总金额" + AmountUtils.toStringAmount(priceTaxAmount) + "不等");
        }
//        Long taxAmount = invoiceE.getTaxAmount();
//        Long totalTaxAmount = invoiceReceiptDetails.stream()
//                .map(detail -> InvoiceUtil.getLongTaxAmount(detail.getTaxRate(), detail.getPriceTaxAmount()))
//                .reduce(Long::sum).get();
//        if (taxAmount.compareTo(totalTaxAmount) != 0) {
//            throw BizException.throw300("选中单据税额总和为" + AmountUtils.toStringAmount(totalTaxAmount) + ",与发票税额" + AmountUtils.toStringAmount(taxAmount) + "不等");
//        }
    }

    /**
     * 删除发票
     * @param invoiceReceiptId
     * @return
     */
    public Boolean deleteInvoice(Long invoiceReceiptId) {
        return invoiceDomainService.deleteInvoice(invoiceReceiptId);
    }


    /***
     * 纸质发票打印，获取打印信息
     * @param form
     * @return
     */
    public String getInvoicePrintF(InvoicePrintF form) {
        return  invoiceDomainService.getInvoicePrintF(form);
    }
    /**
     * 创建异步日志任务
     * @param name
     * @param totalNum
     * @return
     */
    private String createMultiTask(String name ,String code,Integer totalNum){
        //1、构建参数，创建任务
        MultiTaskF multiTaskF = new MultiTaskF();
        multiTaskF.setName(name);
        multiTaskF.setTaskCode(code);
        multiTaskF.setTotalNum(totalNum);
        return multiTaskService.saveMultiTask(multiTaskF);
    }

    public InvoicePreviewV getInvoicePreviewV(InvoicePreviewF invoicePreviewF) {
        prepayPaymentService.checkPrepay(invoicePreviewF.getBillIds(),invoicePreviewF.getSupCpUnitId());
        AddInvoiceCommand command = Global.mapperFacade.map(invoicePreviewF, AddInvoiceCommand.class);
        //获取收款单明细(额外收集账单ids)
        List<BillDetailMoreV> billDetailMoreVList = this.getBillDetailMoreVList(command);
        log.info("getInvoicePreviewV billDetailMoreVList:{}",JSONObject.toJSONString(billDetailMoreVList));
        //设置开票人
        if (StringUtils.isBlank(invoicePreviewF.getClerk())) {
            getUserName().ifPresentOrElse(command::setClerk, () -> command.setClerk(invoicePreviewF.getClerk()));
        }
        /*检查开票金额*/
        command.checkInvoice(this.getCanInvoiceAmount(billDetailMoreVList));
        /*校检开票中,已经开票 相同法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)*/
        billFacade.checkBillDetail(billDetailMoreVList,command);
        return  invoiceDomainService.getInvoicePreviewV(command, billDetailMoreVList);
    }

    /** 获取当前项目下以开票金额分组的定额发票列表
     * @return
     */
    public List<InvoiceGroupByAmountV> quotaListGroupByAmount(String communityId) {
        //根据收据发票ids 获取对应的收据发票、收据信息
        List<InvoiceDto> invoiceDtoList = invoiceDomainService.queryQuotaListByCommunityId(communityId);
        // 根据金额分组
        Map<Long, List<InvoiceDto>> invoiceReceiptGroupByAmount = invoiceDtoList.stream().collect(Collectors.groupingBy(InvoiceDto::getPriceTaxAmount,TreeMap::new,Collectors.toList()));

        List<InvoiceGroupByAmountV>result=new ArrayList<>();
        for (Map.Entry<Long, List<InvoiceDto>> entry : invoiceReceiptGroupByAmount.entrySet()) {
            List<InvoiceDto> value = entry.getValue();
            InvoiceGroupByAmountV amountV = new InvoiceGroupByAmountV();

            List<InvoiceAndReceiptV> receiptVS = Global.mapperFacade.mapAsList(value, InvoiceAndReceiptV.class);
            // 处理金额
            receiptVS.stream()
                    .filter(receiptV -> "元".equals(receiptV.getAmountUnit()))
                    .forEach(receiptV -> {
                        receiptV.setAmountUnit("分");
                        BigDecimal priceTaxAmount = new BigDecimal(receiptV.getPriceTaxAmount());
                        BigDecimal amount = priceTaxAmount.divide(BigDecimal.valueOf(100));
                        amountV.setKey(amount.toString());
                    });
            amountV.setValue(receiptVS);
            result.add(amountV);
        }
        return result;
    }

    /**
     * 定额发票绑定账单 并进行缴费和认领
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void quotaBindBill(QuotaInvoiceBindF form) {
        // 1. 校验发票总额是否可以和账单总额对上 转为分
        Long totalInvoiceAmount = convertAmountMultiply100(form.getTotalInvoiceAmount());
        Long totalAmount = convertAmountMultiply100(form.getTotalAmount());

        Long billId = form.getBillId();
        String communityId = form.getSupCpUnitId();
        List<QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF> idAndAmount = form.getInvoiceReceiptIdAndAmount();

        try {
            // 1. 验证开票总额和账单总额
            if (totalAmount.compareTo(totalInvoiceAmount) != 0) {
                throw BizException.throw400("账单金额与发票金额不匹配");
            }
            // 2. 校验当前账单是否是未缴费的账单
            List<BillOjv> billInfo = billFacade.getBillInfo(Collections.singletonList(billId), form.getBillType(), form.getSupCpUnitId());
            log.info("定额发票开票-获取账单信息：{}", JSONObject.toJSONString(billInfo));
            if (CollectionUtils.isEmpty(billInfo)) {
                throw BizException.throw300("无法获取账单信息");
            }
            BillOjv billOjv = billInfo.get(0);
            if (!billOjv.getSettleState().equals(BillSettleStateEnum.未结算.getCode())) {
                throw BizException.throw400("该批次存在已结算的账单");
            }
            // 3. 对发票进行循环对应账单
            for (QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF idAndAmountF : idAndAmount) {
                String invoiceReceiptId = idAndAmountF.getInvoiceId();
                Long amount = convertAmountMultiply100(idAndAmountF.getAmount());
                log.info("定额发票绑定账单，账单id：{}，开票总金额：{}，当前开票金额：{}，账单金额：{}，当前发票号：{}",
                        billId, totalInvoiceAmount, amount, totalAmount, invoiceReceiptId);

                // 3.1 对账单进行缴费
                List<AddBillSettleF> billSettleForm = new ArrayList<>();
                AddBillSettleF settleF = new AddBillSettleF();
                Global.mapperFacade.map(billOjv, settleF);
                settleF.setPayAmount(amount);
                settleF.setSettleAmount(amount);
                settleF.setCarriedAmount(0l);
                settleF.setDiscountAmount(0l);
                settleF.setSettleChannel(form.getPayChannel());
                settleF.setBillId(billId);
                settleF.setSupCpUnitId(communityId);
                settleF.setSettleWay(SettleWayChannelEnum.valueOfByCode(settleF.getSettleChannel()).getType());
                billSettleForm.add(settleF);
                log.info("定额发票绑定账单，对账单进行缴费，构建缴费请求对象：{}", JSONObject.toJSONString(billSettleForm));

                Long gatherBillId = receivableBillAppService.settleBatch(billSettleForm);
                log.info("定额发票绑定账单，对账单进行缴费，缴费完毕，收款单id：{}", gatherBillId);
                if (gatherBillId == null || gatherBillId < 0) {
                    throw BizException.throw300("账单缴费异常");
                }
                // 3.2 认领
                ClaimInvoiceF claimInvoiceF = new ClaimInvoiceF();
                claimInvoiceF.setCommunityId(communityId);
                claimInvoiceF.setBillIds(Collections.singletonList(gatherBillId));
                claimInvoiceF.setInvoiceReceiptId(Long.valueOf(invoiceReceiptId));
                claimInvoiceF.setBillType(BillTypeEnum.收款单.getCode());
                claimInvoiceF.setInvoiceType(InvoiceLineEnum.定额发票.getCode());
                claimInvoiceF.setBuyerName(form.getBuyerName());
                log.info("定额发票绑定账单，发票认领：{}", JSONObject.toJSONString(claimInvoiceF));
                Boolean claimed = claimInvoice(claimInvoiceF);
                log.info("定额发票绑定账单，发票认领结果：{}", claimed);
            }
        } catch (Exception e) {
            throw BizException.throw400("定额发票开票失败，失败原因："+e);
        }
    }

    /**
     * 获取金额乘100 将金额转为分
     *
     * @param amount 金额
     * @return
     */
    private static long convertAmountMultiply100(Double amount) {
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(100)).longValue();
    }


    public BuildingServiceInfoF buildingServiceInfo(InvoiceBatchF form) {
        BuildingServiceInfoF vo = new BuildingServiceInfoF();
        // 建筑服务类相关信息
        InvoiceBuildingServiceInfoF info = invoiceDomainService.buildingServiceInfo(form);
        vo.setServiceInfoF(info);
        if (ObjectUtil.isNotNull(info)){
            List<InvoiceZoningE> tree = invoiceZoningRepository.getTree();
            vo.setTree(tree);
        }
        return vo;
    }

    public void batchDownloadZip(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        invoiceDomainService.batchDownloadZip(queryF,response);
    }

    public List<ValidReverseGatherBillRV> getValidInvoiceIdsByGatherBillIds(List<Long> gatherBillIds) {
        List<ValidReverseGatherBillRV> reverseGatherBillRVS=new ArrayList<>();
        for (Long gatherBillId : gatherBillIds) {
            ValidReverseGatherBillRV rv = new ValidReverseGatherBillRV();
            rv.setGatherBillId(gatherBillId);
            rv.setInvoiceIds(invoiceReceiptDetailRepository.getValidInvoiceIdsByGatherBillIds(gatherBillId));
            reverseGatherBillRVS.add(rv);
        }

        return reverseGatherBillRVS;
    }

    public List<Long> getValidInvoiceIdsByBillId(Long billId) {
        return invoiceReceiptDetailRepository.getValidInvoiceIdsByBillId(billId);
    }

    /**
     * 发票文件下载
     * @param invoiceReceiptNo 发票编号
     * @param response
     */
    public void getDownloadInvoice(String invoiceReceiptNo, HttpServletResponse response) {
        // 获取发票url
        InvoiceE invoiceE = invoiceRepository.getByInvoiceNo(invoiceReceiptNo);
        Assert.validate(()->Objects.nonNull(invoiceE),()->BizException.throw400("发票信息不存在"));
        Assert.validate(()-> ObjectUtils.anyNotNull(invoiceE.getNuonuoUrl(),invoiceE.getInvoiceUrl()),
                ()->BizException.throw400("发票文件路径不存在"));

        String fileName = "invoice_" + UUID.randomUUID() + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        Boolean result = Boolean.TRUE;
        try {
            result = FileUtil.downLoadPdf(response, invoiceE.getNuonuoUrl());
        }catch (Exception e){
            FileUtil.downLoadPdf(response, fileHost + Optional.ofNullable(invoiceE.getInvoiceUrl()).orElse(""));
        }
        if (!result){
            FileUtil.downLoadPdf(response, fileHost + Optional.ofNullable(invoiceE.getInvoiceUrl()).orElse(""));
        }
    }





    public Long quotaInvoiceBatch(AddInvoiceCommand command, List<BillDetailMoreV> billDetailMoreVS) {
        // 定额发票
        if (!InvoiceLineEnum.定额发票.getCode().equals(command.getType())){
            throw BizException.throw400("接口调用错误");
        }
        if (CollectionUtils.isEmpty(command.getInvoiceReceiptIdAndAmount())){
            throw BizException.throw400("发票收据主表ids和对应开票金额不能为空");
        }
        if (CollectionUtils.isEmpty(command.getGatherDetailBillIds())){
            throw BizException.throw400("收款单明细id不能为空");
        }
        if (CollectionUtils.isEmpty(command.getInvoiceGatherDetailAmounts())){
            throw BizException.throw400("指定收款明细开票金额不能为空");
        }
        if (Objects.isNull(command.getTotalInvoiceAmount())){
            throw BizException.throw400("发票总额不能为空");
        }
        // 预收账单不支持开具定额发票
        if (command.getBillType() != null && command.getBillType() == 2) {
            throw BizException.throw400("预收账单不支持开具定额发票");
        }

        command.setGatherBillType(1);

        // 1. 校验发票总额是否可以和账单总额对上 转为分
        Long totalInvoiceAmount = convertAmountMultiply100(command.getTotalInvoiceAmount());
        Long totalAmount = convertAmountMultiply100(command.getTotalAmount());
        // 2. 校验发票总额是否可以和收款单总额对上 转为分
        log.info("定额发票开票-获取账单信息：{}", JSONObject.toJSONString(billDetailMoreVS));
        String communityId = command.getSupCpUnitId();
        List<QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF> idAndAmount = command.getInvoiceReceiptIdAndAmount();
        List<InvoiceGatherDetailAmount> gatherDetailAmounts = command.getInvoiceGatherDetailAmounts();

        try {
            // 1. 验证开票总额和账单总额以及收款单总额
            if (totalAmount.compareTo(totalInvoiceAmount) != 0) {
                throw BizException.throw400("账单金额与发票金额不匹配");
            }

            // 2. 对发票进行循环对应账单
            // 发票按金额从大到小排序
            idAndAmount = idAndAmount.stream()
                    .sorted(Comparator.comparingDouble(QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF::getAmount).reversed())
                    .collect(Collectors.toList());
            // 收款单明细按金额从大到小排序
            gatherDetailAmounts = gatherDetailAmounts.stream()
                    .sorted(Comparator.comparingDouble(InvoiceGatherDetailAmount::getInvoiceAmount).reversed())
                    .collect(Collectors.toList());
            // 循环匹配开票金额
            List<MatchedInvoiceAndGatherDetail> matchedItems = matchInvoicesAndGatherDetails(idAndAmount, gatherDetailAmounts);

            // 循环进行认领
            for (MatchedInvoiceAndGatherDetail item : matchedItems) {

                // 设置账单的可开票金额为分配出来的金额
                List<BillDetailMoreV> detailMoreVS = new ArrayList<>();
                billDetailMoreVS.stream().filter(t -> t.getGatherDetailId().equals(item.getGatherDetailId())).findFirst()
                        .ifPresent(t -> {
                            t.setCanInvoiceAmount(item.getMatchedAmount());
                            detailMoreVS.add(t);
                        });

                String invoiceReceiptId = String.valueOf(item.getInvoiceReceiptId());
                Long amount = item.getMatchedAmount();
                log.info("收款单批量开具定额发票：{}，开票总金额：{}，当前开票金额：{}，账单金额：{}，当前发票号：{}",
                        item.getGatherDetailId(), totalInvoiceAmount, amount, totalAmount, invoiceReceiptId);

                // 3. 认领
                ClaimInvoiceF claimInvoiceF = new ClaimInvoiceF();
                claimInvoiceF.setCommunityId(communityId);
                // 收款单
                claimInvoiceF.setBillIds(command.getGatherDetailBillIds());
                claimInvoiceF.setInvoiceReceiptId(Long.valueOf(invoiceReceiptId));
                claimInvoiceF.setBillType(BillTypeEnum.收款单.getCode());
                claimInvoiceF.setInvoiceType(InvoiceLineEnum.定额发票.getCode());
                claimInvoiceF.setBuyerName(command.getBuyerName());
                log.info("定额发票绑定收款明细，发票认领参数：{}", JSONObject.toJSONString(claimInvoiceF));
                log.info("定额发票绑定账单，账单参数：{}", JSONObject.toJSONString(detailMoreVS));
                Boolean claimed = claimQuotaInvoice(claimInvoiceF,command,detailMoreVS,item);
                log.info("定额发票绑定账单，发票认领结果：{}", claimed);
            }
        } catch (Exception e) {
            throw BizException.throw400("定额发票开票失败，失败原因："+e);
        }
        return Long.valueOf(command.getInvoiceReceiptIdAndAmount().get(0).getInvoiceId());
    }

    private static List<MatchedInvoiceAndGatherDetail> matchInvoicesAndGatherDetails(List<QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF> invoices, List<InvoiceGatherDetailAmount> gatherDetails) {
        List<MatchedInvoiceAndGatherDetail> result = new ArrayList<>();

        int invoiceIndex = 0;
        for (InvoiceGatherDetailAmount detail : gatherDetails) {
            long remainingAmount = detail.getInvoiceAmount();
            while (remainingAmount > 0 && invoiceIndex < invoices.size()) {
                QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF invoice = invoices.get(invoiceIndex);
                long invoiceAmount = convertAmountMultiply100(invoice.getAmount());
                if (invoiceAmount <= remainingAmount) {
                    // 发票金额小于等于剩余收款金额，完全匹配
                    result.add(new MatchedInvoiceAndGatherDetail(invoice, detail, invoiceAmount, invoiceAmount, Long.parseLong(invoice.getInvoiceId()), detail.getGatherDetailId(), remainingAmount));
                    remainingAmount -= invoiceAmount;
                    invoiceIndex++;
                } else {
                    // 发票金额大于剩余收款金额，部分匹配
                    result.add(new MatchedInvoiceAndGatherDetail(invoice, detail, remainingAmount, invoiceAmount, Long.parseLong(invoice.getInvoiceId()), detail.getGatherDetailId(), remainingAmount));
                    // 更新发票剩余金额
                    invoiceAmount = invoiceAmount - remainingAmount;
                    invoice.setAmount(getAmountDivide100(invoiceAmount));
                    remainingAmount = 0; // 剩余收款金额已用完
                    break; // 跳出当前收款单的循环，因为已经处理完毕
                }
            }
        }
        log.info("为收款单分配发票：{}",JSONObject.toJSONString(result));
        return result;
    }

    private static double getAmountDivide100(long invoiceAmount) {
        return BigDecimal.valueOf(invoiceAmount).divide(BigDecimal.valueOf(100)).doubleValue();
    }

    /**
     * 开票计提
     * @param invoiceReceiptId
     * @return
     */
    @Transactional
    public Boolean expenseReport(Long invoiceReceiptId) {
        InvoiceA invoiceA;
        InvoiceReceiptE invoiceReceipt;
        List<InvoiceReceiptDetailE> invoiceReceiptDetails;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.KING_DEE_PUSH,
                String.valueOf(invoiceReceiptId)))){
            invoiceA = invoiceDomainService.getInvoiceA(invoiceReceiptId);
            invoiceReceipt = invoiceA.getInvoiceReceipt();
            if (KingDeePushStateEnum.已制单.equalsByCode(invoiceReceipt.getExtendFieldTwo()) ||
                    KingDeePushStateEnum.推送中.equalsByCode(invoiceReceipt.getExtendFieldTwo()) ||
                    KingDeePushStateEnum.已推送.equalsByCode(invoiceReceipt.getExtendFieldTwo())) {
                KingDeePushStateEnum kingDeePushStateEnum = KingDeePushStateEnum.valueOfCode(
                        invoiceReceipt.getExtendFieldTwo());
                assert kingDeePushStateEnum != null;
                throw BizException.throw400("票据" + kingDeePushStateEnum.getName() + "，请勿重复推送");
            }
            invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetails();
            boolean isDeveloper = invoiceReceiptDetails.stream().allMatch(
                    detail -> VoucherBillCustomerTypeEnum.开发商.equalsByCode(
                            detail.getPayerType()));
            if (!isDeveloper) {
                throw BizException.throw400("只允许推送收费对象是开发商的票据");
            }
            invoiceDomainService.updateExpenseReportPushStateWithNewTransaction(invoiceReceipt,
                    KingDeePushStateEnum.推送中, null);
        }
        try {
            // 校验
            ExpenseReportA expenseReportA = expenseReportDomainService.generateExpenseReport(invoiceA);
            expenseReportDomainService.pushRecBill(expenseReportA);
            invoiceDomainService.updateExpenseReportPushStateWithNewTransaction(invoiceReceipt,
                    KingDeePushStateEnum.已推送, null);
            return expenseReportDomainService.save(expenseReportA);
        } catch (Exception e) {
            // 记录失败信息
            log.error("票据推送计提失败：{}", e.getMessage(), e);
            invoiceDomainService.updateExpenseReportPushStateWithNewTransaction(invoiceReceipt,
                    KingDeePushStateEnum.推送失败, e.getMessage());
            throw e;
        }
    }

    /**
     * 批量开票计提
     * @param invoiceExpenseReportBatchF
     */
    public Boolean expenseReportBatch(InvoiceExpenseReportBatchF invoiceExpenseReportBatchF) {
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                List<Long> invoiceReceiptIds = invoiceExpenseReportBatchF.getInvoiceReceiptIds();
                invoiceReceiptIds.forEach(invoiceReceiptId -> {
                    // 防止事务失效
                    try {
                        log.info("计提票据:{}", invoiceReceiptId);
                        invoiceAppService.expenseReport(invoiceReceiptId);
                    } catch (Exception e) {
                        log.error("批量开票计提失败:异常id:{}", invoiceReceiptId, e);
                    }
                });
            }
        });
        return true;
    }

}
