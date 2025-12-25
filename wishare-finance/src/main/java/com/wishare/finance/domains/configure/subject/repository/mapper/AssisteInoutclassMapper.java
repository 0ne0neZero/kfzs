package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.AssisteInoutclass;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 辅助核算（收支项目） Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Mapper
public interface AssisteInoutclassMapper extends BaseMapper<AssisteInoutclass> {

    /**
     * 查询辅助核算（收支项目）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（收支项目）列表
     */
    List<AssisteItemOBV> selectAssisteItems(@Param("name") String name, @Param("code")String code);

    /**
     * 批量新增或保存
     * @param assisteInoutclasses
     * @return
     */
    int saveOrUpdateBatch(@Param("aic") List<AssisteInoutclass> assisteInoutclasses);

}
