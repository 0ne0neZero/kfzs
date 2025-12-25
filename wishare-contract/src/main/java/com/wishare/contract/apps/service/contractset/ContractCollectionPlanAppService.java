package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.action.DefaultAction;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.content.UrlLinkDataItem;
import com.wishare.bizlog.entity.BizObject;
import com.wishare.bizlog.operator.Operator;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.finance.facade.FinanceFacade;
import com.wishare.contract.apps.remote.fo.AddBillSettleRf;
import com.wishare.contract.apps.remote.fo.AddGatherBillDetailF;
import com.wishare.contract.apps.remote.fo.AddGatherBillF;
import com.wishare.contract.apps.remote.fo.DeleteReceivableBillRf;
import com.wishare.contract.apps.remote.fo.finance.InvoiceBatchBlueDetailRf;
import com.wishare.contract.apps.remote.fo.finance.InvoiceBatchBlueRf;
import com.wishare.contract.apps.remote.vo.*;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.consts.ErrMsgEnum;
import com.wishare.contract.domains.consts.contractset.ContractCollectionDetailFieldConst;
import com.wishare.contract.domains.consts.contractset.ContractCollectionPlanFieldConst;
import com.wishare.contract.domains.consts.contractset.ContractConcludeFieldConst;
import com.wishare.contract.domains.entity.contractset.*;
import com.wishare.contract.domains.enums.BusinessTypeEnum;
import com.wishare.contract.domains.enums.LogActionEnum;
import com.wishare.contract.domains.service.contractset.ContractCollectionPlanService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.service.contractset.ContractProfitLossPlanService;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
public class ContractCollectionPlanAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionPlanService contractCollectionPlanService;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanAppService profitLossPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionDetailAppService contractCollectionDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceDetailAppService contractInvoiceDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractPaymentDetailAppService contractPaymentDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossBillAppService contractProfitLossBillAppService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanService contractProfitLossPlanService;
    @Setter(onMethod_ = {@Autowired})
    private CollectionPlanDerateDetailAppService collectionPlanDerateDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractReceiveInvoiceDetailAppService contractReceiveInvoiceDetailAppService;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFacade financeFacade;

    @Setter(onMethod_ = {@Autowired})
    private OrgEnhanceComponent orgEnhanceComponent;

    public List<ContractCollectionPlanV> listContractCollectionPlanSimple(ContractCollectionPlanF contractCollectionPlanF) {
        return contractCollectionPlanService.listContractCollectionPlan(contractCollectionPlanF);
    }

    public List<ContractCollectionPlanV> listContractCollectionPlan(ContractCollectionPlanF contractCollectionPlanF) {
        List<ContractCollectionPlanV> collectionPlanVList = contractCollectionPlanService.listContractCollectionPlan(contractCollectionPlanF);
        if (CollectionUtils.isEmpty(collectionPlanVList)) {
            return null;
        }
        for (ContractCollectionPlanV contractCollectionPlanV : collectionPlanVList) {
            //费项
            if (null != financeFeignClient.chargeName(contractCollectionPlanV.getChargeItemId())) {
                contractCollectionPlanV.setChargeItemName(financeFeignClient.chargeName(contractCollectionPlanV.getChargeItemId()));
            }
            //成本中心
            if (null != orgFeignClient.getByFinanceId(contractCollectionPlanV.getCostId())) {
                contractCollectionPlanV.setCostName(orgFeignClient.getByFinanceId(contractCollectionPlanV.getCostId()).getNameCn());
            }
            //责任部门
            if (null != orgFeignClient.getByOrgId(contractCollectionPlanV.getOrgId())) {
                contractCollectionPlanV.setOrgName(orgFeignClient.getByOrgId(contractCollectionPlanV.getOrgId()).getOrgName());
            }
            String taxRateId = contractCollectionPlanV.getTaxRateIdPath();
            if (Strings.isNotBlank(taxRateId)) {
                contractCollectionPlanV.setTaxRateIdList(Arrays.stream(taxRateId.split(",")).map(Long::valueOf).collect(Collectors.toList()));
            }
        }
        return collectionPlanVList;
    }


    public List<ContractCollectionPlanV> collectionPlanList(ContractCollectionPlanF contractCollectionPlanF) {
        return contractCollectionPlanService.collectionPlanList(contractCollectionPlanF);
    }

    public List<ContractCollectionPlanV> collectionPlanVList(ContractCollectionPlanF contractCollectionPlanF) {
        return contractCollectionPlanService.collectionPlanVList(contractCollectionPlanF);
    }

    public Long collectionPlanSave(ContractCollectionPlanSaveF contractCollectionPlanF) {
        Long id = contractCollectionPlanService.saveContractCollectionPlan(contractCollectionPlanF);
        updateWarnState(contractCollectionPlanF.getContractId(),null,id,null);
        return id;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void adjustPlan(List<ContractCollectionPlanSaveF> contractCollectionPlanF) throws ParseException {
        Long contractId=null;
        Integer contractNature=null;
        //新增收款计划生成对应的损益计划
        for (ContractCollectionPlanSaveF planSaveF : contractCollectionPlanF) {
            if (planSaveF.getContractId() == null) {
                throw BizException.throw402("合同id不存在！");
            }
            ContractDetailsV contractDetailsV = contractConcludeService.selectById(planSaveF.getContractId());
            if(contractDetailsV == null){
                throw BizException.throw400("合同信息不存在！");
            }
            if(contractDetailsV.getSigningMethod() != null && ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                    ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())){
                throw BizException.throw402(ErrMsgEnum.COLLECTION_PLAN.getErrMsg());
            }
            contractId = planSaveF.getContractId();
            contractNature = contractDetailsV.getContractNature();
            planSaveF.setContractNature(contractDetailsV.getContractNature());
            //不含税金额=含税金额/（1+税率）保留两位小数
            BigDecimal taxAmount = BigDecimal.ZERO;
            if (Objects.nonNull(planSaveF.getTaxRate())) {
                taxAmount = planSaveF.getTaxRate().multiply(new BigDecimal("0.01")).add(new BigDecimal(1));
            }
            if (contractDetailsV.getContractNature().equals(ContractSetConst.INCOME)) {
                //删除财务财务中台收款计划账单
                receivableDelete(contractId,false);
            }
            if (planSaveF.getId() == null) {
                //新增收付款计划
                Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                //生成损益计划（工程计提合同不生成）
                if (contractDetailsV.getIsNotProfit() != null && contractDetailsV.getIsNotProfit().equals(1)) {
                    profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                }
                updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
            }
            //编辑收付款计划，只编辑未收款的
            if (planSaveF.getId() != null && planSaveF.getPaymentStatus().equals(ContractSetConst.NOT_COLLECTED)) {
                //编辑收付款计划，只编辑未收款的
                contractCollectionPlanService.removeContractCollectionPlan(planSaveF.getId());
                //新增收付款计划
                planSaveF.setId(null);
                Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                //生成损益计划（工程计提合同不生成）
                if (contractDetailsV.getIsNotProfit() != null && contractDetailsV.getIsNotProfit().equals(1)) {
                    profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                }
                updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
            }
        }
        //合并损益计划（相同月份，相同费项，相同成本中心）1215版本合并
        List<ContractProfitLossPlanV> profitLossPlanG = profitLossPlanAppService.selectByContract(contractId);
        if(CollectionUtils.isEmpty(profitLossPlanG)){
            profitLossPlanAppService.deletePlan(contractId);
            for (ContractProfitLossPlanV planV : profitLossPlanG) {
                ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(planV, ContractProfitLossPlanSaveF.class);
                contractProfitLossPlanF.setContractId(contractId);
                contractProfitLossPlanF.setContractNature(contractNature);
                contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
            }
            if (contractNature.equals(ContractSetConst.INCOME)) {
                //新增应收账单
                profitLossPlanAppService.profitLossPlanCreateBill(null, contractId,null,true,profitLossPlanG);
            }
        }
    }

    /**
     * 删除财务财务中台收款计划账单
     */
    @Transactional(rollbackFor = {Exception.class})
    public void receivableDelete(Long contractId,Boolean flag) {
        if(flag){//针对支付合同，//查询损益计划
            ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
            profitLossPlanF.setContractId(contractId);
            profitLossPlanF.setDeleted(0);
            profitLossPlanF.setSetPaymentStatus(Const.State._0);
            List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
            // 根据损益id集查账单id集
            if (!CollectionUtils.isEmpty(profitLossPlanVS)) {
                List<Long> billIds = profitLossPlanVS.stream().map(ContractProfitLossPlanV::getBillId).collect(Collectors.toList());
                if (!billIds.isEmpty()) {
                    DeleteReceivableBillRf deleteReceivableBillRf = new DeleteReceivableBillRf();
                    deleteReceivableBillRf.setBillIds(billIds);
                    //-- TODO supUnitId default
                    deleteReceivableBillRf.setSupCpUnitId("default");
                    ReceivableDeleteRv deleteRv = financeFeignClient.payabledelete(deleteReceivableBillRf);
//                if (deleteRv.getFailCount() > Const.State._0) {
//                    throw BizException.throw400(ErrMsgEnum.DELETE_BILLS.getErrMsg());
//                }
                    log.info("支付计划-中台账单删除成功数量：" + deleteRv.getSuccessCount());
                }
            }
        }else {
            //针对收入合同，查询损益计划
            ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
            profitLossPlanF.setContractId(contractId);
            profitLossPlanF.setDeleted(0);
            profitLossPlanF.setSetPaymentStatus(Const.State._0);
            List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
            // 根据损益id集查账单id集
            if (!CollectionUtils.isEmpty(profitLossPlanVS)) {
                List<Long> billIds = profitLossPlanVS.stream().map(ContractProfitLossPlanV::getBillId).collect(Collectors.toList());
                if (!billIds.isEmpty()) {
                    DeleteReceivableBillRf deleteReceivableBillRf = new DeleteReceivableBillRf();
                    deleteReceivableBillRf.setBillIds(billIds);
                    //-- TODO supUnitId default
                    deleteReceivableBillRf.setSupCpUnitId("default");
                    ReceivableDeleteRv deleteRv = financeFeignClient.receivabledelete(deleteReceivableBillRf);
//                if (deleteRv.getFailCount() > Const.State._0) {
//                    throw BizException.throw400(ErrMsgEnum.DELETE_BILLS.getErrMsg());
//                }
                    log.info("收款计划-中台账单删除成功数量：" + deleteRv.getSuccessCount());
                }
            }
        }
    }

    /**
     * 删除财务财务中台收款计划账单
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void receivableDelete1(Long collectionId,Boolean flag) {
        //查询损益计划
        ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
        profitLossPlanF.setCollectionId(collectionId);
        profitLossPlanF.setSetPaymentStatus(Const.State._0);
        profitLossPlanF.setDeleted(0);
        List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
        // 根据损益id集查账单id集
        if (!CollectionUtils.isEmpty(profitLossPlanVS)) {
            List<Long> profitLossPlanIds = profitLossPlanVS.stream().map(ContractProfitLossPlanV::getId).collect(Collectors.toList());
            // 账单查询参数
            PageF<SearchF<Field>> receivableSearchF = new PageF<>();
            receivableSearchF.setPageNum(1);
            receivableSearchF.setPageSize(profitLossPlanIds.size());
            SearchF<Field> searchF = new SearchF<>();
            searchF.setFields(fields(profitLossPlanIds));
            receivableSearchF.setConditions(searchF);
            PageV<ReceivablePageRv> receivablePageRvPageV = financeFeignClient.receivablePage(receivableSearchF);
            if (!receivablePageRvPageV.getRecords().isEmpty()) {
                List<Long> billIds = receivablePageRvPageV.getRecords().stream().map(ReceivablePageRv::getId).collect(Collectors.toList());
                DeleteReceivableBillRf deleteReceivableBillRf = new DeleteReceivableBillRf();
                deleteReceivableBillRf.setBillIds(billIds);
                //-- TODO supUnitId default
                deleteReceivableBillRf.setSupCpUnitId("default");
                ReceivableDeleteRv deleteRv = financeFeignClient.receivabledelete(deleteReceivableBillRf);
                if (deleteRv.getFailCount() > Const.State._0) {
                    throw BizException.throw400(ErrMsgEnum.DELETE_BILLS.getErrMsg());
                }
                log.info("收款计划-中台账单删除成功数量：" + deleteRv.getSuccessCount());
            }
        }
    }

    public List<Field> fields(List<Long> profitLossPlanIds) {
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setMethod(15);
        field.setName("ext_field1");
        field.setValue(profitLossPlanIds);
        fields.add(field);
        Field field1 = new Field();
        field1.setMethod(1);
        field1.setName("deleted");
        field1.setValue(Const.State._0);
        fields.add(field1);
        Field field3 = new Field();
        field3.setMethod(1);
        field3.setName("app_id");
        field3.setValue(ContractSetConst.CONTRACTAPPID);
        fields.add(field3);

        //-- TODO 字段赋值
        Field field4 = new Field();
        field4.setName("supUnitId");
        field4.setValue("default");
        field4.setMethod(15);
        fields.add(field4);

        return fields;
    }

    /**
     * 更新收付款计划
     */
    @Transactional(rollbackFor = {Exception.class})
    public List<ContractProfitLossPlanV> save(ContractPlanSaveF contractCollectionPlanF){
        List<ContractProfitLossPlanV> profitLossPlanVS = new ArrayList<>();
        //计算本币金额含税+本币金额不含税的总数，更新到合同信息里面
        BigDecimal localCurrencyAmount = new BigDecimal("0.00");
        BigDecimal taxExcludedAmount = new BigDecimal("0.00");
        Long contractId=null;
        Integer contractNature=null;
        //新增收款计划生成对应的损益计划
        contractCollectionPlanService.deleteCollectionPlan(contractCollectionPlanF.getContractId());
        profitLossPlanAppService.deletePlan(contractCollectionPlanF.getContractId());
        for (ContractCollectionPlanSaveF planSaveF : contractCollectionPlanF.getContractCollectionPlanF()) {
            contractId = planSaveF.getContractId();
            localCurrencyAmount = localCurrencyAmount.add(planSaveF.getLocalCurrencyAmount());
            //不含税金额=含税金额/（1+税率）保留两位小数
            BigDecimal taxAmount = BigDecimal.ZERO;
            if (Objects.nonNull(planSaveF.getTaxRate())) {
                taxAmount = planSaveF.getTaxRate().multiply(new BigDecimal("0.01")).add(new BigDecimal(1));
            }
            taxExcludedAmount = taxExcludedAmount.add(planSaveF.getTaxExcludedAmount());
            if (planSaveF.getContractId() == null) {
                throw BizException.throw402("合同id不存在！");
            }
            ContractDetailsV contractDetailsV = contractConcludeService.selectById(planSaveF.getContractId());
            if (contractDetailsV != null) {
                planSaveF.setContractId(contractDetailsV.getId());
                contractNature = contractDetailsV.getContractNature();
                if (planSaveF.getId() == null) {
                    planSaveF.setContractId(contractId);
                    planSaveF.setContractNature(contractDetailsV.getContractNature());
                    planSaveF.setTenantId(contractDetailsV.getTenantId());

                    //成本中心
                    if (null != orgFeignClient.getByFinanceId(planSaveF.getCostId())) {
                        planSaveF.setCostName(orgFeignClient.getByFinanceId(planSaveF.getCostId()).getNameCn());
                    }
                    //责任部门
                    if (null != orgFeignClient.getByOrgId(planSaveF.getOrgId())) {
                        planSaveF.setOrgName(orgFeignClient.getByOrgId(planSaveF.getOrgId()).getOrgName());
                    }

                    //新增收付款计划
                    Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                    if (contractCollectionPlanF.getIsNotProfit() != null && contractCollectionPlanF.getIsNotProfit().equals(1)) {
                        //生成损益计划(工程计提合同不生成)
                        profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                    }
                }
                //编辑收付款计划，只编辑未收款的
                if (planSaveF.getId() != null && planSaveF.getPaymentStatus().equals(ContractSetConst.NOT_COLLECTED)) {
                    //新增收付款计划
                    planSaveF.setId(null);
                    planSaveF.setContractId(contractId);
                    planSaveF.setContractNature(contractDetailsV.getContractNature());
                    Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                    //生成损益计划(工程计提合同不生成)
                    if (contractCollectionPlanF.getIsNotProfit() != null && contractCollectionPlanF.getIsNotProfit().equals(1)) {
                        profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                    }
                }
            }else{
                throw BizException.throw402("合同不存在！");
            }
        }
        //更新合同收款/付款计划金额
        updateContractAmount(localCurrencyAmount, taxExcludedAmount, contractId,contractCollectionPlanF.getIsNotProfit());
        //合并损益计划（相同月份，相同费项，相同成本中心）1215版本合并
        List<ContractProfitLossPlanV> profitLossPlanG = profitLossPlanAppService.selectByContract(contractId);
        profitLossPlanAppService.deletePlan(contractCollectionPlanF.getContractId());
        for (ContractProfitLossPlanV planV : profitLossPlanG) {
            ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(planV, ContractProfitLossPlanSaveF.class);
            contractProfitLossPlanF.setContractId(contractId);
            contractProfitLossPlanF.setContractNature(contractNature);
            contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
        }
        //损益列表
        ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
        profitLossPlanF.setContractId(contractId);
        List<ContractProfitLossPlanV> profitLossPlanList = profitLossPlanAppService.listProfitLossPlan(profitLossPlanF);
        if(!CollectionUtils.isEmpty(profitLossPlanList)){
            profitLossPlanVS.addAll(profitLossPlanList);
        }
        return profitLossPlanVS;
    }

    /**
     * 更新收付款计划(1031版本前)
     */
    @Transactional(rollbackFor = {Exception.class})
    public void saveContractCollectionPlan(List<ContractCollectionPlanSaveF> contractCollectionPlanF) throws ParseException {
        for (ContractCollectionPlanSaveF contractCollectionPlanSaveF : contractCollectionPlanF) {
            ContractDetailsV contractDetailsV = contractConcludeService.getContractConclude(contractCollectionPlanSaveF.getContractId());
            if (contractDetailsV == null) {
                throw BizException.throw402("主合同信息不存在！");
            }
            if (contractDetailsV.getSigningMethod() != null && ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                    ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) {
                if (contractCollectionPlanSaveF.getPaymentStatus().equals(ContractSetConst.NOT_COLLECTED)) {
                    //补充或终止合同 更新主合同的收付款计划(部分收款和已收款的计划不更新)
                    contractCollectionPlanService.deleteCollectionPlan(contractDetailsV.getPid());
                    //删除损益计划，重新生成
                    profitLossPlanAppService.deleteProfitLossPlan(null, contractDetailsV.getPid());
                }
            }
        }
        //计算本币金额含税+本币金额不含税的总数，更新到合同信息里面
        BigDecimal localCurrencyAmount = new BigDecimal("0.00");
        BigDecimal taxExcludedAmount = new BigDecimal("0.00");
        Long contractId = null;
        //新增收款计划生成对应的损益计划
        for (ContractCollectionPlanSaveF planSaveF : contractCollectionPlanF) {
            contractId = planSaveF.getContractId();
            localCurrencyAmount = localCurrencyAmount.add(planSaveF.getLocalCurrencyAmount());
            //不含税金额=含税金额/（1+税率）保留两位小数
            BigDecimal taxAmount = BigDecimal.ZERO;
            if (Objects.nonNull(planSaveF.getTaxRate())) {
                taxAmount = planSaveF.getTaxRate().multiply(new BigDecimal("0.01")).add(new BigDecimal(1));
            }
            taxExcludedAmount = taxExcludedAmount.add(planSaveF.getTaxExcludedAmount());
            ContractDetailsV contractDetailsV = contractConcludeService.getContractConclude(planSaveF.getContractId());
            if (contractDetailsV == null) {
                throw BizException.throw402("主合同信息不存在！");
            }
            planSaveF.setContractId(contractDetailsV.getId());
            if (planSaveF.getId() == null) {
                //补充或终止合同 更新主合同的收付款计划(部分收款和已收款的计划不更新)
                if (contractDetailsV.getSigningMethod() != null && (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                        ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) && planSaveF.getPaymentStatus() == 0) {
                    planSaveF.setContractId(contractDetailsV.getPid());
                    Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                    profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                    updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
                }
                planSaveF.setContractId(contractId);
                //新增收付款计划
                Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                //生成损益计划
                profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
            }
            //编辑收付款计划，只编辑未收款的
            if (planSaveF.getId() != null && planSaveF.getPaymentStatus().equals(ContractSetConst.NOT_COLLECTED)) {
                //补充或终止合同 更新主合同的收付款计划(部分收款和已收款的计划不更新)
                if ((ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                        ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) && planSaveF.getPaymentStatus() == 0) {
                    planSaveF.setContractId(contractDetailsV.getPid());
                    Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                    profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                    updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
                }
                //编辑收付款计划，只编辑未收款的
                contractCollectionPlanService.removeContractCollectionPlan(planSaveF.getId());
                profitLossPlanAppService.deleteProfitLossPlan(planSaveF.getId(), null);
                //新增收付款计划
                planSaveF.setId(null);
                planSaveF.setContractId(contractId);
                Long id = contractCollectionPlanService.saveContractCollectionPlan(planSaveF);
                //生成损益计划
                profitLossPlanSave(planSaveF, id, taxAmount,contractId);
                updateWarnState(planSaveF.getContractId(),contractDetailsV.getTenantId(),id,contractDetailsV.getContractNature());
            }
            //更新合同收款/付款计划金额
            updateContractAmount(localCurrencyAmount, taxExcludedAmount, contractId,null);
        }

    }

    /**
     * 更新收付款计划的预警状态
     */
    public void updateWarnState(Long contractId, String tenantId, Long id, Integer contractNature) {
        List<ContractCollectionPlanV> countExpire = collectionExpire(tenantId,
                contractId, contractNature, id);
        if (countExpire.size() > 0) {
            ContractCollectionPlanUpdateF updateF = new ContractCollectionPlanUpdateF();
            updateF.setId(id);
            updateF.setWarnState(Const.State._2);
            contractCollectionPlanService.updateContractCollectionPlan(updateF);
        }
        List<ContractCollectionPlanV> countAdvent = collectionAdvent(tenantId,
                contractId, contractNature, id,30);
        if (countAdvent.size() > 0) {
            ContractCollectionPlanUpdateF updateF = new ContractCollectionPlanUpdateF();
            updateF.setId(id);
            updateF.setWarnState(Const.State._1);
            contractCollectionPlanService.updateContractCollectionPlan(updateF);
        }
    }

    /**
     * 更新合同的含税和不含税金额
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void updateContractAmount(BigDecimal localCurrencyAmount, BigDecimal taxExcludedAmount, Long id,Integer isNotProfit) {
        ContractConcludeUpdateF concludeUpdateF = new ContractConcludeUpdateF();
        concludeUpdateF.setAmountTaxExcluded(taxExcludedAmount);
        concludeUpdateF.setAmountTaxIncluded(localCurrencyAmount);
        concludeUpdateF.setId(id);
        if(null != isNotProfit){
            concludeUpdateF.setIsNotProfit(isNotProfit);
        }
        contractConcludeService.updateContractConclude(concludeUpdateF);
    }

    /**
     * 保存损益计划
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void profitLossPlanSave(ContractCollectionPlanSaveF planSaveF, Long id, BigDecimal taxAmount,Long contractId) {
        //1.核算方式为合同期内分摊
        if (planSaveF.getProfitLossAccounting().equals(ContractSetConst.CONTRACT_PERIOD)) {
            if (planSaveF.getGmtExpireStart().getYear() == planSaveF.getGmtExpireEnd().getYear()
                    && planSaveF.getGmtExpireStart().getMonthValue() == planSaveF.getGmtExpireEnd().getMonthValue()) {
                //开始时间和结束时间在同一年月内。按照时点分摊
                pointTime(planSaveF, id,contractId);
            } else {
                List<Long> profitLossPlanIds = saveProfitLossPlan(planSaveF.getGmtExpireStart(), planSaveF.getGmtExpireEnd(), planSaveF, id, taxAmount);
                // 计算损益计划税额
                List<ContractProfitLossPlanV> contractProfitLossPlanVS = profitLossPlanAppService.selectProfitLossPlanByIds(profitLossPlanIds);
                profitLossPlanTaxAmount(contractProfitLossPlanVS, planSaveF.getTaxAmount());
            }
        }
//        //2.核算方式为服务期内分摊 -->1215版本去掉
//        if (planSaveF.getProfitLossAccounting().equals(ContractSetConst.SERVICE_PERIOD)) {
//            if (planSaveF.getServiceStartDate().getYear() == planSaveF.getServiceEndDate().getYear()
//                    && planSaveF.getServiceStartDate().getMonthValue() == planSaveF.getServiceEndDate().getMonthValue()) {
//                //开始时间和结束时间在同一年月内。按照时点分摊
//                pointTime(planSaveF, id);
//            } else {
//                saveProfitLossPlan(planSaveF.getServiceStartDate(), planSaveF.getServiceEndDate(), planSaveF, id, taxAmount);
//            }
//        }
        //3.核算方式为按时点结算(与收付款计划一致)
        if (planSaveF.getProfitLossAccounting().equals(ContractSetConst.ON_TIME)) {
            pointTime(planSaveF, id,contractId);
        }
    }

    /**
     * 计算损益计划税额
     */
    public void profitLossPlanTaxAmount(List<ContractProfitLossPlanV> contractProfitLossPlanVS, BigDecimal collectionPlanTaxAmount) {
        if (!contractProfitLossPlanVS.isEmpty() && collectionPlanTaxAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalLocalCurrencyAmount = contractProfitLossPlanVS.stream().map(ContractProfitLossPlanV::getLocalCurrencyAmount).reduce(BigDecimal::add).get();
            BigDecimal totalTaxAmount = BigDecimal.ZERO;
            for (ContractProfitLossPlanV item : contractProfitLossPlanVS) {
                // 税额比例  =  此条损益计划金额 / 总损益计划金额
                BigDecimal taxAmountRate = item.getLocalCurrencyAmount().divide(totalLocalCurrencyAmount, 2, RoundingMode.DOWN);
                // 税额 = 税额比例 * 收款计划税额
                BigDecimal taxAmount = taxAmountRate.multiply(collectionPlanTaxAmount).setScale(2, RoundingMode.DOWN);
                totalTaxAmount = totalTaxAmount.add(taxAmount);
                // 更新损益计划税额
                item.setTaxAmount(taxAmount);
                ContractProfitLossPlanUpdateF map = Global.mapperFacade.map(item, ContractProfitLossPlanUpdateF.class);
                profitLossPlanAppService.updateProfitLossPlan(map);
            }
            // 缺省的归到最后一条
            if (totalTaxAmount.compareTo(collectionPlanTaxAmount) < 0) {
                ContractProfitLossPlanV contractProfitLossPlanV = contractProfitLossPlanVS.get(contractProfitLossPlanVS.size() - 1);
                BigDecimal lastTaxAmount = collectionPlanTaxAmount.subtract(totalTaxAmount);
                contractProfitLossPlanV.setTaxAmount(contractProfitLossPlanV.getTaxAmount().add(lastTaxAmount));
                ContractProfitLossPlanUpdateF map = Global.mapperFacade.map(contractProfitLossPlanV, ContractProfitLossPlanUpdateF.class);
                profitLossPlanAppService.updateProfitLossPlan(map);
            }
        }
    }

    /**
     * 损益计算
     * <p>
     * 按照时点分摊(与收付款计划一致)
     */
    public void pointTime(ContractCollectionPlanSaveF planSaveF, Long id,Long contractId) {
        ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(planSaveF, ContractProfitLossPlanSaveF.class);
        contractProfitLossPlanF.setAmountTaxIncluded(planSaveF.getPlannedCollectionAmount());
        contractProfitLossPlanF.setLocalCurrencyAmount(planSaveF.getLocalCurrencyAmount());
        contractProfitLossPlanF.setCollectionPlanId(id);
        contractProfitLossPlanF.setConfirmTime(planSaveF.getPlannedCollectionTime());
        contractProfitLossPlanF.setContractId(contractId);
        if(StringUtils.isNotBlank(planSaveF.getTaxRateIdPath())){
            contractProfitLossPlanF.setTaxRateId(getTaxRateId(planSaveF.getTaxRateIdPath()));
        }
        profitLossPlanAppService.saveContractProfitLossPlan(contractProfitLossPlanF);
    }

    /**
     * 收款/付款计划分页查询
     */
    public PageV<ContractCollectionPlanDetailV> collectionPlanDetailPage(PageF<SearchF<ContractCollectionPlanPageF>> form) {
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            Set<String> orgIdList = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            List<Field> fields = form.getConditions().getFields();
            Field f = new Field();
            f.setName("cc." + ContractConcludeFieldConst.BELONG_ORG_ID);
            f.setValue(new ArrayList<>(orgIdList));
            f.setMethod(15);
            fields.add(f);
        }

        IPage<ContractCollectionPlanDetailV> pageList = contractCollectionPlanService.collectionPlanDetailPage(form, curIdentityInfo().getTenantId());
        pageList.getRecords().forEach(item -> {
            BigDecimal bigDecimal = new BigDecimal("0");
            if (item.getNotPaymentAmount().compareTo(bigDecimal) < 0) {
                item.setNotPaymentAmount(bigDecimal);
            }
            if (item.getNotInvoiceAmount().compareTo(bigDecimal) < 0) {
                item.setNotInvoiceAmount(bigDecimal);
            }

            //项目名称+租户名称
//            if (null != orgFeignClient.getById(item.getTenantId())) {
//                item.setTenantName(orgFeignClient.getById(item.getTenantId()).getName());
//            }
//            if (null != spaceFeignClient.getById(item.getCommunityId())) {
//                item.setTenantName(spaceFeignClient.getById(item.getCommunityId()).getName());
//            }
            if (item.getContractNature().equals(ContractSetConst.INCOME)) {
                //收入类合同，甲方为客商，乙方法定单位
                if (null != orgFeignClient.queryById(item.getPartyAId())) {
                    item.setPartyAName(orgFeignClient.queryById(item.getPartyAId()).getName());
                }
                if (null != orgFeignClient.getByFinanceId(item.getPartyBId())) {
                    item.setPartyBName(orgFeignClient.getByFinanceId(item.getPartyBId()).getNameCn());
                }
            }
            if (item.getContractNature().equals(ContractSetConst.PAY)) {
                //支出类合同，甲方为组织，乙方客商
                if (null != orgFeignClient.getByFinanceId(item.getPartyAId())) {
                    item.setPartyAName(orgFeignClient.getByFinanceId(item.getPartyAId()).getNameCn());
                }
                if (null != orgFeignClient.queryById(item.getPartyBId())) {
                    item.setPartyBName(orgFeignClient.queryById(item.getPartyBId()).getName());
                }
            }
            //费项
            if (null != financeFeignClient.chargeName(item.getChargeItemId())) {
                item.setChargeItemName(financeFeignClient.chargeName(item.getChargeItemId()));
            }
            //成本中心
            if (StringUtils.isBlank(item.getCostName()) && null != orgFeignClient.getByFinanceId(item.getCostId())) {
                item.setCostName(orgFeignClient.getByFinanceId(item.getCostId()).getNameCn());
            }
            //责任部门
            if (StringUtils.isBlank(item.getOrgName()) && null != orgFeignClient.getByOrgId(item.getOrgId())) {
                item.setOrgName(orgFeignClient.getByOrgId(item.getOrgId()).getOrgName());
            }
        });
        return PageV.of(form, pageList.getTotal(), pageList.getRecords());
    }

    /**
     * 初始化数据，对所有costName字段为空的数据进行赋值
     */
    public Integer initAllDataForCostName() {
        List<ContractCollectionPlanE> list = contractCollectionPlanService.list(new QueryWrapper<ContractCollectionPlanE>()
                .isNull(ContractCollectionPlanFieldConst.COST_NAME)
                .isNotNull(ContractCollectionPlanFieldConst.COST_ID));
        if (!CollectionUtils.isEmpty(list)) {
            for (ContractCollectionPlanE planE : list) {
                //成本中心
                if (StringUtils.isBlank(planE.getCostName()) && null != orgFeignClient.getByFinanceId(planE.getCostId())) {
                    planE.setCostName(orgFeignClient.getByFinanceId(planE.getCostId()).getNameCn());
                    contractCollectionPlanService.updateById(planE);
                }
            }
        }
        return list.size();
    }

    /**
     * 收款/付款计划金额总和
     */
    public CollectionPlanSumV collectionPlanAmountSum(PageF<SearchF<ContractCollectionPlanPageF>> form) {
        //组织权限隔离,超管都能看
        if(!isAdminCurUser()) {
            Set<String> orgIdList = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            List<Field> fields = form.getConditions().getFields();
            Field f = new Field();
            f.setName("cc." + ContractConcludeFieldConst.BELONG_ORG_ID);
            f.setValue(new ArrayList<>(orgIdList));
            f.setMethod(15);
            fields.add(f);
        }
        return contractCollectionPlanService.collectionPlanAmountSum(form, curIdentityInfo().getTenantId());
    }

    /**
     * 批量收款
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void batchCollection(ContractCollectionF collectionF) {
        List<CollectionAmountF> collectionAmountFList = collectionF.getCollectionAmountFList();
//        // 校验是否跳期收款
//        if (!collectionAmountFList.isEmpty()) {
//            // 跳期校验
//            if (!jumpPeriodCheck(collectionAmountFList, collectionF.getContractId(), ContractSetConst.COLLECTION)) {
//                throw BizException.throw400(ErrMsgEnum.COLLECTION_JUMP_PERIOD.getErrMsg());
//            }
//        } else {
//            return;
//        }
        if (collectionAmountFList.isEmpty()) {
            return;
        }
        IdentityInfo identityInfo = curIdentityInfo();
        ContractCollectionDetailF contractCollectionDetail = Global.mapperFacade.map(collectionF, ContractCollectionDetailF.class);
        // 收款凭证文件集处理
        if (collectionF.getReceiptVoucherFileVo() != null && !collectionF.getReceiptVoucherFileVo().isEmpty()) {
            String receiptVoucher = fileStorageUtils.batchSubmitFile(
                    collectionF.getReceiptVoucherFileVo(), null, collectionF.getContractId(), identityInfo.getTenantId());
            contractCollectionDetail.setReceiptVoucher(receiptVoucher);
            contractCollectionDetail.setReceiptVoucherName(fileStorageUtils.batchSubmitName(collectionF.getReceiptVoucherFileVo()));
        }
        List<ContractCollectionDetailE> collectionDetailES = new ArrayList<>();
        for (CollectionAmountF item : collectionAmountFList) {
            // 1.更新收款计划收款金额、状态
            collectionPlanUpdate(item.getCollectionPlanId(), item.getOperationAmount());
            // 2.新增收款明细
            contractCollectionDetail.setId(UidHelper.nextId(ContractCollectionDetailFieldConst.CONTRACT_COLLECTION_DETAIL));
            contractCollectionDetail.setCollectionPlanId(item.getCollectionPlanId());
            contractCollectionDetail.setReceivedAmount(item.getOperationAmount());
            contractCollectionDetail.setCollectionTime(collectionF.getCollectionTime());
            ContractCollectionDetailE result = contractCollectionDetailAppService.saveContractCollectionDetail(contractCollectionDetail);
            collectionDetailES.add(result);
//            //3.根据收款计划更新损益计划
//            List<Long> ids= updateProfitLossPlan(item.getCollectionPlanId(), item.getOperationAmount());
//            //查询ids列表信息，更新收款实收金额
//            Boolean result = receivableSettleBatch(ids,collectionF.getCollectionType(),ContractSetConst.INCOME);
//            if(!result) {
//                throw BizException.throw400(ErrMsgEnum.SETTLE_BILLS.getErrMsg());
//            }
            //  生成收款单
            gatherAddBatch(collectionF.getContractId(), item.getCollectionPlanId(), item.getOperationAmount(), collectionF.getCollectionType(), StringUtils.isBlank(item.getSupCpUnitId()) ? "default" : item.getSupCpUnitId());
        }
        List<String> receiptNumbers = collectionDetailES.stream().map(ContractCollectionDetailE::getReceiptNumber).collect(Collectors.toList());
        //收款计划动态日志
        collectionLog(collectionF.getCollectionAmountFList(),collectionF.getContractId()
                ,BusinessTypeEnum.收款.getName(),receiptNumbers);
    }



    public void gatherAddBatch(Long contractId, Long collectionPlanId, BigDecimal operationAmount, Integer collectionType, String supId) {
        AddGatherBillF addGatherBillF = new AddGatherBillF();
        LocalDateTime now = LocalDateTime.now();
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(contractId);
        ContractCollectionPlanE contractCollectionPlan = contractCollectionPlanService.getById(collectionPlanId);
        if (Objects.nonNull(contractConclude)) {
            addGatherBillF.setOutBusNo(contractConclude.getContractNo());
            addGatherBillF.setOutBusId(contractConclude.getId().toString());
            addGatherBillF.setStatutoryBodyId(contractConclude.getPartyBId());
            addGatherBillF.setStatutoryBodyName(contractConclude.getPartyBName());
            // 结算渠道
            if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_CASH)) {
                addGatherBillF.setPayChannel("CASH");
            } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_ALIPAY)){
                addGatherBillF.setPayChannel("ALIPAY");
            } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_WECHAT)) {
                addGatherBillF.setPayChannel("WECHATPAY");
            } else {
                addGatherBillF.setPayChannel("OTHER");
            }
            // 收款、付款方id  名称
            if(contractConclude.getPartyAId() != null){
                MerchantRv merchantRv = orgFeignClient.queryById(contractConclude.getPartyAId());
                if(merchantRv != null){
                    addGatherBillF.setPayerName(merchantRv.getName());
                }
            }
            if(contractConclude.getPartyBId() != null){
                OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(contractConclude.getPartyBId());
                if(orgFinanceRv != null){
                    addGatherBillF.setPayeeName(orgFinanceRv.getNameCn());
                }
            }
            addGatherBillF.setPayerId(contractConclude.getPartyAId().toString());
            addGatherBillF.setPayeeId(contractConclude.getPartyBId().toString());
        }
        addGatherBillF.setStartTime(contractCollectionPlan.getPlannedCollectionTime().atStartOfDay());
        addGatherBillF.setEndTime(contractCollectionPlan.getPlannedCollectionTime().atStartOfDay());
        addGatherBillF.setPayWay(1);
        addGatherBillF.setPayTime(now);
        // 税率
        if (StringUtils.isNotBlank(contractCollectionPlan.getTaxRateIdPath()) && !contractCollectionPlan.getTaxRateIdPath().equals("666")) {
            addGatherBillF.setTaxRateId(getTaxRateId(contractCollectionPlan.getTaxRateIdPath()));
            addGatherBillF.setTaxRate(contractCollectionPlan.getTaxRate());
        } else {
            addGatherBillF.setTaxRateId(0L);
            addGatherBillF.setTaxRate(BigDecimal.ZERO);
        }
        addGatherBillF.setTaxAmount(contractCollectionPlan.getTaxAmount().multiply(new BigDecimal("100")).longValue());
        addGatherBillF.setTotalAmount(operationAmount.multiply(new BigDecimal("100")).longValue());

        addGatherBillF.setSysSource(2);
        addGatherBillF.setApprovedState(2);
        addGatherBillF.setSupCpUnitId(supId);
        AddGatherBillDetailF addGatherBillDetailF = new AddGatherBillDetailF();
        addGatherBillDetailF.setGatherType(0);
        addGatherBillDetailF.setCostCenterId(contractCollectionPlan.getCostId());
        addGatherBillDetailF.setCostCenterName(orgFeignClient.getByFinanceId(contractCollectionPlan.getCostId()).getNameCn());
        addGatherBillDetailF.setChargeItemId(contractCollectionPlan.getChargeItemId());
        addGatherBillDetailF.setChargeItemName(financeFeignClient.chargeName(contractCollectionPlan.getChargeItemId()));
        if (Objects.nonNull(contractConclude)) {
            // 收费组织暂时不传
//            addGatherBillDetailF.setCpOrgId(contractConclude.getId().toString());
//            addGatherBillDetailF.setCpOrgName(contractConclude.getName());
            // 结算渠道
            if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_CASH)) {
                addGatherBillDetailF.setPayChannel("CASH");
            } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_ALIPAY)){
                addGatherBillDetailF.setPayChannel("ALIPAY");
            } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_WECHAT)) {
                addGatherBillDetailF.setPayChannel("WECHATPAY");
            } else {
                addGatherBillDetailF.setPayChannel("OTHER");
            }
            // 收款、付款方id  名称
            if(contractConclude.getPartyAId() != null){
                MerchantRv merchantRv = orgFeignClient.queryById(contractConclude.getPartyAId());
                if(merchantRv != null){
                    addGatherBillDetailF.setPayerName(merchantRv.getName());
                }
            }
            if(contractConclude.getPartyBId() != null){
                OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(contractConclude.getPartyBId());
                if(orgFinanceRv != null){
                    addGatherBillDetailF.setPayeeName(orgFinanceRv.getNameCn());
                }
            }
            addGatherBillDetailF.setPayerId(contractConclude.getPartyAId().toString());
            addGatherBillDetailF.setPayeeId(contractConclude.getPartyBId().toString());
            // 上级收费单元id：合同id
            addGatherBillDetailF.setSupCpUnitId(contractConclude.getId().toString());
            // 上级收费单元名称：合同名称
            addGatherBillDetailF.setSupCpUnitName(contractConclude.getName());
        }
        // 收费单元  取收款计划id
        addGatherBillDetailF.setCpUnitId(contractCollectionPlan.getId().toString());
        addGatherBillDetailF.setCpUnitName("计划".concat(contractCollectionPlan.getId().toString()));
        addGatherBillDetailF.setPayWay(1);
        addGatherBillDetailF.setPayAmount(operationAmount.multiply(new BigDecimal("100")).longValue());
        addGatherBillDetailF.setPayerType(1);
        addGatherBillDetailF.setPayTime(now);
        addGatherBillDetailF.setChargeStartTime(contractCollectionPlan.getPlannedCollectionTime().atStartOfDay());
        addGatherBillDetailF.setChargeEndTime(contractCollectionPlan.getPlannedCollectionTime().atStartOfDay());
        addGatherBillDetailF.setSupCpUnitId(supId);

        addGatherBillF.setAddGatherBillDetails(Collections.singletonList(addGatherBillDetailF));
        log.info("新增收款单：" + JSONObject.toJSONString(Collections.singletonList(addGatherBillF)));
        financeFeignClient.gatherAddBatch(Collections.singletonList(addGatherBillF));
    }

    /**
     * 收款计划动态日志
     */
    public void collectionLog(List<CollectionAmountF> list,Long contractId,String type,List<String> numbers) {
        BigDecimal amount = new BigDecimal("0.00");
        String amountName = null;
        String codeName = null;
        String objName = "";
        for (CollectionAmountF collectionAmountF : list) {
            amount = amount.add(collectionAmountF.getOperationAmount());
        }
        if(list.size() == 1){
            if(type.equals(BusinessTypeEnum.收款.getName())){
                amountName = "收款金额：";
                codeName = "收款明细：";
                objName = BusinessTypeEnum.收款.getName();
            }else{
                amountName = "开票金额：";
                codeName = "开票明细：";
                objName = BusinessTypeEnum.开票.getName();
            }
        }else{
            if(type.equals(BusinessTypeEnum.收款.getName())){
                amountName = "收款金额：";
                codeName = "合并收款明细：";
                objName = BusinessTypeEnum.合并收款.getName();
            }else{
                amountName = "合并开票金额：";
                codeName = "合并开票明细：";
                objName = BusinessTypeEnum.合并开票.getName();
            }
        }
        String finalObjName = objName;
        String code = null;
        if(numbers != null){
            code = StringUtils.join(numbers, ",");
        }
        normalLog(contractId.toString(),finalObjName,amountName,amount.toString(),codeName, code);
    }

    /**
     * 收款计划-批量结算
     */
    public Boolean receivableSettleBatch(List<Long> ids,Integer collectionType,Integer type) {
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }
        //账单查询参数
        PageV<ReceivablePageRv> billList = new PageV<>();
        PageF<SearchF<Field>> receivableSearchF = new PageF<>();
        receivableSearchF.setPageNum(1);
        receivableSearchF.setPageSize(ids.size());
        SearchF<Field> searchF = new SearchF<>();
        searchF.setFields(fields(ids));
        receivableSearchF.setConditions(searchF);
        if(type.equals(ContractSetConst.INCOME)){
            billList = financeFeignClient.receivablePage(receivableSearchF);
        }
        if(type.equals(ContractSetConst.PAY)){
            billList = financeFeignClient.payablePage(receivableSearchF);
        }
        if (!billList.getRecords().isEmpty()) {
            List<AddBillSettleRf> form = new ArrayList<>();
            List<ReceivablePageRv> records = billList.getRecords();
            for (ReceivablePageRv record : records) {
                AddBillSettleRf addBillSettleRf = new AddBillSettleRf();
                //损益
                ContractProfitLossPlanE profitLossPlanE=profitLossPlanAppService.selectById(Long.parseLong(record.getExtField1()));
                //合同详情
                ContractDetailsV contractDetailsV = contractConcludeService.getContractConclude(profitLossPlanE.getContractId());
                addBillSettleRf.setBillId(record.getId());
                //示例50.52转为5052
                Long amount = Long.parseLong(profitLossPlanE.getPaymentAmount().toPlainString().replace(".", ""));
                Long amount1 = Long.parseLong(profitLossPlanE.getLocalCurrencyAmount().toPlainString().replace(".", ""));
                addBillSettleRf.setSettleAmount(amount - record.getSettleAmount());
                addBillSettleRf.setPayAmount(amount1);
                addBillSettleRf.setPayerType(1);
                addBillSettleRf.setPayerLabel(2);
                addBillSettleRf.setPayerId(contractDetailsV.getPartyAId().toString());
                addBillSettleRf.setPayerName(contractDetailsV.getPartyAName());
                addBillSettleRf.setSettleChannel(collectionType.toString());
                addBillSettleRf.setSettleWay(0);
                addBillSettleRf.setSettleTime(LocalDateTime.now());
                addBillSettleRf.setChargeStartTime(record.getStartTime());
                addBillSettleRf.setChargeEndTime(record.getEndTime());
                form.add(addBillSettleRf);
            }
            if(type.equals(ContractSetConst.INCOME)){
                //-- TODO supUnitId default
                return financeFeignClient.receivableSettleBatch(form);
            }
            if(type.equals(ContractSetConst.PAY)){
                //-- TODO supUnitId default
                return financeFeignClient.payableSettleBatch(form);
            }
        }else{
            throw BizException.throw400(ErrMsgEnum.ADD_BILLS_NOT_EXIST.getErrMsg());
        }
        return null;
    }

    /**
     * 根据收付款计划-计算损益计划规则，并更新损益计划
     */
    public List<Long> updateProfitLossPlan(Long collectionPlanId, BigDecimal operationAmount) {
        List<Long> ids = new ArrayList<>();
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
        //3.根据收款计划更新损益计划
        //查询损益计划列表
        ContractProfitLossPlanF contractProfitLossPlanF = new ContractProfitLossPlanF();
        contractProfitLossPlanF.setCollectionId(collectionPlanId);
        contractProfitLossPlanF.setPaymentStatus(ContractSetConst.ALL_COLLECTED);
        List<ContractProfitLossPlanV> profitLossPlanList = profitLossPlanAppService.getProfitLossPlanList(contractProfitLossPlanF);
        //先判断收款是否是部分
        if (contractCollectionPlanE.getPlannedCollectionAmount().equals(operationAmount)) {
            // 如果为整体收款，全部变更为已收款，全额
            for (ContractProfitLossPlanV planV : profitLossPlanList) {
               Long id1 =updateContractProfitLossPlan(planV.getId(), planV.getLocalCurrencyAmount(), ContractSetConst.ALL_COLLECTED);
                ids.add(id1);
            }
        } else {
            // 如果为部分收款，需要遍历依次递减，修改已收款的损益金额和状态
            BigDecimal operationAmount1 = operationAmount;
            for (ContractProfitLossPlanV planV : profitLossPlanList) {
                BigDecimal amountTaxIncluded = planV.getLocalCurrencyAmount();
                if (operationAmount1.signum() == -1 || operationAmount1.signum() == 0) {
                    break;
                }
                if (planV.getPaymentStatus().equals(ContractSetConst.PART_COLLECTED)) {
                    //如果是部分收款的损益
                    amountTaxIncluded = planV.getLocalCurrencyAmount().subtract(planV.getPaymentAmount());
                }
                if (operationAmount1.subtract(amountTaxIncluded).signum() == 1 || operationAmount1.subtract(amountTaxIncluded).signum() == 0) {
                    //当前损益计划已全额收款
                    Long id2=updateContractProfitLossPlan(planV.getId(), planV.getLocalCurrencyAmount(), ContractSetConst.ALL_COLLECTED);
                    ids.add(id2);
                    operationAmount1 = operationAmount1.subtract(amountTaxIncluded);
                    continue;
                }
                if (operationAmount1.subtract(amountTaxIncluded).signum() == -1) {
                    //当前损益计划部分收款
                    Long id3 = updateContractProfitLossPlan(planV.getId(), operationAmount1, ContractSetConst.PART_COLLECTED);
                    ids.add(id3);
                    operationAmount1 = operationAmount1.subtract(amountTaxIncluded);
                    continue;
                }
            }
        }
       return ids;
    }

    /**
     * 变更收付款计划-更新损益计划
     */
    public Long updateContractProfitLossPlan(Long id, BigDecimal paymentAmount, Integer paymentStatus) {
        ContractProfitLossPlanUpdateF profitLossPlanUpdateF = new ContractProfitLossPlanUpdateF();
        profitLossPlanUpdateF.setId(id);
        profitLossPlanUpdateF.setPaymentAmount(paymentAmount);
        profitLossPlanUpdateF.setPaymentStatus(paymentStatus);
        profitLossPlanAppService.updateContractProfitLossPlan(profitLossPlanUpdateF);
        return id;
    }

    /**
     * 批量开票
     */
    @Transactional
    public void batchInvoice(ContractInvoiceF invoiceF) {
        List<CollectionAmountF> collectionAmountFList = invoiceF.getCollectionAmountFList();
        // 此次开盘的损益计划及对应金额
        List<ContractProfitLossPlanV> contractProfitLossPlanVS = new ArrayList<>();
        // 甲方税号校验
        if (Strings.isBlank(invoiceF.getBuyerTaxNum())) {
            throw BizException.throw400("购买方税号不能为空！");
        }
        // 校验是否跳期开票
        if (!collectionAmountFList.isEmpty()) {
            // 跳期校验
            if (!jumpPeriodCheck(collectionAmountFList, invoiceF.getContractId(), ContractSetConst.INVOICE)) {
                throw BizException.throw400(ErrMsgEnum.INVOICE_JUMP_PERIOD.getErrMsg());
            }
        } else {
            return;
        }
        // 开票明细集
        List<ContractInvoiceDetailF> contractInvoiceDetailFList = new ArrayList<>();
        List<ContractInvoiceDetailE> invoiceDetailES = new ArrayList<>();
        // 20221201 暂时去除损益与账单操作
//        // 账单查询参数
//        PageF<SearchF<Field>> receivableSearchF = new PageF<>();
//        Field field = new Field();
//        field.setMethod(15);
//        field.setName("ext_field1");
        collectionAmountFList.forEach(item -> {
            ContractInvoiceDetailF contractInvoiceDetail = Global.mapperFacade.map(invoiceF, ContractInvoiceDetailF.class);
            // 1.更新收款计划开票金额、状态
            Long collectionPlanId = item.getCollectionPlanId();
            BigDecimal operationAmount = item.getOperationAmount();
            ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
            // 本次开票金额+已开票金额
            BigDecimal totalInvoiceAmount = operationAmount.add(contractCollectionPlanE.getInvoiceAmount());
            contractCollectionPlanE.setInvoiceAmount(totalInvoiceAmount);
            // 设置开票状态
            if (Objects.nonNull(contractCollectionPlanE.getPlannedCollectionAmount())) {
                if (totalInvoiceAmount.compareTo(contractCollectionPlanE.getPlannedCollectionAmount()) == 0) {
                    contractCollectionPlanE.setInvoiceStatus(ContractSetConst.ALL_INVOICED);
                } else {
                    contractCollectionPlanE.setInvoiceStatus(ContractSetConst.PART_INVOICED);
                }
            }
            /** 基于收款计划开票 */
            //获取成本中心
            OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(contractCollectionPlanE.getCostId());
            /** 调财务中台 开具蓝票(无校检) 接口，返回发票ID */
            String sellerAddress = invoiceF.getSellerAddress();
            if (StringUtils.isBlank(sellerAddress)) {
                if (null != orgFinanceRv) {
                    sellerAddress = StringUtils.isBlank(orgFinanceRv.getAddress()) ? "地址未填写" : orgFinanceRv.getAddress();
                }
            }
            InvoiceBatchBlueRf invoiceBatchBlueRf = InvoiceBatchBlueRf.create(invoiceF.getPartyAName(),
                    invoiceF.getBuyerTaxNum(), invoiceF.getBuyerAccount(), invoiceF.getBuyerTel(), invoiceF.getBuyerAddress(),
                    invoiceF.getUserName(), collectionPlanId.toString(), orgFinanceRv.getNameCn(), Const.State._1,
                    List.of(-1), sellerAddress, invoiceF.getSellerTaxNum(), invoiceF.getSellerTel(),
                    3);// 默认电子普票
            InvoiceBatchBlueDetailRf invoiceBatchBlueDetailRf = InvoiceBatchBlueDetailRf.create(null, null,
                    contractCollectionPlanE.getChargeItemId().toString(), invoiceF.getProductName(), Const._1,
                    item.getOperationAmount().subtract(item.getInvoiceTax()).toString(), null,
                    item.getOperationAmount().movePointRight(Const.State._2).intValue(),
                    contractCollectionPlanE.getTaxRate().movePointLeft(Const.State._2).toString(), Const._1);
            invoiceBatchBlueRf.setInvoiceBatchDetailBlueFList(List.of(invoiceBatchBlueDetailRf));
            invoiceBatchBlueRf.setSupCpUnitId(invoiceF.getSupCpUnitId());
            //20230807开票财务没对接联调好，开票接口暂时先注释掉
//            Long invoiceId = financeFeignClient.invoiceBatchBlue(invoiceBatchBlueRf);
            contractCollectionPlanService.updateById(contractCollectionPlanE);
            // 2.新增开票明细
//            contractInvoiceDetail.setInvoiceId(invoiceId);
            contractInvoiceDetail.setCollectionPlanId(item.getCollectionPlanId());
            contractInvoiceDetail.setInvoiceApplyAmount(item.getOperationAmount());
            contractInvoiceDetail.setInvoiceTax(item.getInvoiceTax());
            contractInvoiceDetail.setBillType("3");// 默认电子普票
            contractInvoiceDetailFList.add(contractInvoiceDetail);
            //3.根据收付款开票明细，更新损益计划开票金额  获取开票的损益计划及金额// 20221201 暂时去除损益与账单操作
//            contractProfitLossPlanVS.addAll(updateLossPlanByInvoice(collectionPlanId, item.getOperationAmount()));
        });
        // 20221201 暂时去除损益与账单操作
//        // 根据损益id集查账单id集
//        List<Long> profitLossPlanIds = contractProfitLossPlanVS.stream().map(ContractProfitLossPlanV::getId).collect(Collectors.toList());
//        receivableSearchF.setPageNum(1);
//        receivableSearchF.setPageSize(profitLossPlanIds.size());
//        field.setValue(profitLossPlanIds);
//        SearchF<Field> searchF = new SearchF<>();
//        searchF.setFields(Collections.singletonList(field));
//        receivableSearchF.setConditions(searchF);
//        log.info("开票-中台分页查账单：" + JSON.toJSONString(receivableSearchF));
//        PageV<ReceivablePageRv> receivablePageRvPageV = financeFeignClient.receivablePage(receivableSearchF);
//        if (receivablePageRvPageV.getRecords().isEmpty()) {
//            throw BizException.throw400(ErrMsgEnum.CONTRACT_BILL_NOT_EXIST.getErrMsg());
//        }
//        // 账单开票金额
//        List<Long> billIds = new ArrayList<>();
//        List<InvoiceBillAmountRf> invoiceBillAmounts = new ArrayList<>();
        /*-------------------可删除-----------------
//        // 保存收款计划与损益计划与账单关系
//        receivablePageRvPageV.getRecords().forEach(bill -> {
//            ContractProfitLossBillF contractProfitLossBillF = new ContractProfitLossBillF();
//            // 获取中台账单id
//            contractProfitLossBillF.setBillId(bill.getId());
//            Long profitLossPlanId = Long.valueOf(bill.getExtField1());
//            // 获取损益计划id
//            contractProfitLossBillF.setProfitLossPlanId(profitLossPlanId);
//            // 获取收款计划id
//            List<Long> collectionPlanIds = contractProfitLossPlanVS.stream().filter(
//                    item -> item.getId().compareTo(profitLossPlanId) == 0).map(ContractProfitLossPlanV::getCollectionPlanId).collect(Collectors.toList());
//            contractProfitLossBillF.setCollectionPlanId(collectionPlanIds.get(0));
//            contractProfitLossBillAppService.save(contractProfitLossBillF);
//            // 账单开票金额
//            InvoiceBillAmountRf invoiceBillAmountRf = new InvoiceBillAmountRf();
//            invoiceBillAmountRf.setBillId(bill.getId());
//            List<ContractProfitLossPlanV> contractProfitLossPlan = contractProfitLossPlanVS.stream().filter(
//                    item -> item.getId().compareTo(profitLossPlanId) == 0).collect(Collectors.toList());
//            invoiceBillAmountRf.setInvoiceAmount(contractProfitLossPlan.get(0).getLocalCurrencyAmount().intValue());
//            billIds.add(bill.getId());
//            invoiceBillAmounts.add(invoiceBillAmountRf);
//        });
        -------------------可删除-----------------*/
        // 20221201 暂时去除损益与账单操作
//        InvoiceBatchRf invoiceBatchRf = new InvoiceBatchRf();
//        invoiceBatchRf.setBillIds(billIds);
//        invoiceBatchRf.setInvoiceBillAmounts(invoiceBillAmounts);
//        invoiceBatchRf.setBillType(1);
//        BigDecimal priceTaxAmount = invoiceF.getCollectionAmountFList().stream().map(CollectionAmountF::getOperationAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//        invoiceBatchRf.setPriceTaxAmount(priceTaxAmount.multiply(new BigDecimal("100")).longValue());
//        invoiceBatchRf.setInvoiceTitleType(1);
//        invoiceBatchRf.setClerk(invoiceF.getUserName());
//        invoiceBatchRf.setInvoiceType(1);
//        invoiceBatchRf.setType(3);
//        invoiceBatchRf.setSysSource(2);
//        invoiceBatchRf.setInvSource(1);
//        invoiceBatchRf.setPushMode(Collections.singletonList(-1));
//        invoiceBatchRf.setExtendFieldOne(invoiceF.getContractName());
//        invoiceBatchRf.setRemark(invoiceF.getRemark());
//        // 购方信息
//        invoiceBatchRf.setBuyerName(invoiceF.getPartyAName());
//        invoiceBatchRf.setBuyerTaxNum(invoiceF.getBuyerTaxNum());
//        invoiceBatchRf.setBuyerTel(invoiceF.getBuyerTel());
//        invoiceBatchRf.setBuyerAddress(invoiceF.getBuyerAddress());
//        invoiceBatchRf.setBuyerAccount(invoiceF.getBuyerBank() + invoiceF.getBuyerAccount());
//        //  销售方信息
////        invoiceBatchRf.setSalerTaxNum(invoiceF.getSellerTaxNum());
////        invoiceBatchRf.setSalerTel(invoiceF.getSellerTel());
////        invoiceBatchRf.setSalerAddress(invoiceF.getSellerAddress());
//        invoiceBatchRf.setSalerAccount(invoiceF.getSellerBank() + invoiceF.getSellerAccount());
//        // 中台 根据账单ids开票
//        log.info("开票-中台根据账单ids开票：" + JSON.toJSONString(invoiceBatchRf));
//        Long invoiceId = financeFeignClient.invoiceBatch(invoiceBatchRf);
        // 保存开票明细
        contractInvoiceDetailFList.forEach(item -> {
            // 20221201 暂时去除损益与账单操作
//            item.setInvoiceId(invoiceId);
            ContractInvoiceDetailE invoiceDetailE = contractInvoiceDetailAppService.saveContractInvoiceDetail(item);
            invoiceDetailES.add(invoiceDetailE);
        });
        List<String> invoiceApplyNumbers = invoiceDetailES.stream().map(ContractInvoiceDetailE::getInvoiceApplyNumber).collect(Collectors.toList());
        //开票动态日志
        collectionLog(invoiceF.getCollectionAmountFList(),invoiceF.getContractId()
                ,BusinessTypeEnum.开票.getName(),invoiceApplyNumbers);
    }

    /**
     * 根据收付款开票-计算损益计划规则，并更新损益计划
     */
    public List<ContractProfitLossPlanV> updateLossPlanByInvoice(Long collectionPlanId, BigDecimal operationAmount) {
        List<ContractProfitLossPlanV> result = new ArrayList<>();
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
        //3.根据收款计划更新损益计划
        //查询损益计划列表
        ContractProfitLossPlanF contractProfitLossPlanF = new ContractProfitLossPlanF();
        contractProfitLossPlanF.setCollectionId(collectionPlanId);
        contractProfitLossPlanF.setInvoiceStatus(ContractSetConst.ALL_INVOICED);
        List<ContractProfitLossPlanV> profitLossPlanList = profitLossPlanAppService.getProfitLossPlanList(contractProfitLossPlanF);
        //先判断开票是否是部分
        if (contractCollectionPlanE.getLocalCurrencyAmount().equals(operationAmount)) {
            // 如果为整体开票，全部变更为已开票，全额
            for (ContractProfitLossPlanV planV : profitLossPlanList) {
                updateLossPlanByInvoice(planV.getId(), planV.getLocalCurrencyAmount(), ContractSetConst.ALL_INVOICED);
                ContractProfitLossPlanV contractProfitLossPlanV = new ContractProfitLossPlanV();
                contractProfitLossPlanV.setId(planV.getId());
                contractProfitLossPlanV.setCollectionPlanId(collectionPlanId);
                contractProfitLossPlanV.setLocalCurrencyAmount(planV.getLocalCurrencyAmount().multiply(new BigDecimal("100")));
                result.add(contractProfitLossPlanV);
            }
        } else {
            // 如果为部分收款，需要遍历依次递减，修改已收款的损益金额和状态
            BigDecimal operationAmount1 = operationAmount;
            for (ContractProfitLossPlanV planV : profitLossPlanList) {
                BigDecimal amountTaxIncluded = planV.getLocalCurrencyAmount();
                if (operationAmount1.signum() == -1 || operationAmount1.signum() == 0) {
                    break;
                }
                if (planV.getInvoiceStatus().equals(ContractSetConst.PART_INVOICED)) {
                    //如果是部分收款的损益
                    amountTaxIncluded = planV.getLocalCurrencyAmount().subtract(planV.getInvoiceAmount());
                }
                if (operationAmount1.subtract(amountTaxIncluded).signum() == 1 || operationAmount1.subtract(amountTaxIncluded).signum() == 0) {
                    //当前损益计划已全额收款
                    ContractProfitLossPlanV contractProfitLossPlanV = new ContractProfitLossPlanV();
                    contractProfitLossPlanV.setId(planV.getId());
                    contractProfitLossPlanV.setCollectionPlanId(collectionPlanId);
                    contractProfitLossPlanV.setLocalCurrencyAmount(planV.getLocalCurrencyAmount().multiply(new BigDecimal("100")));
                    result.add(contractProfitLossPlanV);
                    updateLossPlanByInvoice(planV.getId(), planV.getLocalCurrencyAmount(), ContractSetConst.ALL_INVOICED);
                    operationAmount1 = operationAmount1.subtract(amountTaxIncluded);
                    continue;
                }
                if (operationAmount1.subtract(amountTaxIncluded).signum() == -1) {
                    //当前损益计划部分收款
                    ContractProfitLossPlanV contractProfitLossPlanV = new ContractProfitLossPlanV();
                    contractProfitLossPlanV.setId(planV.getId());
                    contractProfitLossPlanV.setCollectionPlanId(collectionPlanId);
                    contractProfitLossPlanV.setLocalCurrencyAmount(operationAmount1.multiply(new BigDecimal("100")));
                    result.add(contractProfitLossPlanV);
                    updateLossPlanByInvoice(planV.getId(), planV.getInvoiceAmount().add(operationAmount1), ContractSetConst.PART_INVOICED);
                    operationAmount1 = operationAmount1.subtract(amountTaxIncluded);
                    continue;
                }
            }
        }
        return result;
    }

    /**
     * 开票变更-更新损益计划
     */
    public void updateLossPlanByInvoice(Long id, BigDecimal invoiceAmount, Integer invoiceStatus) {
        ContractProfitLossPlanUpdateF profitLossPlanUpdateF = new ContractProfitLossPlanUpdateF();
        profitLossPlanUpdateF.setId(id);
        profitLossPlanUpdateF.setInvoiceAmount(invoiceAmount);
        profitLossPlanUpdateF.setInvoiceStatus(invoiceStatus);
        profitLossPlanAppService.updateContractProfitLossPlan(profitLossPlanUpdateF);
    }

    /**
     * 批量付款
     */
//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void batchPayment(ContractPaymentF contractPaymentF) throws ParseException {
        List<CollectionAmountF> collectionAmountFList = contractPaymentF.getCollectionAmountFList();
        // 校验是否付款
        if (!collectionAmountFList.isEmpty()) {
            // 跳期校验
            if (!jumpPeriodCheck(collectionAmountFList, contractPaymentF.getContractId(), ContractSetConst.PAYMENT)) {
                throw BizException.throw400(ErrMsgEnum.PAYMENT_JUMP_PERIOD.getErrMsg());
            }
        } else {
            return;
        }
        IdentityInfo identityInfo = curIdentityInfo();
        ContractPaymentDetailF contractPaymentDetail = Global.mapperFacade.map(contractPaymentF, ContractPaymentDetailF.class);
        // 付款发票文件集处理
        if (!contractPaymentF.getInvoiceFiles().isEmpty()) {
            String invoiceCode = fileStorageUtils.batchSubmitFile(
                    contractPaymentF.getInvoiceFiles(), null, contractPaymentF.getContractId(), identityInfo.getTenantId());
            contractPaymentDetail.setInvoiceCode(invoiceCode);
        }
        for (CollectionAmountF item : collectionAmountFList) {
            // 审批暂时默认通过，并更新付款计划已付款金额、付款状态
            // 1.更新付款计划收款金额、状态
            collectionPlanUpdate(item.getCollectionPlanId(), item.getOperationAmount());
            // 2.新增付款明细
            contractPaymentDetail.setId(UidHelper.nextId(ContractCollectionDetailFieldConst.CONTRACT_COLLECTION_DETAIL));
            contractPaymentDetail.setCollectionPlanId(item.getCollectionPlanId());
            contractPaymentDetail.setPaymentAmount(item.getOperationAmount());
            contractPaymentDetail.setAuditStatus(0);// 0通过
            contractPaymentDetailAppService.saveContractPaymentDetail(contractPaymentDetail);
//            //3.根据付款计划和损益计划推送账单
//            List<Long> ids = updateProfitLossPlan(item.getCollectionPlanId(), item.getOperationAmount());
//            //查询损益计划
//            List<ContractProfitLossPlanV> profitLossPlanList = profitLossPlanAppService.selectProfitLossPlanByIds(ids);
            //判断账单id是否为空，如果null，新增账单，如果不为空，根据账单id更新付款金额和状态
//            profitLossPlanAppService.profitLossPlanCreateBill(
//                    null, contractPaymentF.getContractId(), contractPaymentF.getPaymentMethod(), false, null);
//            //根据账单id更新付款金额和状态
//            List<Long> profitLossIds = profitLossPlanList.stream().map(ContractProfitLossPlanV::getId).collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(profitLossIds)) {
//                throw BizException.throw400(ErrMsgEnum.ADD_BILLS_NOT_EXIST.getErrMsg());
//            }
//            Boolean result = receivableSettleBatch(profitLossIds, contractPaymentF.getPaymentMethod(), ContractSetConst.PAY);
//            if (!result) {
//                throw BizException.throw400(ErrMsgEnum.PAY_BILLS.getErrMsg());
//            }
        }
    }

    /**
     * 更新收/付款计划收/付款金额、状态
     */
    private void collectionPlanUpdate(Long collectionPlanId, BigDecimal operationAmount) {
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
        // 本次收/付款金额+已收/付款金额
        BigDecimal totalPaymentAmount = operationAmount.add(contractCollectionPlanE.getPaymentAmount());
        contractCollectionPlanE.setPaymentAmount(totalPaymentAmount);
        // 设置收/付款状态
        if (Objects.nonNull(contractCollectionPlanE.getPlannedCollectionAmount())) {
            if (totalPaymentAmount.compareTo(BigDecimal.ZERO) == 0) {
                contractCollectionPlanE.setPaymentStatus(ContractSetConst.NOT_COLLECTED);
            } else if (totalPaymentAmount.compareTo(contractCollectionPlanE.getPlannedCollectionAmount()) == 0) {
                contractCollectionPlanE.setPaymentStatus(ContractSetConst.ALL_COLLECTED);
                contractCollectionPlanE.setWarnState(0);
            } else {
                contractCollectionPlanE.setPaymentStatus(ContractSetConst.PART_COLLECTED);
            }
        }
        contractCollectionPlanService.updateById(contractCollectionPlanE);
    }

    /**
     * 批量收款、开票、付款跳期校验
     *
     * @return true 通过校验   false  未通过校验
     */
    private boolean jumpPeriodCheck(List<CollectionAmountF> collectionAmountFList, Long contractId, int operationType) {
        ContractCollectionPlanF contractCollectionPlanF = new ContractCollectionPlanF();
        contractCollectionPlanF.setContractId(contractId);
        // 此合同下所有的未收款/未付款/未开票（包含部分收、付、开票）计划
        List<ContractCollectionPlanV> planList = new ArrayList<>();
        if (operationType == ContractSetConst.COLLECTION || operationType == ContractSetConst.PAYMENT) {
            planList = contractCollectionPlanService.listContractCollectionPlan(contractCollectionPlanF)
                    .stream().filter(item -> item.getPaymentStatus().intValue() != ContractSetConst.ALL_INVOICED.intValue()).collect(Collectors.toList());
        } else if (operationType == ContractSetConst.INVOICE) {
            planList = contractCollectionPlanService.listContractCollectionPlan(contractCollectionPlanF)
                    .stream().filter(item -> item.getInvoiceStatus().intValue() != ContractSetConst.ALL_INVOICED.intValue()).collect(Collectors.toList());
        } else {
            throw BizException.throw400(ErrMsgEnum.FUNCTION_NOT_EXIST.getErrMsg());
        }

        // 此次收款的计划id集
        List<Long> planIdList = collectionAmountFList.stream().map(CollectionAmountF::getCollectionPlanId).collect(Collectors.toList());
        // 此次收款的计划中最晚的
        LocalDate maxLocalDate = collectionAmountFList.stream().map(CollectionAmountF::getPlannedCollectionTime).max(LocalDate::compareTo).get();
        // 未收款的计划集
        List<ContractCollectionPlanV> notCollectionPlanList = planList.stream().filter(item -> !planIdList.contains(item.getId())).collect(Collectors.toList());
        // 在此次收款时间之前的计划，为空代表未跳期
        List<ContractCollectionPlanV> planVList = notCollectionPlanList.stream().filter(
                item -> item.getPlannedCollectionTime().compareTo(maxLocalDate) < 0).collect(Collectors.toList());
        if (planVList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 损益计算
     * //计算公式 1.整月月份值按照月份计算，不整月月份按照天数计算：月份值=天数/该月总天数
     * //2.整月损益计算：计划收款金额/月份数=每月应收金额（含税）
     * //3.首月不足月计算：首月金额=整月损益金额*天数/该月总天数
     * //4.末月不足月计算：总金额-前面其他月的的金额=末月金额
     */
    public List<Long> saveProfitLossPlan(LocalDate startDate, LocalDate endDate, ContractCollectionPlanSaveF planSaveF, Long id, BigDecimal taxAmount) {
        List<Long> profitLossPlanIds = new ArrayList<>();
        //计划收款金额
        BigDecimal amount = planSaveF.getLocalCurrencyAmount();
        //原币金额
        BigDecimal plannedAmount = planSaveF.getPlannedCollectionAmount();
        //每个月应收金额（整月）
        BigDecimal monthAmount = null;
        //原币金额
        BigDecimal plannedMonthAmount = null;
        //包含整月的月份数
        int monthSum = 0;
        //整月月份列表
        List<String> monthList = new ArrayList<>();
        BigDecimal firstAmount = null;
        //原币
        BigDecimal firstPlannedAmount = null;
        if (daySum(null, endDate) == 0 && daySum(startDate, null) > 0) {
            //开始月份不是整月 末月是整月
            int daySum = daySum(startDate, null);
            monthList = getMonthBetween(startDate.plusMonths(1).toString(), endDate.toString());
            monthSum = monthList.size();
            //月份值=天数/该月总天数
            double monthDay = monthSum + new BigDecimal((float) daySum / startDate.lengthOfMonth()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            monthAmount = amount.divide(BigDecimal.valueOf((monthDay)), 2, RoundingMode.HALF_UP);
            plannedMonthAmount = plannedAmount.divide(BigDecimal.valueOf((monthDay)), 2, RoundingMode.HALF_UP);
            //首月金额=整月损益金额*天数/该月总天数
            firstAmount = monthAmount.multiply(BigDecimal.valueOf((daySum)))
                    .divide(BigDecimal.valueOf((startDate.lengthOfMonth())), 2, RoundingMode.HALF_UP);
            firstPlannedAmount = plannedMonthAmount.multiply(BigDecimal.valueOf((daySum)))
                    .divide(BigDecimal.valueOf((startDate.lengthOfMonth())), 2, RoundingMode.HALF_UP);
        }
        if (daySum(startDate, null) == 0 && daySum(null, endDate) == 0) {
            //全部为整月
            monthList = getMonthBetween(startDate.toString(), endDate.toString());
            monthSum = monthList.size();
            monthAmount = amount.divide(BigDecimal.valueOf((monthSum)), 2, RoundingMode.HALF_UP);
            //原币
            plannedMonthAmount = plannedAmount.divide(BigDecimal.valueOf((monthSum)), 2, RoundingMode.HALF_UP);
        }
        if (daySum(startDate, null) > 0 && daySum(null, endDate) > 0) {
            //全部为不整月
            int startDay = daySum(startDate, null);
            int endDay = daySum(null, endDate);
            monthList = getMonthBetween(startDate.plusMonths(1).toString(), endDate.minusMonths(1).toString());
            monthSum = monthList.size();
            BigDecimal a = BigDecimal.valueOf(startDay).divide(BigDecimal.valueOf(startDate.lengthOfMonth()), 2, RoundingMode.HALF_UP);
            BigDecimal b = BigDecimal.valueOf(endDay).divide(BigDecimal.valueOf(endDate.lengthOfMonth()), 2, RoundingMode.HALF_UP);
            BigDecimal c = BigDecimal.valueOf(monthSum).add(a).add(b);
            monthAmount = amount.divide(c, 2, RoundingMode.HALF_UP);
            firstAmount = monthAmount.multiply(a);
            //原币
            BigDecimal c1 = BigDecimal.valueOf(monthSum).add(a).add(b);
            plannedMonthAmount = plannedAmount.divide(c1, 2, RoundingMode.HALF_UP);
            firstPlannedAmount = plannedMonthAmount.multiply(a);
        }
        if (daySum(startDate, null) == 0 && daySum(null, endDate) > 0) {
            //开始月份是整月 && 结束月份非整月
            int endDay = daySum(null, endDate);
            monthList = getMonthBetween(startDate.toString(), endDate.minusMonths(1).toString());
            monthSum = monthList.size();
            double monthDay = monthSum + new BigDecimal((float) endDay / startDate.lengthOfMonth()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            monthAmount = amount.divide(BigDecimal.valueOf((monthDay)), 2, RoundingMode.HALF_UP);
            //原币
            plannedMonthAmount = plannedAmount.divide(BigDecimal.valueOf((monthDay)), 2, RoundingMode.HALF_UP);
        }
        ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(planSaveF, ContractProfitLossPlanSaveF.class);
        if (daySum(startDate, null) > 0) {
            //保存首月损益计划
            contractProfitLossPlanF.setAmountTaxIncluded(firstPlannedAmount);
            contractProfitLossPlanF.setLocalCurrencyAmount(firstAmount);
            contractProfitLossPlanF.setTaxExcludedAmount(firstAmount.divide(taxAmount, 2, RoundingMode.HALF_UP));
            contractProfitLossPlanF.setConfirmTime(startDate);
            contractProfitLossPlanF.setCollectionPlanId(id);
            contractProfitLossPlanF.setTaxRateId(getTaxRateId(planSaveF.getTaxRateIdPath()));
            profitLossPlanIds.add(profitLossPlanAppService.saveContractProfitLossPlan(contractProfitLossPlanF));
        }
        //收集到整月的时间，遍历均分存储
        for (String m : monthList) {
            //遍历新增时去掉结束月份
            String month = endDate.getYear() + "-0" + endDate.getMonthValue();
            if (endDate.getMonthValue() >= 10) {
                month = endDate.getYear() + "-" + endDate.getMonthValue();
            }
            if (!m.equals(month)) {
                contractProfitLossPlanF.setAmountTaxIncluded(plannedMonthAmount);
                contractProfitLossPlanF.setLocalCurrencyAmount(monthAmount);
                contractProfitLossPlanF.setTaxExcludedAmount(monthAmount.divide(taxAmount, 2, RoundingMode.HALF_UP));
                contractProfitLossPlanF.setConfirmTime(LocalDate.parse(m + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                contractProfitLossPlanF.setCollectionPlanId(id);
                contractProfitLossPlanF.setTaxRateId(getTaxRateId(planSaveF.getTaxRateIdPath()));
                profitLossPlanIds.add(profitLossPlanAppService.saveContractProfitLossPlan(contractProfitLossPlanF));
            }
        }
        if (daySum(null, endDate) >= 0) {
            //尾月为不整月
            //总金额-查询前面月份的金额总数
            BigDecimal monthAmountSum = profitLossPlanAppService.selectAmountSum(id);
            BigDecimal localCurrencyAmount = profitLossPlanAppService.localCurrencyAmount(id);
            //本币
            BigDecimal endPlannedMonth = plannedAmount.subtract(monthAmountSum);
            //原币
            BigDecimal endMonth = amount.subtract(localCurrencyAmount);
            contractProfitLossPlanF.setAmountTaxIncluded(endPlannedMonth);
            contractProfitLossPlanF.setLocalCurrencyAmount(endMonth);
            contractProfitLossPlanF.setTaxExcludedAmount(endMonth.divide(taxAmount, 2, RoundingMode.HALF_UP));
            contractProfitLossPlanF.setConfirmTime(endDate);
            contractProfitLossPlanF.setCollectionPlanId(id);
            contractProfitLossPlanF.setTaxRateId(getTaxRateId(planSaveF.getTaxRateIdPath()));
            profitLossPlanIds.add(profitLossPlanAppService.saveContractProfitLossPlan(contractProfitLossPlanF));
        }
        return profitLossPlanIds;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void removeContractCollectionPlan(Long id) {
        contractCollectionPlanService.removeContractCollectionPlan(id);
        //删除收款计划也要删除对应的损益计划
        profitLossPlanAppService.deleteProfitLossPlan(id, null);
        // 删除收款、付款、开票明细
        contractCollectionDetailAppService.deleteByCollectionPlanId(id);
        contractInvoiceDetailAppService.deleteByCollectionPlanId(id);
        contractPaymentDetailAppService.deleteByCollectionPlanId(id);
    }

    /**
     * 获取两个时间节点之间的差值天数
     * 判断首月和尾月是否是整月
     **/
    public Integer daySum(LocalDate startDate, LocalDate endDate) {
        //判断是否整月
        int difference = 0;
        if (startDate != null) {
            //当天
            int nowDay = startDate.getDayOfMonth();
            //最后一天
            int endDay = startDate.lengthOfMonth();
            if (endDay - nowDay >= 0) {
                //差值左闭右开
                difference = endDay - nowDay + 1;
            }
        }
        if (endDate != null) {
            //最后一天
            int endDay = endDate.lengthOfMonth();
            int monthDay = endDate.getDayOfMonth();
            if (monthDay != endDay) {
                difference = monthDay;
            }
        }
        return difference;
    }

    /**
     * 获取两个时间节点之间的月份列表
     **/
    private static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            //格式化为年月
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteCollectionPlan(Long contractId) {
        contractCollectionPlanService.deleteCollectionPlan(contractId);
    }

    public Integer checkContractByPlan(Long id) {
        return contractCollectionPlanService.checkContractByPlan(id);
    }

    public List<ContractCollectionPlanV> collectionExpire(String tenantId, Long contractId, Integer contractNature, Long id) {
        return contractCollectionPlanService.collectionExpire(tenantId, contractId, contractNature, id,true);
    }

    public List<ContractCollectionPlanV> collectionAdvent(String tenantId, Long contractId, Integer contractNature, Long id,Integer dayNum) {
        return contractCollectionPlanService.collectionAdvent(tenantId, contractId, contractNature, id,true,dayNum);
    }

    public Long getTaxRateId(String collectionPlanTaxRateId) {
        List<Long> collect =
                Arrays.stream(collectionPlanTaxRateId.split(",")).map(Long::valueOf).collect(Collectors.toList());
        return collect.get(collect.size() - 1);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void update(CollectionAndLossPlanUpdateF planUpdateF) {
        for (ContractCollectionPlanUpdateF contractCollectionPlanSaveF : planUpdateF.getCollectionPlanSaveFList()) {

            //成本中心
            if (null != orgFeignClient.getByFinanceId(contractCollectionPlanSaveF.getCostId())) {
                contractCollectionPlanSaveF.setCostName(orgFeignClient.getByFinanceId(contractCollectionPlanSaveF.getCostId()).getNameCn());
            }
            //责任部门
            if (null != orgFeignClient.getByOrgId(contractCollectionPlanSaveF.getOrgId())) {
                contractCollectionPlanSaveF.setOrgName(orgFeignClient.getByOrgId(contractCollectionPlanSaveF.getOrgId()).getOrgName());
            }

            contractCollectionPlanService.updateContractCollectionPlan(contractCollectionPlanSaveF);
        }
        for (ContractProfitLossPlanUpdateF contractProfitLossPlanSaveF : planUpdateF.getProfitLossPlanSaveFList()) {
            updateWarnState(null,null,contractProfitLossPlanSaveF.getCollectionPlanId(),null);
            if(contractProfitLossPlanSaveF.getId() == null){
                ContractProfitLossPlanSaveF map = Global.mapperFacade.map(contractProfitLossPlanSaveF, ContractProfitLossPlanSaveF.class);
                Long id = profitLossPlanAppService.saveContractProfitLossPlan(map);
            }else{
                profitLossPlanAppService.updateContractProfitLossPlan(contractProfitLossPlanSaveF);
            }
        }
        //合并损益计划（相同月份，相同费项，相同成本中心）1215版本合并
        List<ContractProfitLossPlanV> profitLossPlanG = profitLossPlanAppService.selectByContract(planUpdateF.getContractId());
        profitLossPlanAppService.deletePlan(planUpdateF.getContractId());
        for (ContractProfitLossPlanV planV : profitLossPlanG) {
            ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(planV, ContractProfitLossPlanSaveF.class);
            contractProfitLossPlanF.setContractId(planUpdateF.getContractId());
            contractProfitLossPlanF.setContractNature(planUpdateF.getContractNature());
            contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
        }
    }

//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void derate(CollectionPlanDerateF collectionPlanDerateF) throws ParseException {
        ContractConcludeE contractConcludeE = contractConcludeService.getById(collectionPlanDerateF.getContractId());
        Long collectionPlanId = collectionPlanDerateF.getCollectionPlanId();
        BigDecimal exchangeRate = new BigDecimal(contractConcludeE.getExchangeRate());
        ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
        BigDecimal derateAmount = collectionPlanDerateF.getDerateAmount();
        // --------------新增减免明细
        List<CollectionPlanDerateDetailE> derateDetailES = new ArrayList<>();
        CollectionPlanDerateDetailF map = Global.mapperFacade.map(collectionPlanDerateF, CollectionPlanDerateDetailF.class);
        map.setAuditStatus(0); // 暂时默认通过
        CollectionPlanDerateDetailE derateDetailE =collectionPlanDerateDetailAppService.saveBondCollectionDetail(map);
        derateDetailES.add(derateDetailE);
        // --------------收款计划
        // 计划收款金额本币   本币 - 减免
        contractCollectionPlanE.setLocalCurrencyAmount(contractCollectionPlanE.getLocalCurrencyAmount().subtract(derateAmount));
        // 计划收款金额原币  本币 / 汇率
        contractCollectionPlanE.setPlannedCollectionAmount(contractCollectionPlanE.getLocalCurrencyAmount().divide(exchangeRate,2,RoundingMode.HALF_UP));
        // 总减免金额
        contractCollectionPlanE.setCreditAmount(contractCollectionPlanE.getCreditAmount().add(derateAmount));
        contractCollectionPlanService.updateById(contractCollectionPlanE);
        // ----------------合同
        //  更新合同实际履约金额、减免金额 计算
        contractConcludeE.setContractAmount(contractConcludeE.getContractAmount().subtract(derateAmount));
        contractConcludeE.setCreditAmount(contractConcludeE.getCreditAmount().add(derateAmount));
        contractConcludeService.updateById(contractConcludeE);
        // --------------------更新损益计划  重新推送账单  //20221201暂时去除损益与账单操作
//        ContractProfitLossPlanF contractProfitLossPlanF = new ContractProfitLossPlanF();
//        contractProfitLossPlanF.setCollectionId(collectionPlanId);
//        contractProfitLossPlanF.setDeleted(0);
//        contractProfitLossPlanF.setSetPaymentStatus(0);
//        contractProfitLossPlanF.setSetInvoiceStatus(0); // 20221117追加只能减未开票的损益
//        List<ContractProfitLossPlanV> profitLossPlanList = profitLossPlanAppService.getProfitLossPlanList(contractProfitLossPlanF);
//        if (profitLossPlanList.isEmpty()) {
//            throw BizException.throw400("此收款计划下无可减免损益计划，减免失败！");
//        }
//        // 未收款的损益总金额
//        BigDecimal localCurrencySum = profitLossPlanList.stream().map(ContractProfitLossPlanV::getLocalCurrencyAmount).reduce(BigDecimal::add).get();
//        BigDecimal derateAmountSum = new BigDecimal("0.0");
//        // 分摊减免金额
//        profitLossPlanList.forEach(item -> {
//            BigDecimal localCurrencyAmount = item.getLocalCurrencyAmount();
//            // 优惠比例  = 此条计划收金额 / 总计划收金额
//            BigDecimal preferentialRate = localCurrencyAmount.divide(localCurrencySum, 2, RoundingMode.DOWN);
//            // 优惠金额  = 优惠比例 * 此次优惠总金额
//            BigDecimal localDerateAmount = preferentialRate.multiply(derateAmount);
//            derateAmountSum.add(localDerateAmount);
//            // 更新应收金额
//            BigDecimal localCurrencyDerate = item.getLocalCurrencyAmount().subtract(localDerateAmount); //本币金额（含税）
//            profitLossPlanAmountCompute(item, localCurrencyDerate, exchangeRate);
//
//        });
//        // 少减免的归到最后一条
//        if (derateAmountSum.compareTo(derateAmount) < 0) {
//            ContractProfitLossPlanV contractProfitLossPlanV = profitLossPlanList.get(profitLossPlanList.size() - 1);
//            BigDecimal lastDerateAmount = derateAmount.subtract(derateAmount);
//            BigDecimal localCurrencyAmount = contractProfitLossPlanV.getLocalCurrencyAmount().subtract(lastDerateAmount);
//            profitLossPlanAmountCompute(contractProfitLossPlanV, localCurrencyAmount, exchangeRate);
//        }
//        // 更新损益
//        profitLossPlanList.forEach(item -> {
//            item.setBillId(null);
//            ContractProfitLossPlanUpdateF map1 = Global.mapperFacade.map(item, ContractProfitLossPlanUpdateF.class);
//            profitLossPlanAppService.updateContractProfitLossPlan(map1);
//        });
//        // 收款计划减免即时更新账单
//        if (contractConcludeE.getContractNature() == 1) {
//            //  作废账单
//            receivableDelete(collectionPlanId, false);
//            // 批量新增账单
//            profitLossPlanAppService.profitLossPlanCreateBill(collectionPlanId, collectionPlanDerateF.getContractId(), null, true, profitLossPlanList);
//        }
        derateLog(contractConcludeE,collectionPlanDerateF.getDerateAmount().toString());
    }

    /**
     * 损益计划金额计算  本币金额（含税） 算出 原币/含税  本币/不含税
     * @param localCurrencyAmount 本币金额（含税）
     * @param exchangeRate 汇率
     */
    public void profitLossPlanAmountCompute(ContractProfitLossPlanV contractProfitLossPlanV,
                                            BigDecimal localCurrencyAmount,
                                            BigDecimal exchangeRate) {
        // 税率
        BigDecimal taxAmount = new BigDecimal(contractProfitLossPlanV.getTaxRate()).multiply(new BigDecimal("0.01")).add(new BigDecimal(1));
        BigDecimal derateTaxIncluded = localCurrencyAmount.divide(exchangeRate,2, RoundingMode.HALF_UP);//计划损益金额（原币/含税）
        //不含税金额=含税金额/（1+税率）保留两位小数
        BigDecimal taxExcludedDerate = localCurrencyAmount.divide(taxAmount, 2, RoundingMode.HALF_UP);//本币金额（不含税）
        contractProfitLossPlanV.setLocalCurrencyAmount(localCurrencyAmount);
        contractProfitLossPlanV.setAmountTaxIncluded(derateTaxIncluded);
        contractProfitLossPlanV.setTaxExcludedAmount(taxExcludedDerate);
    }

    public void paymentInvoice(List<CollectionPlanPaymentInvoiceF> from) {
        IdentityInfo identityInfo = curIdentityInfo();
        if (!from.isEmpty()) {
            List<ContractReceiveInvoiceDetailE> invoiceDetailES = new ArrayList<>();
            List<ContractPaymentDetailE> paymentDetailES = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            from.forEach(item -> {
                if (item.getPaymentType() == 1) {
                    // 付款流程
                    ContractPaymentDetailE contractPaymentDetailE = paymentInvoicePaymentDetail(item, identityInfo, 1);
                    paymentDetailES.add(contractPaymentDetailE);
                } else {
                    // 付款收票
                    // 付款金额不为0  先付款
                    if (item.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                        ContractPaymentDetailE contractPaymentDetailE = paymentInvoicePaymentDetail(item, identityInfo, 0);
                        paymentDetailES.add(contractPaymentDetailE);
                    }
                    // 收票  更新付款计划收票金额、状态
                    ContractCollectionPlanE contractCollectionPlanE = contractCollectionPlanService.getById(item.getCollectionPlanId());
                    BigDecimal invoiceAmount = contractCollectionPlanE.getInvoiceAmount().add(item.getInvoiceAmount());
                    contractCollectionPlanE.setInvoiceAmount(invoiceAmount);
                    if (invoiceAmount.compareTo(contractCollectionPlanE.getLocalCurrencyAmount()) >= 0) {
                        // 已收票
                        contractCollectionPlanE.setInvoiceStatus(2);
                    } else if (invoiceAmount.compareTo(BigDecimal.ZERO) != 0 &&
                            invoiceAmount.compareTo(contractCollectionPlanE.getLocalCurrencyAmount()) < 0) {
                        // 部分收票
                        contractCollectionPlanE.setInvoiceStatus(1);
                    }
                    contractCollectionPlanService.updateById(contractCollectionPlanE);
                    // 追加收票明细
                    ContractReceiveInvoiceDetailF receiveInvoiceDetailF = new ContractReceiveInvoiceDetailF();
                    receiveInvoiceDetailF.setContractId(item.getContractId());
                    receiveInvoiceDetailF.setCollectionPlanId(item.getCollectionPlanId());
                    receiveInvoiceDetailF.setInvoiceAmount(item.getInvoiceAmount());
                    receiveInvoiceDetailF.setInvoiceTime(now);
                    receiveInvoiceDetailF.setRemark(item.getRemark());
                    ContractReceiveInvoiceDetailE receiveInvoiceDetailE = contractReceiveInvoiceDetailAppService.saveReceiveInvoiceDetail(receiveInvoiceDetailF);
                    invoiceDetailES.add(receiveInvoiceDetailE);
                }
                //调用中台生成付款单
                if (item.getPaymentAmount() != null && item.getPaymentAmount() != BigDecimal.ZERO) {
                    financeFacade.addPayBill(item);
                }
            });
            //收票
            List<String> invoiceApplyNumbers = invoiceDetailES.stream().map(ContractReceiveInvoiceDetailE::getInvoiceNumber).collect(Collectors.toList());
            List<String> paymentApplyNumber = paymentDetailES.stream().map(ContractPaymentDetailE::getPaymentApplyNumber).collect(Collectors.toList());
            //动态日志
            paymentInvoiceLog(from,paymentApplyNumber,invoiceApplyNumbers);
        }

    }
    /**
     * 减免扣款动态日志
     */
    public void derateLog(ContractConcludeE concludeE,String derateAmount) {
        String objName = "";
        String amountName = "";
        if(concludeE.getContractNature().equals(ContractSetConst.INCOME)){
            objName = BusinessTypeEnum.减免申请.getName();
            amountName = "申请减免金额：";
        }else{
            objName = BusinessTypeEnum.扣款申请.getName();
            amountName = "申请扣款金额：";
        }
        String finalObjName = objName;
        BizLog.normal(concludeE.getId().toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return finalObjName;
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem(amountName + derateAmount, false))
        );
    }

    /**
     * 付款收票计划动态日志
     */
    public void paymentInvoiceLog(List<CollectionPlanPaymentInvoiceF> list,List<String> numbers,List<String> code) {
        BigDecimal amount = new BigDecimal("0.00");
        String amountName = null;
        Long contractId = null;
        String codeName = null;
        String objName = "";
        Integer type = null;
        BigDecimal paymentAmount = new BigDecimal("0.00");
        for (CollectionPlanPaymentInvoiceF collectionAmountF : list) {
            contractId = collectionAmountF.getContractId();
            amount = amount.add(collectionAmountF.getPaymentAmount());
            type = collectionAmountF.getPaymentType();
            paymentAmount = collectionAmountF.getPaymentAmount();
        }
        if (type == 1) {
            //付款
            if (list.size() == 1) {
                amountName = "申请付款金额：";
                codeName = "付款明细：";
                objName = BusinessTypeEnum.预付款申请.getName();

            } else {
                amountName = "合并付款金额：";
                codeName = "合并付款明细：";
                objName = BusinessTypeEnum.合并预付款.getName();
            }
            String finalObjName = objName;
            normalLog(contractId.toString(),finalObjName,amountName,amount.toString(),codeName, StringUtils.join(numbers, ","));
        } else {
            if (paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                //付款
                if (list.size() == 1) {
                    amountName = "申请付款金额：";
                    codeName = "付款明细：";
                    objName = BusinessTypeEnum.结算收票申请.getName();

                } else {
                    amountName = "合并付款金额：";
                    codeName = "合并付款明细：";
                    objName = BusinessTypeEnum.合并结算收票.getName();
                }
                String finalObjName = objName;
                normalLog(contractId.toString(),finalObjName,amountName,amount.toString(),codeName, StringUtils.join(numbers, ","));
            }else{
                // 付款收票
                if (list.size() == 1) {
                    amountName = "申请收票金额：";
                    codeName = "收票明细：";
                    objName = BusinessTypeEnum.结算收票申请.getName();
                }else{
                    amountName = "合并收票金额：";
                    codeName = "合并收票明细：";
                    objName = BusinessTypeEnum.合并结算收票.getName();
                }
                String finalObjName1 = objName;
                normalLog(contractId.toString(),finalObjName1,amountName,amount.toString(),codeName, StringUtils.join(code, ","));
            }
        }
    }

    /**
     *收付款日志方法
     */
    public void normalLog(String contractId,String finalObjName,String amountName,String amount,String codeName,String code) {
        BizLog.normal(contractId, new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return finalObjName;
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem(amountName + amount, true))
                        .option(new UrlLinkDataItem(codeName+code, false, ""))
        );
    }

    /**
     *
     * @param paymentType 0有票付款  1无票付款
     */
    public ContractPaymentDetailE paymentInvoicePaymentDetail(CollectionPlanPaymentInvoiceF from, IdentityInfo identityInfo, Integer paymentType) {
        // 付款计划更新
        collectionPlanUpdate(from.getCollectionPlanId(), from.getPaymentAmount());
        // 付款明细
        ContractPaymentDetailF contractPaymentDetailF = new ContractPaymentDetailF();
        contractPaymentDetailF.setPaymentAmount(from.getPaymentAmount());
        contractPaymentDetailF.setAuditStatus(0);
        contractPaymentDetailF.setContractId(from.getContractId());
        contractPaymentDetailF.setCollectionPlanId(from.getCollectionPlanId());
        contractPaymentDetailF.setApplyPaymentTime(LocalDateTime.now());
        contractPaymentDetailF.setUserId(identityInfo.getUserId());
        contractPaymentDetailF.setUserName(identityInfo.getUserName());
        contractPaymentDetailF.setRemark(from.getRemark());
        contractPaymentDetailF.setPaymentType(paymentType);
        contractPaymentDetailF.setPaymentMethod(from.getPaymentMethod());
        return contractPaymentDetailAppService.saveContractPaymentDetail(contractPaymentDetailF);
    }

    public Boolean saveOverdueStatement(CollectionPlanOverdueStatementF from) {
        return contractCollectionPlanService.saveOverdueStatement(from);
    }

    public List<ContractCollectionPlanStatisticsV> selectCollectionPlanStatistics(Integer contractNature, String year) {
        return contractCollectionPlanService.selectCollectionPlanStatistics(contractNature, year, curIdentityInfo().getTenantId());
    }
}
