package com.wishare.finance.apis.reconciliation;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.wishare.finance.apps.model.reconciliation.fo.*;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationDetailPageV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationDetailV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationPageV;
import com.wishare.finance.apps.scheduler.bill.TLReconciliationFileSchedule;
import com.wishare.finance.apps.service.reconciliation.ReconciliationAppService;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.domains.reconciliation.service.ReconciliationDomainService;
import com.wishare.finance.domains.reconciliation.service.ReconciliationYinlianDomainService;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对账单接口
 *
 * @Author dxclay
 * @Date 2022/10/12
 * @Version 1.0
 */
@Api(tags = {"对账管理"})
@RestController
@RequestMapping("/reconciliation")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationApi {

    private final ReconciliationAppService reconciliationAppService;
    private final TLReconciliationFileSchedule tlReconciliationFileSchedule;
    private final ReconciliationYinlianDomainService reconciliationYinlianDomainService;
    private final ReconciliationRepository reconciliationRepository;
    private final ReconciliationDomainService reconciliationDomainService;
    private final VoucherBillDetailRepository voucherBillDetailRepository;
    @PostMapping("/reconcile")
    @ApiOperation(value = "发起账票流水对账", notes = "发起账票流水对账")
    public Boolean reconcile(@RequestBody CommunityList communityList ){
        return reconciliationAppService.reconcile(null, ReconcileModeEnum.账票流水对账, communityList);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询账票对账单列表", notes = "分页查询账票对账单列表")
    public PageV<ReconciliationPageV> reconciliationPage(@Validated @RequestBody PageF<SearchF<ReconciliationE>> pageF) {
        return reconciliationAppService.reconciliationPage(pageF);
    }

    @PostMapping("/page/clear")
    @ApiOperation(value = "分页查询清分对账单列表", notes = "分页查询清分对账单列表")
    public PageV<ReconciliationPageV> reconciliationPageClear(@Validated @RequestBody PageF<SearchF<ReconciliationE>> pageF) {
        return reconciliationAppService.reconciliationPageClear(pageF);
    }

    @PostMapping("/detail/page")
    @ApiOperation(value = "分页查询对账详情列表", notes = "分页查询对账详情列表")
    public PageV<ReconciliationDetailPageV> reconciliationDetailPage(@Validated @RequestBody PageF<SearchF<ReconciliationDetailE>> pageF){
        long l = System.currentTimeMillis();
        PageV<ReconciliationDetailPageV> reconciliationDetailPageVPageV = RepositoryUtil.convertMoneyPage(reconciliationAppService.reconciliationDetailPage(pageF), ReconciliationDetailPageV.class);
        List<ReconciliationDetailPageV> reconciliationDetailPageVS = reconciliationDetailPageVList(reconciliationDetailPageVPageV);
        log.info("reconciliationDetailPage查询耗时-{}", DateUtil.spendMs(l));
        l = System.currentTimeMillis();

        long a = System.currentTimeMillis();
        // 查找收款单号
        for (ReconciliationDetailPageV reconciliationDetailPageV : reconciliationDetailPageVS) {
            reconciliationDetailPageV.setRefundCarriedAmount(reconciliationDetailPageV.getCarriedAmount().add(reconciliationDetailPageV.getRefundAmount()));
            ReconciliationE byId = reconciliationRepository.getById(reconciliationDetailPageV.getReconciliationId());
            List<String> billNoList = StrUtil.split(reconciliationDetailPageV.getBillNo(), ',');
            List<ReconciliationRecBillDetailOBV> recBillDetailsById = reconciliationDomainService.getRecBillDetailsByBillNoList(billNoList, byId.getCommunityId());
            log.info("reconciliationDomainService.getRecBillDetailsByBillNoList查询耗时-{}", DateUtil.spendMs(l));
            l = System.currentTimeMillis();
            List<ReconciliationRecBillDetailOBV> recBillDetailsByIdSorted = recBillDetailsById.stream().sorted(Comparator.comparing(ReconciliationRecBillDetailOBV::getBillId)).collect(Collectors.toList());
            // 账单id
            List<Long> collect = recBillDetailsById.stream().map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList());
            if (null != collect && collect.size() > 0) {
                // 账单id

                List<VoucherPushBillDetail> pushDetails = voucherBillDetailRepository.getPushDetails(collect);
                log.info("voucherBillDetailRepository.getPushDetails查询耗时-{}", DateUtil.spendMs(l));
                l = System.currentTimeMillis();
                List<VoucherPushBillDetail> pushDetailsSorted = pushDetails.stream().sorted(Comparator.comparing(VoucherPushBillDetail::getBillId)).collect(Collectors.toList());
                Map<Long, List<VoucherPushBillDetail>> pushDetailsMap = pushDetailsSorted.stream().collect(Collectors.groupingBy(VoucherPushBillDetail::getBillId));

                for (int i = 0; i < recBillDetailsByIdSorted.size(); i++) {
                    Long billId = recBillDetailsByIdSorted.get(i).getBillId();
                    List<VoucherPushBillDetail> voucherPushBillDetails = pushDetailsMap.get(billId);
                    if (CollectionUtils.isNotEmpty(voucherPushBillDetails)){
                        StringBuilder stringBuilder = new StringBuilder();
                        for (VoucherPushBillDetail voucherPushBillDetail : voucherPushBillDetails) {
                            stringBuilder.append(voucherPushBillDetail.getVoucherBillNo());
                            stringBuilder.append(",");
                        }
                        if (StringUtils.isNotBlank(stringBuilder.toString())) {
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            recBillDetailsByIdSorted.get(i).setVoucherBillNo(stringBuilder.toString());
                        }
                    }
                }
            }
            List<Long> idList = recBillDetailsById.stream().map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList());
            List<String> noList = recBillDetailsById.stream().map(ReconciliationRecBillDetailOBV::getBillNo).collect(Collectors.toList());
            reconciliationDetailPageV.setRecBillIdList(idList);
            reconciliationDetailPageV.setRecBillNoList(noList);
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : noList) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
            if (StringUtils.isNotBlank(stringBuilder.toString())){
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            reconciliationDetailPageV.setRecBillNoStr(stringBuilder.toString());
            reconciliationDetailPageV.setRecBillDetails(recBillDetailsByIdSorted);
        }
        log.info("for循环总耗时-{}", DateUtil.spendMs(a));
        return reconciliationDetailPageVPageV;
    }
    @PostMapping("/unreconciliation")
    @ApiOperation(value = "解除对账", notes = "解除对账")
    public Boolean unReconciliation(@Validated @RequestBody UnclaimF unclaimF){
        return reconciliationAppService.unReconciliation(unclaimF);
    }


    @GetMapping("/detail")
    @ApiOperation(value = "查询账单对账详情", notes = "查询账单对账详情")
    public ReconciliationDetailV reconciliationDetail(@RequestParam("billId") Long billId, @RequestParam("reconciliationId") Long reconciliationId
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return reconciliationAppService.reconciliationDetail(billId, reconciliationId, supCpUnitId);
    }

    @GetMapping("/billInvoice/detail")
    @ApiOperation(value = "查询账票对账详情", notes = "查询账票对账详情")
    public ReconciliationDetailV getBillInvoiceDetail(@RequestParam("billId") Long billId, @RequestParam("reconciliationId") Long reconciliationId
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return reconciliationAppService.reconciliationDetail(billId, reconciliationId, supCpUnitId);
    }

    @PostMapping("/unclaim")
    @ApiOperation(value = "解除认领", notes = "解除认领")
    public Boolean unclaim(@Validated @RequestBody UnclaimF unclaimF){
        return reconciliationAppService.unclaim(unclaimF);
    }


    @PostMapping("/reconcileMerchant/clearing")
    @ApiOperation(value = "发起清分对账", notes = "发起清分对账")
    public Boolean reconcileMerchantClearing(@RequestBody ReconcileMerchantClearingF reconcileMerchantClearingF) {
        return reconciliationAppService.reconcileMerchantClearing(null, ReconcileModeEnum.商户清分对账, reconcileMerchantClearingF);
    }

    @PostMapping("/reconcileMerchant/test")
    @ApiOperation(value = "测试通联对账文件下载定时任务", notes = "测试通联对账文件下载定时任务")
    public void testTLFileDownload() {
        try {
             tlReconciliationFileSchedule.accountCheckingTLFileHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/clear/getClearStatistics")
    @ApiOperation(value = "商户清分对账计算统计", notes = "商户清分对账计算统计")
    public ReconciliationClearStatisticsF getReconciliationClearStatistics(@Validated @RequestBody PageF<SearchF<ReconciliationE>> pageF){
        return reconciliationAppService.getReconciliationClearStatistics(pageF);
    }

    @PostMapping("/clear/getClearDetailStatistics")
    @ApiOperation(value = "商户清分对账详情统计", notes = "商户清分对账详情统计")
    public ReconciliationClearStatisticsF reconciliationClearDetailStatistics(@Validated @RequestBody PageF<SearchF<ReconciliationDetailE>> pageF){
        return reconciliationAppService.reconciliationClearDetailStatistics(pageF);
    }

    @PostMapping("/getStatistics")
    @ApiOperation(value = "账票流水对账数据统计", notes = "账票流水对账数据统计")
    public ReconciliationStatisticsF getReconciliationStatistics(@Validated @RequestBody PageF<SearchF<ReconciliationE>> pageF) {
        return reconciliationAppService.getReconciliationStatistics(pageF);
    }


    @PostMapping("/getDetailStatistics")
    @ApiOperation(value = "账票流水对账详情统计", notes = "账票流水对账详情统计")
    public ReconciliationStatisticsF reconciliationDetailStatistics(@Validated @RequestBody PageF<SearchF<ReconciliationDetailE>> pageF){
        return reconciliationAppService.reconciliationDetailStatistics(pageF);
    }

    private List<ReconciliationDetailPageV> reconciliationDetailPageVList(PageV<ReconciliationDetailPageV> pageV) {
        List<ReconciliationDetailPageV> records = pageV.getRecords();
        for (ReconciliationDetailPageV record : records) {
            List<String> idList = new ArrayList<>();
            List<String> noList = new ArrayList<>();
            String voucherBillId = record.getVoucherBillId();
            String VoucherBillNo = record.getVoucherBillNo();
            if (null != voucherBillId){
                String[] idSplit = voucherBillId.split(",");
                for (String s : idSplit) {
                    if (StringUtils.isNotBlank(s)){
                        idList.add(s);
                    }
                }
            }
            if (null != VoucherBillNo){
                String[] noSplit = VoucherBillNo.split(",");
                for (String s : noSplit) {
                    if (StringUtils.isNotBlank(s)){
                         noList.add(s);
                    }
                }
            }
            record.setVoucherBillNoList(noList);
            record.setVoucherBillIdList(idList);
        }
        return records;
    }
}
