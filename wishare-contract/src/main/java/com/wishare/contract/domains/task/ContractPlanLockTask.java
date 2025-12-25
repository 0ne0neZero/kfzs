package com.wishare.contract.domains.task;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanStatisticsV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.consts.Const;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 终止合同定时任务
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
@RequiredArgsConstructor
public class ContractPlanLockTask implements IOwlApiBase {

    private final ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;


    private final ContractIncomeConcludeService contractIncomeConcludeService;

    /**
     * 每天凌晨一点执行
     */
    // @Scheduled(cron = "0 01 14 * * ? ")
    public void contractPlanTask() {
        log.info("锁死拉----------");
        QueryWrapper<ContractPayPlanConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayPlanConcludeE.DELETED, 0)
                .eq(ContractPayPlanConcludeE.PID,"0");
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.selectList(queryWrapper);
        for(ContractPayPlanConcludeE s : contractIncomePlanConcludeVList){
            log.info("父计划----------"+ JSONObject.toJSONString(s));
            if(s.getSettleSumAmount().compareTo(BigDecimal.ZERO) == 0){
               continue;
            }
            BigDecimal useAmount = BigDecimal.ZERO;
            QueryWrapper<ContractPayPlanConcludeE> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq(ContractPayPlanConcludeE.DELETED, 0)
                    .eq(ContractPayPlanConcludeE.PID,s.getId());
            List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList1 = contractPayPlanConcludeMapper.selectList(queryWrapper1);
            if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList1)){
                contractIncomePlanConcludeVList1 = contractIncomePlanConcludeVList1.stream().sorted(Comparator.comparing(ContractPayPlanConcludeE::getTermDate)).collect(Collectors.toList());
            }
            for(ContractPayPlanConcludeE sb : contractIncomePlanConcludeVList1){
                log.info("子计划----------"+ JSONObject.toJSONString(sb));
                useAmount = useAmount.add(sb.getPlannedCollectionAmount());
                if(sb.getPaymentStatus() == 2){
                    continue;
                }
                if(useAmount.compareTo(s.getSettleSumAmount()) < 0){
                    sb.setSettlementAmount(sb.getPlannedCollectionAmount());
                    sb.setPaymentStatus(2);
                    sb.setNoPayAmount(BigDecimal.ZERO);
                    contractPayPlanConcludeMapper.updateById(sb);
                    continue;
                }
                if(useAmount.compareTo(s.getSettleSumAmount()) > 0){
                    BigDecimal sd = useAmount.subtract(s.getSettleSumAmount());
                    sb.setNoPayAmount(sd);
                    sb.setPaymentStatus(1);
                    sb.setSettlementAmount(sb.getPlannedCollectionAmount().subtract(sd));
                    contractPayPlanConcludeMapper.updateById(sb);
                    break;
                }
                if(useAmount.compareTo(s.getSettleSumAmount()) == 0){
                    sb.setSettlementAmount(sb.getPlannedCollectionAmount());
                    sb.setPaymentStatus(2);
                    sb.setNoPayAmount(BigDecimal.ZERO);
                    contractPayPlanConcludeMapper.updateById(sb);
                    break;
                }
            }
        }

    }

    /**
     * 每天凌晨一点执行
     */
//    @Scheduled(cron = "0 10 19 * * ? ")
//    public void contractPullFxmTask() {
//        List<String> ids = new ArrayList<>();
//        ids.add("17000949955902");
//        ids.add("17078822169902");
//        ids.add("17035462260304");
//        ids.add("17033764232801");
//        ids.add("17137708995701");
//        ids.add("17155652021004");
//        ids.add("17188351039108");
//        ids.add("17156158395806");
//        ids.add("17156069991605");
//        ids.add("17150541869600");
//        ids.add("16793389165705");
//        ids.add("17121295603901");
//        ids.add("16903830893904");
//        ids.add("17257886791803");
//        ids.add("17034804479002");
//        ids.add("17009258504607");
//        ids.add("16843035505904");
//        ids.add("17257612427802");
//        ids.add("17380892029604");
//        ids.add("17387123168606");
//        QueryWrapper<ContractIncomeConcludeE> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq(ContractIncomeConcludeE.DELETED, 0)
//                .eq(ContractIncomeConcludeE.REVIEW_STATUS,2)
//                .notIn(ContractIncomeConcludeE.ID,ids)
//                .eq(ContractIncomeConcludeE.SEAL_TYPE,1);
//        List<ContractIncomeConcludeE> contractIncomeConcludeEList = contractIncomeConcludeService.list(queryWrapper1);
//        if(ObjectUtils.isNotEmpty(contractIncomeConcludeEList)){
//            String userId = contractIncomeConcludeEList.get(0).getCreator();
//            String tenantId = contractIncomeConcludeEList.get(0).getTenantId();
//            setGateWay(userId, tenantId);
//        }
//        for(ContractIncomeConcludeE s : contractIncomeConcludeEList){
//            log.info("定时任务开始跑了id：" + s.getId() );
//            contractIncomeConcludeService.contractInfoToFxm(s.getId());
//        }
//    }


    public void setGateWay(String userId, String tenantId){
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setUserId(userId);
        identityInfo.setUserName("");
        identityInfo.setTenantId(tenantId);
        identityInfo.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set(Const.IDENTITY_INFO, identityInfo);
    }
}
