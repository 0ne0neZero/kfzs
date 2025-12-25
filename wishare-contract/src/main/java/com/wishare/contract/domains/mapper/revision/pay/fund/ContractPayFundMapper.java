package com.wishare.contract.domains.mapper.revision.pay.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.apps.fo.revision.pay.report.ContractPayReportDetailListV;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundInfoV;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 支出合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@Mapper
public interface ContractPayFundMapper extends BaseMapper<ContractPayFundE> {

    List<ContractPayFundInfoV> getContractPayFundList(@Param("contractid")String contractid);

    List<ContractPayFundV> getContractPaySettFundList(@Param("contractid")String contractid);
    //获取清单项最大金额数据结算周期数据
    List<ContractPayReportDetailListV> getFundListByContractIdList(@Param("contractList") List<String> contractList);
    //根据支出合同ID获取清单费项数据
    List<FunChargeItemF> getFundChargeItemById(@Param("contractId") String contractId);

    Integer getExtField(@Param("contractId")String contractId,
                        @Param("typeId")String typeId,
                        @Param("taxRateId")String taxRateId,
                        @Param("standardId")String standardId,
                        @Param("standAmount")BigDecimal standAmount);

    int deleteByMainId(@Param("mainId") String mainId);

    int deletedCostData(@Param("contractId") String contractId);
}
