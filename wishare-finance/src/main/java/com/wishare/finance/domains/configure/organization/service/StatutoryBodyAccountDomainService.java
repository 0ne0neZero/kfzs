package com.wishare.finance.domains.configure.organization.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.organization.fo.BankAccountCostOrgF;
import com.wishare.finance.apps.model.configure.organization.fo.ImportStatutoryBodyAccountYyF;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountListF;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountPageF;
import com.wishare.finance.domains.configure.organization.command.AddStatutoryBodyAccountCommand;
import com.wishare.finance.domains.configure.organization.command.UpdateStatutoryBodyAccountCommand;
import com.wishare.finance.domains.configure.organization.consts.enums.RecPayTypeEnum;
import com.wishare.finance.domains.configure.organization.consts.enums.StatutoryBodyAccountTypeEnum;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.BankAccountCostOrgE;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.BankAccountCostOrgRepository;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbProjectRF;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbQueryRF;
import com.wishare.finance.infrastructure.remote.vo.external.mdmmb.MdmMbProjectRV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatutoryBodyAccountDomainService implements ApiBase {

    private final StatutoryBodyAccountRepository statutoryBodyAccountRepository;

    private final BankAccountCostOrgRepository bankAccountCostOrgRepository;

    /**
     * 新增银行账户
     *
     * @param command
     * @return
     */
    public Long addStatutoryBodyAccount(AddStatutoryBodyAccountCommand command) {
        StatutoryBodyAccountE statutoryBodyAccountE = Global.mapperFacade.map(command, StatutoryBodyAccountE.class);
        return statutoryBodyAccountRepository.addStatutoryBodyAccount(statutoryBodyAccountE);
    }

    /**
     * 修改银行账户
     *
     * @param command
     * @return
     */
    public Long updateStatutoryBodyAccount(UpdateStatutoryBodyAccountCommand command) {
        StatutoryBodyAccountE statutoryBodyAccountE = Global.mapperFacade.map(command, StatutoryBodyAccountE.class);
        return statutoryBodyAccountRepository.updateStatutoryBodyAccount(statutoryBodyAccountE);
    }

    /**
     * 删除银行账户
     *
     * @param id
     * @return
     */
    public Boolean deleteStatutoryBodyAccount(Long id) {
        return statutoryBodyAccountRepository.removeById(id);
    }

    /**
     * 启用或者禁用银行账户
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enableOrDisable(Long id, Integer disableState) {
        StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountRepository.getById(id);
        if (null == statutoryBodyAccountE) {
            throw BizException.throw400("该银行账户不存在");
        }
        statutoryBodyAccountE.setDisabled(disableState);
        return statutoryBodyAccountRepository.updateById(statutoryBodyAccountE);
    }

    /**
     * 分页获取银行账户列表
     *
     * @param form
     * @param tenantId
     * @return
     */
    public Page<StatutoryBodyAccountE> queryPage(PageF<? extends SearchF<?>> form, String tenantId) {
        return statutoryBodyAccountRepository.queryPage(form, tenantId);
    }

    /**
     * 根据条件查询银行账户列表
     *
     * @param form
     * @param tenantId
     * @return
     */
    public List<StatutoryBodyAccountE> queryList(StatutoryBodyAccountListF form, String tenantId) {
        return statutoryBodyAccountRepository.queryList(form, tenantId);
    }

    /**
     * 根据id获取银行账户详情
     *
     * @param id
     * @return
     */
    public StatutoryBodyAccountE detailStatutoryBodyAccount(Long id) {
        StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountRepository.getById(id);
        if (null == statutoryBodyAccountE) {
            throw BizException.throw400(ErrMsgEnum.STATUTORY_BODY_ACCOUNT_NO_EXISTS.getErrMsg());
        }
        return statutoryBodyAccountE;
    }

    @Autowired
    private ExternalClient externalClient;

    /**
     * 导入银行账户数据
     *
     * @param importStatutoryBodyAccountYyFS
     * @return
     */
    public Boolean importStatutoryBodyAccount(List<ImportStatutoryBodyAccountYyF> importStatutoryBodyAccountYyFS) {
        List<StatutoryBodyAccountE> statutoryBodyAccountEList = Lists.newArrayList();
        importStatutoryBodyAccountYyFS.forEach(importStatutoryBodyAccountYyF -> {
            StatutoryBodyAccountE entity = new StatutoryBodyAccountE();
            entity.setId(IdentifierFactory.getInstance().generateLongIdentifier("statutoryBodyAccountE"));
            entity.setName(importStatutoryBodyAccountYyF.getName());
            entity.setType(handleType(importStatutoryBodyAccountYyF.getTypeStr()));
            entity.setBankName(importStatutoryBodyAccountYyF.getBankName());
            entity.setBankAccount(importStatutoryBodyAccountYyF.getBankAccount());
            entity.setRecPayType(handleRecPayType(importStatutoryBodyAccountYyF.getRecPayTypeStr()));
            entity.setStatutoryBodyId(handleStatutoryBodyId(importStatutoryBodyAccountYyF.getStatutoryBodyId()));
            entity.setDisabled(DataDisabledEnum.启用.getCode());
            entity.setDeleted(DataDisabledEnum.启用.getCode());
            entity.setTenantId(getTenantId().get());
            entity.setCreator(getUserId().get());
            entity.setCreatorName(getUserName().get());
            entity.setGmtCreate(LocalDateTime.now());
            entity.setOperator(getUserId().get());
            entity.setOperatorName(getUserName().get());
            entity.setGmtModify(LocalDateTime.now());
            statutoryBodyAccountEList.add(entity);
            saveMdmMb(entity.getId(),importStatutoryBodyAccountYyF.getStatutoryBodyId());
        });
        statutoryBodyAccountRepository.batchAddStatutoryBodyAccount(statutoryBodyAccountEList);
        return true;
    }

    /**
     * 保存映射关系
     * @param statutoryBodyAccountId
     * @param statutoryBodyIdYy
     */
    private void saveMdmMb(Long statutoryBodyAccountId, String statutoryBodyIdYy) {
        MdmMbProjectRF mdmMbProjectRF = new MdmMbProjectRF();
        mdmMbProjectRF.setMdmId(statutoryBodyAccountId);
        mdmMbProjectRF.setMdmCode("statutory_body");
        mdmMbProjectRF.setBusinessId(statutoryBodyIdYy);
        mdmMbProjectRF.setRemoteSystemId(12004355279901L);
        mdmMbProjectRF.setTableName("mdm_mb_statutory_body_account");
        externalClient.mdmSave(mdmMbProjectRF);
    }



    /**
     * 获取法定单位映射
     *
     * @param statutoryBodyId
     * @return
     */
    private Long handleStatutoryBodyId(String statutoryBodyId) {
        MdmMbQueryRF mdmMbQueryRF = new MdmMbQueryRF();
        mdmMbQueryRF.setMdmCode("FDDW");
        mdmMbQueryRF.setBusinessId(statutoryBodyId);
        mdmMbQueryRF.setTableName("mdm_mb_org_finance");
        mdmMbQueryRF.setRemoteSystemId(12004355279901L);
        List<MdmMbProjectRV> mdmMbProjectRVS = externalClient.mdmQuery(mdmMbQueryRF);
        if (CollectionUtils.isNotEmpty(mdmMbProjectRVS)) {
            return mdmMbProjectRVS.get(0).getMdmId();
        }
        return null;
    }

    /**
     * 处理收款付款类型
     *
     * @param recPayTypeStr
     * @return
     */
    private Integer handleRecPayType(String recPayTypeStr) {
        if (recPayTypeStr.equals("收入")) {
            return RecPayTypeEnum.收款.getCode();
        } else if (recPayTypeStr.equals("支出")) {
            return RecPayTypeEnum.付款.getCode();
        } else if (recPayTypeStr.equals("收支")) {
            return RecPayTypeEnum.收款付款.getCode();
        }
        return null;
    }

    /**
     * 处理账户类型
     *
     * @param typeStr
     * @return
     */
    private Integer handleType(String typeStr) {
        if (typeStr.equals("基本")) {
            return StatutoryBodyAccountTypeEnum.基本账户.getCode();
        } else if (typeStr.equals("临时")) {
            return StatutoryBodyAccountTypeEnum.临时账户.getCode();
        } else if (typeStr.equals("一般")) {
            return StatutoryBodyAccountTypeEnum.一般账户.getCode();
        } else if (typeStr.equals("专用")) {
            return StatutoryBodyAccountTypeEnum.专用账户.getCode();
        }
        return null;
    }

    /**
     * 获取数据 不过滤租户
     *
     * @param page
     */
    public PageV<StatutoryBodyAccountE> queryPageWithoutTenantId(
        PageF<StatutoryBodyAccountE> page) {
        return statutoryBodyAccountRepository.queryPageWithoutTenantId(page);
    }

    /**
     * 根据开户行账号获取收款账号信息
     * @param account 开户行账号
     * @return 收款账号信息
     */
    public List<StatutoryBodyAccountE> getListByAccount(String account){
        return statutoryBodyAccountRepository.listByAccount(account);
    }

    /**
     * 根据开户行账号获取收款账号信息
     * @param accounts 开户行账号]
     * @param recPayTypes 收款付款类型
     * @return 收款账号信息
     */
    public List<StatutoryBodyAccountE> getListByAccounts(List<String> accounts, List<Integer> recPayTypes){
        return statutoryBodyAccountRepository.listByAccounts(accounts, recPayTypes);
    }

    /**
     * 根据成本中心id获取关联银行账户
     * @param costOrdId 成本中心id
     * @param tenantId 租户id
     * @return
     */
    public List<BankAccountCostOrgDto> queryCostCenterRelation(Long costOrdId, String tenantId) {
        return bankAccountCostOrgRepository.getRelation(costOrdId, tenantId);
    }

    /**
     * 编辑成本中心关联关系
     * @param bankAccountCostOrgF
     * @return
     */
    @Transactional
    public Boolean editCostCenterRelation(BankAccountCostOrgF bankAccountCostOrgF) {
        List<Long> addAccountList = bankAccountCostOrgF.getAddAccountList();
        List<Long> deleteAccountList = bankAccountCostOrgF.getDeleteAccountList();
        if (addAccountList != null && addAccountList.size() > 0) {
            List<BankAccountCostOrgE> bankAccountCostOrgES = addAccountList.stream().map(bankAccountId -> {
                BankAccountCostOrgE bankAccountCostOrgE = new BankAccountCostOrgE();
                bankAccountCostOrgE.setBankAccountId(bankAccountId);
                bankAccountCostOrgE.setCostOrgId(bankAccountCostOrgF.getCostOrgId());
                return bankAccountCostOrgE;
            }).collect(Collectors.toList());
            bankAccountCostOrgRepository.saveBatch(bankAccountCostOrgES);
        }
        if (deleteAccountList != null && deleteAccountList.size() > 0) {
            LambdaQueryWrapper<BankAccountCostOrgE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BankAccountCostOrgE::getCostOrgId, bankAccountCostOrgF.getCostOrgId());
            queryWrapper.in(BankAccountCostOrgE::getBankAccountId, bankAccountCostOrgF.getDeleteAccountList());
            bankAccountCostOrgRepository.remove(queryWrapper);
        }
        return true;
    }

}
