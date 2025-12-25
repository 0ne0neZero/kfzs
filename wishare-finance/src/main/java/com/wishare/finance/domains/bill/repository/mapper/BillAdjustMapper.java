package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.BillAdjustDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.ChargeDeductionDetailDto;
import com.wishare.finance.domains.bill.dto.ChargeDeductionManageDto;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单调整记录映射类
 * @Author dxclay
 * @Date 2022/8/25
 * @Version 1.0
 */
@Mapper
public interface BillAdjustMapper extends BaseMapper<BillAdjustE> {

    void batchUpdateInferenceSate(@Param("list") List<Long> concatIds, @Param("state") int state);

    IPage<ChargeDeductionDetailDto> getDeductionDetail(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper
        , @Param("receivableBillName")String receivableBillName);

    /**
     * 分页查询调整明细记录
     * @param page 分页参数
     * @param wrapper 条件参数
     * @return {@link Page}<>{@link BillAdjustDto}</>
     */
    Page<BillAdjustDto> queryPageBySearch(@Param("page") Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);


    List<BillAdjustE> queryList (@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    void updateBillInferenceState(@Param("list") List<Long> list,  @Param("inferenceState") Integer inferenceState);
    void updateBillInferenceStateByGmtModify(@Param("list") List<Long> list,  @Param("inferenceState") Integer inferenceState, @Param("gmtCreate") LocalDateTime gmtCreate);

}
