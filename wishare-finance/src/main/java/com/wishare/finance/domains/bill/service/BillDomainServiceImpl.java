package com.wishare.finance.domains.bill.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.bill.fo.BillDetailF;
import com.wishare.finance.apps.model.bill.fo.EditBillF;
import com.wishare.finance.apps.model.bill.fo.EditBillForBankSignF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.ApplyBatchDeductionV;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.BillOjV;
import com.wishare.finance.apps.model.bill.vo.BillStatusDetailVo;
import com.wishare.finance.apps.service.bill.BillSplitLogService;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.bill.contract.BillContractAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.*;
import com.wishare.finance.domains.bill.aggregate.approve.AdjustApproveListener;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.event.BillBatchActionEvent;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.support.BillApplyListenerFactory;
import com.wishare.finance.domains.bill.support.BillApproveListenerFactory;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.service.ChargeItemDomainService;
import com.wishare.finance.domains.conts.CommonConst;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.domains.gateway.bill.BillGatherAGateway;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.remote.fo.customer.CustomerF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.vo.customer.CustomerV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.RegexUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.enums.ErrMsgEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;

import java.math.BigDecimal;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 账单领域服务实现
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
//@Service
@Slf4j
@Service("billDomainService")
public class BillDomainServiceImpl<BR extends BillRepository<B>, B extends Bill> implements BillDomainService<B> {

    @Setter(onMethod_ = @Autowired)
    protected BR billRepository;
    @Setter(onMethod_ = @Autowired)
    protected SpacePermissionAppService spacePermissionAppService;
    @Setter(onMethod_ = @Autowired)
    protected BillApproveRepository approveRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillCarryoverRepository carryoverRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillAdjustRepository adjustRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillRefundRepository refundRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillJumpRepository jumpRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillHandRepository handRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillDeductionRepository billDeductionRepository;
    @Setter(onMethod_ = @Autowired)
    protected VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillReverseRepository reverseRepository;
    @Setter(onMethod_ = @Autowired)
    protected ChargeItemDomainService chargeItemDomainService;
    @Setter(onMethod_ = @Autowired)
    protected BillInferenceRepository billInferenceRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillAdjustRepository billAdjustRepository;

    @Setter(onMethod_ = @Autowired)
    protected GatherDetailRepository gatherDetailRepository;
    @Setter(onMethod_ = @Autowired)
    protected BillCarryoverDetailRepository billCarryoverDetailRepository;

    @Setter(onMethod_ = @Autowired)
    protected GatherBillRepository gatherBillRepository;

    @Setter(onMethod_ = @Autowired)
    protected BillContractAppService billContractAppService;

    @Setter(onMethod_ = @Autowired)
    protected PayDetailRepository payDetailRepository;

    @Setter(onMethod_ = @Autowired)
    protected PayBillRepository payBillRepository;

    @Setter(onMethod_ = @Autowired)
    protected GatherBillDomainService gatherBillDomainService;

    @Setter(onMethod_ = @Autowired)
    protected BillGatherAGateway<B> billGatherAGateway;

    @Setter(onMethod_ = @Autowired)
    protected ChargeItemRepository chargeItemRepository;

    @Setter(onMethod_ = @Autowired)
    protected InvoiceReceiptRepository invoiceReceiptRepository;

    @Setter(onMethod_ = @Autowired)
    protected InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    @Setter(onMethod_ = @Autowired)
    protected ReceivableBillRepository receivableBillRepository;

    @Setter(onMethod_ = @Autowired)
    protected ExportService exportService;

    @Setter(onMethod_ = @Autowired)
    protected SharedBillAppService sharedBillAppService;

    @Setter(onMethod_ = @Autowired)
    protected BillSplitLogService billSplitLogService;

    @Resource
    private OrgClient orgClient;

    private final String FANG_YUAN = "fangyuan";

    @Override
    public Class<B> getBillClass() {
        return (Class<B>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId) {
        return new ArrayList<>();
    }

    @Override
    public boolean saveBatch(List<AddBillCommand<B>> commands) {
        List<GatherDetail> billSettleList = new ArrayList<>();
        ArrayList<GatherBill> gatherBillList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(commands)) {
            commands.forEach(command -> {
                B bill = command.getBill();
                BillGatherDetailA<B> billSettleE = initBill(bill, command.getChargeTime(), command.getChargeStartTime(), command.getChargeEndTime(),
                        command.getSettleWay(), command.getSettleChannel(), "账单批量新增", command.getPayeePhone(), command.getPayerPhone());
                if (Objects.nonNull(billSettleE)) {
                    billSettleList.add(billSettleE.getGatherDetail());
                }
                if (Objects.nonNull(command.getApprovedFlag()) && command.getApprovedFlag()) {
                    bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                    if (bill instanceof ReceivableBill) {
                        ((ReceivableBill)bill).setIsInit(false);
                    }else if (bill instanceof TemporaryChargeBill) {
                        ((TemporaryChargeBill)bill).setIsInit(false);
                    }
                    if (Objects.nonNull(billSettleE)) {
                        gatherBillList.add(billSettleE.getGatherBill());
                    }

                }
            });
        }
        if (CollectionUtils.isNotEmpty(billSettleList)) {
            gatherDetailRepository.saveBatch(billSettleList);
        }
        if (CollectionUtils.isNotEmpty(gatherBillList)) {
            gatherBillRepository.saveBatch(gatherBillList);
        }
        List<B> bList = commands.stream().map(AddBillCommand::getBill).collect(Collectors.toList());

        // 根据 备注信息修改账单状态 并获取映射信息
        // 原始账单billNo 和拆分账单 billNo 的映射
        Map<String, List<String>> mp = new HashMap<>();
        // 拆分账单和原始账单的billNo 映射
        Map<String, String> splitMp = new HashMap<>();
        // 原始账单originId 和账单原始 billNo 的映射
        Map<String, String> originIdMap = new HashMap<>();

        List<B> originalBills = new ArrayList<>();

        // 不要合并两个foreach  先要获取到所有的originIdMap
        for (B data1 : bList) {
            String remark = data1.getRemark();
            if (remark != null && remark.contains("[ORIGIN_ID:")) {
                // 原始对象
                data1.setState(BillStateEnum.作废.getCode());
                String originId = RegexUtils.extractId(remark, "ORIGIN_ID");
                originIdMap.put(originId, data1.getBillNo());
                data1.setRemark(remark.replace("[ORIGIN_ID:" + originId + "]", "").trim());
                originalBills.add(data1);
            }
        }

        List<B> splitBills = new ArrayList<>();
        Iterator<B> iterator = bList.iterator();
        while (iterator.hasNext()) {
            B bill = iterator.next();
            String remark = bill.getRemark();
            if (remark != null && remark.contains("[GENERATED_FROM:")) {
                // 生成对象
                String originId = RegexUtils.extractId(remark, "GENERATED_FROM");
                String billNo = originIdMap.get(originId);
                if (StringUtils.isNotBlank(billNo)) {
                    List<String> billNos = mp.computeIfAbsent(billNo, k -> new ArrayList<>());
                    billNos.add(bill.getBillNo());
                }
                splitMp.put(bill.getBillNo(), billNo);
                bill.setRemark(remark.replace("[GENERATED_FROM:" + originId + "]", "").trim());
                splitBills.add(bill);
            }
        }

        // 应收账单要校验账单是否唯一，如果不唯一，拆分失败，记录日志
        // 拆分失败的原始账单编号
        Set<String> errorOriginalBillNos = new HashSet<>();
        Set<String> errorSplitBillNos = new HashSet<>();
        if (CollectionUtils.isNotEmpty(splitBills)) {
            for(B bill: splitBills){
                if (BillTypeEnum.应收账单.getCode()==bill.getType()) {
                    // 拆分账单对应的原账单已经在errorBillNos中，不再校验
                    if (splitMp.containsKey(bill.getBillNo()) &&  errorOriginalBillNos.contains(splitMp.get(bill.getBillNo()))) {
                        errorSplitBillNos.add(bill.getBillNo());
                        continue;
                    }
                    if(!uniqueImportBill(bill, bill.getSupCpUnitId())){
                        errorSplitBillNos.add(bill.getBillNo());
                        errorOriginalBillNos.add(splitMp.get(bill.getBillNo()));
                    }
                }
            }
        }

        // 记录拆分失败的列表
        billSplitLogService.saveBatch(errorOriginalBillNos);

        // 过滤掉拆分失败的账单
        bList = bList.stream().filter(b -> !errorSplitBillNos.contains(b.getBillNo())).collect(Collectors.toList());
        splitBills = splitBills.stream().filter(b -> !errorSplitBillNos.contains(b.getBillNo())).collect(Collectors.toList());
        originalBills = originalBills.stream().filter(b -> !errorOriginalBillNos.contains(b.getBillNo())).collect(Collectors.toList());
        // 修改原始状态的状态
        bList.forEach(
                bill->{
                    if (errorOriginalBillNos.contains(bill.getBillNo())) {
                        bill.setState(BillStateEnum.正常.getCode());
                    }
                }
        );

        // 导入数量比较大，在charge入口服务处单次赋予房号路径值
        billRepository.saveBatch(bList);
        //记录日志
        // 第一次生成操作记录不要生成拆分账单的操作记录
        if(CollectionUtils.isNotEmpty(splitBills)){
            bList.removeAll(splitBills);
        }
        if (CollectionUtils.isNotEmpty(bList)) {
            BizLog.normalBatch(bList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        }

        // 执行拆分操作以后 更新日志
        if (CollectionUtils.isNotEmpty(splitBills)) {
            splitBills.forEach(bill -> {
                String billNo = splitMp.get(bill.getBillNo());
                BizLog.normal(String.valueOf(bill.getId()),
                        LogContext.getOperator(), LogObject.账单, LogAction.生成,
                        new Content().option(new ContentOption(new PlainTextDataItem("由账单编号为", true)))
                                .option(new ContentOption(new PlainTextDataItem(billNo, true)))
                                .option(new ContentOption(new PlainTextDataItem("拆分生成", false))));
            });
        }

        if (CollectionUtils.isNotEmpty(originalBills)) {
            // 原账单日志更新
            originalBills.forEach(bill -> {
                String billNo = bill.getBillNo();
                List<String> data = mp.get(billNo);
                if (CollectionUtils.isEmpty(data)) {
                    return;
                }
                Content content = new Content();
                content.option(new ContentOption(new PlainTextDataItem("该账单拆分为", true)));
                for(int i = 0; i < data.size(); i++) {
                    if (i ==0){
                        content.option(new ContentOption(new PlainTextDataItem(data.get(0), true)));
                    }else{
                        content.option(new ContentOption(new PlainTextDataItem("、"+data.get(i), false)));
                    }
                }
                content.option(new ContentOption(new PlainTextDataItem("  "+ String.valueOf(data.size())+"个账单", false)));

                BizLog.normal(String.valueOf(bill.getId()),
                        LogContext.getOperator(), LogObject.账单, LogAction.拆分,
                        content);
            });
        }
        return true;
    }

    @Override
    public boolean updateBatch(List<UpdateBillCommand<B>> bills) {
        return false;
    }

    @Override
    public <T extends ImportBillDto> List<T> importBill(List<ImportBillCommand<B>> importBillCommands) {
        List<T> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(importBillCommands)) {
            List<GatherDetail> billSettles = new ArrayList<>();
            ArrayList<GatherBill> gatherBillList = new ArrayList<>();
            importBillCommands.forEach(command -> {
                try {
                    B bill = command.getBill();
                    if (bill instanceof TemporaryChargeBill) {
                        ((TemporaryChargeBill)bill).generalTaxAmount();
                    }
                    BillGatherDetailA<B> billSettleE = initBill(bill, command.getChargeTime(), command.getChargeStartTime(), command.getChargeEndTime(), command.getSettleWay(), command.getSettleChannel(), "账单导入", command.getPayeePhone(), command.getPayerPhone());
                    if (Objects.nonNull(billSettleE)) {
                        billSettles.add(billSettleE.getGatherDetail());
                    }
                    if (Objects.nonNull(command.getSettleAmount()) && command.getSettleAmount() > 0) {
                        if (Objects.nonNull(billSettleE)) {
                            gatherBillList.add(billSettleE.getGatherBill());
                        }
                    }
                    results.add((T) Global.mapperFacade.map(bill, ImportBillDto.class).setResult(true).setIndex(command.getIndex()));
                    command.setSuccess(true);
                } catch (BizException e) {
                    results.add((T) new ImportBillDto().setResult(false).setErrorMessage(e.getMessage()).setIndex(command.getIndex()));
                } catch (Exception e) {
                    log.error("账单导入异常", e);
                    results.add((T) new ImportBillDto().setResult(false).setErrorMessage("系统业务处理异常，请联系系统管理员").setIndex(command.getIndex()));
                }
            });
            List<B> bList = importBillCommands.stream().filter(ImportBillCommand::isSuccess).map(ImportBillCommand::getBill).collect(Collectors.toList());
            billRepository.saveBatch(bList);
            if (CollectionUtils.isNotEmpty(billSettles)) {
                gatherDetailRepository.saveBatch(billSettles);
            }
            if (CollectionUtils.isNotEmpty(gatherBillList)) {
                gatherBillRepository.saveBatch(gatherBillList);
            }
            //记录日志
            if (CollectionUtils.isNotEmpty(bList)) {
                BizLog.normalBatch(bList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                        LogContext.getOperator(), LogObject.账单, LogAction.导入, new Content());
            }
        }
        return results;
    }

    @Override
    public <T extends ImportBillDto> List<T> importRecordBill(List<ImportBillCommand<B>> importBillCommands) {
        List<T> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(importBillCommands)) {
            importBillCommands.forEach(command -> {
                try {
                    B bill = command.getBill();
                    results.add((T) Global.mapperFacade.map(bill, ImportBillDto.class).setResult(true).setIndex(command.getIndex()));
                    command.setSuccess(true);
                } catch (BizException e) {
                    results.add((T) new ImportBillDto().setResult(false).setErrorMessage(e.getMessage()).setIndex(command.getIndex()));
                } catch (Exception e) {
                    log.error("账单补录导入异常", e);
                    results.add((T) new ImportBillDto().setResult(false).setErrorMessage("系统业务处理异常，请联系系统管理员").setIndex(command.getIndex()));
                }
            });
            List<B> bList = importBillCommands.stream().filter(ImportBillCommand::isSuccess).map(ImportBillCommand::getBill).collect(Collectors.toList());
            //记录日志
            if (CollectionUtils.isNotEmpty(bList)) {
                BizLog.normalBatch(bList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                        LogContext.getOperator(), LogObject.账单, LogAction.补录导入, new Content());
            }
        }
        return results;
    }

    @Override
    public boolean save(AddBillCommand<B> command) {
        B bill = command.getBill();
        bill.setSupCpUnitId(bill.getCommunityId());
        bill.setSupCpUnitName(bill.getCommunityName());
        bill.setCpUnitId(bill.getRoomId());
        bill.setCpUnitName(bill.getRoomName());
        BillGatherDetailA<B> billSettleE = initBill(bill, command.getChargeTime(), command.getChargeStartTime(), command.getChargeEndTime(), command.getSettleWay(), command.getSettleChannel(), "账单新增", command.getPayeePhone(), command.getPayerPhone());
        if (Objects.nonNull(command.getApprovedFlag()) && command.getApprovedFlag()) {
            bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            if (bill instanceof ReceivableBill) {
                ((ReceivableBill)bill).setIsInit(false);
                ((ReceivableBill)bill).setPath(spacePermissionAppService.getSpacePath(bill.getRoomId()));
            }else if (bill instanceof TemporaryChargeBill) {
                ((TemporaryChargeBill)bill).setIsInit(false);
                ((TemporaryChargeBill)bill).setPath(spacePermissionAppService.getSpacePath(bill.getRoomId()));
            }else if (bill instanceof AdvanceBill){
                ((AdvanceBill)bill).setPath(spacePermissionAppService.getSpacePath(bill.getRoomId()));
            }
            if (FANG_YUAN.equals(EnvData.config)) {
                bill.setBillCostType(BillCostTypeEnum.当期应收.getCode());
            }
            if (Objects.nonNull(billSettleE)) {
                gatherBillRepository.save(billSettleE.getGatherBill());
            }
        }
        if (billRepository.save(bill) && Objects.nonNull(billSettleE)) {
            gatherDetailRepository.save(billSettleE.getGatherDetail());
        }
        //记录日志
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        return true;
    }

    /**
     * 获取审核记录
     * @param billApplyInfoF billApplyInfoF
     * @return {@link BillApproveV}
     */
    @Override
    public List<BillApproveV> getApplyInfo(BillApplyInfoF billApplyInfoF){
        return Global.mapperFacade.mapAsList(approveRepository.getApproveInfoList(billApplyInfoF), BillApproveV.class);
    }


    @Override
    public boolean approve(ApproveCommand command) {

        B bill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", command.getBillId())
                .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//        B bill = billRepository.getById(command.getBillId());
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        BillApproveE approvingE = approveRepository.getApprovingAByBillId(command.getBillId(), command.getSupCpUnitId());
        BillApproveA<B> approvingA = new BillApproveA<>(bill).init(approvingE);
        approvingA.setGmtModify(Optional.ofNullable(command.getOperateTime()).orElse(LocalDateTime.now()));
        if (Objects.isNull(approvingE)) {
            ErrorAssertUtil.isTrueThrow300(Objects.isNull(bill.isInit()) || bill.isInit(),
                    ErrorMessage.BILL_APPROVE_NOT_SUPPORT);
            approvingA.initDefaultApprove(); //默认生成审核
            approveRepository.save(approvingA);
        }
        // 允许对负数托收账单进行作废操作 给与功能
        Optional.of(command.getOperateType()).filter(BillApproveOperateTypeEnum.作废::equalsByCode)
                .ifPresent(a->bill.setNegativeCommission(Const.State._1));
        approvingA.setApprovedAction(command.getApprovedAction());
        // 没有指定费项类型的默认为当前账单的费项类型
        approvingA.setChargeItemId(Objects.isNull(command.getChargeItemId()) ? bill.getChargeItemId()
                : command.getChargeItemId());
        approvingA.setChargeItemName(StringUtils.isBlank(command.getChargeItemName()) ? bill.getChargeItemName() :
                command.getChargeItemName());
        approvingA.initJumpState(approvingA.getOperateType(), command.getApproveState());
        boolean approved = approvingA.approve(BillApproveStateEnum.valueOfByCode(command.getApproveState()), command.getRejectReason(),
                BillApproveListenerFactory.getListener(BillApproveOperateTypeEnum.valueOfByCode(approvingA.getOperateType())));
        if (approved) {
            this.costType(approvingA.getBill(),command.getOperateType(),bill.getType());
            billRepository.update(approvingA.getBill(), new UpdateWrapper<B>().eq("id", approvingA.getBill().getId()).eq("sup_cp_unit_id", approvingA.getBill().getSupCpUnitId()));
            approveRepository.update(approvingA, new UpdateWrapper<BillApproveE>().eq("id", approvingA.getId()).eq("sup_cp_unit_id", approvingA.getSupCpUnitId()));
            if (BillApproveStateEnum.已审核.equalsByCode(command.getApproveState())) {
                handleOtherMq(bill, approvingA.getOperateType());
            }
        }
        return approved;
    }

    private void costType(B bill,Integer operateType,Integer type) {
        if (FANG_YUAN.equals(EnvData.config) && BillTypeEnum.应收账单.getCode() == type
                    && BillApproveOperateTypeEnum.结转.getCode() == operateType) {
            if (Objects.isNull(bill.getBillCostType()) && Objects.nonNull(bill.getAccountDate())) {
                int nowYear = LocalDate.now().getYear();
                int billYear = bill.getAccountDate().getYear();
                if(nowYear > billYear){
                    bill.setBillCostType(BillCostTypeEnum.预收款项.getCode());
                }
                if(nowYear == billYear){
                     int quarterNow = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
                     int quarterBill = (bill.getAccountDate().getMonthValue() - 1) / 3 + 1;
                     if (quarterBill < quarterNow) {
                         bill.setBillCostType(BillCostTypeEnum.预收款项.getCode());
                     }
                }
            }
        }
    }

    /**
     * 处理账单相关其他mq相关业务
     *
     * @param bill        账单id
     * @param operateType 操作类型
     */
    private void handleOtherMq(B bill, Integer operateType) {
        BillApproveOperateTypeEnum billApproveOperateTypeEnum = BillApproveOperateTypeEnum.valueOfByCode(operateType);
        if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.作废.getCode())) {
            log.info("MQ发送账单作废：{}", bill.getId());
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.INVALIDED)
                    .payload(BillActionEvent.invalid(bill.getId(), bill.getType(), "作废", bill.getTenantId()))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.冲销.getCode())) {
            log.info("MQ发送账单冲销：{}", bill.getId());
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.REVERSED)
                    .payload(BillActionEvent.reverse(bill.getId(), bill.getType(), "冲销", bill.getSupCpUnitId()))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.生成审核.getCode())) {
            log.info("MQ发送账单审核(计提)：{}", bill.getId());
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.APPROVED)
                    .payload(BillActionEvent.approved(bill.getId(), bill.getType(), "审核(计提)", bill.getSupCpUnitId()))
                    .build());
        }
    }

    @Override
    public List<Long> approveBatch(BatchApproveBillCommand command, Integer billType) {
        List<B> billList;
        List<Long> billIds = Optional.ofNullable(command.getBillIds()).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(billIds)) {
            PageF<SearchF<?>> query = command.getQuery();
            ErrorAssertUtil.notNullThrow300(query, ErrorMessage.BILL_BATCH_QUERY_ERROR);
            //根据查询条件获取账单(只过滤出初始审核的账单)
            //大数据量进行分页处理，防止内存溢出
            QueryWrapper<?> wrapper = billRepository.getWrapper(query);
            query.getConditions().getSpecialMap().put("operate_type", List.of(0));
            QueryWrapper<?> approveWrapper = getApproveWrapper(query, wrapper);
            //应收账单和临时账单在同一张表，需要根据类型查询不同数据
            if (BillTypeEnum.应收账单.equalsByCode(billType) || BillTypeEnum.临时收费账单.equalsByCode(billType)) {
                approveWrapper.eq("b.bill_type", billType);
            }
            approveWrapper.groupBy("b.id");
            Page<Object> page = Page.of(1, 1000);
            IPage<B> billPage = billRepository.listByInitialBill(page, approveWrapper);
            if (billPage.getPages() > billPage.getCurrent()) {
                for (long i = 2; i <= billPage.getPages(); i++) {
                    List<B> records = billRepository.listByInitialBill(page, approveWrapper).getRecords();
                    billIds.addAll(
                            Optional
                                    .ofNullable(records)
                                    .orElse(new ArrayList<>()).stream().map(B::getId).collect(Collectors.toList())
                    );
                    doApprovedBatch(command.getApproveState(), command.getRejectReason(), billRepository.listByInitialBill(page, approveWrapper).getRecords());
                }
            }
            doApprovedBatch(command.getApproveState(), command.getRejectReason(), billPage.getRecords());
            billIds.addAll(
                    Optional
                            .ofNullable(billPage.getRecords())
                            .orElse(new ArrayList<>()).stream().map(B::getId).collect(Collectors.toList())
            );
        } else {
            billList = billRepository.list(new QueryWrapper<B>().in("id", billIds).eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            billList = billRepository.listByIds(billIds);
            doApprovedBatch(command.getApproveState(), command.getRejectReason(), billList);
        }
        return billIds;
    }

    /**
     * 处理账单相关其他mq相关业务
     *
     * @param billList    账单id
     * @param operateType 操作类型
     */
    private void batchHandleOtherMq(List<B> billList, Integer operateType, String supCpUnitId) {
        if (billList.isEmpty()) {
            return;
        }
        List<Long> billIds = billList.stream().map(Bill::getId).collect(Collectors.toList());
        BillApproveOperateTypeEnum billApproveOperateTypeEnum = BillApproveOperateTypeEnum.valueOfByCode(operateType);
        if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.作废.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.INVALIDED_BATCH)
                    .payload(BillBatchActionEvent.invalid(billIds, billList.get(0).getType(), "作废",
                            billList.get(0).getTenantId(), supCpUnitId))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.冲销.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.REVERSED_BATCH)
                    .payload(BillBatchActionEvent.reverse(billIds, billList.get(0).getType(), "冲销", supCpUnitId))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.调整.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.ADJUSTED_BATCH)
                    .payload(BillBatchActionEvent.adjust(billIds, billList.get(0).getType(), "调整", supCpUnitId))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.生成审核.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.APPROVED_BATCH)
                    .payload(BillBatchActionEvent.approve(billIds, billList.get(0).getType(), "审核(计提)", supCpUnitId))
                    .build());
        }
    }

    @Override
    public Long apply(BillApplyCommand applyInfo) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", applyInfo.getBillId()).eq(StringUtils.isNotBlank(applyInfo.getSupCpUnitId()), "sup_cp_unit_id", applyInfo.getSupCpUnitId()));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId,applyInfo.getBillId()).eq(B::getSupCpUnitId,applyInfo.getSupCpUnitId()));
//        B bill = billRepository.getById(applyInfo.getBillId());
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        //获取审核中的记录
        BillApproveE approvingE = approveRepository.getApprovingAByBillId(applyInfo.getBillId(), applyInfo.getSupCpUnitId());
        if (BillApproveStateEnum.待审核.equalsByCode(bill.getApprovedState())) {
            //账单未审核进行调整，判断是否审核过（处理反审的情况，将上次审核状态设置为已审核）
            if (approveRepository.hasApproved(applyInfo.getBillId(), applyInfo.getSupCpUnitId())) {
                applyInfo.setLastApprovedState(BillApproveStateEnum.已审核.getCode());
            }
        }
        // 允许对负数托收账单进行作废操作 给与功能
        Optional.of(applyInfo.getApproveOperateType().getCode()).filter(BillApproveOperateTypeEnum.作废::equalsByCode)
                .ifPresent(a->bill.setNegativeCommission(Const.State._1));
        BillApproveA<B> approvingA = new BillApproveA<>(bill).init(approvingE);
        approvingA.setOperationId(applyInfo.getOperationId());
        approvingA.apply(applyInfo, BillApplyListenerFactory.getListener(applyInfo.getApproveOperateType()));
        billRepository.update(approvingA.getBill(), new UpdateWrapper<B>().eq("id", approvingA.getBill().getId()).eq(StringUtils.isNotBlank(applyInfo.getSupCpUnitId()), "sup_cp_unit_id", applyInfo.getSupCpUnitId()));
//        billRepository.updateById(approvingA.getBill());
        boolean applyRes = approveRepository.save(approvingA);
        if (applyRes) {
            if (applyInfo.getDetail() instanceof BillCarryoverE) {
                carryoverRepository.save((BillCarryoverE) applyInfo.getDetail());
            } else if (applyInfo.getDetail() instanceof BillAdjustE) {
                adjustRepository.save((BillAdjustE) applyInfo.getDetail());
            } else if (applyInfo.getDetail() instanceof BillRefundE) {
                refundRepository.save((BillRefundE) applyInfo.getDetail());
            }
        }
        return approvingA.getApproveE().getId();
    }

    @Override
    public Long updateApply(Long billApproveId, String outApproveId, String supCpUnitId) {
        BillApproveE billApproveE =
                approveRepository.getOne(
                        new QueryWrapper<BillApproveE>().eq("sup_cp_unit_id", supCpUnitId)
                                .eq("id", billApproveId)
                );
        billApproveE.setOutApproveId(outApproveId);
        billApproveE.setApproveType(1);//审核类型： 0内部审核，1外部审核
        approveRepository.update(billApproveE, new UpdateWrapper<BillApproveE>()
                .eq("sup_cp_unit_id", supCpUnitId)
                .eq("id", billApproveId));
        return billApproveE.getId();
    }

    @Override
    public Boolean updateBatchApplyByIds(List<Long> billApproveIds, String outApproveId, String supCpUnitId) {
        return approveRepository.update(
                new LambdaUpdateWrapper<BillApproveE>()
                        .in(BillApproveE::getId,billApproveIds)
                        .eq(BillApproveE::getSupCpUnitId, supCpUnitId)
                        .set(BillApproveE::getOutApproveId,outApproveId)
                        .set(BillApproveE::getApproveType,1)
        );
    }


    public List<BillApproveE> outApprove(String outApproveId, String supCpUnitId) {
        return approveRepository.outApprove(outApproveId, supCpUnitId);
    }

    @Override
    public List<BillApproveE> approveHistory(List<Long> billIds, String outApproveId, String supCpUnitId) {
        return approveRepository.approveHistory(billIds, outApproveId, supCpUnitId);
    }

    @Override
    @Transactional
    public boolean applyBatch(BillApplyBatchCommand<?> billApplyBatchCommand) {
        ErrorAssertUtil.isTrueThrow402(!BillApproveOperateTypeEnum.结转
                .equals(billApplyBatchCommand.getApproveOperateType()));
        if (BillApproveOperateTypeEnum.作废.equals(billApplyBatchCommand.getApproveOperateType())) {
            billApplyBatchCommand.setReason(StringUtils.isNotBlank(billApplyBatchCommand.getReason()) ?
                    billApplyBatchCommand.getReason() :"账单作废");
        }
        List<Long> billIds = billApplyBatchCommand.getBillIds();
        List<B> bills = billRepository.list(new QueryWrapper<B>().in("id", billIds).eq(StringUtils.isNotBlank(billApplyBatchCommand.getSupCpUnitId()), "sup_cp_unit_id", billApplyBatchCommand.getSupCpUnitId()));
//        List<B> bills = billRepository.listByIds(billIds);
        ErrorAssertUtil.notEmptyThrow400(bills, ErrorMessage.BILL_NOT_EXIST);
        List<BillApproveE> billApproveList = approveRepository.getApprovingByBillIdList(billIds, billApplyBatchCommand.getSupCpUnitId());
        Map<Long, List<BillApproveE>> collect = billApproveList.stream().collect(Collectors.groupingBy(BillApproveE::getBillId));
        Map<Long, Object> detailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(billApplyBatchCommand.getDetails())) {
            detailMap = billApplyBatchCommand.getDetails().stream()
                    .collect(Collectors.toMap(BillDetailBase::getBillId, Function.identity()));
        }
        ArrayList<BillApproveE> approveSaveList = new ArrayList<>();
        List<String> bids = new ArrayList<>();
        for (B bill : bills) {
            List<BillApproveE> approveList = collect.get(bill.getId());
            // 允许对负数托收账单进行作废操作 给与功能
            Optional.of(billApplyBatchCommand.getApproveOperateType().getCode()).filter(BillApproveOperateTypeEnum.作废::equalsByCode)
                    .ifPresent(a->bill.setNegativeCommission(Const.State._1));
            BillApproveA<B> approvingA = new BillApproveA<>(bill).init(CollectionUtils.isNotEmpty(approveList) ?
                    approveList.get(0) : null);
            BillApplyCommand applyCommand = new BillApplyCommand(bill.getId(),
                    billApplyBatchCommand.getReason(),
                    billApplyBatchCommand.getApproveOperateType(),
                    billApplyBatchCommand.getOutApproveId(), null,
                    detailMap.get(bill.getId()),
                    billApplyBatchCommand.getSupCpUnitId(),
                    billApplyBatchCommand.getOperationRemark());
            if (BillApproveStateEnum.待审核.equalsByCode(bill.getApprovedState())) {
                //账单未审核进行调整，判断是否审核过（处理反审的情况，将上次审核状态设置为已审核）
                if (approveRepository.hasApproved(bill.getId(), bill.getSupCpUnitId())) {
                    applyCommand.setLastApprovedState(BillApproveStateEnum.已审核.getCode());
                }
            }
            approvingA.apply(applyCommand, BillApplyListenerFactory.getListener(applyCommand.getApproveOperateType()));
            approvingA.setSupCpUnitId(billApplyBatchCommand.getSupCpUnitId());
            approvingA.setOperationId(billApplyBatchCommand.getOperationId());
            approveSaveList.add(approvingA.getApproveE());
            bids.add(String.valueOf(bill.getId()));
        }
        for (B bill : bills) {
            billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(billApplyBatchCommand.getSupCpUnitId()), "sup_cp_unit_id", billApplyBatchCommand.getSupCpUnitId()));
        }
//        billRepository.updateBatchById(bills);
        boolean applyRes = approveRepository.saveBatch(approveSaveList);
        List<?> details = billApplyBatchCommand.getDetails();
        if (applyRes && CollectionUtils.isNotEmpty(details)) {
            Object fist = details.get(0);
            if (fist instanceof BillAdjustE) {
                adjustRepository.saveBatch((List<BillAdjustE>) details);
            } else if (fist instanceof BillRefundE) {
                refundRepository.saveBatch((List<BillRefundE>) details);
            } else if (fist instanceof BillJumpE) {
                jumpRepository.saveBatch((List<BillJumpE>) details);
            }
        }
        return true;
    }

    @Override
    public boolean delete(Long billId, String supCpUnitId) {
        B bill = null;
        if (StringUtils.isNotBlank(supCpUnitId)) {
            bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(BillSharedingColumn.应收账单.getColumnName(), supCpUnitId));
        } else {
            bill = billRepository.getById(billId);
        }

        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        if (bill.getAppNumber().equals(AppNumberEnum.收费系统.getValue())) {
            //校验账单是否反审核
            List<BillApproveE> billApproveList = approveRepository.queryApprovedRecord(List.of(billId), bill.getSupCpUnitId());
            ErrorAssertUtil.isTrueThrow300(CollectionUtils.isEmpty(billApproveList), ErrorMessage.BILL_DELETE_ERROR);
        }
        bill.delete();
        //删除关联的收款单和收款明细
        invalidGatherInfos(bill);
        return billRepository.update(new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId).set("deleted", bill.getDeleted()));
//        return billRepository.removeById(billId);
    }

    /**
     * 将账单名下的收款单和明细进行删除
     */
    private void invalidGatherInfos(B bill) {
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bill.getId(), bill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().eq("rec_bill_no",bill.getBillNo()).set("deleted",1)
                    .eq("sup_cp_unit_id",bill.getSupCpUnitId()));
            List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).distinct().collect(Collectors.toList());
            List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds, bill.getSupCpUnitId());
            for (GatherBill gatherBill1 : gatherBillList) {
                List<GatherDetail> gatherDetail1s = gatherDetailRepository.getByGatherBillId(gatherBill1.getId(), gatherBill1.getSupCpUnitId());
                if (CollectionUtils.isEmpty(gatherDetail1s)){
                    gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",gatherBill1.getId()).set("deleted",1)
                            .eq("sup_cp_unit_id",gatherBill1.getSupCpUnitId()));
                }
            }
        }
    }

    @Override
    public BillBatchResultDto deleteBatch(DeleteBatchBillCommand command, Integer billType) {
        List<B> billList;
        BillBatchResultDto billBatchResultDto = new BillBatchResultDto();
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            ErrorAssertUtil.notNullThrow300(command.getQuery(), ErrorMessage.BILL_BATCH_QUERY_ERROR);
            //根据查询条件获取账单
            //大数据量的时候需要额外处理 TODO
            billList = billRepository.listByPageSearch(command.getQuery(), billType);
        } else {
            billList = billRepository.list(new QueryWrapper<B>().in("id", billIds).eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            billList = billRepository.list(new LambdaQueryWrapper<B>().eq(B::getId,billIds).eq(B::getSupCpUnitId,command.getSupCpUnitId()));
//            billList = billRepository.listByIds(billIds);
        }
        if (CollectionUtils.isNotEmpty(billList)) {
            doDeletedBatch(billList, billBatchResultDto, command.getSupCpUnitId());
        } else {
            billBatchResultDto.setFailCount(billIds.size());
        }
        return billBatchResultDto;
    }

    /**
     * 冲销回退处理
     * @param bill 冲销账单
     * @param backAmount 冲销收款明细金额
     * @param billCarryoverId  结转关联表ID
     */
    public void reverseCarryoverBill(B bill,Long backAmount,Long billCarryoverId) {
        BillCarryoverDetailRepository billCarryoverDetailRepository = Global.ac.getBean(BillCarryoverDetailRepository.class);
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        BillCarryoverRepository billCarryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        BillAdjustRepository billAdjustRepository = Global.ac.getBean(BillAdjustRepository.class);
        List<BillCarryoverDetailE> list = new ArrayList<>();
        // 获取该账单被结转的记录数据
        if (Objects.nonNull(billCarryoverId)){
            BillCarryoverE carryoverE = billCarryoverRepository.getById(billCarryoverId);
            if (Objects.isNull(carryoverE)){return;}
            list = billCarryoverDetailRepository.list(new QueryWrapper<BillCarryoverDetailE>().eq(
                            "deleted", DataDeletedEnum.NORMAL.getCode()).eq("reversed",DataDeletedEnum.NORMAL.getCode())
                    .in("bill_carryover_id", billCarryoverId)
                    .eq("target_bill_id",bill.getId())
                    .eq("back", DataDeletedEnum.NORMAL.getCode())
                    .orderByDesc("carryover_time")
            );
        }else {
            list = billCarryoverDetailRepository.list(new QueryWrapper<BillCarryoverDetailE>().eq(
                            "deleted", DataDeletedEnum.NORMAL.getCode()).eq("reversed",DataDeletedEnum.NORMAL.getCode())
                    .eq("target_bill_id", bill.getId())
                    .eq("back", DataDeletedEnum.NORMAL.getCode())
                    .orderByDesc("carryover_time"));
        }


        if (CollectionUtils.isEmpty(list)){
            return;
        }
        // 获取账单自身总共应回退金额
        long sum = list.stream().mapToLong(BillCarryoverDetailE::getCarryoverAmount).sum();
        // 判断获取最大可退金额
        long canBackAmount = backAmount == null ? Math.min(sum,bill.getActualSettleAmount())
                : Math.min(Math.min(sum,backAmount),bill.getActualSettleAmount()) ;
        for (BillCarryoverDetailE carryoverDetailE : list) {
            // 可回退金额用完
            if (canBackAmount <= 0){
                break;
            }
            // 获取被结转的源账单
            BillRepository<B> billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(carryoverDetailE.getBillType()));
            B carryoverBill = billRepository.getOne(new QueryWrapper<B>().eq("id", carryoverDetailE.getCarriedBillId()).eq("sup_cp_unit_id", bill.getSupCpUnitId())
                    .eq("deleted", DataDeletedEnum.NORMAL.getCode()));

            // 判断源账单状态是否可被回退
            if (Objects.isNull(carryoverBill)){
                continue;
            }
            if (carryoverBill.getState().equals(BillStateEnum.作废.getCode()) || carryoverBill.getReversed().equals(BillReverseStateEnum.已冲销.getCode())){
                continue;
            }

            // 回退金额
            Long carryoverAmount = carryoverDetailE.getCarryoverAmount() >= canBackAmount ? canBackAmount : carryoverDetailE.getCarryoverAmount();
            Long totalAmount = carryoverBill.getTotalAmount();

            // 如果源账单实收加上回退金额加上减免金额 超过账单金额时，对应也调大账单金额，生成调整记录
            carryoverBill.setCarriedAmount(carryoverBill.getCarriedAmount() - carryoverAmount);
            long maxAmount = carryoverBill.getActualSettleAmount() + carryoverBill.getDeductibleAmount() + carryoverBill.getDiscountAmount();
            if (maxAmount > totalAmount){
                // 调高原账单金额
                carryoverBill.setTotalAmount(maxAmount);
                // 添加调整明细
                addAjustRecord(billAdjustRepository, carryoverDetailE, carryoverBill, maxAmount-totalAmount, totalAmount);
                // 日志记录
                BizLog.normal(String.valueOf(carryoverBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                        new Content().option(new ContentOption<>(new PlainTextDataItem("审核内容：", true)))
                                .option(new ContentOption<>(new PlainTextDataItem("调整方式: ", false)))
                                .option(new ContentOption<>(new PlainTextDataItem(BillAdjustWayEnum.valueOfByCode(Const.State._2).getValue(), true), OptionStyle.normal())));
            }
            // 重置应收金额
            carryoverBill.resetReceivableAmount();
            // 更新结转状态
            carryoverBill.resetCarriedState();
            // 更新结算状态
            carryoverBill.resetSettleState();

            // 更新退款状态
            if (BillRefundStateEnum.已退款.getCode() == carryoverBill.getRefundState()) {
                carryoverBill.setRefundState(BillRefundStateEnum.部分退款.getCode());
            }
            // 更新账单收款单已结转金额回退
            fixGatherInfos(bill, gatherBillRepository, gatherDetailRepository, carryoverBill, carryoverAmount);

            // 给原账单生成一条反向结转记录
            BillCarryoverE entity = new BillCarryoverE();
            entity.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CARRYOVER));
            entity.setBillType(carryoverDetailE.getBillType());
            entity.setCarriedBillId(carryoverDetailE.getCarriedBillId());
            entity.setCarriedBillNo(carryoverDetailE.getCarriedBillNo());
            entity.setCarryoverAmount(-carryoverAmount);
            entity.setCarryRule("冲销反向结转");
            entity.setCarryoverType(carryoverDetailE.getCarryoverType());
            entity.setApproveTime(LocalDateTime.now());
            entity.setCarryoverTime(LocalDateTime.now());
            entity.setCarryoverDetail(List.of(new CarryoverDetail().setTargetBillId(carryoverDetailE.getTargetBillId()).setTargetBillNo(carryoverDetailE.getTargetBillNo())
                    .setCarryoverAmount(-carryoverAmount).setActualCarryoverAmount(-carryoverAmount)));
            entity.setRemark("冲销生成反向结转记录");
            entity.setState(Const.State._2);
            billCarryoverRepository.save(entity);

            // 更新原账单信息
            billRepository.update(carryoverBill,new UpdateWrapper<B>().eq("id",carryoverBill.getId()).eq("id", carryoverDetailE.getCarriedBillId()).eq("sup_cp_unit_id", bill.getSupCpUnitId()));

            // 更新回退状态
            carryoverDetailE.setBack(Const.State._1);
            billCarryoverDetailRepository.update(carryoverDetailE,new QueryWrapper<BillCarryoverDetailE>().eq("id",carryoverDetailE.getId()));

            // 重置可回退金额
            canBackAmount = canBackAmount - carryoverAmount;
        }

    }

    /**
     * 账单冲销涉及添加调整明细
     * @param billAdjustRepository billAdjustRepository
     * @param carryoverDetailE 结转明细详情
     * @param carryoverBill 原账单
     * @param carryoverAmount 调整金额
     * @param totalAmount 原账单金额
     */
    private void addAjustRecord(BillAdjustRepository billAdjustRepository, BillCarryoverDetailE carryoverDetailE, B carryoverBill, Long carryoverAmount, Long totalAmount) {
        BillAdjustE billAdjustE = new BillAdjustE();
        billAdjustE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_ADJUST));
        billAdjustE.setBillId(carryoverBill.getId());
        billAdjustE.setBillType(carryoverDetailE.getBillType());
        billAdjustE.setAdjustWay(BillAdjustWayEnum.RECEIVABLE_AMOUNT.getCode());
        billAdjustE.setDeductionMethod(DeductionMethodEnum.不减免.getCode());
        billAdjustE.setContent("因账单冲销反结转:通过:调整-账单金额，调高"+ BigDecimal.valueOf(carryoverAmount).divide(Const.BIG_DECIMAL_HUNDRED,2, RoundingMode.HALF_UP) +"元");
        billAdjustE.setReason(99);
        billAdjustE.setBillAmount(totalAmount);
        billAdjustE.setAdjustAmount(carryoverAmount);
        billAdjustE.setAdjustType(BillAdjustTypeEnum.调高.getCode());
        billAdjustE.setState(Const.State._2);
        billAdjustE.setApproveTime(LocalDateTime.now());
        billAdjustE.setAdjustTime(LocalDateTime.now());
        billAdjustE.setFileVos(new ArrayList<>());
        billAdjustE.setInferenceState(Const.State._0);
        billAdjustE.setIsExact(Const.State._2);
        billAdjustRepository.save(billAdjustE);
    }

    /**
     * 修改冲销后源账单收款单数据
     * @param bill                      冲销账单
     * @param gatherBillRepository      gatherBillRepository
     * @param gatherDetailRepository    gatherDetailRepository
     * @param carryoverBill             源账单
     * @param carryoverAmount           回退金额
     */
    private void fixGatherInfos(B bill, GatherBillRepository gatherBillRepository, GatherDetailRepository gatherDetailRepository, B carryoverBill, Long carryoverAmount) {
        List<GatherDetail> gatherDetailList = gatherDetailRepository.list(new QueryWrapper<GatherDetail>().eq("rec_bill_id", carryoverBill.getId())
                .eq("available", Const.State._0)
                .eq("sup_cp_unit_id", bill.getSupCpUnitId())
                .eq("deleted", DataDeletedEnum.NORMAL.getCode()));
        Long carryoverSumAmount = carryoverAmount;
        for (GatherDetail gatherDetail : gatherDetailList) {
            Long carriedAmount = gatherDetail.getCarriedAmount();
            GatherBill one = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id", gatherDetail.getGatherBillId())
                    .eq("sup_cp_unit_id", bill.getSupCpUnitId()).eq("deleted", DataDeletedEnum.NORMAL.getCode())
            );
            if (carriedAmount >= carryoverSumAmount){
                gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount() - carryoverSumAmount);
                // 将对应的gatherBill中的结转金额减少
                one.setCarriedAmount(one.getCarriedAmount() - carryoverSumAmount);
                gatherBillRepository.update(one,new UpdateWrapper<GatherBill>().eq("id",one.getId()).eq("sup_cp_unit_id", bill.getSupCpUnitId()));
                break;
            }else {
                carryoverSumAmount = carryoverSumAmount - gatherDetail.getCarriedAmount();
                // 将对应的gatherBill中的结转金额减少
                one.setCarriedAmount(one.getCarriedAmount() - gatherDetail.getCarriedAmount());
                gatherDetail.setCarriedAmount(0L);
                gatherBillRepository.update(one,new UpdateWrapper<GatherBill>().eq("id",one.getId()).eq("sup_cp_unit_id", bill.getSupCpUnitId()));
            }
        }
        // 更新收款详情单数据
        for (GatherDetail gatherDetail : gatherDetailList) {
            gatherDetailRepository.update(gatherDetail,new QueryWrapper<GatherDetail>()
                    .eq("id",gatherDetail.getId())
                    .eq("sup_cp_unit_id",gatherDetail.getSupCpUnitId()));
        }
    }

    @Override
    public boolean deapprove(Long billId, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId,billId).eq(StringUtils.isNotBlank(supCpUnitId),B::getSupCpUnitId,supCpUnitId));
//        B bill = billRepository.getById(billId);
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        bill.deapprove();
        boolean result = billRepository.update(bill, new UpdateWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        boolean result = billRepository.updateById(bill);
        if (result) {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.反审核, new Content());
        }
        sendMqForAccountHand(bill);
        return result;
    }

    @Async
    public void sendMqForAccountHand(B bill) {
        //反审核,发mq同步去修改交账列表状态
        log.info("账单信息：{}", JSON.toJSONString(bill));
        EventLifecycle.apply(EventMessage.builder()
                .headers("action", BillAction.REVERSE_APPROVED)
                .payload(BillActionEvent.reverseApproved(bill.getId(), bill.getType(), "反审核",bill.getSupCpUnitId()))
                .build());
    }

    @Override
    public boolean freeze(Long bid, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", bid).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId,bid).eq(StringUtils.isNotBlank(supCpUnitId),B::getSupCpUnitId,supCpUnitId));
//        B bill = billRepository.getById(bid);
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        bill.freeze();
        boolean result = billRepository.update(bill, new UpdateWrapper<B>().eq("id", bid).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        boolean result = billRepository.updateById(bill);
        if (result) {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.冻结, new Content());
        }
        return result;
    }

    @Override
    public boolean freezeBatch(FreezeBatchBillCommand command, Integer billType) {
        List<B> billList = null;
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            ErrorAssertUtil.notNullThrow300(command.getQuery(), ErrorMessage.BILL_BATCH_QUERY_ERROR);
            //根据查询条件获取账单
            //大数据量的时候需要额外处理 TODO
            billList = billRepository.listByPageSearch(command.getQuery(), billType);
        } else {
            billList = billRepository.list(new QueryWrapper<B>().in("id", billIds).eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            billList = billRepository.listByIds(billIds);
        }
        if (CollectionUtils.isNotEmpty(billList)) {
            billList.forEach(B::freeze);
        }
        boolean result = true;
        for (B b : billList) {
            result = billRepository.update(b, new UpdateWrapper<B>().eq("id", b.getId()).eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
        }
//        boolean result = billRepository.updateBatchById(billList);
        if (result) {
            BizLog.initiateBatch(billList.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.冻结, new Content());
        }


        return result;
    }

    @Override
    public boolean freezeBatchAddReason(FreezeBatchBillCommand command, Integer billType, Integer freezeType) {
        log.info("批量冻结参数：{}", JSON.toJSONString(command));
        List<B> billList ;
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            ErrorAssertUtil.notNullThrow300(command.getQuery(), ErrorMessage.BILL_BATCH_QUERY_ERROR);
            //根据查询条件获取账单
            //大数据量的时候需要额外处理 TODO
            billList = billRepository.listByPageSearch(command.getQuery(), billType);
        } else {
            billList = billRepository.list(new QueryWrapper<B>()
                    .in("id", billIds)
                    .eq("sup_cp_unit_id", command.getSupCpUnitId()));
        }
        if (CollectionUtils.isNotEmpty(billList)) {
            billList.forEach(e -> e.freezeBatchAddReason(freezeType));
        }
        billRepository. batchFreezeBillByIds(billList, freezeType, command.getSupCpUnitId());
        BizLog.initiateBatch(billList.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.冻结, new Content());
        return true;
    }

    @Override
    public boolean deapproveBatch() {
        return false;
    }

    @Override
    public boolean handAccount() {
        return false;
    }

    @Override
    public boolean onAccount(Long billId, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getById(billId);
        bill.onAccount();
        boolean result = billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        boolean result = billRepository.updateById(bill);
        if (result) {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.挂账, new Content());
        }
        return true;
    }

    @Override
    public boolean writeOff(Long billId, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getById(billId);
        bill.writeOff();
        boolean result = billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        boolean result = billRepository.updateById(bill);
        if (result) {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.销账, new Content());
        }
        return true;
    }


    @Override
    public boolean invoice() {
        return false;
    }

    /***
     * 根据账单ids 修改账单信息为开票中 [invoiceState = 1]
     * @param billIds
     * @param supCpUnitId
     * @param billIdsMap key-billId,value-开票状态
     * @return
     */
    @Override
    @Transactional
    public boolean invoiceBatch(List<Long> billIds, String supCpUnitId, Map<Long,Integer> billIdsMap) {
        List<B> billList = billRepository.list(new QueryWrapper<B>()
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                .in("id", billIds));
//        List<B> billList = billRepository.listByIds(billIds);
        billList.forEach(bill -> {
            /** 修改bill的开票状态 */
            if(org.springframework.util.CollectionUtils.isEmpty(billIdsMap)){
                bill.invoice();
            }else{
                Integer status = billIdsMap.get(bill.getId());
                bill.invoice(status);
            }
            billRepository.update(bill, new UpdateWrapper<B>()
                    .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                    .eq("id", bill.getId()));
        });
        return true;
//        return billRepository.updateBatchById(billList);
    }

    @Override
    public boolean finishInvoice() {
        return false;
    }

    /**
     * 修改 收款单表[gather_bill] 开票金额、开票状态
     * 修改 账单表 开票状态、挂账状态、开票金额
     * @param finishInvoiceCommands
     * @return
     */
    @Override
    /*public boolean finishInvoiceBatch(List<FinishInvoiceCommand> finishInvoiceCommands) {
        List<Long> billIds = finishInvoiceCommands.stream().map(FinishInvoiceCommand::getBillId).collect(Collectors.toList());
        List<B> billList = billRepository.list(new QueryWrapper<B>()
                .in("id", billIds)
                .eq(StringUtils.isNotBlank(finishInvoiceCommands.get(0).getSupCpUnitId()), "sup_cp_unit_id", finishInvoiceCommands.get(0).getSupCpUnitId())
        );
//        List<B> billList = billRepository.listByIds(billIds);
        Map<Long, List<FinishInvoiceCommand>> finishInvoiceCommandMap = finishInvoiceCommands.stream().collect(Collectors.groupingBy(FinishInvoiceCommand::getBillId));
        List<GatherBill> gatherBillEList = Lists.newArrayList();
        Long gatherBillId = null;
        //票据id
        Long invoiceReceiptId = finishInvoiceCommands.get(0).getInvoiceReceiptId();
        //发票收据主表(invoice_receipt)
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        if (invoiceReceiptE != null && invoiceReceiptE.getGatherBillId() != null) {
            //收款单id
            gatherBillId = invoiceReceiptE.getGatherBillId();
            //收款单表(gather_bill) gatherBill修改开票金额、开票状态
            gatherBillEList = gatherBillDomainService.gatherBillFinishInvoice(gatherBillId, finishInvoiceCommands.get(0).getSupCpUnitId());
        }
        for (B bill : billList) {
            FinishInvoiceCommand finishInvoiceCommand = finishInvoiceCommandMap.get(bill.getId()).get(0);
            log.info("重新计算账单开票状态：账单id:{}, 原开票金额:{}, 新开票金额:{}, 总可开票金额:{}, 是否成功:{}",
                    bill.getId(), bill.getInvoiceAmount(), finishInvoiceCommand.getInvoiceAmount(), bill.getAllInvoiceAmount(), finishInvoiceCommand.getSuccess());
            //修改开票状态、挂账状态、开票金额
            bill.finishInvoice(finishInvoiceCommand.getInvoiceAmount(), finishInvoiceCommand.getSuccess());
            //开票成功
            if (finishInvoiceCommand.getSuccess()) {
                //通过账单id获取未开票和部分开票状态的收款单
                if (finishInvoiceCommand.getInvoiceAmount() > 0) {
                    if (gatherBillId == null) {
                        //gatherBillId有值说明是从移动端的收款记录进行开票,gatherBillEList变动开票金额、开票状态
                        gatherBillEList = gatherBillDomainService.gatherBillFinishInvoice(gatherBillEList, bill.getId(), finishInvoiceCommand.getInvoiceAmount(), bill.getSupCpUnitId());
                    }
                } else {
                    // gatherBillEList变动开票金额、开票状态
                    gatherBillEList = gatherBillDomainService.voidBatch(gatherBillEList, bill.getId(), -finishInvoiceCommand.getInvoiceAmount(), bill.getSupCpUnitId());
                    //修改挂账状态
                    bill.offAccount();
                }
            }
        }
        if (CollectionUtils.isNotEmpty(gatherBillEList)) {
            gatherBillEList.forEach(b -> {
                //更新收款单表[gather_bill]
                gatherBillRepository.update(b, new UpdateWrapper<GatherBill>().eq("id", b.getId())
                        .eq(StringUtils.isNotBlank(b.getSupCpUnitId()), "sup_cp_unit_id", b.getSupCpUnitId()));
            });
        }

        boolean flag = true;
        for (B b : billList) {
            //账单更新
            flag = billRepository.update(b, new UpdateWrapper<B>().eq("id", b.getId()).eq(StringUtils.isNotBlank(finishInvoiceCommands.get(0).getSupCpUnitId()), "sup_cp_unit_id", b.getSupCpUnitId()));
        }
//        boolean flag = billRepository.updateBatchById(billList);
        if (flag && !billList.isEmpty()) {
            log.info("发送 INVOICED_BATCH 事件,账单id{}", JSON.toJSONString(billList));
            EventLifecycle.apply(EventMessage.builder().headers("action", BillAction.INVOICED_BATCH)
                    .payload(BillBatchActionEvent.invoiced(billList.stream().map(Bill::getId)
                            .collect(Collectors.toList()), billList.get(0).getType(), "结算", finishInvoiceCommands.get(0).getSupCpUnitId())).build());
        }
        return flag;
    }**/

    /**
     * 完成开票
     * @param finishInvoiceCommands
     * @return
     */
    public boolean finishInvoiceBatch(List<FinishInvoiceCommand> finishInvoiceCommands) {
        boolean success = finishInvoiceCommands.get(0).getSuccess();
        String supCpUnitId = finishInvoiceCommands.get(0).getSupCpUnitId();
        log.info("完成开票信息:{}", JSON.toJSONString(finishInvoiceCommands));
        List<Long> billIds = finishInvoiceCommands.stream().map(FinishInvoiceCommand::getBillId).collect(Collectors.toList());
        List<B> billList = billRepository.list(new QueryWrapper<B>()
                .in("id", billIds)
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
            Map<Long, List<FinishInvoiceCommand>> billFinishInvoiceCommandMap = finishInvoiceCommands.stream().collect(Collectors.groupingBy(FinishInvoiceCommand::getBillId));
            // 处理账单
            billList.forEach(bill -> {
                List<FinishInvoiceCommand> finishInvoiceCommandList = billFinishInvoiceCommandMap.get(bill.getId());
                if (finishInvoiceCommandList != null) {
                    Long invoiceAmount = finishInvoiceCommandList.stream()
                            .mapToLong(FinishInvoiceCommand::getInvoiceAmount)
                            .sum();
                    log.info("重新计算账单开票状态：账单id:{}, 原开票金额:{}, 新开票金额:{}, 总可开票金额:{}, 是否成功:{}",
                            bill.getId(), bill.getInvoiceAmount(), invoiceAmount, bill.getAllInvoiceAmount(), success);
                    // 修改开票状态、挂账状态、开票金额
                    bill.finishInvoice(invoiceAmount, success);
                    if (success && invoiceAmount <= 0) {
                        // 修改挂账状态
                        bill.offAccount();
                    }
                }
            });
        billList.forEach(bill ->{
            //更新账单表
            billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", bill.getSupCpUnitId()));
        });
        //收款单状态修改
        List<Long> gatherBillIds = finishInvoiceCommands.stream().map(FinishInvoiceCommand::getGatherBillId).collect(Collectors.toList());
        if(gatherBillIds.stream().anyMatch(Objects::nonNull)){
            List<GatherBill> gatherBills = gatherBillRepository.list(new QueryWrapper<GatherBill>()
                    .in("id", gatherBillIds)
                    .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
            Map<Long, List<FinishInvoiceCommand>> gatherFinishInvoiceCommandMap = finishInvoiceCommands
                    .stream()
                    .filter(command -> Objects.nonNull(command.getGatherBillId()))
                    .collect(Collectors.groupingBy(FinishInvoiceCommand::getGatherBillId));
            // 处理收款单
            gatherBills.forEach(gatherBill -> {
                List<FinishInvoiceCommand> finishInvoiceCommandList = gatherFinishInvoiceCommandMap.get(gatherBill.getId());
                if (finishInvoiceCommandList != null) {
                    long invoiceAmount = finishInvoiceCommandList.stream()
                            .mapToLong(FinishInvoiceCommand::getInvoiceAmount)
                            .sum();
                    log.info("重新计算收款单开票状态：收款单id:{}, 原开票金额:{}, 新开票金额:{}, 收款单:{}, 是否成功:{}",
                            gatherBill.getId(), gatherBill.getInvoiceAmount(), invoiceAmount, JSONObject.toJSONString(gatherBill), success);
                    gatherBill.invoice(invoiceAmount,success);
                }
            });
            gatherBills.forEach(gatherBill -> {
                //更新收款单表
                gatherBillRepository.update(gatherBill, new UpdateWrapper<GatherBill>().eq("id", gatherBill.getId())
                        .eq(StringUtils.isNotBlank(gatherBill.getSupCpUnitId()), "sup_cp_unit_id", gatherBill.getSupCpUnitId()));
            });
            // 收款明细状态修改
            List<Long> gatherDetailIds = finishInvoiceCommands.stream()
                    .map(FinishInvoiceCommand::getGatherDetailId).collect(Collectors.toList());
            List<GatherDetail> gatherDetails = gatherDetailRepository.list(new QueryWrapper<GatherDetail>()
                    .in("id", gatherDetailIds)
                    .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
            Map<Long, FinishInvoiceCommand> gatherDetailCommandMap = finishInvoiceCommands
                    .stream()
                    .filter(command -> Objects.nonNull(command.getGatherDetailId()))
                    .collect(Collectors.toMap(FinishInvoiceCommand::getGatherDetailId, command -> command));
            gatherDetails.forEach(gatherDetail -> {
                FinishInvoiceCommand gatherDetailCommand = gatherDetailCommandMap
                        .get(gatherDetail.getId());
                log.info("重新计算收款明细开票状态：收款明细id:{}, 原开票金额:{}, 新开票金额:{}, 收款单:{}, 是否成功:{}",
                        gatherDetail.getId(), gatherDetail.getInvoiceAmount(), gatherDetailCommand.getInvoiceAmount(), JSONObject.toJSONString(gatherDetail), success);
                gatherDetail.invoice(gatherDetailCommand.getInvoiceAmount(), success);
                //更新收款明细表
                gatherDetailRepository.update(gatherDetail, new UpdateWrapper<GatherDetail>().eq("id", gatherDetail.getId())
                        .eq(StringUtils.isNotBlank(gatherDetail.getSupCpUnitId()), "sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
            });
        }
        return true;
    }


    /**
     *
     * @param billId
     * @param supCpUnitId
     */
    @Override
    public void reSetBillInvoiceState(Long billId, String supCpUnitId) {
        QueryWrapper<B> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", billId);
        queryWrapper.eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId);
        B bill = billRepository.getOne(queryWrapper);
        bill.finishInvoice(0L, false);
        UpdateWrapper<B> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", bill.getId());
        updateWrapper.eq("sup_cp_unit_id", bill.getSupCpUnitId());
        billRepository.update(bill, updateWrapper);
    }

    @Override
    @Transactional
    public boolean invoiceVoidBatch(List<FinishInvoiceCommand> invoiceVoidBatchFList,String supCpUnitId) {
        List<Long> billIds = invoiceVoidBatchFList.stream().map(FinishInvoiceCommand::getBillId).collect(Collectors.toList());
        List<Long> gatherBillIds = invoiceVoidBatchFList.stream().map(FinishInvoiceCommand::getGatherBillId).collect(Collectors.toList());
        List<Long> gatherDetailIds = invoiceVoidBatchFList.stream()
                .map(FinishInvoiceCommand::getGatherDetailId).collect(Collectors.toList());
        List<B> billList = billRepository.list(new QueryWrapper<B>()
                .in("id", billIds)
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        List<GatherBill> gatherBills = gatherBillRepository.list(new QueryWrapper<GatherBill>()
                .in("id", gatherBillIds)
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(
                new LambdaUpdateWrapper<GatherDetail>()
                        .in(GatherDetail::getId, gatherDetailIds)
                        .eq(GatherDetail::getSupCpUnitId, supCpUnitId));
        Map<Long, List<FinishInvoiceCommand>> billFinishInvoiceCommandMap = invoiceVoidBatchFList
                .stream().collect(Collectors.groupingBy(FinishInvoiceCommand::getBillId));
        Map<Long, List<FinishInvoiceCommand>> gatherFinishInvoiceCommandMap = invoiceVoidBatchFList.stream()
                .filter(command -> command.getGatherBillId() != null)
                .collect(Collectors.groupingBy(FinishInvoiceCommand::getGatherBillId));
        Map<Long, FinishInvoiceCommand> gatherDetailCommandMap = invoiceVoidBatchFList.stream()
                .filter(command -> Objects.nonNull(command.getGatherDetailId()))
                .collect(
                Collectors.toMap(FinishInvoiceCommand::getGatherDetailId, command -> command));
        // 处理账单
        billList.forEach(bill -> {
            List<FinishInvoiceCommand> finishInvoiceCommandList = billFinishInvoiceCommandMap.get(bill.getId());
            if (finishInvoiceCommandList != null) {
                Long invoiceAmount = finishInvoiceCommandList.stream()
                        .mapToLong(FinishInvoiceCommand::getInvoiceAmount)
                        .sum();
                bill.voidBatch(invoiceAmount);
                bill.offAccount();
            }
        });
        // 处理收款单
        gatherBills.forEach(gatherBill -> {
            List<FinishInvoiceCommand> finishInvoiceCommandList = gatherFinishInvoiceCommandMap.get(gatherBill.getId());
            if (finishInvoiceCommandList != null) {
                long invoiceAmount = finishInvoiceCommandList.stream()
                        .mapToLong(FinishInvoiceCommand::getInvoiceAmount)
                        .sum();
                gatherBill.voidBatch(invoiceAmount);
            }
        });
        gatherBills.forEach(gatherBill -> {
            //更新收款单表
            gatherBillRepository.update(gatherBill, new UpdateWrapper<GatherBill>().eq("id", gatherBill.getId())
                    .eq(StringUtils.isNotBlank(gatherBill.getSupCpUnitId()), "sup_cp_unit_id", gatherBill.getSupCpUnitId()));
        });
        billList.forEach(bill ->{
            //更新账单表
            billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", bill.getSupCpUnitId()));
        });
        gatherDetails.forEach(gatherDetail -> {
            FinishInvoiceCommand gatherDetailCommand = gatherDetailCommandMap.get(gatherDetail.getId());
            gatherDetail.voidBatch(gatherDetailCommand.getInvoiceAmount());
            gatherDetailRepository.update(gatherDetail, new LambdaUpdateWrapper<GatherDetail>()
                    .eq(GatherDetail::getId, gatherDetail.getId())
                    .eq(GatherDetail::getSupCpUnitId, gatherDetail.getSupCpUnitId()));
        });
        return true;
    }

    @Override
    public boolean reverse(Long billId, String extField1, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId,billId).eq(StringUtils.isNotBlank(supCpUnitId),B::getSupCpUnitId,supCpUnitId));
//        B bill = billRepository.getById(billId);
        if (bill.getReversed() == BillReverseStateEnum.已冲销.getCode()) {
            return true;
        }
        BillReverseA<B> billReverseA = new BillReverseA<B>(bill);
        billReverseA.reverse();
        billRepository.update(bill, new UpdateWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        billRepository.updateById(bill);
        billRepository.save(billReverseA.getReverseBill());
//        gatherBillRepository.save(billReverseA.gatherBill);
//        gatherDetailRepository.save(billReverseA.gatherDetail);
        reverseRepository.save(billReverseA);
        //冲销后生成待审核，未结算的账单
        if (extField1.equals("ReversedInitBill")) {
            reversedInitBillNew(bill);
        }
        //冲销
        EventLifecycle.apply(EventMessage.builder().headers("action", BillAction.REVERSED).payload(BillActionEvent.reverse(bill.getId(), bill.getType(), "冲销", supCpUnitId)).build());
        //日志记录
        BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.冲销, new Content());
        return true;
    }

    /**
     * 冲销后生成待审核，未结算的账单
     *
     * @param bill
     */
    private void reversedInitBillNew(B bill) {
        String billId = null;
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = Global.mapperFacade.map(bill, ReceivableBill.class);
            receivableBill.setId(null);
            receivableBill.setBillNo(null);
            receivableBill.setChargeTime(null);
            receivableBill.init();
            receivableBill.resetState();
            receivableBill.resetAmount(bill.getTotalAmount());
            receivableBill.resetOperatorInfo();
            ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
            receivableBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(receivableBill.getRoomId()));
            receivableBillRepository.save(receivableBill);
            billId = String.valueOf(receivableBill.getId());
        } else if (bill instanceof AdvanceBill) {
            AdvanceBill advanceBill = Global.mapperFacade.map(bill, AdvanceBill.class);
            advanceBill.setId(null);
            advanceBill.setBillNo(null);
            advanceBill.init();
            advanceBill.resetState();
            advanceBill.resetAmount(bill.getTotalAmount());
            advanceBill.resetOperatorInfo();
            AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
            advanceBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(advanceBill.getRoomId()));
            advanceBillRepository.save(advanceBill);
            billId = String.valueOf(advanceBill.getId());
        } else if (bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill temporaryChargeBill = Global.mapperFacade.map(bill, TemporaryChargeBill.class);
            temporaryChargeBill.setId(null);
            temporaryChargeBill.setBillNo(null);
            temporaryChargeBill.setChargeTime(null);
            temporaryChargeBill.init();
            temporaryChargeBill.resetState();
            temporaryChargeBill.resetAmount(bill.getTotalAmount());
            temporaryChargeBill.resetOperatorInfo();
            TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(TemporaryChargeBillRepository.class);
            temporaryChargeBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(temporaryChargeBill.getRoomId()));
            temporaryChargeBillRepository.save(temporaryChargeBill);
            billId = String.valueOf(temporaryChargeBill.getId());
        }

        if (Objects.nonNull(billId)) {
            //日志记录
            BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("冲销后生成待审核，未结算的账单", true))));
        }

    }

    @Override
    public boolean robackReverse(Long billId, String supCpUnitId) {
        ErrorAssertUtil.notEmptyThrow400(supCpUnitId, ErrorMessage.SUP_CP_UNIT_ID_NOT_EXIST);
        BillReverseE billReverseE = reverseRepository.getOneByBillIdDescTime(billId);
        B bill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", billReverseE.getBillId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getById(billReverseE.getBillId());

        //将原账单置为未冲销
        bill.setReversed(BillReverseStateEnum.未冲销.getCode());
        billRepository.update(bill, new UpdateWrapper<B>()
                .eq("id", bill.getId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        billRepository.updateById(bill);

        //删除新的冲销单子
        B reverseBill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", billReverseE.getReverseBillId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        billRepository.update(new UpdateWrapper<B>()
                .eq("id", reverseBill.getId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                .set("deleted", "1"));
//        B reverseBill = billRepository.getById(billReverseE.getReverseBillId());
//        billRepository.removeById(reverseBill);

        reverseRepository.removeById(billReverseE);

        //删除新的收款单
        GatherDetail gatherDetail = gatherDetailRepository.getByRecBillId(reverseBill.getId(), supCpUnitId);
        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id", gatherDetail.getGatherBillId())
                .eq("sup_cp_unit_id", supCpUnitId));
        gatherDetailRepository.remove(new QueryWrapper<GatherDetail>()
                .eq("id", gatherDetail.getId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        gatherBillRepository.remove(new QueryWrapper<GatherBill>()
                .eq("id", gatherBill.getId())
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));

        //日志记录
        BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.回滚冲销, new Content());
        return true;
    }


    @Override
    @Transactional
    public Long settle(AddBillSettleCommand command) {
        return this.settleBatch(Lists.newArrayList(command));
    }

    @Override
    public Long settleBatch(List<AddBillSettleCommand> commands) {
        if (CollectionUtils.isEmpty(commands)) {
            throw BizException.throw400("结算信息不存在");
        }
        //获取账单信息
        List<B> billList = billRepository.list(new QueryWrapper<B>()
                .in("id", commands.stream().map(AddBillSettleCommand::getBillId).collect(Collectors.toList()))
                .eq(StringUtils.isNotBlank(commands.get(0).getSupCpUnitId()), "sup_cp_unit_id", commands.get(0).getSupCpUnitId())
                .eq("state", 0)
                .notIn("refund_state", List.of(1,3))
                .eq("verify_state", 0));
//        List<B> billList = billRepository.listByIds(commands.stream().map(AddBillSettleCommand::getBillId).collect(Collectors.toList()));
        ErrorAssertUtil.isFalseThrow402(CollectionUtils.isEmpty(billList), ErrorMessage.BILL_NOT_EXIST);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getSettleState().equals(BillSettleStateEnum.已结算.getCode())), ErrorMessage.BILL_NOT_SETTLE);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getSettleState().equals(BillSettleStateEnum.结算中.getCode())), ErrorMessage.BILL_SETTLEING);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getCarriedState().equals(BillCarriedStateEnum.已结转.getCode())), ErrorMessage.BILL_NOT_SETTLE_HAS_CARRYOVER);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> !a.getApprovedState().equals(BillApproveStateEnum.已审核.getCode())), ErrorMessage.BILL_NOT_SETTLE_NO_APPROVE);
        Map<Long, List<B>> billMap = billList.stream().collect(Collectors.groupingBy(B::getId));
        BillGatherA<B> billGatherA = new BillGatherA<>(billList, commands);
        List<BillGatherDetailA<B>> gatherDetailAList = Lists.newArrayList();
        List<Long> advanceBillIds = commands.stream().map(AddBillSettleCommand::getCarriedBillId)
                .collect(Collectors.toList());
        Map<Long, AdvanceBill> advanceBillMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(advanceBillIds)) {
            AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
            List<AdvanceBill> advanceBills = advanceBillRepository.listByIds(advanceBillIds);
            advanceBillMap = advanceBills.stream().collect(Collectors.toMap(AdvanceBill::getId, Function.identity()));
        }
        for (AddBillSettleCommand command : commands) {
            List<B> bs = billMap.get(command.getBillId());
            if (CollectionUtils.isEmpty(bs)){
                continue;
            }
            B bill = bs.get(0);
            if (command.getCarriedBillId() != null) {
                AdvanceBill advanceBill = advanceBillMap.get(command.getCarriedBillId());
                BillGatherDetailA<B> gatherDetailA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
                gatherDetailA.discountBillCarryover(command, advanceBill,billCarryoverDetailRepository);
                gatherDetailAList.add(gatherDetailA);
                carryoverGatherBill(command);
            } else {
                BillGatherDetailA<B> gatherDetailA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
                gatherDetailA.gather(command);
                gatherDetailAList.add(gatherDetailA);
                if (FANG_YUAN.equals(EnvData.config)) {
                    if (Objects.isNull(bill.getBillCostType()) && Objects.nonNull(bill.getAccountDate())) {
                        int nowYear = LocalDate.now().getYear();
                        int billYear = bill.getAccountDate().getYear();
                        if(billYear > nowYear){
                            bill.setBillCostType(BillCostTypeEnum.当期应收.getCode());
                        }
                        if(billYear == nowYear){
                            int quarterNow = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
                            int quarterBill = (bill.getAccountDate().getMonthValue() - 1) / 3 + 1;
                            if (quarterBill > quarterNow) {
                                bill.setBillCostType(BillCostTypeEnum.当期应收.getCode());
                            }
                        }
                    }
                }
            }
        }
        billGatherA.setGatherDetailAS(gatherDetailAList);
        Long actualPayAmount = gatherDetailAList.stream().map(BillGatherDetailA::getGatherDetail).filter(Objects::nonNull)
                .map(GatherDetail::getPayAmount).reduce(Long::sum).orElse(0L);
        Long actualAdvanceAmount = gatherDetailAList.stream()
                .map(BillGatherDetailA::getCarryoverAdvanceBillCarryoverA)
                .filter(Objects::nonNull)
                .map(BillCarryoverA::getAdvanceBillSettle)
                .filter(Objects::nonNull).map(GatherDetail::getPayAmount).reduce(Long::sum).orElse(0L);
        billGatherA.getGatherBill().setTotalAmount(actualPayAmount + actualAdvanceAmount);
        if (gatherDetailAList.stream().map(BillGatherDetailA::getGatherDetail).filter(Objects::nonNull)
                .allMatch(a->a.getPayChannel().equals(SettleChannelEnum.押金结转.getCode()))){
            billGatherA.getGatherBill().setPayChannel(SettleChannelEnum.押金结转.getCode());
        }
        return saveRepository(billGatherA);
    }

    @Override
    @Transactional
    public BillGatherA<B> settleImportBatch(List<AddBillSettleCommand> commands,List<B> billList) {
        if (CollectionUtils.isEmpty(commands)) {
            throw BizException.throw400("结算信息不存在");
        }
        ErrorAssertUtil.isFalseThrow402(CollectionUtils.isEmpty(billList), ErrorMessage.BILL_NOT_EXIST);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getSettleState().equals(BillSettleStateEnum.已结算.getCode())), ErrorMessage.BILL_NOT_SETTLE);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getSettleState().equals(BillSettleStateEnum.结算中.getCode())), ErrorMessage.BILL_SETTLEING);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> a.getCarriedState().equals(BillCarriedStateEnum.已结转.getCode())), ErrorMessage.BILL_NOT_SETTLE_HAS_CARRYOVER);
        ErrorAssertUtil.isFalseThrow402(billList.stream().anyMatch(a -> !a.getApprovedState().equals(BillApproveStateEnum.已审核.getCode())), ErrorMessage.BILL_NOT_SETTLE_NO_APPROVE);
        Map<Long, List<B>> billMap = billList.stream().collect(Collectors.groupingBy(B::getId));
        BillGatherA<B> billGatherA = new BillGatherA<>(billList, commands);
        List<BillGatherDetailA<B>> gatherDetailAList = Lists.newArrayList();
        List<Long> advanceBillIds = commands.stream().map(AddBillSettleCommand::getCarriedBillId)
                .collect(Collectors.toList());
        Map<Long, AdvanceBill> advanceBillMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(advanceBillIds)) {
            AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
            List<AdvanceBill> advanceBills = advanceBillRepository.listByIds(advanceBillIds);
            advanceBillMap = advanceBills.stream().collect(Collectors.toMap(AdvanceBill::getId, Function.identity()));
        }
        for (AddBillSettleCommand command : commands) {
            B bill = billMap.get(command.getBillId()).get(0);
            if (command.getCarriedBillId() != null) {
                AdvanceBill advanceBill = advanceBillMap.get(command.getCarriedBillId());
                BillGatherDetailA<B> gatherDetailA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
                gatherDetailA.discountBillCarryover(command, advanceBill,billCarryoverDetailRepository);
                gatherDetailAList.add(gatherDetailA);
                carryoverGatherBill(command);
            } else {
                BillGatherDetailA<B> gatherDetailA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
                gatherDetailA.gather(command);
                gatherDetailAList.add(gatherDetailA);
                if (FANG_YUAN.equals(EnvData.config)) {
                    if (Objects.isNull(bill.getBillCostType()) && Objects.nonNull(bill.getAccountDate())) {
                        int quarterNow = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
                        int quarterBill = (bill.getAccountDate().getMonthValue() - 1) / 3 + 1;
                        if (quarterBill > quarterNow) {
                            bill.setBillCostType(BillCostTypeEnum.当期应收.getCode());
                        }
                    }
                }
            }
        }
        billGatherA.setGatherDetailAS(gatherDetailAList);
        saveRepository(billGatherA);
        return billGatherA;
    }

    private void carryoverGatherBill(AddBillSettleCommand command) {
        //获取结转账单的收款单
        List<GatherDetail> gatherDetailList = Optional
                .ofNullable(gatherDetailRepository.getListByRecBillId(command.getCarriedBillId(),
                        command.getSupCpUnitId()))
                .orElse(new ArrayList<>())
                .stream().filter(e -> e.getRemainingCarriedAmount() > 0)
                .collect(Collectors.toList());

        if (!org.springframework.util.CollectionUtils.isEmpty(gatherDetailList)) {
            // key -> gatherBillId, value -> List<GatherDetail>
            Map<Long, List<GatherDetail>> gatherDetailMap =
                    gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
            // 总结转金额
            Long totalCarryoverAmount = command.getCarriedAmount();

            for (Map.Entry<Long, List<GatherDetail>> item : gatherDetailMap.entrySet()) {
                if (totalCarryoverAmount == 0L) {
                    break;
                }
                List<GatherDetail> gatherDetails = item.getValue();
                Long gatherBillCarriedAmount = 0L;
                for (GatherDetail gatherDetail : gatherDetails) {
                    if (totalCarryoverAmount == 0L) {
                        break;
                    }
                    gatherDetail.setRemark(command.getRemark());
                    Long remainingCarriedAmount = gatherDetail.getRemainingCarriedAmount();
                    if (totalCarryoverAmount > remainingCarriedAmount) {
                        gatherBillCarriedAmount += gatherDetail.getRemainingCarriedAmount();
                        totalCarryoverAmount -= gatherDetail.getRemainingCarriedAmount();
                        gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount()
                                + gatherDetail.getRemainingCarriedAmount());
                    } else {
                        gatherBillCarriedAmount += totalCarryoverAmount;
                        gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount() + totalCarryoverAmount);
                        totalCarryoverAmount = 0L;
                        break;
                    }
                }

                GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id", item.getKey()).eq("sup_cp_unit_id", command.getSupCpUnitId()));
                gatherBill.setCarriedAmount(gatherBill.getCarriedAmount() + gatherBillCarriedAmount);
                gatherBill.restCarriedState();
                gatherBillRepository.update(gatherBill, new UpdateWrapper<GatherBill>().eq("id", gatherBill.getId())
                        .eq(StringUtils.isNotBlank(gatherBill.getSupCpUnitId()), "sup_cp_unit_id", gatherBill.getSupCpUnitId()));
                gatherDetails.forEach(b -> {
                    gatherDetailRepository.update(b, new UpdateWrapper<GatherDetail>().eq("id", b.getId())
                            .eq(StringUtils.isNotBlank(b.getSupCpUnitId()), "sup_cp_unit_id", b.getSupCpUnitId()));
                });
                //结转账单日志记录
                BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.结转,
                        new Content().option(new ContentOption(new PlainTextDataItem("结转方式为：" + CarryoverTypeEnum.抵扣.getValue(), true)))
                                .option(new ContentOption(new PlainTextDataItem(CarryoverTypeEnum.抵扣.getValue() + "金额为：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(gatherBillCarriedAmount), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            }
        }
    }

    /**
     * 保存结算资源
     *
     * @param billGatherA billGatherA
     */
    @Transactional
    public Long saveRepository(BillGatherA<B> billGatherA) {
        Long gatherBillId = null;
        // 获取账单信息
        List<B> billList = billGatherA.getBillList();
        if (!billList.isEmpty()) {
            log.info("settle bill info : {}", JSON.toJSONString(billList));
            billList.forEach(v -> {
                if (Objects.nonNull(v.getId())) {
                    billRepository.update(v, new UpdateWrapper<B>().eq("id", v.getId())
                            .eq(StringUtils.isNotBlank(v.getSupCpUnitId()), "sup_cp_unit_id", v.getSupCpUnitId()));
                    // 更新收据内账单的开票时间
                    invoiceReceiptDetailRepository.updateBillPayTime(v.getType(), v.getId(), v.getPayTime());
                } else {
                    if (v instanceof ReceivableBill) {
                        ((ReceivableBill)v).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(v.getRoomId()));
                    }else if (v instanceof TemporaryChargeBill) {
                        ((TemporaryChargeBill)v).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(v.getRoomId()));
                    }else if (v instanceof AdvanceBill){
                        ((AdvanceBill)v).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(v.getRoomId()));
                    }
                    billRepository.save(v);
                }
            });
            gatherBillId = billGatherAGateway.saveBillGatherA(billGatherA);
//            EventLifecycle.push(
//                    EventMessage.builder()
//                            .headers("action", BillAction.SETTLED_BATCH)
//                            .payload(
//                                    BillBatchActionEvent.settle(
//                                            billList.stream()
//                                                    .map(Bill::getId)
//                                                    .collect(Collectors.toList())
//                                            , billList.get(0).getType(), "结算", billGatherA.getGatherBill().getSupCpUnitId()))
//                            .build()
//            );

//            List<BillGatherDetailA<B>> gatherDetailAS = billGatherA.getGatherDetailAS();
//            if (null != gatherDetailAS && gatherDetailAS.size() > 0) {
//                for (BillGatherDetailA<B> gatherDetailA : gatherDetailAS) {
//                    BillCarryoverA<B, AdvanceBill> carryoverAdvanceBillCarryoverA = gatherDetailA.getCarryoverAdvanceBillCarryoverA();
//                    if (null != carryoverAdvanceBillCarryoverA) {
//                        AdvanceBill advanceBill = carryoverAdvanceBillCarryoverA.getAdvanceBill();
//                        if (null != advanceBill) {
//                            ArrayList<Long> longs = new ArrayList<>();
//                            longs.add(advanceBill.getId());
//                            EventLifecycle.push(
//                                    EventMessage.builder()
//                                            .headers("action", BillAction.SETTLED_BATCH)
//                                            .payload(
//                                                    BillBatchActionEvent.settle(longs, 2, "结算", billGatherA.getGatherBill().getSupCpUnitId()))
//                                            .build()
//                            );
//                        }
//                    }
//                }
//            }
        }
        return gatherBillId;
    }




//    @Override
//    public Boolean settleBatch(List<AddBillSettleCommand> commands) {
//        if (CollectionUtils.isEmpty(commands)) {
//            throw BizException.throw400("结算信息不存在");
//        }
//        //获取账单信息
//        List<B> billList = billRepository.listByIds(commands.stream().map(AddBillSettleCommand::getBillId).collect(Collectors.toList()));
//        ErrorAssertUtil.isFalseThrow402(CollectionUtils.isEmpty(billList), ErrorMessage.BILL_NOT_EXIST);
//        Map<Long, List<B>> billMap = billList.stream().collect(Collectors.groupingBy(B::getId));
//        BillGatherA<B> billGatherA = new BillGatherA(billList,commands);
//        List<GatherDetail> gatherDetailList = Lists.newArrayList();
//
//        commands.forEach(command -> {
//            if (command.getCarriedBillId() != null) {
//                B bill = billMap.get(command.getBillId()).get(0);
//                AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
//                AdvanceBill advanceBill = advanceBillRepository.getById(command.getCarriedBillId());
//                BillCarryoverE billCarryoverE = generalBillCarryover(command, bill, advanceBill);
//                BillCarryoverA<AdvanceBill, B> advanceBillBBillCarryoverA = new BillCarryoverA<>(advanceBill, null, billCarryoverE, Lists.newArrayList(bill));
//                advanceBillBBillCarryoverA.setGatherBill(billGatherA.getGatherBill());
//                advanceBillBBillCarryoverA.carryover();
//                advanceBillRepository.updateBatchById(Lists.newArrayList(advanceBill));
//                carryoverRepository.save(advanceBillBBillCarryoverA);
//                List<GatherDetail> gatherDetails = advanceBillBBillCarryoverA.getTargetSettles();
//                for (GatherDetail gatherDetail : gatherDetails) {
//                    gatherDetail.setPayerPhone(command.getPayerPhone());
//                    gatherDetail.setPayeePhone(command.getPayeePhone());
//                }
//                gatherDetailList.addAll(gatherDetails);
//
//            } else {
//                B bill = billMap.get(command.getBillId()).get(0);
//                BillGatherDetailA<Bill> gatherDetailA = new BillGatherDetailA<>(bill);
//                gatherDetailA.gather(billGatherA.getGatherBill(),command);
//                gatherDetailList.add(gatherDetailA.getGatherDetail());
//            }
//        });
//        return saveRepository(billList,billGatherA.getGatherBill(),gatherDetailList);
//    }

//
//    /**
//     * 保存结算资源
//     * @param billList
//     * @param gatherBill
//     * @param gatherDetailList
//     */
//    @Transactional
//    public Boolean saveRepository(List<B> billList, GatherBill gatherBill, List<GatherDetail> gatherDetailList) {
//        gatherBillRepository.save(gatherBill);
//        gatherDetailRepository.saveBatch(gatherDetailList);
//        billRepository.saveOrUpdateBatch(billList);
//        if (!billList.isEmpty()) {
//            EventLifecycle.apply(EventMessage.builder().headers("action", BillAction.SETTLED_BATCH).payload(BillBatchActionEvent.settle(billList.stream().map(Bill::getId).collect(Collectors.toList()), billList.get(0).getType(), "结算")).build());
//            //写日志
//            gatherDetailList.forEach(gd -> {
//                if (Objects.nonNull(gd.getRecBillId())){
//                    SettleWayEnum settleWayEnum = SettleWayEnum.valueOfByCode(gd.getPayWay());
//                    SettleChannelEnum settleChannelEnum = SettleChannelEnum.valueOfByCode(gd.getPayChannel());
//                    BizLog.initiate(String.valueOf(gd.getRecBillId()),
//                            LogContext.getOperator(), LogObject.账单, LogAction.收款,
//                            new Content().option(new ContentOption(new PlainTextDataItem("收款方式： " + settleWayEnum.getValue() + "-" + settleChannelEnum.getValue(), true)))
//                                    .option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
//                                    .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(gd.getPayAmount()), false), OptionStyle.normal()))
//                                    .option(new ContentOption(new PlainTextDataItem("元", false))));
//                }
//            });
//            //收款单日志
//            BizLog.normal(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.创建,
//                    new Content().option(new ContentOption(new PlainTextDataItem("账单结算", true))));
//        }
//        return true;
//    }

//    /**
//     * 结算构建结转数据
//     *
//     * @return
//     */
//    private BillCarryoverE generalBillCarryover(AddBillSettleCommand addBillSettleCommand, B bill, AdvanceBill advanceBill) {
//        BillCarryoverE billCarryoverE = new BillCarryoverE();
//        billCarryoverE.setCarriedBillId(addBillSettleCommand.getCarriedBillId());
//        billCarryoverE.setCarriedBillNo(advanceBill.getBillNo());
//        billCarryoverE.setCarryoverAmount(addBillSettleCommand.getCarriedAmount());
//        billCarryoverE.setCarryoverType(1);
//        billCarryoverE.setApproveTime(LocalDateTime.now());
//        billCarryoverE.setCarryoverTime(LocalDateTime.now());
//        CarryoverDetail carryoverDetail = new CarryoverDetail();
//        carryoverDetail.setTargetBillId(bill.getId());
//        carryoverDetail.setTargetBillNo(bill.getBillNo());
//        carryoverDetail.setCarryoverAmount(addBillSettleCommand.getCarriedAmount());
//        if (bill instanceof ReceivableBill) {
//            carryoverDetail.setChargeStartTime(((ReceivableBill) bill).getStartTime());
//            carryoverDetail.setChargeEndTime(((ReceivableBill) bill).getEndTime());
//        }
//        billCarryoverE.setCarryoverTime(LocalDateTime.now());
//        billCarryoverE.setCarryoverDetail(Lists.newArrayList(carryoverDetail));
//        billCarryoverE.setAdvanceCarried(0);
//        billCarryoverE.setState(2);
//        billCarryoverE.setRemark(StringUtils.isBlank(addBillSettleCommand.getRemark()) ? "" : addBillSettleCommand.getRemark());
//        billCarryoverE.setBillType(bill.getType());
//        return billCarryoverE;
//    }

    @Override
    public List<BillSettleDto> getBillSettle(List<Long> billIds, BillTypeEnum billTypeEnum, String supCpUnitId) {
        List<GatherDetail> gatherDetailList = Lists.newArrayList();
        if (billTypeEnum == BillTypeEnum.应收账单) {
            gatherDetailList = gatherDetailRepository.getByRecBillIds(billIds, supCpUnitId);
        } else if (billTypeEnum == BillTypeEnum.预收账单) {
            gatherDetailList = gatherDetailRepository.getByGatherBillIds(billIds, supCpUnitId);
        }
        if (CollectionUtils.isNotEmpty(gatherDetailList)) {
            List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
            for (GatherDetail gatherDetail : gatherDetailList) {
                GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>()
                        .eq("id", gatherDetail.getGatherBillId())
                        .eq("sup_cp_unit_id", supCpUnitId));
                BillSettleDto billSettleDto = Global.mapperFacade.map(gatherBill, BillSettleDto.class);
                billSettleDto.setBillId(gatherDetail.getRecBillId());
                billSettleDto.setSettleNo(gatherDetail.getGatherBillNo());
                billSettleDto.setSettleChannel(gatherDetail.getPayChannel());
                billSettleDto.setSettleWay(gatherDetail.getPayWay());
                billSettleDto.setSettleAmount(gatherDetail.getRecPayAmount());
                billSettleDto.setPayAmount(gatherBill.getTotalAmount());
                billSettleDto.setSettleTime(gatherDetail.getPayTime());
                billSettleDto.setChargeStartTime(gatherDetail.getChargeStartTime());
                billSettleDto.setChargeEndTime(gatherDetail.getChargeEndTime());
                billSettleDtoList.add(billSettleDto);
            }
            return billSettleDtoList;

        }
        return null;
    }

    @Override
    public Boolean refund(RefundCommand command) {
        B bill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", command.getBillId())
                .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//        B bill = billRepository.getById(command.getBillId());
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        BillRefundA<B> billRefundA = new BillRefundA<>(bill);
        Global.mapperFacade.map(command, billRefundA);
        boolean refund = billRefundA.refund();
        if (refund) {
            boolean result = billRepository.update(billRefundA.getBill(), new UpdateWrapper<B>()
                    .eq("id", billRefundA.getBill().getId())
                    .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            boolean result = billRepository.updateById(billRefundA.getBill());
            refundRepository.saveOrUpdate(billRefundA);
            //日志记录
            if (result) {
                BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                        new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", true)))
                                .option(new ContentOption(new PlainTextDataItem("退款金额为：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(billRefundA.getRefundAmount()), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            }
        }
        return true;
    }

    @Override
    public boolean verify() {
        return false;
    }

    @Override
    public B getById(Long bid, String supCpUnitId) {
        return billRepository.getOne(new QueryWrapper<B>().eq("id", bid).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        return billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId, bid).eq(StringUtils.isNotBlank(supCpUnitId), B::getSupCpUnitId, supCpUnitId));
//        return billRepository.getById(bid);
    }

    @Override
    public BillApproveDetailDto getWithApproving(Long bid, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", bid).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId, bid).eq(StringUtils.isNotBlank(supCpUnitId), B::getSupCpUnitId, supCpUnitId));
//        B bill = billRepository.getById(bid);
        if (Objects.nonNull(bill)) {
            BillApproveDetailDto detailDto = new BillApproveDetailDto();
            detailDto.setBill(bill);
            BillApproveE billApproveE = approveRepository.getOne(new QueryWrapper<BillApproveE>()
                            .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                    .eq("bill_id", bid).in("approved_state", Bill.getApprovingState()));
            if (Objects.nonNull(billApproveE)) {
                log.info("approveOperateType is ：{}", billApproveE.getOperateType());
                BillApproveOperateTypeEnum approveOperateType = BillApproveOperateTypeEnum.valueOfByCode(billApproveE.getOperateType());
                detailDto.setBillApprove(billApproveE);
                switch (approveOperateType) {
                    case 减免:
                    case 调整:
                        BillAdjustE billAdjustE = adjustRepository.getOne(new LambdaQueryWrapper<BillAdjustE>()
                                .eq(BillAdjustE::getBillId, bid)
                                .in(BillAdjustE::getState, BillAdjustE.getApprovingState())
                                .eq(BillAdjustE::getBillApproveId, billApproveE.getId()));
                        detailDto.setBillAdjust(billAdjustE);
                        break;
                    case 结转:
                        BillCarryoverE billCarryoverE = carryoverRepository.getOne(new LambdaQueryWrapper<BillCarryoverE>()
                                .eq(BillCarryoverE::getCarriedBillId, bid)
                                .eq(BillCarryoverE::getBillApproveId, billApproveE.getId())
                                .in(BillCarryoverE::getState, BillCarryoverE.getApprovingState()));
                        detailDto.setBillCarryover(billCarryoverE);
                        break;
                    case 退款:
                        BillRefundE billRefundE = refundRepository.getOne(new LambdaQueryWrapper<BillRefundE>()
                                .eq(BillRefundE::getBillId, bid)
                                .in(BillRefundE::getState, BillRefundE.getRefundingState())
                                .eq(BillRefundE::getBillApproveId, billApproveE.getId()));
                        detailDto.setBillRefund(billRefundE);
                        break;
                    case 跳收:
                        List<BillJumpE> jumpList = jumpRepository.list(new LambdaQueryWrapper<BillJumpE>()
                                .eq(BillJumpE::getBillId, bid)
                                .in(BillJumpE::getState, BillJumpE.getJumpApprovingState())
                                .eq(BillJumpE::getBillApproveId, billApproveE.getId())
                                .orderByAsc(BillJumpE::getGmtModify));
                        if (CollectionUtils.isNotEmpty(jumpList)) {
                            detailDto.setBillJump(jumpList.get(0));
                        }

                }
            }
            return detailDto;
        }
        return null;
    }

    /**
     * 账单关联数据集合
     * @param bid
     * @param supCpUnitId
     * @return
     */
    @Override
    public BillDetailDto getDetailById(Long bid, String supCpUnitId) {
        //账单表(receivable_bill)
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", bid).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        if(Objects.isNull(bill)){return null;}
        BillDetailDto billDetailDto = new BillDetailDto();
        billDetailDto.setBill(bill);
        //调整信息记录(bill_adjust)
        billDetailDto.setBillAdjustDtos(adjustRepository.listByBillId(bid));
        //申请信息记录(bill_approve)
        billDetailDto.setApproves(approveRepository.listByBillId(bid, supCpUnitId));
        if (BillTypeEnum.预收账单.equalsByCode(bill.getType())) {
            //收款单明细(gather_detail)
            billDetailDto.setBillSettleDtos(handleSettle(gatherDetailRepository.listByRecBillId(bid, supCpUnitId)));
        } else {
            List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bid, supCpUnitId);
            Map<Long, List<GatherBill>> gatherBillMap = new HashedMap<>();
            if (CollectionUtils.isNotEmpty(gatherDetails)) {
                List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
                //收款单表(gather_bill)
                List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds, supCpUnitId);
                gatherBillMap = gatherBillList.stream().collect(Collectors.groupingBy(GatherBill::getId));
            }
            List<BillSettleDto> billSettleDtos = handleSettle(gatherDetails);
            if (CollectionUtils.isNotEmpty(billSettleDtos)) {
                for (BillSettleDto billSettleDto : billSettleDtos) {
                    if (gatherBillMap != null && !gatherBillMap.isEmpty()
                            // 只取有效状态的收款单，排除掉失效的收款单
                            && Objects.equals(Const.State._0, billSettleDto.getAvailable())) {
                        GatherBill gatherBill = gatherBillMap.get(billSettleDto.getGatherBillId()).get(0);
                        billSettleDto.setTradeNo(gatherBill.getTradeNo());
                        billSettleDto.setDiscounts(JSON.toJSONString(gatherBill.getDiscounts()));
                        billSettleDto.setRemark(gatherBill.getRemark());
                        billSettleDto.setPaySource(gatherBill.getPaySource());
                    }

                }
            }
            billDetailDto.setBillSettleDtos(billSettleDtos);
        }
        //退款单表(bill_refund)
        billDetailDto.setBillRefundDtos(refundRepository.listByBillId(bid));
        //账单结转记录表(bill_carryover)
        billDetailDto.setBillCarryoverDtos(carryoverRepository.listByCarriedBillId(bid));
        setOverdueAmount(billDetailDto);
        return billDetailDto;
    }

    public void setOverdueAmount(BillDetailDto billDetailDto) {
        if (BillTypeEnum.应收账单.equalsByCode(billDetailDto.getBill().getType())
                && EnvConst.FANGYUAN.equals(EnvData.config)) {
            Bill bill = billDetailDto.getBill();
            List<ReceivableBill> billOverdueDetail = receivableBillRepository.getBillOverdueDetail(List.of(billDetailDto.getBill().getId()), bill.getSupCpUnitId());
            if (CollectionUtils.isNotEmpty(billOverdueDetail)){
                billDetailDto.setReceivableOverdueAmount(billOverdueDetail.get(0).getReceivableAmount());
                billDetailDto.setActualPayOverdueAmount(billOverdueDetail.get(0).getActualPayAmount());
                billDetailDto.setDeductionOverdueAmount(billOverdueDetail.get(0).getDiscountAmount());
                billDetailDto.setNotReceivedOverdueAmount(billOverdueDetail.get(0).getNotReceivedOverdueAmount());
            }
        }


    }

    /**
     * 处理结算记录映射
     *
     * @param gatherDetailList
     * @return
     */
    public List<BillSettleDto> handleSettle(List<GatherDetail> gatherDetailList) {
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
            billSettleDto.setSettleWayString(SettleWayEnum.valueNameOfByCode(billSettleDto.getSettleWay()));
            billSettleDtoList.add(billSettleDto);
        });
        return billSettleDtoList;
    }

    @Override
    public IPage<B> getPage(PageF<SearchF<?>> query) {
        return billRepository.queryBillByPage(query);
    }

    @Override
    public IPage<B> getPageWithApprove(PageF<SearchF<?>> query) {
        query.getConditions().getFields().add(new Field("b.approved_state", BillApproveStateEnum.已审核.getCode(), 2));
        return billRepository.getPageWithApprove(query);
    }

    @Override
    public IPage<B> getApprovePage(PageF<SearchF<?>> query) {
        return null;
    }

    @Override
    public List<B> getList(List<Long> bids) {
        return billRepository.listByIds(bids);
    }


    @Override
    public List<B> getList(List<Long> bids, String supCpUnitId) {
        return billRepository.list(new QueryWrapper<B>().eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId).in("id", bids)
                .eq("deleted",Const._0));
//        return billRepository.list(new LambdaQueryWrapper<B>().eq(StringUtils.isNotBlank(supCpUnitId),B::getSupCpUnitId,supCpUnitId).in(B::getId,bids));
    }

    /**
     * 根据查询条件查询符合账单
     * @param billF temporaryBillF
     * @param supCpUnitId supCpUnitId
     * @return
     */
    @Override
    public List<B> getConditionList(TemporaryBillF billF, String supCpUnitId) {
        // 根据参数构建查询wrapper
        List<String> roomIds = CollectionUtils.isNotEmpty(billF.getRoomIdList())?
                Global.mapperFacade.mapAsList(billF.getRoomIdList(),String.class):new ArrayList<>();
        QueryWrapper<B> wrapper = new QueryWrapper<B>().eq("sup_cp_unit_id", supCpUnitId)
                .in(CollectionUtils.isNotEmpty(billF.getBillIds()),"id", billF.getBillIds())
                .in(CollectionUtils.isNotEmpty(billF.getCarriedStateList()),"carried_state",billF.getCarriedStateList())
                .in(CollectionUtils.isNotEmpty(billF.getSettleStateList()),"settle_state",billF.getSettleStateList())
                .in(CollectionUtils.isNotEmpty(billF.getRefundStateList()),"refund_state",billF.getRefundStateList())
                .in(CollectionUtils.isNotEmpty(billF.getApproveStateList()),"approved_state",billF.getApproveStateList())
                .in(CollectionUtils.isNotEmpty(billF.getStateList()),"state",billF.getStateList())
                .in(CollectionUtils.isNotEmpty(billF.getChargeItemIdList()),"charge_item_id",billF.getChargeItemIdList())
                .in(CollectionUtils.isNotEmpty(billF.getRoomIdList()),"room_id",roomIds)
                .eq(Objects.nonNull(billF.getIsInit()),"is_init",billF.getIsInit())
                .eq("reversed",BillReverseStateEnum.未冲销.getCode())
                .eq("deleted",Const._0)
                .le(Objects.nonNull(billF.getEndTime()),"end_time",billF.getEndTime())
                .isNull("bill_label");
        // 构建排序
        if (Objects.nonNull(billF.getAsc())){
            if (billF.getAsc()){
                wrapper.orderByAsc(billF.getOrderValue());
            }else {
                wrapper.orderByDesc(billF.getOrderValue());
            }
        }
        wrapper.last(Objects.nonNull(billF.getSize()),"LIMIT "+billF.getSize());
        return billRepository.list(wrapper);
    }

    @Override
    public List<B> getList(PageF<SearchF<?>> query, Integer billType) {
        return billRepository.listByPageSearch(query, billType);
    }

    @Override
    public Boolean handBatch(BatchHandBillCommand command, Integer billType) {
        List<B> billList;
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            ErrorAssertUtil.notNullThrow300(command.getQuery(), ErrorMessage.BILL_BATCH_QUERY_ERROR);
            //根据查询条件获取账单
            billList = billRepository.listByPageSearch(command.getQuery(), billType);
        } else {
            billList = billRepository.list(new QueryWrapper<B>().in("id", billIds).eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            billList = billRepository.listByIds(billIds);
        }
        if (billList.isEmpty()) {
            log.error("未根据账单id集合找到账单信息： BatchHandBillCommand {}", command);
            return Boolean.FALSE;
        }
        List<BillHandA<B>> billHandAS = new ArrayList<>();
        billList.forEach(bill -> {
            BillHandA<B> billHandA = new BillHandA<>(bill);
            billHandA.initDefaultHand().hand(command.getInvoiceReceipts());
            billHandAS.add(billHandA);
        });

        billRepository.updateBatchById(billHandAS.stream().map(BillHandA::getBill).collect(Collectors.toList()));
        handRepository.handBatch(billHandAS);
        return Boolean.TRUE;
    }

    @Override
    public BillTotalDto queryTotal(StatisticsBillTotalQuery query) {
        return billRepository.queryTotal(query.getQuery(), query.getBillIds(), query.getBillInvalid(), query.getBillRefund(), query.getSupCpUnitId());
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds, String supCpUnitId) {
        return billRepository.listBillHand(billIds, supCpUnitId);
    }

    @Override
    public Boolean handReversal(Long billId, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getOne(new LambdaQueryWrapper<B>().eq(B::getId,billId).eq(StringUtils.isNotBlank(supCpUnitId),B::getSupCpUnitId,supCpUnitId));
//        B bill = billRepository.getById(billId);
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        BillHandA<B> billHandA = new BillHandA<>(bill);
        billHandA.handReversal();
        billRepository.update(billHandA.getBill(), new UpdateWrapper<B>().eq("id", billHandA.getBill().getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        billRepository.updateById(billHandA.getBill());
        return handRepository.handReversal(billHandA);
    }

    @Override
    public BillTotalDto queryBillReviewTotal(PageF<SearchF<?>> query, List<Long> billIds, String supCpUnitId) {
        QueryWrapper<?> queryWrapper;
        if (CollectionUtils.isNotEmpty(billIds)) {
            queryWrapper = new QueryWrapper<>().in("b.id", billIds);
            queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode());
        } else {
            queryWrapper = getApproveWrapper(query, query.getConditions().getQueryModel());
        }
        return billRepository.queryBillReviewTotal(queryWrapper, supCpUnitId);
    }

    @Override
    public List<BillInferenceV> listInferenceInfo(BillInferenceQuery map) {
        B bill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", map.getBillId())
                .eq(StringUtils.isNotBlank(map.getSupCpUnitId()), "sup_cp_unit_id", map.getSupCpUnitId()));
//        B bill = billRepository.getById(map.getBillId());
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        BillInferenceE billInferenceE = null;
        if (map.getActionEventCode() == 1) {
            if (BillTypeEnum.应付账单.getCode() == bill.getType()) {
                billInferenceE = billInferenceRepository.getByBillIdAndEventType(bill.getId(), bill.getType(), EventTypeEnum.应付计提.getEvent());
            } else if (BillTypeEnum.应收账单.getCode() == bill.getType()) {
                billInferenceE = billInferenceRepository.getByBillIdAndEventType(bill.getId(), bill.getType(), EventTypeEnum.应收计提.getEvent());
            }
        } else {
            billInferenceE = billInferenceRepository.getByBillIdAndType(bill.getId(), bill.getType());
        }
        BillInferenceV billInferenceV = new BillInferenceV();
        Global.mapperFacade.map(bill, billInferenceV);
        billInferenceV.setInferenceStatus(billInferenceE == null ? 0 : 1);
        return Collections.singletonList(billInferenceV);
    }

    @Override
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceQuery map) {
        // 获取账单列表
        List<B> bList = billRepository.list(new QueryWrapper<B>()
                .in("id", map.getBillIds())
                .eq(StringUtils.isNotBlank(map.getSupCpUnitId()), "sup_cp_unit_id", map.getSupCpUnitId()));
//        List<B> bList = billRepository.listByIds(map.getBillIds());
        if (CollectionUtils.isEmpty(bList)) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.msg());
        }
        // 获取账单对应的费项
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getByIdList(
                bList.stream().map(B::getChargeItemId).collect(Collectors.toList()));
        Map<Long, ChargeItemE> chargeItemMap = null;
        if (CollectionUtils.isNotEmpty(chargeItemList)) {
            chargeItemMap = chargeItemList.stream()
                    .collect(Collectors.toMap(ChargeItemE::getId, Function.identity()));
        }
        //
//        int chargeItemType = 0;
        List<BillInferenceE> billInferenceList = new ArrayList<>(bList.size());
//
//        Map<Long, ChargeItemE> finalChargeItemMap = chargeItemMap;

        // 查询账单推凭记录
        if (BillActionEventEnum.计提.getCode() == map.getActionEventCode()) {
            Integer billType = bList.get(0).getType();
            List<Long> incomeBillIds = bList.stream().map(B::getId).collect(Collectors.toList());
            if (BillTypeEnum.应收账单.getCode() == billType) {
                billInferenceList = billInferenceRepository.listByBillIdAndEventType(incomeBillIds, bList.get(0).getType(), EventTypeEnum.应收计提.getEvent());
            } else {
                billInferenceList = billInferenceRepository.listByBillIdAndEventType(incomeBillIds, bList.get(0).getType(), EventTypeEnum.应付计提.getEvent());
            }

        } else {
            billInferenceList = billInferenceRepository.listByBillIdAndType(bList.stream().map(B::getId).collect(Collectors.toList()), bList.get(0).getType());
        }

        Map<Long, BillInferenceE> billInferenceMap = billInferenceList.stream()
                .collect(Collectors.toMap(BillInferenceE::getBillId, Function.identity()));

        List<BillInferenceV> billInferenceVS = Global.mapperFacade.mapAsList(bList, BillInferenceV.class);
        billInferenceVS.forEach(billInferenceV -> {
            billInferenceV.setInferenceStatus(billInferenceMap.containsKey(billInferenceV.getId()) ? 0 : 1);
//            if ((BillActionEventEnum.计提.getCode() == map.getActionEventCode() ||
//                    BillActionEventEnum.结算.getCode() == map.getActionEventCode()) &&
//                    Objects.requireNonNull(finalChargeItemMap).containsKey(billInferenceV.getChargeItemId())) {
//                Integer type = finalChargeItemMap.get(billInferenceV.getChargeItemId()).getType();
//                billInferenceV.setChargeItemType((type == 1 || type == 2 || type == 3) ? 1 : 2);
//            } else {
//                billInferenceV.setChargeItemType(chargeItemType);
//            }
        });
        return billInferenceVS;
    }

    @Override
    public PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum) {
        List<Long> billIds = null;
        Page<BillInferenceV> page = billRepository.pageBillInferenceInfo(form, billIds);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    @Override
    public boolean reconcileBatch(List<ReconcileBatchCommand> reconcileBatchCommands) {
        reconcileBatchCommands.forEach(command -> {
            List<B> bills = billRepository.list(new QueryWrapper<B>()
                    .in("id", command.getBillIds())
                    .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//            List<B> bills = billRepository.listByIds(command.getBillIds());
            if (CollectionUtils.isNotEmpty(bills)) {
                bills.forEach(b -> b.reconcile(command.isResult()));
            }
            for (B bill : bills) {
                billRepository.update(bill, new UpdateWrapper<B>()
                        .eq("id", bill.getId())
                        .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
            }
//            billRepository.updateBatchById(bills);
        });
        return true;
    }

    @Override
    public IPage<B> getPageNoTenantLine(PageF<SearchF<?>> queryF) {
        return billRepository.queryBillByPageNoTenantLine(queryF);
    }

    @Override
    public boolean inferBatch(List<Long> billIds) {
        List<B> list = billRepository.listByIds(billIds);
        list.forEach(Bill::infer);
        billRepository.updateBatchById(list);
        return true;
    }

    @Override
    public Boolean carryover(CarryoverCommand carryoverInfo) {
        Long billId = carryoverInfo.getCarriedBillId();
        B bill = checkBillState(billId, carryoverInfo.getSupCpUnitId());
        //账单状态校验
        bill.applyCarryover();
        bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        //可结转余额不能小于结转金额
        ErrorAssertUtil.isTrueThrow402(bill.getRemainingCarriedAmount() >= carryoverInfo.getCarryoverAmount(), ErrorMessage.BILL_CARRYOVER_AMOUNT_NO_ENOUGH);

        //创建结转记录
        BillCarryoverE carryoverE = Global.mapperFacade.map(carryoverInfo, BillCarryoverE.class);
        carryoverE.setApproveTime(LocalDateTime.now());
        carryoverE.setCarriedBillNo(bill.getBillNo());
        if (carryoverInfo.getRemark() == null) {
            carryoverE.setRemark("");
        }

        //获取目标账单
        List<CarryoverDetail> carryoverDetail = carryoverE.getCarryoverDetail();
        Map<Integer, List<CarryoverDetail>> billTypeCollect = carryoverDetail.stream().collect(Collectors.groupingBy(CarryoverDetail::getBillType));
        ArrayList<B> targetBillList = new ArrayList<>();
        for (Map.Entry<Integer, List<CarryoverDetail>> entry : billTypeCollect.entrySet()) {
            Integer type = entry.getKey();
            BillRepository billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(type));
            List<CarryoverDetail> value = entry.getValue();
            List<Long> billIds = value.stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
            QueryWrapper<B> queryWrapper = new QueryWrapper<B>().in("id", billIds)
                    .eq("sup_cp_unit_id", carryoverInfo.getSupCpUnitId());
            List<B> billList = billRepository.list(queryWrapper);
            targetBillList.addAll(billList);
        }

        //账单结转
        BillCarryoverA<B, B> billCarryoverA = new BillCarryoverA<>(bill, null, carryoverE, targetBillList);
        billCarryoverA.carryover();

        //更新被结转账单
        List<GatherDetail> settleList = new ArrayList<>(billCarryoverA.getTargetSettles());
        List<B> updateBills = billCarryoverA.getTargetBills();
        Map<Integer, List<B>> billGroupByType = updateBills.stream().collect(Collectors.groupingBy(B::getType));
        for (Map.Entry<Integer, List<B>> entry : billGroupByType.entrySet()) {
            Integer type = entry.getKey();
            BillRepository billRepository = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(type));
            billRepository.updateBatchById(entry.getValue());
        }

        //保存账单信息
        carryoverRepository.save(billCarryoverA);
        gatherDetailRepository.saveBatch(settleList);
        billRepository.update(bill, new UpdateWrapper<B>()
                .eq("id", bill.getId())
                .eq(StringUtils.isNotBlank(carryoverInfo.getSupCpUnitId()), "sup_cp_unit_id", carryoverInfo.getSupCpUnitId()));
//        billRepository.updateById(bill);
        return true;
    }

    @Override
    public Boolean adjust(AdjustCommand command) {
        B bill = checkBillState(command.getBillId(), command.getSupCpUnitId());
        //账单状态校验
        bill.applyAdjust();
        bill.setApprovedState(BillApproveStateEnum.已审核.getCode());

        //创建调整记录
        BillAdjustE billAdjustE = Global.mapperFacade.map(command, BillAdjustE.class);
        billAdjustE.setApproveTime(LocalDateTime.now());
        billAdjustE.setBillAmount(bill.getTotalAmount());
        billAdjustE.setBillType(bill.getType());
        if (billAdjustE.getAdjustType() != null) {
            if (BillAdjustTypeEnum.减免.equalsByCode(billAdjustE.getAdjustType()) || BillAdjustTypeEnum.调低.equalsByCode(billAdjustE.getAdjustType())) {
                billAdjustE.setAdjustAmount(-billAdjustE.getAdjustAmount());
            }
        }
        //账单调整
        BillApproveE billApprove = new BillApproveE();
        billApprove.setLastApproveState(bill.getApprovedState());
        billApprove.setApprovedState(bill.getApprovedState());
        billApprove.setApprovedAction(command.getApprovedAction());
        billApprove.setChargeItemId(command.getChargeItemId());
        billApprove.setChargeItemName(command.getChargeItemName());
        BillAdjustA<B> billAdjustA = new BillAdjustA<>(bill, billApprove, billAdjustE);

        if (BillAdjustWayEnum.TAXAMOUNT_ADJUST.equalsByCode(command.getAdjustWay())) {
            doTaxamountAdjust(bill, command.getAdjustAmount());
        } else {
            billAdjustA.adjust(2);
            AdjustApproveListener<B> listener = new AdjustApproveListener<>();
            listener.saveBillInfo(bill, BillApproveStateEnum.已审核.getCode(), billAdjustA, billApprove);
        }
        //保存调整信息
        adjustRepository.save(billAdjustA);
        //更新账单状态
        billRepository.update(bill, new UpdateWrapper<B>()
                .eq("id", bill.getId())
                .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption<>(new PlainTextDataItem("审核内容：", true)))
                        .option(new ContentOption<>(new PlainTextDataItem("调整方式: ", false)))
                        .option(new ContentOption<>(new PlainTextDataItem(BillAdjustWayEnum.valueOfByCode(billAdjustA.getAdjustWay()).getValue(), true), OptionStyle.normal())));
        return true;
    }

    @Override
    public boolean invalid(InvalidCommand command) {
        B bill = checkBillState(command.getBillId(), command.getSupCpUnitId());
        bill.invalid();
        boolean flag = billRepository.update(bill, new UpdateWrapper<B>()
                .eq("id", bill.getId())
                .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id", command.getSupCpUnitId()));
//        boolean flag = billRepository.updateById(bill);
        if (flag) {
            handleOtherMq(bill, BillApproveOperateTypeEnum.作废.getCode());
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.作废,
                    new Content().option(new PlainTextDataItem("作废原因:")).option(new PlainTextDataItem(command.getReason())));
        }
        return flag;
    }

    @Override
    public BillBatchResultDto invalidBatch(List<Long> billIdList, String supCpUnitId) {
        List<B> billList = billRepository.list(new QueryWrapper<B>().in("id", billIdList).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        List<B> billList = billRepository.list(new LambdaQueryWrapper<B>().in(B::getId, billIdList).eq(B::getSupCpUnitId, supCpUnitId));
//        List<B> billList = billRepository.listByIds(billIdList);
        BillBatchResultDto billBatchResultDto = new BillBatchResultDto();
        billList.forEach(bill -> {
            try {
                bill.invalid();
                checkBillState(bill.getId(), supCpUnitId);
                billBatchResultDto.success();
            } catch (BizException e) {
                billBatchResultDto.fail();
            }
        });
        //更新账单数据
        for (B b : billList) {
            billRepository.update(b, new UpdateWrapper<B>().eq("id", b.getId()).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
        }
//        billRepository.updateBatchById(billList);
        batchHandleOtherMq(billList, BillApproveOperateTypeEnum.作废.getCode(), supCpUnitId);
        return billBatchResultDto;
    }

    @Override
    public Boolean editBill(EditBillF editBillF) {
        B bill ;
        //已经分表的账单
        if (StringUtils.isNotBlank(editBillF.getSupCpUnitId())) {
            bill = billRepository.getOne(new QueryWrapper<B>().eq("id", editBillF.getBillId())
                    .eq(BillSharedingColumn.应收账单.getColumnName(), editBillF.getSupCpUnitId()));
        } else {
            bill = billRepository.getById(editBillF.getBillId());
        }

        if (bill == null) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }

        if (bill.getSettleState() == BillSettleStateEnum.已结算.getCode()) {
            throw BizException.throw400("该账单已经完全结算");
        }
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            if (receivableBill.getOverdue() == 1 && "1".equals(receivableBill.getExtField1())) {
                log.info("已经调整金额的违约金账单不能再修改，billNo:{}", receivableBill.getBillNo());
                return false;
            }
        }
        if (editBillF.getTotalAmount() != null) {
            bill.setTotalAmount(editBillF.getTotalAmount());
            bill.resetReceivableAmount();
        }
        if (editBillF.getEndTime() != null) {
            if (bill instanceof ReceivableBill) {
                bill.setEndTime(editBillF.getEndTime());
            }
        }
        return billRepository.update(bill, new UpdateWrapper<B>().eq("id", editBillF.getBillId()).eq("sup_cp_unit_id", editBillF.getSupCpUnitId()));
//        return billRepository.updateById(bill);
    }

    @Override
    public Boolean editRecForBankSign(EditBillForBankSignF editBillF) {
        B bill = billRepository.getOne(new QueryWrapper<B>()
                .eq("id", editBillF.getBillId()).eq("sup_cp_unit_id", editBillF.getSupCpUnitId()));
        if (bill == null) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }
        if (editBillF.getTotalAmount() != null) {
            bill.setTotalAmount(editBillF.getTotalAmount());
            bill.resetReceivableAmount();
        }
        if (editBillF.getEndTime() != null) {
            if (bill instanceof ReceivableBill) {
                bill.setEndTime(editBillF.getEndTime());
            }
        }
        if (editBillF.getExtField1() != null) {
            if (bill instanceof ReceivableBill) {
                bill.setExtField1(editBillF.getExtField1());
            }
        }
        if (editBillF.getExtField2() != null) {
            if (bill instanceof ReceivableBill) {
                bill.setExtField2(editBillF.getExtField2());
            }
        }
        if (editBillF.getExtField5() != null) {
            if (bill instanceof ReceivableBill) {
                bill.setExtField5(editBillF.getExtField5());
            }
        }
        if (editBillF.getOverdue() != null) {
            if (bill instanceof ReceivableBill) {
                ((ReceivableBill) bill).setOverdue(editBillF.getOverdue());
            }
        }
        if (editBillF.getFreezeType() != null) {
            if (bill instanceof ReceivableBill) {
                ((ReceivableBill) bill).setFreezeType(editBillF.getFreezeType());
            }
        }
        if (editBillF.getState() != null) {
            if (bill instanceof ReceivableBill) {
                ((ReceivableBill) bill).setState(editBillF.getState());
            }
        }
        return billRepository.update(bill, new QueryWrapper<B>().eq("id", editBillF.getBillId())
                .eq("sup_cp_unit_id", editBillF.getSupCpUnitId()));
    }

    /**
     * 根据账单编号查询账单信息
     *
     * @param billNo 账单编号
     * @return {@link BillOjV}
     */
    @Override
    public BillOjV getBillInfoByBillNo(String billNo, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("bill_no", billNo).eq("sup_cp_unit_id", supCpUnitId));
        return Global.mapperFacade.map(bill, BillOjV.class);
    }


    @Override
    public BillStatusDetailVo statusDetailBill(BillDetailF billDetailF) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billDetailF.getBillId()).eq("sup_cp_unit_id", billDetailF.getSupCpUnitId()));
        return Global.mapperFacade.map(bill, BillStatusDetailVo.class);
    }

    /**
     * 校验账单
     *
     * @param billId 账单id
     * @return B
     */
    private B checkBillState(Long billId, String supCpUnitId) {
        B bill = billRepository.getOne(new QueryWrapper<B>().eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId));
//        B bill = billRepository.getById(billId);
        ErrorAssertUtil.notNullThrow300(bill, ErrorMessage.BILL_NOT_EXIST);
        BillApproveE approvingE = approveRepository.getApprovingAByBillId(billId, supCpUnitId);
        //存在审核中的审核记录不允许进行调整
        ErrorAssertUtil.isFalseThrow402(approvingE != null, ErrorMessage.BILL_IS_APPROVING);
        return bill;
    }



    /**
     * 初始化新增的账单
     */
    public BillGatherDetailA<B> initBill(B bill,
                                         LocalDateTime chargeTime,
                                         LocalDateTime chargeStartTime,
                                         LocalDateTime chargeEndTime,
                                         Integer settleWay,
                                         String settleChannel,
                                         String remark, String payeePhone, String payerPhone) {
        bill.init();
        //处理已经支付账单信息
        if (Objects.nonNull(bill.getSettleAmount()) && Objects.nonNull(settleWay) && Objects.nonNull(settleChannel)) {
            //重置结算金额
            Long settleAmount = bill.getSettleAmount();
            bill.setSettleAmount(0L);
            AddBillSettleCommand addBillSettleCommand = new AddBillSettleCommand();
            addBillSettleCommand.setPayeeId(bill.getPayeeId());
            addBillSettleCommand.setPayeeName(bill.getPayeeName());
            addBillSettleCommand.setPayeePhone(payeePhone);
            addBillSettleCommand.setPayerPhone(payerPhone);
            addBillSettleCommand.setPayerId(bill.getPayerId());
            addBillSettleCommand.setPayerType(bill.getPayerType());
            addBillSettleCommand.setPayerName(bill.getPayerName());
            addBillSettleCommand.setPayerLabel(bill.getPayerLabel());
            addBillSettleCommand.setSettleAmount(settleAmount);
            addBillSettleCommand.setSettleTime(chargeTime);
            addBillSettleCommand.setChargeStartTime(chargeStartTime);
            addBillSettleCommand.setChargeEndTime(chargeEndTime);
            addBillSettleCommand.setPayAmount(settleAmount);
            addBillSettleCommand.setSettleWay(SettleWayEnum.valueOfByCode(settleWay).getCode());
            addBillSettleCommand.setSettleChannel(SettleChannelEnum.valueOfByCode(settleChannel).getCode());
            addBillSettleCommand.setRemark(remark);
            BillGatherA<B> billGatherA = new BillGatherA(List.of(bill), List.of(addBillSettleCommand));
            // 初始化导入的收款单先处于待审核状态
            billGatherA.getGatherBill().setApprovedState(bill.getApprovedState());
            BillGatherDetailA<B> billSettleA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
            billSettleA.gather(addBillSettleCommand);
            return billSettleA;
        } else {
            if (Objects.isNull(bill.getSettleAmount())) {
                bill.setSettleAmount(0L);
            }
        }
        return null;
    }

    /**
     * 进行批量审核
     *
     * @param approveState 审核状态
     * @param rejectReason 原因
     * @param billList     账单集合
     */
    public void doApprovedBatch(BillApproveStateEnum approveState, String rejectReason, List<B> billList) {
        if (CollectionUtils.isNotEmpty(billList)) {
            List<BillApproveA<B>> billApproves = new ArrayList<>();
            billList.forEach(bill -> {
                BillApproveA<B> approveA = new BillApproveA<>(bill);
                approveA.initDefaultApprove().approve(approveState, rejectReason, null);
                bill.setInit(false);
                billApproves.add(approveA);
            });
            List<B> bills = billApproves.stream().map(BillApproveA::getBill).collect(Collectors.toList());
            boolean result = true;
            for (B bill : bills) {
                result = billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(bill.getSupCpUnitId()), "sup_cp_unit_id", bill.getSupCpUnitId()));
            }
//            boolean result = billRepository.updateBatchById(bills);
            approveRepository.saveBatch(Global.mapperFacade.mapAsList(billApproves, BillApproveE.class));
            //没有批量审核，故无需批量发送mq
            // todo 需要批量的时候调用
            // batchHandleOtherMq(billList, operateType);
            if (result) {
                //日志记录
                BizLog.normalBatch(bills.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                        LogContext.getOperator(), LogObject.账单, LogAction.生成审核, new Content());
            }
        }
    }

    /**
     * 进行批量删除操作
     *
     * @param billList           账单集合
     * @param billBatchResultDto 批量删除结果
     */
    private void doDeletedBatch(List<B> billList, BillBatchResultDto billBatchResultDto, String supCpUnitId) {
        List<Long> billIdList = billList.stream().filter(s -> s.getAppNumber().equals(AppNumberEnum.收费系统.getValue())).map(B::getId).collect(Collectors.toList());
        List<BillApproveE> billApproveList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(billIdList)) {
            billApproveList = approveRepository.queryApprovedRecord(billIdList, supCpUnitId);
        }
        Map<Long, List<BillApproveE>> collect = billApproveList.stream().collect(Collectors.groupingBy(BillApproveE::getBillId));
        ArrayList<B> deleteBillList = new ArrayList<>(billList);
        billList.forEach(bill -> {
            try {
                if (CollectionUtils.isNotEmpty(collect.get(bill.getId()))) {
                    //反审核的账单不允许删除
                    billBatchResultDto.fail();
                    deleteBillList.remove(bill);
                } else if (bill.delete()) {
                    billBatchResultDto.success();
                } else {
                    billBatchResultDto.fail();
                    deleteBillList.remove(bill);
                }
            } catch (BizException e) {
                billBatchResultDto.fail();
                deleteBillList.remove(bill);
            }
        });
        //更新账单数据
        for (B b : deleteBillList) {
            billRepository.update(new UpdateWrapper<B>()
                    .eq("id", b.getId())
                    .eq(StringUtils.isNotBlank(b.getSupCpUnitId()), "sup_cp_unit_id", supCpUnitId)
                    .set("deleted", "1"));
            // 收款单和明细进行删除
            invalidGatherInfos(b);
        }
//        billRepository.removeBatchByIds(deleteBillList);
        List<Long> recBillIdList = deleteBillList.stream().map(B::getId).collect(Collectors.toList());
    }

    /**
     * 调整税额
     */
    private void doTaxamountAdjust(B bill, Long adjustAmount) {
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
            receivableBill.setTaxAmount(adjustAmount);
        } else if (bill instanceof PayableBill) {
            PayableBill payableBill = (PayableBill) bill;
            payableBill.setTaxAmount(adjustAmount);
        }
    }

    private QueryWrapper<B> getWrapper(Long id, String supCpUnitId) {
        QueryWrapper<B> bQueryWrapper = new QueryWrapper<>();
        bQueryWrapper.eq("id", id).eq("sup_cp_unit_id", supCpUnitId);
        return bQueryWrapper;
    }

    private QueryWrapper<B> getWrapper(List<Long> ids, String supCpUnitId) {
        QueryWrapper<B> bQueryWrapper = new QueryWrapper<>();
        bQueryWrapper.in("id", ids).eq("sup_cp_unit_id", supCpUnitId);
        return bQueryWrapper;
    }




    @Override
    @Transactional
    public ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchCommand<?> billApplyBatchCommand) {
        ErrorAssertUtil.isTrueThrow402(!BillApproveOperateTypeEnum.结转
                .equals(billApplyBatchCommand.getApproveOperateType()));
//        if (BillApproveOperateTypeEnum.作废.equals(billApplyBatchCommand.getApproveOperateType())) {
//            billApplyBatchCommand.setReason("账单作废");
//        }
        List<Long> billIds = billApplyBatchCommand.getBillIds();
        QueryWrapper<B> queryWrapper = new QueryWrapper<B>().in("id", billIds)
                .eq("sup_cp_unit_id", billApplyBatchCommand.getSupCpUnitId());
        List<B> bills = billRepository.list(queryWrapper);
        ErrorAssertUtil.notEmptyThrow400(bills, ErrorMessage.BILL_NOT_EXIST);
        List<BillApproveE> billApproveList = approveRepository.getApprovingByBillIdList(billIds, billApplyBatchCommand.getSupCpUnitId());
        Map<Long, List<BillApproveE>> collect = billApproveList.stream().collect(Collectors.groupingBy(BillApproveE::getBillId));
        Map<Long, Object> detailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(billApplyBatchCommand.getDetails())) {
            detailMap = billApplyBatchCommand.getDetails().stream()
                    .collect(Collectors.toMap(BillDetailBase::getBillId, Function.identity()));
        }
        ArrayList<BillApproveE> approveSaveList = new ArrayList<>();
        List<String> bids = new ArrayList<>();
        for (B bill : bills) {
            List<BillApproveE> approveList = collect.get(bill.getId());
            BillApproveA<B> approvingA = new BillApproveA<>(bill).init(CollectionUtils.isNotEmpty(approveList) ?
                    approveList.get(0) : null);
            BillApplyCommand applyCommand = new BillApplyCommand(bill.getId(),
                    billApplyBatchCommand.getReason(),
                    billApplyBatchCommand.getApproveOperateType(),
                    billApplyBatchCommand.getOutApproveId(), null,
                    detailMap.get(bill.getId()),
                    billApplyBatchCommand.getSupCpUnitId(),
                    billApplyBatchCommand.getOperationRemark());
            if (BillApproveStateEnum.待审核.equalsByCode(bill.getApprovedState())) {
                //账单未审核进行调整，判断是否审核过（处理反审的情况，将上次审核状态设置为已审核）
                if (approveRepository.hasApproved(bill.getId(), bill.getSupCpUnitId())) {
                    applyCommand.setLastApprovedState(BillApproveStateEnum.已审核.getCode());
                }
            }
            approvingA.apply(applyCommand, BillApplyListenerFactory.getListener(applyCommand.getApproveOperateType()));
            BillApproveE approveE = approvingA.getApproveE();
            approveE.setSupCpUnitId(billApplyBatchCommand.getSupCpUnitId());
            approveE.setOperationId(billApplyBatchCommand.getOperationId());
            approveSaveList.add(approveE);
            bids.add(String.valueOf(bill.getId()));
        }
        for (B bill : bills) {
            billRepository.update(bill, new UpdateWrapper<B>().eq("id", bill.getId()).eq(StringUtils.isNotBlank(billApplyBatchCommand.getSupCpUnitId()), "sup_cp_unit_id", billApplyBatchCommand.getSupCpUnitId()));
        }
        boolean applyRes = approveRepository.saveBatch(approveSaveList);
        List<?> details = billApplyBatchCommand.getDetails();
        List<Long> billAdjustIds = new ArrayList<>();
        if (applyRes && CollectionUtils.isNotEmpty(details)) {
            Object fist = details.get(0);
            if (fist instanceof BillAdjustE) {
                List<BillAdjustE> details1 = (List<BillAdjustE>) details;
                adjustRepository.saveBatch(details1);
                billAdjustIds = details1.stream().map(BillAdjustE::getId).collect(Collectors.toList());
            } else if (fist instanceof BillRefundE) {
                refundRepository.saveBatch((List<BillRefundE>) details);
            } else if (fist instanceof BillJumpE) {
                jumpRepository.saveBatch((List<BillJumpE>) details);
            }
        }
        ApplyBatchDeductionV applyBatchDeductionV = new ApplyBatchDeductionV();
        List<Long> approveIds = approveSaveList.stream().map(BillApproveE::getId).collect(Collectors.toList());
        applyBatchDeductionV.setApproveIds(approveIds);
        applyBatchDeductionV.setBillAdjustIds(billAdjustIds);
        return applyBatchDeductionV;
    }

    @Override
    public boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        return false;
    }

    /**
     * 性能不如saveBatch+updateBatchById，量大不建议使用
     *
     * @param receivableBillList
     * @return
     */
    @Override
    public Boolean syncBatchByCommunity(List<ReceivableBill> receivableBillList) {
        receivableBillList.forEach(bill->bill.setPath(spacePermissionAppService.getSpacePath(bill.getRoomId())));
        return receivableBillRepository.saveOrUpdateBatch(receivableBillList);
    }

    @Override
    public Boolean addReceivableBatch(List<ReceivableBill> receivableBillList) {
        receivableBillList.forEach(bill->bill.setPath(spacePermissionAppService.getSpacePath(bill.getRoomId())));
        receivableBillRepository.saveBatch(receivableBillList);
        return true;
    }

    @Override
    public Boolean syncBatchUpdateByCommunity(List<ReceivableBill> receivableBillList) {
        return receivableBillRepository.updateBatchById(receivableBillList);
    }

    @Override
    public Boolean updateBillInfoByIds(List<Long> billIds,String supCpUnitId,Integer status) {
        return billRepository.update(new UpdateWrapper<B>().set("settle_state",status)
                .in("id",billIds).eq("sup_cp_unit_id",supCpUnitId));
    }

    @Override
    public void setAdjustInfo(List<ReceivableBillPageV> list) {
        List<Long> billId = list.stream().map(ReceivableBillPageV::getId).collect(Collectors.toList());
        List<BillAdjustE> billAdjustES = adjustRepository.listByReductionApprove(billId);
        if(CollectionUtils.isNotEmpty(billAdjustES)){
            billAdjustES.stream().forEach(adjust ->{
                Long adjustBillId = adjust.getBillId();
                List<ReceivableBillPageV> collect = list.stream().filter(bill -> adjustBillId.equals(bill.getId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect)){
                    ReceivableBillPageV billv = collect.get(0);
                    billv.setAdjustAmount(new BigDecimal(adjust.getAdjustAmount()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP) );
                    billv.setAdjustRatio(adjust.getAdjustRatio() + "%");
                    billv.setAdjustCreatorName(adjust.getCreatorName());
                }
            });
        }
    }

    @Override
    public Boolean addAdvanceBill(AdvanceBill advanceBill) {
        advanceBill.setPath(spacePermissionAppService.getSpacePath(advanceBill.getRoomId()));
        return billRepository.save((B) advanceBill);
    }




    /**
     * 拆分账单(部分结算账单)
     * @param billExecuteSplitF 账单详情
     * @return
     */
    public Boolean splitPartialSettlement(BillExecuteSplitF billExecuteSplitF) {
        //获取原账单
        B recBill = billRepository.getOne(new QueryWrapper<B>().eq("id", billExecuteSplitF.getBillId())
                .eq("sup_cp_unit_id", billExecuteSplitF.getSupCpUnitId()));
        //新账单、旧账单
        BillExecuteSplitDetailF newBill = null, oldBill = null;
        for (BillExecuteSplitDetailF billT:billExecuteSplitF.getExecuteSplitDetailves()) {
            if (Boolean.TRUE.equals(billT.getIsAdjustSplit())) {
                oldBill = billT;
            } else {
                newBill = billT;
            }
        }
        /* 挂账*/
        ErrorAssertUtils.isFalseThrow400(recBill.getOnAccount() != 0,"已挂账状态账单不可拆分");
        /* 初步校验：修改后账单账单金额 =？ 应收减免 + 实收减免金额 + 实收金额  如果不相等存在前端数据统计异常or存在操作修改了账单金额信息*/
        ErrorAssertUtils.isTrueThrow400(oldBill.getTotalAmount().multiply(Const.BIG_DECIMAL_HUNDRED).longValue()
                        - (recBill.getDeductibleAmount() + recBill.getSettleAmount() + recBill
                        .getDiscountAmount()) >= 0, "拆分账单1的账单金额需要大于等于原账单的实收金额与已减免金额之和");
        /* 原账单金额 = 2个账单金额的总和*/
        ErrorAssertUtils.isFalseThrow400(recBill.getTotalAmount() - ((oldBill.getTotalAmount().add(newBill.getTotalAmount())).multiply(new BigDecimal("100")).longValue()) != 0L,"抱歉，此账单已被其他用户修改，请刷新页面以查看最新信息，并重新进行操作");
        //克隆账单
        B newB = (B)recBill.clone();
        /**制作新账单 newB*/
        this.doGenerateNewBill(newB,newBill,billExecuteSplitF);
        /**调整旧账单 recBill*/
        this.adjustOldBill(recBill,oldBill,billExecuteSplitF);
        //do 新账单入库、旧账单修改部分信息
        Global.ac.getBean("billDomainService",BillDomainServiceImpl.class).saveAndUpdate(newB,recBill,billRepository);
        //bizLog记录账单变更记录
        this.bizLogSplitPartialSettlement(newB,recBill,billExecuteSplitF);
        return true;
    }


    /**
     *
     * @param newB
     * @param recBill
     * @param billExecuteSplitF
     */
    private void bizLogSplitPartialSettlement(B newB,B recBill,BillExecuteSplitF billExecuteSplitF){
        //日志记录
        BizLog.normal(String.valueOf(newB.getId()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成,
                new Content().option(new ContentOption(new PlainTextDataItem("由账单编号为", true)))
                        .option(new ContentOption(new PlainTextDataItem(recBill.getBillNo(), true)))
                        .option(new ContentOption(new PlainTextDataItem("拆分生成", false))));
        // 原账单动态更新
        BizLog.normal(String.valueOf(billExecuteSplitF.getBillId()),
                LogContext.getOperator(), LogObject.账单, LogAction.拆分,
                new Content().option(new ContentOption(new PlainTextDataItem("该账单拆分为", true)))
                        .option(new ContentOption(new PlainTextDataItem(recBill.getBillNo().toString(), true)))
                        .option(new ContentOption(new PlainTextDataItem("和" + newB.getBillNo(), false)))
                        .option(new ContentOption(new PlainTextDataItem("两个账单", false))));
    }


    /**
     * 调整旧账单 旧账单：修改账单表(结算状态、账单金额、应收金额、开始时间结束时间)
     * @param recBill
     * @param oldBill
     */
    private void adjustOldBill(B recBill,BillExecuteSplitDetailF oldBill,BillExecuteSplitF billExecuteSplitF) {
        /**开始时间*/
        recBill.setStartTime(oldBill.getStartTime());
        /**结束时间*/
        recBill.setEndTime(oldBill.getEndTime().withHour(23).withMinute(59).withSecond(59));
        /** 账单总金额 */
        recBill.setTotalAmount(oldBill.getTotalAmount().multiply(new BigDecimal(100)).longValue());
        /** 应收金额 = 账单金额 - 应收减免 */
        recBill.setReceivableAmount(oldBill.getTotalAmount().multiply(new BigDecimal(100)).longValue() - recBill.getDeductibleAmount());
        /** 备注 */
        recBill.setRemark(this.doRemark(recBill.getRemark(),oldBill.getRemark()));
        /** 归属月 */
        recBill.setAccountDate(LocalDate.parse(oldBill.getAccountDate() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (!org.springframework.util.CollectionUtils.isEmpty(billExecuteSplitF.getRoomMessage())) {
            /** 设置收款对象 */
            this.setCharInfos(billExecuteSplitF, oldBill, recBill);
        }
        /** 结算状态（0未结算，1部分结算，2已结算） */
        // recBill.setSettleState(BillSettleStateEnum.已结算.getCode());
        recBill.refresh();
    }

    /**
     * 备注拼接
     * @param oldRemark
     * @param newRemark
     * @return
     */
    private String doRemark(String oldRemark,String newRemark){
        return (oldRemark != null && !oldRemark.isEmpty()) ?
                ((newRemark != null && !newRemark.isEmpty()) ?
                        oldRemark + "," + newRemark : oldRemark) : (newRemark != null && !newRemark.isEmpty() ? newRemark : "");
    }


    /**
     * 新账单入库
     * 就账单维护部分数据(修改账单表(结算状态、账单金额、应收金额、开始时间结束时间)、修改收款单表、收款明细表)
     * @param newB 新账单
     * @param recBill 原账单
     */
    @Transactional
    public void saveAndUpdate(B newB ,B recBill,BR billRepositoryTmp){
        //更新账单表[receivable_bill]
        boolean update = billRepositoryTmp.update(recBill, new UpdateWrapper<B>()
                .eq("id", recBill.getId())
                .eq("sup_cp_unit_id", recBill.getSupCpUnitId())
                //临时处理方案:根据更新时间来初步校验当前数据是否被其他操作变更数据
                .eq("gmt_modify", recBill.getGmtModify()));
        ErrorAssertUtils.isTrueThrow400(update,"抱歉，此账单已被其他用户修改，请刷新页面以查看最新信息，并重新进行操作");
        //当前账单涉及的收款明细
        List<GatherDetail> byRecBillId = gatherDetailRepository.getListByRecBillId(recBill.getId(), recBill.getSupCpUnitId());
        final List<Long> gatherBillIds = byRecBillId.stream().map(GatherDetail::getGatherBillId).distinct().collect(Collectors.toList());
        final List<Long> gatherDetailIds = byRecBillId.stream().map(GatherDetail::getId).distinct().collect(Collectors.toList());
        //更新当前账单关联的收款明细开始结束时间
        this.updateGatherDetailTime(recBill,gatherDetailIds);
        //收款明细关联的所有收款明细（增加获取其他收款单的下明细）
        byRecBillId = gatherDetailRepository.getListByGatherBillIds(gatherBillIds, recBill.getSupCpUnitId());
        //key：收款单id ，value:明细记录
        final Map<Long, List<GatherDetail>> collect = byRecBillId.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
        //更新收款单的开始、结束时间
        this.updateGatherBill(collect,recBill.getSupCpUnitId());
        //新账单入库
        billRepositoryTmp.save(newB);
    }


    /**
     * 更新收款单表的开始结束时间
     *
     * @param collect
     * @param supCpUnitId
     */
    private void updateGatherBill(Map<Long, List<GatherDetail>> collect, String supCpUnitId){
        collect.forEach((x,y)->{
            LocalDateTime minChargeStartTime = y.stream()
                    .map(GatherDetail::getChargeStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
            LocalDateTime maxChargeEndTime = y.stream()
                    .map(GatherDetail::getChargeEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            //更新收款单表
            gatherBillRepository.update(new LambdaUpdateWrapper<GatherBill>().eq(GatherBill::getId,x)
                            .eq(GatherBill::getSupCpUnitId,supCpUnitId)
                    .set(GatherBill::getStartTime,minChargeStartTime)
                    .set(GatherBill::getEndTime,maxChargeEndTime));
        });
    }


    /**
     * 账单关联的收款明细修改开始结束时间
     * @param recBill
     */
    private void updateGatherDetailTime(B recBill,List<Long> gatherDetailIds){
        LocalDateTime endTime = recBill.getEndTime().with(LocalTime.of(23, 59, 59));
        //更细收款单明细表
        gatherDetailRepository.update(new LambdaUpdateWrapper<GatherDetail>()
                .in(GatherDetail::getId,gatherDetailIds)
                .eq(GatherDetail::getSupCpUnitId,recBill.getSupCpUnitId())
                .set(GatherDetail::getChargeStartTime, recBill.getStartTime())
                .set(GatherDetail::getChargeEndTime, endTime));
    }


    /**
     * 根据旧账单制作新账单
     * @param bill
     * @return
     */
    private B doGenerateNewBill(B bill,BillExecuteSplitDetailF newBill,BillExecuteSplitF billExecuteSplitF){
        /**自动加载账单id*/
        bill.setIdentifier(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        /**自动加载账单编号*/
        bill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        /**加载初始化信息*/
        bill.setState(Const.State._0);
        /**根据规则设定账单金额and应收金额*/
        bill.setTotalAmount(newBill.getTotalAmount().multiply(Const.BIG_DECIMAL_HUNDRED).longValue());
        /**应收金额*/
        bill.setReceivableAmount(bill.getTotalAmount());
        if (!org.springframework.util.CollectionUtils.isEmpty(billExecuteSplitF.getRoomMessage())) {
            /** 设置收款对象 */
            this.setCharInfos(billExecuteSplitF, newBill, bill);
        }
        /** 结转状态：0未结转，1待结转，2部分结转，3已结转 */
        bill.setCarriedState(0);
        /** 结转金额 */
        bill.setCarriedAmount(0L);
        /**应收减免金额*/
        bill.setDeductibleAmount(0L);
        /**实收减免金额*/
        bill.setDiscountAmount(0L);
        /**实收金额*/
        bill.setSettleAmount(0L);
        /**未结算*/
        bill.setSettleState(0);
        /** 支付时间 */
        bill.setPayTime(null);
        /** 退款状态（0未退款，1退款中，2部分退款，已退款） */
        bill.setRefundState(0);
        /** 退款金额*/
        bill.setRefundAmount(0L);
        if(bill instanceof ReceivableBill){
            /** 支付信息 */
            ((ReceivableBill) bill).setPayInfos(Lists.newArrayList());
            /** 缴费时间 */
            ((ReceivableBill) bill).setChargeTime(null);
        }
//        /** 挂账状态 */
//        bill.setState(0);
//        /** 是否挂账：0未挂账，1已挂账 */
//        bill.setOnAccount(0);
        /**开票金额 100 开100 */
        bill.setInvoiceAmount(0L);
        /** 未开票 */
        bill.setInvoiceState(0);
        /** 开票类型 */
        bill.setInvoiceType("[]");
        /** 备注 */
        bill.setRemark(newBill.getRemark());
        /** 账单开始时间 */
        bill.setStartTime(newBill.getStartTime());
        /** 账单结束时间 */
        bill.setEndTime(newBill.getEndTime().withHour(23).withMinute(59).withSecond(59));
        /** 归属月（账期） */
        bill.setAccountDate(LocalDate.parse(newBill.getAccountDate() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        /*校验是否满足账单唯一性*/
        if (BillTypeEnum.应收账单.getCode()==billExecuteSplitF.getBillType()) {
            ErrorAssertUtils.isFalseThrow400(
                    !this.uniqueImportBill(bill, billExecuteSplitF.getSupCpUnitId()),
                    "系统中存在与" + bill.getBillNo()
                            + "编号相同房号、费项、计费周期、收费对象的账单，无法拆分。");
        }
        /** 创建人ID */
        bill.setCreator(LogContext.getOperator().getId());
        /** 创建时间 */
        bill.setGmtCreate(LocalDateTime.now());
        /** 创建人*/
        bill.setCreatorName(LogContext.getOperator().getName());
        return bill;
    }





    /**
     * 拆分账单
     *
     * @param billExecuteSplitF 账单详情
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean billSplit(BillExecuteSplitF billExecuteSplitF) {

        // 1.更改原账单状态
        BatchEditBillF editRec = new BatchEditBillF();
        editRec.setBillIds(List.of(billExecuteSplitF.getBillId()));
        editRec.setState(BillStateEnum.作废.getCode());
        editRec.setSupCpUnitId(billExecuteSplitF.getSupCpUnitId());
        Boolean result = this.batchEditBill(editRec);
        ErrorAssertUtil.isTrueThrow402(result, ErrMsgEnum.FAILED,"原账单暂不可更新，请稍后重试");

        // 获取原账单和子账单
        List<BillExecuteSplitDetailF> splitDetailves = billExecuteSplitF.getExecuteSplitDetailves();
        B recBill = billRepository.getOne(new QueryWrapper<B>().eq("id",billExecuteSplitF.getBillId()).eq("sup_cp_unit_id", billExecuteSplitF.getSupCpUnitId()));
        // 获取原账单金额 (单位/分)
        final BigDecimal totalAmount = BigDecimal.valueOf(recBill.getTotalAmount());
        final BigDecimal receiveAmount = BigDecimal.valueOf(recBill.getReceivableAmount());

        // 排除金额低等于1分的账单
        ErrorAssertUtil.isFalseThrow402((totalAmount.compareTo(BigDecimal.ONE) <= 0 || receiveAmount.compareTo(BigDecimal.ONE) <= 0),
                ErrMsgEnum.FAILED,"请不要选取账单金额或应收金额过低的账单进行拆分哦");

        // 原账单编号获取
        final String billNo = recBill.getBillNo();
        final Long id = recBill.getId();
        //应收减免金额
        final Long deductibleAmount = recBill.getDeductibleAmount();

        // 搜集子账单
        List<String> bList = new ArrayList<>();
        for (BillExecuteSplitDetailF detailF : splitDetailves) {
            B bill = recBill;
            // 自动加载账单id
            bill.setIdentifier(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
            // 自动加载账单编号
            bill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
//            bill.setBillNo(UUID.randomUUID().toString());
            bList.add(bill.getBillNo());
            // 加载初始化信息
            bill.setState(Const.State._0);
            // 根据规则设定账单金额and应收金额
            bill.setTotalAmount(detailF.getTotalAmount().multiply(Const.BIG_DECIMAL_HUNDRED).longValue());
            bill.setReceivableAmount(bill.getTotalAmount());
            if(bill instanceof ReceivableBill){
                BigDecimal chargingArea = detailF.getChargingArea();
                if(Objects.nonNull(chargingArea)){
                    ((ReceivableBill) bill).setChargingArea(chargingArea);
                }
            }

            // 减免金额，应收金额处理
            if (detailF.getIsAdjustSplit()) {
                this.adjustSplit(bill, id,deductibleAmount);
            }else {
                //应收减免金额
                bill.setDeductibleAmount(0L);
                //实收减免金额
                bill.setDiscountAmount(0L);
            }

            // 设置收款对象
            if (!org.springframework.util.CollectionUtils.isEmpty(billExecuteSplitF.getRoomMessage())){
                this.setCharInfos(billExecuteSplitF, detailF, bill);
            }

            bill.setRemark(detailF.getRemark());
            bill.setStartTime(detailF.getStartTime());
            bill.setEndTime(detailF.getEndTime().withHour(23).withMinute(59).withSecond(59));
            // 归属月
            String belongToMonth = detailF.getAccountDate() + "-01";
            bill.setAccountDate(LocalDate.parse(belongToMonth, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            // 校验是否满足账单唯一性
            if (BillTypeEnum.应收账单.getCode()==billExecuteSplitF.getBillType()) {
                if (!this.uniqueImportBill(bill, billExecuteSplitF.getSupCpUnitId())) {
                    throw BizException.throw403(
                            "系统中存在与拆分后账单相同房号、费项、计费周期、收费对象的账单，遂无法拆分");
                }
            }
            bill.setCreator(LogContext.getOperator().getId());
            bill.setGmtCreate(LocalDateTime.now());
            bill.setCreatorName(LogContext.getOperator().getName());
            // TODO: 2023/8/22 账单表存在一个是否金额与标准对等的字段
            if (bill instanceof ReceivableBill) {
                ((ReceivableBill)bill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(bill.getRoomId()));
            }else if (bill instanceof TemporaryChargeBill) {
                ((TemporaryChargeBill)bill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(bill.getRoomId()));
            }else if (bill instanceof AdvanceBill){
                ((AdvanceBill)bill).setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(bill.getRoomId()));
            }
            /* 往来单位需要查询org接口 */
            if (VoucherBillCustomerTypeEnum.开发商.getCode() == detailF.getCustomerType()){
                String mainDataCode = detailF.getCustomerId();
                CustomerF customer = new CustomerF();
                CustomerV customerV = orgClient.getByMainDataCode(customer.setMainDataCode(mainDataCode));
                if (Objects.nonNull(customerV)){
                    bill.setCustomerName(customerV.getName());
                    bill.setPayerName(customerV.getName());
                    bill.setPayerId(customerV.getMainDataCode());
                    bill.setPayerType(VoucherBillCustomerTypeEnum.开发商.getCode() );
                }
            }
            bill.refresh();
            billRepository.save(bill);
            // //中交
            // if (TenantUtil.bf2()) {
            //     //中交拆单数据同步到推凭表
            //     this.voucherPushBillDetailZJ(bill,id);
            // }
            //日志记录
            BizLog.normal(String.valueOf(bill.getId()),
                    LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("由账单编号为", true)))
                            .option(new ContentOption(new PlainTextDataItem(billNo, true)))
                            .option(new ContentOption(new PlainTextDataItem("拆分生成", false))));
        }

        // 原账单动态更新
        BizLog.normal(String.valueOf(billExecuteSplitF.getBillId()),
                LogContext.getOperator(), LogObject.账单, LogAction.拆分,
                new Content().option(new ContentOption(new PlainTextDataItem("该账单拆分为", true)))
                        .option(new ContentOption(new PlainTextDataItem(bList.get(0), true)))
                        .option(new ContentOption(new PlainTextDataItem("和"+bList.get(1), false)))
                        .option(new ContentOption(new PlainTextDataItem("两个账单", false))));

        return true;
    }

    /**
     * 中交拆单数据同步到推凭表
     */
    private void voucherPushBillDetailZJ(B bill,Long id){
        VoucherPushBillDetailZJ one = voucherBillDetailZJRepository.getOne(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getBillId, id)
                .eq(VoucherPushBillDetailZJ::getDeleted, 0)
        );
        if(Objects.nonNull(one)){
               VoucherPushBillDetailZJ voucherPushBillDetailZJ = new VoucherPushBillDetailZJ();
               voucherPushBillDetailZJ.setBillId(bill.getId());
               voucherPushBillDetailZJ.setCommunityId(bill.getCommunityId());
               voucherPushBillDetailZJ.setCommunityName(bill.getCommunityName());
               voucherPushBillDetailZJ.setAccountDate(bill.getAccountDate());
               voucherPushBillDetailZJ.setBillType(bill.getType());
               voucherPushBillDetailZJ.setChargeItemId(bill.getChargeItemId());
               voucherPushBillDetailZJ.setChargeItemName(bill.getChargeItemName());
               voucherPushBillDetailZJ.setRoomName(bill.getRoomName());
               voucherPushBillDetailZJ.setRoomId(bill.getRoomId());
               voucherPushBillDetailZJ.setPayeeId(bill.getPayeeId());
               voucherPushBillDetailZJ.setPayeeName(bill.getPayeeName());
               voucherBillDetailZJRepository.save(voucherPushBillDetailZJ);
        }
    }

    /**
     * 主账单减免记录拆分
     * 账单金额计算
     *
     * @param bill    子账单
     * @param id 原账单id
     * @param deductibleAmount 减免金额
     */
    private void adjustSplit(B bill, Long id,Long deductibleAmount) {
        List<BillAdjustE> billAdjustEList = billAdjustRepository.listByApproveBillId(id);
        if (CollectionUtils.isNotEmpty(billAdjustEList)){
            // 如果主账单的调整总金额小于等于子账单金额时才可以转移减免
            long oldDeductAmount = billAdjustEList.stream().mapToLong(BillAdjustE::getAdjustAmount).reduce(0L, Long::sum);
            ErrorAssertUtil.isTrueThrow402(bill.getTotalAmount()+oldDeductAmount>=0,ErrMsgEnum.FAILED,"拆分失败，继承调整信息账单的账单金额需≥原账单的调整总金额");
            // 将主账单的减免记录归置标记的子账单
            billAdjustRepository.update(new LambdaUpdateWrapper<BillAdjustE>().in(BillAdjustE::getId,billAdjustEList.stream().map(BillAdjustE::getId).collect(Collectors.toList()))
                    .set(BillAdjustE::getBillId,bill.getId()));

        }
        bill.setReceivableAmount(bill.getTotalAmount()-deductibleAmount);
    }


    /**
     * 设置收费对象信息
     * @param batchSplitF 拆分参数
     * @param detailF 拆分子账单详情
     * @param bill 新账单
     */
    private void setCharInfos(BillExecuteSplitF batchSplitF, BillExecuteSplitDetailF detailF, B bill) {
        if (StringUtils.isBlank(detailF.getCustomerId())){return;}
        Map<String, List<ResidentEnterpriseV>> roomMessage = batchSplitF.getRoomMessage();
        List<ResidentEnterpriseV> residentEnterpriseVS = roomMessage.get(VoucherBillCustomerTypeEnum.valueOfByCode(detailF.getCustomerType()).getValue());

        if (CollectionUtils.isNotEmpty(residentEnterpriseVS)){
            // 处理企业业主，企业租客，开发商
            List<ResidentEnterpriseV> collect = residentEnterpriseVS.stream().filter(a -> detailF.getCustomerId().equals(a.getEnterpriseId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)){
                ResidentEnterpriseV residentEnterpriseV = collect.get(0);
                /** 付款方ID */
                bill.setPayerId(residentEnterpriseV.getEnterpriseId());
                /** 付款方名称 */
                bill.setPayerName(residentEnterpriseV.getEnterpriseName());
                /** 付款方类型 */
                bill.setPayerType(detailF.getCustomerType());
                if (bill instanceof ReceivableBill){
                    ((ReceivableBill) bill).setPayerPhone(residentEnterpriseV.getEnterprisePhone());
                }else if (bill instanceof TemporaryChargeBill){
                    ((TemporaryChargeBill) bill).setPayerPhone(residentEnterpriseV.getEnterprisePhone());
                }else if (bill instanceof AdvanceBill){
                    ((AdvanceBill) bill).setPayerPhone(residentEnterpriseV.getEnterprisePhone());
                }
            }else {
                // 处理普通业主租客
                collect = residentEnterpriseVS.stream().filter(a -> detailF.getCustomerId().equals(a.getResidentId())).collect(Collectors.toList());
                ErrorAssertUtil.isTrueThrow402(CollectionUtils.isNotEmpty(collect),ErrMsgEnum.FAILED,"收费对象"+detailF.getCustomerId()+"不存在");
                ResidentEnterpriseV residentEnterpriseV = collect.get(0);
                /** 付款方ID */
                bill.setPayerId(residentEnterpriseV.getResidentId());
                /** 付款方名称 */
                bill.setPayerName(residentEnterpriseV.getName());
                /** 付款方类型 */
                bill.setPayerType(detailF.getCustomerType());
                if (bill instanceof ReceivableBill){
                    ((ReceivableBill) bill).setPayerPhone(residentEnterpriseV.getPhone());
                }else if (bill instanceof TemporaryChargeBill){
                    ((TemporaryChargeBill) bill).setPayerPhone(residentEnterpriseV.getPhone());
                }else if (bill instanceof AdvanceBill){
                    ((AdvanceBill) bill).setPayerPhone(residentEnterpriseV.getPhone());
                }
            }
        }else {
            if (ObjectUtils.allNull(bill.getPayerId(),bill.getPayerName(),bill.getPayerType())){
                throw BizException.throw403("拆分后的两个子账单都不存在收费对象");
            }
            bill.setPayerId(null);
            bill.setPayerName(null);
            bill.setPayerType(null);
        }
    }

    /**
     * 批量编辑账单
     * @param editBillF 编辑参数
     * @return {@link Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchEditBill(BatchEditBillF editBillF) {
        boolean result = true;
        List<B> bill = billRepository.list(new QueryWrapper<B>().in("id", editBillF.getBillIds()).eq("sup_cp_unit_id", editBillF.getSupCpUnitId()));
        if (bill == null || CollectionUtils.isEmpty(bill)) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }
        if (bill.stream().filter(a->a.getSettleState()== BillSettleStateEnum.已结算.getCode()).count() > 0) {
            throw BizException.throw400("存在完全结算的账单");
        }
        for (B b : bill) {
            result = billRepository.update(b,new UpdateWrapper<B>()
                    .eq("id", b.getId())
                    .eq(StringUtils.isNotBlank(editBillF.getSupCpUnitId()),"sup_cp_unit_id", editBillF.getSupCpUnitId())
                    .set("state",editBillF.getState()));
            if (!result){
                return result;
            }
        }
        return result;
    }


    /**
     * 批量编辑账单
     * @param editBillF 编辑参数
     * @return {@link Boolean}
     */
    public Boolean editBill(BatchEditBillF editBillF) {
        boolean result = true;
        List<B> bill = billRepository.list(new QueryWrapper<B>().in("id", editBillF.getBillIds()).eq("sup_cp_unit_id", editBillF.getSupCpUnitId()));
        if (bill == null || CollectionUtils.isEmpty(bill)) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }
        if (bill.stream().filter(a->a.getSettleState()== BillSettleStateEnum.已结算.getCode()).count() > 0) {
            throw BizException.throw400("存在完全结算的账单");
        }
        for (B b : bill) {
            result = billRepository.update(b,new UpdateWrapper<B>()
                    .eq("id", b.getId())
                    .eq(StringUtils.isNotBlank(editBillF.getSupCpUnitId()),"sup_cp_unit_id", editBillF.getSupCpUnitId())
                    .set("state",editBillF.getState()));
            if (!result){
                return result;
            }
        }
        return result;
    }

    /**
     * 拆分后校验账单唯一
     *
     * @param bill        校验对象
     * @param supCpUnitId 上级收费单元id
     * @return {@link Boolean}
     */
    public boolean uniqueImportBill(B bill, String supCpUnitId) {
        // 判断系统中已存在与该编号账单相同房号、费项、计费周期、收费对象的账单
        QueryWrapper<B> wrapper = new QueryWrapper<B>()
                .ne("carried_state", BillCarriedStateEnum.已结转.getCode())
                .eq("deleted", Const.State._0)
                .eq("is_init", Const.State._0)
                .eq("reversed", Const.State._0)
                .ne("state", BillStateEnum.作废.getCode())
                .ne("refund_state", BillRefundStateEnum.已退款.getCode())
                .eq("start_time", bill.getStartTime())
                .eq("end_time", bill.getEndTime())
                .eq("charge_item_id", bill.getChargeItemId())
                .eq("sup_cp_unit_id", supCpUnitId)
                //是否是违约金：0-否/1-是
                .eq("overdue",0)
                .eq("room_id", bill.getRoomId());
        if (StringUtils.isNotBlank(bill.getPayerId())){
            wrapper.eq("payer_id", bill.getPayerId());
        }else {
            wrapper.eq("payer_id", null);
        }
        long count = billRepository.count(wrapper);
        if (count != 0){
            return false;
        }
        return true;
    }
}
