package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractBondPlanF;
import com.wishare.contract.apps.fo.contractset.ContractBondPlanSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondPlanUpdateF;
import com.wishare.contract.apps.fo.contractset.ContractCollectionPlanPageF;
import com.wishare.contract.domains.consts.contractset.ContractCollectionPlanFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractBondPlanE;
import com.wishare.contract.domains.mapper.contractset.ContractBondPlanMapper;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanPageV;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanSumV;
import com.wishare.contract.domains.vo.contractset.ContractBondPlanV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 合同保证金计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Service
@Slf4j
public class ContractBondPlanService extends ServiceImpl<ContractBondPlanMapper, ContractBondPlanE> {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanMapper contractBondPlanMapper;

    @Nonnull
    public List<ContractBondPlanV> listContractBondPlan(ContractBondPlanF contractBondPlanF) {
        LambdaQueryWrapper<ContractBondPlanE> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper
                .eq(ContractBondPlanE::getContractId, contractBondPlanF.getContractId())
                .eq(ContractBondPlanE::getDeleted, Const.State._0);
        return Global.mapperFacade.mapAsList(contractBondPlanMapper.selectList(objectLambdaQueryWrapper), ContractBondPlanV.class);
    }

    public List<Long> contractBondPlanBillIds(Long contractId,
                                              Integer bondType,
                                              Integer paymentStatus,
                                              Integer refundStatus,
                                              Integer bidBond) {

        return contractBondPlanMapper.contractBondPlanBillIds(contractId, bondType, paymentStatus, refundStatus, bidBond);
    }

    public Long saveContractBondPlan(ContractBondPlanSaveF contractBondPlanF) {
        ContractBondPlanE map = Global.mapperFacade.map(contractBondPlanF, ContractBondPlanE.class);
        map.setId(UidHelper.nextId("contract_bond_plan"));
        contractBondPlanMapper.insert(map);
        return map.getId();
    }

    public void updateContractBondPlan(ContractBondPlanUpdateF contractBondPlanF) {
        ContractBondPlanE map = Global.mapperFacade.map(contractBondPlanF, ContractBondPlanE.class);
        contractBondPlanMapper.updateById(map);
    }

    public void removeContractBondPlan(Long id) {
        ContractBondPlanE map = new ContractBondPlanE();
        map.setId(id);
        map.setDeleted(Const.State._1);
        contractBondPlanMapper.updateById(map);
    }

    public void deleteBondPlan(Long contractId) {
        contractBondPlanMapper.deleteBondPlan(contractId);
    }

    public IPage<ContractBondPlanPageV> pageContractBondPlan(PageF<SearchF<?>> form, String tenantId) {
        Page<?> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        pageContractBondPlanCondition(queryModel, tenantId);
        return contractBondPlanMapper.pageContractBondPlan(pageF, queryModel);
    }

    public ContractBondPlanSumV pageContractBondPlanSum(PageF<SearchF<?>> form, String tenantId) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        pageContractBondPlanCondition(queryModel, tenantId);
        return contractBondPlanMapper.pageContractBondPlanSum(queryModel);
    }

    public void pageContractBondPlanCondition(QueryWrapper<?> queryModel, String tenantId) {
        queryModel.eq("cc.tenantId", tenantId);
        queryModel.eq("cc.deleted", Const.State._0);
//        queryModel.eq("cc.reviewStatus", Const.State._1);//审批状态：审批通过
        queryModel.eq("cbp.deleted", Const.State._0);
//        queryModel.in("cc.contractState", Arrays.asList(1, 2, 3, 4));// 合同状态：履行中  已到期  //20221115去除 已终止  20221208 产品又要加 已终止 还要加终止中
//        queryModel.eq("cc.frameworkContract", 0);// 框架合同：否  20221025改为  框架合同，不展示  基于框架合同签订的子合同，展示sql体现
//        queryModel.eq("cc.virtualContract", 0);// 虚拟合同：否
        queryModel.in("cc.signingMethod", Arrays.asList(0, 2));// 签约方式：新签  续签
    }

    public void updateBillId(Long id, Long billId) {
        contractBondPlanMapper.updateBillId(id, billId);
    }
}
