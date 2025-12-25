package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.finance.domains.bill.command.AccountHandCommand;
import com.wishare.finance.domains.bill.command.BatchAccountHandCommand;
import com.wishare.finance.domains.bill.command.BatchRefreshAccountHandCommand;
import com.wishare.finance.domains.bill.command.RefreshAccountHandCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.AccountHandCountDto;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.HandAccountDto;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.Optional;

import jdk.security.jarsigner.JarSigner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 交账领域服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HandAccountDomainService {

    private final InvoiceRepository invoiceRepository;
    private final GatherBillRepository gatherBillRepository;
    private final AdvanceBillRepository advanceBillRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final BillAccountHandRepository billAccountHandRepository;
    private final BillAccountHandRuleRepository billAccountHandRuleRepository;
    private final PayDetailRepository payDetailRepository;

    private final SharedBillAppService sharedBillAppService;

    /**
     * 发起交账
     *
     * @param accountHandCommand  交账命令
     * @return
     */
    public HandAccountDto handAccount(AccountHandCommand accountHandCommand) {
        Long billId = accountHandCommand.getBillId();
        BillTypeEnum billType = accountHandCommand.getBillType();
        BillAccountHand accountHand = refreshOrAddAccountHand(new RefreshAccountHandCommand(billId, billType, accountHandCommand.getSupCpUnitId()));
        HandAccountDto handAccountDto = accountHand.handAccount();
        //交账成功保存资源库
        if (handAccountDto.getSuccess()){
            billAccountHandRepository.update(accountHand, new QueryWrapper<BillAccountHand>()
                .eq("id", accountHand.getId())
                .eq("sup_cp_unit_id", accountHand.getSupCpUnitId()));
            switch (billType) {
                case 应收账单:
                case 临时收费账单:
                    // 判断是否有明细
                    if(StringUtils.isBlank(accountHandCommand.getSupCpUnitId())) {
                        throw new IllegalArgumentException("上级收费单元不能为空!");
                    }
                    GatherDetail byRecBillId = gatherDetailRepository.getByRecBillId(billId, accountHandCommand.getSupCpUnitId());
                    if (null != byRecBillId){
                        gatherBillRepository.updateHandState(byRecBillId.getGatherBillId(),BillAccountHandedStateEnum.已交账, accountHandCommand.getSupCpUnitId());
                    }
                    receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                            .eq("id", billId).eq(StringUtils.isNotBlank(accountHandCommand.getSupCpUnitId()),"sup_cp_unit_id",accountHandCommand.getSupCpUnitId()).set("account_handed", accountHand.getAccountHanded()));
                    break;
                case 预收账单:
                    advanceBillRepository.update(new LambdaUpdateWrapper<AdvanceBill>()
                            .eq(Bill::getId, billId).set(Bill::getAccountHanded, accountHand.getAccountHanded()));
                    break;
                default:break;
            }
        }

        //日志记录
        BizLog.initiate(String.valueOf(accountHandCommand.getBillId()), LogContext.getOperator(), LogObject.账单, LogAction.交账, new Content());

        return handAccountDto;
    }

    /**
     * 发起批量交账
     *
     * @param batchAccountHandCommand 批量交账命令
     * @return
     */
    public BillBatchResultDto batchHandAccount(BatchAccountHandCommand batchAccountHandCommand) {
        PageF<SearchF<?>> query = batchAccountHandCommand.getQuery();
        List<Long> billIds = batchAccountHandCommand.getBillIds();
        ErrorAssertUtil.isFalseThrow402(Objects.isNull(query) && CollectionUtils.isEmpty(billIds), ErrorMessage.BILL_BATCH_ERROR);
        QueryWrapper<?> queryWrapper;
        if (CollectionUtils.isNotEmpty(billIds)){
            if(StringUtils.isBlank(batchAccountHandCommand.getSupCpUnitId())) {
                throw new IllegalArgumentException("账单ID列表不为空时，上级收费单元ID不能为空!");
            }
            queryWrapper = new QueryWrapper<>().in("bah.bill_id", billIds).eq("sup_cp_unit_id", batchAccountHandCommand.getSupCpUnitId());
        }else {
            String alias = "bah.";
            PageQueryUtils.validQueryContainsFieldAndValue(query, alias + BillSharedingColumn.交账信息.getColumnName());
            queryWrapper = query.getConditions().getQueryModel();
        }
        List<AccountHandCountDto> handCountDtoList = billAccountHandRepository.getHandAccountCount(queryWrapper);
        int successCount = 0;
        int totalCount = 0;
        if (CollectionUtils.isNotEmpty(handCountDtoList)){
            //记录账单日志
            if (CollectionUtils.isNotEmpty(billIds)){
                BizLog.initiateBatch(billIds.stream().map(String::valueOf).collect(Collectors.toList()), LogContext.getOperator(), LogObject.账单, LogAction.交账, new Content());
            }else{
                List<String> batchBillIds = null;
                Page<String> handAccountBillIdPage = null;
                int current = 1;
                int size = 500;
                while (handAccountBillIdPage == null || handAccountBillIdPage.hasNext()){
                    handAccountBillIdPage = billAccountHandRepository.getHandAccountBillIdPage(Page.of(current, size), queryWrapper);
                    batchBillIds = handAccountBillIdPage.getRecords();
                    if (CollectionUtils.isNotEmpty(batchBillIds)){
                        BizLog.initiateBatch(batchBillIds, LogContext.getOperator(), LogObject.账单, LogAction.交账, new Content());
                    }
                    current++;
                }
            }

            successCount = billAccountHandRepository.updateHandAccount(queryWrapper);
            for (AccountHandCountDto handCountDto : handCountDtoList) {
                QueryWrapper<?> cloneWrapper = SerializationUtils.clone(queryWrapper);
                totalCount += handCountDto.getHandCount();
                String receivableBillName = sharedBillAppService.getShareTableName(batchAccountHandCommand.getSupCpUnitId(),
                    BillSharedingColumn.应收账单.getTableName());
                String billAccountHandName = sharedBillAppService.getShareTableName(batchAccountHandCommand.getSupCpUnitId(),
                    BillSharedingColumn.交账信息.getTableName());
                switch (BillTypeEnum.valueOfByCode(handCountDto.getBillType())) {
                    case 应收账单:
                    case 临时收费账单:
                        Optional.ofNullable(batchAccountHandCommand.getSupCpUnitId()).ifPresentOrElse(v -> {
                            cloneWrapper.eq("rb." + BillSharedingColumn.应收账单.getColumnName(), batchAccountHandCommand.getSupCpUnitId());
                        }, () -> {
                            throw new RuntimeException("上级收费单元supCpUnitId 必传!");
                        });

                        billAccountHandRepository.updateRecBillOfHandAccount(cloneWrapper,batchAccountHandCommand.getSupCpUnitId(), receivableBillName, billAccountHandName);
                        break;
                    case 预收账单:
                        if(StringUtils.isBlank(batchAccountHandCommand.getSupCpUnitId())) {
                            throw new IllegalArgumentException("级收费单元ID不能为空!");
                        }
                        billAccountHandRepository.updateAdvBillOfHandAccount(cloneWrapper, billAccountHandName);
                        break;
                    default:break;
                }
            }
        }
        return new BillBatchResultDto(successCount, totalCount - successCount);
    }

    /**
     * 反交账
     *
     * @param billId
     * @param billType
     * @return
     */
    public boolean reversal(Long billId, BillTypeEnum billType, String supCpUnitId) {
        // 收款单
        // ReconciliationDetail 表里的billId 关联  gatherBill 表里的id
        // ReconciliationDetail 表里的billId 关联 gatherDetail 的gather_bill_id
        // gatherDetail 的 rec_bill_id  关联到 bill_account_hand 的bill_id
        //反交账
        // billAccountHandRepository.updateHandState(billId, billType, BillAccountHandedStateEnum.未交账);
        switch (billType) {
            case 应收账单:
            case 临时收费账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("请必传上级收费单元supCpUnitId !");
                }
                receivableBillRepository.updateHandState(billId, BillAccountHandedStateEnum.未交账, supCpUnitId);
                billAccountHandRepository.updateHandState(billId, billType, BillAccountHandedStateEnum.未交账, supCpUnitId);
                break;
            case 预收账单:
                advanceBillRepository.updateHandState(billId, BillAccountHandedStateEnum.未交账);
                billAccountHandRepository.updateHandState(billId, billType, BillAccountHandedStateEnum.未交账, supCpUnitId);
                break;
            case 收款单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("请必传上级收费单元supCpUnitId !");
                }
                gatherBillRepository.updateHandState(billId, BillAccountHandedStateEnum.未交账, supCpUnitId);
                gatherDetailRepository.updateHandRecState(billId, BillAccountHandedStateEnum.未交账, supCpUnitId);
                List<GatherDetail> details = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(billId), supCpUnitId);
                // 应收账单
                for (GatherDetail detail : details) {
                    ReceivableBill receivableBill = receivableBillRepository.getOne(new QueryWrapper<ReceivableBill>()
                        .eq("id", detail.getRecBillId()).eq("sup_cp_unit_id", supCpUnitId));
                    billAccountHandRepository.updateHandState(detail.getRecBillId(), BillTypeEnum.valueOfByCode(receivableBill.getBillType()), BillAccountHandedStateEnum.未交账, supCpUnitId);
                }
               break;
            default:break;
        }
        return true;
    }

    /**
     * 更新或新增交账信息
     * @param command 命令
     * @return
     */
    public BillAccountHand refreshOrAddAccountHand(RefreshAccountHandCommand command) {
        Long billId = command.getBillId();
        BillTypeEnum billType = command.getBillType();
        BillAccountHand billAccountHand = billAccountHandRepository.getByBillIdAndType(billId, billType.getCode(), command.getSupCpUnitId());
        billAccountHand = Objects.isNull(billAccountHand) ? new BillAccountHand() : billAccountHand;
        ReceivableBill receivableBill = receivableBillRepository.getOne(new QueryWrapper<ReceivableBill>()
                .eq("id", billId).eq("sup_cp_unit_id", command.getSupCpUnitId()));
        AdvanceBill advanceBill = advanceBillRepository.getById(billId);
        switch (billType) {
            case 应收账单:
            case 临时收费账单:
                /*if(StringUtils.isBlank(command.getSupCpUnitId())) {
                    throw new IllegalArgumentException("请传入supCpUnitId!");
                }*/
                billAccountHand.mergeBill(receivableBill,command.getAction(),billType);
                //添加结算信息
                addPayInfo(billAccountHand, gatherDetailRepository.getByRecBillIds(List.of(billId), command.getSupCpUnitId()));
                break;
            case 预收账单:
                billAccountHand.mergeBill(advanceBill,command.getAction(),billType);
                break;
        }
        //添加开票信息
        addInvoice(billAccountHand, invoiceRepository.getInvoiceBillDetails(List.of(billId)));
        if (null == billAccountHand.getBillId()) {
            billAccountHand.setBillId(billId);
        }
        billAccountHand.setSupCpUnitId(command.getSupCpUnitId());
        // 已审核的账单或者反审核的时候才会进行操作

        if (BillApproveStateEnum.已审核.equalsByCode(Objects.nonNull(receivableBill) ? receivableBill.getApprovedState() : 0) ||
                BillApproveStateEnum.已审核.equalsByCode(Objects.nonNull(advanceBill) ? advanceBill.getApprovedState() : 0)){

            BillAccountHand bill = billAccountHandRepository.getByBillIdAndType(billId, billType.getCode(), command.getSupCpUnitId());
            if (Objects.isNull(bill)){
                log.info("插入交账记录:账单id:{},项目id:{},账单行为:{}", billId, command.getSupCpUnitId(), command.getAction());
                billAccountHandRepository.save(billAccountHand);
            }else {
                billAccountHandRepository.update(billAccountHand, new UpdateWrapper<BillAccountHand>()
                        .eq("id", billAccountHand.getId())
                        .eq("sup_cp_unit_id", billAccountHand.getSupCpUnitId()));
            }
        }else if (command.getAction() != null && command.getAction().equals(BillAction.REVERSE_APPROVED)){
            billAccountHandRepository.update(billAccountHand, new UpdateWrapper<BillAccountHand>()
                    .eq("id", billAccountHand.getId())
                    .eq("sup_cp_unit_id", billAccountHand.getSupCpUnitId()));
        }
        return billAccountHand;
    }

    /**
     * 批量更新或新增交账信息
     * @param command 命令
     * @return
     */
    public void batchRefreshOrAddAccountHand(BatchRefreshAccountHandCommand command) {
        BillTypeEnum billType = command.getBillType();
        for (Long billId : command.getBillIds()) {
            refreshOrAddAccountHand(new RefreshAccountHandCommand(billId, billType, command.getSupCpUnitId()));
        }
    }

    /**
     * 添加发票信息
     * @param billAccountHand
     * @param invoiceBillDetails
     * @return
     */
    private BillAccountHand addInvoice(BillAccountHand billAccountHand, List<InvoiceBillDetailDto> invoiceBillDetails){
        if (CollectionUtils.isNotEmpty(invoiceBillDetails)){
            long invoiceAmount = 0;
            long invoiceTotalAmount = 0;
            LocalDateTime invoiceTime = null;
            List<String> invoiceNos = new ArrayList<>();
            List<Integer> invoiceTypes = new ArrayList<>();
            for (InvoiceBillDetailDto invoiceBillDetail : invoiceBillDetails) {
                invoiceAmount += invoiceBillDetail.getInvoiceAmount();
                invoiceTotalAmount += invoiceBillDetail.getReceiptAmount();
                invoiceTypes.add(invoiceBillDetail.getType());
                invoiceNos.add(invoiceBillDetail.getInvoiceNo());
                invoiceTime = DateTimeUtil.maxDateTime(invoiceTime, invoiceBillDetail.getInvoiceTime());
            }
            billAccountHand.putInvoice(invoiceNos, invoiceAmount, invoiceTotalAmount, invoiceTypes, invoiceTime);
        }
        return billAccountHand;
    }

    /**
     * 添加支付信息
     * @param billAccountHand
     * @param gatherDetails
     * @return
     */
    private BillAccountHand addPayInfo(BillAccountHand billAccountHand, List<GatherDetail> gatherDetails){
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            long payAmount = 0;
            long payableAmount = 0;
            LocalDateTime payTime = null;
            List<PayWay> payWays = new ArrayList<>();
            for (GatherDetail gatherDetail : gatherDetails) {
                payAmount += gatherDetail.getRecPayAmount();
                payTime = DateTimeUtil.maxDateTime(payTime, gatherDetail.getPayTime());
                payWays.add(new PayWay(gatherDetail.getPayWay(), gatherDetail.getPayChannel()));
                payableAmount += gatherDetail.getPayAmount() - gatherDetail.getRefundAmount() - gatherDetail.getCarriedAmount();
            }
            billAccountHand.putPayInfo(payAmount, payTime, payWays, payableAmount);
        }
        return billAccountHand;
    }

    /**
     * 创建一个规则
     * @return
     */
    public BillHandAccountRule getOrCreateHandAccountRule(Long ruleId) {
        BillHandAccountRule billHandAccountRule = billAccountHandRuleRepository.getByTenant(ApiData.API.getTenantId().get());
        if (Objects.isNull(billHandAccountRule) && ruleId.compareTo(Long.valueOf(-66)) == 0){
            billHandAccountRule = new BillHandAccountRule();
            billAccountHandRuleRepository.save(billHandAccountRule);
        }
        return billHandAccountRule;
    }

    /**
     * 更新交账规则
     * @param billHandAccountRule
     * @return
     */
    public boolean updateHandAccountRule(BillHandAccountRule billHandAccountRule) {
        BillHandAccountRule oldBillAccount = billAccountHandRuleRepository.getById(billHandAccountRule.getId());
        ErrorAssertUtil.notNullThrow404(oldBillAccount, ErrorMessage.ACCOUNT_HAND_RULE_NOT_EXIST);
        return billAccountHandRuleRepository.updateById(billHandAccountRule);
    }

    public boolean updateAccountHandDelete(RefreshAccountHandCommand command) {
        Long billId = command.getBillId();
        BillTypeEnum billType = command.getBillType();
        String supCpUnitId = command.getSupCpUnitId();
        BillAccountHand billAccountHand = billAccountHandRepository.getBillIdAndTypeAndSupCpUnitId(billId, billType.getCode(),supCpUnitId);
        if (Objects.isNull(billAccountHand)){
            return false;
        }
        LambdaUpdateWrapper<BillAccountHand> billAccountHandLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        billAccountHandLambdaUpdateWrapper.set(BillAccountHand::getDeleted, DataDeletedEnum.DELETED.getCode())
                .eq(BillAccountHand::getId , billAccountHand.getId())
                .eq(BillAccountHand::getSupCpUnitId , billAccountHand.getSupCpUnitId());
        return billAccountHandRepository.update(billAccountHandLambdaUpdateWrapper);
    }


    public boolean flowClaimAutoAccountHand(RefreshAccountHandCommand command) {
        Long billId = command.getBillId();
        //通过收款id获取应收，预收，临时账单的id--去判断交账表是否存在数据
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GatherDetail::getGatherBillId,billId)
             .eq(GatherDetail::getSupCpUnitId,command.getSupCpUnitId())
        ;
        List<GatherDetail> list = gatherDetailRepository.list(wrapper);
        if (CollectionUtils.isNotEmpty(list)){
            for (GatherDetail gatherDetail : list) {
                Long recBillId = gatherDetail.getRecBillId();
                String supCpUnitId = gatherDetail.getSupCpUnitId();
                LambdaQueryWrapper<BillAccountHand> wrapperHand = new LambdaQueryWrapper<>();
                wrapperHand.eq(BillAccountHand::getBillId,recBillId)
                        .eq(BillAccountHand::getSupCpUnitId,supCpUnitId)
                        .eq(BillAccountHand::getAccountHanded, BillAccountHandedStateEnum.未交账.getCode())
                        .eq(BillAccountHand::getState, BillStateEnum.正常.getCode());
                BillAccountHand billAccountHand = billAccountHandRepository.getOne(wrapperHand);
                if (Objects.nonNull(billAccountHand)){
                    // 流水认领成功后 金额一致，修改状态为已交账
                    if (billAccountHand.getPayableAmount().equals(billAccountHand.getPayAmount()) && billAccountHand.getPayAmount().equals(billAccountHand.getInvoiceAmount())){
                        LambdaUpdateWrapper<BillAccountHand> billAccountHandLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        billAccountHandLambdaUpdateWrapper.set(BillAccountHand::getAccountHanded, BillAccountHandedStateEnum.已交账.getCode())
                                .eq(BillAccountHand::getId , billAccountHand.getId())
                                .eq(BillAccountHand::getSupCpUnitId , billAccountHand.getSupCpUnitId());
                        billAccountHandRepository.update(billAccountHandLambdaUpdateWrapper);
                    }
                }
            }
        }
        //通过付款id获取payable_bill_id--判断交账表是否存在数据
        LambdaQueryWrapper<PayDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayDetail::getPayBillId,billId)
                  .eq(PayDetail::getSupCpUnitId , command.getSupCpUnitId())
        ;
        List<PayDetail> payList = payDetailRepository.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(payList)){
            for (PayDetail payDetail : payList) {
                Long payableBillId = payDetail.getPayableBillId();
                String supCpUnitId = payDetail.getSupCpUnitId();
                LambdaQueryWrapper<BillAccountHand> wrapperHand = new LambdaQueryWrapper<>();
                wrapperHand.eq(BillAccountHand::getBillId, payableBillId)
                        .eq(BillAccountHand::getSupCpUnitId, supCpUnitId)
                        .eq(BillAccountHand::getAccountHanded, BillAccountHandedStateEnum.未交账.getCode())
                        .eq(BillAccountHand::getState, BillStateEnum.正常.getCode());
                BillAccountHand billAccountHand = billAccountHandRepository.getOne(wrapperHand);
                if (Objects.nonNull(billAccountHand)) {
                    // 流水认领成功后 金额一致，修改状态为已交账
                    if (billAccountHand.getPayableAmount().equals(billAccountHand.getPayAmount()) && billAccountHand.getPayAmount().equals(billAccountHand.getInvoiceAmount())) {
                        LambdaUpdateWrapper<BillAccountHand> billAccountHandLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        billAccountHandLambdaUpdateWrapper.set(BillAccountHand::getAccountHanded, BillAccountHandedStateEnum.已交账.getCode())
                                .eq(BillAccountHand::getId , billAccountHand.getId())
                                .eq(BillAccountHand::getSupCpUnitId , billAccountHand.getSupCpUnitId());
                        billAccountHandRepository.update(billAccountHandLambdaUpdateWrapper);
                    }
                }
            }
        }
        return true;
    }
}
