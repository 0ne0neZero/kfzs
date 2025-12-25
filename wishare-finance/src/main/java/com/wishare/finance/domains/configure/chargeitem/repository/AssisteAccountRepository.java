package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.fo.AssisteAccountF;
import com.wishare.finance.domains.configure.chargeitem.entity.AssisteAccountE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.AssisteAccountMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Service
public class AssisteAccountRepository extends ServiceImpl<AssisteAccountMapper, AssisteAccountE> {

    @Autowired
    private AssisteAccountMapper assisteAccountMapper;

    /**
     * 分页获取辅助核算
     *
     * @param form
     * @return
     */
    public Page<AssisteAccountE> queryPage(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        return assisteAccountMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }

    /**
     * 获取辅助核算列表
     *
     * @param form
     * @return
     */
    public List<AssisteAccountE> assisteAccountList(AssisteAccountF form) {
        LambdaQueryWrapper<AssisteAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(form.getAsAcCode()), AssisteAccountE::getAsAcCode, form.getAsAcCode());
        wrapper.like(StringUtils.isNotBlank(form.getAsAcItem()), AssisteAccountE::getAsAcItem, form.getAsAcItem());
        wrapper.like(StringUtils.isNotBlank(form.getAsAcTarget()), AssisteAccountE::getAsAcTarget, form.getAsAcTarget());
        wrapper.like(StringUtils.isNotBlank(form.getReferenceName()), AssisteAccountE::getReferenceName, form.getReferenceName());
        return assisteAccountMapper.selectList(wrapper);
    }

    /**
     * 根据code获取辅助核算列表
     *
     * @param auxiliaryCountList
     * @return
     */
    public List<AssisteAccountE> assisteAccountListByCodes(List<String> auxiliaryCountList) {
        if (CollectionUtils.isEmpty(auxiliaryCountList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AssisteAccountE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AssisteAccountE::getAsAcCode, auxiliaryCountList);
        return assisteAccountMapper.selectList(wrapper);
    }

    /**
     * 导出辅助核算查询
     *
     * @param form
     * @return
     */
    public List<AssisteAccountE> exportList(PageF<SearchF<?>> form) {
        SearchF<?> conditions = form.getConditions();
        QueryWrapper<?> queryModel = conditions.getQueryModel();
        return assisteAccountMapper.exportList(queryModel);
    }


}
