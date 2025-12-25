package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrg;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherSchemeOrgMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 凭证核算方案财务组织关联表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Service
public class VoucherSchemeOrgRepository extends ServiceImpl<VoucherSchemeOrgMapper, VoucherSchemeOrg> {


    /**
     * 新增、删除、或修改更新组织
     * @param orgs 组织信息
     * @return 结果
     */
    public boolean sudOrg(List<VoucherSchemeOrg> orgs){
        //获取已配置的数据
        Long voucherSchemeId = orgs.get(0).getVoucherSchemeId();
        List<VoucherSchemeOrg> voucherSchemeOrgs = listBySchemeId(voucherSchemeId);
        Map<Long, VoucherSchemeOrg> schemeOrgMap = new HashMap<>();
        for (VoucherSchemeOrg voucherSchemeOrg : voucherSchemeOrgs) {
            schemeOrgMap.put(voucherSchemeOrg.getOrgId(), voucherSchemeOrg);
        }
        List<VoucherSchemeOrg> saveOrUpdateOrgs = new ArrayList<>();
        for (VoucherSchemeOrg schemeOrg : orgs) {
            if (schemeOrgMap.containsKey(schemeOrg.getOrgId())){
                schemeOrg.setId(schemeOrgMap.get(schemeOrg.getOrgId()).getId());
                schemeOrgMap.remove(schemeOrg.getOrgId());
            }
            saveOrUpdateOrgs.add(schemeOrg);
        }
        boolean result = saveOrUpdateBatch(saveOrUpdateOrgs);
        if (!schemeOrgMap.isEmpty()){
            remove(new LambdaUpdateWrapper<VoucherSchemeOrg>().eq(VoucherSchemeOrg::getVoucherSchemeId, voucherSchemeId)
                    .in(VoucherSchemeOrg::getOrgId, schemeOrgMap.values().stream().map(VoucherSchemeOrg::getOrgId).collect(Collectors.toList())));
        }
        return result;
    }


    public boolean removeBySchemeId(Long voucherSchemeId){
        return remove(new LambdaUpdateWrapper<VoucherSchemeOrg>().eq(VoucherSchemeOrg::getVoucherSchemeId, voucherSchemeId));
    }


    public Page<VoucherSchemeOrg> getPage(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchFPageF), RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public List<VoucherSchemeOrg> listBySchemeId(Long voucherSchemeId) {
        return list(new LambdaQueryWrapper<VoucherSchemeOrg>().eq(VoucherSchemeOrg::getVoucherSchemeId, voucherSchemeId));
    }

    public List<VoucherSchemeOrg> listBySchemeIds(List<Long> voucherSchemeIds) {
        return CollectionUtils.isEmpty(voucherSchemeIds) ? new ArrayList<>() :
                list(new LambdaQueryWrapper<VoucherSchemeOrg>()
                .in(VoucherSchemeOrg::getVoucherSchemeId, voucherSchemeIds)
                .eq(VoucherSchemeOrg::getDeleted, DataDeletedEnum.NORMAL.getCode()));
    }

    public List<VoucherScheme> schemeListByOrgIds(List<Long> orgIds){
        return baseMapper.schemeListByOrgIds(orgIds);
    }

}
