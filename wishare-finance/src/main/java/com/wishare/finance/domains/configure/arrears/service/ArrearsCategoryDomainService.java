package com.wishare.finance.domains.configure.arrears.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.UpdateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryLastLevelV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.SearchV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.TreeE;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsCategoryE;
import com.wishare.finance.domains.configure.arrears.repository.ArrearsCategoryRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费项service
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsCategoryDomainService {

    private final ArrearsCategoryRepository arrearsCategoryRepository;

    public ArrearsCategoryE create(CreateArrearsCategoryF createArrearsCategoryF) {
        //校验只能三级
        verifyLevel(createArrearsCategoryF.getParentId());
        //校验名称
        verifyName(null,createArrearsCategoryF.getArrearsCategoryName());
        ArrearsCategoryE arrearsCategoryE = new ArrearsCategoryE();
        BeanUtils.copyProperties(createArrearsCategoryF,arrearsCategoryE);
        arrearsCategoryE.setCreator(ThreadLocalUtil.curIdentityInfo().getUserId());
        arrearsCategoryE.setGmtCreate(LocalDateTime.now());
        arrearsCategoryE.setCreatorName(ThreadLocalUtil.curIdentityInfo().getUserName());
        arrearsCategoryRepository.save(arrearsCategoryE);
        return arrearsCategoryE;
    }

    private void verifyLevel(Long id) {
        if (ObjectUtil.isNull(id)){
            return;
        }
        QueryWrapper<ArrearsCategoryE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("deleted",0);
        ArrearsCategoryE one = arrearsCategoryRepository.getOne(queryWrapper);
        if (ObjectUtil.isNull(one)){
            throw BizException.throw400("父级类别不存在或已删除！");
        }
        if (ObjectUtil.equals(one.getLastLevel(),1)){
            throw BizException.throw400("末级类别不允许新增子类别！");
        }
        if (one.getParentId() == null){
            return;
        }
        QueryWrapper<ArrearsCategoryE> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("id",one.getParentId()).eq("deleted",0);
        ArrearsCategoryE two = arrearsCategoryRepository.getOne(queryWrapper2);
        if (ObjectUtil.isNull(two) && one.getParentId() != null){
            throw BizException.throw400("请检查上级类别是否存在！");
        }
        QueryWrapper<ArrearsCategoryE> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("id",two.getParentId()).eq("deleted",0);
        ArrearsCategoryE three = arrearsCategoryRepository.getOne(queryWrapper3);
        if (ObjectUtil.isNotNull(three)){
            throw BizException.throw400("层级最大为3级！");
        }
    }


    @Transactional
    public Boolean update(UpdateArrearsCategoryF form) {
        //校验名称
        verifyName(form.getId(),form.getArrearsCategoryName());

        ArrearsCategoryE byId = arrearsCategoryRepository.getById(form.getId());
        //校验是否为末级,若不为末级,且有子项,那么不允许修改为末级
        if (ObjectUtil.equals(byId.getLastLevel(),0) && ObjectUtil.equals(form.getLastLevel(),1)){
            if (arrearsCategoryRepository.getBaseMapper().exists(new QueryWrapper<ArrearsCategoryE>()
                    .eq("parent_id", form.getId())
                    .eq("deleted", 0))){
                throw BizException.throw400("该欠费类别存在子项,不允许修改为末级");
            }
        }

        ArrearsCategoryE arrearsCategoryE = new ArrearsCategoryE();
        BeanUtils.copyProperties(form, arrearsCategoryE);
        arrearsCategoryRepository.updateById(arrearsCategoryE);

        //若名称修改,则修改所有的子费项的父级名称
        if (!ObjectUtil.equals(byId.getArrearsCategoryName(),form.getArrearsCategoryName())) {
            QueryWrapper<ArrearsCategoryE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", byId.getId())
                    .eq("deleted", 0)
                    .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId());
            ArrearsCategoryE updateCategoryE = new ArrearsCategoryE();
            updateCategoryE.setParentName(form.getArrearsCategoryName());
            arrearsCategoryRepository.getBaseMapper().update(updateCategoryE, queryWrapper);
        }

        return true;
    }

    private void verifyName(Long id , String arrearsCategoryName) {
        QueryWrapper<ArrearsCategoryE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("arrears_category_name", arrearsCategoryName)
                .eq("deleted", 0)
                .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId())
                .ne(ObjectUtil.isNotNull(id), "id", id);
        if (arrearsCategoryRepository.getBaseMapper().exists(queryWrapper)){
            throw BizException.throw400("该欠费类别名称已存在");
        }
    }

    /**
     * 启用或禁用欠费类别
     */
    @Transactional
    public Boolean updateStatus(Long id, int status) {
        ArrearsCategoryE arrearsCategoryE = arrearsCategoryRepository.getById(id);
        if (ObjectUtil.isNull(arrearsCategoryE)){
            throw BizException.throw301("资源存在变更,请刷新后重试！");
        }
        arrearsCategoryE.setStatus(status);
        return recursionUpdate(arrearsCategoryE,2);

    }

    /**
     * 删除欠费类别
     */
    public Boolean delete(Long id) {
        ArrearsCategoryE arrearsCategoryE = arrearsCategoryRepository.getById(id);
        if (ObjectUtil.isNull(arrearsCategoryE)){
            return true;
        }
        arrearsCategoryE.setDeleted(1);
        return recursionUpdate(arrearsCategoryE,1);
    }

    /**
     * 分页查询欠费类别
     */
    public PageV<ArrearsCategoryV> queryAllByPage(PageF<SearchF<?>> searchF) {
        Page<ArrearsCategoryE> page = Page.of(searchF.getPageNum(), searchF.getPageSize());
        QueryWrapper<ArrearsCategoryE> queryModel = searchF.getConditions().getQueryModel(ArrearsCategoryE.class);
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode())
                .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId())
                .orderByDesc("gmt_modify")
                .orderByAsc("id");
        Page<ArrearsCategoryE> queryByPage = arrearsCategoryRepository.page(page, queryModel);

        return PageV.of(searchF,queryByPage.getTotal(),Global.mapperFacade.mapAsList(queryByPage.getRecords(), ArrearsCategoryV.class));
    }

    /**
     * 查询所有启用状态的欠费类别
     */
    public List<ArrearsCategoryLastLevelV> queryAllLastLevel() {

        QueryWrapper<ArrearsCategoryE> queryModel = new QueryWrapper<>();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode())
                .eq("status", 1)
                .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId()).orderByDesc("id");
        List<ArrearsCategoryE> list = arrearsCategoryRepository.list(queryModel);
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        List<ArrearsCategoryLastLevelV> allList = Global.mapperFacade.mapAsList(list, ArrearsCategoryLastLevelV.class);
        //循环来做成树状结构
        List<ArrearsCategoryLastLevelV> oneList = allList.stream().filter(e -> ObjectUtil.isNull(e.getParentId())).collect(Collectors.toList());
        return doTreeDate(oneList,allList);
    }


    private <T extends TreeE> List<T> doTreeDate(List<T> parentList, List<T> allList) {
        if (CollectionUtils.isEmpty(parentList)|| CollectionUtils.isEmpty(allList)){
            return parentList;
        }
        for (T t : parentList) {
            List<T> childList = allList.stream().filter(e -> ObjectUtil.equals(e.getParentId(), t.getId())).collect(Collectors.toList());
            t.setTList(childList);
            if (CollectionUtils.isNotEmpty(childList)){
                doTreeDate(childList,allList);
            }
        }
        return parentList;

    }


    /**
     * 查询所有非末级欠费类别
     */
    public List<ArrearsCategoryE> queryAllNotLastLevel() {
        QueryWrapper<ArrearsCategoryE> queryModel = new QueryWrapper<>();
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode())
                .eq("last_level",0)
                .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId()).orderByDesc("id");
        return arrearsCategoryRepository.list(queryModel);
    }


    /**
     * 根据ID查欠费类别
     */
    public ArrearsCategoryE getById(Long id) {
        return arrearsCategoryRepository.getById(id);
    }

    /**
     * 递归修改启用状态或删除状态
     * code 1 删除 2修改启用/禁用状态
     */
    private boolean recursionUpdate(ArrearsCategoryE arrearsCategoryE,int code) {
        if (ObjectUtil.isNull(arrearsCategoryE) || ObjectUtil.isNull(arrearsCategoryE.getId())){
            throw BizException.throw301("资源存在变更,请刷新后重试！");
        }
        //修改本身,--这里分开用两个方法是因为updateById这个方法不会去修改deleted这个字段...
        if (code == 1){
            arrearsCategoryRepository.removeBatchByIds(List.of(arrearsCategoryE.getId()));
        }else {
            arrearsCategoryRepository.updateById(arrearsCategoryE);
        }

        //修改子项
        Long parentId = arrearsCategoryE.getId();
        String tenantId = arrearsCategoryE.getTenantId();
        //递归修改所有的子项的启用/禁用状态或删除状态
        QueryWrapper<ArrearsCategoryE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId)
                .eq("tenant_id",tenantId)
                .eq("deleted",0);
        List<ArrearsCategoryE> categoryEList = arrearsCategoryRepository.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(categoryEList)){
            for (ArrearsCategoryE categoryE : categoryEList) {
                categoryE.setStatus(arrearsCategoryE.getStatus());
                categoryE.setDeleted(arrearsCategoryE.getDeleted());
                recursionUpdate(categoryE,code);
            }
        }
        return true;
    }

    public List<SearchV> searchComponent(String searchKey) {
        return arrearsCategoryRepository.searchComponent(searchKey);
    }
}
