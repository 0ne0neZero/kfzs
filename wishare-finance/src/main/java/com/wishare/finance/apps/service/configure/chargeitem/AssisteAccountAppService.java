package com.wishare.finance.apps.service.configure.chargeitem;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountSyncF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.BusinessTypeListF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.SyncF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.AssisteAccountV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.BusinessTypeV;
import com.wishare.finance.domains.configure.chargeitem.entity.AssisteAccountE;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessTypeE;
import com.wishare.finance.domains.configure.chargeitem.service.AssisteAccountDomainService;
import com.wishare.finance.domains.configure.subject.command.subject.FindSubjectByIdQuery;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteAccountAppService {

    private final AssisteAccountDomainService assisteAccountDomainService;

    private final SubjectDomainService subjectDomainService;

    /**
     * 分页获取辅助核算
     *
     * @param form
     * @return
     */
    public PageV<AssisteAccountV> assisteAccountPage(PageF<SearchF<?>> form) {
        Page<AssisteAccountE> assisteAccountEPage = assisteAccountDomainService.queryPage(form);
        return PageV.of(form, assisteAccountEPage.getTotal(), Global.mapperFacade.mapAsList(assisteAccountEPage.getRecords(), AssisteAccountV.class));
    }

    /**
     * 获取辅助核算列表
     *
     * @return
     */
    public List<AssisteAccountV> assisteAccountList(AssisteAccountF form) {
        List<AssisteAccountE> assisteAccountEList = assisteAccountDomainService.assisteAccountList(form);
        return Global.mapperFacade.mapAsList(assisteAccountEList, AssisteAccountV.class);
    }

    /**
     * 根据辅助核算编码获取辅助核算列表
     *
     * @return
     */
    public List<AssisteAccountV> assisteAccountListByCodes(List<String> auxiliaryCountList) {
        List<AssisteAccountE> assisteAccountEList = assisteAccountDomainService.assisteAccountListByCodes(auxiliaryCountList);
        return Global.mapperFacade.mapAsList(assisteAccountEList, AssisteAccountV.class);
    }

    /**
     * 同步
     *
     * @param form
     * @return
     */
    public Boolean sync(SyncF form) {
        return assisteAccountDomainService.sync(form);
    }

    /**
     * 导出数据列表
     *
     * @param form
     * @return
     */
    public List<AssisteAccountV> exportList(PageF<SearchF<?>> form) {
        List<AssisteAccountE> assisteAccountEList = assisteAccountDomainService.exportList(form);
        return Global.mapperFacade.mapAsList(assisteAccountEList, AssisteAccountV.class);
    }

    /**
     * 根据条件获取业务类型
     *
     * @param form
     * @return
     */
    @Deprecated
    public List<BusinessTypeV> businessTypeList(BusinessTypeListF form) {
        List<BusinessTypeE> businessTypeVList = assisteAccountDomainService.businessTypeList(form);
        return Global.mapperFacade.mapAsList(businessTypeVList, BusinessTypeV.class);
    }

    /**
     * 辅助核算同步
     *
     * @param form
     * @return
     */
    public Boolean assisteAccountSync(AssisteAccountSyncF form) {
        return assisteAccountDomainService.sync(Global.mapperFacade.map(form, SyncF.class));
    }

    /**
     * 导出
     *
     * @param form
     * @param response
     * @return
     */
    public Boolean export(PageF<SearchF<?>> form, HttpServletResponse response) {
        List<AssisteAccountV> assisteAccountRvList = Global.mapperFacade.mapAsList(assisteAccountDomainService.exportList(form), AssisteAccountV.class);
        if (CollectionUtils.isNotEmpty(assisteAccountRvList)) {
            String fileName = "辅助核算导出" + UidHelper.nextIdByDTStr("assiste_account_export");
            try {
                ExcelUtil.export(fileName, assisteAccountRvList, AssisteAccountV.class, response);
                return true;
            } catch (Exception e) {
                throw BizException.throw400("数据导出失败：" + e.getMessage());
            }
        }
        throw BizException.throw400("导出数据为空");
    }

    /**
     * 根据科目id获取辅助核算列表
     * @param subjectId  科目id
     * @return
     */
    public List<AssisteAccountV> assisteAccountListBySubject(Long subjectId) {
        SubjectE subject = subjectDomainService.getById(new FindSubjectByIdQuery(subjectId));
        if (Objects.isNull(subject)) {
            throw BizException.throw400(ErrorMessage.SUBJECT_NOT_EXIST.msg());
        }
        if (StringUtils.isBlank(subject.getAuxiliaryCount())) {
            return Collections.emptyList();
        }
        List<AssisteAccountE> assisteAccountEList = assisteAccountDomainService.assisteAccountListByCodes(
            JSONArray.parseArray(subject.getAuxiliaryCount(), String.class));
        List<AssisteAccountV> assisteAccountVS = Global.mapperFacade.mapAsList(assisteAccountEList, AssisteAccountV.class);
        assisteAccountVS.forEach(ac -> ac.setType(AssisteItemTypeEnum.valueOfByAscCode(ac.getAsAcCode()).getCode()));
        return assisteAccountVS;
    }
}
