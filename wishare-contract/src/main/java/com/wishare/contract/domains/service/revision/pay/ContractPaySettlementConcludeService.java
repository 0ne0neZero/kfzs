package com.wishare.contract.domains.service.revision.pay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apis.file.enums.FileBusinessTypeEnum;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemSaveF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceOtherFileF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settlement.*;
import com.wishare.contract.apps.fo.revision.remote.ContractInvoiceInfoF;
import com.wishare.contract.apps.fo.revision.remote.InvoiceZJF;
import com.wishare.contract.apps.fo.revision.remote.MeasurementDetailF;
import com.wishare.contract.apps.remote.clients.*;
import com.wishare.contract.apps.remote.fo.InvoiceBaseInfoF;
import com.wishare.contract.apps.remote.fo.ResultTemporaryChargeBillF;
import com.wishare.contract.apps.remote.fo.UpdateTemporaryChargeBillF;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalDataF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.vo.EsbRetrunInfoV;
import com.wishare.contract.apps.remote.vo.OrgFinanceRv;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.apps.service.revision.pay.ContractPayBusinessService;
import com.wishare.contract.domains.bo.CommonRangeAmountBO;
import com.wishare.contract.domains.bo.CommonRangeDateBO;
import com.wishare.contract.domains.bo.CommonRangeDayAmountBO;
import com.wishare.contract.domains.bo.SettlementPlanBO;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillDetailsE;
import com.wishare.contract.domains.entity.revision.pay.*;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsFundE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.enums.InvoiceChangeEnum;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.enums.settlement.SettlementTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.attachment.AttachmentMapper;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillDetailsMapper;
import com.wishare.contract.domains.mapper.revision.pay.*;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPayConcludeSettdeductionMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPaySettDetailsMapper;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillCalculateService;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillDetailsService;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillItemService;
import com.wishare.contract.domains.service.revision.pay.bo.PlanWriteOffBo;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPayConcludeSettdeductionService;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPaySettDetailsService;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractSettFundV;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundInfoV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.pay.settlement.*;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.infrastructure.utils.BigDecimalUtils;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.owl.exception.OwlSysException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.beans.Tree;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.enums.WithinDateTimeEnum;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/13:57
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPaySettlementConcludeService extends ServiceImpl<ContractPaySettlementConcludeMapper, ContractPaySettlementConcludeE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettDetailsMapper contractPaySettDetailsMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeSettdeductionMapper contractPayConcludeSettdeductionMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillMapper contractPayBillMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillDetailsMapper contractSettlementsBillDetailsMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsFundMapper contractSettlementsFundMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayPlanConcludeService contractPayPlanConcludeService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettDetailsService contractPaySettDetailsService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeSettdeductionService contractPayConcludeSettdeductionService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractProcessRecordMapper contractProcessRecordMapper;
    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentMapper attachmentMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayFundMapper contractPayFundMapper;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillCalculateService contractSettlementsBillCalculateService;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillItemService contractSettlementsBillItemService;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillDetailsService contractSettlementsBillDetailsService;

    @Setter(onMethod_ = {@Autowired})
    private ContractOrgCommonService contractOrgCommonService;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettlementPeriodMapper settlementPeriodMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettlementPlanRelationMapper settlementPlanRelationMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettlementContractSnapshotMapper settlementContractSnapshotMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayCostPlanService contractPayCostPlanService;

    @Value("${process.create.flag:0}")
    private Integer createProcessFlag;

    @Value("${process.create.projectId:}")
    private String createProcessProjectId;

    @Value("${process.create.bizCode:}")
    private String bizCode;

    @Value("${sett.detail.mock:true}")
    private Boolean settDetailMock;
    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    private static final String DIFF_TAX_TYPE_NAME = "差额纳税";
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;
    @Autowired
    private ContractPayConcludeSettlementPeriodMapper contractPayConcludeSettlementPeriodMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    @Lazy
    private ContractPayFundService contractPayFundService;
    @Autowired
    @Lazy
    private ContractPayBusinessService contractPayBusinessService;
    @Autowired
    private OrgFeignClient orgFeignClient;

    public ContractPaySettlementDetailsV getDetailsById(String id){
        ContractPaySettlementConcludeE map = contractPaySettlementConcludeMapper.selectById(id);
        ContractPaySettlementDetailsV contractPayPlanDetailsV = Global.mapperFacade.map(map, ContractPaySettlementDetailsV.class);
        //查询对应的合同，编号，供应商名称
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(map.getContractId());
        contractPayPlanDetailsV.setContractNo(contractPayConcludeE.getContractNo());
        contractPayPlanDetailsV.setContractAmount(contractPayConcludeE.getContractAmountOriginalRate());
//        contractPayPlanDetailsV.setSettleCycle(contractPayPlanDetailsV.getSettleCycle().replace(",","至"));
        contractPayPlanDetailsV.setRealSettlementAmount(map.getPlannedCollectionAmount().subtract(map.getDeductionAmount()));
        //附件
        LambdaQueryWrapper<AttachmentE> queryWrapper4 = new LambdaQueryWrapper<>();
        queryWrapper4.eq(AttachmentE::getBusinessId, id).eq(AttachmentE::getDeleted, 0);

        List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper4);
        if(ObjectUtils.isNotEmpty(attachmentEList)){
            contractPayPlanDetailsV.setContractOne(attachmentEList.stream().filter(s -> s.getType() == 1).map(s -> s.getName()).collect(Collectors.joining(",")));
            contractPayPlanDetailsV.setContractTwo(attachmentEList.stream().filter(s -> s.getType() == 2).map(s -> s.getName()).collect(Collectors.joining(",")));
            contractPayPlanDetailsV.setContractThree(attachmentEList.stream().filter(s -> s.getType() == 3).map(s -> s.getName()).collect(Collectors.joining(",")));
        }

        //查询周期信息
        List<PayPlanPeriodV> contractPaySettlementPeriodVList = settlementPeriodMapper.getPeriodList(id);

        //查询对应的结算单明细信息
        LambdaQueryWrapper<ContractPaySettDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPaySettDetailsE::getSettlementId, id)
                .eq(ContractPaySettDetailsE::getDeleted,0);
        List<ContractPaySettDetailsE> contractPaySettlementConcludeEList = contractPaySettDetailsMapper.selectList(queryWrapper);
        List<ContractPaySettDetailsV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractPaySettlementConcludeEList, ContractPaySettDetailsV.class);
        if(ObjectUtils.isNotEmpty(contractPaySettDetailsVList)){
            //获取该合同清单数据
            List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, contractPayConcludeE.getId())
                    .eq(ContractPayFundE.IS_HY, 0)
                    .eq(ContractPayFundE.DELETED, 0)
                    .in(ContractPayFundE.ID, contractPaySettDetailsVList.stream().map(ContractPaySettDetailsV :: getPayFundId).collect(Collectors.toList()))
                    .isNotNull("cbApportionId"));

            for(ContractPaySettDetailsV s : contractPaySettDetailsVList){
                String[] sk = s.getChargeItem().split("/");
                if(sk.length > 0){
                    s.setChargeItem(sk[sk.length-1]);
                }
                s.setChargeItemAllPath(financeFeignClient.chargeName(s.getChargeItemId()));
                s.setTaxRateAmount(s.getTaxRateAmount().setScale(2, RoundingMode.HALF_UP));
                s.setAmountWithOutRate(s.getAmountWithOutRate().setScale(2, RoundingMode.HALF_UP));

                ContractPayFundE payFun = mainFunList.stream().filter(
                                x->x.getId().equals(s.getPayFundId())).findFirst()
                        .orElse(new ContractPayFundE());
                if(ObjectUtils.isNotEmpty(payFun) && ObjectUtils.isNotEmpty(payFun.getCbApportionId())){
                    s.setIsCostData(1);
                }else{
                    s.setIsCostData(0);
                }
            }

            //获取成本计划
            List<PayCostPlanV> payCostPlanVList = new ArrayList<>();
            if(Objects.nonNull(contractPaySettlementConcludeEList.get(0).getStartDate()) && Objects.nonNull(contractPaySettlementConcludeEList.get(0).getEndDate())){
                for(ContractPaySettDetailsE settD : contractPaySettlementConcludeEList){
                    payCostPlanVList.addAll(contractPayCostPlanService.queryPlanVListByPlanIdList(
                            map.getContractId(),
                            settD.getPayFundId(),
                            Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
                }
            }else{
                List<ContractPayPlanConcludeE> planFunList = contractPayPlanConcludeMapper.getFunDateList(map.getContractId(),contractPaySettlementConcludeEList.stream().map(ContractPaySettDetailsE :: getPayFundId).collect(Collectors.toList()),
                        Global.mapperFacade.mapAsList(contractPaySettlementPeriodVList, ContractPaySettlementPeriodF.class));
                for(ContractPayPlanConcludeE settD : planFunList){
                    payCostPlanVList.addAll(contractPayCostPlanService.queryPlanVListByPlanIdList(
                            map.getContractId(),
                            settD.getContractPayFundId(),
                            Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
                }
            }
            contractPayPlanDetailsV.setPayCostPlanVList(payCostPlanVList);

           /* List<String> funIdList = contractPaySettDetailsVList.stream()
                    .map(ContractPaySettDetailsV::getPayFundId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(contractPaySettlementPeriodVList)){
                List<PayCostPlanV> payCostPlanVList = contractPayCostPlanService.queryPlanVListByPlanIdList(funIdList,contractPaySettlementPeriodVList);
                contractPayPlanDetailsV.setPayCostPlanVList(payCostPlanVList);
            }*/

        }
        //扣款明细详情
        LambdaQueryWrapper<ContractPayConcludeSettdeductionE> queryWrapper10 = new LambdaQueryWrapper<>();
        queryWrapper10.eq(ContractPayConcludeSettdeductionE::getSettlementId, id)
                .eq(ContractPayConcludeSettdeductionE :: getDeleted, 0);
        List<ContractPayConcludeSettdeductionE> contractPayConcludeSettdeductionEList = contractPayConcludeSettdeductionMapper.selectList(queryWrapper10);
        List<ContractPayConcludeSettdeductionV> contractPayConcludeSettdeductionVList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(contractPayConcludeSettdeductionEList)){
            contractPayConcludeSettdeductionVList = Global.mapperFacade.mapAsList(contractPayConcludeSettdeductionEList, ContractPayConcludeSettdeductionV.class);
            for(ContractPayConcludeSettdeductionV s :  contractPayConcludeSettdeductionVList){
                String[] sk = s.getChargeItem().split("/");
                if(sk.length > 0){
                    s.setChargeItem(sk[sk.length-1]);
                }
                if (StringUtils.isNotBlank(s.getTypeId())) {
                    List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项业务类型.getCode(), s.getTypeId());
                    if (CollectionUtils.isNotEmpty(value)) {
                        s.setType(value.get(0).getName());
                    }
                }
            }
            contractPayPlanDetailsV.setContractPayConcludeSettdeductionVList(contractPayConcludeSettdeductionVList);
        }


        //查询对应的收票信息
        LambdaQueryWrapper<ContractSettlementsBillE> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContractSettlementsBillE::getSettlementId, id);
        List<ContractSettlementsBillE> contractPayBillES = contractPayBillMapper.selectList(queryWrapper1);
        List<ContractPayBillV> contractPayBillVList = Global.mapperFacade.mapAsList(contractPayBillES, ContractPayBillV.class);

        if(ObjectUtils.isNotEmpty(contractPayBillVList)){
            List<String> billId = contractPayBillVList.stream().map( s -> s.getId()).collect(Collectors.toList());
            List<Integer> reviewStatus = new ArrayList<>();
            reviewStatus.add(1);
            reviewStatus.add(2);
            LambdaQueryWrapper<ContractSettlementsBillDetailsE> queryWrapper99 = new LambdaQueryWrapper<>();
            queryWrapper99.in(ContractSettlementsBillDetailsE::getBillId, billId)
                    .in(ContractSettlementsBillDetailsE::getReviewStatus, reviewStatus);
            List<ContractSettlementsBillDetailsE> contractSettlementsBillDetailsEList = contractSettlementsBillDetailsMapper.selectList(queryWrapper99);
            if(ObjectUtils.isNotEmpty(contractSettlementsBillDetailsEList)){
                List<ContractSettlementsBillDetailsV>  contractSettlementsBillDetailsVList = Global.mapperFacade.mapAsList(contractSettlementsBillDetailsEList, ContractSettlementsBillDetailsV.class);
                for(ContractSettlementsBillDetailsV s : contractSettlementsBillDetailsVList){
                    s.setReviewStatusName(ReviewStatusEnum.parseName(s.getReviewStatus()));
                    List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.发票类型.getCode(), s.getBillType().toString());
                    if (CollectionUtils.isNotEmpty(value)) {
                        s.setBillTypeName(value.get(0).getName());
                    }
                }
                contractPayPlanDetailsV.setContractSettlementsBillDetailsVList(contractSettlementsBillDetailsVList);
            }
        }

        //查询对应的付款信息
        LambdaQueryWrapper<ContractSettlementsFundE> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ContractSettlementsFundE::getSettlementId, id);
        List<ContractSettlementsFundE> contractSettlementsFundES = contractSettlementsFundMapper.selectList(queryWrapper2);
        List<ContractSettFundV> contractSettFundVList = Global.mapperFacade.mapAsList(contractSettlementsFundES, ContractSettFundV.class);

        contractPayPlanDetailsV.setContractSettFundVList(contractSettFundVList);
        contractPayPlanDetailsV.setContractPayBillVList(contractPayBillVList);
        contractPayPlanDetailsV.setContractPaySettDetailsSaveFList(contractPaySettDetailsVList);

        List<ContractPayPlanForSettlementV> contractPayPlanForSettlementVList = settlementPlanRelationMapper.getPlanList(id);
        contractPayPlanDetailsV.setContractPayPlanForSettlementVList(contractPayPlanForSettlementVList);

        if (CollectionUtils.isNotEmpty(contractPayPlanForSettlementVList)){
            // start~end 逗号分隔
            String periodStr = contractPaySettlementPeriodVList.stream().map(e ->
                    DateUtil.format(e.getStartDate(), "yyyy-MM-dd") + "~" + DateUtil.format(e.getEndDate(), "yyyy-MM-dd"))
                    .collect(Collectors.joining(","));
            contractPayPlanDetailsV.setPeriodStr(periodStr);
        }
        contractPayPlanDetailsV.setContractPaySettlementPeriodVList(contractPaySettlementPeriodVList);

        // 合同相关信息
        ContractPayConcludeSettlementContractSnapshotE snapshot = settlementContractSnapshotMapper.querySnapshotBySettlementId(id);
        if (Objects.isNull(snapshot)){
            snapshot = settlementContractSnapshotMapper.querySnapshotByContractId(map.getContractId());
        }

        ContractPaySettlementContractInfoV contractInfoV = Global.mapperFacade.map(snapshot, ContractPaySettlementContractInfoV.class);
        contractPayPlanDetailsV.setContractPaySettlementContractInfoV(contractInfoV);
        return contractPayPlanDetailsV;
    }


    @Transactional(rollbackFor = Exception.class)
    public String save(ContractPaySettlementAddF addF){
        checkBeforeSaveSettlement(addF);

        ContractPaySettlementConcludeE map = Global.mapperFacade.map(addF, ContractPaySettlementConcludeE.class);
//        map.setSettleCycle(addF.getSettleCycle().stream().collect(Collectors.joining(",")));
//        map.setStartTime(LocalDate.parse(addF.getSettleCycle().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        map.setEndTime(LocalDate.parse(addF.getSettleCycle().get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BigDecimal plannedCollectionAmountSum = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;
        BigDecimal sumChooseAmount = BigDecimal.ZERO;
        BigDecimal sumSettleAmount = BigDecimal.ZERO;
        for(int i = 0; i < addF.getContractPaySettDetailsSaveFList().size(); i ++){
            plannedCollectionAmountSum = plannedCollectionAmountSum.add(addF.getContractPaySettDetailsSaveFList().get(i).getAmount());
        }
        for(int i = 0; i < addF.getContractPayConcludeSettdeductionSaveFList().size(); i ++){
            deductionAmount = deductionAmount.add(addF.getContractPayConcludeSettdeductionSaveFList().get(i).getAmount());
        }

        //父结算单
        ContractPaySettlementConcludeV sb = contractPaySettlementConcludeMapper.getContractPaySettlementInfo(addF.getContractId());

        if(ObjectUtils.isNotEmpty(sb)){
            map.setPid(sb.getId());
        }else{
            ContractPaySettlementConcludeE pidMap = new ContractPaySettlementConcludeE();
            pidMap.setContractId(map.getContractId());
            pidMap.setContractName(map.getContractName());
            pidMap.setMerchant(map.getMerchant());
            pidMap.setMerchantName(map.getMerchantName());
            pidMap.setPlannedCollectionAmount(plannedCollectionAmountSum);
            pidMap.setDeductionAmount(deductionAmount);
            pidMap.setTenantId(tenantId());
            contractPaySettlementConcludeMapper.insert(pidMap);
            map.setPid(pidMap.getId());
        }
        map.setPayFundNumber(genFundSettleNode(addF.getContractId()));
        map.setPlannedCollectionAmount(plannedCollectionAmountSum);
        map.setDeductionAmount(deductionAmount);
        map.setTenantId(tenantId());
        boolean isCreateProcess = isCreatePrecess(map.getContractId());
        if(addF.getSaveType().equals("2") && isCreateProcess){
            map.setReviewStatus(1);
            map.setPlannedCollectionTime(LocalDate.now());
        }else {
            map.setReviewStatus(ReviewStatusEnum.DRAFT.getCode());
        }
//        List<String> termDates = addF.getContractPayPlanConcludeUpdateFS().stream().map(s -> s.getTermDate().toString()).collect(Collectors.toList());
//        List<String> planId = addF.getContractPayPlanConcludeUpdateFS().stream().map(s -> s.getId()).collect(Collectors.toList());
        List<String> planId = addF.getPlanIdList();
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeService.getByIdList(planId);
        for(ContractPayPlanConcludeE s : contractIncomePlanConcludeVList){
            sumChooseAmount = sumChooseAmount.add(s.getPlannedCollectionAmount());
            sumSettleAmount = sumSettleAmount.add(s.getSettlementAmount());
        }
        if(plannedCollectionAmountSum.add(sumSettleAmount).compareTo(sumChooseAmount) > 0){
            throw new OwlBizException("结算总金额不能超过付款计划总金额!");
        }
//        String termDate = termDates.stream().collect(Collectors.joining(","));
        map.setTermDate(addF.getTermDate());
        contractPaySettlementConcludeMapper.insert(map);
        if(addF.getSaveType().equals("2") && isCreateProcess){
            ContractPayConcludeE payContract = contractPayConcludeMapper.queryByContractId(map.getContractId());
            ContractPayProcessV payProcessV = checkSettlentPeriod(map.getId(), payContract);
            if(payProcessV.getCode().equals("500")){
                throw new OwlBizException(payProcessV.getMessage());
            }
            createProcess(map,payProcessV.getType());//创建审批流
        }
        List<ContractPayPlanConcludeE> planFunList = contractPayPlanConcludeMapper.getFunDateList(map.getContractId(),addF.getContractPaySettDetailsSaveFList().stream().map(ContractPaySettDetailsSaveF :: getPayFundId).collect(Collectors.toList()),
                addF.getContractPaySettlementPeriodSaveFList());
        for(int i = 0; i < addF.getContractPaySettDetailsSaveFList().size(); i ++) {
            ContractPaySettDetailsE contractPaySettDetailsE = Global.mapperFacade.map(addF.getContractPaySettDetailsSaveFList().get(i), ContractPaySettDetailsE.class);
            contractPaySettDetailsE.setSettlementId(map.getId());
            contractPaySettDetailsE.setTenantId(tenantId());
            if (StringUtils.isNotBlank(addF.getContractPaySettDetailsSaveFList().get(i).getTaxRateId())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), addF.getContractPaySettDetailsSaveFList().get(i).getTaxRateId());
                if (CollectionUtils.isNotEmpty(value)) {
                    contractPaySettDetailsE.setTaxRate(value.get(0).getName());
                }
            }
            ContractPayPlanConcludeE planFun = planFunList.stream().filter(s -> contractPaySettDetailsE.getPayFundId().equals(s.getContractPayFundId())).findFirst().orElse(new ContractPayPlanConcludeE());
            contractPaySettDetailsE.setStartDate(planFun.getCostStartTime());
            contractPaySettDetailsE.setEndDate(planFun.getCostEndTime());
            contractPaySettDetailsMapper.insert(contractPaySettDetailsE);
        }
        for(int i = 0; i < addF.getContractPayConcludeSettdeductionSaveFList().size(); i ++) {
            ContractPayConcludeSettdeductionE contractPayConcludeSettdeductionE = Global.mapperFacade.map(addF.getContractPayConcludeSettdeductionSaveFList().get(i), ContractPayConcludeSettdeductionE.class);
            contractPayConcludeSettdeductionE.setSettlementId(map.getId());
            contractPayConcludeSettdeductionE.setTenantId(tenantId());
            contractPayConcludeSettdeductionMapper.insert(contractPayConcludeSettdeductionE);
        }

        this.extractedReductionAmount(addF.getContractPayConcludeSettdeductionSaveFList(),contractIncomePlanConcludeVList);
        if (CollectionUtils.isNotEmpty(addF.getContractPaySettlementPeriodSaveFList())){
            List<ContractPayConcludeSettlementPeriodE> periodList = Lists.newArrayList();
            for (ContractPaySettlementPeriodF periodF : addF.getContractPaySettlementPeriodSaveFList()) {
                ContractPayConcludeSettlementPeriodE periodE = new ContractPayConcludeSettlementPeriodE();
                periodE.setSettlementId(map.getId());
                periodE.setStartDate(periodF.getStartDate());
                periodE.setEndDate(periodF.getEndDate());
                periodList.add(periodE);
            }
            settlementPeriodMapper.insertBatch(periodList);

        }
        if (CollectionUtils.isNotEmpty(addF.getPlanIdList())){
            List<ContractPayConcludeSettlementPlanRelationE> relationList = Lists.newArrayList();
            for (String curPlanId : addF.getPlanIdList()) {
                ContractPayConcludeSettlementPlanRelationE relationE = new ContractPayConcludeSettlementPlanRelationE();
                relationE.setPlanId(curPlanId);
                relationE.setSettlementId(map.getId());
                relationList.add(relationE);
            }
            settlementPlanRelationMapper.insertBatch(relationList);
        }
        handlePidPaySettlementInfo(map.getPid());
        List<AttachmentE> ssk = attachmentService.getAttachmentByBusinessId(map.getContractId(),1);
        if(ObjectUtils.isNotEmpty(ssk)){
            for(AttachmentE  sk : ssk){
                AttachmentE newAtt = new AttachmentE();
                BeanUtils.copyProperties(sk,newAtt);
                newAtt.setId(null);
                newAtt.setBusinessType(1001);
                newAtt.setBusinessId(map.getId());
                attachmentMapper.insert(newAtt);
            }
        }
        dealSettlementContractSnapshot(map.getId(), addF.getContractId(), false);
        updateSettlementStep(map.getId(), 1);
        return map.getId();
    }

    private void extractedReductionAmount(List<ContractPayConcludeSettdeductionSaveF> contractPayConcludeSettdeductionSaveFList, List<ContractPayPlanConcludeE> contractPayPlanConcludeVList) {
        if (CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionSaveFList)){
            // 按减免清单中费项分组，并汇总其减免金额
            Map<Long, BigDecimal> groupedByChargeItemId = contractPayConcludeSettdeductionSaveFList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayConcludeSettdeductionSaveF::getChargeItemId,
                            Collectors.reducing(BigDecimal.ZERO, ContractPayConcludeSettdeductionSaveF::getAmount,BigDecimal::add)
                    ));
            List<Long> chargeItemIdList = groupedByChargeItemId.keySet().stream().collect(Collectors.toList());

            //筛选减免清单中费项相同的计划数据
            List<ContractPayPlanConcludeE> chargePlanList = contractPayPlanConcludeVList.stream()
                    .filter(plan -> chargeItemIdList.contains(plan.getChargeItemId()))
                    .collect(Collectors.toList());
            //获取所有符合计划的未结算总额
            BigDecimal planTotalAmount = chargePlanList.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //处理减免金额逻辑
            for (Map.Entry<Long, BigDecimal> entry : groupedByChargeItemId.entrySet()) {
                Long chargeItemId = entry.getKey();
                BigDecimal reductionTotalAmount = entry.getValue();

                //获取减免费项对应计划数据
                List<ContractPayPlanConcludeE> funPlanList = chargePlanList.stream()
                        .filter(plan -> chargeItemId.equals(plan.getChargeItemId()))
                        .collect(Collectors.toList());

                // 按清单分组计划并累计其计划未结算金额
                Map<String, BigDecimal> funTotalAmount = funPlanList.stream()
                        .collect(Collectors.groupingBy(
                                ContractPayPlanConcludeE::getContractPayFundId,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        plan -> plan.getPlannedCollectionAmount().subtract(plan.getSettlementAmount()),
                                        BigDecimal::add
                                )
                        ));

                // 计算各清单占用比例,且计算对应减免金额
                Map<String, BigDecimal> funReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funTotalAmount, planTotalAmount,reductionTotalAmount);
                //获取清单下计划数，并按期数排序
                Map<String, List<ContractPayPlanConcludeE>> groupedAndSorted = funPlanList.stream()
                        .collect(Collectors.groupingBy(
                                ContractPayPlanConcludeE::getContractPayFundId,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .sorted(Comparator.comparing(ContractPayPlanConcludeE::getTermDate))
                                                .collect(Collectors.toList())
                                )
                        ));
                for (Map.Entry<String, List<ContractPayPlanConcludeE>> plan : groupedAndSorted.entrySet()) {
                    String key = plan.getKey();
                    //该清单减免金额
                    BigDecimal planReductionAmount = funReductionAmountMap.get(key);
                    List<ContractPayPlanConcludeE> planList = plan.getValue();
                    //获取该清单计划未结算金额
                    BigDecimal planAmount = planList.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Map<String, BigDecimal> planDeAmount = planList.stream()
                            .collect(Collectors.groupingBy(
                                    ContractPayPlanConcludeE::getId,
                                    Collectors.reducing(
                                            BigDecimal.ZERO,
                                            p -> p.getPlannedCollectionAmount().subtract(p.getSettlementAmount()),
                                            BigDecimal::add
                                    )
                            ));
                    Map<String, BigDecimal> planReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(planDeAmount, planAmount,planReductionAmount);
                    List<CommonRangeDayAmountBO> dayAmountList = new ArrayList<>();
                    for (Map.Entry<String, BigDecimal> pAmout : planReductionAmountMap.entrySet()) {
                        CommonRangeDayAmountBO bo = new CommonRangeDayAmountBO();
                        bo.setId(pAmout.getKey());
                        bo.setReductionAmount(pAmout.getValue());
                        dayAmountList.add(bo);
                    }
                    contractPayPlanConcludeMapper.updatePayPlan(dayAmountList);
                }
            }
        }
    }

    private void checkBeforeSaveSettlement(ContractPaySettlementAddF addF) {
        List<String> planIdList = addF.getPlanIdList();
        Map<String, List<ContractPayPlanConcludeE>> groupMap = checkPlanList(planIdList);
        List<String> existedSettlementIds = settlementPlanRelationMapper.selectExistRuningSettlement(planIdList);
        if (CollectionUtils.isNotEmpty(existedSettlementIds)){
            throw new OwlBizException("所选结算计划已被其他结算单选择，请重新选择");
        }
        Map<String, ContractPayFundE> funMap = new HashMap<>();
        //查询全部的清单数据
        List<ContractPayFundE> allPayFunds = contractPayFundMapper.selectList(Wrappers.<ContractPayFundE>lambdaQuery()
                .in(ContractPayFundE::getContractId, addF.getContractId())
                .eq(ContractPayFundE::getDeleted, 0));
        if (CollectionUtils.isNotEmpty(allPayFunds)) {
            funMap = allPayFunds.stream()
                    .collect(Collectors.toMap(
                            ContractPayFundE::getId,
                            fund -> fund,
                            (oldValue, newValue) -> newValue) // 如果 key 冲突，保留新值
                    );
        }
        // 所有金额必须大于等于0
        if (Objects.nonNull(addF.getActualSettlementAmount()) && addF.getActualSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("实际结算金额不能为空或小于0");
        }
        if (Objects.nonNull(addF.getTotalSettledAmount()) && addF.getTotalSettledAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计结算金额不能为空或小于0");
        }
        if (Objects.nonNull(addF.getTotalSettlementAmount()) && addF.getTotalSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计应结算金额不能为空或小于0");
        }
        if (CollectionUtils.isNotEmpty(addF.getContractPaySettDetailsSaveFList())) {
            for (ContractPaySettDetailsSaveF detailsSaveF : addF.getContractPaySettDetailsSaveFList()) {
                if (detailsSaveF.getAmount().stripTrailingZeros().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("结算明细-金额不能小于0");
                }
            }
        } else {
            throw new OwlBizException("结算明细不能为空");
        }
        if (CollectionUtils.isNotEmpty(addF.getContractPayConcludeSettdeductionSaveFList())) {
            for (ContractPayConcludeSettdeductionSaveF deductionSaveF : addF.getContractPayConcludeSettdeductionSaveFList()) {
                if (deductionSaveF.getAmount().stripTrailingZeros().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("扣款明细汇总金额不能小于0");
                }
            }
        }

        //结算明细中，税率为差额纳税的明细，税额+不含税金额 必须等于 明细总额
        List<ContractPaySettDetailsSaveF> targetDetails = addF.getContractPaySettDetailsSaveFList().stream()
                .filter(detail -> StringUtils.equals("差额纳税", detail.getTaxRate())
                        && ObjectUtils.isNotEmpty(detail.getTaxRateAmount())
                        && ObjectUtils.isNotEmpty(detail.getAmountWithOutRate())
                        && ObjectUtils.isNotEmpty(detail.getAmount()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(targetDetails)) {
            for (ContractPaySettDetailsSaveF targetDetail : targetDetails) {
                BigDecimal addAmount = targetDetail.getTaxRateAmount().add(targetDetail.getAmountWithOutRate()).stripTrailingZeros();
                if (targetDetail.getAmount().stripTrailingZeros().compareTo(addAmount) != 0) {
                    throw new OwlBizException("差额纳税的税额加不含税金额必须等于明细总额");
                }
            }
        }

        // 扣款总金额不能大于结算明细的汇总金额
        BigDecimal settlementAmount = addF.getContractPaySettDetailsSaveFList().stream()
                .map(ContractPaySettDetailsSaveF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros();
        BigDecimal deductionAmount = addF.getContractPayConcludeSettdeductionSaveFList().stream()
                .map(ContractPayConcludeSettdeductionSaveF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros();
        if (deductionAmount.compareTo(settlementAmount) > 0) {
            throw new OwlBizException("扣款总金额不能大于结算明细的汇总金额");
        }

        // 结算明细分组金额不能大于对应分组成本预估金额的汇总
        Map<String, List<ContractPaySettDetailsSaveF>> groupSettleMap = addF.getContractPaySettDetailsSaveFList().stream()
                .collect(Collectors.groupingBy(ContractPaySettDetailsSaveF::getPayFundId));
        for (Map.Entry<String, List<ContractPaySettDetailsSaveF>> entry : groupSettleMap.entrySet()) {
            String key = entry.getKey();
            List<ContractPayPlanConcludeE> curPlans = groupMap.get(key);
            if (CollectionUtils.isEmpty(curPlans)){
                throw new OwlBizException("结算明细的清单项、费项、税率 要和所选结算计划保持一致");
            }
            List<ContractPaySettDetailsSaveF> value = entry.getValue();
            BigDecimal settleAmount = value.stream()
                    .map(ContractPaySettDetailsSaveF::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros();
            BigDecimal unSettleAmount = curPlans.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros();
            if (settleAmount.compareTo(unSettleAmount) > 0) {
                log.error("结算明细分组金额不能大于对应分组结算计划未结算金额：清单金额{}，预计未结算金额：{}",settleAmount,unSettleAmount);
                throw new OwlBizException("结算明细分组金额不能大于对应分组结算计划未结算金额");
            }
            ContractPayFundE payFun = funMap.get(key);
            if (Objects.nonNull(payFun) && Objects.nonNull(payFun.getCheckAmount()) && payFun.getCheckAmount().compareTo(BigDecimal.ZERO) >= 0 && settleAmount.compareTo(payFun.getCheckAmount()) > 0) {
                //清单分组【费项ID，款项类型，付费方式ID，税率ID，付费类型ID，标准金额，收费标准ID】
                String title = payFun.getChargeItem()+"_"
                        +payFun.getType()+"_"
                        +"含税单价："+payFun.getStandAmount();
                throw new OwlBizException("清单【"+title+"】的结算金额不能大于清单可操作金额，可操作金额："+payFun.getCheckAmount());
            }
        }

        String contractId = addF.getContractId();
        ContractPaySettlementConcludeV exist = contractPaySettlementConcludeMapper.getApprovingContractPaySettlementInfo(contractId);
        if (Objects.nonNull(exist)){
            throw new OwlBizException("系统内有未完成的结算审批单，请审核通过后再创建结算审批单");
        }
    }

    private Map<String, List<ContractPayPlanConcludeE>> checkPlanList(List<String> planIdList) {
        if (CollectionUtils.isEmpty(planIdList)) {
            throw new OwlBizException("请选择成本预估计划");
        }
        LambdaQueryWrapper<ContractPayPlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPayPlanConcludeE::getId, planIdList)
                .eq(ContractPayPlanConcludeE::getDeleted,0)
                .ne(ContractPayPlanConcludeE::getPaymentStatus, 2);
        List<ContractPayPlanConcludeE> payPlanList = contractPayPlanConcludeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(payPlanList)){
            throw new OwlBizException("所选成本预估计划不存在或已结算");
        }
        //所选成本预估计划的应结算时间必须小于等于当前月份
        //获取下个月第一天
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        ContractPayPlanConcludeE overNowPlan = payPlanList.stream()
                .filter(e -> e.getPlannedCollectionTime().isAfter(nextMonthFirstDay))
                .findFirst().orElse(null);
        if (Objects.nonNull(overNowPlan)){
            throw new OwlBizException("所选成本预估计划应结算时间必须小于等于当前月份");
        }
        // 按照 payFundId 分组
        Map<String, List<ContractPayPlanConcludeE>> groupMap = payPlanList.stream()
                .collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractPayFundId));
        for (List<ContractPayPlanConcludeE> list : groupMap.values()) {
            //组内按照期数升序排序
            list.sort(Comparator.comparing(ContractPayPlanConcludeE::getTermDate));
            Integer curTermDate = null;
            LocalDate currentDate = null;
            for (ContractPayPlanConcludeE e : list) {
               /* if (!Objects.isNull(curTermDate)) {
                    //  组内应该允许相邻期数相等
                    if (e.getTermDate() != curTermDate + 1) {
                        throw new OwlBizException("所选成本预估计划期数必须严格连续");
                    }
                }*/

                if (!Objects.isNull(currentDate)) {
                    // 组内应该允许相邻计划的时间不连续[期数相等不校验]
                    if (e.getTermDate() == curTermDate+1 &&
                            !currentDate.plusDays(1).isEqual(e.getCostStartTime())) {
                        throw new OwlBizException("所选成本预估计划时间必须严格连续");
                    }
                }
                curTermDate = e.getTermDate();
                currentDate = e.getCostEndTime();
            }
        }
        return groupMap;
    }


    private void checkBeforeUpdateSettlement(ContractPaySettlementConcludeUpdateF updateF) {
        List<String> planIdList = updateF.getPlanIdList();
        Map<String, List<ContractPayPlanConcludeE>> groupMap = checkPlanList(planIdList);

        List<String> existedSettlementIds = settlementPlanRelationMapper.selectExistRuningSettlement(planIdList);
        if (CollectionUtils.isNotEmpty(existedSettlementIds)){
            existedSettlementIds = existedSettlementIds.stream()
                    .filter(e -> !e.equals(updateF.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(existedSettlementIds)){
                throw new OwlBizException("所选结算计划被其他结算单选择，请重新选择");
            }
        }

        Map<String, ContractPayFundE> funMap = new HashMap<>();
        //查询全部的清单数据
        List<ContractPayFundE> allPayFunds = contractPayFundMapper.selectList(Wrappers.<ContractPayFundE>lambdaQuery()
                .in(ContractPayFundE::getContractId, updateF.getContractId())
                .eq(ContractPayFundE::getDeleted, 0));
        if (CollectionUtils.isNotEmpty(allPayFunds)) {
            funMap = allPayFunds.stream()
                    .collect(Collectors.toMap(
                            ContractPayFundE::getId,
                            fund -> fund,
                            (oldValue, newValue) -> newValue) // 如果 key 冲突，保留新值
                    );
        }

        // 所有金额必须大于等于0
        if (Objects.nonNull(updateF.getActualSettlementAmount()) && updateF.getActualSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("实际结算金额不能为空或小于0");
        }
        if (Objects.nonNull(updateF.getTotalSettledAmount()) && updateF.getTotalSettledAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计结算金额不能为空或小于0");
        }
        if (Objects.nonNull(updateF.getTotalSettlementAmount()) && updateF.getTotalSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计应结算金额不能为空或小于0");
        }
        if (CollectionUtils.isNotEmpty(updateF.getContractPaySettDetailsSaveFList())) {
            for (ContractPaySettDetailsUpdateF detailsSaveF : updateF.getContractPaySettDetailsSaveFList()) {
                if (detailsSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("结算明细-金额不能小于0");
                }
            }
        } else {
            throw new OwlBizException("结算明细不能为空");
        }
        if (CollectionUtils.isNotEmpty(updateF.getContractPayConcludeSettdeductionSaveFList())) {
            for (ContractPayConcludeSettdeductionUpdateF deductionSaveF : updateF.getContractPayConcludeSettdeductionSaveFList()) {
                if (deductionSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("扣款明细汇总金额不能小于0");
                }
            }
        }

        // 扣款总金额不能大于结算明细的汇总金额
        BigDecimal settlementAmount = updateF.getContractPaySettDetailsSaveFList().stream()
                .map(ContractPaySettDetailsUpdateF::getAmount)

                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deductionAmount = updateF.getContractPayConcludeSettdeductionSaveFList().stream()
                .map(ContractPayConcludeSettdeductionUpdateF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (deductionAmount.compareTo(settlementAmount) > 0) {
            throw new OwlBizException("扣款总金额不能大于结算明细的汇总金额");
        }

        // 结算明细分组金额不能大于对应分组成本预估金额的汇总
        Map<String, List<ContractPaySettDetailsUpdateF>> groupSettleMap = updateF.getContractPaySettDetailsSaveFList().stream()
                .collect(Collectors.groupingBy(ContractPaySettDetailsUpdateF::getPayFundId));
        for (Map.Entry<String, List<ContractPaySettDetailsUpdateF>> entry : groupSettleMap.entrySet()) {
            String key = entry.getKey();
            List<ContractPayPlanConcludeE> curPlans = groupMap.get(key);
            if (CollectionUtils.isEmpty(curPlans)){
                throw new OwlBizException("结算明细的清单项、费项、税率 要和所选结算计划保持一致");
            }
            List<ContractPaySettDetailsUpdateF> value = entry.getValue();
            BigDecimal settleAmount = value.stream()
                    .map(ContractPaySettDetailsUpdateF::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal unSettleAmount = curPlans.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleAmount.compareTo(unSettleAmount) > 0) {
                throw new OwlBizException("结算明细分组金额不能大于对应分组结算计划未结算金额");
            }
            ContractPayFundE payFun = funMap.get(key);
            if (Objects.nonNull(payFun) && Objects.nonNull(payFun.getCheckAmount()) && payFun.getCheckAmount().compareTo(BigDecimal.ZERO) >= 0 && settleAmount.compareTo(payFun.getCheckAmount()) > 0) {
                //清单分组【费项ID，款项类型，付费方式ID，税率ID，付费类型ID，标准金额，收费标准ID】
                String title = payFun.getChargeItem()+"_"
                        +payFun.getType()+"_"
                        +payFun.getPayWay()+"_"
                        +payFun.getTaxRate()+"_"
                        +payFun.getPayType()+"_"
                        +"含税单价："+payFun.getStandAmount()+"_"
                        +payFun.getStandard();
                throw new OwlBizException("该清单【"+title+"】的结算金额不能大于清单可操作金额");
            }
        }
    }

    public PageV<ContractSettdeductionDetailV> contractSettdeductionDetailPage(PageF<SearchF<?>> request) {
        List<String> settlementIds = new ArrayList<>();
        if (settDetailMock) {
            settlementIds.add("076a4154a92f4177a8520d8da85ab2bb");
        } else {
            PageF<SearchF<?>> pageConditions = new PageF<>();
            BeanUtils.copyProperties(request, pageConditions);
            pageConditions.setPageNum(1);
            pageConditions.setPageSize(Integer.MAX_VALUE);
            PageV<ContractPaySettlementPageV2> settlementPage = this.pageV2(pageConditions);
            if (Objects.isNull(settlementPage) || CollectionUtils.isEmpty(settlementPage.getRecords())) {
                return PageV.of(request, 0, new ArrayList<>());
            }
            Set<String> settlementIdsSet = settlementPage.getRecords().stream()
                    .map(ContractPaySettlementPageV2::getChildren)
                    .flatMap(Collection::stream)
                    .map(ContractPaySettlementPageV2::getId)
                    .collect(Collectors.toSet());
            settlementIds.addAll(settlementIdsSet);
        }
        Page<?> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        IPage<ContractSettdeductionDetailV> pageList = contractPayConcludeSettdeductionMapper.contractSettdeductionDetailPage(pageF, settlementIds);
        if (Objects.isNull(pageList) || CollectionUtils.isEmpty(pageList.getRecords())) {
            return PageV.of(request, 0, new ArrayList<>());
        }
        //处理periodDisplay+type字段
        List<DictionaryCode> dictionaryCodes = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项业务类型.getCode(), null);
        dictionaryCodes.addAll(customDictionaryCode());
        Map<String, DictionaryCode> dictionaryCodeMap = dictionaryCodes.stream()
                .collect(Collectors.toMap(DictionaryCode::getCode, Function.identity(), (v1, v2) -> v1));
        pageList.getRecords().forEach(detail -> {
            if (CollectionUtils.isNotEmpty(detail.getPeriods())) {
                List<String> dateList = detail.getPeriods().stream().map(p -> {
                    String start = DateUtil.format(p.getStartDate(), "yyyy.MM.dd");
                    String end = DateUtil.format(p.getEndDate(), "yyyy.MM.dd");
                    return start + "-" + end;
                }).collect(Collectors.toList());
                detail.setPeriodDisplay(String.join(",", dateList));
                if (dictionaryCodeMap.containsKey(detail.getTypeId())) {
                    detail.setType(dictionaryCodeMap.get(detail.getTypeId()).getName());
                }
            }
        });

        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    /**
     * hotfix：增加"品质扣款"、"缺勤扣款"、"其他扣款"三个项目
     *
     * @return
     */
    private List<DictionaryCode> customDictionaryCode() {
        List<DictionaryCode> list = new ArrayList<>();
        DictionaryCode d1 = new DictionaryCode();
        d1.setCode("100");
        d1.setName("品质扣款");
        list.add(d1);

        DictionaryCode d2 = new DictionaryCode();
        d2.setCode("99");
        d2.setName("缺勤扣款");
        list.add(d2);

        DictionaryCode d3 = new DictionaryCode();
        d3.setCode("98");
        d3.setName("其他扣款");
        list.add(d3);

        return list;
    }

    private void dealSettlementContractSnapshot(String settlementId, String contractId,boolean needDel) {
        if (needDel){
            settlementContractSnapshotMapper.deleteBySettlementId(settlementId);
        }
        ContractPayConcludeSettlementContractSnapshotE snapshotE = settlementContractSnapshotMapper.querySnapshotByContractId(contractId);
        if (Objects.isNull(snapshotE)){
            throw new OwlBizException("合同不存在或数据异常，请检查!");
        }
        snapshotE.setSettlementId(settlementId);
        settlementContractSnapshotMapper.insert(snapshotE);
    }

    private boolean isCreatePrecess(String contractId){
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(contractId);
        if(payConcludeE == null || !createProcessFlag.equals(1) || !createProcessProjectId.equals(payConcludeE.getCommunityId())){
            return false;
        }
        return true;
    }



    private String createProcess(ContractPaySettlementConcludeE concludeE,Integer type){
        LambdaQueryWrapper queryWrappers = new LambdaQueryWrapper<ContractProcessRecordE>()
                .eq(ContractProcessRecordE::getContractId, concludeE.getId())
                .eq(ContractProcessRecordE::getType, type)
                .eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE sk = contractProcessRecordMapper.selectOne(queryWrappers);
        if (ObjectUtils.isNotEmpty(sk) && StringUtils.isNotBlank(sk.getProcessId())) {
            BusinessInfoF businessInfoF = buildBusinessInfoF(concludeE,type);
            businessInfoF.setProcessId(sk.getProcessId());
            if(ContractConcludeEnum.SETTLEMENT_FUND.getCode()==  type){
                log.info("结算单更新审批表单数据入参:{}", JSON.toJSONString(businessInfoF));
            }else{
                log.info("NK结算单更新审批表单数据入参:{}", JSON.toJSONString(businessInfoF));
            }
            //响应结构保持不变
            ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
            if(ContractConcludeEnum.SETTLEMENT_FUND.getCode()==  type){
                log.info("结算单更新审批表单数据出参:{}", JSON.toJSONString(processCreateV));
            }else{
                log.info("NK结算单更新审批表单数据出参:{}", JSON.toJSONString(processCreateV));
            }
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("结算单流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                sk.setReviewStatus(ReviewStatusEnum.已拒绝.getCode());
                contractProcessRecordMapper.updateById(sk);
                throw new OwlBizException("流程更新失败");
            }
            //流程正常发起了，将流程记录表的审批状态修改为"审批中"
            sk.setReviewStatus(ReviewStatusEnum.审批中.getCode());
            contractProcessRecordMapper.updateById(sk);
            //流程名更新成功后再继续原逻辑
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(sk.getProcessId());
            return externalFeignClient.validateFw(f);
        }
        BusinessInfoF businessInfoF = buildBusinessInfoF(concludeE,type);
        if(ContractConcludeEnum.SETTLEMENT_FUND.getCode()==  type){
            log.info("结算单新增审批表单数据入参:{}", JSON.toJSONString(businessInfoF));
        }else{
            log.info("NK结算单新增审批表单数据入参:{}", JSON.toJSONString(businessInfoF));
        }
        //发起流程
        ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
        if(ContractConcludeEnum.SETTLEMENT_FUND.getCode()==  type){
            log.info("结算单更新审批表单数据出参:{}", JSON.toJSONString(processCreateV));
        }else{
            log.info("NK结算单更新审批表单数据出参:{}", JSON.toJSONString(processCreateV));
        }
        Integer reviewStatusCode = ReviewStatusEnum.审批中.getCode();
        // 若创建失败数据不入库
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("支出合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            reviewStatusCode = ReviewStatusEnum.已拒绝.getCode();
//            throw new OwlBizException("中建返回数据时,根据合同ID检索数据失败,流程创建失败");
        }

        // ζ.构造入库数据,能存的都存下来
        String requestid = processCreateV.getET_RESULT().getRequestid();
        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestid) // 流程请求id
                .with(ContractProcessRecordE::setContractId, concludeE.getId()) // 合同ID
                .with(ContractProcessRecordE::setReviewStatus, reviewStatusCode) // 审核状态
                .with(ContractProcessRecordE::setTenantId, concludeE.getTenantId()) // 租户ID
                .with(ContractProcessRecordE::setCreator, concludeE.getCreator()) // 创建人
                .with(ContractProcessRecordE::setCreatorName, concludeE.getCreatorName()) // 创建人名称
                .with(ContractProcessRecordE::setType, type)
                .build();
        // η.非并发接口,为保证幂等性,无对应记录再插入数据 CHECK 代码逻辑不适用并发环境;暂不做redis缓存
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, requestid).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(recordE)) {
            contractProcessRecordMapper.updateById(contractProcessRecordE);
            log.info("返回的支出流程已存在,已更新数据库记录");
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }

        // θ.数据入 [contract_process_record] 库
        contractProcessRecordMapper.insert(contractProcessRecordE);
        log.info("返回的支出流程不存在,已插入数据库记录");

        if(StringUtils.isNotBlank(requestid)){
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if(ObjectUtils.isNotEmpty(s)){
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }

        return "";
    }

    private BusinessInfoF buildBusinessInfoF(ContractPaySettlementConcludeE concludeE,Integer type){
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //下面原来就传了的参数完全不动
        businessInfoF.setFormDataId(concludeE.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(type);
        businessInfoF.setFlowType(bizCode);
        businessInfoF.setContractName(concludeE.getTitle());
        //下面是新增的参数
        businessInfoF.setSsqy(concludeE.getBelongRegion());
        businessInfoF.setJslx(concludeE.getSettlementType());
        businessInfoF.setSscj(concludeE.getBelongLevel());
        businessInfoF.setJsfl(concludeE.getSettlementClassify());
        businessInfoF.setXmlx(concludeE.getCommunityType());
        //businessInfoF.setHtjshsje(concludeE.getPlannedCollectionAmount().subtract(concludeE.getDeductionAmount()));
        //逻辑替换，更改为时间结算金额
        businessInfoF.setHtjshsje(concludeE.getActualSettlementAmount());

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(concludeE.getContractId());
        if (Objects.isNull(contractPayConcludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        if(1 == contractPayConcludeE.getDeleted() && NkStatusEnum.getNkStatusList().contains(contractPayConcludeE.getNkStatus()) && 0 == contractPayConcludeE.getContractType()){
            contractPayConcludeE = contractPayConcludeMapper.queryByContractId(contractPayConcludeE.getPid());
        }
        if (Objects.isNull(contractPayConcludeE)) {
            throw new OwlBizException("根据合同ID检索数据失败");
        }
        if(ContractBusinessLineEnum.物管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
            businessInfoF.setGlzz(1);
            if(StringUtils.equals("总部", contractPayConcludeE.getRegion())){
                businessInfoF.setGlzz(0);
            }
        }else if (ContractBusinessLineEnum.建管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
            businessInfoF.setGlzz(3);
        }else{
            businessInfoF.setGlzz(2);
        }
        return businessInfoF;
    }

    private String genFundSettleNode(String contractId){
        String date = new SimpleDateFormat("yyyyMM", Locale.CHINESE).format(new Date());
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(contractId);
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getContractId, contractId)
                .notIn(ContractPaySettlementConcludeE::getPid,0)
                .eq(ContractPaySettlementConcludeE::getDeleted,0);
        List<ContractPaySettlementConcludeE> contractPayPlanConcludeEList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        int amount = contractPayPlanConcludeEList.size() + 1;
        return contractPayConcludeE.getContractNo() + "-" + date.substring(2,6) + "-0" + amount;
    }

    private void createProcess4Fund(ContractSettlementsFundE concludeE){
        if(contractProcessRecordMapper.exists(new LambdaQueryWrapper<ContractProcessRecordE>()
                .eq(ContractProcessRecordE::getContractId, concludeE.getId())
                .eq(ContractProcessRecordE::getType, ContractConcludeEnum.SETTLEMENT_FUND.getCode())
                .eq(ContractProcessRecordE::getDeleted, 0))){
            return;
        }
        BusinessInfoF businessInfoF = new BusinessInfoF();
        businessInfoF.setFormDataId(concludeE.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(ContractConcludeEnum.SETTLEMENT_FUND.getCode());
        ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
        Integer reviewStatusCode = ReviewStatusEnum.审批中.getCode();
        // 若创建失败数据不入库
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("支出合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            reviewStatusCode = ReviewStatusEnum.已拒绝.getCode();
//            throw new OwlBizException("中建返回数据时,根据合同ID检索数据失败,流程创建失败");
        }

        // ζ.构造入库数据,能存的都存下来
        String requestid = processCreateV.getET_RESULT().getRequestid();
        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestid) // 流程请求id
                .with(ContractProcessRecordE::setContractId, concludeE.getId()) // 合同ID
                .with(ContractProcessRecordE::setReviewStatus, reviewStatusCode) // 审核状态
                .with(ContractProcessRecordE::setTenantId, concludeE.getTenantId()) // 租户ID
                .with(ContractProcessRecordE::setCreator, concludeE.getCreator()) // 创建人
                .with(ContractProcessRecordE::setCreatorName, concludeE.getCreatorName()) // 创建人名称
                .with(ContractProcessRecordE::setType, ContractConcludeEnum.SETTLEMENT_FUND.getCode()) // 类型（1合同订立支出2合同订立收入）
                .build();
        // η.非并发接口,为保证幂等性,无对应记录再插入数据 CHECK 代码逻辑不适用并发环境;暂不做redis缓存
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, requestid).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(recordE)) {
            contractProcessRecordMapper.updateById(contractProcessRecordE);
            log.info("返回的支出流程已存在,已更新数据库记录");
            return;
        }

        // θ.数据入 [contract_process_record] 库
        contractProcessRecordMapper.insert(contractProcessRecordE);
        log.info("返回的支出流程不存在,已插入数据库记录");
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractPaySettlementConcludeV> page(PageF<SearchF<ContractPaySettlementConcludePageF>> form) {
        Page<ContractPaySettlementConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractPaySettlementConcludePageF> queryModel = form.getConditions().getQueryModel();
        IPage<ContractPaySettlementConcludeV> pageList = contractPaySettlementConcludeMapper.collectionPaySettlementPage(pageF,conditionPage(queryModel, curIdentityInfo().getTenantId()));
        List<ContractPaySettlementConcludeV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(ContractPaySettlementConcludeV::getId).collect(Collectors.toList());
        List<ContractPaySettlementConcludeV> concludeVList = contractPaySettlementConcludeMapper.queryByPath(queryModel, parentIdList, getIdentityInfo().get().getTenantId());
        List<ContractPaySettlementConcludeV> list = TreeUtil.treeing(concludeVList);
        for(ContractPaySettlementConcludeV s : list){
            if(s.getPid().equals("0")){
                s.setReviewStatus(null);
                s.setTermDate(null);
                s.setPaymentStatus(null);
                s.setInvoiceStatus(null);
                s.setPlanStatus(null);
                s.setPaymentMethod(null);
            }
        }
        return PageV.of(form, pageList.getTotal(), list);
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public IPage<ContractPaySettlementConcludeInfoV> pageInfo(PageF<SearchF<ContractPaySettlementConcludePageF>> form) {
        Page<ContractPaySettlementConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractPaySettlementConcludePageF> queryModel = form.getConditions().getQueryModel();
        return contractPaySettlementConcludeMapper.queryByPathInfo(pageF,conditionPage(queryModel, curIdentityInfo().getTenantId()));

    }

    public ContractPaySettlementConcludeEditV list(ContractPaySettlementConcludeListF contractPayPlanConcludeListF){
        ContractPaySettlementConcludeEditV contractPaySettlementConcludeEditV = new ContractPaySettlementConcludeEditV();

        ContractPaySettlementConcludeE concludeE = contractPaySettlementConcludeMapper.selectById(contractPayPlanConcludeListF.getId());
        ContractPaySettlementConcludeInfoV contractPaySettlementConcludeInfoV = Global.mapperFacade.map(concludeE, ContractPaySettlementConcludeInfoV.class);
        ContractPaySettlementConcludeV contractPaySettlementConcludeV = Global.mapperFacade.map(contractPaySettlementConcludeInfoV, ContractPaySettlementConcludeV.class);
        if(ObjectUtils.isNotEmpty(concludeE) && StringUtils.isNotEmpty(concludeE.getSettleCycle())){
            contractPaySettlementConcludeV.setSettleCycle(Arrays.asList(concludeE.getSettleCycle().split(",")));
        }

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(concludeE.getContractId());
        contractPaySettlementConcludeV.setChangContractAmount(contractPayConcludeE.getChangContractAmount());
        contractPaySettlementConcludeEditV.setContractPaySettlementConcludeV(contractPaySettlementConcludeV);

        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList = contractPayPlanConcludeMapper.getHowOrder(contractPayPlanConcludeListF.getContractId(), null);
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
        contractPaySettlementConcludeEditV.setContractPayPlanConcludeVList(contractPayPlanConcludeVList);

        List<ContractPaySettDetailsV> contractPaySettDetailsVList = contractPaySettDetailsService.getDetailsBySettlementId(contractPayPlanConcludeListF.getId());
        //获取该合同清单数据
        List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractPayConcludeE.getId())
                .eq(ContractPayFundE.IS_HY, 0)
                .eq(ContractPayFundE.DELETED, 0)
                .in(ContractPayFundE.ID, contractPaySettDetailsVList.stream().map(ContractPaySettDetailsV :: getPayFundId).collect(Collectors.toList()))
                .isNotNull("cbApportionId"));
        for(ContractPaySettDetailsV s : contractPaySettDetailsVList){
            ContractPayFundE payFun = mainFunList.stream().filter(
                            x->x.getId().equals(s.getPayFundId())).findFirst()
                    .orElse(new ContractPayFundE());
            if(ObjectUtils.isNotEmpty(payFun) && ObjectUtils.isNotEmpty(payFun.getCbApportionId())){
                s.setIsCostData(1);
                s.setAccountItemCode(payFun.getAccountItemCode());
                s.setAccountItemName(payFun.getAccountItemName());
                s.setAccountItemFullCode(payFun.getAccountItemFullCode());
                s.setAccountItemFullName(payFun.getAccountItemFullName());
            }else{
                s.setIsCostData(0);
            }
        }

        contractPaySettlementConcludeEditV.setContractPaySettDetailsVList(contractPaySettDetailsVList);

        List<ContractPayConcludeSettdeductionV> contractPayConcludeSettdeductionVList = contractPayConcludeSettdeductionService.getDetailsBySettlementId(contractPayPlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractPayConcludeSettdeductionVList(contractPayConcludeSettdeductionVList);

        List<ContractPayPlanForSettlementV> contractPayPlanForSettlementVList = settlementPlanRelationMapper.getPlanList(contractPayPlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractPayPlanForSettlementVList(contractPayPlanForSettlementVList);

        List<PayPlanPeriodV> contractPaySettlementPeriodVList = settlementPeriodMapper.getPeriodList(contractPayPlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractPaySettlementPeriodVList(contractPaySettlementPeriodVList);
        return contractPaySettlementConcludeEditV;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public ContractPaySettlementConcludeSumV accountAmountSum(PageF<SearchF<ContractPaySettlementConcludePageF>> form) {
        Page<ContractPaySettlementConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<ContractPaySettlementConcludePageF> queryModel = form.getConditions().getQueryModel();
        return contractPaySettlementConcludeMapper.accountAmountSum(conditionPage(queryModel, curIdentityInfo().getTenantId()));
    }

    public ContractPaySettlementConcludeSumV accountAmountSumV2(PageF<SearchF<?>> request) {
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            ContractPaySettlementConcludeSumV sumV = new ContractPaySettlementConcludeSumV();
            sumV.setPlannedCollectionAmountSum(new BigDecimal("0.00"));
            sumV.setPaymentAmountSum(new BigDecimal("0.00"));
            return sumV;
        }
        QueryWrapper<?> queryModel = request.getConditions().getQueryModel();
        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio()) && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            queryModel.in("c.departId", orgPermissionV.getOrgIds());
        }
        queryModel.ne("s.pid",'0');
        queryModel.eq("s.deleted",0);
        queryModel.groupBy("s.id");
        return contractPaySettlementConcludeMapper.accountAmountSum2(queryModel);
    }

    /**
     * 根据Id更新
     *
     * @param contractPayConcludeF 根据Id更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ContractPaySettlementConcludeUpdateF contractPayConcludeF){
        if (contractPayConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        // 前置校验
        checkBeforeUpdateSettlement(contractPayConcludeF);
        ContractPaySettlementConcludeE map1 = contractPaySettlementConcludeMapper.selectById(contractPayConcludeF.getId());
        ContractPaySettlementConcludeE map = Global.mapperFacade.map(contractPayConcludeF, ContractPaySettlementConcludeE.class);
//        map.setSettleCycle(contractPayConcludeF.getSettleCycle().stream().collect(Collectors.joining(",")));
//        map.setStartTime(LocalDate.parse(contractPayConcludeF.getSettleCycle().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        map.setEndTime(LocalDate.parse(contractPayConcludeF.getSettleCycle().get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BigDecimal plannedCollectionAmountSum = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;
        BigDecimal sumChooseAmount = BigDecimal.ZERO;
        BigDecimal sumSettleAmount = BigDecimal.ZERO;
        for(int i = 0; i < contractPayConcludeF.getContractPaySettDetailsSaveFList().size(); i ++){
            plannedCollectionAmountSum = plannedCollectionAmountSum.add(contractPayConcludeF.getContractPaySettDetailsSaveFList().get(i).getAmount());
        }
        for(int i = 0; i < contractPayConcludeF.getContractPayConcludeSettdeductionSaveFList().size(); i ++){
            deductionAmount = deductionAmount.add(contractPayConcludeF.getContractPayConcludeSettdeductionSaveFList().get(i).getAmount());
        }
        map.setPlannedCollectionAmount(plannedCollectionAmountSum);
        map.setDeductionAmount(deductionAmount);
        if(contractPayConcludeF.getSaveType().equals("2")){
            map.setReviewStatus(2);
        }
//        List<String> termDates = contractPayConcludeF.getContractPayPlanConcludeUpdateFS().stream().map(s -> s.getTermDate().toString()).collect(Collectors.toList());
//        List<String> planId = contractPayConcludeF.getContractPayPlanConcludeUpdateFS().stream().map(s -> s.getId()).collect(Collectors.toList());
        List<String> planId = contractPayConcludeF.getPlanIdList();
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeService.getByIdList(planId);
        for(ContractPayPlanConcludeE s : contractIncomePlanConcludeVList){
            sumChooseAmount = sumChooseAmount.add(s.getPlannedCollectionAmount());
            sumSettleAmount = sumSettleAmount.add(s.getSettlementAmount());
        }
        if(plannedCollectionAmountSum.add(sumSettleAmount).compareTo(sumChooseAmount) > 0){
            throw new OwlBizException("结算总金额不能超过付款计划总金额!");
        }
//        String termDate = termDates.stream().collect(Collectors.joining(","));
        /*if(StringUtils.isNotEmpty(contractPayConcludeF.getTermDate())){
            map.setTermDate(contractPayConcludeF.getTermDate());
        }else{*/
            Set<Integer> termDates = new LinkedHashSet<>();
            for (ContractPayPlanConcludeE item : contractIncomePlanConcludeVList) {
                if (item.getTermDate() != null) {
                    termDates.add(item.getTermDate());
                }
            }
            map.setTermDate(termDates.stream().map(String::valueOf).collect(Collectors.joining(",")));
        //}
        contractPaySettlementConcludeMapper.updateById(map);

        //在删除之前先把已有的结算明细查出来
        List<ContractPaySettDetailsE> existDetailsES = contractPaySettDetailsMapper.selectList(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, map.getId())
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        //转为map
        Map<String, ContractPaySettDetailsE> existDetailsMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(existDetailsES)) {
            existDetailsMap = existDetailsES.stream().collect(Collectors.toMap(ContractPaySettDetailsE::getId, Function.identity(), (v1, v2) -> v1));
        }
        //执行删除
        contractPaySettDetailsMapper.deleteBySettlementId(map.getId());
        List<ContractPayPlanConcludeE> planFunList = contractPayPlanConcludeMapper.getFunDateList(map.getContractId(),contractPayConcludeF.getContractPaySettDetailsSaveFList().stream().map(ContractPaySettDetailsUpdateF :: getPayFundId).collect(Collectors.toList()),
                contractPayConcludeF.getContractPaySettlementPeriodSaveFList());
        List<ContractPaySettDetailsE> contractPaySettDetailsEList = Global.mapperFacade.mapAsList(contractPayConcludeF.getContractPaySettDetailsSaveFList(),ContractPaySettDetailsE.class);
        for(ContractPaySettDetailsE s : contractPaySettDetailsEList){
            if (StringUtils.isNotBlank(s.getTaxRateId())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), s.getTaxRateId());
                if (CollectionUtils.isNotEmpty(value)) {
                    s.setTaxRate(value.get(0).getName());
                }
            }
            ContractPayPlanConcludeE planFun = planFunList.stream().filter(p -> s.getPayFundId().equals(p.getContractPayFundId())).findFirst().orElse(new ContractPayPlanConcludeE());
            s.setStartDate(planFun.getCostStartTime());
            s.setEndDate(planFun.getCostEndTime());
            if(null != s.getId()){
                //说明是更新，从existDetailsMap中获取ext字段值
                s.setExtField(existDetailsMap.getOrDefault(s.getId(), new ContractPaySettDetailsE()).getExtField());
                //再执行原逻辑
                s.setId(null);
                contractPaySettDetailsMapper.insert(s);
            }else{
                //说明是新增，逻辑不变，直接插入
                s.setSettlementId(map.getId());
                contractPaySettDetailsMapper.insert(s);
            }
        }

        contractPayConcludeSettdeductionMapper.deleteBySettlementId(map.getId());
        List<ContractPayConcludeSettdeductionE> contractPayConcludeSettdeductionEList = Global.mapperFacade.mapAsList(contractPayConcludeF.getContractPayConcludeSettdeductionSaveFList(),ContractPayConcludeSettdeductionE.class);
        for(ContractPayConcludeSettdeductionE s : contractPayConcludeSettdeductionEList){
            if(null != s.getId()){
                s.setId(null);
                contractPayConcludeSettdeductionMapper.insert(s);
            }else{
                s.setSettlementId(map.getId());
                contractPayConcludeSettdeductionMapper.insert(s);
            }
        }

        //计算金额减免逻辑
        List<ContractPayConcludeSettdeductionSaveF> concludeSettdeductionSaveList =
                Global.mapperFacade.mapAsList(contractPayConcludeF.getContractPayConcludeSettdeductionSaveFList(),
                        ContractPayConcludeSettdeductionSaveF.class);
        extractedReductionAmount(concludeSettdeductionSaveList, contractIncomePlanConcludeVList);
        settlementPlanRelationMapper.deleteBySettlementId(contractPayConcludeF.getId());
        settlementPeriodMapper.deleteBySettlementId(contractPayConcludeF.getId());
        if (CollectionUtils.isNotEmpty(contractPayConcludeF.getContractPaySettlementPeriodSaveFList())){
            List<ContractPayConcludeSettlementPeriodE> periodList = Lists.newArrayList();
            for (ContractPaySettlementPeriodF periodF : contractPayConcludeF.getContractPaySettlementPeriodSaveFList()) {
                ContractPayConcludeSettlementPeriodE periodE = new ContractPayConcludeSettlementPeriodE();
                periodE.setSettlementId(map.getId());
                periodE.setStartDate(periodF.getStartDate());
                periodE.setEndDate(periodF.getEndDate());
                periodList.add(periodE);
            }
            settlementPeriodMapper.insertBatch(periodList);

        }
        if (CollectionUtils.isNotEmpty(contractPayConcludeF.getPlanIdList())){
            List<ContractPayConcludeSettlementPlanRelationE> relationList = Lists.newArrayList();
            for (String curPlanId : contractPayConcludeF.getPlanIdList()) {
                ContractPayConcludeSettlementPlanRelationE relationE = new ContractPayConcludeSettlementPlanRelationE();
                relationE.setPlanId(curPlanId);
                relationE.setSettlementId(map.getId());
                relationList.add(relationE);
            }
            settlementPlanRelationMapper.insertBatch(relationList);
        }

        dealSettlementContractSnapshot(contractPayConcludeF.getId(), contractPayConcludeF.getContractId(),true);
        handlePidPaySettlementInfo(map1.getPid());
    }


    public void handlePidPaySettlementInfo(String pid) {
        ContractPaySettlementConcludeE setPid = contractPaySettlementConcludeMapper.selectById(pid);
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getPid, pid)
                .eq(ContractPaySettlementConcludeE::getDeleted,0);
        List<ContractPaySettlementConcludeE> contractPaySettlementConcludeEList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        BigDecimal planAmount = BigDecimal.ZERO;
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal dueAmount = BigDecimal.ZERO;
        for(ContractPaySettlementConcludeE s : contractPaySettlementConcludeEList){
            planAmount = planAmount.add(s.getPlannedCollectionAmount());
            invoiceAmount = invoiceAmount.add(s.getInvoiceApplyAmount());
            payAmount = payAmount.add(s.getPaymentAmount());
            dueAmount = dueAmount.add(s.getDeductionAmount());
        }
        setPid.setDeductionAmount(dueAmount);
        setPid.setInvoiceApplyAmount(invoiceAmount);
        setPid.setPlannedCollectionAmount(planAmount);
        setPid.setPaymentAmount(payAmount);
        contractPaySettlementConcludeMapper.updateById(setPid);
    }

    /**
     *
     * @param id 根据Id删除
     * @return 删除结果
     */
    public boolean removeById(String id){
        ContractPaySettlementConcludeE s = contractPaySettlementConcludeMapper.selectById(id);
        String pid = s.getPid();
        contractPaySettlementConcludeMapper.deleteById(id);
        handlePidPaySettlementInfo(pid);
        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPaySettlementConcludeE::getPid, pid)
                .eq(ContractPaySettlementConcludeE::getDeleted,0);
        List<ContractPaySettlementConcludeE> concludeEList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        if(!ObjectUtils.isNotEmpty(concludeEList)){
            contractPaySettlementConcludeMapper.deleteById(pid);
        }
        // 删除计量周期表
        settlementPeriodMapper.deleteBySettlementId(id);
        // 删除成本预估计划关联表
        settlementPlanRelationMapper.deleteBySettlementId(id);
        return true;
    }

    /**
     *
     * @param id 根据Id提交
     */
    @Transactional(rollbackFor = Exception.class)
    public ContractPayProcessV sumbitId(String id){
        ContractPayProcessV result = new ContractPayProcessV();
        ContractPaySettlementConcludeE map = contractPaySettlementConcludeMapper.selectById(id);
        if (!ReviewStatusEnum.待提交.getCode().equals(map.getReviewStatus())) {
            throw new OwlBizException("流程已发起，请勿重复提交!");
        }
        LambdaQueryWrapper<ContractProcessRecordE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractProcessRecordE::getContractId, map.getId())
                .eq(ContractProcessRecordE::getDeleted, 0);
        List<ContractProcessRecordE> processList = contractProcessRecordMapper.selectList(queryWrappers);
        if(CollectionUtils.isNotEmpty(processList) && !userId().equals(processList.get(0).getOperator())){
            throw new OwlBizException("流程发起人为“"+processList.get(0).getOperatorName()+"”，非流程发起人不允许发起流程！");
        }
        //查询合同信息
        ContractPayConcludeE payContract = contractPayConcludeMapper.queryByContractId(map.getContractId());

        ContractPayProcessV payProcessV = checkSettlentPeriod(id, payContract);
        if(payProcessV.getCode().equals("500")){
            return payProcessV;
        }
        int type = payProcessV.getType() ;
        extracted(id);
        map.setReviewStatus(1);
        isSumbits(map);
        contractPaySettlementConcludeMapper.updateById(map);
        try {
            Boolean isSync = Boolean.TRUE;
            //查询当前结算单结算周期
            List<ContractPayConcludeSettlementPeriodE> periodList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                    .eq(ContractPayConcludeSettlementPeriodE::getSettlementId, id));
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryYjWrapper = new LambdaQueryWrapper<>();
            queryYjWrapper.in(ContractPaySettlementConcludeE::getContractId, payContract.getPid())
                    .ne(ContractPaySettlementConcludeE::getPid,0)
                    .eq(ContractPaySettlementConcludeE::getDeleted,0);
            List<ContractPaySettlementConcludeE> settlemenYJList = contractPaySettlementConcludeMapper.selectList(queryYjWrapper);
            if(CollectionUtils.isNotEmpty(settlemenYJList)){
                List<ContractPayConcludeSettlementPeriodE> periodYJList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                        .in(ContractPayConcludeSettlementPeriodE::getSettlementId, settlemenYJList.stream().map(ContractPaySettlementConcludeE :: getId).collect(Collectors.toList())));
                Set<String> nkDateSet = periodList.stream()
                        .map(period -> period.getStartDate() + "_" + period.getEndDate())
                        .collect(Collectors.toSet());
                List<ContractPayConcludeSettlementPeriodE> havaNk = periodYJList.stream()
                        .filter(period -> nkDateSet.contains(
                                period.getStartDate() + "_" + period.getEndDate()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(havaNk)){
                    isSync = Boolean.FALSE;
                }
            }
            //NK结算单，发起流程后同步生成YJ结算单
            if(ContractConcludeEnum.SETTLEMENT_NK.getCode() == type && isSync){
                result.setContractId(payContract.getPid());
                result.setCurContractName(payContract.getName());
                String userName = userName();
                LocalDateTime dateTime = LocalDateTime.now();
                String userId = userId();
                //--拷贝结算单pid为“0”数据
                LambdaQueryWrapper<ContractPaySettlementConcludeE> queryMainWrapper = new LambdaQueryWrapper<>();
                queryMainWrapper.in(ContractPaySettlementConcludeE::getContractId, payContract.getPid())
                        .eq(ContractPaySettlementConcludeE::getPid,0)
                        .eq(ContractPaySettlementConcludeE::getDeleted,0);
                ContractPaySettlementConcludeE settlemenMain = contractPaySettlementConcludeMapper.selectOne(queryMainWrapper);
                if(Objects.isNull(settlemenMain)){
                    LambdaQueryWrapper<ContractPaySettlementConcludeE> queryNKWrapper = new LambdaQueryWrapper<>();
                    queryNKWrapper.in(ContractPaySettlementConcludeE::getContractId, payContract.getId())
                            .eq(ContractPaySettlementConcludeE::getPid,0)
                            .eq(ContractPaySettlementConcludeE::getDeleted,0);
                    ContractPaySettlementConcludeE settlemenNk = contractPaySettlementConcludeMapper.selectOne(queryNKWrapper);
                    settlemenMain = new ContractPaySettlementConcludeE();
                    BeanUtils.copyProperties(settlemenNk,settlemenMain);
                    settlemenMain.setId(null);
                    settlemenMain.setContractId(payContract.getPid());
                    settlemenMain.setMainId(settlemenNk.getId());
                    this.save(settlemenMain);
                }
                String settlePid = settlemenMain.getId();

                LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(ContractPaySettlementConcludeE::getContractId, map.getContractId())
                        .eq(ContractPaySettlementConcludeE::getId, id)
                        .eq(ContractPaySettlementConcludeE::getDeleted,0);
                ContractPaySettlementConcludeE settlementNK = contractPaySettlementConcludeMapper.selectOne(queryWrapper);
                //--拷贝结算单pid不为“0”数据
                settlementNK.setMainId(settlementNK.getId());
                settlementNK.setId(null);
                settlementNK.setPid(settlePid);
                settlementNK.setContractId(payContract.getPid());
                settlementNK.setCreator(userId);
                settlementNK.setCreatorName(userName);
                settlementNK.setGmtCreate(dateTime);
                settlementNK.setOperator(userId);
                settlementNK.setOperatorName(userName);
                settlementNK.setGmtModify(dateTime);
                settlementNK.setReviewStatus(ReviewStatusEnum.待提交.getCode());
                this.save(settlementNK);
                result.setSettlementId(settlementNK.getId());

                List<ContractPayFundE> funList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                        .orderByDesc(ContractPayFundE.GMT_CREATE)
                        .eq(ContractPayFundE.TENANT_ID, tenantId())
                        .eq(ContractPayFundE.CONTRACT_ID, payContract.getId())
                        .eq(ContractPayFundE.DELETED, 0)
                );
                Map<String, String> funMainMap = funList.stream()
                        .collect(Collectors.toMap(
                                ContractPayFundE::getId,
                                ContractPayFundE::getMainId));


                //-------拷贝结算明细数据-------
                List<ContractPaySettDetailsE> settDetailsList = contractPaySettDetailsMapper.selectList(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                        .eq(ContractPaySettDetailsE::getSettlementId, id).eq(ContractPaySettDetailsE :: getDeleted, 0));
                if(CollectionUtils.isNotEmpty(settDetailsList)){
                    settDetailsList.forEach(x->{
                        x.setSettlementId(settlementNK.getId());
                        x.setId(null);
                        x.setCreator(userId);
                        x.setCreatorName(userName);
                        x.setGmtCreate(dateTime);
                        x.setOperator(userId);
                        x.setOperatorName(userName);
                        x.setGmtModify(dateTime);
                        x.setPayFundId(funMainMap.get(x.getPayFundId()));
                    });
                    contractPaySettDetailsService.saveBatch(settDetailsList);
                }

                //-------拷贝扣款明细数据-------
                List<ContractPayConcludeSettdeductionE> settdeductionEList = contractPayConcludeSettdeductionMapper.selectList(Wrappers.<ContractPayConcludeSettdeductionE>lambdaQuery()
                        .eq(ContractPayConcludeSettdeductionE::getSettlementId, id).eq(ContractPayConcludeSettdeductionE :: getDeleted, 0));
                if(CollectionUtils.isNotEmpty(settdeductionEList)){
                    settdeductionEList.forEach(x->{
                        x.setSettlementId(settlementNK.getId());
                        x.setId(null);
                        x.setCreator(userId);
                        x.setCreatorName(userName);
                        x.setGmtCreate(dateTime);
                        x.setOperator(userId);
                        x.setOperatorName(userName);
                        x.setGmtModify(dateTime);
                    });
                    contractPayConcludeSettdeductionService.saveBatch(settdeductionEList);
                }

                //-------拷贝结算单周期数据-------
                if(CollectionUtils.isNotEmpty(periodList)){
                    periodList.forEach(x->{
                        x.setSettlementId(settlementNK.getId());
                        x.setId(null);
                    });
                    contractPayConcludeSettlementPeriodMapper.insertBatch(periodList);
                }

                //-------拷贝结算单与计划关联关系-------
                List<ContractPayConcludeSettlementPlanRelationE> planRelationList = settlementPlanRelationMapper
                        .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                                .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, id));
                if(CollectionUtils.isNotEmpty(planRelationList)){
                    LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanNKWrapper = new LambdaQueryWrapper<>();
                    queryPlanNKWrapper.eq(ContractPayPlanConcludeE::getContractId, payContract.getId())
                            .in(ContractPayPlanConcludeE :: getId, planRelationList.stream().map(ContractPayConcludeSettlementPlanRelationE :: getPlanId).collect(Collectors.toList()))
                            .eq(ContractPayPlanConcludeE::getDeleted,0);
                    List<ContractPayPlanConcludeE> concludePlanNKList = contractPayPlanConcludeMapper.selectList(queryPlanNKWrapper);
                    if(CollectionUtils.isNotEmpty(concludePlanNKList)){
                        LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                        queryPlanWrapper.eq(ContractPayPlanConcludeE::getContractId, payContract.getPid())
                                .in(ContractPayPlanConcludeE :: getId, concludePlanNKList.stream().map(ContractPayPlanConcludeE :: getMainId).collect(Collectors.toList()))
                                .eq(ContractPayPlanConcludeE::getDeleted,0);
                        List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
                        if(CollectionUtils.isNotEmpty(concludePlanList)){
                            List<ContractPayConcludeSettlementPlanRelationE> relationEList = new ArrayList<>();
                            concludePlanList.forEach(x->{
                                ContractPayConcludeSettlementPlanRelationE relationE = new ContractPayConcludeSettlementPlanRelationE();
                                relationE.setSettlementId(settlementNK.getId());
                                relationE.setPlanId(x.getId());
                                relationE.setId(null);
                                relationEList.add(relationE);
                            });
                            settlementPlanRelationMapper.insertBatch(relationEList);
                        }
                    }
                }
            }
            if (devFlag == 1) {
                return result;
            }
            result.setOldString(createProcess(map,type));
            return result;
        } catch (Exception e) {
            log.info("结算审批流程发起异常：{}，{}",e.getMessage(),e);
            throw new OwlBizException("OA流程发起超时，请稍后重试！");
        }
    }

    private ContractPayProcessV checkSettlentPeriod(String id, ContractPayConcludeE payContract) {
        ContractPayProcessV result = new ContractPayProcessV();
        int type = ContractConcludeEnum.SETTLEMENT_FUND.getCode();
        if(Arrays.asList(NkStatusEnum.已开启.getCode(), NkStatusEnum.关闭中.getCode()).contains(payContract.getNkStatus()) && payContract.getDeleted().equals(1)){
            type = ContractConcludeEnum.SETTLEMENT_NK.getCode();
        }
        //若为YJ结算审批。且含有NK合同，则校验该结算单结算周期，在NK结算中是否已审批通过
        if(ContractConcludeEnum.SETTLEMENT_FUND.getCode() ==  type && Arrays.asList(NkStatusEnum.已开启.getCode(), NkStatusEnum.关闭中.getCode()).contains(payContract.getNkStatus())){
            List<ContractPayConcludeSettlementPeriodE> periodList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                    .eq(ContractPayConcludeSettlementPeriodE::getSettlementId, id));
            ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(payContract.getId());
            //查询主合同有无审批中及审批通过的结算单
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPaySettlementConcludeE::getContractId, nkContract.getId())
                    .notIn(ContractPaySettlementConcludeE::getPid,0)
                    .eq(ContractPaySettlementConcludeE::getDeleted,0)
                    .in(ContractPaySettlementConcludeE :: getReviewStatus , ReviewStatusEnum.已通过.getCode(),ReviewStatusEnum.审批中.getCode());
            List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(settlementList)){
                result.setCode("500");
                result.setMessage("该合同未创建NK结算审批，请NK结算流程发起后，再发起流程");
                return result;
            }
            List<ContractPayConcludeSettlementPeriodE> periodNkList = contractPayConcludeSettlementPeriodMapper.selectList(Wrappers.<ContractPayConcludeSettlementPeriodE>lambdaQuery()
                    .in(ContractPayConcludeSettlementPeriodE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE::getId).collect(Collectors.toList())));
            Set<String> nkDateSet = periodNkList.stream()
                    .map(period -> period.getStartDate() + "_" + period.getEndDate())
                    .collect(Collectors.toSet());
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Set<String> notNk = periodList.stream()
                    .filter(period -> !nkDateSet.contains(
                            period.getStartDate() + "_" + period.getEndDate()))
                    .collect(Collectors.toList()).stream().map(period -> sdf1.format(period.getStartDate()) + "至" + sdf1.format(period.getEndDate()))
                    .collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(notNk)){
                result.setCode("500");
                result.setMessage("该结算单中结算周期【"+String.join(", ", notNk)+"】未在NK结算中发起流程，请NK结算发起流程后，再发起流程");
                return result;
            }
        }
        result.setType( type);
        return result;
    }

    //校验附件（合同数量签认单、合同结算表）是否存在
    private void extracted(String id) {

        LambdaQueryWrapper<AttachmentE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttachmentE::getBusinessId, id)
                .eq(AttachmentE::getDeleted, 0)
                .eq(AttachmentE::getBusinessType, FileSaveTypeEnum.其他说明文件.getCode())
                .in(AttachmentE::getType, 2, 3);
        List<AttachmentE> attachmentEList = attachmentMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(attachmentEList)){
            throw new OwlBizException("请上传用印后的合同结算表及合同结算数量确认单，再发起结算审批流程");
        }
        List<AttachmentE> attachmentEList2 = attachmentEList.stream().filter(e -> e.getType() == 2).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(attachmentEList2)){
            throw new OwlBizException("请上传用印后的合同结算表及合同结算数量确认单，再发起结算审批流程");
        }
        List<AttachmentE> attachmentEList3 = attachmentEList.stream().filter(e -> e.getType() == 3).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(attachmentEList3)){
            throw new OwlBizException("请上传用印后的合同结算表及合同结算数量确认单，再发起结算审批流程");
        }
    }

    /**
     *
     * @param id 根据Id反审核
     */
    public void returnId(String id){
        ContractPaySettlementConcludeE map = contractPaySettlementConcludeMapper.selectById(id);
        if(map.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0){
            throw BizException.throw404("已存在付款数据，不允许反审核!");
        }
        if(map.getInvoiceApplyAmount().compareTo(BigDecimal.ZERO) > 0){
            throw BizException.throw404("已存在收票数据，不允许反审核!");
        }
        map.setReviewStatus(0);
        contractPaySettlementConcludeMapper.updateById(map);
    }

    private QueryWrapper<ContractPaySettlementConcludePageF> conditionPage(QueryWrapper<ContractPaySettlementConcludePageF> queryModel, String tenantId) {
        queryModel.eq("cck.deleted", 0);
        queryModel.eq("cck.tenantId", tenantId);
        return queryModel;
    }

    /**
     *
     * @param contractSettlementsBillF 参数
     */
    @Transactional(rollbackFor = {Exception.class})
    public String invoice(ContractSettlementsBillF contractSettlementsBillF){
        ContractSettlementsBillE map = Global.mapperFacade.map(contractSettlementsBillF, ContractSettlementsBillE.class);
        if(ObjectUtils.isNotEmpty(contractSettlementsBillF.getAttachment())){
            map.setAttachments(JSON.toJSONString(contractSettlementsBillF.getAttachment()));
        }
        map.setTenantId(tenantId());
        List<BigDecimal> sumInvoiceApplyAmount = contractSettlementsBillF.getContractSettlementsBillDetailsSaveFList().stream().map(s -> s.getAmount()).collect(Collectors.toList());
        BigDecimal sumInvoiceApplyAmount1 = sumInvoiceApplyAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        contractSettlementsBillF.setAmount(sumInvoiceApplyAmount1);
        map.setAmount(sumInvoiceApplyAmount1);
        ContractPaySettlementConcludeE map1 = contractPaySettlementConcludeMapper.selectById(contractSettlementsBillF.getSettlementId());
        map1.setInvoiceApplyAmount(map1.getInvoiceApplyAmount().add(contractSettlementsBillF.getAmount()));
        ContractPayConcludeE map2 = contractPayConcludeMapper.selectById(map1.getContractId());
        if(map1.getInvoiceApplyAmount().compareTo(map1.getPlannedCollectionAmount()) > 0){
            throw BizException.throw404("开票金额不能大于结算金额!");
        }else if(map1.getInvoiceApplyAmount().compareTo(map1.getPlannedCollectionAmount()) == 0){
            map1.setInvoiceStatus(1);
        }
        BigDecimal calculateAmount = BigDecimal.ZERO;
        BigDecimal sumAmount = contractSettlementsBillF.getAmount();
        List<Integer> termDates = Arrays.stream(map1.getTermDate().split(",")).map(Integer::valueOf).sorted().collect(Collectors.toList());
        for(Integer s : termDates){
            List<ContractPayPlanConcludeV> contractPayPlanConcludeVList = contractPayPlanConcludeMapper.getPayPlanByTermDate(map1.getContractId(), s);
            if(ObjectUtils.isNotEmpty(contractPayPlanConcludeVList)){
                ContractPayPlanConcludeV sb = contractPayPlanConcludeVList.get(0);
                ContractPayPlanConcludeE bs = contractPayPlanConcludeMapper.selectById(sb.getId());
                if(sb.getSettlementAmount().compareTo(sb.getInvoiceApplyAmount()) == 0){
                    continue;
                }else if(sb.getNoInvoiceApplyAmount().compareTo(BigDecimal.ZERO) > 0){
                    if(sumAmount.compareTo(sb.getNoInvoiceApplyAmount()) > 0){
                        bs.setInvoiceApplyAmount(bs.getInvoiceApplyAmount().add(sb.getNoInvoiceApplyAmount()));
                        bs.setInvoiceStatus(1);
                        sumAmount = sumAmount.subtract(sb.getNoInvoiceApplyAmount());
                    }else if(sumAmount.compareTo(sb.getNoInvoiceApplyAmount()) == 0){
                        bs.setInvoiceApplyAmount(bs.getInvoiceApplyAmount().add(sb.getNoInvoiceApplyAmount()));
                        bs.setInvoiceStatus(1);
                        contractPayPlanConcludeMapper.updateById(bs);
                        break;
                    }else if(sumAmount.compareTo(sb.getNoInvoiceApplyAmount()) < 0){
                        bs.setInvoiceApplyAmount(bs.getInvoiceApplyAmount().add(sumAmount));
                    }
                }
                contractPayPlanConcludeMapper.updateById(bs);
            }
        }
        map2.setInvoiceAmount(map2.getInvoiceAmount().add(contractSettlementsBillF.getAmount()));
        contractPayConcludeMapper.updateById(map2);
        ContractSettlementsBillItemSaveF sk = new ContractSettlementsBillItemSaveF();
        BeanUtils.copyProperties(contractSettlementsBillF,sk);
        if(StringUtils.isNotBlank(contractSettlementsBillF.getChangeId())){
            sk.setChangeName(InvoiceChangeEnum.parseName(Integer.parseInt(contractSettlementsBillF.getChangeId())));
        }
        for(ContractSettlementsBillCalculateSaveF s : contractSettlementsBillF.getContractSettlementsBillCalculateSaveFList()) {
            calculateAmount = calculateAmount.add(s.getAmount());
        }
        for(ContractSettlementsBillDetailsSaveF s : contractSettlementsBillF.getContractSettlementsBillDetailsSaveFList()) {
            List<ContractSettlementsBillDetailsV> sl = contractSettlementsBillDetailsService.getIsEeistBillNo(s.getBillNum());
            if (ObjectUtils.isNotEmpty(sl)) {
                throw BizException.throw404("发票号{" + s.getBillNum() + "}已经存在！");
            }
            if (s.getAmount().compareTo(s.getAmountRate()) < 0) {
                throw BizException.throw404("收票含税金额不能小于税额！");
            }
        }
        if(contractSettlementsBillF.getAmount().compareTo(calculateAmount) < 0){
            throw BizException.throw404("收票明细收票含税金额不能小于结算含税金额！");
        }
        Long sc = gatherAddBatch(contractSettlementsBillF,map1.getPayFundNumber());
        map.setPaymentid(sc);
        contractPayBillMapper.insert(map);
        sk.setBillId(map.getId());
        for(ContractSettlementsBillCalculateSaveF s : contractSettlementsBillF.getContractSettlementsBillCalculateSaveFList()){
            s.setBillId(map.getId());
            contractSettlementsBillCalculateService.save(s);
        }
        for(ContractSettlementsBillDetailsSaveF s : contractSettlementsBillF.getContractSettlementsBillDetailsSaveFList()){
            s.setBillId(map.getId());
            if(createProcessFlag == 0){
                s.setReviewStatus(1);
            }else{
                s.setReviewStatus(2);
            }
            contractSettlementsBillDetailsService.save(s);
        }
        contractSettlementsBillItemService.save(sk);
        contractPaySettlementConcludeMapper.updateById(map1);
        handlePidPaySettlementInfo(map1.getPid());
        handlePayPlanInvoceAndPayState(map1.getId());
        return map.getId();
    }

    private Long gatherAddBatch(ContractSettlementsBillF contractSettlementsBillF, String payFundNumber){
        ContractInvoiceInfoF contractInvoiceInfoF = new ContractInvoiceInfoF();
        List<MeasurementDetailF> detailFList = new ArrayList<>();
        List<InvoiceZJF> invoiceZJFList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(contractSettlementsBillF.getItemCode())){
            contractInvoiceInfoF.setPaymentId(contractSettlementsBillF.getItemCode().get(contractSettlementsBillF.getItemCode().size() - 1));
            contractInvoiceInfoF.setPaymentName(externalFeignClient.mdm17query(contractInvoiceInfoF.getPaymentId()));
        }
        if(ObjectUtils.isNotEmpty(contractSettlementsBillF.getBussTypeCode())){
            contractInvoiceInfoF.setBusinessCode(contractSettlementsBillF.getBussTypeCode().get(contractSettlementsBillF.getBussTypeCode().size() - 1));
            contractInvoiceInfoF.setBusinessName(externalFeignClient.mdm16query(contractInvoiceInfoF.getBusinessCode()));
        }

        if(ObjectUtils.isNotEmpty(contractSettlementsBillF.getAttachment())){
            contractInvoiceInfoF.setFiles(JSON.toJSONString(contractSettlementsBillF.getAttachment()));
        }

        contractInvoiceInfoF.setChangeType(contractSettlementsBillF.getChangeId());
        ContractPayConcludeE map2 = contractPayConcludeMapper.selectById(contractSettlementsBillF.getContractId());
        contractInvoiceInfoF.setSettlementNo(payFundNumber);
        contractInvoiceInfoF.setApproveState(createProcessFlag);
        contractInvoiceInfoF.setApproveRule(createProcessProjectId);
        contractInvoiceInfoF.setCommunityId(map2.getCommunityId());
        contractInvoiceInfoF.setCommunityName(map2.getCommunityName());
        contractInvoiceInfoF.setContractId(contractSettlementsBillF.getContractId());
        contractInvoiceInfoF.setContractNo(map2.getConmaincode());
        contractInvoiceInfoF.setCostCenterId(Long.parseLong(map2.getCostCenterId()));
        contractInvoiceInfoF.setCostCenterName(map2.getCostCenterName());
        contractInvoiceInfoF.setPayAmount(contractSettlementsBillF.getAmount().multiply(new BigDecimal("100")).longValue());
        contractInvoiceInfoF.setSupplier(map2.getOppositeOne());
        contractInvoiceInfoF.setSupplierId(map2.getOppositeOneId());
        contractInvoiceInfoF.setTaxAmount(1l);
        contractInvoiceInfoF.setWriteOffInfo(null);
        for(ContractSettlementsBillCalculateSaveF s : contractSettlementsBillF.getContractSettlementsBillCalculateSaveFList()){
            MeasurementDetailF measurementDetailF = new MeasurementDetailF();
            measurementDetailF.setTaxRate(new BigDecimal(s.getTaxRate().replace("%","")));
            measurementDetailF.setContractItem(s.getChargeItem());
            measurementDetailF.setChargeItemId(s.getChargeItemId());
            measurementDetailF.setContractItem(s.getType());
            measurementDetailF.setTaxIncludedAmount(s.getAmount().multiply(new BigDecimal("100")).longValue());
            measurementDetailF.setTaxExcludedAmount(s.getAmount().subtract(s.getTaxRateAmount()).multiply(new BigDecimal("100")).longValue());
            detailFList.add(measurementDetailF);
        }
        for(ContractSettlementsBillDetailsSaveF s : contractSettlementsBillF.getContractSettlementsBillDetailsSaveFList()){
            InvoiceZJF invoiceZJF = new InvoiceZJF();
            invoiceZJF.setInvoiceCode(s.getBillCode());
            invoiceZJF.setInvoiceNo(s.getBillNum());
            invoiceZJF.setInvoiceDate(s.getBillDate());
            invoiceZJF.setInvoiceType(s.getBillType().toString());
            invoiceZJF.setPayAmount(s.getAmount().multiply(new BigDecimal("100")).longValue());
            invoiceZJF.setTaxAmount(s.getAmountRate().multiply(new BigDecimal("100")).longValue());
            invoiceZJFList.add(invoiceZJF);
        }
        contractInvoiceInfoF.setInvoiceZJFList(invoiceZJFList);
        contractInvoiceInfoF.setDetailFList(detailFList);
        log.info("调到财务中台报文:" + JSONObject.toJSONString(contractInvoiceInfoF));
        return financeFeignClient.acceptContractInvoice(contractInvoiceInfoF);
    }

    public static void main(String[] args) {
        List<ZJFileVo> ss = new ArrayList<>();
        ZJFileVo sb = new ZJFileVo();
        sb.setFileId("503fe7df97514c9aaa2f6380a054d450");
        sb.setSize(255403l);
        sb.setFileKey("tmp/13554968497211/20240529/1716949339509104.pdf");
        sb.setName("多票据_接口文档.pdf");
        ss.add(sb);
//        JSONObject s = JSONObject.parseObject(JSON.toJSONString(ss));
        System.out.printf(JSON.toJSONString(ss));
    }


    public PageV<ContractPayBillV> pageForContractDetailBill(PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        Page<ContractPayPlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.contractId");
            }
        }
        QueryWrapper<ContractPayPlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        queryModel.eq("cc.deleted", 0);
        queryModel.eq("cc.tenantId", tenantId());
        IPage<ContractPayBillV> pageList = contractPayBillMapper.settlementsBillForContractDetail(pageF, queryModel);
        return PageV.of(form, pageList.getTotal(), pageList.getRecords());
    }


    private boolean checkInvoiceInfo(ContractSettlementsBillF contractSettlementsBillF){
        InvoiceBaseInfoF invoiceBaseInfoF = new InvoiceBaseInfoF();
        invoiceBaseInfoF.setInvoicecode(contractSettlementsBillF.getBillCode());
        invoiceBaseInfoF.setInvoicenumber(contractSettlementsBillF.getBillNum());
        EsbRetrunInfoV s = externalFeignClient.checkInvoice(invoiceBaseInfoF);
        if(ObjectUtils.isEmpty(s)){
            throw new OwlBizException("发票验重接口有误，请检查！");
        }
        if(!s.getCode().equals(1)){
            throw new OwlBizException(s.getMessage());
        }


        return false;
    }

    private boolean isSumbits(ContractPaySettlementConcludeE map){
        if(StringUtils.isEmpty(map.getTermDate())){
            throw new OwlBizException("该结算单结算计划使用期数字段为空！");
        }
        List<Integer> termDates = Arrays.stream(map.getTermDate().split(",")).map(Integer::valueOf).sorted().collect(Collectors.toList());
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal planTotal = BigDecimal.ZERO;
        for(Integer s : termDates){
            LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(ContractPaySettlementConcludeE::getContractId, map.getContractId())
                    .eq(ContractPaySettlementConcludeE::getDeleted, 0)
                    .notIn(ContractPaySettlementConcludeE::getReviewStatus,0)
                    .like(ContractPaySettlementConcludeE::getTermDate,s);
            List<ContractPaySettlementConcludeE> contractSettlementsFundES = contractPaySettlementConcludeMapper.selectList(queryWrapper2);
            if(ObjectUtils.isNotEmpty(contractSettlementsFundES)){
                List<BigDecimal> sk = contractSettlementsFundES.stream().map(sb -> sb.getPlannedCollectionAmount()).collect(Collectors.toList());
                total = sk.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            List<ContractPayPlanConcludeV> contractPayPlanConcludeVList = contractPayPlanConcludeMapper.getPayPlanByTermDate(map.getContractId(), s);
            if(ObjectUtils.isNotEmpty(contractPayPlanConcludeVList)){
                planTotal = planTotal.add(contractPayPlanConcludeVList.get(0).getPlannedCollectionAmount());
            }

        }
//        if(map.getPlannedCollectionAmount().add(total).compareTo(planTotal) > 0){
//            throw new OwlBizException("当前期数已审批金额大于计划金额，不允许提交");
//        }
        return Boolean.TRUE;

    }

    /**
     *
     * @param contractSettlementsFundF 参数
     */
    public String setFund(ContractSettlementsFundF contractSettlementsFundF){
        ContractSettlementsFundE map = Global.mapperFacade.map(contractSettlementsFundF, ContractSettlementsFundE.class);
        map.setTenantId(tenantId());
        map.setPayNotecode("ZC" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date()));
        ContractPaySettlementConcludeE map1 = contractPaySettlementConcludeMapper.selectById(contractSettlementsFundF.getSettlementId());
        ContractPayConcludeE map2 = contractPayConcludeMapper.selectById(map1.getContractId());
        map1.setPaymentAmount(map1.getPaymentAmount().add(contractSettlementsFundF.getAmount()));
        if(map1.getPaymentAmount().compareTo(map1.getPlannedCollectionAmount()) > 0){
            throw BizException.throw404("付款金额不能大于结算金额!");
        }else if(map1.getPaymentAmount().compareTo(map1.getPlannedCollectionAmount()) == 0){
            map1.setPaymentStatus(1);
        }
        BigDecimal sumAmount = contractSettlementsFundF.getAmount();
        // 根据relation表的plan_id获取plan
        LambdaQueryWrapper<ContractPayConcludeSettlementPlanRelationE> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, map.getId());
        List<ContractPayConcludeSettlementPlanRelationE> relationList =  settlementPlanRelationMapper.selectList(queryWrapper2);
        if (ObjectUtils.isEmpty(relationList)){
            return "无关联的结算计划";
        }
        List<String> planIdList = relationList.stream().map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(planIdList)){
            return "结算计划不存在";
        }
        for(String planId : planIdList){
            ContractPayPlanConcludeE contractPayPlanConcludeVList = contractPayPlanConcludeMapper.selectById(planId);

            if(Objects.nonNull(contractPayPlanConcludeVList)){
                ContractPayPlanConcludeE bs = contractPayPlanConcludeVList;
                BigDecimal noPaymentAmount = bs.getPlannedCollectionAmount().subtract(bs.getPaymentAmount());
                if(map1.getSettleStatus().equals(2)){
                    bs.setPlanStatus(2);
                }
                if(bs.getSettlementAmount().compareTo(bs.getPaymentAmount()) == 0){
                    continue;
                }else if(noPaymentAmount.compareTo(BigDecimal.ZERO) > 0){
                    if(sumAmount.compareTo(noPaymentAmount) > 0){
                        bs.setPaymentAmount(bs.getPaymentAmount().add(noPaymentAmount));
                        bs.setNoPayAmount(BigDecimal.ZERO);;
                        sumAmount = sumAmount.subtract(noPaymentAmount);
                    }else if(sumAmount.compareTo(noPaymentAmount) == 0){
                        bs.setPaymentAmount(bs.getPaymentAmount().add(noPaymentAmount));
                        bs.setNoPayAmount(BigDecimal.ZERO);
                        contractPayPlanConcludeMapper.updateById(bs);
                        break;
                    }else if(sumAmount.compareTo(noPaymentAmount) < 0){
                        bs.setNoPayAmount(noPaymentAmount.subtract(sumAmount));
                        bs.setPaymentAmount(bs.getPaymentAmount().add(sumAmount));
                    }
                }
                contractPayPlanConcludeMapper.updateById(bs);
            }
        }
        map2.setPayAmount(map2.getPayAmount().add(contractSettlementsFundF.getAmount()));
        contractPayConcludeMapper.updateById(map2);
        contractSettlementsFundMapper.insert(map);
        contractPaySettlementConcludeMapper.updateById(map1);
        handlePidPaySettlementInfo(map1.getPid());
        handlePayPlanInvoceAndPayState(map1.getId());
        return map.getId();
    }

    public OpinionApprovalV opinionApproval(String id) {
        OpinionApprovalF opinionApprovalF = new OpinionApprovalF();
        OpinionApprovalDataF opinionApprovalDataF = new OpinionApprovalDataF();
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eq(ContractProcessRecordE::getContractId, id).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(recordE)){
            return new OpinionApprovalV();
        }
        opinionApprovalDataF.setFormdataid(id);
        opinionApprovalDataF.setRequestId(recordE.getProcessId());
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalFeignClient.opinionApproval(opinionApprovalF);
    }

    /**
     * 获取结算单打印信息
     *
     * @param id
     * @return
     */
    public ContractPaySettlementInfoV getSettleInfoV2(String id){
        ContractPaySettlementConcludeE settlement = this.getById(id);
        ContractPaySettAggregationV aggregationV = new ContractPaySettAggregationV();
        aggregationV.init();
        aggregationV.setSettlement(settlement);
        combineFundMap(aggregationV);
        combineSettDetailMap(aggregationV);
        List<ContractPayFundInfoV> payFundList = generatePayFundList(aggregationV);
        return assembleResult(aggregationV, payFundList);
    }

    /**
     * 组装收集清单数据
     *
     * @param aggregationV
     */
    private void combineFundMap(ContractPaySettAggregationV aggregationV) {
        ContractPaySettlementConcludeE settlement = aggregationV.getSettlement();
        ContractPayConcludeE isNK = contractPayConcludeMapper.queryNKContractById(settlement.getContractId());
        String contractId = settlement.getContractId();
        if(Objects.nonNull(isNK) && settlement.getContractId().equals(isNK.getId())){
            contractId = isNK.getPid();
        }
        //查询该结算单关联的合同的全部补充合同的id
        LambdaQueryWrapper<ContractPayConcludeE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractPayConcludeE::getPid, contractId)
                .eq(ContractPayConcludeE::getContractType, ContractTypeEnum.补充协议.getCode())
                .eq(ContractPayConcludeE::getDeleted, 0);
        List<String> subConcludeIds = contractPayConcludeMapper.selectList(queryWrappers)
                .stream()
                .map(ContractPayConcludeE::getId)
                .collect(Collectors.toList());
        //将该结算单关联的合同id加进去,所以subConcludeIds中至少有一个数据
        subConcludeIds.add(settlement.getContractId());
        //查询全部的清单数据
        List<ContractPayFundE> allPayFunds = contractPayFundMapper.selectList(Wrappers.<ContractPayFundE>lambdaQuery()
                .in(ContractPayFundE::getContractId, subConcludeIds)
                .eq(ContractPayFundE::getDeleted, 0));
        if (CollectionUtils.isEmpty(allPayFunds)) {
            return;
        }
        List<ContractPayFundE> pidPayFunds = allPayFunds.stream()
                .filter(fund -> settlement.getContractId().equals(fund.getContractId()))
                .collect(Collectors.toList());

        Map<String, List<ContractPayFundE>> subPayFundMap = allPayFunds.stream()
                .filter(fund -> !settlement.getContractId().equals(fund.getContractId()))
                .collect(Collectors.groupingBy(this::fundGroupKey));
        if (CollectionUtils.isNotEmpty(pidPayFunds)) {
            aggregationV.setPidPayFunds(pidPayFunds);
        }

        if(MapUtils.isNotEmpty(subPayFundMap)){
            aggregationV.setSubPayFundMap(subPayFundMap);
        }
    }

    /**
     * 组装收集结算数据
     *
     * @param aggregationV
     */
    private void combineSettDetailMap(ContractPaySettAggregationV aggregationV) {
        ContractPaySettlementConcludeE settlement = aggregationV.getSettlement();
        //查询往期结算单的id信息
        List<String> preSettIds = contractPaySettlementConcludeMapper.selectList(Wrappers.<ContractPaySettlementConcludeE>lambdaQuery()
                .select(ContractPaySettlementConcludeE::getId)
                .eq(ContractPaySettlementConcludeE::getContractId, settlement.getContractId())
                .eq(ContractPaySettlementConcludeE::getReviewStatus,ReviewStatusEnum.已通过.getCode())
                .eq(ContractPaySettlementConcludeE::getDeleted, 0)).stream()
                .map(ContractPaySettlementConcludeE::getId)
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(preSettIds)){
            List<ContractPaySettlementConcludeE> preSettlements = this.listByIds(preSettIds);
            if(CollectionUtils.isNotEmpty(preSettlements)){
                aggregationV.setPreSettlements(preSettlements);
            }
        }

        //将当前阶段单id也加进去，所以preSettIds中至少有一个数据
        preSettIds.add(settlement.getId());
        //查询全部的结算明细
        List<ContractPaySettDetailsE> allPaySettDetails = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .in(ContractPaySettDetailsE::getSettlementId, preSettIds)
                .eq(ContractPaySettDetailsE::getDeleted, 0));

        if (CollectionUtils.isEmpty(allPaySettDetails)) {
            return;
        }

        List<ContractPaySettDetailsE> currPaySettDetails = allPaySettDetails.stream()
                .filter(detail -> settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.toList());

        Map<String, List<ContractPaySettDetailsE>> currPaySettDetailMap = allPaySettDetails.stream()
                .filter(detail -> settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.groupingBy(this::settDetailGroupKey));

        Map<String, List<ContractPaySettDetailsE>> currPaySettDetailExtMap = allPaySettDetails.stream()
                .filter(detail -> settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.groupingBy(this::settDetailGroupKeyInExt));


        List<ContractPaySettDetailsE> prePaySettDetails = allPaySettDetails.stream()
                .filter(detail -> !settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.toList());

        Map<String, List<ContractPaySettDetailsE>> prePaySettDetailMap = allPaySettDetails.stream()
                .filter(detail -> !settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.groupingBy(this::settDetailGroupKey));

        Map<String, List<ContractPaySettDetailsE>> prePaySettDetailExtMap = allPaySettDetails.stream()
                .filter(detail -> !settlement.getId().equals(detail.getSettlementId()))
                .collect(Collectors.groupingBy(this::settDetailGroupKeyInExt));

        if (CollectionUtils.isNotEmpty(currPaySettDetails)) {
            aggregationV.setCurrPaySettDetails(currPaySettDetails);
        }

        if (MapUtils.isNotEmpty(currPaySettDetailMap)) {
            aggregationV.setCurrPaySettDetailMap(currPaySettDetailMap);
        }

        if (MapUtils.isNotEmpty(currPaySettDetailExtMap)) {
            aggregationV.setCurrPaySettDetailExtMap(currPaySettDetailExtMap);
        }

        if (CollectionUtils.isNotEmpty(prePaySettDetails)) {
            aggregationV.setPrePaySettDetails(prePaySettDetails);
        }

        if (MapUtils.isNotEmpty(prePaySettDetailMap)) {
            aggregationV.setPrePaySettDetailMap(prePaySettDetailMap);
        }

        if (MapUtils.isNotEmpty(prePaySettDetailExtMap)) {
            aggregationV.setPrePaySettDetailExtMap(prePaySettDetailExtMap);
        }
    }

    /**
     * 统计计算清单金额
     *
     * @param aggregationV
     * @return
     */
    private List<ContractPayFundInfoV> generatePayFundList(ContractPaySettAggregationV aggregationV) {
        log.info("当前aggregationV信息:{}", JSON.toJSONString(aggregationV));
        ContractPaySettlementConcludeE settlement = aggregationV.getSettlement();
        return aggregationV.getPidPayFunds().stream().map(pidFund -> {
            ContractPayFundInfoV fundInfoV = new ContractPayFundInfoV();
            //清单项
            fundInfoV.setType(pidFund.getType());
            //税率
            fundInfoV.setTaxRate(pidFund.getTaxRate());
            //单位
            fundInfoV.setStandard(pidFund.getStandard());
            //单价
            fundInfoV.setStandAmount(pidFund.getStandAmount());

            //获取pidFund的四元组分组key
            String groupKey = this.fundGroupKey(pidFund);
            //获取pidFund的五元组分组key
            String groupKeyExt = this.fundGroupKeyInExt(pidFund);
            log.info("清单{}的groupKey:{},groupKeyExt:{}",pidFund.getId(),groupKey,groupKeyExt);
            //获取该组下，补充合同的清单集合
            List<ContractPayFundE> funds = aggregationV.getSubPayFundMap().getOrDefault(groupKey, new ArrayList<>());

            //补充合同-数量
            fundInfoV.setChangeNum(funds.stream().map(ContractPayFundE::getAmountNum).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            //补充合同-金额
            fundInfoV.setChangeAmount(funds.stream().map(ContractPayFundE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            fundInfoV.setChangeAmountString(ObjectUtils.isEmpty(fundInfoV.getChangeAmount()) ? null : fundInfoV.getChangeAmount().toString());
            //"本期"，先根据清单id匹配对应的结算明细，若不为空，则使用匹配出来的结算明细统计，否则维持原逻辑走组映射
            List<ContractPaySettDetailsE> currDetails = aggregationV.getCurrPaySettDetails().stream()
                    .filter(detail -> StringUtils.equals(detail.getPayFundId(), pidFund.getId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(currDetails)) {
                //本期-数量
                fundInfoV.setThisNum(currDetails.stream().map(ContractPaySettDetailsE::getAmountNum).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                //本期-金额
                fundInfoV.setThisAmount(currDetails.stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            } else {
                //本期-数量
                fundInfoV.setThisNum(aggregationV.getCurrPaySettDetailExtMap().getOrDefault(groupKeyExt, new ArrayList<>()).stream().map(ContractPaySettDetailsE::getAmountNum).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                //本期-金额
                fundInfoV.setThisAmount(aggregationV.getCurrPaySettDetailExtMap().getOrDefault(groupKeyExt, new ArrayList<>()).stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            }
            //"至上期末"，先根据清单id匹配对应的结算明细，若不为空，则使用匹配出来的结算明细统计，否则维持原逻辑走组映射
            List<ContractPaySettDetailsE> preDetails = aggregationV.getPrePaySettDetails().stream()
                    .filter(detail -> StringUtils.equals(detail.getPayFundId(), pidFund.getId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(preDetails)) {
                //至上期末统计-数量
                fundInfoV.setFromThisNum(preDetails.stream().map(ContractPaySettDetailsE::getAmountNum).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                //至上期末统计-金额
                fundInfoV.setFromThisAmount(preDetails.stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            } else {
                //至上期末统计-数量
                fundInfoV.setFromThisNum(aggregationV.getPrePaySettDetailExtMap().getOrDefault(groupKeyExt, new ArrayList<>()).stream().map(ContractPaySettDetailsE::getAmountNum).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                //至上期末统计-金额
                fundInfoV.setFromThisAmount(aggregationV.getPrePaySettDetailExtMap().getOrDefault(groupKeyExt, new ArrayList<>()).stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            }
            fundInfoV.setFromThisNumString(ObjectUtils.isEmpty(fundInfoV.getFromThisNum()) ? null : fundInfoV.getFromThisNum().toString());
            fundInfoV.setFromThisAmountString(ObjectUtils.isEmpty(fundInfoV.getFromThisAmount()) ? null : fundInfoV.getFromThisAmount().toString());
            //至本期末统计-数量
            fundInfoV.setToThisNum(fundInfoV.getFromThisNum().add(fundInfoV.getThisNum()).setScale(2, RoundingMode.HALF_UP));
            //至本期末统计-金额
            fundInfoV.setToThisAmount(fundInfoV.getFromThisAmount().add(fundInfoV.getThisAmount()).setScale(2, RoundingMode.HALF_UP));
            //原合同数量
            fundInfoV.setNum(pidFund.getAmountNum().subtract(fundInfoV.getChangeNum()).setScale(2, RoundingMode.HALF_UP));
            //原合同金额
            fundInfoV.setAmount(pidFund.getAmount().subtract(fundInfoV.getChangeAmount()).setScale(2, RoundingMode.HALF_UP));
            return fundInfoV;
        }).collect(Collectors.toList());
    }

    /**
     * 统计计算汇总金额
     *
     * @param aggregationV
     * @param payFundList
     * @return
     */
    private ContractPaySettlementInfoV assembleResult(ContractPaySettAggregationV aggregationV,List<ContractPayFundInfoV> payFundList) {
        ContractPaySettlementConcludeE settlement = aggregationV.getSettlement();
        ContractPaySettlementInfoV infoV = Global.mapperFacade.map(settlement, ContractPaySettlementInfoV.class);
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.queryByContractId(settlement.getContractId());
        infoV.setContractNo(contractPayConcludeE.getContractNo());
        if (CollectionUtils.isEmpty(payFundList)) {
            return infoV;
        }
        //清单小计-原合同-金额
        infoV.setContractSum(payFundList.stream().map(ContractPayFundInfoV::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        //清单小计-补充合同-金额
        infoV.setChangeContractSum(payFundList.stream().map(ContractPayFundInfoV::getChangeAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        infoV.setChangeContractSumString(ObjectUtils.isEmpty(infoV.getChangeContractSum()) ? null : infoV.getChangeContractSum().toString());
        //清单小计-本期-金额
        infoV.setThisContractSum(payFundList.stream().map(ContractPayFundInfoV::getThisAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        //清单小计-至上期末统计-金额
        infoV.setFromContractSum(payFundList.stream().map(ContractPayFundInfoV::getFromThisAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        infoV.setFromContractSumString(ObjectUtils.isEmpty(infoV.getFromContractSum()) ? null : infoV.getFromContractSum().toString());
        //清单小计-至本期末统计-金额
        infoV.setToContractSum(payFundList.stream().map(ContractPayFundInfoV::getToThisAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));

        //扣款-本期-金额
        infoV.setThisDeductionSum(settlement.getDeductionAmount().setScale(2, RoundingMode.HALF_UP));
        //扣款-至上期末统计-金额
        infoV.setFromDeductionSum(aggregationV.getPreSettlements().stream().map(ContractPaySettlementConcludeE::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        infoV.setFromDeductionSumString(ObjectUtils.isEmpty(infoV.getFromDeductionSum()) ? null : infoV.getFromDeductionSum().toString());
        //扣款-至本期末统计-金额
        infoV.setToDeductionSum(infoV.getFromDeductionSum().add(infoV.getThisDeductionSum()).setScale(2, RoundingMode.HALF_UP));

        //合计-原合同-金额（同contractSum字段）

        //合计-补充合同-金额（同changeContractSum字段）

        //合计-本期-金额
        infoV.setThisGathContractSum(infoV.getThisContractSum().subtract(infoV.getThisDeductionSum()).setScale(2, RoundingMode.HALF_UP));
        //合计-至上期末统计-金额
        infoV.setFromGathContractSum(infoV.getFromContractSum().subtract(infoV.getFromDeductionSum()).setScale(2, RoundingMode.HALF_UP));
        infoV.setFromGathContractSumString(ObjectUtils.isEmpty(infoV.getFromGathContractSum()) ? null : infoV.getFromGathContractSum().toString());
        //合计-至本期末统计-金额
        infoV.setToGathContractSum(infoV.getToContractSum().subtract(infoV.getToDeductionSum()).setScale(2, RoundingMode.HALF_UP));

        infoV.setContractPayFundVList(payFundList);
        return infoV;
    }

    /**
     * 获取清单分组key
     *
     * @param fund
     * @return
     */
    private String fundGroupKey(ContractPayFundE fund){
        String originalGroup = nullToEmpty(fund.getTypeId()) +
                "|" + nullToEmpty(fund.getTaxRateId()) +
                "|" + nullToEmpty(fund.getStandardId()) +
                "|" + nullToEmpty(fund.getStandAmount().toString());
        if (StringUtils.isNotBlank(fund.getExtField())) {
            originalGroup = originalGroup + "|" + nullToEmpty(fund.getExtField());
        }
        return originalGroup;
    }

    /**
     * 清单的五元组分组key，对于问题单之外的单子，ext字段为空，该函数等价为fundGroupKey
     *
     * @param fund
     * @return
     */
    private String fundGroupKeyInExt(ContractPayFundE fund) {
        String originalGroup = nullToEmpty(fund.getTypeId()) +
                "|" + nullToEmpty(fund.getTaxRateId()) +
                "|" + nullToEmpty(fund.getStandardId()) +
                "|" + nullToEmpty(fund.getStandAmount().toString());
        if (StringUtils.isNotBlank(fund.getExtField())) {
            originalGroup = originalGroup + "|" + nullToEmpty(fund.getExtField());
        }
        return originalGroup;
    }

    /**
     * 获取结算明细分组key
     *
     * @param settDetail
     * @return
     */
    private String settDetailGroupKey(ContractPaySettDetailsE settDetail){
        return nullToEmpty(settDetail.getTypeId()) +
                "|" + nullToEmpty(settDetail.getTaxRateId()) +
                "|" + nullToEmpty(settDetail.getStandardId()) +
                "|" + nullToEmpty(settDetail.getStandAmount().toString());
    }

    /**
     * 结算明细的五元组分组key，对于问题单之外的单子，ext字段为空，该函数等价为settDetailGroupKey
     *
     * @param settDetail
     * @return
     */
    private String settDetailGroupKeyInExt(ContractPaySettDetailsE settDetail){
        String originalGroup = nullToEmpty(settDetail.getTypeId()) +
                "|" + nullToEmpty(settDetail.getTaxRateId()) +
                "|" + nullToEmpty(settDetail.getStandardId()) +
                "|" + nullToEmpty(settDetail.getStandAmount().toString());
        if (StringUtils.isNotBlank(settDetail.getExtField())) {
            originalGroup = originalGroup + "|" + nullToEmpty(settDetail.getExtField());
        }
        return originalGroup;
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public ContractPaySettlementInfoV getSettleInfo(String id) {

        ContractPaySettlementConcludeE map = contractPaySettlementConcludeMapper.selectById(id);
        ContractPaySettlementInfoV contractPaySettlementInfoV = Global.mapperFacade.map(map, ContractPaySettlementInfoV.class);

        //查询对应的合同，编号，供应商名称
        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.selectById(map.getContractId());
        contractPaySettlementInfoV.setContractNo(contractPayConcludeE.getContractNo());

        List<ContractPayFundInfoV> contractPayFundVList = contractPayFundMapper.getContractPayFundList(contractPayConcludeE.getId());
        contractPaySettlementInfoV.setContractPayFundVList(contractPayFundVList);

//        LambdaQueryWrapperX<ContractPayConcludeE> queryWrapper = WrapperX.lambdaQueryX();
//        queryWrapper.eq(ContractPayConcludeE::getPid, contractPayConcludeE.getId()).eq(ContractPayConcludeE::getDeleted, 0);
//        List<ContractPayConcludeE> payConcludeEList = contractPayConcludeMapper.selectList(queryWrapper);

        //本期结算单
        List<ContractPaySettDetailsV> contractPaySettDetailsVList = contractPaySettDetailsService.getDetailsBySettlementId(id);

        BigDecimal fromDeductionSum = BigDecimal.ZERO;

        //至上末结算单
        LambdaQueryWrapperX<ContractPaySettlementConcludeE> queryWrapper1 = WrapperX.lambdaQueryX();
        queryWrapper1.eq(ContractPaySettlementConcludeE::getContractId, map.getContractId())
                .eq(ContractPaySettlementConcludeE::getDeleted, 0)
                .lt(ContractPaySettlementConcludeE::getStartTime,map.getStartTime());
        List<ContractPaySettlementConcludeE> startSettleList = contractPaySettlementConcludeMapper.selectList(queryWrapper1);

        List<ContractPaySettDetailsE> contractStartPaySettDetailsEList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(startSettleList)){

            List<BigDecimal> sb = startSettleList.stream().map( s -> s.getDeductionAmount()).collect(Collectors.toList());

            fromDeductionSum = sb.stream().reduce(BigDecimal.ZERO,BigDecimal::add);

            List<String> stratIds = startSettleList.stream().map(s->s.getId()).collect(Collectors.toList());

            LambdaQueryWrapperX<ContractPaySettDetailsE> queryWrapper11 = WrapperX.lambdaQueryX();
            queryWrapper11.in(ContractPaySettDetailsE::getSettlementId, stratIds).eq(ContractPaySettDetailsE::getDeleted, 0);
            contractStartPaySettDetailsEList =  contractPaySettDetailsMapper.selectList(queryWrapper11);

        }



        BigDecimal toDeductionSum = BigDecimal.ZERO;

        //本期金额
        for(ContractPayFundInfoV s : contractPayFundVList){

            List<BigDecimal> thisNumList  =  contractPaySettDetailsVList.stream().filter( sb -> sb.getType().equals(s.getType())).map( sk -> sk.getAmountNum()).collect(Collectors.toList());

            BigDecimal thisNum  =  thisNumList.stream().reduce(BigDecimal.ZERO, BigDecimal :: add);
            Double thisChangeAmount =  contractPaySettDetailsVList.stream().filter( sb -> sb.getType().equals(s.getType())).mapToDouble(sb -> Double.parseDouble(sb.getAmount().toString())).sum();
            s.setThisNum(thisNum);
            s.setThisAmount(new BigDecimal(thisChangeAmount.toString()));

            s.setToThisAmount(BigDecimal.ZERO);
            s.setToThisNum(BigDecimal.ZERO);

            s.setFromThisAmount(BigDecimal.ZERO);
            s.setFromThisNum(BigDecimal.ZERO);

            if(ObjectUtils.isNotEmpty(contractStartPaySettDetailsEList)){
                List<BigDecimal> fromThisNumList = contractStartPaySettDetailsEList.stream().filter(sb -> sb.getType().equals(s.getType())).map( sk -> sk.getAmountNum()).collect(Collectors.toList());
                BigDecimal fromThisNum = fromThisNumList.stream().reduce(BigDecimal.ZERO, BigDecimal :: add);
                Double fromThisAmount =  contractStartPaySettDetailsEList.stream().filter( sb -> sb.getType().equals(s.getType())).mapToDouble(sb -> Double.parseDouble(sb.getAmount().toString())).sum();
                s.setFromThisNum(fromThisNum);
                s.setFromThisAmount(new BigDecimal(fromThisAmount.toString()));
            }
            toDeductionSum = fromDeductionSum.add(map.getDeductionAmount());
            s.setToThisAmount(s.getFromThisAmount().add(s.getThisAmount()));
            s.setToThisNum(s.getThisNum().add(s.getToThisNum()));
        }


        // 如果有补充协议把主合同金额汇总
//        if(ObjectUtils.isNotEmpty(payConcludeE)){
//            List<ContractPayFundInfoV> changeContractPayFundInfoVS = contractPayFundMapper.getContractPayFundList(payConcludeE.getId());
//            for(ContractPayFundInfoV s : contractPayFundVList){
//
//               List<BigDecimal> changAmountList =  changeContractPayFundInfoVS.stream().filter( sb -> sb.getType().equals(s.getType())).map(  sk -> sk.getNum()).collect(Collectors.toList());
//
//               BigDecimal numAmount  =  changAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal :: add);
//
//               Double changStandAmount =  changeContractPayFundInfoVS.stream().filter( sb -> sb.getType().equals(s.getType())).mapToDouble(sb -> Double.parseDouble(sb.getStandAmount().toString())).sum();
//
//               Double changAmount =  changeContractPayFundInfoVS.stream().filter( sb -> sb.getType().equals(s.getType())).mapToDouble(sb -> Double.parseDouble(sb.getAmount().toString())).sum();
//
//               s.setChangeNum(s.getNum().add(numAmount));
//               s.setChangeStandAmount(new BigDecimal(changStandAmount.toString()));
//               s.setChangeAmount(new BigDecimal(changAmount.toString()).add(s.getAmount()));
//            }
//        }

        List<ContractPayFundInfoV> sum = contractPaySettlementInfoV.getContractPayFundVList();
        for(ContractPayFundInfoV s : sum){
            if(s.getChangeAmount() == null ){
                s.setChangeAmount(BigDecimal.ZERO);
            }
            if(s.getFromThisAmount() == null){
                s.setFromThisAmount(BigDecimal.ZERO);
            }
            if(s.getToThisAmount() == null){
                s.setToThisAmount(BigDecimal.ZERO);
            }
        }

        contractPaySettlementInfoV.setThisDeductionSum(map.getDeductionAmount());
        contractPaySettlementInfoV.setFromDeductionSum(fromDeductionSum);
        contractPaySettlementInfoV.setToDeductionSum(toDeductionSum);
        contractPaySettlementInfoV.setContractSum(new BigDecimal(sum.stream().mapToDouble(sb -> Double.parseDouble(sb.getAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
        contractPaySettlementInfoV.setChangeContractSum(new BigDecimal(sum.stream().mapToDouble(sb -> Double.parseDouble(sb.getChangeAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
        contractPaySettlementInfoV.setThisContractSum(new BigDecimal(sum.stream().mapToDouble(sb -> Double.parseDouble(sb.getThisAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
        contractPaySettlementInfoV.setFromContractSum(new BigDecimal(sum.stream().mapToDouble(sb -> Double.parseDouble(sb.getFromThisAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
        contractPaySettlementInfoV.setToContractSum(new BigDecimal(sum.stream().mapToDouble(sb -> Double.parseDouble(sb.getToThisAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
        contractPaySettlementInfoV.setThisGathContractSum(contractPaySettlementInfoV.getThisContractSum().subtract(contractPaySettlementInfoV.getThisDeductionSum()));
        contractPaySettlementInfoV.setFromGathContractSum(contractPaySettlementInfoV.getFromContractSum().subtract(contractPaySettlementInfoV.getFromDeductionSum()));
        contractPaySettlementInfoV.setToGathContractSum(contractPaySettlementInfoV.getToContractSum().subtract(contractPaySettlementInfoV.getToDeductionSum()));
        for(ContractPayFundInfoV s : contractPaySettlementInfoV.getContractPayFundVList()){
            if(s.getChangeAmount().compareTo(BigDecimal.ZERO) == 0){
                s.setChangeAmountString(null);
            }else{
                s.setChangeAmountString(s.getChangeAmount().toString());
            }
            if(s.getFromThisAmount().compareTo(BigDecimal.ZERO) == 0){
                s.setFromThisAmountString(null);
            }else{
                s.setFromThisAmountString(s.getFromThisAmount().toString());
            }
            if(s.getFromThisNum().compareTo(BigDecimal.ZERO) == 0){
                s.setFromThisNumString(null);
            }else{
                s.setFromThisNumString(s.getFromThisNum().toString());
            }
        }
        if(contractPaySettlementInfoV.getChangeContractSum().compareTo(BigDecimal.ZERO) == 0){
            contractPaySettlementInfoV.setChangeContractSumString(null);
        }else{
            contractPaySettlementInfoV.setChangeContractSumString(contractPaySettlementInfoV.getChangeContractSum().toString());
        }
        if(contractPaySettlementInfoV.getFromContractSum().compareTo(BigDecimal.ZERO) == 0){
            contractPaySettlementInfoV.setFromContractSumString(null);
        }else{
            contractPaySettlementInfoV.setFromContractSumString(contractPaySettlementInfoV.getFromContractSum().toString());
        }
        if(contractPaySettlementInfoV.getFromDeductionSum().compareTo(BigDecimal.ZERO) == 0){
            contractPaySettlementInfoV.setFromDeductionSumString(null);
        }else{
            contractPaySettlementInfoV.setFromDeductionSumString(contractPaySettlementInfoV.getFromDeductionSum().toString());
        }
        if(contractPaySettlementInfoV.getFromGathContractSum().compareTo(BigDecimal.ZERO) == 0){
            contractPaySettlementInfoV.setFromGathContractSumString(null);
        }else{
            contractPaySettlementInfoV.setFromGathContractSumString(contractPaySettlementInfoV.getFromGathContractSum().toString());
        }
        return contractPaySettlementInfoV;
    }

    public PageV<ContractPaySettlementPageV2> pageMockV2(PageF<SearchF<?>> request) {
        ContractPaySettlementPageV2 pageV2 = new ContractPaySettlementPageV2();
        pageV2.setContractRegion("西部区域");
        pageV2.setCommunityName("测试项目1");
        pageV2.setContractNo("123123");
        pageV2.setContractCategoryName("24234");
        pageV2.setContractServeType(1);
        pageV2.setContractName("测试合同1");
        pageV2.setMerchantName("asdad");
        pageV2.setContractAmount(new BigDecimal("1.23"));
        pageV2.setContractStartEndDisplay("2024-01-01~2024-02-02");
        pageV2.setContractStatusName("履行中");
        pageV2.setSplitModeName("asdads");
        pageV2.setRangeAmountPayable(new BigDecimal("3.45"));
        pageV2.setRangeSettledAmount(new BigDecimal("5.67"));
        pageV2.setRangeUnsettledAmount(new BigDecimal("7.89"));
        pageV2.setRangeUnsettledStartEndDisplay("2024-01-01~2024-02-02");
        pageV2.setRangeUnsettledTermDate(4);
        List<ContractPaySettlementPageV2> records = new ArrayList<>();
        records.add(pageV2);
        return PageV.of(request, 1, records);
    }

    public void handlePayPlanInvoceAndPayState(String id){
        ContractPaySettlementConcludeE pay = contractPaySettlementConcludeMapper.selectById(id);
//        List<Integer> termDates = Arrays.stream(pay.getTermDate().split(",")).map(Integer::valueOf).sorted().collect(Collectors.toList());
        QueryWrapper<ContractPayPlanConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayPlanConcludeE.DELETED, 0)
                .notIn(ContractPayPlanConcludeE.PID,0)
                .eq(ContractPayPlanConcludeE.CONTRACT_ID, pay.getContractId());
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.selectList(queryWrapper);
        BigDecimal paymentAmountSum = BigDecimal.ZERO;
        BigDecimal invoiceApplyAmountSum = BigDecimal.ZERO;
        for(ContractPayPlanConcludeE s : contractIncomePlanConcludeVList){
            paymentAmountSum = paymentAmountSum.add(s.getPaymentAmount());
            invoiceApplyAmountSum = invoiceApplyAmountSum.add(s.getInvoiceApplyAmount());
        }
//                .in(ContractPayPlanConcludeE.TERMDATE,termDates);
//        BigDecimal paymentAmount = pay.getPaymentAmount();
//        BigDecimal invoiceApplyAmount = pay.getInvoiceApplyAmount();
//        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeMapper.selectList(queryWrapper);
//        contractIncomePlanConcludeVList = contractIncomePlanConcludeVList.stream().sorted(Comparator.comparing(ContractPayPlanConcludeE :: getTermDate)).collect(Collectors.toList());
//        for(int i = 0; i < contractIncomePlanConcludeVList.size(); i ++){
//            ContractPayPlanConcludeE concludeE = contractIncomePlanConcludeVList.get(i);
//            if(paymentAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount())).compareTo(BigDecimal.ZERO) > 0){
//                paymentAmount = paymentAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount()));
//                concludeE.setPaymentAmount(concludeE.getPlannedCollectionAmount());
//                concludeE.setNoPayAmount(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount()));
//                contractPayPlanConcludeService.updateById(concludeE);
//            }else if(paymentAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount())).compareTo(BigDecimal.ZERO) == 0){
//                concludeE.setPaymentAmount(concludeE.getPlannedCollectionAmount());
//                concludeE.setNoPayAmount(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount()));
//                contractPayPlanConcludeService.updateById(concludeE);
//                break;
//            }else if(paymentAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount())).compareTo(BigDecimal.ZERO) < 0){
//                concludeE.setPaymentAmount(concludeE.getPaymentAmount().add(paymentAmount));
//                concludeE.setNoPayAmount(concludeE.getPlannedCollectionAmount().subtract(concludeE.getPaymentAmount()));
//                contractPayPlanConcludeService.updateById(concludeE);
//                break;
//            }
//        }
//        for(int i = 0; i < contractIncomePlanConcludeVList.size(); i ++){
//            ContractPayPlanConcludeE concludeE = contractIncomePlanConcludeVList.get(i);
//            if(invoiceApplyAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getInvoiceApplyAmount())).compareTo(BigDecimal.ZERO) > 0){
//                invoiceApplyAmount = invoiceApplyAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getInvoiceApplyAmount()));
//                concludeE.setInvoiceApplyAmount(concludeE.getPlannedCollectionAmount());
//                concludeE.setInvoiceStatus(2);
//                contractPayPlanConcludeService.updateById(concludeE);
//            }else if(invoiceApplyAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getInvoiceApplyAmount())).compareTo(BigDecimal.ZERO) == 0){
//                concludeE.setInvoiceApplyAmount(concludeE.getPlannedCollectionAmount());
//                concludeE.setInvoiceStatus(2);
//                contractPayPlanConcludeService.updateById(concludeE);
//                break;
//            }else if(invoiceApplyAmount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getInvoiceApplyAmount())).compareTo(BigDecimal.ZERO) < 0){
//                concludeE.setInvoiceApplyAmount(concludeE.getInvoiceApplyAmount().add(invoiceApplyAmount));
//                concludeE.setInvoiceStatus(1);
//                contractPayPlanConcludeService.updateById(concludeE);
//                break;
//            }
//        }
        QueryWrapper<ContractPayPlanConcludeE> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(ContractPayPlanConcludeE.DELETED, 0)
                .eq(ContractPayPlanConcludeE.PID,0)
                .eq(ContractPayPlanConcludeE.CONTRACT_ID, pay.getContractId());
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList1 = contractPayPlanConcludeMapper.selectList(queryWrapper1);
        contractIncomePlanConcludeVList1.get(0).setPaymentAmount(paymentAmountSum);
        contractIncomePlanConcludeVList1.get(0).setInvoiceApplyAmount(invoiceApplyAmountSum);
        contractIncomePlanConcludeVList1.get(0).setNoPayAmount(contractIncomePlanConcludeVList1.get(0).getPlannedCollectionAmount().subtract(paymentAmountSum));
        contractPayPlanConcludeService.updateById(contractIncomePlanConcludeVList1.get(0));
    }


    public void handleCallBack(String settleId, BigDecimal invoiceAmount){
        ContractPaySettlementConcludeE pay = contractPaySettlementConcludeMapper.selectById(settleId);
        pay.setInvoiceApplyAmount(pay.getInvoiceApplyAmount().subtract(invoiceAmount));
        pay.setInvoiceStatus(0);
        contractPaySettlementConcludeMapper.updateById(pay);
        handlePidPaySettlementInfo(pay.getPid());
        handlePayPlanInvoceAndPayState(pay.getId());
    }

    public void handlePayPlanState(String settleId) {
        log.info("开始结算单核销逻辑,当前结算单id:{}", settleId);
        //查关联的结算计划id
        List<ContractPayConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return;
        }
        List<String> planIds = relations.stream()
                .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //查询周期信息
        log.info("当前结算单关联的结算计划id集合:{}", planIds);
        List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }
        log.info("当前结算单关联的结算周期信息:{}", JSON.toJSONString(periodList));
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractPayPlanConcludeE> planConcludes = contractPayPlanConcludeService.queryByCostTime(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return;
        }
        //转换为分组
        Map<String, List<ContractPayPlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
        log.info("根据结算计划id集合和周期信息匹配到的结算计划详情:{}",JSON.toJSONString(planConcludeMap));
        //查关联的结算详情
        List<ContractPaySettDetailsE> settDetails = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, settleId)
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }
        log.info("当前结算单关联的结算详情:{}", JSON.toJSONString(settDetails));
        //汇总所有结算计划id，查询全部的成本计划
        List<String> allCostPlanIds = planConcludes.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
        List<PayCostPlanE> payCostPlanES = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                .in(PayCostPlanE::getPlanId, allCostPlanIds)
                .eq(PayCostPlanE::getDeleted, 0));
        //转为map
        /*Map<String, List<PayCostPlanE>> payCostPlanMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(payCostPlanES)) {
            payCostPlanMap = payCostPlanES.stream().collect(Collectors.groupingBy(PayCostPlanE::getPlanId));
        }
        log.info("结算计划下的成本计划信息:{}", JSON.toJSONString(payCostPlanMap));*/
        //遍历进行已结算金额处理
        for (ContractPaySettDetailsE detail : settDetails) {
            //获取分组key
            String groupKey = groupKey(detail);
            if (!planConcludeMap.containsKey(groupKey)) {
                continue;
            }
            //获取key对应的结算计划列表
            List<ContractPayPlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
            //转bo-list，参与核销逻辑 后续使用
            List<PlanWriteOffBo> planWriteOffBoList = planConcludeList.stream()
                    .map(PlanWriteOffBo::transferByPayPlan)
                    .collect(Collectors.toList());
            // bo 根据 planId 转map
            Map<String, PlanWriteOffBo> planWriteOffBoMap = planWriteOffBoList.stream()
                    .collect(Collectors.toMap(PlanWriteOffBo::getPlanId, Function.identity()));
            //当前结算明细的总金额、税额、不含税金额
            BigDecimal amount = detail.getAmount();
            //按照原逻辑进行已结算金额和结算状态的维护
            for (ContractPayPlanConcludeE concludeE : planConcludeList) {
                PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(concludeE.getId());
                if (concludeE.getPaymentStatus() == 2) {
                    //如果已经完成结算了就continue
                    continue;
                }
                if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                    //amount减去当前处理的金额，多出的用于后续核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                    //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                    break;
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                    concludeE.setSettlementAmount(concludeE.getSettlementAmount().add(amount));
                    //amount核销不完，将amount累加到已结算金额后，将amount置为0
                    amount=BigDecimal.ZERO;
                    concludeE.setPaymentStatus(1);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                    break;
                }
            }
            //测试下来没问题，核销结束后，若amount还大于0，直接加到每组最后一个的结算金额上
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                ContractPayPlanConcludeE contractPayPlanConcludeE = planConcludeList.get(planConcludeList.size() - 1);
                log.info("核销完该组结算计划后amount金额还大于0,剩余金额:{},当前组最后一个结算计划当前已结算金额:{}", amount, contractPayPlanConcludeE.getSettlementAmount());
                //保险起见还是加一个，虽然走到这里一定是从循环第二个if出来的
                contractPayPlanConcludeE.setPaymentStatus(2);
                contractPayPlanConcludeE.setSettlementAmount(contractPayPlanConcludeE.getSettlementAmount().add(amount));
            }
            doHandlePayPlanSettleCalculate(detail, planConcludeList, planWriteOffBoMap);

            //doHandlePayCostPlanV3(planWriteOffBoList,payCostPlanMap);
        }

        List<PayCostPlanE> payCostPlanNewList = new ArrayList<>();
        doHandlePayCostPlanV4(payCostPlanES, settDetails, planConcludes, payCostPlanNewList);
        //批量更新
        contractPayPlanConcludeService.updateBatchById(planConcludes);
        if (CollectionUtils.isNotEmpty(payCostPlanNewList)) {
            contractPayCostPlanService.updateBatchById(payCostPlanNewList);
        }
        //处理父级的结算金额，逻辑不变，plannedCollectionAmount字段就是该单子的结算总额，等于结算明细中amount字段的和
        ContractPaySettlementConcludeE pay = contractPaySettlementConcludeMapper.selectById(settleId);
        QueryWrapper<ContractPayPlanConcludeE> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(ContractPayPlanConcludeE.DELETED, 0)
                .eq(ContractPayPlanConcludeE.PID,0)
                .eq(ContractPayPlanConcludeE.CONTRACT_ID, pay.getContractId());
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList1 = contractPayPlanConcludeMapper.selectList(queryWrapper1);
        BigDecimal settlementAmount = contractIncomePlanConcludeVList1.get(0).getSettlementAmount();
        contractIncomePlanConcludeVList1.get(0).setSettlementAmount(settlementAmount.add(pay.getPlannedCollectionAmount()));
        contractPayPlanConcludeService.updateById(contractIncomePlanConcludeVList1.get(0));
    }

    //核销结算计划及成本计划金额
    public void writeOffPayPlanAndCost(String settleId){
        log.info("开始结算单的核销逻辑,当前结算单id为:{}", settleId);
        ContractPaySettlementConcludeE paySettlement = contractPaySettlementConcludeMapper.selectById(settleId);

        //查关联的结算详情
        List<ContractPaySettDetailsE> settDetailList = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, settleId)
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        log.info("当前结算单关联的结算详情:{}", JSON.toJSONString(settDetailList));
        if (CollectionUtils.isEmpty(settDetailList)) {
            log.info("当前结算单无关联的结算详情信息，直接跳出");
            return;
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
            return;
        }

        //-------【核销结算计划金额】------
        List<ContractPayConcludeSettlementPlanRelationE> relationList = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isNotEmpty(relationList)) {
            List<String> planIdList = relationList.stream()
                    .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                    .distinct()
                    .collect(Collectors.toList());
            List<ContractPayPlanConcludeE> planList = contractPayPlanConcludeService.queryByCostTime(planIdList, periodList);
            log.info("根据结算计划id集合和周期信息匹配到的结算计划:{}",JSON.toJSONString(planList));
            if (CollectionUtils.isNotEmpty(planList)) {
                //转换为分组
                Map<String, List<ContractPayPlanConcludeE>> planConcludeMap = getPlanConcludeMap(planList);
                log.info("根据结算计划id集合和周期信息匹配到的结算计划详情:{}",JSON.toJSONString(planConcludeMap));
                //遍历进行已结算金额处理
                for (ContractPaySettDetailsE detail : settDetailList) {
                    //获取分组key
                    String groupKey = groupKey(detail);
                    if (!planConcludeMap.containsKey(groupKey)) {
                        continue;
                    }
                    //获取key对应的结算计划列表
                    List<ContractPayPlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
                    //转bo-list，参与核销逻辑 后续使用
                    List<PlanWriteOffBo> planWriteOffBoList = planConcludeList.stream()
                            .map(PlanWriteOffBo::transferByPayPlan)
                            .collect(Collectors.toList());
                    // bo 根据 planId 转map
                    Map<String, PlanWriteOffBo> planWriteOffBoMap = planWriteOffBoList.stream()
                            .collect(Collectors.toMap(PlanWriteOffBo::getPlanId, Function.identity()));
                    //当前结算明细的总金额、税额、不含税金额
                    BigDecimal amount = detail.getAmount();
                    //按照原逻辑进行已结算金额和结算状态的维护
                    for (ContractPayPlanConcludeE concludeE : planConcludeList) {
                        PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(concludeE.getId());
                        if (concludeE.getPaymentStatus() == 2) {
                            //如果已经完成结算了就continue
                            continue;
                        }
                        if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                            //amount减去当前处理的金额，多出的用于后续核销
                            amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                            concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                            concludeE.setPaymentStatus(2);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayCostFinished(1);
                            }
                        } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                            //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                            amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                            concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                            concludeE.setPaymentStatus(2);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayCostFinished(1);
                            }
                            break;
                        } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                            concludeE.setSettlementAmount(concludeE.getSettlementAmount().add(amount));
                            //amount核销不完，将amount累加到已结算金额后，将amount置为0
                            amount=BigDecimal.ZERO;
                            concludeE.setPaymentStatus(2);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayCostFinished(1);
                            }
                            break;
                        }
                    }
                    //测试下来没问题，核销结束后，若amount还大于0，直接加到每组最后一个的结算金额上
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        ContractPayPlanConcludeE contractPayPlanConcludeE = planConcludeList.get(planConcludeList.size() - 1);
                        log.info("核销完该组结算计划后amount金额还大于0,剩余金额:{},当前组最后一个结算计划当前已结算金额:{}", amount, contractPayPlanConcludeE.getSettlementAmount());
                        //保险起见还是加一个，虽然走到这里一定是从循环第二个if出来的
                        contractPayPlanConcludeE.setPaymentStatus(2);
                        contractPayPlanConcludeE.setSettlementAmount(contractPayPlanConcludeE.getSettlementAmount().add(amount));
                    }
                    doHandlePayPlanSettleCalculate(detail, planConcludeList, planWriteOffBoMap);
                }

                //批量更新
                contractPayPlanConcludeService.updateBatchById(planList);
                //处理父级的结算金额，逻辑不变，plannedCollectionAmount字段就是该单子的结算总额，等于结算明细中amount字段的和
                QueryWrapper<ContractPayPlanConcludeE> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq(ContractPayPlanConcludeE.DELETED, 0)
                        .eq(ContractPayPlanConcludeE.PID,0)
                        .eq(ContractPayPlanConcludeE.CONTRACT_ID, paySettlement.getContractId());
                List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList1 = contractPayPlanConcludeMapper.selectList(queryWrapper1);
                BigDecimal settlementAmount = contractIncomePlanConcludeVList1.get(0).getSettlementAmount();
                contractIncomePlanConcludeVList1.get(0).setSettlementAmount(settlementAmount.add(paySettlement.getPlannedCollectionAmount()));
                contractPayPlanConcludeService.updateById(contractIncomePlanConcludeVList1.get(0));
            }
        }


        //-------【核销成本计划金额】------
        //根据清单ID及结算周期查询范围内的成本计划
        List<PayCostPlanE> payCostPlanList = new ArrayList<>();
        if(Objects.nonNull(settDetailList.get(0).getStartDate()) && Objects.nonNull(settDetailList.get(0).getEndDate())){
            for(ContractPaySettDetailsE settD : settDetailList){
                payCostPlanList.addAll(contractPayCostPlanService.getCostListByPlanAndCostTime(
                        paySettlement.getContractId(),
                        settD.getPayFundId(),
                        Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }else{
            List<ContractPayPlanConcludeE> planFunList = contractPayPlanConcludeMapper.getFunDateList(paySettlement.getContractId(),settDetailList.stream().map(ContractPaySettDetailsE :: getPayFundId).collect(Collectors.toList()),
                    Global.mapperFacade.mapAsList(periodList, ContractPaySettlementPeriodF.class));
            for(ContractPayPlanConcludeE settD : planFunList){
                payCostPlanList.addAll(contractPayCostPlanService.getCostListByPlanAndCostTime(
                        paySettlement.getContractId(),
                        settD.getContractPayFundId(),
                        Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }
        log.info("根据结算计划ID及结算周期查询范围内的成本计划，{}",JSON.toJSONString(payCostPlanList));
        if(CollectionUtils.isNotEmpty(payCostPlanList)){
            List<PayCostPlanE> payCostPlanNewList = new ArrayList<>();
            doHandlePayCostPlanV5(payCostPlanList, payCostPlanNewList, settDetailList);
            if (CollectionUtils.isNotEmpty(payCostPlanNewList)) {
                contractPayCostPlanService.updateBatchById(payCostPlanNewList);
            }
        }
    }

    //核销成本计划金额
    private void doHandlePayCostPlanV5(List<PayCostPlanE> payCostPlanOldList,List<PayCostPlanE> payCostPlanNewList, List<ContractPaySettDetailsE> settDetails) {
        //根据结算单获取费项对应减免金额
        List<ContractPayConcludeSettdeductionV> contractPayConcludeSettdeductionVList = contractPayConcludeSettdeductionService.getDetailsBySettlementId(settDetails.get(0).getSettlementId());
        Map<Long, BigDecimal> sumAmountByChargeItemId = new HashMap<>();
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            sumAmountByChargeItemId = contractPayConcludeSettdeductionVList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayConcludeSettdeductionV::getChargeItemId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ContractPayConcludeSettdeductionV::getAmount,
                                    BigDecimal::add
                            )
                    ));
        }
        List<Long> chargeItemIds = settDetails.stream().map(ContractPaySettDetailsE::getChargeItemId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            List<Long> removeAll = contractPayConcludeSettdeductionVList.stream().map(ContractPayConcludeSettdeductionV::getChargeItemId).collect(Collectors.toList());
            chargeItemIds = chargeItemIds.stream().filter(chargeItemId -> !removeAll.contains(chargeItemId)).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(chargeItemIds)){
            for(Long chargeItemId : chargeItemIds){
                sumAmountByChargeItemId.put(chargeItemId, BigDecimal.ZERO);
            }
        }
        //计算每条清单计算金额（结算明细金额-对应费项的减免金额）
        Map<String, BigDecimal> funMapChargeAmountMap = new HashMap<>();
        Map<String, BigDecimal> funMapChargeDetailAmountMap = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : sumAmountByChargeItemId.entrySet()) {
            //获取费项对应清单列表
            List<ContractPaySettDetailsE> funSettDetailList = settDetails.stream()
                    .filter(settDetail -> settDetail.getChargeItemId().equals(entry.getKey()))
                    .collect(Collectors.toList());
            //获取同费项清单总金额
            BigDecimal settDetailTotalAmount = funSettDetailList.stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> funSettMap = new HashMap<>();
            funSettDetailList.forEach(plan-> {
                funSettMap.put(plan.getId(), plan.getAmount());
            });
            //获取费项对应核销金额
            Map<String, BigDecimal> funSettReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funSettMap, settDetailTotalAmount, entry.getValue());
            funMapChargeDetailAmountMap.putAll(funSettReductionAmountMap);
            funSettDetailList.forEach(plan-> {
                funMapChargeAmountMap.put(plan.getId(), plan.getAmount().subtract(funSettReductionAmountMap.get(plan.getId())));
            });
        }

        //
        for (Map.Entry<String, BigDecimal> entry : funMapChargeAmountMap.entrySet()) {
            ContractPaySettDetailsE settDetailNew = settDetails.stream().filter(settDetail -> settDetail.getId().equals(entry.getKey())).findFirst().get();
            //获取该清单计算金额
            BigDecimal costPlanAmountTotal = entry.getValue();
            //减免金额
            BigDecimal funMapChargeDetailAmount = funMapChargeDetailAmountMap.get(entry.getKey());
            //获取清单对应的成本计划数据
            List<PayCostPlanE> planCostList = payCostPlanOldList.stream().filter(x -> x.getPayFundId().equals(settDetailNew.getPayFundId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(planCostList)){
                continue;
            }
            List<CommonRangeDateBO> dateBOList = new ArrayList<>();
            planCostList.forEach(x->{
                CommonRangeDateBO dateBO = new CommonRangeDateBO();
                BeanUtils.copyProperties(x,dateBO);
                dateBOList.add(dateBO);
            });
            Map<String, BigDecimal> costPlanMap = commonRangeAmountService.calculateDistributedAmounts(dateBOList,costPlanAmountTotal);
            Map<String, BigDecimal> costPlanReductionMap = commonRangeAmountService.calculateDistributedAmounts(dateBOList,funMapChargeDetailAmount);
           for(PayCostPlanE cost : planCostList){
               cost.setPaymentAmount(costPlanMap.get(cost.getId()));
               cost.setReductionAmount(costPlanReductionMap.get(cost.getId()));
               extracted(cost);
               // 有关后续的核销
               cost.setSettlementStatus(cost.getPaymentAmount()
                       .compareTo(cost.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
           }

/*
            planCostList = planCostList.stream()
                    .sorted(Comparator.comparing(PayCostPlanE::getCostStartTime))
                    .collect(Collectors.toList());
            LocalDate costStartTime = planCostList.stream()
                    .map(PayCostPlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = planCostList.stream()
                    .map(PayCostPlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,costPlanAmountTotal);
            CommonRangeAmountBO funMapChargeDetailAmountBo = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,funMapChargeDetailAmount);
            if(planCostList.size() <= 3){
                for (int i = 0; i < planCostList.size(); i++) {
                    PayCostPlanE payCostPlan = planCostList.get(i);
                    if(i == 0){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getStartMonthAmount());
                        extracted(payCostPlan);
                    }else if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getEndMonthAmount());
                        extracted(payCostPlan);
                    }else{
                        payCostPlan.setPaymentAmount(settlementAmountBO.getAvgMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getAvgMonthAmount());
                        extracted(payCostPlan);
                    }
                    // 有关后续的核销
                    payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                            .compareTo(payCostPlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
                }
            }else{
                BigDecimal useAmount = BigDecimal.ZERO;
                BigDecimal useChargeAmount = BigDecimal.ZERO;
                //核销月平均金额
                BigDecimal avgPaymentAmount = settlementAmountBO.getAvgMonthAmount();
                //减免月平均金额
                BigDecimal avgChargeAmount = funMapChargeDetailAmountBo.getAvgMonthAmount();
                for (int i = 0; i < planCostList.size(); i++) {
                    PayCostPlanE payCostPlan = planCostList.get(i);
                    if (i == planCostList.size() - 1) {
                        payCostPlan.setPaymentAmount(costPlanAmountTotal.subtract(useAmount));
                        payCostPlan.setReductionAmount(funMapChargeDetailAmount.subtract(useChargeAmount));
                        extracted(payCostPlan);
                    } else {
                        //判断开始日期是否为当月第一天
                        Boolean isFirstDayOfMonth = payCostPlan.getCostStartTime().getDayOfMonth() == 1;
                        //当前月份天数
                        int daysInMonth = payCostPlan.getCostStartTime().lengthOfMonth();
                        //当前时间段占据时间
                        int nowDay;
                        // 如果开始日期不是当月第一天，
                        if (!isFirstDayOfMonth) {
                            //计算开始时间到月底的剩余天数
                            LocalDate lastDayOfMonth = payCostPlan.getCostStartTime().with(TemporalAdjusters.lastDayOfMonth());
                            nowDay = lastDayOfMonth.getDayOfMonth() - payCostPlan.getCostStartTime().getDayOfMonth() + 1;
                        } else {
                            //开始时间是当月第一天
                            //计算结束时间到月初天数
                            LocalDate firstDayOfMonth = payCostPlan.getCostEndTime().with(TemporalAdjusters.firstDayOfMonth());
                            nowDay = payCostPlan.getCostEndTime().getDayOfMonth() - firstDayOfMonth.getDayOfMonth() + 1;
                        }
                        BigDecimal ratio = BigDecimal.valueOf(nowDay)
                                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_UP);

                        BigDecimal thisPaymentAmount = avgPaymentAmount.multiply(ratio).setScale(0, RoundingMode.DOWN);
                        BigDecimal thisReductionmentAmount = avgChargeAmount.multiply(ratio).setScale(0, RoundingMode.DOWN);
                        useAmount = useAmount.add(thisPaymentAmount);
                        useChargeAmount = useChargeAmount.add(thisReductionmentAmount);
                        payCostPlan.setPaymentAmount(thisPaymentAmount);
                        payCostPlan.setReductionAmount(thisReductionmentAmount);
                        extracted(payCostPlan);
                    }
                }
            }*/

            BigDecimal settlementTaxAmount = settDetailNew.getTaxRateAmount();
            if ("差额纳税".equals( planCostList.get(0).getTaxRate())) {
                int periodCount = planCostList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        planCostList.get(i).setTaxAmount(taxAmount);
                        planCostList.get(i).setNoTaxAmount(planCostList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
            payCostPlanNewList.addAll(planCostList);
        }
    }

    /*public void handlePayPlanStateNew(String settleId) {
        log.info("开始结算单核销逻辑,当前结算单id:{}", settleId);
        //查关联的结算计划id
        List<ContractPayConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return;
        }
        List<String> planIds = relations.stream()
                .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //查询周期信息
        log.info("当前结算单关联的结算计划id集合:{}", planIds);
        List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }
        log.info("当前结算单关联的结算周期信息:{}", JSON.toJSONString(periodList));
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractPayPlanConcludeE> planConcludes = contractPayPlanConcludeService.queryByCostTime(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return;
        }
        //获取所有该结算单关联的计划
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList = contractPayPlanConcludeService.getByIdList(planIds);

        //查关联的结算详情
        List<ContractPaySettDetailsE> settDetails = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, settleId)
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }

        List<ContractPayPlanConcludeE> planAllList = new ArrayList<>();
        for (ContractPaySettDetailsE detail : settDetails) {
            //当前结算明细的总金额、税额、不含税金额
            BigDecimal amount = detail.getAmount();
            //获取该结算明细关联的结算计划
            List<ContractPayPlanConcludeE> settPlanList = planConcludes.stream().filter(settPlan -> settPlan.getContractPayFundId().equals(detail.getPayFundId()) && !settPlan.getPaymentStatus().equals(2)).collect(Collectors.toList());
            //获取计划总金额
            BigDecimal planTotalAmount = settPlanList.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, BigDecimal> planDetAmountMap = settPlanList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayPlanConcludeE::getContractPayFundId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    plan -> plan.getPlannedCollectionAmount().subtract(plan.getSettlementAmount()),
                                    BigDecimal::add
                            )
                    ));
            //剩余核销金额
            BigDecimal remainingAmount = BigDecimal.ZERO;
            for(ContractPayPlanConcludeE settPlan : settPlanList){
                //计算每条计划占用核销金额
                Map<String, BigDecimal> planAmountMap = BigDecimalUtils.calculateDistributedAmounts(planDetAmountMap, planTotalAmount,amount);
                BigDecimal clauseAmount = planAmountMap.get(settPlan.getId());
                if (clauseAmount.subtract(settPlan.getPlannedCollectionAmount().subtract(settPlan.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                    //amount减去当前处理的金额，多出的用于后续核销
                    remainingAmount = remainingAmount.add(clauseAmount.subtract(settPlan.getPlannedCollectionAmount().subtract(settPlan.getSettlementAmount())));
                    settPlan.setSettlementAmount(settPlan.getPlannedCollectionAmount());
                    settPlan.setPaymentStatus(2);
                    settPlan.setPayCostFinished(1);
                } else if (clauseAmount.subtract(settPlan.getPlannedCollectionAmount().subtract(settPlan.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                    //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                    settPlan.setSettlementAmount(settPlan.getPlannedCollectionAmount());
                    settPlan.setPaymentStatus(2);
                    settPlan.setPayCostFinished(1);
                } else if (clauseAmount.subtract(settPlan.getPlannedCollectionAmount().subtract(settPlan.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                    settPlan.setSettlementAmount(clauseAmount.subtract(settPlan.getPlannedCollectionAmount().subtract(settPlan.getSettlementAmount()).add(settPlan.getSettlementAmount())));
                    //amount核销不完，将amount累加到已结算金额后，将amount置为0
                    settPlan.setPaymentStatus(1);
                        settPlan.setPayCostFinished(1);
                }
                planAllList.add(settPlan);
            }
        }





        //转换为分组
        Map<String, List<ContractPayPlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
        log.info("根据结算计划id集合和周期信息匹配到的结算计划详情:{}",JSON.toJSONString(planConcludeMap));

        log.info("当前结算单关联的结算详情:{}", JSON.toJSONString(settDetails));
        //汇总所有结算计划id，查询全部的成本计划
        List<String> allCostPlanIds = planConcludes.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
        List<PayCostPlanE> payCostPlanES = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                .in(PayCostPlanE::getPlanId, allCostPlanIds)
                .eq(PayCostPlanE::getDeleted, 0));
        //转为map
        Map<String, List<PayCostPlanE>> payCostPlanMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(payCostPlanES)) {
            payCostPlanMap = payCostPlanES.stream().collect(Collectors.groupingBy(PayCostPlanE::getPlanId));
        }
        log.info("结算计划下的成本计划信息:{}", JSON.toJSONString(payCostPlanMap));
        //遍历进行已结算金额处理
        for (ContractPaySettDetailsE detail : settDetails) {
            //获取分组key
            String groupKey = groupKey(detail);
            if (!planConcludeMap.containsKey(groupKey)) {
                continue;
            }
            //获取key对应的结算计划列表
            List<ContractPayPlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
            //转bo-list，参与核销逻辑 后续使用
            List<PlanWriteOffBo> planWriteOffBoList = planConcludeList.stream()
                    .map(PlanWriteOffBo::transferByPayPlan)
                    .collect(Collectors.toList());
            // bo 根据 planId 转map
            Map<String, PlanWriteOffBo> planWriteOffBoMap = planWriteOffBoList.stream()
                    .collect(Collectors.toMap(PlanWriteOffBo::getPlanId, Function.identity()));
            //当前结算明细的总金额、税额、不含税金额
            BigDecimal amount = detail.getAmount();
            //按照原逻辑进行已结算金额和结算状态的维护
            for (ContractPayPlanConcludeE concludeE : planConcludeList) {
                PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(concludeE.getId());
                if (concludeE.getPaymentStatus() == 2) {
                    //如果已经完成结算了就continue
                    continue;
                }
                if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                    //amount减去当前处理的金额，多出的用于后续核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                    //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                    break;
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                    concludeE.setSettlementAmount(concludeE.getSettlementAmount().add(amount));
                    //amount核销不完，将amount累加到已结算金额后，将amount置为0
                    amount=BigDecimal.ZERO;
                    concludeE.setPaymentStatus(1);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayCostFinished(1);
                    }
                    break;
                }
            }
            //测试下来没问题，核销结束后，若amount还大于0，直接加到每组最后一个的结算金额上
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                ContractPayPlanConcludeE contractPayPlanConcludeE = planConcludeList.get(planConcludeList.size() - 1);
                log.info("核销完该组结算计划后amount金额还大于0,剩余金额:{},当前组最后一个结算计划当前已结算金额:{}", amount, contractPayPlanConcludeE.getSettlementAmount());
                //保险起见还是加一个，虽然走到这里一定是从循环第二个if出来的
                contractPayPlanConcludeE.setPaymentStatus(2);
                contractPayPlanConcludeE.setSettlementAmount(contractPayPlanConcludeE.getSettlementAmount().add(amount));
            }
            doHandlePayPlanSettleCalculate(detail, planConcludeList, planWriteOffBoMap);

            doHandlePayCostPlanV3(planWriteOffBoList,payCostPlanMap);
        }
        //批量更新
        contractPayPlanConcludeService.updateBatchById(planConcludes);
        if (CollectionUtils.isNotEmpty(payCostPlanES)) {
            contractPayCostPlanService.updateBatchById(payCostPlanES);
        }
        //处理父级的结算金额，逻辑不变，plannedCollectionAmount字段就是该单子的结算总额，等于结算明细中amount字段的和
        ContractPaySettlementConcludeE pay = contractPaySettlementConcludeMapper.selectById(settleId);
        QueryWrapper<ContractPayPlanConcludeE> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(ContractPayPlanConcludeE.DELETED, 0)
                .eq(ContractPayPlanConcludeE.PID,0)
                .eq(ContractPayPlanConcludeE.CONTRACT_ID, pay.getContractId());
        List<ContractPayPlanConcludeE> contractIncomePlanConcludeVList1 = contractPayPlanConcludeMapper.selectList(queryWrapper1);
        BigDecimal settlementAmount = contractIncomePlanConcludeVList1.get(0).getSettlementAmount();
        contractIncomePlanConcludeVList1.get(0).setSettlementAmount(settlementAmount.add(pay.getPlannedCollectionAmount()));
        contractPayPlanConcludeService.updateById(contractIncomePlanConcludeVList1.get(0));
    }
*/


    private void doHandlePayPlanSettleCalculate(ContractPaySettDetailsE detail, List<ContractPayPlanConcludeE> planConcludeList, Map<String, PlanWriteOffBo> planWriteOffBoMap) {
        // 按照从小到达详细处理核销金额,便于后续成本计划的核销处理
        BigDecimal taxRateAmount = detail.getTaxRateAmount();
        BigDecimal noTaxAmount = detail.getAmountWithOutRate();
        int idx = 0;
        for (ContractPayPlanConcludeE planConcludeE : planConcludeList) {
            idx++;
            PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(planConcludeE.getId());
            if (Objects.isNull(writeOffBo) || !writeOffBo.isWriteOffFlag()){
                continue;
            }
            BigDecimal settlementAmount = planConcludeE.getSettlementAmount();
            String taxRate = planConcludeE.getTaxRate();
            boolean diffTaxFlag = StringUtils.equals(DIFF_TAX_TYPE_NAME, taxRate);
            BigDecimal taxRateNumber = BigDecimal.ZERO;
            if (!diffTaxFlag){
                taxRateNumber = new BigDecimal(taxRate).divide(new BigDecimal(100),2,
                        RoundingMode.HALF_UP);
            }
            // 计算基准的税额和不含税金额-核销口径[预计金额]
            writeOffBo.setSettlementAmount(settlementAmount);
            BigDecimal curSettlementTaxAmount = null;
            BigDecimal curSettlementNoTaxAmount = null;
            // 全额核销
            if (settlementAmount.compareTo(planConcludeE.getPlannedCollectionAmount()) == 0) {
                curSettlementTaxAmount = planConcludeE.getTaxAmount();
                curSettlementNoTaxAmount = planConcludeE.getNoTaxAmount();
            } else {
                // 部分核销 和 超额核销
                if (diffTaxFlag){
                    curSettlementTaxAmount = planConcludeE.getTaxAmount().multiply(
                            settlementAmount.divide(planConcludeE.getPlannedCollectionAmount(), 6,
                                    RoundingMode.HALF_UP)
                    ).setScale(2, RoundingMode.HALF_UP);
                    curSettlementNoTaxAmount = settlementAmount.subtract(curSettlementTaxAmount);
                } else {
                    curSettlementNoTaxAmount = settlementAmount.divide(BigDecimal.ONE.add(taxRateNumber),
                            2, RoundingMode.HALF_UP);
                    curSettlementTaxAmount = settlementAmount.subtract(curSettlementNoTaxAmount);
                }
            }

            if (diffTaxFlag && idx == planConcludeList.size()){
                writeOffBo.setSettlementTaxAmount(taxRateAmount);
                taxRateAmount = BigDecimal.ZERO;
            } else {
                if (taxRateAmount.compareTo(curSettlementTaxAmount) >= 0){
                    taxRateAmount = taxRateAmount.subtract(curSettlementTaxAmount);
                    writeOffBo.setSettlementTaxAmount(curSettlementTaxAmount);
                } else {
                    writeOffBo.setSettlementTaxAmount(taxRateAmount);
                    taxRateAmount = BigDecimal.ZERO;
                }
            }

            if (diffTaxFlag && idx == planConcludeList.size()){
                writeOffBo.setSettlementNoTaxAmount(noTaxAmount);
                noTaxAmount = BigDecimal.ZERO;
            } else {
                if (noTaxAmount.compareTo(curSettlementNoTaxAmount) >= 0){
                    noTaxAmount = noTaxAmount.subtract(curSettlementNoTaxAmount);
                    writeOffBo.setSettlementNoTaxAmount(curSettlementNoTaxAmount);
                } else {
                    writeOffBo.setSettlementNoTaxAmount(noTaxAmount);
                    noTaxAmount = BigDecimal.ZERO;
                }
            }


            planConcludeE.setSettlementTaxAmount(writeOffBo.getSettlementTaxAmount());
            planConcludeE.setSettlementNoTaxAmount(writeOffBo.getSettlementNoTaxAmount());
        }
    }

    /**
     * 单独处理结算单下成本计划的核销
     *
     * @param settleId
     */
    public void singleHandlePayCostPlan(String settleId) {
        log.info("开始结算单核销逻辑,当前结算单id:{}", settleId);
        ContractPaySettlementConcludeE settlement = this.getById(settleId);
        if (!ReviewStatusEnum.已通过.getCode().equals(settlement.getReviewStatus())) {
            return;
        }
        //查关联的结算计划id
        List<ContractPayConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return;
        }
        List<String> planIds = relations.stream()
                .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //查询周期信息
        log.info("当前结算单关联的结算计划id集合:{}", planIds);
        List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }
        log.info("当前结算单关联的结算周期信息:{}", JSON.toJSONString(periodList));
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractPayPlanConcludeE> planConcludes = contractPayPlanConcludeService.queryByCostTimeNotFinished(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return;
        }
        //转换为分组
        Map<String, List<ContractPayPlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
        log.info("根据结算计划id集合和周期信息匹配到的结算计划详情:{}",JSON.toJSONString(planConcludeMap));
        //查关联的结算详情
        List<ContractPaySettDetailsE> settDetails = contractPaySettDetailsService.list(Wrappers.<ContractPaySettDetailsE>lambdaQuery()
                .eq(ContractPaySettDetailsE::getSettlementId, settleId)
                .eq(ContractPaySettDetailsE::getDeleted, 0));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }
        log.info("当前结算单关联的结算详情:{}", JSON.toJSONString(settDetails));
        //汇总所有结算计划id，查询全部的成本计划
        List<String> allCostPlanIds = planConcludes.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
        List<PayCostPlanE> payCostPlanES = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                .in(PayCostPlanE::getPlanId, allCostPlanIds)
                .eq(PayCostPlanE::getDeleted, 0));
        if (CollectionUtils.isEmpty(payCostPlanES)) {
            return;
        }
        //转为map
        Map<String, List<PayCostPlanE>> payCostPlanMap = payCostPlanES.stream().collect(Collectors.groupingBy(PayCostPlanE::getPlanId));
        log.info("结算计划下的成本计划信息:{}", JSON.toJSONString(payCostPlanMap));
        //遍历进行已结算金额处理
        for (ContractPaySettDetailsE detail : settDetails) {
            //获取分组key
            String groupKey = groupKey(detail);
            if (!planConcludeMap.containsKey(groupKey)) {
                continue;
            }
            //获取key对应的结算计划列表
            List<ContractPayPlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
            //核销这批结算计划的成本计划
            handlePayCostPlan(detail, planConcludeList, payCostPlanMap);
        }
        //批量更新
        contractPayPlanConcludeService.updateBatchById(planConcludes); //更新结算计划的成本计划-核销字段
        contractPayCostPlanService.updateBatchById(payCostPlanES);
    }

    /**
     * 批量处理结算单下成本计划的核销
     *
     * @param settleIds
     */
    public void batchHandlePayCostPlan(List<String> settleIds) {
        if (CollectionUtils.isEmpty(settleIds)) {
            return;
        }
        settleIds.forEach(this::singleHandlePayCostPlan);
    }

    /**
     * 核销成本计划V2
     * @param planWriteOffBoList 核销bo集合
     * @param payCostPlanMap 成本计划map key=planId
     **/
    private void doHandlePayCostPlanV2(List<PlanWriteOffBo> planWriteOffBoList,
                                     Map<String, List<PayCostPlanE>> payCostPlanMap) {
        if (CollectionUtils.isEmpty(planWriteOffBoList) || MapUtils.isEmpty(payCostPlanMap)){
            return;
        }
        for (PlanWriteOffBo bo : planWriteOffBoList) {
            if (!bo.isWriteOffFlag()){
                continue;
            }
            List<PayCostPlanE> payCostPlanList = payCostPlanMap.get(bo.getPlanId());
            if (CollectionUtils.isEmpty(payCostPlanList)){
                continue;
            }
            BigDecimal settlementAmount = bo.getSettlementAmount();
            BigDecimal settlementNoTaxAmount = bo.getSettlementNoTaxAmount();
            BigDecimal settlementTaxAmount = bo.getSettlementTaxAmount();
            BigDecimal totalDayLength = BigDecimal.valueOf(bo.getDayLength());
            for (int i = 0; i < payCostPlanList.size(); i++) {
                PayCostPlanE payCostPlan = payCostPlanList.get(i);
                if (i == payCostPlanList.size() - 1){
                    // 最后一个,无脑走尾差
                    payCostPlan.setPaymentAmount(settlementAmount);
                    payCostPlan.setTaxAmount(settlementTaxAmount);
                    payCostPlan.setNoTaxAmount(settlementNoTaxAmount);
                } else {
                    // 按照costStartTime到costEndTime的天数差和bo的天数比值进行均摊,保留两位小数,同时维护三个金额
                    long payCostPlanDayLength = ChronoUnit.DAYS.between(payCostPlan.getCostStartTime(),
                            payCostPlan.getCostEndTime()) + 1;
                    BigDecimal curDayLengthRate = BigDecimal.valueOf(payCostPlanDayLength).divide(totalDayLength, 6,
                            RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementAmount =
                            settlementAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementNoTaxAmount =
                            settlementNoTaxAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementTaxAmount =
                            settlementTaxAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    if (settlementAmount.compareTo(curCostPlanSettlementAmount) >= 0){
                        payCostPlan.setPaymentAmount(curCostPlanSettlementAmount);
                        settlementAmount = settlementAmount.subtract(curCostPlanSettlementAmount);
                    } else {
                        payCostPlan.setPaymentAmount(settlementAmount);
                        settlementAmount = BigDecimal.ZERO;
                    }

                    if (settlementNoTaxAmount.compareTo(curCostPlanSettlementNoTaxAmount) >= 0){
                        payCostPlan.setNoTaxAmount(curCostPlanSettlementNoTaxAmount);
                        settlementNoTaxAmount = settlementNoTaxAmount.subtract(curCostPlanSettlementNoTaxAmount);
                    } else {
                        payCostPlan.setNoTaxAmount(settlementNoTaxAmount);
                        settlementNoTaxAmount = BigDecimal.ZERO;
                    }

                    if (settlementTaxAmount.compareTo(curCostPlanSettlementTaxAmount) >= 0){
                        payCostPlan.setTaxAmount(curCostPlanSettlementTaxAmount);
                        settlementTaxAmount = settlementTaxAmount.subtract(curCostPlanSettlementTaxAmount);
                    } else {
                        payCostPlan.setTaxAmount(settlementTaxAmount);
                        settlementTaxAmount = BigDecimal.ZERO;
                    }
                }
                // 有关后续的核销
                payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                        .compareTo(payCostPlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
            }


        }
    }
    /**
     * 核销成本计划V2
     **/
    private void doHandlePayCostPlanV4(List<PayCostPlanE> payCostPlanOldList,List<ContractPaySettDetailsE> settDetails, List<ContractPayPlanConcludeE> planConcludes, List<PayCostPlanE> payCostPlanNewList) {

        if(CollectionUtils.isEmpty(payCostPlanOldList)){
            return;
        }
        //根据结算单获取费项对应减免金额
        List<ContractPayConcludeSettdeductionV> contractPayConcludeSettdeductionVList = contractPayConcludeSettdeductionService.getDetailsBySettlementId(settDetails.get(0).getSettlementId());
        Map<Long, BigDecimal> sumAmountByChargeItemId = new HashMap<>();
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            sumAmountByChargeItemId = contractPayConcludeSettdeductionVList.stream()
                    .collect(Collectors.groupingBy(
                            ContractPayConcludeSettdeductionV::getChargeItemId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ContractPayConcludeSettdeductionV::getAmount,
                                    BigDecimal::add
                            )
                    ));
        }
        List<Long> chargeItemIds = settDetails.stream().map(ContractPaySettDetailsE::getChargeItemId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            List<Long> removeAll = contractPayConcludeSettdeductionVList.stream().map(ContractPayConcludeSettdeductionV::getChargeItemId).collect(Collectors.toList());
            chargeItemIds = chargeItemIds.stream().filter(chargeItemId -> !removeAll.contains(chargeItemId)).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(chargeItemIds)){
            for(Long chargeItemId : chargeItemIds){
                sumAmountByChargeItemId.put(chargeItemId, BigDecimal.ZERO);
            }
        }
        //计算每条清单计算金额（结算明细金额-对应费项的减免金额）
        Map<String, BigDecimal> funMapChargeAmountMap = new HashMap<>();
        Map<String, BigDecimal> funMapChargeDetailAmountMap = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : sumAmountByChargeItemId.entrySet()) {
            //获取费项对应清单列表
            List<ContractPaySettDetailsE> funSettDetailList = settDetails.stream()
                    .filter(settDetail -> settDetail.getChargeItemId().equals(entry.getKey()))
                    .collect(Collectors.toList());
            //获取同费项清单总金额
            BigDecimal settDetailTotalAmount = funSettDetailList.stream().map(ContractPaySettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> funSettMap = new HashMap<>();
            funSettDetailList.forEach(plan-> {
                funSettMap.put(plan.getId(), plan.getAmount());
            });
            //获取费项对应核销金额
            Map<String, BigDecimal> funSettReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funSettMap, settDetailTotalAmount, entry.getValue());
            funMapChargeDetailAmountMap.putAll(funSettReductionAmountMap);
            funSettDetailList.forEach(plan-> {
                funMapChargeAmountMap.put(plan.getId(), plan.getAmount().subtract(funSettReductionAmountMap.get(plan.getId())));
            });
        }

        //
        for (Map.Entry<String, BigDecimal> entry : funMapChargeAmountMap.entrySet()) {
            ContractPaySettDetailsE settDetailNew = settDetails.stream().filter(settDetail -> settDetail.getId().equals(entry.getKey())).findFirst().get();
            //获取该清单计算金额
            BigDecimal costPlanAmountTotal = entry.getValue();
            BigDecimal funMapChargeDetailAmount = funMapChargeDetailAmountMap.get(entry.getKey());
            //获取该清单对应计划列表
            List<ContractPayPlanConcludeE> payPlanList = planConcludes.stream()
                    .filter(plan -> plan.getContractPayFundId().equals(settDetailNew.getPayFundId()))
                    .collect(Collectors.toList());
            List<String> allCostPlanIds = payPlanList.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
            //获取结算明细下计划对应的成本计划数据
            List<PayCostPlanE> planCostList = payCostPlanOldList.stream().filter(payCostPlanE -> allCostPlanIds.contains(payCostPlanE.getPlanId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(planCostList)){
                continue;
            }
            planCostList = planCostList.stream()
                    .sorted(Comparator.comparing(PayCostPlanE::getCostStartTime))
                    .collect(Collectors.toList());
            LocalDate costStartTime = planCostList.stream()
                    .map(PayCostPlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = planCostList.stream()
                    .map(PayCostPlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,costPlanAmountTotal);
            CommonRangeAmountBO funMapChargeDetailAmountBo = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,funMapChargeDetailAmount);
            if(planCostList.size() <= 3){
                for (int i = 0; i < planCostList.size(); i++) {
                    PayCostPlanE payCostPlan = planCostList.get(i);
                    if(i == 0){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getStartMonthAmount());
                        extracted(payCostPlan);
                    }else if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getEndMonthAmount());
                        extracted(payCostPlan);
                    }else{
                        payCostPlan.setPaymentAmount(settlementAmountBO.getAvgMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getAvgMonthAmount());
                        extracted(payCostPlan);
                    }
                    // 有关后续的核销
                    payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                            .compareTo(payCostPlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
                }
            }else{
                BigDecimal useAmount = BigDecimal.ZERO;
                BigDecimal useChargeAmount = BigDecimal.ZERO;
                //核销月平均金额
                BigDecimal avgPaymentAmount = settlementAmountBO.getAvgMonthAmount();
                //减免月平均金额
                BigDecimal avgChargeAmount = funMapChargeDetailAmountBo.getAvgMonthAmount();
                for (int i = 0; i < planCostList.size(); i++) {
                    PayCostPlanE payCostPlan = planCostList.get(i);
                    if (i == planCostList.size() - 1) {
                        payCostPlan.setPaymentAmount(costPlanAmountTotal.subtract(useAmount));
                        payCostPlan.setReductionAmount(funMapChargeDetailAmount.subtract(useChargeAmount));
                        extracted(payCostPlan);
                    } else {
                        //判断开始日期是否为当月第一天
                        Boolean isFirstDayOfMonth = payCostPlan.getCostStartTime().getDayOfMonth() == 1;
                        //当前月份天数
                        int daysInMonth = payCostPlan.getCostStartTime().lengthOfMonth();
                        //当前时间段占据时间
                        int nowDay;
                        // 如果开始日期不是当月第一天，
                        if (!isFirstDayOfMonth) {
                            //计算开始时间到月底的剩余天数
                            LocalDate lastDayOfMonth = payCostPlan.getCostStartTime().with(TemporalAdjusters.lastDayOfMonth());
                            nowDay = lastDayOfMonth.getDayOfMonth() - payCostPlan.getCostStartTime().getDayOfMonth() + 1;
                        } else {
                            //开始时间是当月第一天
                            //计算结束时间到月初天数
                            LocalDate firstDayOfMonth = payCostPlan.getCostEndTime().with(TemporalAdjusters.firstDayOfMonth());
                            nowDay = payCostPlan.getCostEndTime().getDayOfMonth() - firstDayOfMonth.getDayOfMonth() + 1;
                        }
                        BigDecimal ratio = BigDecimal.valueOf(nowDay)
                                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_UP);

                        BigDecimal thisPaymentAmount = avgPaymentAmount.multiply(ratio).setScale(0, RoundingMode.DOWN);
                        BigDecimal thisReductionmentAmount = avgChargeAmount.multiply(ratio).setScale(0, RoundingMode.DOWN);
                        useAmount = useAmount.add(thisPaymentAmount);
                        useChargeAmount = useChargeAmount.add(thisReductionmentAmount);
                        payCostPlan.setPaymentAmount(thisPaymentAmount);
                        payCostPlan.setReductionAmount(thisReductionmentAmount);
                        extracted(payCostPlan);
                    }
                }
            }

            BigDecimal settlementTaxAmount = settDetailNew.getTaxRateAmount();
            if ("差额纳税".equals(payPlanList.get(0).getTaxRate())) {
                int periodCount = planCostList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        planCostList.get(i).setTaxAmount(taxAmount);
                        planCostList.get(i).setNoTaxAmount(planCostList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
            payCostPlanNewList.addAll(planCostList);
        }

    }
    /**
     * 核销成本计划V2
     * @param planWriteOffBoList 核销bo集合
     * @param payCostPlanMap 成本计划map key=planId
     **/
    private void doHandlePayCostPlanV3(List<PlanWriteOffBo> planWriteOffBoList,
                                     Map<String, List<PayCostPlanE>> payCostPlanMap) {
        if (CollectionUtils.isEmpty(planWriteOffBoList) || MapUtils.isEmpty(payCostPlanMap)){
            return;
        }
        for (PlanWriteOffBo bo : planWriteOffBoList) {
            if (!bo.isWriteOffFlag()){
                continue;
            }
            List<PayCostPlanE> payCostPlanList = payCostPlanMap.get(bo.getPlanId());
            if (CollectionUtils.isEmpty(payCostPlanList)){
                continue;
            }
            LocalDate costStartTime = payCostPlanList.stream()
                    .map(PayCostPlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = payCostPlanList.stream()
                    .map(PayCostPlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            BigDecimal settlementAmount = bo.getSettlementAmount();
            //BigDecimal settlementNoTaxAmount = bo.getSettlementNoTaxAmount();
            BigDecimal settlementTaxAmount = bo.getSettlementTaxAmount();
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementAmount);
            //CommonRangeAmountBO settlementTaxAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementTaxAmount);
            //CommonRangeAmountBO settlementNoTaxAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementNoTaxAmount);
            BigDecimal totalDayLength = BigDecimal.valueOf(bo.getDayLength());
            for (int i = 0; i < payCostPlanList.size(); i++) {
                PayCostPlanE payCostPlan = payCostPlanList.get(i);
                if(i == 0){
                    payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                    extracted(payCostPlan);
                }else if  (i == payCostPlanList.size() - 1){
                    payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                    extracted(payCostPlan);
                }else{
                    payCostPlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                    extracted(payCostPlan);
                }
                // 有关后续的核销
                payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                        .compareTo(payCostPlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
            }
            if ("差额纳税".equals(payCostPlanList.get(0).getTaxRate())) {
                int periodCount = payCostPlanList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        payCostPlanList.get(i).setTaxAmount(taxAmount);
                        payCostPlanList.get(i).setNoTaxAmount(payCostPlanList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
        }
    }

    private static void extracted(PayCostPlanE payCostPlan) {
        if (!"差额纳税".equals(payCostPlan.getTaxRate())) {
            BigDecimal taxRateDecimal = parseTaxRate(payCostPlan.getTaxRate());
            BigDecimal noTaxAmount = payCostPlan.getPaymentAmount().divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
            payCostPlan.setNoTaxAmount(noTaxAmount);
            payCostPlan.setTaxAmount(payCostPlan.getPaymentAmount().subtract(noTaxAmount));
        }
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
     * 核销成本计划
     *
     * @param planConcludes
     */
    private void handlePayCostPlan(ContractPaySettDetailsE detail, List<ContractPayPlanConcludeE> planConcludes, Map<String, List<PayCostPlanE>> payCostPlanMap) {
        if (CollectionUtils.isEmpty(planConcludes) || MapUtils.isEmpty(payCostPlanMap)) {
            return;
        }
        for (ContractPayPlanConcludeE planConclude : planConcludes) {
            String planConcludeId = planConclude.getId();
            if (!payCostPlanMap.containsKey(planConcludeId)) {
                continue;
            }
            log.info("当前处理的结算计划id:{}", planConcludeId);
            //获取该结算计划下的成本计划
            List<PayCostPlanE> payCostPlanList = payCostPlanMap.get(planConcludeId);
            BigDecimal settlementAmount = planConclude.getSettlementAmount();
            if (payCostPlanList.size() == 1) {
                //说明只有1个成本计划
                log.info("结算计划{}下只有一个成本计划,直接设置核销金额", planConcludeId);
                PayCostPlanE singlePlan = payCostPlanList.get(0);
                singlePlan.setPaymentAmount(settlementAmount);
                singlePlan.setSettlementStatus(settlementAmount.compareTo(singlePlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
                handleSingleTaxAmount(detail,singlePlan);
                //将结算计划设置为已核销成本计划
                planConclude.setPayCostFinished(1);
                continue;
            }
            //计算总天数
            long totalDays = ChronoUnit.DAYS.between(planConclude.getCostStartTime(), planConclude.getCostEndTime()) + 1;
            log.info("结算计划{}的总天数是:{}", planConcludeId, totalDays);
            BigDecimal preTotal = BigDecimal.ZERO;
            BigDecimal preTaxAmount = BigDecimal.ZERO;
            for (int i = 0; i < payCostPlanList.size() - 1; i++) {
                PayCostPlanE payCostPlanE = payCostPlanList.get(i);
                //计算该成本计划的天数
                long planDays = ChronoUnit.DAYS.between(payCostPlanE.getCostStartTime(), payCostPlanE.getCostEndTime()) + 1;
                //计算占比
                BigDecimal proportion = BigDecimal.valueOf(planDays).divide(BigDecimal.valueOf(totalDays), 6, RoundingMode.HALF_UP);
                //计算该成本计划核销金额
                BigDecimal settAmount = settlementAmount.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
                log.info("结算计划{}天数:{},占比:{},核销金额:{}", payCostPlanE.getId(), planDays, proportion, settAmount);
                payCostPlanE.setPaymentAmount(settAmount);
                payCostPlanE.setSettlementStatus(settAmount.compareTo(payCostPlanE.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
                handleMiddleTaxAmount(detail, payCostPlanE, proportion);
                preTotal = preTotal.add(settAmount);
                preTaxAmount = preTaxAmount.add(payCostPlanE.getTaxAmount());
            }
            //最后一个成本计划使用减法计算核销金额
            BigDecimal lastSettAmount = settlementAmount.subtract(preTotal);
            PayCostPlanE lastPlan = payCostPlanList.get(payCostPlanList.size() - 1);
            log.info("最后一个收入计划id:{},前面收入计划核销金额总数:{},剩余金额:{}", lastPlan.getId(), preTotal, lastSettAmount);
            lastPlan.setPaymentAmount(lastSettAmount);
            lastPlan.setSettlementStatus(lastSettAmount.compareTo(lastPlan.getPlannedSettlementAmount()) >= 0 ? 2 : 1);
            handleLastTaxAmount(detail, planConclude, lastPlan, preTaxAmount);
            //将结算计划设置为已核销成本计划
            planConclude.setPayCostFinished(1);
        }
    }

    /**
     * 税率转换
     *
     * @param planE
     * @return
     */
    private BigDecimal convertTaxRate(PayCostPlanE planE) {
        if ("差额纳税".equals(planE.getTaxRate())) {
            return BigDecimal.ZERO;
        }
        BigDecimal taxRatePercentNum = new BigDecimal(planE.getTaxRate().replace("%", ""));
        BigDecimal base = new BigDecimal("100.000000");
        return taxRatePercentNum.divide(base, 6, RoundingMode.HALF_UP);
    }

    /**
     * 处理只有一个成本计划时的税额
     *
     * @param detail
     * @param planE
     */
    private void handleSingleTaxAmount(ContractPaySettDetailsE detail, PayCostPlanE planE){
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount() :
                planE.getPaymentAmount().multiply(taxRate).setScale(6, RoundingMode.HALF_UP));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }

    /**
     * 处理非最后一个成本计划的税额
     *
     * @param detail
     * @param planE
     * @param proportion
     */
    private void handleMiddleTaxAmount(ContractPaySettDetailsE detail, PayCostPlanE planE, BigDecimal proportion){
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount().multiply(proportion).setScale(6, RoundingMode.HALF_UP) :
                planE.getPaymentAmount().multiply(taxRate).setScale(6, RoundingMode.HALF_UP));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }

    /**
     * 处理最后一个成本计划的税额
     *
     * @param detail
     * @param planConclude
     * @param planE
     * @param preTaxAmount
     */
    private void handleLastTaxAmount(ContractPaySettDetailsE detail, ContractPayPlanConcludeE planConclude, PayCostPlanE planE, BigDecimal preTaxAmount) {
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount().subtract(preTaxAmount) :
                planConclude.getSettlementAmount().multiply(taxRate).subtract(preTaxAmount));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }

    /**
     * 结算计划分组
     *
     * @param planConcludes
     * @return
     */
    private Map<String, List<ContractPayPlanConcludeE>> getPlanConcludeMap(List<ContractPayPlanConcludeE> planConcludes) {
        return planConcludes.stream().collect(Collectors.groupingBy(this::groupKey,
                Collectors.collectingAndThen(Collectors.toList(),
                        list -> list.stream().sorted(Comparator.comparing(ContractPayPlanConcludeE::getTermDate))
                                .collect(Collectors.toList()))));
    }

    /**
     * 获取分组key，费项+清单+税率
     *
     * @param planConclude
     * @return
     */
    private String groupKey(ContractPayPlanConcludeE planConclude) {
        return planConclude.getContractPayFundId();
//        return planConclude.getChargeItem() + "_" + planConclude.getServiceType() + "_" + planConclude.getTaxRateId();
    }

    /**
     * 获取分组key，费项+清单+税率
     *
     * @param settDetail
     * @return
     */
    private String groupKey(ContractPaySettDetailsE settDetail) {
        return settDetail.getPayFundId();
//        return settDetail.getChargeItem() + "_" + settDetail.getTypeId() + "_" + settDetail.getTaxRateId();
    }

    public ContractPayPlanPeriodV getPlanPeriod(String contractId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        String lastDayOfMonth =  DateUtil.format(calendar.getTime(), "yyyy-MM-dd 23:59:59");
        List<PayPlanPeriodV> payPlanPeriodList = contractPayPlanConcludeMapper.getPlanPeriod(contractId,lastDayOfMonth);

        //查询审批中跟通过结算单
        QueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPaySettlementConcludeE.DELETED, 0)
                .eq(ContractPaySettlementConcludeE.CONTRACTID,contractId)
                .in("reviewStatus", 1,2);
        List<ContractPaySettlementConcludeE> contractSettlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(payPlanPeriodList) && CollectionUtils.isNotEmpty(contractSettlementList)){
            List<String> payPlanPeriodUse = new ArrayList<>();
            //根据结算单查询周期
            for(ContractPaySettlementConcludeE settle: contractSettlementList){
                List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settle.getId());
                periodList.forEach(e -> payPlanPeriodUse.add(e.getStartDate() + "_" + e.getEndDate()));
            }
            //过滤周期中已使用周期
            payPlanPeriodList = payPlanPeriodList.stream()
                    .filter(e -> !payPlanPeriodUse.contains(e.getStartDate() + "_" + e.getEndDate()))
                    .collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(payPlanPeriodList)){
            payPlanPeriodList.stream()
                    .collect(Collectors.groupingBy(PayPlanPeriodV::getType))
                    .forEach((type, list) -> {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setTypeName(SplitModeEnum.parseName( type));
                            list.get(i).setSort(i + 1);
                        }
                    });
        }

        return ContractPayPlanPeriodV.builder().payPlanPeriodList(payPlanPeriodList).build();
    }

    public List<ContractPayPlanForSettlementV> getPlanList(ContractPayPlanListF contractPayPlanListF) {
        List<PayPlanPeriodF> periodList = contractPayPlanListF.getPeriodList();

        List<PayPlanPeriodV> payPlanPeriodList = this.getPlanPeriod(contractPayPlanListF.getContractId()).getPayPlanPeriodList();
        List<Integer> planTypeList = new ArrayList<>();
        Map<String,Integer> payPlanPeriodMap = new HashMap<>();
        payPlanPeriodList.forEach(e -> payPlanPeriodMap.put(e.getStartDate() + "_" + e.getEndDate(), e.getType()));
        //前端交互缺失，代码注释
        //periodList.forEach(e -> planTypeList.add(payPlanPeriodMap.get(e.getStartDate() + "_" + e.getEndDate())));

        // 取periodList的最大的结束时间
        Date endDate = periodList.stream().map(PayPlanPeriodF::getEndDate).max(Date::compareTo).orElse(null);
        List<ContractPayPlanForSettlementV> originPlanList = contractPayPlanConcludeMapper.getOriginPlanList(contractPayPlanListF.getContractId(),endDate,planTypeList);
        if (CollectionUtils.isEmpty(originPlanList)){
            return Collections.emptyList();
        }
        // 20250520 需求 by@YK1730084330JrsAA 李琦, 若某一组的计划都处于部分核销/全额核销,  则该组计划不返回
        List<String> notSettlePlanGroupList = contractPayPlanConcludeMapper.notSettlementGroup(contractPayPlanListF.getContractId());
        originPlanList.removeIf(e -> !notSettlePlanGroupList.contains(e.getSettlePlanGroup()));
        Map<String,Boolean> periodListMap = new HashMap<>();
        periodList.forEach(e -> periodListMap.put(e.getStartDate() + "_" + e.getEndDate(), true));
        originPlanList.forEach(x->x.setIsDefaultCheck(Objects.nonNull(periodListMap.get(x.getCostStartTime()+"_"+x.getCostEndTime())) ? Boolean.TRUE : Boolean.FALSE));
        return originPlanList;
    }

    public PageV<ContractPaySettlementPageV2> pageV2(PageF<SearchF<?>> request) {
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            return PageV.of(request, 0, Collections.emptyList());
        }

        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
            }
        }

        Page<?> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        Field isNKField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "isNK".equals(field.getName()))
                .findFirst().orElse(null);
        request.getConditions().getFields().removeIf(e -> e.getName().equals("isNK"));
        QueryWrapper<?> queryModel = request.getConditions().getQueryModel();
        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio()) && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            queryModel.in("c.departId", orgPermissionV.getOrgIds());
        }
        queryModel.ne("s.pid",'0');
        queryModel.eq("s.deleted",0);
        queryModel.orderByDesc("s.gmtCreate");

        if(Objects.nonNull(isNKField)){
            queryModel.eq("c.deleted",1);
            queryModel.ne("c.pid",0);
            queryModel.in("c.nkStatus",1,2,3);
            queryModel.eq("c.contractType",0);
        }else{
            queryModel.eq("c.deleted",0);
        }
        // 根据搜索条件查询出pid!=0的结算单的pid - 锁定合同
        IPage<String> pids = contractPaySettlementConcludeMapper.getPidsByCondition(pageF, queryModel);
        if(pids.getRecords().size() == 0){
            return PageV.of(request, 0, Collections.emptyList());
        }
        // 这里去掉合同的搜索条件，因为已经锁定了pid
        request.getConditions().getFields().removeIf(e -> e.getName().startsWith("c."));
        request.getConditions().getFields().removeIf(e -> e.getName().equals("isNK"));
        request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
        QueryWrapper<?> queryModel2 = request.getConditions().getQueryModel();
        queryModel2.eq("s.deleted",0);
        List<ContractPaySettlementPageV2> list =
                contractPaySettlementConcludeMapper.selectPageV2ByPids(pids.getRecords(), queryModel2);
        // 获取这些结算单的pid[因为最多只有两级],对应的就是合同的数据
        List<String> settlementIds = list.stream().map(Tree::getId).collect(Collectors.toList());
        Map<String,List<String>> curContractToPlanIdsMap = new HashMap<>();

        Field payableTimeCondition = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) &&"p.plannedCollectionTime".equals(field.getName()))
                .findFirst().orElse(null);


        fillPeriodAndTermDateInfoWithAssemblySummaryData(list, settlementIds, curContractToPlanIdsMap, payableTimeCondition);
        // 合同的数据查询
        List<ContractPaySettlementPageV2> contractList = getContractPaySettlementPageV2List(pids);
        summaryCalculateForContract(curContractToPlanIdsMap, contractList, payableTimeCondition);

//        list.addAll(contractList);

        // 手动树形组装 - 避免框架代码排序混乱
        Map<String, List<ContractPaySettlementPageV2>> listMap = list.stream().collect(Collectors.groupingBy(ContractPaySettlementPageV2::getPid));
        for (ContractPaySettlementPageV2 contractPageV2 : contractList) {
            contractPageV2.setContractBusinessLineName(ContractBusinessLineEnum.parseName(contractPageV2.getContractBusinessLine()));
            //节点-处理时间拼接
            contractPageV2.setContractStartEndDisplay(combineDate(contractPageV2.getContractStartDate(), contractPageV2.getContractEndDate()));
            contractPageV2.setRangeUnsettledStartEndDisplay(combineDate(contractPageV2.getRangeUnsettledStartDate(), contractPageV2.getRangeUnsettledEndDate()));
            List<ContractPaySettlementPageV2> curList = listMap.get(contractPageV2.getId());
            if (CollectionUtils.isNotEmpty(curList)) {
                //子节点处理时间拼接
                curList.forEach(item -> {
                    item.setContractStartEndDisplay(combineDate(item.getContractStartDate(), item.getContractEndDate()));
                    item.setRangeUnsettledStartEndDisplay(combineDate(item.getRangeUnsettledStartDate(), item.getRangeUnsettledEndDate()));
                });
                // list 按照 创建时间倒排序
                curList.sort(Comparator.comparing(ContractPaySettlementPageV2::getGmtCreate).reversed());
                // 应结算金额
                BigDecimal rangeToSettleAmountFromSettlement =
                        curList.stream()
                                .filter(e -> ReviewStatusEnum.已通过.getCode().equals(e.getReviewStatus()))
                                .map(ContractPaySettlementPageV2::getAmountPayable).reduce(BigDecimal.ZERO, BigDecimal::add);
                contractPageV2.setRangeToSettleAmountFromSettlement(rangeToSettleAmountFromSettlement);
                BigDecimal rangeDeductAmountFromSettlement =
                        curList.stream()
                                .filter(e -> ReviewStatusEnum.已通过.getCode().equals(e.getReviewStatus()))
                                .map(ContractPaySettlementPageV2::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                contractPageV2.setRangeDeductAmountFromSettlement(rangeDeductAmountFromSettlement);
                BigDecimal rangeActualSettleAmountFromSettlement =
                        rangeToSettleAmountFromSettlement.subtract(rangeDeductAmountFromSettlement);
                contractPageV2.setRangeActualSettleAmountFromSettlement(rangeActualSettleAmountFromSettlement);
                contractPageV2.setRangeUnsettledAmount(contractPageV2.getRangeAmountPayable().subtract(rangeToSettleAmountFromSettlement));

            }
            contractPageV2.setChildren(curList);
        }

//        List<ContractPaySettlementPageV2> resultList = TreeUtil.treeing(list);
        return PageV.of(request, pids.getTotal(), contractList);
    }

    /**
     * 查询支出合同结算明细分页列表
     * @param req
     * @return
     */
    public PageV<ContractPaySettlementPageV2> exportDetailsPage(PageF<SearchF<?>> req) {
        log.info("结算审批导出,查询合同结算明细入参:{}", JSON.toJSONString(req));
        // 1. 权限校验与基础查询
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            return PageV.of(req, 0, Collections.emptyList());
        }
        // 2. 构建分页和查询条件
        Page<?> pageF = Page.of(req.getPageNum(), req.getPageSize(), req.isCount());
        Field isNKField = req.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "isNk".equals(field.getName()))
                .findFirst().orElse(null);
        req.getConditions().getFields().removeIf(e -> e.getName().equals("isNK"));
        QueryWrapper<?> baseQuery = buildBaseQuery(req, orgPermissionV);
        if(Objects.nonNull(isNKField)){
            baseQuery.eq("c.deleted",1);
            baseQuery.ne("c.pid",0);
            baseQuery.in("c.nkStatus",1,2,3);
            baseQuery.eq("c.contractType",0);
        }else{
            baseQuery.eq("c.deleted",0);
        }
        // 3. 分页查询合同Id
        IPage<String> pidsPage = contractPaySettlementConcludeMapper.getPidsByCondition(pageF, baseQuery);
        if (pidsPage.getRecords().isEmpty()) {
            return PageV.of(req, 0, Collections.emptyList());
        }
        // 4. 这里去掉合同的搜索条件，因为已经锁定了pid
        req.getConditions().getFields().removeIf(e -> e.getName().startsWith("c."));
        req.getConditions().getFields().removeIf(e -> e.getName().equals("isNk"));
        QueryWrapper<?> queryModel2 = req.getConditions().getQueryModel();
        queryModel2.eq("s.deleted",0);
        // 5. 根据合同Id查询入参查询结算单明细
        List<ContractPaySettlementPageV2> settlementPageV2List = contractPaySettlementConcludeMapper.selectPageV2ByPids(pidsPage.getRecords(), queryModel2);
        // 6. 获取这些结算单的pid[因为最多只有两级],对应的就是合同的数据
        List<String> settlementIds = settlementPageV2List.stream().map(Tree::getId).collect(Collectors.toList());
        Map<String,List<String>> curContractToPlanIdsMap = new HashMap<>();
        Field payableTimeCondition = req.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "p.plannedCollectionTime".equals(field.getName()))
                .findFirst().orElse(null);
        fillPeriodAndTermDateInfoWithAssemblySummaryData(settlementPageV2List, settlementIds, curContractToPlanIdsMap, payableTimeCondition);
        // 7. 查询关联合同数据并建立映射
        Map<String, ContractPaySettlementPageV2> contractMap = queryAndMapContracts(pidsPage);
        // 8. 数据关联与补充
        enrichSettlementsWithContracts(settlementPageV2List, contractMap);
        return PageV.of(req, pidsPage.getTotal(), settlementPageV2List);
    }

    // 构建基础查询条件
    private QueryWrapper<?> buildBaseQuery(PageF<SearchF<?>> request, ContractOrgPermissionV orgPermissionV) {
        QueryWrapper<?> query = request.getConditions().getQueryModel();
        query.ne("s.pid", '0')
                .eq("s.deleted", 0)
                .orderByDesc("s.gmtCreate");
        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio())
                && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            query.in("c.departId", orgPermissionV.getOrgIds());
        }
        return query;
    }

    // 查询并映射合同数据
    private Map<String, ContractPaySettlementPageV2> queryAndMapContracts(IPage<String> pidsPage) {
        List<ContractPaySettlementPageV2> contractList = getContractPaySettlementPageV2List(pidsPage);
        return contractList.stream().collect(Collectors.toMap(
                ContractPaySettlementPageV2::getId,
                Function.identity()
        ));
    }

    // 关联合同数据到结算明细
    private void enrichSettlementsWithContracts(
            List<ContractPaySettlementPageV2> settlements,
            Map<String, ContractPaySettlementPageV2> contractMap) {
        settlements.forEach(settlement -> {
            ContractPaySettlementPageV2 contract = contractMap.get(settlement.getPid());
            if (contract != null) {
                // 复制合同关键信息到结算明细（根据实际字段调整）
                settlement.setContractId(contract.getContractNo());
                settlement.setContractRegion(contract.getContractName());
                settlement.setCommunityId(contract.getCommunityId());
                settlement.setCommunityName(contract.getCommunityName());
                settlement.setSettlementTypeName(
                        Optional.ofNullable(settlement.getSettlementType())
                                .map(code -> SettlementTypeEnum.getByCode(code))
                                .map(SettlementTypeEnum::getName)
                                .orElse(null));
                settlement.setContractNo(contract.getContractNo());
                settlement.setContractName(contract.getContractName());
                settlement.setContractCategoryName(contract.getContractCategoryName());
                settlement.setContractServeType(contract.getContractServeType());
                settlement.setContractRegion(contract.getContractRegion());
                settlement.setMerchantName(contract.getMerchantName());
                settlement.setContractAmount(contract.getContractAmount());
                settlement.setContractStartEndDisplay(combineDate(contract.getContractStartDate(), contract.getContractEndDate()));
                settlement.setContractStatusName(contract.getContractStatusName());
                settlement.setSplitModeName(contract.getSplitModeName());
            }
        });
    }

    /**
     * 时间拼接
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private String combineDate(Date startDate, Date endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return null;
        }
        String startDateStr = DateUtil.format(startDate, DatePattern.NORM_DATE_PATTERN);
        String endDateStr = DateUtil.format(endDate, DatePattern.NORM_DATE_PATTERN);
        return startDateStr + "~" + endDateStr;
    }


    /**
     * 汇总数据处理 [未结算 不同字段的概念不一样]
     **/
    private void summaryCalculateForContract(Map<String, List<String>> curContractToPlanIdsMap,
                                             List<ContractPaySettlementPageV2> contractList,
                                             Field payableTimeCondition) {
        if (Objects.isNull(payableTimeCondition)){
            throw new OwlBizException("应结算日期必填");
        }
        Date plannedDateTimeStart = null;
        Date plannedDateTimeEnd = null;

        if (null != payableTimeCondition.getValue()) {
            LocalDateTime[] startAndEnd = WithinDateTimeEnum.valueOf(payableTimeCondition.getValue().toString()).startAndEnd();
            plannedDateTimeStart = Date.from(startAndEnd[0].atZone(ZoneId.systemDefault()).toInstant());
            plannedDateTimeEnd = Date.from(startAndEnd[1].atZone(ZoneId.systemDefault()).toInstant());
        } else if (payableTimeCondition.getMap() != null && !payableTimeCondition.getMap().isEmpty()) {
            plannedDateTimeStart = DateUtil.parse(payableTimeCondition.getMap().get("start").toString(),"yyyy-MM-dd");
            plannedDateTimeEnd = DateUtil.parse(payableTimeCondition.getMap().get("end").toString(),"yyyy-MM-dd");
        }

        QueryWrapper<ContractPayPlanConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(ContractPayPlanConcludeE.DELETED, 0)
                .ne(ContractPayPlanConcludeE.PID, "0")
                .in(ContractPayPlanConcludeE.CONTRACT_ID, contractList.stream().map(ContractPaySettlementPageV2::getContractId).collect(Collectors.toList()))
                .ge(ContractPayPlanConcludeE.PLANNED_COLLECTION_TIME, plannedDateTimeStart)
                .le(ContractPayPlanConcludeE.PLANNED_COLLECTION_TIME, plannedDateTimeEnd);
        List<ContractPayPlanConcludeE> planEntityList = contractPayPlanConcludeMapper.selectList(queryWrapper);

        Map<String, ContractPayPlanConcludeE> planIdToEntityMap = planEntityList.stream().collect(Collectors.toMap(ContractPayPlanConcludeE::getId, e -> e));
        Map<String, List<ContractPayPlanConcludeE>> allContractToPlanIdsMap = planEntityList.stream().collect(Collectors.groupingBy(ContractPayPlanConcludeE::getContractId));
        for (ContractPaySettlementPageV2 contractPageV : contractList) {
            // 结算单关联的结算计划
            List<String> curPlanIdList = curContractToPlanIdsMap.get(contractPageV.getContractId());
            List<ContractPayPlanConcludeE> contractPayPlanConcludeES = allContractToPlanIdsMap.get(contractPageV.getContractId());
            if (CollectionUtils.isEmpty(contractPayPlanConcludeES)){
                contractPageV.setRangeAmountPayable(BigDecimal.ZERO);
                contractPageV.setRangeSettledAmount(BigDecimal.ZERO);
                contractPageV.setRangeUnsettledAmount(BigDecimal.ZERO);
                contractPageV.setRangeUnsettledStartDate(null);
                contractPageV.setRangeUnsettledEndDate(null);
                contractPageV.setRangeUnsettledTermDate(null);
            } else {
                List<String> allPlanId = contractPayPlanConcludeES.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList());
                BigDecimal curRangeAmountPayable = BigDecimal.ZERO;
                BigDecimal curRangeSettledAmount = BigDecimal.ZERO;
                List<Integer> curRangeUnsettledTermDateList = Lists.newArrayList();
                // 拿出所有的成本预估计划，并按照费项目+清单项目+税率分组，后续对照使用
                List<ContractPayPlanConcludeE> curPlanEntityList = planEntityList.stream().filter(e -> allPlanId.contains(e.getId())).collect(Collectors.toList());
                Map<String, List<ContractPayPlanConcludeE>> curContractPlanGroup = curPlanEntityList.stream()
                        .collect(Collectors.groupingBy(e -> e.getChargeItemId() + "_" + e.getServiceType() + "_" + e.getTaxRateId()));
                int groupSize = curContractPlanGroup.size();
                Map<String, List<SettlementPlanBO>> boMap = Maps.newHashMap();

                for (ContractPayPlanConcludeE plan : contractPayPlanConcludeES) {
                    // 应结算金额 [都加]
                    curRangeAmountPayable = curRangeAmountPayable.add(plan.getPlannedCollectionAmount());
                    // 已结算金额 [都加]
                    curRangeSettledAmount = curRangeSettledAmount.add(plan.getSettlementAmount());

                    // if (Objects.nonNull(plan.getPaymentStatus()) && plan.getPaymentStatus() == 0){
                    // 周期内未关联的计划 都认为是未结算,计算 未结算周期和期数
                    if (CollUtil.isEmpty(curPlanIdList) || !curPlanIdList.contains(plan.getId())) {
                        // 维护期数分组
                        String key = plan.getSettlePlanGroup();
                        List<SettlementPlanBO> curBoList1 = boMap.getOrDefault(key, Lists.newArrayList());
                        curBoList1.add(new SettlementPlanBO(plan.getCostStartTime(), plan.getCostEndTime()));
                        boMap.put(key, curBoList1);

                    }
                }
                SettlementPlanBO settlementPlanBO = this.calcDate(boMap);
                // 未结算期数合计、周期 = 分组后,count(未结算) 的最大值
                contractPageV.setRangeAmountPayable(curRangeAmountPayable);
                contractPageV.setRangeSettledAmount(curRangeSettledAmount);
                contractPageV.setRangeUnsettledStartDate(DateUtil.date(settlementPlanBO.getStartTime()));
                contractPageV.setRangeUnsettledEndDate(DateUtil.date(settlementPlanBO.getEndTime()));
                contractPageV.setRangeUnsettledTermDate(settlementPlanBO.getRangeUnsettledTermDate());
            }
        }
    }

    /**
     * 计算
     * @param map
     */
    private SettlementPlanBO calcDate(Map<String, List<SettlementPlanBO>> map) {
        if (CollUtil.isEmpty(map)) {
            return new SettlementPlanBO();
        }
        // 1. 找出周期数量最多的key(s)
        int maxSize = map.values().stream()
                .mapToInt(List::size)
                .max()
                .orElseThrow();

        List<String> keysWithMaxSize = map.entrySet().stream()
                .filter(entry -> entry.getValue().size() == maxSize)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 2. 如果只有一个key数量最多，直接返回它的最大时间范围
        if (keysWithMaxSize.size() == 1) {
            String key = keysWithMaxSize.get(0);
            return this.getMaxRange(map.get(key));
        }
        // 3. 如果多个key数量相同，找出它们中时间跨度最大的
        else {
            Map.Entry<String, SettlementPlanBO> result = keysWithMaxSize.stream()
                    .collect(Collectors.toMap(
                            key -> key,
                            key -> this.getMaxRange(map.get(key))
                    ))
                    .entrySet().stream()
                    .max(Comparator.comparingLong(
                            entry -> this.calculateDays(entry.getValue().getStartTime(), entry.getValue().getEndTime())
                    ))
                    .orElseThrow();

            return result.getValue();
        }
    }

    /**
     * 计算一个key下所有周期的最大时间范围（最早开始到最晚结束）
     */
    private SettlementPlanBO getMaxRange(List<SettlementPlanBO> plans) {
        LocalDate minStart = plans.stream()
                .map(SettlementPlanBO::getStartTime)
                .min(LocalDate::compareTo)
                .orElseThrow();
        LocalDate maxEnd = plans.stream()
                .map(SettlementPlanBO::getEndTime)
                .max(LocalDate::compareTo)
                .orElseThrow();
        return new SettlementPlanBO(minStart, maxEnd, plans.size());
    }

    /**
     * 计算时间跨度（天数）
     */
    private static long calculateDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) + 1; // 包含结束日
    }

    @NotNull
    private List<ContractPaySettlementPageV2> getContractPaySettlementPageV2List(IPage<String> pids) {
        QueryWrapper<ContractPaySettlementConcludeE> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(ContractPaySettlementConcludeE.DELETED, 0)
                .eq(ContractPaySettlementConcludeE.PID,0)
                .in(ContractPaySettlementConcludeE.ID, pids.getRecords());
        // 根据合同Id查询结算表中结算总计记录
        List<ContractPaySettlementConcludeE> contractSettlementList = contractPaySettlementConcludeMapper.selectList(queryWrapper1);
        if (CollectionUtils.isEmpty(contractSettlementList)){
            throw new OwlBizException("结算单数据异常,缺失合同数据");
        }
        // 将合同Id去重
        List<String> contractIdList = contractSettlementList.stream()
                .map(ContractPaySettlementConcludeE::getContractId)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        // 根据合同Id查询合同记录
        List<ContractPaySettlementPageV2> contractList = contractPaySettlementConcludeMapper.selectPageV2OfContract(contractIdList);
        // 合同对象要组装id 和 pid
        Map<String, String> contractIdToSettlementIdMap = contractSettlementList.stream()
                .collect(Collectors.toMap(ContractPaySettlementConcludeE::getContractId, ContractPaySettlementConcludeE::getId));

        //获取合同管理类别
        List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);
        contractList.forEach(e -> {
            e.setId(contractIdToSettlementIdMap.get(e.getContractId()));
            e.setPid("0");
            e.setContractStatusName(ContractRevStatusEnum.parseName(e.getContractStatusCode()));
            //合同管理类别汉化
            if(StringUtils.isNotEmpty(e.getContractCategoryName())) {
                DictionaryCode conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(e.getContractCategoryName())).findFirst().orElse(new DictionaryCode());
                //合同管理类别描述
                e.setContractCategoryName(conmanagetypeDict.getName());
            }
            if (StringUtils.isNotBlank(e.getSplitMode())){
                Set<String> splitModeName = Sets.newHashSet();
                for (String s : e.getSplitMode().split(",")) {
                    try{
                        splitModeName.add(SplitModeEnum.parseName(Integer.valueOf(s)));
                    } catch (Exception ex){
                        log.error("合同结算周期转换异常,合同结算周期:{}", s);
                    }
                }
                e.setSplitModeName(String.join(",", splitModeName));
            }
        });
        // 使用合同对象的id 按照pids的顺序对合同进行排序
        List<String> sortedContractIdList = pids.getRecords();
        contractList.sort((o1, o2) -> {
            int io1 = sortedContractIdList.indexOf(o1.getId());
            int io2 = sortedContractIdList.indexOf(o2.getId());
            if (io1 != -1){
                io1 = sortedContractIdList.size() - io1;
            }
            if (io2 != -1){
                io2 = sortedContractIdList.size() - io2;
            }
            return io2 - io1;
        });
        return contractList;
    }

    /**
     * 字段补全，期数=清单项-费项-期数{逗号分隔}, 计量周期= 开始-结束{逗号分隔}
     **/
    private void fillPeriodAndTermDateInfoWithAssemblySummaryData(List<ContractPaySettlementPageV2> list,
                                                                  List<String> settlementIds,
                                                                  Map<String,List<String>> contractToPlanIdsMap,
                                                                  Field payableTimeCondition) {
        List<SettlementSimpleStr> periodSimpleStrList = settlementPeriodMapper.getSimpleStrList(settlementIds);
        Map<String, String> settlementIdToPeriodStrMap = periodSimpleStrList.stream()
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, SettlementSimpleStr::getStr));

        List<SettlementSimpleStr> termDateSimpleStrList =  settlementPlanRelationMapper.getSimpleStrList(settlementIds);
        Map<String, String> settlementIdToTermDateStrMap = termDateSimpleStrList.stream()
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, SettlementSimpleStr::getStr));

        if (Objects.isNull(payableTimeCondition)){
            throw new OwlBizException("应结算日期必填");
        }
        Date plannedDateTimeStart = null;
        Date plannedDateTimeEnd = null;

        if (null != payableTimeCondition.getValue()) {
            LocalDateTime[] startAndEnd = WithinDateTimeEnum.valueOf(payableTimeCondition.getValue().toString()).startAndEnd();
            plannedDateTimeStart = Date.from(startAndEnd[0].atZone(ZoneId.systemDefault()).toInstant());
            plannedDateTimeEnd = Date.from(startAndEnd[1].atZone(ZoneId.systemDefault()).toInstant());
        } else if (payableTimeCondition.getMap() != null && !payableTimeCondition.getMap().isEmpty()) {
            plannedDateTimeStart = DateUtil.parse(payableTimeCondition.getMap().get("start").toString(),"yyyy-MM-dd");
            plannedDateTimeEnd = DateUtil.parse(payableTimeCondition.getMap().get("end").toString(),"yyyy-MM-dd");
        }

        if (Objects.isNull(plannedDateTimeStart) || Objects.isNull(plannedDateTimeEnd)){
            throw new OwlBizException("应结算日期的开始时间和结束时间必填");
        }
        // 筛选成本计划 只用应结算日期筛选
        List<SettlementSimpleStr> settlementIdToPlanIdStrList =
                settlementPlanRelationMapper.getSettlementPlanIdSimpleStrList(settlementIds, plannedDateTimeStart, plannedDateTimeEnd);
        Map<String, List<String>> settlementIdToPlanIdListMap = settlementIdToPlanIdStrList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getStr()))
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, e -> Arrays.asList(e.getStr().split(","))));


        list.forEach(e -> {
            e.setPeriodsStr(settlementIdToPeriodStrMap.get(e.getId()));
            e.setTermDateStr(settlementIdToTermDateStrMap.get(e.getId()));
            e.setReviewStatusName(ReviewStatusEnum.parseName(e.getReviewStatus()));
            e.setInvoiceStatusName(InvoiceStatusEnum.parseName(e.getInvoiceStatus()));
            e.setPaymentMethodName(PaymentMethodEnum.parseName(e.getPaymentMethod()));
            e.setPaymentStatusName(PaymentStatusEnum.parseName(e.getPaymentStatus()));
            e.setSettlementTypeName(Optional.ofNullable(e.getSettlementType())
                    .map(code -> SettlementTypeEnum.getByCode(code))
                    .map(SettlementTypeEnum::getName)
                    .orElse(null));
            String payFundNumber = e.getPayFundNumber();
            if (StringUtils.isNotBlank(payFundNumber)) {
                int lastDashIndex = payFundNumber.lastIndexOf('-');
                int secondLastDashIndex = payFundNumber.lastIndexOf('-', lastDashIndex - 1);
                if (lastDashIndex == -1 || secondLastDashIndex == -1) {
                    e.setPayFundNumberCapture(null);
                }else {
                    e.setPayFundNumberCapture(payFundNumber.substring(secondLastDashIndex + 1));
                }
            }
            if (ReviewStatusEnum.已通过.getCode().equals(e.getReviewStatus())) {
                List<String> curPlanIdList = settlementIdToPlanIdListMap.get(e.getId());
                if (CollectionUtils.isNotEmpty(curPlanIdList)){
                    List<String> curPlanId = contractToPlanIdsMap.getOrDefault(e.getContractId(), Lists.newArrayList());
                    curPlanId.addAll(curPlanIdList);
                    curPlanId = curPlanId.stream().distinct().collect(Collectors.toList());
                    contractToPlanIdsMap.put(e.getContractId(), curPlanId);
                }
            }
        });
    }

    public ContractPaySettlementFileInfoV fileInfo(String settlementId) {
        ContractPaySettlementConcludeE settlement = contractPaySettlementConcludeMapper.selectById(settlementId);
        if (Objects.isNull(settlement)){
            throw new OwlBizException("结算单不存在");
        }
        ContractPaySettlementFileInfoV fileInfoV = new ContractPaySettlementFileInfoV();
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AttachmentE.BUSINESS_ID,settlementId)
                .eq(AttachmentE.BUSINESS_TYPE, 1001)
                .eq(AttachmentE.DELETED, false);
        List<AttachmentE> attachmentES = attachmentMapper.selectList(queryWrapper);
        List<AttachmentV> attachmentVS = Global.mapperFacade.mapAsList(attachmentES, AttachmentV.class);
        // 合同数量确认单 type = 2
        List<AttachmentV> quantityFiles = attachmentVS.stream().filter(e -> e.getType() == 2).collect(Collectors.toList());
        // 合同结算表 type = 3
        List<AttachmentV> settlementFiles = attachmentVS.stream().filter(e -> e.getType() == 3).collect(Collectors.toList());
        // 其他 type = 4
        List<AttachmentV> otherFiles = attachmentVS.stream().filter(e -> e.getType() == 4).collect(Collectors.toList());

        fileInfoV.setQuantityFiles(quantityFiles);
        fileInfoV.setSettlementFiles(settlementFiles);
        fileInfoV.setOtherFiles(otherFiles);

        if (StringUtils.isNotBlank(settlement.getContractId())){
            QueryWrapper<AttachmentE> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq(AttachmentE.BUSINESS_ID,settlement.getContractId())
                    .eq(AttachmentE.BUSINESS_TYPE, 1002)
                    .eq(AttachmentE.DELETED, false);
            List<AttachmentE> attachmentES2 = attachmentMapper.selectList(queryWrapper2);
            List<AttachmentV> attachmentVS2 = Global.mapperFacade.mapAsList(attachmentES2, AttachmentV.class);
            fileInfoV.setContractFiles(attachmentVS2);
        }

        return fileInfoV;
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateSettlementStep(String settlementId, Integer step) {
        int updateResult = contractPaySettlementConcludeMapper.updateSettlementStep(settlementId, step);
        // 第三步，更新审批状态为待提交
        if (step == 3){
            //校验附件（合同数量签认单、合同结算表）是否存在
            extracted(settlementId);
            contractPaySettlementConcludeMapper.updateReviewStatus(settlementId, ReviewStatusEnum.待提交.getCode());
        }
        return "更新成功";
    }


    public List<String> checkSettleStatus(List<String> planIdList) {
        return contractPaySettlementConcludeMapper.checkSettleStatus(planIdList);
    }
    public List<String> getSettlementByPlan(List<String> planIdList) {
        return contractPaySettlementConcludeMapper.getSettlementByPlan(planIdList);
    }

    public List<String> queryBySettleId(String settleId) {
        return contractPaySettlementConcludeMapper.queryBySettleId(settleId);
    }

    /**
     * 财务结算表单消息回显
     *
     * @param id
     * @return
     */
    public ContractPaySettlementSimpleInfoV settlementSimpleInfo(String id) {
        ContractPaySettlementSimpleInfoV simpleInfoV = new ContractPaySettlementSimpleInfoV();
        ContractPaySettlementConcludeE settlement = this.getById(id);
        if (ObjectUtils.isEmpty(settlement)) {
            return simpleInfoV;
        }
        simpleInfoV.setId(settlement.getId());
        simpleInfoV.setPayFundNumber(settlement.getPayFundNumber());
        simpleInfoV.setMerchantName(settlement.getMerchantName());
        ContractPayConcludeE payConclude = contractPayConcludeMapper.selectById(settlement.getContractId());
        if (ObjectUtils.isNotEmpty(payConclude)) {
            simpleInfoV.setContractNo(payConclude.getContractNo());
            simpleInfoV.setContractName(payConclude.getContractNo());
        }
        simpleInfoV.setRealSettlementAmount(settlement.getActualSettlementAmount());
        log.info("财务结算表单消息回显simpleInfoV:{}", JSON.toJSONString(simpleInfoV));
        return simpleInfoV;
    }


    public List<ContractPayPlanConcludeE> handlePayPlanStateForBill(String settleId) {
        log.info("开始结算单核销逻辑,当前结算单id:{}", settleId);



        //查关联的结算计划id
        List<ContractPayConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return null;
        }
        List<String> planIds = relations.stream()
                .map(ContractPayConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //查询周期信息
        log.info("当前结算单关联的结算计划id集合:{}", planIds);
        List<PayPlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return null;
        }
        log.info("当前结算单关联的结算周期信息:{}", JSON.toJSONString(periodList));
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractPayPlanConcludeE> planConcludes = contractPayPlanConcludeService.queryByCostTimeForBill(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return null;
        }
        return planConcludes;

    }

    public List<ContractPaySettlementF> listSettleByCondition(PaySettlementQueryF queryF) {
        List<ContractPaySettlementF> contractPaySettlementFS = new ArrayList<>();
        List<ContractPaySettlementConcludeE> contractPaySettlementConcludeEList = this.lambdaQuery()
                .eq(ContractPaySettlementConcludeE::getDeleted,0)
                .eq(ContractPaySettlementConcludeE::getContractId,queryF.getContractId())
                .eq(ContractPaySettlementConcludeE::getApplyStatus,1)
                .ne(ContractPaySettlementConcludeE::getPid,0)
                .list();
        contractPaySettlementConcludeEList.forEach(settlement->{
            ContractPaySettlementF contractPaySettlementF = new ContractPaySettlementF();
            contractPaySettlementF.setSettlementId(settlement.getId());
            contractPaySettlementF.setTitle(settlement.getTitle());
            contractPaySettlementFS.add(contractPaySettlementF);
        });
        if (StringUtils.isNotBlank(queryF.getContractId()) && CollectionUtils.isEmpty(queryF.getSettlementIdList())) {
            return contractPaySettlementFS;
        }
        if (CollectionUtils.isNotEmpty(queryF.getSettlementIdList())) {
            Map<String,ContractPaySettlementF> contractPaySettlementFMap = contractPaySettlementFS.stream().collect(Collectors.toMap(ContractPaySettlementF::getSettlementId, e -> e,(e1,e2)->e1));
            List<ContractPaySettlementF> contractPaySettlementFList = contractPaySettlementConcludeMapper.calculateTotalAmount(queryF.getSettlementIdList());
            if (CollectionUtils.isNotEmpty(contractPaySettlementFList)) {
                contractPaySettlementFList.forEach(settlement -> {
                    ContractPaySettlementF contractPaySettlementF = contractPaySettlementFMap.get(settlement.getSettlementId());
                    settlement.setTitle(contractPaySettlementF.getTitle());
                });
            }
            return contractPaySettlementFList;

        }
        return contractPaySettlementFS;
    }


    public Boolean updateStatus(ContractSettlementStatusF contractSettlementStatusF) {
        List<ContractPaySettlementConcludeE> contractPaySettlementConcludeEList = this.lambdaQuery().in(ContractPaySettlementConcludeE::getId,contractSettlementStatusF.getSettlementIdList()).list();
        if (CollectionUtils.isEmpty(contractPaySettlementConcludeEList)) {
            return false;
        }
        contractPaySettlementConcludeEList.forEach(settlement-> settlement.setApplyStatus(contractSettlementStatusF.getApplyStatus()));
        this.updateBatchById(contractPaySettlementConcludeEList);
        return true;
    }

    public List<String> getAllContractId(String id) {
        ContractPaySettlementConcludeE contractPaySettlementConcludeE = this.getById(id);
        if (Objects.isNull(contractPaySettlementConcludeE)){
            throw new OwlBizException("结算单不存在,请检查");
        }
        if (StringUtils.isBlank(contractPaySettlementConcludeE.getContractId())){
            throw new OwlBizException("结算单关联合同数据异常,请检查");
        }
        LambdaQueryWrapper<ContractPayConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeE::getPid,contractPaySettlementConcludeE.getContractId());
        queryWrapper.eq(ContractPayConcludeE::getContractType,ContractTypeEnum.补充协议.getCode());
        queryWrapper.eq(ContractPayConcludeE::getDeleted,0);
        List<ContractPayConcludeE> childContractList = contractPayConcludeMapper.selectList(queryWrapper);
        List<String> list = Lists.newArrayList();
        list.add(contractPaySettlementConcludeE.getContractId());
        if (CollectionUtils.isNotEmpty(childContractList)){
            list.addAll(childContractList.stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()));
        }
        return list;
    }

    //更新结算单中其他附件-业务事由
    public void updateOtherBusinessReasons(String settlementId, String otherBusinessReasons, String externalDepartmentCode, Integer calculationMethod){
        contractPaySettlementConcludeMapper.updateOtherBusinessReasons(settlementId,otherBusinessReasons, externalDepartmentCode, calculationMethod);
    }

    //根据结算单ID获取财务结算中其他附件信息
    public ContractSettlementInvoiceOtherFileF getFinanceOtherDetail(String id){
        ContractSettlementInvoiceOtherFileF result = new ContractSettlementInvoiceOtherFileF();
        ContractPaySettlementConcludeE settlement = contractPaySettlementConcludeMapper.selectById(id);
        if (Objects.isNull(settlement)){
            throw new OwlBizException("结算单不存在");
        }
        ContractPayConcludeE payConclude = contractPayConcludeMapper.selectById(settlement.getContractId());
        result.setOtherBusinessReasons(settlement.getOtherBusinessReasons());
        if(StringUtils.isBlank(settlement.getOtherBusinessReasons())){
            List<PayPlanPeriodV> contractPaySettlementPeriodVList = settlementPeriodMapper.getPeriodList(id);
            String remark = "【业财合同】" + payConclude.getCommunityName()+"-"+payConclude.getName();
            if(CollectionUtils.isNotEmpty(contractPaySettlementPeriodVList)){
                contractPaySettlementPeriodVList = contractPaySettlementPeriodVList.stream()
                        .sorted(Comparator.comparing(PayPlanPeriodV::getEndDate).reversed())
                        .collect(Collectors.toList());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
                remark = remark + "-" +sdf.format(contractPaySettlementPeriodVList.get(0).getEndDate());
            }
            remark = remark + "-成本确认-￥" + settlement.getActualSettlementAmount()+"元";
            result.setOtherBusinessReasons(remark);
        }
        QueryWrapper<AttachmentE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AttachmentE.BUSINESS_ID,id)
                .eq(AttachmentE.BUSINESS_TYPE, FileBusinessTypeEnum.FINANCE_OTHER_FILE.getCode())
                .eq(AttachmentE.DELETED, false);
        List<AttachmentE> attachmentES = attachmentMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(attachmentES)){
            result.setFileList(attachmentES);
        }
        result.setDepartmentList(configFeignClient.getDeportList(Collections.singletonList(payConclude.getCommunityId())));
        result.setExternalDepartmentCode(settlement.getExternalDepartmentCode());
        result.setCalculationMethod(settlement.getCalculationMethod());
        if(Objects.isNull(result.getCalculationMethod()) && StringUtils.isNotEmpty(payConclude.getOurPartyId())){
            //根据我方单位ID查询对应法定单位信息
            OrgFinanceRv orgFinanceById = orgFeignClient.getOrgFinanceById(Long.valueOf(payConclude.getOurPartyId()));
            log.info("根据法定单位id：{}，查询对应法定单位信息：{}", payConclude.getOurPartyId(), orgFinanceById);
            if(Objects.nonNull(orgFinanceById)){
                //简单计税
                result.setCalculationMethod(2);
                //一般纳税人
                if(Objects.isNull(orgFinanceById.getTaxpayerType()) || orgFinanceById.getTaxpayerType() == 2){
                    //一般计税
                    result.setCalculationMethod(1);
                }
            }
        }
        return result;
    }

    public boolean refresh3(PageF<SearchF<?>> req) {
        log.info("开始执行刷数3", JSON.toJSONString(req));
        // 1. 构建分页查询条件
        Page<?> pageF = Page.of(req.getPageNum(), req.getPageSize(), req.isCount());
        QueryWrapper<?> baseQuery = req.getConditions().getQueryModel();
        baseQuery.ne("s.pid", '0')
                .eq("s.deleted", 0)
                .orderByDesc("s.gmtCreate");
        // 2. 分页查询合同ID
        IPage<String> pidsPage = contractPaySettlementConcludeMapper.getPidsByCondition(pageF, baseQuery);
        if (pidsPage.getRecords().isEmpty()) {
            log.error("查询合同Id异常");
        }
        // 3. 清理合同搜索条件
        req.getConditions().getFields().removeIf(e -> e.getName().startsWith("c."));
        QueryWrapper<?> queryModel2 = req.getConditions().getQueryModel();
        queryModel2.eq("s.deleted", 0);
        // 4. 根据合同ID查询结算单明细
        List<ContractPaySettlementPageV2> list = contractPaySettlementConcludeMapper.selectPageV2ByPids(pidsPage.getRecords(), queryModel2);
        // 5. 剔除所有contractId重复的记录，只保留查询出来只有一条contractId的记录
        Map<String, Long> contractIdCountMap = list.stream()
                .collect(Collectors.groupingBy(
                        ContractPaySettlementPageV2::getContractId,
                        Collectors.counting()
                ));
        list = list.stream()
                .filter(record -> contractIdCountMap.get(record.getContractId()) == 1)
                .collect(Collectors.toList());
        log.info("剔除后的结算单明细集合:{}", JSON.toJSONString(list));

        // 6. 剔除所有totalSettledAmount为空的数据
        list = list.stream()
                .filter(record -> record.getTotalSettledAmount() != null)
                .collect(Collectors.toList());

        // 7. 获取需要更新的结算单实体列表Id
        List<String> settlementIds = list.stream()
                .map(ContractPaySettlementPageV2::getId)
                .collect(Collectors.toList());
        log.info("剔除totalSettledAmount为空的记录后，剩余记录数:{}", JSON.toJSONString(settlementIds));
        List<ContractPaySettlementConcludeE> entitiesToUpdate = this.lambdaQuery()
                .eq(ContractPaySettlementConcludeE::getDeleted, 0)
                .in(ContractPaySettlementConcludeE::getId, settlementIds)
                .list();
        // 8. 构建ID到实体的映射以快速查找
        Map<String, ContractPaySettlementConcludeE> entityMap = entitiesToUpdate.stream()
                .collect(Collectors.toMap(ContractPaySettlementConcludeE::getId, e -> e));
        // 9. 计算并更新累计应结算金额
        for (ContractPaySettlementPageV2 settlement : list) {
            ContractPaySettlementConcludeE entity = entityMap.get(settlement.getId());
            if (entity == null) {
                log.warn("未找到对应结算单记录，ID: {}", settlement.getId());
                continue;
            }
            BigDecimal amountPayable = Optional.ofNullable(settlement.getTotalSettledAmount()).orElse(BigDecimal.ZERO);
            BigDecimal contractTotal = Optional.ofNullable(settlement.getDeductionAmount()).orElse(BigDecimal.ZERO);
            entity.setTotalSettlementAmount(amountPayable.add(contractTotal));
        }
        // 10. 分批更新数据库
        List<List<ContractPaySettlementConcludeE>> batches = com.google.common.collect.Lists.partition(entitiesToUpdate, 500);
        // 11. 单条循环更新并记录
        int totalRecords = entitiesToUpdate.size();
        int successCount = 0;
        int errorCount = 0;
        log.info("开始单条更新，共需处理 {} 条记录", totalRecords);
        for (int i = 0; i < entitiesToUpdate.size(); i++) {
            ContractPaySettlementConcludeE entity = entitiesToUpdate.get(i);
            try {
                // 执行单条更新
                boolean isUpdated = this.updateById(entity);
                if (isUpdated) {
                    successCount++;
                    if ((i + 1) % 100 == 0) { // 每100条打印进度
                        log.info("已处理 {}/{} 条，成功率：{:.2f}%",
                                i + 1, totalRecords,
                                (successCount * 100.0 / (i + 1)));
                    }
                } else {
                    errorCount++;
                    log.warn("更新失败 [序号：{}][ID：{}] 未找到匹配记录",
                            i + 1, entity.getId());
                }
            } catch (Exception e) {
                errorCount++;
                log.error("更新异常 [序号：{}][ID：{}] 错误信息：{}",
                        i + 1, entity.getId(), e.getMessage());
                // 可在此处添加错误记录到特定集合，用于后续处理
            }
        }
        log.info("处理完成！共成功 {} 条，失败 {} 条，成功率：{}%",
                successCount, errorCount,
                (successCount * 100.0 / totalRecords));
        return Boolean.TRUE;
    }

    public boolean refresh4(PageF<SearchF<?>> req) {
        log.info("开始执行刷数3", JSON.toJSONString(req));
        // 1. 构建分页查询条件
        Page<?> pageF = Page.of(req.getPageNum(), req.getPageSize(), req.isCount());
        QueryWrapper<?> baseQuery = req.getConditions().getQueryModel();
        baseQuery.ne("s.pid", '0')
                .eq("s.deleted", 0)
                .orderByDesc("s.gmtCreate");
        // 2. 分页查询合同ID
        IPage<String> pidsPage = contractPaySettlementConcludeMapper.getPidsByCondition(pageF, baseQuery);
        log.info("1.分页查询合同ID:{}", JSON.toJSONString(pidsPage));

        if (pidsPage.getRecords().isEmpty()) {
            log.error("查询合同Id异常");
        }
        // 3. 清理合同搜索条件
        req.getConditions().getFields().removeIf(e -> e.getName().startsWith("c."));
        QueryWrapper<?> queryModel2 = req.getConditions().getQueryModel();
        queryModel2.eq("s.deleted",0);
        // 4. 根据合同ID查询结算单明细
        List<ContractPaySettlementPageV2> list = contractPaySettlementConcludeMapper.selectPageV2ByPids(pidsPage.getRecords(), queryModel2);
        log.info("2.根据合同ID查询结算单明细:{}", JSON.toJSONString(list));
        // 5. 根据list集合中,contractId为多条查询出来pid的记录
        Map<String, List<String>> contractIdToPidsMap = list.stream()
                .collect(Collectors.groupingBy(
                        ContractPaySettlementPageV2::getContractId,
                        Collectors.mapping(ContractPaySettlementPageV2::getPid, Collectors.toList())
                ));
        // 获取有多个pid的contractId
        List<String> multiPidContractIds = contractIdToPidsMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("3.有多条pid记录的contractIds: {}", JSON.toJSONString(multiPidContractIds));
        // 6. 剔除所有contractId重复的记录，只保留查询出来只有一条contractId的记录
        Map<String, Long> contractIdCountMap = list.stream()
                .collect(Collectors.groupingBy(
                        ContractPaySettlementPageV2::getContractId,
                        Collectors.counting()
                ));
        list = list.stream()
                .filter(record -> contractIdCountMap.get(record.getContractId()) == 1)
                .collect(Collectors.toList());
        log.info("4.剔除后的结算单明细集合:{}", JSON.toJSONString(list));

        // 7. 剔除所有totalSettledAmount为空的数据
        list = list.stream()
                .filter(record -> record.getTotalSettledAmount() != null)
                .collect(Collectors.toList());

        // 8. 获取需要更新的结算单实体列表Id
        List<String> settlementIds = list.stream()
                .map(ContractPaySettlementPageV2::getId)
                .collect(Collectors.toList());
        log.info("5.剔除totalSettledAmount为空的记录后，剩余记录数:{}", JSON.toJSONString(settlementIds));
        List<ContractPaySettlementConcludeE> entitiesToUpdate = this.lambdaQuery()
                .eq(ContractPaySettlementConcludeE::getDeleted, 0)
                .in(ContractPaySettlementConcludeE::getId, settlementIds)
                .list();
        // 9. 构建ID到实体的映射以快速查找
        Map<String, ContractPaySettlementConcludeE> entityMap = entitiesToUpdate.stream()
                .collect(Collectors.toMap(ContractPaySettlementConcludeE::getId, e -> e));
        // 10. 计算并更新累计应结算金额
        for (ContractPaySettlementPageV2 settlement : list) {
            ContractPaySettlementConcludeE entity = entityMap.get(settlement.getId());
            if (entity == null) {
                log.warn("未找到对应结算单记录，ID: {}", settlement.getId());
                continue;
            }
            BigDecimal amountPayable = Optional.ofNullable(settlement.getTotalSettledAmount()).orElse(BigDecimal.ZERO);
            BigDecimal contractTotal = Optional.ofNullable(settlement.getDeductionAmount()).orElse(BigDecimal.ZERO);
            entity.setTotalSettlementAmount(amountPayable.add(contractTotal));
        }
        // 11. 分批更新数据库
        List<List<ContractPaySettlementConcludeE>> batches = com.google.common.collect.Lists.partition(entitiesToUpdate, 500);
        log.info("6.需要更新的记录数据:{}", JSON.toJSONString(entitiesToUpdate));
        return Boolean.TRUE;
    }

    //根据id删除结算审批
    @Transactional
    public Boolean deletedPaySettlementById(String id) {

        ContractPaySettlementConcludeE settlement = contractPaySettlementConcludeMapper.selectById(id);
        if (Objects.isNull(settlement)){
            throw new OwlBizException("结算单不存在");
        }
        ContractPayConcludeE payConcludeE = contractPayConcludeMapper.selectById(settlement.getContractId());
        if(Objects.isNull(payConcludeE)){
            throw new OwlBizException("该合同不存在");
        }
        payConcludeE.setStatus(ContractRevStatusEnum.正在履行.getCode());
        contractPayConcludeMapper.updateById(payConcludeE);
        //查询结算单关联的结算计划
        List<String> planIdList = contractPaySettlementConcludeMapper.getPlanBySettlement(id);
        List<String> costPlanIdList = contractPaySettlementConcludeMapper.queryBySettleId(id);
        if(CollectionUtils.isNotEmpty(costPlanIdList)){
            QueryWrapper<PayCostPlanE> queryModel = new QueryWrapper<>();
            queryModel.eq("contractId", settlement.getContractId());
            queryModel.in("id", costPlanIdList);
            queryModel.eq("deleted",0);
            List<PayCostPlanE> payCostPlanEList = contractPayCostPlanService.list(queryModel);
            if(CollectionUtils.isNotEmpty(payCostPlanEList)){
                List<String> billIdList = payCostPlanEList.stream().map(PayCostPlanE::getBillId).collect(Collectors.toList());
                //[校验]根据临时账单ID获取报账单数据-实签
                String message = financeFeignClient.getVoucherBillSq(billIdList, payConcludeE.getCommunityId());
                if(StringUtils.isNotEmpty(message)){
                    throw new OwlBizException(message);
                }
            }
        }
        for(String planId:planIdList){
            ContractPayPlanConcludeE planConcludeE = contractPayPlanConcludeMapper.selectById(planId);
            QueryWrapper<PayCostPlanE> queryModel = new QueryWrapper<>();
            queryModel.eq("contractId", settlement.getContractId());
            queryModel.eq("planId", planId);
            queryModel.eq("deleted",0);
            List<PayCostPlanE> payCostPlanEList = contractPayCostPlanService.list(queryModel);
            if(CollectionUtils.isNotEmpty(payCostPlanEList)){

                ContractPayPlanForSettlementV2 fundV = contractPayPlanConcludeMapper.getPlanListByPlanId(planId);
                //获取成本原始数据，对指定数据进行核销
                List<PayCostPlanE> payCostPlanNewList = contractPayPlanConcludeService.splitByMonth(planConcludeE,payConcludeE,fundV);
                for(PayCostPlanE cost : payCostPlanEList){
                    PayCostPlanE newCost = payCostPlanNewList.stream().filter(x->cost.getCostStartTime().equals(x.getCostStartTime()) && cost.getCostEndTime().equals(x.getCostEndTime())).findFirst()
                            .orElse(new PayCostPlanE());
                    cost.setPaymentAmount(Objects.nonNull(newCost.getPaymentAmount()) ? newCost.getPaymentAmount() : BigDecimal.ZERO);
                    cost.setReductionAmount(Objects.nonNull(newCost.getReductionAmount()) ? newCost.getReductionAmount() : BigDecimal.ZERO);
                    cost.setNoTaxAmount(Objects.nonNull(newCost.getNoTaxAmount()) ? newCost.getNoTaxAmount() : BigDecimal.ZERO);
                    cost.setTaxAmount(Objects.nonNull(newCost.getTaxAmount()) ? newCost.getTaxAmount() : BigDecimal.ZERO);
                    cost.setSettlementStatus(newCost.getSettlementStatus());
                }
                contractPayCostPlanService.updateBatchById(payCostPlanEList);
                List<ResultTemporaryChargeBillF> billList = new ArrayList<>();
                payCostPlanEList.forEach(plan->{
                    ResultTemporaryChargeBillF updateTemporaryChargeBillF = new ResultTemporaryChargeBillF();
                    updateTemporaryChargeBillF.setReductionAmount(BigDecimal.ZERO);
                    updateTemporaryChargeBillF.setId(String.valueOf(plan.getBillId()));
                    updateTemporaryChargeBillF.setReceivableAmount(Long.getLong("0"));
                    updateTemporaryChargeBillF.setSupCpUnitId(payConcludeE.getCommunityId());
                    updateTemporaryChargeBillF.setExtField7(null);
                    updateTemporaryChargeBillF.setTaxAmountNew(plan.getTaxAmount().multiply(new BigDecimal("100")));
                    billList.add(updateTemporaryChargeBillF);
                });
                //根据临时账单ID还原对应临时账单数据及删除合同报账单
                financeFeignClient.deleteReceivableBillAndVoucher(billList);
            }
        }
        //还原结算计划
        contractPayPlanConcludeMapper.restoreplan(planIdList);
        //删除结算单
        contractPaySettlementConcludeMapper.deletedSettlement(id);

        //查询对应的结算单明细信息
        List<ContractPaySettDetailsE> contractPaySettlementConcludeEList = contractPaySettDetailsService.list(new QueryWrapper<ContractPaySettDetailsE>()
                .eq("settlementId", id)
                .eq(ContractPayFundE.DELETED, 0)
                .isNotNull("cbApportionId"));

        ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(settlement.getContractId());
        Boolean isNK = Boolean.FALSE;
        if(Objects.nonNull(nkConclude) && !NkStatusEnum.已关闭.getCode().equals(nkConclude.getNkStatus())){
            isNK = Boolean.TRUE;
            log.info("该合同含有NK合同");
        }
        if(CollectionUtils.isNotEmpty(contractPaySettlementConcludeEList) && !isNK){
            log.info("该合同无NK合同，对其成本进行释放");
            contractPayBusinessService.releasePaySettlementCost(settlement,payConcludeE,contractPaySettlementConcludeEList.stream().map(ContractPaySettDetailsE::getCbApportionId).collect(Collectors.toList()));
        }

        LambdaQueryWrapper<ContractPaySettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPaySettlementConcludeE::getContractId, settlement.getContractId())
                .eq(ContractPaySettlementConcludeE::getDeleted,0);
        List<ContractPaySettlementConcludeE> contractPayPlanConcludeEList = contractPaySettlementConcludeMapper.selectList(queryWrapper);
        if(contractPayPlanConcludeEList.size() == 1 && contractPayPlanConcludeEList.get(0).getPid().equals("0")){
            contractPaySettlementConcludeMapper.deletedSettlement(contractPayPlanConcludeEList.get(0).getId());
        }
        return Boolean.TRUE;
    }
}
