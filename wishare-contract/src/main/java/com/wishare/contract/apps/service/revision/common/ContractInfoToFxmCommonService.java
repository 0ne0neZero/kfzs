package com.wishare.contract.apps.service.revision.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.fo.revision.*;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.clients.SpaceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.vo.revision.CustomerRv;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludePlanFxmReceiptRecordE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludePlanFxmRecordE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsFundE;
import com.wishare.contract.domains.enums.PlanFxmType;
import com.wishare.contract.domains.enums.revision.PlanFxmPushStatus;
import com.wishare.contract.domains.enums.revision.PlanFxmPushType;
import com.wishare.starter.Global;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 同步枫行梦公共方法类
 */
@Service
@Slf4j
public class ContractInfoToFxmCommonService {

    @Setter(onMethod_ = {@Autowired})
    private SpaceFeignClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;

    @Value("${contract.fmx.enablePush:false}")
    private Boolean enablePush;

    /**
     * 合同新增、修改，同步枫行梦的合同同步接口
     *
     * @param concludeE
     * @return
     */
    public ContractInfoToSpaceResourceF contractInfoToFxm(ContractIncomeConcludeE concludeE, ContractIncomeConcludeExpandE contractRecordInfoE) {
        ContractInfoToSpaceResourceF contractInfoToSpaceResourceF = new ContractInfoToSpaceResourceF();
        contractInfoToSpaceResourceF.setFormId(concludeE.getFromid()); // 临时表单id
        contractInfoToSpaceResourceF.setAgreementId(concludeE.getId()); // 合同id
        contractInfoToSpaceResourceF.setAgreementNo(concludeE.getContractNo()); // 合同编号
        contractInfoToSpaceResourceF.setAgreementName(concludeE.getName()); // 合同名称
        contractInfoToSpaceResourceF.setTeamName(concludeE.getRegion());
        contractInfoToSpaceResourceF.setCityName(contractRecordInfoE.getConperformcityname());

        //对方类型 对方证件号码
        CustomerRv customerRv = orgFeignClient.getCustomerVById(concludeE.getOppositeOneId());
        if(ObjectUtils.isNotEmpty(customerRv)){
            log.info("查询客户信息" + JSONObject.toJSONString(customerRv));
            if(customerRv.getNature() == 2){
                contractInfoToSpaceResourceF.setBusinessType(1);
                contractInfoToSpaceResourceF.setCertNumber(customerRv.getLicenseNum());
            }
            if(customerRv.getNature() == 1){
                contractInfoToSpaceResourceF.setBusinessType(0);
                contractInfoToSpaceResourceF.setCertNumber(customerRv.getCreditCode());
            }
        }

        // 待确认
        contractInfoToSpaceResourceF.setAgreementType(0); // 合同类型：0：服务类合同 1：销售类（流水）合同
        contractInfoToSpaceResourceF.setCompanyName(concludeE.getOurParty()); // 我方名称
        contractInfoToSpaceResourceF.setManagerNickname(concludeE.getPrincipalName()); // 我方经办人
        if(StringUtils.isNotBlank(concludeE.getPrincipalId())){
            contractInfoToSpaceResourceF.setManagerPhone(Optional.ofNullable(userFeignClient.getUsreInfoByUserId(concludeE.getPrincipalId()).getMobileNum()).orElse("")); // 我方经办人电话
        }
        if(ObjectUtils.isNotEmpty(spaceFeignClient.getById(concludeE.getCommunityId()))){
            List<String> ss = new ArrayList<>();
            ss.add(spaceFeignClient.getById(concludeE.getCommunityId()).getSerialNumber());
            contractInfoToSpaceResourceF.setAgreementProjectIds(ss); // 关联的项目 pj 码
        }
        contractInfoToSpaceResourceF.setAgreementStartDate(concludeE.getGmtExpireStart()); // 合同生效日期
        contractInfoToSpaceResourceF.setAgreementEndDate(concludeE.getGmtExpireEnd()); // 合同到期时间
        // 待确认
        contractInfoToSpaceResourceF.setChannel(0); // 业务航道 0：社区资源业务合同 1：
        contractInfoToSpaceResourceF.setPartyName(concludeE.getOppositeOne()); // 对方名称
        contractInfoToSpaceResourceF.setCompanyRate(new BigDecimal("1000")); // 我司分成比例
        contractInfoToSpaceResourceF.setOwnerRate(new BigDecimal("10")); // 业主分成比例
        contractInfoToSpaceResourceF.setTotalAmount(concludeE.getContractAmountOriginalRate().multiply(new BigDecimal("100"))); // 合同总金额，以分计算
        contractInfoToSpaceResourceF.setTotalAmountAvg(concludeE.getContractAmountOriginalRate()); // 合同总金额，以元计算
        return contractInfoToSpaceResourceF;
    }

    public ContractInfoToSpaceResourceF contractTempInfoToFxm(ContractIncomeConcludeE concludeE) {
        ContractInfoToSpaceResourceF contractInfoToSpaceResourceF = new ContractInfoToSpaceResourceF();
        contractInfoToSpaceResourceF.setFormId(concludeE.getFromid()); // 临时表单id
        contractInfoToSpaceResourceF.setAgreementId(concludeE.getId()); // 合同id
        return contractInfoToSpaceResourceF;
    }

    /**
     * 合同状态变更
     *
     * @param agreementId
     * @param status
     * @return
     */
    public ContractStatusF contractStatusToFxm(String agreementId, Integer status) {
        ContractStatusF contractStatusF = new ContractStatusF();
        contractStatusF.setAgreementId(agreementId);
        contractStatusF.setStatus(status);
        return contractStatusF;
    }

    /**
     * 收款计划添加成功后，同步枫行梦的应收账单的接口
     *
     * @param incomePlanConclude
     * @param pushType
     * @return
     */
    public ReceivableBillF receivableBillInfoToFxm(ContractIncomePlanConcludeE incomePlanConclude, Integer pushType, ContractIncomeConcludeE contractIncomeConcludeE) {
        ReceivableBillF receivableBillF = new ReceivableBillF();
        //操作类型
        receivableBillF.setOperator(pushType);
        //合同id
        receivableBillF.setAgreementId(contractIncomeConcludeE.getId());
        //合同编号
        receivableBillF.setAgreementNo(contractIncomeConcludeE.getContractNo());
        //应收账单id
        receivableBillF.setAgreementBillId(incomePlanConclude.getId());
        //项目PJ码
        receivableBillF.setProjectCode(spaceFeignClient.getById(contractIncomeConcludeE.getCommunityId()).getSerialNumber());
        //应收金额（含税）
        receivableBillF.setAmount(incomePlanConclude.getPlannedCollectionAmount().multiply(new BigDecimal("100")));
        //我司分成比例
        receivableBillF.setCompanyRate(new BigDecimal("1000"));
        //业主分层比例
        receivableBillF.setOwnerRate(new BigDecimal("20"));
        //费用名称
        String[] chargeItemNames = incomePlanConclude.getChargeItem().split("/");
        receivableBillF.setFeeName(chargeItemNames[chargeItemNames.length - 1]);
        //费用id
        receivableBillF.setFeeNameConfigInfoId(incomePlanConclude.getChargeItemId());
        //计划收款日期
        receivableBillF.setFirstStartDate(incomePlanConclude.getPlannedCollectionTime());
        //开始日期
        receivableBillF.setStartDate(incomePlanConclude.getCostStartTime());
        //结束日期
        receivableBillF.setEndDate(incomePlanConclude.getCostEndTime());
        //收款周期类型
        receivableBillF.setFundPeriodType(convert2FundPeriodType(incomePlanConclude.getSplitMode()));
        //收入关系
        receivableBillF.setIncomeType(0);
        //分成类型
        receivableBillF.setPercentType(0);
        //备注
        receivableBillF.setRemark(incomePlanConclude.getRemark());
        //税率
        if (ObjectUtils.isNotEmpty(incomePlanConclude.getTaxRate())) {
            if ("差额纳税".equals(incomePlanConclude.getTaxRate())) {
                receivableBillF.setTaxRate(new BigDecimal("6").multiply(new BigDecimal("100")));
            } else {
                receivableBillF.setTaxRate(new BigDecimal(incomePlanConclude.getTaxRate()).multiply(new BigDecimal("100")));
            }
        }

        return receivableBillF;
    }

    /**
     * 合同测: 1->月 2->季度 3->半年 4->年 5->其它
     *
     * @param splitMode
     * @return
     */
    private Integer convert2FundPeriodType(Integer splitMode) {
        Integer fundPeriodType = null;
        if (splitMode == 4) {
            fundPeriodType = 0; //按年
        } else if (splitMode == 3) {
            fundPeriodType = 1; //按半年
        } else if (splitMode == 2) {
            fundPeriodType = 2; //按季度
        } else if (splitMode == 1) {
            fundPeriodType = 3; //按月
        } else if (splitMode == 5) {
            fundPeriodType = 4; //按单次
        }
        return fundPeriodType;
    }


    /**
     * 推送收款计划到枫行梦
     *
     * @param plans
     * @param concludeE
     * @param pushType
     * @return
     */
    public List<ContractIncomeConcludePlanFxmRecordE> pushIncomePlan2Fxm(List<ContractIncomePlanConcludeE> plans, ContractIncomeConcludeE concludeE, PlanFxmPushType pushType) {
        List<ContractIncomeConcludePlanFxmRecordE> recordList = new ArrayList<>();
        //如果没有开启推送或者plans为空，直接返回
        if (!enablePush || CollectionUtils.isEmpty(plans)) {
            return recordList;
        }
        for (ContractIncomePlanConcludeE plan : plans) {
            ReceivableBillF receivableBillF = this.receivableBillInfoToFxm(plan, pushType.getCode(), concludeE);
            String requestBody = JSON.toJSONString(receivableBillF);
            log.info("收款计划推送枫行梦请求体数据:{}", requestBody);
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            contractBasePullF.setType(PlanFxmPushType.INSERT.equals(pushType) ? 0 : 1);
            Integer pushStatus = PlanFxmPushStatus.SUCCESS.getCode();
            String pushMessage = "成功";
            try {
                Boolean isSuccess = externalFeignClient.contractReceivableBill(contractBasePullF);
                if (!isSuccess) {
                    //如果是false，记录推送失败
                    pushStatus = PlanFxmPushStatus.FAILED.getCode();
                    pushMessage = "失败";
                }
            } catch (Exception e) {
                //推送异常，记录异常信息
                log.error("推送收款计划到枫行梦失败,msg:{}", e.getMessage(), e);
                pushStatus = PlanFxmPushStatus.FAILED.getCode();
                pushMessage = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
            }
            //属性对拷
            ContractIncomeConcludePlanFxmRecordE recordE = Global.mapperFacade.map(receivableBillF, ContractIncomeConcludePlanFxmRecordE.class);
            recordE.setPushType(pushType.getCode());
            recordE.setPlanType(PlanFxmType.INCOME.getCode());
            recordE.setPushStatus(pushStatus);
            recordE.setPushMessage(pushMessage);
            recordList.add(recordE);
        }

        return recordList;
    }

    /**
     * 损益添加成功后，同步枫行梦的财务账单的接口
     *
     * @param incomePlanConclude
     * @param operator
     * @return
     */
    public FinanceBillF financeBillInfoToFxm(ContractIncomeConcludeProfitLossE incomePlanConclude, Integer operator, ContractIncomeConcludeE contractIncomeConcludeE) {
        FinanceBillF financeBillF = new FinanceBillF();
        financeBillF.setOperator(operator); //操作类型 1： 新增 2：变更 3: 废除
        financeBillF.setAgreementId(contractIncomeConcludeE.getId()); //合同 ID，必填
        financeBillF.setAgreementNo(incomePlanConclude.getContractNo());
        financeBillF.setAgreementFinBillId(incomePlanConclude.getId()); // 财务账单 ID
        financeBillF.setProjectCode(spaceFeignClient.getById(contractIncomeConcludeE.getCommunityId()).getSerialNumber()); //项目 ID，必填
        financeBillF.setAmount(incomePlanConclude.getPlannedCollectionAmount().multiply(new BigDecimal("100"))); //应收金额（含税）,以分计数，必填

        financeBillF.setCompanyRate(new BigDecimal("1000")); //我司分成比例，以 0.01%计数，必填
        financeBillF.setOwnerRate(new BigDecimal("1000")); //业主分层比例，必填
        String[] ss = incomePlanConclude.getChargeItem().split("/");
        financeBillF.setFeeName(ss[ss.length-1]); //费用名称，必填
        financeBillF.setFirstStartDate(incomePlanConclude.getPlannedCollectionTime()); //计划收款日期，必填
        // 合同起止期?
        financeBillF.setStartDate(contractIncomeConcludeE.getGmtExpireStart()); //计费开始日期，必填
        financeBillF.setEndDate(contractIncomeConcludeE.getGmtExpireEnd()); //计费结束日期，必填
        // 拆分方式?
        financeBillF.setFundPeriodType(incomePlanConclude.getSplitMode()); //收款周期类型：0、按年，1、按半年，2、按季度，3、按月，4、按单次，必填
        financeBillF.setIncomeType(0); //收入关系：0、计收入类，1、不计入收入类，必填
        financeBillF.setPercentType(0); //分成类型：0、按收入，1、按受益，2、不分成，必填
        financeBillF.setRemark(incomePlanConclude.getRemark()); //备注，选填
        String taxRate = incomePlanConclude.getTaxRate();
        financeBillF.setTaxRate(new BigDecimal(taxRate).multiply(new BigDecimal("100"))); //税率,以 0.01%计数，必填
        return financeBillF;
    }

    /**
     * 收款成功后，同步枫行梦的收款接口
     *
     * @param
     * @return
     */
    public ReceiptRecordF receiptRecordInfoToFxm(ContractSettlementsFundE map, ContractIncomePlanConcludeE map1,ContractIncomeConcludeE map2) {
        ReceiptRecordF receiptRecordF = new ReceiptRecordF();
        receiptRecordF.setAgreementId(map2.getId());  //合同 ID，必填
        receiptRecordF.setAgreementNo(map2.getContractNo());//合同编号
        receiptRecordF.setAgreementBillId(map1.getId());   //应收账单 ID，必填
        receiptRecordF.setDatetime(LocalDate.now());  //实收日期,必填
//        receiptRecordF.setCertUrl();  //收款凭证图片地址，可以填多个，逗号间隔
        receiptRecordF.setInvoiceType(1);  //发票类型：0、未开票，1、已开票，2、不需要开票 ，必填
        receiptRecordF.setPayAmount(map.getAmount().multiply(new BigDecimal("100"))); //收款金额, 以分计算，必填
        receiptRecordF.setPayType(4); //收款方式, 可以写 1:刷卡 2: 微信 3:支付宝 4:支票 5:转账 6:银行托收 ，必填
        receiptRecordF.setRemark(map.getRemark()); //备注,选填
        return receiptRecordF;
    }

    public ReceiptRecordF receiptRecordInfoToFxmV2(ContractIncomePlanConcludeE plan, ContractIncomeConcludeE concludeE) {
        ReceiptRecordF receiptRecordF = new ReceiptRecordF();
        //合同id
        receiptRecordF.setAgreementId(concludeE.getId());
        //合同编号
        receiptRecordF.setAgreementNo(concludeE.getContractNo());
        //应收账单id
        receiptRecordF.setAgreementBillId(plan.getId());
        //实收日期
        receiptRecordF.setDatetime(LocalDate.now());
        //发票类型
        receiptRecordF.setInvoiceType(1);
        //收款金额
        receiptRecordF.setPayAmount(plan.getSettlementAmount().multiply(new BigDecimal("100")));
        //收款方式
        receiptRecordF.setPayType(4);
        //备注
        receiptRecordF.setRemark(plan.getRemark());

        return receiptRecordF;
    }

    /**
     * 推送收款计划实际核销金额到枫行梦
     *
     * @param plans
     * @param concludeE
     * @return
     */
    public List<ContractIncomeConcludePlanFxmReceiptRecordE> pushIncomePlanReceipt2Fxm(List<ContractIncomePlanConcludeE> plans, ContractIncomeConcludeE concludeE) {
        List<ContractIncomeConcludePlanFxmReceiptRecordE> recordList = new ArrayList<>();
        //如果没有开启推送或者plans为空，直接返回
        if (!enablePush || CollectionUtils.isEmpty(plans)) {
            return recordList;
        }
        for (ContractIncomePlanConcludeE plan : plans) {
            //如果收款计划是"未结算"，不处理
            if (plan.getPaymentStatus() == 0) {
                continue;
            }
            ReceiptRecordF receiptRecordF = this.receiptRecordInfoToFxmV2(plan, concludeE);
            String requestBody = JSON.toJSONString(receiptRecordF);
            log.info("收款计划核销金额推送枫行梦请求体数据:{}", requestBody);
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            contractBasePullF.setRequestBody(requestBody);
            contractBasePullF.setType(0);
            Integer pushStatus = PlanFxmPushStatus.SUCCESS.getCode();
            String pushMessage = "成功";
            try {
                Boolean isSuccess = externalFeignClient.contractReceiptRecord(contractBasePullF);
                if (!isSuccess) {
                    //如果是false，记录推送失败
                    pushStatus = PlanFxmPushStatus.FAILED.getCode();
                    pushMessage = "失败";
                }
            } catch (Exception e) {
                //推送异常，记录异常信息
                log.error("推送收款计划Receipt到枫行梦失败,msg:{}", e.getMessage(), e);
                pushStatus = PlanFxmPushStatus.FAILED.getCode();
                pushMessage = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
            }
            //属性对拷
            ContractIncomeConcludePlanFxmReceiptRecordE recordE = Global.mapperFacade.map(receiptRecordF, ContractIncomeConcludePlanFxmReceiptRecordE.class);
            recordE.setPlanType(PlanFxmType.INCOME.getCode());
            recordE.setFinishApproveTime(receiptRecordF.getDatetime());
            recordE.setPushStatus(pushStatus);
            recordE.setPushMessage(pushMessage);
            recordList.add(recordE);
        }

        return recordList;
    }
}
