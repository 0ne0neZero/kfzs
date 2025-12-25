package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.PayBillDetailV;
import com.wishare.finance.apps.model.bill.vo.PayBillV;
import com.wishare.finance.domains.bill.command.AddGatherBillCommand;
import com.wishare.finance.domains.bill.command.AddGatherBillDetailCommand;
import com.wishare.finance.domains.bill.command.AddPayBillCommand;
import com.wishare.finance.domains.bill.command.AddPayDetailCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.entity.PayableBill;
import com.wishare.finance.domains.bill.service.PayBillDomainService;
import com.wishare.finance.domains.bill.service.PayableBillDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayBillAppService {

    private final PayBillDomainService payBillDomainService;

    private final PayableBillDomainService payableBillDomainService;

    /**
     * 新增付款单
     *
     * @param addPayBillF 付款单参数
     * @return PayBillDetailV
     */
    public PayBillV addBill(AddPayBillF addPayBillF) {
        AddPayBillCommand addPayBillCommand = Global.mapperFacade.map(addPayBillF, AddPayBillCommand.class);
        List<AddPayDetailF> addPayDetailves = addPayBillF.getAddPayDetailves();
        List<AddPayDetailCommand> addPayDetailCommands = Global.mapperFacade.mapAsList(addPayDetailves, AddPayDetailCommand.class);
        addPayBillCommand.setAddPayDetailCommands(addPayDetailCommands);
        return payBillDomainService.addBill(addPayBillCommand);
    }

    /**
     * 批量新增付款单
     *
     * @param addPayBillListFs 付款单参数
     * @return List
     */
    public List<PayBillV> addBatch(List<AddPayBillF> addPayBillListFs) {
        List<AddPayBillCommand> addPayBillCommandList = Lists.newArrayList();
        for (AddPayBillF addPayBillListF : addPayBillListFs) {
            AddPayBillCommand addPayBillCommand = Global.mapperFacade.map(addPayBillListF, AddPayBillCommand.class);
            List<AddPayDetailF> addPayDetailves = addPayBillListF.getAddPayDetailves();
            List<AddPayDetailCommand> addPayDetailCommands = Global.mapperFacade.mapAsList(addPayDetailves, AddPayDetailCommand.class);
            addPayBillCommand.setAddPayDetailCommands(addPayDetailCommands);
            addPayBillCommandList.add(addPayBillCommand);
        }
        return payBillDomainService.addBatch(addPayBillCommandList);
    }

    /**
     * 发起审核申请
     *
     * @param billApplyF 申请参数
     * @return Boolean
     */
    public Boolean apply(BillApplyF billApplyF) {
        return null;
    }

    /**
     * 审核付款单
     *
     * @param approvePayBillF 审核信息
     * @return Boolean
     */
    public Boolean approve(ApprovePayBillF approvePayBillF) {
        return null;
    }

    /**
     * 批量审核应付账单
     *
     * @param approveBatchPayBillF 审核信息
     * @return Boolean
     */
    public Boolean approveBatch(ApproveBatchPayBillF approveBatchPayBillF) {
        return null;
    }

    /**
     * 删除付款单
     *
     * @param payBillId 付款单id
     * @return Boolean
     */
    public Boolean delete(Long payBillId) {
        return payBillDomainService.delete(payBillId);
    }

    /**
     * 批量删除应付账单
     *
     * @param deleteBatchPayBillF 删除参数
     * @return BillBatchResultDto
     */
    public BillBatchResultDto deleteBatch(DeleteBatchPayBillF deleteBatchPayBillF) {
        return null;
    }

    /**
     * 批量发起审核申请
     *
     * @param billApplyBatchF 申请参数
     * @return Boolean
     */
    public Boolean applyBatch(BillApplyBatchF billApplyBatchF) {
        return null;
    }

    /**
     * 查询付款单详情
     *
     * @param payBillId 付款单id
     * @return PayBillDetailV
     */
    public PayBillV queryById(Long payBillId) {
        return Global.mapperFacade.map(payBillDomainService.queryById(payBillId), PayBillV.class);
    }

    /**
     * 账单结转
     *
     * @param billCarryoverF 结转参数
     * @return Boolean
     */
    public Boolean carryover(PayBillCarryoverF billCarryoverF) {
        return null;
    }

    /**
     * 分页查询已审核付款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<PayBillV> getApprovedPage(PageF<SearchF<?>> queryF) {
        return payBillDomainService.getApprovedPage(queryF);
    }

    /**
     * 分页查询审核付款单列表
     * @param queryF
     * @return
     */
    public PageV<PayBillV> getPage(PageF<SearchF<?>> queryF) {
        return payBillDomainService.getPage(queryF);
    }

    /**
     * 分页查询未审核付款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public PageV<PayBillV> queryNotApprovedPage(PageF<SearchF<?>> queryF) {
        return payBillDomainService.queryNotApprovedPage(queryF);
    }

    /**
     * 作废付款单
     *
     * @param payBillInvalidF 作废参数
     * @return Boolean
     */
    public Boolean invalid(PayBillInvalidF payBillInvalidF) {
        return null;
    }

    /**
     * 冲销付款单
     *
     * @param payBillId 付款单id
     * @return Boolean
     */
    public Boolean reverse(Long payBillId) {
        return null;
    }

    /**
     * 查询付款单详情(包含明细)
     *
     * @param payBillId 付款单id
     * @return PayBillDetailV
     */
    public PayBillDetailV queryDetailById(Long payBillId) {
        return payBillDomainService.queryDetailById(payBillId);
    }

    /**
     * 根据id集合获取付款单
     *
     * @param payBillIdList 付款单id集合
     * @return List
     */
    public List<PayBillV> queryByIdList(List<Long> payBillIdList) {
        return payBillDomainService.queryByIdList(payBillIdList);
    }

    /**
     * 导出收款单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        payBillDomainService.export(queryF,response);
    }

    /**
     * 获取账单推凭信息
     * @param form
     * @param billTypeEnum
     * @return
     */
    public PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum) {
        SearchF<BillInferenceV> searchF = new SearchF<>();
        searchF.setFields(form.getFieldList());
        PageF<SearchF<BillInferenceV>> page = new PageF<>();
        page.setConditions(searchF);
        page.setPageNum(form.getPageNum());
        page.setPageSize(form.getPageSize());
        Page<BillInferenceV> pageV = payBillDomainService.pageBillInferenceInfo(page, form.getEventType());
        fillTaxRate(pageV.getRecords());
        return PageV.of(page.getPageNum(), page.getPageSize(), pageV.getTotal(), pageV.getRecords());
    }

    /**
     * 获取账单推凭信息
     * @param billInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF) {
        List<BillInferenceV> list = payBillDomainService.listInferenceInfo(billInferenceF);
        fillTaxRate(list);
        return list;
    }

    /**
     * 获取账单推凭信息
     * @param batchBillInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF) {
        List<BillInferenceV> list = payBillDomainService.listInferenceInfoByIds(batchBillInferenceF);
        fillTaxRate(list);
        return list;
    }

    /**
     * 填充收款单的税率
     * @param list
     */
    private void fillTaxRate(List<BillInferenceV> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<PayableBill> taxBillList = payableBillDomainService.getList(
            list.stream().map(BillInferenceV::getTaxBillId).collect(Collectors.toList()), null);
        if (CollectionUtils.isEmpty(taxBillList)) {
            return;
        }
        Map<Long, BigDecimal> taxMap = taxBillList.stream()
            .collect(Collectors.toMap(PayableBill::getId, PayableBill::getTaxRate));
        if (CollectionUtils.isEmpty(taxMap)) {
            return;
        }
        list.forEach(billInference ->
            billInference.setTaxRate(taxMap.getOrDefault(billInference.getTaxBillId(), new BigDecimal(0))));
    }

}
