package com.wishare.contract.apps.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.action.DefaultAction;
import com.wishare.bizlog.content.BusinessDataItem;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.content.UrlLinkDataItem;
import com.wishare.bizlog.entity.BizLogger;
import com.wishare.bizlog.entity.BizObject;
import com.wishare.bizlog.logger.BizLogConfiguration;
import com.wishare.bizlog.logger.DefaultBizLoggerExecutor;
import com.wishare.bizlog.operator.Operator;
import com.wishare.bizlog.plugins.BizLogInterceptor;
import com.wishare.bizlog.plugins.dynamictable.DynamicLoggerTableInterceptor;
import com.wishare.bizlog.repository.BizLogRepository;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.fo.ApproveBatchTemporaryChargeBillRf;
import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.apps.vo.contractset.ContractCategoryDetailV;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.BusinessTypeEnum;
import com.wishare.contract.domains.enums.LogActionEnum;
import com.wishare.contract.domains.enums.revision.RevTypeEnum;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.contractset.ContractProfitLossPlanService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//import com.wishare.contract.apps.remote.clients.FinanceTemporaryFeignClient;

/**
 * <p>
 * 合同订立信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Service
@Slf4j
public class ContractConcludeAppService implements IOwlApiBase{

    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionPlanAppService collectionPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractCategoryAppService contractCategoryAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanAppService bondPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanAppService profitLossPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractTemplateAppService contractTemplateAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeService contractIncomeConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionDetailAppService contractCollectionDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceDetailAppService contractInvoiceDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractPaymentDetailAppService contractPaymentDetailAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanService contractProfitLossPlanService;
    @Setter(onMethod_ = {@Autowired})
    private ContractSpaceResourcesAppService resourcesAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractBondPlanAppService contractBondPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractReceiveInvoiceDetailAppService contractReceiveInvoiceDetailAppService;
//    @Setter(onMethod_ = {@Autowired})
//    private BizLog bizLog;
    @Setter(onMethod_ = {@Autowired})
    private ContractOrgCommonService contractOrgCommonService;

    public ContractDetailsV getContractConclude(Long id) {
        //查询合同基础信息+收款计划+保证金计划
        ContractDetailsV contractConcludeV = contractConcludeService.getContractConclude(id);
        if (contractConcludeV != null) {
            //项目名称+租户名称
            if (null != orgFeignClient.getById(contractConcludeV.getTenantId())) {
                contractConcludeV.setTenantName(orgFeignClient.getById(contractConcludeV.getTenantId()).getName());
            }
            if (contractConcludeV.getCommunityId().length() < 20) {
                if (null != orgFeignClient.getByFinanceId(Long.parseLong(contractConcludeV.getCommunityId()))) {
                    contractConcludeV.setCommunityName(orgFeignClient.getByFinanceId(Long.parseLong(contractConcludeV.getCommunityId())).getNameCn());
                }
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.INCOME)) {
                //收入类合同，甲方为客商，乙方法定单位
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.queryById(contractConcludeV.getPartyAId()).getName());
                }
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyBId()).getNameCn());
                }
            }
            if (contractConcludeV.getContractNature().equals(ContractSetConst.PAY)) {
                //支出类合同，甲方为组织，乙方客商
                if (null != orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId())) {
                    contractConcludeV.setPartyAName(orgFeignClient.getByFinanceId(contractConcludeV.getPartyAId()).getNameCn());
                }
                if (null != orgFeignClient.queryById(contractConcludeV.getPartyBId())) {
                    contractConcludeV.setPartyBName(orgFeignClient.queryById(contractConcludeV.getPartyBId()).getName());
                }
            }
            //丙方名称(客商)
            if (contractConcludeV.getPartyCId() != null) {
                contractConcludeV.setPartyCName(orgFeignClient.queryById(contractConcludeV.getPartyCId()).getName());
            }
            if (contractConcludeV.getTempId() != null) {
                //合同范本
                if (null != contractTemplateAppService.queryById(contractConcludeV.getTempId())) {
                    contractConcludeV.setTempName(contractTemplateAppService.queryById(contractConcludeV.getTempId()).getName());
                }
            }
            //合同费项
            if (null != financeFeignClient.chargeName(contractConcludeV.getChargeItemId())) {
                contractConcludeV.setChargeItemName(financeFeignClient.chargeName(contractConcludeV.getChargeItemId()));
            }
            //合同分类
            ContractCategoryDetailV contractCategoryDetailV = contractCategoryAppService.queryById(contractConcludeV.getCategoryId());
            if (null != contractCategoryDetailV) {
                contractConcludeV.setCategoryName(contractCategoryDetailV.getName());
                contractConcludeV.setCategoryPath(contractCategoryDetailV.getPathList());
                //  查询合同分类  业务类型
                contractConcludeV.setNatureTypeId(contractCategoryDetailV.getNatureTypeId());
                contractConcludeV.setNatureTypeCode(contractCategoryDetailV.getNatureTypeCode());
                contractConcludeV.setNatureTypeName(contractCategoryDetailV.getNatureTypeName());
            }
            //关联收入/支出合同
            if (StringUtils.hasText(contractConcludeV.getIncomeContract())) {
                contractConcludeV.setIncomeContractList(contractName(contractConcludeV.getIncomeContract()));
            }
            if (StringUtils.hasText(contractConcludeV.getExpenditureContract())) {
                contractConcludeV.setExpenditureContractList(contractName(contractConcludeV.getExpenditureContract()));
            }
            //关联虚拟合同
            if (contractConcludeV.getRelationContract() != null) {
                contractConcludeV.setRelationContractName(
                        contractConcludeService.getById(contractConcludeV.getRelationContract()).getName());
            }
            if(contractConcludeV.getOriginalContractId() != null){
                contractConcludeV.setOriginalContractName(contractConcludeService.getContractConclude(contractConcludeV.getOriginalContractId()).getName());
            }
            //收款计划
            ContractCollectionPlanF collectionPlanF = new ContractCollectionPlanF();
            collectionPlanF.setContractId(id);
            List<ContractCollectionPlanV> contractCollectionPlanVS = collectionPlanAppService.listContractCollectionPlan(collectionPlanF);
            contractConcludeV.setCollectionPlanVList(contractCollectionPlanVS);
            if (Objects.nonNull(contractCollectionPlanVS) && !contractCollectionPlanVS.isEmpty()) {
                contractConcludeV.getCollectionPlanVList().forEach(item -> {
                    String taxRateId = item.getTaxRateIdPath();
                    if (Strings.isNotBlank(taxRateId)) {
                        item.setTaxRateIdList(Arrays.stream(taxRateId.split(",")).map(Long::valueOf).collect(Collectors.toList()));
                    }
                    BigDecimal exchangeRate = new BigDecimal(contractConcludeV.getExchangeRate());
                    // 减免金额原币  本币 / 汇率
                    if(item.getCreditAmount() != null && exchangeRate != null){
                        BigDecimal creditAmount = item.getCreditAmount().divide(exchangeRate,2, RoundingMode.HALF_UP);
                        item.setPlannedCollectionAmount(item.getPlannedCollectionAmount().add(creditAmount));
                    }
                    if(item.getLocalCurrencyAmount() != null && item.getCreditAmount() != null){
                        item.setLocalCurrencyAmount(item.getLocalCurrencyAmount().add(item.getCreditAmount()));
                    }
                    if(item.getTaxExcludedAmount() != null && item.getCreditAmount() != null){
                        item.setTaxExcludedAmount(item.getTaxExcludedAmount().add(item.getCreditAmount()));
                    }
                });
            }
            //保证金计划
            ContractBondPlanF bondPlanF = new ContractBondPlanF();
            bondPlanF.setContractId(id);
            contractConcludeV.setContractBondPlanV(bondPlanAppService.listContractBondPlan(bondPlanF));
            //损益计划
            ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
            profitLossPlanF.setContractId(id);
            contractConcludeV.setProfitLossPlanList(profitLossPlanAppService.listProfitLossPlan(profitLossPlanF));
            ContractCollectionPlanF contractCollectionPlanF = new ContractCollectionPlanF();
            contractCollectionPlanF.setContractId(id);
            List<ContractCollectionPlanV> collectionPlanVS = collectionPlanAppService.listContractCollectionPlanSimple(contractCollectionPlanF);
            //  已收/付款金额总和
            BigDecimal totalPaymentAmount = collectionPlanVS.stream().map(ContractCollectionPlanV::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            contractConcludeV.setTotalPaymentAmount(totalPaymentAmount);
            //  已开票/收票金额总和
            BigDecimal totalInvoiceAmount = collectionPlanVS.stream().map(ContractCollectionPlanV::getInvoiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            contractConcludeV.setTotalInvoiceAmount(totalInvoiceAmount);
            if (contractConcludeV.getContractNature() == 1) {
                // 收款明细列表
                contractConcludeV.setCollectionDetailPlanVList(contractCollectionDetailAppService.contractCollectionDetailList(id, null));
                // 开票明细列表
                contractConcludeV.setInvoiceDetailPlanVList(contractInvoiceDetailAppService.contractInvoiceDetailList(id, null));
            } else {
                // 付款明细列表
                contractConcludeV.setContractPaymentDetailVList(contractPaymentDetailAppService.contractPaymentDetailList(id, null));
                // 收票明细列表
                contractConcludeV.setReceiveInvoiceDetailVList(contractReceiveInvoiceDetailAppService.contractReceiveInvoiceDetailList(id, null));
            }
            //空间资源列表
            ContractSpaceResourcesF spaceResourcesF = new ContractSpaceResourcesF();
            spaceResourcesF.setContractId(id);
            contractConcludeV.setSpaceResourcesV(resourcesAppService.list(spaceResourcesF));
        }
        return contractConcludeV;
    }

    public List<ContractNameV> contractName(String contractId) {
        List<ContractNameV> incomeContractList = new ArrayList<>();
        String[] incomeContract = contractId.split(",");
        for (String s : incomeContract) {
            ContractNameV contractNameV = new ContractNameV();
            contractNameV.setId(Long.parseLong(s));
            contractNameV.setName(contractConcludeService.getContractConclude(Long.parseLong(s)).getName());
            incomeContractList.add(contractNameV);
        }
        return incomeContractList;
    }

    public List<ConcludeInfoV> listContractConclude(ContractConcludeF contractConcludeF) {
        return contractConcludeService.listContractConclude(contractConcludeF);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Long saveContractConclude(ContractConcludeSaveF contractConcludeF) {
        //子合同添加，需要备份主合同
        backupsContract(contractConcludeF);
        if(!org.apache.commons.lang3.StringUtils.isNotBlank(contractConcludeF.getContractNo())){
            //新增合同基础信息
            contractConcludeF.setContractNo(contractCode(contractConcludeF.getTenantId(), contractConcludeF.getPid(), contractConcludeF.getContractNature()));
        }
        //查询所属部门组织信息
        this.fillBelongOrgInfo(contractConcludeF);
        Long id = contractConcludeService.saveContractConclude(contractConcludeF);
        updateWarnState(contractConcludeF.getTenantId(), id);
        saveContractLog(contractConcludeF,id);
        //新增空间资源
        for (ContractSpaceResourcesSaveF contractSpaceResourcesSaveF : contractConcludeF.getContractSpaceResourcesF()) {
            contractSpaceResourcesSaveF.setContractId(id);
        }
        resourcesAppService.save(contractConcludeF.getContractSpaceResourcesF());
        if (null != contractConcludeF.getRelationContract()) {
            //如果关联虚拟合同，虚拟合同的父级变更为当前合同的id
            ContractConcludeUpdateF concludeUpdateF = new ContractConcludeUpdateF();
            concludeUpdateF.setId(contractConcludeF.getRelationContract());
            concludeUpdateF.setPid(id);
            concludeUpdateF.setSigningMethod(ContractSetConst.SUB_CONTRACT);
            contractConcludeService.updateContractConclude(concludeUpdateF);
        }
        //  招投标保证金账单设置引用
        if (contractConcludeF.getBidBond()) {
            if (!reference(contractConcludeF.getBidBondBillId(), 1)) {
                throw BizException.throw400("设置关联招投标保证金失败，新增合同失败");
            }
        }
        return id;
    }

    /**
     * 填充所属部门信息
     * @param form
     */
    public void fillBelongOrgInfo(ContractConcludeSaveF form) {
        if(StringUtils.isEmpty(form.getHandlerId()) && !isAdminCurUser()) {
            form.setHandlerId(userId());
            form.setHandledBy(userName());
        }
        try{
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.valueOf(form.getBelongOrgId()))).ifPresent(v -> {
                form.setBelongOrgName(v.getOrgName());
                form.setBelongOrgPath(v.getStandardOrgPath());
            });
        }catch (Exception e) {
            throw new OwlBizException("查询所属部门信息失败!");
        }

    }

    public void saveContractLog(ContractConcludeSaveF contractConcludeF,Long id) {
        Long contract = id;
        String objName = BusinessTypeEnum.合同.getName();
        String title = "";
        LogActionEnum action = LogActionEnum.新增;
        if(contractConcludeF.getSigningMethod().equals(ContractSetConst.SUPPLEMENT)){
            action = LogActionEnum.发起;
            title = "新增了补充协议：";
            objName = BusinessTypeEnum.补充申请.getName();
            contract = contractConcludeF.getPid();
        }
        if(contractConcludeF.getSigningMethod().equals(ContractSetConst.RENEWAL)){
            action = LogActionEnum.发起;
            title = "新增了续签合同：";
            objName = BusinessTypeEnum.续签申请.getName();
            contract = contractConcludeF.getOriginalContractId();
        }
        if(contractConcludeF.getSigningMethod().equals(ContractSetConst.STOP)){
            action = LogActionEnum.发起;
            title = "新增了终止协议：";
            objName = BusinessTypeEnum.协议终止申请.getName();
            contract = contractConcludeF.getPid();
        }
        if(contractConcludeF.getSigningMethod().equals(ContractSetConst.SUB_CONTRACT)){
            action = LogActionEnum.发起;
            title = "新增了子合同：";
            objName = BusinessTypeEnum.子合同申请.getName();
            contract = contractConcludeF.getPid();
        }
        if(contractConcludeF.getSigningMethod().equals(ContractSetConst.KILL_CONTRACT)){
            action = LogActionEnum.发起;
            objName = BusinessTypeEnum.强制终止申请.getName();
            contract = contractConcludeF.getPid();
        }
        String finalObjName = objName;
        BizLog.normal(contract.toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return finalObjName;
                    }
                },
                action,
                new Content().option(new PlainTextDataItem(title+contractConcludeF.getName(), false)));
    }

    /**
     * 新增合同-备份合同
     */
    public void backupsContract(ContractConcludeSaveF contractConcludeF) {
        if (contractConcludeF.getPid() != null && contractConcludeF.getPid() !=0) {
            ContractConcludeF concludeF = new ContractConcludeF();
            concludeF.setPid(contractConcludeF.getPid());
            concludeF.setContractState(ContractSetConst.CHANGE);
            List<ConcludeInfoV> concludeInfos = contractConcludeService.listContractConclude(concludeF);
            if (concludeInfos.size() == 0) {
                //该合同下没有已变更状态的合同，如果没有，查询合同信息，新增一份数据
                ContractDetailsV contractDetailsV = contractConcludeService.selectById(contractConcludeF.getPid());
                ContractConcludeSaveF concludeSaveF = Global.mapperFacade.map(contractDetailsV, ContractConcludeSaveF.class);
                concludeSaveF.setContractState(ContractSetConst.CHANGE);
                concludeSaveF.setReviewStatus(ContractSetConst.POSS);
                concludeSaveF.setPid(contractConcludeF.getPid());
                concludeSaveF.setDeleted(1);
                Long contractId = contractConcludeService.saveContract(concludeSaveF);
                //新增收付款计划
                ContractCollectionPlanF contractCollectionPlanF = new ContractCollectionPlanF();
                contractCollectionPlanF.setContractId(contractConcludeF.getPid());
                List<ContractCollectionPlanV> collectionPlanVList = collectionPlanAppService.listContractCollectionPlan(contractCollectionPlanF);
                //新增主合同收付款计划
                List<ContractCollectionPlanSaveF> planSaveF = Global.mapperFacade.mapAsList(collectionPlanVList, ContractCollectionPlanSaveF.class);
                for (ContractCollectionPlanSaveF contractCollectionPlanSaveF : planSaveF) {
                    //新增收付款计划
                    contractCollectionPlanSaveF.setContractId(contractId);
                    contractCollectionPlanSaveF.setContractNature(contractConcludeF.getContractNature());
                    collectionPlanAppService.collectionPlanSave(contractCollectionPlanSaveF);
                    //生成损益计划
                    ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
                    profitLossPlanF.setContractId(contractConcludeF.getPid());
                    profitLossPlanF.setDeleted(0);
                    List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
                    for (ContractProfitLossPlanV profitLossPlanV : profitLossPlanVS) {
                        ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(profitLossPlanV, ContractProfitLossPlanSaveF.class);
//                        contractProfitLossPlanF.setCollectionPlanId(collectionPlanId);
                        contractProfitLossPlanF.setContractId(contractId);
                        contractProfitLossPlanF.setContractNature(contractConcludeF.getContractNature());
                        contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
                    }
                }
                //新增保证金计划
                ContractBondPlanF contractBondPlanF = new ContractBondPlanF();
                contractBondPlanF.setContractId(contractConcludeF.getPid());
                List<ContractBondPlanV> bondPlanVList = bondPlanAppService.listContractBondPlan(contractBondPlanF);
                if (!CollectionUtils.isEmpty(bondPlanVList)) {
                    //新增主合同保证金计划
                    List<ContractBondPlanSaveF> bondPlanSave = Global.mapperFacade.mapAsList(bondPlanVList, ContractBondPlanSaveF.class);
                    for (ContractBondPlanSaveF bondPlanSaveF : bondPlanSave) {
                        bondPlanSaveF.setContractId(contractId);
                        bondPlanAppService.save(bondPlanSaveF);
                    }
                }
                //新增空间资源
                ContractSpaceResourcesF contractSpaceResourcesF = new ContractSpaceResourcesF();
                contractSpaceResourcesF.setContractId(contractConcludeF.getPid());
                List<ContractSpaceResourcesV> resourcesList = resourcesAppService.list(contractSpaceResourcesF);
                if (!CollectionUtils.isEmpty(resourcesList)) {
                    List<ContractSpaceResourcesSaveF> resourcesSave = Global.mapperFacade.mapAsList(resourcesList, ContractSpaceResourcesSaveF.class);
                    for (ContractSpaceResourcesSaveF contractSpaceResourcesSaveF : resourcesSave) {
                        contractSpaceResourcesSaveF.setContractId(contractId);
                    }
                    resourcesAppService.save(resourcesSave);
                }
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void updateContractConclude(ContractConcludeUpdateF contractConcludeF) {
        ContractDetailsV contractDetailsV = contractConcludeService.selectById(contractConcludeF.getId());
        //  招投标保证金取消关联  1.编辑前有招投标保证金，编辑后去除  2.编辑前有招投标保证金，编辑后变更
        if (Objects.nonNull(contractDetailsV) && contractDetailsV.getBidBond()) {
            if (!contractConcludeF.getBidBond() || (contractConcludeF.getBidBondBillId().compareTo(contractDetailsV.getBidBondBillId()) != 0)) {
                if (!reference(contractDetailsV.getBidBondBillId(), 0)) {
                    throw BizException.throw400("设置取消关联招投标保证金失败，更新合同失败");
                }
            }
        }
        //查询所属部门组织信息
        this.fillEditBelongOrgInfo(contractConcludeF);
        if(!org.apache.commons.lang3.StringUtils.isNotBlank(contractConcludeF.getContractNo())){
            //新增合同基础信息
            contractConcludeF.setContractNo(contractCode(contractConcludeF.getTenantId(), contractConcludeF.getPid(), contractConcludeF.getContractNature()));
        }
        contractConcludeService.updateContractConclude(contractConcludeF);
        updateWarnState(contractConcludeF.getTenantId(), contractConcludeF.getId());
        BizLog.normal(contractConcludeF.getId().toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.合同.getName();
                    }
                },
                LogActionEnum.修改,
                new Content().option(new PlainTextDataItem(contractConcludeF.getName(), false)));
        //更新空间资源
        resourcesAppService.deleteByContractId(contractConcludeF.getId());
        resourcesAppService.save(contractConcludeF.getContractSpaceResourcesF());
        if (null != contractConcludeF.getRelationContract()) {
            //如果关联虚拟合同，虚拟合同的父级变更为当前合同的id
            ContractConcludeUpdateF concludeUpdateF = new ContractConcludeUpdateF();
            concludeUpdateF.setId(contractConcludeF.getRelationContract());
            concludeUpdateF.setPid(contractConcludeF.getId());
            contractConcludeService.updateContractConclude(concludeUpdateF);
        }
    }

    /**
     * 填充所属部门信息
     * @param form
     */
    public void fillEditBelongOrgInfo(ContractConcludeUpdateF form) {
        if(StringUtils.isEmpty(form.getHandlerId()) && !isAdminCurUser()) {
            form.setHandlerId(userId());
            form.setHandledBy(userName());
        }
        try{
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.valueOf(form.getBelongOrgId()))).ifPresent(v -> {
                form.setBelongOrgName(v.getOrgName());
                form.setBelongOrgPath(v.getStandardOrgPath());
            });
        }catch (Exception e) {
            throw new OwlBizException("查询所属部门信息失败!");
        }

    }

    @Transactional(rollbackFor = {Exception.class})
    public void removeContractConclude(Long id) {
        //  招投标保证金取消关联
        ContractDetailsV contractConclude = contractConcludeService.getContractConclude(id);
        if (Objects.nonNull(contractConclude) && contractConclude.getBidBond()) {
            if (!reference(contractConclude.getBidBondBillId(), 0)) {
                throw BizException.throw400("设置取消关联招投标保证金失败，删除合同失败");
            }
        }
        //删除合同需要删除对应的收款计划+保证金计划+损益计划
        contractConcludeService.removeContractConclude(id);
        BizLog.normal(id.toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.合同.getName();
                    }
                },
                LogActionEnum.删除,
                new Content().option(new PlainTextDataItem(contractConclude.getName(), false)));
        collectionPlanAppService.deleteCollectionPlan(id);
        bondPlanAppService.deleteBondPlan(id);
        profitLossPlanAppService.deleteProfitLossPlan(null, id);
        // 删除收款、付款、开票明细
        contractCollectionDetailAppService.deleteByContractId(id);
        contractInvoiceDetailAppService.deleteByContractId(id);
        contractPaymentDetailAppService.deleteByContractId(id);
    }

    public PageV<ContractConcludeV> pageContractConcludePage(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        PageV<ContractConcludeV> concludeVPageV = contractConcludeService.pageContractConcludePage(request, tenantId);
        return concludeVPageV;
    }

    public String contractCode(String tenantId, Long pid, Integer contractNature) {
        String code = null;
        //生成合同编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;子合同编号规则：主合同编号+两位数数值，如YYHT220816000101
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Integer count = contractConcludeService.selectContractCount(pid,tenantId);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        String nature = "";
        if (contractNature.equals(ContractSetConst.INCOME)) {
            nature = "SR";
        }
        if (contractNature.equals(ContractSetConst.PAY)) {
            nature = "ZC";
        }
        if (pid == 0) {
            //主合同编号
            String code1 = String.format("%0" + 4 + "d", count + 1);
            code = year + code1;
            if (null != tenantInfoRv && StringUtils.hasText(tenantInfoRv.getEnglishName())) {
                code = tenantInfoRv.getEnglishName() + nature + year + code1;
            } else {
                code = nature + year + code1;
            }
        } else {
            //子合同编号
            ContractDetailsV contractConcludeV = contractConcludeService.getContractConclude(pid);
            String code2 = String.format("%0" + 2 + "d", count + 1);
            code = contractConcludeV.getContractNo() + code2;
        }
        return code;
    }

    /**
     * 合同改版生成合同编号
     * @param tenantId 租户ID
     * @param pid 父级ID
     * @param contractNature 支出 or 收入合同
     * @return
     */
    public String revContractCode(String tenantId, String pid, Integer contractNature) {
        String code = null;
        //生成合同编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;子合同编号规则：主合同编号+两位数数值，如YYHT220816000101
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());


        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        String nature = "";

        Integer count = 0;
        if (contractNature.equals(RevTypeEnum.收入合同.getCode())) {
            nature = "SR";
            QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, tenantId)
                    .ge(ContractIncomeConcludeE.GMT_CREATE, LocalDate.now().atStartOfDay());
            List<ContractIncomeConcludeE> list = contractIncomeConcludeService.list(queryWrapper);
            count = list.size();
        }
        if (contractNature.equals(RevTypeEnum.支出合同.getCode())) {
            nature = "ZC";
            QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId)
                    .ge(ContractPayConcludeE.GMT_CREATE, LocalDate.now().atStartOfDay());
            List<ContractPayConcludeE> list = contractPayConcludeService.list(queryWrapper);
            count = list.size();
        }

        if (org.apache.commons.lang.StringUtils.isBlank(pid)) {
            //主合同编号
            String code1 = String.format("%0" + 4 + "d", count + 1);
            if (null != tenantInfoRv && StringUtils.hasText(tenantInfoRv.getEnglishName())) {
                code = tenantInfoRv.getEnglishName() + nature + year + code1;
            } else {
                code = nature + year + code1;
            }
        } else {
            if (contractNature.equals(RevTypeEnum.收入合同.getCode())) {
                ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(pid);
                if (Objects.isNull(concludeE)) {
                    throw new OwlBizException("根据父级合同ID检索数据失败");
                }
                String code2 = String.format("%0" + 2 + "d", count + 1);
                code = concludeE.getContractNo() + code2;
            }
            if (contractNature.equals(RevTypeEnum.支出合同.getCode())) {
                ContractPayConcludeE concludeE = contractPayConcludeService.getById(pid);
                if (Objects.isNull(concludeE)) {
                    throw new OwlBizException("根据父级合同ID检索数据失败");
                }
                String code2 = String.format("%0" + 2 + "d", count + 1);
                code = concludeE.getContractNo() + code2;
            }

        }
        return code;
    }

    public ContractConcludeSumV amountSum(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        return contractConcludeService.amountSum(request, tenantId);
    }

    public void contractApprove(Long id) {
        ContractDetailsV contractConclude = getContractConclude(id);
        if (!contractConclude.getReviewStatus().equals(ContractSetConst.NOT_POSS)){
            throw BizException.throw400("该合同审核状态不是未提交,无法提交审核");
        }
        contractConcludeService.contractApprove(contractConclude);
    }


    @Transactional(rollbackFor = {Exception.class})
    public void contractState(Long id, Integer contractState, Integer reviewStatus) throws ParseException {
        ContractDetailsV contractDetailsV = contractConcludeService.getContractConclude(id);
        if (contractDetailsV.getBond()) {
            ContractBondPlanF contractBondPlanF = new ContractBondPlanF();
            contractBondPlanF.setContractId(id);
            List<ContractBondPlanV> bondPlanVList = bondPlanAppService.listContractBondPlan(contractBondPlanF);
            if (CollectionUtils.isEmpty(bondPlanVList)) {
                throw BizException.throw402("提交不通过，您还有保证金计划未填写！");
            }
        }
        contractConcludeService.contractState(id, contractState, reviewStatus);
        if(contractDetailsV.getPid() != null && contractDetailsV.getPid() != 0L){
            //备份原合同
            contractConcludeService.contractBackups(contractDetailsV.getPid());
        }
        updateWarnState(null, id);
        ContractCollectionPlanF contractCollectionPlanF = new ContractCollectionPlanF();
        contractCollectionPlanF.setContractId(id);
        List<ContractCollectionPlanV> collectionPlans = collectionPlanAppService.collectionPlanList(contractCollectionPlanF);
        if(collectionPlans != null){
            for (ContractCollectionPlanV contractCollectionPlanV : collectionPlans) {
                collectionPlanAppService.updateWarnState(null,null,contractCollectionPlanV.getId(),null);
            }
        }
        //只有提交时，才会操作账单和变更合同信息
        if ((contractState.equals(ContractSetConst.PERFORM) || contractState.equals(ContractSetConst.STOP_CONTRACT)) && reviewStatus.equals(ContractSetConst.POSS)) {
            //提交时如果是补充协议和终止协议，则更新主合同基础信息
            if (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                    ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) {
                if (ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod())) {
                    contractState = ContractSetConst.STOP_CONTRACT;
                }
                updatePlan(id, contractState, contractDetailsV);
            }
            //查询收付款计划，针对收款合同
            if (contractDetailsV.getContractNature().equals(ContractSetConst.INCOME)) {
                //新增应收账单(补充协议和终止协议不推送账单)
                if (contractDetailsV.getSigningMethod() != null && (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                        ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod()))) {
                    //删除财务中台收款计划账单(true 删除主合同账单)
                    collectionPlanAppService.receivableDelete(contractDetailsV.getPid(), false);
                    if(!(contractDetailsV.getNatureTypeCode() != null && contractDetailsV.getNatureTypeCode().contains("工程服务"))){
                        //新增应收账单
                        profitLossPlanAppService.profitLossPlanCreateBill(null, contractDetailsV.getPid(), null, true, null);
                    }
                } else {
                    if(!(contractDetailsV.getNatureTypeCode() != null && contractDetailsV.getNatureTypeCode().contains("工程服务"))){
                        profitLossPlanAppService.profitLossPlanCreateBill(null, id, null, true, null);
                    }
                }
            }
            //查询收付款计划,针对支付合同
            if (contractDetailsV.getContractNature().equals(ContractSetConst.PAY)) {
                //新增支付合同账单(补充协议和终止协议不推送账单)
                if (contractDetailsV.getSigningMethod() != null && (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                        ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod()))) {
                    //删除财务中台付款计划账单(true 删除主合同账单)
                    collectionPlanAppService.receivableDelete(contractDetailsV.getPid(), true);
                    if(!(contractDetailsV.getNatureTypeCode() != null && contractDetailsV.getNatureTypeCode().contains("工程服务"))){
                        //新增支付账单
                        profitLossPlanAppService.profitLossPlanCreateBill(null, contractDetailsV.getPid(), null, false, null);
                    }
                } else {
                    if(!(contractDetailsV.getNatureTypeCode() != null && contractDetailsV.getNatureTypeCode().contains("工程服务"))){
                        profitLossPlanAppService.profitLossPlanCreateBill(null, id, null, false, null);
                    }
                }
            }
            // 推送临时账单 收取类直接推，缴纳类付款时推送
            if (contractDetailsV.getBond() && contractDetailsV.getBondType() == 0) {
                // 补充协议和终止协议先删除主合同临时账单，再推送
                if (contractDetailsV.getSigningMethod() != null && (ContractSetConst.SUPPLEMENT.equals(contractDetailsV.getSigningMethod()) ||
                        ContractSetConst.STOP.equals(contractDetailsV.getSigningMethod()))) {
                    // 主合同
                    ContractDetailsV parentContract = contractConcludeService.getContractConclude(contractDetailsV.getPid());
                    // 查询主合同收取类未收款未开票保证金账单id  // 不删招投标保证金账单
                    List<Long> billIds = bondPlanAppService.contractBondPlanBillIds(contractDetailsV.getPid(), 0, 0, 0, 0);
                    List<Long> billIdList = billIds.stream().filter(item -> item != 0L).collect(Collectors.toList());
                    if (!billIdList.isEmpty()) {
                        bondPlanAppService.deleteBatch(billIdList);
                    }
                    // 推送主合同临时账单
                    if (Objects.nonNull(parentContract)) {
                        pushTemporaryBill(parentContract);
                    }
                } else {
                    // 推送当前合同账单
                    pushTemporaryBill(contractDetailsV);
                }
            }
        }
        BizLog.normal(id.toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.合同.getName();
                    }
                },
                LogActionEnum.提交,
                new Content().option(new PlainTextDataItem(contractDetailsV.getName(), false)));
    }

    /**
     * 补充和终止协议时更改主合同收付款计划和保证金计划
     */
    public void updatePlan(Long id, Integer contractState, ContractDetailsV contractDetailsV) {
        ContractConcludeUpdateF concludeUpdateF = Global.mapperFacade.map(contractDetailsV, ContractConcludeUpdateF.class);
        concludeUpdateF.setId(contractDetailsV.getPid());
        concludeUpdateF.setPid(0L);
        concludeUpdateF.setSigningMethod(ContractSetConst.NEW_SIGNATURE);
        concludeUpdateF.setContractState(ContractSetConst.PERFORM);
        if (contractState.equals(ContractSetConst.STOP_CONTRACT)) {
            concludeUpdateF.setContractState(ContractSetConst.STOP_CONTRACT);
        }
        concludeUpdateF.setReviewStatus(ContractSetConst.POSS);
        contractConcludeService.updateContractConclude(concludeUpdateF);
        updateWarnState(contractDetailsV.getTenantId(), contractDetailsV.getPid());
        //补充或终止合同 更新主合同的收付款计划(部分收款和已收款的计划不更新)
        collectionPlanAppService.deleteCollectionPlan(contractDetailsV.getPid());
        //删除损益计划，重新生成
        profitLossPlanAppService.deleteProfitLossPlan(null, contractDetailsV.getPid());
        //查询合同下的收付款计划
        ContractCollectionPlanF contractCollectionPlanF = new ContractCollectionPlanF();
        //查询补充或终止协议的收付款计划
        contractCollectionPlanF.setContractId(id);
        List<ContractCollectionPlanV> collectionPlanVList = collectionPlanAppService.collectionPlanList(contractCollectionPlanF);
        //新增主合同收付款计划
        List<ContractCollectionPlanSaveF> planSaveF = Global.mapperFacade.mapAsList(collectionPlanVList, ContractCollectionPlanSaveF.class);
        for (ContractCollectionPlanSaveF contractCollectionPlanSaveF : planSaveF) {
            //新增收付款计划
            contractCollectionPlanSaveF.setContractId(contractDetailsV.getPid());
            Long collectionPlanId = collectionPlanAppService.collectionPlanSave(contractCollectionPlanSaveF);
            collectionPlanAppService.updateWarnState(contractDetailsV.getPid(),contractDetailsV.getTenantId(),collectionPlanId,contractDetailsV.getContractNature());
        }
        //生成损益计划
        ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
        profitLossPlanF.setContractId(id);
        profitLossPlanF.setDeleted(0);
        profitLossPlanF.setSetPaymentStatus(0);
        List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
        if(!CollectionUtils.isEmpty(profitLossPlanVS)){
            for (ContractProfitLossPlanV profitLossPlanV : profitLossPlanVS) {
                ContractProfitLossPlanSaveF contractProfitLossPlanF = Global.mapperFacade.map(profitLossPlanV, ContractProfitLossPlanSaveF.class);
                contractProfitLossPlanF.setContractId(contractDetailsV.getPid());
                contractProfitLossPlanF.setContractNature(contractDetailsV.getContractNature());
                contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
            }
        }
        //补充或终止合同删除保证金
        bondPlanAppService.deleteBondPlan(contractDetailsV.getPid());
        //查询合同下的保证金计划
        ContractBondPlanF contractBondPlanF = new ContractBondPlanF();
        contractBondPlanF.setContractId(id);
        List<ContractBondPlanV> bondPlanVList = bondPlanAppService.listContractBondPlan(contractBondPlanF);
        if (!CollectionUtils.isEmpty(bondPlanVList)) {
            //新增主合同保证金计划
            List<ContractBondPlanSaveF> bondPlanSave = Global.mapperFacade.mapAsList(bondPlanVList, ContractBondPlanSaveF.class);
            for (ContractBondPlanSaveF bondPlanSaveF : bondPlanSave) {
                bondPlanSaveF.setContractId(contractDetailsV.getPid());
                bondPlanAppService.save(bondPlanSaveF);
            }
        }
        //更新空间资源
        resourcesAppService.deleteByContractId(contractDetailsV.getPid());
        //新增空间资源
        ContractSpaceResourcesF contractSpaceResourcesF = new ContractSpaceResourcesF();
        contractSpaceResourcesF.setContractId(id);
        List<ContractSpaceResourcesV> resourcesList = resourcesAppService.list(contractSpaceResourcesF);
        if (!CollectionUtils.isEmpty(resourcesList)) {
            List<ContractSpaceResourcesSaveF> resourcesSave = Global.mapperFacade.mapAsList(resourcesList, ContractSpaceResourcesSaveF.class);
            for (ContractSpaceResourcesSaveF contractSpaceResourcesSaveF : resourcesSave) {
                contractSpaceResourcesSaveF.setContractId(contractDetailsV.getPid());
            }
            resourcesAppService.save(resourcesSave);
        }
    }

    /**
     * 推送中台临时账单
     */
    public void pushTemporaryBill(ContractDetailsV contractDetailsV) {
        // 先获取此合同下所有保证金计划，用于推送
        ContractBondPlanF contractBondPlanF1 = new ContractBondPlanF();
        contractBondPlanF1.setContractId(contractDetailsV.getId());
        List<ContractBondPlanV> addTemporaryChargeBillFs = contractBondPlanAppService.listContractBondPlan(contractBondPlanF1);
        // 批量新增临时账单
        List<TemporaryChargeBillPageV> temporaryChargeBillPageVS = contractBondPlanAppService.temporaryAddBatch(addTemporaryChargeBillFs, contractDetailsV);
        // 审核账单
        ApproveBatchTemporaryChargeBillRf temporaryChargeBillRf = new ApproveBatchTemporaryChargeBillRf();
        temporaryChargeBillRf.setBillIds(temporaryChargeBillPageVS.stream().map(TemporaryChargeBillPageV::getId).collect(Collectors.toList()));
        temporaryChargeBillRf.setApproveState(2);
        //-- TODO supUnitId default
        temporaryChargeBillRf.setSupCpUnitId("default");
        financeFeignClient.temporaryApproveBatch(temporaryChargeBillRf);
        // 设置账单id
        temporaryChargeBillPageVS.forEach(item -> {
            contractBondPlanAppService.updateBillId(Long.valueOf(item.getExtField1()), item.getId());
        });
    }


    public void stopContract(Long id, String reason) {
        contractConcludeService.stopContract(id, reason);
        ContractDetailsV contractDetailsV = contractConcludeService.selectById(id);
        BizLog.normal(id.toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.强制终止申请.getName();
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem("强制终止合同："+contractDetailsV.getName(), false)));
    }

    public Long checkContract(Long id, Integer signingMethod) {
        Long contractId = null;
        if (signingMethod.equals(ContractSetConst.RENEWAL)) {
            //查询合同
            contractId = contractConcludeService.checkContract(id, signingMethod);
        }
        return contractId;
    }

    public Boolean checkContractNo(ContractConcludeF contractConcludeF) {
        return contractConcludeService.checkContractNo(contractConcludeF);
    }

    public ContractStatisticsV contractStatistics(String tenantId) {
        ContractStatisticsV statisticsV = new ContractStatisticsV();
        //合同到期数量
        List<ContractDetailsV> expireContractNum = contractConcludeService.expireContract(tenantId, null, true);
        statisticsV.setContractExpire(expireContractNum.size());
        //合同临期数量
        List<ContractDetailsV> contractAdventNum = contractConcludeService.contractAdvent(tenantId, null, true, 90);
        statisticsV.setContractAdvent(contractAdventNum.size());
        //应收账单逾期数量
        List<ContractCollectionPlanV> collectionExpire = collectionPlanAppService.collectionExpire(tenantId, null, ContractSetConst.INCOME, null);
        statisticsV.setCollectionExpire(collectionExpire.size());
        //应收账单临期数量
        List<ContractCollectionPlanV> collectionAdvent = collectionPlanAppService.collectionAdvent(tenantId, null, ContractSetConst.INCOME, null, 30);
        statisticsV.setCollectionAdvent(collectionAdvent.size());
        //应付账单逾期数量
        List<ContractCollectionPlanV> payExpire = collectionPlanAppService.collectionExpire(tenantId, null, ContractSetConst.PAY, null);
        statisticsV.setPayExpire(payExpire.size());
        //应付账单临期数量
        List<ContractCollectionPlanV> payAdvent = collectionPlanAppService.collectionAdvent(tenantId, null, ContractSetConst.PAY, null, 30);
        statisticsV.setPayAdvent(payAdvent.size());
        return statisticsV;
    }

    /**
     * 更新合同到期的预警状态
     */
    public void updateWarnState(String tenantId, Long id) {
        List<ContractDetailsV> countExpire = contractConcludeService.expireContract(tenantId, id, true);
        if (countExpire.size() > 0) {
            ContractConcludeUpdateF updateF = new ContractConcludeUpdateF();
            updateF.setId(id);
            updateF.setWarnState(Const.State._2);
            contractConcludeService.updateContractConclude(updateF);
        }
        List<ContractDetailsV> countAdvent = contractConcludeService.contractAdvent(tenantId, id, true, 90);
        if (countAdvent.size() > 0) {
            ContractConcludeUpdateF updateF = new ContractConcludeUpdateF();
            updateF.setId(id);
            updateF.setWarnState(Const.State._1);
            contractConcludeService.updateContractConclude(updateF);
        }
        if (countAdvent.size() == 0 && countExpire.size() == 0) {
            ContractConcludeUpdateF updateF = new ContractConcludeUpdateF();
            updateF.setId(id);
            updateF.setWarnState(Const.State._0);
            contractConcludeService.updateContractConclude(updateF);
        }
    }

    public PageV<TemporaryChargeBillPageV> temporaryQueryPage(PageF<SearchF<?>> queryF) {
        SearchF<?> conditions = queryF.getConditions();
        Field appId = fields("b.app_id", 2, "85e822bdf5b54d27a8c49ed1d5ec234e");
        Field approvedState = fields("b.approved_state", 1, "2");
        Field state = fields("b.state", 1, "0");
        Field settleState = fields("b.settle_state", 1, "2");//20221201改为获取已结算的
        /*Field supUnitId = fields("supUnitId", 15, "");*/
        conditions.getFields().add(appId);
        conditions.getFields().add(approvedState);
        conditions.getFields().add(state);
        conditions.getFields().add(settleState);
//        return financeTemporaryFeignClient.temporaryQueryPage(queryF).getRecords();
        return financeFeignClient.temporaryQueryPage(queryF);
    }

    /**
     * 临时账单引用
     * @param referenceState 0 解除关联 1设置关联
     */
    public Boolean reference(Long billId, Integer referenceState) {
        return contractConcludeService.reference(billId, referenceState);
    }

    public Field fields(String name, int method, Object value) {
        Field field = new Field();
        field.setName(name);
        field.setMethod(method);
        field.setValue(value);
        return field;
    }

    public PageV<ContractInfoV> pageContractPage(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        PageV<ContractInfoV> concludeVPageV = contractConcludeService.pageContractPage(request, tenantId);
        return concludeVPageV;
    }

    public ContractAccountSumV accountAmountSum(PageF<SearchF<ContractConcludeE>> request, String tenantId) {
        return contractConcludeService.accountAmountSum(request, tenantId);
    }

    public List<OrgInfoTreeV> orgInfoTree(OrgInfoTreeF orgInfoF){
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if(RadioEnum.NONE.equals(orgPermissionV.getRadio())){
            return new ArrayList<>();
        }
        if(RadioEnum.APPOINT.equals(orgPermissionV.getRadio())&&!CollectionUtils.isEmpty(orgPermissionV.getOrgIds())){
            List<Long> orgIds = orgPermissionV.getOrgIds().stream().map(Long::valueOf).collect(Collectors.toList());
            orgInfoF.setPermissionOrgIds(orgIds);
        }
        return orgFeignClient.orgInfoTree(orgInfoF);
    }
}
