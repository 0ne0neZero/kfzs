package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.command.MaxGatherTimeQuery;
import com.wishare.finance.domains.bill.dto.ChargeBillPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一账单mapper
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Mapper
public interface UnitaryBillMapper {


    /**
     * 查询收费账单分页信息(包括应收、预收)
     * @param queryWrapper
     * @return   收费账单分页信息(包括应收、预收)
     */
    Page<ChargeBillPageDto> queryChargeBillPage(@Param(Constants.COLUMN_MAP) QueryWrapper<?> queryWrapper);


    /**
     * 最大账单周期查询
     * @param query
     * @return
     */
    List<String> queryMaxGatherTime(@Param("qe") MaxGatherTimeQuery query);
}
