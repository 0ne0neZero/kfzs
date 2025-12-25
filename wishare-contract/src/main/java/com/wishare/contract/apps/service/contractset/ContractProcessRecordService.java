package com.wishare.contract.apps.service.contractset;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.fo.revision.SettBatchIdF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.UpdateTemporaryChargeBillF;
import com.wishare.contract.apps.remote.fo.VoucherBillGenerateOnContractSettlementF;
import com.wishare.contract.apps.fo.revision.income.ContractCttmsjF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandF;
import com.wishare.contract.apps.fo.revision.income.ParamCallBackInfoF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.fo.procreate.ProcessAdjustCallBackF;
import com.wishare.contract.apps.service.revision.income.ContractIncomeBusinessService;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.apps.service.revision.pay.ContractPayBusinessService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectInitiationAppService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectOrderAppService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectPlanCostConfirmAppService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectPlanMonthlyAllocationAppService;
import com.wishare.contract.apps.service.revision.relation.ContractRelationBusinessService;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeCorrectionE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillDetailsE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.PayCostPlanE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectInitiationE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectOrderE;
import com.wishare.contract.domains.enums.BillTypeEnum;
import com.wishare.contract.domains.enums.OperationTypeEnum;
import com.wishare.contract.domains.enums.revision.ContractConcludeEnum;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.ContractTypeEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.enums.settlement.SettlementTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeCorrectionMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillDetailsMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeExpandService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeSettlementConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPaySettlementConcludeService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectInitiationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectOrderInfoV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.StartOrderForJDReqF;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * 中建回调，返回合同审批相关数据
 *
 * @author long
 * @date 2023/7/18 17:02
 */
@Service
@Slf4j
public class ContractProcessRecordService {
    @Autowired
    private ContractPayConcludeService contractPayConcludeService;

    @Autowired
    private ContractIncomeConcludeService contractIncomeConcludeService;

    @Autowired
    private ContractRelationBusinessService contractRelationBusinessService;

    @Autowired
    private ContractOrgRelationService orgRelationService;

    @Autowired
    private ContractProcessRecordMapper contractProcessRecordMapper;

    @Autowired
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Autowired
    private ContractIncomeBusinessService contractIncomeBusinessService;

    @Autowired
    private ContractPayBusinessService contractPayBusinessService;

    @Autowired
    private ContractPaySettlementConcludeService contractPaySettlementConcludeService;

    @Autowired
    private ContractIncomeSettlementConcludeService contractIncomeSettlementConcludeService;

    @Autowired
    private ContractProjectInitiationAppService contractProjectInitiationAppService;

    @Autowired
    private ContractProjectOrderAppService contractProjectOrderAppService;

    @Autowired
    private ContractProjectPlanCostConfirmAppService contractProjectPlanCostConfirmAppService;

    @Autowired
    private ContractProjectPlanMonthlyAllocationAppService contractProjectPlanMonthlyAllocationAppService;

    @Autowired
    private ContractSettlementsBillMapper contractPayBillMapper;

    @Autowired
    private ContractSettlementsBillDetailsMapper contractSettlementsBillDetailsMapper;

    @Autowired
    private ContractIncomePlanConcludeService contractIncomePlanConcludeService;

    @Autowired
    private ContractPayPlanConcludeService contractPayPlanConcludeService;
    @Autowired
    private FinanceFeignClient financeFeignClient;

    @Autowired
    private ContractIncomeConcludeExpandService contractIncomeConcludeExpandService;

    @Autowired
    private ExternalFeignClient externalFeignClient;
    @Autowired
    private ContractPayCostPlanService contractPayCostPlanService;
    @Autowired
    private ContractPayIncomePlanService contractPayIncomePlanService;
    @Autowired
    private ContractIncomeConcludeCorrectionMapper contractIncomeConcludeCorrectionMapper;

    /**
     * 合同审批完，中建流程创建回调，返回审批数据
     *
     * @param paCallBackF 合同流程创建回调数据
     * @param type        类型(1:合同订立支出 2:合同订立收入) 弃用外部入参，直接使用数据库查出来的类型
     * @return Boolean 是否调用成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean dealContractProcess(ProcessAdjustCallBackF paCallBackF, Integer type, Integer skip,Boolean isHx) {
        if(paCallBackF == null || paCallBackF.getFormData() == null){
            return Boolean.FALSE;
        }
        String processId = paCallBackF.getProcessId();
        String contractId = paCallBackF.getFormData().getFormDataId();
        Integer reviewStatus = paCallBackF.getReviewStatus();
        log.info("审批回调逻辑处理接收到的核心参数,processId:{},contractId:{},reviewStatus:{},type:{}",
                processId, contractId, reviewStatus, type);
//        String rejectReason = paCallBackF.getRejectReason();

        if (StringUtils.isBlank(processId)) {
            log.error("external流程创建回调没有没有传流程ID");
            throw new OwlBizException("external流程创建回调没有没有传流程ID");
        }

        // α.先查询未被假删的流程记录,数据在刚发起流程创建时就已入库
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, processId).eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);

        // β.相关校验
        if (Objects.isNull(recordE)) {
            log.error("external流程创建回调无法找到流程记录, processId:" + processId);
            throw new OwlBizException("external流程创建回调无法找到流程记录");
        }
        if (Objects.isNull(contractId)) {
            log.error("external流程创建回调无法找到流程id对应合同, contractId为空");
            throw new OwlBizException("external流程创建回调无法找到流程id对应合同");
        }
        //若skip为空（正常回调） 且 重复调用，则进行拦截；若skip不为空（手动回调），那么不进行校验
        if (ObjectUtils.isEmpty(skip) && Objects.equals(recordE.getReviewStatus(), reviewStatus)) {
            log.error("external流程创建回调状态重复次本次跳过");
            throw new OwlBizException("external流程创建回调状态重复次本次跳过");
        }
        type = recordE.getType();
        log.info("流程id查询得到的回调类型:{}", type);
        // γ.设置审核状态并更新到 [contract_process_record] 表
        recordE.setReviewStatus(reviewStatus);
//        if (ContractSetConst.REJECT.equals(reviewStatus)) {
//            recordE.setRejectReason(rejectReason);
//        }
        contractProcessRecordMapper.updateById(recordE);

        // δ.回调接口以闭环合同审批流程 状态均为已完成结束状态
        ContractIncomeConcludeE contractIncomeConcludeE;
        ContractPayConcludeE contractPayConcludeE;
        ContractPaySettlementConcludeE contractPaySettlementConcludeE;

        // ε.若是合同订立收入类型 【INCOME】 走不同的业务流程
        LocalDateTime now = LocalDateTime.now();
        if (ContractConcludeEnum.INCOME.equalsByCode(type)) {
            contractIncomeConcludeE = contractIncomeConcludeService.getById(contractId);
            if (Objects.isNull(contractIncomeConcludeE)) {
                throw new OwlBizException("根据合同ID检索收入合同数据失败");
            }

            // ε1.改合同审核状态 回调接口只会传[已通过][已拒绝]两种
            switch (reviewStatus) {
                case 0:
                    //fix:审批驳回时，将合同状态存储为"驳回"
                    contractIncomeConcludeE.setReviewStatus(ReviewStatusEnum.已驳回.getCode());
                    break;
                case 1:
                    contractIncomeConcludeE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
                    break;
                // 审核已通过
                case 2:
                    contractIncomeConcludeE.setReviewStatus(ReviewStatusEnum.已通过.getCode());
                    //合同审批通过，根据当前时间和合同开始时间，来更新合同状态
                    if (LocalDate.now().isBefore(contractIncomeConcludeE.getGmtExpireStart())) {
                        contractIncomeConcludeE.setStatus(ContractRevStatusEnum.尚未履行.getCode());
                    } else {
                        contractIncomeConcludeE.setStatus(ContractRevStatusEnum.正在履行.getCode());
                    }
                    /*if(contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                        contractIncomeConcludeService.handleConcludeSupple(contractIncomeConcludeE.getContractAmountOriginalRate(),contractIncomeConcludeE.getPid(),contractIncomeConcludeE.getId());
                    }*/
                    if(contractIncomeConcludeE.getSealType() != null && contractIncomeConcludeE.getSealType() == 1){
                        contractIncomeConcludeService.contractInfoToFxm(contractIncomeConcludeE.getId());
                    }
                    this.shiTuoApproveContactCallback(contractIncomeConcludeE);
                    break;
                default:
                    log.error("审核状态既不是[已通过]也不是[已拒绝], 现在中建回调慧享接口后审核状态码为: {}", reviewStatus);
                    return Boolean.FALSE;
            }
//            contractIncomeConcludeE.setStatus(ContractRevStatusEnum.已终止.getCode());

            // ε2.必要的业务流程 ➤➤➤ 校验收入合同,完成合同等
//            contractRelationBusinessService.dealActionAfterPost(contractId);
//            contractIncomeConcludeService.checkEForUpdateStatus(contractId);
//            orgRelationService.mutualForIncome(contractIncomeConcludeE);

            // ε3.改 [contract_income_conclude] 表的审核状态
            ContractIncomeConcludeE income = Builder.of(ContractIncomeConcludeE::new)
                    .with(ContractIncomeConcludeE::setId, contractId)
                    .with(ContractIncomeConcludeE::setReviewStatus, contractIncomeConcludeE.getReviewStatus())
                    .with(ContractIncomeConcludeE::setStatus,contractIncomeConcludeE.getStatus())
                    .build();
            if(ReviewStatusEnum.已通过.getCode().equals(income.getReviewStatus())){
                income.setApprovalDate(now);
            }
            contractIncomeConcludeMapper.updateById(income);
            if(ReviewStatusEnum.已通过.getCode().equals(income.getReviewStatus())) {
//                contractIncomeBusinessService.pullContract(income.getId());
            }
            return Boolean.TRUE;
        }

        // ζ.若是合同订立支出类型 【PAY】 走不同的业务流程
        if (ContractConcludeEnum.PAY.equalsByCode(type)) {
            contractPayConcludeE = contractPayConcludeService.getById(contractId);
            if (Objects.isNull(contractPayConcludeE)) {
                throw new OwlBizException("根据合同ID检索支出合同数据失败");
            }

            // ζ1.改合同审核状态
            switch (reviewStatus) {
                case 0:
                    //fix:审批驳回时，将合同状态存储为"驳回"
                    contractPayConcludeE.setReviewStatus(ReviewStatusEnum.已驳回.getCode());
                    //TODO-hhb释放成本占用
                    contractPayBusinessService.releasePayContractCost(contractPayConcludeE);
                    break;
                case 1:
                    contractPayConcludeE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
                    break;
                // 审核已通过
                case 2:
                    contractPayConcludeE.setReviewStatus(ReviewStatusEnum.已通过.getCode());
                    //合同审批通过，根据当前时间和合同开始时间，来更新合同状态
                    if (LocalDate.now().isBefore(contractPayConcludeE.getGmtExpireStart())) {
                        contractPayConcludeE.setStatus(ContractRevStatusEnum.尚未履行.getCode());
                    } else {
                        contractPayConcludeE.setStatus(ContractRevStatusEnum.正在履行.getCode());
                    }
                    if(contractPayConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                        //contractPayConcludeService.handleConcludeSupple(contractPayConcludeE);
                        contractPayConcludeE.setIsSptg(1);
                    }
                    break;
                default:
                    log.error("审核状态, 现在中建回调慧享接口后审核状态码为: {}", reviewStatus);
                    return Boolean.FALSE;
            }
//            contractPayConcludeE.setStatus(ContractRevStatusEnum.已终止.getCode());

            // ζ2.必要的业务流程 ➤➤➤ 校验支出合同,完成合同等
//            contractRelationBusinessService.dealActionAfterPost(contractId);
//            contractPayConcludeService.checkEForUpdateStatus(contractId);
//            orgRelationService.mutualForPay(contractPayConcludeE);

            // ζ3.改 [contract_pay_conclude] 表的审核状态
            ContractPayConcludeE pay = Builder.of(ContractPayConcludeE::new)
                    .with(ContractPayConcludeE::setId, contractId)
                    .with(ContractPayConcludeE::setReviewStatus, contractPayConcludeE.getReviewStatus())
                    .with(ContractPayConcludeE::setStatus,contractPayConcludeE.getStatus())
                    .with(ContractPayConcludeE::setIsSptg,contractPayConcludeE.getIsSptg())
                    .build();
            if(ReviewStatusEnum.已通过.getCode().equals(pay.getReviewStatus())){
                pay.setApprovalDate(now);
            }
            contractPayConcludeMapper.updateById(pay);
            if(ReviewStatusEnum.已通过.getCode().equals(pay.getReviewStatus())) {
//                contractPayBusinessService.pullContract(pay.getId());
            }
            return Boolean.TRUE;
        }

        if (ContractConcludeEnum.SETTLEMENT_FUND.equalsByCode(type) || ContractConcludeEnum.SETTLEMENT_NK.equalsByCode(type)) {
            contractPaySettlementConcludeE = contractPaySettlementConcludeService.getById(contractId);
            if (Objects.isNull(contractPaySettlementConcludeE)) {
                throw new OwlBizException("根据付款结算单ID检索付款结算单数据失败");
            }
            if(ReviewStatusEnum.已通过.getCode().equals(contractPaySettlementConcludeE.getReviewStatus())){
                log.info("当前结算单：{}已审批通过，默认当前流程为合同报账单流程，直接跳出",contractId);
                return Boolean.TRUE;
            }
            // ζ1.改合同审核状态
            switch (reviewStatus) {
                case 0:
                    contractPaySettlementConcludeE.setReviewStatus(ReviewStatusEnum.待提交.getCode());
                    //审核未通过，只更新"审批状态"
                    contractPaySettlementConcludeService.updateById(contractPaySettlementConcludeE);
                    break;
                case 1:
                    contractPaySettlementConcludeE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
                    contractPaySettlementConcludeService.updateById(contractPaySettlementConcludeE);
                    break;
                // 审核已通过【结算单与合同报账单共用审批，99判断为结算单审批通过，2判断为合同报账单审批通过】
                case 99:
                case 2:
                    contractPaySettlementConcludeE.setReviewStatus(ReviewStatusEnum.已通过.getCode());
                    contractPaySettlementConcludeE.setPlannedCollectionTime(LocalDate.now());
                    //审核通过再执行原来的结算金额处理逻辑
                    ContractPaySettlementConcludeE pay = Builder.of(ContractPaySettlementConcludeE::new)
                            .with(ContractPaySettlementConcludeE::setId, contractId)
                            .with(ContractPaySettlementConcludeE::setReviewStatus, contractPaySettlementConcludeE.getReviewStatus())
                            .with(ContractPaySettlementConcludeE::setPlannedCollectionTime, LocalDate.now())
                            .with(ContractPaySettlementConcludeE::setSettleStatus, 2)
                            .build();
                    //设置审批完成时间
                    pay.setApproveCompletedTime(now);
                    contractPaySettlementConcludeService.updateById(pay);
                    handlePayContractStatus(contractPaySettlementConcludeE);
                    List<PayCostPlanE> payCostList = contractPayCostPlanService.list(Wrappers.<PayCostPlanE>lambdaQuery()
                            .in(PayCostPlanE::getContractId, contractPaySettlementConcludeE.getContractId())
                            .eq(PayCostPlanE::getDeleted, 0));
                    if(isHx){
                        //contractPaySettlementConcludeService.handlePayPlanState(contractId);
                        contractPaySettlementConcludeService.writeOffPayPlanAndCost(contractId);
                    }
                    ContractPayConcludeE payNK = contractPayConcludeMapper.queryNKContractById(contractPaySettlementConcludeE.getContractId());
                    //TODO-hhb释放成本占用
                    contractPayBusinessService.releasePayContractSettlementCost(contractPaySettlementConcludeE);
                    if(Objects.nonNull(payNK) && contractPaySettlementConcludeE.getContractId().equals(payNK.getId())){
                        log.info("该合同为NK合同，不生成合同报账单及临时账单等数据");
                        return Boolean.TRUE;
                    }
                    List<UpdateTemporaryChargeBillF> temporaryChargeBillFList = contractPayPlanConcludeService.iriUpdate(contractPaySettlementConcludeE.getId(),contractPaySettlementConcludeE.getContractId());
                    if (CollectionUtils.isEmpty(temporaryChargeBillFList)) {
                        //throw new OwlBizException("创建临时账单失败");
                        log.error("创建临时账单失败");
                        return Boolean.TRUE;
                    }
                    //创建报账单
                    VoucherBillGenerateOnContractSettlementF contractSettlementF = new VoucherBillGenerateOnContractSettlementF();
                    contractSettlementF.setCommunityId(temporaryChargeBillFList.get(0).getSupCpUnitId());
                    contractSettlementF.setBillIdList(temporaryChargeBillFList.stream().map(UpdateTemporaryChargeBillF::getId).collect(Collectors.toList()));
                    contractSettlementF.setEventType(4);
                    contractSettlementF.setSettlementId(temporaryChargeBillFList.get(0).getExtField7());
                    if(reviewStatus == 99){
                        contractSettlementF.setProcessId(processId);
                    }
                    financeFeignClient.createCheckSheet(contractSettlementF);

                    //TODO-hhb调用成本占用
                    break;
                default:
                    log.error("审核状态, 现在中建回调慧享接口后审核状态码为: {}", reviewStatus);
                    return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }

        if (ContractConcludeEnum.INCOME_SETTLEMENT.equalsByCode(type)) {
            dealContractIncomeConcludeSettlement(reviewStatus, contractId, now, isHx, processId);
            return Boolean.TRUE;
        }

        if (ContractConcludeEnum.PROJECT_INITIATION_COST_CONFIRM.equalsByCode(type)) {
            return this.dealContractProjectInitiation(reviewStatus, contractId);
        }

        log.error("中建返回的数据中，合同类型数据既不是[支出合同]也不是[收入合同], 合同类型为: {}", type);
        return Boolean.FALSE;
    }
    /**
     * 市拓合同状态回调
     * @param contractIncomeConcludeE
     */
    private void shiTuoApproveContactCallback(ContractIncomeConcludeE contractIncomeConcludeE) {
        try {
            // 判断是否为市拓系统创建合同
            ContractIncomeConcludeExpandF concludeExpandF = new ContractIncomeConcludeExpandF();
            concludeExpandF.setContractId(contractIncomeConcludeE.getId());
            Optional<ContractIncomeConcludeExpandV> expandV = contractIncomeConcludeExpandService.get(concludeExpandF);
            if(expandV.isPresent() && Objects.nonNull(expandV.get().getCttmsj())) {
                List<ContractCttmsjF> cttmsj = JSONObject.parseArray(expandV.get().getCttmsj(), ContractCttmsjF.class);
                if (CollUtil.isNotEmpty(cttmsj)) {
                    ParamCallBackInfoF paramCallBackInfoF = new ParamCallBackInfoF().setId(cttmsj.get(0).getId())
                            .setContractId(contractIncomeConcludeE.getId())
                            .setReviewStatus(ReviewStatusEnum.已通过.getCode())
                            .setActualTotalContractAmount(contractIncomeConcludeE.getContractAmountOriginalRate());
                    log.info("approveContactCallback() called with parameters => 【paramCallBackInfoF = {}】", JSON.toJSONString(paramCallBackInfoF));
                    boolean b = externalFeignClient.approveContactCallback(paramCallBackInfoF);
                    log.info("approveContactCallback() resp:{}", b);

                }
            }
        } catch (Exception e) {
            log.error("ContractProcessRecordService.shiTuoApproveContactCallback() called with exception => 【contractIncomeConcludeE = {}】", JSON.toJSONString(contractIncomeConcludeE), e);
        }
    }

    /**
     * 批量处理结算单下成本计划的核销
     *
     * @param settBatchIdF
     * @return
     */
    public Boolean batchHandlePayCostPlan(SettBatchIdF settBatchIdF){
        contractPaySettlementConcludeService.batchHandlePayCostPlan(settBatchIdF.getSettIds());
        return Boolean.TRUE;
    }

    /**
     * 批量处理收款单下收入计划的核销
     *
     * @param settBatchIdF
     * @return
     */
    public Boolean batchHandlePayIncomePlan(SettBatchIdF settBatchIdF) {
        contractIncomeSettlementConcludeService.batchHandlePayIncomePlan(settBatchIdF.getSettIds());
        return Boolean.TRUE;
    }

    /**
     * 结算单是"最终结算"，将合同状态修改为合同终止
     *
     * @param settlement
     */
    private void handlePayContractStatus(ContractPaySettlementConcludeE settlement) {
        if (!SettlementTypeEnum.FINAL.getCode().equals(settlement.getSettlementType())) {
            return;
        }
        List<ContractPayConcludeE> targetConcludes = new ArrayList<>();
        ContractPayConcludeE pConclude = contractPayConcludeMapper.queryByContractId(settlement.getContractId());
        targetConcludes.add(pConclude);
        //查询子合同
        List<ContractPayConcludeE> subConcludes = contractPayConcludeService.list(Wrappers.<ContractPayConcludeE>lambdaQuery()
                .eq(ContractPayConcludeE::getPid, settlement.getContractId())
                .in(ContractPayConcludeE::getDeleted, 0,1));
        if (!CollectionUtils.isEmpty(subConcludes)) {
            targetConcludes.addAll(subConcludes);
        }
        targetConcludes.forEach(conclude -> conclude.setStatus(ContractRevStatusEnum.合同终止.getCode()));
        contractPayConcludeService.updateBatchById(targetConcludes);
    }

    /**
     * 确收单审批回调逻辑处理
     *
     * @param reviewStatus
     * @param id
     */
    private void dealContractIncomeConcludeSettlement(Integer reviewStatus, String id, LocalDateTime now, Boolean isHx, String processId) {
        ContractIncomeSettlementConcludeE settlement = contractIncomeSettlementConcludeService.getById(id);
        if (ObjectUtils.isEmpty(settlement)) {
            throw new OwlBizException("确收单数据不存在");
        }
        if(ReviewStatusEnum.已通过.getCode().equals(settlement.getReviewStatus())){
            log.info("当前确收单：{}已审批通过，默认当前流程为合同报账单流程，直接跳出",id);
            return ;
        }
        if (ReviewStatusEnum.待提交.getCode().equals(reviewStatus)) {
            contractIncomeSettlementConcludeService.handleToReviewStatus(id, ReviewStatusEnum.待提交.getCode());
        } else if (ReviewStatusEnum.审批中.getCode().equals(reviewStatus)) {
            contractIncomeSettlementConcludeService.handleToReviewStatus(id, ReviewStatusEnum.审批中.getCode());
        } else if (ReviewStatusEnum.已通过.getCode().equals(reviewStatus) || ReviewStatusEnum.审批通过.getCode().equals(reviewStatus)) {
            contractIncomeSettlementConcludeService.handleApproveCompletedDataChange(id);
            handleIncomeContractStatus(settlement);
            List<PayIncomePlanE> payIncomePlanList = contractPayIncomePlanService.list(Wrappers.<PayIncomePlanE>lambdaQuery()
                    .eq(PayIncomePlanE::getContractId, settlement.getContractId())
                    .eq(PayIncomePlanE::getDeleted, 0));
            if(isHx){
                //contractIncomeSettlementConcludeService.handleIncomePlanState(id);
                contractIncomeSettlementConcludeService.writeOffIncomePlanAndCost(id);
            }
            //更新应收金额
            List<UpdateTemporaryChargeBillF> temporaryChargeBillFList = contractIncomePlanConcludeService.iriUpdate(settlement.getId(),settlement.getContractId(), now,payIncomePlanList);
            if (CollectionUtils.isEmpty(temporaryChargeBillFList)) {
                //throw new OwlBizException("创建临时账单失败");
                log.error("确收审批，创建临时账单失败");
                return;
            }
            //创建报账单
            VoucherBillGenerateOnContractSettlementF contractSettlementF = new VoucherBillGenerateOnContractSettlementF();
            contractSettlementF.setCommunityId(temporaryChargeBillFList.get(0).getSupCpUnitId());
            contractSettlementF.setBillIdList(temporaryChargeBillFList.stream().map(UpdateTemporaryChargeBillF::getId).collect(Collectors.toList()));
            contractSettlementF.setEventType(6);
            contractSettlementF.setSettlementId(temporaryChargeBillFList.get(0).getExtField7());
            if(ReviewStatusEnum.审批通过.getCode().equals(reviewStatus)){
                contractSettlementF.setProcessId(processId);
            }
            financeFeignClient.createCheckSheet(contractSettlementF);
        }
    }

    /**
     * 立项管理审批回调逻辑处理
     *
     * @param reviewStatus
     * @param id
     */
    private Boolean dealContractProjectInitiation(Integer reviewStatus, String id) {
        ContractProjectInitiationV contractProjectInitiation = contractProjectInitiationAppService.getDetail(id);
        if (ObjectUtils.isEmpty(contractProjectInitiation)) {
            throw new OwlBizException("立项审批数据不存在");
        }
        ContractProjectInitiationE contractProjectInitiationE = new ContractProjectInitiationE();
        contractProjectInitiationE.setId(contractProjectInitiation.getId());

        switch (reviewStatus) {
            case 0:
                contractProjectInitiationE.setReviewStatus(ReviewStatusEnum.已驳回.getCode());
                // 释放成本占用
                contractProjectInitiationAppService.syncDynamicCostIncurred(contractProjectInitiation, BillTypeEnum.PROJECT_REJECT, OperationTypeEnum.RELEASE);
                break;
            case 1:
                contractProjectInitiationE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
                break;
            case 2:
                contractProjectInitiationE.setReviewStatus(ReviewStatusEnum.已通过.getCode())
                        .setApproveCompletedTime(LocalDateTime.now());
                // 签订合同选择是 则释放立项金额
                if (Objects.equals(contractProjectInitiation.getIsContractSigned(), 1)) {
                    contractProjectInitiationAppService.syncDynamicCostIncurred(contractProjectInitiation, BillTypeEnum.PROJECT_REJECT, OperationTypeEnum.RELEASE);
                    // 修改成本确认状态和金额
                    contractProjectInitiationE.setCostConfirmationAmount(contractProjectInitiation.getAmountWithoutTax().negate());
                }
                break;
            default:
                log.error("审核状态既不是[已通过]也不是[已拒绝], 现在中建回调慧享接口后审核状态码为 dealContractProjectInitiation reviewStatus: {}", reviewStatus);
                return Boolean.FALSE;
        }
        contractProjectInitiationAppService.updateById(contractProjectInitiationE);
        return Boolean.TRUE;
    }

    /**
     * 结算单是"最终结算"，将合同状态修改为合同终止
     *
     * @param settlement
     */
    private void handleIncomeContractStatus(ContractIncomeSettlementConcludeE settlement){
        if (!SettlementTypeEnum.FINAL.getCode().equals(settlement.getSettlementType())) {
            return;
        }
        List<ContractIncomeConcludeE> targetConcludes = new ArrayList<>();
        ContractIncomeConcludeE pConclude = contractIncomeConcludeService.getById(settlement.getContractId());
        targetConcludes.add(pConclude);
        //查询子合同
        List<ContractIncomeConcludeE> subConcludes = contractIncomeConcludeService.list(Wrappers.<ContractIncomeConcludeE>lambdaQuery()
                .eq(ContractIncomeConcludeE::getPid, settlement.getContractId())
                .eq(ContractIncomeConcludeE::getDeleted, 0));
        if (!CollectionUtils.isEmpty(subConcludes)) {
            targetConcludes.addAll(subConcludes);
        }
        targetConcludes.forEach(conclude -> conclude.setStatus(ContractRevStatusEnum.合同终止.getCode()));
        contractIncomeConcludeService.updateBatchById(targetConcludes);
    }

    /**
     * 报账单审批完，创建回调
     * @return Boolean 是否调用成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean dealContractInvoice(Long id,Integer state) {
        LambdaQueryWrapperX<ContractSettlementsBillE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractSettlementsBillE::getPaymentid, id).eq(ContractSettlementsBillE::getDeleted, 0);
        ContractSettlementsBillE recordE = contractPayBillMapper.selectOne(queryWrapper);
        if(!ObjectUtils.isNotEmpty(recordE)){
            throw new OwlBizException("根据报账单id找不到对应票据！");
        }
        LambdaQueryWrapperX<ContractSettlementsBillDetailsE> queryWrapper1 = WrapperX.lambdaQueryX();
        queryWrapper1.eqIfPresent(ContractSettlementsBillDetailsE::getBillId, recordE.getId()).eq(ContractSettlementsBillDetailsE::getDeleted, 0);
        List<ContractSettlementsBillDetailsE> contractSettlementsBillDetailsEList = contractSettlementsBillDetailsMapper.selectList(queryWrapper1);
        switch (state) {
            // 审核已通过
            case 2:
                for(ContractSettlementsBillDetailsE s : contractSettlementsBillDetailsEList){
                    s.setReviewStatus(2);
                    s.setGmtModify(LocalDateTime.now());
                    contractSettlementsBillDetailsMapper.updateById(s);
                }
                break;
            case 3:
                for(ContractSettlementsBillDetailsE s : contractSettlementsBillDetailsEList){
                    contractSettlementsBillDetailsMapper.deleteById(s);
                }
                contractPaySettlementConcludeService.handleCallBack(recordE.getSettlementId(), recordE.getAmount());
                break;
            case 4:
                for(ContractSettlementsBillDetailsE s : contractSettlementsBillDetailsEList){
                    contractSettlementsBillDetailsMapper.deleteById(s);
                }
                contractPaySettlementConcludeService.handleCallBack(recordE.getSettlementId(), recordE.getAmount());
                break;
            default:
                log.error("审核状态, 现在票据回调审核状态码为: {}", state);
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 支出合同NK结束BPM流程回调
     * @param id
     * @param state
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean payNkBpmProcess(String id,Integer state) {
        ContractPayConcludeE mainContract = contractPayConcludeMapper.selectById(id);
        if(Objects.isNull(mainContract)){
            throw new OwlBizException("合同不存在，请输入正确合同ID");
        }
        //审批通过
        if(state.equals(BPMStatusEnum.已通过.getCode())){
            mainContract.setBpmStatus(BPMStatusEnum.已通过.getCode());
            mainContract.setNkStatus(NkStatusEnum.已关闭.getCode());
            mainContract.setBpmApprovalDate(LocalDateTime.now());
        }else{
            mainContract.setBpmStatus(BPMStatusEnum.已驳回.getCode());
            mainContract.setNkStatus(NkStatusEnum.已开启.getCode());
        }
        contractPayConcludeMapper.updateById(mainContract);
        ContractPayConcludeE nkContract = contractPayConcludeMapper.queryNKContractById(id);
        if(ObjectUtil.isNotNull(nkContract)){
            //审批通过
            if(state.equals(BPMStatusEnum.已通过.getCode())){
                contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.已关闭.getCode(),BPMStatusEnum.已通过.getCode(),null,LocalDateTime.now());
            }else{
                contractPayConcludeMapper.updateNKContractById(nkContract.getId(),NkStatusEnum.已开启.getCode(),BPMStatusEnum.已驳回.getCode(),null,null);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 立项管理成本确认BPM流程回调
     * @param id
     * @param state
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean projectInitiationBpmProcess(String id, Integer state) {
        ContractProjectInitiationV contractProjectInitiation = contractProjectInitiationAppService.getDetail(id);
        if (Objects.isNull(contractProjectInitiation)) {
            throw new OwlBizException("根据立项ID检索数据失败");
        }

        ContractProjectInitiationE contractProjectInitiationE = new ContractProjectInitiationE();
        contractProjectInitiationE.setId(contractProjectInitiation.getId());

        //审批通过
        if(state.equals(BPMStatusEnum.已通过.getCode())) {
            contractProjectInitiationE.setBpmReviewStatus(BPMStatusEnum.已通过.getCode());
            contractProjectInitiationE.setBpmApprovalDate(LocalDateTime.now());
            contractProjectInitiationE.setCostConfirmationStatus(1);
            if (CollUtil.isNotEmpty(contractProjectInitiation.getContractList())) {
                // 关联合同则确认金额为立项金额取反
                contractProjectInitiationE.setCostConfirmationAmount(
                        contractProjectInitiation.getAmountWithoutTax().negate()
                );
            } else {
                contractProjectInitiationE.setCostConfirmationAmount(
                        contractProjectInitiation.getContractPlanList().stream()
                                .map(ContractProjectPlanV::getConfirmAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
            }
            // 释放和占用成本系统金额
            contractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred(contractProjectInitiation, BillTypeEnum.PROJECT_SETTLE);
        } else {
            contractProjectInitiationE.setBpmReviewStatus(BPMStatusEnum.已驳回.getCode());
            contractProjectPlanMonthlyAllocationAppService.recovery(contractProjectInitiation.getId());
        }
        // 更新成本确认审批状态
        contractProjectPlanCostConfirmAppService.update(id, state);

        // 更新立项bpm审批状态
        contractProjectInitiationAppService.updateById(contractProjectInitiationE);

        return true;
    }

    /**
     * 立项管理慧采下单BPM流程回调
     * @param id
     * @param state
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean projectInitiationOrderForJDBpmProcess(String id, Integer state) {
        ContractProjectOrderE contractProjectOrderE = contractProjectOrderAppService.getById(id);
        if (Objects.isNull(contractProjectOrderE)) {
            throw new OwlBizException("根据订单ID检索数据失败");
        }
        Boolean result = false;
        StartOrderForJDReqF startOrderForJDReqF = new StartOrderForJDReqF()
                .setJdOrderId(contractProjectOrderE.getOrderNumber())
                .setUserName(contractProjectOrderE.getJdHuiCaiUserName())
                .setPwdMd5(contractProjectOrderE.getJdHuiCaiPwdMd5());
        log.info("ContractProcessRecordService.projectInitiationOrderForJDBpmProcess startOrderForJDReqF:{}", JSON.toJSONString(startOrderForJDReqF));
        //审批通过
        if(state.equals(BPMStatusEnum.已通过.getCode())) {
            // 确认订单
            result = externalFeignClient.confirmOrder(startOrderForJDReqF);
        } else if(state.equals(BPMStatusEnum.已驳回.getCode())) {
            // 取消订单
            result = externalFeignClient.cancelOrder(startOrderForJDReqF);
            // 释放立项订单可用金额
            ContractProjectInitiationE projectInitiationE = contractProjectInitiationAppService.getById(contractProjectOrderE.getProjectInitiationId());
            BigDecimal newOrderTotalAmount = NumberUtil.sub(projectInitiationE.getOrderTotalAmount(), contractProjectOrderE.getOrderAmountWithoutTax());
            projectInitiationE.setOrderTotalAmount(newOrderTotalAmount)
                    .setRemainingAmountWithoutTax(NumberUtil.sub(projectInitiationE.getAmountWithoutTax(), newOrderTotalAmount));
            contractProjectInitiationAppService.updateById(projectInitiationE);

            // 修改订单状态
            List<ContractProjectOrderInfoV> contractProjectOrderInfoVS = JSONObject.parseArray(contractProjectOrderE.getGoodsInfo(), ContractProjectOrderInfoV.class);
            contractProjectOrderInfoVS.stream().forEach(e -> e.setOrderStatus(2));
            contractProjectOrderE.setGoodsInfo(JSON.toJSONString(contractProjectOrderInfoVS));
            contractProjectOrderE.setOrderStatus(2);
        }

        contractProjectOrderE.setBpmReviewStatus(state);
        contractProjectOrderAppService.updateById(contractProjectOrderE);

        log.info("ContractProcessRecordService.projectInitiationOrderForJDBpmProcess returned:{}", result);
        return result;
    }

    /**
     * 收入合同修正BPM流程回调
     * @param id
     * @param state
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean incomeCorrectionBpmProcess(String id,Integer state) {
        ContractIncomeConcludeCorrectionE contractIncomeConclude = contractIncomeConcludeCorrectionMapper.selectById(Long.parseLong(id));
        if(Objects.isNull(contractIncomeConclude) ){
            throw new OwlBizException("该记录不存在，请输入正确修正记录ID");
        }
        //审批通过
        if(state.equals(BPMStatusEnum.已通过.getCode())){
            contractIncomeConcludeMapper.updateIsCorrectionAndPlan(contractIncomeConclude.getContractId(),CorrectionStatusEnum.已通过.getCode());
            contractIncomeConclude.setCorrectionStatus(CorrectionStatusEnum.已通过.getCode());
            contractIncomeConclude.setBpmApprovalDate(LocalDateTime.now());
            contractIncomeBusinessService.correctionIncomeFun(contractIncomeConclude.getContractId(), id);
        }else{
            contractIncomeConclude.setCorrectionStatus(CorrectionStatusEnum.已驳回.getCode());
            contractIncomeConcludeMapper.updateIsCorrectionAndPlan(contractIncomeConclude.getContractId(),CorrectionStatusEnum.已驳回.getCode());
        }
        contractIncomeConcludeCorrectionMapper.updateById(contractIncomeConclude);
        return Boolean.TRUE;
    }

}
