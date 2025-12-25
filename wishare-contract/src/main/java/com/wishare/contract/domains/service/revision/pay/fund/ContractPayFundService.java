package com.wishare.contract.domains.service.revision.pay.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundSaveF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.mapper.revision.pay.fund.ContractPayFundMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundListV;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundPageF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundSaveF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundUpdateF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundListF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 支出合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractPayFundService extends ServiceImpl<ContractPayFundMapper, ContractPayFundE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayFundMapper contractPayFundMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    @Lazy
    private ContractPayConcludeService contractPayConcludeService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractPayFundV> get(ContractPayFundF conditions){
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractPayFundE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayFundE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (Objects.nonNull(conditions.getTaxRate())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractPayFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractPayFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractPayFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractPayFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractPayFundE contractPayFundE = contractPayFundMapper.selectOne(queryWrapper);
        if (contractPayFundE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayFundE, ContractPayFundV.class));
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
    public ContractPayFundListV list(ContractPayFundListF conditions){
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayFundE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (Objects.nonNull(conditions.getTaxRate())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractPayFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractPayFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractPayFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractPayFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(ContractPayFundE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(ContractPayFundE.ID);
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<ContractPayFundV> retVList = Global.mapperFacade.mapAsList(contractPayFundMapper.selectList(queryWrapper),ContractPayFundV.class);
        ContractPayFundListV retV = new ContractPayFundListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(ContractPayFundSaveF contractPayFundF){
        ContractPayFundE map = Global.mapperFacade.map(contractPayFundF, ContractPayFundE.class);
        //根据数据查询该组最新扩展字段
        Integer extField = contractPayFundMapper.getExtField(map.getContractId(), map.getTypeId(), map.getTaxRateId(), map.getStandardId(), map.getStandAmount());
        if(Objects.isNull(extField)){
            extField = 0;
        }
        extField++;
        map.setExtField(extField.toString());
        map.setTenantId(tenantId());

        //-- 数据字典项字段匹配赋值
        if (StringUtils.isNotBlank(map.getTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项业务类型.getCode(), map.getTypeId());
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
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出结算周期.getCode(), map.getPayWayId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayWay(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(contractPayFundF.getTaxRateId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), contractPayFundF.getTaxRateId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setTaxRate(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(contractPayFundF.getStandardId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项收费单位.getCode(), contractPayFundF.getStandardId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setStandard(value.get(0).getName());
            }
        }
        if (Objects.nonNull(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeGetById(map.getChargeItemId())).ifPresentOrElse(v -> {
                map.setChargeItem(v.getName());
            }, () -> {throw new OwlBizException("根据费项ID检索数据失败");});
        }

        //-- 标准金额字段默认赋值为0
        if (Objects.isNull(map.getStandAmount())) {
            map.setStandAmount(BigDecimal.ZERO);
        }

        contractPayFundMapper.insert(map);
        String funId = map.getId();
        ContractPayConcludeE nkConclude = contractPayConcludeMapper.queryNKContractById(map.getContractId());
        if(Objects.nonNull(nkConclude)){
            map.setMainId(map.getId());
            map.setContractId(nkConclude.getId());
            map.setId(null);
            contractPayFundMapper.insert(map);
        }
        return funId;
    }


   /**
    * 根据Id更新
    *
    * @param contractPayFundF 根据Id更新
    */
    public void update(ContractPayFundUpdateF contractPayFundF){
        if (contractPayFundF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractPayFundE map = Global.mapperFacade.map(contractPayFundF, ContractPayFundE.class);
        //根据数据查询该组最新扩展字段
        Integer extField = contractPayFundMapper.getExtField(map.getContractId(), map.getTypeId(), map.getTaxRateId(), map.getStandardId(), map.getStandAmount());
        if(Objects.isNull(extField)){
            extField = 0;
        }
        extField++;
        map.setExtField(extField.toString());
        //-- 数据字典项字段匹配赋值
        if (StringUtils.isNotBlank(map.getTypeId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项业务类型.getCode(), map.getTypeId());
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
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.支出结算周期.getCode(), map.getPayWayId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setPayWay(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(contractPayFundF.getTaxRateId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), contractPayFundF.getTaxRateId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setTaxRate(value.get(0).getName());
            }
        }
        if (StringUtils.isNotBlank(contractPayFundF.getStandardId())) {
            List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项收费单位.getCode(), contractPayFundF.getStandardId());
            if (CollectionUtils.isNotEmpty(value)) {
                map.setStandard(value.get(0).getName());
            }
        }
        if (Objects.nonNull(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeGetById(map.getChargeItemId())).ifPresentOrElse(v -> {
                map.setChargeItem(v.getName());
            }, () -> {throw new OwlBizException("根据费项ID检索数据失败");});
        }

        contractPayFundMapper.updateById(map);
        ContractPayFundE nkFun = contractPayFundMapper.selectOne(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.MAIN_ID, map.getId())
                .eq(ContractPayFundE.DELETED, 0)
        );
        if(Objects.nonNull(nkFun)){
            ContractPayFundE nkUpFun = new ContractPayFundE();
            BeanUtils.copyProperties(map, nkUpFun);
            nkUpFun.setId(nkFun.getId());
            nkUpFun.setContractId(nkFun.getContractId());
            nkUpFun.setMainId(nkFun.getMainId());
            contractPayFundMapper.updateById(nkUpFun);
        }

    }

    /**
     * 批处理新增&编辑&删除数据
     * @param list 参数集合
     * @return true
     */
    public Boolean dealBatch(List<ContractPayFundUpdateF> list, Boolean isCheckFundAmount) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }

        String contractId = "";

        for (ContractPayFundUpdateF record : list) {
            if (StringUtils.isNotBlank(record.getContractId())) {
                contractId = record.getContractId();
            }
            if ("add".equals(record.getActionCode())) {
                save(Global.mapperFacade.map(record, ContractPayFundSaveF.class));
            }
            if ("edit".equals(record.getActionCode())) {
                update(record);
            }
            if ("delete".equals(record.getActionCode())) {
                removeById(record.getId());
            }
        }

        judgeContractAmountAfterDeal(contractId, isCheckFundAmount);

        return true;
    }

    public Boolean dealBatchModify(List<ContractPayFundUpdateF> list, Boolean isCheckFundAmount) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        String contractId = "";
        for (ContractPayFundUpdateF record : list) {
            if (StringUtils.isNotBlank(record.getContractId())) {
                contractId = record.getContractId();
            }
            if ("add".equals(record.getActionCode())) {
                save(Global.mapperFacade.map(record, ContractPayFundSaveF.class));
            }
            if ("edit".equals(record.getActionCode())) {
                record.setId(null);
                save(Global.mapperFacade.map(record, ContractPayFundSaveF.class));
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

        ContractPayConcludeE concludeE = contractPayConcludeService.getById(contractId);
        if (Objects.isNull(concludeE)) {
            throw new OwlBizException("检索合同数据失败");
        }

        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, contractId)
                .eq(ContractPayFundE.DELETED, 0);
        List<ContractPayFundE> list = list(queryWrapper);

        //-- 金额
        BigDecimal amount = BigDecimal.ZERO;
        //-- 税额
        BigDecimal rateAmount = BigDecimal.ZERO;
        //-- 不含税金额
        BigDecimal amountOutRate = BigDecimal.ZERO;

        for (ContractPayFundE record : list) {
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
        contractPayFundMapper.deleteById(id);
        ContractPayFundE nkFun = contractPayFundMapper.selectOne(new QueryWrapper<ContractPayFundE>()
                .orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.MAIN_ID, id)
                .eq(ContractPayFundE.DELETED, 0)
        );
        if(Objects.nonNull(nkFun)){
            contractPayFundMapper.deleteById(nkFun.getId());
        }
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayFundV> page(PageF<ContractPayFundPageF> request) {
        ContractPayFundPageF conditions = request.getConditions();
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getName())) {
            queryWrapper.eq(ContractPayFundE.NAME, conditions.getName());
        }

        if (StringUtils.isNotBlank(conditions.getTypeId())) {
            queryWrapper.eq(ContractPayFundE.TYPE_ID, conditions.getTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(ContractPayFundE.TYPE, conditions.getType());
        }

        if (Objects.nonNull(conditions.getAmount())) {
            queryWrapper.eq(ContractPayFundE.AMOUNT, conditions.getAmount());
        }

        if (Objects.nonNull(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayFundE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (Objects.nonNull(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (Objects.nonNull(conditions.getTaxRate())) {
            queryWrapper.eq(ContractPayFundE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getPayTypeId())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE_ID, conditions.getPayTypeId());
        }

        if (StringUtils.isNotBlank(conditions.getPayType())) {
            queryWrapper.eq(ContractPayFundE.PAY_TYPE, conditions.getPayType());
        }

        if (StringUtils.isNotBlank(conditions.getPayWayId())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY_ID, conditions.getPayWayId());
        }

        if (StringUtils.isNotBlank(conditions.getPayWay())) {
            queryWrapper.eq(ContractPayFundE.PAY_WAY, conditions.getPayWay());
        }

        if (Objects.nonNull(conditions.getStartDate())) {
            queryWrapper.gt(ContractPayFundE.START_DATE, conditions.getStartDate());
        }

        if (Objects.nonNull(conditions.getEndDate())) {
            queryWrapper.gt(ContractPayFundE.END_DATE, conditions.getEndDate());
        }

        if (StringUtils.isNotBlank(conditions.getStandardId())) {
            queryWrapper.eq(ContractPayFundE.STANDARD_ID, conditions.getStandardId());
        }

        if (StringUtils.isNotBlank(conditions.getStandard())) {
            queryWrapper.eq(ContractPayFundE.STANDARD, conditions.getStandard());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractPayFundE.REMARK, conditions.getRemark());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayFundE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayFundE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayFundE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayFundE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayFundE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayFundE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(ContractPayFundE.GMT_CREATE);
        }

        queryWrapper.eq(ContractPayFundE.TENANT_ID, tenantId());

        Page<ContractPayFundE> page = contractPayFundMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayFundV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayFundV> frontPage(PageF<SearchF<ContractPayFundE>> request) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayFundE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId());

        Page<ContractPayFundE> page = contractPayFundMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayFundV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayFundE中仅包含a字段的值
    *
    * @param fields ContractPayFundE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayFundE selectOneBy(Consumer<QueryWrapper<ContractPayFundE>> consumer,String... fields) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayFundMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayFundE中id字段的值, select 指定字段
    *
    * @param fields ContractPayFundE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayFundE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayFundMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayFundE>> consumer) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayFundMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayFundE中仅包含a字段的值
     *
     * @param fields ContractPayFundE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayFundE> selectListBy(Consumer<QueryWrapper<ContractPayFundE>> consumer,String... fields) {
         QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayFundMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayFundE中仅包含a字段的值
    *
    * @param fields ContractPayFundE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayFundE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayFundE>> consumer, String... fields) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayFundMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayFundE中id字段的值, select 指定字段
     *
     * @param fields ContractPayFundE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayFundE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayFundE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayFundE> queryPage = contractPayFundMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }

    //根据支出合同ID获取清单费项数据
    public List<FunChargeItemF> getFundChargeItemById(String id) {
        return contractPayFundMapper.getFundChargeItemById(id);
    }
}
