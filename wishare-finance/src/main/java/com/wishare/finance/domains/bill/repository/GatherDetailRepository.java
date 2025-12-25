package com.wishare.finance.domains.bill.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.GatherDetailV;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.repository.mapper.GatherDetailMapper;
import com.wishare.finance.domains.bill.repository.mapper.GatherDetailOriginProxyMapper;
import com.wishare.finance.domains.invoicereceipt.dto.GatherDetailInfo;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收款详情资源库
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherDetailRepository extends ServiceImpl<GatherDetailMapper, GatherDetail> {
    @Autowired
    private GatherDetailOriginProxyMapper gatherDetailOriginProxyMapper;
    /**
     * 根据应收单id查询
     * @param recBillId
     * @return
     */
    public List<GatherDetail> listByRecBillId(Long recBillId, String supCpUnitId) {
        return baseMapper.listGatherBillByRecIdAndSupCpUnitId(recBillId, supCpUnitId);
    }

    /**
     * 根据应收单id查询
     * @param recBillIds
     * @return
     */
    public List<GatherDetail> listsByRecBillId(List<Long> recBillIds, String supCpUnitId) {
        return baseMapper.listGatherBillByRecIdAndSupCpUnitIds(recBillIds, supCpUnitId);
    }


    /**
     * 根据应收单isd查询未开票收款单明细
     * @param recBillIds
     * @return
     */
    public List<GatherDetail> queryByRecBillIdList(List<Long> recBillIds, String supCpUnitId) {
        LambdaQueryWrapper<GatherDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GatherDetail::getRecBillId,recBillIds);
        queryWrapper.eq(GatherDetail::getSupCpUnitId, supCpUnitId);
        queryWrapper.eq(GatherDetail::getAvailable, 0);
        queryWrapper.in(GatherDetail::getInvoiceState, List.of(BillInvoiceStateEnum.未开票.getCode(), BillInvoiceStateEnum.部分开票.getCode()));
        return list(queryWrapper);
    }



    /**
     * 根据应收单id查询收款详情
     * @param billId 账单id
     * @return
     */
    public List<GatherDetail> listByBillId(Long billId,Integer billType, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        if(BillTypeEnum.预收账单.getCode() == billType || BillTypeEnum.收款单.getCode() == billType){
            queryWrapper.eq("gather_bill_id", billId);
        }else{
            queryWrapper.eq("rec_bill_id", billId);
        }
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }

    /**
     * 根据收款单id获取收款单明细
     *
     * @param gatherBillId 收款单id
     * @return List
     */
    public List<GatherDetail> queryByGatherBillId(Long gatherBillId, String supCpUintId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUintId);
        queryWrapper.eq("gather_bill_id",gatherBillId).orderByDesc("gmt_create");
        return list(queryWrapper);
    }

    /**
     * 根据收款单id获取付款单明细
     *
     * @param gatherBillIdList 收款单id
     * @return List
     */
    public List<GatherDetail> queryByPayBillIdList(List<Long> gatherBillIdList, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in(CollectionUtils.isNotEmpty(gatherBillIdList), "gather_bill_id", gatherBillIdList);
        return list(queryWrapper);
    }


    /**
     * 根据收款单id获取收款单单明细
     *
     * @param gatherBillIdList 收款单id
     * @return List
     */
    public List<GatherDetail> queryGatherDetailList(List<Long> gatherBillIdList, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in("invoice_state", List.of(BillInvoiceStateEnum.未开票.getCode(), BillInvoiceStateEnum.部分开票.getCode()));
        queryWrapper.in(CollectionUtils.isNotEmpty(gatherBillIdList), "gather_bill_id", gatherBillIdList);
        return list(queryWrapper);
    }

    /**
     * 根据收款明细ids获取收款单单明细
     *
     * @param ids 收款明细id
     * @return List
     */
    public List<GatherDetail> queryByIdList(List<Long> ids, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in(CollectionUtils.isNotEmpty(ids), "id", ids);
        return list(queryWrapper);
    }


    /**
     * 修改账单详情的推凭状态
     * @param concatIds
     * @param state
     */
    public void batchUpdateDetailInferenceSate(List<Long> concatIds, int state) {
        baseMapper.batchUpdateDetailInferenceSate(concatIds, state);
    }

    /**
     * 根据应收单id获取收款明细
     *
     * @param recBillId
     */
    public GatherDetail getByRecBillId(Long recBillId, String supCpUnitId) {
        List<GatherDetail> byRecBillId = this.getByRecBillId(recBillId, supCpUnitId, 1);
        return CollectionUtils.isEmpty(byRecBillId)?null:byRecBillId.get(0);
    }

    /**
     * 根据应收单id获取收款明细
     *
     * @param recBillId
     */
    public List<GatherDetail> getByRecBillId(Long recBillId, String supCpUnitId,Integer limitNum) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rec_bill_id", recBillId);
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.orderByDesc("gmt_create");
        if(Objects.nonNull(limitNum)){
            queryWrapper.last("limit "+limitNum);
        }
        return baseMapper.selectList(queryWrapper);
    }





    public List<GatherDetail> getByRecBillIds(Long recBillIds) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("rec_bill_id", recBillIds);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }

    /**
     * 根据应收单id获取收款明细
     *
     * @param recBillId 账单id
     */
    public List<GatherDetail> getListByRecBillId(Long recBillId,String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rec_bill_id", recBillId);
        queryWrapper.eq("sup_cp_unit_id",supCpUnitId);
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("available",0);
        queryWrapper.orderByDesc("gmt_create");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据账单ids获取收款明细
     *
     * @param recBillIds
     * @return
     */
    public List<GatherDetail> getByRecBillIds(List<Long> recBillIds,String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id",supCpUnitId);
        queryWrapper.in("rec_bill_id", recBillIds);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }


    /**
     * 根据账单ids获取收款明细
     *
     * @param gatherBillIds
     * @return
     */
    public List<GatherDetail> getListByGatherBillIds(List<Long> gatherBillIds,String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id",supCpUnitId);
        queryWrapper.in("gather_bill_id", gatherBillIds);
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("available",0);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }


    /**
     * 根据收款单ids获取收款明细
     *
     * @param gatherBillIds
     * @return
     */
    public List<GatherDetail> getByGatherBillIds(List<Long> gatherBillIds, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in("gather_bill_id", gatherBillIds);
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("available",0);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }


    /**
     * 根据收款单id获取收款明细
     * @param gatherBillId
     * @param supCpUnitId
     * @return
     */
    public List<GatherDetail> getByGatherBillId(Long gatherBillId, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.eq("gather_bill_id", gatherBillId);
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("available",0);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }

    /**
     * 根据收款单ids获取收款明细
     * 对账专用！
     *  其他业务请勿调用
     * @param gatherBillIds
     * @return
     */
    public List<GatherDetail> getByGatherBillIdsForReconciliation(List<Long> gatherBillIds, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in("gather_bill_id", gatherBillIds);
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("gmt_create");
        return list(queryWrapper);
    }


    /**
     *
     * @param form
     * @return
     */
    public List<Long> listBillIdsByIdsAndChannel(SettleChannelAndIdsF form) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", form.getSupCpUnitId());
        queryWrapper.in("gather_bill_id", form.getBillIds());
        JSONObject channel = JSON.parseObject(form.getParams());
        Integer method = channel.getInteger("method");
        if (method == 1) {
            queryWrapper.eq("pay_channel", channel.getJSONArray("value").getString(0));
        } else if (method == 2 || method == 15) {
            queryWrapper.in("pay_channel", channel.getJSONArray("value").toJavaList(String.class));
        } else if (method == 3 || method == 16) {
            queryWrapper.notIn("pay_channel", channel.getJSONArray("value").toJavaList(String.class));
        }
        List<GatherDetail> billSettleES = baseMapper.selectList(queryWrapper);
        return billSettleES.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());

    }

    /**
     * 根据id和推凭状态查询账单
     * @param billId
     * @param inferenceState
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIdAndInfer(Long billId, int inferenceState, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq("b.id", billId);
        queryWrapper.eq("bd.inference_state", inferenceState);
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.eq("bd.deleted", 0);
        queryWrapper.eq("bd.sup_cp_unit_id", supCpUnitId);
        queryWrapper.eq("b.sup_cp_unit_id", supCpUnitId);
        return baseMapper.listInferenceInfoByIdAndInfer(queryWrapper);
    }

    /**
     * 根据id和推凭状态查询账单
     * @param billIds
     * @param inferenceState
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIdsAndInfer(List<Long> billIds, int inferenceState, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper();
        queryWrapper.in("b.id", billIds);
        queryWrapper.eq("bd.inference_state", inferenceState);
        queryWrapper.eq("b.deleted", 0);
        queryWrapper.eq("bd.deleted", 0);
        queryWrapper.eq("bd.sup_cp_unit_id", supCpUnitId);
        queryWrapper.eq("b.sup_cp_unit_id", supCpUnitId);
        return baseMapper.listInferenceInfoByIdAndInfer(queryWrapper);
    }

    public GatherDetail getByGatherBillId(Long gatherId) {
        return getOne(new QueryWrapper<GatherDetail>().eq("gather_bill_id", gatherId),false);
    }

    /**
     * 根据收款单更新应收账单对账状态
     * @param gatherBillId 收款单id
     * @param handedState
     * @return
     */
    public boolean updateHandRecState(Long gatherBillId, BillAccountHandedStateEnum handedState, String supCpUnitId) {
        return gatherDetailOriginProxyMapper.updateHandRecState(gatherBillId, handedState.getCode(), supCpUnitId) > 0;
    }

    /**
     * 根据out_pay_no获取收款单明细
     *
     * @param outPayNo 支付编码
     * @return List
     */
    public List<GatherDetail> queryByOutPayNo(String outPayNo, String supCpUnitId) {
        QueryWrapper<GatherDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_pay_no", outPayNo);
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        return list(queryWrapper);
    }

    /**
     * 获取收款单对应账单的id
     * @param gatherBillIds 收款单id
     * @param billTypeEnum 账单类型
     * @return
     */
    public List<Long> getBillIds(List<Long> gatherBillIds, BillTypeEnum billTypeEnum) {
        switch (billTypeEnum) {
            case 预收账单:
            case 应收账单:
            case 临时收费账单:
                QueryWrapper<GatherDetail> wrapper =
                        new QueryWrapper<GatherDetail>().select("distinct rec_bill_id");
                wrapper.in("gather_bill_id", gatherBillIds);
                List<GatherDetail> details = this.list(wrapper);
                return details.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
            default:
                throw BizException.throw400("不支持该账单类型：" + billTypeEnum.getValue());
        }
    }


    /**
     * 获取收款明细 租户隔离
     * @param id
     * @return
     */
    public List<GatherDetail> queryByGatherBillIdIgnore(Long id, String supCpUnitId) {
        return baseMapper.queryByGatherBillIdIgnore(id, supCpUnitId);
    }

    public Page<GatherDetailV> queryPageGatherDetail(PageF<SearchF<?>> pageF) {
        SearchF<?> conditions = pageF.getConditions();
        QueryWrapper<?> queryPayModel = conditions.getQueryModel();
        return baseMapper.queryPageGatherDetail(Page.of(pageF.getPageNum(), pageF.getPageSize(), pageF.isCount()),queryPayModel);
    }

    public void updateInferenceStateByIds(List<Long> billIds, Integer inferenceState, List<String> collect) {
        update(new UpdateWrapper<GatherDetail>().set("inference_state", inferenceState)
                .in("id", billIds)
                .in("sup_cp_unit_id",collect));
    }

    public List<GatherDetail> getNewPaymentList(List<Long> billIds, String supCpUnitId) {
        QueryWrapper<GatherDetail> wrapper = new QueryWrapper<>();
        wrapper.in("rec_bill_id", billIds);
        wrapper.eq("deleted", 0);
        wrapper.eq("sup_cp_unit_id",supCpUnitId);
        wrapper.isNotNull("pay_channel");
        wrapper.isNotNull("payee_name");
        wrapper.orderByDesc("pay_time");
        wrapper.last("limit 1");
        return baseMapper.selectList(wrapper);
    }

    /**
     * 发起开票
     * 根据收款明细ids 修改收款明细信息为开票中 [invoiceState = 1]
     * @param gatherDetailIds
     * @return
     */
    @Transactional
    public boolean gatherDetailInvoiceBatch(List<Long> gatherDetailIds, String supCpUnitId, Map<Long,Integer> billIdsMap) {
        List<GatherDetail> GatherDetailBillList = list(new QueryWrapper<GatherDetail>()
                .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                .in("id", gatherDetailIds));
        GatherDetailBillList.forEach(gatherDetail -> {
            Integer state = this.doInvoiceState(billIdsMap, gatherDetail.getId());
            if(Objects.isNull(state)){return;}
            /** 修改bill的开票状态 */
            gatherDetail.setInvoiceState(state);
            update(gatherDetail,new UpdateWrapper<GatherDetail>()
                    .eq(StringUtils.isNotBlank(supCpUnitId), "sup_cp_unit_id", supCpUnitId)
                    .eq("id", gatherDetail.getId()));
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

    public List<PushZJBusinessBill> getFundReceiptsBillZJList(QueryWrapper<?> wrappers,  String gatherBillTableName,
            String gatherDetailTableName,
            String receivableBillTableName){
        return baseMapper.getFundReceiptsBillZJList(wrappers,  gatherBillTableName,
                gatherDetailTableName,
                receivableBillTableName);
    }

    /**
     * 根据roomIds 修改成本中心信息 存在于临时、应收
     * @param roomIds 空间ids
     * @param supCpUnitId 项目id
     * @param costCenterId 成本中心id
     * @param costCenterName 成本中心名称
     */
    public void updateCostMsgByRoomIds(List<String> roomIds,Integer billType,String supCpUnitId,Long costCenterId,String costCenterName,String gatherDetailName,String receivableBillName){
        baseMapper.updateCostMsgByRoomIds(roomIds,billType,supCpUnitId,costCenterId,costCenterName,gatherDetailName,receivableBillName);
    }

    /**
     * 根据项目ids+账单类型 修改成本中心信息 存在于临时、应收
     *
     * @param billType           账单类型
     * @param supCpUnitId        项目id
     * @param costCenterId       成本中心id
     * @param costCenterName     成本中心名称
     * @param gatherDetailName   分表名
     * @param receivableBillName 分表名
     */
    public void updateCostMsgBySupCpUnitId(Integer billType,String supCpUnitId,Long costCenterId,String costCenterName,String gatherDetailName,String receivableBillName){
        baseMapper.updateCostMsgBySupCpUnitId(billType,supCpUnitId,costCenterId,costCenterName,gatherDetailName,receivableBillName);
    }





    /**
     * 根据roomIds 修改成本中心信息 存在预收
     * @param roomIds
     * @param supCpUnitId
     * @param costCenterId
     * @param costCenterName
     */
    public void updateCostMsgByRoomIds(List<String> roomIds,String supCpUnitId,Long costCenterId,String costCenterName,String gatherDetailName){
        baseMapper.updateCostMsgByRoomIdsAdv(roomIds,supCpUnitId,costCenterId,costCenterName,gatherDetailName);
    }

    /**
     * 根据项目id 修改成本中心
     *
     * @param supCpUnitId      项目id
     * @param costCenterId     成本中心id
     * @param costCenterName   成本中心名称
     * @param gatherDetailName 分表名
     */
    public void updateCostMsgBySupCpUnitId(String supCpUnitId,Long costCenterId,String costCenterName,String gatherDetailName){
        baseMapper.updateCostMsgBysupCpUnitIdAdv(supCpUnitId,costCenterId,costCenterName,gatherDetailName);
    }



    public GatherAndPayStatisticsDto statistics(SearchF<?> form,String gatherBillName,String gatherDetailName) {
        QueryWrapper<?> queryModel = form.getQueryModel();
        Long gatherAmountSum = baseMapper.statisticsGather(queryModel,gatherBillName,gatherDetailName);
        return new GatherAndPayStatisticsDto(gatherAmountSum,0L);
    }

    public void updateGatherDetailDeleted(List<Long> ids,  String communityId) {
        update(new UpdateWrapper<GatherDetail>().set("deleted", 1)
                .in("id", ids)
                .eq("sup_cp_unit_id",communityId));
    }

    public List<GatherDetail> getAllDetailByGatherBillIds(List<Long> gatherBillIds, String supCpUnitId) {
        return  baseMapper.getAllDetailByGatherBillIds(gatherBillIds,supCpUnitId);
    }

    public List<GatherDetail> getAllDetailByIds(List<Long> ids, String supCpUnitId) {
        return baseMapper.getAllDetailByIds(ids,supCpUnitId);
    }
}
