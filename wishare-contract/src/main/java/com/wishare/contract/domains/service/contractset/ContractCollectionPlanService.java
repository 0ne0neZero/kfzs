package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.domains.consts.contractset.ContractCollectionPlanFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.wishare.contract.domains.mapper.contractset.ContractCollectionPlanMapper;
import com.wishare.contract.domains.vo.contractset.CollectionPlanSumV;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanDetailV;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanStatisticsV;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanV;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同收款计划信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Service
@Slf4j
public class ContractCollectionPlanService extends ServiceImpl<ContractCollectionPlanMapper, ContractCollectionPlanE> {

    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionPlanMapper contractCollectionPlanMapper;

    @Nonnull
    public List<ContractCollectionPlanV> listContractCollectionPlan(ContractCollectionPlanF contractCollectionPlanF) {
        LambdaQueryWrapper<ContractCollectionPlanE> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper
                .eq(ContractCollectionPlanE::getContractId, contractCollectionPlanF.getContractId())
                .eq(ContractCollectionPlanE::getDeleted, Const.State._0);
        return Global.mapperFacade.mapAsList(contractCollectionPlanMapper.selectList(objectLambdaQueryWrapper), ContractCollectionPlanV.class);
    }

    public List<ContractCollectionPlanV> collectionPlanList(ContractCollectionPlanF contractCollectionPlanF) {
        LambdaQueryWrapper<ContractCollectionPlanE> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper
                .eq(ContractCollectionPlanE::getContractId, contractCollectionPlanF.getContractId())
                .eq(ContractCollectionPlanE::getPaymentStatus, Const.State._0)
                .eq(ContractCollectionPlanE::getDeleted, Const.State._0);
        return Global.mapperFacade.mapAsList(contractCollectionPlanMapper.selectList(objectLambdaQueryWrapper), ContractCollectionPlanV.class);
    }

    public List<ContractCollectionPlanV> collectionPlanVList (ContractCollectionPlanF contractCollectionPlanF) {
        LambdaQueryWrapper<ContractCollectionPlanE> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper
                .eq(ContractCollectionPlanE::getContractId, contractCollectionPlanF.getContractId())
                .eq(ContractCollectionPlanE::getPaymentStatus, Const.State._0);
        return Global.mapperFacade.mapAsList(contractCollectionPlanMapper.selectList(objectLambdaQueryWrapper), ContractCollectionPlanV.class);
    }

    public Long saveContractCollectionPlan(ContractCollectionPlanSaveF contractCollectionPlanF) {
        ContractCollectionPlanE map = Global.mapperFacade.map(contractCollectionPlanF, ContractCollectionPlanE.class);
        map.setId(UidHelper.nextId(ContractCollectionPlanFieldConst.COLLECTION_PLAN));
        contractCollectionPlanMapper.insert(map);
        return map.getId();
    }

    public void updateContractCollectionPlan(ContractCollectionPlanUpdateF contractCollectionPlanF) {
        ContractCollectionPlanE map = Global.mapperFacade.map(contractCollectionPlanF, ContractCollectionPlanE.class);
        contractCollectionPlanMapper.updateById(map);
    }

    public void removeContractCollectionPlan(Long id) {
        ContractCollectionPlanE map = new ContractCollectionPlanE();
        map.setId(id);
        map.setDeleted(1);
        contractCollectionPlanMapper.updateById(map);
    }

    public void deleteCollectionPlan(Long contractId) {
        contractCollectionPlanMapper.deleteCollectionPlan(contractId);
    }


    public IPage<ContractCollectionPlanDetailV> collectionPlanDetailPage(PageF<SearchF<ContractCollectionPlanPageF>> form,
                                                                         String tenantId) {
        Page<ContractCollectionPlanPageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<ContractCollectionPlanPageF> queryModel = form.getConditions().getQueryModel();
        return contractCollectionPlanMapper.collectionPlanDetailPage(pageF, conditionPage(queryModel, tenantId));
    }

    public CollectionPlanSumV collectionPlanAmountSum(PageF<SearchF<ContractCollectionPlanPageF>> form,
                                                      String tenantId) {
        QueryWrapper<ContractCollectionPlanPageF> queryModel = form.getConditions().getQueryModel();
        return contractCollectionPlanMapper.collectionPlanAmountSum(conditionPage(queryModel, tenantId));
    }

    private QueryWrapper<ContractCollectionPlanPageF> conditionPage(QueryWrapper<ContractCollectionPlanPageF> queryModel, String tenantId) {
        queryModel.eq("cc.deleted", 0);
        queryModel.eq("ccp.deleted", 0);
        queryModel.eq("cc.tenantId", tenantId);
//        queryModel.eq("cc.reviewStatus", 1); //审批状态：审批通过
//        queryModel.in("cc.contractState", Arrays.asList(1 ,2, 3, 4));// 合同状态：履行中  已到期  //20221115去除 已终止  20221208 产品又要加已终止 还要加终止中
        queryModel.eq("cc.frameworkContract", 0);// 框架合同：否  20221025改为  框架合同，不展示  基于框架合同签订的子合同，展示sql体现
//        queryModel.eq("cc.virtualContract", 0);// 虚拟合同：否
        queryModel.in("cc.signingMethod", Arrays.asList(0, 2));// 签约方式：新签  续签
        return queryModel;
    }

    public Integer checkContractByPlan(Long id) {
        return contractCollectionPlanMapper.checkContractByPlan(id);
    }

    public List<ContractCollectionPlanV> collectionExpire(String tenantId, Long contractId,Integer contractNature,Long id,Boolean flag) {
        return contractCollectionPlanMapper.collectionExpire(tenantId,contractId,contractNature,id,flag);
    }

    public List<ContractCollectionPlanV> collectionAdvent(String tenantId, Long contractId,Integer contractNature,Long id,Boolean flag,Integer dayNum) {
        return contractCollectionPlanMapper.collectionAdvent(tenantId,null,contractNature,id,flag,dayNum);
    }

    public void updateWarnState(Long id,Integer warnState) {
        contractCollectionPlanMapper.updateWarnState(id,warnState);
    }

    public Boolean saveOverdueStatement(CollectionPlanOverdueStatementF from) {
        LambdaUpdateWrapper<ContractCollectionPlanE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ContractCollectionPlanE::getOverdueReason, from.getOverdueReason());
        updateWrapper.set(ContractCollectionPlanE::getOverdueStatement, from.getOverdueStatement());
        updateWrapper.set(ContractCollectionPlanE::getOverdueReasonIds, from.getOverdueReasonIds());
        updateWrapper.set(ContractCollectionPlanE::getOverdueReasonTime, LocalDateTime.now());
        updateWrapper.eq(ContractCollectionPlanE::getId, from.getCollectionPlanId());
        return update(updateWrapper);
    }

    private final List<String> DATE_MONTH = new ArrayList<>(Arrays.asList("-01","-02","-03","-04","-05","-06","-07","-08","-09",
            "-10","-11","-12"));

    public List<ContractCollectionPlanStatisticsV> selectCollectionPlanStatistics(Integer contractNature, String year, String tenantId) {
        List<ContractCollectionPlanStatisticsV> result = contractCollectionPlanMapper.selectCollectionPlanStatistics(contractNature, year, tenantId);
        List<String> dateMonthList = new ArrayList<>();
        if (!result.isEmpty()) {
            dateMonthList = result.stream().map(ContractCollectionPlanStatisticsV::getDateMonth).collect(Collectors.toList());
        }
        for (String item : DATE_MONTH) {
            if (!dateMonthList.contains(year.concat(item))) {
                ContractCollectionPlanStatisticsV statisticsV = new ContractCollectionPlanStatisticsV();
                statisticsV.setDateMonth(year.concat(item));
                statisticsV.setLocalCurrencyAmountSum(BigDecimal.ZERO);
                statisticsV.setPaymentAmountSum(BigDecimal.ZERO);
                result.add(statisticsV);
            }
        }
        return result.stream().sorted(Comparator.comparing(ContractCollectionPlanStatisticsV::getDateMonth)).collect(Collectors.toList());
    }
}
