//package com.wishare.contract.apps.remote.clients;
//
//import com.wishare.contract.apps.remote.fo.AddBillSettleRf;
//import com.wishare.contract.apps.remote.fo.AddTemporaryChargeBillRf;
//import com.wishare.contract.apps.remote.fo.BillRefundRf;
//import com.wishare.contract.apps.remote.fo.DeleteBatchBillRf;
//import com.wishare.contract.apps.remote.vo.BillDeleteBatchResultDto;
//import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
//import com.wishare.starter.annotations.OpenFeignClient;
//import com.wishare.starter.beans.PageF;
//import com.wishare.starter.beans.PageV;
//import com.wishare.tools.starter.fo.search.SearchF;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.List;
//
///**
// * @Author ljx
// * 财务中台调用  临时账单
// */
//@OpenFeignClient(name = "wishare-amp-finance", serverName = "财务中台", path = "/amp-finance/temporary")
//public interface FinanceTemporaryFeignClient {
//
//    /**
//     * 分页查询临时收费账单列表
//     */
//    @PostMapping("/page")
//    PageV<TemporaryChargeBillPageV> temporaryQueryPage(@Validated @RequestBody PageF<SearchF<?>> queryF);
//
//    /**
//     *批量新增临时收费账单
//     */
//    @PostMapping("/add/batch")
//    List<TemporaryChargeBillPageV> addBatch(@RequestBody AddTemporaryChargeBillRf addTemporaryChargeBillFs);
//
//    /**
//     * 批量结算
//     */
//    @PostMapping("/settle/batch")
//    Boolean settleBatch(@Validated @RequestBody List<AddBillSettleRf> form);
//
//    /**
//     * 账单退款
//     */
//    @PostMapping("/refund")
//    Boolean refund(@Validated @RequestBody BillRefundRf billRefundRf);
//
//    /**
//     * 批量删除临时收费账单
//     */
//    @DeleteMapping("/delete/batch")
//    BillDeleteBatchResultDto deleteBatch(@Validated @RequestBody DeleteBatchBillRf deleteBatchBillRf);
//}
