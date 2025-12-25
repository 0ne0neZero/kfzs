package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInferStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.GatherBillDto;
import com.wishare.finance.domains.bill.dto.GatherDto;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.repository.mapper.GatherBillMapper;
import com.wishare.finance.domains.bill.repository.mapper.GatherBillOriginProxyMapper;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 收款单资源库
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherBillRepository extends ServiceImpl<GatherBillMapper, GatherBill> {

    @Autowired
    private GatherBillOriginProxyMapper gatherBillOriginProxyMapper;

    /**
     * 分页查询已审核付款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    public IPage<GatherBillDto> queryPage(Page<Object> page, QueryWrapper<?> queryWrapper, String gatherBillName, String gatherDetailName) {
        return baseMapper.queryPage(page,queryWrapper, gatherBillName, gatherDetailName);
    }

    /**
     * 分页查询已审核收款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    public IPage<GatherBillDto> queryGatherPage(Page<Object> page, QueryWrapper<?> queryWrapper, String gatherBillName, String gatherDetailName) {
        return baseMapper.queryGatherPage(page,queryWrapper, gatherBillName, gatherDetailName);
    }

    /**
     * 分页查询未开票收款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return IPage
     */
    public IPage<UnInvoiceGatherBillDto> unInvoiceGatherBillPage(Page<Object> page, QueryWrapper<?> queryWrapper, String gatherBillName, String gatherDetailName) {
        return baseMapper.unInvoiceGatherBillPage(page,queryWrapper, gatherBillName, gatherDetailName);
    }



    /**
     * 分页查询已审核付款单count
     *
     * @param queryWrapper 查询参数
     * @return IPage
     */
    public Integer queryPageCount(QueryWrapper<?> queryWrapper, String gatherBillName, String gatherDetailName) {
        return baseMapper.queryPageCount(queryWrapper, gatherBillName, gatherDetailName);
    }

    /**
     * 统计付款单信息
     *
     * @param wrapper 统计条件
     * @return BillTotalDto
     */
    public BillTotalDto queryTotal(QueryWrapper<?> wrapper) {
        return baseMapper.queryTotal(wrapper);
    }
    public BillTotalDto queryTotalNew(QueryWrapper<?> wrapper,String gatherBillTableName,String gatherDetailName) {
        return baseMapper.queryTotalNew(wrapper,gatherBillTableName,gatherDetailName);
    }

    /**
     * 查询历史缴费记录
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    public Page<PayListDto> payList(Page<Object> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.payList(page, queryWrapper);
    }

    /**
     * 查询开票收款单列表
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    public Page<PayListDto> payInvoiceList(Page<Object> page, QueryWrapper<?> queryWrapper, String gatherBillTableName, String gatherDetailTableName) {
//        return gatherBillOriginProxyMapper.payInvoiceList(page, queryWrapper, gatherBillTableName, gatherDetailTableName);
        //上面写法还是很久之前gather_bill表不记录预收表而采用union all方法，效率不高
        log.info("分页参数:current:{},size:{}",page.getCurrent(), page.getSize());
        List<PayListDto> payListDtoPage = baseMapper.payInvoiceList(page, queryWrapper,
                gatherBillTableName, gatherDetailTableName);
        Integer total = baseMapper.payInvoiceListCnt(queryWrapper,
                gatherBillTableName, gatherDetailTableName);
        Page<PayListDto> result = new Page<>(page.getCurrent(), page.getSize());
        result.setTotal(total);
        result.setRecords(payListDtoPage);
        return result;
    }

    /**
     * 根据收款单id获取收款单
     *
     * @param gatherBillId 收款单id
     * @return GatherBillDto
     */
    public GatherBillDto queryById(Long gatherBillId, String gatherBillTableName, String gatherDetailTableName) {

        return baseMapper.queryById(gatherBillId,gatherBillTableName, gatherDetailTableName);
    }

    /**
     * 根据id获取收款单
     *
     * @param gatherBillIdList 收款单id
     * @return GatherBillDto
     */
    public List<GatherBillDto> queryByIdList(List<Long> gatherBillIdList, String supCpUnitId) {
        return baseMapper.queryByIdList(gatherBillIdList, supCpUnitId);
    }

    /**
     * 根据收款单id和开票状态获取收款单列表
     *
     * @param gatherBillIds
     * @param invoiceStateList 开票状态
     * @return
     */
    public List<GatherBill> listByIdsAndInvoiceState(List<Long> gatherBillIds,
                                                     List<Integer> invoiceStateList, String supCpUnitId) {
        QueryWrapper<GatherBill> wrapper = new QueryWrapper<>();
        wrapper.eq("sup_cp_unit_id", supCpUnitId);
        wrapper.in("invoice_state", invoiceStateList);
        wrapper.in("id", gatherBillIds);
        wrapper.orderByDesc("gmt_create");
        return list(wrapper);
    }

    /**
     * 获取结算账单推凭信息
     * @param page
     * @param queryModel
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceInfo(PageF<SearchF<BillInferenceV>> page, QueryWrapper<BillInferenceV> queryModel) {
        return baseMapper.pageBillInferenceInfo(Page.of(page.getPageNum(), page.getPageSize()), queryModel);
    }

    /**
     * 获取冲销作废账单推凭信息
     * @param page
     * @param queryModel
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceOffInfo(PageF<SearchF<BillInferenceV>> page, QueryWrapper<BillInferenceV> queryModel) {
        return baseMapper.pageBillInferenceOffInfo(Page.of(page.getPageNum(), page.getPageSize()), queryModel);
    }

    /**
     * 根据收款单ids获取收款单
     *
     * @param gatherBillIds
     * @return
     */
    public List<GatherBill> getGatherBill(List<Long> gatherBillIds, String supCpUnitId) {
        return list(new QueryWrapper<GatherBill>().eq("sup_cp_unit_id", supCpUnitId).in("id", gatherBillIds));
    }


    public List<GatherBill> getGatherBillByBillNo(List<String> billNos, String supCpUnitId) {
        return list(new QueryWrapper<GatherBill>().eq("sup_cp_unit_id", supCpUnitId).in("bill_no", billNos));
    }

    public List<GatherBill> getByOutBusId(String invRecUnitId, String communityId) {
        return list(new QueryWrapper<GatherBill>().eq("out_bus_id", invRecUnitId)
                .eq("sup_cp_unit_id", communityId)
                .in("invoice_state", BillInvoiceStateEnum.未开票.getCode(), BillInvoiceStateEnum.部分开票.getCode()));
    }

    public List<GatherBill> getByOutBusIdV(String hncId, String communityId) {
        return baseMapper.getByOutBusIdV(hncId,communityId);
    }

    public List<GatherBill> getByTradeNo(List<String> tradeNoList,String supCpUnitId) {
        return baseMapper.getByTradeNo(tradeNoList, supCpUnitId);
    }
    /**
     * 更新交账状态
     * @param billId
     * @param handedState
     * @return
     */
    public boolean updateHandState(Long billId, BillAccountHandedStateEnum handedState, String supCpUnitId) {
        return update(new UpdateWrapper<GatherBill>().eq("id", billId).eq("sup_cp_unit_id", supCpUnitId).set("account_handed", handedState.getCode()));
    }

    /**
     * 更新交账状态
     * @param billId
     * @return
     */
    public boolean updateReconcileState(List<Long> billId,  String supCpUnitId) {
        return update(new UpdateWrapper<GatherBill>().in("id", billId).eq("sup_cp_unit_id", supCpUnitId).set("reconcile_state", 0));
    }


    /**
     * 查询凭证业务单据信息
     *
     * @param wrapper
     * @param special
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherBillByQuery(QueryWrapper<?> wrapper, int voucherEventType, boolean special, String tableName) {
        return baseMapper.listVoucherBillByQuery(wrapper,voucherEventType,special,tableName);
    }

    /**
     * 查询凭证业务已银行对账单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherBankBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherBankBillByQuery(wrapper,tableName);
    }

    /**
     * 查询凭证业务已流水认领单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherClaimBillByQuery(QueryWrapper<?> wrapper, @Param("gatherBillTableName") String gatherBillTableName,
                                                                 @Param("gatherDetailTableName") String gatherDetailTableName,
                                                                 @Param("receivableBillTableName") String receivableBillTableName, String tableName) {
        return baseMapper.listVoucherClaimBillByQuery(wrapper, gatherBillTableName, gatherDetailTableName,receivableBillTableName,tableName);
    }

    public Integer queryCountByTradeNo(String tradeNo, String supCpUnitId){
        return baseMapper.queryCountByTradeNo(tradeNo, supCpUnitId);
    }


    /**
     * 分页查询收款记录(收款单维度无租户隔离)
     * @param pageF
     * @return
     */
    public Page<GatherDto> queryPageGatherBillIgnore(PageF<SearchF<?>> pageF) {
        SearchF<?> conditions = pageF.getConditions();
        conditions.getFields().add(new Field("b.deleted", DataDeletedEnum.NORMAL.getCode(),1));
        QueryWrapper<?> queryPayModel = conditions.getQueryModel();
        return baseMapper.queryPageGatherBillIgnore(Page.of(pageF.getPageNum(), pageF.getPageSize(), pageF.isCount()),queryPayModel);

    }

    /**
     * 通过成功做了凭证收款单更新其对应的应收单为 已推凭 状态
     */
    public void updateReceivableBillInferenceStateByGatherBillIds(List<Long> idList, String supCpUnitId) {
        baseMapper.updateReceivableBillInferenceStateByGatherBillIds(idList, BillInferStateEnum.已推凭.getCode(), supCpUnitId);
    }

    public void updateInvoiceState(Long gatherBillId, BillInvoiceStateEnum stateEnum) {
        if (gatherBillId != null) {
            GatherBill gatherBill = getById(gatherBillId);
            // 老数据的advanceBill没有gatherBill
            if (gatherBill != null) {
                gatherBill.setInvoiceState(stateEnum.getCode());
                updateById(gatherBill);
            }
        }
    }

    public List<VoucherBusinessBill> listVoucherBillByQueryFromContract(QueryWrapper<?> wrapper, int voucherEventType) {
        return baseMapper.listVoucherBillByQueryFromContract(wrapper,voucherEventType,BillTypeEnum.收款单.getCode());
    }

    /**
     * 修改收款账户ID
     * @param sbAccountId
     * @param idList
     */
    public void updateSbAccountId(Long sbAccountId, List<Long> idList,  String supCpUnitId) {
        baseMapper.updateSbAccountId(sbAccountId, idList, supCpUnitId);
    }

    /**
     * 发起开票
     * 根据收款单ids 修改收款单信息为开票中 [invoiceState = 1]
     * @param gatherBillIds
     * @return
     */
    @Transactional
    public boolean gatherBillInvoiceBatch(List<Long> gatherBillIds, String supCpUnitId, Map<Long,Integer> billIdsMap) {
        if(CollectionUtils.isEmpty(billIdsMap)){
            return true;
        }
        List<GatherBill> billList = list(new QueryWrapper<GatherBill>()
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                .in("id", gatherBillIds));
        billList.forEach(gatherBill -> {
            /** 修改bill的开票状态 */
            Integer state = this.doInvoiceState(billIdsMap, gatherBill.getId());
            if(Objects.isNull(state)){return;}
            gatherBill.setInvoiceState(state);
            update(gatherBill,new UpdateWrapper<GatherBill>()
                    .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                    .eq("id", gatherBill.getId()));
        });
        return true;
    }

    /**
     *
     * @param billIdsMap
     * @param id
     * @return
     */
    private Integer doInvoiceState(Map<Long,Integer> billIdsMap,Long id){
        if(org.springframework.util.CollectionUtils.isEmpty(billIdsMap)){
            return BillInvoiceStateEnum.开票中.getCode();
        }
        Integer result = billIdsMap.get(id);
        if(Objects.nonNull(result)){
            return result;
        }
        return null;
    }

    public void updateGatherBillDeleted(List<Long> ids,  String communityId) {
        update(new UpdateWrapper<GatherBill>().set("deleted", 1)
                .in("id", ids)
                .eq("sup_cp_unit_id",communityId));
    }
}


