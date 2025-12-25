package com.wishare.contract.domains.service.revision.income;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.revision.ContractInfoToSpaceResourceF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.pay.MergePayFundPidF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.revision.common.ContractInfoToFxmCommonService;
import com.wishare.contract.apps.service.revision.income.ContractIncomeConcludeExpandAppService;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import com.wishare.contract.domains.enums.ConcludeContractNatureEnum;
import com.wishare.contract.domains.enums.revision.ContractBusinessLineEnum;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeExpandMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.bond.pay.RevisionBondPayService;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.income.*;
import com.wishare.contract.infrastructure.utils.DateTimeUtil;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 收入合同订立信息表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeConcludeService extends ServiceImpl<ContractIncomeConcludeMapper, ContractIncomeConcludeE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeExpandMapper contractIncomeConcludeExpandMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ExternalFeignClient externalFeignClient;

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
    private RevisionBondPayService bondPayService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomePlanConcludeService incomePlanConcludeService;


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeProfitLossService lossService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractInfoToFxmCommonService contractInfoToFxmCommonService;


    @Resource
    @Lazy
    private ContractIncomeFundService contractIncomeFundService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractOrgCommonService contractOrgCommonService;
    @Autowired
    private ContractIncomeConcludeExpandAppService contractIncomeConcludeExpandAppService;
    @Autowired
    private ConfigFeignClient configFeignClient;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeConcludeV> get(ContractIncomeConcludeF conditions){
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractIncomeConcludeE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractIncomeConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractIncomeConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getDealTypeId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE_ID, conditions.getDealTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getDealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE, conditions.getDealType());
        }

        if (Objects.nonNull(conditions.getSealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE, conditions.getSealType());
        }

        if (StringUtils.isNotBlank(conditions.getSealTypeName())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE_NAME, conditions.getSealTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractIncomeConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractIncomeConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectOne(queryWrapper);
        if (contractIncomeConcludeE != null) {
            Optional<ContractIncomeConcludeV> incomeConclude = Optional.of(Global.mapperFacade.map(contractIncomeConcludeE, ContractIncomeConcludeV.class));
            ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
            concludeExpandF.setContractId(contractIncomeConcludeE.getId());
            ContractIncomeConcludeExpandV concludeExpandV = contractIncomeConcludeExpandAppService.get(concludeExpandF);
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
                        incomeConclude.ifPresent(income -> {
                            income.setZcfstr(zcfstr.toString());
                            income.setSjfkrstr(sjfkrstr.toString());
                        });
                    }
                } catch (BeansException e) {
                }
            }

            return incomeConclude;
        }else {
            return Optional.empty();
        }
    }

    public ContractIncomeConcludeListV queryInfo(ContractIncomeConcludeListF conditions){
        ContractIncomeConcludeListV retV = new ContractIncomeConcludeListV();
        List<ContractIncomeConcludeV> retVList = contractIncomeConcludeMapper.queryInfo(conditions.getNameNo(),tenantId(),conditions.getContractId());
        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        if (!CollectionUtils.isEmpty(retVList)) {
            List<String> contractIdList = retVList.stream().map(ContractIncomeConcludeV::getId).collect(Collectors.toList());
            List<ContractIncomeFundE> list = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .in(ContractPayFundE.CONTRACT_ID, contractIdList)
                    .eq(ContractPayFundE.DELETED, 0));
            Map<String,ContractIncomeFundE> map = list.stream().collect(Collectors.toMap(ContractIncomeFundE::getContractId, v->v, (v1, v2) -> v1));
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
                if(StringUtils.isNotEmpty(ret.getConmanagetype())) {
                    List<DictionaryCode> value = new ArrayList<>();
                    if(ContractBusinessLineEnum.物管.getCode().equals(ret.getContractBusinessLine())){
                        value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), ret.getConmanagetype());
                    }else{
                        value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.建管合同管理类别.getCode(), ret.getConmanagetype());
                    }
                    if (CollectionUtils.isNotEmpty(value)) {
                        ret.setConmanagetypeName(value.get(0).getName());
                    }
                }
            });
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public ContractIncomeConcludeListV queryInfoNew(ContractIncomeConcludeListF conditions) {
        ContractIncomeConcludeListV retV = new ContractIncomeConcludeListV();
        ContractOrgPermissionV orgPermission = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermission.getRadio())) {
            return retV;
        }
        //直接走下面的sql，在sql中判断是否进行权限判定，若权限是ALL则不会拼接部门id，若权限是APPOINT，则会拼接部门id
        List<ContractIncomeConcludeV> retVList = contractIncomeConcludeMapper.queryInfoNew(conditions.getNameNo(), tenantId(),
                conditions.getContractId(), orgPermission.getOrgIds());
        retV.setInfoList(retVList);
        if (!CollectionUtils.isEmpty(retVList)) {
            List<String> contractIdList = retVList.stream().map(ContractIncomeConcludeV::getId).collect(Collectors.toList());
            List<ContractIncomeFundE> list = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                    .orderByDesc(ContractPayFundE.GMT_CREATE)
                    .eq(ContractPayFundE.TENANT_ID, tenantId())
                    .in(ContractPayFundE.CONTRACT_ID, contractIdList)
                    .eq(ContractPayFundE.DELETED, 0));
            Map<String,ContractIncomeFundE> map = list.stream().collect(Collectors.toMap(ContractIncomeFundE::getContractId, v->v, (v1, v2) -> v1));
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

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public ContractIncomeConcludeListV list(ContractIncomeConcludeListF conditions){
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotBlank(conditions.getNameNo())){
            queryWrapper.like(ContractPayConcludeE.NAME, conditions.getNameNo()).or()
                    .like(ContractPayConcludeE.CONTRACT_NO, conditions.getNameNo());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.like(ContractIncomeConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.like(ContractIncomeConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractIncomeConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractIncomeConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getDealTypeId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE_ID, conditions.getDealTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getDealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE, conditions.getDealType());
        }

        if (Objects.nonNull(conditions.getSealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE, conditions.getSealType());
        }

        if (StringUtils.isNotBlank(conditions.getSealTypeName())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE_NAME, conditions.getSealTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractIncomeConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractIncomeConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractIncomeConcludeE.ID, conditions.getIndexId());
        }
        queryWrapper.eq(ContractPayConcludeE.TENANT_ID, tenantId());
        queryWrapper.orderByDesc(ContractIncomeConcludeE.ID)
                .eq(ContractIncomeConcludeE.TENANT_ID, tenantId());
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractIncomeConcludeV> retVList = Global.mapperFacade.mapAsList(contractIncomeConcludeMapper.selectList(queryWrapper),ContractIncomeConcludeV.class);
        for(int i = 0; i < retVList.size(); i ++){
            retVList.get(i).setNameNo(retVList.get(i).getName() + "-" + retVList.get(i).getContractNo());
        }
        ContractIncomeConcludeListV retV = new ContractIncomeConcludeListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractIncomeConcludeSaveF contractIncomeConcludeF){
        ContractIncomeConcludeE map = Global.mapperFacade.map(contractIncomeConcludeF, ContractIncomeConcludeE.class);
        contractIncomeConcludeMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param contractIncomeConcludeF 根据Id更新
    */
    public void update(ContractIncomeConcludeUpdateF contractIncomeConcludeF){
        if (contractIncomeConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeConcludeE map = Global.mapperFacade.map(contractIncomeConcludeF, ContractIncomeConcludeE.class);
        contractIncomeConcludeMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        //-- 校验保证金计划
        List<RevisionBondPayE> list = bondPayService.list(new QueryWrapper<RevisionBondPayE>().eq(RevisionBondPayE.CONTRACT_ID, id));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new OwlBizException("该合同关联了保证金数据，无法被删除");
        }
        //-- 校验收款计划
        List<ContractIncomePlanConcludeE> list1 = incomePlanConcludeService.list(new QueryWrapper<ContractIncomePlanConcludeE>()
                .eq(ContractIncomePlanConcludeE.CONTRACT_ID, id));
        if (CollectionUtils.isNotEmpty(list1)) {
            throw new OwlBizException("该合同关联了收款计划，无法被删除");
        }
        contractIncomeConcludeMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeConcludeV> page(PageF<ContractIncomeConcludePageF> request) {
        ContractIncomeConcludePageF conditions = request.getConditions();
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.like(ContractIncomeConcludeE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.like(ContractIncomeConcludeE.CONTRACT_NO, conditions.getContractNo());
        }

        if (Objects.nonNull(conditions.getContractNature())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_NATURE, conditions.getContractNature());
        }

        if (Objects.nonNull(conditions.getCategoryId())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_ID, conditions.getCategoryId());
        }

        if (StringUtils.isNotBlank(conditions.getCategoryName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CATEGORY_NAME, conditions.getCategoryName());
        }

        if (StringUtils.isNotBlank(conditions.getPid())) {
            queryWrapper.eq(ContractIncomeConcludeE.PID, conditions.getPid());
        }

        if (Objects.nonNull(conditions.getContractType())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_TYPE, conditions.getContractType());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_AID, conditions.getPartyAId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BID, conditions.getPartyBId());
        }

        if (StringUtils.isNotBlank(conditions.getPartyAName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_ANAME, conditions.getPartyAName());
        }

        if (StringUtils.isNotBlank(conditions.getPartyBName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PARTY_BNAME, conditions.getPartyBName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_ID, conditions.getDepartId());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEPART_NAME, conditions.getDepartName());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(ContractIncomeConcludeE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getSignPerson())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON, conditions.getSignPerson());
        }

        if (StringUtils.isNotBlank(conditions.getSignPersonId())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGN_PERSON_ID, conditions.getSignPersonId());
        }

        if (Objects.nonNull(conditions.getSignDate())) {
            queryWrapper.gt(ContractIncomeConcludeE.SIGN_DATE, conditions.getSignDate());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(ContractIncomeConcludeE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalId())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_ID, conditions.getPrincipalId());
        }

        if (StringUtils.isNotBlank(conditions.getDealTypeId())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE_ID, conditions.getDealTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getDealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.DEAL_TYPE, conditions.getDealType());
        }

        if (Objects.nonNull(conditions.getSealType())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE, conditions.getSealType());
        }

        if (StringUtils.isNotBlank(conditions.getSealTypeName())) {
            queryWrapper.eq(ContractIncomeConcludeE.SEAL_TYPE_NAME, conditions.getSealTypeName());
        }

        if (StringUtils.isNotBlank(conditions.getPrincipalName())) {
            queryWrapper.eq(ContractIncomeConcludeE.PRINCIPAL_NAME, conditions.getPrincipalName());
        }

        if (Objects.nonNull(conditions.getContractAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.CONTRACT_AMOUNT, conditions.getContractAmount());
        }

        if (Objects.nonNull(conditions.getBond())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND, conditions.getBond());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(ContractIncomeConcludeE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getGmtExpireStart())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_START, conditions.getGmtExpireStart());
        }

        if (Objects.nonNull(conditions.getGmtExpireEnd())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_EXPIRE_END, conditions.getGmtExpireEnd());
        }

        if (Objects.nonNull(conditions.getTempId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_ID, conditions.getTempId());
        }

        if (Objects.nonNull(conditions.getIsBackDate())) {
            queryWrapper.eq(ContractIncomeConcludeE.IS_BACK_DATE, conditions.getIsBackDate());
        }

        if (StringUtils.isNotBlank(conditions.getTempFilekey())) {
            queryWrapper.eq(ContractIncomeConcludeE.TEMP_FILEKEY, conditions.getTempFilekey());
        }

        if (Objects.nonNull(conditions.getSigningMethod())) {
            queryWrapper.eq(ContractIncomeConcludeE.SIGNING_METHOD, conditions.getSigningMethod());
        }

        if (Objects.nonNull(conditions.getWarnState())) {
            queryWrapper.eq(ContractIncomeConcludeE.WARN_STATE, conditions.getWarnState());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(ContractIncomeConcludeE.STATUS, conditions.getStatus());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractIncomeConcludeE.GMT_CREATE);
        }
        queryWrapper.eq(ContractIncomeConcludeE.TENANT_ID, tenantId());
        Page<ContractIncomeConcludeE> page = contractIncomeConcludeMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeV.class));
    }

    /**
     * 该接口供给后端
     *
     * @param request 请求分页的参数
     * @return 查询出的分页列表
     */
    public PageV<ContractIncomeConcludeV> choose(PageF<ContractIncomePageF> request) {
        return null;
    }

    /**
     * 收入合同列表平铺
     *
     * @param request
     * @return
     */
    public PageV<ContractIncomeConcludeTreeV> frontPageV2(PageF<SearchF<ContractIncomeConcludeE>> request) {
        Page<ContractIncomeConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        List<Field> fields = request.getConditions().getFields();
        fields.add(new Field(ContractPayConcludeE.TENANT_ID, tenantId(), 1));
        for (Field field : fields){
            if (field.getName().contains("approvalDate") || field.getName().contains("archivedDate")){
                if(Objects.nonNull(field.getMap())){
                    field.setMap(DateTimeUtil.processTimeRange(field.getMap()));
                }
            }
        }
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = request.getConditions().getQueryModel();

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
        IPage<ContractIncomeConcludeTreeV> page = contractIncomeConcludeMapper.queryByPathV2(pageF, queryWrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return PageV.of(request, 0, new ArrayList<>());
        }

        List<ContractIncomeConcludeTreeV> results = new ArrayList<>();
        //汇总全部的pid
        List<String> pIds = page.getRecords().stream().map(ContractIncomeConcludeTreeV::getPid).distinct().collect(Collectors.toList());
        //按照pid进行分组
        Map<String, List<ContractIncomeConcludeTreeV>> pidMap = page.getRecords().stream()
                .collect(Collectors.groupingBy(ContractIncomeConcludeTreeV::getPid));
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
    public PageV<ContractIncomeConcludeTreeV> frontPage(PageF<SearchF<ContractIncomeConcludeE>> request) {
        Page<ContractIncomeConcludeE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractIncomeConcludeE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

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

        IPage<ContractIncomeConcludeTreeV> pageList
                = contractIncomeConcludeMapper.collectionIncomeConcludePage(pageF, queryWrapper);
        List<ContractIncomeConcludeTreeV> records = pageList.getRecords();
        List<String> parentIdList = records.stream().map(ContractIncomeConcludeTreeV::getId).collect(Collectors.toList());
        List<ContractIncomeConcludeTreeV> concludeVList = contractIncomeConcludeMapper.queryByPath(queryWrapper, parentIdList);
        List<ContractIncomeConcludeTreeV> list = TreeUtil.treeing(concludeVList);
        return PageV.of(request, pageList.getTotal(), list);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractIncomeConcludeE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeConcludeE selectOneBy(Consumer<QueryWrapper<ContractIncomeConcludeE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeE中id字段的值, select 指定字段
    *
    * @param fields ContractIncomeConcludeE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeConcludeMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeConcludeE>> consumer) {
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeConcludeMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractIncomeConcludeE中仅包含a字段的值
     *
     * @param fields ContractIncomeConcludeE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeConcludeE> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeConcludeMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractIncomeConcludeE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeConcludeE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeConcludeE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeE中id字段的值, select 指定字段
     *
     * @param fields ContractIncomeConcludeE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeConcludeE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeConcludeE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeConcludeE> queryPage = contractIncomeConcludeMapper.selectPage(page, queryWrapper);
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
    public Boolean dealBtnShowForDetail(ContractIncomeConcludeTreeV map) {
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
        List<ContractIncomeConcludeV> list = contractIncomeConcludeMapper.queryIsExistContract(map.getId());
        if (ObjectUtils.isNotEmpty(list)) {
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
     * 校验收入合同是否能被修改
     * @param concludeE
     */
    public Boolean checkIsCanChangeOrRenewal(ContractIncomeConcludeE concludeE) {
        if(concludeE.getContractNature() != null && concludeE.getContractNature() == 1 && (concludeE.getStatus() == 4 || concludeE.getStatus() == 5 || concludeE.getStatus() == 6)){
            return true;
        }
        return false;
    }

    /**
     * 校验合同是否能被反审
     * @param concludeE
     */
    public Boolean checkIsCanBackReview(ContractIncomeConcludeE concludeE) {

        return true;
    }

    /**
     * 反审时校验合同关联的变更 & 续签流程
     * @param concludeE
     */
    public Boolean checkIsCanChangeOrRenewalForBackReview(ContractIncomeConcludeE concludeE, Integer actionType) {
        List<ContractRelationRecordE> list = contractRelationRecordService.list(new QueryWrapper<ContractRelationRecordE>()
                .eq(ContractRelationRecordE.OLD_ID, concludeE.getId())
                .eq(ContractRelationRecordE.IS_DONE, 0)
                .eq(ContractRelationRecordE.TYPE, actionType));
        if (CollectionUtils.isNotEmpty(list)) {
            for (ContractRelationRecordE recordE : list) {
                ContractIncomeConcludeE addOne = getById(recordE.getAddId());
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


    public List<ContractIncomeConcludeDetailV> getInfoListByIds(List<String> ids) {
        List<ContractIncomeConcludeDetailV> resultList = new ArrayList<>();
        List<ContractIncomeConcludeE> list = contractIncomeConcludeMapper.selectList(new LambdaQueryWrapper<ContractIncomeConcludeE>()
                .in(ContractIncomeConcludeE::getId, ids)
                .select(ContractIncomeConcludeE::getId, ContractIncomeConcludeE::getContractNo, ContractIncomeConcludeE::getName, ContractIncomeConcludeE::getFromid));

        if (CollectionUtils.isNotEmpty(list)) {
            resultList = Global.mapperFacade.mapAsList(list, ContractIncomeConcludeDetailV.class);
        }
        return resultList;
    }

    /**
     * 处理主合同数据
     *
     * @param conAmount 合同金额
     * @param pid       主合同ID
     * @param contractId
     */
    public void handleConcludeSupple(BigDecimal conAmount, String pid, String contractId) {
        ContractIncomeConcludeE concludeE = contractIncomeConcludeMapper.selectById(pid);
        if(ObjectUtils.isNotEmpty(concludeE)){
            mergeIncomeFund(concludeE,contractId,pid);
            mergeEndTime(concludeE,contractId);
            //累加前的金额：若变更后金额不为0，那么使用变更后金额进行累加，否则使用合同金额进行累加
            BigDecimal baseAmount = concludeE.getChangContractAmount().compareTo(BigDecimal.ZERO) > 0
                    ? concludeE.getChangContractAmount() : concludeE.getContractAmountOriginalRate();
            concludeE.setChangContractAmount(conAmount.add(baseAmount));
            concludeE.setGmtModify(LocalDateTime.now());
            contractIncomeConcludeMapper.updateById(concludeE);
        }
    }


    private void mergeEndTime(ContractIncomeConcludeE mainContract, String contractId) {
        ContractIncomeConcludeE concludeE = contractIncomeConcludeMapper.selectById(contractId);
        if (Objects.isNull(mainContract)){
            return;
        }
        // 若子合同的开始时间落在主合同的开始时间和结束时间+1天之间,则将主合同的结束时间设为max(原结束时间,子合同结束时间)
        LocalDate childStart = concludeE.getGmtExpireStart();
        if (!childStart.isBefore(mainContract.getGmtExpireStart()) &&
                !childStart.isAfter(mainContract.getGmtExpireEnd().plusDays(1))) {
            LocalDate childEnd = concludeE.getGmtExpireEnd();
            LocalDate newEnd = childEnd.isAfter(mainContract.getGmtExpireEnd()) ? childEnd : mainContract.getGmtExpireEnd();
            mainContract.setGmtExpireEnd(newEnd);
            contractIncomeConcludeMapper.updateById(mainContract);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleMergeIncomeFund(String pid){
        // 查询对应的支出合同的补充协议
        QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<ContractIncomeConcludeE>()
                .eq(ContractIncomeConcludeE.PID, pid)
                .eq(ContractIncomeConcludeE.CONTRACT_TYPE, 2)
                .eq(ContractIncomeConcludeE.REVIEW_STATUS, 2)
                .eq(ContractIncomeConcludeE.DELETED, 0);
        List<ContractIncomeConcludeE> list = contractIncomeConcludeMapper.selectList(queryWrapper);
        // 循环mergeFund
        if (CollectionUtils.isNotEmpty(list)){
            list.forEach(item -> mergeIncomeFund(null,item.getId(),pid));
        }
    }

    public void batchHandleMergePayFund(MergePayFundPidF mergePayFundPidF) {
        if (Objects.isNull(mergePayFundPidF) || CollectionUtils.isEmpty(mergePayFundPidF.getPids())) {
            return;
        }
        mergePayFundPidF.getPids().forEach(this::handleMergeIncomeFund);
    }

    private void mergeIncomeFund(ContractIncomeConcludeE concludeE, String contractId, String pid) {
        log.info("mergeIncomeFund start,contractId is:{}",contractId);
        List<ContractIncomeFundE> newList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId())
                .eq(ContractIncomeFundE.CONTRACT_ID, contractId));

        if (CollectionUtils.isEmpty(newList)) {
            log.info("mergeIncomeFund end,contractId is:{},reason:there is no incomeFund",contractId);
            return;
        }

        List<ContractIncomeFundE> existList = contractIncomeFundService.list(new QueryWrapper<ContractIncomeFundE>()
                .orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId())
                .eq(ContractIncomeFundE.CONTRACT_ID, pid));

        if (CollectionUtils.isEmpty(existList)) {
            existList = new ArrayList<>();
        }

        Map<String, ContractIncomeFundE> existContractIncomeFundMap = existList.stream()
                .collect(Collectors.toMap(
                        this::generateCompositeKey,
                        fund -> fund,
                        (fund1, fund2) -> fund1
                ));

        List<ContractIncomeFundE> addContractIncomeFund = new ArrayList<>();
        List<ContractIncomeFundE> updatedExistFunds = new ArrayList<>();

        for (ContractIncomeFundE newFund : newList) {
            String key = generateCompositeKey(newFund);
            ContractIncomeFundE existFund = existContractIncomeFundMap.get(key);

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
            } else {
                ContractIncomeFundE fundToAdd = new ContractIncomeFundE();
                BeanUtils.copyProperties(newFund, fundToAdd);
                fundToAdd.setId(null);
                fundToAdd.setContractId(pid);
                addContractIncomeFund.add(fundToAdd);
            }
        }

        try {
            if (!updatedExistFunds.isEmpty()) {
                contractIncomeFundService.updateBatchById(updatedExistFunds);
            }
            if (!addContractIncomeFund.isEmpty()) {
                contractIncomeFundService.saveBatch(addContractIncomeFund);
            }
        } catch (Exception e) {
            log.error("合并合同清单时发生异常,error:{}",e.getMessage(),e);
        }
        log.info("mergePayFund end,contractId is:{}",contractId);
    }

    private String generateCompositeKey(ContractIncomeFundE fund) {
        return fund.getChargeItemId() + "|" +
                nullToEmpty(fund.getType()) + "|" +
                nullToEmpty(fund.getPayWayId()) + "|" +
                nullToEmpty(fund.getTaxRateId()) + "|" +
                nullToEmpty(fund.getPayTypeId()) + "|" +
                (fund.getStandAmount() != null ? fund.getStandAmount().toPlainString() : "0") + "|" +
                nullToEmpty(fund.getStandardId())+ "|" +
                nullToEmpty(fund.getChargeMethodId());
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    public void contractInfoToFxm(String id){
        ContractIncomeConcludeE concludeE = contractIncomeConcludeMapper.selectById(id);
        if(null != concludeE && concludeE.getSealType() != null && concludeE.getSealType() == 1) {
            LambdaQueryWrapper<ContractIncomeConcludeExpandE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractIncomeConcludeExpandE::getContractId, id)
                    .eq(ContractIncomeConcludeExpandE::getDeleted,0);
            ContractIncomeConcludeExpandE contractRecordInfoE = contractIncomeConcludeExpandMapper.selectOne(payQueryWrapper);
            if(ObjectUtils.isEmpty(contractRecordInfoE) || StringUtils.isEmpty(contractRecordInfoE.getConincrementype())){
                return;
            }
            if( !(contractRecordInfoE.getConincrementype().equals("0") || contractRecordInfoE.getConincrementype().equals("3") || contractRecordInfoE.getConincrementype().equals("5")) ){
                return;
            }
            ContractInfoToSpaceResourceF contractInfoToSpaceResourceF = contractInfoToFxmCommonService.contractInfoToFxm(concludeE,contractRecordInfoE);
            String requestBody = JSON.toJSONString(contractInfoToSpaceResourceF);
            if (StringUtils.isNotEmpty(requestBody)) {
                ContractBasePullF contractBasePullF = new ContractBasePullF();
                contractBasePullF.setRequestBody(requestBody);
                contractBasePullF.setType(0);
                Boolean isSuccess = externalFeignClient.contractInfoPull(contractBasePullF);
                log.info("新建收入合同同步到枫行梦是否成功contractId:{}, isSuccess:{}", id, isSuccess);
            }
        }
    }

    public void contractInfoToFxmExcludeFormId(String id){
        ContractIncomeConcludeE concludeE = contractIncomeConcludeMapper.selectById(id);
        if(null != concludeE && concludeE.getSealType() != null && concludeE.getSealType() == 1) {
            LambdaQueryWrapper<ContractIncomeConcludeExpandE> payQueryWrapper = new LambdaQueryWrapper<>();
            payQueryWrapper.eq(ContractIncomeConcludeExpandE::getContractId, id)
                    .eq(ContractIncomeConcludeExpandE::getDeleted,0);
            ContractIncomeConcludeExpandE contractRecordInfoE = contractIncomeConcludeExpandMapper.selectOne(payQueryWrapper);
            if(ObjectUtils.isEmpty(contractRecordInfoE) || StringUtils.isEmpty(contractRecordInfoE.getConincrementype())){
                return;
            }
            if( !(contractRecordInfoE.getConincrementype().equals("0") || contractRecordInfoE.getConincrementype().equals("3") || contractRecordInfoE.getConincrementype().equals("5")) ){
                return;
            }
            concludeE.setFromid(null);
            ContractInfoToSpaceResourceF contractInfoToSpaceResourceF = contractInfoToFxmCommonService.contractInfoToFxm(concludeE, contractRecordInfoE);
            String requestBody = JSON.toJSONString(contractInfoToSpaceResourceF);
            if (StringUtils.isNotEmpty(requestBody)) {
                ContractBasePullF contractBasePullF = new ContractBasePullF();
                contractBasePullF.setRequestBody(requestBody);
                contractBasePullF.setType(0);
                Boolean isSuccess = externalFeignClient.contractInfoPull(contractBasePullF);
                log.info("新建收入合同同步到枫行梦是否成功contractId:{}, isSuccess:{}", id, isSuccess);
            }
        }
    }
}
