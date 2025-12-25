package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 功能解释
 *
 * @author long
 * @date 2023/7/18 18:54
 */
@Mapper
public interface ContractProcessRecordMapper extends BaseMapper<ContractProcessRecordE> {


    /**
     * 暂时不使用
     */
    @Deprecated
    Long selectOneByRecordId(@Param("recordId") Long recordId);
}
