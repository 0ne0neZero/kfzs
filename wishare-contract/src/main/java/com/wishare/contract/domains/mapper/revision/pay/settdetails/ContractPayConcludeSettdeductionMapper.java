package com.wishare.contract.domains.mapper.revision.pay.settdetails;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractSettdeductionDetailV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 结算单扣款明细表信息
 * </p>
 *
 * @author zhangfy
 * @since 2024-05-20
 */
@Mapper
public interface ContractPayConcludeSettdeductionMapper extends BaseMapper<ContractPayConcludeSettdeductionE> {

    IPage<ContractSettdeductionDetailV> contractSettdeductionDetailPage(Page<?> pageF, @Param("settlementIds") List<String> settlementIds);

    int deleteBySettlementId(@Param("settlementId") String settlementId);
}
