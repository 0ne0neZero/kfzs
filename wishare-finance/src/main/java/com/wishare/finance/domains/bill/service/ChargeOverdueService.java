package com.wishare.finance.domains.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.ChargeOverdueBatchDeleteF;
import com.wishare.finance.apps.model.bill.fo.ChargeSearchF;
import com.wishare.finance.apps.model.bill.fo.StateUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillOverduePageV;
import com.wishare.finance.apps.model.bill.vo.BillOverdueStatisticsV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillDetailV;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.ChargeOverdueDto;
import com.wishare.finance.domains.bill.entity.ChargeOverdueE;
import com.wishare.finance.domains.bill.repository.mapper.ChargeOverdueMapper;
import com.wishare.finance.infrastructure.conts.FinanceConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.MapperFacadeUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargeOverdueService extends ServiceImpl<ChargeOverdueMapper, ChargeOverdueE> {


    private final ChargeOverdueMapper chargeOverdueMapper;

    private final ReceivableBillAppService receivableBillAppService;

    private final SharedBillAppService sharedBillAppService;



    public PageV<BillOverduePageV> findPage(PageF<SearchF<?>> f, String tenantId) {
        ChargeSearchF<ChargeOverdueE> searchF = new ChargeSearchF<>();
        searchF.fieldNotLikeToNotIn(f.getConditions().getFields());

        PageV<BillOverduePageV> pageV = new PageV<>();
        Global.mapperFacade.map(f, pageV);
        /*
         * 分页查询
         */
        PageQueryUtils.validQueryContainsFieldAndValue(f, "rb." + BillSharedingColumn.应收账单.getColumnName());
        String supCpUnitId = null;
        for (Field item : f.getConditions().getFields()) {
            if ("rb.sup_cp_unit_id".equals(item.getName())) {
                supCpUnitId = (String) item.getValue();
                item.setName("b.community_id");
            }
        }
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        QueryWrapper<ChargeOverdueE>  queryWrapper = searchF.getQueryModel();
        queryWrapper.eq("b.deleted", Const.State._0).eq("b.tenant_id", tenantId).orderByDesc("b.gmt_create").orderByAsc("b.id");

        Page<ChargeOverdueDto> billAdjustDtoPage = chargeOverdueMapper.queryPageBySearch(RepositoryUtil.convertMPPage(f),
                RepositoryUtil.putLogicDeleted(queryWrapper, "b"), shareTableName);
        List<ChargeOverdueDto> pageRecords = billAdjustDtoPage.getRecords();
        MapperFacade mapperFacade = MapperFacadeUtil.getMoneyMapperFacade(ChargeOverdueDto.class, BillOverduePageV.class, FinanceConst.MONEY_FIELD_MAP);
        return PageV.of(billAdjustDtoPage.getCurrent(), billAdjustDtoPage.getSize(), billAdjustDtoPage.getTotal()
                , mapperFacade.mapAsList(pageRecords, BillOverduePageV.class));
    }


    public BillOverdueStatisticsV queryCount(PageF<SearchF<?>> f, String tenantId){
        ChargeSearchF<ChargeOverdueE> searchF = new ChargeSearchF<>();
        searchF.fieldNotLikeToNotIn(f.getConditions().getFields());
        QueryWrapper<ChargeOverdueE> queryWrapper = searchF.getQueryModel();
        queryWrapper.eq("b.deleted", Const.State._0).eq("b.tenant_id", tenantId);
        BillOverdueStatisticsV result = new BillOverdueStatisticsV();
        result.setRoomCount(chargeOverdueMapper.queryRoomCount(queryWrapper));
        result.setOverdueCount(chargeOverdueMapper.count(queryWrapper));
        Long overdueAmountSum = chargeOverdueMapper.querySumOverdueAmount(queryWrapper);
        if(Objects.isNull(overdueAmountSum)){
            overdueAmountSum = 0L;
        }
        result.setOverdueAmount(BigDecimal.valueOf(overdueAmountSum).divide(new BigDecimal(100)));
        return result;
    }

    public ChargeOverdueE getOverdueByRefBillId(Long refBillId){
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0).eq("ref_bill_id", refBillId);
        List<ChargeOverdueE> list = chargeOverdueMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<ChargeOverdueE> getOverdueByRefBillNo(String refBillNo){
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0).eq("ref_bill_no", refBillNo);
        List<ChargeOverdueE> list = chargeOverdueMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    public List<ChargeOverdueE> getOverdueByOverDueNo(String overdueNo){
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0).eq("overdue_no", overdueNo);
        List<ChargeOverdueE> list = chargeOverdueMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    public ChargeOverdueE getOverdueByBillId(Long billId){
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0).eq("bill_id", billId).eq("bill_create_state", Const.State._1);
        List<ChargeOverdueE> list = chargeOverdueMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<ChargeOverdueE> getOverdueByRefBillIds(List<Long> refBillIdList){
        if(CollectionUtils.isEmpty(refBillIdList)){
            return Collections.emptyList();
        }
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0)
                .in("ref_bill_id", refBillIdList);
        return chargeOverdueMapper.selectList(queryWrapper);
    }

    /**
     * 根据主体账单获取违约金账单信息
     *
     * @param refBillIdList 主体账单id
     * @return 违约金账单id
     */
    public List<ReceivableBillDetailV> getOverdueReceivableBillDetailVByBillIds(List<Long> refBillIdList) {
        if(CollectionUtils.isEmpty(refBillIdList)){
            return Collections.emptyList();
        }
        List<ChargeOverdueE> overdueByRefBillIds = getOverdueByRefBillIds(refBillIdList);
        List<Long> billIds = overdueByRefBillIds
                .stream()
                .map(ChargeOverdueE::getBillId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(billIds)) {
            return Collections.emptyList();
        }
        return receivableBillAppService.getBillInfoByIds(billIds,ReceivableBillDetailV.class,overdueByRefBillIds.get(0).getCommunityId());
    }

    public List<ChargeOverdueE> getOverdueByBillIds(List<Long> billIdList){
        if(CollectionUtils.isEmpty(billIdList)){
            return Collections.emptyList();
        }
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", Const.State._0).in("bill_id", billIdList).eq("bill_create_state", Const.State._1);
        return chargeOverdueMapper.selectList(queryWrapper);
    }

    public Integer updateSettleState(List<StateUpdateF> list){
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        ChargeOverdueE updateE = new ChargeOverdueE();
        for(StateUpdateF f : list){
            updateE.setBillSettleState(f.getState());
            QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bill_id", f.getId());
            chargeOverdueMapper.update(updateE, queryWrapper);
        }
        return 1;
    }

    public boolean deleteByBillIds(List<Long> billIds){

        return chargeOverdueMapper.deleteByBillIds(billIds) > 0;
    }




    public void del(String communityId,List<ChargeOverdueE> chargeOverdueEList){
        chargeOverdueMapper.updateByBillId(communityId,chargeOverdueEList);
    }

    /**
     * 批量删除违约金管理数据
     * @param chargeOverdueBatchDeleteF 批量删除传参
     * @return 批量删除结果
     */
    public Boolean delete(ChargeOverdueBatchDeleteF chargeOverdueBatchDeleteF) {
        // 根据违约金管理数据id获取违约金管理数据
        QueryWrapper<ChargeOverdueE> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(ChargeOverdueE::getCommunityId, chargeOverdueBatchDeleteF.getCommunityId())

                .eq(ChargeOverdueE::getDeleted, 0)
                .in(ChargeOverdueE::getId, chargeOverdueBatchDeleteF.getOverdueIds());
        List<ChargeOverdueE> list = chargeOverdueMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw BizException.throw300("根据查询条件未查到相关违约金管理数据！");
        }
        // 判断违约金管理数据是不是已经生成违约金账单
        List<ChargeOverdueE> haveOverdueBillInfo =
                list.stream()
                        .filter(e -> Objects.nonNull(e.getBillId()))
                        .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(haveOverdueBillInfo)) {
            List<String> overdueNos = haveOverdueBillInfo
                    .stream()
                    .map(ChargeOverdueE::getOverdueNo)
                    .collect(Collectors.toList());
            throw BizException.throw300(
                    "存在已生成违约金账单的违约金管理数据！违约金编码： " +
                            CollectionUtil.join(overdueNos, ",")
            );
        }
        // 违约金管理数据逻辑删除
        chargeOverdueMapper.deleteBatchIds(chargeOverdueBatchDeleteF.getOverdueIds());
        return true;
    }

    public Boolean delete(ChargeOverdueE overdue) {
        if (Objects.isNull(overdue)) {
            return false;
        }
        chargeOverdueMapper.deleteById(overdue);
        return true;
    }
}
