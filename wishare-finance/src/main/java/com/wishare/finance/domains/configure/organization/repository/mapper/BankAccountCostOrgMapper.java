package com.wishare.finance.domains.configure.organization.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.BankAccountCostOrgE;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Mapper
public interface BankAccountCostOrgMapper extends BaseMapper<BankAccountCostOrgE> {

    List<BankAccountCostOrgDto> getRelation(@Param("costOrdId") Long costOrdId, @Param("tenantId") String tenantId);

    List<StatutoryBodyAccountE> queryStaIdByCostIds(@Param("ids") List<Long> ids);

    String queryCostOrgIdByBankAccount(@Param("bankAccount") String bankAccount);
}
