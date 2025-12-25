package com.wishare.finance.domains.mdm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.mdm.entity.Mdm11E;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface Mdm11Mapper extends BaseMapper<Mdm11E> {

    void insertBatch(@Param("list") Collection<Mdm11E> list);

    List<Mdm11E> selectCashFlowItems();
}
