package com.wishare.finance.apps.service.reconciliation;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apps.model.reconciliation.fo.AddFlowTempF;
import com.wishare.finance.apps.model.reconciliation.vo.FlowTempRecordV;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.reconciliation.entity.*;
import com.wishare.finance.domains.reconciliation.repository.*;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FlowTempAppService {

    private final FlowTempDetailRepository flowTempDetailRepository;
    private final FlowTempRepository flowTempRepository;
    private final FlowTempRecordRepository flowTempRecordRepository;
    private final FlowDetailRepository flowDetailRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final GatherBillRepository gatherBillRepository;
    private final FlowClaimGatherDetailRepository flowClaimGatherDetailRepository;


    @Transactional
    public Boolean addFlowTemp(AddFlowTempF addFlowTempF) {
        // 计算改流水金额

        long sum = addFlowTempF.getGatherBillList().stream().mapToLong(FlowBillPageDto::getTotalAmount).sum();
        FlowTempRecordE flowTempRecordE = new FlowTempRecordE();
        flowTempRecordE.setName(addFlowTempF.getName());
        flowTempRecordE.setRemark(addFlowTempF.getRemark());
        flowTempRecordE.setGatherNum(addFlowTempF.getGatherBillList().size());
        flowTempRecordE.setGatherAmount(sum);
        flowTempRecordE.setSupCpUnitId(addFlowTempF.getSupCpUnitId());
        flowTempRecordE.init();
        // 插入暂存记录表
        List<FlowTempE> list = flowTempRepository.list(new LambdaQueryWrapper<FlowTempE>().in(FlowTempE::getFlowDetailId, addFlowTempF.getFlowIds())
                .eq(FlowTempE::getCreator, ApiData.API.getUserId().get()));
        for (FlowTempE flowTempE : list) {
            flowTempRepository.remove(new LambdaQueryWrapper<FlowTempE>().eq(FlowTempE::getFlowTempRecordId, flowTempE.getFlowTempRecordId()));
            // flow_temp_record
            flowTempRecordRepository.removeById(flowTempE.getFlowTempRecordId());
            // flow_temp_detail
            flowTempDetailRepository.remove(new LambdaQueryWrapper<FlowTempDetailE>().eq(FlowTempDetailE::getFlowTempRecordId, flowTempE.getFlowTempRecordId()));
        }

        flowTempRecordRepository.save(flowTempRecordE);

        List<FlowTempDetailE> flowTempDetailES = Global.mapperFacade.mapAsList(addFlowTempF.getGatherBillList(), FlowTempDetailE.class);
        for (FlowTempDetailE flowTempDetailE : flowTempDetailES) {
            flowTempDetailE.init();
            flowTempDetailE.setFlowTempRecordId(flowTempRecordE.getId());

        }
        flowTempDetailRepository.saveBatch(flowTempDetailES);

        List<FlowTempE> flowTempES = new ArrayList<>();

        for (Long flowId : addFlowTempF.getFlowIds()) {
            FlowTempE flowTempE = new FlowTempE();
            flowTempE.init();
            flowTempE.setFlowDetailId(flowId);
            flowTempE.setFlowTempRecordId(flowTempRecordE.getId());
            flowTempE.setFlowTempRecordName(flowTempRecordE.getName());
            flowTempES.add(flowTempE);
        }
        return flowTempRepository.saveBatch(flowTempES);
    }



    public List<FlowBillPageDto> queryFlowTempDetail( Long flowTempRecordId) {
        List<FlowTempDetailE> detailEList = flowTempDetailRepository.list(new LambdaQueryWrapper<FlowTempDetailE>().eq(FlowTempDetailE::getFlowTempRecordId, flowTempRecordId));
        return Global.mapperFacade.mapAsList(detailEList, FlowBillPageDto.class);
    }

    public List<FlowTempRecordV> queryFlowTemp(List<Long> flowIds) {
        // 根据id查询
        List<FlowTempE> list = flowTempRepository.list(new LambdaQueryWrapper<FlowTempE>().in(FlowTempE::getFlowDetailId, flowIds).eq(FlowTempE::getCreator, ApiData.API.getUserId().get()));
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        // 判断该id 下几个暂存记录 切是否对应
        List<Long> collect = list.stream().map(FlowTempE::getFlowTempRecordId).collect(Collectors.toList());
        // 对应就返回数据， 不对应则不返回数据
        List<FlowTempE> flowTempList = flowTempRepository.list(new LambdaQueryWrapper<FlowTempE>().in(FlowTempE::getFlowTempRecordId, collect).eq(FlowTempE::getCreator, ApiData.API.getUserId().get()));
        List<Long> flowIdList = flowTempList.stream().map(FlowTempE::getFlowDetailId).collect(Collectors.toList());

        if (!flowIds.containsAll(flowIdList)){
            return null;
        }

        List<FlowTempRecordE> flowTempRecordEList = flowTempRecordRepository.list(new LambdaQueryWrapper<FlowTempRecordE>().in(FlowTempRecordE::getId, collect));
        List<FlowTempRecordV> flowTempRecordVS = Global.mapperFacade.mapAsList(flowTempRecordEList, FlowTempRecordV.class);

        // 查询流水是否被认领
        List<FlowDetailE> flowDetailEList = flowDetailRepository.list(new LambdaQueryWrapper<FlowDetailE>().ne(FlowDetailE::getClaimStatus, 0).in(FlowDetailE::getId, flowIdList));
        if (CollectionUtils.isNotEmpty(flowDetailEList)){
            for (FlowTempRecordV flowTempRecordV : flowTempRecordVS) {
                flowTempRecordV.setState(1);
            }
            return flowTempRecordVS;
        }
        // 查询收款单是否被冲销
        List<FlowTempDetailE> flowTempDetailEList = flowTempDetailRepository.list(new LambdaQueryWrapper<FlowTempDetailE>().in(FlowTempDetailE::getFlowTempRecordId, collect));

        StringBuilder stringBuilder = new StringBuilder();
        for (FlowTempDetailE flowTempDetailE : flowTempDetailEList) {
            String gatherDetailIds = flowTempDetailE.getGatherDetailIds();
            stringBuilder.append(gatherDetailIds);
            stringBuilder.append(",");
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        String string = stringBuilder.toString();
        List<Long> collect1 = Arrays.asList(string.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        List<FlowClaimGatherDetailE> gatherDetailES = flowClaimGatherDetailRepository.list(new LambdaQueryWrapper<FlowClaimGatherDetailE>().in(FlowClaimGatherDetailE::getGatherDetailId, collect1));
        if (CollectionUtils.isNotEmpty(gatherDetailES)){
            for (FlowTempRecordV flowTempRecordV : flowTempRecordVS) {
                flowTempRecordV.setState(1);
            }
            return flowTempRecordVS;
        }

        List<Long> gatherBillIdList = flowTempDetailEList.stream().map(FlowTempDetailE::getBillId).collect(Collectors.toList());
        List<GatherBill> list1 = gatherBillRepository.list(new LambdaQueryWrapper<GatherBill>()
                .in(GatherBill::getId, gatherBillIdList)
                .eq(GatherBill::getReversed, BillReverseStateEnum.已冲销.getCode())
                .eq(GatherBill::getSupCpUnitId, flowTempRecordEList.get(0).getSupCpUnitId()));
        if (CollectionUtils.isNotEmpty(list1)){
            for (FlowTempRecordV flowTempRecordV : flowTempRecordVS) {
                flowTempRecordV.setState(1);
            }
            return flowTempRecordVS;
        }
        for (FlowTempDetailE flowTempDetailE : flowTempDetailEList) {
            String[] split = flowTempDetailE.getGatherDetailIds().split(",");
            List<String> list2 = Arrays.asList(split);
            List<GatherDetail> gatherDetailList = gatherDetailRepository.list(new LambdaQueryWrapper<GatherDetail>()
                    .in(GatherDetail::getId, list2.stream().map(Long::parseLong).collect(Collectors.toList()))
                    .eq(GatherDetail::getAvailable, 1)
                    .eq(GatherDetail::getSupCpUnitId, flowTempRecordEList.get(0).getSupCpUnitId()));
            if (CollectionUtils.isNotEmpty(gatherDetailList)){
                for (FlowTempRecordV flowTempRecordV : flowTempRecordVS) {
                    flowTempRecordV.setState(1);
                }
                return flowTempRecordVS;
            }
        }
        return flowTempRecordVS;
    }


    @Transactional
    public Boolean deleteFlowTemp(Long flowTempId) {
        // 删除三张表的数据
        // flow_temp
        flowTempRepository.remove(new LambdaQueryWrapper<FlowTempE>().eq(FlowTempE::getFlowTempRecordId, flowTempId));
        // flow_temp_record
        flowTempRecordRepository.removeById(flowTempId);
        // flow_temp_detail
        flowTempDetailRepository.remove(new LambdaQueryWrapper<FlowTempDetailE>().eq(FlowTempDetailE::getFlowTempRecordId, flowTempId));
        return true;
    }
}
