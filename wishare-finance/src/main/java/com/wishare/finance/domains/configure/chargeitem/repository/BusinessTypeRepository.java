package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.fo.BusinessTypeListF;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessTypeE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.BusinessTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/6
 * @Description:
 */
@Service
public class BusinessTypeRepository extends ServiceImpl<BusinessTypeMapper, BusinessTypeE> {

    /**
     * 根据条件获取业务类型
     *
     * @param form
     * @return
     */
    public List<BusinessTypeE> businessTypeList(BusinessTypeListF form) {
        LambdaQueryWrapper<BusinessTypeE> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(form.getCode()), BusinessTypeE::getCode, form.getCode());
        wrapper.like(StringUtils.isNotBlank(form.getName()), BusinessTypeE::getName, form.getName());
        return baseMapper.selectList(wrapper);
    }
}
