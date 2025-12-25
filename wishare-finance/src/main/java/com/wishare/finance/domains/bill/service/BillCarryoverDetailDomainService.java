package com.wishare.finance.domains.bill.service;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.bill.entity.BillCarryoverDetailE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yyx
 * @project wishare-finance
 * @title BillCarryoverDetailDomainService
 * @date 2023.09.21  11:01
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryoverDetailDomainService {

    private final BillCarryoverDetailRepository billCarryoverDetailRepository;

    private final BillCarryoverRepository billCarryoverRepository;

    // 每次查询数量
    private static final int PAGE_SIZE = 250;


    public Boolean reverseCarryoverInfo(Boolean isTodayOpr) {
        // 分页获取所有审核通过的结转明细
        int currentPage = 1;
        PageF<SearchF<?>> queryF = PageF.of(currentPage,
                PAGE_SIZE, new SearchF<>());
        queryF.getConditions().setFields(new ArrayList<>());
        if (isTodayOpr){
            queryF.getConditions().getFields().add(new Field("b.gmt_modify", LocalDateTime.now().with(LocalTime.MIN), 4));
        }

        PageV<BillCarryoverE> carriedPage = billCarryoverRepository.getBillCarriedPage(queryF);
        while(carriedPage.getPageNum() >= currentPage) {
            for(BillCarryoverE billCarryoverE : carriedPage.getRecords()) {
                // 滤除反向结转的记录单
                if (billCarryoverE.getCarryoverAmount()<=0){
                    continue;
                }
                List<BillCarryoverDetailE> resultList = Lists.newArrayList();
                for (CarryoverDetail carryoverDetail : billCarryoverE.getCarryoverDetail()) {
                    BillCarryoverDetailE billCarryoverDetailE = new BillCarryoverDetailE();
                    billCarryoverDetailE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CARRYOVER_DETAIL));
                    billCarryoverDetailE.setBillType(billCarryoverE.getBillType());
                    billCarryoverDetailE.setCarriedBillId(billCarryoverE.getCarriedBillId());
                    billCarryoverDetailE.setCarriedBillNo(billCarryoverE.getCarriedBillNo());
                    billCarryoverDetailE.setTargetBillId(carryoverDetail.getTargetBillId());
                    billCarryoverDetailE.setTargetBillNo(carryoverDetail.getTargetBillNo());
                    billCarryoverDetailE.setCarryoverType(billCarryoverE.getCarryoverType());
                    billCarryoverDetailE.setCarryoverAmount(carryoverDetail.getActualCarryoverAmount()==null? carryoverDetail.getCarryoverAmount():carryoverDetail.getActualCarryoverAmount());
                    billCarryoverDetailE.setCarryoverTime(billCarryoverE.getCarryoverTime());
                    billCarryoverDetailE.setBillCarryoverId(billCarryoverE.getId());
                    billCarryoverDetailE.setRemark(billCarryoverE.getRemark());
                    if (!validInfo(billCarryoverDetailE)){
                        continue;
                    }
                    resultList.add(billCarryoverDetailE);
                }
                billCarryoverDetailRepository.saveBatch(resultList);
            }
            queryF.setPageNum(++currentPage);
            carriedPage = billCarryoverRepository.getBillCarriedPage(queryF);
        }
        return true;
    }

    /**
     * 校验结转历史数据
     * @param billCarryoverDetailE info
     * @return {@link Boolean}
     */
    private Boolean validInfo(BillCarryoverDetailE billCarryoverDetailE) {
        if (Objects.isNull(billCarryoverDetailE)){
            return false;
        }
        if (ObjectUtils.anyNull(billCarryoverDetailE.getTargetBillId(),billCarryoverDetailE.getTargetBillNo()
        ,billCarryoverDetailE.getCarryoverAmount())){
            return false;
        }
        return true;
    }
}
