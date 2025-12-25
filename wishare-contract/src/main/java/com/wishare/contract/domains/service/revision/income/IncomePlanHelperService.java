package com.wishare.contract.domains.service.revision.income;

import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanAddF;
import com.wishare.contract.apps.fo.revision.income.IncomePlanAddF;
import com.wishare.contract.domains.dto.settlementPlan.SettlementPlanResult;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.owl.exception.OwlBizException;
import io.seata.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Service
public class IncomePlanHelperService {

    public void paramValidate(IncomePlanAddF req, ContractIncomeConcludeE concludeE) {
        List<List<ContractIncomePlanAddF>> addFs = req.getContractPayPlanAddFLists();
        BigDecimal contractAmountOriginalRate = concludeE.getContractAmountOriginalRate();
        BigDecimal changContractAmount = concludeE.getChangContractAmount();
        BigDecimal plannedCollectionAmountSum = addFs.stream().flatMap(List::stream)
                .map(ContractIncomePlanAddF::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        boolean isEqual;
        BigDecimal zeroAmount = new BigDecimal("0.00");
        if (changContractAmount != null && !changContractAmount.equals(zeroAmount)) {
            isEqual = plannedCollectionAmountSum.compareTo(changContractAmount) == 0;
        } else {
            isEqual = plannedCollectionAmountSum.compareTo(contractAmountOriginalRate) == 0;
        }
        if (!isEqual) {
            throw new OwlBizException("成本预估金额必须等于合同履约金额或变更后金额");
        }
        addFs.forEach(addF->{
            addF.forEach(add->{
                if (StringUtils.isBlank(add.getSettlePlanGroup())) {
                    throw new OwlBizException("费项:" + add.getChargeItem() + "期数:" + add.getTermDate() + "->对应的分组号为空");
                }
                if (StringUtils.isBlank(add.getContractPayFundId())) {
                    throw new OwlBizException("费项:" + add.getChargeItem() + "期数:" + add.getTermDate() + "->合同清单id不能为空");
                }
            });
        });

    }

    public void editParamValidateAndFillData(IncomePlanAddF req, ContractIncomeConcludeE concludeE, List<ContractIncomePlanConcludeE> notOneTimeContractPayPlanList,boolean removeAll) {

        if (5 != req.getSplitMode()  && !removeAll) {
            if (CollectionUtils.isEmpty(notOneTimeContractPayPlanList)) {
                throw new OwlBizException("非法数据,请检查");
            }
/*            Optional<ContractIncomePlanAddF> op = req.getContractPayPlanAddFLists().stream().flatMap(List::stream).filter(c->StringUtils.isBlank(c.getId())).findAny();
            if (op.isPresent()) {
                settlementPlanResult.setSuccess(false);
                settlementPlanResult.setErrorMessage(List.of("计划id不能为空"));
                return;
            }

            List<String> existPlanList = req.getContractPayPlanAddFLists().stream().flatMap(List::stream).map(ContractIncomePlanAddF::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            if (existPlanList.size() != notOneTimeContractPayPlanList.size()) {
                settlementPlanResult.setSuccess(false);
                settlementPlanResult.setErrorMessage(List.of("结算计划只能修改不能删除和新增"));
                return;
            }*/
        }
        paramValidate(req,concludeE);
        if (5 == req.getSplitMode()  && !removeAll) {
            req.getContractPayPlanAddFLists().forEach(list->{
                for (int i = 0; i < list.size(); i++) {
                    ContractIncomePlanAddF item = list.get(i);
                    item.setTermDate(i + 1);
                }
            });
        }

    }


    public void fillGroupCodeAndTerm(List<List<ContractIncomePlanAddF>> addFs) {
        addFs.forEach(list->{
            for (int i = 0; i < list.size(); i++) {
                ContractIncomePlanAddF item = list.get(i);
                item.setTermDate(i + 1);
            }
        });
    }
}
