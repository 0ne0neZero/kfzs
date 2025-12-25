package com.wishare.finance.apis.yuanyang;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.vo.BillTransactionV;
import com.wishare.finance.apps.model.bill.vo.ProcessVoucherResultV;
import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.apps.model.yuanyang.vo.BusinessSyncVoucherV;
import com.wishare.finance.apps.model.yuanyang.vo.ReimbursementNotifyV;
import com.wishare.finance.apps.model.yuanyang.vo.ReimbursementSyncVoucherV;
import com.wishare.finance.apps.service.yuanyang.ReimbursementAppService;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.bill.repository.TransactionOrderRepository;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.ProcessStateEnum;
import com.wishare.finance.domains.voucher.entity.BusinessBill;
import com.wishare.finance.domains.voucher.entity.VoucherBook;
import com.wishare.finance.domains.voucher.entity.VoucherMakeError;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.finance.domains.voucher.repository.VoucherTemplateRepository;
import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.PushMode;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherStrategyCommand;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.support.lock.LockHelper;
import com.wishare.finance.infrastructure.support.lock.Locker;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 远洋报销
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Api(tags = {"远洋报销"})
@Validated
@RestController
@RequestMapping("/yaunyang")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReimbursementApi {

    private final ReimbursementAppService reimbursementAppService;

    private final TransactionOrderRepository transactionOrderRepository;

    @ApiOperation("报销入账")
    @PostMapping("/reimburse")
    public BillTransactionV reimburse(@RequestBody @Validated ReimbursementF reimbursementF){
        BillTransactionV billTransactionV = null;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.TRANSACTION, reimbursementF.getBizTransactionNo()))){
            billTransactionV = reimbursementAppService.reimburse(reimbursementF);
        }catch (BizException e){
            billTransactionV = new BillTransactionV();
            billTransactionV.setBizTransactionNo(reimbursementF.getBizTransactionNo());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
        }
        return billTransactionV;
    }

    @ApiOperation("差旅报销")
    @PostMapping("/travelReimburse")
    public BillTransactionV travelReimburse(@RequestBody @Validated TravelReimbursementF travelReimbursementF){
        BillTransactionV billTransactionV = null;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.TRANSACTION, travelReimbursementF.getBizTransactionNo()))){
            billTransactionV = reimbursementAppService.travelReimburse(travelReimbursementF);
        }catch (BizException e){
            billTransactionV = new BillTransactionV();
            billTransactionV.setBizTransactionNo(travelReimbursementF.getBizTransactionNo());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
        }
        return billTransactionV;
    }

    @ApiOperation("资金下拨")
    @PostMapping("/allocateFunds")
    public BillTransactionV allocateFunds(@RequestBody @Validated AllocateFundsReimbursementF allocateFundsReimbursementF){
        BillTransactionV billTransactionV = null;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.TRANSACTION, allocateFundsReimbursementF.getBizTransactionNo()))){
            billTransactionV = reimbursementAppService.allocateFundsReimburse(allocateFundsReimbursementF);
        }catch (BizException e){
            billTransactionV = new BillTransactionV();
            billTransactionV.setBizTransactionNo(allocateFundsReimbursementF.getBizTransactionNo());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
        }
        return billTransactionV;
    }

    @ApiOperation("资金上缴")
    @PostMapping("/turnoverFunds")
    public BillTransactionV turnoverFunds(@RequestBody @Validated TurnoverFundsReimbursementF turnoverFundsReimbursementF){
        BillTransactionV billTransactionV = null;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.TRANSACTION, turnoverFundsReimbursementF.getBizTransactionNo()))){
            billTransactionV = reimbursementAppService.turnoverReimburse(turnoverFundsReimbursementF);
        }catch (BizException e){
            billTransactionV = new BillTransactionV();
            billTransactionV.setBizTransactionNo(turnoverFundsReimbursementF.getBizTransactionNo());
            billTransactionV.setErrCode("999999");
            billTransactionV.setErrMsg(e.getMessage());
        }
        return billTransactionV;
    }


    @ApiOperation("查询报销交易信息")
    @GetMapping("/query/reimburse")
    public ReimbursementNotifyV getReimburseInfo(@RequestParam("transactionNo") String transactionNo){
        TransactionOrder transactionOrder = transactionOrderRepository.getByBizTransactionNo(transactionNo);
        ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
        ReimbursementNotifyV reimbursementNotifyV = Global.mapperFacade.map(transactionOrder, ReimbursementNotifyV.class);
        //凭证记录
        return reimbursementNotifyV;
    }

    @ApiOperation("同步报销凭证")
    @PostMapping("/reimburseSync")
    public ReimbursementSyncVoucherV reimbursementSyncVoucher(@RequestBody @Validated ReimbursementSyncVoucherF reimbursementSyncVoucherF){
        return reimbursementAppService.reimburseSyncVoucher(reimbursementSyncVoucherF.getBizTransactionNo());
    }

    @ApiOperation("同步报销凭证（新）")
    @PostMapping("/businessSync")
    public BusinessSyncVoucherV businessSync(@RequestBody @Validated ReimbursementSyncVoucherF reimbursementSyncVoucherF){
        return reimbursementAppService.businessSync(reimbursementSyncVoucherF.getBizTransactionNo());
    }

    @ApiOperation("BPM凭证流程统一入口")
    @PostMapping("/businessProcessHandle")
    public ProcessVoucherResultV businessProcessHandle(@RequestBody @Validated BusinessProcessHandleF businessProcessHandleF) {
        ProcessVoucherResultV processVoucherResultV;
        try (LockHelper ignored = LockHelper.tryLock(Locker.of(LockerEnum.TRANSACTION, businessProcessHandleF.getBusinessId()))){
            processVoucherResultV = reimbursementAppService.businessProcessHandle(businessProcessHandleF);
        }catch (BizException e){
            processVoucherResultV = new ProcessVoucherResultV();
            processVoucherResultV.setBusinessId(businessProcessHandleF.getBusinessId());
            processVoucherResultV.setErrCode("999999");
            processVoucherResultV.setErrMsg(e.getMessage());
            processVoucherResultV.setState(ProcessStateEnum.失败.getCode());
        }
        return processVoucherResultV;
    }


    @ApiOperation("获取BPM跳转链接")
    @GetMapping("/getBPMLink")
    public String getBPMLink(@RequestParam("voucherId") String  voucherId){
        return reimbursementAppService.getBPMLink(voucherId);
    }

    // @Autowired
    // private VoucherTemplateRepository voucherTemplateRepository;
    //
    // @ApiOperation("测试凭证生成")
    // @GetMapping("/test")
    // public void test() {
    //     AbstractVoucherStrategy strategy = new AbstractVoucherStrategy(PushMode.手动,
    //             VoucherEventTypeEnum.收款结算) {
    //         @Override
    //         public List<VoucherBusinessBill> businessBills(VoucherStrategyCommand command,
    //                 List conditions) {
    //             ;
    //             return null;
    //         }
    //
    //         @Override
    //         public VoucherRuleRecord execute(VoucherStrategyCommand command) {
    //             return null;
    //         }
    //     };
    //     VoucherBusinessBill bill = JSON.parseObject("{\"payerId\": \"16932952326833\", \"sceneId\": \"170076471125297\", \"cpUnitId\": \"16109027511712\", \"payerType\": 0, \"sceneType\": 4, \"sysSource\": 1, \"taxAmount\": 5752, \"taxRateId\": 12068740562901, \"cpUnitName\": \"一期-8号楼-第2单元-8-2-1205\", \"customerId\": \"16932952326833\", \"accountDate\": \"2024-06-01\", \"accountYear\": 2024, \"sbAccountId\": 133909478083269, \"supCpUnitId\": \"91cfa767789c771f95584c1d5c5fec6c\", \"totalAmount\": 50000, \"businessCode\": \"\", \"businessName\": \"\", \"chargeItemId\": 11893882195355, \"costCenterId\": 13043436889224, \"customerName\": \"高岩枫\", \"customerType\": 0, \"supCpUnitName\": \"苏州远洋览月阁项目\", \"businessBillId\": 165490113685177, \"businessBillNo\": \"YYFWZD20240517001336\", \"businessUnitId\": 135123345707380, \"chargeItemName\": \"公共能耗费\", \"costCenterName\": \"苏州览月阁项目\", \"discountAmount\": 0, \"originalAmount\": -44248, \"actualPayAmount\": 0, \"originalPayerId\": \"14476320778539\", \"statutoryBodyId\": 12925044842726, \"businessBillType\": 1, \"deductibleAmount\": 0, \"receivableAmount\": 50000, \"originalPayerType\": 1, \"statutoryBodyName\": \"远洋亿家物业服务股份有限公司苏州分公司\"}", VoucherBusinessBill.class);
    //     List<VoucherBusinessBill> bills = new ArrayList<>();
    //     bills.add(bill);
    //     VoucherTemplate byId = voucherTemplateRepository.getById(169404694495101L);
    //     VoucherRuleRecord voucherRuleRecord = new VoucherRuleRecord();
    //     voucherRuleRecord.setEventType(VoucherEventTypeEnum.账单调整.getCode());
    //     strategy.doMakeVoucher(new VoucherBook(), byId, new BusinessUnitE(), bills, new VoucherMakeError(),
    //             voucherRuleRecord);
    // }

}
