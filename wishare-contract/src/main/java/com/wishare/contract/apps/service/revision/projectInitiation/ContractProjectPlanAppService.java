package com.wishare.contract.apps.service.revision.projectInitiation;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectPlanF;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectInitiationE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectPlanE;
import com.wishare.contract.domains.mapper.revision.projectInitiation.ContractProjectPlanMapper;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectInitiationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanCostConfirmV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.DynamicCostSurplusPropReqF;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 合约规划Service实现类
 */
@Service
@AllArgsConstructor
public class ContractProjectPlanAppService extends ServiceImpl<ContractProjectPlanMapper, ContractProjectPlanE> implements IOwlApiBase {

    private final ContractProjectPlanMonthlyAllocationAppService contractProjectPlanMonthlyAllocationAppService;

    private final ContractProjectPlanCostConfirmAppService contractProjectPlanCostConfirmAppService;

    public void getByProjectInitiationId(ContractProjectInitiationV contractProjectInitiationV, Boolean queryMonthlyFlag) {
        String projectInitiationId = contractProjectInitiationV.getId();

        LambdaQueryWrapper<ContractProjectPlanE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanE::getDeleted, 0);
        List<ContractProjectPlanE> projectPlanEList = this.list(queryWrapper);
        List<ContractProjectPlanV> contractProjectPlanVS = Global.mapperFacade.mapAsList(projectPlanEList, ContractProjectPlanV.class);

        Map<Integer, List<ContractProjectPlanV>> contractProjectPlanVMap = contractProjectPlanVS.stream().collect(Collectors.groupingBy(ContractProjectPlanV::getType));
        contractProjectInitiationV.setContractPlanList(contractProjectPlanVMap.getOrDefault(0, new ArrayList<>()));
        contractProjectInitiationV.setContractList(contractProjectPlanVMap.get(1));

        if (queryMonthlyFlag) {
            DynamicCostSurplusPropReqF dynamicCostSurplusPropReqF = new DynamicCostSurplusPropReqF()
                    .setMdmId(contractProjectInitiationV.getCommunityId())
                    .setPlanStartTime(contractProjectInitiationV.getPlanStartTime())
                    .setPlanEndTime(contractProjectInitiationV.getPlanEndTime())
                    .setSourceType(1);

            contractProjectInitiationV.getContractPlanList().stream().forEach(projectPlan -> {
                // 查询最新的月度分摊
                dynamicCostSurplusPropReqF.setAllocationAmount(projectPlan.getAllocationAmount())
                        .setCbCode(projectPlan.getCostChargeItemCode());
                projectPlan.setMonthlyAllocationList(
                        contractProjectPlanMonthlyAllocationAppService.getMonthlyAllocationDetail(dynamicCostSurplusPropReqF)
                );
            });
        } else {
            // 查询月度分摊明细
            List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = contractProjectPlanMonthlyAllocationAppService.getByProjectInitiationId(projectInitiationId);
            Map<String, Map<Integer, List<ContractProjectPlanMonthlyAllocationV>>> monthlyAllocationVMap = monthlyAllocationVS.stream()
                    .collect(Collectors.groupingBy(
                            ContractProjectPlanMonthlyAllocationV::getContractProjectPlanId,
                            Collectors.groupingBy(ContractProjectPlanMonthlyAllocationV::getMonthlyAllocationType)
                    ));

            // 查询成本确认明细
            List<ContractProjectPlanCostConfirmV> costConfirmVS = contractProjectPlanCostConfirmAppService.getByProjectInitiationId(projectInitiationId);
            contractProjectInitiationV.setCostConfirmList(costConfirmVS);

            Map<String, List<ContractProjectPlanCostConfirmV>> costConfirmVMap = costConfirmVS.stream()
                    .collect(Collectors.groupingBy(ContractProjectPlanCostConfirmV::getContractProjectPlanId));

            contractProjectInitiationV.getContractPlanList().stream().forEach(projectPlan -> {
                projectPlan.setMonthlyAllocationList(
                        monthlyAllocationVMap
                                .computeIfAbsent(projectPlan.getId(), k -> new HashMap<>())
                                .computeIfAbsent(0, k -> new ArrayList<>())
                        )
                        .setCostConfirmMonthlyAllocationList(
                                monthlyAllocationVMap
                                        .computeIfAbsent(projectPlan.getId(), k -> new HashMap<>())
                                        .computeIfAbsent(1, k -> new ArrayList<>())
                        )
                        .setCostConfirmList(costConfirmVMap.getOrDefault(projectPlan.getId(), new ArrayList<>()));
            });
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(ContractProjectInitiationE contractProjectInitiationE, List<ContractProjectPlanF> planList) {
        String projectInitiationId = contractProjectInitiationE.getId();
        // 先删除原有的合约规划
        this.deleteByProjectInitiationId(projectInitiationId);

        List<ContractProjectPlanE> contractProjectPlanES = planList.stream().map(e -> {
            ContractProjectPlanE planE = Global.mapperFacade.map(e, ContractProjectPlanE.class);
            planE.setProjectInitiationId(projectInitiationId);
            planE.setTenantId(tenantId());
            planE.setId(null);
            return planE;
        }).collect(Collectors.toList());

        // 批量保存新的合约规划
        boolean b = this.saveBatch(contractProjectPlanES);
        contractProjectPlanES.stream().forEach(e -> {
            if (CollUtil.isEmpty(e.getMonthlyAllocationList())) {
                DynamicCostSurplusPropReqF req = new DynamicCostSurplusPropReqF();
                req.setCbCode(e.getCostChargeItemCode())
                        .setMdmId(contractProjectInitiationE.getCommunityId())
                        .setSourceType(1)
                        .setAllocationAmount(e.getAllocationAmount())
                        .setPlanStartTime(contractProjectInitiationE.getPlanStartTime())
                        .setPlanEndTime(contractProjectInitiationE.getPlanEndTime())
                        .setVerificationFlag(true);
                List<ContractProjectPlanMonthlyAllocationV> allocationDetail = contractProjectPlanMonthlyAllocationAppService.getMonthlyAllocationDetail(req);
                e.setMonthlyAllocationList(allocationDetail);
            }
            contractProjectPlanMonthlyAllocationAppService.saveBatch(projectInitiationId, e.getId(), e.getMonthlyAllocationList());
            // contractProjectPlanCostConfirmAppService.saveBatch(e.getCostConfirmList());
        });

        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateForCostConfirm(String projectInitiationId, List<ContractProjectPlanF> planList) {
        List<ContractProjectPlanE> planES = Global.mapperFacade.mapAsList(planList, ContractProjectPlanE.class);

        // 批量更新新的合约规划
        boolean b = this.updateBatchById(planES);

        planList.stream().forEach(e -> {
            contractProjectPlanMonthlyAllocationAppService.saveBatchForCostConfirm(projectInitiationId, e.getId(), e.getCostConfirmMonthlyAllocationList());
            contractProjectPlanCostConfirmAppService.saveBatchForCostConfirm(e.getCostConfirmList());
        });

        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatchForCostConfirm(List<ContractProjectPlanF> contractProjectPlanFList) {
        List<ContractProjectPlanE> entityList = Global.mapperFacade.mapAsList(contractProjectPlanFList, ContractProjectPlanE.class);
        boolean b = this.saveOrUpdateBatch(entityList);
        entityList.stream().forEach(e -> {
            e.getCostConfirmList().stream().forEach(costConfirm -> {
                costConfirm.setContractProjectPlanId(e.getId());
            });
            contractProjectPlanCostConfirmAppService.saveBatchForCostConfirm(e.getCostConfirmList());
        });

        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAllByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanE::getDeleted, 0);

        // 删除关联的合约规划月度分摊
        contractProjectPlanMonthlyAllocationAppService.deleteByProjectInitiationId(projectInitiationId);
        // 删除关联的合约规划成本确认
        contractProjectPlanCostConfirmAppService.deleteByProjectInitiationId(projectInitiationId);
        return this.remove(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ContractProjectPlanE> list(Collection<String> projectInitiationIdList) {
        LambdaQueryWrapper<ContractProjectPlanE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractProjectPlanE::getProjectInitiationId, projectInitiationIdList);
        queryWrapper.eq(ContractProjectPlanE::getDeleted, 0);

        return this.list(queryWrapper);
    }

}