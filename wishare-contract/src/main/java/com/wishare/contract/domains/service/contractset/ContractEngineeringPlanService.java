package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanF;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanSaveF;
import com.wishare.contract.domains.consts.contractset.ContractEngineeringPlanFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.entity.contractset.ContractEngineeringPlanE;
import com.wishare.contract.domains.mapper.contractset.ContractEngineeringPlanMapper;
import com.wishare.contract.domains.vo.contractset.ContractEngineeringPlanV;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 工程类合同计提信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@Service
@Slf4j
public class ContractEngineeringPlanService extends ServiceImpl<ContractEngineeringPlanMapper, ContractEngineeringPlanE> {

    @Setter(onMethod_ = {@Autowired})
    private ContractEngineeringPlanMapper contractEngineeringPlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;

    /**
     * 根据请求参数获取指定对象
     *
     * @param contractEngineeringPlanF 查询条件
     * @return option 如果查询不到，上层可自定义处理
     */
    @Nonnull
    public Optional<ContractEngineeringPlanV> get(ContractEngineeringPlanF contractEngineeringPlanF) {
        QueryWrapper<ContractEngineeringPlanE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(contractEngineeringPlanF.getId())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.ID, contractEngineeringPlanF.getId());
        }
        if (Objects.nonNull(contractEngineeringPlanF.getContractId())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.CONTRACT_ID, contractEngineeringPlanF.getContractId());
        }
        if (Objects.nonNull(contractEngineeringPlanF.getAccrualCode())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.ACCRUAL_CODE, contractEngineeringPlanF.getAccrualCode());
        }
        queryWrapper.eq(ContractEngineeringPlanFieldConst.DELETED, 0);
        ContractEngineeringPlanE contractEngineeringPlanE = contractEngineeringPlanMapper.selectOne(queryWrapper);
        if (contractEngineeringPlanE != null) {
            return Optional.of(Global.mapperFacade.map(contractEngineeringPlanE, ContractEngineeringPlanV.class));
        } else {
            return Optional.empty();
        }
    }

    /**
     * 列表接口
     *
     * @param contractEngineeringPlanF 根据Id更新
     * @return 下拉列表
     */
    @Nonnull
    public List<ContractEngineeringPlanV> listEngineering(ContractEngineeringPlanF contractEngineeringPlanF) {
        QueryWrapper<ContractEngineeringPlanE> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(contractEngineeringPlanF.getId())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.ID, contractEngineeringPlanF.getId());
        }
        if (Objects.nonNull(contractEngineeringPlanF.getContractId())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.CONTRACT_ID, contractEngineeringPlanF.getContractId());
        }
        if (Objects.nonNull(contractEngineeringPlanF.getAccrualCode())) {
            queryWrapper.eq(ContractEngineeringPlanFieldConst.ACCRUAL_CODE, contractEngineeringPlanF.getAccrualCode());
        }
        queryWrapper.eq(ContractEngineeringPlanFieldConst.DELETED, 0);
        queryWrapper.orderByDesc(ContractEngineeringPlanFieldConst.GMT_CREATE);
        return Global.mapperFacade.mapAsList(contractEngineeringPlanMapper.selectList(queryWrapper), ContractEngineeringPlanV.class);
    }

    public Long save(ContractEngineeringPlanSaveF contractEngineeringPlanF) {
        ContractEngineeringPlanE map = Global.mapperFacade.map(contractEngineeringPlanF, ContractEngineeringPlanE.class);
        Long id = UidHelper.nextId(ContractEngineeringPlanFieldConst.CONTRACT_ENGINEERING);
        //上传计提资料
        if (!CollectionUtils.isEmpty(contractEngineeringPlanF.getAccrualDataFileVo()) && contractEngineeringPlanF.getAccrualDataFileVo().size() > 0) {
            map.setAccrualData(fileStorageUtils.batchSubmitFile(
                    contractEngineeringPlanF.getAccrualDataFileVo(),null, id, contractEngineeringPlanF.getTenantId()));
            map.setAccrualDataName(fileStorageUtils.batchSubmitName(contractEngineeringPlanF.getAccrualDataFileVo()));
        }
        map.setId(id);
        contractEngineeringPlanMapper.insert(map);
        return map.getId();
    }

    public Integer selectContractCount(String tenantId) {
        return contractEngineeringPlanMapper.selectContractCount(tenantId);
    }
}
