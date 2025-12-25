package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.model.bill.fo.AddAdvanceBillF;
import com.wishare.finance.apps.model.bill.fo.AdvanceMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.fo.FinishInvoiceF;
import com.wishare.finance.apps.model.bill.fo.InvoiceVoidBatchF;
import com.wishare.finance.apps.model.bill.fo.ReceivableMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.domains.bill.command.AddAdvanceBillCommand;
import com.wishare.finance.domains.bill.command.FinishInvoiceCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.AdvanceBillGroupDetailDto;
import com.wishare.finance.domains.bill.dto.BillAddBatchResultDto;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.service.AdvanceBillDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预收帐单service
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdvanceBillAppService extends BillAppServiceImpl<AdvanceBillDomainService, AdvanceBill>{

    @Override
    public BillTypeEnum getBillType() {
        return BillTypeEnum.预收账单;
    }


    private final AdvanceBillDomainService advanceBillDomainService;

    /**
     * 批量新增预收账单
     *
     * @param addAdvanceBillList addAdvanceBillList
     * @return Boolean
     */
    @Transactional
    public List<BillAddBatchResultDto> addBatchAdvanceBill(List<AddAdvanceBillF> addAdvanceBillList) {
        //List<AddAdvanceBillCommand> advanceBillList = new ArrayList<>();
        //for (AddAdvanceBillF addAdvanceBillF : addAdvanceBillList) {
        //    AddAdvanceBillCommand command = Global.mapperFacade.map(addAdvanceBillF, AddAdvanceBillCommand.class);
        //    advanceBillList.add(command);
        //}
        return advanceBillDomainService
                .addBatchAndReturnErrorInfo(Global.mapperFacade.mapAsList(addAdvanceBillList, AddAdvanceBillCommand.class));
    }

    /**
     * 预收预缴批量新增预收账单
     *
     * @param addAdvanceBillList addAdvanceBillList
     * @return Boolean
     */
    @Transactional
    public List<AdvanceBillAllDetailV> payBatchAdvanceBill(List<AddAdvanceBillF> addAdvanceBillList) {
        return advanceBillDomainService
                .addBatch(Global.mapperFacade.mapAsList(addAdvanceBillList, AddAdvanceBillCommand.class));
    }

    /**
     * 发起开票
     * 根据账单ids 修改账单信息为开票中 [invoiceState = 1]
     * @param billIds
     * @param billIdsMap key-billId,value-开票状态
     * @return
     */
    public Boolean invoiceBatch(List<Long> billIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return baseBillDomainService.invoiceBatch(billIds,supCpUnitId,billIdsMap);
    }

    /**
     * 完成开票
     * 修改 收款单表[gather_bill] 开票金额、开票状态
     * 修改 账单表 开票状态、挂账状态、开票金额
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
     * 批量作废,红冲开票金额
     *
     * @param invoiceVoidBatchFList
     * @return
     */
    public Boolean invoiceVoidBatch(List<InvoiceVoidBatchF> invoiceVoidBatchFList) {
        List<FinishInvoiceCommand> finishInvoiceCommands = Global.mapperFacade.mapAsList(invoiceVoidBatchFList, FinishInvoiceCommand.class);
        return baseBillDomainService.invoiceVoidBatch(finishInvoiceCommands, finishInvoiceCommands.get(0).getSupCpUnitId());
    }


    /**
     * 根据账单ids获取结算记录
     *
     * @param billIds
     * @return
     */
    public List<BillSettleDto> getBillSettle(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.getBillSettle(billIds,BillTypeEnum.预收账单, supCpUnitId);
    }

    /**
     * 冲销
     *
     * @param advanceBillId
     * @return
     */
    public Boolean reverse(Long advanceBillId,String extField1) {
        return baseBillDomainService.reverse(advanceBillId,extField1,null);
    }


    /**
     * 回滚冲销
     *
     * @param advanceBillId
     * @return
     */
    public Boolean robackReverse(Long advanceBillId,String supCpUnitId) {
        return baseBillDomainService.robackReverse(advanceBillId,supCpUnitId);
    }

    /**
     * 导出预收单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        baseBillDomainService.export(queryF,response);
    }

    /**
     * 查询预收账单最大账单结束时间
     *
     * @param maxEndTimeBillF 查询条件
     * @return AdvanceMaxEndTimeV
     */
    public AdvanceMaxEndTimeV queryMaxEndTime(AdvanceMaxEndTimeBillF maxEndTimeBillF) {
        return baseBillDomainService.queryMaxEndTime(maxEndTimeBillF);
    }

    public List<Long> getAdvanceRoomIds(String communityId, Long chargeItemId) {
        return baseBillDomainService.getAdvanceRoomIds(communityId, chargeItemId);
    }

    /**
     * 根据房间号查询预收菜单信息
     * @param roomIds 房间号集合
     * @return 房间号下预收账单集合
     */
    public List<AdvanceBillDetailV> getAdvanceBillByRoomIds(List<Long> roomIds) {
        List<AdvanceBill> advanceBillList = baseBillDomainService.getAdvanceBillByRoomIds(roomIds);
        return Global.mapperFacade.mapAsList(advanceBillList, AdvanceBillDetailV.class);
    }

    public AdvanceBillTotalMoneyV getAdvanceBillTotalMoney(String payerId,String communityId) {
        return advanceBillDomainService.getAdvanceBillTotalMoney(payerId, communityId);
    }

    public PageV<AdvanceBillGroupDetailDto> queryPageApprove(PageF<SearchF<?>> queryF) {
        IPage page = advanceBillDomainService.queryPageApprove(queryF);
        List<AdvanceBillGroupDetailDto> list = Global.mapperFacade.mapAsList(page.getRecords(), AdvanceBillGroupDetailDto.class);
        for (AdvanceBillGroupDetailDto rbd : list) {
            rbd.setPayInfosString(advanceBillDomainService.getPayInfosPaychannelString(rbd));
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), list);
    }

    public Boolean deleteInitBill(PageF<SearchF<?>> queryF) {
        return advanceBillDomainService.deleteInitBill(queryF);
    }
}
