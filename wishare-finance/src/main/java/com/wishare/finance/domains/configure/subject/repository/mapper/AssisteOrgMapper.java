package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 辅助核算（业务单元） Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Mapper
public interface AssisteOrgMapper extends BaseMapper<AssisteOrg> {

    /**
     * 查询辅助核算（业务单元）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（业务单元）列表
     */
    List<AssisteItemOBV> selectAssisteItems(@Param("name") String name, @Param("code")String code);

    /**
     * 批量新增或更新
     * @param assisteOrgs
     * @return
     */
    int saveOrUpdateBatch(@Param("ao") List<AssisteOrg> assisteOrgs);
}
