package com.wishare.contract.domains.service.contractset;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.domains.consts.ContractEnum;
import com.wishare.contract.domains.consts.ErrMsgEnum;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import com.wishare.contract.domains.mapper.contractset.ContractTemplateRepository;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractTemplateDomainsService {

    private final ContractTemplateRepository contractTemplateRepository;

    private final FileStorageUtils fileStorageUtils;

    /**
     * 创建合同范本
     */
    public Long create(CreateContractTemplateF createContractTemplateF, IdentityInfo identityInfo) {
        String name = createContractTemplateF.getName();
        String tenantId = identityInfo.getTenantId();
        Long parentId = createContractTemplateF.getParentId();
        ContractTemplateE templateE = Global.mapperFacade.map(createContractTemplateF, ContractTemplateE.class);
        // 范本名称唯一性校验
        List<ContractTemplateE> contractTemplateES = contractTemplateRepository.queryByName(name, tenantId);
        if (Objects.nonNull(contractTemplateES) && !contractTemplateES.isEmpty()) {
            // 获取是否有启用状态的范本，若有，不允许创建
            List<ContractTemplateE> collect = contractTemplateES.stream().filter(
                    template -> template.getStatus() == ContractEnum.ContractTemplateStatus.ENABLE.getValue()).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                return 0L;
//                throw BizException.throw400(ErrMsgEnum.CONTRACT_TEMPLATE_NAME_EXIST.getErrMsg());
            } else {
                // 更新版本
                BigDecimal maxVersion = contractTemplateES.stream().map(ContractTemplateE::getVersion).max(BigDecimal::compareTo).get();
                templateE.setVersion(maxVersion.add(new BigDecimal("1.0")));
            }
        }
        long id = UidHelper.nextId("contractTemplateId");
        // 文件改为正式文件
        String fileKey = fileStorageUtils.handleFileOV(null, createContractTemplateF.getFile(), tenantId, String.valueOf(id), ContractTemplateE.class);
        LocalDateTime now = LocalDateTime.now();
        templateE.setId(id);
        templateE.setFileUrl(fileKey);
        templateE.setFileName(createContractTemplateF.getFile().getName());
        templateE.setTenantId(tenantId);
        templateE.setCreator(identityInfo.getUserId());
        templateE.setCreatorName(identityInfo.getUserName());
        templateE.setGmtCreate(now);
        templateE.setOperator(identityInfo.getUserId());
        templateE.setOperatorName(identityInfo.getUserName());
        templateE.setGmtModify(now);
        // 设置path
        ArrayList<Long> pathList = new ArrayList<>();
        pathList.add(id);
        templateE.setPath(JSON.toJSONString(pathList));
        contractTemplateRepository.save(templateE);
        // 更新子级parentId、path
        List<Long> childrenIdList = contractTemplateES.stream().map(ContractTemplateE::getId).collect(Collectors.toList());
        if (!childrenIdList.isEmpty()) {
            updateChildren(childrenIdList, id);
        }
        return id;
    }

    /**
     * 分页查询
     */
    public PageV<ContractTemplateTreeV> queryByPage(PageF<SearchF<PageContractTemplateF>> form, String tenantId) {
        /**
         * 合同分类条件特殊处理
         */
        List<Field> fields = form.getConditions().getFields();
        List<Field> fieldList = fields.stream().filter(s -> "ct.categoryId".equals(s.getName())).collect(Collectors.toList());
        if (!fieldList.isEmpty()) {
            List<Long> values = (List<Long>) fieldList.get(0).getValue();
            if (!values.isEmpty()) {
                fieldList.get(0).setMethod(15);
            }
        }

        Page<PageContractTemplateF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<PageContractTemplateF> queryModel = form.getConditions().getQueryModel();
        queryModel.eq("ct.deleted", 0);
        // 查最上级
        IPage<ContractTemplateTreeV> pageList = contractTemplateRepository.queryByPage(pageF, queryModel, tenantId);
        List<ContractTemplateTreeV> records = pageList.getRecords();
        // 查所有子级
        List<Long> parentIdList = records.stream().map(ContractTemplateTreeV::getId).collect(Collectors.toList());
        List<ContractTemplateTreeV> contractTemplateTreeVS = contractTemplateRepository.queryByPath(queryModel, parentIdList, tenantId);
        // 做成树结构
        List<ContractTemplateTreeV> treeing = TreeUtil.treeing(contractTemplateTreeVS);
        return PageV.of(form, pageList.getTotal(), treeing);
    }

    /**
     * id查询范本
     */
    public ContractTemplateE queryById(Long id) {
        return contractTemplateRepository.queryById(id);
    }

    /**
     * 变更范本
     */
    public Long changeContractTemplate(ChangeContractTemplateF changeContractTemplateF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        String name = changeContractTemplateF.getName();
        Long originalId = changeContractTemplateF.getId();
        String tenantId = identityInfo.getTenantId();
        List<ContractTemplateE> contractTemplateES = contractTemplateRepository.queryByName(name, tenantId);
        // 判断是否存在草稿状态范本
        List<ContractTemplateE> draftTemplate = contractTemplateES.stream().
                filter(t -> t.getStatus().equals(ContractEnum.ContractTemplateStatus.DRAFT.getValue())).collect(Collectors.toList());
        if (!draftTemplate.isEmpty()) {
            throw BizException.throw400(ErrMsgEnum.CONTRACT_TEMPLATE_CHANGING.getErrMsg());
        }
        long id = UidHelper.nextId("contractTemplateId");
        ContractTemplateE templateE = Global.mapperFacade.map(changeContractTemplateF, ContractTemplateE.class);
        // 是否变更文件
        if (changeContractTemplateF.getChangeFile() == 0) {
            templateE.setFileName(changeContractTemplateF.getFileName());
            templateE.setFileUrl(changeContractTemplateF.getFileUrl());
        } else {
            // 变更了需改为正式文件
            FileVo file = changeContractTemplateF.getFile();
            String fileKey = fileStorageUtils.handleFileOV(null, file, tenantId, String.valueOf(id), ContractTemplateE.class);
            templateE.setFileUrl(fileKey);
            templateE.setFileName(file.getName());
        }
        // 范本版本升级
        if (!contractTemplateES.isEmpty()) {
            BigDecimal maxVersion = contractTemplateES.stream().map(ContractTemplateE::getVersion).max(BigDecimal::compareTo).get();
            templateE.setVersion(maxVersion.add(new BigDecimal("1.0")));
        }
        // 状态判断
        if (changeContractTemplateF.getStatus() == ContractEnum.ContractTemplateStatus.ENABLE.getValue()) {
            // 启用需更新其他同名版本范本，更新parentId、path
            List<Long> childrenIdList = contractTemplateES.stream().map(ContractTemplateE::getId).collect(Collectors.toList());
            if (!childrenIdList.isEmpty()) {
                updateChildren(childrenIdList, id);
            }
            // 设置自身path
            ArrayList<Long> pathList = new ArrayList<>();
            pathList.add(id);
            templateE.setPath(JSON.toJSONString(pathList));
            // 禁用原范本
            contractTemplateRepository.updateStatusById(originalId, ContractEnum.ContractTemplateStatus.DISABLE.getValue());
        } else {
            // 其他状态
            // 判断当前是否有启用的范本
            List<ContractTemplateE> enableTemplateList = contractTemplateES.stream().filter(template ->
                    ContractEnum.ContractTemplateStatus.ENABLE.getValue() == template.getStatus()).collect(Collectors.toList());
            if (!enableTemplateList.isEmpty()) {
                // 有启用范本当前范本当做子范本
                ArrayList<Long> childrenPathList = new ArrayList<>();
                childrenPathList.add(originalId);
                childrenPathList.add(id);
                // 设置自身parentId、path
                templateE.setParentId(originalId);
                templateE.setPath(JSON.toJSONString(childrenPathList));
            } else {
                // 无启用范本当前范本当做父范本
                // 需更新其他同名版本范本，更新parentId、path
                List<Long> childrenIdList = contractTemplateES.stream().map(ContractTemplateE::getId).collect(Collectors.toList());
                if (!childrenIdList.isEmpty()) {
                    updateChildren(childrenIdList, id);
                }
                // 设置自身path
                ArrayList<Long> pathList = new ArrayList<>();
                pathList.add(id);
                templateE.setPath(JSON.toJSONString(pathList));
            }
        }
        templateE.setId(id);
        templateE.setTenantId(tenantId);
        templateE.setCreator(identityInfo.getUserId());
        templateE.setCreatorName(identityInfo.getUserName());
        templateE.setGmtCreate(now);
        templateE.setOperator(identityInfo.getUserId());
        templateE.setOperatorName(identityInfo.getUserName());
        templateE.setGmtModify(now);
        contractTemplateRepository.save(templateE);
        return id;
    }

    /**
     * id删除
     */
    public Boolean deleteById(Long id, IdentityInfo identityInfo) {
        String tenantId = identityInfo.getTenantId();
        ContractTemplateE contractTemplateE = contractTemplateRepository.queryById(id);
        if (Objects.nonNull(contractTemplateE)) {
            // 引用状态判断
            if (contractTemplateE.getUseStatus() == 1) {
                throw BizException.throw400(ErrMsgEnum.CONTRACT_TEMPLATE_IN_USE.getErrMsg());
            }
            // 说明是父级，删除前先设置新父级范本、需更新子级parentId、path
            if (contractTemplateE.getParentId() == 0) {
                String name = contractTemplateE.getName();
                List<ContractTemplateE> contractTemplateES = contractTemplateRepository.queryByName(name, tenantId);
                // 所有子级
                List<ContractTemplateE> allChildren = contractTemplateES.stream().filter(t -> !t.getId().equals(id)).collect(Collectors.toList());
                if (!allChildren.isEmpty()) {
                    // 更新版本最高的子级为父级
                    ContractTemplateE maxChildren = allChildren.stream().max(Comparator.comparing(ContractTemplateE::getVersion)).get();
                    Long maxChildrenId = maxChildren.getId();
                    // 除最大子级外的子级
                    List<Long> childrenList = allChildren.stream().filter(t -> !t.getId().equals(maxChildrenId)).map(ContractTemplateE::getId).collect(Collectors.toList());
                    // 设置子级parentId、path
                    if (!childrenList.isEmpty()) {
                        updateChildren(childrenList, maxChildrenId);
                    }
                    // 设置父级parentId、path
                    updateParent(maxChildrenId);
                }
            }
            return contractTemplateRepository.removeById(id);
        }
        return false;
    }

    /**
     * 编辑范本
     */
    public Boolean editorContractTemplate(EditorContractTemplateF contractTemplateF, IdentityInfo identityInfo) {
        String tenantId = identityInfo.getTenantId();
        LocalDateTime now = LocalDateTime.now();
        String name = contractTemplateF.getName();
        Long id = contractTemplateF.getId();
        ContractTemplateE templateE = Global.mapperFacade.map(contractTemplateF, ContractTemplateE.class);
        // 是否变更文件
        if (contractTemplateF.getChangeFile() == 0) {
            templateE.setFileName(contractTemplateF.getFileName());
            templateE.setFileUrl(contractTemplateF.getFileUrl());
        } else {
            // 变更了需改为正式文件
            FileVo file = contractTemplateF.getFile();
            String fileKey = fileStorageUtils.handleFileOV(null, file, tenantId, String.valueOf(id), ContractTemplateE.class);
            templateE.setFileUrl(fileKey);
            templateE.setFileName(file.getName());
        }
        templateE.setOperator(identityInfo.getUserId());
        templateE.setOperatorName(identityInfo.getUserName());
        templateE.setGmtModify(now);
        if (contractTemplateRepository.updateById(templateE)) {
            // 启用状态需更改父子级关系
            if (contractTemplateF.getStatus() == ContractEnum.ContractTemplateStatus.ENABLE.getValue()) {
                List<ContractTemplateE> contractTemplateES = contractTemplateRepository.queryByName(name, tenantId);
                List<Long> childrenList = contractTemplateES.stream().filter(t -> !t.getId().equals(id)).map(ContractTemplateE::getId).collect(Collectors.toList());
                if (!childrenList.isEmpty()) {
                    updateChildren(childrenList, id);
                }
                // 禁用父级范本
                contractTemplateRepository.updateStatusById(contractTemplateF.getParentId(), ContractEnum.ContractTemplateStatus.DISABLE.getValue());
                // 启用当前范本
                contractTemplateRepository.updateStatusById(contractTemplateF.getId(), ContractEnum.ContractTemplateStatus.ENABLE.getValue());
                // 更新当前范本parentId、path
                updateParent(contractTemplateF.getId());
            }
            return true;
        }
        return false;
    }

    /**
     * 启用/禁用范本
     */
    public Boolean updateContractTemplateStatus(UpdateContractTemplateF updateContractTemplateF, IdentityInfo identityInfo) {
        Long id = updateContractTemplateF.getId();
        LocalDateTime now = LocalDateTime.now();
        ContractTemplateE contractTemplateE = contractTemplateRepository.queryById(id);
        if (updateContractTemplateF.getStatus() == ContractEnum.ContractTemplateStatus.ENABLE.getValue()) {
            // 启用
            List<Long> otherTemplateIdList = contractTemplateRepository.queryByName(contractTemplateE.getName(), identityInfo.getTenantId()).
                    stream().filter(t -> !t.getId().equals(id)).map(ContractTemplateE::getId).collect(Collectors.toList());
            // 禁用父级范本
            contractTemplateRepository.updateStatusById(contractTemplateE.getParentId(), ContractEnum.ContractTemplateStatus.DISABLE.getValue());
            // 启用当前范本
            contractTemplateRepository.updateStatusById(id, ContractEnum.ContractTemplateStatus.ENABLE.getValue());
            // 更新时间
            contractTemplateRepository.updateOperator(id, now, identityInfo.getUserId(), identityInfo.getUserName());
            // 更新当前范本parentId、path
            updateParent(id);
            // 把其他同名范本设为子级
            if (!otherTemplateIdList.isEmpty()) {
                updateChildren(otherTemplateIdList, id);
            }
            return true;
        } else if (updateContractTemplateF.getStatus() == ContractEnum.ContractTemplateStatus.DISABLE.getValue()) {
            // 禁用
            // 禁用当前范本
            contractTemplateRepository.updateStatusById(contractTemplateE.getId(), ContractEnum.ContractTemplateStatus.DISABLE.getValue());
            List<ContractTemplateE> templateList = contractTemplateRepository.queryByName(contractTemplateE.getName(), identityInfo.getTenantId());
            // 更新时间
            contractTemplateRepository.updateOperator(id, now, identityInfo.getUserId(), identityInfo.getUserName());
            // 取出版本最高的设为父级
            ContractTemplateE maxTemplate = templateList.stream().max(Comparator.comparing(ContractTemplateE::getVersion)).get();
            updateParent(maxTemplate.getId());
            // 其他同名范本为子级
            List<Long> childrenIdList = templateList.stream().
                    filter(t -> !t.getId().equals(maxTemplate.getId())).map(ContractTemplateE::getId).collect(Collectors.toList());
            if (!childrenIdList.isEmpty()) {
                updateChildren(childrenIdList, maxTemplate.getId());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询范本集
     */
    public List<ContractTemplateE> query(ContractTemplateF contractTemplateF, IdentityInfo identityInfo) {
        return contractTemplateRepository.query(contractTemplateF, identityInfo.getTenantId());
    }

    /**
     * 更新父级parentId、path
     */
    private void updateParent(Long parentId) {
        List<Long> idPath = new ArrayList<>();
        idPath.add(parentId);
        contractTemplateRepository.updateParentIdPathById(parentId, 0L, JSON.toJSONString(idPath));
    }

    /**
     * 更新子级parentId、path
     */
    private void updateChildren(List<Long> childrenIdList, Long parentId) {
        if (!childrenIdList.isEmpty()) {
            // 设置子级id
            contractTemplateRepository.updateParentIdByIdList(childrenIdList, parentId);
            // 设置子级path
            childrenIdList.forEach(item -> {
                ArrayList<Long> childrenPathList = new ArrayList<>();
                childrenPathList.add(parentId);
                childrenPathList.add(item);
                contractTemplateRepository.updatePathById(item, JSON.toJSONString(childrenPathList));
            });
        }
    }
}
