package com.wishare.contract.domains.service.revision.income.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.mapper.revision.income.fund.ContractIncomeFundMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundListV;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundPageF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundSaveF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundListF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
/**
 * <p>
 * 收入合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeFundService extends ServiceImpl<ContractIncomeFundMapper, ContractIncomeFundE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeFundMapper contractIncomeFundMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeService contractIncomeConcludeService;



    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeFundV> get(ContractIncomeFundF conditions){
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractIncomeFundE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeFundE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractIncomeFundE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractIncomeFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractIncomeFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractIncomeFundE contractIncomeFundE = contractIncomeFundMapper.selectOne(queryWrapper);
        if (contractIncomeFundE != null) {
            return Optional.of(Global.mapperFacade.map(contractIncomeFundE, ContractIncomeFundV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public ContractIncomeFundListV list(ContractIncomeFundListF conditions){
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeFundE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractIncomeFundE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractIncomeFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractIncomeFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractIncomeFundE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractIncomeFundE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractIncomeFundV> retVList = Global.mapperFacade.mapAsList(contractIncomeFundMapper.selectList(queryWrapper),ContractIncomeFundV.class);
        ContractIncomeFundListV retV = new ContractIncomeFundListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public ContractIncomeFundE save(ContractIncomeFundSaveF contractIncomeFundF){
        ContractIncomeFundE map = Global.mapperFacade.map(contractIncomeFundF, ContractIncomeFundE.class);

        if (StringUtils.isBlank(map.getContractId())) {
            throw new OwlBizException("关联合同ID不可为空");
        }

        map.setTenantId(tenantId());

        //-- 数据字典项字段匹配赋值
        if (StringUtils.isNotBlank(map.getTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同清单.getCode(), map.getTypeId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setType(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getPayTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项付费类型.getCode(), map.getPayTypeId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayType(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getPayWayId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项付费方式.getCode(), map.getPayWayId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayWay(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getTaxRateId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), contractIncomeFundF.getTaxRateId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setTaxRate(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getStandardId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项收费单位.getCode(), contractIncomeFundF.getStandardId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setStandard(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getChargeMethodId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收费方式.getCode(), contractIncomeFundF.getChargeMethodId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setChargeMethodName(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeGetById(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
                map.setChargeItem(v.getName());
            }, () -> {throw new OwlBizException("根据费项ID检索数据失败");});
        }

        //-- 标准金额字段默认赋值为0
        if (Objects.isNull(map.getStandAmount())) {
            map.setStandAmount(BigDecimal.ZERO);
        }

        contractIncomeFundMapper.insert(map);
        return map;
    }


   /**
    * 根据Id更新
    *
    * @param contractIncomeFundF 根据Id更新
    */
    public ContractIncomeFundE update(ContractIncomeFundUpdateF contractIncomeFundF){


        ContractIncomeFundE map = Global.mapperFacade.map(contractIncomeFundF, ContractIncomeFundE.class);

        //-- 数据字典项字段匹配赋值
        if (StringUtils.isNotBlank(map.getTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同清单.getCode(), map.getTypeId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setType(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getPayTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项付费类型.getCode(), map.getPayTypeId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayType(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getPayWayId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项付费方式.getCode(), map.getPayWayId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayWay(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getTaxRateId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), contractIncomeFundF.getTaxRateId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setTaxRate(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getStandardId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项收费单位.getCode(), contractIncomeFundF.getStandardId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setStandard(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeGetById(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
                map.setChargeItem(v.getName());
            }, () -> {throw new OwlBizException("根据费项ID检索数据失败");});
        }

        contractIncomeFundMapper.updateById(map);
        return map;
    }

    /**
     * 批处理新增&编辑&删除数据
     * @param list 参数集合
     * @return true
     */
    public Boolean dealBatch(List<ContractIncomeFundUpdateF> list, Boolean isCheckFundAmount) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }

        String contractId = "";

        for (ContractIncomeFundUpdateF record : list) {
            if (StringUtils.isNotBlank(record.getContractId())) {
                contractId = record.getContractId();
            }
            if ("add".equals(record.getActionCode())) {
                save(Global.mapperFacade.map(record, ContractIncomeFundSaveF.class));
            }
            if ("edit".equals(record.getActionCode())) {
                record.setContractId(null);
                update(record);
            }
            if ("delete".equals(record.getActionCode())) {
                removeById(record.getId());
            }
        }

        judgeContractAmountAfterDeal(contractId, isCheckFundAmount);

        return true;
    }

    public Boolean dealBatchModify(List<ContractIncomeFundUpdateF> list, Boolean isCheckFundAmount) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        String contractId = "";
        for (ContractIncomeFundUpdateF record : list) {
            if (StringUtils.isNotBlank(record.getContractId())) {
                contractId = record.getContractId();
            }
            if ("add".equals(record.getActionCode())) {
                save(Global.mapperFacade.map(record, ContractIncomeFundSaveF.class));
            }
            if ("edit".equals(record.getActionCode())) {
                record.setId(null);
                save(Global.mapperFacade.map(record, ContractIncomeFundSaveF.class));
            }
        }
        judgeContractAmountAfterDeal(contractId, isCheckFundAmount);

        return true;
    }


    /**
     * 处理款项数据后校验&计算合同关联金额字段
     * @param contractId 合同ID
     */
    public void judgeContractAmountAfterDeal(String contractId, Boolean isCheckFundAmount) {

        ContractIncomeConcludeE concludeE = contractIncomeConcludeService.getById(contractId);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("检索合同数据失败");
        }

        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId())
                .eq(ContractIncomeFundE.CONTRACT_ID, contractId);
        List<ContractIncomeFundE> list = list(queryWrapper);

        //-- 金额
        BigDecimal amount = BigDecimal.ZERO;
        //-- 税额
        BigDecimal rateAmount = BigDecimal.ZERO;
        //-- 不含税金额
        BigDecimal amountOutRate = BigDecimal.ZERO;

        for (ContractIncomeFundE record : list) {
            if (Objects.isNull(record.getAmount()) || StringUtils.isBlank(record.getTaxRate()) || Objects.isNull(record.getTaxRateAmount()) || Objects.isNull(record.getAmountWithOutRate())) {
                throw new OwlBizException("存在金额 或 税率 信息为空或错误的数据， 请重新编辑再提交");
            }

            amount = amount.add(record.getAmount());
            rateAmount = rateAmount.add(record.getTaxRateAmount());
            amountOutRate = amountOutRate.add(record.getAmountWithOutRate());
        }

        if (isCheckFundAmount && amount.compareTo(concludeE.getContractAmountOriginalRate()) != 0) {
            throw new OwlBizException("合同清单项累加金额须与[补充]合同金额(原币-含税)一致，请检查！");
        }

    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractIncomeFundMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeFundV> page(PageF<ContractIncomeFundPageF> request) {
        ContractIncomeFundPageF conditions = request.getConditions();
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeFundE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractIncomeFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractIncomeFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractIncomeFundE.AMOUNT, conditions.getAmount());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractIncomeFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractIncomeFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractIncomeFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractIncomeFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractIncomeFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractIncomeFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractIncomeFundE.GMT_CREATE);
        }
        Page<ContractIncomeFundE> page = contractIncomeFundMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeFundV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeFundV> frontPage(PageF<SearchF<ContractIncomeFundE>> request) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractIncomeFundE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.orderByDesc(ContractIncomeFundE.GMT_CREATE)
                .eq(ContractIncomeFundE.TENANT_ID, tenantId());

        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractIncomeFundE> page = contractIncomeFundMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeFundV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractIncomeFundE中仅包含a字段的值
    *
    * @param fields ContractIncomeFundE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeFundE selectOneBy(Consumer<QueryWrapper<ContractIncomeFundE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeFundMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeFundE中id字段的值, select 指定字段
    *
    * @param fields ContractIncomeFundE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeFundE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeFundMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeFundE>> consumer) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeFundMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractIncomeFundE中仅包含a字段的值
     *
     * @param fields ContractIncomeFundE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeFundE> selectListBy(Consumer<QueryWrapper<ContractIncomeFundE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeFundMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractIncomeFundE中仅包含a字段的值
    *
    * @param fields ContractIncomeFundE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeFundE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeFundE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeFundMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeFundE中id字段的值, select 指定字段
     *
     * @param fields ContractIncomeFundE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeFundE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeFundE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeFundE> queryPage = contractIncomeFundMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    //根据收入合同ID获取清单费项数据
    public List<FunChargeItemF> getFundChargeItemById(String id) {
        return contractIncomeFundMapper.getFundChargeItemById(id);
    }

}
