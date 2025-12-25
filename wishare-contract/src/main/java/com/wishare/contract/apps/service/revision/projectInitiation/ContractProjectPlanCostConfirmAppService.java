package com.wishare.contract.apps.service.revision.projectInitiation;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectPlanCostConfirmF;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectPlanCostConfirmE;
import com.wishare.contract.domains.mapper.revision.projectInitiation.ContractProjectPlanCostConfirmMapper;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanCostConfirmV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合约规划成本确认Service实现类
 */
@Service
@AllArgsConstructor
public class ContractProjectPlanCostConfirmAppService extends ServiceImpl<ContractProjectPlanCostConfirmMapper, ContractProjectPlanCostConfirmE> implements IOwlApiBase {

    public List<ContractProjectPlanCostConfirmV> getByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanCostConfirmE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getDeleted, 0);
        List<ContractProjectPlanCostConfirmE> costConfirmES = this.list(queryWrapper);
        return Global.mapperFacade.mapAsList(costConfirmES, ContractProjectPlanCostConfirmV.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchForCostConfirm(List<ContractProjectPlanCostConfirmF> costConfirmList) {
        if (CollUtil.isEmpty(costConfirmList)) {
            return false;
        }
        List<ContractProjectPlanCostConfirmE> costConfirmEList = Global.mapperFacade.mapAsList(costConfirmList, ContractProjectPlanCostConfirmE.class);
        // 批量保存新的合约规划成本确认
        return this.saveBatch(costConfirmEList);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByContractProjectPlanId(String contractProjectPlanId) {
        LambdaQueryWrapper<ContractProjectPlanCostConfirmE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getContractProjectPlanId, contractProjectPlanId);
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    /**
     * bpm审批回调更新成本占用
     *
     * @param projectInitiationId
     * @param bpmStatusEnum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(String projectInitiationId, Integer bpmStatusEnum) {
        LambdaQueryWrapper<ContractProjectPlanCostConfirmE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getDeleted, 0);
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getBpmReviewStatus, 1);

        // 查询所有关联的合约规划成本确认
        List<ContractProjectPlanCostConfirmE> costConfirmList = this.list(queryWrapper);

        for (ContractProjectPlanCostConfirmE costConfirm : costConfirmList) {
            costConfirm.setBpmReviewStatus(bpmStatusEnum);
        }

        return this.updateBatchById(costConfirmList);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanCostConfirmE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanCostConfirmE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

}