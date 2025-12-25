package com.wishare.finance.domains.bill.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.AllBillPageDto;
import com.wishare.finance.domains.bill.dto.ReceiptAmountDto;
import com.wishare.finance.domains.bill.dto.ReconciliationBillDto;
import com.wishare.finance.domains.bill.repository.mapper.BillMapper;
import com.wishare.starter.beans.PageV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author szh
 * @date 2024/4/7 15:02
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiptAmountUtils {

    private final BillMapper billMapper;

    /**
     * 求 收款方式为结转和押金结转的收款金额
     *
     * @param gatherDetailName 分表名称
     * @param gatheIdSet       gatheIdSet
     * @return
     */
    public Map<Long, Long> getReceiptAmount(String gatherDetailName, Set<Long> gatheIdSet) {
        if (CollUtil.isEmpty(gatheIdSet)) {
            return Collections.emptyMap();
        }

        List<ReceiptAmountDto> list = billMapper.getReceiptAmount(gatherDetailName, gatheIdSet);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }

        return list.stream().collect(Collectors.toMap(ReceiptAmountDto::getGatherBillId, ReceiptAmountDto::getNoCheckAmount, (a, b) -> a));

    }

    public void handleNoCheckAmount(String gatherDetailName, IPage<AllBillPageDto> billDtoPage) {
        if (ObjectUtil.isNull(billDtoPage) || CollUtil.isEmpty(billDtoPage.getRecords())) {
            return;
        }
        List<AllBillPageDto> list = billDtoPage.getRecords();
        Set<Long> id = list.stream().filter(v -> BillTypeEnum.收款单.getCode() == v.getBillType()).map(AllBillPageDto::getId).collect(Collectors.toSet());
        Map<Long, Long> map = getReceiptAmount(gatherDetailName, id);

        log.error("szh-{}",map);
        list.forEach(v -> {
            Long aLong = map.getOrDefault(v.getId(), 0L);
            log.error("szh-v.getId()-{},value-{}",v.getId(),aLong);
            v.setTotalAmount(v.getTotalAmount() - aLong);
        });
    }

    public void handleNoCheckAmount(String gatherDetailName,  List<AllBillPageDto> list) {
        if ( CollUtil.isEmpty(list)) {
            return;
        }
        Set<Long> id = list.stream().filter(v -> BillTypeEnum.收款单.getCode() == v.getBillType()).map(AllBillPageDto::getId).collect(Collectors.toSet());
        Map<Long, Long> map = getReceiptAmount(gatherDetailName, id);

        list.forEach(v -> {
            v.setTotalAmount(v.getTotalAmount() - map.getOrDefault(v.getId(), 0L));
        });
    }

    public void handleNoCheckAmount(String gatherDetailName, PageV<ReconciliationBillDto> page) {

        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return;
        }
        List<ReconciliationBillDto> list = page.getRecords();
        Set<Long> id = list.stream().filter(v -> BillTypeEnum.收款单.getCode() == v.getBillType())
                .map(ReconciliationBillDto::getId).collect(Collectors.toSet());
        Map<Long, Long> map = getReceiptAmount(gatherDetailName, id);

        list.forEach(v -> {
            v.setActualAmount(v.getReceivableAmount() - map.getOrDefault(v.getId(), 0L));
            v.setReceivableAmount(v.getReceivableAmount() - map.getOrDefault(v.getId(), 0L));
        });

    }
}
