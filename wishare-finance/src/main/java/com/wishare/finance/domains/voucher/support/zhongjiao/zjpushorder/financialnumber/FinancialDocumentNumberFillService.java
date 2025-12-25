package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.financialnumber;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.DocumentTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherPushBillZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjbill.ZJBillDataClient;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderDealResult;
import com.wishare.finance.infrastructure.remote.fo.zj.FinancialDocumentStatusListQueryBody;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author longhuadmin
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FinancialDocumentNumberFillService {

    private final VoucherPushBillDxZJMapper voucherPushBillDxZJMapper;
    private final VoucherPushBillZJMapper voucherPushBillZJMapper;

    private final ZJBillDataClient zjBillDataClient;

    @Data
    class FinancialNumberV {
        String financeId;
        String financeNo;
    }

    public void fillFinancialDocumentNumber() {
        fillNumberOnFundsReceipt();
        fillNumberOnRevenueRecognitionWithoutContract();
        fillNumberOnRevenueRecognitionWithContract();
        fillNumberOnSettlement();
        fillNumberOnPaymentApply();
    }

    @Transactional(rollbackFor = Exception.class)
    public void doFillWithoutContract(VoucherBillZJ voucherBillZJ, String businessCode) {
        FinancialNumberV numberV = doQueryOutSystem(voucherBillZJ.getVoucherBillNo(), businessCode);
        if (Objects.isNull(numberV)){
            return;
        }
        voucherBillZJ.setFinanceId(numberV.getFinanceId());
        voucherBillZJ.setFinanceNo(numberV.getFinanceNo());
        voucherPushBillZJMapper.updateById(voucherBillZJ);
    }


    @Transactional(rollbackFor = Exception.class)
    public void doFillWithContract(VoucherBillDxZJ voucherBillDxZJ, String businessCode){
        FinancialNumberV numberV = doQueryOutSystem(voucherBillDxZJ.getVoucherBillNo(), businessCode);
        if (Objects.isNull(numberV)){
            return;
        }
        voucherBillDxZJ.setFinanceId(numberV.getFinanceId());
        voucherBillDxZJ.setFinanceNo(numberV.getFinanceNo());
        voucherPushBillDxZJMapper.updateById(voucherBillDxZJ);
    }

    private FinancialNumberV doQueryOutSystem(String voucherBillNo, String businessCode){
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderStatusResultList";
        FinancialDocumentStatusListQueryBody queryBody = new FinancialDocumentStatusListQueryBody();
        queryBody.setDjbh(voucherBillNo);
        queryBody.setDjnm(voucherBillNo);
        queryBody.setLyxt("CCCG-DMC");
        queryBody.setQueryType(1);
        queryBody.setBusinessCode(businessCode);
        String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(queryBody), url);
        log.info("中交推单body返回信息 ：{}", body);
        OrderDealResult orderDeal = JSONObject.parseObject(body, OrderDealResult.class);
        if (Objects.isNull(orderDeal) || StringUtils.isBlank(orderDeal.getData())){
            return null;
        }

        List<FinancialDocumentStatusResultV> resultList =
                JSONObject.parseArray(orderDeal.getData(), FinancialDocumentStatusResultV.class);
        if (CollectionUtils.isEmpty(resultList)){
            return null;
        }
        log.info("财务云单据状态列表返回信息 ：{}", resultList);
        FinancialDocumentStatusResultV resultV = resultList.stream()
                .filter(e -> "6".equals(e.getDQHJ()) || "30".equals(e.getDQHJ()))
                .findFirst().orElse(null);
        if (Objects.isNull(resultV)){
            return null;
        }
        FinancialNumberV financialNumberV = new FinancialNumberV();
        financialNumberV.setFinanceId(resultV.getDJNM());
        financialNumberV.setFinanceNo(resultV.getDJBH());
        return financialNumberV;
    }

    /**
     * 资金收款单
     **/
    public void fillNumberOnFundsReceipt(){
        LambdaQueryWrapper<VoucherBillZJ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VoucherBillZJ::getBillEventType, 2)
                .notIn(VoucherBillZJ::getPushState, Lists.newArrayList(1,4))
                .isNull(VoucherBillZJ::getFinanceNo)
                .eq(VoucherBillZJ::getDeleted, 0);
        List<VoucherBillZJ> voucherBills = voucherPushBillZJMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(voucherBills)){
            return;
        }
        for (VoucherBillZJ voucherBill : voucherBills) {
            doFillWithoutContract(voucherBill, DocumentTypeEnum.资金收款单.getValue());
        }
    }

    /**
     * 收入确认单-小业主
     **/
    public void fillNumberOnRevenueRecognitionWithoutContract(){
        LambdaQueryWrapper<VoucherBillZJ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VoucherBillZJ::getBillEventType, 1)
                .notIn(VoucherBillZJ::getPushState, Lists.newArrayList(1,4))
                .isNull(VoucherBillZJ::getFinanceNo)
                .eq(VoucherBillZJ::getDeleted, 0);
        List<VoucherBillZJ> voucherBills = voucherPushBillZJMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(voucherBills)){
            return;
        }
        for (VoucherBillZJ voucherBill : voucherBills) {
            doFillWithoutContract(voucherBill, DocumentTypeEnum.收入确认单.getValue());
        }
    }

    /**
     * 收入确认单-合同
     **/
    public void fillNumberOnRevenueRecognitionWithContract(){
        LambdaQueryWrapper<VoucherBillDxZJ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(VoucherBillDxZJ::getBillEventType, Lists.newArrayList(5,6,8))
                .notIn(VoucherBillDxZJ::getPushState, Lists.newArrayList(1,4))
                .isNull(VoucherBillDxZJ::getFinanceNo)
                .eq(VoucherBillDxZJ::getDeleted, 0);
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(voucherBills)){
            return;
        }
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            doFillWithContract(voucherBill, DocumentTypeEnum.收入确认单.getValue());
        }
    }

    /**
     * 对下结算单
     **/
    public void fillNumberOnSettlement(){
        LambdaQueryWrapper<VoucherBillDxZJ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(VoucherBillDxZJ::getBillEventType, Lists.newArrayList(3,4,7))
                .notIn(VoucherBillDxZJ::getPushState, Lists.newArrayList(1,4))
                .isNull(VoucherBillDxZJ::getFinanceNo)
                .eq(VoucherBillDxZJ::getDeleted, 0);
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(voucherBills)){
            return;
        }
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            doFillWithContract(voucherBill, DocumentTypeEnum.对下结算计提.getValue());
        }
    }

    /**
     * 业务支付申请单
     **/
    public void fillNumberOnPaymentApply(){
        LambdaQueryWrapper<VoucherBillDxZJ> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VoucherBillDxZJ::getBillEventType, 9)
                .notIn(VoucherBillDxZJ::getPushState, Lists.newArrayList(1,4))
                .isNull(VoucherBillDxZJ::getFinanceNo)
                .eq(VoucherBillDxZJ::getDeleted, 0);
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(voucherBills)){
            return;
        }
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            doFillWithContract(voucherBill, DocumentTypeEnum.业务支付申请.getValue());
        }
    }

}
