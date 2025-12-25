package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.chargeitem.entity.AssisteAccountE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Mapper
public interface AssisteAccountMapper extends BaseMapper<AssisteAccountE> {

    /**
     * 分页获取辅助核算
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<AssisteAccountE> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 导出辅助核算查询
     *
     * @param queryModel
     * @return
     */
    List<AssisteAccountE> exportList(@Param("ew") QueryWrapper<?> queryModel);
}
