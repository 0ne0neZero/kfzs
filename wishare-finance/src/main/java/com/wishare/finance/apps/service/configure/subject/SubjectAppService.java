package com.wishare.finance.apps.service.configure.subject;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.subject.fo.CreateSubjectF;
import com.wishare.finance.apps.model.configure.subject.fo.UpdateSubjectF;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectCashFlowV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectDetailV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectSimpleV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectTreeV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectV;
import com.wishare.finance.domains.configure.subject.command.subject.CreateSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.DeleteSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.FindSubjectByIdQuery;
import com.wishare.finance.domains.configure.subject.command.subject.FindSubjectChildByIdQuery;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSystemSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.UpdateSubjectCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectCategoryDefaultEnum;
import com.wishare.finance.domains.configure.subject.dto.SubjectD;
import com.wishare.finance.domains.configure.subject.entity.SubjectCashFlow;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.service.SubjectCashFlowDomainService;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 科目应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectAppService implements ApiBase {

    private final SubjectDomainService subjectDomainService;
    private final SubjectCashFlowDomainService subjectCashFlowDomainService;

    /**
     * 创建科目
     *
     * @param createSubjectF createSubjectF
     * @return Long
     */
    @Transactional
    public Long create(CreateSubjectF createSubjectF) {
        CreateSubjectCommand command = createSubjectF.getCreateSubjectCommand(curIdentityInfo());
        Long subjectId = subjectDomainService.create(command);
        if (Objects.nonNull(subjectId)){
            List<SubjectCashFlow> cashFlows = Global.mapperFacade.mapAsList(createSubjectF.getCashFlows(), SubjectCashFlow.class);
            if (CollectionUtils.isNotEmpty(cashFlows)){
                cashFlows.forEach(item -> {
                    item.setSubjectId(subjectId);
                });
                subjectCashFlowDomainService.saveOrUpdateSubjectCashFlow(subjectId, cashFlows, false);
            }
        }
        return subjectId;
    }

    /**
     * 更新科目
     *
     * @param updateSubjectF updateSubjectF
     * @return Boolean
     */
    @Transactional
    public Boolean update(UpdateSubjectF updateSubjectF) {
        UpdateSubjectCommand command = updateSubjectF.getUpdateSubjectCommand(curIdentityInfo());
        boolean result = subjectDomainService.update(command);
        if (result){
            if(CollectionUtils.isNotEmpty(updateSubjectF.getCashFlows())) {
                List<SubjectCashFlow> cashFlows = Global.mapperFacade.mapAsList(updateSubjectF.getCashFlows(), SubjectCashFlow.class);
                if (CollectionUtils.isNotEmpty(cashFlows)){
                    cashFlows.forEach(cashFlow ->  {
                        cashFlow.setSubjectId(command.getId());
                    });
                }
                subjectCashFlowDomainService.saveOrUpdateSubjectCashFlow(command.getId(), cashFlows, true);
            }
        }
        return result;
    }

    /**
     * 根据id查询科目详情
     *
     * @param id id
     * @return SubjectV
     */
    public SubjectDetailV getById(Long id) {
        SubjectE subjectE = subjectDomainService.getById(new FindSubjectByIdQuery(id));
        SubjectDetailV subjectV = Global.mapperFacade.map(subjectE, SubjectDetailV.class);
        List<Long> subjectPath = JSON.parseArray(subjectE.getPath(), Long.class);
        List<String> auxiliaryCountList = JSON.parseArray(subjectE.getAuxiliaryCount(), String.class);
        subjectPath.remove(subjectPath.size() - 1);
        subjectV.setSubjectPath(subjectPath);
        subjectV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
        subjectV.setCashFlows(Global.mapperFacade.mapAsList(subjectCashFlowDomainService.getSubjectCashFlowBySubjectIds(List.of(subjectE.getId())), SubjectCashFlowV.class));
        return subjectV;
    }

    /**
     * 删除科目
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        return subjectDomainService.delete(new DeleteSubjectCommand(id));
    }

    /**
     * 根据id查询子科目列表
     *
     * @param id id
     * @return List
     */
    public List<SubjectSimpleV> queryChildById(Long id,Long systemId) {
        List<SubjectE> subjectList = subjectDomainService.queryChildById(new FindSubjectChildByIdQuery(id,systemId));
        List<SubjectSimpleV> resultList = Global.mapperFacade.mapAsList(subjectList, SubjectSimpleV.class);
        for (SubjectSimpleV subjectSimpleV : resultList) {
            if(Objects.nonNull(subjectSimpleV.getCategoryId())){
                SubjectCategoryDefaultEnum categoryEnum = SubjectCategoryDefaultEnum.valueOfByCode(subjectSimpleV.getCategoryId().intValue());
                subjectSimpleV.setCategoryName(categoryEnum == null ? null : categoryEnum.getValue());
            }
        }
        return resultList;
    }

    /**
     * 分页查询科目
     *
     * @param querySubjectPageF querySubjectPageF
     * @return PageV
     */
    public PageV<SubjectV> queryByPage(PageF<SearchF<SubjectE>> querySubjectPageF) {
        Page<SubjectD> queryByPage = subjectDomainService.queryByPage(querySubjectPageF);
        List<SubjectD> records = queryByPage.getRecords();

        ArrayList<SubjectV> subjectVoList = new ArrayList<>();
        for (SubjectD subjectD : records) {
            SubjectV subjectV = Global.mapperFacade.map(subjectD, SubjectV.class);
            List<String> auxiliaryCountList = JSON.parseArray(subjectD.getAuxiliaryCount(), String.class);
            subjectV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
            subjectVoList.add(subjectV);
        }

        //组装树结构
        for (SubjectV subjectV : subjectVoList) {
            if (subjectV.getParentId() != null) {
                subjectV.setPid(subjectV.getParentId());
            }
        }
        List<SubjectV> treeing = TreeUtil.treeing(subjectVoList);
        return PageV.of(querySubjectPageF, queryByPage.getTotal(), treeing);
    }

    /**
     * 获取科目树
     *
     * @return List
     */
    public List<SubjectTreeV> queryTree(Long filterId,Long categoryId,Long systemId) {
        //获取当前体系，当前类型下的科目
        List<SubjectE> subjectTreeList = subjectDomainService.queryChargeItemList(filterId, categoryId, systemId,curIdentityInfo().getTenantId());
        ArrayList<SubjectTreeV> subjectTreeVoList = new ArrayList<>();
        for (SubjectE subjectE : subjectTreeList) {
            SubjectTreeV subjectTreeV = Global.mapperFacade.map(subjectE, SubjectTreeV.class);
            if (subjectE.getParentId() != null) {
                subjectTreeV.setPid(subjectE.getParentId());
            }
            List<String> auxiliaryCountList = JSON.parseArray(subjectE.getAuxiliaryCount(), String.class);
            subjectTreeV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
            subjectTreeVoList.add(subjectTreeV);
        }
        return TreeUtil.treeing(subjectTreeVoList);
    }

    /**
     * 获取科目树
     *
     * @return List
     */
    public List<SubjectV> queryList(Long filterId,Long categoryId,Long systemId) {
        //获取当前体系，当前类型下的科目
        List<SubjectE> subjectTreeList = subjectDomainService.queryChargeItemList(filterId, categoryId, systemId,curIdentityInfo().getTenantId());
        ArrayList<SubjectV> subjectVList = new ArrayList<>();
        for (SubjectE subjectE : subjectTreeList) {
            SubjectV subjectV = Global.mapperFacade.map(subjectE, SubjectV.class);
            if (subjectE.getParentId() != null) {
                subjectV.setPid(subjectE.getParentId());
            }
            List<String> auxiliaryCountList = JSON.parseArray(subjectE.getAuxiliaryCount(), String.class);
            subjectV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
            subjectVList.add(subjectV);
        }
        return subjectVList;
    }

    /**
     * 获取科目树
     *
     * @return List
     */
    public List<SubjectV> queryListWithAllPath(Long filterId,Long categoryId,Long systemId) {
        //获取当前体系，当前类型下的科目
        List<SubjectE> subjectTreeList = subjectDomainService.queryChargeItemList(filterId, categoryId, systemId,curIdentityInfo().getTenantId());
        Map<Long, String> map = subjectTreeList.stream()
            .collect(Collectors.toMap(SubjectE::getId, SubjectE::getSubjectName));
        ArrayList<SubjectV> subjectVList = new ArrayList<>();
        for (SubjectE subjectE : subjectTreeList) {
            SubjectV subjectV = Global.mapperFacade.map(subjectE, SubjectV.class);
            if (subjectE.getParentId() != null) {
                subjectV.setPid(subjectE.getParentId());
            }
            List<String> auxiliaryCountList = JSON.parseArray(subjectE.getAuxiliaryCount(), String.class);
            subjectV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
            subjectVList.add(subjectV);

            List<Long> subjectPath = JSON.parseArray(subjectE.getPath(), Long.class);
            StringJoiner joiner = new StringJoiner("/");
            for (Long path : subjectPath) {
                joiner.add(map.getOrDefault(path, ""));
            }
            subjectV.setSubjectName(joiner.toString());
        }
        return subjectVList;
    }

    /**
     * 导入科目
     *
     * @param file file
     * @param subjectSystemId 科目体系id
     * @return Boolean
     */
    public Boolean importSubject(MultipartFile file, Long subjectSystemId) {
        return subjectDomainService.importSubject(file,subjectSystemId,curIdentityInfo());
    }

    /**
     * 根据ID查科目名称
     *
     * @param id 科目id
     * @return String
     */
    public String getNameById(Long id) {
        return subjectDomainService.getNameById(id);
    }

    /**
     * 获取末级科目(最后一级科目)
     *
     * @param systemId 体系id
     * @param categoryId 科目类型id
     * @return List
     */
    public List<SubjectSimpleV> getLastStage(Long systemId, Long categoryId,String name,Long subjectId) {
        List<SubjectE> subjectList = subjectDomainService.getLastStage(systemId, categoryId,name,subjectId, 0);
        List<SubjectSimpleV> resultList = Global.mapperFacade.mapAsList(subjectList, SubjectSimpleV.class);
        for (SubjectSimpleV subjectSimpleV : resultList) {
            if(Objects.nonNull(subjectSimpleV.getCategoryId())){
                SubjectCategoryDefaultEnum categoryEnum = SubjectCategoryDefaultEnum.valueOfByCode(subjectSimpleV.getCategoryId().intValue());
                subjectSimpleV.setCategoryName(categoryEnum == null ? null : categoryEnum.getValue());
            }
        }
        return resultList;
    }

    /**
     * 获取末级科目(最后一级科目) 全路径
     *
     * @param systemId 体系id
     * @param categoryId 科目类型id
     * @return List
     */
    public List<SubjectV> getLastStageAllPatch(Long systemId, Long categoryId, String name, Long subjectId) {
        List<SubjectE> subjectList = subjectDomainService.getLastStage(systemId, categoryId,name,subjectId, 50);
        List<SubjectV> resultList = new ArrayList<>();
        for (SubjectE subjectE : subjectList) {
            SubjectV subjectV = Global.mapperFacade.map(subjectE, SubjectV.class);
            if(Objects.nonNull(subjectV.getCategoryId())){
                SubjectCategoryDefaultEnum categoryEnum = SubjectCategoryDefaultEnum.valueOfByCode(subjectV.getCategoryId().intValue());
                subjectV.setCategoryName(categoryEnum == null ? null : categoryEnum.getValue());

                List<Long> subjectPath = JSON.parseArray(subjectE.getPath(), Long.class);
                List<SubjectV> subjectVS = subjectDomainService.listByIds(subjectPath);
                Map<Long, String> map = subjectVS.stream()
                    .collect(Collectors.toMap(SubjectV::getId, SubjectV::getSubjectName));
                StringJoiner joiner = new StringJoiner("/");
                for (Long path : subjectPath) {
                    joiner.add(map.getOrDefault(path, ""));
                }
                subjectV.setSubjectName(joiner.toString());
            }
            resultList.add(subjectV);
        }
        return resultList;
    }

    /**
     * 根据ids查询科目详情
     * @param subjectIds
     * @return
     */
    public List<SubjectV> listByIds(List<Long> subjectIds) {
        return subjectDomainService.listByIds(subjectIds);
    }

    /**
     * 根据费项id查科目数据
     * @param headSubjectId 一级科目id
     * @param chargeItemId 费项id
     * @return
     */
    public SubjectV getSubjectByChargeItemIdAndHeadSubjectId(Long headSubjectId, Long chargeItemId) {
        return subjectDomainService.getSubjectByChargeItemIdAndHeadSubjectId(headSubjectId, chargeItemId);
    }

    /**
     * 同步科目信息
     * @param syncSystemSubjectCommand
     * @return
     */
    @Transactional
    public boolean syncSubject(SyncSystemSubjectCommand syncSystemSubjectCommand){
        return subjectDomainService.syncSubject(syncSystemSubjectCommand);
    }

    public List<String> getAllAuxiliary(Long subjectId) {
        return subjectDomainService.getAllAuxiliary(subjectId);
    }

}
