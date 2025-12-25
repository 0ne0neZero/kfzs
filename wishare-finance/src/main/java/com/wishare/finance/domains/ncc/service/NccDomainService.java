package com.wishare.finance.domains.ncc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryBodyAccountListF;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.service.StatutoryBodyAccountDomainService;
import com.wishare.finance.domains.configure.taxrate.entity.NccTaxRate;
import com.wishare.finance.domains.configure.taxrate.service.NccTaxRateDomainService;
import com.wishare.finance.domains.ncc.repository.NcTaskRepository;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccPersonE;
import com.wishare.finance.domains.voucher.facade.OldVoucherFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccPersonRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddPersonF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.NccPersonJobF;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.Result;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/17 14:51
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NccDomainService {

    private final NcTaskRepository ncTaskRepository;

    private final OldVoucherFacade oldVoucherFacade;

    private final AccountOrgFacade accountOrgFacade;

    private final StatutoryBodyAccountDomainService statutoryBodyAccountDomainService;

    private final NccTaxRateDomainService nccTaxRateDomainService;

    private final YyNccPersonRepository yyNccPersonRepository;

    private final ExternalClient externalClient;

    public List<?> getSupValues(String name, String code, String tenantId) {
//        return ncTaskRepository
        switch (code) {
            case "0001": // 部门
                return ncTaskRepository.getOrgFinanceByName(name);
            case "0010": // 项目
                return ncTaskRepository.pageSpaceCommunityByName(name);
            case "0004": // 客商
                return accountOrgFacade.listMerchantByName(name);
            case "0063":  // 业务类型
                return Collections.emptyList();
            case "0011":  // 银行账户
                return getBanKAccount(name, tenantId);
            case "0065":  // 存款账户性质
                return Collections.emptyList();
            case "0054":  // 坏账准备增减方式
                return Collections.emptyList();
            case "0066":  // 增值税率
                List<NccTaxRate> list = nccTaxRateDomainService.listRate();
                if (StringUtils.isNotBlank(name)) {
                    list = list.stream().filter(item -> item.getName().contains(name)).collect(
                        Collectors.toList());
                }
                return list;
            default:
                return Collections.emptyList();
        }
    }

    private List<?> getBanKAccount(String name, String tenantId) {

        StatutoryBodyAccountListF statutoryBodyAccountListF = new StatutoryBodyAccountListF();
        statutoryBodyAccountListF.setName(name);
        List<StatutoryBodyAccountE> list1 = statutoryBodyAccountDomainService.queryList(
            statutoryBodyAccountListF, tenantId);
        List<JSONObject> list = new ArrayList<>();
        list1.forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", item.getBankAccount());
            jsonObject.put("name", item.getName());
            list.add(jsonObject);
        });
        return list;
    }

    /**
     * 新增ncc人员
     * @param addPersonF
     */
    public void addNccPerson(AddPersonF addPersonF) {
        List<YyNccPersonE> personList = yyNccPersonRepository.list(new LambdaUpdateWrapper<YyNccPersonE>()
                .eq(YyNccPersonE::getCode, addPersonF.getCode()));
        if (CollectionUtils.isEmpty(personList)) {
            Result result = externalClient.addPerson(addPersonF);
            boolean personExist = (StringUtils.contains(result.getMsg(), "下列字段值已存在，不允许重复，请检查") && StringUtils.contains(result.getMsg(), "人员编码"))
                    || (StringUtils.contains(result.getMsg(), "人员数据已被引用，不允许删除兼职"));
            if ("002".equals(result. getCode())
                    && !personExist) {
                throw BizException.throw300("新增ncc人员失败：" + result.getMsg());
            }
            YyNccPersonE yyNccPersonE = new YyNccPersonE();
            yyNccPersonE.setCode(addPersonF.getCode());
            yyNccPersonE.setName(addPersonF.getName());
            yyNccPersonE.setBusinessUnitCode(addPersonF.getPkOrg());
            List<NccPersonJobF> psnjobs = addPersonF.getPsnjob();
            NccPersonJobF nccPersonJobF = psnjobs.get(0);
            yyNccPersonE.setDeptCode(nccPersonJobF.getPkDept());
            yyNccPersonRepository.save(yyNccPersonE);
        }
    }

}
