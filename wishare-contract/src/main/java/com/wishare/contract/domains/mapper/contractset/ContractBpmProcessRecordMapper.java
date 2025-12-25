package com.wishare.contract.domains.mapper.contractset;

import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 
 * </p>
 *
 * @author jinhui
 * @since 2023-02-24
 */
@Mapper
public interface ContractBpmProcessRecordMapper extends BaseMapper<ContractBpmProcessRecordE> {
    ContractBpmProcessRecordE selectOneByProId(String proId);
}
