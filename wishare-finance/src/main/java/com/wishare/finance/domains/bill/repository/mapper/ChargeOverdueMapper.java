package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.ChargeOverdueDto;
import com.wishare.finance.domains.bill.entity.ChargeOverdueE;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (ChargeOverdue)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-02 15:16:04
 */
public interface ChargeOverdueMapper extends BaseMapper<ChargeOverdueE> {


    Long queryRoomCount(@Param("ew") QueryWrapper<?> queryWrapper);


    Long querySumOverdueAmount(@Param("ew") QueryWrapper<?> queryWrapper);

    void updateByBillId(@Param("communityId") String communityId,@Param("chargeOverdueEList") List<ChargeOverdueE> chargeOverdueEList);

    int deleteByBillIds(@Param("billIds") List<Long> billIds);

    Page<ChargeOverdueDto> queryPageBySearch(@Param("page") Page<SearchF<?>> page,
                                             @Param(Constants.WRAPPER) QueryWrapper<?> wrapper,
                                             @Param("tableName") String tableName);

    Long count(@Param(Constants.WRAPPER)  QueryWrapper<ChargeOverdueE> queryWrapper);

}

