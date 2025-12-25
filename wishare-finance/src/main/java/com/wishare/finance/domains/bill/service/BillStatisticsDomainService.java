package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.component.imports.utils.FieldUtils;
import com.wishare.finance.apps.model.bill.vo.BillTotalV;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalCommand;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillCarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillApproveTotalNewDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.SearchFileUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账单统计领域服务
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillStatisticsDomainService {


    private final ReceivableBillRepository receivableBillRepository;
    private final TemporaryChargeBillRepository temporaryChargeBillRepository;
    private final AdvanceBillRepository advanceBillRepository;

    /**
     * 根据审核状态查询账单合计
     * @param approveStates
     * @return
     */
    public List<BillApproveTotalDto> queryApproveTotal(List<BillApproveStateEnum> approveStates, PageF<SearchF<?>> query) {
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        Field field = SearchFileUtil.getField(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        List<BillApproveTotalDto> totals = new ArrayList<>();
        List<Integer> states = approveStates.stream().map(BillApproveStateEnum::getCode).collect(Collectors.toList());
        QueryWrapper<?> wrapper;
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        if(Objects.nonNull(query)){
            wrapper = query.getConditions().getQueryModel();
        }else{
            wrapper = new QueryWrapper<>();
        }
        wrapper.in(CollectionUtils.isNotEmpty(approveStates), "b.approved_state", states);
        wrapper.eq("b.state", 0);
        wrapper.groupBy(List.of("operateType", "b.approved_state"));
        wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode());
        List<BillApproveTotalDto> advanceTotals = advanceBillRepository.queryApproveTotal(
                wrapper, (String) field.getValue()
        );
        if (CollectionUtils.isNotEmpty(advanceTotals)){
            advanceTotals.forEach(item-> item.setType(BillTypeEnum.预收账单.getCode()));
            totals.addAll(advanceTotals);
        }

        QueryWrapper<?> receiveWrapper = wrapper.clone();
        receiveWrapper.groupBy("b.bill_type");
        List<BillApproveTotalDto> billApproveTotalDtos = receivableBillRepository.queryApproveTotalNew(receiveWrapper);
        if (CollectionUtils.isNotEmpty(billApproveTotalDtos)){
            totals.addAll(billApproveTotalDtos);
        }
//        List<BillApproveTotalDto> receiveTotals = receivableBillRepository.queryApproveTotal(receiveWrapper);
//        if (CollectionUtils.isNotEmpty(receiveTotals)){
//            receiveTotals.forEach(item-> item.setType(BillTypeEnum.应收账单.getCode()));
//            totals.addAll(receiveTotals);
//        }
//        QueryWrapper<?> temporaryWrapper = wrapper.clone();
//        List<BillApproveTotalDto> temporaryTotals = temporaryChargeBillRepository.queryApproveTotal(temporaryWrapper);
//        if (CollectionUtils.isNotEmpty(temporaryTotals)){
//            temporaryTotals.forEach(item-> item.setType(BillTypeEnum.临时收费账单.getCode()));
//            totals.addAll(temporaryTotals);
//        }
        return totals;
    }

    public List<BillApproveTotalDto> queryAdvanceApproveTotal(List<Integer> states ,
                                                              QueryWrapper<?> wrapper,
                                                              String supCpUnitId) {

        wrapper.groupBy(List.of("operateType", "b.approved_state"));
        List<BillApproveTotalDto> advanceTotals = advanceBillRepository.queryApproveTotal(wrapper, supCpUnitId);
        if (CollectionUtils.isNotEmpty(advanceTotals)){
            advanceTotals.forEach(item-> item.setType(BillTypeEnum.预收账单.getCode()));
        }
        return advanceTotals;
    }

    public List<BillApproveTotalNewDto> queryReceivableBillApproveTotal(List<Integer> states ,
                                                                        QueryWrapper<?> wrapper,
                                                                        String supCpUnitId ) {
        wrapper.in(CollectionUtils.isNotEmpty(states), "b.approved_state", states);
        wrapper.eq("b.state", 0);
        wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode());
        return receivableBillRepository.queryApproveBillTotal(wrapper, supCpUnitId);
    }

    public List<BillApproveTotalNewDto> queryReceivableBillUnionApproveTotal(List<Integer> states ,
            QueryWrapper<?> wrapper,
            String supCpUnitId ) {
        wrapper.in(CollectionUtils.isNotEmpty(states), "b.approved_state", states);
        wrapper.eq("b.state", 0);
        wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode());
        wrapper.eq("ba.deleted", 0);
        wrapper.in("ba.approved_state", List.of(0,1));
        return receivableBillRepository.queryApproveUnionBillTotal(wrapper, supCpUnitId);
    }

    /**
     * 统计减免合计金额
     * @param discountTotalQuery
     * @return
     */
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery discountTotalQuery) {
        //1:应收账单，2:预收账单，3:临时收费账单 66全部
        Integer billType = discountTotalQuery.getBillType();
        if (billType == 1){
            return receivableBillRepository.queryDiscountTotal(discountTotalQuery);
        }
        if (billType == 2){
            return temporaryChargeBillRepository.queryDiscountTotal(discountTotalQuery);
        }
        if (billType == 3){
            return advanceBillRepository.queryDiscountTotal(discountTotalQuery);
        }
        BillDiscountTotalDto billDiscountTotalDto = new BillDiscountTotalDto();
        if (billType == 66){
            billDiscountTotalDto.add(receivableBillRepository.queryDiscountTotal(discountTotalQuery));
            billDiscountTotalDto.add(temporaryChargeBillRepository.queryDiscountTotal(discountTotalQuery));
            billDiscountTotalDto.add(advanceBillRepository.queryDiscountTotal(discountTotalQuery));
        }
        return billDiscountTotalDto;
    }
}
