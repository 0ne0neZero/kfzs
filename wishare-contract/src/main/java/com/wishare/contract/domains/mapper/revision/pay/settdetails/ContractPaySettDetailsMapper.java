package com.wishare.contract.domains.mapper.revision.pay.settdetails;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:20
 */
@Mapper
public interface ContractPaySettDetailsMapper extends BaseMapper<ContractPaySettDetailsE> {

    int deleteBySettlementId(@Param("settlementId") String settlementId);
}
