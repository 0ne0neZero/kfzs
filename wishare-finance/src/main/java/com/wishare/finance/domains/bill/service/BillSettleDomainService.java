package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillSettleDomainService {

    @Autowired
    private GatherDetailRepository gatherDetailRepository;


    public List<BillSettleChannelV> listBillSettleChannelByIds(List<Long> list,String supCpUnitId) {
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByRecBillIds(list, supCpUnitId);
        List<BillSettleChannelV> settleChannelVList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleChannelV billSettleChannelV = new BillSettleChannelV();
            billSettleChannelV.setId(gatherDetail.getId());
            billSettleChannelV.setBillId(gatherDetail.getRecBillId());
            billSettleChannelV.setSettleChannel(gatherDetail.getPayChannel());
            settleChannelVList.add(billSettleChannelV);
        });
        return settleChannelVList;
    }

    /**
     * 根据结算id列表查询结算记录
     * @param settleIds
     * @return
     */
    public List<BillSettleDto> getByIds(List<Long> settleIds,String supCpUnitId){
        List<GatherDetail> gatherDetailList = gatherDetailRepository.list(new QueryWrapper<GatherDetail>()
            .in("id", settleIds).eq("sup_cp_unit_id", supCpUnitId));
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
            billSettleDtoList.add(billSettleDto);
        });
        return billSettleDtoList;
    }

    /**
     * 根据账单id获取结算记录
     * @param billId
     * @return
     */
    public List<BillSettleDto> getByBillId(Long billId,Integer billType, String supCpUnitId){
        List<GatherDetail> gatherDetailList = gatherDetailRepository.listByBillId(billId,billType,supCpUnitId);
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
            billSettleDtoList.add(billSettleDto);
        });
        return billSettleDtoList;
    }

    /**
     * 根据账单id获取结算记录
     * @param billIds
     * @return
     */
    public List<BillSettleDto> getByBillIds(List<Long> billIds, String supCpUnitId){
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByRecBillIds(billIds,supCpUnitId);
        List<BillSettleDto> billSettleDtoList = Lists.newArrayList();
        gatherDetailList.forEach(gatherDetail -> {
            BillSettleDto billSettleDto = new BillSettleDto().generalBillSettleDto(gatherDetail);
            billSettleDtoList.add(billSettleDto);
        });
        return billSettleDtoList;
    }

    /**
     * 根据账单ids和结算方式获取对应的账单ids
     * @param form
     * @return
     */
    public List<Long> listBillIdsByIdsAndChannel(SettleChannelAndIdsF form) {
        return gatherDetailRepository.listBillIdsByIdsAndChannel(form);
    }

}
