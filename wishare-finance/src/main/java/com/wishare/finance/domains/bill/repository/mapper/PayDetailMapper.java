package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.PayDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  付款明细Mapper 接口
 * </p>
 *
 * @author yancao
 * @since 2022-12-22
 */
@Mapper
public interface PayDetailMapper extends BaseMapper<PayDetail> {

    /**
     * 批量更新账单详情的账单推凭状态
     * @param concatIds
     * @param state
     */
    void batchUpdateDetailInferenceSate(@Param("list") List<Long> concatIds, @Param("state") int state);

    /**
     * 根据id和推凭状态查询账单
     * @param queryWrapper
     * @return
     */
    List<BillInferenceV> listInferenceInfoByIdAndInfer(@Param(Constants.WRAPPER) QueryWrapper<GatherDetail> queryWrapper);
}
