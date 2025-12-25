package com.wishare.finance.domains.bill.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.finance.apps.model.bill.fo.AdvanceMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.fo.BillSettleChannelInfo;
import com.wishare.finance.apps.model.bill.fo.UnFreezeBatchF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.configure.businessunit.BusinessUnitAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.BillApproveA;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.AddAdvanceBillCommand;
import com.wishare.finance.domains.bill.command.AddBillCommand;
import com.wishare.finance.domains.bill.command.BatchApproveBillCommand;
import com.wishare.finance.domains.bill.command.BillSettleCommand;
import com.wishare.finance.domains.bill.command.BillUnitaryEnterCommand;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.AdvanceBillMapper;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.gateway.bill.BillGatherAGateway;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportAdvanceBillData;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 预收账单领域Service
 *
 * @author yancao
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdvanceBillDomainService extends BillDomainServiceImpl<AdvanceBillRepository, AdvanceBill> {

    @Setter(onMethod_ = @Autowired)
    protected BillGatherAGateway<AdvanceBill> billGatherAGateway;

    @Setter(onMethod_ = @Autowired)
    protected SpacePermissionAppService spacePermissionAppService;

    @Setter(onMethod_ = @Autowired)
    protected BusinessUnitAppService businessUnitAppService;

    private final SharedBillAppService sharedBillAppService;

    private final String FANG_YUAN = "fangyuan";

    @Override
    public List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId) {
        //List<GatherBill> gatherBills = new ArrayList<>();
//        List<GatherDetail> gatherDetails = new ArrayList<>();
        List<AdvanceBill> advanceBills = new ArrayList<>();
        for (BillUnitaryEnterCommand unitaryEnterF : enterCommands) {
            BillSettleCommand settleInfo = unitaryEnterF.getSettleInfo();
            String tradeNo = settleInfo.getTradeNo();
            if(StringUtils.isNotBlank(tradeNo)){
                //防止移动端同一笔订单流水重复插入
                Integer count = gatherBillRepository.queryCountByTradeNo(tradeNo,supCpUnitId);
                if(Objects.nonNull(count) && count > 0){
                    log.info("统一入账tradeNo重复----->tradeNo:{}",tradeNo);
                    continue;
                }
            }else{
                log.info("统一入账tradeNo为空----->unitaryEnterF:{}", JSON.toJSONString(unitaryEnterF));
                continue;
            }
            AdvanceBill advanceBill = Global.mapperFacade.map(unitaryEnterF, AdvanceBill.class);
            initAvdanceBillBySettleInfo(advanceBill,settleInfo);
            advanceBill.init();
            advanceBill.setPath(spacePermissionAppService.getSpacePath(advanceBill.getRoomId()));
            advanceBills.add(advanceBill);
            /*if (Objects.nonNull(settleInfo)){
                GatherBill gatherBill = settleInfo.buildGatherBill(advanceBill);
                //gatherBills.add(gatherBill);
                List<GatherDetail> gds = settleInfo.buildGatherDetails(advanceBill, gatherBill);
                if (CollectionUtils.isNotEmpty(gds)){
                    gatherDetails.addAll(gds);
                }
            }*/
        }
        if(CollectionUtils.isEmpty(advanceBills)){
            throw BizException.throw400("预收支付流水订单已存在");
        }
        billRepository.saveBatch(advanceBills);
        //if (CollectionUtils.isNotEmpty(gatherBills)){
        //    gatherBillRepository.saveBatch(gatherBills);
        //}
        //收款明细表gather_bill_id收款单id对应为advance_bill主键id
       /* for (int i = 0; i <gatherDetails.size(); i++) {
            gatherDetails.get(i).setGatherBillId(advanceBills.get(i).getId());
        }
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.saveBatch(gatherDetails);
        }*/
        saveGatherInfo(advanceBills,1);
        return CollectionUtils.isNotEmpty(advanceBills) ? advanceBills.stream().map(i->{
            BillUnitaryEnterResultDto billUnitaryEnterResultDto = new BillUnitaryEnterResultDto();
            GatherDetail detail = gatherDetailRepository.getByRecBillId(i.getId(), supCpUnitId);
            billUnitaryEnterResultDto.setId(detail.getGatherBillId());
            billUnitaryEnterResultDto.setBillType(BillTypeEnum.收款单.getCode());
            return billUnitaryEnterResultDto;
        }).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public boolean save(AddBillCommand<AdvanceBill> command) {
        AdvanceBill bill = command.getBill();
        bill.init();
        bill.setReceivableAmount(bill.getTotalAmount());
        bill.setSettleAmount(bill.getTotalAmount());
        if(bill.getPayTime() == null){
            bill.setPayTime(LocalDateTime.now());
        }
//        if(Objects.nonNull(command.getApprovedFlag()) && command.getApprovedFlag()){
//            bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
//        }
        bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        bill.setSettleState(BillSettleStateEnum.已结算.getCode());
        bill.setGmtCreate(LocalDateTime.now());
        bill.setPayWay(Objects.isNull(bill.getPayWay()) ? command.getSettleWay() : bill.getPayWay());
        bill.setPayChannel(StringUtils.isBlank(bill.getPayChannel()) ? command.getPayChannel() : bill.getPayChannel());
        bill.setSupCpUnitId(bill.getCommunityId());
        bill.setSupCpUnitName(bill.getCommunityName());
        bill.setCpUnitId(bill.getRoomId());
        bill.setCpUnitName(bill.getRoomName());
        bill.setPreferentialAmount(bill.getPreferentialAmount());
        bill.setDiscountAmount(bill.getDiscountAmount());
        //预收账单创建结算记录
        GatherBill gatherBill = createGatherBill(bill);
        GatherDetail billSettleE = createBillSettle(bill, gatherBill);
        billSettleE.setPayeePhone(command.getPayeePhone());
        billSettleE.setPayerPhone(command.getPayerPhone());
        gatherDetailRepository.save(billSettleE);
        //记录日志
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        bill.setPath(spacePermissionAppService.getSpacePath(bill.getRoomId()));
        return billRepository.save(bill);
    }

    @Override
    public Boolean addAdvanceBill(AdvanceBill advanceBill) {
        advanceBill.setPath(spacePermissionAppService.getSpacePath(advanceBill.getRoomId()));
        billRepository.save(advanceBill);
        return true;
    }

    public List<AdvanceBillAllDetailV> addBatch(List<AddAdvanceBillCommand> billList) {
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isEmpty(billList), ErrorMessage.BILL_ADD_LENGTH_ERROR);
        List<AdvanceBill> advanceBillList = new ArrayList<>();
        //是否进行周期校验
        boolean flag = true;
        //批量缴费转预收没有周期，不进行周期校验
        ArrayList<List<String>> roomIdAndChargeIdList = new ArrayList<>();
        for (AddAdvanceBillCommand command : billList) {
            AdvanceBill bill = Global.mapperFacade.map(command, AdvanceBill.class);
            //是否已审核 true已审核，false待审核
            if(Objects.nonNull(command.getApprovedFlag()) && command.getApprovedFlag()){
                bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            }
            bill.setPayWay(command.getSettleWay());
            bill.setPayChannel(command.getSettleChannel());
            bill.setTotalAmount(command.getTotalAmount());
            bill.setSettleAmount(command.getTotalAmount()-command.getPreferentialAmount()-command.getPresentAmount());
            Assert.validate(()->bill.getSettleAmount()>=0,()->BizException.throw400("账单实收金额不能低于0"));
            bill.setDeductibleAmount(command.getPreferentialAmount()+command.getPresentAmount());
            //自动生成账单id和编码
            bill.init();
            bill.setSettleState(BillSettleStateEnum.已结算.getCode());
            bill.setSupCpUnitId(bill.getCommunityId());
            bill.setSupCpUnitName(bill.getCommunityName());
            bill.setCpUnitId(bill.getRoomId());
            bill.setCpUnitName(bill.getRoomName());
            bill.refresh();
            roomIdAndChargeIdList.add(Arrays.asList(bill.getRoomId(), bill.getChargeItemId().toString()));
            if(Objects.isNull(bill.getStartTime()) || Objects.isNull(bill.getEndTime())){
                flag = false;
            }
            bill.setPath(spacePermissionAppService.getSpacePath(bill.getRoomId()));
            advanceBillList.add(bill);
        }

        ArrayList<AdvanceBill> notOverlapAdvanceBillLst = new ArrayList<>(advanceBillList);

        //需要创建结算记录的账单
        ArrayList<AdvanceBill> createSettleBill;
        //和何玉莹沟通说，暂且把唯一性校验给去了
        /*if(flag){
            //1.检验传入的数据是否存在重复时间，移除存在重复时间的账单
            //根据房号和费项分组
            Map<String, List<AdvanceBill>> groupByRoomAndCharge = advanceBillList.stream().collect(Collectors.groupingBy(
                    item -> StringUtils.joinWith("|",item.getRoomId(), String.valueOf(item.getChargeItemId()))));
            //校验当前房号和费项下时间是否存在重叠
            for (Map.Entry<String, List<AdvanceBill>> entry : groupByRoomAndCharge.entrySet()) {
                List<AdvanceBill> valueList = entry.getValue();
                checkTimeIsOverLap(valueList);
            }

            //2.根据开始时间获取库中的账单数据
            List<AdvanceBill> existAdvanceBillList = Optional
                    .ofNullable(billRepository.queryByRoomIdAndChargeId(roomIdAndChargeIdList))
                    .orElse(new ArrayList<>());
            existAdvanceBillList= existAdvanceBillList
                    .stream().filter(AdvanceBill::isEffectiveBill).collect(Collectors.toList());
            //根据房号和费项进行分组
            Map<String, List<AdvanceBill>> collect = existAdvanceBillList.stream()
                    .collect(Collectors.groupingBy(bill -> StringUtils.joinWith("|",bill.getRoomId(), String.valueOf(bill.getChargeItemId()))));
            ArrayList<AdvanceBill> saveAdvanceBillList = new ArrayList<>(notOverlapAdvanceBillLst);
            //3.遍历已经去除重叠时间的账单，与库中的账单进行对比
            for (AdvanceBill advanceBill : notOverlapAdvanceBillLst) {
                String key = StringUtils.joinWith("|",advanceBill.getRoomId(), String.valueOf(advanceBill.getChargeItemId()));
                List<AdvanceBill> existAdvanceBillListWithRoomIdAndChargeId = collect.get(key);
                //当前房号和费项存在预收账单时校验时间是否存在重复
                if (CollectionUtils.isNotEmpty(existAdvanceBillListWithRoomIdAndChargeId)) {
                    LocalDateTime startTime = advanceBill.getStartTime();
                    LocalDateTime endTime = advanceBill.getEndTime();
                    for (AdvanceBill existAdvanceBill : existAdvanceBillList) {
                        //移除存在重复时间的账单
                        if ( Objects.nonNull(existAdvanceBill.getStartTime()) && isOverlap(startTime, endTime, existAdvanceBill.getStartTime(), existAdvanceBill.getEndTime())) {
                            throw BizException.throw300("账单在数据库中存在重复时间");
                        }
                    }
                }
            }
            //4.批量保存账单并返回失败信息
            billRepository.saveBatch(saveAdvanceBillList);
            // 添加减免记录
            addBillJustRecord(saveAdvanceBillList);
            createSettleBill = saveAdvanceBillList;
        }else{
            billRepository.saveBatch(notOverlapAdvanceBillLst);
            // 添加减免记录
            addBillJustRecord(notOverlapAdvanceBillLst);
            createSettleBill = notOverlapAdvanceBillLst;
        }*/
        billRepository.saveBatch(notOverlapAdvanceBillLst);
        // 添加减免记录
        addBillJustRecord(notOverlapAdvanceBillLst);
        createSettleBill = notOverlapAdvanceBillLst;
        //5.添加结算信息
        Long gatherBillId = saveGatherInfo(createSettleBill, 1);
        //记录日志
        BizLog.normalBatch(createSettleBill.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());

        // 在返回集第一个元素里记录生成收款单ID
        List<AdvanceBillAllDetailV> vList = Global.mapperFacade.mapAsList(
                createSettleBill, AdvanceBillAllDetailV.class);
        if (Objects.nonNull(gatherBillId) && CollectionUtils.isNotEmpty(vList)){
            vList.get(0).setGatherBillId(gatherBillId);
        }
        return vList;
    }

    /**
     * 添加预存送的减免记录
     * @param saveAdvanceBillList 预收账单id列表
     */
    private void addBillJustRecord(List<AdvanceBill> saveAdvanceBillList) {
        // 挑出存在优惠金额的账单
        List<AdvanceBill> preferentialBillList = saveAdvanceBillList.stream().filter(a -> a.getPreferentialAmount() > 0L).collect(Collectors.toList());
        for (AdvanceBill advanceBill : preferentialBillList) {
            // 创建关联的优惠减免明细记录
            BillAdjustE billAdjustE = new BillAdjustE();
            billAdjustE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_ADJUST));
            billAdjustE.setBillId(advanceBill.getId());
            billAdjustE.setBillType(BillTypeEnum.预收账单.getCode());
            billAdjustE.setAdjustWay(BillAdjustWayEnum.ADJUST_PREFERENTIAL_RULE.getCode());
            billAdjustE.setDeductionMethod(DeductionMethodEnum.应收减免.getCode());
            billAdjustE.setContent("因:优惠规则:通过:应收减免-优惠，应收减免"+ BigDecimal.valueOf(advanceBill.getPreferentialAmount()).divide(Const.BIG_DECIMAL_HUNDRED,2, RoundingMode.HALF_UP) +"元");
            billAdjustE.setReason(99);
            billAdjustE.setBillAmount(advanceBill.getTotalAmount());
            billAdjustE.setAdjustAmount(-advanceBill.getPreferentialAmount());
            billAdjustE.setAdjustType(BillAdjustTypeEnum.减免.getCode());
            billAdjustE.setState(Const.State._2);
            billAdjustE.setApproveTime(LocalDateTime.now());
            billAdjustE.setAdjustTime(LocalDateTime.now());
            billAdjustE.setFileVos(new ArrayList<>());
            billAdjustE.setInferenceState(Const.State._0);
            billAdjustE.setIsExact(Const.State._2);
            billAdjustRepository.save(billAdjustE);
        }
        // 挑出存在优惠金额的账单
        List<AdvanceBill> presentList = saveAdvanceBillList.stream().filter(a -> a.getPresentAmount() > 0L).collect(Collectors.toList());
        for (AdvanceBill advanceBill : presentList) {
            // 创建关联的赠送减免明细记录
            BillAdjustE billAdjustE = new BillAdjustE();
            billAdjustE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_ADJUST));
            billAdjustE.setBillId(advanceBill.getId());
            billAdjustE.setBillType(BillTypeEnum.预收账单.getCode());
            billAdjustE.setAdjustWay(BillAdjustWayEnum.ADJUST_PRESENT_RULE.getCode());
            billAdjustE.setDeductionMethod(DeductionMethodEnum.应收减免.getCode());
            billAdjustE.setContent("因:赠送规则:通过:应收减免-赠送，应收减免"+ BigDecimal.valueOf(advanceBill.getPresentAmount()).divide(Const.BIG_DECIMAL_HUNDRED,2, RoundingMode.HALF_UP) +"元");
            billAdjustE.setReason(99);
            billAdjustE.setBillAmount(advanceBill.getTotalAmount());
            billAdjustE.setAdjustAmount(-advanceBill.getPresentAmount());
            billAdjustE.setAdjustType(BillAdjustTypeEnum.减免.getCode());
            billAdjustE.setState(Const.State._2);
            billAdjustE.setApproveTime(LocalDateTime.now());
            billAdjustE.setAdjustTime(LocalDateTime.now());
            billAdjustE.setFileVos(new ArrayList<>());
            billAdjustE.setInferenceState(Const.State._0);
            billAdjustE.setIsExact(Const.State._2);
            billAdjustRepository.save(billAdjustE);
        }
    }

    /**
     * 批量保存并返回失败数据的原因
     *
     * @param billList billList
     * @return List
     */
    public List<BillAddBatchResultDto> addBatchAndReturnErrorInfo(List<AddAdvanceBillCommand> billList) {
        log.info("批量导入预收账单参数 {}", JSON.toJSONString(billList));
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isEmpty(billList), ErrorMessage.BILL_ADD_LENGTH_ERROR);
        List<BillAddBatchResultDto> failList = new ArrayList<>();
        List<AdvanceBill> advanceBillList = new ArrayList<>();
        for (AddAdvanceBillCommand command : billList) {
            AdvanceBill bill = Global.mapperFacade.map(command, AdvanceBill.class);
            if(Objects.nonNull(command.getApprovedFlag()) && command.getApprovedFlag()){
                bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            }
            bill.setPayWay(command.getSettleWay());
            bill.setPayChannel(command.getSettleChannel());
            advanceBillList.add(bill);
        }

        ArrayList<AdvanceBill> notOverlapAdvanceBillLst = new ArrayList<>(advanceBillList);
        //是否进行周期校验
        boolean flag = true;
        //批量缴费转预收没有周期，不进行周期校验
        ArrayList<List<String>> roomIdAndChargeIdList = new ArrayList<>();
        //自动生成账单id和编码
        for (AdvanceBill advanceBill : notOverlapAdvanceBillLst) {
            advanceBill.init();
            advanceBill.setSettleState(BillSettleStateEnum.已结算.getCode());
            advanceBill.setReceivableAmount(advanceBill.getTotalAmount());
            advanceBill.setSettleAmount(advanceBill.getTotalAmount());
            advanceBill.setSupCpUnitId(advanceBill.getCommunityId());
            advanceBill.setSupCpUnitName(advanceBill.getCommunityName());
            advanceBill.setCpUnitId(advanceBill.getRoomId());
            advanceBill.setCpUnitName(advanceBill.getRoomName());
            roomIdAndChargeIdList.add(Arrays.asList(advanceBill.getRoomId(), advanceBill.getChargeItemId().toString()));
            if(Objects.isNull(advanceBill.getStartTime()) || Objects.isNull(advanceBill.getEndTime())){
                flag = false;
            }
        }

        //需要创建结算记录的账单
        ArrayList<AdvanceBill> createSettleBill;
        if(flag){
            //1.检验传入的数据是否存在重复时间，移除存在重复时间的账单
            //根据房号和费项分组
            Map<String, List<AdvanceBill>> groupByRoomAndCharge = advanceBillList.stream().collect(Collectors.groupingBy(
                    item -> StringUtils.joinWith("|",item.getRoomId(), String.valueOf(item.getChargeItemId()))));
            //校验当前房号和费项下时间是否存在重叠
            for (Map.Entry<String, List<AdvanceBill>> entry : groupByRoomAndCharge.entrySet()) {
                List<AdvanceBill> valueList = entry.getValue();
                checkTimeIsOverLap(failList, notOverlapAdvanceBillLst, valueList);
            }

            //2.根据开始时间获取库中的账单数据
            List<AdvanceBill> existAdvanceBillList = Optional
                    .ofNullable(billRepository.queryByRoomIdAndChargeId(roomIdAndChargeIdList))
                    .orElse(new ArrayList<>());
            existAdvanceBillList= existAdvanceBillList
                    .stream().filter(AdvanceBill::isEffectiveBill).collect(Collectors.toList());
            //根据房号和费项进行分组
            Map<String, List<AdvanceBill>> collect = existAdvanceBillList.stream()
                    .collect(Collectors.groupingBy(bill -> StringUtils.joinWith("|",bill.getRoomId(), String.valueOf(bill.getChargeItemId()))));
            ArrayList<AdvanceBill> saveAdvanceBillList = new ArrayList<>(notOverlapAdvanceBillLst);
            //3.遍历已经去除重叠时间的账单，与库中的账单进行对比
            for (AdvanceBill advanceBill : notOverlapAdvanceBillLst) {
                String key = StringUtils.joinWith("|",advanceBill.getRoomId(), String.valueOf(advanceBill.getChargeItemId()));
                List<AdvanceBill> existAdvanceBillListWithRoomIdAndChargeId = collect.get(key);
                //当前房号和费项存在预收账单时校验时间是否存在重复
//                if (CollectionUtils.isNotEmpty(existAdvanceBillListWithRoomIdAndChargeId)) {
//                    LocalDateTime startTime = advanceBill.getStartTime();
//                    LocalDateTime endTime = advanceBill.getEndTime();
//                    for (AdvanceBill existAdvanceBill : existAdvanceBillList) {
//                        //移除存在重复时间的账单
//                        if ( Objects.nonNull(existAdvanceBill.getStartTime()) && isOverlap(startTime, endTime, existAdvanceBill.getStartTime(), existAdvanceBill.getEndTime())) {
//                            failList.add(new BillAddBatchResultDto("账单在数据库中存在重复时间,账单编号:" + existAdvanceBill.getBillNo(), advanceBill.getRowNumber()));
//                            saveAdvanceBillList.remove(advanceBill);
//                            break;
//                        }
//                    }
//                }
            }
            //4.批量保存账单并返回失败信息
            // 导入功能charge端引入path值
            billRepository.saveBatch(saveAdvanceBillList);
            createSettleBill = saveAdvanceBillList;
        }else{
            billRepository.saveBatch(notOverlapAdvanceBillLst);
            createSettleBill = notOverlapAdvanceBillLst;
        }
        //5.添加结算信息
        saveGatherInfo(createSettleBill, 2);
        //记录日志
        BizLog.normalBatch(createSettleBill.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());

        return failList;
    }


    private void initAvdanceBillBySettleInfo(AdvanceBill advanceBill,BillSettleCommand settleInfo){
        advanceBill.setMchNo(settleInfo.getMchNo());
        advanceBill.setDeviceNo(settleInfo.getDeviceNo());
        advanceBill.setPaySource(settleInfo.getPaySource());
        advanceBill.setPayChannel(settleInfo.getPayChannel());
        advanceBill.setPayWay(settleInfo.getPayWay());
        advanceBill.setPayerPhone(settleInfo.getPayerPhone());
        advanceBill.setPayerId(settleInfo.getPayerId());
        advanceBill.setPayerPhone(settleInfo.getPayerPhone());
        advanceBill.setPayeeId(settleInfo.getPayeeId());
        advanceBill.setPayeeName(settleInfo.getPayeeName());
        advanceBill.setTradeNo(settleInfo.getTradeNo());
        advanceBill.setPayerType(settleInfo.getPayerType());
        advanceBill.setPayTime(settleInfo.getPayTime());
        advanceBill.setSettleWay(settleInfo.getPayWay());
    }


    /**
     * 预收预缴保存收款明细
     *
     * @param createSettleBill 保存的预收账单信息
     * @param flag 1：合并预缴生成一条gatherBill和多条gatherDetail 2：分开预缴生成多条gatherBill和多条gatherDetail
     */
    private Long saveGatherInfo(List<AdvanceBill> createSettleBill, Integer flag) {

        if (2 == flag) {
            List<BillGatherA<AdvanceBill>> billGatherAS = splitGatherBill(createSettleBill);
            for (BillGatherA<AdvanceBill> item : billGatherAS) {
                billGatherAGateway.saveBillGatherA(item);
            }
        }else if (1 == flag) {
            BillGatherA<AdvanceBill> advanceBillBillGatherA = aggregationGatherBill(createSettleBill);
            if (Objects.nonNull(advanceBillBillGatherA)) {
                return billGatherAGateway.saveBillGatherA(advanceBillBillGatherA);
            }
        }
        return null;
    }

    private BillGatherA<AdvanceBill> aggregationGatherBill(List<AdvanceBill> createSettleBill) {
        if (CollectionUtils.isEmpty(createSettleBill)) {
            return null;
        }
        GatherBill gatherBill = createGatherBill(createSettleBill.get(0));
        List<BillGatherDetailA<AdvanceBill>> billGatherDetailAList = new ArrayList<>();
        for (AdvanceBill advanceBill : createSettleBill) {
            String payeeId = Objects.isNull(advanceBill.getStatutoryBodyId()) ?
                    advanceBill.getPayeeId() : advanceBill.getStatutoryBodyId().toString();
            String payeeName = Objects.isNull(advanceBill.getStatutoryBodyName()) ?
                    advanceBill.getPayeeName() : advanceBill.getStatutoryBodyName();
            if (CollectionUtils.isEmpty(advanceBill.getSettleChannelInfos())) {
                GatherDetail billSettleE = createBillSettle(advanceBill,gatherBill);
                billSettleE.setPayeeId(payeeId);
                billSettleE.setPayeeName(payeeName);
                BillGatherDetailA<AdvanceBill> advanceBillBillGatherDetailA =
                        new BillGatherDetailA<>(advanceBill, gatherBill, billSettleE);
                billGatherDetailAList.add(advanceBillBillGatherDetailA);
            }else {
                List<BillGatherDetailA<AdvanceBill>> gatherDetailAS = multiGatherDetail(advanceBill, gatherBill);
                billGatherDetailAList.addAll(gatherDetailAS);
            }
        }
        gatherBill.setTotalAmount(
                createSettleBill.stream()
                        .map(AdvanceBill::getSettleAmount)
                        .filter(Objects::nonNull)
                        .reduce(Long::sum).orElse(0L));
        gatherBill.setPreferentialAmount(
                createSettleBill.stream()
                        .map(AdvanceBill::getPreferentialAmount)
                        .filter(Objects::nonNull)
                        .reduce(Long::sum).orElse(0L));
        return new BillGatherA<>(gatherBill, billGatherDetailAList);
    }

    private List<BillGatherA<AdvanceBill>> splitGatherBill(List<AdvanceBill> createSettleBill) {
        List<BillGatherA<AdvanceBill>> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(createSettleBill)) {
            return result;
        }
        for (AdvanceBill advanceBill : createSettleBill) {
            GatherBill gatherBill = createGatherBill(advanceBill);
            List<BillGatherDetailA<AdvanceBill>> gatherDetails ;
            String payeeId = Objects.isNull(advanceBill.getStatutoryBodyId()) ?
                    advanceBill.getPayeeId() : advanceBill.getStatutoryBodyId().toString();
            String payeeName = Objects.isNull(advanceBill.getStatutoryBodyName()) ?
                    advanceBill.getPayeeName() : advanceBill.getStatutoryBodyName();
            if (CollectionUtils.isEmpty(advanceBill.getSettleChannelInfos())) {
                GatherDetail billSettleE = createBillSettle(advanceBill,gatherBill);
                billSettleE.setPayeeId(payeeId);
                billSettleE.setPayeeName(payeeName);
                BillGatherDetailA<AdvanceBill> advanceBillBillGatherDetailA =
                        new BillGatherDetailA<>(advanceBill, gatherBill, billSettleE);
                gatherDetails = Collections.singletonList(advanceBillBillGatherDetailA);
            }else {
                gatherDetails = multiGatherDetail(advanceBill, gatherBill);
            }
            gatherBill.setPayeeId(payeeId);
            gatherBill.setPayeeName(payeeName);
            gatherBill.setApprovedState(advanceBill.getApprovedState());
            BillGatherA<AdvanceBill> advanceBillBillGatherA =
                    new BillGatherA<>(gatherBill, gatherDetails);
            result.add(advanceBillBillGatherA);
        }
        return result;
    }

    private List<BillGatherDetailA<AdvanceBill>> multiGatherDetail(
            AdvanceBill advanceBill, GatherBill gatherBill
    ) {
        String payeeId = Objects.isNull(advanceBill.getStatutoryBodyId()) ?
                advanceBill.getPayeeId() : advanceBill.getStatutoryBodyId().toString();
        String payeeName = Objects.isNull(advanceBill.getStatutoryBodyName()) ?
                advanceBill.getPayeeName() : advanceBill.getStatutoryBodyName();
        List<BillGatherDetailA<AdvanceBill>> gatherDetails = new ArrayList<>();
        List<BillSettleChannelInfo> settleChannelInfos = advanceBill.getSettleChannelInfos();
        for (BillSettleChannelInfo item : settleChannelInfos) {
            GatherDetail billSettleE = createBillSettle(advanceBill,gatherBill, item);
            billSettleE.setPayeeId(payeeId);
            billSettleE.setPayeeName(payeeName);
            BillGatherDetailA<AdvanceBill> advanceBillBillGatherDetailA =
                    new BillGatherDetailA<>(advanceBill, gatherBill, billSettleE);
            gatherDetails.add(advanceBillBillGatherDetailA);
        }
        return gatherDetails;
    }

    /**
     * 校验传入的账单时间是否存在重叠，移除重叠账单并添加错误信息
     *
     * @param billList                 同一房号和费项的账单
     */
    private void checkTimeIsOverLap(List<AdvanceBill> billList) {
        for (AdvanceBill advanceBill : billList) {
            LocalDateTime startTime = advanceBill.getStartTime();
            LocalDateTime endTime = advanceBill.getEndTime();
            if (startTime.isAfter(endTime)) {
                throw BizException.throw300(ErrorMessage.BILL_ADVANCE_START_TIME_AFTER_END_TIME.msg());
            } else {
                for (AdvanceBill targetAdvanceBill : billList) {
                    //存在重复时间不生成账单,创建错误信息
                    if (!targetAdvanceBill.equals(advanceBill)) {
                        if (isOverlap(startTime, endTime, targetAdvanceBill.getStartTime(), targetAdvanceBill.getEndTime())) {
                            throw BizException.throw300("账单存在重复时间");
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验传入的账单时间是否存在重叠，移除重叠账单并添加错误信息
     *
     * @param failList                 错误信息集合
     * @param notOverlapAdvanceBillLst 不重叠账单集合
     * @param billList                 同一房号和费项的账单
     */
    private void checkTimeIsOverLap(List<BillAddBatchResultDto> failList,
                                    ArrayList<AdvanceBill> notOverlapAdvanceBillLst,
                                    List<AdvanceBill> billList) {
        for (AdvanceBill advanceBill : billList) {
            LocalDateTime startTime = advanceBill.getStartTime();
            LocalDateTime endTime = advanceBill.getEndTime();
            if (startTime.isAfter(endTime)) {
                //开始时间大于结束时间
                failList.add(new BillAddBatchResultDto(ErrorMessage.BILL_ADVANCE_START_TIME_AFTER_END_TIME.msg(), advanceBill.getRowNumber()));
                notOverlapAdvanceBillLst.remove(advanceBill);
            } else {
                for (AdvanceBill targetAdvanceBill : billList) {
                    //存在重复时间不生成账单,创建错误信息
                    if (!targetAdvanceBill.equals(advanceBill)) {
                        if (isOverlap(startTime, endTime, targetAdvanceBill.getStartTime(), targetAdvanceBill.getEndTime())) {
                            //移除重叠时间的账单
                            failList.add(new BillAddBatchResultDto("账单存在重复时间,重复行号:" + targetAdvanceBill.getRowNumber(), advanceBill.getRowNumber()));
                            notOverlapAdvanceBillLst.remove(advanceBill);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断两个时间是否重复
     *
     * @param start       需要比较的开始时间
     * @param end         需要比较的结束时间
     * @param startTarget 已经存在的开始时间
     * @param endTarget   已经存在的结束时间
     * @return boolean
     */
    public boolean isOverlap(LocalDateTime start, LocalDateTime end, LocalDateTime startTarget, LocalDateTime endTarget) {
        return (start.equals(startTarget) && end.equals(endTarget));
    }

    @Override
    public <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren) {
        IPage<AdvanceBillGroupDto> billGroupDtoIPage = getAdvanceBillGroup(queryF, type);
        List<AdvanceBillGroupDto> groupRecords = billGroupDtoIPage.getRecords();
        List<AdvanceBillGroupDetailDto> advanceBillGroupDetailPageList = Global.mapperFacade.mapAsList(groupRecords, AdvanceBillGroupDetailDto.class);
        //2.根据父分组中的账单id获取所有的子分组预收账单
        if (loadChildren && CollectionUtils.isNotEmpty(groupRecords)) {
            //2.1.查询所有的子分组预收账单
            List<String> billIds = new ArrayList<>();
            groupRecords.forEach(gb->  billIds.addAll(List.of(gb.getBillIds().split(","))));
            QueryWrapper<AdvanceBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id" ,billIds);
            if (FANG_YUAN.equals(EnvData.config)) {
                queryWrapper.orderByDesc("start_time").orderByDesc("end_time").orderByDesc("id");
            }else {
                queryWrapper.orderByAsc("start_time").orderByAsc("end_time").orderByAsc("id");
            }
            List<AdvanceBill> advanceBillList = billRepository.list(queryWrapper);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
            //2.2.对子分组账单进行分组，方便父分组和子分组进行关联
            Map<String, List<AdvanceBill>> groupDetailMap = advanceBillList.stream().collect(Collectors.groupingBy(
                    bill -> AdvanceBillGroupDetailDto.getGroupKey(bill, dateTimeFormatter)));
            //3.为父分组设置子分组
            AtomicInteger index = new AtomicInteger(0);
            advanceBillGroupDetailPageList.forEach(groupItem -> {
                groupItem.setId(Long.parseLong("66" + System.currentTimeMillis()) + (index.incrementAndGet()));
                List<AdvanceBillGroupDetailDto> rbds = Global.mapperFacade.mapAsList(groupDetailMap.get(AdvanceBillGroupDetailDto.getGroupKey(groupItem, dateTimeFormatter)), AdvanceBillGroupDetailDto.class);
                for (AdvanceBillGroupDetailDto rbd : rbds) {
                    rbd.setPayInfosString(getPayInfosPaychannelString(rbd));
                    if (rbd.getPayerType() != null) {
                        rbd.setPayerTypeStr(VoucherBillCustomerTypeEnum.valueOfByCode(rbd.getPayerType()).getValue());
                    }
                    if (Objects.nonNull(rbd.getBusinessUnitId())){
                        String businessName = Optional.ofNullable(
                                businessUnitAppService.getById(rbd.getBusinessUnitId()))
                                .map(BusinessUnitV::getName).orElse(null);
                        rbd.setBusinessUnitName(businessName);
                    }
                }
                groupItem.setChildren(rbds);
                groupItem.setActualPayAmount(rbds.stream().mapToLong(AdvanceBillGroupDetailDto::getActualPayAmount).sum());
                groupItem.setActualUnpayAmount(rbds.stream().mapToLong(AdvanceBillGroupDetailDto::getActualUnpayAmount).sum());
            });
        }
        return (PageV<T>) PageV.of(billGroupDtoIPage.getCurrent(), billGroupDtoIPage.getSize(), billGroupDtoIPage.getTotal(), advanceBillGroupDetailPageList);
    }

    public String getPayInfosPaychannelString(AdvanceBillGroupDetailDto advanceBillGroupDetailDto){
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(SettleChannelEnum.valueNameOfByCode(advanceBillGroupDetailDto.getPayChannel()));
        return stringBuilder.toString();
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


    @Override
    public List<Long> approveBatch(BatchApproveBillCommand command, Integer billType) {
        List<AdvanceBill> billList = new ArrayList<>();
        List<Long> billIds = Optional.ofNullable(command.getBillIds()).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(billIds)){
            ErrorAssertUtil.notNullThrow300(command.getQuery(), ErrorMessage.BILL_BATCH_QUERY_ERROR);
            command.getQuery().getConditions().getFields().forEach(f -> {
                f.setName(f.getName().replace("b.", ""));
                Map<String, Object> map = f.getMap();
                if("searchs".equals(f.getName())) {
                    Map<String, Object> map2 = new HashMap<>();
                    map.forEach((k, v) -> {
                        map2.put(k.replace("b.", ""), v);
                    });
                    f.setMap(map2);
                }
            });
            command.getQuery().getConditions().getFields().add(new Field("approved_state", 0,1));
            billList = billRepository.listByPageSearch(command.getQuery(), billType);
            billIds = Optional.ofNullable(billList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(AdvanceBill::getId)
                    .collect(Collectors.toList());
        }else {
            billList = billRepository.listByIds(billIds);
        }
        List<BillApproveA<AdvanceBill>> billApproves = new ArrayList<>();
        billList.forEach(bill -> {
            if (TenantUtil.bf4()){
                if (StringUtils.isBlank(bill.getCostCenterName())){
                    throw BizException.throw400("账单"+bill.getBillNo()+"未获取到成本中心，无法审核通过");
                }
                if (StringUtils.isBlank(bill.getPayerId()) && StringUtils.isBlank(bill.getPayerName())){
                    throw BizException.throw400("账单"+bill.getBillNo()+"没有收费对象，无法审核通过");
                }
            }
            BillApproveA<AdvanceBill> approveA = new BillApproveA<>(bill);
            approveA.initDefaultApprove().approve(command.getApproveState(), command.getRejectReason(), null);
            billApproves.add(approveA);
        });
        billRepository.updateBatchById(billApproves.stream().map(BillApproveA::getBill).collect(Collectors.toList()));
        approveRepository.saveBatch(Global.mapperFacade.mapAsList(billApproves, BillApproveE.class));

        //记录日志
        if (command.getApproveState() == BillApproveStateEnum.已审核){
            BizLog.normalBatch(billApproves.stream().map(i -> String.valueOf(i.getBillId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.审核通过, new Content());
        }else {
            BizLog.normalBatch(billApproves.stream().map(i -> String.valueOf(i.getBillId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
        }
        return billIds;
    }


    /**
     * 创建结算记录数据
     *
     * @param bill       预收账单
     * @param gatherBill
     * @return BillSettleE
     */
    private GatherDetail createBillSettle(AdvanceBill bill, GatherBill gatherBill) {
        GatherDetail billSettleE = new GatherDetail()
                .setGatherBillId(gatherBill.getId())
                .setGatherBillNo(gatherBill.getBillNo())
                .setRecBillId(bill.getId())
                .setRecBillNo(bill.getBillNo())
                .setChargeItemId(bill.getChargeItemId())
                .setChargeItemName(bill.getChargeItemName())
                .setCostCenterId(bill.getCostCenterId())
                .setCostCenterName(bill.getCostCenterName())
                .setSupCpUnitId(bill.getCommunityId())
                .setSupCpUnitName(bill.getCommunityName())
                .setCpUnitId(bill.getRoomId())
                .setCpUnitName(bill.getRoomName())
                .setRecPayAmount(bill.getSettleAmount())
                .setPreferentialAmount(bill.getPreferentialAmount())
                .setPayChannel(StringUtils.isEmpty(bill.getSettleChannel()) ? SettleChannelEnum.其他.getCode() : bill.getSettleChannel())
                .setPayWay(Objects.isNull(bill.getSettleWay()) ? SettleWayEnum.线上.getCode() : bill.getSettleWay())
                .setPayAmount(bill.getSettleAmount())
                .setPayTime(Objects.nonNull(bill.getPayTime()) ? bill.getPayTime() : LocalDateTime.now())
                .setChargeStartTime(bill.getStartTime())
                .setChargeEndTime(bill.getEndTime())
                .setPayerType(bill.getPayerType())
                .setPayerId(bill.getPayerId())
                .setPayerName(bill.getPayerName())
                .setPayeePhone(bill.getPayerPhone())
                .setPayeeId(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserId())
                        .orElse(bill.getPayeeId()))
                .setPayeeName(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserName())
                        .orElse(bill.getPayeeName()))
                .setGatherType(GatherTypeEnum.预收.getCode())
                .setRemark(bill.getRemark())
                .setOutPayNo(gatherBill.getTradeNo());
        billSettleE.init();
        return billSettleE;
    }

    private GatherDetail createBillSettle(AdvanceBill bill, GatherBill gatherBill, BillSettleChannelInfo channelInfo) {
        GatherDetail billSettleE = new GatherDetail()
                .setGatherBillId(gatherBill.getId())
                .setGatherBillNo(gatherBill.getBillNo())
                .setRecBillId(bill.getId())
                .setRecBillNo(bill.getBillNo())
                .setChargeItemId(bill.getChargeItemId())
                .setChargeItemName(bill.getChargeItemName())
                .setCostCenterId(bill.getCostCenterId())
                .setCostCenterName(bill.getCostCenterName())
                .setSupCpUnitId(bill.getCommunityId())
                .setSupCpUnitName(bill.getCommunityName())
                .setCpUnitId(bill.getRoomId())
                .setCpUnitName(bill.getRoomName())
                .setRecPayAmount(
                        channelInfo.getPayAmount()
                )
                .setPreferentialAmount(bill.getPreferentialAmount())
                .setPayChannel(StringUtils.isEmpty(channelInfo.getSettleChannel()) ? SettleChannelEnum.其他.getCode() :
                        channelInfo.getSettleChannel())
                .setPayWay(Objects.isNull(channelInfo.getSettleWay()) ? SettleWayEnum.线上.getCode() :
                        channelInfo.getSettleWay())
                .setPayAmount(
                        channelInfo.getPayAmount()
                )
                .setPayTime(Objects.nonNull(bill.getPayTime()) ? bill.getPayTime() : LocalDateTime.now())
                .setChargeStartTime(bill.getStartTime())
                .setChargeEndTime(bill.getEndTime())
                .setPayerType(bill.getPayerType())
                .setPayerId(bill.getPayerId())
                .setPayerName(bill.getPayerName())
                .setPayeePhone(bill.getPayerPhone())
                .setPayeeId(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserId())
                        .orElse(bill.getPayeeId()))
                .setPayeeName(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserName())
                        .orElse(bill.getPayeeName()))
                .setGatherType(GatherTypeEnum.预收.getCode())
                .setRemark(bill.getRemark())
                .setOutPayNo(gatherBill.getTradeNo());
        billSettleE.init();
        return billSettleE;
    }

    /**
     * 创建结算记录详情数据
     * @param advanceBill 预收账单
     * @return {@link GatherBill}
     */
    private GatherBill createGatherBill(AdvanceBill advanceBill) {
        GatherBill gatherBill = new GatherBill();
        gatherBill.setPaySource(advanceBill.getPaySource());
        gatherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        gatherBill.setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        gatherBill.setStatutoryBodyId(advanceBill.getStatutoryBodyId());
        gatherBill.setStatutoryBodyName(advanceBill.getStatutoryBodyName());
        gatherBill.setSbAccountId(advanceBill.getSbAccountId());
        gatherBill.setSupCpUnitId(advanceBill.getSupCpUnitId());
        gatherBill.setSupCpUnitName(advanceBill.getSupCpUnitName());
        gatherBill.setStartTime(advanceBill.getStartTime());
        gatherBill.setEndTime(advanceBill.getEndTime());
        gatherBill.setPayTime(Objects.isNull(advanceBill.getPayTime()) ? LocalDateTime.now() : advanceBill.getPayTime());
        gatherBill.setPayChannel(advanceBill.getPayChannel());
        gatherBill.setPayWay(Objects.isNull(advanceBill.getSettleWay()) ? SettleWayEnum.线上.getCode() : advanceBill.getSettleWay());
        gatherBill.setDescription(advanceBill.getDescription());
        gatherBill.setTotalAmount(advanceBill.getTotalAmount());
        gatherBill.setPayeeId(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserId())
                .orElse(advanceBill.getPayeeId()));
        gatherBill.setPayeeName(Optional.ofNullable(ThreadLocalUtil.curIdentityInfo().getUserName())
                .orElse(advanceBill.getPayeeName()));
        gatherBill.setPayerId(advanceBill.getPayerId());
        gatherBill.setPayerName(advanceBill.getPayerName());
        gatherBill.setSysSource(advanceBill.getSysSource());
        gatherBill.setApprovedState(advanceBill.getApprovedState());
        gatherBill.setTradeNo(advanceBill.getTradeNo());
        List<DiscountOBV> discounts = Lists.newArrayList();
        gatherBill.setDiscounts(discounts);
        gatherBill.setDiscountAmount(0L);
        gatherBill.setPreferentialAmount(advanceBill.getPreferentialAmount());
        gatherBill.setDeviceNo(advanceBill.getDeviceNo());
        gatherBill.setMchNo(advanceBill.getMchNo());
        gatherBill.setBankFlowNo(advanceBill.getBankFlowNo());
        // 涉及优惠赠送规则的进行标识
        Optional.of(advanceBill).filter(a->a.getPresentAmount()>0L||a.getPreferentialAmount()>0L)
                        .ifPresent(a->gatherBill.setPreferential(Const.State._1));
        gatherBill.init();
        return gatherBill;
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
     * 导出预收单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        String fileName = "预收单";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportAdvanceBillData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportAdvanceBillData> resultList;
            queryF.setPageSize(pageSize);
            while(pageNumber <= totalPage){
                queryF.setPageNum(pageNumber);
                IPage<AdvanceBill> advanceBillPage = billRepository.queryBillByPage(queryF);
                resultList = Global.mapperFacade.mapAsList(advanceBillPage.getRecords(), ExportAdvanceBillData.class);
                if(CollectionUtils.isEmpty(resultList)){
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet,writeTable);
                totalPage = advanceBillPage.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询应收账单最大账单结束时间
     *
     * @return ReceivableMaxEndTimeBillF
     */
    public AdvanceMaxEndTimeV queryMaxEndTime(AdvanceMaxEndTimeBillF maxEndTimeBillF) {
        return billRepository.queryMaxEndTime(maxEndTimeBillF);
    }

    /**
     * 获取项目下存在预收账单的房间
     *
     * @param communityId 项目id
     * @return 房间id
     */
    public List<Long> getAdvanceRoomIds(String communityId, Long chargeItemId) {
        return billRepository.getAdvanceRoomIds(communityId, chargeItemId);
    }

    public List<AdvanceBill> getAdvanceBillByRoomIds(List<Long> roomIds) {
        QueryWrapper<AdvanceBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_id",Global.mapperFacade.mapAsList(roomIds,String.class));
        queryWrapper.eq("deleted",0);
        queryWrapper.in("state", List.of(BillStateEnum.正常.getCode(),BillStateEnum.冻结.getCode()));
        queryWrapper.in("settle_state", List.of(BillSettleStateEnum.未结算.getCode(),BillSettleStateEnum.部分结算.getCode()));
        queryWrapper.in("carried_state", List.of(BillCarriedStateEnum.未结转.getCode(),BillCarriedStateEnum.部分结转.getCode()));
        queryWrapper.in("refund_state", List.of(BillRefundStateEnum.未退款.getCode(),BillRefundStateEnum.部分退款.getCode()));
        queryWrapper.eq("reversed", BillReverseStateEnum.未冲销.getCode());
        return billRepository.list(queryWrapper);
    }

    public AdvanceBillTotalMoneyV getAdvanceBillTotalMoney(String payerId,String communityId) {
        return billRepository.getAdvanceBillTotalMoney(payerId, communityId);
    }

    @Override
    public boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        if (CollectionUtils.isEmpty(unFreezeBatchF.getBillIds())) {
            throw BizException.throw400("解冻账单id不能为空");
        }
        return batchUnFreezeBill(unFreezeBatchF.getBillIds());
    }

    private boolean batchUnFreezeBill(List<Long> billIds) {
        ErrorAssertUtil.notEmptyThrow400(billIds, ErrorMessage.BILL_BATCH_QUERY_ERROR);
        List<AdvanceBill> advanceBillList = Optional.ofNullable(billRepository.listByIds(billIds))
                .orElse(new ArrayList<>())
                .stream()
                .filter(e -> BillStateEnum.冻结.getCode() == e.getState()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(advanceBillList)) {
            return true;
        }
        advanceBillList.forEach(AdvanceBill::unfreeze);
        boolean result = billRepository.updateBatchById(advanceBillList);
        if (result){
            BizLog.initiateBatch(advanceBillList.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                    LogContext.getOperator(), LogObject.账单, LogAction.解冻, new Content());
        }
        return result;
    }

    public IPage queryPageApprove(PageF<SearchF<?>> queryF) {
        setOrder(queryF);
        List<Field> fields = queryF.getConditions().getFields();
        String supCpUnitId = null ;
        for (Field item : fields) {
            if ("b.sup_cp_unit_id".equals(item.getName())) {
                supCpUnitId = (String) item.getValue();
                break;
            }
        }
        if (!org.springframework.util.StringUtils.hasText(supCpUnitId)) {
            throw BizException.throw400("分表建不存在，请传分表键sup_cp_unit_id");
        }
        QueryWrapper<?> wrapper = getApproveWrapper(queryF, billRepository.getWrapper(queryF));
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.BILL_APPROVE);
        // 导出场合
        IPage billPage;
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            long total = 0;
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            if (totalObj == null) {
                total = billRepository.countPageApprove(wrapper, shareTableName);
            } else {
                total = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (total > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.ADVANCE_BILL;
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.ADVANCE_APPROVE);

                // 深分页查询优化
                long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
                billPage = exportService.queryAdvanceBillByPageOnTempTbl(
                        Page.of(1, queryF.getPageSize(), false), tblName, exportTaskId, tid);
                billPage.setTotal(total);
                return billPage;
            }
        }

        return billRepository.queryPageApprove(queryF, wrapper, shareTableName);
    }

    private void setOrder(PageF<SearchF<?>> queryF) {
        OrderBy orderByStarTime = new OrderBy();
        orderByStarTime.setAsc(true);
        orderByStarTime.setField("start_time");
        OrderBy orderByEndTime = new OrderBy();
        orderByEndTime.setAsc(true);
        orderByEndTime.setField("end_time");
        OrderBy orderById = new OrderBy();
        orderById.setAsc(true);
        orderById.setField("id");
        queryF.setOrderBy(List.of(orderByStarTime,orderByEndTime,orderById));
    }

    /**
     * 通过项目id，房间号id，收费费项id和账单年度为分组维度查询账单相关统计信息
     *
     * @param queryF 查询条件
     * @param type 0:一般列表 1：审核列表
     * @return 账单相关统计信息列表
     */
    private IPage<AdvanceBillGroupDto> getAdvanceBillGroup(PageF<SearchF<?>> queryF, int type) {
        //1.获取所有父分组
        QueryWrapper wrapper = billRepository.getWrappers(queryF);
        wrapper.groupBy(List.of("b.community_id", "b.room_id", "b.charge_item_id", "bill_year"));

        // 导出场合
        AdvanceBillMapper mapper = billRepository.getBaseMapper();
        IPage<AdvanceBillGroupDto> billGroupDtoIPage;
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            int total = 0;
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            if (totalObj == null) {
                if (type == 1) {
                    total = mapper.countWithGroupByApprove(getApproveWrapper(queryF, wrapper));
                } else {
                    total = mapper.countWithGroup(wrapper);
                }
            } else {
                total = Integer.parseInt(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (total > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.ADVANCE_BILL;
                exportService.createTmpTbl(wrapper, tblName, exportTaskId,
                        type == 1 ? ExportTmpTblTypeEnum.ADVANCE_GROUP_APPROVE : ExportTmpTblTypeEnum.ADVANCE_GROUP);

                // 深分页查询优化
                long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
                billGroupDtoIPage = exportService.queryAdvanceBillGroupByPageOnTempTbl(
                        Page.of(1, queryF.getPageSize(), false), tblName, exportTaskId, tid);
                billGroupDtoIPage.setTotal(total);
                return billGroupDtoIPage;
            }
        }

        // 原查询逻辑
        if (type == 1){
            billGroupDtoIPage = mapper.pageWithGroupByApprove(
                    Page.of(queryF.getPageNum(), queryF.getPageSize()), getApproveWrapper(queryF, wrapper));
        }else{
            billGroupDtoIPage = mapper.pageWithGroup(
                    Page.of(queryF.getPageNum(), queryF.getPageSize(), queryF.isCount()), wrapper);
        }
        return billGroupDtoIPage;
    }


    public Boolean  deleteInitBill(PageF<SearchF<?>> queryF) {
        QueryWrapper wrapper = billRepository.getWrapper(queryF);
        wrapper.in("b.approved_state", List.of(0,1));
        List<Long> initBillIds = billRepository.getBaseMapper().getAdvanceBillIds(wrapper);
        if(CollectionUtils.isNotEmpty(initBillIds)){
            billRepository.update(new LambdaUpdateWrapper<AdvanceBill>().in(Bill::getId, initBillIds).set(Bill::getDeleted, 1));
        }
        return true;

    }
}
