package com.wishare.finance.domains.configure.cashflow.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.cashflow.comamnd.CashFlowDtoQuery;
import com.wishare.finance.domains.configure.cashflow.dto.CashFlowDto;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 13:23
 * @version: 1.0.0
 */
@Mapper
public interface CashFlowMapper extends BaseMapper<CashFlowE> {


    @InterceptorIgnore(tenantLine = "on")
    List<CashFlowE> listCashFlowBySubjectId(@Param("subjectId") Long subjectId, @Param("isMain") boolean isMain);

    /**
     * 批量新增或更新
     * @param cashFlows
     * @return
     */
    int saveOrUpdate(@Param("cfs") List<CashFlowE> cashFlows);

    /**
     * 根据编码查询上级现金流量
     * @param code
     * @return
     */
    CashFlowE selectSupByCode(@Param("code") String code);

    /**
     * 根据科目id获取现金流量项目
     * @param subejctId 科目id
     * @return
     */
    List<CashFlowE> selectBySubjectId(@Param("subjectId") Long subejctId);

    /**
     * 根据科目id列表获取现金流量项目
     * @param subjectIds 科目id列表
     * @return
     */
    List<CashFlowE> selectBySubjectIds(@Param("subjectIds")List<Long> subjectIds);

    /**
     * 查询现金流量
     * @param query 查询信息
     * @return 查询现金流量列表 限制500条
     */
    List<CashFlowDto> selectDtoByCodeName(@Param("cfq") CashFlowDtoQuery query);

}
