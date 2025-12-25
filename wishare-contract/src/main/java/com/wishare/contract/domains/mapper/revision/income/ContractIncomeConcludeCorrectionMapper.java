package com.wishare.contract.domains.mapper.revision.income;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeCorrectionE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractIncomeConcludeCorrectionMapper extends BaseMapper<ContractIncomeConcludeCorrectionE> {
    //根据修正记录ID删除数据
    int deletedCorrectionById(@Param("id") Long id);
}
