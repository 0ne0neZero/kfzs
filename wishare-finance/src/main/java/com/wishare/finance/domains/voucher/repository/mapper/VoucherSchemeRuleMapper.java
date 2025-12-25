package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.voucher.dto.VoucherSchemeRuleDto;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRule;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 凭证核算方案凭证规则关联表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Mapper
public interface VoucherSchemeRuleMapper extends BaseMapper<VoucherSchemeRule> {

    List<VoucherSchemeRuleDto> selectByRuleIds(@Param("ruleIds") List<Long> ruleIds);

    List<VoucherSchemeRuleOBV> selectOBVSBySchemeId(@Param("schemeId") Long voucherSchemeId);


    List<VoucherScheme> selectSchemesByRuleId(@Param("ruleId") Long ruleId);
}
