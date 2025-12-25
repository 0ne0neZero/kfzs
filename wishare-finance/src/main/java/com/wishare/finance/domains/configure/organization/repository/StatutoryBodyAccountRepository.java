package com.wishare.finance.domains.configure.organization.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountListF;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountPageF;
import com.wishare.finance.domains.configure.organization.consts.enums.StatutoryBodyAccountTypeEnum;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.mapper.StatutoryBodyAccountMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.List;

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
public class StatutoryBodyAccountRepository extends ServiceImpl<StatutoryBodyAccountMapper, StatutoryBodyAccountE> {

    @Autowired
    private StatutoryBodyAccountMapper statutoryBodyAccountMapper;

    /**
     * 新增银行账账户
     *
     * @param statutoryBodyAccountE
     * @return
     */
    public Long addStatutoryBodyAccount(StatutoryBodyAccountE statutoryBodyAccountE) {
        //校检一个法定单位下银行账户名称不能重复
//        checkStatutoryBodyAccountName(statutoryBodyAccountE.getStatutoryBodyId(), statutoryBodyAccountE.getName());
        //如果新增为基本户,则校检该法定单位下是否已存在基本户
        if (statutoryBodyAccountE.getType() == StatutoryBodyAccountTypeEnum.基本账户.getCode()) {
            checkBasicAccount(statutoryBodyAccountE.getStatutoryBodyId());
        }
        statutoryBodyAccountMapper.insert(statutoryBodyAccountE);
        return statutoryBodyAccountE.getId();
    }

    /**
     * 新增银行账账户
     *
     * @param statutoryBodyAccountEList
     * @return
     */
    @Transactional
    public Boolean batchAddStatutoryBodyAccount(List<StatutoryBodyAccountE> statutoryBodyAccountEList) {
        statutoryBodyAccountEList.forEach(statutoryBodyAccountE -> {
            //校检一个法定单位下银行账户名称不能重复
            checkStatutoryBodyAccountName(statutoryBodyAccountE.getStatutoryBodyId(), statutoryBodyAccountE.getName());
            //如果新增为基本户,则校检该法定单位下是否已存在基本户
            if (statutoryBodyAccountE.getType() == StatutoryBodyAccountTypeEnum.基本账户.getCode()) {
                checkBasicAccount(statutoryBodyAccountE.getStatutoryBodyId());
            }
        });
        statutoryBodyAccountMapper.insertBatch(statutoryBodyAccountEList);
        return true;
    }

    /**
     * 修改银行账户
     *
     * @param statutoryBodyAccountE
     * @return
     */
    public Long updateStatutoryBodyAccount(StatutoryBodyAccountE statutoryBodyAccountE) {
        StatutoryBodyAccountE entity = statutoryBodyAccountMapper.selectById(statutoryBodyAccountE.getId());
        if (null == entity) {
            throw BizException.throw400(ErrMsgEnum.STATUTORY_BODY_ACCOUNT_NO_EXISTS.getErrMsg());
        }
//        if (StringUtils.isNotBlank(statutoryBodyAccountE.getName()) && !entity.getName().equals(statutoryBodyAccountE.getName())) {
//            checkStatutoryBodyAccountName(entity.getStatutoryBodyId(), statutoryBodyAccountE.getName());
//        }
        if (entity.getType() != statutoryBodyAccountE.getType() && statutoryBodyAccountE.getType() == StatutoryBodyAccountTypeEnum.基本账户.getCode()) {
            checkBasicAccount(statutoryBodyAccountE.getStatutoryBodyId());
        }
        statutoryBodyAccountMapper.updateById(statutoryBodyAccountE);
        return statutoryBodyAccountE.getId();
    }

    /**
     * 分页获取银行账户列表
     *
     * @param form
     * @param tenantId
     * @return
     */
    public Page<StatutoryBodyAccountE> queryPage(PageF<? extends SearchF<?>> form, String tenantId) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        queryModel.eq("tenant_id", tenantId);
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        return statutoryBodyAccountMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 根据条件查询银行账户列表
     *
     * @param form
     * @param tenantId
     * @return
     */
    public List<StatutoryBodyAccountE> queryList(StatutoryBodyAccountListF form, String tenantId) {
        LambdaQueryWrapper<StatutoryBodyAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != form.getId(), StatutoryBodyAccountE::getId, form.getId());
        wrapper.in(CollectionUtils.isNotEmpty(form.getIds()),StatutoryBodyAccountE::getId,form.getIds());
        wrapper.eq(null != form.getStatutoryBodyId(), StatutoryBodyAccountE::getStatutoryBodyId, form.getStatutoryBodyId());
        wrapper.like(StringUtils.isNotBlank(form.getName()), StatutoryBodyAccountE::getName, form.getName());
        wrapper.eq(null != form.getDisabled(), StatutoryBodyAccountE::getDisabled, form.getDisabled());
        wrapper.eq(StatutoryBodyAccountE::getTenantId, tenantId);
        return statutoryBodyAccountMapper.selectList(wrapper);
    }

    /**
     * 校检一个法定单位下银行账户名称不能重复
     *
     * @param statutoryBodyId
     * @param name
     */
    private void checkStatutoryBodyAccountName(Long statutoryBodyId, String name) {
        LambdaQueryWrapper<StatutoryBodyAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatutoryBodyAccountE::getStatutoryBodyId, statutoryBodyId);
        wrapper.eq(StatutoryBodyAccountE::getName, name);
        wrapper.eq(StatutoryBodyAccountE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountMapper.selectOne(wrapper);
        if (null != statutoryBodyAccountE) {
            throw BizException.throw400(ErrMsgEnum.STATUTORY_BODY_ACCOUNT_NAME_EXISTS.getErrMsg());
        }
    }

    /**
     * 校检该法定单位下是否已存在基本户
     *
     * @param statutoryBodyId
     */
    private void checkBasicAccount(Long statutoryBodyId) {
        LambdaQueryWrapper<StatutoryBodyAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatutoryBodyAccountE::getStatutoryBodyId, statutoryBodyId);
        wrapper.eq(StatutoryBodyAccountE::getType, StatutoryBodyAccountTypeEnum.基本账户.getCode());
        wrapper.eq(StatutoryBodyAccountE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountMapper.selectOne(wrapper);
        if (null != statutoryBodyAccountE) {
            throw BizException.throw400(
                ErrMsgEnum.STATUTORY_BODY_ACCOUNT_BASIC_TYPE_EXISTS.getErrMsg());
        }
    }


    /**
     * 获取数据 不过滤租户
     *
     * @param page
     */
    public PageV<StatutoryBodyAccountE> queryPageWithoutTenantId(
        PageF<StatutoryBodyAccountE> page) {
        LambdaQueryWrapper<StatutoryBodyAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatutoryBodyAccountE::getDeleted, DataDeletedEnum.NORMAL.getCode());
        wrapper.eq(StatutoryBodyAccountE::getDisabled, DataDisabledEnum.启用.getCode());
        Page<StatutoryBodyAccountE> result = statutoryBodyAccountMapper.queryPageWithoutTenantId(
            new Page<>(page.getPageNum(), page.getPageSize()), wrapper);
        PageV<StatutoryBodyAccountE> pageV = new PageV<>();
        pageV.setPageNum(page.getPageNum());
        pageV.setPageSize(page.getPageSize());
        pageV.setTotal(result.getTotal());
        pageV.setRecords(result.getRecords());
        return pageV;
    }

    /**
     * 根据名称和编码获取银行账户信息
     * @param code 银行账户编码
     * @param name 银行账户名称
     * @return 银行账户信息
     */
    public List<StatutoryBodyAccountE> getByCodeAndName(String code, String name){
        return baseMapper.selectByCodeAndName(code, name);
    }

    /**
     * 根据开户行账号获取收款账号信息
     * @param account 开户行账号
     * @return 收款账号信息
     */
    public List<StatutoryBodyAccountE> listByAccount(String account){
        return list(new LambdaQueryWrapper<StatutoryBodyAccountE>().eq(StatutoryBodyAccountE::getBankAccount, account));
    }

    public List<StatutoryBodyAccountE> listByAccounts(List<String> account, List<Integer> recPayTypes){
        return list(new LambdaQueryWrapper<StatutoryBodyAccountE>()
                .in(StatutoryBodyAccountE::getBankAccount, account)
                .in(StatutoryBodyAccountE::getRecPayType, recPayTypes));
    }


}
