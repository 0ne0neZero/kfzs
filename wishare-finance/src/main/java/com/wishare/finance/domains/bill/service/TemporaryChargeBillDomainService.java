package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.BillDeductionF;
import com.wishare.finance.apps.model.bill.fo.UnFreezeBatchF;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.utils.RegexUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TemporaryChargeBillDomainService extends BillDomainServiceImpl<TemporaryChargeBillRepository, TemporaryChargeBill> {

    /**
     * 分页查询临时收费导出账单列表
     *
     * @param queryF queryF
     * @return IPage
     */
    public IPage<TempChargeBillExportDto> queryExportDataPage(PageF<SearchF<?>> queryF) {
        List<Integer> operateTypeValue = (List<Integer>) queryF.getConditions().getSpecialMap().get("operate_type");
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());
        if (CollectionUtils.isNotEmpty(operateTypeValue) ){
            return billRepository.queryInitialApprovedExportDataPage(queryF,getApproveWrapper(queryF,queryWrapper));
        }else{
            return billRepository.queryExportDataPage(queryF,billRepository.getWrapper(queryF));
        }
    }

    @Override
    public boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        if (CollectionUtils.isEmpty(unFreezeBatchF.getBillIds())) {
            throw BizException.throw400("解冻账单id不能为空");
        }
        return batchUnFreezeBill(unFreezeBatchF.getBillIds(), unFreezeBatchF.getSupCpUnitId());
    }

    private boolean batchUnFreezeBill(List<Long> billIds, String supCpUnitId) {
        ErrorAssertUtil.notEmptyThrow400(billIds, ErrorMessage.BILL_BATCH_QUERY_ERROR);
        QueryWrapper<TemporaryChargeBill> queryWrapper = new QueryWrapper<TemporaryChargeBill>().in("id", billIds)
                .eq("sup_cp_unit_id", supCpUnitId);
        List<TemporaryChargeBill> temporaryChargeBills = Optional.ofNullable(billRepository.list(queryWrapper))
                .orElse(new ArrayList<>())
                .stream()
                .filter(e -> BillStateEnum.冻结.getCode() == e.getState()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(temporaryChargeBills)) {
            return true;
        }
        temporaryChargeBills.forEach(TemporaryChargeBill::unfreeze);
        boolean result = billRepository.updateBatchById(temporaryChargeBills);
        if (result){
            BizLog.initiateBatch(temporaryChargeBills.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.解冻, new Content());
        }
        return result;
    }

    /**
     * 根据账单ids获取临时收费信息
     *
     * @param billIds
     * @return
     */
    public List<TempChargeBillMoreInfoDto> tempChargeBillInfo(List<Long> billIds, String supCpUnitId) {
        List<TemporaryChargeBill> temporaryChargeBillList = billRepository.list(new QueryWrapper<TemporaryChargeBill>()
                .in("id",billIds).eq("sup_cp_unit_id",supCpUnitId).eq("deleted", Const._0));
        List<TempChargeBillMoreInfoDto> tempChargeBillMoreInfoDtos = Global.mapperFacade.mapAsList(temporaryChargeBillList, TempChargeBillMoreInfoDto.class);
        tempChargeBillMoreInfoDtos.forEach(receivableInfo -> {
            //获取结算记录 暂时，后续优化循环
            //获取结算记录 暂时，后续优化循环
            List<GatherDetail> gatherDetailList = gatherDetailRepository.listByRecBillId(receivableInfo.getId(), supCpUnitId);
            List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
            gatherDetailList.forEach(gatherDetail -> {
                BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
                billSettleDtoList.add(billSettleDto);
            });
            receivableInfo.setBillSettleDtoList(billSettleDtoList);
        });
        return tempChargeBillMoreInfoDtos;
    }

    /**
     * 根据账单ids获取临时账单结算详情
     *
     * @param billIds
     * @return
     */
    public SettleDetailDto settleDetail(List<Long> billIds, String supCpUnitId) {
        //需校验是否是同项目,同个收费对象的账单
        QueryWrapper<TemporaryChargeBill> queryWrapper = new QueryWrapper<TemporaryChargeBill>().in("id", billIds)
                .eq("sup_cp_unit_id", supCpUnitId);
        List<TemporaryChargeBill> temporaryChargeBillList = billRepository.list(queryWrapper);
        ErrorAssertUtil.notNullThrow300(temporaryChargeBillList, ErrorMessage.BILL_NOT_EXIST);
        //批量缴费需要限制相同法定单位以及账单来源
        checkBillDetail(temporaryChargeBillList);
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByRecBillIds(billIds, supCpUnitId);
        return new SettleDetailDto().temporarySettleDetail(temporaryChargeBillList,gatherDetailList);
    }

    /**
     * 批量缴费需要限制相同法定单位以及账单来源
     *
     * @param temporaryChargeBillList
     */
    private void checkBillDetail(List<TemporaryChargeBill> temporaryChargeBillList) {
        //校检账单中法定单位是否相同
        List<Long> statutoryBodyIds = temporaryChargeBillList.stream().map(TemporaryChargeBill::getStatutoryBodyId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(statutoryBodyIds) && statutoryBodyIds.size() != 1) {
            throw BizException.throw400("请选择同法定单位的账单进行缴费");
        }
    }


    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param query
     * @return
     */
    public BillTotalDto statisticsBillRefund(StatisticsBillTotalQuery query) {
        return billRepository.statisticsBillRefund(query);
    }


    /**
     * 设置账单引用
     * @param billId
     * @param referenceState
     * @return
     */
    public boolean reference(Long billId, BillReferenceStateEnum referenceState, String supCpUnitId) {
        TemporaryChargeBill bill = billRepository.getOne(new QueryWrapper<TemporaryChargeBill>()
                .eq("id", billId).eq("sup_cp_unit_id", supCpUnitId));
        if (bill.reference(referenceState)){
            return billRepository.update(bill, new UpdateWrapper<TemporaryChargeBill>()
                    .eq("id", billId).eq(StringUtils.isNotBlank(supCpUnitId),"sup_cp_unit_id",supCpUnitId));
        }
        return false;
    }

    /**
     * 批量保存临时账单命令
     *
     * @param commands 批量保存临时账单命令
     * @return boolean
     */
    @Override
    public boolean  saveBatch(List<AddBillCommand<TemporaryChargeBill>> commands) {
        List<TemporaryChargeBill> bills = commands.stream().map(AddBillCommand::getBill).collect(Collectors.toList());
        ArrayList<GatherDetail> gatherDetailList = new ArrayList<>();
        ArrayList<GatherBill> gatherBillList = new ArrayList<>();
        //自动生成账单id和编码
        bills.forEach(bill -> bill.init(TenantUtil.curTag()));

        // 根据 备注信息修改账单状态 并获取映射信息
        // 原始账单和拆分账单的billNo 映射
        Map<String, List<String>> mp = new HashMap<>();
        // 拆分账单和原始账单的billNo 映射
        Map<String, String> splitMp = new HashMap<>();
        Map<String, String> originIdMap = new HashMap<>();

        List<TemporaryChargeBill> originalBills = new ArrayList<>();

        // 不要合并两个foreach  先要获取到所有的originIdMap
        bills.forEach(data -> {
            String remark = data.getRemark();
            if (remark != null && remark.contains("[ORIGIN_ID:")) {
                // 原始对象
                data.setState(BillStateEnum.作废.getCode());
                String originId = RegexUtils.extractId(remark, "ORIGIN_ID");
                originIdMap.put(originId, data.getBillNo());
                data.setRemark(remark.replace("[ORIGIN_ID:" + originId + "]", "").trim());
                originalBills.add(data);
            }
        });

        List<TemporaryChargeBill> splitBills = new ArrayList<>();
        Iterator<TemporaryChargeBill> iterator = bills.iterator();
        while (iterator.hasNext()) {
            TemporaryChargeBill bill = iterator.next();
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

        for (TemporaryChargeBill temporaryChargeBill : bills) {
            Optional.ofNullable(temporaryChargeBill.getNegativeCommission())
                    .filter(a->Const.State._1==a).ifPresent(a->{temporaryChargeBill.setPayTime(LocalDateTime.now());
                        temporaryChargeBill.setSettleWay(SettleWayEnum.线下.getCode());
                        //temporaryChargeBill.setSettleWay(SettleWayEnum.线上.getCode());
                        /** 结算渠道 */
                        temporaryChargeBill.setSettleChannel(SettleChannelEnum.银行托收.getCode());});
                        //temporaryChargeBill.setSettleChannel(SettleChannelEnum.通联微信扫码.getCode());});
            if(Objects.nonNull(temporaryChargeBill.getPayTime())){
                temporaryChargeBill.setSettleState(BillSettleStateEnum.已结算.getCode());
                /**临时已结算的账单生成结算记录*/
                BillGatherDetailA<TemporaryChargeBill> billSettleE = this.createBillSettle(temporaryChargeBill);
                gatherDetailList.add(billSettleE.getGatherDetail());
                // 初始化导入的收款单先处于待审核状态
                billSettleE.getGatherBill().setApprovedState(temporaryChargeBill.getApprovedState());
                gatherBillList.add(billSettleE.getGatherBill());
            }
        }
        gatherDetailRepository.saveBatch(gatherDetailList);
        gatherBillRepository.saveBatch(gatherBillList);
        billRepository.saveBatch(bills);

        // 同步临时账单到风梦行
        List<TemporaryChargeBill> billList = bills.stream().filter(a -> StringUtils.isNotBlank(a.getContractNo())).collect(Collectors.toList());
        if (TenantUtil.bf2() && CollectionUtils.isNotEmpty(billList)){
            billContractAppService.syncBill(billList);
        }

        BizLog.normalBatch(gatherBillList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        // 第一次生成操作记录不要生成拆分账单的操作记录
        if(CollectionUtils.isNotEmpty(splitBills)){
            bills.removeAll(splitBills);
        }
        BizLog.normalBatch(bills.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());

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
                List<String> bList = mp.get(billNo);
                if (CollectionUtils.isEmpty(bList)) {
                    return;
                }
                Content content = new Content();
                content.option(new ContentOption(new PlainTextDataItem("该账单拆分为", true)));
                for(int i = 0; i < bList.size(); i++) {
                    if (i ==0){
                        content.option(new ContentOption(new PlainTextDataItem(bList.get(0), true)));
                    }else{
                        content.option(new ContentOption(new PlainTextDataItem("、"+bList.get(i), false)));
                    }
                }
                content.option(new ContentOption(new PlainTextDataItem("  "+ String.valueOf(bList.size())+"个账单", false)));

                BizLog.normal(String.valueOf(bill.getId()),
                        LogContext.getOperator(), LogObject.账单, LogAction.拆分,
                        content);
            });
        }

        return true;
    }


    @Override
    public boolean updateBatch(List<UpdateBillCommand<TemporaryChargeBill>> commands) {
        List<TemporaryChargeBill> bills = commands.stream().map(UpdateBillCommand::getBill).collect(Collectors.toList());
        billRepository.updateBatch(bills);
        return true;
    }

    /**
     * 分页查询临时账单审核列表
     *
     * @param queryF
     * @return PageV
     */
    public IPage<TemporaryChargeBill> getPageNotApprove(PageF<SearchF<?>> queryF) {
        return billRepository.getPageNotApprove(queryF, getApproveWrapper(queryF, billRepository.getWrapper(queryF)));
    }

    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    public List<RoomBillTotalDto> roomBills(List<Long> roomIdList) {
        return billRepository.roomBills(roomIdList);
    }

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList 房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear 是否统计当年
     * @return List
     */
    public List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear) {
        return billRepository.roomChargeBills(roomIdList, chargeItemIdList, currentYear);
    }

    /**
     * 创建结算记录数据
     *
     * @param bill 预收账单
     * @return BillSettleE
     */
    private BillGatherDetailA<TemporaryChargeBill> createBillSettle(TemporaryChargeBill bill) {
        AddBillSettleCommand addBillSettleCommand = new AddBillSettleCommand();
        addBillSettleCommand.setPayeeId(Objects.isNull(bill.getStatutoryBodyId()) ? bill.getPayeeId() : bill.getStatutoryBodyId().toString());
        addBillSettleCommand.setPayeeName(Objects.isNull(bill.getStatutoryBodyName()) ?  bill.getPayeeName() : bill.getStatutoryBodyName());
        addBillSettleCommand.setPayerId(bill.getPayerId());
        addBillSettleCommand.setPayerName(bill.getPayerName());
        addBillSettleCommand.setPayAmount(bill.getTotalAmount());
        addBillSettleCommand.setSettleAmount(bill.getTotalAmount());
        /** 结算方式(0线上，1线下) */
        addBillSettleCommand.setSettleWay(Objects.isNull(bill.getSettleWay()) ? SettleWayEnum.线上.getCode() : bill.getSettleWay());
        /** 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他） */
        addBillSettleCommand.setSettleChannel(StringUtils.isEmpty(bill.getSettleChannel()) ? SettleChannelEnum.其他.getCode() : bill.getSettleChannel());
        /** 结算时间 */
        addBillSettleCommand.setSettleTime(Objects.nonNull(bill.getPayTime()) ? bill.getPayTime() : LocalDateTime.now());
        addBillSettleCommand.setChargeStartTime(bill.getStartTime());
        addBillSettleCommand.setChargeEndTime(bill.getEndTime());
        BillGatherA<TemporaryChargeBill> billGatherA = new BillGatherA(List.of(bill),List.of(addBillSettleCommand));
        BillGatherDetailA<TemporaryChargeBill> billSettleA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
        billSettleA.gather(addBillSettleCommand);
        return billSettleA;
    }

    /**
     * 处理临时账单数据(将临时账单数据同步到应收账单表，只需要调用一次)
     *
     * @return Integer
     */
    public Boolean dealData() {
        List<TemporaryChargeBill> list = billRepository.getDealDate();
        list.forEach( temporaryChargeBill -> {
            temporaryChargeBill.setBillType(BillTypeEnum.临时收费账单.getCode());
            temporaryChargeBill.setPath(spacePermissionAppService.getSpacePath(temporaryChargeBill.getRoomId()));
        });
        return billRepository.saveBatch(list);
    }

    public Boolean deduct(BillDeductionF deductionF) {
        TemporaryChargeBill bill = billRepository.getOne(new QueryWrapper<TemporaryChargeBill>().eq("id", deductionF.getBillId())
                .eq("sup_cp_unit_id", deductionF.getSupCpUnitId()));
        if (bill == null) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }
        //在账单状态为正常，审核状态为已审核，部分结算/已结算后，可有扣款按钮
        if (bill.getState()!=0||bill.getApprovedState()!=2||bill.getSettleState()==0){
            throw BizException.throw400(ErrorMessage.BILL_NOT_DEDUCT.getErrMsg());
        }
        //收费系统-押金收费类型的费项的临时账单
        ChargeItemE chargeItemE = chargeItemRepository.getOne(new QueryWrapper<ChargeItemE>().eq("id", bill.getChargeItemId()));
        if (chargeItemE==null||chargeItemE.getType()!=3){
            throw BizException.throw400(ErrorMessage.BILL_CHARGE_NOT_DEDUCT.getErrMsg());
        }
        //可扣款金额校验 收款金额-已退款金额-已扣款金额-已结转金额
        Long deductMoney = bill.getSettleAmount()-bill.getRefundAmount()-bill.getDeductionAmount()-bill.getCarriedAmount();
        long deductionAmountNew = deductionF.getDeductionAmount().multiply(new BigDecimal("100")).longValue();
        if (deductMoney < deductionAmountNew){
            throw BizException.throw400(ErrorMessage.BILL_MONEY_NOT_DEDUCT.getErrMsg());
        }
        //金额修改
        bill.setDeductionAmount(bill.getDeductionAmount()+deductionAmountNew);
        boolean save = billRepository.update(new UpdateWrapper<TemporaryChargeBill>().eq("id",bill.getId())
                .eq("sup_cp_unit_id", deductionF.getSupCpUnitId()).set("deduction_amount",bill.getDeductionAmount()));
        if (!save){
            throw BizException.throw300("修改扣款金额失败！");
        }
        BillDeductionE billDeductionE = new BillDeductionE();
        billDeductionE.generateIdentifier();
        billDeductionE.setBillId(bill.getId());
        billDeductionE.setBillType(bill.getType());
        billDeductionE.setDeductionAmount(deductionAmountNew);
        billDeductionE.setRemark(deductionF.getRemark());
        billDeductionE.setCreator(LogContext.getOperator().getId());
        billDeductionE.setCreatorName(LogContext.getOperator().getName());
        billDeductionE.setGmtCreate(LocalDateTime.now());
        billDeductionE.setOperator(LogContext.getOperator().getId());
        billDeductionE.setOperatorName(LogContext.getOperator().getName());
        billDeductionE.setGmtModify(LocalDateTime.now());
        billDeductionE.setTenantId(bill.getTenantId());
        save = billDeductionRepository.save(billDeductionE);
        // 将扣款金额记录进最近的收款单
        // 收款单资源库
        //收款明细资源库
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        // 获取最新的一条收款明细记录
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(deductionF.getBillId(), deductionF.getSupCpUnitId());
        long deductAmount = deductionF.getDeductionAmount().multiply(new BigDecimal("100")).longValue();
        getherDetailDeduct(gatherDetails,deductAmount,gatherDetailRepository,gatherBillRepository);
        if (!save){
            throw BizException.throw300("添加扣款明细失败！");
        }
        // 原账单动态更新
        BizLog.normal(String.valueOf(bill.getId()),
                LogContext.getOperator(), LogObject.账单, LogAction.扣款,
                new Content().option(new ContentOption(new PlainTextDataItem("账单发起扣款", true)))
                        .option(new ContentOption(new PlainTextDataItem("扣款金额为："+deductionF.getDeductionAmount()+"元", true))));
        return save;
    }

    /**
     * 扣款更新收款详情数据
     *
     * @param gatherDetailList       detail数据 已经按时间倒序排序
     * @param deductAmount           扣款金额
     * @param gatherDetailRepository repository
     * @param gatherBillRepository
     */
    private void getherDetailDeduct(List<GatherDetail> gatherDetailList, Long deductAmount, GatherDetailRepository gatherDetailRepository, GatherBillRepository gatherBillRepository) {
        if (CollectionUtils.isNotEmpty(gatherDetailList)){
            // 已扣减金额
            Long deductedMoney = 0L;
            for (GatherDetail gatherDetail : gatherDetailList) {
                // 获取该笔收款详情的具体收款金额（分）
                long recPayAmount = gatherDetail.getRecPayAmount() - gatherDetail.getRefundAmount() - gatherDetail.getCarriedAmount() - gatherDetail.getDeductionAmount();
                if (recPayAmount > 0) {
                    if (recPayAmount <= deductAmount - deductedMoney) {
                        gatherDetail.setDeductionAmount(recPayAmount);
                        // 关联的收款主表更新扣款金额
                        gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",gatherDetail.getGatherBillId()).set("deduction_amount",recPayAmount));
                        deductedMoney += recPayAmount;
                    } else {
                        gatherDetail.setDeductionAmount(deductAmount - deductedMoney);
                        gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",gatherDetail.getGatherBillId()).set("deduction_amount",deductAmount - deductedMoney));
                        break;
                    }
                }
            }
            // 更新收款详情单数据
            gatherDetailList.forEach(v -> {
                gatherDetailRepository.update(v, new QueryWrapper<GatherDetail>()
                        .eq("id", v.getId())
                        .eq("sup_cp_unit_id", v.getSupCpUnitId()));
            });
        }
    }

    @Override
    public List<Long> approveBatch(BatchApproveBillCommand command, Integer billType) {
        List<Long> billIds = Optional.ofNullable(command.getBillIds()).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(billIds)) {
            PageF<SearchF<?>> query = command.getQuery();
            ErrorAssertUtil.notNullThrow300(query, ErrorMessage.BILL_BATCH_QUERY_ERROR);
            QueryWrapper<?> wrapper = billRepository.getWrapper(query);
            Page<Object> page = Page.of(1, 1000);
            wrapper.orderByAsc("b.id");
            Long maxId = null;
            int totalSize;
            do {
                if (Objects.nonNull(maxId)) {
                    query.getConditions().getFields().add(new Field("b.id", maxId, 3));
                }
                IPage<TemporaryChargeBill> billPage = billRepository.listByInitialBill(page, wrapper);
                List<TemporaryChargeBill> records = billPage.getRecords();
                records.forEach(bill->{
                    if (TenantUtil.bf4()){
                        if (StringUtils.isBlank(bill.getCostCenterName())){
                            throw BizException.throw400("账单"+bill.getBillNo()+"未获取到成本中心，无法审核通过");
                        }
                        if (StringUtils.isBlank(bill.getPayerId()) && StringUtils.isBlank(bill.getPayerName())){
                            throw BizException.throw400("账单"+bill.getBillNo()+"没有收费对象，无法审核通过");
                        }
                    }
                });
                totalSize = records.size();
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(records)) {
                    maxId = records.get(records.size() - 1).getId();
                    doApprovedBatch(command.getApproveState(), command.getRejectReason(), billPage.getRecords());
                    billIds.addAll(
                            Optional.ofNullable(billPage.getRecords()).orElse(new ArrayList<>())
                                    .stream().map(TemporaryChargeBill::getId).collect(Collectors.toList())
                    );
                }
            } while (totalSize == 1000);
        } else {
            List<TemporaryChargeBill> billList =
                    billRepository.list(new QueryWrapper<TemporaryChargeBill>()
                    .eq(StringUtils.isNotBlank(command.getSupCpUnitId()), "sup_cp_unit_id",
                            command.getSupCpUnitId()).in("id", billIds));
            billList.forEach(bill->{
                if (TenantUtil.bf4()){
                    if (StringUtils.isBlank(bill.getCostCenterName())){
                        throw BizException.throw400("账单"+bill.getBillNo()+"未获取到成本中心，无法审核通过");
                    }
                    if (StringUtils.isBlank(bill.getPayerId()) && StringUtils.isBlank(bill.getPayerName())){
                        throw BizException.throw400("账单"+bill.getBillNo()+"没有收费对象，无法审核通过");
                    }
                }
            });
            doApprovedBatch(command.getApproveState(), command.getRejectReason(), billList);
        }
        return billIds;
    }

    public Boolean  deleteInitBill(PageF<SearchF<?>> queryF) {
        List<Field> fieldList = queryF.getConditions().getFields().stream().filter(e -> "b.sup_cp_unit_id".equals(e.getName())).collect(Collectors.toList());
        Field field = fieldList.get(0);
        String supCpUnitId = field.getValue().toString();
        QueryWrapper wrapper = billRepository.getWrapper(queryF);
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        wrapper.eq("b.is_init", 1);
        wrapper.in("b.approved_state", List.of(0,1));
        List<Long> initBillIds = billRepository.getBaseMapper().initBillIds(wrapper);
        if(CollectionUtils.isNotEmpty(initBillIds)){
            billRepository.update(new LambdaUpdateWrapper<TemporaryChargeBill>()
                    .eq(Bill::getSupCpUnitId,supCpUnitId).in(Bill::getId, initBillIds).set(Bill::getDeleted, 1));
        }
        return true;

    }

    public List<Long> getBillRoomIds(String communityId, Long chargeItemId) {
        return billRepository.getBillRoomIds(communityId, chargeItemId);
    }

}
