package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.TemporaryChargeBillPageV;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.command.FinishInvoiceCommand;
import com.wishare.finance.domains.bill.consts.enums.BillReferenceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.dto.SettleDetailDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillExportDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillMoreInfoDto;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.service.TemporaryChargeBillDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TemporaryChargeBillAppService extends BillAppServiceImpl<TemporaryChargeBillDomainService, TemporaryChargeBill>  {

    @Override
    public BillTypeEnum getBillType() {
        return BillTypeEnum.临时收费账单;
    }

    /**
     * 设置账单引用
     * @param referenceBillF
     * @return
     */
    public boolean reference(ReferenceBillF referenceBillF) {
        return baseBillDomainService.reference(referenceBillF.getBillId(), BillReferenceStateEnum.valueOfByCode(referenceBillF.getReferenceState()), referenceBillF.getSupCpUnitId());
    }

    /**
     * 根据账单ids获取临时收费信息
     *
     * @param billIds
     * @return
     */
    public List<TempChargeBillMoreInfoDto> tempChargeBillInfo(List<Long> billIds, String supCpUnitId) {
        List<TempChargeBillMoreInfoDto> dtoList = baseBillDomainService.tempChargeBillInfo(billIds, supCpUnitId);
        return dtoList;
    }

    /**
     * 根据账单ids获取临时账单结算详情
     *
     * @param billIds
     * @return
     */
    public SettleDetailDto settleDetail(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.settleDetail(billIds, supCpUnitId);
    }

    /**
     * 分页查询临时收费导出账单列表
     *
     * @param queryF queryF
     * @return PageV
     */
    public PageV<TempChargeBillExportDto> queryExportDataPage(PageF<SearchF<?>> queryF) {
        IPage<TempChargeBillExportDto> temporaryChargeBillPage = baseBillDomainService.queryExportDataPage(queryF);
        return PageV.of(temporaryChargeBillPage.getCurrent(),
                temporaryChargeBillPage.getSize(),
                temporaryChargeBillPage.getTotal(),
               temporaryChargeBillPage.getRecords());
    }

    /**
     * 批量添加临时账单结算记录
     *
     * @param form
     * @return
     */
    @Transactional
    public Long settleBatch(List<AddBillSettleF> form) {
        List<AddBillSettleCommand> addBillSettleCommands = Global.mapperFacade.mapAsList(form, AddBillSettleCommand.class);
        return baseBillDomainService.settleBatch(addBillSettleCommands);
    }

    /**
     * 根据账单ids获取结算记录
     *
     * @param billIds
     * @return
     */
    public List<BillSettleDto> getBillSettle(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.getBillSettle(billIds,BillTypeEnum.应收账单, supCpUnitId);
    }

    /**
     * 发起开票
     *
     * @param billIds
     * @param billIdsMap key-billId,value-开票状态
     * @return
     */
    public Boolean invoiceBatch(List<Long> billIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return baseBillDomainService.invoiceBatch(billIds,supCpUnitId,billIdsMap);
    }

    /**
     * 完成开票
     *
     * @param finishInvoiceFList
     * @return
     */
    public Boolean finishInvoiceBatch(List<FinishInvoiceF> finishInvoiceFList) {
        List<FinishInvoiceCommand> finishInvoiceCommands = Global.mapperFacade.mapAsList(finishInvoiceFList, FinishInvoiceCommand.class);
        return baseBillDomainService.finishInvoiceBatch(finishInvoiceCommands);
    }

    /**
     * 重新计算设置开票状态
     * @param billId
     */
    public void reSetBillInvoiceState(Long billId, String supCpUnitId) {
        baseBillDomainService.reSetBillInvoiceState(billId, supCpUnitId);
    }

    /**
     *
     * @param invoiceVoidBatchFList
     * @return
     */
    public Boolean invoiceVoidBatch(List<InvoiceVoidBatchF> invoiceVoidBatchFList) {
        List<FinishInvoiceCommand> finishInvoiceCommands = Global.mapperFacade.mapAsList(invoiceVoidBatchFList, FinishInvoiceCommand.class);
        return baseBillDomainService.invoiceVoidBatch(finishInvoiceCommands, finishInvoiceCommands.get(0).getSupCpUnitId());
    }

    /**
     * 冲销
     *
     * @param temporaryChargeBillId
     * @return
     */
    public Boolean reverse(Long temporaryChargeBillId,String extField1,String supCpUnitId) {
        return baseBillDomainService.reverse(temporaryChargeBillId,extField1,supCpUnitId);
    }


    /**
     * 回滚冲销
     *
     * @param temporaryChargeBillId
     * @return
     */
    public Boolean robackReverse(Long temporaryChargeBillId,String supCpUnitId) {
        return baseBillDomainService.robackReverse(temporaryChargeBillId,supCpUnitId);
    }

    /**
     * 分页查询临时账单审核列表
     *
     * @param queryF
     * @return PageV
     */
    public PageV<TemporaryChargeBillPageV> queryApprovePage(PageF<SearchF<?>> queryF) {
        IPage<TemporaryChargeBill> page = baseBillDomainService.getPageNotApprove(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), TemporaryChargeBillPageV.class));
    }

    /**
     * 处理临时账单数据(将临时账单数据同步到应收账单表，只需要调用一次)
     *
     * @return Integer
     */
    public Boolean dealData() {
        return baseBillDomainService.dealData();
    }

    @Transactional
    public Boolean deduct(BillDeductionF deductionF) {
        if (BillTypeEnum.临时收费账单.getCode()==deductionF.getType()) {
            return baseBillDomainService.deduct(deductionF);
        }else {
            return false;
        }
    }

    public Boolean deleteInitBill(PageF<SearchF<?>> queryF) {
        return baseBillDomainService.deleteInitBill(queryF);
    }

    public List<Long> getBillRoomIds(String communityId, Long chargeItemId) {
        return baseBillDomainService.getBillRoomIds(communityId, chargeItemId);
    }
}
