package com.wishare.finance.apps.service.configure.organization;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.organization.fo.*;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountSimpleV;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountV;
import com.wishare.finance.domains.configure.organization.command.AddStatutoryBodyAccountCommand;
import com.wishare.finance.domains.configure.organization.command.UpdateStatutoryBodyAccountCommand;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.service.StatutoryBodyAccountDomainService;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Service
public class StatutoryBodyAccountAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private StatutoryBodyAccountDomainService statutoryBodyAccountDomainService;
    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    /**
     * 新增银行账户
     *
     * @param form
     * @return
     */
    public Long addStatutoryBodyAccount(AddStatutoryBodyAccountF form) {
        OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(form.getStatutoryBodyId());
        if (orgFinanceRv == null) {
            throw BizException.throw400(ErrMsgEnum.STATUTORY_BODY_NO_EXISTS.getErrMsg());
        }
        form.setStatutoryBodyName(orgFinanceRv.getNameCn());
        AddStatutoryBodyAccountCommand command = form.getAddStatutoryBodyAccountCommand(curIdentityInfo());
        return statutoryBodyAccountDomainService.addStatutoryBodyAccount(command);
    }

    /**
     * 修改银行账户
     *
     * @param form
     * @return
     */
    public Long updateStatutoryBodyAccount(UpdateStatutoryBodyAccountF form) {
        UpdateStatutoryBodyAccountCommand command = form.getUpdateStatutoryBodyAccountCommand(curIdentityInfo());
        return statutoryBodyAccountDomainService.updateStatutoryBodyAccount(command);
    }

    /**
     * 删除银行账户
     *
     * @param id
     * @return
     */
    public Boolean deleteStatutoryBodyAccount(Long id) {
        //todo 若银行账户已和实际的账单相关联则不可删除(调用账单接口校检)
        return statutoryBodyAccountDomainService.deleteStatutoryBodyAccount(id);
    }

    /**
     * 启用禁用银行账户
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        return statutoryBodyAccountDomainService.enableOrDisable(id, disableState);
    }

    /**
     * 分页获取银行账户列表
     *
     * @param form
     * @return
     */
    public PageV<StatutoryBodyAccountV> queryPage(PageF<? extends SearchF<?>> form) {
        Page<StatutoryBodyAccountE> statutoryBodyPage = statutoryBodyAccountDomainService.queryPage(form, getTenantId().get());
        return PageV.of(form, statutoryBodyPage.getTotal(), Global.mapperFacade.mapAsList(statutoryBodyPage.getRecords(), StatutoryBodyAccountV.class));
    }

    /**
     * 分页获取银行账户列表（用于成本中心关联）
     * @param form
     * @return
     */
    public PageV<StatutoryBodyAccountSimpleV> queryPageForBind(PageF<SearchF<?>> form) {
        Page<StatutoryBodyAccountE> statutoryBodyPage = statutoryBodyAccountDomainService.queryPage(form, getTenantId().get());
        return PageV.of(form, statutoryBodyPage.getTotal(), Global.mapperFacade.mapAsList(statutoryBodyPage.getRecords(),StatutoryBodyAccountSimpleV.class));
    }

    /**
     * 根据条件查询银行账户列表
     *
     * @param form
     * @return
     */
    public List<StatutoryBodyAccountV> queryList(StatutoryBodyAccountListF form) {
        List<StatutoryBodyAccountE> statutoryBodyList = statutoryBodyAccountDomainService.queryList(form, getTenantId().get());
        return Global.mapperFacade.mapAsList(statutoryBodyList, StatutoryBodyAccountV.class);
    }

    /**
     * 根据id获取银行账户详情
     *
     * @param id
     * @return
     */
    public StatutoryBodyAccountV detailStatutoryBodyAccount(Long id) {
        StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountDomainService.detailStatutoryBodyAccount(id);
        return Global.mapperFacade.map(statutoryBodyAccountE, StatutoryBodyAccountV.class);
    }

    /**
     * 导入银行账户
     *
     * @param file
     * @return
     */
    public Boolean importStatutoryBodyAccount(MultipartFile file) {
        List<?> list = ExcelUtil.importExcel(file, ImportStatutoryBodyAccountYyF.class);
        return statutoryBodyAccountDomainService.importStatutoryBodyAccount(Global.mapperFacade.mapAsList(list, ImportStatutoryBodyAccountYyF.class));
    }

    /**
     * 根据成本中心id获取关联银行账户
     * @param costOrdId 成本中心id
     * @return
     */
    public List<BankAccountCostOrgDto> queryCostCenterRelation(Long costOrdId) {
        return statutoryBodyAccountDomainService.queryCostCenterRelation(costOrdId, getTenantId().get());
    }

    /**
     * 编辑成本中心关联关系
     * @param bankAccountCostOrgF
     * @return
     */
    public Boolean editCostCenterRelation(BankAccountCostOrgF bankAccountCostOrgF) {
        return statutoryBodyAccountDomainService.editCostCenterRelation(bankAccountCostOrgF);
    }
}
