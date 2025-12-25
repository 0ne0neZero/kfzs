package com.wishare.finance.domains.mdm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.mdm.entity.Mdm73E;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhuadmin
 */
@Mapper
public interface Mdm73Mapper extends BaseMapper<Mdm73E> {

    int deleteByOid(@Param("oid") String oid);

    void insertBatch(@Param("list") List<Mdm73E> list);
}
