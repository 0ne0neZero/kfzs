package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessTypeE;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xujian
 * @date 2022/12/6
 * @Description:
 */
@Mapper
public interface BusinessTypeMapper extends BaseMapper<BusinessTypeE> {

    /**
     * 根据select的条件判断是否插入，如果没有就插入，有就更新
     *
     * @param businessTypeE
     */
    void updateSync(BusinessTypeE businessTypeE);
}
