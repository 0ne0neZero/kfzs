package com.wishare.finance.apps.service.bill;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.apps.model.bill.vo.ChargeDeductionDetailPageV;
import com.wishare.finance.apps.model.bill.vo.ReceivableAndTemporaryBillTotalV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillPageV;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.ReceivableAndTemporaryBillRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.bill.service.ReceivableAndTemporaryBillDomainService;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ReverseFlagEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.Md5Utils;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.consts.Const.State;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author fengxiaolin
 * @date 2023/5/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceivableAndTemporaryBillService extends
        BillAppServiceImpl<ReceivableAndTemporaryBillDomainService, ReceivableBill> {

    private final ReceivableAndTemporaryBillRepository billRepository;

    private final BillAdjustRepository billAdjustRepository;

    private final AdvanceBillRepository advanceBillRepository;

    private final ReceivableBillRepository receivableBillRepository;

    private final TemporaryChargeBillRepository temporaryChargeBillRepository;

    private final SharedBillAppService sharedBillAppService;

    private final ReceivableBillDomainService receivableBillDomainService;

    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;

    @Setter(onMethod_ = @Autowired)
    protected ExportService exportService;

    public <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren) {
        //1.获取所有的分组
        QueryWrapper wrapper = billRepository.getWrapper(queryF);
        wrapper.orderByDesc("b.start_time");
        wrapper.groupBy(List.of("b.community_id", "b.room_id", "b.charge_item_id", "bill_year"));
        IPage<ReceivableBillGroupDto> billGroupDtoIPage = null;
        Integer total = 0;
        wrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());

        QueryWrapper<?> queryCountWrapper = queryF.getConditions().getQueryModel().eq("b.deleted", DataDisabledEnum.启用.getCode());
        queryCountWrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
        queryCountWrapper.groupBy(List.of("b.community_id", "b.room_id", "b.charge_item_id", "DATE_FORMAT( b.start_time, '%Y')"));
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        billGroupDtoIPage = getExportReceivableBillGroup(queryF, wrapper, queryCountWrapper, type);

        // 原有逻辑
        if (billGroupDtoIPage == null) {
            if (type == 1) {
                billGroupDtoIPage = billRepository.getBaseMapper().pageWithGroupByApprove(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), getApproveWrapper(queryF, wrapper));
                total = billRepository.getBaseMapper().pageWithGroupByApproveCount(getApproveWrapper(queryF, queryCountWrapper));
            } else {
                billGroupDtoIPage = billRepository.getBaseMapper().pageWithGroup(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), wrapper);
                total = billRepository.getBaseMapper().pageWithGroupCount(queryCountWrapper);
            }
        }
        List<ReceivableBillGroupDto> groupRecords = billGroupDtoIPage.getRecords();
        List<ReceivableBillGroupDetailDto> receivableBillPageVS = Global.mapperFacade.mapAsList(groupRecords, ReceivableBillGroupDetailDto.class);
        //2.根据合并的应收账单id再查询出所有的列表
        if (loadChildren && CollectionUtils.isNotEmpty(groupRecords)) {
            List<String> rbIds = new ArrayList<>();
            groupRecords.forEach(gb -> rbIds.addAll(List.of(gb.getBillIds().split(","))));
            List<Field> supCpUnitIdFileld = queryF.getConditions().getFields().stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
            Field supCpUnitIdF = supCpUnitIdFileld.get(0);
            //2.1.查询分组的所有应收账单
            QueryWrapper<ReceivableBill> queryWrapper = new QueryWrapper<ReceivableBill>().in("id", rbIds)
                    .eq("sup_cp_unit_id", supCpUnitIdF.getValue().toString());
            List<ReceivableBill> receivableBills = billRepository.list(queryWrapper);
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
        return (PageV<T>) PageV.of(billGroupDtoIPage.getCurrent(), billGroupDtoIPage.getSize(), total == 0 ? billGroupDtoIPage.getTotal() : total, receivableBillPageVS);
    }


    private QueryWrapper<?> getApproveWrapper(PageF<SearchF<?>> query, QueryWrapper<?> queryWrapper){
        List<Integer> operateTypeValue = (List<Integer>) query.getConditions().getSpecialMap().get("operate_type");
        if (CollectionUtils.isNotEmpty(operateTypeValue) ){
            if (operateTypeValue.contains(0)) {
                queryWrapper.and(wrapper -> wrapper.in("ba.operate_type", operateTypeValue).or().isNull("ba.operate_type"));
            }else {
                queryWrapper.in("ba.operate_type", operateTypeValue);
            }
        }
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.in("b.approved_state", List.of(0,1));//审核分组加上已审核过滤
        return queryWrapper;
    }


    public PageV<ChargeDeductionDetailPageV> getDeductionDetail(PageF<SearchF<?>> pageF) {
        QueryWrapper<?> queryModel = pageF.getConditions().getQueryModel();
        PageQueryUtils.validQueryContainsFieldAndValue(pageF, "rb." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(pageF,
            TableNames.RECEIVABLE_BILL, "rb." + BillSharedingColumn.应收账单.getColumnName());
        IPage<ChargeDeductionDetailDto> iPage = billAdjustRepository.getDeductionDetail(Page.of(pageF.getPageNum(), pageF.getPageSize(),pageF.isCount()),queryModel, receivableBillName);
        List<ChargeDeductionDetailDto> records = iPage.getRecords();
        List<ChargeDeductionDetailPageV> returnList = Global.mapperFacade.mapAsList(records, ChargeDeductionDetailPageV.class);
        return PageV.of(pageF.getPageNum(),iPage.getSize(),iPage.getTotal(),returnList);
    }

    @Transactional
    public Boolean updateBillApprovedState(UpdateBillApprovedStateF fo) {

        Map<Integer, List<BatchDeductionF>> map = fo.getBillDetailVs().stream().collect(Collectors.groupingBy(BatchDeductionF::getBillType));
        List<BatchDeductionF> batchReceivableDeductions = map.get(BillTypeEnum.应收账单.getCode());
        List<BatchDeductionF> batchAdvanceDeductions = map.get(BillTypeEnum.预收账单.getCode());
        if (CollectionUtils.isNotEmpty(batchReceivableDeductions)) {
            Map<Long, BatchDeductionF> receivableMap = batchReceivableDeductions.stream().collect(Collectors.toMap(BatchDeductionF::getId, e -> e));
            List<Long> receivableIds = batchReceivableDeductions.stream().map(BatchDeductionF::getId).collect(Collectors.toList());
            QueryWrapper<ReceivableBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",receivableIds);
            List<ReceivableBill> receivableBillList = billRepository.list(queryWrapper);
            for (ReceivableBill receivableBill : receivableBillList) {
                BatchDeductionF batchDeductionF = receivableMap.get(receivableBill.getId());
                if (batchDeductionF != null){
                    if (batchDeductionF.getDeductibleAmount() != null ){
                        receivableBill.setDeductibleAmount(receivableBill.getDeductibleAmount() + batchDeductionF.getDeductibleAmount());
                    }
                    if (batchDeductionF.getDiscountAmount() != null){
                        receivableBill.setDiscountAmount(receivableBill.getDiscountAmount() + batchDeductionF.getDiscountAmount());
                    }
                    receivableBill.setSupCpUnitId(fo.getSupCpUnitId());
                    receivableBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                    receivableBill.setSupCpUnitId(fo.getSupCpUnitId());
                }
            }
            receivableBillList.forEach(v -> {
                billRepository.update(v, new QueryWrapper<ReceivableBill>().eq("id", v.getId())
                    .eq("sup_cp_unit_id", v.getSupCpUnitId()));
            });
        }
        if (CollectionUtils.isNotEmpty(batchAdvanceDeductions)) {
            Map<Long, BatchDeductionF> advanceMap = batchAdvanceDeductions.stream().collect(Collectors.toMap(BatchDeductionF::getId, e -> e));
            List<Long> advanceIds = batchAdvanceDeductions.stream().map(BatchDeductionF::getId).collect(Collectors.toList());
            QueryWrapper<AdvanceBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",advanceIds);
            List<AdvanceBill> advanceBills = advanceBillRepository.list(queryWrapper);
            for (AdvanceBill advanceBill : advanceBills) {
                BatchDeductionF batchDeductionF = advanceMap.get(advanceBill.getId());
                if (batchDeductionF != null){
                    if (batchDeductionF.getDeductibleAmount() != null ){
                        advanceBill.setDeductibleAmount(advanceBill.getDeductibleAmount() + batchDeductionF.getDeductibleAmount());
                    }
                    if (batchDeductionF.getDiscountAmount() != null){
                        advanceBill.setDiscountAmount(advanceBill.getDiscountAmount() + batchDeductionF.getDiscountAmount());
                    }
                    advanceBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                }
            }
            advanceBillRepository.updateBatchById(advanceBills);
        }
        return true;
    }


    public Map<Integer,List<Long>> getBatchDeductionBill(GetBatchDeductionBillF getBatchDeductionBillF) {
        List<Long> roomIds = getBatchDeductionBillF.getRoomIds();
        List<Integer> settleState = getBatchDeductionBillF.getSettleState();
        List<Long> chargeItemId = getBatchDeductionBillF.getChargeItemId();
        QueryWrapper<?> temporaryBillQueryWrapper = new QueryWrapper<>();
        QueryWrapper<?> receivableBillQueryWrapper = new QueryWrapper<>();
        temporaryBillQueryWrapper.eq("deleted",0);
        temporaryBillQueryWrapper.eq("bill_type",BillTypeEnum.临时收费账单.getCode());
        temporaryBillQueryWrapper.ne("carried_state",3);
        temporaryBillQueryWrapper.ne("refund_state",3);
        temporaryBillQueryWrapper.isNull("bill_label");
        temporaryBillQueryWrapper.eq("approved_state",2);
        temporaryBillQueryWrapper.eq("state",0);
        temporaryBillQueryWrapper.eq("verify_state",0);
        temporaryBillQueryWrapper.eq("reversed",0);
        temporaryBillQueryWrapper.eq("overdue",0);

        receivableBillQueryWrapper.eq("deleted",0);
        receivableBillQueryWrapper.eq("bill_type",BillTypeEnum.应收账单.getCode());
        receivableBillQueryWrapper.ne("carried_state",3);
        receivableBillQueryWrapper.ne("refund_state",3);
        receivableBillQueryWrapper.isNull("bill_label");
        receivableBillQueryWrapper.eq("approved_state",2);
        receivableBillQueryWrapper.eq("state",0);
        receivableBillQueryWrapper.eq("verify_state",0);
        receivableBillQueryWrapper.eq("reversed",0);
        receivableBillQueryWrapper.eq("overdue",0);
        if (CollectionUtils.isNotEmpty(roomIds)){
            temporaryBillQueryWrapper.in("room_id",roomIds);
            receivableBillQueryWrapper.in("room_id",roomIds);
        }
        if (CollectionUtils.isNotEmpty(settleState)){
            temporaryBillQueryWrapper.in("settle_state",settleState);
            receivableBillQueryWrapper.in("settle_state",settleState);
        }
        if (CollectionUtils.isNotEmpty(chargeItemId)){
            temporaryBillQueryWrapper.in("charge_item_id",chargeItemId);
            receivableBillQueryWrapper.in("charge_item_id",chargeItemId);
        }
        if (StringUtils.isBlank(getBatchDeductionBillF.getSupCpUnitId())) {
            throw new IllegalArgumentException("上级收费单元ID不能为空!");
        }
        receivableBillQueryWrapper.eq("sup_cp_unit_id", getBatchDeductionBillF.getSupCpUnitId());

        if (ObjectUtil.isNotEmpty(getBatchDeductionBillF.getStartTime()) && ObjectUtil.isNotEmpty(getBatchDeductionBillF.getEndTime())){


            LocalDate endTime = getBatchDeductionBillF.getEndTime().plusDays(1).toLocalDate();

            LocalDate start = getBatchDeductionBillF.getStartTime().toLocalDate();
            temporaryBillQueryWrapper.apply("UNIX_TIMESTAMP(start_time) >= UNIX_TIMESTAMP('" + start + "')");
            temporaryBillQueryWrapper.apply("UNIX_TIMESTAMP(end_time) <= UNIX_TIMESTAMP('" + endTime + "')");
            receivableBillQueryWrapper.apply("UNIX_TIMESTAMP(start_time) >= UNIX_TIMESTAMP('" + start + "')");
            receivableBillQueryWrapper.apply("UNIX_TIMESTAMP(end_time) <= UNIX_TIMESTAMP('" + endTime + "')");
        }
        receivableBillQueryWrapper.eq("sup_cp_unit_id", getBatchDeductionBillF.getSupCpUnitId());

        List<Long> temporaryBillIds = temporaryChargeBillRepository.getTemporaryBillIds(temporaryBillQueryWrapper);
        List<Long> receivableBillIds = billRepository.getReceivableBillIds(receivableBillQueryWrapper);
        Map<Integer,List<Long>> map = new HashMap<>();
        map.put(BillTypeEnum.应收账单.getCode(), receivableBillIds);
        map.put(BillTypeEnum.临时收费账单.getCode(), temporaryBillIds);
        return map;
    }

    public void updateBillApproveState(UpdateBillApproveStateF fo) {
        List<Long> temporaryBillIds = fo.getTemporaryBillIds();
        List<Long> receivableBillIds = fo.getReceivableBillIds();
        receivableBillIds.addAll(temporaryBillIds);
        if (CollectionUtils.isNotEmpty(receivableBillIds) && StringUtils.isNotBlank(fo.getSupCpUnitId())){
            receivableBillRepository.updateBatchApprovedStateById(receivableBillIds,fo.getApprovedState());
        }else if(CollectionUtils.isNotEmpty(receivableBillIds) && StringUtils.isBlank(fo.getSupCpUnitId())) {
            throw new IllegalArgumentException("处理应收账单时，参数上级收费单元ID不能为空!");
        }
    }

    public ReceivableAndTemporaryBillTotalV queryTotalMoney(PageF<SearchF<?>> queryF) {
        return receivableBillRepository.queryTotalMoney(queryF);
    }

    public ReceivableAndTemporaryBillTotalV queryTotalMoney(TemporaryBillF temporaryBillF) {
        return receivableBillRepository.queryTotalMoney(temporaryBillF);
    }


    public List<Long> getRoomIds(TemporaryBillF temporaryBillF) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", temporaryBillF.getCommunityId());
        queryWrapper.eq("deleted", State._0);
        List<String> roomIds = CollectionUtils.isNotEmpty(temporaryBillF.getRoomIdList())?
                Global.mapperFacade.mapAsList(temporaryBillF.getRoomIdList(),String.class):new ArrayList<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(roomIds),"room_id", roomIds);
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getBillIds()),"id", temporaryBillF.getBillIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getStateList()),"state", temporaryBillF.getStateList());
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getSettleStateList()),"settle_state", temporaryBillF.getSettleStateList());
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getCarriedStateList()),"carried_state", temporaryBillF.getCarriedStateList());
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getRefundStateList()),"refund_state", temporaryBillF.getRefundStateList());
        queryWrapper.in(CollectionUtils.isNotEmpty(temporaryBillF.getReversedStateList()),"reversed", temporaryBillF.getReversedStateList());
        return billRepository.getRoomIds(queryWrapper);
    }

    /**
     * 获取待导出的账单数据
     *
     * @param queryF 查询条件
     * @param queryWrapper 列表查询参数
     * @param queryCountWrapper 总数查询参数
     * @param type 0:一般列表 1：审核列表
     * @return
     */
    private IPage<ReceivableBillGroupDto> getExportReceivableBillGroup(
            PageF<SearchF<?>> queryF, QueryWrapper queryWrapper, QueryWrapper<?> queryCountWrapper, int type) {
        int total = 0;
        IPage<ReceivableBillGroupDto> billGroupDtoIPage = null;

        // 由导出中心实现导出时回写的导出任务id
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");

        // 非导出中心（业务系统）实现导出时回写的导出标识
        boolean isExport = MapUtils.getBoolean(queryF.getConditions().getSpecialMap(), "isExport", false);
        if (exportTaskIdObj != null || isExport) {
            // 获取导出总条数
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            if (totalObj == null) {
                ReceivableBillMapper mapper = billRepository.getBaseMapper();
                if (type == 1) {
                    total = mapper.pageWithGroupByApproveCount(getApproveWrapper(queryF, queryCountWrapper));
                } else {
                    total = mapper.pageWithGroupCount(queryCountWrapper);
                }
            } else {
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

                String tblNameSuffix;
                if (exportTaskIdObj == null) {
                    // 非导出中心（业务系统）实现导出时，计算查询条件的MD5做为临时表表名的后缀
                    tblName = "";
                    JSONObject jsonObject = new JSONObject(true);
                    String sqlSegment = queryWrapper.getCustomSqlSegment();
                    Map<String, Object> nameVals = queryWrapper.getParamNameValuePairs();
                    nameVals.keySet().stream().sorted((name1, name2) -> {return name1.compareTo(name2);})
                            .forEach(key -> jsonObject.put(key, nameVals.get(key)));
                    tblNameSuffix = Md5Utils.getMD5(sqlSegment + jsonObject.toJSONString(), "utf-8");
                    if (StringUtils.isEmpty(tblNameSuffix)) {
                        tblNameSuffix = String.valueOf(System.currentTimeMillis());
                    }
                } else {
                    tblNameSuffix = String.valueOf(exportTaskIdObj);
                }

                // 根据查询条件创建临时表
                exportService.createTmpTbl(queryWrapper, tblName, tblNameSuffix,
                        type == 1 ? ExportTmpTblTypeEnum.RECEIVABLE_GROUP_APPROVE : ExportTmpTblTypeEnum.RECEIVABLE_GROUP);

                // 深分页查询优化
                long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
                billGroupDtoIPage = exportService.queryReceivableBillGroupByPageOnTempTbl(
                        Page.of(1, queryF.getPageSize(), false), tblName, tblNameSuffix, tid);
                billGroupDtoIPage.setTotal(total);
            }
        }
        return billGroupDtoIPage;
    }

    /**
     * 统计临时账单和应收账单
     * @param statisticsBillAmountF
     * @return
     */
    public BillTotalDto receivableAndTemporaryBillTotal(StatisticsBillAmountF statisticsBillAmountF) {
        return receivableBillDomainService
                .queryTotal(
                        new StatisticsBillTotalQuery(
                                statisticsBillAmountF.getQuery(),
                                statisticsBillAmountF.getBillIds(),
                                statisticsBillAmountF.getBillInvalid(),
                                statisticsBillAmountF.getBillRefund(),
                                statisticsBillAmountF.getSupCpUnitId()
                        )
                );


    }

    public Boolean updateBillSign(UpdateSignF updateSignF) {
        String shareTableName =
                sharedBillAppService.getShareTableName(updateSignF.getSupCpUnitId(), TableNames.RECEIVABLE_BILL);
        return receivableBillRepository.updateSignByRoomIds(
                List.of(updateSignF.getRoomId()),
                updateSignF.getIsSign() ? 1 : 0,
                shareTableName
        );
    }

    public Boolean batchUpdateBillSign(BatchUpdateSignF batchUpdateSignF) {
        String shareTableName =
                sharedBillAppService.getShareTableName(batchUpdateSignF.getSupCpUnitId(), TableNames.RECEIVABLE_BILL);
        return receivableBillRepository.updateSignByRoomIds(
                batchUpdateSignF.getRoomId(),
                batchUpdateSignF.getIsSign() ? 1 : 0,
                shareTableName
        );
    }


    public Boolean editRecAndTemPushFlag(BillFlagF billFlagF){
        // 判断类型  是添加标记还是取消标记
        if (billFlagF.getFlag().equals(1)) {
            // 添加标记
            receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 1)
                    .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                    .in("id", billFlagF.getBillIdList()));
            // 对应的 调整减免明细更新为已标记
            billAdjustRepository.update(new UpdateWrapper<BillAdjustE>().set("inference_state", 1)
                    .in("bill_id", billFlagF.getBillIdList())
                    .eq("state", 2));
        } else if (billFlagF.getFlag().equals(0)){
            // 取消标记
            // 查询是否生成收入确认
            List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                    .eq(VoucherPushBillDetailZJ::getBillEventType, 1)
                    .eq(VoucherPushBillDetailZJ::getDeleted, 0)
                    .in(VoucherPushBillDetailZJ::getBillId, billFlagF.getBillIdList())
                    .eq(VoucherPushBillDetailZJ::getPushBillState, 2));
//                    .in(VoucherPushBillDetailZJ::getReverseFlag, ReverseFlagEnum.减免.getCode(), ReverseFlagEnum.调整.getCode())
            List<Long> collect = pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList());
            for (Long l : collect) {
                billFlagF.getBillIdList().removeIf(i -> i.equals(l));
            }
            if (CollectionUtils.isNotEmpty( billFlagF.getBillIdList())) {
                // 判断是否生成逆向的 报账单
                List<VoucherPushBillDetailZJ> pushBillDetailZJS1 = voucherBillDetailZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                        .eq(VoucherPushBillDetailZJ::getBillEventType, 1)
                        .eq(VoucherPushBillDetailZJ::getDeleted, 0)
                        .in(VoucherPushBillDetailZJ::getBillId, billFlagF.getBillIdList())
                        .in(VoucherPushBillDetailZJ::getReverseFlag, ReverseFlagEnum.减免.getCode(), ReverseFlagEnum.调整.getCode()));

                List<Long> collect1 = pushBillDetailZJS1.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList());
                for (Long l : collect1) {
                    billFlagF.getBillIdList().removeIf(i -> i.equals(l));
                }


            }
            if (CollectionUtils.isNotEmpty( billFlagF.getBillIdList())) {
                receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                        .in("id", billFlagF.getBillIdList()));
            }
        }
        return true;
    }

}
