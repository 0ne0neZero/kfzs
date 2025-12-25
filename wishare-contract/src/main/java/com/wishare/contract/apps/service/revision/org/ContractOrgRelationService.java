package com.wishare.contract.apps.service.revision.org;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.remote.OrgRelationF;
import com.wishare.contract.apps.remote.clients.revision.RevisionFeignClient;
import com.wishare.contract.apps.remote.fo.org.CustomerMutualF;
import com.wishare.contract.apps.remote.fo.org.SupplierMutualF;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.enums.revision.org.CoopStatusEnum;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.ContractMiniOrgV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/4  19:57
 */
@Service
@Slf4j
public class ContractOrgRelationService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionFeignClient revisionFeignClient;

    /**
     * 传入供应商ID，返回合作状态&合同数量 并更新org中的关联数据
     * @param id 供应商ID
     * @return SupplierMutualF
     */
    public SupplierMutualF mutualSupplierLasted(String id) {
        return null;
    }

    /**
     * 传入客户ID，返回合作状态&合同数量 并更新org中的关联数据
     * @param id 客户ID
     * @return CustomerMutualF
     */
    public CustomerMutualF mutualCustomerLasted(String id) {
        return null;
    }

    /**
     * 将所关联的合同的状态更新到最新状态
     */
    public void updateStatusLastedS(String supplierId) {

    }

    /**
     * 将所关联的合同的状态更新到最新状态
     */
    public void updateStatusLastedC(String customerId) {

    }

    public Boolean mutualForPay(ContractPayConcludeE concludeE) {
        String one = concludeE.getOppositeOneId();
        String two = concludeE.getOppositeTwoId();

        if (StringUtils.isNotBlank(one)) {
            mutualSupplierLasted(one);
        }
        if (StringUtils.isNotBlank(two)) {
            mutualSupplierLasted(two);
        }

        return true;
    }

    public Boolean mutualForIncome(ContractIncomeConcludeE concludeE) {
        String one = concludeE.getOppositeOneId();
        String two = concludeE.getOppositeTwoId();

        if (StringUtils.isNotBlank(one)) {
            mutualCustomerLasted(one);
        }
        if (StringUtils.isNotBlank(two)) {
            mutualCustomerLasted(two);
        }

        return true;
    }

    public PageV<ContractMiniOrgV> pageBySupplierId(@RequestBody PageF<SearchF<OrgRelationF>> request) {

        if (Objects.isNull(request.getConditions()) || CollectionUtils.isEmpty(request.getConditions().getFields())) {
            return null;
        }

        String id = request.getConditions().getFields().stream().filter(field -> "id".equals(field.getName())).findFirst().map(field -> (String) field.getValue()).orElse("");

        //-- 结果集

        //-- 支出合同  对方单位 1&2 为供应商
        QueryWrapper<ContractPayConcludeE> queryWrapperPay = new QueryWrapper<>();
        queryWrapperPay.and(i -> i.eq(ContractPayConcludeE.OPPOSITE_ONE_ID, id)
                        .or(m -> m.eq(ContractPayConcludeE.OPPOSITE_TWO_ID, id)))
                .orderByDesc(ContractPayConcludeE.GMT_CREATE);

        List<ContractPayConcludeE> payList = contractPayConcludeService.list(queryWrapperPay);

        List<ContractMiniOrgV> list = new ArrayList<>(Global.mapperFacade.mapAsList(payList, ContractMiniOrgV.class));
        for (ContractMiniOrgV orgV : list) {
            orgV.setIsPay(true);
        }

        dealAmountForPageById(list);

        return pageByHandel(request, list);
    }

    public PageV<ContractMiniOrgV> pageByCustomerId(@RequestBody PageF<SearchF<OrgRelationF>> request) {

        if (Objects.isNull(request.getConditions()) || CollectionUtils.isEmpty(request.getConditions().getFields())) {
            return null;
        }

        String id = request.getConditions().getFields().stream().filter(field -> "id".equals(field.getName())).findFirst().map(field -> (String) field.getValue()).orElse("");

        //-- 结果集
        List<ContractMiniOrgV> list = new ArrayList<>();

        //-- 收入合同  对方单位1 & 2 为客户
        QueryWrapper<ContractIncomeConcludeE> queryWrapperIncome = new QueryWrapper<>();
        queryWrapperIncome.and(i -> i.eq(ContractIncomeConcludeE.OPPOSITE_ONE_ID, id)
                        .or(m -> m.eq(ContractIncomeConcludeE.OPPOSITE_TWO_ID, id)))
                .orderByDesc(ContractIncomeConcludeE.GMT_CREATE);
        List<ContractIncomeConcludeE> incomeList = contractIncomeConcludeService.list(queryWrapperIncome);

        for (ContractMiniOrgV orgV : list) {
            orgV.setIsPay(true);
        }
        list.addAll(Global.mapperFacade.mapAsList(incomeList, ContractMiniOrgV.class));

        dealAmountForPageById(list);

        return pageByHandel(request, list);
    }

    public void dealAmountForPageById(List<ContractMiniOrgV> list) {
        for (ContractMiniOrgV map : list) {
            if (Objects.nonNull(map.getIsPay()) && map.getIsPay()) {
                map.setUnPay(map.getContractAmountOriginalRate().subtract(Objects.isNull(map.getPayAmount()) ? BigDecimal.ZERO : map.getPayAmount()));
            } else {
                map.setIsPay(false);
                map.setUnCollect(map.getContractAmountOriginalRate().subtract(Objects.isNull(map.getCollectAmount()) ? BigDecimal.ZERO : map.getCollectAmount()));
            }
            map.setContractAmount(map.getContractAmountOriginalRate());
        }
    }

    public PageV<ContractMiniOrgV> pageByHandel(PageF<SearchF<OrgRelationF>> request, List<ContractMiniOrgV> result){
        PageV<ContractMiniOrgV> pageResult = new PageV<>();
        //-- 进行手动分页
        long pageNum = request.getPageNum();
        long pageSize = request.getPageSize();
        //计算总页数
        long page = result.size() % pageSize == 0 ? result.size() / pageSize : result.size() / pageSize + 1;
        //兼容性分页参数错误
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageNum = Math.min(pageNum, page);
        // 开始索引
        long begin = 0L;
        // 结束索引
        long end = 0L;
        if (pageNum != page) {
            begin = (pageNum - 1) * pageSize;
            end = begin + pageSize;
        } else {
            begin = (pageNum - 1) * pageSize;
            end = Long.parseLong(result.size() + "");
        }
        pageResult.setPageNum(pageNum)
                .setPageSize(pageSize)
                .setLast(true)
                .setRecords(result.size() == 0 ? result : result.subList((int) begin, (int) end))
                .setTotal(result.size());
        return pageResult;
    }

    /**
     * 将数据库中一些关键金额字段为空的数据初始化金额字段为0
     * @return 数量
     */
    public Integer fixAmountNullData() {

        Integer num = 0;

        List<ContractIncomeConcludeE> incomeConcludeES = contractIncomeConcludeService.list();
        for (ContractIncomeConcludeE concludeE : incomeConcludeES) {
            Boolean action = false;
            if (Objects.isNull(concludeE.getCollectAmount())) {
                concludeE.setCollectAmount(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getInvoiceAmount())) {
                concludeE.setInvoiceAmount(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getContractAmountOriginal())) {
                concludeE.setContractAmountOriginal(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getContractAmountLocal())) {
                concludeE.setContractAmountLocal(BigDecimal.ZERO);
                action = true;
            }

            if (action) {
                contractIncomeConcludeService.updateById(concludeE);
                num = num + 1;
            }
        }

        List<ContractPayConcludeE> payConcludeES = contractPayConcludeService.list();
        for (ContractPayConcludeE concludeE : payConcludeES) {
            Boolean action = false;
            if (Objects.isNull(concludeE.getPayAmount())) {
                concludeE.setPayAmount(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getInvoiceAmount())) {
                concludeE.setInvoiceAmount(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getContractAmountOriginal())) {
                concludeE.setContractAmountOriginal(BigDecimal.ZERO);
                action = true;
            }

            if (Objects.isNull(concludeE.getContractAmountLocal())) {
                concludeE.setContractAmountLocal(BigDecimal.ZERO);
                action = true;
            }

            if (action) {
                contractPayConcludeService.updateById(concludeE);
                num = num + 1;
            }
        }

        return num;
    }

}
