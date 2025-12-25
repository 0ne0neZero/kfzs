package com.wishare.finance.domains.configure.subject.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalBatchF;
import com.wishare.finance.apps.model.configure.subject.fo.RelatedLegalF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemRelatedV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSystemSimpleV;
import com.wishare.finance.domains.configure.subject.command.system.CreateSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.command.system.DeleteSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.command.system.FindSubjectSystemByIdQuery;
import com.wishare.finance.domains.configure.subject.command.system.UpdateSubjectSystemCommand;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectStatutoryRelationE;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import com.wishare.finance.domains.configure.subject.facade.SubjectOrgFacade;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectStatutoryRelationRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectSystemRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 科目体系业务层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectSystemDomainService {

    private final SubjectRepository subjectRepository;

    private final SubjectSystemRepository subjectSystemRepository;

    private final SubjectStatutoryRelationRepository subjectStatutoryRelationRepository;

    private final SubjectOrgFacade subjectOrgFacade;

    /**
     * 创建科目体系
     *
     * @param command command
     * @return Long
     */
    public Long create(CreateSubjectSystemCommand command) {
        //校验编码是否存在
        checkCodeIsExist(null, command.getCode(), command.getTenantId());

        //创建体系
        SubjectSystemE subjectSystemE = Global.mapperFacade.map(command, SubjectSystemE.class);
        subjectSystemRepository.save(subjectSystemE);
        return subjectSystemE.getId();
    }

    /**
     * 检验编码是否存在
     *
     * @param currentId currentId
     * @param code      code
     * @param tenantId  tenantId
     */
    private void checkCodeIsExist(Long currentId, String code, String tenantId) {
        SubjectSystemE subjectSystem = subjectSystemRepository.getSubjectSystemByCode(currentId, code, tenantId);
        if (subjectSystem != null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_SYSTEM_CODE_EXIST.getErrMsg());
        }
    }

    /**
     * 更新科目体系
     *
     * @param command command
     * @return Boolean
     */
    public Boolean update(UpdateSubjectSystemCommand command) {
        Long subjectSystemId = command.getId();
        //校验科目体系是否存在
        SubjectSystemE oldSubjectSystem = subjectSystemRepository.getById(subjectSystemId);
        if (oldSubjectSystem == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_SYSTEM_NOT_EXIST.getErrMsg());
        }

        //校验体系编码是否存在
        checkCodeIsExist(command.getId(), command.getCode(), command.getTenantId());

        SubjectSystemE newSubjectSystem = Global.mapperFacade.map(command, SubjectSystemE.class);
        return subjectSystemRepository.updateById(newSubjectSystem);
    }

    /**
     * 删除科目体系
     *
     * @param command command
     * @return Boolean
     */
    public Boolean delete(DeleteSubjectSystemCommand command) {
        //校验科目体系是否存在
        SubjectSystemE subjectSystem = subjectSystemRepository.getById(command.getId());
        ErrorAssertUtil.notNullThrow300(subjectSystem, ErrorMessage.SUBJECT_SYSTEM_NOT_EXIST);

        //校验当前体系是否存在科目
        SubjectE subject = subjectRepository.getSubjectBySubjectSystemId(subjectSystem.getId());
        ErrorAssertUtil.isNullThrow300(subject, ErrorMessage.SUBJECT_SYSTEM_EXIST_SUBJECT);

        //当前科目体系是否关联法定单位
        List<SubjectStatutoryRelationE> subjectStatutoryRelationList = subjectStatutoryRelationRepository.queryBySubjectSystemId(command.getId());
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(subjectStatutoryRelationList), ErrorMessage.SUBJECT_SYSTEM_RELATED_STATUTORY);

        return subjectSystemRepository.removeById(subjectSystem.getId());
    }

    /**
     * 根据id查询科目体系信息
     *
     * @param query query
     * @return SubjectE
     */
    public SubjectSystemE queryById(FindSubjectSystemByIdQuery query) {
        return subjectSystemRepository.getById(query.getId());
    }


    /**
     * 获取当前租户下所有科目体系
     *
     * @return List
     */
    public List<SubjectSystemE> queryAllSubjectSystem(String name) {
        return subjectSystemRepository.querySubjectSystemByName(name);
    }

    /**
     * 根据法定单位id查询科目体系
     *
     * @param id 法定单位id
     * @return List
     */
    public List<SubjectSystemE> queryByPertainId(Long id) {
        List<SubjectStatutoryRelationE> subjectStatutoryRelationList = subjectStatutoryRelationRepository.queryByStatutoryBodyId(id);
        List<Long> subjectSystemIdList = subjectStatutoryRelationList.stream().map(SubjectStatutoryRelationE::getSubjectSystemId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(subjectSystemIdList)){
            return subjectSystemRepository.listByIds(subjectSystemIdList);
        }
        return new ArrayList<>();
    }

    /**
     * 关联法定单位
     *
     * @param relatedLegalF relatedLegalF
     * @return Boolean
     */
    public Boolean relatedLegalUnitsBatch(RelatedLegalBatchF relatedLegalF) {
        Long subjectSystemId = relatedLegalF.getSubjectSystemId();
        List<Long> statutoryBodyIdList = relatedLegalF.getStatutoryBodyIdList();
        //获取当前科目体系已关联的法定单位
        List<SubjectStatutoryRelationE> existRelationList = subjectStatutoryRelationRepository.queryByStatutoryBodyIdAndSubjectSystemId(subjectSystemId, statutoryBodyIdList);
        List<Long> collect = existRelationList.stream().map(SubjectStatutoryRelationE::getStatutoryBodyId).collect(Collectors.toList());
        //添加未关联的法定单位
        statutoryBodyIdList.removeAll(collect);
        ArrayList<SubjectStatutoryRelationE> saveList = new ArrayList<>();
        for (Long statutoryBodyId : statutoryBodyIdList) {
            SubjectStatutoryRelationE relation = new SubjectStatutoryRelationE();
            relation.setSubjectSystemId(subjectSystemId);
            relation.setStatutoryBodyId(statutoryBodyId);
            saveList.add(relation);
        }
        return subjectStatutoryRelationRepository.saveBatch(saveList);
    }

    /**
     * 关联法定单位
     *
     * @param relatedLegalF relatedLegalF
     * @return Boolean
     */
    public Boolean relatedLegalUnits(RelatedLegalF relatedLegalF) {
        SubjectStatutoryRelationE relation = new SubjectStatutoryRelationE();
        relation.setSubjectSystemId(relatedLegalF.getSubjectSystemId());
        relation.setStatutoryBodyId(relatedLegalF.getStatutoryBodyId());
        return subjectStatutoryRelationRepository.save(relation);
    }

    /**
     * 分页获取法定单位
     *
     * @param queryPageF 撤销认领返回信息
     * @return PageV
     */
    public PageV<SubjectSystemRelatedV> queryLegalUnitsPage(PageF<SearchF<?>> queryPageF) {
        Long subjectSystemId = null;
        List<Field> fieldList = queryPageF.getConditions().getFields();
        List<Field> subjectSystemIdList = fieldList.stream().filter(s -> "subject_system_id".equals(s.getName())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(subjectSystemIdList)){
            subjectSystemId = (Long) subjectSystemIdList.get(0).getValue();
            fieldList.removeAll(subjectSystemIdList);
        }
        if(Objects.isNull(subjectSystemId)){
            return PageV.of(queryPageF, 0, new ArrayList<>());
        }
        //根据体系id获取已关联的法定单位
        List<SubjectStatutoryRelationE> subjectStatutoryRelationList = subjectStatutoryRelationRepository.queryBySubjectSystemId(subjectSystemId);
        //是否查询法定单位标识
        boolean searchFlag = true;
        List<Field> relatedList = fieldList.stream().filter(s -> "related".equals(s.getName())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(relatedList)){
            relatedList.get(0).setName("id");
            List<Long> statutoryBodyIdList = subjectStatutoryRelationList.stream().map(SubjectStatutoryRelationE::getStatutoryBodyId).collect(Collectors.toList());
            Integer related = (Integer) relatedList.get(0).getValue();
            if(related == 1){
                if(CollectionUtils.isNotEmpty(statutoryBodyIdList)){
                    //选择已关联，则根据法定单位id进行in查询
                    relatedList.get(0).setMethod(15);
                    relatedList.get(0).setValue(statutoryBodyIdList);
                }else{
                    searchFlag = false;
                }
            }else{
                fieldList.removeAll(relatedList);
                //选择未关联，则根据法定单位id进行不等于查询
                for (Long id : statutoryBodyIdList) {
                    fieldList.add(new Field("id",id,2));
                }
            }
        }
        //类型为法定单位
        fieldList.add(new Field("type", "1", 1));
        //状态为启用
        fieldList.add(new Field("disabled",0,1));
        //获取法定单位数据
        if(searchFlag){
            PageV<StatutoryBodyRv> page = subjectOrgFacade.getOrgFinancePage(queryPageF);
            List<StatutoryBodyRv> records = page.getRecords();
            List<SubjectSystemRelatedV> subjectSystemRelatedList = Global.mapperFacade.mapAsList(records, SubjectSystemRelatedV.class);
            //设置是否关联字段
            Map<Long, List<SubjectStatutoryRelationE>> collect = subjectStatutoryRelationList.stream().collect(Collectors.groupingBy(SubjectStatutoryRelationE::getStatutoryBodyId));
            for (SubjectSystemRelatedV subjectSystemRelatedV : subjectSystemRelatedList) {
                if(CollectionUtils.isNotEmpty(collect.get(subjectSystemRelatedV.getId()))){
                    subjectSystemRelatedV.setRelated(1);
                    subjectSystemRelatedV.setRelatedId(collect.get(subjectSystemRelatedV.getId()).get(0).getId());
                }else{
                    subjectSystemRelatedV.setRelated(0);
                }
                subjectSystemRelatedV.setSubjectSystemId(subjectSystemId);
            }
            return PageV.of(queryPageF, page.getTotal(), subjectSystemRelatedList);
        }else{
            return PageV.of(queryPageF, 0, new ArrayList<>());
        }
    }

    /**
     * 撤销关联法定单位
     *
     * @param relatedId relatedId
     * @return Boolean
     */
    public Boolean disassociateLegalUnits(Long relatedId) {
        return subjectStatutoryRelationRepository.removeById(relatedId);
    }

    /**
     * 分页获取科目体系
     *
     * @param queryPageF queryPageF
     * @return PageV
     */
    public PageV<SubjectSystemSimpleV> queryPage(PageF<SearchF<SubjectSystemE>> queryPageF) {
        Long statutoryBodyId = null;
        List<Field> fieldList = queryPageF.getConditions().getFields();
        List<Field> statutoryBodyIdList = fieldList.stream().filter(s -> "statutory_body_id".equals(s.getName())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(statutoryBodyIdList)){
            Object statutoryBodyIdValue = statutoryBodyIdList.get(0).getValue();
            if (Objects.nonNull(statutoryBodyIdValue)) {
                statutoryBodyId = Long.valueOf(statutoryBodyIdValue.toString());
            }
            fieldList.removeAll(statutoryBodyIdList);
        }
        //根据法定单位id获取关联的科目体系
        Page<SubjectSystemE> page = subjectSystemRepository.queryPageByStatutoryBodyId(new Page<>(queryPageF.getPageNum(), queryPageF.getPageSize()),statutoryBodyId);
        List<SubjectSystemSimpleV> result = Global.mapperFacade.mapAsList(page.getRecords(), SubjectSystemSimpleV.class);
        return PageV.of(queryPageF, page.getTotal(), result);
    }
}
