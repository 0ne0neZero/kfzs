package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrgDept;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteOrgDeptMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 辅助核算（部门）
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
public class AssisteOrgDeptRepository extends ServiceImpl<AssisteOrgDeptMapper, AssisteOrgDept> {


    /**
     * 批量新增或修改
     * @param assisteOrgDepts
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<AssisteOrgDept> assisteOrgDepts){
        return baseMapper.saveOrUpdateBatch(assisteOrgDepts) > 0;
    }

    public List<AssisteItemOBV> getAssisteItems(String name, String code, String orgCode){
        return baseMapper.selectAssisteItems(name, code, orgCode);
    }

}
