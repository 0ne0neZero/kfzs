package com.wishare.finance.domains.configure.businessunit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountBankDto;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountDto;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.DeletedBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.UpdateBusinessUnitCommand;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务单元service
 *
 * @author
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessUnitDomainService {

    private final BusinessUnitRepository businessUnitRepository;

    /**
     * 分页查询业务单元信息树
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public Page<BusinessUnitE> queryByTreePage(PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        QueryWrapper<BusinessUnitE> queryWrapper = queryBusinessUnitPageF.getConditions().getQueryModel();
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        Page<BusinessUnitE> page = new Page<>(queryBusinessUnitPageF.getPageNum(), queryBusinessUnitPageF.getPageSize());
        Page<BusinessUnitE> chargeItemDoPage = businessUnitRepository.queryBusinessUnitByPage(page, queryWrapper);
        List<BusinessUnitE> recordList = chargeItemDoPage.getRecords();
        //获取子业务单元
        List<Long> idList = recordList.stream().map(BusinessUnitE::getId).collect(Collectors.toList());
        List<BusinessUnitE> businessUnitEList = new ArrayList<>(recordList);
        if (!CollectionUtils.isEmpty(idList)) {
            QueryWrapper<BusinessUnitE> queryModel = queryBusinessUnitPageF.getConditions().getQueryModel();
            queryModel.notIn("bu.id",idList);
            queryWrapper.eq("bu.deleted", DataDeletedEnum.NORMAL.getCode());
            queryWrapper.orderByDesc("bu.id");
            businessUnitEList.addAll(businessUnitRepository.getBusinessUnitWithRateByPath(idList,queryModel));
        }
        chargeItemDoPage.setRecords(businessUnitEList);
        return chargeItemDoPage;
    }

    /**
     * 启用或禁用业务单元
     *
     * @param command command
     * @return Boolean
     */
    public Boolean enableOrDisabled(UpdateBusinessUnitCommand command) {
        BusinessUnitE newBusinessUnitE = Global.mapperFacade.map(command, BusinessUnitE.class);
        enableOrDisabledBusinessUnit(newBusinessUnitE);
        return true;
    }

    /**
     * 启用或禁用业务单元
     *
     * @param businessUnitE businessUnitE
     */
    private void enableOrDisabledBusinessUnit(BusinessUnitE businessUnitE) {
        Long currentId = businessUnitE.getId();
        String tenantId = businessUnitE.getTenantId();
        Integer disabled = businessUnitE.getDisabled();
        List<Long> queryIdList = Collections.singletonList(currentId);

        LambdaUpdateWrapper<BusinessUnitE> updateWrapper = new LambdaUpdateWrapper<>();
        if (disabled == DataDisabledEnum.禁用.getCode()) {
            updateWrapper.set(BusinessUnitE::getDisabled, DataDisabledEnum.禁用.getCode());
        } else if (disabled == DataDisabledEnum.启用.getCode()) {
            updateWrapper.set(BusinessUnitE::getDisabled, DataDisabledEnum.启用.getCode());
        }
        updateWrapper.eq(BusinessUnitE::getId, currentId);
        updateWrapper.set(BusinessUnitE::getOperator, businessUnitE.getOperator());
        updateWrapper.set(BusinessUnitE::getOperatorName, businessUnitE.getOperatorName());
        updateWrapper.set(BusinessUnitE::getGmtModify, businessUnitE.getGmtModify());
        businessUnitRepository.update(updateWrapper);
    }

    /**
     * 删除业务单元
     *
     * @param command command
     * @return Boolean
     */
    public Boolean delete(DeletedBusinessUnitCommand command) {
        Long currentId = command.getId();
        List<Long> queryIdList = Collections.singletonList(currentId);
        return businessUnitRepository.removeById(currentId);
    }

    /**
     * 查询业务单元树信息
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public List<BusinessUnitE> queryByTree(SearchF<BusinessUnitE> queryBusinessUnitPageF) {
        QueryWrapper<BusinessUnitE> queryWrapper = queryBusinessUnitPageF.getQueryModel();
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        List<BusinessUnitE> chargeItemDoPage = businessUnitRepository.queryBusinessUnitByTree(queryWrapper);
        return chargeItemDoPage;
    }

    /**
     * 分页查询业务单元信息list
     *
     * @param queryBusinessUnitPageF queryBusinessUnitPageF
     * @return PageV
     */
    public Page<BusinessUnitE> queryByListPage(PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        QueryWrapper<BusinessUnitE> queryWrapper = queryBusinessUnitPageF.getConditions().getQueryModel();
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        Page<BusinessUnitE> page = new Page<>(queryBusinessUnitPageF.getPageNum(), queryBusinessUnitPageF.getPageSize());
        Page<BusinessUnitE> chargeItemDoPage = businessUnitRepository.queryBusinessUnitByListPage(page, queryWrapper);
        return chargeItemDoPage;
    }

    public List<BusinessUnitE> getByWrapper(Wrapper wrapper){
        return businessUnitRepository.list(wrapper);
    }

    public BusinessUnitE getOne(Wrapper wrapper){
        return businessUnitRepository.getOne(wrapper);
    }
    public List<BusinessUnitE> getList(Wrapper wrapper){
        return businessUnitRepository.list(wrapper);
    }
    public Boolean update(BusinessUnitE businessUnitE){
        return businessUnitRepository.updateById(businessUnitE);
    }
    public Boolean create(BusinessUnitE businessUnitE){
        return businessUnitRepository.save(businessUnitE);
    }

    public BusinessUnitE queryByDetailId(Long id){
        LambdaUpdateWrapper<BusinessUnitE> businessUnitELambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        businessUnitELambdaUpdateWrapper.eq(BusinessUnitE::getId,id);
        businessUnitELambdaUpdateWrapper.eq(BusinessUnitE::getDeleted,DataDeletedEnum.NORMAL.getCode());
        return businessUnitRepository.getOne(businessUnitELambdaUpdateWrapper);
    }

    public BusinessUnitE geByCode(String businessUnitCode) {
        return businessUnitRepository.getByCode(businessUnitCode);
    }

    public List<BusinessUnitE> getByCodes(List<String> businessUnitCodes) {
        return businessUnitRepository.getByCodes(businessUnitCodes);
    }

    public BusinessUnitE getById(Long id) {
        return businessUnitRepository.getById(id);
    }

    /**
     * 根据法定单位id查询业务单元
     *
     * @param id 关联id
     * @return {@link List}<>{@link BusinessUnitAccountDto}</>
     */
    public List<BusinessUnitAccountDto> queryBusinessUnitWithByStatutoryBodysId(Long id) {
        return businessUnitRepository.queryBusinessUnitWithByStatutoryBodysId(id);
    }

    public List<BusinessUnitAccountBankDto> queryBusinessUnitAccount(Long id) {
        return businessUnitRepository.queryBusinessUnitAccount(id);
    }
}
