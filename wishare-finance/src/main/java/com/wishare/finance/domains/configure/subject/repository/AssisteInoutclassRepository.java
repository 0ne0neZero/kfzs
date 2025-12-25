package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.AssisteInoutclass;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteInoutclassMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 辅助核算（收支项目）
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
public class AssisteInoutclassRepository extends ServiceImpl<AssisteInoutclassMapper, AssisteInoutclass> {

    /**
     * 批量新增或修改
     * @param assisteInoutclasses
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<AssisteInoutclass> assisteInoutclasses){
        return baseMapper.saveOrUpdateBatch(assisteInoutclasses) > 0;
    }

    /**
     * 查询辅助核算（收支项目）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（收支项目）列表
     */
    public List<AssisteItemOBV> getAssisteItems(String name, String code){
        return baseMapper.selectAssisteItems(name, code);
    }

}
