package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrgDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 辅助核算（部门） Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Mapper
public interface AssisteOrgDeptMapper extends BaseMapper<AssisteOrgDept> {

    /**
     * 查询辅助核算（部门）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（部门）列表
     */
    List<AssisteItemOBV> selectAssisteItems(@Param("name") String name, @Param("code")String code, @Param("orgCode")String orgCode);

    /**
     * 批量新增或更新
     * @param assisteOrgDepts
     * @return
     */
    int saveOrUpdateBatch(@Param("aod") List<AssisteOrgDept> assisteOrgDepts);
}
