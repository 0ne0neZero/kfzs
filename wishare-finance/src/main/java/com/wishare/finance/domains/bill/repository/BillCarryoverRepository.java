package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.domains.bill.aggregate.BillCarryoverA;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.repository.mapper.BillCarryoverMapper;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Service
public class BillCarryoverRepository extends ServiceImpl<BillCarryoverMapper, BillCarryoverE> {

    /**
     * 账单结转
     * @param carryoverA
     * @return
     */
    public boolean carryover(BillCarryoverA carryoverA){

        return true;
    }

    /**
     * 根据审核记录获取结转信息信息
     * @param approveId
     * @return
     */
    public BillCarryoverE getByApproveId(Long approveId){
        return getOne(new LambdaQueryWrapper<BillCarryoverE>().eq(BillCarryoverE::getBillApproveId, approveId));
    }


    /**
     * 根据结转账单id查询结转记录
     * @param carriedBillId
     * @return
     */
    public List<BillCarryoverE> listByCarriedBillId(Long carriedBillId) {
        LambdaQueryWrapper<BillCarryoverE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillCarryoverE::getCarriedBillId, carriedBillId);
        wrapper.ne(BillCarryoverE::getCarryoverAmount, 0L);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 获取已审核的调整记录
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillAdjustV}</>
     */
    public PageV<BillCarryoverE> getBillCarriedPage(PageF<SearchF<?>> queryF) {
        queryF.getConditions().getFields().add(new Field("b.state", Const.State._2, 1));
        queryF.getConditions().getFields().add(new Field("b.deleted", DataDeletedEnum.NORMAL.getCode(), 1));
        Page<BillCarryoverE> billAdjustDtoPage = baseMapper.queryPageBySearch(RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel(), "b"));
        return RepositoryUtil.convertPage(billAdjustDtoPage, BillCarryoverE.class);
    }

    public List<PushBusinessBill> getBillIdByList(Long billId,String supCpUnitId) {
        return baseMapper.getBillIdByList(billId,supCpUnitId);
    }
}
