package com.wishare.finance.apps.service.configure.organization;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.organization.fo.AddStatutoryInvoiceConfF;
import com.wishare.finance.apps.model.configure.organization.fo.StatutoryInvoiceConfListF;
import com.wishare.finance.apps.model.configure.organization.fo.UpdateStatutoryInvoiceConfF;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryInvoiceConfV;
import com.wishare.finance.domains.configure.organization.command.AddStatutoryInvoiceConfCommand;
import com.wishare.finance.domains.configure.organization.command.UpdateStatutoryInvoiceConfCommand;
import com.wishare.finance.domains.configure.organization.entity.StatutoryInvoiceConfE;
import com.wishare.finance.domains.configure.organization.service.StatutoryInvoiceConfDomainService;
import com.wishare.finance.domains.invoicereceipt.entity.nuonuo.NuonuoTokenE;
import com.wishare.finance.domains.invoicereceipt.repository.NuonuoTokenRepository;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Service
public class StatutoryInvoiceConfAppService {

    @Setter(onMethod_ = {@Autowired})
    private StatutoryInvoiceConfDomainService statutoryInvoiceConfDomainService;
    @Setter(onMethod_ = {@Autowired})
    private NuonuoTokenRepository nuonuoTokenRepository;
    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    /**
     * 新增电子开票设置
     *
     * @param form
     * @return
     */
    @Transactional
    public Long addStatutoryInvoiceConf(AddStatutoryInvoiceConfF form) {
        nuonuoMethod(form.getToken(),form.getStatutoryBodyId());
        return statutoryInvoiceConfDomainService.addStatutoryInvoiceConf(Global.mapperFacade.map(form, AddStatutoryInvoiceConfCommand.class));
    }

    /**
     * 修改电子开票设置
     *
     * @param form
     * @return
     */
    @Transactional
    public Long updateStatutoryInvoiceConf(UpdateStatutoryInvoiceConfF form) {
        nuonuoMethod(form.getToken(),form.getStatutoryBodyId());
        return statutoryInvoiceConfDomainService.updateStatutoryInvoiceConf(Global.mapperFacade.map(form, UpdateStatutoryInvoiceConfCommand.class));
    }

    public void nuonuoMethod(String token,Long statutoryBodyId){
        if(StringUtils.isNotBlank(token)){
            OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(statutoryBodyId);
            OrgTenantRv orgTenantRv = orgClient.tenantGetById(orgFinanceRv.getTenantId());
            NuonuoTokenE one = nuonuoTokenRepository.getOne(new LambdaQueryWrapper<NuonuoTokenE>()
                    .eq(NuonuoTokenE::getSalerTaxNum, orgFinanceRv.getTaxpayerNo())
                    .eq(NuonuoTokenE::getTenantId,orgTenantRv.getId()));
            if(Objects.isNull(one)){
                NuonuoTokenE nuonuoTokenE=new NuonuoTokenE();
                nuonuoTokenE.setToken(token);
                nuonuoTokenE.setSalerTaxNum(orgFinanceRv.getTaxpayerNo());
                nuonuoTokenE.setTenantName(orgTenantRv.getName());
                nuonuoTokenE.setTenantId(orgTenantRv.getId());
                nuonuoTokenE.setDeleted(0);
                nuonuoTokenRepository.save(nuonuoTokenE);
            }else {
                if(!one.getToken().equals(token)){
                    one.setToken(token);
                    nuonuoTokenRepository.updateById(one);
                }
            }

        }
    }
    /**
     * 删除电子开票设置
     *
     * @param id
     * @return
     */
    public Boolean deleteStatutoryInvoiceConf(Long id) {
        return statutoryInvoiceConfDomainService.deleteStatutoryInvoiceConf(id);
    }

    /**
     * 分页查询电子开票设置
     *
     * @param form
     * @return
     */
    public PageV<StatutoryInvoiceConfV> queryPage(PageF<SearchF<?>> form) {
        Page<StatutoryInvoiceConfE> pageResult = statutoryInvoiceConfDomainService.queryPage(form);
        if (CollectionUtils.isNotEmpty(pageResult.getRecords())) {
            OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(pageResult.getRecords().get(0).getStatutoryBodyId());
            if (Objects.nonNull(orgFinanceRv)) {
                NuonuoTokenE one = nuonuoTokenRepository.getOne(new LambdaQueryWrapper<NuonuoTokenE>()
                        .eq(NuonuoTokenE::getSalerTaxNum, orgFinanceRv.getTaxpayerNo())
                        .eq(NuonuoTokenE::getTenantId,orgFinanceRv.getTenantId()));
                if (Objects.nonNull(one)) {
                    pageResult.getRecords().stream().forEach(statutoryInvoiceConfE ->
                                statutoryInvoiceConfE.setToken(one.getToken())
                            );
                }
            }
        }
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), StatutoryInvoiceConfV.class));
    }

    /**
     * 列表查询电子开票设置
     *
     * @param form
     * @return
     */
    public List<StatutoryInvoiceConfV> queryList(StatutoryInvoiceConfListF form) {
        List<StatutoryInvoiceConfE> statutoryInvoiceConfEList = statutoryInvoiceConfDomainService.queryList(form);
        return Global.mapperFacade.mapAsList(statutoryInvoiceConfEList, StatutoryInvoiceConfV.class);
    }

    public String queryGet(Long id) {
        OrgFinanceRv orgFinanceRv = orgClient.getOrgFinanceById(id);
        NuonuoTokenE one = nuonuoTokenRepository.getOne(new LambdaQueryWrapper<NuonuoTokenE>()
                .eq(NuonuoTokenE::getSalerTaxNum, orgFinanceRv.getTaxpayerNo())
                .eq(NuonuoTokenE::getTenantId, orgFinanceRv.getTenantId()));
        if (Objects.nonNull(one)) {
           return one.getToken();
        }
        return "";
    }


    /**
     * 根据法定单位查询开票人列表
     * @param statutoryBodyId
     * @return
     */
    public List<String> queryClerkList(Long statutoryBodyId) {
        StatutoryInvoiceConfListF invoiceConfListF = new StatutoryInvoiceConfListF();
        invoiceConfListF.setStatutoryBodyId(statutoryBodyId);
        List<StatutoryInvoiceConfE> statutoryInvoiceConfEList = statutoryInvoiceConfDomainService.queryList(invoiceConfListF);
        return statutoryInvoiceConfEList.stream().map(StatutoryInvoiceConfE::getClerk).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
