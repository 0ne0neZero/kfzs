package com.wishare.contract.domains.mapper.revision.log;

import com.wishare.contract.domains.entity.revision.log.RevisionLogE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同改版动态记录表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-12
 */
@Mapper
public interface RevisionLogMapper extends BaseMapper<RevisionLogE> {

}
