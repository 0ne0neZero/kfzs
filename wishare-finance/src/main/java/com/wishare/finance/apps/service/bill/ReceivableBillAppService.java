package com.wishare.finance.apps.service.bill;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.CheckBillTimeOverlapV;
import com.wishare.finance.apps.model.bill.vo.HistoryV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillDetailV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillPageV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillsV;
import com.wishare.finance.apps.model.bill.vo.ReceivableIntervalBillV;
import com.wishare.finance.apps.model.bill.vo.ReceivableMaxEndTimeV;
import com.wishare.finance.apps.model.bill.vo.ReceivableRoomsV;
import com.wishare.finance.apps.model.bill.vo.RecordImportChargeBillV;
import com.wishare.finance.apps.model.invoice.invoice.fo.ReceiptBatchF;
import com.wishare.finance.apps.model.third.BillInfoResponse;
import com.wishare.finance.apps.model.third.QueryBillReq;
import com.wishare.finance.apps.service.configure.chargeitem.ChargeItemAppService;
import com.wishare.finance.apps.service.invoicereceipt.ReceiptAppService;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.command.AddPeriodicReceivableBillCommand;
import com.wishare.finance.domains.bill.command.FinishInvoiceCommand;
import com.wishare.finance.domains.bill.command.UpdateBillCommand;
import com.wishare.finance.domains.bill.consts.enums.BillCarriedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.dto.JumpRecordDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillGroupDetailDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillMoreInfoDto;
import com.wishare.finance.domains.bill.dto.ReceivableRoomsDto;
import com.wishare.finance.domains.bill.dto.SettleDetailDto;
import com.wishare.finance.domains.bill.entity.ChargeOverdueE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.ChargeOverdueMapper;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.refund.ChargeTicketRuleQueryTypeV;
import com.wishare.finance.domains.refund.ChargeTicketRuleTypeQueryF;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.OverdueStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillDxZJMapper;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.SpacePermissionF;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityPJCodeV;
import com.wishare.finance.infrastructure.support.lock.BusinessLockHelper;
import com.wishare.finance.infrastructure.support.lock.LockException;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.finance.infrastructure.support.lock.MultiLocker;
import com.wishare.finance.infrastructure.utils.RegexUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.enums.ErrMsgEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ReceivableBillAppService extends BillAppServiceImpl<ReceivableBillDomainService, ReceivableBill> {

    private final ChargeOverdueMapper chargeOverdueMapper;

    private final ChargeItemAppService chargeItemAppService;

    private final ReceivableBillMapper billMapper;

    private final SharedBillAppService sharedBillAppService;

    private final SpaceClient spaceClient;

    private final ReceivableBillRepository receivableBillRepository;
    private final VoucherPushBillDxZJMapper voucherPushBillZJMapper;
    private final VoucherBillDetailDxZJMapper voucherBillDetailZJMapper;

    private final VoucherPushBillDxZJMapper voucherPushBillDxZJMapper;

    private final VoucherBillDetailDxZJMapper voucherBillDetailDxZJMapper;

    private final ReceiptAppService receiptAppService;

    private final ChargeClient chargeClient;

    @Override
    public BillTypeEnum getBillType() {
        return BillTypeEnum.应收账单;
    }

    /**
     * 新增周期性账单
     *
     * @param createPeriodicReceivableBillList
     * @return
     */
    @Transactional
    public Boolean addBatchPeriodicReceivableBill(List<AddPeriodicReceivableBillF> createPeriodicReceivableBillList) {
        List<AddPeriodicReceivableBillCommand> addPeriodicReceivableBillCommands = Global.mapperFacade.mapAsList(createPeriodicReceivableBillList, AddPeriodicReceivableBillCommand.class);
        addPeriodicReceivableBillCommands.forEach(periodicReceivableBillCommand -> {
            periodicReceivableBillCommand.getReceivableBills().forEach(addReceivableBillCommand -> {
                addReceivableBillCommand.setStatutoryBodyId(periodicReceivableBillCommand.getStatutoryBodyId());
                addReceivableBillCommand.setStatutoryBodyName(periodicReceivableBillCommand.getStatutoryBodyName());
                addReceivableBillCommand.setCommunityId(periodicReceivableBillCommand.getCommunityId());
                addReceivableBillCommand.setCommunityName(periodicReceivableBillCommand.getCommunityName());
                addReceivableBillCommand.setSupCpUnitId(periodicReceivableBillCommand.getCommunityId());
                addReceivableBillCommand.setChargeItemId(periodicReceivableBillCommand.getChargeItemId());
                addReceivableBillCommand.setChargeItemName(periodicReceivableBillCommand.getChargeItemName());
                addReceivableBillCommand.setRoomId(periodicReceivableBillCommand.getRoomId());
                addReceivableBillCommand.setRoomName(periodicReceivableBillCommand.getRoomName());
                addReceivableBillCommand.setTaxRateId(periodicReceivableBillCommand.getTaxRateId());
                addReceivableBillCommand.setTaxRate(periodicReceivableBillCommand.getTaxRate());
            });
        });
        baseBillDomainService.saveBatchByPeriodic(addPeriodicReceivableBillCommands);
        return Boolean.TRUE;
    }

    /**
     * 根据账单ids获取应收账单信息
     *
     * @param billIds
     * @return
     */
    public List<ReceivableBillMoreInfoDto> receivableBillInfo(List<Long> billIds, String supCpUnitId) {
        List<ReceivableBillMoreInfoDto> dtoList = baseBillDomainService.receivableBillInfo(billIds, supCpUnitId);
        return dtoList;
    }


    public Integer queryBillCountByPage(PageF<SearchF<?>> queryF) {
        return baseBillDomainService.queryBillCountByPage(queryF);
    }

    /**
     * 根据账单ids获取应收账单结算详情
     *
     * @param billIds
     * @return
     */
    public SettleDetailDto settleDetail(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.settleDetail(billIds, supCpUnitId);
    }

    /**
     * 根据账单ids获取结算记录
     *
     * @param billIds
     * @return
     */
    public List<BillSettleDto> getBillSettle(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.getBillSettle(billIds, BillTypeEnum.应收账单, supCpUnitId);
    }

    /**
     * 批量结算应收账单
     *
     * @param form
     * @return
     */
    @Transactional
    public Long settleBatch(List<AddBillSettleF> form) {

        List<AddBillSettleCommand> addBillSettleCommands = Global.mapperFacade.mapAsList(form, AddBillSettleCommand.class);
        List<String> strBillIds = addBillSettleCommands.stream()
                .map(e -> e.getBillId().toString()).distinct().collect(Collectors.toList());
        //锁定账单信息
        try {
            BusinessLockHelper.tryMultiLock(MultiLocker.of(LockerEnum.BILL_LOCK, strBillIds,
                    LockerEnum.BILL_LOCK.defLeaseTime()));
        } catch (LockException e) {
            log.error("结算获取锁失败！", e);
            ErrorAssertUtil.throw400(ErrorMessage.PAYMENT_PRE_TRANSACT_PAYING);
        }
        Long result;
        try {
            result = baseBillDomainService.settleBatch(addBillSettleCommands);
        } finally {
            //账单解锁
            BusinessLockHelper.unLock(MultiLocker.of(LockerEnum.BILL_LOCK, strBillIds));
        }
        return result;
    }


    /**
     * 补录批量结算(返回收款单id)
     *
     * @param form param
     * @return {@link Boolean}
     */
    public Map<String, List<RecordImportChargeBillV>> settleBatchRecordReturn(List<List<AddBillSettleF>> form, String pcId, String userName) {
        // 搜集成功与错误信息条数
        List<RecordImportChargeBillV> errorList = Lists.newArrayList();
        List<RecordImportChargeBillV> successList = Lists.newArrayList();
        log.info("本批次账单数量，{}", form.size());
        long start = System.currentTimeMillis();
        log.info("本批次账单查询耗时开始，{}", start);
        List<ReceivableBill> billList = baseBillDomainService.getList(
                form.stream().map(a -> a.get(0).getBillId()).collect(Collectors.toList())
                , form.get(0).get(0).getSupCpUnitId());
        log.info("本批次账单查询耗时结束，{}", System.currentTimeMillis() - start);
        // 封装处理
        Map<Long, List<ReceivableBill>> map = billList.stream().collect(Collectors.groupingBy(ReceivableBill::getId));
        form.forEach(a -> {
            // 批量缴费
            for (AddBillSettleF addBillSettleF : a) {
                List<ReceivableBill> receivableBills = map.get(addBillSettleF.getBillId());
                try {
                    // 缴费
                    long start1 = System.currentTimeMillis();
                    log.info("账单" + addBillSettleF.getBillId() + "缴费耗时开始，{}", start1);
                    List<AddBillSettleCommand> addBillSettleCommands = Global.mapperFacade.mapAsList(List.of(addBillSettleF), AddBillSettleCommand.class);
                    List<String> strBillIds = addBillSettleCommands.stream()
                            .map(e -> e.getBillId().toString()).distinct().collect(Collectors.toList());
                    //锁定账单信息
                    try {
                        BusinessLockHelper.tryMultiLock(MultiLocker.of(LockerEnum.BILL_LOCK, strBillIds,
                                LockerEnum.BILL_LOCK.defLeaseTime()));
                    } catch (LockException e) {
                        ErrorAssertUtil.throw400(ErrorMessage.PAYMENT_PRE_TRANSACT_PAYING);
                    }
                    try {
                        BillGatherA<ReceivableBill> gatherBillDto = baseBillDomainService.settleImportBatch(addBillSettleCommands, receivableBills);
                        try {
                            // 账单补录结算完自动开票
                            log.info("账单补录自动开票开始");
                            GatherBill gatherBill = gatherBillDto.getGatherBill();
                            List<GatherDetail> gatherBillDetails = gatherBillDto.getGatherDetails();
                            // 查询默认配置模板
                            String supCpUnitId = gatherBill.getSupCpUnitId();
                            Long gatherBillId = gatherBill.getId();
                            List<Long> gatherDetailIds = gatherBillDetails.stream().map(GatherDetail::getId).collect(Collectors.toList());
                            ChargeTicketRuleTypeQueryF chargeTicketRuleTypeQueryF = new ChargeTicketRuleTypeQueryF()
                                    .setCommunityId(supCpUnitId)
                                    .setGatherBillId(gatherBillId)
                                    .setGatherDetailIds(gatherDetailIds);
                            ChargeTicketRuleQueryTypeV chargeTicketRule = chargeClient.queryGatherDetailIds(chargeTicketRuleTypeQueryF);
                            if (null != chargeTicketRule) {
                                // 开具电子收据并进行签章
                                ReceiptBatchF receiptBatchF = new ReceiptBatchF();
                                receiptBatchF.setReceiptTemplateId(chargeTicketRule.getReceiptTemplateId());
                                receiptBatchF.setType(InvoiceLineEnum.电子收据.getCode());
                                receiptBatchF.setSignStatus(0);
                                receiptBatchF.setPushMode(List.of(-1));
                                receiptBatchF.setSysSource(1);
                                receiptBatchF.setInvSource(1);
                                receiptBatchF.setBillIds(
                                        com.google.common.collect.Lists.newArrayList(addBillSettleF.getBillId())
                                );
                                receiptBatchF.setPriceTaxAmount(
                                        gatherBillDetails.stream()
                                                .mapToLong(GatherDetail::getCanInvoiceAmount)
                                                .sum()
                                );
                                receiptBatchF.setClerk(userName);
                                receiptBatchF.setGatherBillIds(
                                        com.google.common.collect.Lists.newArrayList(gatherBillId)
                                );
                                receiptBatchF.setGatherDetailBillIds(gatherDetailIds);
                                receiptBatchF.setGatherBillType(0);
                                receiptBatchF.setSupCpUnitId(supCpUnitId);
                                log.info("账单补录自动开票入参:{}", JSON.toJSONString(receiptBatchF));
                                Long invoiceReceiptEId = receiptAppService.invoiceBatch(receiptBatchF);
                                log.info("账单补录自动开票出参:{}", invoiceReceiptEId);
                            }
                        } catch (Exception e) {
                            log.error("账单补录自动开票异常 form:{}, pcId:{}, userName:{}", JSON.toJSONString(form), pcId, userName, e);
                        }
                    } finally {
                        //账单解锁
                        BusinessLockHelper.unLock(MultiLocker.of(LockerEnum.BILL_LOCK, strBillIds));
                    }
                    successList.add(new RecordImportChargeBillV().setId(addBillSettleF.getBillId())
                            .setExtField4(CollectionUtils.isEmpty(receivableBills) ? null : receivableBills.get(0).getExtField4())
                            .setActualSettleAmount(CollectionUtils.isEmpty(receivableBills) ? 0L : receivableBills.get(0).getActualSettleAmount())
                            .setCommunityId(CollectionUtils.isEmpty(receivableBills) ? null : receivableBills.get(0).getCommunityId())
                            .setResult(true).setIndex(addBillSettleF.getIndex())
                            .setRowNumber(Long.valueOf(addBillSettleF.getIndex())));
                    // 成功补录结算记录日志
                    BizLog.normal(String.valueOf(addBillSettleF.getBillId()),
                            LogContext.getOperator(), LogObject.账单, LogAction.补录导入, new Content());
                    log.info("账单" + addBillSettleF.getBillId() + "缴费耗时结束，{}", System.currentTimeMillis() - start1);
                } catch (BizException e) {
                    errorList.add(new RecordImportChargeBillV().setId(addBillSettleF.getBillId()).setResult(false).setErrorMessage(e.getMessage())
                            .setCommunityId(CollectionUtils.isEmpty(receivableBills) ? null : receivableBills.get(0).getCommunityId())
                            .setIndex(addBillSettleF.getIndex()).setRowNumber(Long.valueOf(addBillSettleF.getIndex())).setErrMsg(e.getMessage()));
                } catch (Exception e) {
                    log.error("账单补录导入异常", e);
                    errorList.add(new RecordImportChargeBillV().setId(addBillSettleF.getBillId()).setResult(false)
                            .setErrorMessage("系统业务处理异常，请联系系统管理员").setIndex(addBillSettleF.getIndex())
                            .setCommunityId(CollectionUtils.isEmpty(receivableBills) ? null : receivableBills.get(0).getCommunityId())
                            .setIndex(addBillSettleF.getIndex()).setRowNumber(Long.valueOf(addBillSettleF.getIndex())).setErrMsg(e.getMessage()));
                }
            }
            //key 续命
            RedisHelper.setGAtExpire(pcId, 3 * 60, Boolean.FALSE.toString());
        });
        Map<String, List<RecordImportChargeBillV>> resultMap = new HashMap<>();
        resultMap.put("success", successList);
        resultMap.put("error", errorList);
        return resultMap;
    }

    /**
     * 发起开票
     *
     * @param billIds
     * @param billIdsMap key-billId,value-开票状态
     * @return
     */
    public Boolean invoiceBatch(List<Long> billIds, String supCpUnitId, Map<Long, Integer> billIdsMap) {
        return baseBillDomainService.invoiceBatch(billIds, supCpUnitId, billIdsMap);
    }

    /**
     * 完成开票
     * 修改 收款单表[gather_bill] 开票金额、开票状态
     * 修改 账单表 开票状态、挂账状态、开票金额
     *
     * @param finishInvoiceFList
     * @return
     */
    public Boolean finishInvoiceBatch(List<FinishInvoiceF> finishInvoiceFList) {
        List<FinishInvoiceCommand> finishInvoiceCommands = Global.mapperFacade.mapAsList(finishInvoiceFList, FinishInvoiceCommand.class);
        return baseBillDomainService.finishInvoiceBatch(finishInvoiceCommands);
    }

    /**
     * 重新计算设置开票状态
     *
     * @param billId
     */
    public void reSetBillInvoiceState(Long billId, String supCpUnitId) {
        baseBillDomainService.reSetBillInvoiceState(billId, supCpUnitId);
    }

    /**
     * 绿城poc查询应收账单分页接口
     *
     * @param queryF
     * @return
     */
    public PageV<ReceivableBillPageV> getCommonPage(PageF<SearchF<?>> queryF) {
        IPage page = baseBillDomainService.getCommonPage(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), getBillOverdueDetail(Global.mapperFacade.mapAsList(page.getRecords(), ReceivableBillPageV.class)));
    }

    /**
     * 批量作废,红冲开票金额
     *
     * @param invoiceVoidBatchFList
     * @return
     */
    public Boolean invoiceVoidBatch(List<InvoiceVoidBatchF> invoiceVoidBatchFList) {
        List<FinishInvoiceCommand> finishInvoiceCommands = Global.mapperFacade.mapAsList(invoiceVoidBatchFList, FinishInvoiceCommand.class);
        return baseBillDomainService.invoiceVoidBatch(finishInvoiceCommands, finishInvoiceCommands.get(0).getSupCpUnitId());
    }


    /**
     * 应收账单挂账
     *
     * @param receivableBillId
     * @return
     */
    public Boolean onAccount(Long receivableBillId, String supCpUnitId) {
        return baseBillDomainService.onAccount(receivableBillId, supCpUnitId);
    }

    /**
     * 应收账单销账
     *
     * @param billId
     * @return
     */
    public Boolean writeOff(Long billId, String supCpUnitId) {
        return baseBillDomainService.writeOff(billId, supCpUnitId);
    }

    /**
     * 应收账单冲销
     *
     * @param receivableBillId
     * @return
     */
    @Transactional
    public Boolean reverse(Long receivableBillId, String extField1, String supCpUnitId) {
        return baseBillDomainService.reverse(receivableBillId, extField1, supCpUnitId);
    }

    /**
     * 回滚应收账单冲销
     *
     * @param receivableBillId
     * @return
     */
    @Transactional
    public Boolean robackReverse(Long receivableBillId, String supCpUnitId) {
        return baseBillDomainService.robackReverse(receivableBillId, supCpUnitId);
    }

    /**
     * 银行托收清结
     *
     * @param bankSettleF
     * @return
     */
    @Transactional
    public Boolean bankSettle(BankSettleF bankSettleF) {
        return baseBillDomainService.bankSettle(bankSettleF);
    }

    @Override
    public Boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        return baseBillDomainService.unfreezeBatch(unFreezeBatchF);
    }

    /**
     * 查询应收收费信息
     *
     * @param pageF 分页入参
     * @return ReceivableBillApplyDetailV
     */
    public PageV<ReceivableRoomsV> receivableRooms(ReceivableRoomsPageF pageF) {
        Page<ReceivableRoomsV> page = baseBillDomainService.receivableRooms(pageF);
        return PageV.of(pageF.getPageNum(), pageF.getPageSize(), page.getTotal(), page.getRecords());

    }

    /**
     * 查询可预收信息
     *
     * @param pageF 分页入参
     * @return ReceivableBillApplyDetailV
     */
    public PageV<ReceivableRoomsDto> queryCanAdvanceRooms(ReceivableRoomsPageF pageF) {
        Page<ReceivableRoomsDto> page = baseBillDomainService.queryCanAdvanceRooms(pageF);
        return PageV.of(pageF.getPageNum(), pageF.getPageSize(), page.getTotal(), page.getRecords());

    }


    /**
     * 查询房间应收账单列表
     *
     * @param queryF 查询入参
     * @return List
     */
    public List<ReceivableBillsV> receivableBills(ReceivableBillF queryF) {
        return baseBillDomainService.receivableBills(queryF);
    }

    /**
     * 分页查询应收账单列表
     *
     * @param queryF 查询入参
     * @return PageV
     */
    public PageV<HistoryV> history(HistoryF queryF) {
        IPage<ReceivableBill> page = baseBillDomainService.history(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), HistoryV.class));
    }

    /**
     * 导出收款单
     *
     * @param queryF   查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        baseBillDomainService.export(queryF, response);
    }

    /**
     * 编辑应收账单
     *
     * @param editBillF
     * @return
     */
    public Boolean editRec(EditBillF editBillF) {
        return baseBillDomainService.editBill(editBillF);
    }


    /**
     * 银行签约对应编辑应收账单
     *
     * @param editBillF
     * @return
     */
    public Boolean editRecForBankSign(EditBillForBankSignF editBillF) {
        return baseBillDomainService.editRecForBankSign(editBillF);
    }

    /**
     * 查询应收账单最大账单结束时间
     *
     * @param maxEndTimeBillF
     * @return
     */
    public List<ReceivableMaxEndTimeV> queryMaxEndTime(ReceivableMaxEndTimeBillF maxEndTimeBillF) {
        return baseBillDomainService.queryMaxEndTime(maxEndTimeBillF);
    }

    /**
     * 查询区间账单信息
     *
     * @param query
     * @return
     */
    public List<ReceivableIntervalBillV> queryIntervalBill(ReceivableIntervalBillF query) {
        return baseBillDomainService.queryIntervalBill(query);
    }


    public void getBillCostType() {
        baseBillDomainService.getBillCostType();
    }

    public Boolean deleteInitBill(PageF<SearchF<?>> queryF) {
        return baseBillDomainService.deleteInitBill(queryF);
    }


    public List<ReceivableBillDetailV> getAdvanceBillByRoomIds(List<Long> roomIds) {
        List<ReceivableBill> advanceBillByRoomIds = baseBillDomainService.getAdvanceBillByRoomIds(roomIds);
        return Global.mapperFacade.mapAsList(advanceBillByRoomIds, ReceivableBillDetailV.class);
    }

    public Boolean editBillDeviceReading(EditBillReadingF editBillReadingF) {
        return baseBillDomainService.editBillDeviceReading(editBillReadingF);
    }

    public <T> PageV<T> jumpRecordPage(PageF<SearchF<?>> queryF, Class<T> tClass) {
        IPage<JumpRecordDto> page = baseBillDomainService.jumpRecordPage(queryF);
        return PageV.of(queryF.getPageNum(), queryF.getPageSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), tClass));
    }

    /**
     * 拆分账单
     *
     * @param billExecuteSplitF 账单详情
     * @return {@link Boolean}
     */
    public Boolean billSplit(BillExecuteSplitF billExecuteSplitF) {
        return baseBillDomainService.billSplit(billExecuteSplitF);
    }


    @Transactional(rollbackFor = Exception.class)
    public Boolean billSplitChargeItem(List<ReceivableBillDetailV> splitBills) {
        // 根据 备注信息修改账单状态 并获取映射信息
        ReceivableBillDetailV originalBill = null;
        List<ReceivableBill> newBills = new ArrayList<>();

        for (ReceivableBillDetailV splitBill : splitBills) {
            String remark = splitBill.getRemark();
            if (remark != null && remark.contains("[ORIGIN_ID:")) {
                // 原始对象
                splitBill.setState(BillStateEnum.作废.getCode());
                String originId = RegexUtils.extractId(remark, "ORIGIN_ID");
                splitBill.setRemark(remark.replace("[ORIGIN_ID:" + originId + "]", "").trim());
                originalBill = splitBill;
                // 1.更改原账单状态
                BatchEditBillF editRec = new BatchEditBillF();
                editRec.setBillIds(List.of(originalBill.getBillId()));
                editRec.setState(BillStateEnum.作废.getCode());
                editRec.setSupCpUnitId(originalBill.getSupCpUnitId());
                Boolean result = baseBillDomainService.batchEditBill(editRec);
                ErrorAssertUtil.isTrueThrow402(result, ErrMsgEnum.FAILED, "原账单暂不可更新，请稍后重试");
            }
            if (remark != null && remark.contains("[GENERATED_FROM:")) {
                // 生成对象
                String originId = RegexUtils.extractId(remark, "GENERATED_FROM");
                splitBill.setRemark(remark.replace("[GENERATED_FROM:" + originId + "]", "").trim());
                ReceivableBill bill = Global.mapperFacade.map(splitBill, ReceivableBill.class);
                newBills.add(bill);
            }
        }
        ErrorAssertUtil.isTrueThrow402(Objects.nonNull(originalBill), ErrorMessage.BILL_NOT_EXIST, "原账单不存在");

        for (ReceivableBill bill : newBills) {
            // 自动加载账单id
            bill.setIdentifier(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
            // 自动加载账单编号
            bill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
            // 加载初始化信息
            bill.setState(Const.State._0);
            // 校验是否满足账单唯一性
            if (BillTypeEnum.应收账单.getCode() == bill.getBillType()) {
                if (!baseBillDomainService.uniqueImportBill(bill, bill.getSupCpUnitId())) {
                    throw BizException.throw403(
                            "系统中存在与拆分后账单相同房号、费项、计费周期、收费对象的账单，遂无法拆分");
                }
            }
            bill.refresh();
            receivableBillRepository.save(bill);
            //日志记录
            assert originalBill != null;
            BizLog.normal(String.valueOf(bill.getId()),
                    LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("由账单编号为", true)))
                            .option(new ContentOption(new PlainTextDataItem(originalBill.getBillNo(), true)))
                            .option(new ContentOption(new PlainTextDataItem("拆分生成", false))));
        }
        if (CollectionUtils.isNotEmpty(newBills)) {
            // 原账单日志更新
            Content content = new Content();
            content.option(new ContentOption(new PlainTextDataItem("该账单拆分为", true)));
            for (int i = 0; i < newBills.size(); i++) {
                content.option(new ContentOption(new PlainTextDataItem(newBills.get(i).getBillNo(), true)));
            }
            content.option(new ContentOption(new PlainTextDataItem("  " + newBills.size() + "个账单", false)));
            BizLog.normal(String.valueOf(originalBill.getId()),
                    LogContext.getOperator(), LogObject.账单, LogAction.拆分,
                    content);
        }

        return true;
    }

    /**
     * 拆分账单(部分结算账单)
     *
     * @param billExecuteSplitF 账单详情
     * @return {@link Boolean}
     */
    public Boolean splitPartialSettlement(BillExecuteSplitF billExecuteSplitF) {
        return baseBillDomainService.splitPartialSettlement(billExecuteSplitF);
    }


    @Override
    public List<ReceivableBill> getByChargeNcId(String chargeNcId, String communityId) {
        return baseBillDomainService.getByChargeNcId(chargeNcId, communityId);
    }

    @Override
    public Boolean deleteBillById(String communityId, List<ReceivableBill> list) {
        return baseBillDomainService.deleteBillById(communityId, list);
    }

    public Boolean updateById(ReceivableBill receivableBill) {
        return baseBillDomainService.updateById(receivableBill);
    }

    public int batchUpdateGatherBillById(List<UpdateGatherBillF> updateGatherBillFList) {
        return baseBillDomainService.batchUpdateGatherBillById(updateGatherBillFList);
    }

    public PageV<ReceivableBillGroupDetailDto> queryPageApprove(PageF<SearchF<?>> queryF) {
        IPage page = baseBillDomainService.queryPageApprove(queryF);
        List<ReceivableBillGroupDetailDto> list = Global.mapperFacade.mapAsList(page.getRecords(), ReceivableBillGroupDetailDto.class);
        for (ReceivableBillGroupDetailDto rbd : list) {
            rbd.setPayInfosString(baseBillDomainService.getPayInfosPaychannelString(rbd));
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), list);
    }

    private List<ReceivableBillPageV> getBillOverdueDetail(List<ReceivableBillPageV> records) {
        //如果是临港环境，需要把费项全路径返回
        if (EnvConst.LINGANG.equals(EnvData.config) && CollectionUtils.isNotEmpty(records)) {
            List<Long> itemIds = records.stream().map(ReceivableBillPageV::getChargeItemId).collect(Collectors.toList());
            List<ChargeItemE> chargeItems = chargeItemAppService.getNameByIds(itemIds);

            records.forEach(bill -> {
                Long chargeItemId = bill.getChargeItemId();
                if (Objects.nonNull(chargeItemId) && CollectionUtils.isNotEmpty(chargeItems)) {
                    List<ChargeItemE> collect = chargeItems.stream().filter(item -> chargeItemId.equals(item.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        bill.setChargeItemName(collect.get(0).getName());
                    }
                }
            });
            return records;
        }


        if (!EnvConst.FANGYUAN.equals(EnvData.config)) {
            //除了方圆其他项目没有违约金
            return records;
        }
        if (CollectionUtil.isNotEmpty(records)) {
            List<Long> billIds = records.stream().map(ReceivableBillPageV::getId).collect(Collectors.toList());
            //先查违约金表查到账单id,再查应收账单表查到违约金账单信息
            List<Long> overdueBillIds = getBillOverdueIdList(billIds);
            if (CollectionUtil.isEmpty(overdueBillIds)) {
                return records;
            }
//            List<ReceivableBill> overdueBills = baseBillDomainService.getList(overdueBillIds, records.get(0).getSupCpUnitId());
            PageF<SearchF<?>> pageF = new PageF<>();
            SearchF<ReceivableBill> searchF = new SearchF<>();
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("id", overdueBillIds, 15));
            fields.add(new Field("state", List.of(0, 1), 15));
            fields.add(new Field("reversed", 0, 1));
//            fields.add(new Field("deleted",0,1));
            fields.add(new Field("overdue", 1, 1));
            fields.add(new Field("sup_cp_unit_id", records.get(0).getCommunityId(), 1));
            searchF.setFields(fields);
            pageF.setConditions(searchF);
            pageF.setPageNum(1);
            pageF.setPageSize(records.size());
            List<ReceivableBill> overdueBills = baseBillDomainService.getList(pageF, 1);
            if (CollectionUtil.isEmpty(overdueBills)) {
                return records;
            }
            Map<Long, ReceivableBill> billMap = overdueBills.stream().collect(Collectors.toMap(ReceivableBill::getRefBillId, e -> e, (u1, u2) -> u1));
            for (ReceivableBillPageV record : records) {
                ReceivableBill receivableBill = billMap.get(record.getId());
                if (receivableBill != null) {
                    record.setReceivableOverdueAmount(receivableBill.getReceivableAmount());
                    record.setActualPayOverdueAmount(receivableBill.getActualPayAmount());
                    record.setDeductionOverdueAmount(receivableBill.getDiscountAmount());
                    record.setNotReceivedOverdueAmount(receivableBill.getNotReceivedOverdueAmount());
                }
            }
        }
        return records;
    }

    public List<Long> getBillOverdueIdList(List<Long> billIds) {
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ref_bill_id", billIds);
        queryWrapper.eq("deleted", 0);
        List<ChargeOverdueE> chargeOverdueES = chargeOverdueMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(chargeOverdueES)) {
            return new ArrayList<>();
        }
        return chargeOverdueES.stream().filter(e -> e.getBillId() != null).map(ChargeOverdueE::getBillId).collect(Collectors.toList());
    }

    public PageV<ReceivableBillPageV> queryCommonBillByPageField(PageF<SearchF<?>> queryF) {
        IPage page = baseBillDomainService.getCommonPage(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), getBillOverdueDetail(Global.mapperFacade.mapAsList(page.getRecords(), ReceivableBillPageV.class)));
    }

    public CheckBillTimeOverlapV checkBillTimeOverlap(CheckBillTimeOverlapF checkBillTimeOverlapF) {
        if (checkBillTimeOverlapF.getStartTime().isAfter(checkBillTimeOverlapF.getEndTime())) {
            throw BizException.throw400("账单周期开始时间不能晚于周期结束时间");
        }
        QueryWrapper<ReceivableBill> receivableBillQueryWrapper = new QueryWrapper<>();
        receivableBillQueryWrapper
                .lambda()
                .eq(ReceivableBill::getSupCpUnitId, checkBillTimeOverlapF.getSupCpUnitId())
                .eq(ReceivableBill::getRoomId, checkBillTimeOverlapF.getRoomId())
                .eq(ReceivableBill::getChargeItemId, checkBillTimeOverlapF.getChargeItemId())
                .eq(ReceivableBill::getBillType, BillTypeEnum.应收账单.getCode())
                .eq(ReceivableBill::getReversed, Const.State._0)
                .eq(ReceivableBill::getOverdue, OverdueStateEnum.无违约金.getCode())
                .ne(ReceivableBill::getState, BillStateEnum.作废.getCode())
                .ne(ReceivableBill::getRefundState, BillRefundStateEnum.已退款.getCode())
                .ne(ReceivableBill::getCarriedState, BillCarriedStateEnum.已结转.getCode())
                .isNull(ReceivableBill::getBillLabel)
                .and(
                        wrapper -> wrapper
                                .between(
                                        ReceivableBill::getStartTime,
                                        checkBillTimeOverlapF.getStartTime(),
                                        checkBillTimeOverlapF.getEndTime()
                                )
                                .or()
                                .between(
                                        ReceivableBill::getEndTime,
                                        checkBillTimeOverlapF.getStartTime(),
                                        checkBillTimeOverlapF.getEndTime())
                                .or(
                                        e -> e
                                                .lt(ReceivableBill::getStartTime, checkBillTimeOverlapF.getStartTime())
                                                .gt(ReceivableBill::getEndTime, checkBillTimeOverlapF.getEndTime())
                                )
                );
        List<ReceivableBill> receivableBills = billMapper.selectList(receivableBillQueryWrapper);
        if (CollectionUtils.isEmpty(receivableBills)) {
            return CheckBillTimeOverlapV.noOverlap();
        } else {
            ReceivableBill receivableBill = receivableBills.get(0);
            return new CheckBillTimeOverlapV(true, receivableBill.getId(), receivableBill.getBillNo(),
                    receivableBill.getRoomId(), receivableBill.getStartTime(), receivableBill.getEndTime());
        }
    }


    /**
     * 查询账单信息
     *
     * @param queryBillReq
     * @return
     */
    public PageV<BillInfoResponse> queryBillInfo(QueryBillReq queryBillReq) {
        Page<BillInfoResponse> page = Page.of(queryBillReq.getPageNum(), queryBillReq.getPageSize());
        processBillInfoQuery(queryBillReq, queryBillReq.getCommunityId());
        String receivableBillName = sharedBillAppService.getShareTableName(queryBillReq.getCommunityId(), TableNames.RECEIVABLE_BILL);
        Page<BillInfoResponse> pages = billMapper.queryBillInfo(page, queryBillReq.getCommunityId(), queryBillReq, receivableBillName, queryBillReq.getSysSouce(), queryBillReq.getAttribute());
        if (ObjectUtils.isNotEmpty(queryBillReq.getSysSouce()) && queryBillReq.getSysSouce() == 2) {
            //只有给枫行梦的有项目PJ码，其他外部系统不需要
            handleCommunityPJCodes(pages.getRecords());
        }
        return PageV.of(pages.getCurrent(), pages.getSize(), pages.getTotal(), new ArrayList<>(pages.getRecords()));
    }

    /**
     * 处理项目PJ码
     *
     * @param records
     */
    private void handleCommunityPJCodes(List<BillInfoResponse> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        //获取项目id
        List<String> communityIds = records.stream()
                .map(BillInfoResponse::getCommunityId)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(communityIds)) {
            return;
        }
        //查询项目的
        List<CommunityPJCodeV> communityPJCodes = spaceClient.getCommunityPJCodes(communityIds);
        if (CollectionUtils.isEmpty(communityPJCodes)) {
            return;
        }
        Map<String, CommunityPJCodeV> pjCodeVMap = communityPJCodes.stream()
                .collect(Collectors.toMap(CommunityPJCodeV::getCommunityId, Function.identity(), (v1, v2) -> v1));
        records.forEach(record -> {
            if (pjCodeVMap.containsKey(record.getCommunityId())) {
                record.setCommunityCode(pjCodeVMap.get(record.getCommunityId()).getCommunityCode());
            }
        });
    }

    private void processBillInfoQuery(QueryBillReq query, String communityId) {
        if (Objects.isNull(query.getSysSouce())) {
            query.setSysSouce(1);
        }
        if (CollectionUtils.isNotEmpty(query.getRoomIds())) {
            return;
        }
        SpacePermissionF spacePermissionF = new SpacePermissionF();
        spacePermissionF.setCommunityId(communityId);
        if (CollectionUtils.isNotEmpty(query.getUnitIds())) {
            spacePermissionF.setSpaceIds(query.getUnitIds());
        } else if (CollectionUtils.isNotEmpty(query.getBuildingIds())) {
            spacePermissionF.setSpaceIds(query.getBuildingIds());
        } else {
            return;
        }
        List<String> roomIds = spaceClient.selectRoomIdsBySpaceTypeIds(spacePermissionF);
        query.setRoomIds(roomIds);
    }

    public List<CheckBillTimeOverlapV> batchCheckBillTimeOverlap(CheckBillTimeOverlapF checkBillTimeOverlapF) {
        if (checkBillTimeOverlapF.getStartTime().isAfter(checkBillTimeOverlapF.getEndTime())) {
            throw BizException.throw400("账单周期开始时间不能晚于周期结束时间");
        }
        QueryWrapper<ReceivableBill> receivableBillQueryWrapper = new QueryWrapper<>();
        receivableBillQueryWrapper
                .lambda()
                .eq(ReceivableBill::getSupCpUnitId, checkBillTimeOverlapF.getSupCpUnitId())
                .in(ReceivableBill::getRoomId, checkBillTimeOverlapF.getRoomIds())
                .eq(ReceivableBill::getChargeItemId, checkBillTimeOverlapF.getChargeItemId())
                .eq(ReceivableBill::getBillType, BillTypeEnum.应收账单.getCode())
                .eq(ReceivableBill::getReversed, Const.State._0)
                .eq(ReceivableBill::getOverdue, OverdueStateEnum.无违约金.getCode())
                .ne(ReceivableBill::getState, BillStateEnum.作废.getCode())
                .ne(ReceivableBill::getRefundState, BillRefundStateEnum.已退款.getCode())
                .ne(ReceivableBill::getCarriedState, BillCarriedStateEnum.已结转.getCode())
                .isNull(ReceivableBill::getBillLabel)
                .eq(ReceivableBill::getDeleted,false)
                .and(
                        wrapper -> wrapper
                                .between(
                                        ReceivableBill::getStartTime,
                                        checkBillTimeOverlapF.getStartTime(),
                                        checkBillTimeOverlapF.getEndTime()
                                )
                                .or()
                                .between(
                                        ReceivableBill::getEndTime,
                                        checkBillTimeOverlapF.getStartTime(),
                                        checkBillTimeOverlapF.getEndTime())
                                .or(
                                        e -> e
                                                .lt(ReceivableBill::getStartTime, checkBillTimeOverlapF.getStartTime())
                                                .gt(ReceivableBill::getEndTime, checkBillTimeOverlapF.getEndTime())
                                )
                );
        List<ReceivableBill> receivableBills = billMapper.selectList(receivableBillQueryWrapper);
        if (CollectionUtils.isEmpty(receivableBills)) {
            return new ArrayList<>();
        }else {
            return receivableBills
                    .stream().map(receivableBill ->
                            new CheckBillTimeOverlapV(true, receivableBill.getId(), receivableBill.getBillNo(), receivableBill.getRoomId(), receivableBill.getStartTime(), receivableBill.getEndTime())
                    ).collect(Collectors.toList());
        }
    }

    //根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID
    public List<String> getReceivableBillIdList(String contractId,String communityId){
        return billMapper.getReceivableBillIdList(contractId,communityId);
    }
    //根据临时账单ID删除临时账单及对应合同报账单中对下-计提
    public void deleteReceivableAndHtBzd(List<String> billIdList,String communityId){
        //根据临时账单ID获取有无对下结算单计提
        List<String> zjList = billMapper.getdeleteReceivableDxList(billIdList,communityId);
        if(CollectionUtils.isNotEmpty(zjList)){
            LambdaQueryWrapper<VoucherBillDxZJ> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(VoucherBillDxZJ :: getVoucherBillNo , zjList)
                    .eq(VoucherBillDxZJ::getDeleted,0);
            List<VoucherBillDxZJ> dzjList = voucherPushBillZJMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(dzjList)){
                dzjList.forEach(dzj -> {dzj.setDeleted(1);
                    voucherPushBillZJMapper.updateById(dzj);
                });
            }
            LambdaQueryWrapper<VoucherPushBillDetailDxZJ> queryDetWrapper = new LambdaQueryWrapper<>();
            queryDetWrapper.in(VoucherPushBillDetailDxZJ :: getVoucherBillNo , zjList)
                    .eq(VoucherPushBillDetailDxZJ::getDeleted,0);
            List<VoucherPushBillDetailDxZJ> detdzjList = voucherBillDetailZJMapper.selectList(queryDetWrapper);
            if(CollectionUtils.isNotEmpty(detdzjList)){
                detdzjList.forEach(dzj -> {dzj.setDeleted(1);
                    voucherBillDetailZJMapper.updateById(dzj);
                });
            }
            billMapper.deleteReceivable(billIdList,communityId);
        }
    }

    //根据临时账单ID获取对应报账单数据
    public String getVoucherBillByReceivableId(List<String> billIdList,String communityId){
        HashMap<String,String> resultMap = new HashMap<>();
        //根据临时账单ID获取报账单数据
        List<Map<Integer, String>> zjBillList = billMapper.getVoucherBillByReceivableId(communityId, billIdList);
        String resultMessage = null;
        if(CollectionUtils.isNotEmpty(zjBillList)){

            List<String> billCodeList = new ArrayList<>();
            for (Map<Integer, String> item : zjBillList) {
                /*String billEventType = ZJTriggerEventBillTypeEnum.valueOfByCode(Integer.parseInt(ObjectUtils.toString(item.get("bill_event_type")))).getValue();
                String voucherBillNos = item.get("voucher_bill_nos");*/
                billCodeList.add(ObjectUtils.toString(item.get("voucher_bill_nos")));
            }
            resultMessage = "报账单["+ String.join(",", billCodeList)+"]";
        }
        //根据临时账单ID获取合同报账单数据
        List<Map<String, Object>> zjDxBillList = billMapper.getVoucherBillDxByReceivableId(communityId, billIdList);
        if(CollectionUtils.isNotEmpty(zjDxBillList)){
            List<String> billCodeList = new ArrayList<>();
            for (Map<String, Object> item : zjDxBillList) {
                billCodeList.add(ObjectUtils.toString(item.get("voucher_bill_nos")));
            }
            resultMap.put("合同报账单", String.join(",", billCodeList));
            if(StringUtils.isNotEmpty(resultMessage)){
                resultMessage = "计划已生成:\n"+resultMessage+"\n合同报账单:["+ String.join(",", billCodeList)+"]\n不允许删除，请删除对应账单后重试！";
            }else{
                resultMessage = "计划已生成:合同报账单["+ String.join(",", billCodeList)+"],不允许删除，请删除对应合同报账单后重试！";
            }
        }else{
            if(StringUtils.isNotEmpty(resultMessage)){
                resultMessage = "计划已生成:"+ resultMessage+",不允许删除，请删除对应报账单后重试！";
            }
        }
        return resultMessage;
    }
    //根据临时账单ID删除对应临时账单数据
    public void deleteReceivableBillById(List<String> billIdList,String communityId){
        //根据临时账单ID删除对应临时账单数据
        billMapper.deleteReceivableBillById(communityId, billIdList);
    }

    //根据临时账单ID获取报账单数据-实签
    public String getVoucherBillSq(List<String> billIdList,String communityId){

        HashMap<String,String> resultMap = new HashMap<>();
        //根据临时账单ID获取报账单数据
        List<Map<String, String>> zjBillMap = billMapper.getVoucherBillSq(communityId, billIdList);
        if(CollectionUtils.isEmpty(zjBillMap)){
            return null;
        }
        for(Map<String, String> billMap : zjBillMap){
            String key = ObjectUtils.toString(billMap.get("voucher_bill_no"));
            String value = ObjectUtils.toString(billMap.get("stageAndType"));
            String[] parts = value.split(",", 2); // 只分割成两部分
            String approveState = parts[0];
            String billEventType = parts[1];
            if(PushBillApproveStateEnum.审核中.getCode() == Integer.parseInt(approveState)){
                if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == Integer.parseInt(billEventType)){
                    return ZJTriggerEventBillTypeEnum.对下结算实签.getValue()+"["+key+"]推送审批中，请流程驳回后重试！";
                }else {
                    return ZJTriggerEventBillTypeEnum.收入确认实签.getValue()+"["+key+"]推送审批中，请流程驳回后重试！";
                }
            }else if(PushBillApproveStateEnum.已审核.getCode() == Integer.parseInt(approveState)){
                if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == Integer.parseInt(billEventType)){
                    return ZJTriggerEventBillTypeEnum.对下结算实签.getValue()+"["+key+"]已审批通过，禁止删除！";
                }else {
                    return ZJTriggerEventBillTypeEnum.收入确认实签.getValue()+"["+key+"]已审批通过，禁止删除！";
                }
            }
        }

        return null;
    }
    //根据临时账单ID获取报账单数据-实签
    public void deleteReceivableBillAndVoucher(List<ResultTemporaryChargeBillF> addTemporaryChargeBillFs){
        List<ReceivableBill> updateBillCommands = addTemporaryChargeBillFs.stream().map(updateBillF -> {
            ReceivableBill updateBillCommand = new ReceivableBill();
            Global.mapperFacade.map(updateBillF, updateBillCommand);
            return updateBillCommand;
        }).collect(Collectors.toList());
        for(ReceivableBill bill : updateBillCommands){
            billMapper.restoreBill(bill.getId(), bill.getSupCpUnitId(), bill.getTaxAmountNew());
        }
        //billMapper.restoreBill(updateBillCommands);

        List<Long> billIdList = updateBillCommands.stream().map(ReceivableBill::getId).collect(Collectors.toList());
        String voucherNo = billMapper.getVoucherBillList(billIdList,addTemporaryChargeBillFs.get(0).getSupCpUnitId());
        if(StringUtils.isNotEmpty(voucherNo)){
            voucherPushBillDxZJMapper.deleteByVoucherBIllNo(voucherNo);
            voucherBillDetailDxZJMapper.deleteByVoucherBIllNo(voucherNo);
        }
    }
}
