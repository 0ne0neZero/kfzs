package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrg;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteOrgMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 辅助核算（业务单元）
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
public class AssisteOrgRepository extends ServiceImpl<AssisteOrgMapper, AssisteOrg> {

    /**
     * 批量保存或更新
     * @param assisteOrgs
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<AssisteOrg> assisteOrgs) {
        return baseMapper.saveOrUpdateBatch(assisteOrgs) > 0;
    }
    /**
     * 查询辅助核算（业务单元）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（业务单元）列表
     */
    public List<AssisteItemOBV> getAssisteItems(String name, String code){
        return baseMapper.selectAssisteItems(name, code);
    }
}
