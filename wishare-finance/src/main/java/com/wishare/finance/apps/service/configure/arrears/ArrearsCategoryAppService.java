package com.wishare.finance.apps.service.configure.arrears;

import cn.hutool.core.collection.CollectionUtil;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.UpdateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryLastLevelV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.SearchV;
import com.wishare.finance.domains.configure.arrears.service.ArrearsCategoryDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsCategoryAppService implements ApiBase {

    private final ArrearsCategoryDomainService arrearsCategoryDomainService;

    /**
     * 新增欠费类别
     */
    public Long create(CreateArrearsCategoryF createArrearsCategoryF) {
        return arrearsCategoryDomainService.create(createArrearsCategoryF).getId();
    }

    public Boolean update(UpdateArrearsCategoryF updateArrearsCategoryF) {
        return arrearsCategoryDomainService.update(updateArrearsCategoryF);
    }

    /**
     * 启用/禁用费项
     *
     * @param id id
     * @return Boolean
     */
    public Boolean updateStatus(Long id,int status) {
        return arrearsCategoryDomainService.updateStatus(id,status);
    }

    public Boolean delete(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                arrearsCategoryDomainService.delete(id);
            }
        }
        return true;
    }


    /**
     * 分页查询所有欠费类别
     */
    public PageV<ArrearsCategoryV> queryAllByPage(PageF<SearchF<?>> searchF) {
        return arrearsCategoryDomainService.queryAllByPage(searchF);
    }

    /**
     * 查询所有末级欠费类型
     */
    public List<ArrearsCategoryLastLevelV> queryAllLastLevel() {
        return arrearsCategoryDomainService.queryAllLastLevel();
    }

    /**
     * 根据id查询欠费类型信息
     */
    public ArrearsCategoryV getById(Long id) {
        return Global.mapperFacade.map(arrearsCategoryDomainService.getById(id), ArrearsCategoryV.class);
    }

    /**
     * 查询所有非末级欠费类型
     */
    public List<ArrearsCategoryV> queryAllNotLastLevel() {
        return Global.mapperFacade.mapAsList(arrearsCategoryDomainService.queryAllNotLastLevel(), ArrearsCategoryV.class);
    }

    public List<SearchV> searchComponent(String searchKey) {
        return arrearsCategoryDomainService.searchComponent(searchKey);
    }
}
