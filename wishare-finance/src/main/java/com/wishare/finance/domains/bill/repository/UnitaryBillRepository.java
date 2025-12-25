package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.command.MaxGatherTimeQuery;
import com.wishare.finance.domains.bill.dto.ChargeBillPageDto;
import com.wishare.finance.domains.bill.repository.mapper.UnitaryBillMapper;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 统一账单资源库
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UnitaryBillRepository {

    private final UnitaryBillMapper unitaryBillMapper;

    /**
     * 查询收费账单分页信息(包括应收、预收)
     * @param queryF 查询条件
     * @return 收费账单列表
     */
    public Page<ChargeBillPageDto> getChargeBillPage(PageF<SearchF<?>> queryF){
        return unitaryBillMapper.queryChargeBillPage(queryF.getConditions().getQueryModel());
    }


    /**
     * 获取最大账单周期（应收、预收）
     * @param query 最大账单周期查询
     * @return 最大账单周期
     */
    public String getMaxGatherTime(MaxGatherTimeQuery query){
        List<String> maxGatherTimes = unitaryBillMapper.queryMaxGatherTime(query);
        if (CollectionUtils.isEmpty(maxGatherTimes)){
            return null;
        }
        return maxGatherTimes.stream().filter(Objects::nonNull).max((o1, o2) -> StringUtils.compare(o1, o2)).orElse(null);
    }


}
