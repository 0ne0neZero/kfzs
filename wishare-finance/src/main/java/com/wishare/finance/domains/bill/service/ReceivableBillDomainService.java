package com.wishare.finance.domains.bill.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.consts.enums.third.BillReceivableRoomTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.*;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportReceivableBillData;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceivableBillDomainService extends BillDomainServiceImpl<ReceivableBillRepository, ReceivableBill> {


    @Autowired
    private GatherDetailRepository gatherDetailRepository;

    @Autowired
    private SpacePermissionAppService spacePermissionAppService;

    private static final Pattern compile = Pattern.compile("[A-Z]");


    /**
     * 入账
     *
     * @param receivableBill    应收
     * @param billSettleCommand 入账命令
     * @return
     */
    public boolean enter(ReceivableBill receivableBill, BillSettleCommand billSettleCommand){
        //初始化应收账单
        receivableBill.init();
        GatherBill gatherBill = null;
        List<GatherDetail> gatherDetails = null;
        if (Objects.nonNull(billSettleCommand)){
            gatherBill = billSettleCommand.buildGatherBill(receivableBill);
            gatherDetails = billSettleCommand.buildGatherDetails(receivableBill, gatherBill);
        }
        receivableBill.setPath(spacePermissionAppService.getSpacePath(receivableBill.getRoomId()));
        billRepository.save(receivableBill);
        if (Objects.nonNull(gatherBill)){
            gatherBillRepository.save(gatherBill);
        }
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.saveBatch(gatherDetails);
        }
        return true;
    }

    /**
     * @param enterCommands
     * @return
     */
    @Override
    public List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId){
        List<GatherDetail> gatherDetails = new ArrayList<>();
        List<ReceivableBill> receivableBills = new ArrayList<>();
        Map<String, GatherBill> gatherMap = new HashMap<>();
        GatherBill gatherBill = null;
        BillUnitaryEnterCommand enterCommand = enterCommands.get(0);
        //远洋环境做重复提交校验
        if(TenantUtil.bf4() && Objects.nonNull(enterCommand)){
            //防止重复提交
            String tradeNo = enterCommand.getTradeNo();
            //bankFlowNo是远洋移动端入账专有的，只针对这个场景做锁
            String bankFlowNo = enterCommand.getSettleInfo().getBankFlowNo();
            if(StringUtils.isNotBlank(bankFlowNo)){
                String messageKey = CacheEnum.TRADE_NO.getCacheKey(tradeNo);
                if (!RedisHelper.setNotExists(messageKey, "1")) {
                    log.info("临时账单入账交易流水号重复请求：{}", tradeNo);
                    throw BizException.throw402("请勿重复支付");
                }
                RedisHelper.expire(messageKey, 15L);
            }
        }

        for (BillUnitaryEnterCommand unitaryEnterF : enterCommands) {
            BillSettleCommand settleInfo = unitaryEnterF.getSettleInfo();
            String tradeNo = settleInfo.getTradeNo();
            if(StringUtils.isNotBlank(tradeNo)){
                //防止移动端同一笔订单流水重复插入
                Integer count = gatherBillRepository.queryCountByTradeNo(tradeNo, supCpUnitId);
                if(Objects.nonNull(count) && count > 0){
                    log.info("统一入账tradeNo重复----->tradeNo:{}",tradeNo);
                    continue;
                }
            }else{
                log.info("统一入账tradeNo为空----->unitaryEnterF:{}",JSON.toJSONString(unitaryEnterF));
                continue;
            }
            ReceivableBill receivableBill = Global.mapperFacade.map(unitaryEnterF, ReceivableBill.class);
            receivableBill.init();
            receivableBill.refresh();
            receivableBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            receivableBill.setIsInit(false);
            receivableBill.initPayInfos(receivableBill.getTotalAmount(),settleInfo.getPayWay(),settleInfo.getPayChannel());
            receivableBill.setPath(spacePermissionAppService.getSpacePath(receivableBill.getRoomId()));
            receivableBills.add(receivableBill);
            if (Objects.nonNull(settleInfo)){
                if (gatherMap.containsKey(settleInfo.getTradeNo())){
                    gatherBill = gatherMap.get(settleInfo.getTradeNo());
                    gatherBill.setTotalAmount(gatherBill.getTotalAmount() + settleInfo.getPayAmount());
                }else{
                    gatherBill = settleInfo.buildGatherBill(receivableBill);
                    gatherBill.setDeviceNo(settleInfo.getDeviceNo());
                    gatherBill.setPaySource(settleInfo.getPaySource());
                    gatherBill.setMchNo(settleInfo.getMchNo());
                }
                gatherBill.setBankFlowNo(settleInfo.getBankFlowNo());
                gatherMap.put(gatherBill.getTradeNo(), gatherBill);
                List<GatherDetail> gds = settleInfo.buildGatherDetails(receivableBill, gatherBill);
                if (CollectionUtils.isNotEmpty(gds)){
                    gatherDetails.addAll(gds);
                }
            }
        }
        if(CollectionUtils.isEmpty(receivableBills)){
            throw BizException.throw400("应收支付流水订单已存在");
        }
        billRepository.saveBatch(receivableBills);
        Collection<GatherBill> gatherBills = gatherMap.values();
        //处理收款单获取所有账单中最小开始时间和最大结束时间
        Map<String, List<BillUnitaryEnterCommand>> groupByTradeNoMap = enterCommands.stream().collect(Collectors.groupingBy(command -> command.getSettleInfo().getTradeNo()));
        if(Objects.nonNull(groupByTradeNoMap)){
            for (Map.Entry<String, GatherBill> entry : gatherMap.entrySet()) {
                String key = entry.getKey();
                GatherBill value = entry.getValue();
                List<BillUnitaryEnterCommand> billUnitaryEnterCommands = groupByTradeNoMap.get(key);
                Optional<BillUnitaryEnterCommand> maxEndTimeOptional = billUnitaryEnterCommands.stream().max(Comparator.comparing(BillUnitaryEnterCommand::getEndTime));
                Optional<BillUnitaryEnterCommand> minStartTimeOptional = billUnitaryEnterCommands.stream().min(Comparator.comparing(BillUnitaryEnterCommand::getStartTime));
                minStartTimeOptional.ifPresent(billUnitaryEnterCommand -> value.setStartTime(billUnitaryEnterCommand.getStartTime()));
                maxEndTimeOptional.ifPresent(billUnitaryEnterCommand -> value.setEndTime(billUnitaryEnterCommand.getEndTime()));
            }
        }


        if (CollectionUtils.isNotEmpty(gatherBills)){
            gatherBillRepository.saveBatch(gatherBills);
        }
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.saveBatch(gatherDetails);
        }

        List<BillUnitaryEnterResultDto> results = new ArrayList<>();
        results.addAll(receivableBills.stream().map(i->{
            BillUnitaryEnterResultDto billUnitaryEnterResultDto = new BillUnitaryEnterResultDto();
            billUnitaryEnterResultDto.setId(i.getId());
            billUnitaryEnterResultDto.setBillType(i.getBillType());
            return billUnitaryEnterResultDto;
        }).collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(gatherBills)){
            results.addAll(gatherBills.stream().map(i->{
                BillUnitaryEnterResultDto billUnitaryEnterResultDto = new BillUnitaryEnterResultDto();
                billUnitaryEnterResultDto.setId(i.getId());
                billUnitaryEnterResultDto.setBillType(BillTypeEnum.收款单.getCode());
                return billUnitaryEnterResultDto;
            }).collect(Collectors.toList()));
        }
        return results;
    }

    /**
     * 根据账单周期批量新增应收账单
     *
     * @param periodicCommands 周期账单生成命令
     * @return
     */
    public boolean saveBatchByPeriodic(List<AddPeriodicReceivableBillCommand> periodicCommands) {
        //需要删除的账单
        int maxSize = 500;
        List<ReceivableBill> cacheDeleteBills = new ArrayList<>();
        List<ReceivableBill> cacheReceivableBills = new ArrayList<>();
        for (AddPeriodicReceivableBillCommand periodicCommand : periodicCommands) {
            //1.根据批量的集合（房号、费项、权责周期）相同的账单
            List<ReceivableBill> receivableBillS = billRepository.listByPeriodic(Global.mapperFacade.map(periodicCommand, ReceivableBill.class));
            //2.如果周期内存在已经生成的账单，则将账单进行业务拆
            if (CollectionUtils.isNotEmpty(receivableBillS)) {
                for (ReceivableBill oldBill : receivableBillS) {
                    //匹配相关的账单，是否存在[待审核or未通过]账单，存在则删除
                    if (BillApproveStateEnum.待审核.equalsByCode(oldBill.getApprovedState()) ||
                            BillApproveStateEnum.未通过.equalsByCode(oldBill.getApprovedState())) {
                        cacheDeleteBills.add(oldBill);
                    } else {
                        //判断账单的时间是否与新建的账单区间匹配内，如果存在偏差，则对时间段进行业务拆单
                        separatePeriodicBill(oldBill, periodicCommand.getReceivableBills());
                    }
                }
            }
            List<ReceivableBill> newReceivableBills = periodicCommand.getReceivableBills().stream().map(this::buildReceivableBill).collect(Collectors.toList());
            cacheReceivableBills.addAll(newReceivableBills);
            if (cacheReceivableBills.size() >= maxSize) {
                cacheDeleteBills.forEach(v -> {
                    billRepository.remove(new QueryWrapper<ReceivableBill>().eq("id", v.getId())
                        .eq("sup_cp_unit_id", v.getSupCpUnitId()));
                });
                billRepository.saveBatch(cacheReceivableBills);
                //创建新对象，将缓存的数据交给GC回收
                cacheDeleteBills = new ArrayList<>();
                cacheReceivableBills = new ArrayList<>();
            }
        }
        //提交缓存中剩余的账单
        if (!cacheReceivableBills.isEmpty()) {
            cacheDeleteBills.forEach(v -> {
                billRepository.remove(new QueryWrapper<ReceivableBill>().eq("id", v.getId())
                    .eq("sup_cp_unit_id", v.getSupCpUnitId()));
            });
            billRepository.saveBatch(cacheReceivableBills);
        }
        return true;
    }

    /**
     *
     * @param queryF
     * @param type   0:一般列表  1：审核列表
     * @param loadChildren  是否加载子项
     * @return
     * @param <T>
     */
    @Override
    public <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren) {
        IPage<ReceivableBillGroupDto> billGroupDtoIPage = this.getReceivableBillGroup(queryF, type);
        List<ReceivableBillGroupDto> groupRecords = billGroupDtoIPage.getRecords();
        List<ReceivableBillGroupDetailDto> receivableBillPageVS = Global.mapperFacade.mapAsList(groupRecords, ReceivableBillGroupDetailDto.class);
        //2.根据合并的应收账单id再查询出所有的列表
        if (loadChildren && CollectionUtils.isNotEmpty(groupRecords)) {
            List<String> rbIds = new ArrayList<>();
            groupRecords.forEach(gb -> rbIds.addAll(List.of(gb.getBillIds().split(","))));
            List<Field> supCpUnitIdFileld = queryF.getConditions().getFields().stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
            Field supCpUnitIdF = supCpUnitIdFileld.get(0);
            //2.1.查询分组的所有应收账单
            List<ReceivableBill> receivableBills = billRepository.list(new QueryWrapper<ReceivableBill>()
                .in("id", rbIds)
                .eq("sup_cp_unit_id", supCpUnitIdF.getValue().toString()));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
            //2.2.对账单再进行分组
            Map<String, List<ReceivableBill>> groupDetailMap = receivableBills.stream().collect(Collectors.groupingBy(
                    bill -> ReceivableBillGroupDetailDto.getGroupKey(bill, dateTimeFormatter)));
            //3.为每个分组设置子分组（仅一个子分组，子分组无子分组）
            AtomicInteger index = new AtomicInteger(0);
            receivableBillPageVS.forEach(groupItem -> {
                groupItem.setBillMethod(null);
                groupItem.setId(Long.valueOf("66" + System.currentTimeMillis()) + (index.incrementAndGet()));
                List<ReceivableBillGroupDetailDto> rbds = Global.mapperFacade.mapAsList(groupDetailMap.get(ReceivableBillGroupDetailDto.getGroupKey(groupItem, dateTimeFormatter)), ReceivableBillGroupDetailDto.class);
                if (CollectionUtils.isNotEmpty(rbds)) {
                    for (ReceivableBillGroupDetailDto rbd : rbds) {
                        rbd.setPayInfosString(getPayInfosPaychannelString(rbd));
                    }
                    groupItem.setChildren(rbds);
                    groupItem.setActualPayAmount(rbds.stream().mapToLong(ReceivableBillGroupDetailDto::getActualPayAmount).sum());
                    groupItem.setActualUnpayAmount(rbds.stream().mapToLong(ReceivableBillGroupDetailDto::getActualUnpayAmount).sum());
                    //设置分组的开始时间和结束时间
//                    groupItem.setStartTime(rbds.get(0).getStartTime());
//                    groupItem.setEndTime(rbds.get(rbds.size() - 1).getEndTime());
                } else {
                    groupItem.setChildren(new ArrayList<>());
                    groupItem.setActualPayAmount(0L);
                    groupItem.setActualUnpayAmount(0L);
                }
            });
        }
        return (PageV<T>) PageV.of(billGroupDtoIPage.getCurrent(), billGroupDtoIPage.getSize(), billGroupDtoIPage.getTotal(), receivableBillPageVS);
    }

    public String getPayInfosPaychannelString(ReceivableBillGroupDetailDto receivableBillGroupDetailDto){
        StringBuilder stringBuilder = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(receivableBillGroupDetailDto.getPayInfos())){
            for (PayInfo payInfo : receivableBillGroupDetailDto.getPayInfos()) {
                Integer paySource = payInfo.getPaySource();
                if(Objects.nonNull(paySource)){
                    stringBuilder.append(PaySourceEnum.valueOfByCode(paySource) + "-" + SettleChannelEnum.valueNameOfByCode(payInfo.getPayChannel())).append(",");
                }else{
                    stringBuilder.append(SettleChannelEnum.valueNameOfByCode(payInfo.getPayChannel())).append(",");
                }
            }
        }
        String string = stringBuilder.toString();
        if (string.length()==0){
            return string;
        }else {
            return string.substring(0, string.length() - 1);
        }
    }

    /**
     * 构建应收账单聚合
     *
     * @param command
     * @return
     */
    private ReceivableBill buildReceivableBill(AddPeriodicBillCommand command) {
        ReceivableBill receivableBill = Global.mapperFacade.map(command, ReceivableBill.class);
        receivableBill.init();//初始化账单信息
        receivableBill.setPath(spacePermissionAppService.getSpacePath(receivableBill.getRoomId()));
        return receivableBill;
    }

    /**
     * 周期性账单业务拆单
     *
     * @param oldBill
     * @param newBills
     * @return
     */
    private void separatePeriodicBill(ReceivableBill oldBill, List<AddPeriodicBillCommand> newBills) {
        //对新单子进行排序
        newBills.stream().sorted(Comparator.comparing(AddPeriodicBillCommand::getStartTime)).collect(Collectors.toList());
        //老单子：[2022-07-01 2022-07-31]
        //新单子：[2022-07-01 2022-07-10] 、[2022-07-11 2022-07-21]、[2022-07-22 2022-07-31]
        if (CollectionUtils.isNotEmpty(newBills)) {
            //如果老单子结束时间大于最后一个账单的结束时间,则删除所有新建的账单
            if (oldBill.getEndTime().compareTo(newBills.get(newBills.size() - 1).getEndTime()) >= 0) {
                newBills.clear();
            }
            List<AddPeriodicBillCommand> separateBills = new ArrayList<>();
            //账单时间进行业务拆单
            AddPeriodicBillCommand newBill;
            for (int i = 0; i < newBills.size(); i++) {
                newBill = newBills.get(i);
                //如果新单子的开始时间大于等于老单子的结束时间，则不需要再进行拆单了
                if (newBill.getStartTime().compareTo(oldBill.getEndTime()) >= 0) {
                    break;
                }
                if (newBill.getStartTime().compareTo(oldBill.getStartTime()) >= 0) {
                    if (newBill.getEndTime().isAfter(oldBill.getEndTime())) {
                        AddPeriodicBillCommand newReceivableBill = Global.mapperFacade.map(newBill, AddPeriodicBillCommand.class);
                        newReceivableBill.setStartTime(oldBill.getEndTime().plusDays(1));//老单子结束时间的下一天
                        newReceivableBill.setEndTime(newBill.getEndTime()); //新单子的结束时间
                        separateBills.add(newReceivableBill);
                    }
                } else {
                    AddPeriodicBillCommand newReceivableBill = Global.mapperFacade.map(newBill, AddPeriodicBillCommand.class);
                    newReceivableBill.setStartTime(newBill.getStartTime()); //新单子的开始时间
                    newReceivableBill.setEndTime(oldBill.getStartTime().plusDays(-1)); //老单子的前一天
                    separateBills.add(newReceivableBill);
                    if (newBill.getEndTime().isAfter(oldBill.getEndTime())) {
                        newReceivableBill.setStartTime(oldBill.getEndTime().plusDays(1));//老单子结束时间的下一天
                        newReceivableBill.setEndTime(newBill.getEndTime()); //新单子的结束时间
                        separateBills.add(newReceivableBill);
                    }
                }
                newBills.remove(i); //删除当前新单子
            }
            //如果存在拆单单子，则加入到新的账单列表中
            if (!separateBills.isEmpty()) {
                newBills.addAll(separateBills);
            }
        }
    }

    /**
     * 根据账单ids获取应收账单信息
     *
     * @param billIds
     * @return
     */
    public List<ReceivableBillMoreInfoDto> receivableBillInfo(List<Long> billIds, String supCpUnitId) {
        List<ReceivableBill> receivableBillList = billRepository.queryByIds(billIds,supCpUnitId);
        List<ReceivableBillMoreInfoDto> receivableBillMoreInfoDto = Global.mapperFacade.mapAsList(receivableBillList, ReceivableBillMoreInfoDto.class);
        //获取结算记录 暂时，后续优化循环
        List<GatherDetail> gatherDetailList = gatherDetailRepository.listsByRecBillId(billIds, supCpUnitId);
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        Map<Long, List<GatherDetail>> listMap = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getRecBillId));
        receivableBillMoreInfoDto.forEach(receivableInfo -> {
            List<GatherDetail> gatherDetails = listMap.get(receivableInfo.getId());
            if (CollectionUtils.isNotEmpty(gatherDetails)){
                gatherDetails.forEach(gatherDetail -> {
                    BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
                    billSettleDtoList.add(billSettleDto);
                });
            }
            receivableInfo.setBillSettleDtoList(billSettleDtoList);
        });
        return receivableBillMoreInfoDto;
    }

    /**
     * 根据账单ids获取应收账单结算详情
     *
     * @param billIds
     * @return
     */
    public SettleDetailDto settleDetail(List<Long> billIds,String supCpUnitId) {
        //需校验是否是同项目,同个收费对象的账单
        List<ReceivableBill> receivableBillList = billRepository.list(new QueryWrapper<ReceivableBill>().eq(StringUtils.isNotBlank(supCpUnitId),"sup_cp_unit_id", supCpUnitId)
                .in("id", billIds));

        ErrorAssertUtil.notNullThrow300(receivableBillList, ErrorMessage.BILL_NOT_EXIST);
        //批量缴费需要限制相同法定单位以及账单来源
        checkBillDetail(receivableBillList);
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByRecBillIds(billIds, supCpUnitId);
        return new SettleDetailDto().generalDetail(receivableBillList, gatherDetailList);
    }

    /**
     * 批量缴费需要限制相同法定单位以及账单来源
     *
     * @param receivableBillList
     */
    private void checkBillDetail(List<ReceivableBill> receivableBillList) {
        //校检账单中法定单位是否相同
        List<Long> statutoryBodyIds = receivableBillList.stream().map(ReceivableBill::getStatutoryBodyId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(statutoryBodyIds) && statutoryBodyIds.size() != 1) {
            throw BizException.throw400("请选择同法定单位的账单进行缴费");
        }
    }

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param statisticsBillTotalQuery
     * @return
     */
    public BillTotalDto statisticsBillRefund(StatisticsBillTotalQuery statisticsBillTotalQuery) {
        return billRepository.statisticsBillRefund(statisticsBillTotalQuery);
    }

    /**
     * 催缴欠缴账单统计
     *
     * @param form
     * @return
     */
    public BillTotalDto call(StatisticsBillAmountF form) {
        return billRepository.call(form);
    }

    /**
     * 催缴欠缴账单统计
     *
     * @param form
     * @return
     */
    public List<BillTotalDto> callGroupByRoomAndItem(StatisticsBillAmountF form) {
        return billRepository.callGroupByRoomAndItem(form);
    }





    /**
     * 银行托收清结
     *
     * @param bankSettleF
     * @return
     */
    public boolean bankSettle(BankSettleF bankSettleF) {
        boolean res = false;
        if (CollectionUtils.isNotEmpty(bankSettleF.getErrorBillIds())) {
            res =  batchUnFreezeBill(bankSettleF.getErrorBillIds(),bankSettleF.getSupCpUnitId());
            if (!res) {
                return res;
            }
        }
        if (CollectionUtils.isEmpty(bankSettleF.getBillIds())) {
            return res;
        }
        List<ReceivableBill> receivableBills = billRepository.list(new QueryWrapper<ReceivableBill>()
                .in("id", bankSettleF.getBillIds())
                .eq(StringUtils.isNotBlank(bankSettleF.getSupCpUnitId()), "sup_cp_unit_id", bankSettleF.getSupCpUnitId()));
//        List<ReceivableBill> receivableBills = billRepository.listByIds(bankSettleF.getBillIds());
        if (CollectionUtils.isNotEmpty(receivableBills)) {
            List<GatherBill> gatherBills = Lists.newArrayList();
            List<GatherDetail> gatherDetailList = Lists.newArrayList();
            receivableBills.forEach(bill -> {
                AddBillSettleCommand command = new AddBillSettleCommand();
                command.setPayeeId(bill.getPayeeId());
                command.setPayeeName(bill.getPayeeName());
                command.setPayerId(bill.getPayerId());
                command.setPayerLabel(bill.getPayerLabel());
                command.setPayerName(bill.getPayerName());
                command.setSettleAmount(bill.getRemainingSettleAmount());
                command.setSettleWay(SettleWayEnum.线下.getCode());
                command.setSettleChannel(SettleChannelEnum.银行托收.getCode());
                command.setRemark("银行托收清结");
                command.setSettleTime(bankSettleF.getCounterofferTime());
                bill.unfreeze(); //解冻
                BillGatherA billGatherA = new BillGatherA<>(Lists.newArrayList(bill),Lists.newArrayList(command));
                BillGatherDetailA<Bill> gatherDetailA = new BillGatherDetailA<>(bill, billGatherA.getGatherBill());
                gatherDetailA.gather(command);
                gatherBills.add(billGatherA.getGatherBill());
                gatherDetailList.add(gatherDetailA.getGatherDetail());
            });
            for (ReceivableBill receivableBill : receivableBills) {
                res=billRepository.update(receivableBill,new UpdateWrapper<ReceivableBill>()
                        .eq("id",receivableBill.getId())
                        .eq(StringUtils.isNotBlank(bankSettleF.getSupCpUnitId()), "sup_cp_unit_id", bankSettleF.getSupCpUnitId()));
            }
//            res = billRepository.updateBatchById(receivableBills);

            if (CollectionUtils.isNotEmpty(gatherBills)){
                res = gatherBillRepository.saveBatch(gatherBills);
            }
            if (CollectionUtils.isNotEmpty(gatherDetailList)) {
                res = gatherDetailRepository.saveBatch(gatherDetailList);
            }

            //记录日志
            if (CollectionUtils.isNotEmpty(gatherBills)){
                BizLog.normalBatch(gatherBills.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                        LogContext.getOperator(), LogObject.账单, LogAction.生成,
                        new Content().option(new ContentOption(new PlainTextDataItem("银行托收清结生成", true))));
                for (GatherDetail gd : gatherDetailList) {
                    BizLog.initiate(String.valueOf(gd.getRecBillId()),
                            LogContext.getOperator(), LogObject.账单, LogAction.收款,
                            new Content().option(new ContentOption(new PlainTextDataItem("收款方式： " + SettleWayEnum.线下.getValue() + "-" + SettleChannelEnum.银行托收.getValue(), true)))
                                    .option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
                                    .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(gd.getPayAmount()), false), OptionStyle.normal()))
                                    .option(new ContentOption(new PlainTextDataItem("元", false))));
                }
            }
        }
        return res;
    }

    private boolean batchUnFreezeBill(List<Long> billIds,String supCpUnitId) {
        ErrorAssertUtil.notEmptyThrow400(billIds, ErrorMessage.BILL_BATCH_QUERY_ERROR);
        List<ReceivableBill> receivableBills =
                Optional.ofNullable(billRepository.list(new QueryWrapper<ReceivableBill>()
                                .in("id", billIds)
                                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)))
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(e -> BillStateEnum.冻结.getCode() == e.getState()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(receivableBills)) {
            return true;
        }

        billRepository.batchUnFreezeBillByIds(receivableBills, supCpUnitId);
        BizLog.initiateBatch(receivableBills.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.解冻, new Content());

        return true;
    }

    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    public List<RoomBillTotalDto> roomBills(List<Long> roomIdList,String supCpUnitId) {
        return billRepository.roomBills(roomIdList, supCpUnitId);
    }

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList 房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear 是否统计当年
     * @return List
     */
    public List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear, String supCpUnitId) {
        return billRepository.roomChargeBills(roomIdList, chargeItemIdList, currentYear, supCpUnitId);
    }


    /**
     * 查询应收收费信息
     *
     * @param pageF 分页入参
     * @return ReceivableBillApplyDetailV
     */
    public Page<ReceivableRoomsV> receivableRooms(ReceivableRoomsPageF pageF) {
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize(),false);
        pageF.setSupCpUnitId(pageF.getCommunityId());
        QueryWrapper<?> queryWrapper = getCommonQueryWrapper(pageF);
        Page<ReceivableRoomsDto> receivableRoomsDtoPage = billRepository.receivableRooms(page, queryWrapper);
        List<ReceivableRoomsDto> records = receivableRoomsDtoPage.getRecords();
        Integer count = 0;
        if(CollectionUtils.isNotEmpty(records)){
            count = billRepository.receivableRoomsCount(queryWrapper);
        }
        ArrayList<ReceivableRoomsV> receivableRoomsVoList = new ArrayList<>();
        for (ReceivableRoomsDto record : records) {
            String[] chargeItemIds = record.getChargeItemId().split(",");
            String[] chargeItemNames = record.getChargeItemName().split(",");
            ReceivableRoomsV receivableRoomsVo = Global.mapperFacade.map(record, ReceivableRoomsV.class);
            List<ReceivableRoomsV.ChargeItems> chargeItemList = new ArrayList<>();
            for (int i = 0; i < chargeItemIds.length; i++) {
                ReceivableRoomsV.ChargeItems chargeItems = new ReceivableRoomsV.ChargeItems();
                chargeItems.setChargeItemId(chargeItemIds[i]);
                chargeItems.setChargeItemName(chargeItemNames[i]);
                chargeItemList.add(chargeItems);
            }
            receivableRoomsVo.setChargeItems(chargeItemList);
            receivableRoomsVoList.add(receivableRoomsVo);
        }
        Page<ReceivableRoomsV> result = Global.mapperFacade.map(receivableRoomsDtoPage, Page.class);
        result.setRecords(receivableRoomsVoList);
        result.setTotal(count);
        return result;
    }

    public Page<ReceivableRoomsDto> queryCanAdvanceRooms(ReceivableRoomsPageF pageF) {
        if(StringUtils.isBlank(pageF.getCommunityId())) {
            throw new IllegalArgumentException("必须传入项目ID:communityId字段!");
        }
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize());
        pageF.setSupCpUnitId(pageF.getCommunityId());
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        String communityId = pageF.getCommunityId();
        String chargeItemId = pageF.getChargeItemId();
        String roomId = pageF.getRoomId();
        List<String> roomIds = pageF.getRoomIds();
        List<String> targetObjIds = pageF.getTargetObjIds();
        queryWrapper.eq("b.sup_cp_unit_id", communityId);
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());

        if(StringUtils.isNotEmpty(chargeItemId)){
            queryWrapper.eq("b.charge_item_id", chargeItemId);
        }
        if(StringUtils.isNotEmpty(roomId)){
            queryWrapper.eq("b.room_id", roomId);
        }
        if(CollectionUtils.isNotEmpty(roomIds)){
            queryWrapper.in("b.room_id", roomIds);
        }
        queryWrapper.eq("b.settle_state", 2);
        if(CollectionUtils.isNotEmpty(targetObjIds)){
            queryWrapper.in("b.payer_id", targetObjIds);
        }

        QueryWrapper<?> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("b.sup_cp_unit_id", communityId);
        queryWrapper1.eq("b.deleted", 0);
        queryWrapper1.eq("b.bill_type", BillTypeEnum.应收账单.getCode());

        if(StringUtils.isNotEmpty(chargeItemId)){
            queryWrapper1.eq("b.charge_item_id", chargeItemId);
        }
        if(StringUtils.isNotEmpty(roomId)){
            queryWrapper1.eq("b.room_id", roomId);
        }
        if(CollectionUtils.isNotEmpty(roomIds)){
            queryWrapper1.in("b.room_id", roomIds);
        }
        queryWrapper1.ne("b.settle_state", 2);
        if(CollectionUtils.isNotEmpty(targetObjIds)){
            queryWrapper.in("b.payer_id", targetObjIds);
        }
        Page<ReceivableRoomsDto> canAdvanceRoomsPage = billRepository.queryCanAdvanceRooms(page, queryWrapper,queryWrapper1,pageF.getTenantId());
        return canAdvanceRoomsPage;
    }


    /**
     * 查询房间应收账单列表
     *
     * @param queryF 查询入参
     * @return List
     */
    public List<ReceivableBillsV> receivableBills(ReceivableBillF queryF) {
        long start1 = System.currentTimeMillis();
        List<Integer> payState = queryF.getPayState();
        //如果获取未缴或者部分缴，把缴费中也返回给移动端，可以去取消支付
        if(CollectionUtils.isNotEmpty(payState)){
            if(payState.contains(BillSettleStateEnum.未结算.getCode()) || payState.contains(BillSettleStateEnum.部分结算.getCode())){
                payState.add(BillSettleStateEnum.结算中.getCode());
            }
        }
        List<ReceivableBillsDto> receivableBillList = billRepository.receivableBills(BillStateEnum.正常.getCode(),
                queryF.getRoomId(),queryF.getCommunityId(),payState,queryF.getTargetObjIds(),queryF.getChargeItemIds(),queryF.getRoomIds());
        long times1 = (System.currentTimeMillis() - start1);
        log.info("receivableBills 数据加载耗时：{} 秒", times1);
        List<ReceivableBillsV> receivableBillsVoList = new ArrayList<>();
        List<ReceivableBillsDto> groopReceivableBillList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(receivableBillList)){
            return receivableBillsVoList;
        }
        long start2 = System.currentTimeMillis();
        Map<String, List<ReceivableBillsDto>> itemIdBillsMap = receivableBillList.stream().collect(Collectors.groupingBy(bill -> StringUtils.join(Arrays.asList(
                bill.getRoomId(), String.valueOf(bill.getChargeItemId())), "|")));
        for (Map.Entry<String, List<ReceivableBillsDto>> entry : itemIdBillsMap.entrySet()) {
            List<ReceivableBillsDto> bills = entry.getValue();
            ReceivableBillsDto receivableBillsDto = bills.get(0);
            Long chargeItemId = receivableBillsDto.getChargeItemId();
            ReceivableBillsDto billsDto =  Global.mapperFacade.map(receivableBillsDto, ReceivableBillsDto.class);
            Long totalAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getTotalAmount).sum();
            Long receivableAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getReceivableAmount).sum();
            Long deductibleAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getDeductibleAmount).sum();
            Long discountAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getDiscountAmount).sum();
            Long carriedAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getCarriedAmount).sum();
            Long refundAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getRefundAmount).sum();
            Long settleAmountTotal = bills.stream().mapToLong(ReceivableBillsDto::getSettleAmount).sum();
            LocalDateTime maxEndTime = bills.stream().map(Bill::getEndTime).max(LocalDateTime::compareTo).get();
            LocalDateTime minStartTime = bills.stream().map(Bill::getStartTime).min(LocalDateTime::compareTo).get();
            String billIds = Joiner.on(",").join(bills.stream().map(ReceivableBillsDto :: getId).collect(Collectors.toList()));
            billsDto.setTotalAmount(totalAmountTotal);
            billsDto.setReceivableAmount(receivableAmountTotal);
            billsDto.setDeductibleAmount(deductibleAmountTotal);
            billsDto.setDiscountAmount(discountAmountTotal);
            billsDto.setCarriedAmount(carriedAmountTotal);
            billsDto.setRefundAmount(refundAmountTotal);
            billsDto.setSettleAmount(settleAmountTotal);
            billsDto.setChargeItemId(chargeItemId);
            billsDto.setBillIds(billIds);
            billsDto.setStartTime(minStartTime);
            billsDto.setEndTime(maxEndTime);
            groopReceivableBillList.add(billsDto);
        }
        long times2 = (System.currentTimeMillis() - start2);
        log.info("for 数据加载耗时：{} 秒", times2);
        ArrayList<String> billIds = new ArrayList<>();

        groopReceivableBillList.forEach(rb->  billIds.addAll(List.of(rb.getBillIds().split(","))));
        if(CollectionUtils.isEmpty(billIds)){
            return receivableBillsVoList;
        }
        long start = System.currentTimeMillis();
        List<ReceivableBill> billList = billRepository.queryByIdsOrderByCloum(billIds, List.of("start_time","end_time"), Boolean.TRUE, queryF.getCommunityId());
        long times = (System.currentTimeMillis() - start);
        log.info("queryByIdsOrderByCloum 数据加载耗时：{} 秒", times);
        //根据费项和房号分组
        Map<String, List<ReceivableBill>> collect = billList.stream().collect(Collectors.groupingBy(bill -> StringUtils.join(Arrays.asList(
                bill.getRoomId(), String.valueOf(bill.getChargeItemId())), "|")));
        for (ReceivableBillsDto receivableBillsDto : groopReceivableBillList) {
            ReceivableBillsV receivableBillsV = Global.mapperFacade.map(receivableBillsDto, ReceivableBillsV.class);
            String key = StringUtils.join(Arrays.asList(receivableBillsDto.getRoomId(), String.valueOf(receivableBillsDto.getChargeItemId())), "|");
            List<ReceivableBill> bills = collect.get(key);
            receivableBillsV.setBillList(Global.mapperFacade.mapAsList(bills, ReceivableBillsV.ReceivableBillV.class));
            receivableBillsVoList.add(receivableBillsV);
        }
        return receivableBillsVoList;
    }

    /**
     * 分页查询应收账单列表
     *
     * @param pageF 查询入参
     * @return PageV
     */
    public IPage<ReceivableBill> history(HistoryF pageF) {
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize());

        List<String> targertIds = Objects.isNull(pageF.getTargetObjId()) ? null : List.of(pageF.getTargetObjId());
        QueryWrapper<?> queryWrapper = getQueryWrapper(pageF.getCommunityId(),pageF.getChargeItemId(),pageF.getRoomId(), targertIds,pageF.getOrderBy());
        queryWrapper.ne("b.settle_state", BillSettleStateEnum.已结算.getCode());
        queryWrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        billRepository.normalBillCondition(queryWrapper);
        return billRepository.history(page, queryWrapper);
    }

    /**
     * 查询应收账单条数，唯一性校验使用
     *
     * @return PageF
     */
    public Integer queryBillCountByPage(PageF<SearchF<?>> queryF){
        return billRepository.queryBillCountByPage(queryF);
    }

    /**
     * 查询应收账单最大账单结束时间
     *
     * @return ReceivableMaxEndTimeBillF
     */
    public List<ReceivableMaxEndTimeV> queryMaxEndTime(ReceivableMaxEndTimeBillF maxEndTimeBillF){
        return billRepository.queryMaxEndTime(maxEndTimeBillF);
    }

    /**
     * 查询区间账单信息
     * @param query
     * @return
     */
    public List<ReceivableIntervalBillV> queryIntervalBill(ReceivableIntervalBillF query){
        return billRepository.queryIntervalBill(query);
    }



    /**
     * 每个月第一天去查归属月包含在对应季度内的无费用分类的未结算、已审核的应收账单打上标签
     */
    public void getBillCostType() {
        billRepository.getBillCostType();
    }


    public Boolean  deleteInitBill(PageF<SearchF<?>> queryF) {
        List<Field> fieldList = queryF.getConditions().getFields().stream().filter(e -> "b.sup_cp_unit_id".equals(e.getName())).collect(Collectors.toList());
        Field field = fieldList.get(0);
        String supCpUnitId = field.getValue().toString();
        QueryWrapper wrapper = billRepository.getWrapper(queryF);
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        wrapper.eq("b.is_init", 1);
        wrapper.in("b.approved_state", List.of(0,1));
        List<Long> initBillIds = billRepository.getBaseMapper().initBillIds(wrapper);
        if(CollectionUtils.isNotEmpty(initBillIds)){
            billRepository.update(new LambdaUpdateWrapper<ReceivableBill>()
                    .eq(Bill::getSupCpUnitId,supCpUnitId).in(Bill::getId, initBillIds).set(Bill::getDeleted, 1));
        }
        return true;

    }


    /**
     * 获取查询条件
     *
     * @return QueryWrapper
     */
    public QueryWrapper<?> getQueryWrapper(String communityId, String chargeItemId, String roomId, List<String> targetObjIds,List<OrderBy> orderBy){
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(communityId)){
            queryWrapper.eq("b.community_id", communityId);
            queryWrapper.eq("b.sup_cp_unit_id", communityId);
        }
        if(StringUtils.isNotEmpty(chargeItemId)){
            queryWrapper.eq("b.charge_item_id", chargeItemId);
        }
        if(StringUtils.isNotEmpty(roomId)){
            queryWrapper.eq("b.room_id", roomId);
        }
        if(CollectionUtils.isNotEmpty(targetObjIds)){
            queryWrapper.in("b.payer_id", targetObjIds);
        }
        if(CollectionUtils.isNotEmpty(orderBy)){
            for (OrderBy order : orderBy) {
                if(Objects.nonNull(order) && StringUtils.isNotEmpty(order.getField())){
                    String field = order.getField();
                    Matcher matcher = compile.matcher(field);
                    StringBuilder sb = new StringBuilder();
                    while (matcher.find()) {
                        matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                    }
                    matcher.appendTail(sb);
                    queryWrapper.orderBy(Boolean.TRUE, order.isAsc(), sb.toString());
                }
            }
        }else{
            queryWrapper.orderByAsc("b.start_time");
        }
        queryWrapper.eq("b.deleted", 0);
        return queryWrapper;
    }

    /**
     * 导出收款单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        String fileName = "应收单";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportReceivableBillData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportReceivableBillData> resultList;
            queryF.setPageSize(pageSize);
            while(pageNumber <= totalPage){
                queryF.setPageNum(pageNumber);
                IPage<ReceivableBill> receivableBillPage = billRepository.queryBillByPage(queryF);
                resultList = Global.mapperFacade.mapAsList(receivableBillPage.getRecords(), ExportReceivableBillData.class);
                if(CollectionUtils.isEmpty(resultList)){
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet,writeTable);
                totalPage = receivableBillPage.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绿城poc查询应收账单分页接口
     *
     * @return PageV
     */
    public IPage<ReceivableBill> getCommonPage(PageF<SearchF<?>> queryF) {
        return billRepository.queryCommonBillByPage(queryF);
    }

    private QueryWrapper<?> getCommonQueryWrapper(ReceivableRoomsPageF pageF){
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        String communityId = pageF.getCommunityId();
        String chargeItemId = pageF.getChargeItemId();
        List<String> chargeItemIdsParams = pageF.getChargeItemIds();
        String roomId = pageF.getRoomId();
        List<String> roomIds = pageF.getRoomIds();
        Integer payStatus = pageF.getPayStatus();
        List<String> targetObjIds = pageF.getTargetObjIds();
        List<OrderBy> orderBy = pageF.getOrderBy();
        if(StringUtils.isNotEmpty(communityId)){
            queryWrapper.eq("b.sup_cp_unit_id", communityId);
        }
        queryWrapper.eq("b.approved_state",2);
        queryWrapper.ne("b.state",2);
        if(Objects.nonNull(payStatus)){
            if(payStatus == 2){
                queryWrapper.eq("b.settle_state", payStatus);
            }else{
                //把缴费中也返回给移动端，可以去取消支付
//                queryWrapper.in("b.settle_state", List.of(0,1,3));
                queryWrapper.ne("b.settle_state", 2);
            }
        }else {
            queryWrapper.ne("b.settle_state", 2);
        }
        queryWrapper.in("b.refund_state", List.of(0,1,2));
        if(CollectionUtils.isNotEmpty(roomIds) || StringUtils.isNotEmpty(roomId)){
            if(CollectionUtils.isNotEmpty(roomIds)){
                queryWrapper.in("b.room_id", roomIds);
            }else {
                queryWrapper.eq("b.room_id", roomId);
            }
        }else {
            queryWrapper.isNotNull("b.room_id");
        }
        if(StringUtils.isNotEmpty(chargeItemId)){
            queryWrapper.eq("b.charge_item_id", chargeItemId);
        }
        if(CollectionUtils.isNotEmpty(chargeItemIdsParams)){
            queryWrapper.in("b.charge_item_id", chargeItemIdsParams);
        }
        queryWrapper.isNull("b.bill_label");
        queryWrapper.eq("b.reversed",0);
        queryWrapper.in("b.carried_state", List.of(0,1));
        if(CollectionUtils.isNotEmpty(targetObjIds)){
            queryWrapper.in("b.payer_id", targetObjIds);
        }
        queryWrapper.eq("b.deleted", 0);
        if(CollectionUtils.isNotEmpty(orderBy)){
            for (OrderBy order : orderBy) {
                if(Objects.nonNull(order) && StringUtils.isNotEmpty(order.getField())){
                    String field = order.getField();
                    Matcher matcher = compile.matcher(field);
                    StringBuilder sb = new StringBuilder();
                    while (matcher.find()) {
                        matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                    }
                    matcher.appendTail(sb);
                    queryWrapper.orderBy(Boolean.TRUE, order.isAsc(), sb.toString());
                }
            }
        }else{
            if(BillReceivableRoomTypeEnum.预收查询房间.getCode() != pageF.getType()){
                queryWrapper.orderByAsc("b.start_time");
            }
        }
        if(BillReceivableRoomTypeEnum.预收查询房间.getCode() != pageF.getType()){
            queryWrapper.groupBy(List.of("b.room_id","b.payer_id"));
        }
        if(BillReceivableRoomTypeEnum.催缴房间.getCode() == pageF.getType()){
            queryWrapper.lt("b.end_time", LocalDateTime.now());
        }
        return queryWrapper;
    }

    public List<ReceivableBill> getAdvanceBillByRoomIds(List<Long> roomIds) {

        QueryWrapper<ReceivableBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_id",roomIds);
        queryWrapper.eq("deleted",0);

        return billRepository.list(queryWrapper);
    }

    public Boolean editBillDeviceReading(EditBillReadingF editBillReadingF) {

        ReceivableBill receivableBill = billRepository.getOne(new QueryWrapper<ReceivableBill>()
            .eq("id", editBillReadingF.getBillId())
            .eq("sup_cp_unit_id", editBillReadingF.getSupCpUnitId()));
        if (receivableBill.getSettleState() == BillSettleStateEnum.已结算.getCode()) {
            throw BizException.throw400("该账单已经完全结算");
        }
        if (receivableBill == null) {
            throw BizException.throw400(ErrorMessage.BILL_NOT_EXIST.getErrMsg());
        }
        if (editBillReadingF.getTotalAmount() != null) {
            receivableBill.setTotalAmount(editBillReadingF.getTotalAmount());
            receivableBill.setChargingArea(editBillReadingF.getChargingArea());
            receivableBill.resetReceivableAmount();
        }
        return billRepository.update(receivableBill, new QueryWrapper<ReceivableBill>().eq("id", receivableBill.getId())
            .eq("sup_cp_unit_id", editBillReadingF.getSupCpUnitId()));

    }

    /**
     * 分页查询跳收记录
     *
     * @param pageF 查询入参
     * @return PageV
     */
    public IPage<JumpRecordDto> jumpRecordPage(PageF<SearchF<?>> pageF) {
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize());
        QueryWrapper<?> queryWrapper = pageF.getConditions().getQueryModel();
        return billRepository.jumpRecordPage(page, queryWrapper);
    }

    @Override
    public boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        if (CollectionUtils.isEmpty(unFreezeBatchF.getBillIds())) {
            throw BizException.throw400("解冻账单id不能为空");
        }
        return batchUnFreezeBill(unFreezeBatchF.getBillIds(), unFreezeBatchF.getSupCpUnitId());
    }

    public List<ReceivableBill> getByChargeNcId(String chargeNcId,String communityId){
        return billRepository.getByChargeNcId(chargeNcId,communityId);
    }

    public Boolean deleteBillById(String communityId, List<ReceivableBill> list) {
        return billRepository.deleteBillById(communityId, list);
    }

    public Boolean updateById(ReceivableBill receivableBill) {
        return billRepository.updateById(receivableBill);
    }

    public int batchUpdateGatherBillById(List<UpdateGatherBillF> updateGatherBillFList) {
        return billRepository.batchUpdateGatherBillById(updateGatherBillFList);
    }

    public IPage<ReceivableBill> queryPageApprove(PageF<SearchF<?>> queryF) {
        return billRepository.queryPageApprove(queryF, getApproveWrapper(queryF, billRepository.getWrapper(queryF)));
    }

    /**
     * 通过项目id，房间号id，收费费项id和账单年度为分组维度查询账单相关统计信息
     *
     * @param queryF 查询条件
     * @param type 0:一般列表 1：审核列表
     * @return 账单相关统计信息列表
     */
    private IPage<ReceivableBillGroupDto> getReceivableBillGroup(PageF<SearchF<?>> queryF, int type) {
        //1.获取所有的分组
        QueryWrapper wrapper = billRepository.getWrapper(queryF);
        wrapper.orderByDesc("b.start_time");
        wrapper.orderByAsc("b.id");
        wrapper.groupBy(List.of("b.community_id", "b.room_id", "b.charge_item_id", "bill_year"));

        //charge服务催缴列表接口/call/groups 会在specialMap设置"tempBill"，说明是中交环境需要把临时账单也查出来
        Map<String, Object> specialMap = Optional.ofNullable(queryF.getConditions().getSpecialMap()).orElse(new HashMap<>());
        Boolean tempBill = (Boolean) specialMap.get("tempBill");
        if (Objects.isNull(tempBill) || !tempBill) {
            wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        }
        //审核列表展示在变更审核页里
        if (type != 1){
            wrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
        }
        QueryWrapper<?> queryCountWrapper = queryF.getConditions().getQueryModel().eq("b.deleted", DataDisabledEnum.启用.getCode());
        if (Objects.isNull(tempBill) || !tempBill) {
            queryCountWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        }
        if (type != 1){
            queryCountWrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
        }
        queryCountWrapper.groupBy(List.of("b.community_id", "b.room_id", "b.charge_item_id", "DATE_FORMAT( b.start_time, '%Y')"));
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 查询总数
        ReceivableBillMapper mapper = billRepository.getBaseMapper();
        Integer total = 0;
        if (queryF.isCount()) {
            if (type == 1) {
                total = mapper.pageWithGroupByApproveCount(getApproveWrapper(queryF, queryCountWrapper));
            } else {
                total = mapper.pageWithGroupCount(queryCountWrapper);
            }
        }

        // 导出场合
        IPage<ReceivableBillGroupDto> billGroupDtoIPage;
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            if (totalObj != null) {
                total = Integer.parseInt(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (total > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.RECEIVABLE_BILL;
                List<Field> supCpUnitIds = queryF.getConditions().getFields().stream().filter(
                        s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
                if (supCpUnitIds != null && supCpUnitIds.size() > 0) {
                    tblName = sharedBillAppService.getShareTableName(supCpUnitIds.get(0).getValue().toString(), tblName);
                }

                String tblNameSuffix = String.valueOf(exportTaskIdObj);
                exportService.createTmpTbl(wrapper, tblName, tblNameSuffix,
                        type == 1 ? ExportTmpTblTypeEnum.RECEIVABLE_GROUP_APPROVE : ExportTmpTblTypeEnum.RECEIVABLE_GROUP);

                // 深分页查询优化
                long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
                billGroupDtoIPage = exportService.queryReceivableBillGroupByPageOnTempTbl(
                        Page.of(1, queryF.getPageSize(), false), tblName, tblNameSuffix, tid);
                billGroupDtoIPage.setTotal(total);
                return billGroupDtoIPage;
            }
        }

        // 原查询逻辑
        if (type == 1) {
            billGroupDtoIPage = mapper.pageWithGroupByApprove(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), getApproveWrapper(queryF, wrapper));
        } else {
            billGroupDtoIPage = mapper.pageWithGroup(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), wrapper);
        }
        billGroupDtoIPage.setTotal(total);
        return billGroupDtoIPage;
    }

    /**
     * 分页查询未开票收款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public IPage<UnInvoiceReceivableBillDto> unInvoiceBillPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        //添加正常账单条件
        queryWrapper.eq("b.state", BillStateEnum.正常.getCode());
        queryWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("b.invoice_state", InvoiceStateEnum.未开票.getCode());
        return receivableBillRepository.unInvoiceBillPage(Page.of(queryF.getPageNum(), queryF.getPageSize()), queryWrapper);
    }

//    @Override
//    public BillDetailDto getDetailById(Long bid) {
//        ReceivableBill bill = billRepository.getById(bid);
//        if (Objects.nonNull(bill)) {
//            BillDetailDto billDetailDto = new BillDetailDto();
//            billDetailDto.setBill(bill);
//            billDetailDto.setBillAdjustDtos(adjustRepository.listByBillId(bid));
//            billDetailDto.setApproves(approveRepository.listByBillId(bid));
//            if (BillTypeEnum.预收账单.equalsByCode(bill.getType())) {
//                billDetailDto.setBillSettleDtos(handleSettle(gatherDetailRepository.queryByGatherBillId(bid)));
//            } else {
//                List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bid);
//                Map<Long, List<GatherBill>> gatherBillMap = new HashedMap<>();
//                if (CollectionUtils.isNotEmpty(gatherDetails)) {
//                    List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
//                    List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds);
//                    gatherBillMap = gatherBillList.stream().collect(Collectors.groupingBy(GatherBill::getId));
//                }
//                List<BillSettleDto> billSettleDtos = handleSettle(gatherDetails);
//                if (CollectionUtils.isNotEmpty(billSettleDtos)) {
//                    for (BillSettleDto billSettleDto : billSettleDtos) {
//                        if (gatherBillMap != null && !gatherBillMap.isEmpty()) {
//                            GatherBill gatherBill = gatherBillMap.get(billSettleDto.getGatherBillId()).get(0);
//                            billSettleDto.setTradeNo(gatherBill.getTradeNo());
//                            billSettleDto.setDiscounts(JSON.toJSONString(gatherBill.getDiscounts()));
//                            billSettleDto.setRemark(gatherBill.getRemark());
//                        }
//
//                    }
//                }
//                billDetailDto.setBillSettleDtos(billSettleDtos);
//            }
//            billDetailDto.setBillRefundDtos(refundRepository.listByBillId(bid));
//            billDetailDto.setBillCarryoverDtos(carryoverRepository.listByCarriedBillId(bid));
//            return billDetailDto;
//        }
//        return null;
//    }


    /**
     * 共同field查询应收账单分页接口
     *
     * @return PageV
     */
    public IPage<ReceivableBill> queryCommonBillByPageField(PageF<SearchF<?>> queryF) {
        return billRepository.queryCommonBillByPageField(queryF);
    }


}
