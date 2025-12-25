package com.wishare.contract.apps.service.revision.pay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.wishare.contract.apps.fo.revision.pay.report.*;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.SpaceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.vo.SpaceCommunityUserV;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.bo.CommonRangeDateBO;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.enums.revision.NkStatusEnum;
import com.wishare.contract.domains.enums.revision.ServeTypeEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayPlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPaySettlementConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPaySettDetailsMapper;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementPeriodAmountV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayYjSubListV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hhb
 * @describe
 * @date 2025/5/28 15:06
 */
@Service
@Slf4j
public class ContractPayReportService implements IOwlApiBase {

    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;
    @Autowired
    private ContractPayFundMapper contractPayFundMapper;
    @Autowired
    private ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private UserFeignClient userFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private SpaceFeignClient spaceFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private OrgEnhanceComponent orgEnhanceComponent;
    @Autowired
    private ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;
    @Autowired
    private ContractPaySettDetailsMapper contractPaySettDetailsMapper;

    //根据条件获取统计表数据
    public List<ContractPayReportListV> getTotalPayReportList(@RequestBody ContractPayReportF reportF){
        try {
            List<ContractPayReportListV> resultList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(reportF.getPlannedCollectionTime())){
                reportF.setPlannedCollectionTimeStart(reportF.getPlannedCollectionTime().get(0));
                reportF.setPlannedCollectionTimeEnd(reportF.getPlannedCollectionTime().get(1));
            }
            if(CollectionUtils.isNotEmpty(reportF.getPaymentDate())){
                reportF.setPaymentDateSatrt(reportF.getPaymentDate().get(0));
                reportF.setPaymentDateEnd(reportF.getPaymentDate().get(1));
            }
            reportF.setTenantId(tenantId());
            log.info("【未结算台账】获取用户ID：{}",userId());
            UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
            log.info("【未结算台账】获取用户状态：{}",JSONArray.toJSON(userStateRV));
            boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
            UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
            log.info("【未结算台账】获取用户通用信息：{}",JSONArray.toJSON(userInfoRv));
            if (userInfoRv.getOrgIds().contains(13554968509111L)) {
                superAccount = true;
                reportF.setSuperAccount(Boolean.TRUE);
            }
            if (!superAccount) {
                Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
                log.info("【未结算台账】获取用户组织信息：{}",JSONArray.toJSON(orgListByOrgId));
                reportF.setOrgList(orgListByOrgId.stream().collect(Collectors.toList()));
            }
            log.info("【未结算台账】查询报表信息入参：{}",JSONArray.toJSON(reportF));
            List<ContractPayDataListD> totalList = contractPayConcludeMapper.getTotalPayReportList(reportF);
            if(CollectionUtils.isEmpty(totalList)){
                return resultList;
            }
            List<String> bcNoCfContractIdList = new ArrayList<>();
            bcNoCfContractIdList.add("补充合同未拆分");
            //获取所有合同ID
            List<String> contractIdList = totalList.stream().map(ContractPayDataListD :: getContractId).collect(Collectors.toList());
            //获取YJ合同对应主合同
            List<String> yjContractIdList = totalList.stream().filter(e -> NkStatusEnum.getNkStatusList().contains(e.getNkStatus())).map(ContractPayDataListD :: getYjPidContractId).collect(Collectors.toList());
            Map<String,String> yjBcContractId = new HashMap<>();
            yjBcContractId.put("原合同ID","YJ合同ID");
            if(CollectionUtils.isNotEmpty(yjContractIdList)){
                contractIdList.addAll(yjContractIdList);
                yjBcContractId = totalList.stream().filter(e -> NkStatusEnum.getNkStatusList().contains(e.getNkStatus()) && StringUtils.isNotBlank(e.getYjPidContractId()) && !e.getYjPidContractId().equals("0"))
                        .collect(Collectors.toMap(
                                ContractPayDataListD::getYjPidContractId,
                                ContractPayDataListD::getContractId
                        ));
            }
            //查询对应合同的补充合同数据
            LambdaQueryWrapper<ContractPayConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPayConcludeE::getPid, contractIdList)
                    .eq(ContractPayConcludeE::getContractType, 2)
                    .eq(ContractPayConcludeE::getDeleted,0);
            List<ContractPayConcludeE> bcPlanConcludeList = contractPayConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(bcPlanConcludeList)){
                //查询所有合同的计划
                LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                        .in(ContractPayPlanConcludeE::getContractId, contractIdList)
                        .eq(ContractPayPlanConcludeE::getDeleted,0);
                List<ContractPayPlanConcludeE> planList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);

                contractIdList.addAll(bcPlanConcludeList.stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()));
                //查询所有合同的清单
                LambdaQueryWrapper<ContractPayFundE> queryFunWrapper = new LambdaQueryWrapper<>();
                queryFunWrapper.in(ContractPayFundE::getContractId, contractIdList)
                        .eq(ContractPayFundE::getDeleted,0);
                List<ContractPayFundE> funList = contractPayFundMapper.selectList(queryFunWrapper);

                Map<String, List<String>> contractIdMap = bcPlanConcludeList.stream()
                        .collect(Collectors.groupingBy(
                                ContractPayConcludeE::getPid,
                                Collectors.mapping(
                                        ContractPayConcludeE::getId,
                                        Collectors.toList()
                                )
                        ));
                for (Map.Entry<String, List<String>> entry : contractIdMap.entrySet()){
                    String key = entry.getKey();
                    List<String> value = entry.getValue();
                    List<ContractPayFundE> mainFunList = funList.stream()
                            .filter(item -> item.getContractId().equals(key))
                            .collect(Collectors.toList());
                    List<ContractPayFundE> bcFunList = funList.stream()
                            .filter(item -> value.contains(item.getContractId()))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(bcFunList)){
                        bcNoCfContractIdList.add( key);
                        if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                            bcNoCfContractIdList.add( yjBcContractId.get(key));
                        }
                        continue;
                    }
                    Map<String, ContractPayFundE> bcFunMap = bcFunList.stream()
                            .collect(Collectors.toMap(
                                    this::generateCompositeKey,
                                    fund -> fund,
                                    (fund1, fund2) -> fund1
                            ));
                    Map<String, ContractPayFundE> mainFunMap = mainFunList.stream()
                            .collect(Collectors.toMap(
                                    this::generateCompositeKey,
                                    fund -> fund,
                                    (fund1, fund2) -> fund1
                            ));
                    for (String bckey : bcFunMap.keySet()) {
                        if (!mainFunMap.containsKey(bckey)) {
                            bcNoCfContractIdList.add( key);
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                                bcNoCfContractIdList.add( yjBcContractId.get(key));
                            }
                            break;
                        }
                        ContractPayFundE mainFund = mainFunMap.get(bckey);
                        List<ContractPayPlanConcludeE> funPLanList = planList.stream()
                                .filter(item -> item.getContractId().equals(key) && item.getContractPayFundId().equals(mainFund.getId()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isEmpty(funPLanList)){
                            bcNoCfContractIdList.add( key);
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                                bcNoCfContractIdList.add( yjBcContractId.get(key));
                            }
                            break;
                        }
                    }
                }
            }
            totalList.forEach(detail -> detail.setBcwcfNum(bcNoCfContractIdList.contains(detail.getContractId()) ? 1 : 0));
            //根据条件获取支出报表底数-已发生未结算数据
            Map<String, List<ContractPayReportDetailListV>> planNoMapList = this.getPlanNoMapList( reportF.getPaymentDateSatrt(),
                    reportF.getPaymentDateEnd(),
                    reportF.getPlannedCollectionTimeStart(),
                    reportF.getPlannedCollectionTimeEnd(),
                    totalList.stream().map(ContractPayDataListD :: getContractId).collect(Collectors.toList()));
            //重新封装未结算期数
            totalList.forEach(detail -> {
                List<ContractPayReportDetailListV> planList = planNoMapList.get(detail.getContractId());
                if(CollectionUtils.isNotEmpty(planList)){
                    //【封装】已发生未结算-未结算期数合计
                    detail.setNoSettlementNum(planList.get(0).getYfswjsPeriodsTotal());
                }
            });

            //获取所有合同计划List
            List<String> allPlanIdList = totalList.stream()
                    .map(ContractPayDataListD::getPlanIdList)
                    .filter(Objects::nonNull)
                    .flatMap(planIdStr -> Arrays.stream(planIdStr.split(",")))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            //获取所有合同结算单应结算金额
            List<ContractPayDataListD> totalSettlementList = contractPayConcludeMapper.getTotalPayReportSettlementList(allPlanIdList,reportF.getPaymentDateSatrt(),reportF.getPaymentDateEnd());
            Map<String, List<ContractPayDataListD>> settlementByContractId = totalSettlementList.stream().collect(Collectors.groupingBy(ContractPayDataListD::getContractId));
            ContractPayReportListV totalData = new ContractPayReportListV();
            //根据区域对数据进行分组
            Map<String, List<ContractPayDataListD>> regionMapList = totalList.stream().collect(Collectors.groupingBy(ContractPayDataListD::getRegion));

            List<String> totalContractList = new ArrayList<>();
            List<String> totalContractNoSplitList = new ArrayList<>();
            List<String> totalSbyfContractList = new ArrayList<>();
            List<String> totalSbyfContractThreeList = new ArrayList<>();
            List<String> totalSbyfContractFiveList = new ArrayList<>();

            for (Map.Entry<String, List<ContractPayDataListD>> entry : regionMapList.entrySet()) {
                ContractPayReportListV regionData = new ContractPayReportListV();
                //【封装】区域
                regionData.setRegion(entry.getKey());
                //重新封装合同数据
                List<ContractPayDataListD> contractList = new ArrayList<>();
                //该区域下所有合同数据
                List<ContractPayDataListD> contractMapList = entry.getValue();
                for (ContractPayDataListD contractData : contractMapList) {
                    // 根据合同ID获取对应的结算单数据
                    List<ContractPayDataListD> settlements = settlementByContractId.get(contractData.getContractId());
                    // 将结算单应结算金额设置到合同数据中
                    if (CollectionUtils.isNotEmpty(settlements)) {
                        contractData.setShouldPlannedCollectionAmount(settlements.get(0).getShouldPlannedCollectionAmount()); // 假设ContractPayDataListD有setSettlementList方法
                    }
                    //若该合同数据中的应结算金额等于该合同数据中的结算金额，则跳过该合同数据
                    if(Objects.nonNull(contractData.getPlannedCollectionAmount()) && Objects.nonNull(contractData.getShouldPlannedCollectionAmount()) && contractData.getPlannedCollectionAmount().compareTo(contractData.getShouldPlannedCollectionAmount()) == 0){
                        continue;
                    }
                    //若该合同数据中的已发生未结算期数为0，则跳过该合同数据
                    if(Objects.isNull(contractData.getNoSettlementNum()) || contractData.getNoSettlementNum() == 0){
                        continue;
                    }
                    //未结算金额 = 计划结算金额-应结算金额
                    contractData.setNoSettlementAmount(Objects.nonNull(contractData.getPlannedCollectionAmount()) && Objects.nonNull(contractData.getShouldPlannedCollectionAmount()) ? contractData.getPlannedCollectionAmount().subtract(contractData.getShouldPlannedCollectionAmount()) : contractData.getPlannedCollectionAmount());
                    //签约单位信息转化
                    /*if(StringUtils.isNotBlank(contractData.getQydws())){
                        List<ContractQydwsF> qydwsFS = JSONObject.parseArray(contractData.getQydws(),ContractQydwsF.class);
                        if(CollectionUtils.isNotEmpty(qydwsFS)) {
                            contractData.setQydwsList(qydwsFS);
                        }
                    }*/
                    contractList.add(contractData);
                }
                //【封装】汇总-未结算金额
                regionData.setTotalNoSettlementAmountNum(contractList.stream().map(ContractPayDataListD::getNoSettlementAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                regionData.setTotalNoSettlementAmount(regionData.getTotalNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                totalData.setTotalNoSettlementAmountNum(totalData.getTotalNoSettlementAmountNum().add(regionData.getTotalNoSettlementAmountNum()));
                //【封装】汇总-合同数量
                regionData.setTotalContractNum(contractList.size());
                totalData.setTotalContractNum(totalData.getTotalContractNum() + regionData.getTotalContractNum());
                //汇总-合同数量对应合同
                regionData.setContractList(contractList.stream().map(ContractPayDataListD::getContractId).collect(Collectors.toList()));
                totalContractList.addAll(regionData.getContractList());
                //totalData.getContractList().addAll(regionData.getContractList());
                //【封装】汇总-项目数量
                regionData.setTotalProjectNum(contractList.stream().map(ContractPayDataListD::getCommunityId).filter(Objects::nonNull).distinct().count());
                totalData.setTotalProjectNum(totalData.getTotalProjectNum() + regionData.getTotalProjectNum());
                //【封装】汇总-供方数量
                regionData.setTotalSupplierNum(contractList.stream().map(ContractPayDataListD::getQydws).filter(Objects::nonNull).distinct().count());
                totalData.setTotalSupplierNum(totalData.getTotalSupplierNum() + regionData.getTotalSupplierNum());
                //【封装】汇总-补充协议未拆分数量
                regionData.setTotalNoSplitNum(contractList.stream().filter(contractData -> contractData.getBcwcfNum() > 0).count());
                totalData.setTotalNoSplitNum(totalData.getTotalNoSplitNum() + regionData.getTotalNoSplitNum());
                //汇总-补充协议未拆分数量对应合同
                regionData.setContractNoSplitList(contractList.stream().filter(contractData -> contractData.getBcwcfNum() > 0).map(ContractPayDataListD::getContractId).collect(Collectors.toList()));
                totalContractNoSplitList.addAll(regionData.getContractNoSplitList());
                //totalData.getContractNoSplitList().addAll(regionData.getContractNoSplitList());
                //-----四保一服数据------
                contractList = contractList.stream().filter(c -> ServeTypeEnum.SBYF.getCode().equals(c.getContractServeType()) && c.getNoSettlementNum() > 0).collect(Collectors.toList());
                //【封装】四保一服-未结算金额
                regionData.setSbyfNoSettlementAmountNum(contractList.stream().map(ContractPayDataListD::getNoSettlementAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                regionData.setSbyfNoSettlementAmount(regionData.getSbyfNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                totalData.setSbyfNoSettlementAmountNum(totalData.getSbyfNoSettlementAmountNum().add(regionData.getSbyfNoSettlementAmountNum()));
                //【封装】四保一服-合同数量
                regionData.setSbyfContractNum(contractList.size());
                totalData.setSbyfContractNum(totalData.getSbyfContractNum() + regionData.getSbyfContractNum());
                //四保一服-合同数量对应合同
                regionData.setSbyfContractList(contractList.stream().map(ContractPayDataListD::getContractId).collect(Collectors.toList()));
                totalSbyfContractList.addAll(regionData.getSbyfContractList());
                //totalData.getSbyfContractList().addAll(regionData.getSbyfContractList());
                //【封装】四保一服-项目数量
                regionData.setSbyfProjectNum(contractList.stream().map(ContractPayDataListD::getCommunityId).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfProjectNum(totalData.getSbyfProjectNum() + regionData.getSbyfProjectNum());
                //【封装】四保一服-供方数量
                regionData.setSbyfSupplierNum(contractList.stream().map(ContractPayDataListD::getQydws).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfSupplierNum(totalData.getSbyfSupplierNum() + regionData.getSbyfSupplierNum());

                //-----四保一服(超3个月及以上)数据------
                contractList = contractList.stream().filter(c -> ServeTypeEnum.SBYF.getCode().equals(c.getContractServeType()) && c.getNoSettlementNum() >= 3).collect(Collectors.toList());
                //【封装】四保一服(超3个月及以上)-未结算金额
                regionData.setSbyfThreeNoSettlementAmountNum(contractList.stream().map(ContractPayDataListD::getNoSettlementAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                regionData.setSbyfThreeNoSettlementAmount(regionData.getSbyfThreeNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                totalData.setSbyfThreeNoSettlementAmountNum(totalData.getSbyfThreeNoSettlementAmountNum().add(regionData.getSbyfThreeNoSettlementAmountNum()));
                //【封装】四保一服(超3个月及以上)-合同数量
                regionData.setSbyfThreeContractNum(contractList.size());
                totalData.setSbyfThreeContractNum(totalData.getSbyfThreeContractNum() + regionData.getSbyfThreeContractNum());
                //四保一服(超3个月及以上)-合同数量对应合同
                regionData.setSbyfContractThreeList(contractList.stream().map(ContractPayDataListD::getContractId).collect(Collectors.toList()));
                totalSbyfContractThreeList.addAll(regionData.getSbyfContractThreeList());
                //totalData.getSbyfContractThreeList().addAll(regionData.getSbyfContractThreeList());
                //【封装】四保一服(超3个月及以上)-项目数量
                regionData.setSbyfThreeProjectNum(contractList.stream().map(ContractPayDataListD::getCommunityId).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfThreeProjectNum(totalData.getSbyfThreeProjectNum() + regionData.getSbyfThreeProjectNum());
                //【封装】四保一服(超3个月及以上)-供方数量
                regionData.setSbyfThreeSupplierNum(contractList.stream().map(ContractPayDataListD::getQydws).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfThreeSupplierNum(totalData.getSbyfThreeSupplierNum() + regionData.getSbyfThreeSupplierNum());

                //-----四保一服(超5个月及以上)数据------
                contractList = contractList.stream().filter(c -> ServeTypeEnum.SBYF.getCode().equals(c.getContractServeType()) && c.getNoSettlementNum() >= 5).collect(Collectors.toList());
                //【封装】四保一服(超5个月及以上)-未结算金额
                regionData.setSbyfFiveNoSettlementAmountNum(contractList.stream().map(ContractPayDataListD::getNoSettlementAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                regionData.setSbyfFiveNoSettlementAmount(regionData.getSbyfFiveNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                totalData.setSbyfFiveNoSettlementAmountNum(totalData.getSbyfFiveNoSettlementAmountNum().add(regionData.getSbyfFiveNoSettlementAmountNum()));
                //【封装】四保一服(超5个月及以上)-合同数量
                regionData.setSbyfFiveContractNum(contractList.size());
                totalData.setSbyfFiveContractNum(totalData.getSbyfFiveContractNum() + regionData.getSbyfFiveContractNum());
                //四保一服(超5个月及以上)-合同数量对应合同
                regionData.setSbyfContractFiveList(contractList.stream().map(ContractPayDataListD::getContractId).collect(Collectors.toList()));
                totalSbyfContractFiveList.addAll(regionData.getSbyfContractFiveList());
                //totalData.getSbyfContractFiveList().addAll(regionData.getSbyfContractFiveList());
                //【封装】四保一服(超5个月及以上)-项目数量
                regionData.setSbyfFiveProjectNum(contractList.stream().map(ContractPayDataListD::getCommunityId).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfFiveProjectNum(totalData.getSbyfFiveProjectNum() + regionData.getSbyfFiveProjectNum());
                //【封装】四保一服(超5个月及以上)-供方数量
                regionData.setSbyfFiveSupplierNum(contractList.stream().map(ContractPayDataListD::getQydws).filter(Objects::nonNull).distinct().count());
                totalData.setSbyfFiveSupplierNum(totalData.getSbyfFiveSupplierNum() + regionData.getSbyfFiveSupplierNum());

                resultList.add(regionData);
            }

            totalData.setTotalNoSettlementAmount(totalData.getTotalNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalData.setSbyfNoSettlementAmount(totalData.getSbyfNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalData.setSbyfThreeNoSettlementAmount(totalData.getSbyfThreeNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalData.setSbyfFiveNoSettlementAmount(totalData.getSbyfFiveNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
            //根据区域名称进行排序
            resultList = resultList.stream().sorted(Comparator.comparing(ContractPayReportListV::getRegion)).collect(Collectors.toList());
            totalData.setContractList(totalContractList);
            totalData.setContractNoSplitList(totalContractNoSplitList);
            totalData.setSbyfContractList(totalSbyfContractList);
            totalData.setSbyfContractThreeList(totalSbyfContractThreeList);
            totalData.setSbyfContractFiveList(totalSbyfContractFiveList);
            totalData.setRegion("合计");
            resultList.add(totalData);
            return resultList;
        } catch (Exception ex) {
            log.error("根据条件获取统计表数据异常：{}", ex.getMessage());
            log.error("根据条件获取统计表明细数据异常：{}", ex);
            throw new OwlBizException("根据条件获取统计表数据异常："+ ex.getMessage());
        }
    }

    private String generateCompositeKey(ContractPayFundE fund) {
        return fund.getChargeItemId() + "|" +
                nullToEmpty(fund.getType()) + "|" +
                nullToEmpty(fund.getPayWayId()) + "|" +
                nullToEmpty(fund.getTaxRateId()) + "|" +
                nullToEmpty(fund.getPayTypeId()) + "|" +
                (fund.getStandAmount() != null ? fund.getStandAmount().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getStandardId())+ "|" +
                fund.getIsHy()+ "|" +
                nullToEmpty(fund.getBcContractId());
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    //根据条件导出统计表数据
    public PageV<ContractPayReportListV> exportTotalPayReportList(PageF<SearchF<?>> request){

        ContractPayReportF reportSelect = this.getReportDetailSearch(request);
        List<ContractPayReportListV> resultList = this.getTotalPayReportList(reportSelect);
        return PageV.of(request, resultList.size(), resultList);
    }
    //根据条件获取统计表明细数据
    public PageV<ContractPayReportDetailListV> getDetailPayReportList(PageF<SearchF<?>> request){
        try {
            Page<ContractPayReportF> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

            ContractPayReportF reportSelect = this.getReportDetailSearch(request);

            if(CollectionUtils.isEmpty(reportSelect.getContractList())){
                return PageV.of(request, 0, new ArrayList<>());
            }

            //根据条件查询数据
            IPage<ContractPayReportDetailListV> detailList = contractPayConcludeMapper.getDetailPayReportList(pageF,
                    reportSelect.getPaymentDateSatrt(),
                    reportSelect.getPaymentDateEnd(),
                    reportSelect.getPlannedCollectionTimeStart(),
                    reportSelect.getPlannedCollectionTimeEnd(),
                    reportSelect.getContractList(),
                    reportSelect.getIsNK());
            if(CollectionUtils.isEmpty(detailList.getRecords())){
                return PageV.of(request, 0, new ArrayList<>());
            }
            Map<String,String> bcNoCfContractMap = new HashMap<>();
            //获取所有合同ID
            List<String> contractIdList = detailList.getRecords().stream().map(ContractPayReportDetailListV :: getContractId).collect(Collectors.toList());
            List<String> yjContractIdList = detailList.getRecords().stream().filter(e -> NkStatusEnum.getNkStatusList().contains(e.getNkStatus())).map(ContractPayReportDetailListV :: getYjPidContractId).collect(Collectors.toList());
            Map<String,String> yjBcContractId = new HashMap<>();
            yjBcContractId.put("原合同ID","YJ合同ID");
            if(CollectionUtils.isNotEmpty(yjContractIdList)){
                contractIdList.addAll(yjContractIdList);
                yjBcContractId = detailList.getRecords().stream().filter(e -> NkStatusEnum.getNkStatusList().contains(e.getNkStatus()))
                        .filter(e -> !"0".equals(e.getYjPidContractId()))
                        .collect(Collectors.toMap(
                                ContractPayReportDetailListV::getYjPidContractId,
                                ContractPayReportDetailListV::getContractId
                        ));
            }
            //查询对应合同的补充合同数据
            LambdaQueryWrapper<ContractPayConcludeE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ContractPayConcludeE::getPid, contractIdList)
                    .eq(ContractPayConcludeE::getContractType, 2)
                    .eq(ContractPayConcludeE::getDeleted,0);
            List<ContractPayConcludeE> bcPlanConcludeList = contractPayConcludeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(bcPlanConcludeList)){
                //查询所有合同的计划
                LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                        .in(ContractPayPlanConcludeE::getContractId, contractIdList)
                        .eq(ContractPayPlanConcludeE::getDeleted,0);
                List<ContractPayPlanConcludeE> planList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);

                contractIdList.addAll(bcPlanConcludeList.stream().map(ContractPayConcludeE::getId).collect(Collectors.toList()));
                //查询所有合同的清单
                LambdaQueryWrapper<ContractPayFundE> queryFunWrapper = new LambdaQueryWrapper<>();
                queryFunWrapper.in(ContractPayFundE::getContractId, contractIdList)
                        .eq(ContractPayFundE::getDeleted,0);
                List<ContractPayFundE> funList = contractPayFundMapper.selectList(queryFunWrapper);

                Map<String, List<String>> contractIdMap = bcPlanConcludeList.stream()
                        .collect(Collectors.groupingBy(
                                ContractPayConcludeE::getPid,
                                Collectors.mapping(
                                        ContractPayConcludeE::getId,
                                        Collectors.toList()
                                )
                        ));
                for (Map.Entry<String, List<String>> entry : contractIdMap.entrySet()){
                    String key = entry.getKey();
                    List<String> value = entry.getValue();
                    List<ContractPayFundE> mainFunList = funList.stream()
                            .filter(item -> item.getContractId().equals(key))
                            .collect(Collectors.toList());
                    List<ContractPayFundE> bcFunList = funList.stream()
                            .filter(item -> value.contains(item.getContractId()))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(bcFunList)){
                        bcNoCfContractMap.put(key, "否");
                        if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                            bcNoCfContractMap.put(yjBcContractId.get(key), "否");
                        }
                        continue;
                    }
                    Map<String, ContractPayFundE> bcFunMap = bcFunList.stream()
                            .collect(Collectors.toMap(
                                    this::generateCompositeKey,
                                    fund -> fund,
                                    (fund1, fund2) -> fund1
                            ));
                    Map<String, ContractPayFundE> mainFunMap = mainFunList.stream()
                            .collect(Collectors.toMap(
                                    this::generateCompositeKey,
                                    fund -> fund,
                                    (fund1, fund2) -> fund1
                            ));
                    for (String bckey : bcFunMap.keySet()) {
                        if (!mainFunMap.containsKey(bckey)) {
                            bcNoCfContractMap.put(key, "否");
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                                bcNoCfContractMap.put(yjBcContractId.get(key), "否");
                            }
                            break;
                        }
                        ContractPayFundE mainFund = mainFunMap.get(bckey);
                        List<ContractPayPlanConcludeE> funPLanList = planList.stream()
                                .filter(item -> item.getContractId().equals(key) && item.getContractPayFundId().equals(mainFund.getId()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(funPLanList)){
                            bcNoCfContractMap.put(key, "是");
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                                bcNoCfContractMap.put(yjBcContractId.get(key), "是");
                            }
                            break;
                        }else{
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(yjBcContractId.get(key))){
                                bcNoCfContractMap.put(yjBcContractId.get(key), "否");
                            }
                            bcNoCfContractMap.put(key, "否");
                        }
                    }
                }
            }
            detailList.getRecords().forEach(detail -> detail.setContractSplit(bcNoCfContractMap.get(detail.getContractId())));

            //根据条件获取支出报表底数-已发生已结算金额汇总
            List<ContractPayReportDetailListV> totalSettlementList = contractPayConcludeMapper.getDetailPayReportSettlementList(
                    reportSelect.getPaymentDateSatrt(),
                    reportSelect.getPaymentDateEnd(),
                    reportSelect.getPlannedCollectionTimeStart(),
                    reportSelect.getPlannedCollectionTimeEnd(),
                    reportSelect.getContractList());
            Map<String, List<ContractPayReportDetailListV>> settlementByContractId = totalSettlementList.stream().collect(Collectors.groupingBy(ContractPayReportDetailListV::getContractId));

            //获取清单项最大金额数据
            List<ContractPayReportDetailListV> funList = contractPayFundMapper.getFundListByContractIdList(reportSelect.getContractList());
            Map<String, List<ContractPayReportDetailListV>> funMapList = funList.stream().collect(Collectors.groupingBy(ContractPayReportDetailListV::getContractId));

            //获取所有项目的项目经理人员信息
            List<String> communityIdList = detailList.getRecords().stream().map(ContractPayReportDetailListV::getCommunityId).distinct().collect(Collectors.toList());
            List<SpaceCommunityUserV> communityUserList = spaceFeignClient.getUserIdByCommunityIdList(communityIdList);

            //获取合同管理类别
            List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);

            //根据条件获取支出报表底数-已发生未结算数据
            Map<String, List<ContractPayReportDetailListV>> planNoMapList = this.getPlanNoMapList( reportSelect.getPaymentDateSatrt(),
                    reportSelect.getPaymentDateEnd(),
                    reportSelect.getPlannedCollectionTimeStart(),
                    reportSelect.getPlannedCollectionTimeEnd(),
                    reportSelect.getContractList());
            for(ContractPayReportDetailListV detail : detailList.getRecords()){
                //匹配该项目经理人员信息
                SpaceCommunityUserV communityUser = communityUserList.stream().filter(c -> c.getCommunityId().equals(detail.getCommunityId())).findFirst().orElse(new SpaceCommunityUserV());
                //【封装】项目负责人
                detail.setProjectManager(CollectionUtils.isNotEmpty(communityUser.getUserList()) ? communityUser.getUserList().stream().map(UserInfoRv :: getUserName).collect(Collectors.joining(", ")) : null);
                //合同管理类别汉化
                if(StringUtils.isNotBlank(detail.getConmanagetype())) {
                    DictionaryCode conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(detail.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                    //【封装】合同管理类别描述
                    detail.setConmanagetype(conmanagetypeDict.getName());
                }
                //【封装】合同基本信息-四保一服
                detail.setIsSbyf(ServeTypeEnum.SBYF.getCode().toString().equals(detail.getIsSbyf()) ? "是" : "否");
                //供应商名称
                /*if(StringUtils.isNotEmpty(detail.getQydws())){
                    List<ContractQydwsF> qydwsFS = JSONObject.parseArray(detail.getQydws(),ContractQydwsF.class);
                    if(CollectionUtils.isNotEmpty(qydwsFS)) {
                        //【封装】供应商名称
                        detail.setQydws(qydwsFS.stream().map(ContractQydwsF::getOppositeOne).filter(Objects::nonNull).collect(Collectors.joining(", ")));
                    }
                }*/
                //【封装】合同起止时间拼接
                detail.setGmtExpireDate(detail.getGmtExpireStart()+"至"+detail.getGmtExpireEnd());
                //【封装】合同履约状态描述
                detail.setStatusDesc(ContractRevStatusEnum.parseName(detail.getStatus()));

                // 根据合同ID获取对应的结算单数据
                List<ContractPayReportDetailListV> settlements = settlementByContractId.get(detail.getContractId());
                if(CollectionUtils.isNotEmpty(settlements)){
                    //【封装】已发生已结算-应结算金额
                    detail.setYfsyjsPlannedCollectionAmountNum(settlements.get(0).getYfsyjsPlannedCollectionAmountNum());
                    //【封装】已发生已结算-实际结算金额
                    detail.setYfsyjsActualSettlementAmountNum(settlements.get(0).getYfsyjsActualSettlementAmountNum());
                    //【封装】已发生已结算-扣款金额
                    detail.setYfsyjsDeductionAmountNum(settlements.get(0).getYfsyjsDeductionAmountNum());
                }
                List<ContractPayReportDetailListV> funDetList = funMapList.get(detail.getContractId());
                if(CollectionUtils.isNotEmpty(funDetList) && StringUtils.isNotBlank(funDetList.get(0).getSplitMode())){
                    Set<String> splitModeName = Sets.newHashSet();
                    for (String s : funDetList.get(0).getSplitMode().split(",")) {
                        try{
                            splitModeName.add(SplitModeEnum.parseName(Integer.valueOf(s)));
                        } catch (Exception ex){
                            log.error("合同结算周期转换异常,合同结算周期:{}", s);
                        }
                    }
                    detail.setSettleCycle(String.join(",", splitModeName));
                }
                List<ContractPayReportDetailListV> planList = planNoMapList.get(detail.getContractId());
                if(CollectionUtils.isNotEmpty(planList)){
                    //【封装】已发生未结算-未结算周期
                    detail.setYfswjsSettleCycle(planList.get(0).getCostStartTime()+"至"+planList.get(0).getCostEndTime());
                    //【封装】已发生未结算-未结算期数合计
                    detail.setYfswjsPeriodsTotal(planList.get(0).getYfswjsPeriodsTotal());
                    detail.setContractPayFundId(planList.get(0).getContractPayFundId());
                    detail.setCostTimeNum(planList.get(0).getCostTimeNum());
                }
                //【封装】未结算金额 = 计划结算金额-应结算金额
                detail.setYfswjsNoSettlementAmountNum(detail.getPlannedCollectionAmountNum().subtract(detail.getYfsyjsPlannedCollectionAmountNum()));
            }
            detailList.getRecords().forEach(detail -> {
                detail.setContractAmount(detail.getContractAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                detail.setYfswjsNoSettlementAmount(detail.getYfswjsNoSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                detail.setPlannedCollectionAmount(detail.getPlannedCollectionAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                detail.setYfsyjsPlannedCollectionAmount(detail.getYfsyjsPlannedCollectionAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                detail.setYfsyjsActualSettlementAmount(detail.getYfsyjsActualSettlementAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
                detail.setYfsyjsDeductionAmount(detail.getYfsyjsDeductionAmountNum().setScale(2, RoundingMode.HALF_UP).toPlainString());
            });
            return PageV.of(request, detailList.getTotal(), detailList.getRecords());
        } catch (Exception ex) {
            log.error("根据条件获取统计表明细数据异常：{}", ex.getMessage());
            log.error("根据条件获取统计表明细数据异常：{}", ex);
            throw new OwlBizException("根据条件获取统计表明细数据异常："+ ex.getMessage());
        }
    }

    //根据条件获取支出报表底数-已发生未结算数据
    private Map<String, List<ContractPayReportDetailListV>> getPlanNoMapList(String paymentDateSatrt,
                                                                             String paymentDateEnd,
                                                                             String plannedCollectionTimeStart,
                                                                             String plannedCollectionTimeEnd,
                                                                             List<String> contractList){

        List<ContractPayReportDetailListV> planNoList = contractPayPlanConcludeMapper.getDetailPayReportPlanList(
                paymentDateSatrt,paymentDateEnd,plannedCollectionTimeStart,plannedCollectionTimeEnd,contractList);
        /*Map<String, Optional<ContractPayReportDetailListV>> grouped = planNoList.stream().filter(x->x.getMaxTermDate()!=null)
                .collect(Collectors.groupingBy(
                        ContractPayReportDetailListV::getContractId,
                        Collectors.maxBy(Comparator.comparing(ContractPayReportDetailListV::getMaxTermDate))
                ));

        Map<String, String> resultMap = new HashMap<>();
        grouped.forEach((contractId, optionalItem) ->
                optionalItem.map(ContractPayReportDetailListV::getCostEndTime)
                        .filter(Objects::nonNull)
                        .ifPresent(costEndTime -> resultMap.put(contractId, costEndTime))
        );*/

        planNoList = planNoList.stream().filter(item -> item.getYfswjsPeriodsTotal() != null && item.getYfswjsPeriodsTotal() != 0).collect(Collectors.groupingBy(ContractPayReportDetailListV::getContractId,
                Collectors.collectingAndThen(
                        Collectors.maxBy(
                                Comparator.comparingInt(ContractPayReportDetailListV::getYfswjsPeriodsTotal)
                                        .thenComparing(
                                                Comparator.comparing(ContractPayReportDetailListV::getCostTimeNum,Comparator.nullsLast(Comparator.naturalOrder()))
                                        )
                        ),
                        Optional::get
                )
        )).values().stream().collect(Collectors.toList());
        //planNoList.forEach(plan -> plan.setCostEndTime(resultMap.get(plan.getContractId())));
        return planNoList.stream().collect(Collectors.groupingBy(ContractPayReportDetailListV::getContractId));
    }

    //封装查询条件
    private ContractPayReportF getReportDetailSearch(PageF<SearchF<?>> request){
        ContractPayReportF reportSelect = new ContractPayReportF();
        //所属项目ID
        Field communityIdListField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "communityIdList".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(communityIdListField)){
            reportSelect.setCommunityIdList(new ArrayList<>((List<String>) communityIdListField.getValue()));
        }
        //应结日期
        Field plannedCollectionTimeField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "plannedCollectionTime".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(plannedCollectionTimeField)){
            reportSelect.setPlannedCollectionTimeStart((String) plannedCollectionTimeField.getMap().get("start"));
            reportSelect.setPlannedCollectionTimeEnd((String) plannedCollectionTimeField.getMap().get("end"));
        }
        //结算时间
        Field paymentDateField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "paymentDate".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(paymentDateField)){
            reportSelect.setPaymentDateSatrt((String) paymentDateField.getMap().get("start"));
            reportSelect.setPaymentDateEnd((String) paymentDateField.getMap().get("end"));
        }
        //合同ID集合
        Field contractListField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractList".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(contractListField)){
            reportSelect.setContractList(new ArrayList<>((List<String>) contractListField.getValue()));
        }

        //合同履约状态
        Field statusListField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && ("statusList".equals(field.getName()) || "status".equals(field.getName())))
                .findFirst().orElse(null);
        if(Objects.nonNull(statusListField)){
            reportSelect.setStatusList(new ArrayList<>((List<String>) statusListField.getValue()));
        }
        //是否NK
        Field isNKField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "isNK".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(isNKField)){
            reportSelect.setIsNK((Integer) isNKField.getValue());
        }

        //区域
        Field isBackDatetField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "isBackDate".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(isBackDatetField)){
            reportSelect.setIsBackDate(new ArrayList<>((List<String>) isBackDatetField.getValue()));
        }

        //所属部门名称
        Field departNameField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "departName".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(departNameField)){
            reportSelect.setDepartName((String)departNameField.getValue());
        }

        //合同类别
        Field conmanagetypeField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "conmanagetype".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(conmanagetypeField)){
            reportSelect.setConmanagetype(new ArrayList<>((List<String>) conmanagetypeField.getValue()));
        }

        //合同名称
        Field nameField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "name".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(nameField)){
            reportSelect.setName((String)nameField.getValue());
        }
        //合同编码
        Field codeField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractNo".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(codeField)){
            reportSelect.setContractNo((String)codeField.getValue());
        }
        //供应商名称
        Field merchantNameField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "merchantName".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(merchantNameField)){
            reportSelect.setMerchantName((String)merchantNameField.getValue());
        }
        //结算周期
        Field splitModeField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "splitMode".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(splitModeField)){
            reportSelect.setSplitMode(new ArrayList<>((List<String>) splitModeField.getValue()));
        }
        Field gmtExpireStartField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "gmtExpireStart".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(gmtExpireStartField)){
            reportSelect.setGmtExpireStart((String) gmtExpireStartField.getMap().get("start"));
            reportSelect.setGmtExpireStartEnd((String) gmtExpireStartField.getMap().get("end"));
        }
        Field gmtExpireEndField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "gmtExpireEnd".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(gmtExpireEndField)){
            reportSelect.setGmtExpireEndStart((String) gmtExpireEndField.getMap().get("start"));
            reportSelect.setGmtExpireEnd((String) gmtExpireEndField.getMap().get("end"));
        }
        return reportSelect;
    }

    //YJ数据分析报表
    public PageV<ContractPayYjListV> getYjDataAnalysisReport(PageF<SearchF<?>> request) {
        try {
            Page<ContractPayReportF> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

            ContractPayReportF reportSelect = this.getReportDetailSearch(request);

            //根据条件查询数据
            IPage<ContractPayYjListV> detailList = contractPayConcludeMapper.getYjDataAnalysisReport(pageF,
                    reportSelect);
            if (CollectionUtils.isEmpty(detailList.getRecords())) {
                return PageV.of(request, 0, new ArrayList<>());
            }
            List<String> contractIdList = detailList.getRecords().stream()
                    .flatMap(item -> Stream.of(
                            String.valueOf(item.getContractId()),
                            String.valueOf(item.getNkContractId())
                    ))
                    .collect(Collectors.toList());
            //获取结算信息
            List<ContractPaySettlementPeriodAmountV> settlementList = contractPaySettlementConcludeMapper.getSettlementPeriodAmount(contractIdList);
            //查询结算计划信息
            /*LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
            queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                    .in(ContractPayPlanConcludeE::getContractId, contractIdList)
                    .eq(ContractPayPlanConcludeE::getDeleted,0);
            List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);*/

            //获取合同管理类别
            List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);

            for(ContractPayYjListV detail : detailList.getRecords()) {
                //合同管理类别汉化
                if (StringUtils.isNotBlank(detail.getConmanagetype())) {
                    DictionaryCode conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(detail.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                    //【封装】合同管理类别描述
                    detail.setConmanagetype(conmanagetypeDict.getName());
                }
                //【封装】合同基本信息-四保一服
                detail.setIsSbyf(ServeTypeEnum.SBYF.getCode().toString().equals(detail.getIsSbyf()) ? "是" : "否");
                //【封装】合同履约状态描述
                detail.setStatusDesc(ContractRevStatusEnum.parseName(detail.getStatus()));

                //------根据合同起止时间，封装结算周期信息
                List<ContractPayYjSubListV> childrenList = this.getContractPeriodList(detail.getGmtExpireStart(),detail.getGmtExpireEnd());
                //获取周期信息
                Map<String, BigDecimal> yjCcostPlanMap = new HashMap<>();
                Map<String, BigDecimal> nkCcostPlanMap = new HashMap<>();
                //YJ结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodYJList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getContractId())).collect(Collectors.toList());

                if(CollectionUtils.isNotEmpty(settlementPeriodYJList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedYjMap =
                            settlementPeriodYJList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedYjMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> yjRangeList = new ArrayList<>();
                        for(ContractPaySettlementPeriodAmountV period : dataList){
                            yjRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> yjCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(yjRangeList,dataList.get(0).getActualSettlementAmount());
                        yjCommonRangeAmountMap.forEach((key, value) ->
                                yjCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }
                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodYJList){
                        List<CommonRangeDateBO> yjRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        yjCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(yjRangeList,sett.getActualSettlementAmount()));
                    }*/
                }

                //NK结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodNKList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getNkContractId())).collect(Collectors.toList());;
                if(CollectionUtils.isNotEmpty(settlementPeriodNKList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedNkMap =
                            settlementPeriodNKList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedNkMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> nKRangeList = new ArrayList<>();
                        for(ContractPaySettlementPeriodAmountV period : dataList){
                            nKRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> nkCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(nKRangeList,dataList.get(0).getActualSettlementAmount());
                        nkCommonRangeAmountMap.forEach((key, value) ->
                                nkCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }

                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodNKList) {
                        List<CommonRangeDateBO> nKRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(), sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        nkCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(nKRangeList, sett.getActualSettlementAmount()));
                    }*/
                }

                BigDecimal yjTotalAmount = BigDecimal.ZERO;
                BigDecimal nkTotalAmount = BigDecimal.ZERO;
                for(ContractPayYjSubListV sub: childrenList){
                    sub.setYjBqSettlementAmount(yjCcostPlanMap.get(sub.getPeriodKey()));
                    if(Objects.nonNull(sub.getYjBqSettlementAmount())){
                        yjTotalAmount = yjTotalAmount.add(sub.getYjBqSettlementAmount());
                        sub.setYjTotalSettlementAmount(yjTotalAmount);
                    }else{
                        sub.setYjBqSettlementAmount(null);
                        sub.setYjTotalSettlementAmount(null);
                    }
                    sub.setBqSettlementAmount(nkCcostPlanMap.get(sub.getPeriodKey()));
                    if(Objects.nonNull(sub.getBqSettlementAmount())){
                        nkTotalAmount = nkTotalAmount.add(sub.getBqSettlementAmount());
                        sub.setTotalSettlementAmount(nkTotalAmount);
                    }else{
                        sub.setBqSettlementAmount(null);
                        sub.setTotalSettlementAmount(null);
                    }
                    sub.setCeBqSettlementAmount(Objects.nonNull(sub.getBqSettlementAmount()) && Objects.nonNull(sub.getYjBqSettlementAmount()) ?sub.getBqSettlementAmount().subtract(sub.getYjBqSettlementAmount()) :null);
                    sub.setCeTotalSettlementAmount(Objects.nonNull(sub.getTotalSettlementAmount()) && Objects.nonNull(sub.getYjTotalSettlementAmount()) ?sub.getTotalSettlementAmount().subtract(sub.getYjTotalSettlementAmount()):null);
                }
                detail.setChildren(childrenList);
            }

            return PageV.of(request, detailList.getTotal(), detailList.getRecords());
        } catch (Exception ex) {
            log.error("根据条件YJ数据分析报表数据异常：{}", ex.getMessage());
            log.error("根据条件YJ数据分析报表数据异常：{}", ex);
            throw new OwlBizException("根据条件YJ数据分析报表数据异常：" + ex.getMessage());
        }
    }
    //根据条件导出YJ数据分析报表
    public PageV<ContractPayYjListV> exportYjDataAnalysisReport(PageF<SearchF<?>> request) {
        try {
            Page<ContractPayReportF> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

            ContractPayReportF reportSelect = this.getReportDetailSearch(request);

            //根据条件查询数据
            IPage<ContractPayYjListV> detailList = contractPayConcludeMapper.getYjDataAnalysisReport(pageF,
                    reportSelect);
            if (CollectionUtils.isEmpty(detailList.getRecords())) {
                return PageV.of(request, 0, new ArrayList<>());
            }
            List<String> contractIdList = detailList.getRecords().stream()
                    .flatMap(item -> Stream.of(
                            String.valueOf(item.getContractId()),
                            String.valueOf(item.getNkContractId())
                    ))
                    .collect(Collectors.toList());
            //获取结算信息
            List<ContractPaySettlementPeriodAmountV> settlementList = contractPaySettlementConcludeMapper.getSettlementPeriodAmount(contractIdList);

            //获取合同管理类别
            List<DictionaryCode> conmanageTypeDictList = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出合同管理类别.getCode(), null);

            List<ContractPayYjListV> resultList = new ArrayList<>();
            for(ContractPayYjListV detail : detailList.getRecords()) {
                //合同管理类别汉化
                if (StringUtils.isNotBlank(detail.getConmanagetype())) {
                    DictionaryCode conmanagetypeDict = conmanageTypeDictList.stream().filter(d -> d.getCode().equals(detail.getConmanagetype())).findFirst().orElse(new DictionaryCode());
                    //【封装】合同管理类别描述
                    detail.setConmanagetype(conmanagetypeDict.getName());
                }
                //【封装】合同基本信息-四保一服
                detail.setIsSbyf(ServeTypeEnum.SBYF.getCode().toString().equals(detail.getIsSbyf()) ? "是" : "否");
                //【封装】合同履约状态描述
                detail.setStatusDesc(ContractRevStatusEnum.parseName(detail.getStatus()));

                //------根据合同起止时间，封装结算周期信息
                List<ContractPayYjSubListV> childrenList = this.getContractPeriodList(detail.getGmtExpireStart(),detail.getGmtExpireEnd());
                //获取周期信息
                Map<String, BigDecimal> yjCcostPlanMap = new HashMap<>();
                Map<String, BigDecimal> nkCcostPlanMap = new HashMap<>();
                //YJ结算单周期及结算金额
                //YJ结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodYJList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getContractId())).collect(Collectors.toList());

                if(CollectionUtils.isNotEmpty(settlementPeriodYJList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedYjMap =
                            settlementPeriodYJList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedYjMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> yjRangeList = new ArrayList<>();
                        for (ContractPaySettlementPeriodAmountV period : dataList) {
                            yjRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(), period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> yjCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(yjRangeList, dataList.get(0).getActualSettlementAmount());
                        yjCommonRangeAmountMap.forEach((key, value) ->
                                yjCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }
                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodYJList){
                        List<CommonRangeDateBO> yjRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        yjCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(yjRangeList,sett.getActualSettlementAmount()));
                    }*/
                }

                //NK结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodNKList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getNkContractId())).collect(Collectors.toList());;
                if(CollectionUtils.isNotEmpty(settlementPeriodNKList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedNkMap =
                            settlementPeriodNKList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedNkMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> nKRangeList = new ArrayList<>();
                        for(ContractPaySettlementPeriodAmountV period : dataList){
                            nKRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> nkCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(nKRangeList,dataList.get(0).getActualSettlementAmount());
                        nkCommonRangeAmountMap.forEach((key, value) ->
                                nkCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }
                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodNKList) {
                        List<CommonRangeDateBO> nKRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(), sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        nkCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(nKRangeList, sett.getActualSettlementAmount()));
                    }*/
                }

                BigDecimal yjTotalAmount = BigDecimal.ZERO;
                BigDecimal nkTotalAmount = BigDecimal.ZERO;
                for(ContractPayYjSubListV sub: childrenList){
                    sub.setYjBqSettlementAmount(yjCcostPlanMap.get(sub.getPeriodKey()));
                    if(Objects.nonNull(sub.getYjBqSettlementAmount())){
                        yjTotalAmount = yjTotalAmount.add(sub.getYjBqSettlementAmount());
                        sub.setYjTotalSettlementAmount(yjTotalAmount);
                    }else{
                        sub.setYjBqSettlementAmount(null);
                        sub.setYjTotalSettlementAmount(null);
                    }
                    sub.setBqSettlementAmount(nkCcostPlanMap.get(sub.getPeriodKey()));
                    if(Objects.nonNull(sub.getBqSettlementAmount())){
                        nkTotalAmount = nkTotalAmount.add(sub.getBqSettlementAmount());
                        sub.setTotalSettlementAmount(nkTotalAmount);
                    }else{
                        sub.setBqSettlementAmount(null);
                        sub.setTotalSettlementAmount(null);
                    }
                    sub.setCeBqSettlementAmount(Objects.nonNull(sub.getBqSettlementAmount()) && Objects.nonNull(sub.getYjBqSettlementAmount()) ?sub.getYjBqSettlementAmount().subtract(sub.getBqSettlementAmount()) :null);
                    sub.setCeTotalSettlementAmount(Objects.nonNull(sub.getTotalSettlementAmount()) && Objects.nonNull(sub.getYjTotalSettlementAmount()) ?sub.getYjTotalSettlementAmount().subtract(sub.getTotalSettlementAmount()):null);
                    ContractPayYjListV result = new ContractPayYjListV();
                    BeanUtils.copyProperties(detail, result);
                    BeanUtils.copyProperties(sub, result);
                    resultList.add(result);
                }
                detail.setChildren(childrenList);
            }

            return PageV.of(request, detailList.getTotal(), resultList);
        } catch (Exception ex) {
            log.error("根据条件YJ数据分析报表数据异常：{}", ex.getMessage());
            log.error("根据条件YJ数据分析报表数据异常：{}", ex);
            throw new OwlBizException("根据条件YJ数据分析报表数据异常：" + ex.getMessage());
        }
    }
    private List<ContractPayYjSubListV> getContractPeriodList(LocalDate start, LocalDate end) {
        List<ContractPayYjSubListV> childrenList = new ArrayList<>();
        LocalDate currentStart = start;
        while (currentStart.isBefore(end) || currentStart.isEqual(end)) {
            LocalDate currentEnd = currentStart.withDayOfMonth(currentStart.lengthOfMonth());
            if (currentEnd.isAfter(end)) {
                currentEnd = end;
            }
            ContractPayYjSubListV sub = new ContractPayYjSubListV();
            sub.setPeriodKey(currentStart.format(DateTimeFormatter.ofPattern("yyyy/MM")));
            sub.setPeriodsDetail(currentStart.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "-" + currentEnd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            childrenList.add(sub);
            currentStart = currentEnd.plusDays(1);
        }
        return childrenList;
    }
    private List<CommonRangeDateBO> getSettlementPeriodList(LocalDate start, LocalDate end) {
        List<CommonRangeDateBO> childrenList = new ArrayList<>();
        LocalDate currentStart = start;
        while (currentStart.isBefore(end) || currentStart.isEqual(end)) {
            LocalDate currentEnd = currentStart.withDayOfMonth(currentStart.lengthOfMonth());
            if (currentEnd.isAfter(end)) {
                currentEnd = end;
            }
            CommonRangeDateBO sub = new CommonRangeDateBO();
            sub.setId(currentStart.format(DateTimeFormatter.ofPattern("yyyy/MM")));
            sub.setCostStartTime(currentStart);
            sub.setCostEndTime(currentEnd);
            childrenList.add(sub);
            currentStart = currentEnd.plusDays(1);
        }
        return childrenList;
    }

    //NK数据分析汇总报表
    public PageV<ContractPayNkTotalListV> getNKDataAnalysisTotalReport(PageF<SearchF<?>> request) {
        try {
            Page<ContractPayReportF> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

            ContractPayReportF reportSelect = this.getReportDetailSearch(request);

            //根据条件查询数据
            IPage<ContractPayNkTotalListV> detailList = contractPayConcludeMapper.getNKDataAnalysisTotalReport(pageF,
                    reportSelect);
            if (CollectionUtils.isEmpty(detailList.getRecords())) {
                return PageV.of(request, 0, new ArrayList<>());
            }
            List<String> contractIdList = detailList.getRecords().stream()
                    .flatMap(item -> Stream.of(
                            String.valueOf(item.getContractId()),
                            String.valueOf(item.getNkContractId())
                    ))
                    .collect(Collectors.toList());
            List<String> hyContractIdList = new ArrayList<>();
            for(ContractPayNkTotalListV detail : detailList.getRecords()) {
                if(StringUtils.isNotBlank(detail.getHyBcContractId())){
                    hyContractIdList.addAll(Arrays.asList(detail.getHyBcContractId().split(",")));
                }
            }
            Map<String, BigDecimal> nkContractAmountMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(hyContractIdList)){
                //查询HY补充协议清单
                LambdaQueryWrapper<ContractPayFundE> queryFunWrapper = new LambdaQueryWrapper<>();
                queryFunWrapper.in(ContractPayFundE::getBcContractId, hyContractIdList)
                        .eq(ContractPayFundE::getDeleted,0);
                List<ContractPayFundE> funList = contractPayFundMapper.selectList(queryFunWrapper);
                List<String> settlementIdList = contractPaySettlementConcludeMapper.getApprovedSettlementId(hyContractIdList);
                if(CollectionUtils.isNotEmpty(funList) && CollectionUtils.isNotEmpty(settlementIdList)){
                    LambdaQueryWrapper<ContractPaySettDetailsE> querySetdetWrapper = new LambdaQueryWrapper<>();
                    querySetdetWrapper.in(ContractPaySettDetailsE::getSettlementId, settlementIdList)
                            .in(ContractPaySettDetailsE::getPayFundId, funList.stream().map(ContractPayFundE::getId).collect(Collectors.toList()))
                            .eq(ContractPaySettDetailsE::getDeleted,0);
                    List<ContractPaySettDetailsE> settDetList = contractPaySettDetailsMapper.selectList(querySetdetWrapper);
                    if(CollectionUtils.isNotEmpty(settDetList)){
                        Map<String, BigDecimal> payFunMap = settDetList.stream()
                                .filter(e -> e.getPayFundId() != null)
                                .collect(Collectors.toMap(
                                        e -> e.getPayFundId(),
                                        e -> e.getAmount().subtract(e.getDeductionAmount() != null ? e.getDeductionAmount() : BigDecimal.ZERO),
                                        BigDecimal::add
                                ));
                        funList.forEach(x->x.setAmount(payFunMap.get(x.getId())));
                    }
                    nkContractAmountMap = funList.stream()
                            .filter(x -> x.getAmount() != null)
                            .collect(Collectors.toMap(
                                    ContractPayFundE::getBcContractId,
                                    ContractPayFundE::getAmount,
                                    BigDecimal::add
                            ));
                }
            }

            //获取结算信息
            List<ContractPaySettlementPeriodAmountV> settlementList = contractPaySettlementConcludeMapper.getSettlementPeriodAmount(contractIdList);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            for(ContractPayNkTotalListV detail : detailList.getRecords()) {

                //获取周期信息
                Map<String, BigDecimal> yjCcostPlanMap = new HashMap<>();
                Map<String, BigDecimal> nkCcostPlanMap = new HashMap<>();

                //------根据合同起止时间，封装结算周期信息
                List<ContractPayYjSubListV> childrenList = this.getContractPeriodList(detail.getGmtExpireStart(),detail.getGmtExpireEnd());
                //NK结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodNKList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getNkContractId())).collect(Collectors.toList());;
                if(CollectionUtils.isNotEmpty(settlementPeriodNKList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedNkMap =
                            settlementPeriodNKList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedNkMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> nKRangeList = new ArrayList<>();
                        for(ContractPaySettlementPeriodAmountV period : dataList){
                            nKRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> nkCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(nKRangeList,dataList.get(0).getActualSettlementAmount());
                        nkCommonRangeAmountMap.forEach((key, value) ->
                                nkCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }
                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodNKList) {
                        List<CommonRangeDateBO> nKRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(), sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        nkCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(nKRangeList, sett.getActualSettlementAmount()));
                    }*/
                    Date minStartDate = settlementPeriodNKList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getStartDate)
                            .filter(date -> date != null)
                            .min(Date::compareTo)
                            .orElse(null);

                    Date maxEndDate = settlementPeriodNKList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getEndDate)
                            .filter(date -> date != null)
                            .max(Date::compareTo)
                            .orElse(null);
                    //NK-已结算周期
                    detail.setNkPperiodsDetail(sdf.format(minStartDate) + "-" + sdf.format(maxEndDate));
                    //NK-累计结算金额
                    BigDecimal totalAmount = settlementPeriodNKList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getActualSettlementAmount)
                            .filter(amount -> amount != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    detail.setNkTotalSettlementAmount(totalAmount);
                }


                //YJ结算单周期及结算金额
                List<ContractPaySettlementPeriodAmountV> settlementPeriodYJList = settlementList.stream().filter(x -> x.getContractId().equals(detail.getContractId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(settlementPeriodYJList)){
                    Map<String, List<ContractPaySettlementPeriodAmountV>> groupedYjMap =
                            settlementPeriodYJList.stream()
                                    .collect(Collectors.groupingBy(
                                            ContractPaySettlementPeriodAmountV::getSettlementId
                                    ));
                    for (Map.Entry<String, List<ContractPaySettlementPeriodAmountV>> entry : groupedYjMap.entrySet()) {
                        List<ContractPaySettlementPeriodAmountV> dataList = entry.getValue();
                        List<CommonRangeDateBO> yjRangeList = new ArrayList<>();
                        for(ContractPaySettlementPeriodAmountV period : dataList){
                            yjRangeList.addAll(this.getSettlementPeriodList(period.getStartDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),period.getEndDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()));
                        }
                        Map<String, BigDecimal> yjCommonRangeAmountMap = commonRangeAmountService.calculateDistributedAmounts(yjRangeList,dataList.get(0).getActualSettlementAmount());
                        yjCommonRangeAmountMap.forEach((key, value) ->
                                yjCcostPlanMap.merge(key, value, BigDecimal::add)
                        );
                    }
                    /*for(ContractPaySettlementPeriodAmountV sett : settlementPeriodYJList) {
                        List<CommonRangeDateBO> yjRangeList = this.getSettlementPeriodList(sett.getStartDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(), sett.getEndDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate());
                        yjCcostPlanMap.putAll(commonRangeAmountService.calculateDistributedAmounts(yjRangeList, sett.getActualSettlementAmount()));
                    }*/
                    Date minStartDate = settlementPeriodYJList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getStartDate)
                            .filter(date -> date != null)
                            .min(Date::compareTo)
                            .orElse(null);

                    Date maxEndDate = settlementPeriodYJList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getEndDate)
                            .filter(date -> date != null)
                            .max(Date::compareTo)
                            .orElse(null);
                    //YJ-已结算周期
                    detail.setYjPperiodsDetail(sdf.format(minStartDate) + "-" + sdf.format(maxEndDate));
                    //YJ-累计结算金额
                    BigDecimal totalAmount = settlementPeriodYJList.stream()
                            .map(ContractPaySettlementPeriodAmountV::getActualSettlementAmount)
                            .filter(amount -> amount != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    detail.setYjTotalSettlementAmount(totalAmount);
                }

                BigDecimal yjTotalAmount = BigDecimal.ZERO;
                for(ContractPayYjSubListV sub: childrenList){
                    sub.setYjBqSettlementAmount(yjCcostPlanMap.get(sub.getPeriodKey()));
                    sub.setBqSettlementAmount(nkCcostPlanMap.get(sub.getPeriodKey()));
                    BigDecimal lJOldYJAmount = Objects.nonNull(sub.getBqSettlementAmount()) && Objects.nonNull(sub.getYjBqSettlementAmount()) ?sub.getBqSettlementAmount().subtract(sub.getYjBqSettlementAmount()) :BigDecimal.ZERO;
                    if(lJOldYJAmount.compareTo(BigDecimal.ZERO)>=0){
                        yjTotalAmount = yjTotalAmount.add(lJOldYJAmount);
                    }
                }
                if(StringUtils.isNotBlank(detail.getHyBcContractId())){
                    List<String> hyIdList = Arrays.asList(detail.getHyBcContractId().split(","));
                    Map<String, BigDecimal> finalNkContractAmountMap = nkContractAmountMap;
                    BigDecimal totalAmount = hyIdList.stream()
                            .filter(id -> id != null && finalNkContractAmountMap.containsKey(id))
                            .map(finalNkContractAmountMap::get)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    detail.setNkTotaltAmount(totalAmount);
                }
                //YJ-累计金额[取YJ合同减K合同各期结算金额差为负数的累计值]
                detail.setYjTotalAmount(yjTotalAmount);
                //累计结算差额[取YJ累计结算金额减K累计结金额差值]
                detail.setCeTotalSettlementAmount(Objects.nonNull(detail.getNkTotalSettlementAmount()) && Objects.nonNull(detail.getYjTotalSettlementAmount()) ?detail.getNkTotalSettlementAmount().subtract(detail.getYjTotalSettlementAmount()):null);
            }

            return PageV.of(request, detailList.getTotal(), detailList.getRecords());
        } catch (Exception ex) {
            log.error("NK数据分析汇总报表异常：{}", ex.getMessage());
            log.error("NK数据分析汇总报表异常：{}", ex);
            throw new OwlBizException("NK数据分析汇总报表异常：" + ex.getMessage());
        }
    }
}
