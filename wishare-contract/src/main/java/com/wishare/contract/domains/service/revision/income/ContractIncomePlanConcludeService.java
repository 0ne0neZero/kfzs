package com.wishare.contract.domains.service.revision.income;

import cn.hutool.core.builder.GenericBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.ReceiptRecordF;
import com.wishare.contract.apps.fo.revision.ReceivableBillF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsReductF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.AddTemporaryChargeBillRf;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.fo.UpdateTemporaryChargeBillF;
import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.contractset.ContractPayIncomePlanService;
import com.wishare.contract.apps.service.revision.common.ContractInfoToFxmCommonService;
import com.wishare.contract.apps.service.revision.income.ContractIncomeConcludeExpandAppService;
import com.wishare.contract.domains.bo.CommonRangeAmountBO;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.dto.settlementPlan.SettlementPlanResult;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.income.*;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeSettDetailsE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.PayCostPlanE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsFundE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsReductE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.IriStatusEnum;
import com.wishare.contract.domains.enums.PlanFxmType;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.contractset.ContractPayIncomePlanMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeSettlementPeriodMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomePlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeSettlementConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.fund.ContractIncomeFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsReductMapper;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.service.revision.income.settdetails.ContractIncomeSettDetailsService;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.income.*;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import com.wishare.contract.domains.vo.revision.income.settlement.IncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanInnerInfoV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractSettFundV;
import com.wishare.contract.infrastructure.utils.BigDecimalUtils;
import com.wishare.contract.infrastructure.utils.DateTimeUtil;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.exception.SysException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收入合同服务 不使用@Transactional注解 统一使用编程式事务TransactionTemplate
 *
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/13:57
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContractIncomePlanConcludeService extends ServiceImpl<ContractIncomePlanConcludeMapper, ContractIncomePlanConcludeE> implements IOwlApiBase {

    private final ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;

    private final ContractIncomeSettlementConcludeMapper contractIncomeSettlementConcludeMapper;

    private final TransactionTemplate transactionTemplate;

    private final ContractSettlementsBillMapper contractPayBillMapper;

    private final ContractSettlementsFundMapper contractSettlementsFundMapper;

    private final ContractSettlementsReductMapper contractSettlementsReductMapper;

    private final ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    private final ConfigFeignClient configFeignClient;

    private final ContractInfoToFxmCommonService contractInfoToFxmCommonService;

    private final ExternalFeignClient externalFeignClient;

    private final StringRedisTemplate redisTemplate;

    private final ContractPayIncomePlanService payIncomePlanService;

    private final ContractOrgCommonService contractOrgCommonService;

    private final ContractIncomeFundMapper contractIncomeFundMapper;

    private final FinanceFeignClient financeFeignClient;

    private final PayIncomePlanLogService payIncomePlanLogService;

    private final ContractIncomeConcludePlanFxmRecordService contractIncomeConcludePlanFxmRecordService;

    private final ContractIncomeConcludePlanFxmReceiptRecordService receiptRecordService;

    @Resource
    @Lazy
    private ContractIncomeSettlementConcludeService settlementConcludeService;


    @Resource
    @Lazy
    private ContractIncomeFundService incomeFundService;
    @Resource
    @Lazy
    private ContractIncomeConcludeService contractIncomeConcludeService;

    private final IncomePlanHelperService planHelperService;

    private final static String COST_ESTIMATION_CODE_KEY = "COST:SKJH:CODE";
    private final static String KEY_PAYEE_ID = "payeeid";
    private final static String KEY_PAYEE_NAME = "payee";
    private final static String KEY_DRAW_ID = "draweeid";
    private final static String KEY_DRAW_NAME = "drawee";
    @Autowired
    private ContractConcludeService contractConcludeService;
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;
    @Autowired
    private ContractPayIncomePlanMapper payIncomePlanMapper;
    @Autowired
    private ContractIncomeSettDetailsService contractIncomeSettDetailsService;
    @Autowired
    private ContractIncomeConcludeSettlementPeriodMapper settlementPeriodMapper;
    @Autowired
    private ContractIncomeConcludeExpandAppService contractIncomeConcludeExpandAppService;
    @Autowired
    private ContractPayIncomePlanMapper contractPayIncomePlanMapper;

    @Nullable
    public ContractIncomePlanDetailsV getDetailsById(String id) {
        ContractIncomePlanConcludeE map = contractIncomePlanConcludeMapper.selectById(id);
        // 多处都需要空值校验,业务上面应该不会出现问题;
        if (Objects.isNull(map)) {
            throw BizException.throw404("抱歉! 收入合同计划表中没有对应的数据");
        }
        ContractIncomePlanDetailsV contractPayPlanDetailsV = Global.mapperFacade.map(map, ContractIncomePlanDetailsV.class);

        //查询对应的结算单信息
        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ContractIncomeSettlementConcludeE::getContractId, map.getContractId())
                .like(ContractIncomeSettlementConcludeE::getTermDate,map.getTermDate());
        List<ContractIncomeSettlementConcludeE> contractPaySettlementConcludeList = contractIncomeSettlementConcludeMapper.selectList(queryWrapper);
        if (Objects.isNull(contractPaySettlementConcludeList)) {
            throw BizException.throw404("抱歉! 收入结算单表中没有对应的数据");
        }
        List<ContractIncomeSettlementConcludeDetailsV> contractPaySettlementConcludeDetailsVs
                = Global.mapperFacade.mapAsList(contractPaySettlementConcludeList, ContractIncomeSettlementConcludeDetailsV.class);

        for (ContractIncomeSettlementConcludeDetailsV s : contractPaySettlementConcludeDetailsVs) {
            s.setPaymentMethodName(PaymentMethodEnum.parseName(s.getPaymentMethod()));
            s.setPaymentStatusName(PaymentStatusEnum.parseName(s.getPaymentStatus()));
            s.setInvoiceStatusName(InvoiceStatusEnum.parseName(s.getInvoiceStatus()));
            s.setSettleStatusName(SettleStatusEnum.parseName(s.getSettleStatus()));
            s.setPaymentTypeName(PaymentTypeEnum.parseName(s.getPaymentType()));
        }

        contractPayPlanDetailsV.setContractPaySettDetailsSaveList(contractPaySettlementConcludeDetailsVs);
        return contractPayPlanDetailsV;
    }

    @Nullable
    public ContractIncomePlanDetailsV getDetailsByIdSettle(String id) {
        ContractIncomePlanConcludeE map = contractIncomePlanConcludeMapper.selectById(id);
        // 多处都需要空值校验,业务上面应该不会出现问题;
        if (Objects.isNull(map)) {
            throw BizException.throw404("抱歉! 收入合同计划表中没有对应的数据");
        }
        ContractIncomePlanDetailsV contractPayPlanDetailsV = Global.mapperFacade.map(map, ContractIncomePlanDetailsV.class);
        //查询对应的收票信息
        LambdaQueryWrapper<ContractSettlementsBillE> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContractSettlementsBillE::getSettlementId, id);
        List<ContractSettlementsBillE> contractPayBillES = contractPayBillMapper.selectList(queryWrapper1);
        List<ContractPayBillV> contractPayBillVList = Global.mapperFacade.mapAsList(contractPayBillES, ContractPayBillV.class);
        for (ContractPayBillV s : contractPayBillVList) {
            s.setBillTypeName(BillTypeEnum.parseName(Integer.parseInt(s.getBillType())));
            s.setInvoiceStatusName("已开票");
        }
        //查询对应的付款信息
        LambdaQueryWrapper<ContractSettlementsFundE> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ContractSettlementsFundE::getSettlementId, id);
        List<ContractSettlementsFundE> contractSettlementsFundES = contractSettlementsFundMapper.selectList(queryWrapper2);
        List<ContractSettFundV> contractSettFundVList = Global.mapperFacade.mapAsList(contractSettlementsFundES, ContractSettFundV.class);
        for(ContractSettFundV settFundV :  contractSettFundVList){
            settFundV.setPlannedCollectionAmount(contractPayPlanDetailsV.getPlannedCollectionAmount());
            //收款状态
            if(contractPayPlanDetailsV.getNoReceiptAmount().compareTo(BigDecimal.ZERO) > 0){
                settFundV.setPlanStatusName("未完成");
            }else{
                settFundV.setPlanStatusName("已完成");
            }
            //收款方式
            if(settFundV.getFundType() != null) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收款计划收款方式.getCode(), settFundV.getFundType().toString());
                if (CollectionUtils.isNotEmpty(value)) {
                    settFundV.setFundTypeName(value.get(0).getName());
                }
            }
        }
        contractPayPlanDetailsV.setContractSettFundVList(contractSettFundVList);
        contractPayPlanDetailsV.setContractPayBillList(contractPayBillVList);
        List<ContractIncomeFundE> contractIncomeFundEList = incomeFundService.lambdaQuery().eq(ContractIncomeFundE::getId,map.getContractPayFundId()).list();
        List<ContractIncomeConcludeE> contractConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,map.getContractId()).list();
        if (CollectionUtils.isNotEmpty(contractConcludeEList)) {
            contractPayPlanDetailsV.setOurParty(contractConcludeEList.get(0).getOurParty());
            contractPayPlanDetailsV.setDepartName(contractConcludeEList.get(0).getDepartName());
            contractPayPlanDetailsV.setCostCenterName(contractConcludeEList.get(0).getCostCenterName());
            contractPayPlanDetailsV.setServiceType(CollectionUtils.isNotEmpty(contractIncomeFundEList) ? contractIncomeFundEList.get(0).getType() : null);
            contractPayPlanDetailsV.setCreator(contractConcludeEList.get(0).getCreatorName());
            contractPayPlanDetailsV.setContractAmountOriginalRate(contractConcludeEList.get(0).getContractAmountOriginalRate());
            contractPayPlanDetailsV.setCustomerName(contractConcludeEList.get(0).getOppositeOne());
        }
        return contractPayPlanDetailsV;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractIncomePlanConcludeV> page(PageF<SearchF<ContractIncomePlanConcludePageF>> form) {
        Page<ContractIncomePlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractIncomePlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        IPage<ContractIncomePlanConcludeV> pageList
                = contractIncomePlanConcludeMapper.collectionPlanDetailPage(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
        List<ContractIncomePlanConcludeV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(ContractIncomePlanConcludeV::getId).collect(Collectors.toList());
        List<ContractIncomePlanConcludeV> concludeVList = contractIncomePlanConcludeMapper.queryByPath(queryModel, parentIdList, getIdentityInfo().get().getTenantId());
        List<ContractIncomePlanConcludeV> list = TreeUtil.treeing(concludeVList);
        for(ContractIncomePlanConcludeV s : list){
            if(s.getPid().equals("0")){
                s.setReviewStatus(null);
                s.setTermDate(null);
                s.setPaymentStatus(null);
                s.setInvoiceStatus(null);
                s.setPlanStatus(null);
            }
        }
        return PageV.of(form, pageList.getTotal(), list);
    }


    public Boolean save(List<ContractIncomePlanAddF> addF) {
        if (addF == null) {
            throw SysException.throw403("收款计划信息不能为空");
        }
        String saveType = addF.get(0).getSaveType();
        List<ContractIncomePlanConcludeE> incomePlanConcludeES = Global.mapperFacade.mapAsList(addF, ContractIncomePlanConcludeE.class);
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomePlanConcludeMapper.getHowOrder(addF.get(0).getContractId(),null);
        Integer howOrder = 0;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            howOrder = contractIncomePlanConcludeVList.get(0).getHowOrder();
        }
        BigDecimal sumAmount = BigDecimal.ZERO;
        for(ContractIncomePlanAddF s : addF){
            sumAmount = sumAmount.add(s.getPlannedCollectionAmount());
        }
        //创建一个父流程
        ContractIncomePlanConcludeE concludeE = new ContractIncomePlanConcludeE();
        concludeE.setCustomer(addF.get(0).getCustomer());
        concludeE.setCustomerName(addF.get(0).getCustomerName());
        concludeE.setContractNo(addF.get(0).getContractNo());
        concludeE.setContractName(addF.get(0).getContractName());
        concludeE.setDepartName(addF.get(0).getDepartName());
        concludeE.setContractId(addF.get(0).getContractId());
        concludeE.setPlannedCollectionAmount(sumAmount);
        concludeE.setReceiptAmount(BigDecimal.ZERO);
        concludeE.setNoReceiptAmount(sumAmount);
        concludeE.setTenantId(tenantId());
        contractIncomePlanConcludeMapper.insert(concludeE);
        for(ContractIncomePlanConcludeE incomePlanConclude : incomePlanConcludeES){
            log.info("新增时转换的入库对象: {}", JSONObject.toJSONString(incomePlanConclude));
            incomePlanConclude.setPayNotecode(contractCode()) // 付款计划编号
                    .setTenantId(tenantId());    // 租户id
            if (Objects.equals("1", saveType)) {
                incomePlanConclude.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                incomePlanConclude.setReviewStatus(2);
            }
            if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(addF.get(0).getTimeRange())){
                incomePlanConclude.setTimeRanges(addF.get(0).getTimeRange().stream().collect(Collectors.joining(",")));
                incomePlanConclude.setStartTime(LocalDate.parse(addF.get(0).getTimeRange().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                incomePlanConclude.setEndTime(LocalDate.parse(addF.get(0).getTimeRange().get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            incomePlanConclude.setPid(concludeE.getId());
            incomePlanConclude.setReceiptAmount(BigDecimal.ZERO);
            incomePlanConclude.setNoReceiptAmount(incomePlanConclude.getPlannedCollectionAmount());
            incomePlanConclude.setHoworder(howOrder + 1);
            contractIncomePlanConcludeMapper.insert(incomePlanConclude);

            ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectById(incomePlanConclude.getContractId());
            // 收款计划同步到枫行梦
            if(contractIncomeConcludeE.getSealType() != null && contractIncomeConcludeE.getSealType() == 1){
                ReceivableBillF receivableBillF = contractInfoToFxmCommonService.receivableBillInfoToFxm(incomePlanConclude,1, contractIncomeConcludeE);
                String requestBody = JSON.toJSONString(receivableBillF);
                if (StringUtils.isNotEmpty(requestBody)) {
                    ContractBasePullF contractBasePullF = new ContractBasePullF();
                    contractBasePullF.setRequestBody(requestBody);
                    contractBasePullF.setType(0);
                    Boolean isSuccess = externalFeignClient.contractReceivableBill(contractBasePullF);
                    log.info("收款计划同步到枫行梦是否成功" + isSuccess);
                }
            }

        }
        return Boolean.TRUE;
    }

    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {

        LocalDate planTime = LocalDate.now();

        if(planDateF.getHowOrder() != null){
            List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomePlanConcludeMapper.getHowOrder(planDateF.getContractId(),planDateF.getHowOrder());
            for(ContractIncomePlanConcludeV s : contractIncomePlanConcludeVList){
                contractIncomePlanConcludeMapper.deleteById(s.getId());
            }
        }
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomePlanConcludeMapper.getHowOrder(planDateF.getContractId(),null);
        Integer termDate = 1;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            termDate = contractIncomePlanConcludeVList.stream().max(Comparator.comparing(ContractIncomePlanConcludeV :: getTermDate)).get().getTermDate() + 1;
            contractIncomePlanConcludeVList.sort((t1,t2) -> t2.getPlannedCollectionTime().compareTo(t1.getPlannedCollectionTime()));
            planTime = contractIncomePlanConcludeVList.get(0).getPlannedCollectionTime();
        }else{
            planTime = planDateF.getContractStartTime();
        }
        ArrayList<ContractPlanDateV> planDateList = Lists.newArrayList();

        switch (planDateF.getSplitMode()) {
            case 1: // 一次性收款,只有一条,前面已经塞进去了,这里不做处理
                getSplitWay(planTime, SplitEnum.once.getWay(), planDateF, planDateList, termDate);
                break;
            case 2: // 按年收款：根据开始结束日期相减和1年比较
                // 每次加1年即12个月
                getSplitWay(planTime, SplitEnum.year.getWay(), planDateF, planDateList, termDate);
                break;
            case 3: // 按半年收款
                // 每次加半年即6个月
                getSplitWay(planTime, SplitEnum.halfYear.getWay(), planDateF, planDateList,termDate);
                break;
            case 4: // 按季度收款
                // 每次加1/4年即3个月
                getSplitWay(planTime, SplitEnum.quarter.getWay(), planDateF, planDateList,termDate);
                break;
            case 5: // 按月收款,一次加1个月
                getSplitWay(planTime, SplitEnum.month.getWay(), planDateF, planDateList,termDate);
                break;
            default:
                throw BizException.throw403("拆分方式错误,请检查拆分方式是否正确");
        }
        return planDateList;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractIncomePlanConcludeInfoV> pageInfo(PageF<SearchF<ContractIncomePlanConcludePageF>> form) {
        Page<ContractIncomePlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractIncomePlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        IPage<ContractIncomePlanConcludeInfoV> pageList
                = contractIncomePlanConcludeMapper.pageInfo(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
        return PageV.of(form, pageList.getTotal(), pageList.getRecords());
    }

    public List<ContractIncomePlanConcludeV> list(ContractIncomePlanConcludeListF contractIncomeConcludeListF){
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVS = contractIncomePlanConcludeMapper.getHowOrder(contractIncomeConcludeListF.getContractId(),contractIncomeConcludeListF.getHowOrder());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractIncomePlanConcludeV s : contractIncomePlanConcludeVS){
            ssettlementAmount = ssettlementAmount.add(s.getReceiptAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for(ContractIncomePlanConcludeV s : contractIncomePlanConcludeVS){
            s.setSsettlementAmount(ssettlementAmount);
            s.setSreceiptAmount(sreceiptAmount);
            if(StringUtils.isNotEmpty(s.getTimeRanges())){
                s.setTimeRange(Arrays.asList(s.getTimeRanges().split(",")));
            }
        }
        return contractIncomePlanConcludeVS;
    }

    public List<ContractIncomePlanConcludeInfoV> listInfo(ContractIncomePlanConcludeListF contractIncomeConcludeListF){
        List<ContractIncomePlanConcludeInfoV> contractIncomePlanConcludeVS = contractIncomePlanConcludeMapper.getHowOrderInfo(contractIncomeConcludeListF.getContractId(),contractIncomeConcludeListF.getHowOrder());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractIncomePlanConcludeInfoV s : contractIncomePlanConcludeVS){
            ssettlementAmount = ssettlementAmount.add(s.getReceiptAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for(ContractIncomePlanConcludeInfoV s : contractIncomePlanConcludeVS){
            s.setSsettlementAmount(ssettlementAmount);
            s.setSreceiptAmount(sreceiptAmount);
        }
        return contractIncomePlanConcludeVS;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public ContractIncomePlanConcludeSumV accountAmountSum(PageF<SearchF<ContractIncomePlanConcludePageF>> form) {
        QueryWrapper<ContractIncomePlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        return transactionTemplate.execute(status -> {
            try {
                return contractIncomePlanConcludeMapper.accountAmountSum(conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.error("收入合同计算总额失败,已回滚，失败原因:{}", ex.getMessage());
                throw SysException.throw403("收入合同计算总额失败,已回滚");
            }
        });
    }

    /**
     * 根据Id更新
     *
     * @param contractPayConcludeF 根据Id更新
     */
    public void update(List<ContractIncomePlanConcludeUpdateF> contractPayConcludeF) {
        if (contractPayConcludeF == null) {
            throw SysException.throw403("收款计划信息不能为空");
        }
        String saveType = contractPayConcludeF.get(0).getSaveType();
        List<ContractIncomePlanConcludeE> mapAsList = Global.mapperFacade.mapAsList(contractPayConcludeF, ContractIncomePlanConcludeE.class);
        for(ContractIncomePlanConcludeE map : mapAsList){
            if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(contractPayConcludeF.get(0).getTimeRange())){
                map.setTimeRanges(contractPayConcludeF.get(0).getTimeRange().stream().collect(Collectors.joining(",")));
                map.setStartTime(LocalDate.parse(contractPayConcludeF.get(0).getTimeRange().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                map.setEndTime(LocalDate.parse(contractPayConcludeF.get(0).getTimeRange().get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if (Objects.equals("1", saveType)) {
                map.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                map.setReviewStatus(2);
            }
            map.setNoReceiptAmount(map.getPlannedCollectionAmount());
            contractIncomePlanConcludeMapper.updateById(map);
        }
        ContractIncomePlanConcludeE sb = contractIncomePlanConcludeMapper.selectById(contractPayConcludeF.get(0).getId());
        handlePidPaySettlementInfo(sb.getPid());
    }

    /**
     * @param id 根据Id删除
     * @return 删除结果
     */
    public Boolean removeById(String id) {
        ContractIncomePlanConcludeE ss = contractIncomePlanConcludeMapper.selectById(id);
        contractIncomePlanConcludeMapper.deleteById(id);
        handlePidPaySettlementInfo(ss.getPid());
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContractIncomePlanConcludeE::getPid, ss.getPid())
                .eq(ContractIncomePlanConcludeE::getDeleted,0);
        List<ContractIncomePlanConcludeE> concludeEList = contractIncomePlanConcludeMapper.selectList(queryWrapper1);
        if(!ObjectUtils.isNotEmpty(concludeEList)){
            contractIncomePlanConcludeMapper.deleteById(ss.getPid());
        }
        return Boolean.TRUE;
    }

    /**
     * @param id 根据Id提交
     */
    public void sumbitId(String id) {
        ContractIncomePlanConcludeE map = contractIncomePlanConcludeMapper.selectById(id);
        map.setReviewStatus(2);
        contractIncomePlanConcludeMapper.updateById(map);
    }

    private QueryWrapper<ContractIncomePlanConcludePageF> conditionPage(QueryWrapper<ContractIncomePlanConcludePageF> queryModel, String tenantId) {
        queryModel.eq("ccp.deleted", 0);
        queryModel.eq("ccp.tenantId", tenantId);
        return queryModel;
    }

    private String contractCode() {
        //生成合同编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;子合同编号规则：主合同编号+两位数数值，如YYHT220816000101
        String year = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date());
        return "SKJH" + year;
    }

    /**
     * 拆分逻辑
     *
     * @param planTime     计划时间
     * @param monthsToAdd  拆分方式
     * @param planDateF    拆分依据
     * @param planDateList 拆分数据集合
     */
    private void getSplitWay(LocalDate planTime, int monthsToAdd, ContractPlanDateF planDateF, ArrayList<ContractPlanDateV> planDateList, Integer termDate) {

        // 先把第一条塞进去
        ContractPlanDateV first = GenericBuilder.of(ContractPlanDateV::new)
                .with(ContractPlanDateV::setTermDate, termDate)
                .with(ContractPlanDateV::setPlannedCollectionTime, planTime)
                .build();
        if(SplitEnum.once.getWay().equals(monthsToAdd)){
            first.setRatioAmount(new BigDecimal(100).setScale(2));
            first.setPlannedCollectionAmount(planDateF.getPlanAllAmount().setScale(2));
            planDateList.add(first);
            return;
        }
        planDateList.add(first);
        int j = 1;
        // 从2开始，是因为已经有第一期了，999是因为不可能有超过999年的合同
        for (int i = 2; i < 999; i++) {
            // 按拆分方式加固定年数
            planTime = planTime.plusMonths(monthsToAdd);
            // 计划时间小于等于，都可以继续拆分
            if (planTime.isBefore(planDateF.getContractEndTime()) || planTime.equals(planDateF.getContractEndTime())) {
                ContractPlanDateV planDateV = GenericBuilder.of(ContractPlanDateV::new)
                        .with(ContractPlanDateV::setTermDate, i)
                        .with(ContractPlanDateV::setPlannedCollectionTime, planTime)
                        .build();
                planDateList.add(planDateV);
                j += 1;
            } else {
                // 超出了，就不拆分
                break;
            }
        }
        BigDecimal splitPlanAmont = planDateF.getPlanAllAmount().divide(new BigDecimal(j),2, RoundingMode.HALF_UP);
        BigDecimal splitRatioAmount =  new BigDecimal(100).setScale(2).divide(new BigDecimal(j),2, RoundingMode.HALF_UP);
        for(int c = 0; c < j; c ++){
            ContractPlanDateV contractPlanDateV = planDateList.get(c);
            contractPlanDateV.setPlannedCollectionAmount(splitPlanAmont);
            contractPlanDateV.setRatioAmount(splitRatioAmount);
            planDateList.set(c,contractPlanDateV);
        }
    }

    public List<ContractIncomePlanConcludeE> getByIdList(List<String> idList) {
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomePlanConcludeE::getId, idList)
                .eq(ContractIncomePlanConcludeE::getDeleted,0);
        return list(queryWrapper);
    }

    public String invoice(ContractSettlementsBillF contractSettlementsBillF) {
        ContractSettlementsBillE map = Global.mapperFacade.map(contractSettlementsBillF, ContractSettlementsBillE.class);
        ContractIncomePlanConcludeE map1 = contractIncomePlanConcludeMapper.selectById(contractSettlementsBillF.getSettlementId());
        ContractIncomeConcludeE map2 = contractIncomeConcludeMapper.selectById(map1.getContractId());
        if(map1.getInvoiceStatus() == 1){
            throw SysException.throw403("已全部开票，不允许再次开票!");
        }
        if(map1.getInvoiceApplyAmount().add(contractSettlementsBillF.getAmount()).compareTo(map1.getPlannedCollectionAmount()) > 0){
            throw SysException.throw403("开票总金额大于计划金额，不允许开票!");
        }
        map.setTenantId(tenantId());
        contractPayBillMapper.insert(map);
        map1.setInvoiceApplyAmount(map1.getInvoiceApplyAmount().add(contractSettlementsBillF.getAmount()));
        if(map1.getInvoiceApplyAmount().compareTo(map1.getPlannedCollectionAmount()) == 0){
            map1.setInvoiceStatus(1);
        }
        map2.setInvoiceAmount(map2.getInvoiceAmount().add(contractSettlementsBillF.getAmount()));
        if(!map1.getPid().equals("0")){
            ContractIncomePlanConcludeE map6 = contractIncomePlanConcludeMapper.selectById(map1.getPid());
            map6.setInvoiceApplyAmount(map6.getInvoiceApplyAmount().add(map1.getInvoiceApplyAmount()));
            contractIncomePlanConcludeMapper.updateById(map6);
        }
        contractIncomeConcludeMapper.updateById(map2);
        contractIncomePlanConcludeMapper.updateById(map1);
        handlePidPaySettlementInfo(map1.getPid());
        return map.getId();
    }

    public void handlePidPaySettlementInfo(String pid) {
        ContractIncomePlanConcludeE setPid = contractIncomePlanConcludeMapper.selectById(pid);
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomePlanConcludeE::getPid, pid)
                .eq(ContractIncomePlanConcludeE::getDeleted,0);
        List<ContractIncomePlanConcludeE> contractPaySettlementConcludeEList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
        BigDecimal planAmount = BigDecimal.ZERO;
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal dueAmount = BigDecimal.ZERO;
        for(ContractIncomePlanConcludeE s : contractPaySettlementConcludeEList){
            planAmount = planAmount.add(s.getPlannedCollectionAmount());
            invoiceAmount = invoiceAmount.add(s.getInvoiceApplyAmount());
            payAmount = payAmount.add(s.getReceiptAmount());
            dueAmount = dueAmount.add(s.getDeductionAmount());
        }
        setPid.setNoReceiptAmount(setPid.getPlannedCollectionAmount().subtract(setPid.getReceiptAmount()));
        setPid.setDeductionAmount(dueAmount);
        setPid.setInvoiceApplyAmount(invoiceAmount);
        setPid.setPlannedCollectionAmount(planAmount);
        setPid.setReceiptAmount(payAmount);
        contractIncomePlanConcludeMapper.updateById(setPid);
    }

    /**
     * @param contractSettlementsFundF 参数
     */
    public String setFund(ContractSettlementsFundF contractSettlementsFundF) {
        ContractSettlementsFundE map = Global.mapperFacade.map(contractSettlementsFundF, ContractSettlementsFundE.class);
        ContractIncomePlanConcludeE map1 = contractIncomePlanConcludeMapper.selectById(contractSettlementsFundF.getSettlementId());
        ContractIncomeConcludeE map2 = contractIncomeConcludeMapper.selectById(map1.getContractId());
        if(map1.getPlanStatus() == 2){
            throw SysException.throw403("已全部收款，不允许再次收款!");
        }
        if(map1.getReceiptAmount().add(contractSettlementsFundF.getAmount()).compareTo(map1.getPlannedCollectionAmount().subtract(map1.getDeductionAmount())) > 0){
            throw SysException.throw403("收款总金额大于未收款金额，不允许收款!");
        }
        map.setTenantId(tenantId());
        map.setPayNotecode("ZC" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date()));
        contractSettlementsFundMapper.insert(map);
        map1.setReceiptAmount(map1.getReceiptAmount().add(contractSettlementsFundF.getAmount()));
        map1.setNoReceiptAmount(map1.getNoReceiptAmount().subtract(contractSettlementsFundF.getAmount()));
        if(map1.getNoReceiptAmount().compareTo(map1.getPlannedCollectionAmount()) == 0){
            map1.setPlanStatus(0);
        }
        if(map1.getReceiptAmount().compareTo(BigDecimal.ZERO) > 0){
            map1.setPlanStatus(1);
        }
        if((map1.getReceiptAmount().add(map1.getDeductionAmount())).compareTo(map1.getPlannedCollectionAmount()) == 0){
            map1.setPlanStatus(2);
        }
        map2.setCollectAmount(map2.getCollectAmount().add(contractSettlementsFundF.getAmount()));
        if(!map1.getPid().equals("0")){
            ContractIncomePlanConcludeE map6 = contractIncomePlanConcludeMapper.selectById(map1.getPid());
            map6.setReceiptAmount(map6.getReceiptAmount().add(map1.getReceiptAmount()));
            map6.setNoReceiptAmount(map6.getPlannedCollectionAmount().subtract(map6.getNoReceiptAmount()).subtract(map6.getDeductionAmount()));
            contractIncomePlanConcludeMapper.updateById(map6);
        }
        contractIncomeConcludeMapper.updateById(map2);
        contractIncomePlanConcludeMapper.updateById(map1);
        handlePidPaySettlementInfo(map1.getPid());
        if(map2.getSealType() != null && map2.getSealType() == 1) {
            ReceiptRecordF receiptRecordF = contractInfoToFxmCommonService.receiptRecordInfoToFxm(map, map1, map2);
            String requestBody = JSON.toJSONString(receiptRecordF);
            if (StringUtils.isNotEmpty(requestBody)) {
                ContractBasePullF contractBasePullF = new ContractBasePullF();
                contractBasePullF.setRequestBody(requestBody);
                contractBasePullF.setType(0);
                Boolean isSuccess = externalFeignClient.contractReceiptRecord(contractBasePullF);
                log.info("损益管理收入计划同步到枫行梦是否成功" + isSuccess);
            }
        }
        return map.getId();
    }

    /**
     * @param contractSettlementsFundF 参数
     */
    public String setReduct(ContractSettlementsReductF contractSettlementsFundF) {
        ContractSettlementsReductE map = Global.mapperFacade.map(contractSettlementsFundF, ContractSettlementsReductE.class);
        ContractIncomePlanConcludeE map1 = contractIncomePlanConcludeMapper.selectById(contractSettlementsFundF.getSettlementId());
        ContractIncomeConcludeE map2 = contractIncomeConcludeMapper.selectById(map1.getContractId());
        if(map1.getReceiptAmount().compareTo(BigDecimal.ZERO) > 0){
            throw SysException.throw403("已收款，不允许减免!");
        }
        if(map1.getDeductionAmount().add(contractSettlementsFundF.getAmount()).compareTo(map1.getPlannedCollectionAmount()) > 0){
            throw SysException.throw403("减免总金额大于计划金额，不允许减免!");
        }
        map.setTenantId(tenantId());
        contractSettlementsReductMapper.insert(map);
        map1.setDeductionAmount(map1.getDeductionAmount().add(contractSettlementsFundF.getAmount()));
        map1.setNoReceiptAmount(map1.getPlannedCollectionAmount().subtract(map1.getDeductionAmount().add(map1.getReceiptAmount())));
        contractIncomePlanConcludeMapper.updateById(map1);
        if(!map1.getPid().equals("0")){
            ContractIncomePlanConcludeE map6 = contractIncomePlanConcludeMapper.selectById(map1.getPid());
            map6.setDeductionAmount(map6.getDeductionAmount().add(map1.getDeductionAmount()));
            map6.setNoReceiptAmount(map6.getPlannedCollectionAmount().subtract(map6.getDeductionAmount()).subtract(map6.getReceiptAmount()));
            contractIncomePlanConcludeMapper.updateById(map6);
        }
        map2.setDeductionAmount(map2.getDeductionAmount().add(contractSettlementsFundF.getAmount()));
        contractIncomeConcludeMapper.updateById(map2);
        handlePidPaySettlementInfo(map1.getPid());
        return map.getId();
    }

    public BigDecimal getByContractList(String contractId) {
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomePlanConcludeE::getContractId, contractId)
                .eq(ContractIncomePlanConcludeE::getReviewStatus,2)
                .eq(ContractIncomePlanConcludeE::getDeleted,0);
        List<ContractIncomePlanConcludeE> contractPayPlanConcludeEList = list(queryWrapper);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(contractPayPlanConcludeEList)){
            for(ContractIncomePlanConcludeE s : contractPayPlanConcludeEList){
                bigDecimal = bigDecimal.add(s.getPlannedCollectionAmount());
            }
        }
        return bigDecimal;
    }

    public String save(ContractIncomePlanAddF addF){
        ContractIncomePlanConcludeE map = Global.mapperFacade.map(addF, ContractIncomePlanConcludeE.class);
        map.setPayNotecode(contractCode());
        map.setTermDate(1);
        map.setTenantId(tenantId());
        if(addF.getSaveType().equals("2")){
            map.setReviewStatus(2);
        }
        contractIncomePlanConcludeMapper.insert(map);
        return map.getId();
    }

    /**
     * 根据Id更新
     *
     * @param contractPayConcludeF 根据Id更新
     */
    public void update(ContractIncomePlanConcludeUpdateF contractPayConcludeF){
        if (contractPayConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomePlanConcludeE map = Global.mapperFacade.map(contractPayConcludeF, ContractIncomePlanConcludeE.class);
        contractIncomePlanConcludeMapper.updateById(map);
    }



    @Transactional(rollbackFor = Exception.class)
    public SettlementPlanResult saveSettlementPlan(IncomePlanAddF req) {
        List<ContractIncomeConcludeE> contractPayConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        List<ContractIncomePlanConcludeE> existContractPayPlanConcludeEList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getContractId,req.getContractId()).list();
        if (CollectionUtils.isNotEmpty(existContractPayPlanConcludeEList)) {
            throw new OwlBizException("非法操作,结算计划已经新增过,请使用编辑");
        }
        planHelperService.paramValidate(req, contractPayConcludeEList.get(0));

        validateSettlementPlans(req,contractPayConcludeEList.get(0));
        planHelperService.fillGroupCodeAndTerm(req.getContractPayPlanAddFLists());

        IdentityInfo identityInfo = curIdentityInfo();
        List<List<ContractIncomePlanAddF>> lists = req.getContractPayPlanAddFLists();



        BigDecimal plannedCollectionAmountSum = lists.stream().flatMap(List::stream)
                .map(ContractIncomePlanAddF::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ContractIncomePlanConcludeE concludeE = new ContractIncomePlanConcludeE();
        concludeE.setContractNo(lists.get(0).get(0).getContractNo());
        concludeE.setContractName(contractPayConcludeEList.get(0).getName());
        concludeE.setContractId(lists.get(0).get(0).getContractId());
        concludeE.setPlannedCollectionAmount(plannedCollectionAmountSum);
        concludeE.setTenantId(tenantId());
        concludeE.setCreator(identityInfo.getUserId());
        concludeE.setCreatorName(identityInfo.getUserName());
        concludeE.setGmtCreate(LocalDateTime.now());
        concludeE.setOperatorName(identityInfo.getUserName());
        concludeE.setOperator(identityInfo.getUserName());
        concludeE.setReviewStatus(2);
        List<ContractIncomePlanAddF> flatList = lists.stream().flatMap(List::stream).collect(Collectors.toList());
        flatList.sort(Comparator.comparing(ContractIncomePlanAddF::getCostStartTime));
        concludeE.setCostStartTime(flatList.get(0).getCostStartTime());
        concludeE.setCostEndTime(flatList.get((flatList.size()-1)).getCostEndTime());

        flatList.sort(Comparator.comparing(ContractIncomePlanAddF::getPlannedCollectionTime));
        concludeE.setPtMin(flatList.get(0).getPlannedCollectionTime());
        concludeE.setPtMax(flatList.get((flatList.size()-1)).getPlannedCollectionTime());
        contractIncomePlanConcludeMapper.insert(concludeE);

        List<ContractIncomeConcludePlanFxmRecordE> recordList = new ArrayList<>();
        lists.forEach(list->{
            List<ContractIncomePlanConcludeE> contractPayPlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractIncomePlanConcludeE.class);
            contractPayPlanConcludeEList.forEach(contract->{
                contract.setContractName(contractPayConcludeEList.get(0).getName());
                contract.setTenantId(identityInfo.getTenantId());
                contract.setCreator(identityInfo.getUserId());
                contract.setCreatorName(identityInfo.getUserName());
                contract.setGmtCreate(LocalDateTime.now());
                contract.setOperatorName(identityInfo.getUserName());
                contract.setOperator(identityInfo.getUserName());
                contract.setPid(concludeE.getId());
                contract.setTaxRate(contract.getTaxRate());
                contract.setReviewStatus(ReviewStatusEnum.已通过.getCode());
                contract.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                contract.setCostEstimationCode(StringUtils.isBlank(contract.getCostEstimationCode()) ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,contract.getTermDate(),contract.getPlannedCollectionTime().getYear() + "-" + contract.getPlannedCollectionTime().getMonthValue(),StringUtils.isNotBlank(contractPayConcludeEList.get(0).getConmaincode()) ? contractPayConcludeEList.get(0).getConmaincode() : contractPayConcludeEList.get(0).getId()) : contract.getCostEstimationCode());
                contract.setConfirmedAmountReceivedTemp(contract.getPlannedCollectionAmount());
                contract.setApproveState(0);

                contract.setPtMin(contract.getPlannedCollectionTime());
                contract.setPtMax(contract.getPlannedCollectionTime());
            });
            this.saveBatch(contractPayPlanConcludeEList);
            List<ContractIncomeConcludePlanFxmRecordE> records = contractInfoToFxmCommonService.pushIncomePlan2Fxm(contractPayPlanConcludeEList, contractPayConcludeEList.get(0), PlanFxmPushType.INSERT);
            recordList.addAll(records);
        });
        if (CollectionUtils.isNotEmpty(recordList)) {
            contractIncomeConcludePlanFxmRecordService.saveBatch(recordList);
        }
        return new SettlementPlanResult();
    }

    @Transactional(rollbackFor =  Exception.class)
    public String saveSettlementPlanV2(IncomePlanEditV req) {
        List<ContractIncomeConcludeE> contractIncomeConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractIncomeConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        // 处理父级
        List<List<IncomeConcludePlanV2>> lists = req.getPlanV2List();
        BigDecimal plannedCollectionAmountSum = lists.stream().flatMap(List::stream)
                .map(IncomeConcludePlanV2::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        IdentityInfo identityInfo = curIdentityInfo();
        ContractIncomePlanConcludeE concludeE = new ContractIncomePlanConcludeE();
        concludeE.setContractNo(lists.get(0).get(0).getContractNo());
        concludeE.setContractName(contractIncomeConcludeEList.get(0).getName());
        concludeE.setContractId(lists.get(0).get(0).getContractId());
        concludeE.setPlannedCollectionAmount(plannedCollectionAmountSum);
        concludeE.setTenantId(tenantId());
        concludeE.setCreator(identityInfo.getUserId());
        concludeE.setCreatorName(identityInfo.getUserName());
        concludeE.setGmtCreate(LocalDateTime.now());
        concludeE.setOperatorName(identityInfo.getUserName());
        concludeE.setOperator(identityInfo.getUserName());
        concludeE.setReviewStatus(2);
        List<IncomeConcludePlanV2> flatList = lists.stream().flatMap(List::stream).collect(Collectors.toList());
        flatList.sort(Comparator.comparing(IncomeConcludePlanV2::getCostStartTime));
        concludeE.setCostStartTime(flatList.get(0).getCostStartTime());
        concludeE.setCostEndTime(flatList.get((flatList.size()-1)).getCostEndTime());
        flatList.sort(Comparator.comparing(IncomeConcludePlanV2::getPlannedCollectionTime));
        concludeE.setPtMin(flatList.get(0).getPlannedCollectionTime());
        concludeE.setPtMax(flatList.get((flatList.size()-1)).getPlannedCollectionTime());
        contractIncomePlanConcludeMapper.insert(concludeE);

        List<ContractIncomeConcludePlanFxmRecordE> recordList = new ArrayList<>();
        req.getPlanV2List().forEach(list->{
            List<ContractIncomePlanConcludeE> contractIncomePlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractIncomePlanConcludeE.class);
            contractIncomePlanConcludeEList.forEach(e ->{
                e.setPid(concludeE.getId());
                e.setTaxRate(StringUtils.isNotBlank(e.getTaxRate()) ? e.getTaxRate().split("%")[0] : null);
                e.setReviewStatus(2);
                e.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                e.setCostEstimationCode(StringUtils.isBlank(e.getCostEstimationCode()) ?
                        generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,e.getTermDate(),
                                e.getPlannedCollectionTime().getYear() +
                                "-" +
                                e.getPlannedCollectionTime().getMonthValue(),contractIncomeConcludeEList.get(0).getConmaincode()) :
                        e.getCostEstimationCode());
            });
            this.saveBatch(contractIncomePlanConcludeEList);
            List<ContractIncomeConcludePlanFxmRecordE> records =
                    contractInfoToFxmCommonService.pushIncomePlan2Fxm(contractIncomePlanConcludeEList, contractIncomeConcludeEList.get(0), PlanFxmPushType.INSERT);
            recordList.addAll(records);
        });

        if (CollectionUtils.isNotEmpty(recordList)) {
            contractIncomeConcludePlanFxmRecordService.saveBatch(recordList);
        }

        return "success";
    }

    @Transactional(rollbackFor =  Exception.class)
    public SettlementPlanResult editSettlementPlan(IncomePlanAddF req) {
        if (StringUtils.isBlank(req.getContractId())) {
            throw new OwlBizException("合同id必传");
        }
        //经过判断contractPayPlanConcludeEList将父级计划移除后的就是旧计划并且后续没有改动字段值，可以用于推送
        List<ContractIncomePlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getContractId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            throw new OwlBizException("非法操作");
        }
        Integer splitMode = contractPayPlanConcludeEList.get(1).getSplitMode();
        Optional<ContractIncomePlanConcludeE> concludeEOptional = contractPayPlanConcludeEList.stream().filter(p->0 != p.getPaymentStatus()).findAny();
        if (concludeEOptional.isPresent() && !Objects.equals(splitMode, req.getSplitMode())) {
            throw new OwlBizException("不支持修改结算周期");
        }
        boolean removeAll = removeAll(req.getSplitMode(),splitMode,req.getContractId());
        List<ContractIncomeConcludeE> contractPayConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        List<ContractIncomePlanConcludeE> notOneTimeContractPayPlanList = new ArrayList<>();
        if (5 != req.getSplitMode() && !removeAll) {
            notOneTimeContractPayPlanList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getContractId,req.getContractId()).ne(ContractIncomePlanConcludeE::getPid,"0").ne(ContractIncomePlanConcludeE::getSplitMode,5).list();
        }

        List<List<ContractIncomePlanAddF>> lists = req.getContractPayPlanAddFLists();
        planHelperService.editParamValidateAndFillData(req, contractPayConcludeEList.get(0),notOneTimeContractPayPlanList,removeAll);
//        validatePlanId(req.getContractPayPlanAddFLists(),settlementPlanResult,removeAll);
        validateSettlementPlans(req,contractPayConcludeEList.get(0));

        List<ContractIncomePlanConcludeE> all = new ArrayList<>();
        lists.forEach(list->{
            List<ContractIncomePlanConcludeE> newContractPayPlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractIncomePlanConcludeE.class);

            ContractIncomePlanConcludeE parentConcludeE = contractPayPlanConcludeEList.stream().filter(c->"0".equals(c.getPid())).findFirst().get();
            IdentityInfo identityInfo = curIdentityInfo();
            newContractPayPlanConcludeEList.forEach(contract->{
                contract.setCustomer(contractPayConcludeEList.get(0).getOppositeOneId());
                contract.setCustomerName(contractPayConcludeEList.get(0).getOppositeOne());
                contract.setContractName(contractPayConcludeEList.get(0).getName());
                contract.setTenantId(identityInfo.getTenantId());
                contract.setCreator(identityInfo.getUserId());
                contract.setCreatorName(identityInfo.getUserName());
                contract.setGmtCreate(LocalDateTime.now());
                contract.setOperatorName(identityInfo.getUserName());
                contract.setOperator(identityInfo.getUserName());
                contract.setSettlementAmount(contract.getSettlementAmount());
                contract.setTaxRate(StringUtils.isNotBlank(contract.getTaxRate()) ? contract.getTaxRate().split("%")[0] : null);
                contract.setReviewStatus(2);
                contract.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                contract.setPid(parentConcludeE.getId());
                contract.setCostEstimationCode(StringUtils.isBlank(contract.getCostEstimationCode()) ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,contract.getTermDate(),contract.getPlannedCollectionTime().getYear() + "-" + contract.getPlannedCollectionTime().getMonthValue(),StringUtils.isNotBlank(contractPayConcludeEList.get(0).getConmaincode()) ? contractPayConcludeEList.get(0).getConmaincode() : parentConcludeE.getId()) : contract.getCostEstimationCode());

                contract.setPtMin(contract.getPlannedCollectionTime());
                contract.setPtMax(contract.getPlannedCollectionTime());
            });
            newContractPayPlanConcludeEList.removeIf(c->"0".equals(c.getPid()));
            if (CollectionUtils.isNotEmpty(newContractPayPlanConcludeEList)) {
                removeFirst(newContractPayPlanConcludeEList,req.getContractId(),req.getSplitMode(),removeAll);
                all.addAll(newContractPayPlanConcludeEList);
                this.saveOrUpdateBatch(newContractPayPlanConcludeEList);
            }
        });

       if (CollectionUtils.isNotEmpty(all)) {
            List<ContractIncomePlanConcludeE> flatList = contractPayPlanConcludeEList.stream().filter(con->"0".equals(con.getPid())).collect(Collectors.toList());
            ContractIncomePlanConcludeE concludeE = flatList.get(0);
            all.sort(Comparator.comparing(ContractIncomePlanConcludeE::getCostStartTime));
            concludeE.setCostStartTime(all.get(0).getCostStartTime());
            concludeE.setCostEndTime(all.get((all.size()-1)).getCostEndTime());


            all.sort(Comparator.comparing(ContractIncomePlanConcludeE::getPlannedCollectionTime));
            concludeE.setPtMin(all.get(0).getPlannedCollectionTime());
            concludeE.setPtMax(all.get((all.size()-1)).getPlannedCollectionTime());
            this.updateById(concludeE);
        }
       //至此编辑逻辑全部执行完成，查询出编辑后的收款计划
        List<ContractIncomePlanConcludeE> newPlans = this.lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getContractId, req.getContractId())
                .ne(ContractIncomePlanConcludeE::getPid,"0")
                .eq(ContractIncomePlanConcludeE::getDeleted,0)
                .list();
        //处理编辑推送
        handleEditPlanPushFxm(contractPayPlanConcludeEList, newPlans, contractPayConcludeEList.get(0));
        return new SettlementPlanResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public String editSettlementPlanV2(IncomePlanEditV req) {
        List<ContractIncomeConcludeE> contractIncomeConcludeEList = contractIncomeConcludeService.lambdaQuery()
                .eq(ContractIncomeConcludeE::getId, req.getContractId())
                .eq(ContractIncomeConcludeE::getDeleted, 0)
                .list();
        if (CollectionUtils.isEmpty(contractIncomeConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeEList.get(0);
        if(Objects.nonNull(incomeConcludeE.getIsCorrectionAndPlan()) && CorrectionStatusEnum.已通过.getCode().equals(incomeConcludeE.getIsCorrectionAndPlan())){
            contractIncomeConcludeMapper.updateIsCorrectionAndPlan(incomeConcludeE.getId(),CorrectionStatusEnum.通过后修改确收计划.getCode());
        }
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomePlanConcludeE::getContractId,req.getContractId());
        queryWrapper.eq(ContractIncomePlanConcludeE::getPid,"0");
        queryWrapper.eq(ContractIncomePlanConcludeE::getDeleted,0);
        ContractIncomePlanConcludeE concludePlanE = contractIncomePlanConcludeMapper.selectOne(queryWrapper);
        if (Objects.isNull(concludePlanE)){
            throw new OwlBizException("数据异常，无法编辑");
        }
        //更改PID为0中的计划结算金额，列表展示汇总时使用
        BigDecimal plannedCollectionAmountSum = req.getPlanV2List().stream().flatMap(List::stream)
                .map(IncomeConcludePlanV2::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        concludePlanE.setPlannedCollectionAmount(plannedCollectionAmountSum);
        contractIncomePlanConcludeMapper.updateById(concludePlanE);

        List<ContractIncomePlanConcludeE> contractIncomePlanConcludeEList = Lists.newArrayList();
        for (List<IncomeConcludePlanV2> planV2s : req.getPlanV2List()) {
            for (IncomeConcludePlanV2 planV2 : planV2s) {
                planV2.setPid(concludePlanE.getId());
            }
            contractIncomePlanConcludeEList.addAll(Global.mapperFacade.mapAsList(planV2s, ContractIncomePlanConcludeE.class));
        }
        // 删除id范围之外的plan
        LambdaQueryWrapper<ContractIncomePlanConcludeE> oldWrapper = new LambdaQueryWrapper<>();
        oldWrapper.eq(ContractIncomePlanConcludeE::getContractId,req.getContractId());
        oldWrapper.ne(ContractIncomePlanConcludeE::getPid,"0");
        oldWrapper.notIn(ContractIncomePlanConcludeE::getId,contractIncomePlanConcludeEList.stream()
                .map(ContractIncomePlanConcludeE::getId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
        List<ContractIncomePlanConcludeE> oldPlanList = contractIncomePlanConcludeMapper.selectList(oldWrapper);
        contractIncomePlanConcludeMapper.delete(oldWrapper);
        this.saveOrUpdateBatch(contractIncomePlanConcludeEList);
        handleEditPlanPushFxm(oldPlanList, contractIncomePlanConcludeEList, contractIncomeConcludeEList.get(0));
        return "success";
    }

    /**
     * 手动推送收款计划到枫行梦
     *
     * @param incomePlanIdsF
     * @return
     */
    public Boolean manualPushIncomePlan2Fxm(IncomePlanIdsF incomePlanIdsF) {
        if (CollectionUtils.isEmpty(incomePlanIdsF.getPlanIds())) {
            return Boolean.TRUE;
        }
        //查收款计划
        List<ContractIncomePlanConcludeE> plans = this.listByIds(incomePlanIdsF.getPlanIds());
        if (CollectionUtils.isEmpty(plans)) {
            return Boolean.TRUE;
        }
        //转为map
        Map<String, List<ContractIncomePlanConcludeE>> contractPlanMap = plans.stream()
                .collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getContractId));
        //汇总合同id查合同信息
        List<String> contractIds = plans.stream()
                .map(ContractIncomePlanConcludeE::getContractId)
                .distinct()
                .collect(Collectors.toList());
        //查合同信息
        List<ContractIncomeConcludeE> incomeConcludes = contractIncomeConcludeService.listByIds(contractIds);
        //转为map
        Map<String, ContractIncomeConcludeE> incomeConcludeEMap = incomeConcludes.stream()
                .collect(Collectors.toMap(ContractIncomeConcludeE::getId, Function.identity(), (v1, v2) -> v1));
        if (Objects.isNull(incomePlanIdsF.getPushType())) {
            //开始推送收款计划核销金额
            List<ContractIncomeConcludePlanFxmReceiptRecordE> allRecords = new ArrayList<>();
            contractPlanMap.forEach((contractId, incomePlans) -> {
                List<ContractIncomeConcludePlanFxmReceiptRecordE> receiptRecords = contractInfoToFxmCommonService.pushIncomePlanReceipt2Fxm(incomePlans, incomeConcludeEMap.get(contractId));
                allRecords.addAll(receiptRecords);
            });
            if (CollectionUtils.isNotEmpty(allRecords)) {
                //先删除已有的推送记录，再保存新的
                receiptRecordService.remove(
                        Wrappers.<ContractIncomeConcludePlanFxmReceiptRecordE>lambdaQuery()
                                .in(ContractIncomeConcludePlanFxmReceiptRecordE::getAgreementBillId, incomePlanIdsF.getPlanIds()));
                receiptRecordService.saveBatch(allRecords);
            }
        } else {
            //开始推送收款计划
            List<ContractIncomeConcludePlanFxmRecordE> allRecords = new ArrayList<>();
            contractPlanMap.forEach((contractId, incomePlans) -> {
                List<ContractIncomeConcludePlanFxmRecordE> records = contractInfoToFxmCommonService.pushIncomePlan2Fxm(
                        incomePlans, incomeConcludeEMap.get(contractId), incomePlanIdsF.getPushType());
                allRecords.addAll(records);
            });

            if (CollectionUtils.isNotEmpty(allRecords)) {
                //先删除已有的推送记录，再保存新的
                contractIncomeConcludePlanFxmRecordService.remove(
                        Wrappers.<ContractIncomeConcludePlanFxmRecordE>lambdaQuery()
                                .in(ContractIncomeConcludePlanFxmRecordE::getAgreementBillId, incomePlanIdsF.getPlanIds()));
                contractIncomeConcludePlanFxmRecordService.saveBatch(allRecords);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 重新推送失败数据到枫行梦
     */
    @Transactional(rollbackFor = Exception.class)
    public void rePushFxmFailedData() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setGateway("saas");
        identityInfo.setTenantId("13554968497211");
        identityInfo.setUserId("system");
        identityInfo.setUserName("系统");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        //查询出推送失败的收款计划数据
        List<ContractIncomeConcludePlanFxmRecordE> planRecords = contractIncomeConcludePlanFxmRecordService.list(
                Wrappers.<ContractIncomeConcludePlanFxmRecordE>lambdaQuery()
                        .eq(ContractIncomeConcludePlanFxmRecordE::getPushStatus, 0)
                        .eq(ContractIncomeConcludePlanFxmRecordE::getDeleted, 0)
                        .eq(ContractIncomeConcludePlanFxmRecordE::getPlanType, PlanFxmType.INCOME.getName())
                        .last("limit 500"));
        pushFailedIncomePlans(planRecords);

        //查询出推送失败的收款计划核销金额数据
        List<ContractIncomeConcludePlanFxmReceiptRecordE> receiptRecords = receiptRecordService.list(
                Wrappers.<ContractIncomeConcludePlanFxmReceiptRecordE>lambdaQuery()
                        .eq(ContractIncomeConcludePlanFxmReceiptRecordE::getPushStatus, 0)
                        .eq(ContractIncomeConcludePlanFxmReceiptRecordE::getDeleted, 0)
                        .eq(ContractIncomeConcludePlanFxmReceiptRecordE::getPlanType, PlanFxmType.INCOME.getName())
                        .last("limit 500"));
        pushFailedIncomePlanReceipts(receiptRecords);
    }

    /**
     * 推送失败的收款计划数据
     *
     * @param planRecords
     */
    private void pushFailedIncomePlans(List<ContractIncomeConcludePlanFxmRecordE> planRecords) {
        if (CollectionUtils.isEmpty(planRecords)) {
            return;
        }
        Map<Integer, List<ContractIncomeConcludePlanFxmRecordE>> recordPushTypeMap = planRecords.stream()
                .collect(Collectors.groupingBy(ContractIncomeConcludePlanFxmRecordE::getPushType));
        //按照新增、更新、删除的顺序重新推送
        pushFxm(recordPushTypeMap.getOrDefault(PlanFxmPushType.INSERT.getCode(), new ArrayList<>()), PlanFxmPushType.INSERT);
        pushFxm(recordPushTypeMap.getOrDefault(PlanFxmPushType.UPDATE.getCode(), new ArrayList<>()), PlanFxmPushType.UPDATE);
        pushFxm(recordPushTypeMap.getOrDefault(PlanFxmPushType.DELETE.getCode(), new ArrayList<>()), PlanFxmPushType.DELETE);
    }

    /**
     * 推送枫行梦
     *
     * @param records
     * @param planFxmPushType
     */
    private void pushFxm(List<ContractIncomeConcludePlanFxmRecordE> records, PlanFxmPushType planFxmPushType) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        //汇总计划id
        List<String> planIds = records.stream()
                .map(ContractIncomeConcludePlanFxmRecordE::getAgreementBillId)
                .distinct()
                .collect(Collectors.toList());

        //进行推送
        if (Objects.nonNull(planFxmPushType)) {
            IncomePlanIdsF incomePlanIdsF = new IncomePlanIdsF();
            incomePlanIdsF.setPlanIds(planIds);
            incomePlanIdsF.setPushType(planFxmPushType);
            this.manualPushIncomePlan2Fxm(incomePlanIdsF);
        }
    }

    /**
     * 推送失败的收款计划核销金额数据
     *
     * @param planRecords
     */
    private void pushFailedIncomePlanReceipts(List<ContractIncomeConcludePlanFxmReceiptRecordE> planRecords) {
        if (CollectionUtils.isEmpty(planRecords)) {
            return;
        }
        //汇总收款计划id
        List<String> planIds = planRecords.stream()
                .map(ContractIncomeConcludePlanFxmReceiptRecordE::getAgreementBillId)
                .distinct()
                .collect(Collectors.toList());

        IncomePlanIdsF incomePlanIdsF = new IncomePlanIdsF();
        incomePlanIdsF.setPlanIds(planIds);
        this.manualPushIncomePlan2Fxm(incomePlanIdsF);
    }

    /**
     * 对比编辑接口执行前后的收款计划情况，推送枫行梦
     *
     * @param oldPlans
     * @param newPlans
     * @param concludeE
     */
    private void handleEditPlanPushFxm(List<ContractIncomePlanConcludeE> oldPlans, List<ContractIncomePlanConcludeE> newPlans, ContractIncomeConcludeE concludeE) {
        //以防万一，先统一将父级收款计划移除
        oldPlans.removeIf(plan -> "0".equals(plan.getPid()));
        newPlans.removeIf(plan -> "0".equals(plan.getPid()));
        log.info("开始处理收款计划编辑接口的推送,原收款计划信息:{},新收款计划信息:{}", JSON.toJSONString(oldPlans), JSON.toJSONString(newPlans));
        //转为map
        Map<String, ContractIncomePlanConcludeE> oldPlanMap = oldPlans.stream()
                .collect(Collectors.toMap(ContractIncomePlanConcludeE::getId, Function.identity(), (v1, v2) -> v1));
        Map<String, ContractIncomePlanConcludeE> newPlanMap = newPlans.stream()
                .collect(Collectors.toMap(ContractIncomePlanConcludeE::getId, Function.identity(), (v1, v2) -> v1));

        //1.旧的里面有，新的里面也有，说明是更新，直接按照新的执行更新推送
        List<String> bothHasPlanIds = newPlanMap.keySet().stream()
                .filter(oldPlanMap::containsKey)
                .collect(Collectors.toList());
        List<ContractIncomePlanConcludeE> updateList = new ArrayList<>();
        bothHasPlanIds.forEach(id -> {
            //添加到更新集合中
            updateList.add(newPlanMap.get(id));
            //移除，不参与后续处理
            newPlanMap.remove(id);
            newPlans.removeIf(plan-> id.equals(plan.getId()));
            oldPlanMap.remove(id);
            oldPlans.removeIf(plan-> id.equals(plan.getId()));
        });
        log.info("分类得到的updateList:{}", JSON.toJSONString(updateList));
        //2.旧的里面没有，新的里面有，说明是新增，加入insertList
        List<ContractIncomePlanConcludeE> insertList = newPlans.stream()
                .filter(plan -> !oldPlanMap.containsKey(plan.getId())).collect(Collectors.toList());
        log.info("分类得到的insertList:{}", JSON.toJSONString(insertList));
        //3.旧的里面有，新的里面没有，说明是删除，加入deleteList
        List<ContractIncomePlanConcludeE> deleteList = oldPlans.stream()
                .filter(plan -> !newPlanMap.containsKey(plan.getId())).collect(Collectors.toList());
        log.info("分类得到的deleteList:{}", JSON.toJSONString(deleteList));
        //推送新增
        List<ContractIncomeConcludePlanFxmRecordE> insertRecords = contractInfoToFxmCommonService.pushIncomePlan2Fxm(insertList, concludeE, PlanFxmPushType.INSERT);
        //推送更新
        List<ContractIncomeConcludePlanFxmRecordE> updateRecords = contractInfoToFxmCommonService.pushIncomePlan2Fxm(updateList, concludeE, PlanFxmPushType.UPDATE);
        //推送删除
        List<ContractIncomeConcludePlanFxmRecordE> deleteRecords = contractInfoToFxmCommonService.pushIncomePlan2Fxm(deleteList, concludeE, PlanFxmPushType.DELETE);
        insertRecords.addAll(updateRecords);
        insertRecords.addAll(deleteRecords);
        if (CollectionUtils.isNotEmpty(insertRecords)) {
            contractIncomeConcludePlanFxmRecordService.saveBatch(insertRecords);
        }
    }


    private boolean removeAll(Integer newSplitMode, Integer oldSplitMode, String contractId) {
        if (newSplitMode.equals(oldSplitMode)) {
            return false;
        }
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomePlanConcludeE::getContractId, contractId);
        queryWrapper.ne(ContractIncomePlanConcludeE::getPid, "0");
        this.remove(queryWrapper);
        return true;
    }

    private void removeFirst(List<ContractIncomePlanConcludeE> newContractPayPlanConcludeEList, String contractId,Integer splitMode, boolean removeAll) {
        if (removeAll) {
            return;
        }
        if (!SplitModeEnum.ACTUAL_SETTLE.getCode().equals(splitMode)) {
            return;
        }
        List<ContractIncomePlanConcludeE> list = this.lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getContractId,contractId)
                .eq(ContractIncomePlanConcludeE::getSettlePlanGroup,newContractPayPlanConcludeEList.get(0).getSettlePlanGroup())
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> oldList = newContractPayPlanConcludeEList.stream().map(ContractIncomePlanConcludeE::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oldList)) {
            return;
        }
        list.removeIf(l->oldList.contains(l.getId()));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        this.removeBatchByIds(list.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList()));
    }

    public PreIncomePlanV preSettlementPlan(PreIncomePlanQuery req) {
        PreIncomePlanV preIncomePlanV = new PreIncomePlanV();
        List<ContractIncomeFundE> list = incomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId())
                .eq(ContractIncomeFundE.CONTRACT_ID, req.getContractId()));
        if (CollectionUtils.isEmpty(list)) {
            return preIncomePlanV;
        }
        if (req.getEdit() && CollectionUtils.isNotEmpty(req.getExistContractPayFundIdList())) {
            list.removeIf(l->req.getExistContractPayFundIdList().contains(l.getId()));
        }
        if (CollectionUtils.isEmpty(list)) {
            return preIncomePlanV;
        }
        List<ContractIncomeConcludeE> contractPayConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            return preIncomePlanV;
        }
        List<PreIncomePlanDataV> preSettlePlanDataVList = new ArrayList<>();
        for (ContractIncomeFundE contractPayFundE : list) {
            int groupCounter = 1;
            String settlePlanGroup = UUID.randomUUID().toString();
            PreIncomePlanDataV preSettlePlanDataV = generatePlansForContract(contractPayFundE,contractPayConcludeEList.get(0),req.getSplitMode(),groupCounter,settlePlanGroup);
            groupCounter++;
            preIncomePlanV.setPreSettlePlanDataVList(preSettlePlanDataVList);
            preSettlePlanDataVList.add(preSettlePlanDataV);
        }
        if (CollectionUtils.isNotEmpty(preSettlePlanDataVList)) {
            preIncomePlanV.setTotalPlannedCollectionAmount(
                    preSettlePlanDataVList.stream()
                            .map(PreIncomePlanDataV::getGroupPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }
        preIncomePlanV.setPreSettlePlanDataVList(preSettlePlanDataVList);
        preIncomePlanV.setSplitMode(req.getSplitMode());
        return preIncomePlanV;
    }

    public List<PreIncomePlanDataV2> preSettlementPlanV2(String contractId) {
        log.info("preSettlementPlanV2 contractId:{}", contractId);
        // 获取合同所有清单项
        LambdaQueryWrapper<ContractIncomeFundE> payFundWrapper = new LambdaQueryWrapper<>();
        payFundWrapper.eq(ContractIncomeFundE::getContractId, contractId);
        payFundWrapper.eq(ContractIncomeFundE::getDeleted, 0);
        List<ContractIncomeFundE> incomeFundList = incomeFundService.list(payFundWrapper);
        if (CollectionUtils.isEmpty(incomeFundList)) {
            log.info("preSettlementPlanV2 contractId:{} incomeFundList is empty", contractId);
            return Collections.emptyList();
        }
        // 获取合同信息 后续使用
        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeService.getById(contractId);
        // 清单项转id map
        Map<String, ContractIncomeFundE> incomeFundIdMap = incomeFundList.stream()
                .collect(Collectors.toMap(ContractIncomeFundE::getId, Function.identity()));
        // 获取合同所有结算计划(父级数据无意义)
        List<ContractIncomePlanConcludeE> planEList = contractIncomePlanConcludeMapper.getEffectivePlan(contractId);
        List<IncomeConcludePlanV2> planV2List = Global.mapperFacade.mapAsList(planEList, IncomeConcludePlanV2.class);
        markUsed(planV2List);
        // 根据合同清单id转map
        Map<String, List<IncomeConcludePlanV2>> planMap = planV2List.stream()
                .collect(Collectors.groupingBy(IncomeConcludePlanV2::getContractPayFundId));

        //查询当前合同下是否含有补充协议
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<ContractIncomeConcludeE>()
                .eq(ContractIncomeConcludeE.PID, contractId)
                .eq(ContractIncomeConcludeE.CONTRACT_TYPE, 2)
                .eq(ContractIncomeConcludeE.REVIEW_STATUS, 2)
                .eq(ContractIncomeConcludeE.DELETED, 0);
        List<ContractIncomeConcludeE> supplyList = contractIncomeConcludeMapper.selectList(queryWrapper);

        Map<String, ContractIncomeFundE> existContractIncomeFundMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(supplyList)){
            //查询补充协议下的所有清单
            LambdaQueryWrapper<ContractIncomeFundE> paySupplyWrapper = new LambdaQueryWrapper<>();
            paySupplyWrapper.in(ContractIncomeFundE::getContractId, supplyList.stream().map(ContractIncomeConcludeE::getId).collect(Collectors.toList()));
            paySupplyWrapper.eq(ContractIncomeFundE::getDeleted, 0);
            List<ContractIncomeFundE> supplyPayFundList = incomeFundService.list(paySupplyWrapper);

            existContractIncomeFundMap = supplyPayFundList.stream()
                    .collect(Collectors.toMap(
                            this::generateCompositeKey,
                            fund -> fund,
                            (fund1, fund2) -> fund1
                    ));
        }

        // 清单id组装上级pojo, 内部的计划数据会在后面进行填充
        List<PreIncomePlanDataV2> preList = Lists.newArrayList();
        for (ContractIncomeFundE incomeFundE : incomeFundList) {
            PreIncomePlanDataV2 preIncomePlanDataV2 = new PreIncomePlanDataV2();
            preIncomePlanDataV2.setContractPayFundId(incomeFundE.getId());
            preIncomePlanDataV2.setChargeItem(incomeFundE.getChargeItem());
            preIncomePlanDataV2.setChargeItemId(incomeFundE.getChargeItemId().toString());
            preIncomePlanDataV2.setGroupPlannedCollectionAmount(incomeFundE.getAmount());
            preIncomePlanDataV2.setSplitMode(Integer.valueOf(incomeFundE.getPayWayId()));
            preIncomePlanDataV2.setSplitModeName(incomeFundE.getPayWay());
            preIncomePlanDataV2.setChargeMethodId(incomeFundE.getChargeMethodId());
            preIncomePlanDataV2.setChargeMethodName(incomeFundE.getChargeMethodName());
            preIncomePlanDataV2.setTaxRateId(incomeFundE.getTaxRateId());
            preIncomePlanDataV2.setTaxRate(incomeFundE.getTaxRate());
            preIncomePlanDataV2.setTypeId(incomeFundE.getTypeId());
            preIncomePlanDataV2.setType(incomeFundE.getType());
            //封装补充协议数据
            extractedSupplyData(existContractIncomeFundMap, incomeFundE, preIncomePlanDataV2);
            List<IncomeConcludePlanV2> curPlanV2List = planMap.get(incomeFundE.getId());
            if (CollectionUtils.isEmpty(curPlanV2List)) {
                continue;
            }
            curPlanV2List.sort(Comparator.comparing(IncomeConcludePlanV2::getTermDate));
            preIncomePlanDataV2.setPlanVList(curPlanV2List);
            preList.add(preIncomePlanDataV2);
        }
        // 处理 合同清单的增加/删除
        log.info("preIncomePlanV2:{} incomeFundStep start", contractId);
        handlePlanOnFundDeleted(incomeFundIdMap,planMap);
        handlePlanOnFundAdded(incomeFundIdMap,planMap,preList,contractIncomeConcludeE,existContractIncomeFundMap);
        log.info("preIncomePlanV2:{} incomeFundStep end", contractId);
        // 处理 合同的(开始/结束)时间修改
        log.info("preIncomePlanV2:{} timeStep start", contractId);
        LambdaQueryWrapper<ContractIncomePlanConcludeE> palnWrapper = new LambdaQueryWrapper<>();
        palnWrapper.eq(ContractIncomePlanConcludeE::getContractId,contractIncomeConcludeE.getId());
        palnWrapper.eq(ContractIncomePlanConcludeE::getDeleted,0);
        List<ContractIncomePlanConcludeE> planList = contractIncomePlanConcludeMapper.selectList(palnWrapper);
        handlePlanOnStartTime(preList, contractIncomeConcludeE,planList);
        handlePlanOnEndTime(preList, contractIncomeConcludeE,planList);
        log.info("preIncomePlanV2:{} timeStep end", contractId);
        // 处理 合同清单的金额和结算计划的金额 使其相等 20250417 17：07 下掉 by@YK1730084330JrsAA
        log.info("preIncomePlanV2:{} amountStep start", contractId);
//        handlePlanOnAmount(preList, incomeFundIdMap);
        log.info("preIncomePlanV2:{} amountStep end", contractId);
        return preList;
    }

    //封装补充协议数据
    private void extractedSupplyData(Map<String, ContractIncomeFundE> existContractIncomeFundMap, ContractIncomeFundE payFundE, PreIncomePlanDataV2 curDataV2) {
        String key = generateCompositeKey(payFundE);
        ContractIncomeFundE existFund = existContractIncomeFundMap.get(key);
        if(Objects.nonNull(existFund)){
            //查询补充合同基本信息
            ContractIncomeConcludeE incomeConclude = contractIncomeConcludeService.getById(existFund.getContractId());
            curDataV2.setSupplyContractId(existFund.getContractId());
            curDataV2.setSupplyStartTime(incomeConclude.getGmtExpireStart());
            curDataV2.setSupplyEndTime(incomeConclude.getGmtExpireEnd());
        }
    }

    private String generateCompositeKey(ContractIncomeFundE fund) {
        return fund.getChargeItemId() + "|" +
                nullToEmpty(fund.getType()) + "|" +
                nullToEmpty(fund.getPayWayId()) + "|" +
                nullToEmpty(fund.getTaxRateId()) + "|" +
                nullToEmpty(fund.getPayTypeId()) + "|" +
                (fund.getStandAmount() != null ? fund.getStandAmount().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getStandardId()) + "|" +
                (fund.getAmountNum() != null ? fund.getAmountNum().toPlainString() : "0") + "|" +
                (fund.getAmount() != null ? fund.getAmount().toPlainString() : "0") + "|" +
                (fund.getAmountWithOutRate() != null ? fund.getAmountWithOutRate().toPlainString() : "0") + "|" +
                (fund.getTaxRateAmount() != null ? fund.getTaxRateAmount().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getChargeMethodId()) ;
    }
    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private void handlePlanOnAmount(List<PreIncomePlanDataV2> preList,
                                    Map<String, ContractIncomeFundE> incomeFundIdMap) {
        if (CollectionUtils.isEmpty(preList) || MapUtils.isEmpty(incomeFundIdMap)){
            return;
        }
        for (PreIncomePlanDataV2 preIncomePlanDataV2 : preList) {
            List<IncomeConcludePlanV2> planV2List = preIncomePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常-金额拆分步骤异常");
            }
            ContractIncomeFundE incomeFundE = incomeFundIdMap.get(preIncomePlanDataV2.getContractPayFundId());
            // 四舍五入清单的总金额,保留2位小数，清单的金额有点问题
            BigDecimal groupPlannedCollectionAmount = incomeFundE.getAmount().setScale(2, RoundingMode.HALF_UP);
            BigDecimal groupUsedAmount = BigDecimal.ZERO;
            int groupUsedCount = 0;
            for (IncomeConcludePlanV2 planV2 : planV2List) {
                if (planV2.getUsed()){
                    groupUsedAmount = groupUsedAmount.add(planV2.getPlannedCollectionAmount());
                    groupUsedCount++;
                }
            }
            if (groupUsedAmount.compareTo(groupPlannedCollectionAmount) > 0){
                throw new OwlBizException("已使用的清单收款计划金额大于合同清单金额，请联系区域负责人");
            }
            // 若全部被使用，直接不处理
            if (groupUsedCount >= planV2List.size() || groupUsedAmount.compareTo(groupPlannedCollectionAmount) == 0){
                continue;
            }
            // 重新分布金额
            BigDecimal amount = groupPlannedCollectionAmount.subtract(groupUsedAmount);
            int totalNoUsedCount = planV2List.size() - groupUsedCount;
            BigDecimal amountPer = amount.divide(new BigDecimal(totalNoUsedCount), 2, RoundingMode.HALF_UP);
            int curCount = 0;
            BigDecimal baseRatio = BigDecimal.ZERO;
            for (IncomeConcludePlanV2 planV2 : planV2List) {
                if (planV2.getUsed()){
                    BigDecimal curRatio = planV2.getPlannedCollectionAmount()
                            .divide(groupPlannedCollectionAmount, 4, RoundingMode.HALF_UP);
                    BigDecimal ratioVal = curRatio.compareTo(baseRatio) > 0 ? baseRatio : curRatio;
                    planV2.setRatioAmount(ratioVal.multiply(BigDecimal.valueOf(100)));
                    if (curRatio.compareTo(baseRatio) > 0){
                        baseRatio = BigDecimal.ZERO;
                    } else {
                        baseRatio = baseRatio.subtract(curRatio);
                    }
                    continue;
                }
                curCount++;
                String taxRateStr = incomeFundE.getTaxRate();
                final boolean isDifferenceTax = "差额纳税".equals(taxRateStr);
                BigDecimal taxRate = isDifferenceTax ? null : parseTaxRate(taxRateStr);
                if (curCount < totalNoUsedCount){
                    planV2.setPlannedCollectionAmount(amountPer);
                    // 不含税金额 税额重新计算
                    if (!isDifferenceTax) {
                        BigDecimal noTaxAmount = amountPer.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
                        BigDecimal taxAmount = amountPer.subtract(noTaxAmount);
                        planV2.setNoTaxAmount(noTaxAmount);
                        planV2.setTaxAmount(taxAmount);
                    }
                    amount = amount.subtract(amountPer);
                }else {
                    planV2.setPlannedCollectionAmount(amount);
                    if (!isDifferenceTax) {
                        BigDecimal noTaxAmount = amount.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
                        BigDecimal taxAmount = amount.subtract(noTaxAmount);
                        planV2.setNoTaxAmount(noTaxAmount);
                        planV2.setTaxAmount(taxAmount);
                    }
                }

                BigDecimal curRatio = planV2.getPlannedCollectionAmount()
                        .divide(groupPlannedCollectionAmount, 4, RoundingMode.HALF_UP);
                BigDecimal ratioVal = curRatio.compareTo(baseRatio) > 0 ? baseRatio : curRatio;
                planV2.setRatioAmount(ratioVal.multiply(BigDecimal.valueOf(100)));
                if (curRatio.compareTo(baseRatio) > 0){
                    baseRatio = BigDecimal.ZERO;
                } else {
                    baseRatio = baseRatio.subtract(curRatio);
                }
            }
            if (baseRatio.compareTo(BigDecimal.ZERO) > 0){
                IncomeConcludePlanV2 planV2 = planV2List.get(planV2List.size() - 1);
                planV2.setRatioAmount(planV2.getRatioAmount()
                        .add(baseRatio.multiply(BigDecimal.valueOf(100))));
            }
        }
    }

    private void handlePlanOnEndTime(List<PreIncomePlanDataV2> preList,
                                     ContractIncomeConcludeE contractIncomeConcludeE,
                                     List<ContractIncomePlanConcludeE> planList) {
        LocalDate contractEnd = contractIncomeConcludeE.getGmtExpireEnd();
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(contractIncomeConcludeE.getId());
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        Boolean isZz = Boolean.FALSE;
        if(StringUtils.isNotEmpty(concludeExpandV.getConmanagetype())) {
            if(ContractBusinessLineEnum.物管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine()) && ContractIncomeManageTypeEnum.增值类合同.getCode().toString().equals(concludeExpandV.getConmanagetype())){
                isZz = Boolean.TRUE;
            }
        }
        for (PreIncomePlanDataV2 preIncomePlanDataV2 : preList) {
            planList = planList.stream()
                    .filter(plan ->plan.getChargeItemId() != null && preIncomePlanDataV2.getChargeItemId().equals(plan.getChargeItemId().toString()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(planList)){
                continue;
            }
            if(Objects.nonNull(preIncomePlanDataV2.getSupplyEndTime())){
                contractEnd = preIncomePlanDataV2.getSupplyEndTime();
            }
            List<IncomeConcludePlanV2> planV2List = preIncomePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常");
            }
            // 拿到最大的结束时间
            LocalDate curGroupEnd = planV2List.stream()
                    .map(IncomeConcludePlanV2::getCostEndTime)
                    .max(Comparator.comparing(LocalDate::toEpochDay))
                    .orElse(null);
            if (Objects.isNull(curGroupEnd)){
                throw new OwlBizException("流程数据处理异常");
            }
            // case1: 组内开始=合同开始 不做处理
            if (curGroupEnd.equals(contractEnd)){
                log.info("case1: 组内开始=合同开始 不做处理");
                continue;
            }
            planV2List.sort(Comparator.comparing(IncomeConcludePlanV2::getTermDate));
            // case2: 组内结束>合同结束，说明合同的结束时间前移, 锁定合同的结束时间落在哪个区间内
            if (curGroupEnd.isAfter(contractEnd)){
                log.info("case2: 组内结束>合同结束，说明合同的结束时间前移");
                int idx = -1;
                for (int i = planV2List.size()-1; i >= 0; i--) {
                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
                    if (!planV2.getCostStartTime().isAfter(contractEnd) && !planV2.getCostEndTime().isBefore(contractEnd)){
                        idx = i;
                        // 本期结束时间=min(合同结束时间，本期结束时间)
                        LocalDate curEndTime = contractEnd.isBefore(planV2.getCostEndTime()) ? contractEnd : planV2.getCostEndTime();
                        planV2.setCostEndTime(curEndTime);
                        //planV2.setPlannedCollectionTime(curEndTime.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()));
                        if(isZz){
                            planV2.setPlannedCollectionTime(planV2.getCostStartTime());
                        }else{
                            planV2.setPlannedCollectionTime(curEndTime.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()));
                        }

                        break;
                    }
                }
                /*for (int i = planV2List.size()-1; i > idx; i--) {
                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
                    if (planV2.getUsed()){
                        throw new OwlBizException("存在时间大于合同结束时间的收款计划被使用，请联系区域负责人");
                    }
                }*/
                // 仅保留开始到idx到结束的计划
                planV2List = planV2List.subList(0, idx+1);
                preIncomePlanDataV2.setPlanVList(planV2List);
            }
            // case3: 组内结束<合同结束, 说明合同的结束时间后移, 20240417 16:13 不要了 by@qd_shenbaocun1
            if (curGroupEnd.isBefore(contractEnd)){
                return;
//                // 若组内已使用金额大于等于清单金额，则不处理
//                BigDecimal totalUsedAmount = planV2List.stream()
//                        .filter(IncomeConcludePlanV2::getUsed)
//                        .map(IncomeConcludePlanV2::getPlannedCollectionAmount)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                if (totalUsedAmount.compareTo(payFundE.getAmount()) >= 0){
//                    log.info("组内金额已全部使用，不进行时间额外处理");
//                    return;
//                }
//                // 取第最后一期
//                IncomeConcludePlanV2 lastPlanV2 = planV2List.get(planV2List.size()-1);
//                List<IncomeConcludePlanV2> addedPlanV2List = null;
//                int maxTermDate = planV2List.stream()
//                        .mapToInt(IncomeConcludePlanV2::getTermDate)
//                        .max()
//                        .orElse(0);
//                if (lastPlanV2.getUsed()){
//                    // 若最后一期被使用, 按照组内的结束时间的下一天到新的结束时间新增拆分内容, 此处先不管金额, 后续会重新进行分布，期数递增
//                    addedPlanV2List = doSplitV2(curGroupEnd.plusDays(1),
//                            contractEnd,
//                            payFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            payFundE.getTaxRate(),
//                            payFundE,
//                            contractIncomeConcludeE,
//                            maxTermDate+1);
//                } else {
//                    // 若最后一期没有被使用，按照最后一期的开始时间到新的结束时间新增拆分内容, 此处先不管金额, 后续会重新进行分布，期数递增(占据最后一期)
//                    addedPlanV2List = doSplitV2(lastPlanV2.getCostStartTime(),
//                            contractEnd,
//                            payFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            payFundE.getTaxRate(),
//                            payFundE,
//                            contractIncomeConcludeE,
//                            maxTermDate);
//                    planV2List.remove(lastPlanV2);
//                }
//                planV2List.addAll(addedPlanV2List);
//                // 按照期数重新排序
//                planV2List.sort(Comparator.comparing(IncomeConcludePlanV2::getTermDate));
            }

        }
    }

    private void handlePlanOnStartTime(List<PreIncomePlanDataV2> preList,
                                       ContractIncomeConcludeE contractIncomeConcludeE,
                                       List<ContractIncomePlanConcludeE> planList) {
        LocalDate contractStart = contractIncomeConcludeE.getGmtExpireStart();
        for (PreIncomePlanDataV2 preIncomePlanDataV2 : preList) {
            planList = planList.stream()
                    .filter(plan -> plan.getChargeItemId() !=null && preIncomePlanDataV2.getChargeItemId().equals(plan.getChargeItemId().toString()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(planList)){
                continue;
            }
            if(Objects.nonNull(preIncomePlanDataV2.getSupplyStartTime())){
                contractStart = preIncomePlanDataV2.getSupplyStartTime();
            }
            List<IncomeConcludePlanV2> planV2List = preIncomePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常");
            }
            // 拿到最早的开始时间
            LocalDate curGroupStart = planV2List.stream()
                    .map(IncomeConcludePlanV2::getCostStartTime)
                    .min(Comparator.comparing(LocalDate::toEpochDay))
                    .orElse(null);
            if (Objects.isNull(curGroupStart)){
                throw new OwlBizException("流程数据处理异常");
            }
            // case1: 组内开始=合同开始 不做处理
            if (curGroupStart.equals(contractStart)){
                log.info("合同开始时间与收款计划开始时间一致，无需处理");
                continue;
            }
            planV2List.sort(Comparator.comparing(IncomeConcludePlanV2::getTermDate));
            // case2: 组内开始<合同开始，说明合同的开始时间后移, 锁定合同的开始时间落在哪个区间内
            if (curGroupStart.isBefore(contractStart)){
                log.info("合同开始时间与收款计划开始时间不一致，合同开始时间后移");
                int idx = -1;
                for (int i = 0; i < planV2List.size(); i++) {
                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
                    if (!planV2.getCostStartTime().isAfter(contractStart) && !planV2.getCostEndTime().isBefore(contractStart)){
                        idx = i;
                        // 开始时间=max(合同新的开始时间, 本期的开始时间)
                        planV2.setCostStartTime(contractStart.isAfter(planV2.getCostStartTime())? contractStart : planV2.getCostStartTime());
                        break;
                    }
                }
                /*for (int i = 0; i < idx; i++) {
                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
                    if (planV2.getUsed()){
                        throw new OwlBizException("存在时间小于合同开始时间的收款计划被使用，请联系区域负责人");
                    }
                }*/
                if(idx == -1){
                    return;
                }
                // 仅保留idx到结束的计划
                planV2List = planV2List.subList(idx, planV2List.size());
                // 期数从1开始重新计数
                for (int i = 0; i < planV2List.size(); i++) {
                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
                    planV2.setTermDate(i+1);
                }
                preIncomePlanDataV2.setPlanVList(planV2List);
            }
            // case3: 组内开始>合同开始, 说明合同的开始时间前移 20240417 16:13 不要了 by@qd_shenbaocun1
            if (curGroupStart.isAfter(contractStart)){
                return;
//                log.info("合同开始时间与收款计划开始时间不一致，合同开始时间前移");
//                // 若组内已使用金额大于等于清单金额，则不处理
//                BigDecimal totalUsedAmount = planV2List.stream()
//                        .filter(IncomeConcludePlanV2::getUsed)
//                        .map(IncomeConcludePlanV2::getPlannedCollectionAmount)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                if (totalUsedAmount.compareTo(incomeFundE.getAmount()) >= 0){
//                    log.info("组内金额已全部使用，不进行时间额外处理");
//                    return;
//                }
//                // 取第一期
//                IncomeConcludePlanV2 firstPlanV2 = planV2List.get(0);
//                List<IncomeConcludePlanV2> addedPlanV2List = null;
//                if (firstPlanV2.getUsed()){
//                    // 若第一期被使用, 按照合同的开始时间到原来开始时间的前一天新增拆分内容, 此处先不管金额, 后续会重新进行分布
//                    addedPlanV2List = doSplitV2(contractStart,
//                            curGroupStart.plusDays(-1),
//                            incomeFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            incomeFundE.getTaxRate(),
//                            incomeFundE,
//                            contractIncomeConcludeE,
//                            1);
//                } else {
//                    // 第一期没有使用,按照合同的开始时间到原来第一期的结束时间拆分新内容, 此处先不管金额，后续会重新进行分布
//                    addedPlanV2List = doSplitV2(contractStart,
//                            firstPlanV2.getCostEndTime(),
//                            incomeFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            incomeFundE.getTaxRate(),
//                            incomeFundE,
//                            contractIncomeConcludeE,
//                            1);
//                    planV2List.remove(firstPlanV2);
//                }
//                // 获取新增的最大的期数，原期数在此基础上递增
//                int maxTermDate = addedPlanV2List.stream()
//                        .mapToInt(IncomeConcludePlanV2::getTermDate)
//                        .max()
//                        .orElse(0);
//                for (int i = 0; i < planV2List.size(); i++) {
//                    IncomeConcludePlanV2 planV2 = planV2List.get(i);
//                    planV2.setTermDate(maxTermDate+i+1);
//                }
//                planV2List.addAll(addedPlanV2List);
//                // 按照期数重新排序
//                planV2List.sort(Comparator.comparing(IncomeConcludePlanV2::getTermDate));
            }

        }
    }

    /**
     * 若计划不存在，清单存在，代表清单新增，需要新增计划
     **/
    private void handlePlanOnFundAdded(Map<String, ContractIncomeFundE> incomeFundIdMap,
                                       Map<String, List<IncomeConcludePlanV2>> planMap,
                                       List<PreIncomePlanDataV2> preList,
                                       ContractIncomeConcludeE contractIncomeConcludeE,
                                       Map<String, ContractIncomeFundE> existContractIncomeFundMap) {
        if (MapUtils.isEmpty(incomeFundIdMap)){
            log.info("合同清单项为空,无法处理清单项新增");
            return;
        }
        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> querySettWrapper = new LambdaQueryWrapper<>();
        querySettWrapper.eq(ContractIncomeSettlementConcludeE::getContractId, contractIncomeConcludeE.getId())
                .ne(ContractIncomeSettlementConcludeE::getPid,"0")
                .in(ContractIncomeSettlementConcludeE::getReviewStatus, ReviewStatusEnum.已通过.getCode(),ReviewStatusEnum.审批中.getCode())
                .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
        List<ContractIncomeSettlementConcludeE> concludeSettList = contractIncomeSettlementConcludeMapper.selectList(querySettWrapper);
        List<String> planIdList = new ArrayList<>();
        planIdList.add("计划ID");
        if(CollectionUtils.isNotEmpty(concludeSettList)){
            concludeSettList.forEach(x-> planIdList.addAll(contractIncomeSettlementConcludeMapper.getPlanBySettlement(x.getId()))
            );
        }
    /*    List<ContractIncomeFundE> funList = incomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId())
                .notIn(ContractIncomeFundE.ID, planIdList)
                .eq(ContractIncomeFundE.CONTRACT_ID, contractIncomeConcludeE.getId()));
*/

        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContractIncomePlanConcludeE::getContractId, contractIncomeConcludeE.getId())
                .ne(ContractIncomePlanConcludeE::getPid,"0")
                .notIn(ContractIncomePlanConcludeE::getId, planIdList)
                .eq(ContractIncomePlanConcludeE::getDeleted,0)
                .orderByAsc(ContractIncomePlanConcludeE:: getTermDate);
        List<ContractIncomePlanConcludeE> concludeEList = contractIncomePlanConcludeMapper.selectList(queryWrapper1);

        for (Map.Entry<String, ContractIncomeFundE> entry : incomeFundIdMap.entrySet()) {
            if (planMap.containsKey(entry.getKey())){
                continue;
            }
            log.info("合同清单项新增,新增计划,清单项id={}", entry.getKey());
            ContractIncomeFundE incomeFundE = entry.getValue();
            List<IncomeConcludePlanV2> curV2List = splitByFundNew(incomeFundE,this.getContractData(existContractIncomeFundMap, incomeFundE, contractIncomeConcludeE,concludeEList));
            PreIncomePlanDataV2 curDataV2 = new PreIncomePlanDataV2();
            curDataV2.setContractPayFundId(incomeFundE.getId());
            curDataV2.setChargeItem(incomeFundE.getChargeItem());
            curDataV2.setChargeItemId(incomeFundE.getChargeItemId());
            curDataV2.setGroupPlannedCollectionAmount(incomeFundE.getAmount());
            curDataV2.setSplitMode(Integer.valueOf(incomeFundE.getPayWayId()));
            curDataV2.setSplitModeName(incomeFundE.getPayWay());
            curDataV2.setChargeMethodId(incomeFundE.getChargeMethodId());
            curDataV2.setChargeMethodName(incomeFundE.getChargeMethodName());
            curDataV2.setChargeMethodId(incomeFundE.getChargeMethodId());
            curDataV2.setChargeMethodName(incomeFundE.getChargeMethodName());
            curDataV2.setTaxRateId(incomeFundE.getTaxRateId());
            curDataV2.setTaxRate(incomeFundE.getTaxRate());
            curDataV2.setTypeId(incomeFundE.getTypeId());
            curDataV2.setType(incomeFundE.getType());
            curDataV2.setPlanVList(curV2List);
            //封装补充协议数据
            extractedSupplyData(existContractIncomeFundMap, incomeFundE, curDataV2);
            preList.add(curDataV2);
        }
    }

    private ContractIncomeConcludeE getContractData(Map<String, ContractIncomeFundE> existContractIncomeFundMap, ContractIncomeFundE payFundE, ContractIncomeConcludeE contractIncomeConcludeE,List<ContractIncomePlanConcludeE> concludeEList) {
        ContractIncomeConcludeE incomeNew = new ContractIncomeConcludeE();
        BeanUtils.copyProperties(contractIncomeConcludeE, incomeNew);
        String key = generateCompositeKey(payFundE);
        ContractIncomeFundE existFund = existContractIncomeFundMap.get(key);
        if(Objects.nonNull(existFund)){
            //查询补充合同基本信息
            ContractIncomeConcludeE payConclude = contractIncomeConcludeService.getById(existFund.getContractId());
            incomeNew.setGmtExpireStart(payConclude.getGmtExpireStart());
            incomeNew.setGmtExpireEnd(payConclude.getGmtExpireEnd());
        }
        //修正清单开始时间重新计算
        if(StringUtils.isNotEmpty(payFundE.getMainId()) && CollectionUtils.isNotEmpty(concludeEList)){
            incomeNew.setGmtExpireStart(concludeEList.get(0).getCostStartTime());
        }
        return incomeNew;
    }

    private List<IncomeConcludePlanV2> splitByFundNew(ContractIncomeFundE incomeFundE,
                                                      ContractIncomeConcludeE contractIncomeConcludeE) {
        // 按照指定的开始时间、结束时间, 金额，税率，进行拆分, 同时复制部分信息
        return doSplitV2(contractIncomeConcludeE.getGmtExpireStart(),
                contractIncomeConcludeE.getGmtExpireEnd(),
                incomeFundE.getPayWayId(),
                incomeFundE.getAmount(),
                incomeFundE.getTaxRate(),
                incomeFundE,
                contractIncomeConcludeE,
                1);
    }

    private List<IncomeConcludePlanV2> doSplitV2(LocalDate start,
                                                 LocalDate end,
                                                 String payWayId,
                                                 BigDecimal amount,
                                                 String taxRateStr,
                                                 ContractIncomeFundE incomeFundE,
                                                 ContractIncomeConcludeE contractIncomeConcludeE,
                                                 int startPeriod) {
        SplitModeEnum splitModeEnum = SplitModeEnum.parseByPayWayId(payWayId);
        Integer intervalMonths = splitModeEnum.getInterval();
        // 处理差额纳税的特殊标记
        final boolean isDifferenceTax = "差额纳税".equals(taxRateStr);
        BigDecimal taxRate = isDifferenceTax ? null : parseTaxRate(taxRateStr);
        if (intervalMonths == 0) {
            return Collections.singletonList(buildSplitPlanV2(start,
                    end,
                    amount,
                    isDifferenceTax,
                    taxRate,
                    startPeriod,
                    BigDecimal.ONE,
                    incomeFundE,
                    contractIncomeConcludeE));
        }
        CommonRangeAmountBO rangrAmount = commonRangeAmountService.getRangeAmount(splitModeEnum.getCode(),start,end,amount);
        List<IncomeConcludePlanV2> results = new ArrayList<>();
        long totalMonths = DateTimeUtil.getBetweenMonth(start, end);
        int totalPeriods = (int) Math.ceil((double) totalMonths / intervalMonths);

        BigDecimal periodAmount = amount.divide(
                BigDecimal.valueOf(totalPeriods), 2, RoundingMode.HALF_UP);
        BigDecimal remainingAmount = amount;
        LocalDate currentStart = start;
        // 期数递增、金额、比例都走尾差
        BigDecimal totalRatio = BigDecimal.ONE;
        for (int periodCount = 0; periodCount < totalPeriods; periodCount++) {
            int currentPeriod = startPeriod + periodCount; // 动态计算当前期数
            LocalDate currentEnd = currentStart.plusMonths(intervalMonths-1).with(TemporalAdjusters.lastDayOfMonth());
            if (currentEnd.isAfter(end)) {
                currentEnd = end;
            }
            boolean isLast = (periodCount == totalPeriods - 1);
            //BigDecimal currentAmount = isLast ? remainingAmount : periodAmount;
            BigDecimal currentAmount = isLast ? rangrAmount.getEndMonthAmount() : periodCount == 0 ? rangrAmount.getStartMonthAmount() : rangrAmount.getAvgMonthAmount();
            // 后续要*100，所以保留4位
            BigDecimal ratio = isLast ? totalRatio : currentAmount.divide(amount, 4, RoundingMode.HALF_UP);
            if (!isLast){
                totalRatio = totalRatio.subtract(ratio);
            }
            results.add(buildSplitPlanV2(currentStart,
                    currentEnd,
                    currentAmount,
                    isDifferenceTax,
                    taxRate,
                    currentPeriod,
                    ratio,
                    incomeFundE,
                    contractIncomeConcludeE));

            //remainingAmount = remainingAmount.subtract(periodAmount);
            currentStart = currentEnd.plusDays(1);
        }
        return results;
    }

    /**
     * 拼接子级plan信息
     **/
    private IncomeConcludePlanV2 buildSplitPlanV2(LocalDate start,
                                                  LocalDate end,
                                                  BigDecimal amount,
                                                  boolean isDifferenceTax,
                                                  BigDecimal taxRate,
                                                  int curPeriod,
                                                  BigDecimal ratio,
                                                  ContractIncomeFundE incomeFundE,
                                                  ContractIncomeConcludeE contractIncomeConcludeE) {
        BigDecimal taxAmount = null;
        BigDecimal noTaxAmount = null;
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(contractIncomeConcludeE.getId());
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        Boolean isZz = Boolean.FALSE;
        if(StringUtils.isNotEmpty(concludeExpandV.getConmanagetype())) {
            if(ContractBusinessLineEnum.物管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine()) && ContractIncomeManageTypeEnum.增值类合同.getCode().toString().equals(concludeExpandV.getConmanagetype())){
                isZz = Boolean.TRUE;
            }
        }

        if (!isDifferenceTax) {
            noTaxAmount = amount.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
            taxAmount = amount.subtract(noTaxAmount);
        }
        IncomeConcludePlanV2 planV2 = new IncomeConcludePlanV2();
        planV2.setContractPayFundId(contractIncomeConcludeE.getId());
        planV2.setContractId(contractIncomeConcludeE.getId());
        planV2.setCustomer(contractIncomeConcludeE.getOppositeOneId());
        planV2.setCustomerName(contractIncomeConcludeE.getOppositeOne());
        planV2.setContractNo(contractIncomeConcludeE.getContractNo());
        planV2.setContractName(contractIncomeConcludeE.getName());
        planV2.setTermDate(curPeriod);

        planV2.setPlannedCollectionAmount(amount);
        planV2.setConfirmedAmountReceivedTemp(amount);
        planV2.setTaxAmount(taxAmount);
        planV2.setNoTaxAmount(noTaxAmount);

        planV2.setCostStartTime(start);
        planV2.setCostEndTime(end);
//        LocalDate planCollectionTime = end.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate planCollectionTime = end.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        if(isZz){
            planCollectionTime = start;
        }
        planV2.setPlannedCollectionTime(planCollectionTime);
        planV2.setPtMax(planCollectionTime);
        planV2.setPtMin(planCollectionTime);

        String costEstimationCode = generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,
                curPeriod,
                planCollectionTime.getYear() + "-" + planCollectionTime.getMonthValue(),
                contractIncomeConcludeE.getConmaincode());
        planV2.setCostEstimationCode(costEstimationCode);

        planV2.setSettlePlanGroup(incomeFundE.getId());
        planV2.setContractPayFundId(incomeFundE.getId());
        planV2.setReviewStatus(2);
        planV2.setSplitMode(SplitModeEnum.parseByPayWayId(incomeFundE.getPayWayId()).getCode());

        planV2.setRatioAmount(ratio.multiply(BigDecimal.valueOf(100)));
        planV2.setServiceType(Integer.valueOf(incomeFundE.getTypeId()));
        planV2.setChargeItemId(Long.valueOf(incomeFundE.getChargeItemId()));
        planV2.setChargeItem(incomeFundE.getChargeItem());
        planV2.setTaxRateId(Long.valueOf(incomeFundE.getTaxRateId()));
        planV2.setTaxRate((StringUtils.isNotBlank(incomeFundE.getTaxRate()) ? incomeFundE.getTaxRate().split("%")[0] : null));
        // used走到这里肯定是false
        planV2.setUsed(false);
        return planV2;

    }

    /**
     * 若计划存在，清单不存在，代表清单被删除了，需要删除计划
     **/
    private void handlePlanOnFundDeleted(Map<String, ContractIncomeFundE> incomeFundIdMap,
                                         Map<String, List<IncomeConcludePlanV2>> planMap) {
        if (MapUtils.isEmpty(planMap)){
            log.info("当前无收款计划,走收款计划新生成步骤");
            return;
        }
        List<String> toRemovePlanIdList = new ArrayList<>();
        for (Map.Entry<String, List<IncomeConcludePlanV2>> entry : planMap.entrySet()) {
            if (incomeFundIdMap.containsKey(entry.getKey())){
                continue;
            }
            for (IncomeConcludePlanV2 planV2 : entry.getValue()) {
                if (planV2.getUsed()){
                    throw new OwlBizException("已使用合同清单项不存在，请联系区域负责人");
                }
            }
            log.info("当前收款计划的合同清单被删除，删除结算计划：{}", entry.getKey());
            toRemovePlanIdList.add(entry.getKey());
        }
        if (CollectionUtils.isNotEmpty(toRemovePlanIdList)){
            for (String planId : toRemovePlanIdList) {
                planMap.remove(planId);
            }
        }
    }

    public void markUsed(List<IncomeConcludePlanV2> planVList) {
        if (CollectionUtils.isEmpty(planVList)){
            return;
        }
        List<String> planIdList = planVList.stream().map(IncomeConcludePlanV2::getId).collect(Collectors.toList());
        // 被结算单使用的结算计划id
        List<String> usedPlanIdListOnSettlement = contractIncomePlanConcludeMapper.getUsedPlanIdListOnSettlement(planIdList);
        // 生成成本计划的结算计划id
        List<String> usedPlanIdListOnPayCostPlan = contractIncomePlanConcludeMapper.getUsedPlanIdListOnPayCostPlan(planIdList);
        for (IncomeConcludePlanV2 v2 : planVList) {
            if (usedPlanIdListOnPayCostPlan.contains(v2.getId()) || usedPlanIdListOnSettlement.contains(v2.getId())){
                v2.setUsed(true);
            }
        }
    }

    public PreIncomePlanV details(IncomePlanDetailQuery req) {
        PreIncomePlanV preSettlePlanV = new PreIncomePlanV();
        List<ContractIncomeConcludeE> contractPayConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            throw new IllegalArgumentException("合同不存在");
        }
        //step1 先获取历史结算计划
        List<ContractIncomePlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getContractId,req.getContractId())
                .list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            throw new OwlBizException("合同计划不存在");
        }
        List<ContractIncomePlanConcludeE> parentContractPayPlanList = contractPayPlanConcludeEList.stream().filter(c->"0".equals(c.getPid())).collect(Collectors.toList());
        List<ContractIncomePlanConcludeE> planList = contractPayPlanConcludeEList.stream().filter(c->!"0".equals(c.getPid())).collect(Collectors.toList());
        //step2 获取最新的合同清单，如果有新增合同清单则需要预生成计划组数据
        PreIncomePlanQuery pre = new PreIncomePlanQuery();
        pre.setContractId(req.getContractId());
        pre.setSplitMode(parentContractPayPlanList.get(0).getSplitMode());
        pre.setExistContractPayFundIdList(planList.stream().map(ContractIncomePlanConcludeE::getContractPayFundId).collect(Collectors.toList()));
        if (1 == req.getSource()) {
            pre.setEdit(true);
            pre.setSplitMode(planList.get(0).getSplitMode());
            preSettlePlanV = preSettlementPlan(pre);
        }
        preSettlePlanV.setSplitMode(planList.get(0).getSplitMode());
        Map<String,List<ContractIncomePlanConcludeE>> contractPayPlanMap = planList.stream().collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getSettlePlanGroup));
        rebuildPreSettlePlanV(preSettlePlanV,contractPayPlanMap);
        List<PreIncomePlanDataV> preSettlePlanDataVList = preSettlePlanV.getPreSettlePlanDataVList();
        if (CollectionUtils.isNotEmpty(preSettlePlanDataVList)) {
            preSettlePlanV.setTotalPlannedCollectionAmount(
                    preSettlePlanDataVList.stream()
                            .map(PreIncomePlanDataV::getGroupPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        };
        orderTerms(preSettlePlanV);
        return preSettlePlanV;
    }

    public PageV<IncomePlanListV> listPlan(PageF<SearchF<IncomePlanListQuery>> form) {
        //合同业务线
        Field lineSelectField = form.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                form.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
            }
        }
        Page<IncomePlanListQuery> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<IncomePlanListQuery> queryModel = form.getConditions().getQueryModel();
        //注意：增加收款计划逻辑删除条件
        queryModel.eq("ccp.deleted", 0);
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }

        IPage<IncomePlanListV> pageList = contractIncomePlanConcludeMapper.listPlan(pageF, planConditionPage(form,queryModel, getIdentityInfo().get().getTenantId()));
        if (null == pageList || CollectionUtils.isEmpty(pageList.getRecords())) {
            return PageV.of(form, 0, Collections.emptyList());
        }
        List<IncomePlanListV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(IncomePlanListV::getPid).collect(Collectors.toList());
        QueryWrapper<IncomePlanListQuery> newQueryModel = planConditionPage(form,queryModel, getIdentityInfo().get().getTenantId());
        List<IncomePlanListV> concludeVList = contractIncomePlanConcludeMapper.queryByNewPath(newQueryModel, parentIdList, getIdentityInfo().get().getTenantId());
        if (CollectionUtils.isNotEmpty(concludeVList)) {
            Map<String, IncomePlanListV> concludeVMap = concludeVList.stream().collect(Collectors.toMap(IncomePlanListV::getId, Function.identity(), (v1, v2) -> v1));
            List<String> planIdList = checkSettleStatus(concludeVList.stream().map(IncomePlanListV::getId).collect(Collectors.toList()));
            Set<String> canNotEditSet = new HashSet<>();
            Map<String,ContractIncomeFundE> contractIncomeFundEMap = new HashMap<>();
            List<ContractIncomeFundE> contractIncomeFundEList = incomeFundService.lambdaQuery().in(ContractIncomeFundE::getId,concludeVList.stream().map(IncomePlanListV::getContractPayFundId).collect(Collectors.toList())).list();
            if (!CollectionUtils.isEmpty(contractIncomeFundEList)) {
                contractIncomeFundEMap = contractIncomeFundEList.stream().collect(Collectors.toMap(ContractIncomeFundE::getId,v->v,(v1,v2)->v1));
            }
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), null);
            Map<String,DictionaryCode> dicMap = value.stream().collect(Collectors.toMap(DictionaryCode::getCode,v->v,(v1,v2)->v1));
            Map<String, ContractIncomeFundE> finalContractIncomeFundEMap = contractIncomeFundEMap;
            List<PayIncomePlanE> payIncomePlanEList = this.payIncomePlanService.lambdaQuery().in(PayIncomePlanE::getPlanId,concludeVList.stream().map(IncomePlanListV::getId).collect(Collectors.toList())).list();
            Map<String,PayIncomePlanE> payIncomePlanEMap = payIncomePlanEList.stream().collect(Collectors.toMap(PayIncomePlanE::getPlanId,v->v,(v1,v2)->v1));
            concludeVList.forEach(r-> {
                r.setContractBusinessLineName(ContractBusinessLineEnum.parseName(r.getContractBusinessLine()));
                r.setConmanagetypename(null != dicMap.get(r.getConmanagetype()) ? dicMap.get(r.getConmanagetype()).getName() : null);
                r.setContractStatusName(ContractRevStatusEnum.parseName(r.getStatus()));
                r.setSplitModeName(null != r.getSplitMode() ? SplitModeEnum.parseName(r.getSplitMode()) : null);
                r.setPaymentStatusName(SettleStatusEnum.parseName(r.getPaymentStatus()));
                if (MapUtils.isNotEmpty(finalContractIncomeFundEMap) && null != finalContractIncomeFundEMap.get(r.getContractPayFundId())) {
                    ContractIncomeFundE contractIncomeFundE = finalContractIncomeFundEMap.get(r.getContractPayFundId());
                    r.setPayWay(null != contractIncomeFundE ? contractIncomeFundE.getPayWay() : null);
                    r.setServiceTypeName(contractIncomeFundE.getType());
                }
                //修正：父级的按钮根据子级判断，无需直接设置，只有非子级才直接设置按钮
                if (!StringUtils.equals("0", r.getPid())) {
                    r.setCanEditPlan(null == payIncomePlanEMap.get(r.getId()) && !planIdList.contains(r.getId()));
                }
                if (null != payIncomePlanEMap.get(r.getId())) {
                    canNotEditSet.add(r.getPid());
                }
                if (canNotEditSet.contains(r.getId()) || planIdList.contains(r.getId())) {
                    r.setCanEditPlan(false);
                }
                r.setHasGeneratePayIncomePlan(null != payIncomePlanEMap.get(r.getId()));
                r.setCanDeletePlan(r.getCanEditPlan());
                //如果子级判断出了不能编辑，那么将其父级的操作去掉即可。总纲：只要有子级不能操作，那么父级就不能操作
                if (!r.getCanEditPlan() && concludeVMap.containsKey(r.getPid())) {
                    //获取到其父级
                    IncomePlanListV pConclude = concludeVMap.get(r.getPid());
                    //将编辑和删除都设置为false
                    pConclude.setCanEditPlan(Boolean.FALSE);
                    pConclude.setCanDeletePlan(pConclude.getCanEditPlan());
                }
            });
        }
        List<IncomePlanListV> list = TreeUtil.treeing(concludeVList);
        for(IncomePlanListV s : list){
            if(s.getPid().equals("0")){
                s.setReviewStatus(null);
                s.setTermDate(null);
                s.setPaymentStatus(null);
                s.setInvoiceStatus(null);
                s.setPlanStatus(null);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            //filterList(list,minPlannedCollectionTime,maxPlannedCollectionTime,minCostEndTime,maxCostEndTime);
            sortList(list);
        }
        return PageV.of(form, pageList.getTotal(), list);
    }

    private void filterList(List<IncomePlanListV> list, AtomicReference<String> minPlannedCollectionTime, AtomicReference<String> maxPlannedCollectionTime,AtomicReference<String> minCostStartTime, AtomicReference<String> maxCostEndTime) {
        list.forEach(con -> {
            List<IncomePlanListV> children = con.getChildren();
            if (children != null && !children.isEmpty()) {
                filterPlannedCollectionTime(children,minPlannedCollectionTime,maxPlannedCollectionTime);
                filterCostTime(children,minCostStartTime,maxCostEndTime);
            }
        });
    }

    private void filterPlannedCollectionTime(List<IncomePlanListV> children, AtomicReference<String> minPlannedCollectionTime, AtomicReference<String> maxPlannedCollectionTime) {
        if (children == null || children.isEmpty()) {
            return;
        }
        if (minPlannedCollectionTime.get() == null || maxPlannedCollectionTime.get() == null ) {
            return;
        }
        LocalDate minDate = LocalDate.parse(minPlannedCollectionTime.get());
        LocalDate maxDate = LocalDate.parse(maxPlannedCollectionTime.get());


        List<IncomePlanListV> filteredList = children.stream()
                .filter(child -> child.getPlannedCollectionTime() != null)
                .filter(child -> {
                    LocalDate plannedDate = child.getPlannedCollectionTime();
                    return !plannedDate.isBefore(minDate) && !plannedDate.isAfter(maxDate);
                })
                .collect(Collectors.toList());
        children.clear();
        children.addAll(filteredList);
    }

    private void filterCostTime(List<IncomePlanListV> children, AtomicReference<String> minCostStartTime, AtomicReference<String> maxCostEndTime) {
        if (children == null || children.isEmpty()) {
            return;
        }
        if (minCostStartTime.get() == null || maxCostEndTime.get() == null ) {
            return;
        }
        LocalDate minCostDate = LocalDate.parse(minCostStartTime.get());
        LocalDate maxCostDate = LocalDate.parse(maxCostEndTime.get());

        List<IncomePlanListV> filteredList = children.stream()
                .filter(child -> child.getCostEndTime() != null)
                .filter(child -> {
                    LocalDate plannedDate = child.getCostEndTime();
                    return !plannedDate.isBefore(minCostDate) && !plannedDate.isAfter(maxCostDate);
                })
                .collect(Collectors.toList());
        children.clear();
        children.addAll(filteredList);
    }

    private List<String> checkSettleStatus(List<String> planIdList) {
        return settlementConcludeService.checkSettleStatus(planIdList);
    }

    private void sortList(List<IncomePlanListV> list) {
        list.forEach(con -> {
            List<IncomePlanListV> children = con.getChildren();
            if (children != null && !children.isEmpty()) {
                children.sort(Comparator
                        .comparingInt((IncomePlanListV o) -> parseReceiptStatus(o.getPaymentStatus()))
                        .thenComparing(IncomePlanListV::getPlannedCollectionTime)
                        .thenComparing(IncomePlanListV::getTermDate, Comparator.nullsLast(Integer::compareTo))
                );
            }
        });
    }

    private int parseReceiptStatus(Integer receiptStaus) {
        if (receiptStaus == null) {
            return Integer.MAX_VALUE;
        }
        return receiptStaus;
    }


    public void validateSettlementPlans(IncomePlanAddF settlementPlanAddF, ContractIncomeConcludeE contract) {
        List<List<ContractIncomePlanAddF>> userPlans = settlementPlanAddF.getContractPayPlanAddFLists();

        LocalDate contractStartDate = contract.getGmtExpireStart();
        LocalDate contractEndDate = contract.getGmtExpireEnd();

        if (userPlans == null || userPlans.isEmpty()) {
            throw new OwlBizException("提交的结算计划不能为空");
        }

        userPlans.forEach(value->{
            for (int i = 0; i < value.size(); i++) {
                ContractIncomePlanAddF currentPlan = value.get(i);

                ContractIncomePlanAddF firstPlan = value.get(0);
                if (!firstPlan.getCostStartTime().equals(contractStartDate)) {
                    throw new OwlBizException(String.format("第一期的费用开始日期应为合同开始日期：%s，提交值：%s",
                            contractStartDate, firstPlan.getCostStartTime()));
                }

                ContractIncomePlanAddF lastPlan = value.get(value.size() - 1);
                if (!lastPlan.getCostEndTime().equals(contractEndDate)) {
                    throw new OwlBizException(String.format("最后一期的费用结束日期应为合同结束日期：%s，提交值：%s",
                            contractEndDate, lastPlan.getCostEndTime()));
                }


                if (currentPlan.getCostStartTime() == null || currentPlan.getCostEndTime() == null) {
                    throw new OwlBizException(String.format("第 %d 期的费用开始日期和结束日期不能为空,费项:%s", i + 1,currentPlan.getChargeItem()));
                }

                if (currentPlan.getCostStartTime().isAfter(currentPlan.getCostEndTime())) {
                    throw new OwlBizException(String.format("第 %d 期的费用开始日期不能晚于结束日期,费项:%s", i + 1,currentPlan.getChargeItem()));
                }

                if (i > 0) {
                    ContractIncomePlanAddF previousPlan = value.get(i - 1);
                    LocalDate expectedStartDate = previousPlan.getCostEndTime().plusDays(1);
                    if (!currentPlan.getCostStartTime().equals(expectedStartDate)) {
                        throw new OwlBizException(String.format("第 %d 期的费用开始日期应为上一期的结束日期加一天：%s，提交值：%s,费项:%s",
                                i + 1, expectedStartDate, currentPlan.getCostStartTime(),currentPlan.getChargeItem()));
                    }
                }
            }
        });

    }


    public String generateCostEstimationCode(String redisKey,int periodNumber, String plannedCollectionDate, String ctCode) {
        // 调用自增方法获取序列号
        String serialNumber = calculateCostEstimationCode(redisKey);
        // 生成成本预估编码
        return String.format("SKJH-%d-%s-%s-%s", periodNumber, plannedCollectionDate, ctCode, serialNumber);
    }



    private String calculateCostEstimationCode(String redisKey){
        Long increment = redisTemplate.opsForValue().increment(redisKey, 1L);
        if (increment > 999L){
            // 当序列号超过最大值时，重置为0
            redisTemplate.opsForValue().set(redisKey, "0");
            increment = redisTemplate.opsForValue().increment(redisKey, 1L);
        }
        // 格式化序列号，前面补零
        String serialStr = String.format("%0"+3+"d", increment);
        return serialStr;
    }


    private PreIncomePlanDataV generatePlansForContract(ContractIncomeFundE contractPayFundE, ContractIncomeConcludeE concludeE, Integer splitMode, int groupCounter,String settlePlanGroup ) {
        PreIncomePlanDataV preSettlePlanDataV = new PreIncomePlanDataV();

        preSettlePlanDataV.setChargeItem(contractPayFundE.getChargeItem());
        preSettlePlanDataV.setChargeItemId(String.valueOf(contractPayFundE.getChargeItemId()));
        preSettlePlanDataV.setContractPayFundId(contractPayFundE.getId());

        List<PreIncomePlanGroupV> plans = new ArrayList<>();

        LocalDate contractStartDate = concludeE.getGmtExpireStart();
        LocalDate contractEndDate = concludeE.getGmtExpireEnd();
        ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
        concludeExpandF.setContractId(concludeE.getId());
        ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
        Boolean isZz = Boolean.FALSE;
        if(StringUtils.isNotEmpty(concludeExpandV.getConmanagetype())) {
            if(ContractBusinessLineEnum.物管.getCode().equals(concludeE.getContractBusinessLine()) && ContractIncomeManageTypeEnum.增值类合同.getCode().toString().equals(concludeExpandV.getConmanagetype())){
                isZz = Boolean.TRUE;
            }
        }

        if (null == splitMode) {
            return preSettlePlanDataV;
        }
        if (5 == splitMode) {
            PreIncomePlanGroupV planV = new PreIncomePlanGroupV();


            planV.setCostStartTime(contractStartDate);
            planV.setCostEndTime(contractEndDate);
            //LocalDate plannedCollectionTime = contractEndDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate plannedCollectionTime = contractEndDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            if(isZz){
                plannedCollectionTime = contractStartDate;
            }
            planV.setPlannedCollectionTime(plannedCollectionTime);

            BigDecimal plannedAmount = contractPayFundE.getAmount();
            planV.setPlannedCollectionAmount(plannedAmount);

            planV.setRatioAmount(BigDecimal.valueOf(100));

            planV.setTaxRate(StringUtils.isNotBlank(contractPayFundE.getTaxRate()) ? contractPayFundE.getTaxRate().split("%")[0] : null);
            planV.setTaxRateId(contractPayFundE.getTaxRateId());

            if (!"差额纳税".equals(contractPayFundE.getTaxRate())) {
                BigDecimal taxRateDecimal = parseTaxRate(contractPayFundE.getTaxRate());

                BigDecimal noTaxAmount = plannedAmount.divide(BigDecimal.ONE.add(taxRateDecimal), 2, RoundingMode.HALF_UP);
                planV.setNoTaxAmount(noTaxAmount);

                BigDecimal taxAmount = plannedAmount.subtract(noTaxAmount);
                planV.setTaxAmount(taxAmount);
            }else{
                planV.setTaxAmount(contractPayFundE.getTaxRateAmount());
                BigDecimal noTaxAmount = plannedAmount.subtract(contractPayFundE.getTaxRateAmount());
                planV.setNoTaxAmount(noTaxAmount);
            }



            planV.setSettlePlanGroup(settlePlanGroup);



            planV.setTermDate(1);
            planV.setChargeItem(contractPayFundE.getChargeItem());
            planV.setChargeItemId(String.valueOf(contractPayFundE.getChargeItemId()));
            planV.setServiceType(contractPayFundE.getType());
            planV.setServiceTypeId(contractPayFundE.getTypeId());
            plans.add(planV);
            preSettlePlanDataV.setPreSettlePlanGroupVList(plans);
            if (CollectionUtils.isNotEmpty(plans)) {
                preSettlePlanDataV.setGroupPlannedCollectionAmount(
                        plans.stream()
                                .map(PreIncomePlanGroupV::getPlannedCollectionAmount)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
            }
            return preSettlePlanDataV;
        }

        LocalDate periodStartDate = contractStartDate;
        LocalDate periodEndDate;
        int periodCount = 0;
        List<LocalDate> periodEndDates = new ArrayList<>();
        List<LocalDate> periodStartDates = new ArrayList<>();

        while (!periodStartDate.isAfter(contractEndDate)) {
            periodStartDates.add(periodStartDate);

            if (periodCount == 0) {
                periodEndDate = getFirstPeriodEndDate(periodStartDate, contractEndDate, splitMode);
            } else {
                periodEndDate = getPeriodEndDate(periodStartDate, contractEndDate, splitMode);
            }

            if (periodEndDate.isAfter(contractEndDate)) {
                periodEndDate = contractEndDate;
            }

            periodEndDates.add(periodEndDate);
            periodCount++;

            periodStartDate = periodEndDate.plusDays(1);

            if (periodStartDate.isAfter(contractEndDate)) {
                break;
            }
        }

        BigDecimal totalAmount = contractPayFundE.getAmount();
        BigDecimal amountPerPeriod = totalAmount.divide(BigDecimal.valueOf(periodCount), 2, BigDecimal.ROUND_DOWN);
        BigDecimal remainingAmount = totalAmount.subtract(amountPerPeriod.multiply(BigDecimal.valueOf(periodCount - 1)));


        BigDecimal totalRatioAmount = new BigDecimal(0.00);
        for (int i = 0; i < periodCount; i++) {
            PreIncomePlanGroupV planV = new PreIncomePlanGroupV();

            LocalDate startDate = periodStartDates.get(i);
            LocalDate endDate = periodEndDates.get(i);

            planV.setCostStartTime(startDate);
            planV.setCostEndTime(endDate);
            //LocalDate plannedCollectionTime = endDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate plannedCollectionTime = endDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            if(isZz){
                plannedCollectionTime = startDate;
            }
            planV.setPlannedCollectionTime(plannedCollectionTime);

            BigDecimal plannedAmount;
            if (i == periodCount - 1) {
                plannedAmount = remainingAmount;
            } else {
                plannedAmount = amountPerPeriod;
            }

            planV.setPlannedCollectionAmount(plannedAmount);

            BigDecimal ratioAmount = plannedAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP);
            if (i != (periodCount-1)) {
                totalRatioAmount = totalRatioAmount.add(ratioAmount);
            }else{
                ratioAmount = BigDecimal.valueOf(1.00).subtract(totalRatioAmount);
            }
            planV.setRatioAmount(ratioAmount.multiply(BigDecimal.valueOf(100)));

            planV.setTaxRate(StringUtils.isNotBlank(contractPayFundE.getTaxRate()) ? contractPayFundE.getTaxRate().split("%")[0] : null);
            planV.setTaxRateId(contractPayFundE.getTaxRateId());


            if (!"差额纳税".equals(contractPayFundE.getTaxRate())) {
                BigDecimal taxRateDecimal = parseTaxRate(contractPayFundE.getTaxRate());

                if (i == periodCount - 1) {
                    // 倒减计算最后一期税额 和 不含税金额,用清单项的金额减去前几期的金额

                    BigDecimal noTaxAmount = amountPerPeriod.divide(BigDecimal.ONE.add(taxRateDecimal), 2, RoundingMode.HALF_UP);
                    BigDecimal taxAmount = amountPerPeriod.subtract(noTaxAmount);
                    planV.setNoTaxAmount(
                            contractPayFundE.getAmountWithOutRate()
                                    .subtract(noTaxAmount.multiply(BigDecimal.valueOf(periodCount - 1)))
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    planV.setTaxAmount(
                            contractPayFundE.getTaxRateAmount()
                                    .subtract(taxAmount.multiply(BigDecimal.valueOf(periodCount - 1)))
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                } else {
                    BigDecimal noTaxAmount = plannedAmount.divide(BigDecimal.ONE.add(taxRateDecimal), 2, RoundingMode.HALF_UP);
                    BigDecimal taxAmount = plannedAmount.subtract(noTaxAmount);
                    planV.setNoTaxAmount(noTaxAmount);
                    planV.setTaxAmount(taxAmount);
                }
            }



            planV.setTermDate(i + 1);
            planV.setChargeItem(contractPayFundE.getChargeItem());
            planV.setChargeItemId(String.valueOf(contractPayFundE.getChargeItemId()));
            planV.setServiceType(contractPayFundE.getType());
            planV.setServiceTypeId(contractPayFundE.getTypeId());

            planV.setSettlePlanGroup(settlePlanGroup);

            plans.add(planV);
        }
        if ("差额纳税".equals(contractPayFundE.getTaxRate())) {
            BigDecimal totalTaxRateAmount = contractPayFundE.getTaxRateAmount();

            if (totalTaxRateAmount != null && totalTaxRateAmount.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal averageAmount = totalTaxRateAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                BigDecimal sumAllocated = BigDecimal.ZERO;
                for (int i = 0; i < periodCount; i++) {
                    BigDecimal taxAmount;
                    if (i < periodCount - 1) {
                        taxAmount = averageAmount;
                        sumAllocated = sumAllocated.add(averageAmount);
                    } else {
                        taxAmount = totalTaxRateAmount.subtract(sumAllocated);
                    }
                    plans.get(i).setTaxAmount(taxAmount);
                    plans.get(i).setNoTaxAmount(plans.get(i).getPlannedCollectionAmount().subtract(taxAmount));
                }
            }
        }
        preSettlePlanDataV.setPreSettlePlanGroupVList(plans);
        if (CollectionUtils.isNotEmpty(plans)) {
            preSettlePlanDataV.setGroupPlannedCollectionAmount(
                    plans.stream()
                            .map(PreIncomePlanGroupV::getPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }
        return preSettlePlanDataV;
    }

    private static BigDecimal parseTaxRate(String taxRateStr) {
        if (taxRateStr == null || taxRateStr.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String rate = taxRateStr.replace("%", "");
            BigDecimal rateDecimal = new BigDecimal(rate).divide(BigDecimal.valueOf(100));
            return rateDecimal;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的税率格式: " + taxRateStr);
        }
    }

    /**
     * 1 一个月 ，2 三个月，3 半年，4 一年，5 据实结算
     * @param startDate
     * @param contractEndDate
     * @param splitMode
     * @return
     */
    private static LocalDate getFirstPeriodEndDate(LocalDate startDate, LocalDate contractEndDate, Integer splitMode) {
        LocalDate endDate;
        switch (splitMode) {
            case 1:
                endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                break;
            case 2:
            {
                endDate = startDate.plusMonths(3).with(TemporalAdjusters.lastDayOfMonth());
            }
            break;
            case 3:
            {
                endDate = startDate.plusMonths(6).with(TemporalAdjusters.lastDayOfMonth());
            }
            break;
            case 4:
                endDate = startDate.plusMonths(12).with(TemporalAdjusters.lastDayOfMonth());
                break;
            case 5:
                endDate = contractEndDate;
                break;
            default:
                throw new IllegalArgumentException("无效的结算周期模式: " + splitMode);
        }

        if (endDate.isAfter(contractEndDate)) {
            endDate = contractEndDate;
        }

        return endDate;
    }


    private static LocalDate getPeriodEndDate(LocalDate startDate, LocalDate contractEndDate, Integer splitMode) {
        LocalDate endDate;
        switch (splitMode) {
            case 1:
                endDate = startDate.plusMonths(1).minusDays(1);
                break;
            case 2:
                endDate = startDate.plusMonths(3).minusDays(1);
                break;
            case 3:
                endDate = startDate.plusMonths(6).minusDays(1);
                break;
            case 4:
                endDate = startDate.plusYears(1).minusDays(1);
                break;
            case 5:
                endDate = contractEndDate;
                break;
            default:
                throw new IllegalArgumentException("无效的结算周期模式: " + splitMode);
        }

        if (endDate.isAfter(contractEndDate)) {
            endDate = contractEndDate;
        }

        return endDate;
    }

    private void validatePlanId(List<List<ContractIncomePlanAddF>> contractPayPlanAddFLists, SettlementPlanResult settlementPlanResult,boolean removeAll) {
        if (removeAll) {
            return;
        }
        List<String> planIdList = contractPayPlanAddFLists.stream().flatMap(List::stream).map(ContractIncomePlanAddF::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<ContractIncomePlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery().in(ContractIncomePlanConcludeE::getId,planIdList).list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            settlementPlanResult.setSuccess(false);
            settlementPlanResult.setErrorMessage(List.of("计划id不合法,请检查"));
            return;
        }
        if (contractPayPlanConcludeEList.size() != planIdList.size()) {
            settlementPlanResult.setSuccess(false);
            settlementPlanResult.setErrorMessage(List.of("部分计划id不合法,请检查"));
        }
    }

    private void rebuildPreSettlePlanV(PreIncomePlanV preSettlePlanV, Map<String, List<ContractIncomePlanConcludeE>> contractPayPlanMap) {
        List<PreIncomePlanDataV> preSettlePlanDataVList = preSettlePlanV.getPreSettlePlanDataVList();
        if (CollectionUtils.isEmpty(preSettlePlanDataVList)) {
            preSettlePlanDataVList = new ArrayList<>();
        }
        List<PreIncomePlanDataV> finalPreSettlePlanDataVList = preSettlePlanDataVList;
        contractPayPlanMap.forEach((settlePlanGroup, list)->{
            PreIncomePlanDataV preSettlePlanDataV = new PreIncomePlanDataV();
            preSettlePlanDataV.setChargeItem(list.get(0).getChargeItem());
            preSettlePlanDataV.setChargeItemId(String.valueOf(list.get(0).getChargeItemId()));
            preSettlePlanDataV.setContractPayFundId(list.get(0).getContractPayFundId());
            List<PreIncomePlanGroupV> preSettlePlanGroupVList = Global.mapperFacade.mapAsList(list, PreIncomePlanGroupV.class);;
            preSettlePlanDataV.setPreSettlePlanGroupVList(preSettlePlanGroupVList);
            preSettlePlanDataV.setGroupPlannedCollectionAmount(
                    preSettlePlanGroupVList.stream()
                            .map(PreIncomePlanGroupV::getPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            finalPreSettlePlanDataVList.add(preSettlePlanDataV);
        });
        preSettlePlanV.setPreSettlePlanDataVList(finalPreSettlePlanDataVList);
    }


    private void orderTerms(PreIncomePlanV preSettlePlanV) {
        if (CollectionUtils.isEmpty(preSettlePlanV.getPreSettlePlanDataVList())) {
            return;
        }
        preSettlePlanV.getPreSettlePlanDataVList().forEach(vlist -> {
            if (CollectionUtils.isNotEmpty(vlist.getPreSettlePlanGroupVList())) {
                vlist.getPreSettlePlanGroupVList().sort(
                        Comparator.comparing(
                                PreIncomePlanGroupV::getTermDate,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                );
            }
        });
    }


    private QueryWrapper<IncomePlanListQuery> planConditionPage(PageF<SearchF<IncomePlanListQuery>> request,QueryWrapper<IncomePlanListQuery> queryModel, String tenantId) {
        queryModel.eq("cc.tenantId", tenantId);


        Field type1Field = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "cpe.conmanagetype".equals(field.getName()))
                .findFirst().orElse(null);
        Field type2Field = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "cpe.conincrementype".equals(field.getName()))
                .findFirst().orElse(null);

        if (Objects.nonNull(type1Field) && Objects.nonNull(type2Field)){
            List<String> type1Values = (List<String>) type1Field.getValue();
            List<String> type2Values = (List<String>) type2Field.getValue();
            if (CollectionUtils.isNotEmpty(type1Values) &&
                    CollectionUtils.isNotEmpty(type2Values) &&
                    type1Values.size() > 1){
                // 同时传递增值和其他的话 删除条件,自行拼接sql
                request.getConditions().getFields().removeIf(e -> e.getName().startsWith("c.") || e.getName().startsWith("cpe."));
                queryModel = request.getConditions().getQueryModel();
                type1Values.removeIf("4"::equals);
                StringBuilder sb = new StringBuilder();
                sb.append("( (cpe.conmanagetype = '4' and cpe.conincrementype in (");
                for (String value : type2Values) {
                    sb.append("'").append(value).append("',");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append(") ) or cpe.conmanagetype in (");
                for (String value : type1Values) {
                    sb.append("'").append(value).append("',");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("))");
                // 这里用param拼接会有问题，直接拼好sql
                queryModel.apply(sb.toString());
            }
        }
        return queryModel;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createIncomePlan(PayIncomePlanV payIncomePlanV) {
        if (StringUtils.isBlank(payIncomePlanV.getIncomePlanId()) || StringUtils.isBlank(payIncomePlanV.getContractId())) {
            throw new OwlBizException("收款计划id以及合同id不能为空");
        }
        List<ContractIncomePlanConcludeE> contractIncomePlanConcludeEList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getContractId,payIncomePlanV.getContractId()).eq(ContractIncomePlanConcludeE::getId,payIncomePlanV.getIncomePlanId()).list();
        if (CollectionUtils.isEmpty(contractIncomePlanConcludeEList)) {
            throw new OwlBizException("收款计划id不存在");
        }
        List<PayIncomePlanE> payIncomePlanEList = payIncomePlanService.lambdaQuery().eq(PayIncomePlanE::getContractId,payIncomePlanV.getContractId()).eq(PayIncomePlanE::getPlanId,payIncomePlanV.getIncomePlanId()).list();
        if (CollectionUtils.isNotEmpty(payIncomePlanEList)) {
            throw new OwlBizException("收入计划已经生成过,请勿重复操作");
        }
        List<ContractIncomeConcludeE> contractIncomeConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,payIncomePlanV.getContractId()).list();
        List<PayIncomePlanE> payIncomePlanES = payIncomePlanService.generateIncomePlans(contractIncomePlanConcludeEList.get(0),contractIncomeConcludeEList.get(0),Boolean.TRUE);
        iri(payIncomePlanES.stream().map(PayIncomePlanE::getId).collect(Collectors.toList()),1);
    }

    public Boolean sakuIncomePlan(IncomePlanCancleF planCancleF) {
        List<ContractIncomeConcludeE> contractIncomeConcludeEList = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,planCancleF.getContractId()).list();
        if (CollectionUtils.isEmpty(contractIncomeConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        List<ContractIncomePlanConcludeE> concludeEList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getContractId,planCancleF.getContractId()).list();
        if (CollectionUtils.isEmpty(concludeEList)) {
            throw new OwlBizException("收款计划不存在");
        }
        //TODO 调用作废接口
        return true;
    }

    public List<DictionaryCode> queryConmanagetype() {
        return configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateIncomePlan(PayIncomePlanV payIncomePlanV) {
        preCheck(payIncomePlanV);
        List<PayIncomePlanE> payIncomePlanEList = payIncomePlanService.lambdaQuery().eq(PayIncomePlanE::getId,payIncomePlanV.getPlanId()).list();
        PayIncomePlanE payIncomePlanE = payIncomePlanEList.get(0);
        BigDecimal confirmedAmountReceivedTemp = payIncomePlanE.getConfirmedAmountReceivedTemp();
        if (!"差额纳税".equals(payIncomePlanE.getTaxRate()) && null != payIncomePlanV.getTaxAmount()) {
            throw new OwlBizException("差额纳税才可以调整税额");
        }
        if ("差额纳税".equals(payIncomePlanE.getTaxRate()) && null != payIncomePlanV.getTaxAmount()) {
            payIncomePlanE.setTaxAmount(payIncomePlanV.getTaxAmount());
            BigDecimal noTaxAmount = payIncomePlanE.getPlannedCollectionAmount().subtract(payIncomePlanE.getTaxAmount());
            payIncomePlanE.setNoTaxAmount(noTaxAmount);
        }
        payIncomePlanE.setConfirmedAmountReceivedTemp(payIncomePlanV.getConfirmedAmountReceivedTemp());
        payIncomePlanE.setAdjustment(payIncomePlanV.getAdjustment());
        payIncomePlanE.setAdjustmentAmount(payIncomePlanV.getConfirmedAmountReceivedTemp().subtract(confirmedAmountReceivedTemp));
        this.payIncomePlanService.updateById(payIncomePlanE);

    }

    private void preCheck(PayIncomePlanV payIncomePlanV) {
        if (StringUtils.isBlank(payIncomePlanV.getPlanId()) || StringUtils.isBlank(payIncomePlanV.getContractId())) {
            throw new OwlBizException("收款计划id以及合同id不能为空");
        }
        List<PayIncomePlanE> payIncomePlanEList = payIncomePlanService.lambdaQuery().eq(PayIncomePlanE::getId,payIncomePlanV.getPlanId()).eq(PayIncomePlanE::getDeleted,0).list();
        if (CollectionUtils.isEmpty(payIncomePlanEList)) {
            throw new OwlBizException("收入计划不存在");
        }
        if (IriStatusEnum.yes.getCode().equals(payIncomePlanEList.get(0).getIriStatus())) {
            throw new OwlBizException("收入计划已入账,不能调整");
        }
        if (null == payIncomePlanV.getConfirmedAmountReceivedTemp()) {
            throw new OwlBizException("确收暂估金额不能为空");
        }
    }
    public ContractIncomePlanPidConcludeV listPidNew(ContractIncomePlanConcludeListF param) {
        ContractIncomePlanPidConcludeV concludeV = new ContractIncomePlanPidConcludeV();
        //查询用户权限范围内的组织
        ContractOrgPermissionV orgPermission = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermission.getRadio())) {
            return concludeV;
        }
        List<ContractIncomePlanConcludeV> concludeVList = contractIncomePlanConcludeMapper.getChoosePlanInfoNew(param.getNameNo(), orgPermission.getOrgIds());
        caculateContractTotalSettlementAmount(concludeVList);
        if (ObjectUtils.isNotEmpty(concludeVList)) {
            List<String> ztList = new ArrayList<>();
            ztList.add("未推送合同");
            List<ContractIncomeConcludeE> contractConcludeEList = contractIncomeConcludeService.lambdaQuery()
                    .in(ContractIncomeConcludeE::getPid,concludeVList.stream().map(ContractIncomePlanConcludeV::getContractId).collect(Collectors.toList()))
                    .eq(ContractIncomeConcludeE::getDeleted,0)
                    .eq(ContractIncomeConcludeE::getContractType,ContractTypeEnum.补充协议.getCode())
                    .ne(ContractIncomeConcludeE::getContractNature, ConcludeContractNatureEnum.SUCCESS.getCode()).list();
            if(CollectionUtils.isNotEmpty(contractConcludeEList)){
                ztList.addAll(contractConcludeEList.stream().map(ContractIncomeConcludeE :: getPid).collect(Collectors.toList()));
            }
            List<ContractIncomeFundV> contractPayFundVList = contractIncomeFundMapper.getContractPaySettFundList(concludeVList.get(0).getContractId());
            concludeV.setContractPayFundVList(contractPayFundVList);
            concludeVList.forEach(x-> {
                x.setIsIncomeCorrection(Objects.nonNull(x.getIsCorrectionAndPlan()) && !CorrectionStatusEnum.通过后修改确收计划.getCode().equals(x.getIsCorrectionAndPlan()));
                x.setIsHaveNoPushedContract(ztList.contains(x.getContractId()));
            });
        }
        concludeV.setConcludeVList(concludeVList);
        return concludeV;
    }

    private void caculateContractTotalSettlementAmount(List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList) {
        if (CollectionUtils.isEmpty(contractIncomePlanConcludeVList)) {
            return;
        }
        List<String> contractIds = contractIncomePlanConcludeVList.stream()
                .map(ContractIncomePlanConcludeV::getContractId)
                .distinct()
                .collect(Collectors.toList());

        List<ContractIncomeSettlementConcludeE> paySettlements = contractIncomeSettlementConcludeMapper.selectList(Wrappers.<ContractIncomeSettlementConcludeE>lambdaQuery()
                .ne(ContractIncomeSettlementConcludeE::getPid, "0")
                .in(ContractIncomeSettlementConcludeE::getContractId, contractIds)
                .eq(ContractIncomeSettlementConcludeE::getReviewStatus, ReviewStatusEnum.已通过.getCode())
                .eq(ContractIncomeSettlementConcludeE::getDeleted, 0));
        Map<String, List<ContractIncomeSettlementConcludeE>> incomeSettlementMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(paySettlements)) {
            incomeSettlementMap = paySettlements.stream().collect(Collectors.groupingBy(ContractIncomeSettlementConcludeE::getContractId));
        }

        for (ContractIncomePlanConcludeV concludeV : contractIncomePlanConcludeVList) {
            if (!incomeSettlementMap.containsKey(concludeV.getContractId())) {
                continue;
            }
            List<ContractIncomeSettlementConcludeE> subSetts = incomeSettlementMap.get(concludeV.getContractId());
            BigDecimal total = subSetts.stream().map(ContractIncomeSettlementConcludeE::getPlannedCollectionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            //计算该合同的扣款总额
            List<ContractIncomeSettlementConcludeE> settlementConcludeES = incomeSettlementMap.get(concludeV.getContractId());
            BigDecimal deductionTotal = settlementConcludeES.stream().map(ContractIncomeSettlementConcludeE::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            //减去扣款总额
            total = total.subtract(deductionTotal);
            concludeV.setContractTotalSettlementAmount(total);
        }
    }

    /**
     * 收入计划单条入账
     * @param payIncomePlanV
     */
    public void iriPlan(PayIncomePlanV payIncomePlanV) {
        if (StringUtils.isBlank(payIncomePlanV.getPlanId())) {
            throw new OwlBizException("收入计划id不能为空");
        }
        PayIncomePlanE payIncomePlanE = this.payIncomePlanService.lambdaQuery().eq(PayIncomePlanE::getId,payIncomePlanV.getPlanId()).one();
        if (null == payIncomePlanE) {
            throw new OwlBizException("收入计划id不合法");
        }

        //合同
        ContractIncomeConcludeE contractConcludeE = this.contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,payIncomePlanE.getContractId()).eq(ContractIncomeConcludeE::getDeleted,0).one();
        //收款计划
        ContractIncomePlanConcludeE contractIncomePlanConcludeE = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getId,payIncomePlanE.getPlanId()).eq(ContractIncomePlanConcludeE::getDeleted,0).one();

        List<AddTemporaryChargeBillRf> addTemporaryChargeBillFs = new ArrayList<>();
        AddTemporaryChargeBillRf addTemporaryChargeBillRf = new AddTemporaryChargeBillRf();
        addTemporaryChargeBillRf.setChargeItemId(contractIncomePlanConcludeE.getChargeItemId());
        addTemporaryChargeBillRf.setChargeItemName(contractIncomePlanConcludeE.getChargeItem());
        addTemporaryChargeBillRf.setTaxRateId(Long.valueOf(payIncomePlanE.getTaxRateId()));
        addTemporaryChargeBillRf.setSource("收入合同");
        addTemporaryChargeBillRf.setBillMethod(10);

        addTemporaryChargeBillRf.setPayerId(payIncomePlanE.getDraweeId());
        addTemporaryChargeBillRf.setPayerName(payIncomePlanE.getDraweeName());
        addTemporaryChargeBillRf.setPayeeId(payIncomePlanE.getOurPartyId());
        addTemporaryChargeBillRf.setPayeeName(payIncomePlanE.getOurParty());
        addTemporaryChargeBillRf.setStatutoryBodyId(Long.valueOf(payIncomePlanE.getOurPartyId()));
        addTemporaryChargeBillRf.setStatutoryBodyName(payIncomePlanE.getOurParty());
        addTemporaryChargeBillRf.setPayerType(4);
        addTemporaryChargeBillRf.setCommunityId(contractConcludeE.getCommunityId());
        addTemporaryChargeBillRf.setCommunityName(contractConcludeE.getCommunityName());
        addTemporaryChargeBillRf.setCostCenterId(Long.valueOf(contractConcludeE.getCostCenterId()));
        addTemporaryChargeBillRf.setCostCenterName(contractConcludeE.getCostCenterName());
        addTemporaryChargeBillRf.setChargingArea(1);
        addTemporaryChargeBillRf.setChargeItemType(2);
        addTemporaryChargeBillRf.setContactName(payIncomePlanE.getDrawee());
        addTemporaryChargeBillRf.setOutBusNo("");//不用填写
        addTemporaryChargeBillRf.setTotalAmount(payIncomePlanE.getPlannedCollectionAmount().multiply(new BigDecimal("100")).longValue());
        addTemporaryChargeBillRf.setUnitPrice(addTemporaryChargeBillRf.getTotalAmount());
        addTemporaryChargeBillRf.setSupCpUnitId(contractConcludeE.getCommunityId());
        addTemporaryChargeBillRf.setPayType("0");
        addTemporaryChargeBillRf.setSysSource(2);
        addTemporaryChargeBillRf.setAppId(ContractSetConst.CONTRACTAPPID);
        addTemporaryChargeBillRf.setAppName(ContractSetConst.CONTRACTAPPNAME);
        addTemporaryChargeBillRf.setDescription(StringUtils.isNotBlank(payIncomePlanE.getRemark()) ? payIncomePlanE.getRemark() : "无");
        addTemporaryChargeBillRf.setApprovedFlag(true);
        addTemporaryChargeBillRf.setAccountDate(LocalDate.parse(payIncomePlanE.getBelongingMonth() + "-01"));

        addTemporaryChargeBillRf.setStartTime(payIncomePlanE.getCostStartTime().atStartOfDay());
        addTemporaryChargeBillRf.setEndTime(payIncomePlanE.getCostEndTime().atTime(23, 59, 59));
        addTemporaryChargeBillRf.setReceivableDate(payIncomePlanE.getPlannedCollectionTime());

        if ("差额纳税".equals(payIncomePlanE.getTaxRate())) {
            addTemporaryChargeBillRf.setTaxRate(null);
            addTemporaryChargeBillRf.setExtField8("差额纳税");
        }else{
            addTemporaryChargeBillRf.setTaxRate(new BigDecimal(payIncomePlanE.getTaxRate()).divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP)));
        }
        addTemporaryChargeBillRf.setTaxAmountNew(payIncomePlanE.getTaxAmount().multiply(new BigDecimal("100")));

        addTemporaryChargeBillFs.add(addTemporaryChargeBillRf);
        List<TemporaryChargeBillPageV> temporaryChargeBillPageVList = financeFeignClient.temporaryAddBatch(addTemporaryChargeBillFs);
        if (CollectionUtils.isEmpty(temporaryChargeBillPageVList)) {
            throw new OwlBizException("收入计划入账异常");
        }
        TemporaryChargeBillPageV temporaryChargeBillPageV = temporaryChargeBillPageVList.get(0);
        payIncomePlanE.setIriStatus(IriStatusEnum.yes.getCode());
        payIncomePlanE.setBillCode(temporaryChargeBillPageV.getBillNo());
        payIncomePlanE.setIriTime(LocalDateTime.now());
        this.payIncomePlanService.updateById(payIncomePlanE);
    }

    public void deleteSettlementPlan(String planId) {
        if (StringUtils.isEmpty(planId)) {
            throw new OwlBizException("收款计划id不存在");
        }
        List<ContractIncomePlanConcludeE> contractIncomePlanConcludeEList = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getId,planId).list();
        if (CollectionUtils.isEmpty(contractIncomePlanConcludeEList)) {
            throw new OwlBizException("收款计划id不合法");
        }
        //查询出pid是planId的收款计划
        List<ContractIncomePlanConcludeE> realPlans = this.list(Wrappers.<ContractIncomePlanConcludeE>lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getPid, planId)
                .eq(ContractIncomePlanConcludeE::getDeleted, 0));
        List<String> realPlanIds = realPlans.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList());
        //查询收入计划
        if (CollectionUtils.isNotEmpty(realPlanIds)) {
            long payIncomeCount = payIncomePlanService.count(Wrappers.<PayIncomePlanE>lambdaQuery()
                    .in(PayIncomePlanE::getPlanId, realPlanIds)
                    .eq(PayIncomePlanE::getDeleted, 0));
            if (payIncomeCount > 0) {
                throw new OwlBizException("收款计划不能删除");
            }
        }
        List<String> canNotRemoveList = settlementConcludeService.checkSettleStatus(realPlanIds);
        if (CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("收款计划不能删除");
        }
        //删除父计划
        this.contractIncomePlanConcludeMapper.deleteById(planId);
        //删除子计划
        this.contractIncomePlanConcludeMapper.deleteBatchIds(realPlanIds);
        //删除成功后推送枫行梦
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(realPlans.get(0).getContractId());
        List<ContractIncomeConcludePlanFxmRecordE> records = contractInfoToFxmCommonService.pushIncomePlan2Fxm(realPlans, concludeE, PlanFxmPushType.DELETE);
        if (CollectionUtils.isNotEmpty(records)) {
            contractIncomeConcludePlanFxmRecordService.saveBatch(records);
        }
    }


    public void iri(List<String> planIdList,Integer billType) {
        if (CollectionUtils.isEmpty(planIdList)) {
            throw new OwlBizException("收入计划id不能为空");
        }
        List<PayIncomePlanE> payIncomePlanEList = this.payIncomePlanService.lambdaQuery().in(PayIncomePlanE::getId,planIdList).list();
        if (CollectionUtils.isEmpty(payIncomePlanEList)) {
            throw new OwlBizException("收入计划id不合法");
        }

        List<PayIncomePlanE> hasExistList = this.payIncomePlanService.lambdaQuery().in(PayIncomePlanE::getId,planIdList).eq(PayIncomePlanE::getBillType,billType).list();
        if (!CollectionUtils.isEmpty(hasExistList)) {
            throw new OwlBizException("不能重复推送临时账单");
        }

        // 根据计划查询确收审批信息
        List<ContractIncomeSettlementConcludeV> settlementList = contractIncomeSettlementConcludeMapper.getContractIncomeSettlementList(planIdList);
        Map<String, ContractIncomeSettlementConcludeV> settlementConcludeVMap = settlementList.stream().collect(Collectors.toMap(ContractIncomeSettlementConcludeV::getPlanId, e -> e));


        //合同
        ContractIncomeConcludeE contractConcludeE = this.contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,payIncomePlanEList.get(0).getContractId()).eq(ContractIncomeConcludeE::getDeleted,0).one();
        //收款计划
        ContractIncomePlanConcludeE contractIncomePlanConcludeE = this.lambdaQuery().eq(ContractIncomePlanConcludeE::getId,payIncomePlanEList.get(0).getPlanId()).eq(ContractIncomePlanConcludeE::getDeleted,0).one();
        List<AddTemporaryChargeBillRf> addTemporaryChargeBillFs = new ArrayList<>();
        payIncomePlanEList.forEach(payIncomePlanE->{
            AddTemporaryChargeBillRf addTemporaryChargeBillRf = new AddTemporaryChargeBillRf();
            addTemporaryChargeBillRf.setChargeItemId(contractIncomePlanConcludeE.getChargeItemId());
            addTemporaryChargeBillRf.setChargeItemName(contractIncomePlanConcludeE.getChargeItem());
            addTemporaryChargeBillRf.setTaxRateId(Long.valueOf(payIncomePlanE.getTaxRateId()));
            addTemporaryChargeBillRf.setSource("收入合同");
            addTemporaryChargeBillRf.setBillMethod(10);

            addTemporaryChargeBillRf.setPayerId(payIncomePlanE.getDraweeId());
            addTemporaryChargeBillRf.setPayerName(payIncomePlanE.getDraweeName());
            addTemporaryChargeBillRf.setPayeeId(payIncomePlanE.getOurPartyId());
            addTemporaryChargeBillRf.setPayeeName(payIncomePlanE.getOurParty());
            addTemporaryChargeBillRf.setStatutoryBodyId(Long.valueOf(payIncomePlanE.getOurPartyId()));
            addTemporaryChargeBillRf.setStatutoryBodyName(payIncomePlanE.getOurParty());
            addTemporaryChargeBillRf.setPayerType(4);
            addTemporaryChargeBillRf.setCommunityId(contractConcludeE.getCommunityId());
            addTemporaryChargeBillRf.setCommunityName(contractConcludeE.getCommunityName());
            addTemporaryChargeBillRf.setCostCenterId(Long.valueOf(contractConcludeE.getCostCenterId()));
            addTemporaryChargeBillRf.setCostCenterName(contractConcludeE.getCostCenterName());
            addTemporaryChargeBillRf.setChargingArea(1);
            addTemporaryChargeBillRf.setChargeItemType(2);
            addTemporaryChargeBillRf.setContactName(payIncomePlanE.getDrawee());
            addTemporaryChargeBillRf.setOutBusNo("");//不用填写
            addTemporaryChargeBillRf.setTotalAmount(payIncomePlanE.getPlannedCollectionAmount().multiply(new BigDecimal("100")).longValue());
            addTemporaryChargeBillRf.setReceivableAmount(payIncomePlanE.getPlannedCollectionAmount().multiply(new BigDecimal("100")).longValue());
            addTemporaryChargeBillRf.setUnitPrice(addTemporaryChargeBillRf.getTotalAmount());
            addTemporaryChargeBillRf.setSupCpUnitId(contractConcludeE.getCommunityId());
            addTemporaryChargeBillRf.setPayType("0");
            addTemporaryChargeBillRf.setSysSource(2);
            addTemporaryChargeBillRf.setAppId(ContractSetConst.CONTRACTAPPID);
            addTemporaryChargeBillRf.setAppName(ContractSetConst.CONTRACTAPPNAME);
            addTemporaryChargeBillRf.setDescription(StringUtils.isNotBlank(payIncomePlanE.getRemark()) ? payIncomePlanE.getRemark() : "无");
            addTemporaryChargeBillRf.setApprovedFlag(true);
            addTemporaryChargeBillRf.setAccountDate(LocalDate.parse(payIncomePlanE.getBelongingMonth() + "-01"));

            addTemporaryChargeBillRf.setStartTime(payIncomePlanE.getCostStartTime().atStartOfDay());
            addTemporaryChargeBillRf.setEndTime(payIncomePlanE.getCostEndTime().atTime(23, 59, 59));
            addTemporaryChargeBillRf.setReceivableDate(payIncomePlanE.getPlannedCollectionTime());

            addTemporaryChargeBillRf.setContractNo(contractConcludeE.getContractNo());
            addTemporaryChargeBillRf.setContractName(contractConcludeE.getName());
            addTemporaryChargeBillRf.setExtField6(contractConcludeE.getId());

            if ("差额纳税".equals(payIncomePlanE.getTaxRate())) {
                addTemporaryChargeBillRf.setTaxRate(null);
                addTemporaryChargeBillRf.setExtField8("差额纳税");
            }else{
                addTemporaryChargeBillRf.setTaxRate(new BigDecimal(payIncomePlanE.getTaxRate()).divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP)));
            }
            addTemporaryChargeBillRf.setTaxAmountNew(payIncomePlanE.getTaxAmount().multiply(new BigDecimal("100")));
            addTemporaryChargeBillRf.setExtField9(payIncomePlanE.getOriginNoTaxAmount().multiply(new BigDecimal("100")).toString());
            addTemporaryChargeBillRf.setExtField10(payIncomePlanE.getOriginTaxAmount().multiply(new BigDecimal("100")).toString());

            // 确认审批通过更新账单的计提状态、确收状态和确收时间
            ContractIncomeSettlementConcludeV settlementConcludeV = settlementConcludeVMap.getOrDefault(payIncomePlanE.getPlanId(), new ContractIncomeSettlementConcludeV());
            if (IncomeSettleStatusEnum.已确收.getCode().equals(settlementConcludeV.getSettleStatus())) {
                addTemporaryChargeBillRf.setBusinessReceiptConfirmationStatus(2);
                addTemporaryChargeBillRf.setReceiptConfirmationTime(settlementConcludeV.getApproveCompletedTime());
            }
            addTemporaryChargeBillFs.add(addTemporaryChargeBillRf);
        });

        List<TemporaryChargeBillPageV> temporaryChargeBillPageVList = financeFeignClient.temporaryAddBatch(addTemporaryChargeBillFs);
        if (CollectionUtils.isEmpty(temporaryChargeBillPageVList)) {
            throw new OwlBizException("收入计划入账异常");
        }
        for (int i = 0; i < payIncomePlanEList.size(); i++) {
            PayIncomePlanE incomeCostPlanE = payIncomePlanEList.get(i);
            TemporaryChargeBillPageV temporaryChargeBillPageV = temporaryChargeBillPageVList.get(i);
            incomeCostPlanE.setIriStatus(IriStatusEnum.yes.getCode());
            incomeCostPlanE.setBillCode(temporaryChargeBillPageV.getBillNo());
            incomeCostPlanE.setIriTime(LocalDateTime.now());
            incomeCostPlanE.setBillId(temporaryChargeBillPageV.getId());
            incomeCostPlanE.setBillType(billType);
        }
        this.payIncomePlanService.updateBatchById(payIncomePlanEList);
    }

    public List<ContractIncomePlanConcludeE> queryByCostTime(List<String> planIds, List<IncomePlanPeriodV> periodList) {
        return this.baseMapper.queryByCostTime(planIds, periodList);
    }
    public List<UpdateTemporaryChargeBillF> iriUpdate(String settleId, String contractId, LocalDateTime now,List<PayIncomePlanE> payIncomePlanList) {

        //查关联的结算详情
        List<ContractIncomeSettDetailsE> settDetails = contractIncomeSettDetailsService.list(Wrappers.<ContractIncomeSettDetailsE>lambdaQuery()
                .eq(ContractIncomeSettDetailsE::getSettlementId, settleId)
                .eq(ContractIncomeSettDetailsE::getDeleted, 0));
        log.info("当前确收单关联的确收详情信息:{}", JSON.toJSONString(settDetails));
        if (CollectionUtils.isEmpty(settDetails)) {
            return Collections.emptyList();
        }
        List<String> funIdList = settDetails.stream()
                .map(ContractIncomeSettDetailsE::getPayFundId)
                .distinct()
                .collect(Collectors.toList());

        //查询周期信息
        List<IncomePlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        log.info("当前确收单关联的周期信息:{}", JSON.toJSONString(periodList));
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return Collections.emptyList();
        }

        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeService.lambdaQuery().eq(ContractIncomeConcludeE::getId,contractId).one();
        List<PayIncomePlanE> payIncomePlanEList = new ArrayList<>();
        if(Objects.nonNull(settDetails.get(0).getStartDate()) && Objects.nonNull(settDetails.get(0).getEndDate())){
            for(ContractIncomeSettDetailsE settD : settDetails){
                payIncomePlanEList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                        contractId,
                        settD.getPayFundId(),
                        Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }else{
            if (CollectionUtils.isNotEmpty(periodList)){
                List<ContractIncomePlanConcludeE> planFunList = contractIncomePlanConcludeMapper.getFunDateList(contractId,settDetails.stream().map(ContractIncomeSettDetailsE :: getPayFundId).collect(Collectors.toList()),
                        periodList);
                for(ContractIncomePlanConcludeE settD : planFunList){
                    payIncomePlanEList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                            contractId,
                            settD.getContractPayFundId(),
                            Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
                }
            }
        }
        if (CollectionUtils.isEmpty(payIncomePlanEList)) {
            //throw new OwlBizException("无法更新临时账单,原因：没有收入计划");
            log.error("确收审批无法更新临时账单,原因：没有收入计划,settleId is:{}",settleId);
            return Collections.emptyList();
        }
        // 按清单分组计划并累计其计划未结算金额
        /*Map<String, BigDecimal> funTotalAmount = payIncomePlanEList.stream()
                .collect(Collectors.groupingBy(
                        PayIncomePlanE::getPlanId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                plan -> plan.getPlannedCollectionAmount(),
                                BigDecimal::add
                        )
                ));

        Map<String, BigDecimal> funMap = new HashMap<>();
        payIncomePlanEList.forEach(plan->{
            funMap.put(plan.getId(),plan.getPlannedCollectionAmount());
        });*/
    /*    Map<String, BigDecimal> funAllMap = new HashMap<>();
        for(ContractIncomePlanConcludeE payPlan: contractIncomePlanConcludeEList){
            List<PayIncomePlanE> planCostList = payIncomePlanEList.stream().filter(plan -> plan.getPlanId().equals(payPlan.getId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(planCostList)){
                continue;
            }
            BigDecimal planTotalAmount = planCostList.stream().map(PayIncomePlanE::getPlannedCollectionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> funMap = new HashMap<>();
            planCostList.forEach(plan-> {
                funMap.put(plan.getId(), plan.getPlannedCollectionAmount());
            });
            Map<String, BigDecimal> funReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funMap, planTotalAmount,payPlan.getReductionAmount());
            funAllMap.putAll(funReductionAmountMap);
        }*/
        List<UpdateTemporaryChargeBillF> updateTemporaryChargeBillFs = new ArrayList<>();
        payIncomePlanEList.forEach(plan->{
            //Map<String, BigDecimal> funReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funMap, funTotalAmount.get(plan.getPlanId()),planConcludeE.getReductionAmount());
            UpdateTemporaryChargeBillF updateTemporaryChargeBillF = new UpdateTemporaryChargeBillF();
            updateTemporaryChargeBillF.setReductionAmount(plan.getReductionAmount());
/*
            updateTemporaryChargeBillF.setReductionAmount(Objects.isNull(funAllMap.get(plan.getId())) ? BigDecimal.ZERO : funAllMap.get(plan.getId()));
            plan.setReductionAmount(Objects.isNull(funAllMap.get(plan.getId())) ? BigDecimal.ZERO : funAllMap.get(plan.getId()));
*/
            updateTemporaryChargeBillF.setId(String.valueOf(plan.getBillId()));
            /*BigDecimal ysjnAmount = plan.getPlannedCollectionAmount().subtract(updateTemporaryChargeBillF.getReductionAmount());
            updateTemporaryChargeBillF.setReceivableAmount(ysjnAmount.multiply(new BigDecimal("100")).longValue());*/
            updateTemporaryChargeBillF.setReceivableAmount(plan.getPaymentAmount().multiply(new BigDecimal("100")).longValue());
            updateTemporaryChargeBillF.setSupCpUnitId(contractIncomeConcludeE.getCommunityId());
            updateTemporaryChargeBillF.setExtField7(settleId);
            updateTemporaryChargeBillF.setTaxAmountNew(plan.getTaxAmount().multiply(new BigDecimal("100")));
            /*if(!"差额纳税".equals(plan.getTaxRate())){
                BigDecimal taxRateDecimal = parseTaxRate(plan.getTaxRate());
                BigDecimal noTaxAmount = ysjnAmount.divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
                updateTemporaryChargeBillF.setTaxAmountNew((ysjnAmount.subtract(noTaxAmount)).multiply(new BigDecimal("100")));
            }else{
                PayIncomePlanE planCost = payIncomePlanList.stream().filter(item -> item.getId().equals(plan.getId())).collect(Collectors.toList()).get(0);
                //应收/账单金额*原始税额
                BigDecimal taxAmountRatio = ysjnAmount.divide(plan.getPlannedCollectionAmount(),2,RoundingMode.HALF_UP);
                updateTemporaryChargeBillF.setTaxAmountNew(planCost.getTaxAmount().multiply(taxAmountRatio).multiply(new BigDecimal("100")));
            }*/
            // 确认审批通过更新账单的计提状态、确收状态和确收时间
            updateTemporaryChargeBillF.setReceiptConfirmationStatus(2);
            updateTemporaryChargeBillF.setBusinessReceiptConfirmationStatus(2);
            updateTemporaryChargeBillF.setReceiptConfirmationTime(now);
            updateTemporaryChargeBillFs.add(updateTemporaryChargeBillF);
        });
        //payIncomePlanMapper.updateIncomePlan(payIncomePlanEList);

        Boolean flag = financeFeignClient.temporaryUpdateBatch(updateTemporaryChargeBillFs);
        if (!flag) {
            log.error("iriUpdate error,settleId is:{}",settleId);
            return Collections.emptyList();
        }
        payIncomePlanEList.forEach(in-> in.setBillType(2));
        this.payIncomePlanService.updateBatchById(payIncomePlanEList);
        return updateTemporaryChargeBillFs;
    }

    public List<ContractIncomePlanConcludeE> queryByCostTimeNotFinished(List<String> planIds, List<IncomePlanPeriodV> periodList) {
        return this.baseMapper.queryByCostTimeNotFinished(planIds, periodList);
    }

    public List<ContractPayPlanInnerInfoV> getInnerInfoByContractId(List<String> contractIds) {
        List<ContractPayPlanInnerInfoV> innerInfoVList = this.baseMapper.getInnerInfoByContractId(contractIds);
        for (ContractPayPlanInnerInfoV v : innerInfoVList) {
            int addMonth = getAddMonth(v.getSplitMode());
            ZonedDateTime zdt = v.getGmtExpireEnd().toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime dateTime = zdt.plusMonths(addMonth);
            int dayOfMonth = zdt.getDayOfMonth();
            LocalDate localDate = dateTime.toLocalDate();
            ZonedDateTime future;
            if (dayOfMonth == 1){
                future = dateTime.with(TemporalAdjusters.lastDayOfMonth())
                        .withDayOfMonth( localDate.lengthOfMonth());
            } else {
                future = dateTime.with(TemporalAdjusters.lastDayOfMonth())
                        .withDayOfMonth(Math.min(dayOfMonth - 1, localDate.lengthOfMonth()));
            }
            JSONArray fkArr = JSON.parseArray(v.getFkdwxx());
            if (fkArr.size() > 0){
                JSONObject fkObj = fkArr.getJSONObject(0);
                v.setDraweeId(fkObj.getString(KEY_DRAW_ID));
                v.setDrawee(fkObj.getString(KEY_DRAW_NAME));
            }
            JSONArray skArr = JSON.parseArray(v.getSkdwxx());
            if (skArr.size() > 0){
                JSONObject skObj = skArr.getJSONObject(0);
                v.setPayeeId(skObj.getString(KEY_PAYEE_ID));
                v.setPayee(skObj.getString(KEY_PAYEE_NAME));
            }
            v.setExpireNextEndDate( Date.from(future.toInstant()));
        }
        return innerInfoVList;
    }

    private int getAddMonth(Integer splitMode) {
        if (Objects.isNull(splitMode)){
            return 1;
        }
        int addMonth = 0;
        if (splitMode.equals(SplitModeEnum.MONTH.getCode())){
            addMonth = 1;
        } else if (splitMode.equals(SplitModeEnum.THREE_MONTH.getCode())){
            addMonth = 3;
        } else if (splitMode.equals(SplitModeEnum.HALF_YEAR.getCode())){
            addMonth = 6;
        } else if (splitMode.equals(SplitModeEnum.YEAR.getCode())){
            addMonth = 12;
        } else {
            addMonth = 1;
        }
        return addMonth;
    }

    public List<ContractIncomePlanConcludeE> queryByCostTimeForBill(List<String> planIds, List<IncomePlanPeriodV> periodList) {
        return this.baseMapper.queryByCostTimeForBill(planIds, periodList);
    }

    //根据id删除收入计划
    public Boolean deletedPayIncomePlan(String id){
        ContractIncomePlanConcludeE incomePlan = this.getById(id);
        if (Objects.isNull(incomePlan)) {
            throw new OwlBizException("请输入正确收款计划ID");
        }
        ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeMapper.selectById(incomePlan.getContractId());
        if(Objects.isNull(incomeConcludeE)){
            throw new OwlBizException("该合同不存在");
        }
        //查询关联确收审批
        List<String> canNotRemoveList = settlementConcludeService.getSettlementByPlan(Arrays.asList(id));
        if (CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该收款计划已被确收单使用，不允许删除");
        }
        QueryWrapper<PayIncomePlanE> queryModel = new QueryWrapper<>();
        queryModel.eq("contractId", incomePlan.getContractId());
        queryModel.eq("planId", incomePlan.getId());
        queryModel.eq("deleted",0);
        List<PayIncomePlanE> incomeCostPlanEList = payIncomePlanService.list(queryModel);
        if(CollectionUtils.isNotEmpty(incomeCostPlanEList)){
            List<String> billIdList = incomeCostPlanEList.stream().map(PayIncomePlanE::getBillId).filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toList());
            //[校验]根据临时账单ID获取对应报账单/合同报账单数据
            String message = financeFeignClient.getVoucherBillByReceivableId(billIdList, incomeConcludeE.getCommunityId());
            if(StringUtils.isNotEmpty(message)){
                throw new OwlBizException(message);
            }
            //删除成本计划
            payIncomePlanMapper.deletePayIncomePlan(incomeCostPlanEList.stream().map(PayIncomePlanE::getId).map(String::valueOf).collect(Collectors.toList()));
            //根据临时账单ID删除对应临时账单数据
            financeFeignClient.deleteReceivableBillById(billIdList, incomeConcludeE.getCommunityId());
        }
        return Boolean.TRUE;
    }

    //根据id删除收款计划
    @Transactional
    public Boolean deletedIncomePlan(String id){

        ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeMapper.selectById(id);
        if(Objects.isNull(incomeConcludeE)){
            throw new OwlBizException("该合同不存在");
        }

        //查询收款计划
        List<ContractIncomePlanConcludeE> realPlans = this.list(Wrappers.<ContractIncomePlanConcludeE>lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getContractId, id)
                .eq(ContractIncomePlanConcludeE::getDeleted, 0));
        List<String> realPlanIds = realPlans.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList());
        //查询收入计划
        long payIncomeCount = payIncomePlanService.count(Wrappers.<PayIncomePlanE>lambdaQuery()
                .in(PayIncomePlanE::getPlanId, realPlanIds)
                .eq(PayIncomePlanE::getDeleted, 0));
        //查询关联确收审批
        List<String> canNotRemoveList = settlementConcludeService.getSettlementByPlan(realPlanIds);
        if (payIncomeCount > 0 && CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该收款计划存在确收审批与收入计划，不允许删除");
        }
        if (payIncomeCount > 0) {
            throw new OwlBizException("该收款计划存在收入计划，不允许删除");
        }
        if (CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该收款计划存在确收审批，不允许删除");
        }
        //删除计划
        this.contractIncomePlanConcludeMapper.deleteBatchIds(realPlanIds);
        //删除成功后推送枫行梦
        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(id);
        List<ContractIncomeConcludePlanFxmRecordE> records = contractInfoToFxmCommonService.pushIncomePlan2Fxm(realPlans.stream().filter(x->!x.getPid().equals("0")).collect(Collectors.toList()), concludeE, PlanFxmPushType.DELETE);
        if (CollectionUtils.isNotEmpty(records)) {
            contractIncomeConcludePlanFxmRecordService.saveBatch(records);
        }
        return Boolean.TRUE;
    }
}
