package com.wishare.finance.domains.configure.subject.service;

import com.wishare.finance.apps.model.configure.businessunit.fo.CreateBusinessUnitF;
import com.wishare.finance.apps.model.fangyuan.vo.BusinessUnitSyncDto;
import com.wishare.finance.apps.service.configure.businessunit.BusinessUnitAppService;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.entity.AssisteInoutclass;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrg;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrgDept;
import com.wishare.finance.domains.configure.subject.repository.AssisteBizTypeRepository;
import com.wishare.finance.domains.configure.subject.repository.AssisteInoutclassRepository;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgDeptRepository;
import com.wishare.finance.domains.configure.subject.repository.AssisteOrgRepository;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.MerchantRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 辅助核算领域服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteItemDomainService {

    private final OrgClient orgClient;

    private final TaxRateRepository taxRateRepository;
    private final AssisteOrgRepository assisteOrgRepository;
    private final AssisteBizTypeRepository assisteBizTypeRepository;
    private final AssisteOrgDeptRepository assisteOrgDeptRepository;
    private final AssisteInoutclassRepository assisteInoutclassRepository;
    private final StatutoryBodyAccountRepository statutoryBodyAccountRepository;
    private final BusinessUnitAppService businessUnitAppService;


    /**
     * 批量新增业务类型
     * @param assisteBizTypes
     * @return
     */
    public boolean syncBatchAssisteBizType(List<AssisteBizType> assisteBizTypes) {
        assisteBizTypes.forEach(AssisteBizType::updateOrInsert);
        return  assisteBizTypeRepository.saveOrUpdateBatchByCode(assisteBizTypes);
    }

    /**
     *  wishare-external（定时调用) 功能：同步业务单元
     * @param assisteOrgs
     * @return
     */
    public List<BusinessUnitSyncDto> syncBatchAssisteOrg(List<AssisteOrg> assisteOrgs) {
        // 同步 assiste_org
        assisteOrgs.forEach(AssisteOrg::updateOrInsert);
        boolean b = assisteOrgRepository.saveOrUpdateBatchByCode(assisteOrgs);
        // 同步 business_unit
        ArrayList<BusinessUnitSyncDto> vo = new ArrayList<>(assisteOrgs.size());
        assisteOrgs.forEach(f->{
            vo.add(businessUnitAppService.syncBusinessDataAddOrUpdate(CreateBusinessUnitF.builder().code(f.getCode()).name(f.getName()).build()));
        });
        return vo;

    }

    /**
     * 批量新增部门
     * @param assisteOrgDepts
     * @return
     */
    public boolean syncBatchAssisteOrgDept(List<AssisteOrgDept> assisteOrgDepts) {
        assisteOrgDepts.forEach(AssisteOrgDept::updateOrInsert);
        return assisteOrgDeptRepository.saveOrUpdateBatchByCode(assisteOrgDepts);
    }

    /**
     * 批量新增收支项目
     * @param assisteInoutclasses
     * @return
     */
    public boolean syncBatchAssisteInoutclass(List<AssisteInoutclass> assisteInoutclasses) {
        assisteInoutclasses.forEach(AssisteInoutclass::updateOrInsert);
        return assisteInoutclassRepository.saveOrUpdateBatchByCode(assisteInoutclasses);
    }

    /**
     * 获取辅助核算信息
     * @param name 名称
     * @param code 编码
     * @param type 类型
     * @return 辅助核算信息列表
     */
    public List<AssisteItemOBV> getBaseAssisteItem(String name, String code, Integer type, String sbId){
        AssisteItemTypeEnum assisteItemTypeEnum = AssisteItemTypeEnum.valueOfByCode(type);
        switch (assisteItemTypeEnum){
            case 部门:
                //获取法定单位
                OrgFinanceRv orgFinance = orgClient.getOrgFinanceById(Long.valueOf(sbId));
                return Objects.nonNull(orgFinance) ? assisteOrgDeptRepository.getAssisteItems(name, code, orgFinance.getCode()) : new ArrayList<>();
            case 业务单元:
                return assisteOrgRepository.getAssisteItems(name, code);
            case 业务类型:
                return assisteBizTypeRepository.getAssisteItems(name, code);
            case 收支项目:
                return assisteInoutclassRepository.getAssisteItems(name, code);
            case 客商:
                List<MerchantRv> merchants = orgClient.getMerchantByCodeAndName(code, name);
                return CollectionUtils.isNotEmpty(merchants) ? merchants.stream()
                        .map(item -> toAssisteItem(assisteItemTypeEnum, item.getCode(), item.getName()))
                        .collect(Collectors.toList()) : new ArrayList<>();
            case 项目:
                List<OrgFinanceCostRv> costRvs = orgClient.getOrgFinanceCostByCodeAndName(code, name);
                return CollectionUtils.isNotEmpty(costRvs) ? costRvs.stream()
                        .map(item -> toAssisteItem(assisteItemTypeEnum, item.getCostCode(), item.getNameCn()))
                        .collect(Collectors.toList()) : new ArrayList<>();
            case 人员档案:
                break;
            case 银行账户:
                List<StatutoryBodyAccountE> accounts = statutoryBodyAccountRepository.getByCodeAndName(code, name);
                return CollectionUtils.isNotEmpty(accounts) ? accounts.stream()
                        .map(item -> toAssisteItem(assisteItemTypeEnum, item.getBankAccount(), item.getName()))
                        .collect(Collectors.toList()) : new ArrayList<>();
            case 增值税税率:
                List<TaxRateE> taxRates = taxRateRepository.getByCodeAndName(code, name);
                return CollectionUtils.isNotEmpty(taxRates) ? taxRates.stream()
                        .map(item -> toAssisteItem(assisteItemTypeEnum, item.getCode(), item.getRate().toString() + "%"))
                        .collect(Collectors.toList()) : new ArrayList<>();
            case 存款账户性质:
                return List.of(toAssisteItem(assisteItemTypeEnum, "01", "活期"), toAssisteItem(assisteItemTypeEnum, "02", "定期"));
        }
        return new ArrayList<>();
    }

    private AssisteItemOBV toAssisteItem(AssisteItemTypeEnum assisteItemTypeEnum, String code, String name){
        AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
        assisteItemOBV.setType(assisteItemTypeEnum.getCode());
        assisteItemOBV.setCode(code);
        assisteItemOBV.setName(name);
        assisteItemOBV.setAscCode(assisteItemTypeEnum.getAscCode());
        assisteItemOBV.setAscName(assisteItemTypeEnum.getValue());
        return assisteItemOBV;
    }

}
