package com.wishare.finance.domains.configure.chargeitem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.BusinessTypeListF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.SyncF;
import com.wishare.finance.domains.configure.chargeitem.entity.AssisteAccountE;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessTypeE;
import com.wishare.finance.domains.configure.chargeitem.repository.AssisteAccountRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.BusinessTypeRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.enums.BusinessTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.AccessItemRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.BusinessTypeRv;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteAccountDomainService {

    private final AssisteAccountRepository assisteAccountRepository;

    private final BusinessTypeRepository businessTypeRepository;

    private final ExternalClient externalClient;

    /**
     * 分页获取辅助核算
     *
     * @param form
     * @return
     */
    public Page<AssisteAccountE> queryPage(PageF<SearchF<?>> form) {
        return assisteAccountRepository.queryPage(form);
    }

    /**
     * 获取辅助核算列表
     *
     * @return
     */
    public List<AssisteAccountE> assisteAccountList(AssisteAccountF form) {
        return assisteAccountRepository.assisteAccountList(form);
    }

    /**
     * 根据code获取辅助核算列表
     *
     * @return
     */
    public List<AssisteAccountE> assisteAccountListByCodes(List<String> auxiliaryCountList) {
        return assisteAccountRepository.assisteAccountListByCodes(auxiliaryCountList);
    }

    /**
     * 同步
     *
     * @param form
     * @return
     */
    public Boolean sync(SyncF form) {
        List<AccessItemRv> accessItemRvs = externalClient.listAccassitem();
        for (AccessItemRv accessItemRv : accessItemRvs) {
            AssisteAccountE assisteAccountE = generalAssisteAccountE(accessItemRv,form.getSysSource());
            QueryWrapper<AssisteAccountE> wrapper = new QueryWrapper<>();
            wrapper.eq("as_ac_code", assisteAccountE.getAsAcCode());
            assisteAccountRepository.saveOrUpdate(assisteAccountE, wrapper);
        }
        return true;
    }

    /**
     * 构建辅助核算实体
     *
     * @param accessItemRv
     * @param sysSource
     * @return
     */
    private AssisteAccountE generalAssisteAccountE(AccessItemRv accessItemRv, Integer sysSource) {
        AssisteAccountE assisteAccountE = new AssisteAccountE();
        assisteAccountE.setAsAcCode(accessItemRv.getCode());
        assisteAccountE.setAsAcItem(accessItemRv.getName());
        assisteAccountE.setAsAcTarget(accessItemRv.getObject());
        assisteAccountE.setReferenceName(accessItemRv.getRefnodename());
        assisteAccountE.setSysSource(sysSource);
        return assisteAccountE;
    }

    /**
     * 导出数据查询
     *
     * @param form
     * @return
     */
    public List<AssisteAccountE> exportList(PageF<SearchF<?>> form) {
        return assisteAccountRepository.exportList(form);
    }


    /**
     * 同步业务类型
     *
     * @param form
     * @return
     */
    public Boolean businessTypeSync(SyncF form) {
        List<BusinessTypeRv> businessTypeRvs = externalClient.listBusinessType();
        for (BusinessTypeRv businessTypeRv : businessTypeRvs) {
            BusinessTypeE businessTypeE = generalBusinessTypeE(businessTypeRv, form.getSysSource());
            QueryWrapper<BusinessTypeE> wrapper = new QueryWrapper<>();
            wrapper.eq("code", businessTypeRv.getCode());
            businessTypeRepository.saveOrUpdate(businessTypeE, wrapper);
        }
        return true;
    }

    /**
     * 构建业务类型实体
     *
     * @return
     */
    private BusinessTypeE generalBusinessTypeE(BusinessTypeRv businessTypeRv, Integer sysSource) {
        BusinessTypeE businessTypeE = new BusinessTypeE();
        businessTypeE.setCode(businessTypeRv.getCode());
        businessTypeE.setName(businessTypeRv.getName());
        businessTypeE.setFcode(businessTypeRv.getFcode());
        businessTypeE.setFname(businessTypeRv.getFname());
        businessTypeE.setPkGroup(businessTypeE.getPkGroup());
        businessTypeE.setPkOrg(businessTypeE.getPkOrg());
        businessTypeE.setSysSource(sysSource);
        businessTypeE.setStateStr(businessTypeRv.getState());
        BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.valueOfByCode(businessTypeRv.getState());
        if (businessTypeEnum != null) {
            businessTypeE.setState(businessTypeEnum.getCode());
        }
        return businessTypeE;
    }

    /**
     * 根据条件获取业务类型
     *
     * @param form
     * @return
     */
    public List<BusinessTypeE> businessTypeList(BusinessTypeListF form) {
        return businessTypeRepository.businessTypeList(form);
    }


}
