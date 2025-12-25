package com.wishare.finance.domains.bill.repository;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.FinanceSearchF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.TemporaryChargeBillPageV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.infrastructure.conts.*;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPaymentVO;
import com.wishare.finance.infrastructure.utils.SearchFieldSortUtils;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fengxiaolin
 * @date 2023/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceivableAndTemporaryBillRepository extends
        ServiceImpl<ReceivableBillMapper, ReceivableBill> implements BillRepository<ReceivableBill>{

    private final SharedBillAppService sharedBillAppService;

    @Autowired
    private ExportService exportService;
    private final ContractClient contractClient;


    @Override
    public List<ReceivableBill> getByIdsNoTenant(List<Long> billIds,String supCpUnitId) {
        return null;
    }

    @Override
    public Boolean updateInvoiceState(List<ReceivableBill> updateBills) {
        return null;
    }

    @Override
    public BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid,
        Integer billRefund, String supCpUnitId) {

        QueryWrapper<?> wrapper = getQueryAndIdsWrapper(query, billIds, billInvalid, billRefund);

        wrapper.in("b.bill_type", List.of(BillTypeEnum.应收账单.getCode(),BillTypeEnum.临时收费账单.getCode()));
        //有效账单条件
        if(Objects.isNull(billInvalid) || billInvalid != 1){
            wrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
        }
        wrapper.eq(StringUtils.isNotBlank(supCpUnitId),"b.sup_cp_unit_id", supCpUnitId);
        return baseMapper.queryTotal(wrapper);
    }



    @Override
    public List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper,String supCpUnitId) {
        return null;
    }

    @Override
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query) {
        return null;
    }

    @Override
    public QueryWrapper<?> getQueryAndIdsWrapper(PageF<SearchF<?>> query, List<Long> billIds,
        Integer billInvalid, Integer billRefund) {
        return BillRepository.super.getQueryAndIdsWrapper(query, billIds, billInvalid, billRefund);
    }

    //收费工作台
    @Override
    public IPage<ReceivableBill> queryBillByPage(PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        List<Field> fields = query.getConditions().getFields();
        List<Field> supCpUnitIds = fields.stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
        log.info("queryBillByPage导出sup_cp_unit_id ----->Field:{}", JSON.toJSONString(supCpUnitIds));
        List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        Object getPayerIdIsNullValue = query.getConditions().getSpecialMap().get("getPayerIdIsNull");
        boolean flag = !Objects.isNull(getPayerIdIsNullValue) && (boolean) getPayerIdIsNullValue;
        if(CollectionUtils.isNotEmpty(collect) && flag){
            fields.removeAll(collect);
        }
        normalFieldBillCondition(query);

        //分页获取应收账单，单独处理结转应收时同时获取收费对象为空或为该房号收费对象的账单
        QueryWrapper<?> wrapper = getWrapper(query);
        if(CollectionUtils.isNotEmpty(collect) && Objects.nonNull(collect.get(0).getValue()) && flag){
            wrapper.and(queryWrapper -> queryWrapper.in("b.payer_id", (List)collect.get(0).getValue())
                    .or()
                    .eq("b.payer_id", StringUtils.EMPTY)
                    .or()
                    .isNull("b.payer_id")
            );
        }else if (flag) {
            wrapper.isNull("b.payer_id");
        }
        // 导出场合
        IPage<ReceivableBill> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            String tblName = TableNames.RECEIVABLE_BILL;
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.queryBillCountByPage(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                log.info("导出sup_cp_unit_id ----->Field:{}", JSON.toJSONString(supCpUnitIds));
                if (!supCpUnitIds.isEmpty()) {
                    tblName = sharedBillAppService.getShareTableName(supCpUnitIds.get(0).getValue().toString(), tblName);
                    log.info("导出sup_cp_unit_id ----->tblName:{}", tblName);
                }
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.RECEIVABLE);

                // 深分页查询优化
                long tid = (query.getPageNum() - 1) * query.getPageSize();
                queryPage = exportService.queryReceivableBillByPageOnTempTbl(
                        Page.of(1, query.getPageSize(), false), tblName, exportTaskId, tid);
                queryPage.setTotal(count);
                return queryPage;
            }
        }
        IPage<ReceivableBill> billList = baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize(), query.isCount()), wrapper);
        if (billList!=null&& CollectionUtils.isNotEmpty(billList.getRecords())){
            //获取来源为“合同”的收入方信息
            Map<String, List<ContractPaymentVO>> contractPaymentVOMap = new HashMap<>();
            List<String> contractIds = billList.getRecords().stream().filter(x->"收入合同".equals(x.getSource()) || "支出合同".equals(x.getSource()))
                    .map(ReceivableBill::getExtField6).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractIds)){
                contractPaymentVOMap = contractClient.getContractPaymentList(contractIds).stream()
                        .collect(Collectors.groupingBy(ContractPaymentVO::getContractId));
            }
            for (ReceivableBill record : billList.getRecords()) {
                if("收入合同".equals(record.getSource()) || "支出合同".equals(record.getSource())){
                    ContractPaymentVO contractPayment = contractPaymentVOMap.get(record.getExtField6()).get(0);
                    record.setPayerId(StringUtils.isEmpty(contractPayment.getPayeeId()) ? contractPayment.getIncomeId() : contractPayment.getPayeeId());
                    record.setPayerName(StringUtils.isEmpty(contractPayment.getPayeeName()) ? contractPayment.getIncomeName() : contractPayment.getPayeeName());
                }
            }
        }
        return billList;
    }

    @Override
    public QueryWrapper getWrapper(PageF<SearchF<?>> query){
        QueryWrapper<?> queryWrapper = SearchFieldSortUtils.sortField(query);
        queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode());

        if (CollectionUtils.isEmpty(query.getOrderBy())){
            queryWrapper.orderByAsc("b.sup_cp_unit_id").orderByAsc("b.room_id");
        }else {
            for (OrderBy item: query.getOrderBy()) {
                if (item.isAsc()) {
                    queryWrapper.orderByAsc(item.getField());
                }else {
                    queryWrapper.orderByDesc(item.getField());
                }
            }
        }
        Map<String, Object> specialMap = Optional.ofNullable(query.getConditions().getSpecialMap()).orElse(new HashMap<>());
        if (Objects.nonNull(specialMap.get("payerTypeIsNotDeveloper"))) {
            queryWrapper.and(wrapper -> wrapper.ne("b.payer_type", 99) .or().isNull("b.payer_type"));
        }
        return queryWrapper;
    }

    @Override
    public IPage<ReceivableBill> listByInitialBill(Page<Object> page, QueryWrapper<?> queryWrapper) {
        return null;
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds, String supCpUnitId) {
        return null;
    }

    @Override
    public BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        return null;
    }

    @Override
    public Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds) {
        return null;
    }

    @Override
    public IPage<ReceivableBill> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF) {
        return null;
    }


    public List<Long> getReceivableBillIds(QueryWrapper<?> queryWrapper) {
        return baseMapper.getReceivableBillIds(queryWrapper);
    }

    public List<Long> getRoomIds(QueryWrapper<?> queryWrapper) {
        return baseMapper.getRoomIds(queryWrapper);
    }
}
