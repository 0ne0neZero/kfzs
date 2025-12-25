package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.GatherAndPayDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Mapper
public interface GatherAndPayMapper {


    /**
     * 分页查询收付款记录
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<GatherAndPayDto> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);

    /**
     * 分页查询收付款记录(收付款单维度)
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<GatherAndPayDto> billQueryPage(Page<?> of,  @Param("ew")QueryWrapper<?> queryModel, @Param("gatherBillName")String gatherBillName
        ,@Param("gatherDetailName")String gatherDetailName);

    /**
     * 分页查询收付款记录(收付款单维度无租户隔离)
     *
     * @param of
     * @param queryModel
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<GatherAndPayDto> billQueryPageIgnoreTenant(Page<Object> of, @Param("ew") QueryWrapper<?> queryModel,@Param("ew2") QueryWrapper<?> queryPayModel
        , @Param("gatherBillName")String gatherBillName
        , @Param("gatherDetailName")String gatherDetailName);
    /**
     * 统计收款单金额之和
     *
     * @param queryModel
     * @return
     */
    Long statisticsGather(@Param("ew") QueryWrapper<?> queryModel);

    /**
     * 统计付款单金额之和
     *
     * @param queryModel
     * @return
     */
    Long statisticsPay(@Param("ew") QueryWrapper<?> queryModel);


    /**
     * 统计收款单金额之和
     *
     * @param queryModel
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Long statisticsGatherIgnoreTenant(@Param("ew") QueryWrapper<?> queryModel,@Param("gatherDetailName")String gatherDetailName);

    /**
     * 统计付款单金额之和
     *
     * @param queryModel
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Long statisticsPayIgnoreTenant(@Param("ew") QueryWrapper<?> queryModel);

}
