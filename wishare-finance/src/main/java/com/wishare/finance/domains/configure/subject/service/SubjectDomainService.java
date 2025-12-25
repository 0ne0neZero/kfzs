package com.wishare.finance.domains.configure.subject.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectV;
import com.wishare.finance.domains.configure.subject.command.subject.CreateSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.DeleteSubjectBySubjectSystemIdCommand;
import com.wishare.finance.domains.configure.subject.command.subject.DeleteSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.FindSubjectByIdQuery;
import com.wishare.finance.domains.configure.subject.command.subject.FindSubjectChildByIdQuery;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.SyncSystemSubjectCommand;
import com.wishare.finance.domains.configure.subject.command.subject.UpdateSubjectCommand;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectCategoryDefaultEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectDisabledEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectLeafStatusEnum;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectStatusEnum;
import com.wishare.finance.domains.configure.subject.dto.SubjectD;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectSystemRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.easyexcel.BaseExcelDataListener;
import com.wishare.finance.infrastructure.easyexcel.SubjectData;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 科目业务层
 *
 * @author yancao
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectDomainService {

    private final SubjectRepository subjectRepository;

    private final SubjectSystemRepository subjectSystemRepository;

    /**
     * 创建科目
     *
     * @param command command
     */
    public Long create(CreateSubjectCommand command) {
        //判断科目名称当前层级是否已存在
        Long parentId = command.getParentId() == null ? Const.State._0 : command.getParentId();
        Long subjectSystemId = command.getSubjectSystemId();
        checkSubjectName(null, null, subjectSystemId, command.getSubjectName(), command.getTenantId());

        //判断科目编码是否已存在
        String subjectCode = command.getSubjectCode();
        checkSubjectCode(subjectCode, null, command.getTenantId(),command.getSubjectSystemId());

        SubjectE subject = Global.mapperFacade.map(command, SubjectE.class);
        //设置层级
        if (parentId != Const.State._0) {
            //判断是否超过科目最大层级
            SubjectE parentSubject = subjectRepository.getById(parentId);
            if (parentSubject == null) {
                throw BizException.throw400(ErrorMessage.SUBJECT_PARENT_NOT_EXIST.getErrMsg());
            }
            if (parentSubject.getLevel() >= SubjectStatusEnum.SUBJECT_MAX_LEVEL.getCode()) {
                throw BizException.throw400(ErrorMessage.SUBJECT_EXCEED_MAX_LEVEL.getErrMsg());
            }

            //父id存在
            List<Long> path = JSON.parseArray(parentSubject.getPath(), Long.class);
            path.add(subject.getId());
            subject.setLevel(parentSubject.getLevel() + 1);
            subject.setPath(JSON.toJSONString(path));

            //更新父科目为非叶子节点
            if (parentSubject.getLeaf() == SubjectLeafStatusEnum.叶子节点.getCode()) {
                parentSubject.setLeaf(SubjectLeafStatusEnum.非叶子节点.getCode());
                subjectRepository.updateById(parentSubject);
            }
        } else {
            //父id不存在
            subject.setLevel(SubjectStatusEnum.SUBJECT_LEVEL_START_NUM.getCode());
            subject.setParentId(0L);
            ArrayList<Long> path = new ArrayList<>();
            path.add(subject.getId());
            subject.setPath(JSON.toJSONString(path));
        }
        subject.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
        subjectRepository.save(subject);
        return subject.getId();
    }

    /**
     * 更新科目
     *
     * @param command command
     * @return Boolean
     */
    public boolean update(UpdateSubjectCommand command) {
        String tenantId = command.getTenantId();

        //判断当前层级下是否存在相同科目名称
        SubjectE oldSubject = subjectRepository.getById(command.getId());
        if (oldSubject == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_NOT_EXIST.getErrMsg());
        }
        if (StringUtils.isNotEmpty(command.getSubjectName())) {
            checkSubjectName(oldSubject.getId(), oldSubject.getParentId(), oldSubject.getSubjectSystemId(), command.getSubjectName(), tenantId);
        }

        //判断科目编码是否已存在
        String subjectCode = command.getSubjectCode();
        if (StringUtils.isNotEmpty(subjectCode)) {
            checkSubjectCode(subjectCode, oldSubject.getId(), tenantId,command.getSubjectSystemId());
        }

        SubjectE subject = Global.mapperFacade.map(command, SubjectE.class);
        //父科目id不为空，重新设置path和level
        if (subject.getParentId() != null) {
            if (!subject.getParentId().equals(oldSubject.getParentId())) {
                SubjectE targetParentSubject = subjectRepository.getById(subject.getParentId());
                List<Long> path = JSON.parseArray(targetParentSubject.getPath(), Long.class);
                path.add(oldSubject.getId());
                subject.setLevel(targetParentSubject.getLevel() + 1);
                subject.setPath(JSON.toJSONString(path));
                //更新目标科目
                if (targetParentSubject.getLeaf() == SubjectLeafStatusEnum.叶子节点.getCode()) {
                    targetParentSubject.setLeaf(SubjectLeafStatusEnum.非叶子节点.getCode());
                    subjectRepository.updateById(targetParentSubject);
                }
            }
        } else {
            subject.setParentId(0L);
            subject.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
            subject.setLevel(SubjectStatusEnum.SUBJECT_LEVEL_START_NUM.getCode());
            subject.setPath(JSON.toJSONString(Collections.singletonList(subject.getId())));
        }
        //更新当前科目的父科目
        if (oldSubject.getParentId() != null && oldSubject.getParentId() != Const.State._0) {
            SubjectE currentParentSubject = subjectRepository.getById(oldSubject.getParentId());
            //获取当前科目下子科目的数量
            long count = subjectRepository.getSubCountWithCurrentSubject(oldSubject.getParentId(), tenantId);
            if (count == 1) {
                currentParentSubject.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
                subjectRepository.updateById(currentParentSubject);
            }
        }

        boolean categoryFlag = subject.getCategoryId() != null && !subject.getCategoryId().equals(oldSubject.getCategoryId());
        boolean disabledFlag = subject.getDisabled() != null && !subject.getDisabled().equals(oldSubject.getDisabled());
        if (categoryFlag || disabledFlag) {
            List<SubjectE> subjectList = subjectRepository.querySubjectByIdList(Collections.singletonList(subject.getId()));
            List<Long> subIdList = subjectList.stream().map(SubjectE::getId).collect(Collectors.toList());
            LambdaUpdateWrapper<SubjectE> updateWrapper = new LambdaUpdateWrapper<>();
            if (disabledFlag) {
                //更新子科目的是否启用状态
                if (subject.getDisabled() == SubjectDisabledEnum.禁用.getCode()) {
                    updateWrapper.set(SubjectE::getDisabled, SubjectDisabledEnum.禁用.getCode());
                } else if (subject.getDisabled() == SubjectDisabledEnum.启用.getCode()) {
                    updateWrapper.set(SubjectE::getDisabled, SubjectDisabledEnum.启用.getCode());
                    List<SubjectE> collect = subjectList.stream().filter(s -> s.getId().equals(subject.getId())).collect(Collectors.toList());
                    if(!CollectionUtils.isEmpty(collect)){
                        List<Long> pathList = JSON.parseArray(collect.get(0).getPath(), Long.class);
                        subIdList.addAll(pathList);
                    }
                }
            }
            if (categoryFlag) {
                //修改为其他科目类型，将子科目的科目类型一起修改
                updateWrapper.set(SubjectE::getCategoryId, subject.getCategoryId());
            }
            updateWrapper.in(SubjectE::getId, subIdList);
            subjectRepository.update(updateWrapper);
        }
        subjectRepository.updateChildrenCashType(JSON.parseArray(subject.getPath(), Long.class), subject.getCashType());
        return subjectRepository.updateById(subject);
    }

    /**
     * 删除科目
     *
     * @param command command
     * @return Boolean
     */
    public Boolean delete(DeleteSubjectCommand command) {
        SubjectE oldSubject = subjectRepository.getById(command.getId());
        if (oldSubject == null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_NOT_EXIST.getErrMsg());
        }

        //获取当前科目以及该科目的所有子科目
        List<Long> idList = Collections.singletonList(oldSubject.getId());
        List<SubjectE> subjectList = subjectRepository.querySubjectByIdList(idList);
        List<Long> subIdList = subjectList.stream().map(SubjectE::getId).collect(Collectors.toList());

        //当前科目的父科目只有一个子科目，更新父科目为叶子节点
        long count = subjectRepository.getSubCountWithCurrentSubject(oldSubject.getParentId(), oldSubject.getTenantId());
        if (count == 1) {
            SubjectE parentSubjectE = new SubjectE();
            parentSubjectE.setId(oldSubject.getParentId());
            parentSubjectE.setLeaf(SubjectLeafStatusEnum.叶子节点.getCode());
            subjectRepository.updateById(parentSubjectE);
        }

        //更新是否删除字段
        return subjectRepository.removeBatchByIds(subIdList);
    }

    /**
     * 根据体系id删除科目
     *
     * @param command command
     */
    public void deleteBySubjectSystemId(DeleteSubjectBySubjectSystemIdCommand command) {
        //校验是否关联账单

        subjectRepository.removeBySubjectSystemId(command.getSubjectSystemId());
    }

    /**
     * 根据id查询科目信息
     *
     * @param query query
     * @return SubjectE
     */
    public SubjectE getById(FindSubjectByIdQuery query) {
        return subjectRepository.getById(query.getId());
    }

    /**
     * 根据id查询子科目列表
     *
     * @param query query
     * @return List
     */
    public List<SubjectE> queryChildById(FindSubjectChildByIdQuery query) {
        long parentId = query.getId() == null ? Const.State._0 : query.getId();
        return subjectRepository.getCurrentLevelSubject(parentId, query.getSystemId(), null, null);
    }

    /**
     * 分页查询科目
     *
     * @param query query
     * @return PageV
     */
    public Page<SubjectD> queryByPage(PageF<SearchF<SubjectE>> query) {
        QueryWrapper<SubjectE> queryWrapper = query.getConditions().getQueryModel();
//        queryWrapper.eq("st.parent_id", Const.State._0);
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        Page<SubjectE> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<SubjectD> subjectPage = subjectRepository.querySubjectByPage(page, queryWrapper);
        List<SubjectD> recordList = subjectPage.getRecords();
        //获取子科目
        List<Long> idList = recordList.stream().map(SubjectE::getId).collect(Collectors.toList());
        List<SubjectD> childSubjectList = new ArrayList<>(recordList);
        if (!CollectionUtils.isEmpty(idList)) {
            QueryWrapper<SubjectE> queryModel = query.getConditions().getQueryModel();
            queryModel.notIn("st.id",idList);
            queryModel.eq("st.deleted", DataDeletedEnum.NORMAL.getCode());
            queryWrapper.orderByDesc("st.id");
            //获取所有返回科目的id
            List<SubjectD> subjectDoList = subjectRepository.querySubjectWithCategoryNameByIdList(idList, queryModel);
            List<String> pathCollect = subjectDoList.stream().map(SubjectE::getPath).collect(Collectors.toList());
            ArrayList<Long> targetIdList = new ArrayList<>();
            pathCollect.forEach(path -> {
                List<Long> longList = JSON.parseArray(path, Long.class);
                longList.removeAll(idList);
                targetIdList.addAll(longList);
            });
            if(!CollectionUtils.isEmpty(targetIdList)){
                List<SubjectE> subjectEoList = subjectRepository.listByIds(targetIdList);
                childSubjectList.addAll(Global.mapperFacade.mapAsList(subjectEoList, SubjectD.class));
            }
            for (SubjectD subjectD : childSubjectList) {
                if(Objects.nonNull(subjectD.getCategoryId())){
                    SubjectCategoryDefaultEnum categoryEnum = SubjectCategoryDefaultEnum.valueOfByCode(subjectD.getCategoryId().intValue());
                    subjectD.setCategoryName(categoryEnum == null ? null : categoryEnum.getValue());
                }
            }
        }
        subjectPage.setRecords(childSubjectList);
        return subjectPage;
    }

    /**
     * 校验当前层级下是否存在相同科目名称
     *
     * @param currentSubjectId currentSubjectId
     * @param parentId         parentId
     * @param subjectName      subjectName
     * @param subjectSystemId  subjectSystemId
     */
    private void checkSubjectName(Long currentSubjectId, Long parentId, Long subjectSystemId, String subjectName, String tenantId) {
        List<SubjectE> currentLevelSubjectList = subjectRepository.getCurrentLevelSubject(parentId, subjectSystemId, tenantId, currentSubjectId);
        List<SubjectE> collect = currentLevelSubjectList.stream()
                .filter(subject -> subject.getSubjectName().equals(subjectName))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            throw BizException.throw400(ErrorMessage.SUBJECT_NAME_EXIST.getErrMsg());
        }
    }

    /**
     * 校验科目编码是否已存在
     *
     * @param subjectCode subjectCode
     */
    private void checkSubjectCode(String subjectCode, Long currentSubjectId, String tenantId, Long subjectSystemId) {
        SubjectE subjectE = subjectRepository.isExistSubjectCode(subjectCode, tenantId, currentSubjectId, subjectSystemId);
        if (subjectE != null) {
            throw BizException.throw400(ErrorMessage.SUBJECT_CODE_EXIST.getErrMsg());
        }
    }

    /**
     * 获取当前租户下的所有正常的科目（过滤传入的id）
     *
     * @param filterId 过滤id
     * @return List
     */
    public List<SubjectE> queryChargeItemList(Long filterId, Long categoryId, Long systemId, String tenantId) {
        List<SubjectE> filterSubjectList;
        if (filterId != null) {
            //获取过滤的费项
            filterSubjectList = subjectRepository.getFilterSubjectList(filterId, tenantId);
        } else {
            filterSubjectList = new ArrayList<>();
        }
        List<Long> filterIdList = filterSubjectList.stream().map(SubjectE::getId).collect(Collectors.toList());
        return subjectRepository.getNormalSubject(filterIdList, categoryId, systemId, tenantId);
    }


    /**
     * 导入科目
     *
     * @param file            file
     * @param subjectSystemId 科目体系id
     * @param identityInfo    identityInfo
     * @return Boolean
     */
    public Boolean importSubject(MultipartFile file, Long subjectSystemId, IdentityInfo identityInfo) {
        try {
            InputStream inputStream = file.getInputStream();
            ArrayList<SubjectData> subjectDataList = new ArrayList<>();
            //读取excel文件数据
            BaseExcelDataListener<SubjectData> baseDataListener = new BaseExcelDataListener<>(subjectDataList::addAll);
            EasyExcel.read(inputStream, SubjectData.class, baseDataListener).sheet().doRead();
            //校验编码是否存在
            List<String> codeList = subjectDataList.stream().map(SubjectData::getCode).collect(Collectors.toList());
            List<SubjectE> existSubjectList = subjectRepository.queryByCodeList(codeList, identityInfo.getTenantId());
            if (!CollectionUtils.isEmpty(existSubjectList)) {
                String msg = existSubjectList.stream().map(SubjectE::getSubjectCode).collect(Collectors.joining(","));
                throw BizException.throw400(ErrorMessage.SUBJECT_CODE_EXIST.getErrMsg() + msg);
            }

            //根据科目体系id获取科目类型
            ArrayList<SubjectE> subjectList = new ArrayList<>();
            for (SubjectData object : subjectDataList) {
                SubjectE subjectE = new SubjectE();
                subjectE.setId(IdentifierFactory.getInstance().generateLongIdentifier("subjectId"));
                subjectE.setSubjectSystemId(subjectSystemId);
                subjectE.setSubjectCode(object.getCode());
                subjectE.setSubjectName(object.getName());
                subjectE.setLeaf("是".equals(object.getLeaf()) ? 1 : 0);
                //设置类型id
                String categoryName = object.getType().trim();
                SubjectCategoryDefaultEnum categoryDefaultEnum = SubjectCategoryDefaultEnum.valueOf(categoryName);
                subjectE.setCategoryId((long) categoryDefaultEnum.getCode());

                //设置辅助核算
                ArrayList<Integer> auxiliaryCount = new ArrayList<>();
                String auxiliaryCountStr = object.getAuxiliaryCount();
                if(StringUtils.isNotEmpty(auxiliaryCountStr)){
                    String[] importAuxiliaryCounts = auxiliaryCountStr.split(",");
                    for (String importAuxiliaryCount : importAuxiliaryCounts) {
                        switch (importAuxiliaryCount.trim()) {
                            case "客商":
                                auxiliaryCount.add(1);
                                break;
                            case "项目":
                                auxiliaryCount.add(2);
                                break;
                            case "部门":
                                auxiliaryCount.add(3);
                                break;
                            case "业务类型":
                                auxiliaryCount.add(4);
                                break;
                            case "资产类别":
                                auxiliaryCount.add(5);
                                break;
                            case "银行账户":
                                auxiliaryCount.add(6);
                                break;
                            case "应收票据":
                                auxiliaryCount.add(7);
                                break;
                            case "应付票据":
                                auxiliaryCount.add(8);
                                break;
                            case "房屋辅助":
                                auxiliaryCount.add(9);
                                break;
                            case "人员档案":
                                auxiliaryCount.add(10);
                                break;
                            case "增减方式":
                                auxiliaryCount.add(11);
                                break;
                            case "收支明细项":
                                auxiliaryCount.add(12);
                                break;
                            case "普通股种类":
                                auxiliaryCount.add(13);
                                break;
                            case "存款账户性质":
                                auxiliaryCount.add(14);
                                break;
                            case "所得税率":
                                auxiliaryCount.add(15);
                                break;
                            case "城建税率":
                                auxiliaryCount.add(16);
                                break;
                            case "增值税税率":
                                auxiliaryCount.add(17);
                                break;
                            case "教育附加税率":
                                auxiliaryCount.add(18);
                                break;
                            case "地方教育附加税率":
                                auxiliaryCount.add(19);
                                break;
                            case "所得税缴纳形式":
                                auxiliaryCount.add(20);
                                break;
                            case "土增税缴纳形式":
                                auxiliaryCount.add(21);
                                break;
                            case "土增税计提形式":
                                auxiliaryCount.add(22);
                                break;
                            case "所得税计提形式":
                                auxiliaryCount.add(23);
                                break;
                            case "借款合同台账":
                                auxiliaryCount.add(24);
                                break;
                            case "应付债券台账":
                                auxiliaryCount.add(25);
                                break;
                            case "交易性金融资产台账":
                                auxiliaryCount.add(26);
                                break;
                            case "资本增减方式":
                                auxiliaryCount.add(27);
                                break;
                            case "坏账准备增减方式":
                                auxiliaryCount.add(28);
                                break;
                            case "资产减值增减方式":
                                auxiliaryCount.add(29);
                                break;
                            case "递延所得税增减原因":
                                auxiliaryCount.add(30);
                                break;
                            default:
                                break;
                        }
                    }

                }

                subjectE.setAuxiliaryCount(JSON.toJSONString(auxiliaryCount));
                subjectE.setDisabled(0);
                subjectE.setParentCode(object.getParentCode() == null ? "0" : object.getParentCode());
                subjectE.setTenantId(identityInfo.getTenantId());
                LocalDateTime now = LocalDateTime.now();
                subjectE.setCreator(identityInfo.getUserId());
                subjectE.setCreatorName(identityInfo.getUserName());
                subjectE.setGmtCreate(now);
                subjectE.setOperator(identityInfo.getUserId());
                subjectE.setOperatorName(identityInfo.getUserName());
                subjectE.setGmtModify(now);
                subjectList.add(subjectE);
            }

            //根据parentCode进行分组
            Map<String, List<SubjectE>> collect = subjectList.stream().collect(Collectors.groupingBy(SubjectE::getParentCode));
            List<SubjectE> levelOneList = collect.get("0");
            for (SubjectE subjectE : levelOneList) {
                subjectE.setParentId(0L);
                subjectE.setPath(JSON.toJSONString(Collections.singletonList(subjectE.getId())));
                setSubSubject(collect, subjectE, 1);
            }
            System.out.println(subjectList.size());
            subjectRepository.saveBatch(subjectList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 递归设置子科目的path和parentId
     *
     * @param collect        collect
     * @param currentSubject 父科目
     */
    public void setSubSubject(Map<String, List<SubjectE>> collect, SubjectE currentSubject, Integer level) {
        List<SubjectE> subjectList = collect.get(currentSubject.getSubjectCode());
        currentSubject.setLevel(level);
        if (CollectionUtils.isEmpty(subjectList)) {
            return;
        }
        for (SubjectE subjectE : subjectList) {
            int nextLevel = level + 1;
            subjectE.setLevel(nextLevel);
            subjectE.setParentId(currentSubject.getId());
            List<Long> path = JSON.parseArray(currentSubject.getPath(), Long.class);
            path.add(subjectE.getId());
            subjectE.setPath(JSON.toJSONString(path));
            setSubSubject(collect, subjectE, nextLevel);
        }
    }

    /**
     * 根据ID查科目名称
     *
     * @param id 科目id
     * @return String
     */
    public String getNameById(Long id) {
        SubjectE subjectE = subjectRepository.getById(id);
        if (subjectE != null) {
            List<Long> idList = JSON.parseArray(subjectE.getPath(), Long.class);
            if (!CollectionUtils.isEmpty(idList)) {
                List<SubjectE> parentSubjectList = subjectRepository.listByIds(idList);
                if (parentSubjectList.size() != idList.size()) {
                    return "";
                }
                StringBuilder result = new StringBuilder();
                for (Long subjectId : idList) {
                    List<SubjectE> collect = parentSubjectList.stream().filter(s -> s.getId().equals(subjectId)).collect(Collectors.toList());
                    result.append(result.length() == 0 ? collect.get(0).getSubjectName() : "-" + collect.get(0).getSubjectName());
                }
                return result.toString();
            }
            return subjectE.getSubjectName();
        }
        return "";
    }

    /**
     * 获取末级科目(最后一级科目)
     *
     * @param systemId 体系id
     * @param categoryId 科目类型id
     * @param limit
     * @return List
     */
    public List<SubjectE> getLastStage(Long systemId, Long categoryId, String name, Long subjectId, int limit) {
        return subjectRepository.getLastStage(systemId,categoryId,name,subjectId, limit);
    }

    /**
     * 根据ids查询科目详情
     * @param subjectIds
     * @return
     */
    public List<SubjectV> listByIds(List<Long> subjectIds) {
        List<SubjectE> subjectES = subjectRepository.listByIds(subjectIds);
        List<SubjectV> subjectDTOS = Global.mapperFacade.mapAsList(subjectES, SubjectV.class);
        for (int i = 0; i < subjectES.size(); i++) {
            SubjectE subjectE = subjectES.get(i);
            List<Long> subjectPath = JSON.parseArray(subjectE.getPath(), Long.class);
            List<String> auxiliaryCountList = JSON.parseArray(subjectE.getAuxiliaryCount(), String.class);
            subjectPath.remove(subjectPath.size() - 1);
            subjectDTOS.get(i).setSubjectPath(subjectPath);
            subjectDTOS.get(i).setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
        }
        return subjectDTOS;
    }

    /**
     *
     * 根据费项id查科目数据
     * @param headSubjectId 一级科目id
     * @param chargeItemId 费项id
     * @return
     */
    public SubjectV getSubjectByChargeItemIdAndHeadSubjectId(Long headSubjectId, Long chargeItemId) {
        SubjectE subject = subjectRepository.getSubjectByChargeItemIdAndHeadSubjectId(headSubjectId, chargeItemId);
        if (Objects.isNull(subject)) {
            return null;
        }
        SubjectV subjectV = Global.mapperFacade.map(subject, SubjectV.class);
        List<String> auxiliaryCountList = JSON.parseArray(subject.getAuxiliaryCount(), String.class);
        subjectV.setAuxiliaryCountList(auxiliaryCountList == null ? new ArrayList<>() : auxiliaryCountList);
        return subjectV;
    }

    /**
     * 通过科目code找
     * @param codes
     * @return
     */
    public List<SubjectE> listByCodes(List<String> codes) {
        return subjectRepository.listByCodes(codes);
    }

    /**
     * 用于更新远洋的科目对应的辅助核算
     * @param subjectES
     */
    public void batchUpdate(List<SubjectE> subjectES) {
        subjectRepository.updateBatchById(subjectES);
    }

    /**
     * 用于更新远洋的科目对应的辅助核算
     * @param searchFPageF
     * @return
     */
    public Page<SubjectD> pageForNcTask(PageF<SearchF<SubjectE>> searchFPageF) {
        QueryWrapper<SubjectE> queryWrapper = searchFPageF.getConditions().getQueryModel();
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        Page<SubjectE> page = new Page<>(searchFPageF.getPageNum(), searchFPageF.getPageSize());
        return subjectRepository.querySubjectByPage(page, queryWrapper);
    }

    /**
     * 同步科目
     * @param syncSystemSubjectCommand 同步科目信息
     * @return
     */
    public boolean syncSubject(SyncSystemSubjectCommand syncSystemSubjectCommand){

        SubjectSystemE subjectSystem = new SubjectSystemE();
        subjectSystem.setCode(syncSystemSubjectCommand.getCode());
        subjectSystem.setName(syncSystemSubjectCommand.getName());

        //获取科目体系
        SubjectSystemE oldSubjectSystem = subjectSystemRepository.getByCode(syncSystemSubjectCommand.getCode());
        if (Objects.nonNull(oldSubjectSystem)){
            subjectSystem.setId(oldSubjectSystem.getId());
        }
        //新增或更新科目体系
        subjectSystemRepository.saveOrUpdate(subjectSystem);

        List<SyncSubjectCommand> syncSubjectCommands = syncSystemSubjectCommand.getSubjects();
        List<String> subjectCodes = syncSubjectCommands.stream().map(SyncSubjectCommand::getSubjectCode).collect(Collectors.toList());
        List<SubjectE> subjects = subjectRepository.listBySubjectCodes(subjectCodes);
        Map<String, SubjectE> oldSubjectMap = new HashMap<>();
        for (SubjectE subject : subjects) {
            oldSubjectMap.put(subject.getSubjectCode(), subject);
        }
        SubjectE oldSubject = null;
        List<SubjectE> newSubjects = new ArrayList<>();
        //将科目按照编码进行排序
        syncSubjectCommands.sort(Comparator.comparing(SyncSubjectCommand::getSubjectCode));

        //父级科目信息
        SubjectE lastSubject = null;
        for (SyncSubjectCommand syncSubjectCommand : syncSubjectCommands) {
            SubjectE subject = Global.mapperFacade.map(syncSubjectCommand, SubjectE.class);
            subject.setSubjectSystemId(subjectSystem.getId());
            oldSubject = oldSubjectMap.get(subject.getSubjectCode());
            if (Objects.nonNull(oldSubject)){
                subject.setId(oldSubject.getId());
                subject.setPath(oldSubject.getPath()); //上级科目路径不设置
                subject.setParentId(oldSubject.getParentId());
                //由ncc科目为主，不需要merge
                //subject.setAuxiliaryCount(mergeAssisteItem(oldSubject.getAuxiliaryCount(), syncSubjectCommand.getAuxiliaryCount()));
                subject.setLevel(oldSubject.getLevel());
            }else {
                subject.init();
                //获取上级科目信息
                try {
                    if (Objects.nonNull(lastSubject) && subject.getSubjectCode().startsWith(lastSubject.getSubjectCode())){
                        //上一个科目为父级科目
                        JSONArray path = JSONObject.parseArray(lastSubject.getPath());
                        path.add(subject.getId());
                        subject.setPath(path.toJSONString());
                        subject.setLevel(lastSubject.getLevel() + 1);
                        subject.setParentId(lastSubject.getId());
                        subject.setParentCode(lastSubject.getSubjectCode());
                        List<String> parentCodePath = lastSubject.getCodePath();
                        if (Objects.isNull(parentCodePath)){
                            parentCodePath = new ArrayList<>();
                            parentCodePath.add(lastSubject.getSubjectCode());
                        }
                        parentCodePath.add(subject.getSubjectCode());
                        subject.setCodePath(parentCodePath);
                    }else if(Objects.nonNull(lastSubject)
                            && Objects.nonNull(lastSubject.getParentCode())
                            && subject.getSubjectCode().startsWith(lastSubject.getParentCode())){
                        //上一个科目同级科目
                        JSONArray path = JSONObject.parseArray(lastSubject.getPath());
                        path.remove(path.size()-1);
                        path.add(subject.getId());
                        subject.setLevel(lastSubject.getLevel());
                        subject.setPath(path.toJSONString());
                        subject.setParentId(lastSubject.getParentId());
                        subject.setParentCode(lastSubject.getParentCode());
                        List<String> parentCodePath = lastSubject.getCodePath();
                        parentCodePath.remove(parentCodePath.size() - 1);
                        parentCodePath.add(subject.getSubjectCode());
                        subject.setCodePath(lastSubject.getCodePath());
                    }else if(Objects.nonNull(lastSubject) && Objects.nonNull(lastSubject.getCodePath())){
                        String pc;
                        int index = lastSubject.getCodePath().size() - 1;
                        for (int i = index; i >= 0; i--) {
                            pc = lastSubject.getCodePath().get(i);
                            if (subject.getSubjectCode().startsWith(pc)){
                                subject.setLevel(i + 1);
                                JSONArray path = JSONObject.parseArray(lastSubject.getPath());
                                List<Object> curPath = path.subList(0, i + 1);
                                curPath.add(subject.getId());
                                subject.setPath(JSONObject.toJSONString(curPath));
                                subject.setParentId(Long.valueOf(path.get(i).toString()));
                                subject.setParentCode(pc);
                                List<String> parentCodePath = lastSubject.getCodePath().subList(0, i + 1);
                                parentCodePath.add(subject.getSubjectCode());
                                subject.setCodePath(parentCodePath);
                                break;
                            }
                        }
                    }
                    if (Objects.isNull(subject.getParentId())){
                        //如果本地没有上级科目，数据库存储的科目信息
                        SubjectE supSubject = subjectRepository.getSupSubjectByCode(syncSubjectCommand.getSubjectCode());
                        if (Objects.nonNull(supSubject)){
                            subject.setParentId(supSubject.getId());
                            String path = supSubject.getPath();
                            if (Objects.nonNull(path)){
                                JSONArray objects = JSONObject.parseArray(path);
                                objects.add(subject.getId());
                                path = objects.toJSONString();
                            }else {
                                path = JSON.toJSONString(List.of(subject.getId()));
                            }
                            subject.setPath(path);
                            subject.setLevel(supSubject.getLevel() + 1);
                            subject.setParentCode(supSubject.getSubjectCode());
                            List<String> codePath = new ArrayList<>();
                            codePath.add(supSubject.getSubjectCode());
                            codePath.add(subject.getSubjectCode());
                            subject.setCodePath(codePath);
                        }else {
                            subject.setPath(JSON.toJSONString(List.of(subject.getId())));
                            subject.setLevel(1);
                            subject.setParentCode("");
                            subject.setParentId(0L);
                            List<String> parentCodePath  = new ArrayList<>();
                            parentCodePath.add(subject.getSubjectCode());
                            subject.setCodePath(parentCodePath);
                        }
                    }
                }catch (Exception e){
                    log.error("上次科目：" + lastSubject.getSubjectCode() + " | " + lastSubject.getParentCode() + "当前科目：" + subject.getSubjectCode());
                    throw new RuntimeException(e);
                }
            }
            //自己作为上级科目
            newSubjects.add(subject);
            lastSubject = subject;
        }
        return subjectRepository.saveOrUpdateBatch(newSubjects);
    }

    /**
     * 合并辅助核算
     * @param as1 辅助核算JSON字符串1
     * @param as2 辅助核算JSON字符串2
     * @return 合并后的JSON字符串
     */
    private String mergeAssisteItem(String as1, String as2){
        if(StringUtils.isBlank(as1)){
            return as2;
        }
        if(StringUtils.isBlank(as2)){
            return as1;
        }
        List<String> list1 = JSONObject.parseArray(as1, String.class);
        List<String> list2 = JSONObject.parseArray(as2, String.class);
        list1.stream().filter(i -> !list2.contains(i)).forEach(item -> list2.add(item));
        return JSON.toJSONString(list2);
    }

    public List<SubjectE> getSubjectByCodes(List<String> subjectCodes) {
        return subjectRepository.listByCodes(subjectCodes);
    }

    /**
     * 根据科目编码查询科目全部信息（包含全部路径名称）
     * @param subjectCodes 科目编码
     * @return 科目信息
     */
    public List<SubjectE> getSubjectsFullByCodes(List<String> subjectCodes) {
        return subjectRepository.listFullByCodes(subjectCodes);
    }


    public SubjectE getBySubjectLastId(Long subjectLastId) {
        return subjectRepository.getById(subjectLastId);
    }

    public SubjectE getByCode(String code) {
        return subjectRepository.getByCode(code);
    }

    public List<String> getAllAuxiliary(Long subjectId) {
        List<String> auxiliaries = subjectRepository.getAllAuxiliary(subjectId);
        Set<String> auxiliarySet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(auxiliaries)) {
            auxiliaries.forEach(allAuxiliary -> {
                if (StringUtils.isNotBlank(allAuxiliary) && !"null".equals(allAuxiliary)) {
                    List<String> parsedArray = JSONArray.parseArray(allAuxiliary, String.class);
                    auxiliarySet.addAll(parsedArray);
                }
            });
        }
        return new ArrayList<>(auxiliarySet);
    }
}
