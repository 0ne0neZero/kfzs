package com.wishare.contract.domains.service.revision.pay;

import cn.hutool.core.builder.GenericBuilder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.google.common.collect.Maps;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.income.ContractSrfxxF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementPeriodF;
import com.wishare.contract.apps.fo.revision.pay.settlement.GenerateCostPlanF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.AddTemporaryChargeBillRf;
import com.wishare.contract.apps.remote.fo.UpdateTemporaryChargeBillF;
import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.domains.bo.CommonRangeAmountBO;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.dto.settlementPlan.SettlementPlanResult;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.*;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.IriStatusEnum;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.revision.income.PayCostPlanMapper;
import com.wishare.contract.domains.mapper.revision.pay.*;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPaySettDetailsService;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.income.IncomeConcludePlanV2;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV2;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import com.wishare.contract.domains.vo.settle.*;
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
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/13:57
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContractPayPlanConcludeService extends ServiceImpl<ContractPayPlanConcludeMapper, ContractPayPlanConcludeE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;
    private final ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;

    private final ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;

    private final TransactionTemplate transactionTemplate;

    private final ContractPayFundMapper contractPayFundMapper;

    private final ContractPayConcludeExpandService contractPayConcludeExpandService;

    private final StringRedisTemplate redisTemplate;
    private final ConfigFeignClient configFeignClient;

    private final static String COST_ESTIMATION_CODE_KEY = "COST:ESTIMATION:CODE";
    private final static String KEY_PAYEE_ID = "payeeid";
    private final static String KEY_PAYEE_NAME = "payee";
    private final static String KEY_DRAW_ID = "draweeid";
    private final static String KEY_DRAW_NAME = "drawee";


    @Resource
    @Lazy
    private ContractPayFundService contractPayFundService;
    @Resource
    @Lazy
    private ContractPayConcludeService contractPayConcludeService;

    private final SettlementPlanHelperService planHelperService;

    private final ContractOrgCommonService contractOrgCommonService;
    @Resource
    @Lazy
    private  ContractPaySettlementConcludeService settlementConcludeService;

    @Resource
    @Lazy
    private ContractPayCostPlanService payCostPlanService;

    private final FinanceFeignClient financeFeignClient;
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;
    @Autowired
    private PayCostPlanMapper payCostPlanMapper;
    @Autowired
    private ContractPayConcludeSettlementPlanRelationMapper settlementPlanRelationMapper;
    @Autowired
    private ContractPaySettDetailsService contractPaySettDetailsService;
    @Autowired
    private ContractPayConcludeSettlementPeriodMapper settlementPeriodMapper;
    @Autowired
    private ContractPayCostPlanService contractPayCostPlanService;

    @Nullable
    public ContractPayPlanDetailsV getDetailsById(String id) {
        ContractPayPlanConcludeE map = contractPayPlanConcludeMapper.selectById(id);
        // 多处都需要空值校验,业务上面应该不会出现问题;
        if (Objects.isNull(map)) {
            throw BizException.throw404("抱歉! 支出合同计划表中没有对应的数据");
        }
        ContractPayPlanDetailsV contractPayPlanDetailsV = Global.mapperFacade.map(map, ContractPayPlanDetailsV.class);

        //查询对应的结算单信息
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ContractPaySettlementConcludeE::getContractId, map.getContractId())
                .like(ContractPaySettlementConcludeE::getTermDate,map.getTermDate());
        List<ContractPaySettlementConcludeE> contractPaySettlementConcludeList = contractPaySettlementConcludeMapper.selectList(queryWrapper);

        BigDecimal deductionAmount = BigDecimal.ZERO;

        if(ObjectUtil.isNotEmpty(contractPaySettlementConcludeList)){
            List<BigDecimal> deductionAmounts = contractPaySettlementConcludeList.stream().map( s -> s.getDeductionAmount()).collect(Collectors.toList());
            deductionAmount = deductionAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            contractPayPlanDetailsV.setDeductionAmount(deductionAmount);
        }

        List<ContractPaySettlementConcludeDetailsV> contractPaySettlementConcludeDetailsVs = Global.mapperFacade.mapAsList(contractPaySettlementConcludeList, ContractPaySettlementConcludeDetailsV.class);

        for (ContractPaySettlementConcludeDetailsV s : contractPaySettlementConcludeDetailsVs) {
            s.setPaymentMethodName(PaymentMethodEnum.parseName(s.getPaymentMethod()));
            s.setPaymentStatusName(PaymentStatusEnum.parseName(s.getPaymentStatus()));
            s.setInvoiceStatusName(InvoiceStatusEnum.parseName(s.getInvoiceStatus()));
            s.setSettleStatusName(SettleStatusEnum.parseName(s.getSettleStatus()));
            s.setPaymentTypeName(PaymentTypeEnum.parseName(s.getPaymentType()));
        }

        contractPayPlanDetailsV.setContractPaySettlementConcludeList(contractPaySettlementConcludeDetailsVs);
        return contractPayPlanDetailsV;
    }

    public Boolean save(List<ContractPayPlanAddF> addF) {
        if (addF == null) {
            throw SysException.throw403("付款计划信息不能为空");
        }
        String saveType = addF.get(0).getSaveType();
        List<ContractPayPlanConcludeE> payPlanConcludeES = Global.mapperFacade.mapAsList(addF, ContractPayPlanConcludeE.class);
        List<ContractPayPlanConcludeV> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.getHowOrder(addF.get(0).getContractId(),null);
        Integer howOrder = 0;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            howOrder = contractIncomePlanConcludeVList.get(0).getHowOrder();
        }
        BigDecimal sumAmount = BigDecimal.ZERO;
        for(ContractPayPlanAddF s : addF){
            sumAmount = sumAmount.add(s.getPlannedCollectionAmount());
        }
        ContractPayPlanConcludeE concludeE = new ContractPayPlanConcludeE();
        concludeE.setMerchant(addF.get(0).getMerchant());
        concludeE.setMerchantName(addF.get(0).getMerchantName());
        concludeE.setContractNo(addF.get(0).getContractNo());
        concludeE.setContractName(addF.get(0).getContractName());
        concludeE.setContractId(addF.get(0).getContractId());
        concludeE.setPlannedCollectionAmount(sumAmount);
        concludeE.setNoPayAmount(sumAmount);
        concludeE.setTenantId(tenantId());
        contractPayPlanConcludeMapper.insert(concludeE);
        for(ContractPayPlanConcludeE payPlanConclude : payPlanConcludeES){
            log.info("新增时转换的入库对象: {}", JSONObject.toJSONString(payPlanConclude));
            payPlanConclude.setPayNotecode(contractCode())      // 付款计划编号
                    .setTenantId(tenantId());        // 租户id
            if (Objects.equals("1", saveType)) {
                payPlanConclude.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                payPlanConclude.setReviewStatus(2);
            }
            payPlanConclude.setPid(concludeE.getId());
            payPlanConclude.setNoPayAmount(payPlanConclude.getSettlementAmount());
            payPlanConclude.setHowOrder(howOrder + 1);
            contractPayPlanConcludeMapper.insert(payPlanConclude);
        }
        return Boolean.TRUE;
    }

    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {

        LocalDate planTime = LocalDate.now();

        if(planDateF.getHowOrder() != null){
            List<ContractPayPlanConcludeV> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.getHowOrder(planDateF.getContractId(),planDateF.getHowOrder());
            for(ContractPayPlanConcludeV s : contractIncomePlanConcludeVList){
                contractPayPlanConcludeMapper.deleteById(s.getId());
            }
        }

        List<ContractPayPlanConcludeV> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.getHowOrder(planDateF.getContractId(),null);
        Integer termDate = 1;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            termDate = contractIncomePlanConcludeVList.stream().max(Comparator.comparing(ContractPayPlanConcludeV :: getTermDate)).get().getTermDate() + 1;
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
                getSplitWay(planTime, SplitEnum.halfYear.getWay(), planDateF, planDateList, termDate);
                break;
            case 4: // 按季度收款
                // 每次加1/4年即3个月
                getSplitWay(planTime, SplitEnum.quarter.getWay(), planDateF, planDateList, termDate);
                break;
            case 5: // 按月收款,一次加1个月
                getSplitWay(planTime, SplitEnum.month.getWay(), planDateF, planDateList, termDate);
                break;
            default:
                throw BizException.throw403("拆分方式错误,请检查拆分方式是否正确");
        }
        return planDateList;
    }

    public ContractPayPlanPidConcludeV listPid(ContractPayPlanConcludeListF contractIncomeConcludeListF){
        ContractPayPlanPidConcludeV concludeV = new ContractPayPlanPidConcludeV();
        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList =  contractPayPlanConcludeMapper.getChoosePlanInfo(contractIncomeConcludeListF.getNameNo());
        concludeV.setConcludeVList(contractPayPlanConcludeVList);
        if(ObjectUtils.isNotEmpty(contractPayPlanConcludeVList)){
            List<ContractPayFundV> contractPayFundVList = contractPayFundMapper.getContractPaySettFundList(contractPayPlanConcludeVList.get(0).getContractId());
            concludeV.setContractPayFundVList(contractPayFundVList);
        }
        return concludeV;
    }

    public ContractPayPlanPidConcludeV listPidNew(ContractPayPlanConcludeListF param) {
        ContractPayPlanPidConcludeV concludeV = new ContractPayPlanPidConcludeV();
        //查询用户权限范围内的组织
        ContractOrgPermissionV orgPermission = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermission.getRadio())) {
            return concludeV;
        }
        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList;
        if (RadioEnum.ALL.equals(orgPermission.getRadio())) {
            contractPayPlanConcludeVList = contractPayPlanConcludeMapper.getChoosePlanInfoBak(param.getNameNo(),param.getIsNK(), param.getCommunityId());
        } else {
            contractPayPlanConcludeVList = contractPayPlanConcludeMapper.getChoosePlanInfoNew(param.getNameNo(), orgPermission.getOrgIds(),param.getIsNK(), param.getCommunityId());
        }
        caculateContractTotalSettlementAmount(contractPayPlanConcludeVList);

        if (ObjectUtils.isNotEmpty(contractPayPlanConcludeVList)) {
            List<ContractPayFundV> contractPayFundVList = contractPayFundMapper.getContractPaySettFundList(contractPayPlanConcludeVList.get(0).getContractId());
            contractPayFundVList.forEach(x->x.setIsCostData(StringUtils.isEmpty(x.getCbApportionId()) ? 0 : 1));
            concludeV.setContractPayFundVList(contractPayFundVList);

            List<String> ztList = new ArrayList<>();
            ztList.add("未推送合同");
            List<ContractPayConcludeE> contractConcludeEList = contractPayConcludeService.lambdaQuery()
                    .in(ContractPayConcludeE::getPid,contractPayPlanConcludeVList.stream().map(ContractPayPlanConcludeV::getContractId).collect(Collectors.toList()))
                    .eq(ContractPayConcludeE::getDeleted,0)
                    .eq(ContractPayConcludeE::getContractType,ContractTypeEnum.补充协议.getCode())
                    .ne(ContractPayConcludeE::getContractNature, ConcludeContractNatureEnum.SUCCESS.getCode()).list();
            if(CollectionUtils.isNotEmpty(contractConcludeEList)){
                ztList.addAll(contractConcludeEList.stream().map(ContractPayConcludeE :: getPid).collect(Collectors.toList()));
            }
            contractPayPlanConcludeVList.forEach(x-> {
                x.setIsHaveNoPushedContract(ztList.contains(x.getContractId()));
            });
        }

        concludeV.setConcludeVList(contractPayPlanConcludeVList);
        return concludeV;
    }

    /**
     * 计算每个合同的结算金额总额
     *
     * @param contractPayPlanConcludeVList
     */
    private void caculateContractTotalSettlementAmount(List<ContractPayPlanConcludeV> contractPayPlanConcludeVList) {
        if (CollectionUtils.isEmpty(contractPayPlanConcludeVList)) {
            return;
        }
        // 合同Id去重集合
        List<String> contractIds = contractPayPlanConcludeVList.stream()
                .map(ContractPayPlanConcludeV::getContractId)
                .distinct()
                .collect(Collectors.toList());

        // 根据合同Id查询结算明细
        List<ContractPaySettlementConcludeE> paySettlements = contractPaySettlementConcludeMapper.selectList(Wrappers.<ContractPaySettlementConcludeE>lambdaQuery()
                .ne(ContractPaySettlementConcludeE::getPid, "0")
                .in(ContractPaySettlementConcludeE::getContractId, contractIds)
                .eq(ContractPaySettlementConcludeE::getReviewStatus, ReviewStatusEnum.已通过.getCode())
                .eq(ContractPaySettlementConcludeE::getDeleted, 0));
        Map<String, List<ContractPaySettlementConcludeE>> paySettlementMap = new HashMap<>();
        // 结算明细集合不为空,将其转为map
        if (CollectionUtils.isNotEmpty(paySettlements)) {
            paySettlementMap = paySettlements.stream().collect(Collectors.groupingBy(ContractPaySettlementConcludeE::getContractId));
        }
        // 循环合同Id集合
        for (ContractPayPlanConcludeV concludeV : contractPayPlanConcludeVList) {
            if (!paySettlementMap.containsKey(concludeV.getContractId())) {
                continue;
            }
            List<ContractPaySettlementConcludeE> subSetts = paySettlementMap.get(concludeV.getContractId());
            BigDecimal total = subSetts.stream().map(ContractPaySettlementConcludeE::getPlannedCollectionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            //计算该合同的扣款总额
            List<ContractPaySettlementConcludeE> settlementConcludeES = paySettlementMap.get(concludeV.getContractId());
            BigDecimal deductionTotal = settlementConcludeES.stream().map(ContractPaySettlementConcludeE::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            //减去扣款总额
            BigDecimal totalSettlement = Optional.ofNullable(total.subtract(deductionTotal)).orElse(BigDecimal.ZERO);
            // 合同结算金额总额
            concludeV.setContractTotalSettlementAmount(totalSettlement);
            // 合同应结算金额总额
            concludeV.setContractTotalSettledAmount(total);
        }
    }

    public List<ContractPayPlanConcludeV> list(ContractPayPlanConcludeListF contractIncomeConcludeListF){
        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList =  contractPayPlanConcludeMapper.getHowOrder(contractIncomeConcludeListF.getContractId(),contractIncomeConcludeListF.getHowOrder());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractPayPlanConcludeV s : contractPayPlanConcludeVList){
            ssettlementAmount = ssettlementAmount.add(s.getPaymentAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for(ContractPayPlanConcludeV s : contractPayPlanConcludeVList){
            s.setSsettlementAmount(ssettlementAmount);
            s.setSreceiptAmount(sreceiptAmount);
        }
        return contractPayPlanConcludeVList;
    }

    public List<ContractPayPlanConcludeInfoV> listInfo(ContractPayPlanConcludeListF contractIncomeConcludeListF){
        List<ContractPayPlanConcludeInfoV> contractPayPlanConcludeVList =  contractPayPlanConcludeMapper.getHowOrderInfo(contractIncomeConcludeListF.getContractId(),contractIncomeConcludeListF.getHowOrder());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractPayPlanConcludeInfoV s : contractPayPlanConcludeVList){
            ssettlementAmount = ssettlementAmount.add(s.getPaymentAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for(ContractPayPlanConcludeInfoV s : contractPayPlanConcludeVList){
            s.setSsettlementAmount(ssettlementAmount);
            s.setSreceiptAmount(sreceiptAmount);
        }
        return contractPayPlanConcludeVList;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractPayPlanConcludeV> page(PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        Page<ContractPayPlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractPayPlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        IPage<ContractPayPlanConcludeV> pageList
                = contractPayPlanConcludeMapper.collectionPlanDetailPage(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
        List<ContractPayPlanConcludeV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(ContractPayPlanConcludeV::getId).collect(Collectors.toList());
        List<ContractPayPlanConcludeV> concludeVList = contractPayPlanConcludeMapper.queryByPath(queryModel, parentIdList, getIdentityInfo().get().getTenantId());
        List<ContractPayPlanConcludeV> list = TreeUtil.treeing(concludeVList);
        for(ContractPayPlanConcludeV s : list){
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

    public PageV<ContractPayPlanConcludeInfoV> pageInfo(PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        Page<ContractPayPlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractPayPlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        IPage<ContractPayPlanConcludeInfoV> pageList
                = contractPayPlanConcludeMapper.pageInfo(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
        return PageV.of(form, pageList.getTotal(), pageList.getRecords());
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public ContractPayPlanConcludeSumV accountAmountSum(PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            ContractPayPlanConcludeSumV sumV = new ContractPayPlanConcludeSumV();
            sumV.setPlannedCollectionAmountSum(new BigDecimal("0.00"));
            sumV.setPaymentAmountSum(new BigDecimal("0.00"));
            return sumV;
        }
        QueryWrapper<ContractPayPlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio()) && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            queryModel.in("cc.departId", orgPermissionV.getOrgIds());
        }
        return transactionTemplate.execute(status -> {
            try {
                return contractPayPlanConcludeMapper.accountAmountSum(conditionPage(queryModel, getIdentityInfo().get().getTenantId()));
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.error("支出合同计算总额失败,已回滚，失败原因:{}", ex.getMessage());
                throw SysException.throw403("支出合同计算总额失败,已回滚");
            }
        });
    }

    /**
     * 根据Id更新
     *
     * @param contractPayConcludeF 根据Id更新
     */
    public void update(List<ContractPayPlanConcludeUpdateF> contractPayConcludeF) {
        if (contractPayConcludeF == null) {
            throw SysException.throw403("付款计划信息不能为空");
        }
        String saveType = contractPayConcludeF.get(0).getSaveType();
        List<ContractPayPlanConcludeE> maps = Global.mapperFacade.mapAsList(contractPayConcludeF, ContractPayPlanConcludeE.class);
        for(ContractPayPlanConcludeE map : maps){
            if (Objects.equals("1", saveType)) {
                map.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                map.setReviewStatus(2);
            }
            contractPayPlanConcludeMapper.updateById(map);
        }
    }

    /**
     * @param id 根据Id删除
     * @return 删除结果
     */
    public Boolean removeById(String id) {
        return transactionTemplate.execute(status -> {
            try {
                ContractPayPlanConcludeE map = contractPayPlanConcludeMapper.selectById(id);
                contractPayPlanConcludeMapper.deleteById(id);
                LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ContractPayPlanConcludeE::getPid, map.getPid())
                        .eq(ContractPayPlanConcludeE::getDeleted,0);
                List<ContractPayPlanConcludeE> concludeEList = contractPayPlanConcludeMapper.selectList(queryWrapper);
                if(!ObjectUtils.isNotEmpty(concludeEList)){
                    contractPayPlanConcludeMapper.deleteById(map.getPid());
                }
                return Boolean.TRUE;
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.error("删除支出合同失败,已回滚，失败原因:{}", ex.getMessage());
                throw SysException.throw403("删除支出合同失败,已回滚");
            }
        });
    }

    /**
     * @param id 根据Id提交
     */
    public void sumbitId(String id) {
        ContractPayPlanConcludeE map = contractPayPlanConcludeMapper.selectById(id);
        map.setReviewStatus(2);
        contractPayPlanConcludeMapper.updateById(map);
    }

    /**
     *
     * @param id 根据Id反审核
     */
    public void returnId(String id){
        ContractPayPlanConcludeE map = contractPayPlanConcludeMapper.selectById(id);
        map.setReviewStatus(0);
        contractPayPlanConcludeMapper.updateById(map);
    }


    private QueryWrapper<ContractPayPlanConcludePageF> conditionPage(QueryWrapper<ContractPayPlanConcludePageF> queryModel, String tenantId) {
        queryModel.eq("ccp.deleted", 0);
        queryModel.eq("ccp.tenantId", tenantId);
        return queryModel;
    }

    private String contractCode() {
        //生成合同编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;子合同编号规则：主合同编号+两位数数值，如YYHT220816000101
        String year = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date());
        return "FKJH" + year;
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
        for (int i = termDate + 1; i < 999; i++) {
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
        BigDecimal splitRatioAmount = new BigDecimal(100).setScale(2).divide(new BigDecimal(j),2, RoundingMode.HALF_UP);
        for(int c = 0; c < j; c ++){
            ContractPlanDateV contractPlanDateV = planDateList.get(c);
            contractPlanDateV.setPlannedCollectionAmount(splitPlanAmont);
            contractPlanDateV.setRatioAmount(splitRatioAmount);
            planDateList.set(c,contractPlanDateV);
        }
    }


    public List<ContractPayPlanConcludeE> getByIdList(List<String> idList) {
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPayPlanConcludeE::getId, idList)
                .eq(ContractPayPlanConcludeE::getDeleted,0);
        return list(queryWrapper);
    }

    private void checkTaxation(ContractPayPlanAddF addF) {
        BigDecimal planAllAmount = addF.getPlanAllAmount();
        BigDecimal noPlanedAmount = addF.getNoPlanAmount();
        BigDecimal plannedAmount = addF.getPlannedCollectionAmount();
        // 1.所有的[计划收款金额(分)]要小于等于[计划收款金额(总)]
        if (plannedAmount.compareTo(planAllAmount) > 0) {
            throw BizException.throw400("[计划收款金额(分)]相加不能超出[总计划金额]");
        }
        // 2.所有的[计划收款金额(分)]不能大于[未计划金额]
        if (plannedAmount.compareTo(noPlanedAmount) > 0) {
            throw BizException.throw400("[计划收款金额(分)]相加不能大于[未计划金额]");
        }
    }

    public BigDecimal getByContractList(String contractId) {
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPayPlanConcludeE::getContractId, contractId)
                .eq(ContractPayPlanConcludeE::getReviewStatus,2)
                .eq(ContractPayPlanConcludeE::getDeleted,0);
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = list(queryWrapper);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(contractPayPlanConcludeEList)){
            for(ContractPayPlanConcludeE s : contractPayPlanConcludeEList){
                bigDecimal = bigDecimal.add(s.getPlannedCollectionAmount());
            }
        }
        return bigDecimal;
    }

    /**
     * 根据Id更新
     *
     * @param contractPayConcludeF 根据Id更新
     */
    public void update(ContractPayPlanConcludeUpdateF contractPayConcludeF){
        if (contractPayConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayPlanConcludeE map = Global.mapperFacade.map(contractPayConcludeF, ContractPayPlanConcludeE.class);
        contractPayPlanConcludeMapper.updateById(map);
    }

    public String save(ContractPayPlanAddF addF){
        ContractPayPlanConcludeE map = Global.mapperFacade.map(addF, ContractPayPlanConcludeE.class);
        map.setPayNotecode(contractCode());
        map.setTermDate(1);
        map.setTenantId(tenantId());
        if(addF.getSaveType().equals("2")){
            map.setReviewStatus(2);
        }
        contractPayPlanConcludeMapper.insert(map);
        return map.getId();
    }

    @Transactional(rollbackFor =  Exception.class)
    public SettlementPlanResult editSettlementPlan(SettlementPlanAddF req) {
        if (StringUtils.isBlank(req.getContractId())) {
            throw new OwlBizException("合同id必传");
        }
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery().eq(ContractPayPlanConcludeE::getContractId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            throw new OwlBizException("非法操作");
        }
        Integer splitMode = contractPayPlanConcludeEList.get(1).getSplitMode();
        Optional<ContractPayPlanConcludeE> concludeEOptional = contractPayPlanConcludeEList.stream().filter(p->0 != p.getPaymentStatus()).findAny();
        if (concludeEOptional.isPresent() && !Objects.equals(splitMode, req.getSplitMode())) {
            throw new OwlBizException("不支持修改结算周期");
        }
        boolean removeAll = removeAll(req.getSplitMode(),splitMode,req.getContractId());
        List<ContractPayConcludeE> contractPayConcludeEList = contractPayConcludeService.lambdaQuery().eq(ContractPayConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        List<ContractPayPlanConcludeE> notOneTimeContractPayPlanList = new ArrayList<>();
        if (5 != req.getSplitMode() && !removeAll) {
            notOneTimeContractPayPlanList = this.lambdaQuery().eq(ContractPayPlanConcludeE::getContractId,req.getContractId()).ne(ContractPayPlanConcludeE::getPid,"0").ne(ContractPayPlanConcludeE::getSplitMode,5).list();
        }

        List<List<ContractPayPlanAddF>> lists = req.getContractPayPlanAddFLists();
        planHelperService.editParamValidateAndFillData(req, contractPayConcludeEList.get(0),notOneTimeContractPayPlanList,removeAll);
//        validatePlanId(req.getContractPayPlanAddFLists(),settlementPlanResult,removeAll);
        validateSettlementPlans(req,contractPayConcludeEList.get(0));
        lists.forEach(list->{
            List<ContractPayPlanConcludeE> newContractPayPlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractPayPlanConcludeE.class);
            if (removeAll) {
                //如果是全部删除，那么手动将id设置为null，执行新增操作
                newContractPayPlanConcludeEList.forEach(plan -> plan.setId(null));
            }

            ContractPayConcludeE parentConcludeE = contractPayConcludeEList.stream().filter(c->"0".equals(c.getPid())).findFirst().get();
            IdentityInfo identityInfo = curIdentityInfo();
            newContractPayPlanConcludeEList.forEach(contract->{
                contract.setMerchant(contractPayConcludeEList.get(0).getOppositeOneId());
                contract.setMerchantName(contractPayConcludeEList.get(0).getOppositeOne());
                contract.setContractName(contractPayConcludeEList.get(0).getName());
                contract.setTenantId(identityInfo.getTenantId());
                contract.setCreator(identityInfo.getUserId());
                contract.setCreatorName(identityInfo.getUserName());
                contract.setGmtCreate(LocalDateTime.now());
                contract.setOperatorName(identityInfo.getUserName());
                contract.setOperator(identityInfo.getUserName());
                contract.setNoPayAmount(contract.getSettlementAmount());
                contract.setTaxRate(StringUtils.isNotBlank(contract.getTaxRate()) ? contract.getTaxRate().split("%")[0] : null);
                contract.setReviewStatus(2);
                contract.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                contract.setPid(parentConcludeE.getId());
                contract.setCostEstimationCode(StringUtils.isBlank(contract.getCostEstimationCode()) ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,contract.getTermDate(),contract.getPlannedCollectionTime().getYear() + "-" + contract.getPlannedCollectionTime().getMonthValue(),parentConcludeE.getConmaincode()) : contract.getCostEstimationCode());
            });
            newContractPayPlanConcludeEList.removeIf(c->"0".equals(c.getPid()));
            if (CollectionUtils.isNotEmpty(newContractPayPlanConcludeEList)) {
                removeFirst(newContractPayPlanConcludeEList,req.getContractId(),req.getSplitMode(),removeAll);
                this.saveOrUpdateBatch(newContractPayPlanConcludeEList);
            }
        });
        return new SettlementPlanResult();
    }

    @Transactional(rollbackFor =  Exception.class)
    public String editSettlementPlanV2(SettlementPlanEditV req) {
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.queryByContractId(req.getContractId());
        if (Objects.isNull(payConcludeE)) {
            throw new OwlBizException("合同不存在");
        }
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPayPlanConcludeE::getContractId,req.getContractId());
        queryWrapper.eq(ContractPayPlanConcludeE::getPid,"0");
        queryWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
        ContractPayPlanConcludeE concludePlanE = contractPayPlanConcludeMapper.selectOne(queryWrapper);
        if (Objects.isNull(concludePlanE)){
            throw new OwlBizException("数据异常，无法编辑");
        }
        //更改PID为0中的计划结算金额，列表展示汇总时使用
        BigDecimal plannedCollectionAmountSum = req.getPlanV2List().stream().flatMap(List::stream)
                .map(PayConcludePlanV2::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        concludePlanE.setPlannedCollectionAmount(plannedCollectionAmountSum);
        contractPayPlanConcludeMapper.updateById(concludePlanE);
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = Lists.newArrayList();
        for (List<PayConcludePlanV2> planV2s : req.getPlanV2List()) {
            for (PayConcludePlanV2 planV2 : planV2s) {
                planV2.setPid(concludePlanE.getId());
            }
            contractPayPlanConcludeEList.addAll(Global.mapperFacade.mapAsList(planV2s, ContractPayPlanConcludeE.class));
        }
        // 删除id范围之外的plan
        LambdaQueryWrapper<ContractPayPlanConcludeE> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ContractPayPlanConcludeE::getContractId,req.getContractId());
        deleteWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
        deleteWrapper.ne(ContractPayPlanConcludeE::getPid,"0");
        deleteWrapper.notIn(ContractPayPlanConcludeE::getId,contractPayPlanConcludeEList.stream()
                .map(ContractPayPlanConcludeE::getId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
        //删除结算计划
        List<ContractPayPlanConcludeE> deletePlanList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(contractPayPlanConcludeEList.stream()
                .map(ContractPayPlanConcludeE::getId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()))){
            deletePlanList = contractPayPlanConcludeMapper.selectList(deleteWrapper);
            contractPayPlanConcludeMapper.delete(deleteWrapper);
        }

        //当前使用结算计划
        List<ContractPayPlanConcludeE> usedPlanList = contractPayPlanConcludeEList.stream().filter(plan->StringUtils.isNotEmpty(plan.getId())).collect(Collectors.toList());
        //新增结算计划
        List<ContractPayPlanConcludeE> newPlanList = contractPayPlanConcludeEList.stream().filter(plan->StringUtils.isEmpty(plan.getId())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(usedPlanList)){
            this.saveOrUpdateBatch(usedPlanList);
        }
        if(CollectionUtils.isNotEmpty(newPlanList)) {
            this.saveOrUpdateBatch(newPlanList);
        }
        //查询有无NK合同，若有，对其结算计划进行修改
        ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(req.getContractId());
        if(Objects.isNull(nkConclude) || payConcludeE.getDeleted().equals(1) || NkStatusEnum.已关闭.getCode().equals(payConcludeE.getNkStatus())){
            return "success";
        }

        //将YJ被删除计划对应NK结算计划删除
        if(CollectionUtils.isNotEmpty(deletePlanList)){
            LambdaQueryWrapper<ContractPayPlanConcludeE> deleteNKWrapper = new LambdaQueryWrapper<>();
            deleteNKWrapper.eq(ContractPayPlanConcludeE::getContractId,nkConclude.getId());
            deleteNKWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
            deleteNKWrapper.in(ContractPayPlanConcludeE::getMainId,deletePlanList.stream()
                    .map(ContractPayPlanConcludeE::getId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList()));
            contractPayPlanConcludeMapper.delete(deleteNKWrapper);
        }
        //将YJ修改计划对应NK结算计划修改
        if(CollectionUtils.isNotEmpty(usedPlanList)){
            Map<String, ContractPayPlanConcludeE> useMap = usedPlanList.stream()
                    .collect(Collectors.toMap(
                            ContractPayPlanConcludeE::getId,
                            plan -> plan,
                            (existing, replacement) -> existing  // 如果ID重复，保留已存在的
                    ));
            LambdaQueryWrapper<ContractPayPlanConcludeE> selectNKWrapper = new LambdaQueryWrapper<>();
            selectNKWrapper.eq(ContractPayPlanConcludeE::getContractId,nkConclude.getId());
            selectNKWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
            selectNKWrapper.in(ContractPayPlanConcludeE::getMainId,usedPlanList.stream()
                    .map(ContractPayPlanConcludeE::getId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList()));
            //现有NK结算计划
            List<ContractPayPlanConcludeE> nkPlanList = contractPayPlanConcludeMapper.selectList(selectNKWrapper);
            nkPlanList.forEach(x->{
                x.setCostStartTime(useMap.get(x.getMainId()).getCostStartTime());
                x.setCostEndTime(useMap.get(x.getMainId()).getCostEndTime());
                x.setPlannedCollectionTime(useMap.get(x.getMainId()).getPlannedCollectionTime());
            });
            this.saveOrUpdateBatch(nkPlanList);
        }
        //将YJ新增计划同步至NK
        if(CollectionUtils.isNotEmpty(newPlanList)){
            LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanPidWrapper = new LambdaQueryWrapper<>();
            queryPlanPidWrapper.eq(ContractPayPlanConcludeE::getContractId, nkConclude.getId())
                    .eq(ContractPayPlanConcludeE::getPid,"0")
                    .eq(ContractPayPlanConcludeE::getDeleted,0);
            List<ContractPayPlanConcludeE> concludePlanPidList = contractPayPlanConcludeMapper.selectList(queryPlanPidWrapper);

            List<ContractPayFundE> funList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, nkConclude.getId())
                    .eq(ContractPayFundE.DELETED, 0)
            );
            Map<String, String> funMainMap = funList.stream()
                    .collect(Collectors.toMap(
                            ContractPayFundE::getMainId,
                            ContractPayFundE::getId));
            newPlanList.forEach(x->{
                x.setMainId(x.getId());
                x.setContractPayFundId(funMainMap.get(x.getContractPayFundId()));
                x.setId(null);
                x.setPid(concludePlanPidList.get(0).getId());
                x.setContractId(nkConclude.getId());
                x.setPlannedCollectionAmount(BigDecimal.ZERO);
                x.setSettlementAmount(BigDecimal.ZERO);
                x.setTaxAmount(BigDecimal.ZERO);
                x.setNoTaxAmount(BigDecimal.ZERO);
                x.setRatioAmount(BigDecimal.ZERO);
            });
            this.saveOrUpdateBatch(newPlanList);
        }
        return "success";
    }

    private boolean removeAll(Integer newSplitMode, Integer oldSplitMode, String contractId) {
        //先通过已有的功能判断，当前合同的结算计划是否能够编辑"结算周期"
        SettlePlanDetailQuery req = new SettlePlanDetailQuery();
        req.setContractId(contractId);
        PreSettlePlanV detail = this.details(req);
        //只有在新旧结算周期相同 且 当前结算周期不能编辑的情况才直接return
        if (newSplitMode.equals(oldSplitMode) && !detail.getCanEditSplitMode()) {
            //说明新旧结算周期相同且不能编辑结算周期（也就意味着当前的结算计划是被引用的，走杰哥的原编辑逻辑处理）
            return false;
        }
        //否则：1.新旧结算周期不同：说明一定可以编辑结算周期且切换为新的结算周期，那么应该删除原结算计划
        //2.新旧结算周期相同，但是目前是可以编辑结算周期的，说明当前的结算计划还是可以被切换的，为了避免产生重复组，还是将原计划删除
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPayPlanConcludeE::getContractId, contractId);
        queryWrapper.ne(ContractPayPlanConcludeE::getPid, "0");
        this.remove(queryWrapper);
        return true;
    }

    private void removeFirst(List<ContractPayPlanConcludeE> newContractPayPlanConcludeEList, String contractId, Integer splitMode, boolean removeAll) {
        if (removeAll) {
            return;
        }
        if (!SplitModeEnum.ACTUAL_SETTLE.getCode().equals(splitMode)) {
            return;
        }
        List<ContractPayPlanConcludeE> list = this.lambdaQuery()
                .eq(ContractPayPlanConcludeE::getContractId,contractId)
                .eq(ContractPayPlanConcludeE::getSettlePlanGroup,newContractPayPlanConcludeEList.get(0).getSettlePlanGroup())
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> oldList = newContractPayPlanConcludeEList.stream().map(ContractPayPlanConcludeE::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oldList)) {
            return;
        }
        list.removeIf(l->oldList.contains(l.getId()));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        this.removeBatchByIds(list.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList()));
    }

    private void validatePlanId(List<List<ContractPayPlanAddF>> contractPayPlanAddFLists, SettlementPlanResult settlementPlanResult, boolean removeAll) {
        if (removeAll) {
            return;
        }
        List<String> planIdList = contractPayPlanAddFLists.stream().flatMap(List::stream).map(ContractPayPlanAddF::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(planIdList)) {
            settlementPlanResult.setSuccess(false);
            settlementPlanResult.setErrorMessage(List.of("计划id不合法,请检查"));
            return;
        }
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery().in(ContractPayPlanConcludeE::getId,planIdList).list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            settlementPlanResult.setSuccess(false);
            settlementPlanResult.setErrorMessage(List.of("计划id不合法,查询不到计划,请检查"));
            return;
        }
        if (contractPayPlanConcludeEList.size() != planIdList.size()) {
            settlementPlanResult.setSuccess(false);
            settlementPlanResult.setErrorMessage(List.of("部分计划id不合法,请检查"));
        }
    }

    @Transactional(rollbackFor =  Exception.class)
    public SettlementPlanResult saveSettlementPlan(SettlementPlanAddF req) {
        List<ContractPayConcludeE> contractPayConcludeEList = contractPayConcludeService.lambdaQuery().eq(ContractPayConcludeE::getId, req.getContractId()).list();
        if (CollectionUtils.isEmpty(contractPayConcludeEList)) {
            throw new OwlBizException("合同不存在");
        }
        List<ContractPayPlanConcludeE> existContractPayPlanConcludeEList = this.lambdaQuery().eq(ContractPayPlanConcludeE::getContractId,req.getContractId()).list();
        if (CollectionUtils.isNotEmpty(existContractPayPlanConcludeEList)) {
            throw new OwlBizException("非法操作,结算计划已经新增过,请使用编辑");
        }
        planHelperService.paramValidate(req, contractPayConcludeEList.get(0));

        validateSettlementPlans(req,contractPayConcludeEList.get(0));

        planHelperService.fillGroupCodeAndTerm(req.getContractPayPlanAddFLists());

        IdentityInfo identityInfo = curIdentityInfo();
        List<List<ContractPayPlanAddF>> lists = req.getContractPayPlanAddFLists();



        BigDecimal plannedCollectionAmountSum = lists.stream().flatMap(List::stream)
                .map(ContractPayPlanAddF::getPlannedCollectionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        ContractPayPlanConcludeE concludeE = new ContractPayPlanConcludeE();
        concludeE.setMerchant(contractPayConcludeEList.get(0).getOppositeOneId());
        concludeE.setMerchantName(contractPayConcludeEList.get(0).getOppositeOne());
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
        contractPayPlanConcludeMapper.insert(concludeE);


        lists.forEach(list->{
            List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractPayPlanConcludeE.class);
            contractPayPlanConcludeEList.forEach(contract->{
                contract.setMerchant(contractPayConcludeEList.get(0).getOppositeOneId());
                contract.setMerchantName(contractPayConcludeEList.get(0).getOppositeOne());
                contract.setContractName(contractPayConcludeEList.get(0).getName());
                contract.setTenantId(identityInfo.getTenantId());
                contract.setCreator(identityInfo.getUserId());
                contract.setCreatorName(identityInfo.getUserName());
                contract.setGmtCreate(LocalDateTime.now());
                contract.setOperatorName(identityInfo.getUserName());
                contract.setOperator(identityInfo.getUserName());
                contract.setNoPayAmount(contract.getSettlementAmount());
                contract.setPid(concludeE.getId());
                contract.setTaxRate(StringUtils.isNotBlank(contract.getTaxRate()) ? contract.getTaxRate().split("%")[0] : null);
                contract.setReviewStatus(2);
                contract.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                contract.setCostEstimationCode(StringUtils.isBlank(contract.getCostEstimationCode()) ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,contract.getTermDate(),contract.getPlannedCollectionTime().getYear() + "-" + contract.getPlannedCollectionTime().getMonthValue(),contractPayConcludeEList.get(0).getConmaincode()) : contract.getCostEstimationCode());
            });
            this.saveBatch(contractPayPlanConcludeEList);
        });
        return new SettlementPlanResult();
    }

    @Transactional(rollbackFor =  Exception.class)
    public String saveSettlementPlanV2(SettlementPlanEditV req) {
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.queryByContractId(req.getContractId());
        if (Objects.isNull(payConcludeE)) {
            throw new OwlBizException("合同不存在");
        }
        IdentityInfo identityInfo = curIdentityInfo();
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPayPlanConcludeE::getContractId,req.getContractId());
        queryWrapper.eq(ContractPayPlanConcludeE::getPid, "0");
        queryWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
        ContractPayPlanConcludeE concludeE = contractPayPlanConcludeMapper.selectOne(queryWrapper);
        if (Objects.isNull(concludeE)){
            concludeE = new ContractPayPlanConcludeE();
            concludeE.setMerchant(payConcludeE.getOppositeOneId());
            concludeE.setMerchantName(payConcludeE.getOppositeOne());
            concludeE.setContractNo(payConcludeE.getContractNo());
            concludeE.setContractName(payConcludeE.getName());
            concludeE.setContractId(payConcludeE.getId());
            concludeE.setPlannedCollectionAmount(Objects.nonNull(payConcludeE.getChangContractAmount()) && payConcludeE.getChangContractAmount().compareTo(BigDecimal.ZERO) > 0 ? payConcludeE.getChangContractAmount() :payConcludeE.getContractAmountOriginalRate());
            concludeE.setTenantId(tenantId());
            concludeE.setCreator(identityInfo.getUserId());
            concludeE.setCreatorName(identityInfo.getUserName());
            concludeE.setGmtCreate(LocalDateTime.now());
            concludeE.setOperatorName(identityInfo.getUserName());
            concludeE.setOperator(identityInfo.getUserName());
            concludeE.setReviewStatus(2);
            contractPayPlanConcludeMapper.insert(concludeE);
        }

        ContractPayPlanConcludeE finalConcludeE = concludeE;
        req.getPlanV2List().forEach(list->{
            List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = Global.mapperFacade.mapAsList(list, ContractPayPlanConcludeE.class);
            contractPayPlanConcludeEList.forEach(contract->{
                contract.setMerchant(payConcludeE.getOppositeOneId());
                contract.setMerchantName(payConcludeE.getOppositeOne());
                contract.setContractName(payConcludeE.getName());
                contract.setTenantId(identityInfo.getTenantId());
                contract.setCreator(identityInfo.getUserId());
                contract.setCreatorName(identityInfo.getUserName());
                contract.setGmtCreate(LocalDateTime.now());
                contract.setOperatorName(identityInfo.getUserName());
                contract.setOperator(identityInfo.getUserName());
                contract.setNoPayAmount(contract.getSettlementAmount());
                contract.setPid(finalConcludeE.getId());
                contract.setTaxRate(StringUtils.isNotBlank(contract.getTaxRate()) ? contract.getTaxRate().split("%")[0] : null);
                contract.setReviewStatus(2);
                contract.setInvoiceStatus(InvoiceStatusEnum.未结算.getCode());
                contract.setCostEstimationCode(StringUtils.isBlank(contract.getCostEstimationCode())
                        ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,contract.getTermDate(),contract.getPlannedCollectionTime().getYear() + "-"
                        + contract.getPlannedCollectionTime().getMonthValue(),payConcludeE.getConmaincode())
                        : contract.getCostEstimationCode());
            });
            this.saveBatch(contractPayPlanConcludeEList);
        });
        return "success";
    }



    public PreSettlePlanV preSettlementPlan(PreSettlePlanQuery req) {
        PreSettlePlanV preSettlePlanV = new PreSettlePlanV();
        List<ContractPayFundE> list = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, req.getContractId())
                .eq(ContractPayFundE.DELETED, 0));
        if (CollectionUtils.isEmpty(list)) {
            return preSettlePlanV;
        }
        if (req.getEdit() && CollectionUtils.isNotEmpty(req.getExistContractPayFundIdList())) {
            list.removeIf(l->req.getExistContractPayFundIdList().contains(l.getId()));
        }
        if (CollectionUtils.isEmpty(list)) {
            return preSettlePlanV;
        }
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.queryByContractId(req.getContractId());
        if (Objects.isNull(payConcludeE)) {
            return preSettlePlanV;
        }
        List<PreSettlePlanDataV> preSettlePlanDataVList = new ArrayList<>();
        for (ContractPayFundE contractPayFundE : list) {
            int groupCounter = 1;
            String settlePlanGroup = UUID.randomUUID().toString();
            PreSettlePlanDataV preSettlePlanDataV = generatePlansForContract(contractPayFundE,payConcludeE,req.getSplitMode(),groupCounter,settlePlanGroup);
            groupCounter++;
            preSettlePlanV.setPreSettlePlanDataVList(preSettlePlanDataVList);
            preSettlePlanDataVList.add(preSettlePlanDataV);
        }
        if (CollectionUtils.isNotEmpty(preSettlePlanDataVList)) {
            preSettlePlanV.setTotalPlannedCollectionAmount(
                    preSettlePlanDataVList.stream()
                            .map(PreSettlePlanDataV::getGroupPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }
        preSettlePlanV.setPreSettlePlanDataVList(preSettlePlanDataVList);
        preSettlePlanV.setSplitMode(req.getSplitMode());
        return preSettlePlanV;
    }

    /**
     * 预生成结算计划
     **/
    public List<PreSettlePlanDataV2> preSettlementPlanV2(String contractId) {
        log.info("preSettlementPlanV2 contractId:{}", contractId);
        // 获取合同所有清单项
        LambdaQueryWrapper<ContractPayFundE> payFundWrapper = new LambdaQueryWrapper<>();
        payFundWrapper.eq(ContractPayFundE::getContractId, contractId);
        payFundWrapper.eq(ContractPayFundE::getDeleted, 0);
        List<ContractPayFundE> payFundList = contractPayFundService.list(payFundWrapper);
        if (CollectionUtils.isEmpty(payFundList)) {
            log.info("preSettlementPlanV2 contractId:{} payFundList is empty", contractId);
            return Collections.emptyList();
        }
        // 获取合同信息 后续使用
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(contractId);
        // 清单项转id map
        Map<String, ContractPayFundE> payFundIdMap = payFundList.stream()
                .collect(Collectors.toMap(ContractPayFundE::getId, Function.identity()));
        // 获取合同所有结算计划(父级数据无意义)
        List<ContractPayPlanConcludeE> planEList = contractPayPlanConcludeMapper.getEffectivePlan(contractId,null);
        List<PayConcludePlanV2> planV2List = Global.mapperFacade.mapAsList(planEList, PayConcludePlanV2.class);
        markUsed(planV2List);
        // 根据合同清单id转map
        Map<String, List<PayConcludePlanV2>> planMap = planV2List.stream()
                .collect(Collectors.groupingBy(PayConcludePlanV2::getContractPayFundId));
        // 清单id组装上级pojo, 内部的计划数据会在后面进行填充
        List<PreSettlePlanDataV2> preList = Lists.newArrayList();

        //查询当前合同下是否含有补充协议
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<ContractPayConcludeE>()
                .eq(ContractPayConcludeE.PID, contractId)
                .eq(ContractPayConcludeE.CONTRACT_TYPE, 2)
                .eq(ContractPayConcludeE.REVIEW_STATUS, 2)
                .eq(ContractPayConcludeE.DELETED, 0);
        List<ContractPayConcludeE> supplyList = contractPayConcludeMapper.selectList(queryWrapper);

        Map<String, ContractPayFundE> existContractPayFundMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(supplyList)){
            //查询补充协议下的所有清单
            LambdaQueryWrapper<ContractPayFundE> paySupplyWrapper = new LambdaQueryWrapper<>();
            paySupplyWrapper.in(ContractPayFundE::getContractId, supplyList.stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()));
            paySupplyWrapper.eq(ContractPayFundE::getDeleted, 0);
            List<ContractPayFundE> supplyPayFundList = contractPayFundService.list(paySupplyWrapper);

            existContractPayFundMap = supplyPayFundList.stream()
                .collect(Collectors.toMap(
                        this::generateCompositeKey,
                        fund -> fund,
                        (fund1, fund2) -> fund1
                ));
        }
        for (ContractPayFundE payFundE : payFundList) {
            if(payFundE.getAmount().compareTo(BigDecimal.ZERO) == 0){
                throw new OwlBizException("清单金额不能为0");
            }
            PreSettlePlanDataV2 preSettlePlanDataV2 = new PreSettlePlanDataV2();
            preSettlePlanDataV2.setContractPayFundId(payFundE.getId());
            preSettlePlanDataV2.setChargeItem(payFundE.getChargeItem());
            preSettlePlanDataV2.setChargeItemId(payFundE.getChargeItemId().toString());
            preSettlePlanDataV2.setGroupPlannedCollectionAmount(payFundE.getAmount());
            preSettlePlanDataV2.setSplitMode(Integer.valueOf(payFundE.getPayWayId()));
            preSettlePlanDataV2.setSplitModeName(payFundE.getPayWay());
            //封装补充协议数据
            extractedSupplyData(existContractPayFundMap, payFundE, preSettlePlanDataV2);
            List<PayConcludePlanV2> curPlanV2List = planMap.get(payFundE.getId());
            if (CollectionUtils.isEmpty(curPlanV2List)) {
                continue;
            }
            curPlanV2List.sort(Comparator.comparing(PayConcludePlanV2::getTermDate));
            preSettlePlanDataV2.setPlanVList(curPlanV2List);
            preList.add(preSettlePlanDataV2);
        }
        // 处理 合同清单的增加/删除
        log.info("preSettlementPlanV2:{} payFundStep start", contractId);
        handlePlanOnFundDeleted(payFundIdMap,planMap);
        handlePlanOnFundAdded(payFundIdMap,planMap,preList,contractPayConcludeE,existContractPayFundMap);
        log.info("preSettlementPlanV2:{} payFundStep end", contractId);
        // 处理 合同的(开始/结束)时间修改
        log.info("preSettlementPlanV2:{} timeStep start", contractId);
        LambdaQueryWrapper<ContractPayPlanConcludeE> palnWrapper = new LambdaQueryWrapper<>();
        palnWrapper.eq(ContractPayPlanConcludeE::getContractId,contractPayConcludeE.getId());
        palnWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
        List<ContractPayPlanConcludeE> planList = contractPayPlanConcludeMapper.selectList(palnWrapper);
        handlePlanOnStartTime(preList, contractPayConcludeE,planList);
        handlePlanOnEndTime(preList, contractPayConcludeE,planList);
        log.info("preSettlementPlanV2:{} timeStep end", contractId);
        // 处理 合同清单的金额和结算计划的金额 使其相等， 20250417 17：07 下掉 by@YK1730084330JrsAA
        log.info("preSettlementPlanV2:{} amountStep start", contractId);
//        handlePlanOnAmount(preList, payFundIdMap);
        log.info("preSettlementPlanV2:{} amountStep end", contractId);
        return preList;
    }

    private String generateCompositeKey(ContractPayFundE fund) {
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
                (fund.getTaxRateAmount() != null ? fund.getTaxRateAmount().toPlainString() : "0")+ "|" +
                fund.getIsHy()+ "|" +
                nullToEmpty(fund.getBcContractId());
    }
    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private void handlePlanOnAmount(List<PreSettlePlanDataV2> preList,
                                    Map<String, ContractPayFundE> payFundIdMap) {
        if (CollectionUtils.isEmpty(preList) || MapUtils.isEmpty(payFundIdMap)){
            return;
        }
        for (PreSettlePlanDataV2 preSettlePlanDataV2 : preList) {
            List<PayConcludePlanV2> planV2List = preSettlePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常-金额拆分步骤异常");
            }
            ContractPayFundE payFundE = payFundIdMap.get(preSettlePlanDataV2.getContractPayFundId());
            // 四舍五入清单的总金额,保留2位小数，清单的金额有点问题
            BigDecimal groupPlannedCollectionAmount = payFundE.getAmount().setScale(2, RoundingMode.HALF_UP);
            BigDecimal groupUsedAmount = BigDecimal.ZERO;
            int groupUsedCount = 0;
            for (PayConcludePlanV2 planV2 : planV2List) {
                if (planV2.getUsed()){
                    groupUsedAmount = groupUsedAmount.add(planV2.getPlannedCollectionAmount());
                    groupUsedCount++;
                }
            }
            if (groupUsedAmount.compareTo(groupPlannedCollectionAmount) > 0){
                throw new OwlBizException("已使用的清单结算计划金额大于合同清单金额，请联系区域负责人");
            }
            // 若全部被使用，直接不处理
            if (groupUsedCount >= planV2List.size() || groupUsedAmount.compareTo(groupPlannedCollectionAmount) == 0){
                continue;
            }
            // 重新分布金额,因为条数或金额都可能发生了变动，此处金额比例需要重新结算
            BigDecimal baseRatio = BigDecimal.ONE;
            BigDecimal amount = groupPlannedCollectionAmount.subtract(groupUsedAmount);
            int totalNoUsedCount = planV2List.size() - groupUsedCount;
            BigDecimal amountPer = amount.divide(new BigDecimal(totalNoUsedCount), 2, RoundingMode.HALF_UP);
            int curCount = 0;
            for (PayConcludePlanV2 planV2 : planV2List) {
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
                String taxRateStr = payFundE.getTaxRate();
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
                PayConcludePlanV2 planV2 = planV2List.get(planV2List.size() - 1);
                planV2.setRatioAmount(planV2.getRatioAmount()
                        .add(baseRatio.multiply(BigDecimal.valueOf(100))));
            }
        }
    }

    private void handlePlanOnEndTime(List<PreSettlePlanDataV2> preList,
                                     ContractPayConcludeE contractPayConcludeE,
                                     List<ContractPayPlanConcludeE> planList) {
        LocalDate contractEnd = contractPayConcludeE.getGmtExpireEnd();
        for (PreSettlePlanDataV2 preSettlePlanDataV2 : preList) {
            if(Objects.nonNull(preSettlePlanDataV2.getSupplyEndTime())){
                contractEnd = preSettlePlanDataV2.getSupplyEndTime();
            }
            List<ContractPayPlanConcludeE> plannewList = planList.stream()
                    .filter(plan -> plan.getChargeItemId() !=null && preSettlePlanDataV2.getChargeItemId().equals(plan.getChargeItemId().toString()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(plannewList)){
                continue;
            }
            List<PayConcludePlanV2> planV2List = preSettlePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常");
            }
            // 拿到最大的结束时间
            LocalDate curGroupEnd = planV2List.stream()
                    .map(PayConcludePlanV2::getCostEndTime)
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
            planV2List.sort(Comparator.comparing(PayConcludePlanV2::getTermDate));
            // case2: 组内结束>合同结束，说明合同的结束时间前移, 锁定合同的结束时间落在哪个区间内
            if (curGroupEnd.isAfter(contractEnd)){
                log.info("case2: 组内结束>合同结束，说明合同的结束时间前移");
                int idx = -1;
                for (int i = planV2List.size()-1; i >= 0; i--) {
                    PayConcludePlanV2 planV2 = planV2List.get(i);
                    if (!planV2.getCostStartTime().isAfter(contractEnd) && !planV2.getCostEndTime().isBefore(contractEnd)){
                        idx = i;
                        // 本期结束时间=min(合同结束时间，本期结束时间)
                        LocalDate curEndTime = contractEnd.isBefore(planV2.getCostEndTime()) ? contractEnd : planV2.getCostEndTime();
                        planV2.setCostEndTime(curEndTime);
                        planV2.setPlannedCollectionTime(curEndTime.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
                        break;
                    }
                }
                /*for (int i = planV2List.size()-1; i > idx; i--) {
                    PayConcludePlanV2 planV2 = planV2List.get(i);
                    if (planV2.getUsed()){
                        throw new OwlBizException("存在时间大于合同结束时间的结算计划被使用，请联系区域负责人");
                    }
                }*/
                // 仅保留开始到idx到结束的计划
                planV2List = planV2List.subList(0, idx+1);
                preSettlePlanDataV2.setPlanVList(planV2List);
            }
            // case3: 组内结束<合同结束, 说明合同的结束时间后移 20240417 16:13 不要了 by@qd_shenbaocun1
            if (curGroupEnd.isBefore(contractEnd)){
                return;
//                // 若组内已使用金额大于等于清单金额，则不处理
//                BigDecimal totalUsedAmount = planV2List.stream()
//                        .filter(PayConcludePlanV2::getUsed)
//                        .map(PayConcludePlanV2::getPlannedCollectionAmount)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                if (totalUsedAmount.compareTo(payFundE.getAmount()) >= 0){
//                    log.info("组内金额已全部使用，不进行时间额外处理");
//                    return;
//                }
//                // 取第最后一期
//                PayConcludePlanV2 lastPlanV2 = planV2List.get(planV2List.size()-1);
//                List<PayConcludePlanV2> addedPlanV2List = null;
//                int maxTermDate = planV2List.stream()
//                        .mapToInt(PayConcludePlanV2::getTermDate)
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
//                            contractPayConcludeE,
//                            maxTermDate+1);
//                } else {
//                    // 若最后一期没有被使用，按照最后一期的开始时间到新的结束时间新增拆分内容, 此处先不管金额, 后续会重新进行分布，期数递增(占据最后一期)
//                    addedPlanV2List = doSplitV2(lastPlanV2.getCostStartTime(),
//                            contractEnd,
//                            payFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            payFundE.getTaxRate(),
//                            payFundE,
//                            contractPayConcludeE,
//                            maxTermDate);
//                    planV2List.remove(lastPlanV2);
//                }
//                planV2List.addAll(addedPlanV2List);
//                // 按照期数重新排序
//                planV2List.sort(Comparator.comparing(PayConcludePlanV2::getTermDate));
            }

        }
    }


    private void handlePlanOnStartTime(List<PreSettlePlanDataV2> preList,
                                       ContractPayConcludeE contractPayConcludeE,
                                       List<ContractPayPlanConcludeE> planList) {
        LocalDate contractStart = contractPayConcludeE.getGmtExpireStart();
        for (PreSettlePlanDataV2 preSettlePlanDataV2 : preList) {

            List<ContractPayPlanConcludeE> plannewList = planList.stream()
                    .filter(plan -> plan.getChargeItemId() !=null && preSettlePlanDataV2.getChargeItemId().equals(plan.getChargeItemId().toString()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(plannewList)){
                continue;
            }

            if(Objects.nonNull(preSettlePlanDataV2.getSupplyStartTime())){
                contractStart = preSettlePlanDataV2.getSupplyStartTime();
            }
            List<PayConcludePlanV2> planV2List = preSettlePlanDataV2.getPlanVList();
            if (CollectionUtils.isEmpty(planV2List)){
                throw new OwlBizException("流程数据处理异常");
            }
            // 拿到最早的开始时间
            LocalDate curGroupStart = planV2List.stream()
                    .map(PayConcludePlanV2::getCostStartTime)
                    .min(Comparator.comparing(LocalDate::toEpochDay))
                    .orElse(null);
            if (Objects.isNull(curGroupStart)){
                throw new OwlBizException("流程数据处理异常");
            }
            // case1: 组内开始=合同开始 不做处理
            if (curGroupStart.equals(contractStart)){
                log.info("合同开始时间与结算计划开始时间一致，无需处理");
                continue;
            }
            planV2List.sort(Comparator.comparing(PayConcludePlanV2::getTermDate));
            // case2: 组内开始<合同开始，说明合同的开始时间后移, 锁定合同的开始时间落在哪个区间内
            if (curGroupStart.isBefore(contractStart)){
                log.info("合同开始时间与结算计划开始时间不一致，合同开始时间后移");
                int idx = -1;
                for (int i = 0; i < planV2List.size(); i++) {
                    PayConcludePlanV2 planV2 = planV2List.get(i);
                    if (!planV2.getCostStartTime().isAfter(contractStart) && !planV2.getCostEndTime().isBefore(contractStart)){
                        idx = i;
                        // 开始时间=max(合同新的开始时间, 本期的开始时间)
                        planV2.setCostStartTime(contractStart.isAfter(planV2.getCostStartTime())? contractStart : planV2.getCostStartTime());
                        break;
                    }
                }
                /*for (int i = 0; i < idx; i++) {
                    PayConcludePlanV2 planV2 = planV2List.get(i);
                    if (planV2.getUsed()){
                        throw new OwlBizException("存在时间小于合同开始时间的结算计划被使用，请联系区域负责人");
                    }
                }*/
                if(idx == -1){
                    return;
                }
                // 仅保留idx到结束的计划
                planV2List = planV2List.subList(idx, planV2List.size());
                // 期数从1开始重新计数
                for (int i = 0; i < planV2List.size(); i++) {
                    PayConcludePlanV2 planV2 = planV2List.get(i);
                    planV2.setTermDate(i+1);
                }
                preSettlePlanDataV2.setPlanVList(planV2List);
            }
            // case3: 组内开始>合同开始, 说明合同的开始时间前移 20240417 16:13 不要了 by@qd_shenbaocun1
            if (curGroupStart.isAfter(contractStart)){
                return;
//                log.info("合同开始时间与结算计划开始时间不一致，合同开始时间前移");
//                // 若组内已使用金额大于等于清单金额，则不处理
//                BigDecimal totalUsedAmount = planV2List.stream()
//                        .filter(PayConcludePlanV2::getUsed)
//                        .map(PayConcludePlanV2::getPlannedCollectionAmount)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                if (totalUsedAmount.compareTo(payFundE.getAmount()) >= 0){
//                    log.info("组内金额已全部使用，不进行时间额外处理");
//                    return;
//                }
//                // 取第一期
//                PayConcludePlanV2 firstPlanV2 = planV2List.get(0);
//                List<PayConcludePlanV2> addedPlanV2List = null;
//                if (firstPlanV2.getUsed()){
//                    // 若第一期被使用, 按照合同的开始时间到原来开始时间的前一天新增拆分内容, 此处先不管金额, 后续会重新进行分布
//                    addedPlanV2List = doSplitV2(contractStart,
//                            curGroupStart.plusDays(-1),
//                            payFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            payFundE.getTaxRate(),
//                            payFundE,
//                            contractPayConcludeE,
//                            1);
//                } else {
//                    // 第一期没有使用,按照合同的开始时间到原来第一期的结束时间拆分新内容, 此处先不管金额，后续会重新进行分布
//                    addedPlanV2List = doSplitV2(contractStart,
//                            firstPlanV2.getCostEndTime(),
//                            payFundE.getPayWayId(),
//                            BigDecimal.ONE,
//                            payFundE.getTaxRate(),
//                            payFundE,
//                            contractPayConcludeE,
//                            1);
//                    planV2List.remove(firstPlanV2);
//                }
//                // 获取新增的最大的期数，原期数在此基础上递增
//                int maxTermDate = addedPlanV2List.stream()
//                        .mapToInt(PayConcludePlanV2::getTermDate)
//                        .max()
//                        .orElse(0);
//                for (int i = 0; i < planV2List.size(); i++) {
//                    PayConcludePlanV2 planV2 = planV2List.get(i);
//                    planV2.setTermDate(maxTermDate+i+1);
//                }
//                planV2List.addAll(addedPlanV2List);
//                // 按照期数重新排序
//                planV2List.sort(Comparator.comparing(PayConcludePlanV2::getTermDate));
            }

        }
    }

    /**
     * 若计划不存在，清单存在，代表清单新增，需要新增计划
     **/
    private void handlePlanOnFundAdded(Map<String, ContractPayFundE> payFundIdMap,
                                       Map<String, List<PayConcludePlanV2>> planMap,
                                       List<PreSettlePlanDataV2> preList,
                                       ContractPayConcludeE contractPayConcludeE,
                                       Map<String, ContractPayFundE> existContractPayFundMap) {
        if (MapUtils.isEmpty(payFundIdMap)){
            log.info("合同清单项为空,无法处理清单项新增");
            return;
        }
        for (Map.Entry<String, ContractPayFundE> entry : payFundIdMap.entrySet()) {
            if (planMap.containsKey(entry.getKey())){
                continue;
            }
            log.info("合同清单项新增,新增计划,清单项id={}", entry.getKey());
            ContractPayFundE payFundE = entry.getValue();
            List<PayConcludePlanV2> curV2List = splitByFundNew(payFundE,this.getContractData(existContractPayFundMap, payFundE, contractPayConcludeE));
            PreSettlePlanDataV2 curDataV2 = new PreSettlePlanDataV2();
            curDataV2.setContractPayFundId(payFundE.getId());
            curDataV2.setChargeItem(payFundE.getChargeItem());
            curDataV2.setChargeItemId(payFundE.getChargeItemId().toString());
            curDataV2.setGroupPlannedCollectionAmount(payFundE.getAmount());
            curDataV2.setSplitMode(Integer.valueOf(payFundE.getPayWayId()));
            curDataV2.setSplitModeName(payFundE.getPayWay());
            curDataV2.setPlanVList(curV2List);
            extractedSupplyData(existContractPayFundMap, payFundE, curDataV2);
            preList.add(curDataV2);
        }
    }

    private ContractPayConcludeE getContractData(Map<String, ContractPayFundE> existContractPayFundMap, ContractPayFundE payFundE, ContractPayConcludeE contractPayConcludeE) {
        ContractPayConcludeE contractPay = new ContractPayConcludeE();
        BeanUtils.copyProperties(contractPayConcludeE, contractPay);
        String key = generateCompositeKey(payFundE);
        ContractPayFundE existFund = existContractPayFundMap.get(key);
        if(Objects.nonNull(existFund)){
            //查询补充合同基本信息
            ContractPayConcludeE payConclude = contractPayConcludeService.getById(existFund.getContractId());
            contractPay.setGmtExpireStart(payConclude.getGmtExpireStart());
            contractPay.setGmtExpireEnd(payConclude.getGmtExpireEnd());
        }
        return contractPay;
    }

    //封装补充协议数据
    private void extractedSupplyData(Map<String, ContractPayFundE> existContractPayFundMap, ContractPayFundE payFundE, PreSettlePlanDataV2 curDataV2) {
        String key = generateCompositeKey(payFundE);
        ContractPayFundE existFund = existContractPayFundMap.get(key);
        if(Objects.nonNull(existFund)){
            //查询补充合同基本信息
            ContractPayConcludeE payConclude = contractPayConcludeService.getById(existFund.getContractId());
            curDataV2.setSupplyContractId(existFund.getContractId());
            curDataV2.setSupplyStartTime(payConclude.getGmtExpireStart());
            curDataV2.setSupplyEndTime(payConclude.getGmtExpireEnd());
        }
    }

    private List<PayConcludePlanV2> splitByFundNew(ContractPayFundE payFundE,
                                                   ContractPayConcludeE contractPayConcludeE) {
        // 按照指定的开始时间、结束时间, 金额，税率，进行拆分, 同时复制部分信息
        return doSplitV2(contractPayConcludeE.getGmtExpireStart(),
                contractPayConcludeE.getGmtExpireEnd(),
                payFundE.getPayWayId(),
                payFundE.getAmount(),
                payFundE.getTaxRate(),
                payFundE,
                contractPayConcludeE,
                1);
    }

    /**
     * 预生成核心逻辑
     * @param start 开始时间
     * @param end 结束时间
     * @param payWayId 付款方式-清单
     * @param amount 金额
     * @param taxRateStr 税率字符串
     * @param payFundE 清单-用于做信息冗余
     * @param contractPayConcludeE 合同信息-用于做信息冗余
     **/
    private List<PayConcludePlanV2> doSplitV2(LocalDate start,
                                              LocalDate end,
                                              String payWayId,
                                              BigDecimal amount,
                                              String taxRateStr,
                                              ContractPayFundE payFundE,
                                              ContractPayConcludeE contractPayConcludeE,
                                              Integer startPeriod) {
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
                    payFundE,
                    contractPayConcludeE));
        }
        CommonRangeAmountBO rangrAmount = commonRangeAmountService.getRangeAmount(splitModeEnum.getCode(),start,end,amount);
        List<PayConcludePlanV2> results = new ArrayList<>();
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
                    payFundE,
                    contractPayConcludeE));

            //remainingAmount = remainingAmount.subtract(periodAmount);
            currentStart = currentEnd.plusDays(1);
        }
        return results;
    }

    /**
     * V2信息拼接
     **/
    private PayConcludePlanV2 buildSplitPlanV2(LocalDate start,
                                               LocalDate end,
                                               BigDecimal amount,
                                               boolean isDifferenceTax,
                                               BigDecimal taxRate,
                                               Integer curPeriod,
                                               BigDecimal ratio,
                                               ContractPayFundE payFundE,
                                               ContractPayConcludeE contractPayConcludeE) {
        BigDecimal taxAmount = null;
        BigDecimal noTaxAmount = null;

        if (!isDifferenceTax) {
            noTaxAmount = amount.divide(BigDecimal.ONE.add(taxRate), 2, RoundingMode.HALF_UP);
            taxAmount = amount.subtract(noTaxAmount);
        }
        PayConcludePlanV2 planV2 = new PayConcludePlanV2();
        planV2.setContractPayFundId(contractPayConcludeE.getId());
        planV2.setContractId(contractPayConcludeE.getId());
        // todo payNoteCode 好像没啥用 先不管试试
        planV2.setPayNotecode(null);
        planV2.setMerchant(contractPayConcludeE.getOppositeOneId());
        planV2.setMerchantName(contractPayConcludeE.getOppositeOne());
        planV2.setContractNo(contractPayConcludeE.getContractNo());
        planV2.setContractName(contractPayConcludeE.getName());
        planV2.setTermDate(curPeriod);

        planV2.setPlannedCollectionAmount(amount);
        planV2.setTaxAmount(taxAmount);
        planV2.setNoTaxAmount(noTaxAmount);

        planV2.setCostStartTime(start);
        planV2.setCostEndTime(end);
        LocalDate planCollectionTime = end.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        planV2.setPlannedCollectionTime(planCollectionTime);

        String costEstimationCode = generateCostEstimationCode(COST_ESTIMATION_CODE_KEY,
                curPeriod,
                planCollectionTime.getYear() + "-" + planCollectionTime.getMonthValue(),
                contractPayConcludeE.getConmaincode());
        planV2.setCostEstimationCode(costEstimationCode);

        planV2.setSettlePlanGroup(payFundE.getId());
        planV2.setContractPayFundId(payFundE.getId());
        planV2.setReviewStatus(2);
        planV2.setSplitMode(SplitModeEnum.parseByPayWayId(payFundE.getPayWayId()).getCode());

        planV2.setRatioAmount(ratio.multiply(BigDecimal.valueOf(100)));
        planV2.setServiceType(Integer.valueOf(payFundE.getTypeId()));
        planV2.setChargeItemId(payFundE.getChargeItemId());
        planV2.setChargeItem(payFundE.getChargeItem());
        planV2.setTaxRateId(Long.valueOf(payFundE.getTaxRateId()));
        planV2.setTaxRate(StringUtils.isNotBlank(payFundE.getTaxRate()) ? payFundE.getTaxRate().split("%")[0] : null);
        // used走到这里肯定是false
        planV2.setUsed(false);
        return planV2;
    }

    /**
     * 若计划存在，清单不存在，代表清单被删除了，需要删除计划
     **/
    private void handlePlanOnFundDeleted(Map<String, ContractPayFundE> payFundIdMap,
                                         Map<String, List<PayConcludePlanV2>> planMap) {
        if (MapUtils.isEmpty(planMap)){
            log.info("当前无结算计划,走结算计划新生成步骤");
            return;
        }
        List<String> toRemovePlanIdList = new ArrayList<>();
        for (Map.Entry<String, List<PayConcludePlanV2>> entry : planMap.entrySet()) {
            if (payFundIdMap.containsKey(entry.getKey())){
                continue;
            }
            for (PayConcludePlanV2 planV2 : entry.getValue()) {
                if (planV2.getUsed()){
                    throw new OwlBizException("已使用合同清单项不存在，请联系区域负责人");
                }
            }
            log.info("当前结算计划的合同清单被删除，删除结算计划：{}", entry.getKey());
            toRemovePlanIdList.add(entry.getKey());
        }
        if (CollectionUtils.isNotEmpty(toRemovePlanIdList)){
            for (String planId : toRemovePlanIdList) {
                planMap.remove(planId);
            }
        }
    }

    private void markUsed(List<PayConcludePlanV2> planVList) {
        if (CollectionUtils.isEmpty(planVList)){
            return;
        }
        List<String> planIdList = planVList.stream().map(PayConcludePlanV2::getId).collect(Collectors.toList());
        // 被结算单使用的结算计划id
        List<String> usedPlanIdListOnSettlement = contractPayPlanConcludeMapper.getUsedPlanIdListOnSettlement(planIdList);
        // 被结算单使用的结算计划id
        List<String> usedNkPlanIdListOnSettlement = contractPayPlanConcludeMapper.getNkUsedPlanIdListOnSettlement(planIdList);
        // 生成成本计划的结算计划id
        List<String> usedPlanIdListOnPayCostPlan = contractPayPlanConcludeMapper.getUsedPlanIdListOnPayCostPlan(planIdList);
        for (PayConcludePlanV2 v2 : planVList) {
            if (usedPlanIdListOnPayCostPlan.contains(v2.getId()) || usedPlanIdListOnSettlement.contains(v2.getId()) || usedNkPlanIdListOnSettlement.contains(v2.getId())){
                v2.setUsed(true);
            }
        }
    }


    private PreSettlePlanDataV generatePlansForContract(ContractPayFundE contractPayFundE, ContractPayConcludeE concludeE, Integer splitMode, int groupCounter,String settlePlanGroup ) {
        PreSettlePlanDataV preSettlePlanDataV = new PreSettlePlanDataV();

        preSettlePlanDataV.setChargeItem(contractPayFundE.getChargeItem());
        preSettlePlanDataV.setChargeItemId(String.valueOf(contractPayFundE.getChargeItemId()));
        preSettlePlanDataV.setContractPayFundId(contractPayFundE.getId());

        List<PreSettlePlanGroupV> plans = new ArrayList<>();

        LocalDate contractStartDate = concludeE.getGmtExpireStart();
        LocalDate contractEndDate = concludeE.getGmtExpireEnd();


        if (null == splitMode) {
            return preSettlePlanDataV;
        }
        if (5 == splitMode) {
            PreSettlePlanGroupV planV = new PreSettlePlanGroupV();


            planV.setCostStartTime(contractStartDate);
            planV.setCostEndTime(contractEndDate);

            LocalDate plannedCollectionTime = contractEndDate.plusMonths(1);
            planV.setPlannedCollectionTime(plannedCollectionTime);

            BigDecimal plannedAmount = contractPayFundE.getAmount();
            planV.setPlannedCollectionAmount(plannedAmount);

            planV.setRatioAmount(BigDecimal.valueOf(100));

            //planV.setTaxRate(contractPayFundE.getTaxRate());
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
                                .map(PreSettlePlanGroupV::getPlannedCollectionAmount)
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
            PreSettlePlanGroupV planV = new PreSettlePlanGroupV();

            LocalDate startDate = periodStartDates.get(i);
            LocalDate endDate = periodEndDates.get(i);

            planV.setCostStartTime(startDate);
            planV.setCostEndTime(endDate);

            LocalDate plannedCollectionTime = endDate.plusMonths(1);
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

            //planV.setTaxRate(contractPayFundE.getTaxRate());
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
                            .map(PreSettlePlanGroupV::getPlannedCollectionAmount)
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

    public String generateCostEstimationCode(String redisKey,int periodNumber, String plannedCollectionDate, String ctCode) {
        // 调用自增方法获取序列号
        String serialNumber = calculateCostEstimationCode(redisKey);
        // 生成成本预估编码
        return String.format("JSJH-%d-%s-%s-%s", periodNumber, plannedCollectionDate, ctCode, serialNumber);
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


    public PreSettlePlanV details(SettlePlanDetailQuery req) {
        PreSettlePlanV preSettlePlanV = new PreSettlePlanV();
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(req.getContractId());
        if (Objects.isNull(contractPayConcludeE)) {
            throw new IllegalArgumentException("合同不存在");
        }
        //step1 先获取历史结算计划
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery()
                .eq(ContractPayPlanConcludeE::getContractId,req.getContractId())
                .list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            throw new OwlBizException("合同计划不存在");
        }
        List<ContractPayPlanConcludeE> parentContractPayPlanList = contractPayPlanConcludeEList.stream().filter(c->"0".equals(c.getPid())).collect(Collectors.toList());
        List<ContractPayPlanConcludeE> planList = contractPayPlanConcludeEList.stream().filter(c->!"0".equals(c.getPid())).collect(Collectors.toList());
        //step2 获取最新的合同清单，如果有新增合同清单则需要预生成计划组数据
        PreSettlePlanQuery pre = new PreSettlePlanQuery();
        pre.setContractId(req.getContractId());
        pre.setSplitMode(parentContractPayPlanList.get(0).getSplitMode());
        pre.setExistContractPayFundIdList(planList.stream().map(ContractPayPlanConcludeE::getContractPayFundId).collect(Collectors.toList()));
        if (1 == req.getSource()) {
            pre.setEdit(true);
            pre.setSplitMode(planList.get(0).getSplitMode());
            preSettlePlanV = preSettlementPlan(pre);
        }
        preSettlePlanV.setSplitMode(planList.get(0).getSplitMode());
        Map<String,List<ContractPayPlanConcludeE>> contractPayPlanMap = planList.stream().collect(Collectors.groupingBy(ContractPayPlanConcludeE::getSettlePlanGroup));
        rebuildPreSettlePlanV(preSettlePlanV,contractPayPlanMap,planList);
        List<PreSettlePlanDataV> preSettlePlanDataVList = preSettlePlanV.getPreSettlePlanDataVList();
        if (CollectionUtils.isNotEmpty(preSettlePlanDataVList)) {
            preSettlePlanV.setTotalPlannedCollectionAmount(
                    preSettlePlanDataVList.stream()
                            .map(PreSettlePlanDataV::getGroupPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        };
        orderTerms(preSettlePlanV);
        checkSettleStatus(preSettlePlanV);
        return preSettlePlanV;
    }

    private void orderTerms(PreSettlePlanV preSettlePlanV) {
        if (CollectionUtils.isEmpty(preSettlePlanV.getPreSettlePlanDataVList())) {
            return;
        }
        preSettlePlanV.getPreSettlePlanDataVList().forEach(vlist -> {
            if (CollectionUtils.isNotEmpty(vlist.getPreSettlePlanGroupVList())) {
                vlist.getPreSettlePlanGroupVList().sort(
                        Comparator.comparing(
                                PreSettlePlanGroupV::getTermDate,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                );
            }
        });
    }

    private void rebuildPreSettlePlanV(PreSettlePlanV preSettlePlanV,
                                       Map<String, List<ContractPayPlanConcludeE>> contractPayPlanMap,
                                       List<ContractPayPlanConcludeE> planList) {
        List<PreSettlePlanDataV> preSettlePlanDataVList = preSettlePlanV.getPreSettlePlanDataVList();
        if (CollectionUtils.isEmpty(preSettlePlanDataVList)) {
            preSettlePlanDataVList = new ArrayList<>();
        }
        List<PreSettlePlanDataV> finalPreSettlePlanDataVList = preSettlePlanDataVList;

        Map<String,List<String>> planIdToCostPlanIdMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(planList)){
            List<String> planIdList = planList.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(planIdList)){
                QueryWrapper<PayCostPlanE> queryModel = new QueryWrapper<>();
                queryModel.in("planId", planIdList);
                queryModel.eq("deleted",0);
                List<PayCostPlanE> payCostPlanEList = payCostPlanService.list(queryModel);
                planIdToCostPlanIdMap = payCostPlanEList.stream().collect(Collectors.groupingBy(PayCostPlanE::getPlanId,Collectors.mapping(PayCostPlanE::getId,Collectors.toList())));
            }
        }


        Map<String, List<String>> finalPlanIdToCostPlanIdMap = planIdToCostPlanIdMap;
        contractPayPlanMap.forEach((settlePlaFnGroup, list)->{
            PreSettlePlanDataV preSettlePlanDataV = new PreSettlePlanDataV();
            preSettlePlanDataV.setChargeItem(list.get(0).getChargeItem());
            preSettlePlanDataV.setChargeItemId(String.valueOf(list.get(0).getChargeItemId()));
            preSettlePlanDataV.setContractPayFundId(list.get(0).getContractPayFundId());
            List<PreSettlePlanGroupV> preSettlePlanGroupVList = Global.mapperFacade.mapAsList(list, PreSettlePlanGroupV.class);;
            if (CollectionUtils.isNotEmpty(preSettlePlanGroupVList)){
                preSettlePlanGroupVList.forEach(v-> v.setPayCostPlanIdList(finalPlanIdToCostPlanIdMap.getOrDefault(v.getId(),null)));
            }
            preSettlePlanDataV.setPreSettlePlanGroupVList(preSettlePlanGroupVList);
            preSettlePlanDataV.setGroupPlannedCollectionAmount(
                    preSettlePlanGroupVList.stream()
                            .map(PreSettlePlanGroupV::getPlannedCollectionAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            finalPreSettlePlanDataVList.add(preSettlePlanDataV);
        });
        preSettlePlanV.setPreSettlePlanDataVList(finalPreSettlePlanDataVList);
    }


    public void validateSettlementPlans(SettlementPlanAddF settlementPlanAddF,ContractPayConcludeE contract) {
        List<List<ContractPayPlanAddF>> userPlans = settlementPlanAddF.getContractPayPlanAddFLists();

        LocalDate contractStartDate = contract.getGmtExpireStart();
        LocalDate contractEndDate = contract.getGmtExpireEnd();

        if (userPlans == null || userPlans.isEmpty()) {
            throw new OwlBizException("提交的结算计划不能为空");
        }

        userPlans.forEach(value->{
            for (int i = 0; i < value.size(); i++) {
                ContractPayPlanAddF currentPlan = value.get(i);

                ContractPayPlanAddF firstPlan = value.get(0);
                if (!firstPlan.getCostStartTime().equals(contractStartDate)) {
                    throw new OwlBizException(String.format("第一期的费用开始日期应为合同开始日期：%s，提交值：%s",
                            contractStartDate, firstPlan.getCostStartTime()));
                }

                ContractPayPlanAddF lastPlan = value.get(value.size() - 1);
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
                    ContractPayPlanAddF previousPlan = value.get(i - 1);
                    LocalDate expectedStartDate = previousPlan.getCostEndTime().plusDays(1);
                    if (!currentPlan.getCostStartTime().equals(expectedStartDate)) {
                        throw new OwlBizException(String.format("第 %d 期的费用开始日期应为上一期的结束日期加一天：%s，提交值：%s,费项:%s",
                                i + 1, expectedStartDate, currentPlan.getCostStartTime(),currentPlan.getChargeItem()));
                    }
                }
            }
        });

    }

    public void refresh() {
        log.info("开始执行刷数");
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery()
                .eq(ContractPayPlanConcludeE::getDeleted,0)
                .isNull(ContractPayPlanConcludeE::getCostEstimationCode)
                .list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            return;
        }
        List<ContractPayFundE> contractPayContractIdList = contractPayFundService.lambdaQuery().in(ContractPayFundE::getContractId,contractPayPlanConcludeEList.stream().map(ContractPayPlanConcludeE::getContractId).collect(Collectors.toList())).list();
        Map<String,ContractPayFundE> payFundMap = contractPayContractIdList.stream().collect(Collectors.toMap(ContractPayFundE::getContractId,v->v,(v1,v2)->v1));
        Map<String,List<ContractPayPlanConcludeE>> conMap = contractPayPlanConcludeEList.stream().filter(c->!"0".equals(c.getPid())).collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractId));
        conMap.forEach((key,value)->{
            List<String> contractIdList = value.stream().map(ContractPayPlanConcludeE::getContractId).collect(Collectors.toList());
            List<ContractPayConcludeE> contractPayList = contractPayConcludeService.lambdaQuery().in(ContractPayConcludeE::getId,contractIdList).list();
            Map<String,ContractPayConcludeE> contractMap = contractPayList.stream().collect(Collectors.toMap(ContractPayConcludeE::getId,v->v,(v1,v2)->v1));
            String settlePlanGroup = UUID.randomUUID().toString();
            value.sort(
                    Comparator.comparing(
                            ContractPayPlanConcludeE::getTermDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
            );
            LocalDate previousCostEndTime = null;
            for (int i = 0; i < value.size(); i++) {
                ContractPayPlanConcludeE con = value.get(i);
                con.setSettlePlanGroup(settlePlanGroup);
                String conmaincode = con.getContractId();
                ContractPayConcludeE concludeE = contractMap.get(con.getContractId());
                if (null != concludeE) {
                    LocalDate gmtExpireStart = concludeE.getGmtExpireStart();
                    if (StringUtils.isNotBlank(contractMap.get(con.getContractId()).getConmaincode())) {
                        conmaincode = contractMap.get(con.getContractId()).getConmaincode();
                    }

                    LocalDate plannedCollectionTime = con.getPlannedCollectionTime();
                    if (i == 0) {
                        LocalDate costStartTime = gmtExpireStart;
                        LocalDate costEndTime = plannedCollectionTime.minusMonths(1);

                        con.setCostStartTime(costStartTime);
                        con.setCostEndTime(costEndTime);

                        previousCostEndTime = costEndTime;
                    } else {
                        LocalDate costStartTime = previousCostEndTime.plusDays(1);
                        LocalDate costEndTime = plannedCollectionTime.minusDays(1);

                        con.setCostStartTime(costStartTime);
                        con.setCostEndTime(costEndTime);

                        previousCostEndTime = costEndTime;
                    }
                    con.setCostEstimationCode(StringUtils.isBlank(con.getCostEstimationCode())
                            ? generateCostEstimationCode(COST_ESTIMATION_CODE_KEY, con.getTermDate(),
                            con.getPlannedCollectionTime().getYear() + "-" + con.getPlannedCollectionTime().getMonthValue(), conmaincode)
                            : con.getCostEstimationCode());
                    if (null != payFundMap.get(con.getContractId())) {
                        con.setContractPayFundId(payFundMap.get(con.getContractId()).getId());
                    }
                }
            }
        });

        List<List<ContractPayPlanConcludeE>> lists = Lists.partition(contractPayPlanConcludeEList,500);
        for (int i = 0; i < lists.size(); i++) {
            log.info("------------------------------------refresh 开始执行第:{}批------------------------------------",i);
            this.saveOrUpdateBatch(lists.get(i));
            log.info("------------------------------------refresh 执行第:{}批,结束------------------------------------",i);
        }
        log.info("执行刷数结束");
    }


    public void refresh2(List<String> contractIds) {
        log.info("开始执行刷数2");
        List<ContractPayPlanConcludeE> contractPayPlanConcludeEList = this.lambdaQuery()
                .eq(ContractPayPlanConcludeE::getDeleted,0)
                .in(ContractPayPlanConcludeE::getContractId,contractIds)
                .list();
        if (CollectionUtils.isEmpty(contractPayPlanConcludeEList)) {
            return;
        }
        Map<String,List<ContractPayPlanConcludeE>> conMap = contractPayPlanConcludeEList.stream().filter(c->!"0".equals(c.getPid())).collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractId));
        conMap.forEach((key,value)->{
            List<String> contractIdList = value.stream().map(ContractPayPlanConcludeE::getContractId).collect(Collectors.toList());
            List<ContractPayConcludeE> contractPayList = contractPayConcludeService.lambdaQuery().in(ContractPayConcludeE::getId,contractIdList).list();
            Map<String,ContractPayConcludeE> contractMap = contractPayList.stream().collect(Collectors.toMap(ContractPayConcludeE::getId,v->v,(v1,v2)->v1));
            value.sort(
                    Comparator.comparing(
                            ContractPayPlanConcludeE::getTermDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
            );
            LocalDate curTime = null;
            for (int i = 0; i < value.size(); i++) {
                ContractPayPlanConcludeE con = value.get(i);
                ContractPayConcludeE concludeE = contractMap.get(con.getContractId());
                if (Objects.isNull(concludeE)){
                    throw new RuntimeException("contractId:"+con.getContractId()+"不存在");
                }
                LocalDate plannedCollectionTime = con.getPlannedCollectionTime();
                if (i == 0) {
                    LocalDate costStartTime = concludeE.getGmtExpireStart();
                    LocalDate costEndTime = getLastDayOfPreviousMonth(plannedCollectionTime);

                    con.setCostStartTime(costStartTime);
                    con.setCostEndTime(costEndTime);

                    curTime = plannedCollectionTime;

                    // 如果也是最后一期，那么结束时间用合同的结束时间
                    if (i == value.size() - 1) {
                        con.setCostEndTime(concludeE.getGmtExpireEnd());
                    }
                } else if (i == value.size() - 1) {
                    LocalDate costStartTime = getFirstDayOfMonth(curTime);
                    LocalDate costEndTime = concludeE.getGmtExpireEnd();
                    con.setCostStartTime(costStartTime);
                    con.setCostEndTime(costEndTime);
                    curTime = plannedCollectionTime;
                } else {
                    LocalDate costStartTime = getFirstDayOfMonth(curTime);
                    LocalDate costEndTime = getLastDayOfPreviousMonth(plannedCollectionTime);

                    con.setCostStartTime(costStartTime);
                    con.setCostEndTime(costEndTime);

                    curTime = plannedCollectionTime;
                }
            }
        });

        List<List<ContractPayPlanConcludeE>> lists = Lists.partition(contractPayPlanConcludeEList,500);
        for (int i = 0; i < lists.size(); i++) {
            log.info("------------------------------------refresh 开始执行第:{}批------------------------------------",i);
            this.saveOrUpdateBatch(lists.get(i));
            log.info("------------------------------------refresh 执行第:{}批,结束------------------------------------",i);
        }
        log.info("执行刷数结束");
    }

    private LocalDate getLastDayOfPreviousMonth(LocalDate date) {
        // 获取指定日期的前一个月
        LocalDate previousMonth = date.minusMonths(1);
        // 获取前一个月的最后一天
        return previousMonth.with(TemporalAdjusters.lastDayOfMonth());
    }

    public LocalDate getFirstDayOfMonth(LocalDate date) {
        // 获取指定日期所在月份的第一天
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }




    public PageV<SettlePlanListV> listPlan(PageF<SearchF<SettlementPlanListQuery>> request) {
        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
            }
        }

        Page<SettlementPlanListQuery> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            return PageV.of(request, 0, new ArrayList<>());
        }
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        SettlementParamQuery param = new SettlementParamQuery();
        Field isNKField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "isNK".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(isNKField)){
            param.setIsNK(Integer.parseInt(String.valueOf(isNKField.getValue())));
        }
        request.getConditions().getFields().removeIf(e -> e.getName().equals("isNK"));
        QueryWrapper<SettlementPlanListQuery> queryModel = request.getConditions().getQueryModel();
        Map<String, Object> specialMap = request.getConditions().getSpecialMap();
        param.setPlannedCollectionStartTime(String.valueOf(specialMap.get("plannedCollectionStartTime")));
        param.setPlannedCollectionEndTime(String.valueOf(specialMap.get("plannedCollectionEndTime")));
        param.setCommunityIdList(null != specialMap.get("communityIdList") ? (List<String>) specialMap.get("communityIdList") : null);
        param.setStatusList(Objects.isNull(specialMap.get("statusList")) ? null : (List<String>) specialMap.get("statusList"));
        if (StringUtils.isBlank(param.getPlannedCollectionStartTime()) || StringUtils.isBlank(param.getPlannedCollectionEndTime())) {
            throw new OwlBizException("应结算日期不能为空");
        }
        param.setTenantId(getIdentityInfo().get().getTenantId());
        IPage<SettlePlanListV> page = contractPayPlanConcludeMapper.listPlan(pageF, planConditionPage(queryModel, getIdentityInfo().get().getTenantId(), param, orgPermissionV), param);
        if (null != page && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<String> planIdList = page.getRecords().stream().map(SettlePlanListV::getPlanId).collect(Collectors.toList());
            List<String> canNotEditList = querySettleStatus(planIdList);
            List<String> hasPayCostPlanList = queryCostPayPlan(planIdList);
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);
            Map<String,DictionaryCode> dicMap = value.stream().collect(Collectors.toMap(DictionaryCode::getCode,v->v,(v1,v2)->v1));
            page.getRecords().forEach(r-> {
                r.setContractBusinessLineName(ContractBusinessLineEnum.parseName(r.getContractBusinessLine()));
                r.setConmanagetypename(null != dicMap.get(r.getConmanagetype()) ? dicMap.get(r.getConmanagetype()).getName() : null);
                r.setYear(String.valueOf(LocalDate.parse(param.getPlannedCollectionStartTime()).getYear()));
                r.setContractStatusName(ContractRevStatusEnum.parseName(r.getStatus()));
                r.setSplitModeName(null != r.getSplitMode() ? SplitModeEnum.parseName(r.getSplitMode()) : null);
                boolean canEditOrDelete = buttonControl(hasPayCostPlanList,canNotEditList,r.getPlanId());
                r.setCanEditPlan(canEditOrDelete);
                r.setCanDeletePlan(canEditOrDelete);
            });
        }
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), SettlePlanListV.class));
    }

    private boolean buttonControl(List<String> hasPayCostPlanList, List<String> canNotEditList, String planId) {
        if (CollectionUtils.isNotEmpty(hasPayCostPlanList) && hasPayCostPlanList.contains(planId)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(canNotEditList) && canNotEditList.contains(planId)) {
            return false;
        }
        return true;
    }

    private List<String> queryCostPayPlan(List<String> planIdList) {
        List<PayCostPlanE> hasPayCostPlanList = this.payCostPlanService.lambdaQuery().in(PayCostPlanE::getPlanId,planIdList).list();
        if (CollectionUtils.isEmpty(hasPayCostPlanList)) {
            return Collections.emptyList();
        }
        return hasPayCostPlanList.stream().map(PayCostPlanE::getPlanId).collect(Collectors.toList());
    }

    private List<String> querySettleStatus(List<String> planIdList) {
        return settlementConcludeService.checkSettleStatus(planIdList);
    }

    private void checkSettleStatus(PreSettlePlanV preSettlePlanV) {
        if (CollectionUtils.isEmpty(preSettlePlanV.getPreSettlePlanDataVList())) {
            return;
        }
        List<PreSettlePlanDataV> preSettlePlanDataVList = preSettlePlanV.getPreSettlePlanDataVList();
        if (CollectionUtils.isEmpty(preSettlePlanDataVList.get(0).getPreSettlePlanGroupVList())) {
            preSettlePlanV.setCanEditSplitMode(false);
            return;
        }

        preSettlePlanDataVList.forEach(pre->{
            List<String> preSettlePlanGroupVList = pre.getPreSettlePlanGroupVList().stream().map(PreSettlePlanGroupV::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(preSettlePlanGroupVList)) {
                List<String> canNotEditList = settlementConcludeService.checkSettleStatus(preSettlePlanGroupVList);
                if (CollectionUtils.isNotEmpty(canNotEditList)) {
                    preSettlePlanV.setCanEditSplitMode(false);
                    pre.getPreSettlePlanGroupVList().forEach(r-> r.setCanEditTerm(!canNotEditList.contains(r.getId())));
                }
            }
        });
    }


    private QueryWrapper<SettlementPlanListQuery> planConditionPage(QueryWrapper<SettlementPlanListQuery> queryModel,
                                                                    String tenantId,
                                                                    SettlementParamQuery param,
                                                                    ContractOrgPermissionV orgPermissionV) {
        //queryModel.eq("c.deleted", 0);
        queryModel.eq("c.tenantId", tenantId);
        if (CollectionUtils.isNotEmpty(param.getCommunityIdList())) {
            queryModel.in("c.communityId", param.getCommunityIdList());
        }
        if (CollectionUtils.isNotEmpty(param.getStatusList())) {
            queryModel.in("c.status", param.getStatusList());
        }
        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio()) && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            queryModel.in("c.departId", orgPermissionV.getOrgIds());
        }
        return queryModel;
    }

    public PageV<ExportSettlePlanListV> exportListPlan(PageF<SearchF<SettlementPlanListQuery>> query) {
        List<ExportSettlePlanListV> data = new ArrayList<>();
        PageV<SettlePlanListV> settlePlanListVPageV = listPlan(query);
        List<SettlePlanListV> settlePlanListVList = settlePlanListVPageV.getRecords();

        settlePlanListVList.forEach(settlePlanListV -> {
            ExportSettlePlanListV exportSettlePlanListV = new ExportSettlePlanListV();
            BeanUtils.copyProperties(settlePlanListV,exportSettlePlanListV);
            // 合同起止日期要特殊处理
            exportSettlePlanListV.setGmtExpireStartEnd(settlePlanListV.getGmtExpireStart() + "~" + settlePlanListV.getGmtExpireEnd());
            exportSettlePlanListV.setContractServeDesc(Objects.nonNull(settlePlanListV.getContractServeType()) && settlePlanListV.getContractServeType() == 1 ? "是" : "否");
            data.add(exportSettlePlanListV);
        });
        log.info("结算计划导出数据总数为：{}",data.size());
        return PageV.of(query, settlePlanListVPageV.getTotal(), data);
    }

    public List<ContractPayPlanConcludeE> queryByCostTime(List<String> planIds, List<PayPlanPeriodV> periodList) {
        return this.baseMapper.queryByCostTime(planIds, periodList);
    }

    /**
     * 单个结算计划生成成本计划
     **/
    @Transactional(rollbackFor = Exception.class)
    public String generateCostPlan(GenerateCostPlanF req) {
        ContractPayPlanConcludeE planConcludeE = contractPayPlanConcludeMapper.selectById(req.getPlanId());
        if (Objects.isNull(planConcludeE)){
            throw new OwlBizException("结算计划不存在");
        }
        List<PayCostPlanE> existList = payCostPlanService.list(new QueryWrapper<PayCostPlanE>()
                .eq("planId", req.getPlanId())
                .eq("deleted", 0));
        if (CollectionUtils.isNotEmpty(existList)){
            throw new OwlBizException("已生成成本计划，不允许重复生成");
        }
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(planConcludeE.getContractId());
        if (Objects.isNull(contractPayConcludeE)){
            throw new OwlBizException("合同不存在或合同数据异常");
        }
        ContractPayPlanForSettlementV2 fundV = contractPayPlanConcludeMapper.getPlanListByPlanId(req.getPlanId());
        if (Objects.isNull(fundV)){
            throw new OwlBizException("结算计划数据异常");
        }
        if (StringUtils.isBlank(fundV.getTypeId())){
            throw new OwlBizException("结算计划数据异常,请检查费项、税率和清单项是否和合同清单保持一致");
        }
        List<PayCostPlanE> payCostPlanEList = splitByMonth(planConcludeE,contractPayConcludeE,fundV);
        payCostPlanService.saveOrUpdateBatch(payCostPlanEList);
        if(1 == contractPayConcludeE.getDeleted() && Arrays.asList(NkStatusEnum.已开启.getCode(), NkStatusEnum.关闭中.getCode()).contains(contractPayConcludeE.getNkStatus()) && 0 == contractPayConcludeE.getContractType()){
            log.info("NK合同不生成临时账单");
            return "生成成功";
        }
        iri(payCostPlanEList.stream().map(PayCostPlanE::getId).collect(Collectors.toList()),1);
        try {
            LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ContractPayPlanConcludeE::getMainId,req.getPlanId());
            queryWrapper.eq(ContractPayPlanConcludeE::getDeleted,0);
            ContractPayPlanConcludeE planBc = contractPayPlanConcludeMapper.selectOne(queryWrapper);
            List<PayCostPlanE> existBcList = payCostPlanService.list(new QueryWrapper<PayCostPlanE>()
                    .eq("planId", planBc.getId())
                    .eq("deleted", 0));
            if (CollectionUtils.isNotEmpty(existBcList)){
                log.info("已生成成本计划，不在重复生成");
                return "生成成功";
            }
            //对该计划对应的NK计划生成生成计划
            req.setPlanId(planBc.getId());
            this.generateCostPlan(req);
        } catch (Exception e) {
            log.error("YJ合同计划生成成本计划异常",e);
        }

        return "生成成功";
    }

    /**
     * 拆分
     **/
    public List<PayCostPlanE> splitByMonth(ContractPayPlanConcludeE contractPayPlanConcludeE,
                                           ContractPayConcludeE contractPayConcludeE,
                                           ContractPayPlanForSettlementV2 fundV) {
        LocalDate start = contractPayPlanConcludeE.getCostStartTime();
        LocalDate end = contractPayPlanConcludeE.getCostEndTime();

        //long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        BigDecimal totalPlannedSettlementAmount = contractPayPlanConcludeE.getPlannedCollectionAmount();
        BigDecimal totalTaxAmount = contractPayPlanConcludeE.getTaxAmount();
        BigDecimal totalNoTaxAmount = contractPayPlanConcludeE.getNoTaxAmount();

        CommonRangeAmountBO rangrAmount = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),start,end,totalPlannedSettlementAmount);

        List<PayCostPlanE> result = new ArrayList<>();
//        BigDecimal remainingPlannedSettlementAmount = totalPlannedSettlementAmount;
//        BigDecimal remainingTaxAmount = totalTaxAmount;
//        BigDecimal remainingNoTaxAmount = totalNoTaxAmount;

        LocalDate currentStart = start;
        if (Objects.isNull(contractPayPlanConcludeE.getPlannedCollectionTime())){
            throw new OwlBizException("结算计划数据异常，应结算日期不能为空");
        }
        Date plannedDate = Date.from(contractPayPlanConcludeE.getPlannedCollectionTime().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String prefix = "CBJH" + "-"
                + DateUtil.format(plannedDate, "yyyyMMdd") + "-"
                + contractPayConcludeE.getConmaincode() +"-";
        Integer maxNumByPrefix = payCostPlanService.getMaxNumByPrefix(prefix);
        Boolean isFirstRecord = true;

        String payeeId = "";
        String payeeName = "";
        List<ContractPayConcludeExpandE> contractPayConcludeExpandES = contractPayConcludeExpandService.lambdaQuery().eq(ContractPayConcludeExpandE::getContractId, contractPayConcludeE.getId()).list();

        if (CollectionUtils.isNotEmpty(contractPayConcludeExpandES)) {
            ContractPayConcludeExpandE contractPayConcludeExpandE = contractPayConcludeExpandES.get(0);
            String skdwxx = contractPayConcludeExpandE.getSkdwxx();
            if (StringUtils.isNotBlank(skdwxx)) {
                List<ContractSrfxxF> contractSrfxxFS = JSONObject.parseArray(skdwxx, ContractSrfxxF.class);
                if(CollectionUtils.isNotEmpty(contractSrfxxFS)) {
                    ContractSrfxxF srf = contractSrfxxFS.get(0);
                    payeeId = StringUtils.isNotBlank(srf.getTruepayeeid()) ? srf.getTruepayeeid() : srf.getPayeeid();
                    payeeName = StringUtils.isNotBlank(srf.getTruepayee()) ? srf.getTruepayee() : srf.getPayee();
                }
            }
        }

        while (currentStart.isBefore(end) || currentStart.isEqual(end)) {


            LocalDate currentEnd = currentStart.withDayOfMonth(currentStart.lengthOfMonth());
            if (currentEnd.isAfter(end)) {
                currentEnd = end;
            }

//            long daysInCurrentPeriod = ChronoUnit.DAYS.between(currentStart, currentEnd) + 1;
//            BigDecimal ratio = BigDecimal.valueOf(daysInCurrentPeriod).divide(BigDecimal.valueOf(totalDays), 6, BigDecimal.ROUND_HALF_UP);
//
//            BigDecimal plannedSettlementAmount = totalPlannedSettlementAmount.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal taxAmount = totalTaxAmount.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal noTaxAmount = totalNoTaxAmount.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal plannedSettlementAmount = BigDecimal.ZERO;
            BigDecimal taxAmount = BigDecimal.ZERO;
            BigDecimal noTaxAmount = BigDecimal.ZERO;
            if (isFirstRecord) {
                plannedSettlementAmount = rangrAmount.getStartMonthAmount();
//                taxAmount = remainingTaxAmount;
//                noTaxAmount = remainingNoTaxAmount;
                isFirstRecord = false; // 后续迭代不再进入
            }else if (currentEnd.isEqual(end)) {
                plannedSettlementAmount = rangrAmount.getEndMonthAmount();
                // 最后一笔使用减法避免尾差
//                plannedSettlementAmount = remainingPlannedSettlementAmount;
//                taxAmount = remainingTaxAmount;
//                noTaxAmount = remainingNoTaxAmount;
            } else {
                plannedSettlementAmount = rangrAmount.getAvgMonthAmount();
//                remainingPlannedSettlementAmount = remainingPlannedSettlementAmount.subtract(plannedSettlementAmount);
//                remainingTaxAmount = remainingTaxAmount.subtract(taxAmount);
//                remainingNoTaxAmount = remainingNoTaxAmount.subtract(noTaxAmount);
            }

            if (!"差额纳税".equals(contractPayPlanConcludeE.getTaxRate())) {
                BigDecimal taxRateDecimal = parseTaxRate(contractPayPlanConcludeE.getTaxRate());
                noTaxAmount = plannedSettlementAmount.divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
                taxAmount = plannedSettlementAmount.subtract(noTaxAmount);
            }

            PayCostPlanE payCostPlanE = new PayCostPlanE();
            payCostPlanE.setCostPlanCode(prefix+payCostPlanService.getNumStr(maxNumByPrefix++));
            payCostPlanE.setTenantId(contractPayPlanConcludeE.getTenantId());
            payCostPlanE.setContractId(contractPayPlanConcludeE.getContractId());
            // 账单信息-这里设空即可，入账后才有
            payCostPlanE.setBillId(null);
            payCostPlanE.setBillCode(null);
            payCostPlanE.setPlanId(contractPayPlanConcludeE.getId());
            payCostPlanE.setPlanNo(contractPayPlanConcludeE.getCostEstimationCode());
            // 核销状态,默认未核销
            payCostPlanE.setSettlementStatus(0);
            // 入账状态，默认未入账
            payCostPlanE.setBillGenerationStatus(0);
            payCostPlanE.setCostStartTime(currentStart);
            payCostPlanE.setCostEndTime(currentEnd);
            payCostPlanE.setPlannedSettlementAmount(plannedSettlementAmount);
            payCostPlanE.setTaxAmount(taxAmount);
            payCostPlanE.setNoTaxAmount(noTaxAmount);
            payCostPlanE.setOriginNoTaxAmount(noTaxAmount);
            payCostPlanE.setOriginTaxAmount(taxAmount);
            // 冗余
            payCostPlanE.setPlannedCollectionTime(contractPayPlanConcludeE.getPlannedCollectionTime());
            payCostPlanE.setPlanNumber(contractPayPlanConcludeE.getCostEstimationCode());
            payCostPlanE.setTermDate(contractPayPlanConcludeE.getTermDate());
            // 费项等冗余从清单表里拿
            payCostPlanE.setChargeItemId(fundV.getChargeItemId());
            payCostPlanE.setChargeItem(fundV.getChargeItem());
            payCostPlanE.setTypeId(fundV.getTypeId());
            payCostPlanE.setType(fundV.getType());
            payCostPlanE.setTaxRateId(fundV.getTaxRateId());
            payCostPlanE.setTaxRate(fundV.getTaxRate());
            if (StringUtils.isNotBlank(fundV.getTaxRate())){
                payCostPlanE.setTaxRate(fundV.getTaxRate().replaceAll("%",""));
            }
            payCostPlanE.setPayTypeId(fundV.getTypeId());
            payCostPlanE.setPayType(fundV.getPayType());
            // 合同冗余
            payCostPlanE.setCostCenterId(contractPayConcludeE.getCostCenterId());
            payCostPlanE.setCostCenterName(contractPayConcludeE.getCostCenterName());
            payCostPlanE.setOurPartyId(contractPayConcludeE.getOurPartyId());
            payCostPlanE.setOurParty(contractPayConcludeE.getOurParty());
            payCostPlanE.setMerchant(contractPayConcludeE.getOppositeOneId());
            payCostPlanE.setMerchantName(contractPayConcludeE.getOppositeOne());
            payCostPlanE.setDepartId(contractPayConcludeE.getDepartId());
            payCostPlanE.setDepartName(contractPayConcludeE.getDepartName());
            payCostPlanE.setPayObjectType("往来单位");
            payCostPlanE.setPayeeId(payeeId);
            payCostPlanE.setPayee(payeeName);

            payCostPlanE.setTenantId(tenantId());
            payCostPlanE.setCreator(userId());
            payCostPlanE.setCreatorName(userName());
            payCostPlanE.setGmtCreate(LocalDateTime.now());

            payCostPlanE.setOperator(userId());
            payCostPlanE.setOperatorName(userName());
            payCostPlanE.setGmtModify(LocalDateTime.now());
            payCostPlanE.setPayFundId(contractPayPlanConcludeE.getContractPayFundId());
            result.add(payCostPlanE);

            currentStart = currentEnd.plusDays(1);
        }

        if ("差额纳税".equals(contractPayPlanConcludeE.getTaxRate())) {
            BigDecimal totalTaxRateAmount = contractPayPlanConcludeE.getTaxAmount();
            int periodCount = result.size();
            if(Objects.isNull(totalTaxRateAmount)){
                totalTaxRateAmount = BigDecimal.ZERO;
            }
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
                result.get(i).setTaxAmount(taxAmount);
                result.get(i).setOriginTaxAmount(taxAmount);
                result.get(i).setNoTaxAmount(result.get(i).getPlannedSettlementAmount().subtract(taxAmount));
                result.get(i).setOriginNoTaxAmount(result.get(i).getPlannedSettlementAmount().subtract(taxAmount));
            }
        }

        return result;
    }

    /**
     * 入账
     * @param planIdList 成本计划id
     */
    public void iri(List<String> planIdList,Integer billType) {
        if (CollectionUtils.isEmpty(planIdList)) {
            throw new OwlBizException("收入计划id不能为空");
        }
        List<PayCostPlanE> payCostPlanEList = this.payCostPlanService.lambdaQuery().in(PayCostPlanE::getId,planIdList).list();
        if (CollectionUtils.isEmpty(payCostPlanEList)) {
            throw new OwlBizException("成本计划id不合法");
        }
        List<PayCostPlanE> hasExistList = this.payCostPlanService.lambdaQuery().in(PayCostPlanE::getId,planIdList).eq(PayCostPlanE::getBillType,billType).list();
        if (!CollectionUtils.isEmpty(hasExistList)) {
            throw new OwlBizException("不能重复推送临时账单");
        }
        //合同
        ContractPayConcludeE contractConcludeE = contractPayConcludeMapper.queryByContractId(payCostPlanEList.get(0).getContractId());
        //结算计划
        ContractPayPlanConcludeE contractIncomePlanConcludeE = this.lambdaQuery().eq(ContractPayPlanConcludeE::getId,payCostPlanEList.get(0).getPlanId()).eq(ContractPayPlanConcludeE::getDeleted,0).one();

        List<AddTemporaryChargeBillRf> addTemporaryChargeBillFs = new ArrayList<>();

        payCostPlanEList.forEach(payCostPlanE -> {
            AddTemporaryChargeBillRf addTemporaryChargeBillRf = new AddTemporaryChargeBillRf();
            addTemporaryChargeBillRf.setChargeItemId(contractIncomePlanConcludeE.getChargeItemId());
            addTemporaryChargeBillRf.setChargeItemName(contractIncomePlanConcludeE.getChargeItem());
            addTemporaryChargeBillRf.setTaxRateId(Long.valueOf(payCostPlanE.getTaxRateId()));
            addTemporaryChargeBillRf.setSource("支出合同");
            addTemporaryChargeBillRf.setBillMethod(10);

            addTemporaryChargeBillRf.setPayerId(payCostPlanE.getPayeeId());
            addTemporaryChargeBillRf.setPayerName(payCostPlanE.getPayee());
            addTemporaryChargeBillRf.setPayeeId(payCostPlanE.getOurPartyId());
            addTemporaryChargeBillRf.setPayeeName(payCostPlanE.getOurParty());
            addTemporaryChargeBillRf.setStatutoryBodyId(Long.valueOf(payCostPlanE.getOurPartyId()));
            addTemporaryChargeBillRf.setStatutoryBodyName(payCostPlanE.getOurParty());
            addTemporaryChargeBillRf.setPayerType(4);
            addTemporaryChargeBillRf.setCommunityId(contractConcludeE.getCommunityId());
            addTemporaryChargeBillRf.setCommunityName(contractConcludeE.getCommunityName());
            addTemporaryChargeBillRf.setCostCenterId(Long.valueOf(contractConcludeE.getCostCenterId()));
            addTemporaryChargeBillRf.setCostCenterName(contractConcludeE.getCostCenterName());
            addTemporaryChargeBillRf.setChargingArea(1);
            addTemporaryChargeBillRf.setChargeItemType(2);
            addTemporaryChargeBillRf.setContactName("往来单位");
            addTemporaryChargeBillRf.setOutBusNo("");//不用填写
            addTemporaryChargeBillRf.setTotalAmount(payCostPlanE.getPlannedSettlementAmount().multiply(new BigDecimal("100")).longValue());
            addTemporaryChargeBillRf.setReceivableAmount(payCostPlanE.getPlannedSettlementAmount().multiply(new BigDecimal("100")).longValue());
            addTemporaryChargeBillRf.setUnitPrice(addTemporaryChargeBillRf.getTotalAmount());
            addTemporaryChargeBillRf.setSupCpUnitId(contractConcludeE.getCommunityId());
            addTemporaryChargeBillRf.setPayType("0");
            addTemporaryChargeBillRf.setSysSource(2);
            addTemporaryChargeBillRf.setAppId(ContractSetConst.CONTRACTAPPID);
            addTemporaryChargeBillRf.setAppName(ContractSetConst.CONTRACTAPPNAME);
            addTemporaryChargeBillRf.setDescription("无");
            addTemporaryChargeBillRf.setApprovedFlag(true);
            addTemporaryChargeBillRf.setAccountDate(payCostPlanE.getCostStartTime());

            addTemporaryChargeBillRf.setStartTime(payCostPlanE.getCostStartTime().atStartOfDay());
            addTemporaryChargeBillRf.setEndTime(payCostPlanE.getCostEndTime().atTime(23, 59, 59));
            addTemporaryChargeBillRf.setReceivableDate(payCostPlanE.getPlannedCollectionTime());

            addTemporaryChargeBillRf.setContractNo(contractConcludeE.getContractNo());
            addTemporaryChargeBillRf.setContractName(contractConcludeE.getName());
            addTemporaryChargeBillRf.setExtField6(contractConcludeE.getId());

            if ("差额纳税".equals(payCostPlanE.getTaxRate())) {
                addTemporaryChargeBillRf.setTaxRate(null);
                addTemporaryChargeBillRf.setExtField8("差额纳税");
            }else{
                addTemporaryChargeBillRf.setTaxRate(new BigDecimal(payCostPlanE.getTaxRate()).divide(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP)));
            }
            addTemporaryChargeBillRf.setTaxAmountNew(payCostPlanE.getTaxAmount().multiply(new BigDecimal("100")));
            addTemporaryChargeBillRf.setExtField9(payCostPlanE.getOriginNoTaxAmount().multiply(new BigDecimal("100")).toString());
            addTemporaryChargeBillRf.setExtField10(payCostPlanE.getOriginTaxAmount().multiply(new BigDecimal("100")).toString());
            addTemporaryChargeBillFs.add(addTemporaryChargeBillRf);
        });
        List<TemporaryChargeBillPageV> temporaryChargeBillPageVList = financeFeignClient.temporaryAddBatch(addTemporaryChargeBillFs);
        if (CollectionUtils.isEmpty(temporaryChargeBillPageVList)) {
            throw new OwlBizException("成本计划入账异常");
        }
        for (int i = 0; i < payCostPlanEList.size(); i++) {
            PayCostPlanE payCostPlanE = payCostPlanEList.get(i);
            TemporaryChargeBillPageV temporaryChargeBillPageV = temporaryChargeBillPageVList.get(i);
            payCostPlanE.setBillGenerationStatus(IriStatusEnum.yes.getCode());
            payCostPlanE.setBillCode(temporaryChargeBillPageV.getBillNo());
            payCostPlanE.setIriTime(LocalDateTime.now());
            payCostPlanE.setBillId(String.valueOf(temporaryChargeBillPageV.getId()));
            payCostPlanE.setBillType(billType);
        }
        this.payCostPlanService.updateBatchById(payCostPlanEList);
    }

    public List<UpdateTemporaryChargeBillF> iriUpdate(String settleId, String contractId) {
        //查关联的结算详情
        List<ContractPaySettDetailsE> settDetailList = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, settleId)
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        log.info("当前结算单关联的结算详情:{}", JSON.toJSONString(settDetailList));
        if (CollectionUtils.isEmpty(settDetailList)) {
            log.info("当前结算单无关联的结算详情信息，直接跳出");
            return Collections.emptyList();
        }
        List<String> funIdList = settDetailList.stream()
                .map(ContractPaySettDetailsE::getPayFundId)
                .distinct()
                .collect(Collectors.toList());
        List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        log.info("当前结算单关联的结算周期信息为:{}", JSON.toJSONString(periodList));

        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            log.info("当前结算单无关联的结算周期信息，直接跳出");
            return Collections.emptyList();
        }

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeService.lambdaQuery().eq(ContractPayConcludeE::getId,contractId).one();
        List<PayCostPlanE> payCostPlanList = new ArrayList<>();
        if(Objects.nonNull(settDetailList.get(0).getStartDate()) && Objects.nonNull(settDetailList.get(0).getEndDate())){
            for(ContractPaySettDetailsE settD : settDetailList){
                payCostPlanList.addAll(contractPayCostPlanService.getCostListByPlanAndCostTime(
                        contractId,
                        settD.getPayFundId(),
                        Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }else{
            List<ContractPayPlanConcludeE> planFunList = contractPayPlanConcludeMapper.getFunDateList(contractId,settDetailList.stream().map(ContractPaySettDetailsE :: getPayFundId).collect(Collectors.toList()),
                    Global.mapperFacade.mapAsList(periodList, ContractPaySettlementPeriodF.class));
            for(ContractPayPlanConcludeE settD : planFunList){
                payCostPlanList.addAll(contractPayCostPlanService.getCostListByPlanAndCostTime(
                        contractId,
                        settD.getContractPayFundId(),
                        Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }
        if (CollectionUtils.isEmpty(payCostPlanList)) {
            //throw new OwlBizException("无法更新临时账单,原因：没有成本计划");
            log.error("结算计划无法更新临时账单,原因：没有成本计划,settleId is:{}",settleId);
            return Collections.emptyList();
        }


        List<UpdateTemporaryChargeBillF> updateTemporaryChargeBillFs = new ArrayList<>();
        payCostPlanList.forEach(plan->{
            UpdateTemporaryChargeBillF updateTemporaryChargeBillF = new UpdateTemporaryChargeBillF();
            updateTemporaryChargeBillF.setReductionAmount(plan.getReductionAmount());
            updateTemporaryChargeBillF.setId(String.valueOf(plan.getBillId()));
            updateTemporaryChargeBillF.setReceivableAmount(plan.getPaymentAmount().multiply(new BigDecimal("100")).longValue());
            updateTemporaryChargeBillF.setSupCpUnitId(contractPayConcludeE.getCommunityId());
            updateTemporaryChargeBillF.setExtField7(settleId);
            updateTemporaryChargeBillF.setTaxAmountNew(plan.getTaxAmount().multiply(new BigDecimal("100")));
            /*if(!"差额纳税".equals(plan.getTaxRate())){
                BigDecimal taxRateDecimal = parseTaxRate(plan.getTaxRate());
                BigDecimal noTaxAmount = ysjnAmount.divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
                updateTemporaryChargeBillF.setTaxAmountNew((ysjnAmount.subtract(noTaxAmount)).multiply(new BigDecimal("100")));
            }else{
                PayCostPlanE planCost = payCostList.stream().filter(item -> item.getId().equals(plan.getId())).collect(Collectors.toList()).get(0);
                //应收/账单金额*原始税额
                BigDecimal taxAmountRatio = ysjnAmount.divide(plan.getPlannedSettlementAmount(),2,RoundingMode.HALF_UP);
                updateTemporaryChargeBillF.setTaxAmountNew(planCost.getTaxAmount().multiply(taxAmountRatio).multiply(new BigDecimal("100")));
            }*/
            updateTemporaryChargeBillFs.add(updateTemporaryChargeBillF);
        });
        //payCostPlanMapper.updatePayPlan(payIncomePlanEList);

        Boolean flag = financeFeignClient.temporaryUpdateBatch(updateTemporaryChargeBillFs);
        if (!flag) {
            log.error("iriUpdate error,settleId is:{}",settleId);
            return Collections.emptyList();
        }


        payCostPlanList.forEach(in-> in.setBillType(2));
        this.payCostPlanService.updateBatchById(payCostPlanList);
        return updateTemporaryChargeBillFs;
    }

    public List<ContractPayPlanConcludeE> queryByCostTimeNotFinished(List<String> planIds, List<PayPlanPeriodV> periodList) {
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


    private static int getAddMonth(Integer splitMode) {
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

    public List<ContractPayPlanConcludeE> queryByCostTimeForBill(List<String> planIds, List<PayPlanPeriodV> periodList) {
        return this.baseMapper.queryByCostTimeForBill(planIds, periodList);
    }

    public List<ContractPayPlanInnerInfoV> getInnerInfoByContractIdsForPayApp(List<String> contractIds) {
        List<ContractPayPlanInnerInfoV> innerInfoVList = this.baseMapper.getInnerInfoByContractIdsForPayApp(contractIds);
        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);
        Map<String,DictionaryCode> dicMap = value.stream().collect(Collectors.toMap(DictionaryCode::getCode,v->v,(v1,v2)->v1));
        for (ContractPayPlanInnerInfoV v : innerInfoVList) {
            v.setConmanagetypename(null != dicMap.get(v.getConmanagetype()) ? dicMap.get(v.getConmanagetype()).getName() : null);
        }
        return innerInfoVList;
    }

    //根据id删除成本计划
    @Transactional
    public Boolean deletedPayCostPlan(String id) {
        ContractPayPlanConcludeE payPlan = contractPayPlanConcludeMapper.selectById(id);
        if(Objects.isNull(payPlan)){
            throw new OwlBizException("请输入正确结算计划ID");
        }
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(payPlan.getContractId());
        if(Objects.isNull(payConcludeE)){
            throw new OwlBizException("该合同不存在");
        }
        //查询关联结算审批
        List<String> canNotRemoveList = settlementConcludeService.getSettlementByPlan(Arrays.asList(id));
        if (CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该结算计划已被结算单使用，不允许删除");
        }
        QueryWrapper<PayCostPlanE> queryModel = new QueryWrapper<>();
        queryModel.eq("contractId", payPlan.getContractId());
        queryModel.eq("planId", payPlan.getId());
        queryModel.eq("deleted",0);
        List<PayCostPlanE> payCostPlanEList = payCostPlanService.list(queryModel);
        if(CollectionUtils.isNotEmpty(payCostPlanEList)){
            List<String> billIdList = payCostPlanEList.stream().map(PayCostPlanE::getBillId).collect(Collectors.toList());
            //[校验]根据临时账单ID获取对应报账单/合同报账单数据
            String message = financeFeignClient.getVoucherBillByReceivableId(billIdList, payConcludeE.getCommunityId());
            if(StringUtils.isNotEmpty(message)){
                throw new OwlBizException(message);
            }
            //删除成本计划
            payCostPlanEList.forEach(x->x.setDeleted(1));
            payCostPlanService.updateBatchById(payCostPlanEList);
            //根据临时账单ID删除对应临时账单数据
            financeFeignClient.deleteReceivableBillById(billIdList, payConcludeE.getCommunityId());
        }
        return Boolean.TRUE;
    }

    //deletedPayPlan
    @Transactional
    public Boolean deletedPayPlan(String id) {

        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(payConcludeE)){
            throw new OwlBizException("该合同不存在");
        }
        //查询结算计划
        List<ContractPayPlanConcludeE> realPlans = this.list(Wrappers.<ContractPayPlanConcludeE>lambdaQuery()
                .eq(ContractPayPlanConcludeE::getContractId, id)
                .eq(ContractPayPlanConcludeE::getDeleted, 0));
        List<String> realPlanIds = realPlans.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
        //查询成本计划
        long payIncomeCount = payCostPlanService.count(Wrappers.<PayCostPlanE>lambdaQuery()
                .in(PayCostPlanE::getPlanId, realPlanIds)
                .eq(PayCostPlanE::getDeleted, 0));
        //查询关联结算审批
        List<String> canNotRemoveList = settlementConcludeService.getSettlementByPlan(realPlanIds);
        if (payIncomeCount > 0 && CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该结算计划存在结算审批与成本计划，不允许删除");
        }
        if (payIncomeCount > 0) {
            throw new OwlBizException("该结算计划存在成本计划，不允许删除");
        }
        if (CollectionUtils.isNotEmpty(canNotRemoveList)) {
            throw new OwlBizException("该结算计划存在结算审批，不允许删除");
        }
        //删除计划
        this.contractPayPlanConcludeMapper.deleteBatchIds(realPlanIds);
        return Boolean.TRUE;
    }
}
