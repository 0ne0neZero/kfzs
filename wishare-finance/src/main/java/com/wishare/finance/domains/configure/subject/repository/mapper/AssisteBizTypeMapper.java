package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 辅助核算（业务类型） Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Mapper
public interface AssisteBizTypeMapper extends BaseMapper<AssisteBizType> {

    /**
     * 查询辅助核算（业务类型）列表
     * @param name 名称
     * @param code 编码
     * @return 辅助核算（业务类型）列表
     */
    List<AssisteItemOBV> selectAssisteItems(@Param("name") String name, @Param("code")String code);

    /**
     * 批量保存或更新
     * @param assisteBizTypes
     * @return
     */
    int saveOrUpdateBatchCode(@Param("abt") List<AssisteBizType> assisteBizTypes);


}
