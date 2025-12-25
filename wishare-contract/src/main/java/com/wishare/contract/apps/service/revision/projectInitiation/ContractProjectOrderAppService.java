package com.wishare.contract.apps.service.revision.projectInitiation;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectOrderF;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectOrderE;
import com.wishare.contract.domains.enums.revision.ContractProjectOrderPlatformEnum;
import com.wishare.contract.domains.mapper.revision.projectInitiation.ContractProjectOrderMapper;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectOrderInfoV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectOrderV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.StartOrderForJDReqF;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 立项审批关联订单Service实现类
 */
@Service
public class ContractProjectOrderAppService extends ServiceImpl<ContractProjectOrderMapper, ContractProjectOrderE> implements IOwlApiBase {

    public List<ContractProjectOrderV> getByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectOrderE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectOrderE::getProjectInitiationId, projectInitiationId);
        queryWrapper.ne(ContractProjectOrderE::getBpmReviewStatus, -1);
        queryWrapper.eq(ContractProjectOrderE::getDeleted, 0);
        List<ContractProjectOrderE> list = this.list(queryWrapper);
        return list.stream().map(e -> {
            ContractProjectOrderV orderV = Global.mapperFacade.map(e, ContractProjectOrderV.class);
            orderV.setGoodsInfoList(JSONObject.parseArray(e.getGoodsInfo(), ContractProjectOrderInfoV.class));
            return orderV;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(String projectInitiationId, List<ContractProjectOrderF> planList) {
        List<ContractProjectOrderE> contractProjectOrderES = planList.stream().map(e -> {
            ContractProjectOrderE orderE = Global.mapperFacade.map(e, ContractProjectOrderE.class);
            orderE.setProjectInitiationId(projectInitiationId);
            orderE.setTenantId(tenantId());
            orderE.setId(null);
            return orderE;
        }).collect(Collectors.toList());

        // 批量保存新的立项审批关联订单
        return this.saveBatch(contractProjectOrderES);
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveForJDHuiCai(StartOrderForJDReqF req) {
        ContractProjectOrderE contractProjectOrderE = new ContractProjectOrderE()
                .setProjectInitiationId(req.getId())
                .setPlatform(ContractProjectOrderPlatformEnum.京东慧采.getCode())
                .setJdHuiCaiUserName(req.getUserName())
                .setJdHuiCaiPwdMd5(req.getPwdMd5())
                .setTenantId(tenantId());
        boolean save = this.save(contractProjectOrderE);
        return contractProjectOrderE.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectOrderE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectOrderE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectOrderE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        LambdaQueryWrapper<ContractProjectOrderE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectOrderE::getId, id);
        queryWrapper.eq(ContractProjectOrderE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

}