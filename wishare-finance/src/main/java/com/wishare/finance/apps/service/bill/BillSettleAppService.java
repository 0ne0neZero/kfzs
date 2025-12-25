package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.domains.bill.service.BillSettleDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 结算应用服务
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillSettleAppService {

    private final BillSettleDomainService billSettleDomainService;

    /**
     * 根据结算id列表查询结算记录
     * @param settleIds
     * @return
     */
    public List<BillSettleV> getSettleByIds(List<Long> settleIds,String supCpUnitId){
        return CollectionUtils.isNotEmpty(settleIds) ? Global.mapperFacade.mapAsList(billSettleDomainService.getByIds(settleIds, supCpUnitId), BillSettleV.class) : new ArrayList<>();
    }

    public List<BillSettleChannelV> listBillSettleChannelByIds(List<Long> list, String supCpUnitId) {
        return billSettleDomainService.listBillSettleChannelByIds(list, supCpUnitId);
    }

    public List<Long> listBillIdsByIdsAndChannel(SettleChannelAndIdsF form) {
        return billSettleDomainService.listBillIdsByIdsAndChannel(form);
    }
    /**
     * 根据账单id获取结算记录
     * @param billId
     * @return
     */
    public List<BillSettleV> getSettleByBillId(Long billId,Integer billType, String supCpUnitId) {
        return Objects.nonNull(billId) ? Global.mapperFacade.mapAsList(billSettleDomainService.getByBillId(billId,billType, supCpUnitId), BillSettleV.class) : new ArrayList<>();
    }

    /**
     * 根据账单id获取结算记录
     * @param billIds
     * @return
     */
    public List<BillSettleV> getSettleByBillIds(List<Long> billIds, String supCpUnitId) {
        return CollectionUtils.isNotEmpty(billIds) ? Global.mapperFacade.mapAsList(billSettleDomainService.getByBillIds(billIds, supCpUnitId), BillSettleV.class) : new ArrayList<>();
    }

}
