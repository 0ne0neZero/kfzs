package com.wishare.finance.domains.configure.subject.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.repository.mapper.AssisteBizTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 辅助核算（业务类型）
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
public class AssisteBizTypeRepository extends ServiceImpl<AssisteBizTypeMapper, AssisteBizType> {

    public boolean saveOrUpdateBatchByCode(List<AssisteBizType> assisteBizTypes){
        return baseMapper.saveOrUpdateBatchCode(assisteBizTypes) > 0;
    }


    public List<AssisteBizType> listByAscCode(String ascCode) {
        return list(new LambdaUpdateWrapper<>());
    }


    /**
     * 根据实体
     * @param assisteBizType
     * @return
     */
    public List<AssisteBizType> listByEntity(AssisteBizType assisteBizType){
        return list(new LambdaQueryWrapper<AssisteBizType>()
                .like(Objects.nonNull(assisteBizType.getCode()), AssisteBizType::getCode, assisteBizType.getCode()).or()
                .like(Objects.nonNull(assisteBizType.getName()), AssisteBizType::getName, assisteBizType.getName())
                .eq(Objects.nonNull(assisteBizType.getDisabled()), AssisteBizType::getDisabled, assisteBizType.getDisabled())
                .last("limit 1000")
        );
    }


    /**
     * 查询辅助核算（业务类型）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（业务类型）列表
     */
    public List<AssisteItemOBV> getAssisteItems(String name, String code){
        return baseMapper.selectAssisteItems(name, code);
    }

}
