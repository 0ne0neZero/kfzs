package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanUpdateF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.fo.AddReceivableBillRf;
import com.wishare.contract.apps.remote.fo.ApproveBatchPayableBillRf;
import com.wishare.contract.apps.remote.fo.ApproveBatchReceivableBillRf;
import com.wishare.contract.apps.remote.vo.MerchantRv;
import com.wishare.contract.apps.remote.vo.OrgFinanceRv;
import com.wishare.contract.apps.remote.vo.ReceivableBillDetailRv;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.consts.ErrMsgEnum;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossPlanE;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.service.contractset.ContractProfitLossPlanService;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossPlanV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangrui
 * @since 2022-09-13
 */
@Service
@Slf4j
public class ContractProfitLossPlanAppService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    private static Calendar calendar = Calendar.getInstance();
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanService contractProfitLossPlanService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;

    public List<ContractProfitLossPlanV> getProfitLossPlanList(ContractProfitLossPlanF contractProfitLossPlanF) {
        return contractProfitLossPlanService.getProfitLossPlanList(contractProfitLossPlanF);
    }

    public Long saveContractProfitLossPlan(ContractProfitLossPlanSaveF contractProfitLossPlanF) {
        return contractProfitLossPlanService.saveContractProfitLossPlan(contractProfitLossPlanF);
    }

    public void updateContractProfitLossPlan(ContractProfitLossPlanUpdateF contractProfitLossPlanF) {
        contractProfitLossPlanService.updateContractProfitLossPlan(contractProfitLossPlanF);
    }

    public void deleteProfitLossPlan(Long collectionPlanId, Long contractId) {
        contractProfitLossPlanService.deleteProfitLossPlan(collectionPlanId, contractId);
    }

    public void deletePlan(Long contractId) {
        contractProfitLossPlanService.delete(contractId);
    }

    public List<ContractProfitLossPlanV> listProfitLossPlan(ContractProfitLossPlanF profitLossPlanF) {
        List<ContractProfitLossPlanV> profitLossPlanList = contractProfitLossPlanService.listProfitLossPlan(profitLossPlanF);
        if (CollectionUtils.isEmpty(profitLossPlanList)) {
            return null;
        }
        for (ContractProfitLossPlanV profitLossPlanListV : profitLossPlanList) {
            //费项
            if (null != financeFeignClient.chargeName(profitLossPlanListV.getChargeItemId())) {
                profitLossPlanListV.setChargeItemName(financeFeignClient.chargeName(profitLossPlanListV.getChargeItemId()));
            }
            //成本中心
            if (null != orgFeignClient.getByFinanceId(profitLossPlanListV.getCostId())) {
                profitLossPlanListV.setCostName(orgFeignClient.getByFinanceId(profitLossPlanListV.getCostId()).getNameCn());
            }
            //责任部门
            if (null != orgFeignClient.getByOrgId(profitLossPlanListV.getOrgId())) {
                profitLossPlanListV.setOrgName(orgFeignClient.getByOrgId(profitLossPlanListV.getOrgId()).getOrgName());
            }
        }
        return profitLossPlanList;
    }

    public BigDecimal selectAmountSum(Long id) {
        return contractProfitLossPlanService.selectAmountSum(id);
    }

    public BigDecimal localCurrencyAmount(Long id) {
        return contractProfitLossPlanService.localCurrencyAmount(id);
    }

//    @GlobalTransactional
    @Transactional(rollbackFor = {Exception.class})
    public void profitLossPlanCreateBill(Long collectionId, Long contractId, Integer collectionType,Boolean flag,List<ContractProfitLossPlanV> planVS) throws ParseException {
        ContractConcludeE byId = contractConcludeService.getById(contractId);
        ContractProfitLossPlanF profitLossPlanF = new ContractProfitLossPlanF();
//        profitLossPlanF.setCollectionId(collectionId);
        profitLossPlanF.setContractId(contractId);
        profitLossPlanF.setDeleted(0);
        List<ContractProfitLossPlanV> profitLossPlanVS = contractProfitLossPlanService.getProfitLossPlanList(profitLossPlanF);
        //如果传入指定推送的账单，只推送指定部分，不传入指定则推送全部的账单
        if(!CollectionUtils.isEmpty(planVS)){
            profitLossPlanVS=planVS;
        }
        List<AddReceivableBillRf> receivableBillRf = new ArrayList<>();
        //按照合同期或者服务期分摊的
        for (ContractProfitLossPlanV profitLossPlanV : profitLossPlanVS) {
            if(profitLossPlanV.getBillId() == null){
                AddReceivableBillRf receivableBillRf1 = new AddReceivableBillRf();
                receivableBillRf1.setBillMethod(Const.State._2);
                receivableBillRf1.setChargeItemId(profitLossPlanV.getChargeItemId());
                receivableBillRf1.setChargeItemName(financeFeignClient.chargeName(profitLossPlanV.getChargeItemId()));
                receivableBillRf1.setTaxRateId(profitLossPlanV.getTaxRateId());
                receivableBillRf1.setSource("合同");
                receivableBillRf1.setCostCenterId(profitLossPlanV.getCostId());
                receivableBillRf1.setCostCenterName(orgFeignClient.getByFinanceId(profitLossPlanV.getCostId()).getNameCn());
                if (flag) {
                    receivableBillRf1.setDescription("收款损益计划生成应收账单");
                    receivableBillRf1.setStatutoryBodyId(byId.getPartyBId());
                    if(null != byId.getPartyBId()){
                        OrgFinanceRv orgFinanceRv = orgFeignClient.getByFinanceId(byId.getPartyBId());
                        if(null != orgFinanceRv){
                            receivableBillRf1.setStatutoryBodyName(orgFinanceRv.getNameCn());
                            receivableBillRf1.setPayeeName(orgFinanceRv.getNameCn());
                        }
                    }
                    receivableBillRf1.setPayerType(Const.State._1);
                    receivableBillRf1.setPayerId(byId.getPartyAId().toString());
                    if(null != byId.getPartyAId()){
                        MerchantRv merchantRv = orgFeignClient.queryById(byId.getPartyAId());
                        if(null != merchantRv){
                            receivableBillRf1.setPayerName(merchantRv.getName());
                        }
                    }
                    receivableBillRf1.setPayerType(1);
                    receivableBillRf1.setPayeeId(byId.getPartyBId().toString());
                    receivableBillRf1.setPayeeType(4);
                } else {
                    receivableBillRf1.setDescription("收款损益计划生成应付账单");
                    receivableBillRf1.setStatutoryBodyId(byId.getPartyAId());
                    receivableBillRf1.setStatutoryBodyName(orgFeignClient.getByFinanceId(byId.getPartyAId()).getNameCn());
                    if(collectionType == null){
                        receivableBillRf1.setSettleChannel("OTHER");
                    }else{
                        if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_CASH)) {
                            receivableBillRf1.setSettleChannel("CASH");
                        } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_TRANSFER)){
                            receivableBillRf1.setSettleChannel("UNIONPAY");
                        } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_ALIPAY)){
                            receivableBillRf1.setSettleChannel("ALIPAY");
                        } else if (collectionType.equals(ContractSetConst.COLLECTION_METHOD_WECHAT)) {
                            receivableBillRf1.setSettleChannel("WECHATPAY");
                        }
                    }
                    receivableBillRf1.setPayerId(byId.getPartyAId().toString());
                    receivableBillRf1.setPayerName(orgFeignClient.getByFinanceId(byId.getPartyAId()).getNameCn());
                    receivableBillRf1.setPayerType(4);
                    receivableBillRf1.setPayeeId(byId.getPartyBId().toString());
                    receivableBillRf1.setPayeeName(orgFeignClient.queryById(byId.getPartyBId()).getName());
                    receivableBillRf1.setPayeeType(3);
                }
//                receivableBillRf1.setOutBusNo(profitLossPlanV.getId().toString());// 20221123 改为合同编号
                receivableBillRf1.setOutBusNo(byId.getContractNo());
                // 20221123 改为合同id
                receivableBillRf1.setOutBusId(contractId.toString());
                // 20221123 改为损益计划id
                receivableBillRf1.setExtField1(profitLossPlanV.getId().toString());
                if(StringUtils.isNotBlank(profitLossPlanV.getTaxRate())){
                    receivableBillRf1.setTaxRate(new BigDecimal(profitLossPlanV.getTaxRate()).multiply(new BigDecimal("0.01")));
                }
                receivableBillRf1.setInvoiceType("[" + profitLossPlanV.getBillType() + "]");
                //示例50.52转为5052
                Long amount = Long.parseLong(profitLossPlanV.getLocalCurrencyAmount().toPlainString().replace(".", ""));
                receivableBillRf1.setTotalAmount(amount);
                receivableBillRf1.setUnitPrice(amount);
                //正常情况下，确认时间就是开始时间，结束时间是当月的最后一天精确到23:59:59
                LocalDateTime startTime = profitLossPlanV.getConfirmTime().atStartOfDay();
                String month = profitLossPlanV.getConfirmTime().atStartOfDay().getYear() + String.valueOf(profitLossPlanV.getConfirmTime().atStartOfDay().getMonthValue());
                LocalDateTime endTime = getMaxDateMonth(month);
                if (byId.getGmtExpireEnd().compareTo(profitLossPlanV.getConfirmTime()) == 0 ||
                        (profitLossPlanV.getServiceEndDate() != null && profitLossPlanV.getServiceEndDate().compareTo(profitLossPlanV.getConfirmTime()) == 0)) {
                    //如果是末月，开始时间要重新计算为当月第一天精确到00:00:00
                    startTime = getMinDateMonth(month);
                    endTime = profitLossPlanV.getConfirmTime().atStartOfDay();
                }
                if (profitLossPlanVS.size() == Const.State._1) {
                    //按照时点分摊的开始时间和结束时间==合同开始时间和合同结束时间
                    startTime = byId.getGmtExpireStart().atStartOfDay();
                    endTime = getMaxDateMonth(byId.getGmtExpireEnd().atStartOfDay());
                }
                receivableBillRf1.setStartTime(startTime);
                receivableBillRf1.setEndTime(endTime);
                receivableBillRf1.setSysSource(2);
                receivableBillRf1.setAppId(ContractSetConst.CONTRACTAPPID);
                receivableBillRf1.setAppName(ContractSetConst.CONTRACTAPPNAME);
                receivableBillRf.add(receivableBillRf1);
            }
        }
        if (flag) {
            log.info("新增应收账单：" + JSON.toJSONString(receivableBillRf));
            if(!CollectionUtils.isEmpty(receivableBillRf)){
                //-- TODO supUnitId default
                for (AddReceivableBillRf billRf : receivableBillRf) {
                    billRf.setSupCpUnitId("default");
                }
                List<ReceivableBillDetailRv> result = financeFeignClient.addBatch(receivableBillRf);
                if (!CollectionUtils.isEmpty(result)) {
                    // 设置账单id
                    result.forEach(item -> {
                        contractProfitLossPlanService.updateBillId(Long.valueOf(item.getExtField1()), item.getId());
                    });
                    //审核账单
                    List<Long> billIds = result.stream().map(ReceivableBillDetailRv::getId).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(result)) {
                        throw BizException.throw400(ErrMsgEnum.NOT_BILLS.getErrMsg());
                    }
                    ApproveBatchReceivableBillRf approveBatchReceivableBillF = new ApproveBatchReceivableBillRf();
                    approveBatchReceivableBillF.setBillIds(billIds);
                    approveBatchReceivableBillF.setApproveState(2);
                    //-- TODO supUnitId default
                    approveBatchReceivableBillF.setSupCpUnitId("default");
                    Boolean approveBatch = financeFeignClient.approveBatch(approveBatchReceivableBillF);
                    if(!approveBatch){
                        throw BizException.throw400(ErrMsgEnum.EXAMINE_BILLS.getErrMsg());
                    }
                }
            }
        } else {
            log.info("新增应付账单：" + JSON.toJSONString(receivableBillRf));
            if(!CollectionUtils.isEmpty(receivableBillRf)){
                //-- TODO supUnitId default
                for (AddReceivableBillRf billRf : receivableBillRf) {
                    billRf.setSupCpUnitId("default");
                }
                List<ReceivableBillDetailRv> result1 = financeFeignClient.addPayableBatch(receivableBillRf);
                if(!CollectionUtils.isEmpty(result1)){
                    // 设置账单id
                    result1.forEach(item -> {
                        contractProfitLossPlanService.updateBillId(Long.valueOf(item.getExtField1()), item.getId());
                    });
                    //审核账单
                    List<Long> billIds = result1.stream().map(ReceivableBillDetailRv::getId).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(result1)) {
                        throw BizException.throw400(ErrMsgEnum.NOT_BILLS.getErrMsg());
                    }
                    ApproveBatchPayableBillRf payableApproveBillF = new ApproveBatchPayableBillRf();
                    payableApproveBillF.setBillIds(billIds);
                    payableApproveBillF.setApproveState(2);
                    //-- TODO supUnitId default
                    payableApproveBillF.setSupCpUnitId("default");
                    Boolean approveBatch = financeFeignClient.payableApproveBatch(payableApproveBillF);
                    if(!approveBatch){
                        throw BizException.throw400(ErrMsgEnum.EXAMINE_BILLS.getErrMsg());
                    }
                }
            }
        }
    }

    /**
     * 输入日期字符串比如202203，返回当月第一天的Date 00:00:00
     */
    public LocalDateTime getMinDateMonth(String month) throws ParseException {
        Date nowDate = sdf.parse(month);
        calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 输入日期字符串，返回当月最后一天的Date 23:59:59
     */
    public LocalDateTime getMaxDateMonth(String month) throws ParseException {
        Date nowDate = sdf.parse(month);
        calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd").
                format(calendar.getTime())+ " " + "23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 输入日期字符串，返回年月日+ 23:59:59
     */
    public LocalDateTime getMaxDateMonth(LocalDateTime localDateTime){
        String s1= DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        return LocalDateTime.parse(s1+ " " + "23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ContractProfitLossPlanE selectById(Long id) {
        return contractProfitLossPlanService.getById(id);
    }

    public List<ContractProfitLossPlanV> selectProfitLossPlanByIds(List<Long> ids) {
        return contractProfitLossPlanService.selectProfitLossPlanByIds(ids);
    }

    public void remove(Long id) {
        contractProfitLossPlanService.removePlan(id);
    }

    public List<ContractProfitLossPlanV> selectByContract(Long contractId) {
        return contractProfitLossPlanService.selectByContract(contractId);
    }

    public void updateProfitLossPlan(ContractProfitLossPlanUpdateF profitLossPlanUpdateF) {
        contractProfitLossPlanService.updateContractProfitLossPlan(profitLossPlanUpdateF);
    }

    public void saveProfitLossPlan(ContractProfitLossPlanSaveF profitLossPlanSaveF) {
        contractProfitLossPlanService.delete(profitLossPlanSaveF.getContractId());
        contractProfitLossPlanService.saveContractProfitLossPlan(profitLossPlanSaveF);
    }
}
