package com.wishare.contract.domains.mapper.revision.pay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.revision.pay.ContractPayCostApportionE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/11/10 16:46
 */
@Mapper
public interface ContractPayCostApportionMapper extends BaseMapper<ContractPayCostApportionE> {

    int deletedCostData(@Param("contractId") String contractId,@Param("idList") List<String> idList);

    int deletedCostDataOne(@Param("contractId") String contractId,@Param("id") String id);
}
