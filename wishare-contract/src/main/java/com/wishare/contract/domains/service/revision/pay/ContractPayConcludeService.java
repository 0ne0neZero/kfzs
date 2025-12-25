package com.wishare.contract.domains.service.revision.pay;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.revision.income.ContractZffxxDraweeExtendF;
import com.wishare.contract.apps.fo.revision.income.ContractZffxxF;
import com.wishare.contract.apps.fo.revision.income.ContractZffxxTrueDraweeExtendF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectPlanMonthlyAllocationAppService;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.apps.service.revision.pay.ContractPayConcludeExpandAppService;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.pay.*;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.ContractTypeEnum;
import com.wishare.contract.domains.enums.revision.CostApportionTypeEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.mapper.revision.income.PayCostPlanMapper;
import com.wishare.contract.domains.mapper.revision.pay.*;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.wishare.contract.domains.mapper.revision.pay.settdetails.ContractPaySettDetailsMapper;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.bond.RevisionBondCollectService;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeDetailV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeListV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeTreeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import com.wishare.contract.infrastructure.utils.BigDecimalUtils;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.infrastructure.utils.DateTimeUtil;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 支出合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPayConcludeService extends ServiceImpl<ContractPayConcludeMapper, ContractPayConcludeE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractRelationRecordService contractRelationRecordService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private OrgEnhanceComponent orgEnhanceComponent;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionBondCollectService bondCollectService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayPlanConcludeService payPlanConcludeService;

    @Resource
    @Lazy
    private ContractPayFundService contractPayFundService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractOrgCommonService contractOrgCommonService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettDetailsMapper contractPaySettDetailsMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPaySettlementConcludeMapper contractPaySettlementConcludeMapper;
    @Autowired
    private ContractPayPlanConcludeMapper contractPayPlanConcludeMapper;
    @Autowired
    private PayCostPlanMapper payCostPlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettlementPlanRelationMapper settlementPlanRelationMapper;
    @Autowired
    private FinanceFeignClient financeFeignClient;
    @Autowired
    private ContractPayFundMapper contractPayFundMapper;
    @Autowired
    private ContractPayConcludeExpandService contractPayConcludeExpandService;
    @Autowired
    private AttachmentService attachmentService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeExpandAppService contractPayConcludeExpandAppService;
    @Autowired
    private ContractProjectPlanMonthlyAllocationAppService contractProjectPlanMonthlyAllocationAppService;
    @Autowired
    private ContractPayCostApportionMapper contractPayCostApportionMapper;

    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractPayConcludeV> get(ContractPayConcludeF conditions){
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractPayConcludeE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractPayConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractPayConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractPayConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractPayConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(ContractPayConcludeE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractPayConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractPayConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractPayConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractPayConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.selectOne(queryWrapper);
        if (contractPayConcludeE != null) {
            Optional<ContractPayConcludeV> payConcludeE = Optional.of(Global.mapperFacade.map(contractPayConcludeE, ContractPayConcludeV.class));
            ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
            concludeExpandF.setContractId(contractPayConcludeE.getId());
            ContractPayConcludeExpandV concludeExpandV = contractPayConcludeExpandAppService.get(concludeExpandF);
            //合同支付信息:支付方信息
            if(StringUtils.isNotEmpty(concludeExpandV.getFkdwxx())){
                try {
                    List<ContractZffxxF> zffxx = JSONObject.parseArray(concludeExpandV.getFkdwxx(),ContractZffxxF.class);
                    List<ContractZffxxDraweeExtendF> draweeName = new ArrayList<>();
                    List<ContractZffxxTrueDraweeExtendF> truedraweeName = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(zffxx)) {
                        StringJoiner zcfstr = new StringJoiner(",");
                        StringJoiner yfhtjestr = new StringJoiner(",");
                        StringJoiner sjfkrstr = new StringJoiner(",");
                        zffxx.forEach(zff ->{

                            //支付方信息
                            ContractZffxxDraweeExtendF contractZffxxDraweeExtendF = new ContractZffxxDraweeExtendF();
                            ContractZffxxTrueDraweeExtendF contractZffxxTrueDraweeExtendF = new ContractZffxxTrueDraweeExtendF();

                            if (StringUtils.isNotEmpty(zff.getDrawee())) {
                                zcfstr.add(zff.getDrawee());
                                contractZffxxDraweeExtendF.setId(zff.getDraweeid());
                                contractZffxxDraweeExtendF.setName(zff.getDrawee());
                                draweeName.add(contractZffxxDraweeExtendF);
                                zff.setDraweeName(draweeName);
                            }

                            if (StringUtils.isNotEmpty(zff.getDraweepayamt())) {
                                yfhtjestr.add(zff.getDraweepayamt());
                            }

                            if (StringUtils.isNotEmpty(zff.getTruedrawee())) {
                                sjfkrstr.add(zff.getTruedrawee());
                                contractZffxxTrueDraweeExtendF.setName(zff.getTruedrawee());
                                contractZffxxTrueDraweeExtendF.setId(zff.getTruedraweeid());
                                truedraweeName.add(contractZffxxTrueDraweeExtendF);
                                zff.setTruedraweeName(truedraweeName);
                            }
                        });
                        payConcludeE.ifPresent(payConclude -> {
                            payConclude.setZcfstr(zcfstr.toString());
                            payConclude.setSjfkrstr(sjfkrstr.toString());
                        });
                    }
                } catch (BeansException e) {
                }
            }
            return payConcludeE;
        }else {
            return Optional.empty();
        }
    }

    public ContractPayConcludeListV queryInfo(ContractPayConcludeListF conditions){
        ContractPayConcludeListV retV = new ContractPayConcludeListV();
        List<ContractPayConcludeV> retVList = contractPayConcludeMapper.queryInfo(conditions.getNameNo(),tenantId(),conditions.getContractId(), conditions.getIsNK());
        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        if (!CollectionUtils.isEmpty(retVList)) {
            List<String> contractIdList = retVList.stream().map(ContractPayConcludeV::getId).collect(Collectors.toList());
            List<ContractPayFundE> list = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .in(ContractPayFundE.CONTRACT_ID, contractIdList)
                    .eq(ContractPayFundE.DELETED, 0));
            Map<String,ContractPayFundE> map = list.stream().collect(Collectors.toMap(ContractPayFundE::getContractId, v->v, (v1, v2) -> v1));
            retVList.forEach(ret-> {
                ret.setSplitMode((MapUtils.isNotEmpty(map) && null != map.get(ret.getId()))? map.get(ret.getId()).getPayWayId() : null);

                BigDecimal changContractAmount = ret.getChangContractAmount();
                BigDecimal contractAmountOriginalRate = ret.getContractAmountOriginalRate();
                BigDecimal zeroAmount = new BigDecimal("0.00");
                if (changContractAmount == null || changContractAmount.compareTo(zeroAmount) == 0) {
                    ret.setChangContractAmount(null);
                } else {
                    ret.setChangContractAmount(changContractAmount);
                }

            });
        }
        return retV;
    }

    public List<ContractPayConcludeV> queryContractInfo(String id){
        List<ContractPayConcludeV> contractPayConcludeV = contractPayConcludeMapper.queryContractInfo(id);
        return contractPayConcludeV;
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public ContractPayConcludeListV list(ContractPayConcludeListF conditions){
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotBlank(conditions.getNameNo())){
            queryWrapper.like(ContractPayConcludeE.NAME, conditions.getNameNo()).or()
                    .like(ContractPayConcludeE.CONTRACT_NO, conditions.getNameNo());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.like(ContractPayConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.like(ContractPayConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractPayConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractPayConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractPayConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractPayConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(ContractPayConcludeE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractPayConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractPayConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractPayConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractPayConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractPayConcludeE.ID, conditions.getIndexId());
        }
        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId());
        queryWrapper.orderByDesc(ContractPayConcludeE.ID)
                .eq(ContractPayConcludeE.TENANT_ID, tenantId());
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractPayConcludeV> retVList = Global.mapperFacade.mapAsList(contractPayConcludeMapper.selectList(queryWrapper),ContractPayConcludeV.class);
        for(int i = 0; i < retVList.size(); i ++){
            retVList.get(i).setNameNo(retVList.get(i).getName() + "-" + retVList.get(i).getContractNo());
        }
        ContractPayConcludeListV retV = new ContractPayConcludeListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractPayConcludeSaveF contractPayConcludeF){
        ContractPayConcludeE map = Global.mapperFacade.map(contractPayConcludeF, ContractPayConcludeE.class);
        contractPayConcludeMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractPayConcludeF 根据Id更新
    */
    public void update(ContractPayConcludeUpdateF contractPayConcludeF){
        if (contractPayConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayConcludeE map = Global.mapperFacade.map(contractPayConcludeF, ContractPayConcludeE.class);
        contractPayConcludeMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        //-- 校验保证金计划
        List<RevisionBondCollectE> list = bondCollectService.list(new QueryWrapper<RevisionBondCollectE>().eq(RevisionBondCollectE.CONTRACT_ID, id));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new OwlBizException("该合同关联了保证金计划，不可被删除");
        }
        //-- 校验付款计划
        List<ContractPayPlanConcludeE> list1 = payPlanConcludeService.list(new QueryWrapper<ContractPayPlanConcludeE>()
                .eq(ContractPayPlanConcludeE.CONTRACT_ID, id));
        if (CollectionUtils.isNotEmpty(list1)) {
            throw new OwlBizException("该合同关联了付款计划，不可被删除");
        }
        contractPayConcludeMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeV> page(PageF<ContractPayConcludePageF> request) {
        ContractPayConcludePageF conditions = request.getConditions();
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractPayConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractPayConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractPayConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractPayConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractPayConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractPayConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractPayConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractPayConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractPayConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractPayConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractPayConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractPayConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractPayConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(ContractPayConcludeE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractPayConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractPayConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractPayConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractPayConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractPayConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayConcludeE.GMT_CREATE);
        }
        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId());
        Page<ContractPayConcludeE> page = contractPayConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeV.class));
    }

    /**
     * 该接口供给后端
     *
     * @param request 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractPayConcludeV> choose(PageF<ContractPayPageF> request) {
        ContractPayPageF conditions = request.getConditions();
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.like(ContractPayConcludeE.NAME, conditions.getName());
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayConcludeE.GMT_CREATE);
        }

        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId());

        List<Integer> status = new ArrayList<>();
        status.add(ContractRevStatusEnum.正在履行.getCode());
        status.add(ContractRevStatusEnum.尚未履行.getCode());

        queryWrapper.in(ContractPayConcludeE.STATUS, status);

        //-- 不是超级管理员时，根据所属部门进行权限隔离
        if(!isAdminCurUser()) {
            queryWrapper.in(ContractPayConcludeE.DEPART_ID, orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class)));
        }

        Page<ContractPayConcludeE> page = contractPayConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeV.class));
    }

    /**
     * 支出合同列表平铺查询
     *
     * @param request
     * @return
     */
    public PageV<ContractPayConcludeTreeV> frontPageV2(PageF<SearchF<ContractPayConcludeE>> request) {
        Page<ContractPayConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        List<Field> fields = request.getConditions().getFields();
        fields.add(new Field(ContractPayConcludeE.TENANT_ID, tenantId(), 1));
        for (Field field : fields){
            if (field.getName().contains("approvalDate") || field.getName().contains("archivedDate")){
                if(Objects.nonNull(field.getMap())){
                    field.setMap(DateTimeUtil.processTimeRange(field.getMap()));
                }
            }
        }
        QueryWrapper<ContractPayConcludeE> queryWrapper = request.getConditions().getQueryModel();

        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
        if (userInfoRv.getOrgIds().contains(13554968509111L)) {
            superAccount = true;
        }
        if (!superAccount) {
            Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            queryWrapper.and(i -> i.in(ContractPayConcludeE.DEPART_ID, orgListByOrgId)
                    .or(m -> m.isNull(ContractPayConcludeE.DEPART_ID)));
        }
        //将父子合同视为一样的，直接从库里面分页查出来
        IPage<ContractPayConcludeTreeV> page = contractPayConcludeMapper.queryByPathV2(pageF, queryWrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return PageV.of(request, 0, new ArrayList<>());
        }

        List<ContractPayConcludeTreeV> results = new ArrayList<>();
        //汇总全部的pid
        List<String> pIds = page.getRecords().stream().map(ContractPayConcludeTreeV::getPid).distinct().collect(Collectors.toList());
        //按照pid进行分组
        Map<String, List<ContractPayConcludeTreeV>> pidMap = page.getRecords().stream()
                .collect(Collectors.groupingBy(ContractPayConcludeTreeV::getPid));
        //按照pid的顺序将每组的数据添加到results中
        for (String pid : pIds) {
            results.addAll(pidMap.getOrDefault(pid, new ArrayList<>()));
        }
        return PageV.of(request, page.getTotal(), results);
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayConcludeTreeV> frontPage(PageF<SearchF<ContractPayConcludeE>> request) {
        Page<ContractPayConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayConcludeE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId())
                .eq(ContractPayConcludeE.DELETED,0);

        //-- 不是超级管理员时，根据所属部门进行权限隔离
        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
        if(userInfoRv.getOrgIds().contains(13554968509111l)){
            superAccount = true;
        }
        if(!superAccount) {
            Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            queryWrapper.and(i -> i.in(ContractPayConcludeE.DEPART_ID, orgListByOrgId)
                    .or(m -> m.isNull(ContractPayConcludeE.DEPART_ID)));
        }

        IPage<ContractPayConcludeTreeV> pageList
                = contractPayConcludeMapper.collectionPayConcludePage(pageF, queryWrapper);
        List<ContractPayConcludeTreeV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(ContractPayConcludeTreeV::getId).collect(Collectors.toList());
        List<ContractPayConcludeTreeV> concludeVList = contractPayConcludeMapper.queryByPath(queryWrapper, parentIdList);
        List<ContractPayConcludeTreeV> list = TreeUtil.treeing(concludeVList);
        return PageV.of(request, pageList.getTotal(), list);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayConcludeE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayConcludeE selectOneBy(Consumer<QueryWrapper<ContractPayConcludeE>> consumer,String... fields) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeE中id字段的值, select 指定字段
    *
    * @param fields ContractPayConcludeE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayConcludeE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayConcludeMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayConcludeE>> consumer) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayConcludeMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayConcludeE中仅包含a字段的值
     *
     * @param fields ContractPayConcludeE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayConcludeE> selectListBy(Consumer<QueryWrapper<ContractPayConcludeE>> consumer,String... fields) {
         QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayConcludeMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayConcludeE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayConcludeE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayConcludeE>> consumer, String... fields) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeE中id字段的值, select 指定字段
     *
     * @param fields ContractPayConcludeE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayConcludeE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayConcludeE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayConcludeE> queryPage = contractPayConcludeMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    /**
     * 校验传进来的已经审批通过的合同的执行状态是否需要更新
     * @param id 合同ID
     * @return boolean
     */
    public Boolean checkEForUpdateStatus(String id) {

        return false;
    }

    /**
     * 处理按钮展示
     * @param map 入参
     */
    public Boolean dealBtnShowForDetail(ContractPayConcludeTreeV map) {
        map.setShowBtnEdit(false)
                .setShowBtnEnd(false)
                .setShowBtnSign(false)
                .setShowBtnRepeatPull(false);

        if(map.getContractType() == 4 && map.getReviewStatus() == 2 && ( map.getContractNature() != null &&  map.getContractNature() == 1)){
            return true;
        }
        //如果合同状态是"合同停用"或"合同终止"直接返回，没有任何操作按钮
        if (ContractRevStatusEnum.合同停用.getCode().equals(map.getStatus()) || ContractRevStatusEnum.合同终止.getCode().equals(map.getStatus())) {
            return true;
        }
        //若该合同存在未审批通过 或 未推送成功的子合同，那么不展示任何按钮
        List<ContractPayConcludeV> list = contractPayConcludeMapper.queryIsExistContract(map.getId());
        if(ObjectUtils.isNotEmpty(list)){
            return true;
        }
        //否则，才走下面的按钮判断逻辑
        if(map.getReviewStatus() == 0){
            map.setShowBtnEdit(true);
        }
        //审批通过但是还未推送，展示"合同签订"
        if (map.getReviewStatus() == 2 && (map.getContractNature() == null || ConcludeContractNatureEnum.TO_PUSH.getCode().equals(map.getContractNature()))) {
            map.setShowBtnSign(true);
        }
        //审批通过但是合同推送失败，展示"重复推送"
        if (map.getReviewStatus() == 2 && (map.getContractNature() != null && map.getContractNature() == 0)) {
            map.setShowBtnRepeatPull(true);
        }
        if(map.getReviewStatus() == 2 && (map.getContractNature() != null && map.getContractNature() == 1)){
            map.setShowBtnEnd(true);
        }
        if (ReviewStatusEnum.已驳回.getCode().equals(map.getReviewStatus())) {
            map.setShowBtnEdit(true);
        }
        return true;
    }

    /**
     * 校验支出合同是否能被变更或续签
     * @param concludeE
     */
    public Boolean checkIsCanChangeOrRenewal(ContractPayConcludeE concludeE) {
        if(concludeE.getContractNature() != null && concludeE.getContractNature() == 1 && (concludeE.getStatus() == 4 || concludeE.getStatus() == 5 || concludeE.getStatus() == 6)){
            return true;
        }
        return false;
    }

    /**
     * 校验合同是否能被反审
     * @param concludeE
     */
    public Boolean checkIsCanBackReview(ContractPayConcludeE concludeE) {
        return true;
    }

    /**
     * 反审时校验合同关联的变更 & 续签流程
     * @param concludeE
     */
    public Boolean checkIsCanChangeOrRenewalForBackReview(ContractPayConcludeE concludeE, Integer actionType) {
        List<ContractRelationRecordE> list = contractRelationRecordService.list(new QueryWrapper<ContractRelationRecordE>()
                .eq(ContractRelationRecordE.OLD_ID, concludeE.getId())
                .eq(ContractRelationRecordE.IS_DONE, 0)
                .eq(ContractRelationRecordE.TYPE, actionType));
        if (CollectionUtils.isNotEmpty(list)) {
            for (ContractRelationRecordE recordE : list) {
                ContractPayConcludeE addOne = getById(recordE.getAddId());
                if (Objects.nonNull(addOne)) {
                    if (ReviewStatusEnum.审批中.getCode().equals(addOne.getReviewStatus())
                            || ReviewStatusEnum.待提交.getCode().equals(addOne.getReviewStatus())
                            || ReviewStatusEnum.已拒绝.getCode().equals(addOne.getReviewStatus())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public List<ContractPayConcludeDetailV> getInfoListByIds(List<String> ids) {
        List<ContractPayConcludeDetailV> resultList = new ArrayList<>();
        List<ContractPayConcludeE> list = contractPayConcludeMapper.selectList(new LambdaQueryWrapper<ContractPayConcludeE>()
                .in(ContractPayConcludeE::getId, ids)
                .select(ContractPayConcludeE::getId, ContractPayConcludeE::getContractNo, ContractPayConcludeE::getName, ContractPayConcludeE::getFromid));

        if (CollectionUtils.isNotEmpty(list)) {
            resultList = Global.mapperFacade.mapAsList(list, ContractPayConcludeDetailV.class);
        }
        return resultList;
    }

    /**
     * 处理主合同数据
     * @param conAmount 合同金额
     * @param pid 主合同ID
     */
    @Transactional
    public void handleConcludeSupple(ContractPayConcludeE contractPayConcludeE) {
        List<String> contractIdList = new ArrayList<>();
        //查询补充合同对应主合同数据
        ContractPayConcludeE concludeE = contractPayConcludeMapper.selectById(contractPayConcludeE.getPid());
        if(Objects.isNull(concludeE)){
            return;
        }
        contractIdList.add(concludeE.getId());
        //--------原逻辑start--------
        mergeEndTime(concludeE,contractPayConcludeE.getId());
        mergePayFund(contractPayConcludeE.getId(),contractPayConcludeE.getPid());
        //累加前的金额：若变更后金额不为0，那么使用变更后金额进行累加，否则使用合同金额进行累加
        BigDecimal baseAmount = concludeE.getChangContractAmount().compareTo(BigDecimal.ZERO) > 0
                ? concludeE.getChangContractAmount() : concludeE.getContractAmountOriginalRate();
        concludeE.setChangContractAmount(contractPayConcludeE.getContractAmountOriginalRate().add(baseAmount));
        concludeE.setGmtModify(LocalDateTime.now());
        contractPayConcludeMapper.updateById(concludeE);
        //--------原逻辑end--------

        //查询有无NK合同，若有变更合同结束时间及变更后合同金额
        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(concludeE.getId());
        if(Objects.nonNull(nkContract)){
            contractPayConcludeMapper.updateGmtExpireEndById(nkContract.getId(),concludeE.getGmtExpireEnd());
            contractPayConcludeMapper.updateChangContractAmountById(nkContract.getId(),concludeE.getChangContractAmount());
        }

        changePayFund(contractIdList);

    }

    //根据合同ID对清单进行调减
    private void changePayFund(List<String> contractIdList) {
        for(String contractId : contractIdList){
            //查询对应合同清单
            List<ContractPayFundE> funList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .eq(ContractPayFundE.CONTRACT_ID, contractId)
                    .eq(ContractPayFundE.DELETED, 0));
            if(CollectionUtils.isEmpty(funList)){
                continue;
            }
            ContractPayConcludeE mainContract = contractPayConcludeMapper.queryByContractId(contractId);

            //筛选出金额为0的清单
            List<ContractPayFundE> funZeroList = funList.stream().filter(x->Objects.isNull(x.getAmount()) || x.getAmount().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(funZeroList)){

                List<String> funIdList = funZeroList.stream().map(ContractPayFundE::getId).collect(Collectors.toList());
                //根据清单查询对应结算明细数据
                LambdaQueryWrapper<ContractPaySettDetailsE> querySetdetWrapper = new LambdaQueryWrapper<>();
                querySetdetWrapper.in(ContractPaySettDetailsE :: getPayFundId,  funIdList)
                        .eq(ContractPaySettDetailsE::getDeleted,0);
                List<ContractPaySettDetailsE> settDetailsList = contractPaySettDetailsMapper.selectList(querySetdetWrapper);
                if(CollectionUtils.isNotEmpty(settDetailsList)){
                    //删除结算单明细
                    settDetailsList.forEach(x->{
                        contractPaySettDetailsMapper.deleteById(x.getId());
                    });
                    //根据结算明细对应结算单查询结算单数据
                    LambdaQueryWrapper<ContractPaySettlementConcludeE> querySetWrapper = new LambdaQueryWrapper<>();
                    querySetWrapper.eq(ContractPaySettlementConcludeE::getContractId, contractId)
                            .in(ContractPaySettlementConcludeE::getId,settDetailsList.stream().map(ContractPaySettDetailsE :: getSettlementId).collect(Collectors.toList()))
                            .eq(ContractPaySettlementConcludeE::getDeleted,0);
                    List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(querySetWrapper);
                    if(CollectionUtils.isNotEmpty(settlementList)){
                        //删除结算单
                        settlementList.forEach(x->
                            contractPaySettlementConcludeMapper.deleteById(x.getId())
                        );
                    }
                }

                //查询结算计划信息
                LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                        .eq(ContractPayPlanConcludeE::getContractId, contractId)
                        .in(ContractPayPlanConcludeE::getContractPayFundId, funIdList)
                        .eq(ContractPayPlanConcludeE::getDeleted,0);
                List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
                if(CollectionUtils.isNotEmpty(concludePlanList)) {
                    //删除对应结算计划
                    concludePlanList.forEach(x ->
                        contractPayPlanConcludeMapper.deleteById(x.getId())
                    );
                    List<PayCostPlanE> payCostList = payCostPlanMapper.selectList(Wrappers.<PayCostPlanE>lambdaQuery()
                            .eq(PayCostPlanE::getContractId, contractId)
                            .in(PayCostPlanE::getPlanId, concludePlanList.stream().map(ContractPayPlanConcludeE::getId).collect(Collectors.toList()))
                            .eq(PayCostPlanE::getDeleted, 0));
                    if (CollectionUtils.isNotEmpty(payCostList)) {
                        payCostList.forEach(x -> {
                            payCostPlanMapper.deleteById(x.getId());
                        });
                        //删除临时账单等数据
                        try{
                            List<String> billIdList = payCostList.stream().map(PayCostPlanE::getBillId).collect(Collectors.toList());
                            financeFeignClient.deleteReceivableAndHtBzd(billIdList, mainContract.getCommunityId());
                        } catch (Exception e) {
                            log.error("删除临时账单等数据失败", e);
                            throw new OwlBizException("删除临时账单等数据失败");
                        }
                    }
                }
                //删除清单
                funZeroList.forEach(x-> {
                    contractPayFundMapper.deleteById(x.getId());
                    contractPayFundMapper.deleteByMainId(x.getId());

                });

                //查询该合同结算数据，若只剩一条父级数据，则删除
                LambdaQueryWrapper<ContractPaySettlementConcludeE> queryTotalWrapper = new LambdaQueryWrapper<>();
                queryTotalWrapper.eq(ContractPaySettlementConcludeE::getContractId, contractId)
                        .eq(ContractPaySettlementConcludeE::getDeleted,0);
                List<ContractPaySettlementConcludeE> settlementTotalList = contractPaySettlementConcludeMapper.selectList(queryTotalWrapper);
                if(settlementTotalList.size() == 1 && settlementTotalList.get(0).getPid().equals("0")){
                    settlementTotalList.forEach(settlementTotal->
                        contractPaySettlementConcludeMapper.deleteById(settlementTotal.getId()));
                }

                //查询结算计划信息
                LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanTotalWrapper = new LambdaQueryWrapper<>();
                queryPlanTotalWrapper.eq(ContractPayPlanConcludeE::getContractId, contractId)
                        .eq(ContractPayPlanConcludeE::getDeleted,0);
                List<ContractPayPlanConcludeE> concludePlanTotalList = contractPayPlanConcludeMapper.selectList(queryPlanTotalWrapper);

                if(concludePlanTotalList.size() == 1 && concludePlanTotalList.get(0).getPid().equals("0")){
                    concludePlanTotalList.forEach(plan->
                        contractPayPlanConcludeMapper.deleteById(plan.getId()));
                }
            }

            //获取有调减的清单
            List<ContractPayFundE> funNoZeroList = funList.stream().filter(x->Objects.nonNull(x.getAmount()) && x.getAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(funNoZeroList)){

                //根据合同ID获取合同报账单中对下计提非进行中（1待推送/3推送失败/5已驳回/6单据驳回/8制单失败）临时账单ID
                List<String> billList = financeFeignClient.getReceivableBillIdList(contractId, mainContract.getCommunityId());
                //根据临时账单ID查询对应成本计划
                List<String> planIdList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(billList)){
                    List<PayCostPlanE> payCostList = payCostPlanMapper.selectList(Wrappers.<PayCostPlanE>lambdaQuery()
                            .eq(PayCostPlanE::getContractId, contractId)
                            .in(PayCostPlanE :: getBillId,  billList)
                            .eq(PayCostPlanE::getDeleted, 0));
                    if(CollectionUtils.isNotEmpty(payCostList)){
                        planIdList = payCostList.stream().map(PayCostPlanE::getPlanId).collect(Collectors.toList());
                    }
                }

                //根据结算明细对应结算单查询结算单数据
                LambdaQueryWrapper<ContractPaySettlementConcludeE> querySetWrapper = new LambdaQueryWrapper<>();
                querySetWrapper.eq(ContractPaySettlementConcludeE::getContractId, contractId)
                        .in(ContractPaySettlementConcludeE :: getReviewStatus , ReviewStatusEnum.审批中.getCode(), ReviewStatusEnum.已通过.getCode())
                        .eq(ContractPaySettlementConcludeE::getDeleted,0);
                List<ContractPaySettlementConcludeE> settlementList = contractPaySettlementConcludeMapper.selectList(querySetWrapper);
                if(CollectionUtils.isNotEmpty(settlementList)){
                    planIdList.addAll(settlementPlanRelationMapper.selectList(Wrappers.<ContractPayConcludeSettlementPlanRelationE>lambdaQuery()
                            .in(ContractPayConcludeSettlementPlanRelationE::getSettlementId, settlementList.stream().map(ContractPaySettlementConcludeE :: getId).collect(Collectors.toList()))).stream().map(ContractPayConcludeSettlementPlanRelationE :: getPlanId).collect(Collectors.toList()));
                }
                for(ContractPayFundE fun: funNoZeroList){
                    //查询结算计划信息
                    LambdaQueryWrapper<ContractPayPlanConcludeE> queryPlanWrapper = new LambdaQueryWrapper<>();
                    queryPlanWrapper.ne(ContractPayPlanConcludeE::getPid, 0)
                            .eq(ContractPayPlanConcludeE::getContractId, contractId)
                            .eq(ContractPayPlanConcludeE::getContractPayFundId, fun.getId())
                            .eq(ContractPayPlanConcludeE::getDeleted,0);
                    List<ContractPayPlanConcludeE> concludePlanList = contractPayPlanConcludeMapper.selectList(queryPlanWrapper);
                    if(CollectionUtils.isNotEmpty(concludePlanList)){
                        //该清单对应计划总金额
                        BigDecimal planTotalAmount =  concludePlanList.stream()
                                .filter(Objects::nonNull)
                                .map(ContractPayPlanConcludeE::getPlannedCollectionAmount)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        if(fun.getAmount().compareTo(planTotalAmount) >= 0){
                            continue;
                        }
                        //计算清单于计划金额差值
                        BigDecimal ceAmount = planTotalAmount.subtract(fun.getAmount());
                        List<String> finalPlanIdList = planIdList;
                        concludePlanList = concludePlanList.stream().filter(x->CollectionUtils.isNotEmpty(finalPlanIdList) && !finalPlanIdList.contains(x.getId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(concludePlanList)){
                            Map<String, BigDecimal> funMap = new HashMap<>();
                            concludePlanList.forEach(plan->
                                funMap.put(plan.getId(), plan.getPlannedCollectionAmount())
                            );
                            //该清单对应计划总金额
                            BigDecimal planTotalNowAmount =  concludePlanList.stream()
                                    .filter(Objects::nonNull)
                                    .map(ContractPayPlanConcludeE::getPlannedCollectionAmount)
                                    .filter(Objects::nonNull)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            Map<String, BigDecimal> funReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funMap, planTotalNowAmount,ceAmount);
                            for(ContractPayPlanConcludeE plan : concludePlanList){
                                if(!"差额纳税".equals(plan.getTaxRate())){
                                    plan.setPlannedCollectionAmount(plan.getPlannedCollectionAmount().subtract(funReductionAmountMap.get(plan.getId())));
                                    BigDecimal taxRateNumber = new BigDecimal(plan.getTaxRate()).divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
                                    plan.setNoTaxAmount(plan.getPlannedCollectionAmount().divide(BigDecimal.ONE.add(taxRateNumber),2, RoundingMode.HALF_UP));
                                    plan.setTaxAmount(plan.getPlannedCollectionAmount().subtract(plan.getNoTaxAmount()));
                                    contractPayPlanConcludeMapper.updateById(plan);
                                }else{
                                    BigDecimal planAmount = plan.getPlannedCollectionAmount();
                                    BigDecimal averageAmount = plan.getTaxAmount().divide(planAmount, 2, RoundingMode.HALF_UP);
                                    plan.setPlannedCollectionAmount(plan.getPlannedCollectionAmount().subtract(funReductionAmountMap.get(plan.getId())));
                                    plan.setTaxAmount(plan.getPlannedCollectionAmount().multiply(plan.getPlannedCollectionAmount()).setScale(2, RoundingMode.HALF_UP));
                                    plan.setNoTaxAmount(plan.getPlannedCollectionAmount().subtract(plan.getTaxAmount()));
                                    contractPayPlanConcludeMapper.updateById(plan);
                                }
                                if(plan.getPlannedCollectionAmount().compareTo(BigDecimal.ZERO) == 0){
                                    List<PayCostPlanE> payCostList = payCostPlanMapper.selectList(Wrappers.<PayCostPlanE>lambdaQuery()
                                            .eq(PayCostPlanE::getContractId, contractId)
                                            .eq(PayCostPlanE::getPlanId, plan.getId())
                                            .eq(PayCostPlanE::getDeleted, 0));
                                    if (CollectionUtils.isNotEmpty(payCostList)) {
                                        payCostList.forEach(x ->
                                            payCostPlanMapper.deleteById(x.getId())
                                        );
                                        //删除临时账单等数据
                                        try{
                                            List<String> billIdList = payCostList.stream().map(PayCostPlanE::getBillId).collect(Collectors.toList());
                                            financeFeignClient.deleteReceivableAndHtBzd(billIdList, mainContract.getCommunityId());
                                        } catch (Exception e) {
                                            log.error("删除临时账单等数据失败", e);
                                            throw new OwlBizException("删除临时账单等数据失败");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void mergeEndTime(ContractPayConcludeE mainContract, String contractId) {
        ContractPayConcludeE concludeE = contractPayConcludeMapper.selectById(contractId);
        // 若子合同的开始时间落在主合同的开始时间和结束时间+1天之间,则将主合同的结束时间设为max(原结束时间,子合同结束时间)
        LocalDate childStart = concludeE.getGmtExpireStart();
        if (!childStart.isBefore(mainContract.getGmtExpireStart()) &&
                !childStart.isAfter(mainContract.getGmtExpireEnd().plusDays(1))) {
            LocalDate childEnd = concludeE.getGmtExpireEnd();
            LocalDate newEnd = childEnd.isAfter(mainContract.getGmtExpireEnd()) ? childEnd : mainContract.getGmtExpireEnd();
            mainContract.setGmtExpireEnd(newEnd);
            contractPayConcludeMapper.updateGmtExpireEndById(mainContract.getId(),newEnd);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleMergePayFund(String pid){
        // 查询对应的支出合同的补充协议
        QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<ContractPayConcludeE>()
                .eq(ContractPayConcludeE.PID, pid)
                .eq(ContractPayConcludeE.CONTRACT_TYPE, 2)
                .eq(ContractPayConcludeE.REVIEW_STATUS, 2)
                .eq(ContractPayConcludeE.DELETED, 0);
        List<ContractPayConcludeE> list = contractPayConcludeMapper.selectList(queryWrapper);
        // 循环mergeFund
        if (CollectionUtils.isNotEmpty(list)){
            list.forEach(item -> mergePayFund(item.getId(),pid));
        }
    }

    public void batchHandleMergePayFund(MergePayFundPidF mergePayFundPidF) {
        if (Objects.isNull(mergePayFundPidF) || CollectionUtils.isEmpty(mergePayFundPidF.getPids())) {
            return;
        }
        mergePayFundPidF.getPids().forEach(this::handleMergePayFund);
    }
    private void mergePayFund(String contractId, String pid) {

        IdentityInfo identityInfo = curIdentityInfo();
        LocalDateTime now = LocalDateTime.now();
        log.info("mergePayFund start,contractId is:{}",contractId);
        List<ContractPayFundE> bcFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractId)
                .eq(ContractPayFundE.DELETED, 0));

        if (CollectionUtils.isEmpty(bcFunList)) {
            return;
        }

        List<ContractPayFundE> mainFunList = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, pid)
                .eq(ContractPayFundE.DELETED, 0));

        if (CollectionUtils.isEmpty(mainFunList)) {
            mainFunList = new ArrayList<>();
        }

        Map<String, ContractPayFundE> existContractPayFundMap = mainFunList.stream()
                .collect(Collectors.toMap(
                        this::generateCompositeKey,
                        fund -> fund,
                        (fund1, fund2) -> fund1
                ));

        List<ContractPayFundE> addContractPayFund = new ArrayList<>();
        List<ContractPayFundE> updatedExistFunds = new ArrayList<>();

        for (ContractPayFundE newFund : bcFunList) {
            String key = generateCompositeKey(newFund);
            ContractPayFundE existFund = existContractPayFundMap.get(key);

            if (existFund != null) {
                existFund.setAmountNum(
                        (existFund.getAmountNum() != null ? existFund.getAmountNum() : BigDecimal.ZERO)
                                .add(newFund.getAmountNum() != null ? newFund.getAmountNum() : BigDecimal.ZERO)
                );
                existFund.setAmount(
                        (existFund.getAmount() != null ? existFund.getAmount() : BigDecimal.ZERO)
                                .add(newFund.getAmount() != null ? newFund.getAmount() : BigDecimal.ZERO)
                );
                existFund.setAmountWithOutRate(
                        (existFund.getAmountWithOutRate() != null ? existFund.getAmountWithOutRate() : BigDecimal.ZERO)
                                .add(newFund.getAmountWithOutRate() != null ? newFund.getAmountWithOutRate() : BigDecimal.ZERO)
                );
                existFund.setTaxRateAmount(
                        (existFund.getTaxRateAmount() != null ? existFund.getTaxRateAmount() : BigDecimal.ZERO)
                                .add(newFund.getTaxRateAmount() != null ? newFund.getTaxRateAmount() : BigDecimal.ZERO)
                );
                updatedExistFunds.add(existFund);

                if(StringUtils.isNotEmpty(existFund.getCbApportionId())){
                    ContractPayConcludeE concludeMainE = contractPayConcludeMapper.selectById(pid);
                    List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = contractProjectPlanMonthlyAllocationAppService.distributeCost(existFund.getAmountWithOutRate(), concludeMainE.getGmtExpireStart(), concludeMainE.getGmtExpireEnd());
                    if(CollectionUtils.isNotEmpty(monthlyAllocationVS)){

                        for(ContractProjectPlanMonthlyAllocationV monthlyAllocationV : monthlyAllocationVS){
                            ContractPayCostApportionE costApportionE = contractPayCostApportionMapper.selectOne(new QueryWrapper<ContractPayCostApportionE>()
                                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                                    .eq(ContractPayCostApportionE.CONTRACT_ID, pid)
                                    .eq(ContractPayCostApportionE.DELETED, 0)
                                    .eq(ContractPayCostApportionE.APPORTION_TYPE, CostApportionTypeEnum.分摊金额.getCode())
                                    .eq(ContractPayCostApportionE.YEAR, monthlyAllocationV.getYear())
                                    .eq(ContractPayCostApportionE.PID, existFund.getCbApportionId())
                            );
                            if(Objects.isNull(costApportionE)){
                                costApportionE = contractPayCostApportionMapper.selectOne(new QueryWrapper<ContractPayCostApportionE>()
                                        .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                                        .eq(ContractPayCostApportionE.CONTRACT_ID, contractId)
                                        .eq(ContractPayCostApportionE.DELETED, 0)
                                        .eq(ContractPayCostApportionE.APPORTION_TYPE, CostApportionTypeEnum.分摊金额.getCode())
                                        .eq(ContractPayCostApportionE.YEAR, monthlyAllocationV.getYear())
                                        .eq(ContractPayCostApportionE.PID, newFund.getCbApportionId())
                                );
                            }
                            //删除原累计金额
                            ContractPayCostApportionE costApportionTotal = contractPayCostApportionMapper.selectOne(new QueryWrapper<ContractPayCostApportionE>()
                                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                                    .eq(ContractPayCostApportionE.CONTRACT_ID, pid)
                                    .eq(ContractPayCostApportionE.DELETED, 0)
                                    .eq(ContractPayCostApportionE.APPORTION_TYPE, CostApportionTypeEnum.累计分摊金额.getCode())
                                    .eq(ContractPayCostApportionE.YEAR, monthlyAllocationV.getYear())
                                    .eq(ContractPayCostApportionE.PID, existFund.getCbApportionId())
                            );
                            if(Objects.nonNull(costApportionTotal)){
                                contractPayCostApportionMapper.deletedCostDataOne(pid,costApportionTotal.getId());
                            }

                            ContractPayCostApportionE costApportionPidE = new ContractPayCostApportionE();
                            BeanUtils.copyProperties(monthlyAllocationV, costApportionPidE);
                            costApportionPidE.setApportionType(CostApportionTypeEnum.累计分摊金额.getCode());
                            costApportionPidE.setDynamicCostGuid(costApportionE.getDynamicCostGuid());
                            costApportionPidE.setCostControlTypeEnum(costApportionE.getCostControlTypeEnum());
                            costApportionPidE.setCostControlTypeName(costApportionE.getCostControlTypeName());
                            costApportionPidE.setContractId(pid);
                            costApportionPidE.setPid(existFund.getCbApportionId());
                            costApportionPidE.setTenantId(identityInfo.getTenantId());
                            costApportionPidE.setCreator(identityInfo.getUserId());
                            costApportionPidE.setCreatorName(identityInfo.getUserName());
                            costApportionPidE.setGmtCreate(now);
                            costApportionPidE.setOperator(identityInfo.getUserId());
                            costApportionPidE.setOperatorName(identityInfo.getUserName());
                            costApportionPidE.setGmtModify(now);
                            contractPayCostApportionMapper.insert(costApportionPidE);
                        }
                    }
                }
            } else {
                ContractPayFundE fundToAdd = new ContractPayFundE();
                BeanUtils.copyProperties(newFund, fundToAdd);
                fundToAdd.setId(null);
                fundToAdd.setContractId(pid);
                if(StringUtils.isNotEmpty(newFund.getCbApportionId())){
                    ContractPayCostApportionE costApportionE = contractPayCostApportionMapper.selectOne(new QueryWrapper<ContractPayCostApportionE>()
                                    .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                                    .eq(ContractPayCostApportionE.CONTRACT_ID, contractId)
                                    .eq(ContractPayCostApportionE.DELETED, 0)
                                    .eq(ContractPayCostApportionE.ID, newFund.getCbApportionId()));
                    ContractPayCostApportionE saveCost = new ContractPayCostApportionE();
                    BeanUtils.copyProperties(costApportionE,saveCost);
                    saveCost.setId(null);
                    saveCost.setContractId(pid);
                    contractPayCostApportionMapper.insert(saveCost);
                    List<ContractPayCostApportionE> costApportionList = contractPayCostApportionMapper.selectList(new QueryWrapper<ContractPayCostApportionE>()
                            .eq(ContractPayCostApportionE.TENANT_ID, tenantId())
                            .eq(ContractPayCostApportionE.CONTRACT_ID, contractId)
                            .eq(ContractPayCostApportionE.DELETED, 0)
                            .eq(ContractPayCostApportionE.PID, newFund.getCbApportionId()));
                    for(ContractPayCostApportionE cost: costApportionList){
                        cost.setId(null);
                        cost.setPid(saveCost.getId());
                        cost.setContractId(pid);
                        contractPayCostApportionMapper.insert(cost);
                    }
                }
                addContractPayFund.add(fundToAdd);
            }
        }

        try {
            ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(pid);
            if (!updatedExistFunds.isEmpty()) {
                contractPayFundService.updateBatchById(updatedExistFunds);
                if(Objects.nonNull(nkConclude)){
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
                    updatedExistFunds.forEach(x->{
                        x.setId(funMainMap.get(x.getId()));
                        x.setContractId(nkConclude.getId());
                    });
                    contractPayFundService.updateBatchById(updatedExistFunds);
                }
            }
            if (!addContractPayFund.isEmpty()) {
                contractPayFundService.saveBatch(addContractPayFund);
                if(Objects.nonNull(nkConclude)){
                    addContractPayFund.forEach(x->{
                        x.setMainId(x.getId());
                        x.setId(null);
                        x.setContractId(nkConclude.getId());
                    });
                    contractPayFundService.saveBatch(addContractPayFund);
                }
            }
        } catch (Exception e) {
            log.error("合并合同清单时发生异常,error:{}",e.getMessage(),e);
        }
        log.info("mergePayFund end,contractId is:{}",contractId);
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



    public ContractPayConcludeListV queryInfoNew(ContractPayConcludeListF conditions) {
        ContractPayConcludeListV retV = new ContractPayConcludeListV();
        ContractOrgPermissionV orgPermission = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermission.getRadio())) {
            return retV;
        }
        List<ContractPayConcludeV> retVList;
        if (RadioEnum.ALL.equals(orgPermission.getRadio())) {
            retVList = contractPayConcludeMapper.queryInfoBak(conditions.getNameNo(), tenantId(), conditions.getContractId(), conditions.getIsNK());
        } else {
            //TODO YJ
            retVList = contractPayConcludeMapper.queryInfoNew(conditions.getNameNo(), tenantId(), conditions.getContractId(), orgPermission.getOrgIds(),  conditions.getIsNK());
        }
        retV.setInfoList(retVList);
        if (!CollectionUtils.isEmpty(retVList)) {
            List<String> contractIdList = retVList.stream().map(ContractPayConcludeV::getId).collect(Collectors.toList());
            List<ContractPayFundE> list = contractPayFundService.list(new QueryWrapper<ContractPayFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .in(ContractPayFundE.CONTRACT_ID, contractIdList)
                    .eq(ContractPayFundE.DELETED, 0));
            Map<String,ContractPayFundE> map = list.stream().collect(Collectors.toMap(ContractPayFundE::getContractId, v->v, (v1, v2) -> v1));
            retVList.forEach(ret-> {
                ret.setSplitMode((MapUtils.isNotEmpty(map) && null != map.get(ret.getId()))? map.get(ret.getId()).getPayWayId() : null);

                BigDecimal changContractAmount = ret.getChangContractAmount();
                BigDecimal contractAmountOriginalRate = ret.getContractAmountOriginalRate();
                BigDecimal zeroAmount = new BigDecimal("0.00");
                if (changContractAmount == null || changContractAmount.compareTo(zeroAmount) == 0) {
                    ret.setChangContractAmount(null);
                } else {
                    ret.setChangContractAmount(changContractAmount);
                }

            });
        }
        return retV;
    }
}
