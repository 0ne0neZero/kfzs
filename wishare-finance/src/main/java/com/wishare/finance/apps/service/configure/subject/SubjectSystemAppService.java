package com.wishare.finance.apps.service.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectSystemF;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalBatchF;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectSystemF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCategoryV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemRelatedV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemSimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemV;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.configure.subject.SubjectAggregate;
import com.wishare.finance.domains.configure.subject.command.system.CreateSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.command.system.DeleteSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.command.system.FindSubjectSystemByIdQuery;
import com.wishare.finance.domains.configure.subject.command.system.UpdateSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.finance.domains.configure.subject.service.SubjectSystemDomainService;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 科目体系应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectSystemAppService implements ApiBase {

    private final AccountOrgFacade accountOrgFacade;
    private final SubjectAggregate subjectAggregate;
    private final SubjectDomainService subjectDomainService;

    private final SubjectSystemDomainService subjectSystemDomainService;


    /**
     * 构造SubjectSystemTreeV信息
     *
     * @param subjectSystemCollect   体系数据
     * @param subjectCategoryCollect 类型数据
     * @param statutoryBodyList      单位数据
     */
    private void createSubjectSystemTreeV(List<SubjectSystemTreeV> result,
                                          Map<Long, List<SubjectSystemV>> subjectSystemCollect,
                                          Map<Long, List<SubjectCategoryV>> subjectCategoryCollect,
                                          List<StatutoryBodyRv> statutoryBodyList) {
        //法定单位根节点
        for (StatutoryBodyRv statutoryBodyRv : statutoryBodyList) {
            SubjectSystemTreeV subjectSystemTreeV = new SubjectSystemTreeV();
            subjectSystemTreeV.setId(statutoryBodyRv.getId());
            subjectSystemTreeV.setName(statutoryBodyRv.getNameCn());
            subjectSystemTreeV.setPid(0L);
            subjectSystemTreeV.setLevel(Const.State._1);
            subjectSystemTreeV.setDisabled(statutoryBodyRv.getDisabled());
            result.add(subjectSystemTreeV);
        }
        //科目体系节点
        //记录科目体系的禁用状态,用于设置科目类型的禁用状态
        HashMap<Long, Integer> map = new HashMap<>(subjectSystemCollect.size());
        for (Map.Entry<Long, List<SubjectSystemV>> entry : subjectSystemCollect.entrySet()) {
            for (SubjectSystemV subjectSystemV : entry.getValue()) {
                map.put(subjectSystemV.getId(), subjectSystemV.getDisabled());
                SubjectSystemTreeV subjectSystemTreeV = new SubjectSystemTreeV();
                subjectSystemTreeV.setId(subjectSystemV.getId());
                subjectSystemTreeV.setName(subjectSystemV.getName());
                subjectSystemTreeV.setPid(entry.getKey());
                subjectSystemTreeV.setLevel(Const.State._2);
                subjectSystemTreeV.setDisabled(subjectSystemV.getDisabled());
                result.add(subjectSystemTreeV);
            }
        }
        //科目类型节点
        for (Map.Entry<Long, List<SubjectCategoryV>> entry : subjectCategoryCollect.entrySet()) {
            for (SubjectCategoryV subjectCategoryV : entry.getValue()) {
                SubjectSystemTreeV subjectSystemTreeV = new SubjectSystemTreeV();
                subjectSystemTreeV.setId(subjectCategoryV.getId());
                subjectSystemTreeV.setName(subjectCategoryV.getCategoryName());
                subjectSystemTreeV.setDisabled(map.get(entry.getKey()));
                if (subjectCategoryV.getParentId().equals(0L)) {
                    subjectSystemTreeV.setPid(entry.getKey());
                } else {
                    subjectSystemTreeV.setPid(subjectCategoryV.getParentId());
                }
                subjectSystemTreeV.setLevel(Const.State._3);
                result.add(subjectSystemTreeV);
            }
        }
    }

    /**
     * 创建科目体系
     *
     * @param createSubjectSystemF createSubjectSystemF
     * @return Long
     */
    @Transactional
    public Long create(CreateSubjectSystemF createSubjectSystemF) {
        CreateSubjectSystemCommand command = createSubjectSystemF.getCreateSubjectSystemCommand(curIdentityInfo());
        return subjectSystemDomainService.create(command);
    }

    /**
     * 更新科目体系
     *
     * @param updateSubjectSystemF updateSubjectSystemF
     * @return Boolean
     */
    @Transactional
    public Boolean update(UpdateSubjectSystemF updateSubjectSystemF) {
        UpdateSubjectSystemCommand command = updateSubjectSystemF.getUpdateSubjectSystemCommand(curIdentityInfo());
        return subjectSystemDomainService.update(command);
    }

    /**
     * 删除科目体系
     *
     * @param id 科目体系id
     * @return Boolean
     */
    @Transactional
    public Boolean delete(Long id) {
        DeleteSubjectSystemCommand command = new DeleteSubjectSystemCommand(id);
        return subjectAggregate.deleteSubjectSystem(command);
    }

    /**
     * 根据id查询科目体系详情
     *
     * @param id id
     * @return SubjectSystemV
     */
    public SubjectSystemV queryById(Long id) {
        SubjectSystemE subjectSystemE = subjectSystemDomainService.queryById(new FindSubjectSystemByIdQuery(id));
        return Global.mapperFacade.map(subjectSystemE, SubjectSystemV.class);
    }

    /**
     * 根据法定单位id查询科目体系
     *
     * @param id 法定单位id
     * @return List
     */
    public List<SubjectSystemV> queryByPertainId(Long id) {
        List<SubjectSystemE> subjectSystemList = subjectSystemDomainService.queryByPertainId(id);
        return Global.mapperFacade.mapAsList(subjectSystemList, SubjectSystemV.class);
    }

    /**
     * 获取所有科目体系
     *
     * @return List
     */
    public List<SubjectSystemV> queryAllByTenantId(String name) {
        List<SubjectSystemE> subjectSystemList = subjectSystemDomainService.queryAllSubjectSystem(name);
        return Global.mapperFacade.mapAsList(subjectSystemList, SubjectSystemV.class);
    }

    /**
     * 批量关联法定单位
     *
     * @param relatedLegalBatchF relatedLegalBatchF
     * @return Boolean
     */
    public Boolean relatedLegalUnitsBatch(RelatedLegalBatchF relatedLegalBatchF) {
        return subjectSystemDomainService.relatedLegalUnitsBatch(relatedLegalBatchF);
    }

    /**
     * 关联法定单位
     *
     * @param relatedLegalF relatedLegalF
     * @return Boolean
     */
    public Boolean relatedLegalUnits(RelatedLegalF relatedLegalF) {
        return subjectSystemDomainService.relatedLegalUnits(relatedLegalF);
    }

    /**
     * 分页获取法定单位
     *
     * @param queryPageF 撤销认领返回信息
     * @return PageV
     */
    public PageV<SubjectSystemRelatedV> queryLegalUnitsPage(PageF<SearchF<?>> queryPageF) {
        return subjectSystemDomainService.queryLegalUnitsPage(queryPageF);
    }

    /**
     * 撤销关联法定单位
     *
     * @param relatedId relatedId
     * @return Boolean
     */
    public Boolean disassociateLegalUnits(Long relatedId) {
        return subjectSystemDomainService.disassociateLegalUnits(relatedId);
    }

    /**
     * 分页获取科目体系
     *
     * @param queryPageF queryPageF
     * @return PageV
     */
    public PageV<SubjectSystemSimpleV> queryPage(PageF<SearchF<SubjectSystemE>> queryPageF) {
        return subjectSystemDomainService.queryPage(queryPageF);
    }

}
