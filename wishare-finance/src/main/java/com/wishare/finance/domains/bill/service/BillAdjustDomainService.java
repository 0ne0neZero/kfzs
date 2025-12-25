package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.GatherBillV;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.BillAdjustDto;
import com.wishare.finance.domains.bill.dto.GatherBillDto;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/28 9:47
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAdjustDomainService {

    private final BillAdjustRepository billAdjustRepository;

    /**
     * 根据账单id获取调整记录
     * @param billId
     * @return
     */
    public List<BillAdjustE> listByBillId(Long billId) {
        return billAdjustRepository.listByBillId(billId);
    }

    /**
     * 批量更新已推凭的调整
     * @param concatIds
     * @param state
     */
    public void batchUpdateInferenceSate(List<Long> concatIds, int state) {
        billAdjustRepository.batchUpdateInferenceSate(concatIds, state);
    }

    /**
     * 分页获取调整记录
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillAdjustV}</>
     */
    public PageV<BillAdjustV> getBillAdjustPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        queryWrapper.in("b.approved_state", BillApproveStateEnum.已审核.getCode(), BillApproveStateEnum.审核中.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "rb." + BillSharedingColumn.应收账单.getColumnName());
        Page<BillAdjustDto> billAdjustDtoPage = billAdjustRepository.queryPageBySearch(RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel(), "b"));
        return RepositoryUtil.convertPage(billAdjustDtoPage, BillAdjustV.class);
    }
}
