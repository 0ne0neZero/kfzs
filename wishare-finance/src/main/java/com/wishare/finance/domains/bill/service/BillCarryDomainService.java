package com.wishare.finance.domains.bill.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.BillCarryV;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.BillAdjustDto;
import com.wishare.finance.domains.bill.repository.BillCarryRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.SearchFileUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryDomainService
 * @date 2024.05.21  17:08
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryDomainService {

    private final BillCarryRepository billCarryRepository;

    /**
     * 分页查询结转记录
     * @param queryF
     * @return {@link BillCarryV}
     */
    public PageV<BillCarryV> getBillCarryPage(PageF<SearchF<?>> queryF) {
        // SearchF<?> conditions = queryF.getConditions();
        // List<Field> fields = conditions.getFields();
        //
        // // 若要查询被结转对象编号，则需要将其抽丝剥茧 转化为json式模糊查询
        // Optional<Field> searchField = fields.stream()
        //         .filter(field -> "searchs".equals(field.getName())).findFirst();
        // searchField.ifPresent(field -> {
        //     Map<String, Object> map = field.getMap();
        //     if (map.containsKey("a.target_bill_no")) {
        //         Object value = map.get("a.target_bill_no");
        //         map.remove("a.target_bill_no");
        //         map.put("JSON_EXTRACT(a.carryover_detail, '$[*].targetBillNo')", value);
        //     }
        // });

        // 分页查询哦
        Page<BillCarryV> billCarryPage = billCarryRepository.queryPageBySearch(
                RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel(), "b"));
        return RepositoryUtil.convertPage(billCarryPage, BillCarryV.class);
    }

    public PageV<BillCarryV> queryCarryoverPage(PageF<SearchF<?>> queryF) {
        Field field = SearchFileUtil.getField(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());
        if (Objects.isNull(field) || Objects.isNull(field.getValue())) {
            throw new IllegalArgumentException("请传入supCpUnitId!");
        }
        // 分页查询哦
        Page<BillCarryV> billCarryPage = billCarryRepository.queryCarryoverPage(
                RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel(), "b"),
                (String) field.getValue());
        return RepositoryUtil.convertPage(billCarryPage, BillCarryV.class);
    }
}
