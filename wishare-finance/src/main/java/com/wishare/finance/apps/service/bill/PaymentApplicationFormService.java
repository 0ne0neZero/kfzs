package com.wishare.finance.apps.service.bill;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.service.PaymentAppProcessService;
import com.wishare.finance.apps.pushbill.fo.*;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.apps.service.pushbill.mdm63.Mdm63Service;
import com.wishare.finance.apps.service.remind.RemindMessageConfigService;
import com.wishare.finance.apps.service.remind.RemindMessageConfigServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.ApplyStatusEnum;
import com.wishare.finance.domains.bill.consts.enums.ApproveStatusEnums;
import com.wishare.finance.domains.mdm.entity.Mdm63LockE;
import com.wishare.finance.domains.refund.RefundManageDTO;
import com.wishare.finance.domains.refund.RefundManageDetailDTO;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormKxmx;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormPayMx;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PayStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.Mdm63LockMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationKXMXMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationPayMxMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.vo.bpm.SendMessageReqVO;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.finance.infrastructure.remote.vo.charge.ApproveFilter;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractSettlementF;
import com.wishare.finance.infrastructure.remote.vo.contract.FirstExamineMessageF;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentApplicationFormService  {

    private final PaymentApplicationFormRepository paymentApplicationFormRepository;
    private final PaymentApplicationFormKXMXRepository paymentApplicationFormKXMXRepository;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final ContractClient contractClient;
    private final VoucherBillDetailDxZJMapper voucherBillDetailDxZJMapper;
    private final PaymentApplicationKXMXMapper paymentApplicationKXMXMapper;
    private final PaymentApplicationPayMxMapper paymentApplicationPayMxMapper;
    private final PaymentApplicationPayMxRepository paymentApplicationPayMxRepository;
    private final BillRuleDomainService billRuleDomainService;
    private final ConfigClient configClient;
    private final ExternalClient externalClient;
    private final RemindMessageConfigService remindMessageConfigService;
    private final FinanceCloudService financeCloudService;
    private final Mdm63Service mdm63Service;
    private final PaymentAppProcessService paymentAppProcessService;
    private final BpmClient bpmClient;
    private final RemindMessageConfigServiceImpl remindMessageConfigServiceImpl;
    private final Mdm63LockMapper mdm63LockMapper;
    @Autowired
    private SpaceClient spaceClient;
    @Autowired
    private UserClient userClient;
    @Value("${inspection.associated.personnel.roleId}")
    private String personnelRoleId;
    @Autowired
    private ChargeClient chargeClient;
    private final static String PAY_APPLY_CODE_KEY = "PAY:APPLY:CODE";


    @Transactional(rollbackFor = Exception.class)
    public PaymentApplicationFormZJ createPaymentApplicationForm(PaymentApplicationAddFormF applicationAddFormF) {
        String gatherContractId = "9999999999";
        if (!gatherContractId.equals(applicationAddFormF.getContractId()) && checkSettlementInfo(applicationAddFormF)) {
            throw new OwlBizException("结算id已经发起过支付单申请，请检查");
        }
        PaymentApplicationFormZJ paymentApplicationFormZJ = new PaymentApplicationFormZJ();
        if (null != applicationAddFormF.getId()) {
            paymentApplicationFormZJ= paymentApplicationFormRepository.queryById(applicationAddFormF.getId());
            if (null != paymentApplicationFormZJ.getApprovalStatus() && paymentApplicationFormZJ.getApprovalStatus() != ApproveStatusEnums.草稿.getCode()) {
                throw new OwlBizException("非草稿状态不允许修改");
            }
        }
        if (null == applicationAddFormF.getSubmitType() || (1 != applicationAddFormF.getSubmitType()  && 2 != applicationAddFormF.getSubmitType())) {
            throw new OwlBizException("提交类型非法");

        }
        if (1 == applicationAddFormF.getSubmitType()) {
            applicationAddFormF.setApprovalStatus(ApproveStatusEnums.草稿.getCode());
        }
        if (2 == applicationAddFormF.getSubmitType()) {
            applicationAddFormF.setApprovalStatus(ApproveStatusEnums.待审批.getCode());
        }
        List<ContractPayPlanInnerInfoV> contractIdOnIncomes = new ArrayList<>();
        if(gatherContractId.equals(applicationAddFormF.getContractId())){
            ContractPayPlanInnerInfoV contractInfo = new ContractPayPlanInnerInfoV();
            contractInfo.setCommunityId(applicationAddFormF.getCommunityId());
            contractInfo.setCommunityName(applicationAddFormF.getCommunityName());
            contractInfo.setRegion(applicationAddFormF.getRegion());
            contractIdOnIncomes.add(contractInfo);
        }else{
            contractIdOnIncomes = contractClient.getInnerInfoByContractIdOnPayApp(Lists.newArrayList(applicationAddFormF.getContractId()));
        }
        if (CollectionUtils.isEmpty(contractIdOnIncomes)) {
            throw new OwlBizException("非法合同id");
        }
        if (null == applicationAddFormF.getId()) {
            PaymentApplicationFormZJ result = save(applicationAddFormF,contractIdOnIncomes.get(0));
            if(gatherContractId.equals(applicationAddFormF.getContractId())){
                //锁定款项明细关联核销数据
                List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS = applicationAddFormF.getPaymentApplicationKXDetailVS();
                for(PaymentApplicationKXDetailV detailV : paymentApplicationKXDetailVS){
                    Mdm63LockE mdm63LockE = new Mdm63LockE();
                    mdm63LockE.setId(IdentifierFactory.getInstance().generateLongIdentifier("mdm63_lock"));
                    mdm63LockE.setFtId(detailV.getFtId());
                    mdm63LockE.setVoucherBillId(result.getId());
                    mdm63LockE.setVoucherBillNo(result.getPayApplyCode());
                    mdm63LockMapper.insert(mdm63LockE);
                }
            }
            return result;
        }else{
            fillBankIdAccount(applicationAddFormF.getAccountCode(),null);
            PaymentApplicationFormZJ update = update(applicationAddFormF, contractIdOnIncomes.get(0));
            /* 同步退款管理状态 */
            if (1 == applicationAddFormF.getSubmitType()){
                RefundManageDTO refundManageDTO = new RefundManageDTO(
                        paymentApplicationFormZJ.getCommunityId(),
                        paymentApplicationFormZJ.getPayApplyCode(),
                        paymentApplicationFormZJ.getId(),
                        ApproveStatusEnum.草稿.getCode(),
                        ApproveStatusEnum.草稿.getValue());
                log.info("同步退款管理状态-完成初审,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
                chargeClient.syncRefundManageStatus(refundManageDTO);
                /* 同步新的申请单id和code */
                RefundManageDetailDTO detailDTO = new RefundManageDetailDTO()
                        .setPayApplyCode(update.getPayApplyCode())
                        .setCommunityId(update.getCommunityId())
                        .setPayApplyId(String.valueOf(update.getId()))
                        .setPayApplyIdOld(String.valueOf(paymentApplicationFormZJ.getId()))
                        .setPayApplyCodeOld(paymentApplicationFormZJ.getPayApplyCode());
                log.info("编辑业务支付申请单同步-入参:{}", JSONObject.toJSONString(detailDTO));
                chargeClient.syncPaymentApplicationForm(detailDTO);
            }else if (2 == applicationAddFormF.getSubmitType()){
                RefundManageDTO refundManageDTO = new RefundManageDTO(
                        paymentApplicationFormZJ.getCommunityId(),
                        paymentApplicationFormZJ.getPayApplyCode(),
                        paymentApplicationFormZJ.getId(),
                        ApproveStatusEnum.待审批.getCode(),
                        ApproveStatusEnum.待审批.getValue());
                log.info("同步退款管理状态-完成初审,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
                chargeClient.syncRefundManageStatus(refundManageDTO);
                /* 同步新的申请单id和code */
                RefundManageDetailDTO detailDTO = new RefundManageDetailDTO()
                        .setPayApplyCode(update.getPayApplyCode())
                        .setCommunityId(update.getCommunityId())
                        .setPayApplyId(String.valueOf(update.getId()))
                        .setPayApplyIdOld(String.valueOf(paymentApplicationFormZJ.getId()))
                        .setPayApplyCodeOld(paymentApplicationFormZJ.getPayApplyCode());
                log.info("编辑业务支付申请单同步-入参:{}", JSONObject.toJSONString(detailDTO));
                chargeClient.syncPaymentApplicationForm(detailDTO);
            }
            return update;
        }


    }

    private boolean checkSettlementInfo(PaymentApplicationAddFormF applicationAddFormF) {
        LambdaQueryWrapper<PaymentApplicationFormZJ> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentApplicationFormZJ::getDeleted, 0);

        if (!CollectionUtils.isEmpty(applicationAddFormF.getSettlementList())) {
            List<String> settlementIds = applicationAddFormF.getSettlementList().stream()
                    .map(SettlementF::getSettlementId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (!settlementIds.isEmpty()) {
                String orCondition = settlementIds.stream()
                        .map(id -> "JSON_SEARCH(settlement_info, 'one', '" + id + "') IS NOT NULL")
                        .collect(Collectors.joining(" OR "));

                orCondition = "(" + orCondition + ")";
                wrapper.apply(orCondition);
            }
        }
        List<PaymentApplicationFormZJ> resultList = paymentApplicationFormRepository.list(wrapper);
        return (null == applicationAddFormF.getId() && CollectionUtils.isNotEmpty(resultList)) || (null != applicationAddFormF.getId() && CollectionUtils.isNotEmpty(resultList) && resultList.size() >1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void approve(ApproveOrRejectF approveOrRejectF) {
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.queryById(approveOrRejectF.getPayApplicationFormId());
        if (null == paymentApplicationFormZJ) {
            throw new OwlBizException("支付申请单不存在");
        }
        if (ApproveStatusEnum.待审批.getCode() != paymentApplicationFormZJ.getApprovalStatus()) {
            throw new OwlBizException("草稿状态不允许发起审核");
        }
        paymentApplicationFormZJ.setGmtModify(LocalDateTime.now());

        FirstExamineMessageF firstExamineMessageF = FirstExamineMessageF.builder().communityId(paymentApplicationFormZJ.getCommunityId()).reason(approveOrRejectF.getRemark()).build();
        if (2 == approveOrRejectF.getType()) {
            paymentApplicationFormZJ.setApprovalStatus(ApproveStatusEnum.审批驳回.getCode());
            this.paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
            remindMessageConfigService.send(firstExamineMessageF, false, paymentApplicationFormZJ);
            RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                    paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                    ApproveStatusEnum.审批驳回.getCode(), ApproveStatusEnum.审批驳回.getValue());
            log.info("初审审批驳回,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
            chargeClient.syncRefundManageStatus(refundManageDTO);
            //chargeClient.refundManageCallBack(refundManageDTO);
        }else{
            paymentApplicationFormZJ.setApprovalStatus(ApproveStatusEnum.完成初审.getCode());
            this.paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
            List<SettlementF> settlementFList = JSON.parseArray(paymentApplicationFormZJ.getSettlementInfo(),SettlementF.class);
            updatePayStatusForSettle(settlementFList.stream().map(SettlementF::getSettlementId).collect(Collectors.toList()));
            remindMessageConfigService.send(firstExamineMessageF, true, paymentApplicationFormZJ);
            RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                    paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                    ApproveStatusEnum.完成初审.getCode(), ApproveStatusEnum.完成初审.getValue());
            log.info("完成初审,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
            chargeClient.syncRefundManageStatus(refundManageDTO);
        }

    }

    private void updatePayStatusForSettle(List<String> settlementIds) {
        contractClient.updateStatus(ContractSettlementF.builder().applyStatus(ApplyStatusEnum.支付中.getCode()).settlementIdList(settlementIds).build());
    }


    @Transactional(rollbackFor = Exception.class)
    public Long financialPreliminaryReview(FinancialPreliminaryReviewF req) {
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.queryById(req.getPayApplicationFormId());
        if (null == paymentApplicationFormZJ) {
            throw new OwlBizException("支付申请单不存在");
        }
        if (ApproveStatusEnum.草稿.getCode() == paymentApplicationFormZJ.getApprovalStatus()) {
            throw new OwlBizException("草稿状态不允许发起财务初审批");
        }
        if (ApproveStatusEnum.审批通过.getCode() == paymentApplicationFormZJ.getApprovalStatus()) {
            throw new OwlBizException("已经审批完成,不允许重复审批");
        }

        paymentApplicationFormZJ.setApprovalDate(LocalDateTime.now());
        paymentApplicationFormZJ.setGmtModify(LocalDateTime.now());
        paymentApplicationFormZJ.setCashFlow(req.getCashFlow());
        paymentApplicationFormZJ.setCashFlowName(req.getCashFlowName());
        paymentApplicationFormZJ.setPaymentMethod(req.getPaymentMethod());
        paymentApplicationFormZJ.setPaymentMethodName(req.getPaymentMethodName());
        paymentApplicationFormZJ.setPayAccountId(req.getPayAccountId());
        paymentApplicationFormZJ.setNameOfPayAccount(req.getNameOfPayAccount());
        paymentApplicationFormZJ.setPayBankAccountNumber(req.getPayBankAccountNumber());
        paymentApplicationFormZJ.setPayOpeningBank(req.getPayOpeningBank());
        paymentApplicationFormZJ.setPaymentMethodForBills(req.getPaymentMethodForBills());
        paymentApplicationFormZJ.setBillsNumbers(StringUtils.isEmpty(req.getBillsNumbers()) ? null : req.getBillsNumbers());
        paymentApplicationFormZJ.setPayDesc(req.getPayDesc());
        paymentApplicationFormZJ.setTransferRemarks(req.getTransferRemarks());
        this.paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);

        //财务初审结束，给发起人发送信息
        //********发送信息至工作台********
        SendMessageReqVO sendMessage = new SendMessageReqVO();
        sendMessage.setAppName("APPROVE");
        sendMessage.setAppDesc("审批流程");
        sendMessage.setType("YWZFSQ");
        sendMessage.setTypeDesc("业务申请");
        sendMessage.setTitle("支付申请已完成初审，请提交支付申请。");
        sendMessage.setCommunityId(paymentApplicationFormZJ.getCommunityId());
        sendMessage.setTenantId(paymentApplicationFormZJ.getTenantId());
        sendMessage.setStatusName(ApproveStatusEnums.完成审批.getValue());
        sendMessage.setStatusValue(String.valueOf(ApproveStatusEnums.完成审批.getCode()));
        sendMessage.setHandleUserIdArr(new String[]{paymentApplicationFormZJ.getCreator()});
        sendMessage.setUserId(paymentApplicationFormZJ.getOperator());
        sendMessage.setUserName(paymentApplicationFormZJ.getOperatorName());
        String s = bpmClient.sendMessage(sendMessage);
        log.info("发送信息至工作台：获取审批流程入参:{},结果:{}", JSON.toJSONString(sendMessage),s);
        paymentApplicationFormZJ.setBpmProcessIdFq(s);
        paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);

        RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                ApproveStatusEnum.待审批.getCode(), ApproveStatusEnum.待审批.getValue());
        log.info("审核-完成初审,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
        chargeClient.syncRefundManageStatus(refundManageDTO);
        return paymentApplicationFormZJ.getId();
    }

    public String initiateTheProcess(String payApplicationFormId) {
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.queryById(payApplicationFormId);
        if (null == paymentApplicationFormZJ) {
            throw new OwlBizException("支付申请单不存在");
        }
        if (ApproveStatusEnum.完成初审.getCode() != paymentApplicationFormZJ.getApprovalStatus() && ApproveStatusEnum.审批驳回.getCode() != paymentApplicationFormZJ.getApprovalStatus()) {
            throw new OwlBizException("非完成初审or审批驳回,不允许发起流程");
        }
        //发起OA审批流程
        String fwSSoUrl = paymentAppProcessService.createProcess(Long.valueOf(payApplicationFormId), BusinessProcessType.PAYMENT_APP_FORM);
        //流程发起成功，将单子审批状态修改为审批中
        paymentApplicationFormZJ.setApprovalStatus(ApproveStatusEnum.OA审批中.getCode());
        /*  修改退款管理状态 */
        RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                ApproveStatusEnum.OA审批中.getCode(), ApproveStatusEnum.OA审批中.getValue());
        this.paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
        log.info("审批中,修改退款状态入参:{}",JSONObject.toJSONString(refundManageDTO));
        chargeClient.syncRefundManageStatus(refundManageDTO);
        //返回OA审批链接
        return fwSSoUrl;
    }

    public PaymentApplicationFormZJ save(PaymentApplicationAddFormF applicationAddFormF,ContractPayPlanInnerInfoV contractPayPlanInnerInfoV) {

        List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS = applicationAddFormF.getPaymentApplicationKXDetailVS();
        if (CollectionUtils.isEmpty(paymentApplicationKXDetailVS)) {
            throw new OwlBizException("款项明细不能为空");
        }

        PaymentApplicationFormZJ paymentApplicationFormZJ = Global.mapperFacade.map(applicationAddFormF, PaymentApplicationFormZJ.class);
        LocalDate nowTime = LocalDate.now();
        paymentApplicationFormZJ.setSettlementInfo(JSON.toJSONString(applicationAddFormF.getSettlementList()));
        paymentApplicationFormZJ.setPayApplyCode(queryPayApplyCode(PAY_APPLY_CODE_KEY,nowTime.getYear() + "-" + nowTime.getMonthValue() + nowTime.getDayOfMonth()));
        paymentApplicationFormZJ.setPayStatus(1);
        paymentApplicationFormZJ.setCommunityId(contractPayPlanInnerInfoV.getCommunityId());
        paymentApplicationFormZJ.setCommunityName(contractPayPlanInnerInfoV.getCommunityName());
        paymentApplicationFormZJ.setOppositeOne(contractPayPlanInnerInfoV.getOppositeOne());
        paymentApplicationFormZJ.setOppositeOneId(contractPayPlanInnerInfoV.getOppositeOneId());
        paymentApplicationFormZJ.setRegion(contractPayPlanInnerInfoV.getRegion());
        paymentApplicationFormZJ.setConmanagetypename(contractPayPlanInnerInfoV.getConmanagetypename());
        paymentApplicationFormZJ.setContractAmountOriginalRate(contractPayPlanInnerInfoV.getContractAmountOriginalRate());
        fillBankIdAccount(applicationAddFormF.getAccountCode(),paymentApplicationFormZJ);
        paymentApplicationFormRepository.save(paymentApplicationFormZJ);
        UpLoadFileF upLoadFileF = applicationAddFormF.getUpLoadFileF();
        for (FileVo file : upLoadFileF.getFiles()) {
            VoucherBillFileZJ voucherBillFileZJ = new VoucherBillFileZJ();
            voucherBillFileZJ.setVoucherBillId(paymentApplicationFormZJ.getId());
            voucherBillFileZJ.setFiles(JSON.toJSONString(file));
            if (null != upLoadFileF.getUploadFlag()){
                voucherBillFileZJ.setUploadFlag(upLoadFileF.getUploadFlag());
            } else {
                voucherBillFileZJ.setUploadFlag(0);
            }
            voucherBillFileZJRepository.save(voucherBillFileZJ);
        }

        List<PaymentApplicationFormKxmx> paymentApplicationFormKxmxList = Global.mapperFacade.mapAsList(paymentApplicationKXDetailVS, PaymentApplicationFormKxmx.class);
        paymentApplicationFormKxmxList.forEach(x->x.setPayAppId(String.valueOf(paymentApplicationFormZJ.getId())));
        paymentApplicationFormKXMXRepository.saveBatch(paymentApplicationFormKxmxList);
        List<PaymentApplicationFormPayMxV> paymentApplicationFormPayMxVS = applicationAddFormF.getPaymentApplicationFormPayMxVS();
        if (CollectionUtils.isNotEmpty(paymentApplicationFormPayMxVS)) {
            List<PaymentApplicationFormPayMx> paymentApplicationFormPayMxes = Global.mapperFacade.mapAsList(paymentApplicationFormPayMxVS, PaymentApplicationFormPayMx.class);
            paymentApplicationFormPayMxes.forEach(p->p.setPayAppId(String.valueOf(paymentApplicationFormZJ.getId())));
            paymentApplicationPayMxRepository.saveBatch(paymentApplicationFormPayMxes);
        }

        //********发送信息至工作台********
        SendMessageReqVO sendMessage = new SendMessageReqVO();
        sendMessage.setAppName("APPROVE");
        sendMessage.setAppDesc("审批流程");
        sendMessage.setType("YWZFSQ");
        sendMessage.setTypeDesc("业务申请");
        sendMessage.setTitle("业务部门已提交一笔支付申请，需您初审，请及时处理。");
        sendMessage.setCommunityId(contractPayPlanInnerInfoV.getCommunityId());
        sendMessage.setTenantId(paymentApplicationFormZJ.getTenantId());
        sendMessage.setStatusName(ApproveStatusEnums.待审批.getValue());
        sendMessage.setStatusValue(String.valueOf(ApproveStatusEnums.待审批.getCode()));
        sendMessage.setHandleUserIdArr(this.getUserIds(paymentApplicationFormZJ,personnelRoleId));
        sendMessage.setUserId(paymentApplicationFormZJ.getCreator());
        sendMessage.setUserName(paymentApplicationFormZJ.getCreatorName());
        String s = bpmClient.sendMessage(sendMessage);
        log.info("发送信息至工作台：获取审批流程入参:{},结果:{}", JSON.toJSONString(sendMessage),s);
        paymentApplicationFormZJ.setBpmProcessId(s);
        paymentApplicationFormRepository.updateById(paymentApplicationFormZJ);
        return paymentApplicationFormZJ;
    }

    private String[] getUserIds(PaymentApplicationFormZJ paymentApplicationFormZJ, String roleId){
// 1. 根据项目ID查询关联的用户ID集合
        Set<String> userIds = spaceClient.getPerUserIdsByCommunityId(paymentApplicationFormZJ.getCommunityId());
        // 2. 处理空集合防止NPE
        Set<String> userSet = Optional.ofNullable(userIds).orElse(Collections.emptySet());
        // 调用 Feign 方法时传入 Header 值
        // 3. 查询指定租户和角色下的用户信息（带分页参数）
        List<UserInfoRawV> userInfoList = userClient.listUserInfoByTenantIdAndRoleId(
                paymentApplicationFormZJ.getTenantId(),
                roleId,
                null,   // 关键词参数（按接口定义保留）
                100,     // 分页大小
                true    // 分页标记
        );
        // 处理可能的空响应
        List<UserInfoRawV> validUserList = Optional.ofNullable(userInfoList).orElse(Collections.emptyList());
        // 4. 过滤出社区用户与角色用户的交集
        List<UserInfoRawV> matchedUsers = validUserList.stream()
                .filter(user -> userSet.contains(user.getId()))
                .collect(Collectors.toList());
        return matchedUsers.stream().map(UserInfoRawV::getId).collect(Collectors.toList()).toArray(String[]::new);
    }

    private void fillBankIdAccount(String accountCode, PaymentApplicationFormZJ paymentApplicationFormZJ) {
        ZjDictionaryResponse<BankAccountV> bankAccountVZjDictionaryResponse =  financeCloudService.unitBankInfo(BankAccountReqV.builder().wldwbh(accountCode).build());
        if (null == bankAccountVZjDictionaryResponse || CollectionUtils.isEmpty(bankAccountVZjDictionaryResponse.getData())) {
            throw new OwlBizException("往来单位(收款账户)对应的银行账户信息不存在");
        }
        if (null != paymentApplicationFormZJ) {
            paymentApplicationFormZJ.setBankIdAccount(bankAccountVZjDictionaryResponse.getData().get(0).getId());
        }
    }

    public String queryPayApplyCode(String redisKey, String time) {
        // 调用自增方法获取序列号
        String serialNumber = calculatePayApplyCode(redisKey);
        return String.format("YWZFSQ-%s-%s", time,serialNumber);
    }

    private String calculatePayApplyCode(String redisKey){
        long increment = RedisHelper.increment(redisKey);
        if (increment > 9999L){
            // 当序列号超过最大值时，重置为0
            RedisHelper.set(redisKey, "0");
            increment = RedisHelper.increment(redisKey);
        }
        // 格式化序列号，前面补零
        String serialStr = String.format("%0"+4+"d", increment);
        return serialStr;
    }


    private PaymentApplicationFormZJ update(PaymentApplicationAddFormF applicationAddFormF,ContractPayPlanInnerInfoV contractPayPlanInnerInfoV) {
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.getById(applicationAddFormF.getId());
        if (null == paymentApplicationFormZJ) {
            throw new OwlBizException("支付申请单不存在");
        }
        paymentApplicationFormRepository.removeById(paymentApplicationFormZJ.getId());
        voucherBillFileZJRepository.removeByBillId(Long.valueOf(applicationAddFormF.getId()));
        paymentApplicationFormKXMXRepository.removeByAppId(paymentApplicationFormZJ.getId());
        paymentApplicationPayMxRepository.removeByAppId(paymentApplicationFormZJ.getId());
        applicationAddFormF.setId(null);
        return this.save(applicationAddFormF,contractPayPlanInnerInfoV);
    }


    public PageV<PaymentApplicationFormZJV> pageBySearch(PageF<SearchF<?>> form) {
        Page<PaymentApplicationFormZJ> page = paymentApplicationFormRepository.pageBySearch(form);
        PageV<PaymentApplicationFormZJV> pageV = RepositoryUtil.convertPage(page, PaymentApplicationFormZJV.class);
        if (null != pageV && CollectionUtils.isNotEmpty(pageV.getRecords())) {
            List<ContractPayPlanInnerInfoV> contractIdOnIncomes = contractClient.getInnerInfoByContractIdOnPayApp(pageV.getRecords().stream().map(PaymentApplicationFormZJV::getContractId).collect(Collectors.toList()));
            Map<String,ContractPayPlanInnerInfoV> contractPayPlanInnerInfoVMap = contractIdOnIncomes.stream().collect(Collectors.toMap(ContractPayPlanInnerInfoV::getContractId, v->v,(v1,v2)->v1));
            pageV.getRecords().forEach(x->{
                x.setApprovalStatusName(ApproveStatusEnum.valueOfByCode(x.getApprovalStatus()).getValue());
                x.setPayStatusName(PayStatusEnum.valueOfByCode(x.getPayStatus()).getValue());
                x.setContractServeType(Objects.nonNull(contractPayPlanInnerInfoVMap.get(x.getContractId())) ? contractPayPlanInnerInfoVMap.get(x.getContractId()).getContractServeType() : null);
            });
        }
        return pageV;
    }

    public PaymentApplicationFormZJV detail(String payApplicationFormId) {
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.queryById(payApplicationFormId);
        PaymentApplicationFormZJV payDetail = Global.mapperFacade.map(paymentApplicationFormZJ, PaymentApplicationFormZJV.class);

        List<PaymentApplicationFormPayMx> paymentApplicationFormPayMxes = paymentApplicationPayMxRepository.queryPayDetail(payDetail.getId());
        if(CollectionUtils.isNotEmpty(paymentApplicationFormPayMxes)){
            List<PaymentApplicationZFDetailV> paymentApplicationFormZJVs = Global.mapperFacade.mapAsList(paymentApplicationFormPayMxes, PaymentApplicationZFDetailV.class);
            payDetail.setPaymentApplicationZFDetailVS(paymentApplicationFormZJVs);
        }

        List<PaymentApplicationFormKxmx> paymentApplicationFormKxmxes = paymentApplicationFormKXMXRepository.queryKXMXDetail(payDetail.getId());
        if(CollectionUtils.isNotEmpty(paymentApplicationFormKxmxes)){
            List<PaymentApplicationKXDetailV> kxDetailVS = Global.mapperFacade.mapAsList(paymentApplicationFormKxmxes, PaymentApplicationKXDetailV.class);
            payDetail.setPaymentApplicationKXDetailVS(kxDetailVS);
        }

        List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(Long.parseLong(payApplicationFormId));
        payDetail.setSettlementInfoList(JSONObject.parseArray(payDetail.getSettlementInfo(), SettlementInfoV.class));
        payDetail.setVoucherBillFileS(voucherBillFileZJS);

        payDetail.setDepartmentList(configClient.getDeportList(Collections.singletonList(paymentApplicationFormZJ.getCommunityId())));
        return payDetail;
    }

    public PageV<PaymentApplicationBZDetailV> queryBZDetail(PageF<SearchF<?>> form) {
        Page<PaymentApplicationBZDetailV> voucherPushBillDetailZJPage = voucherBillDetailDxZJMapper.selectBZDBySearch(RepositoryUtil.convertMPPage(form),
                form.getConditions().getQueryModel().orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(voucherPushBillDetailZJPage.getRecords())) {
            for (PaymentApplicationBZDetailV voucherPushBillDetailZJ : voucherPushBillDetailZJPage.getRecords()) {
                voucherPushBillDetailZJ.setTaxRate(voucherPushBillDetailZJ.getTaxRate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
                voucherPushBillDetailZJ.setTaxIncludAmount(voucherPushBillDetailZJ.getTaxIncludAmount().setScale(2, RoundingMode.HALF_UP));
                voucherPushBillDetailZJ.setTaxExcludAmount(voucherPushBillDetailZJ.getTaxExcludAmount().setScale(2, RoundingMode.HALF_UP));
                voucherPushBillDetailZJ.setTaxAmount(voucherPushBillDetailZJ.getTaxAmount().setScale(2, RoundingMode.HALF_UP));
            }
        }
        return RepositoryUtil.convertMoneyPage(voucherPushBillDetailZJPage, PaymentApplicationBZDetailV.class);
    }

    public PageV<PaymentApplicationKXDetailV> queryKXDetails(PageF<SearchF<?>> form) {
        Page<PaymentApplicationKXDetailV> voucherPushBillDetailZJPage = paymentApplicationKXMXMapper.selectKXBySearch(RepositoryUtil.convertMPPage(form),
                form.getConditions().getQueryModel().orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(voucherPushBillDetailZJPage.getRecords())) {
            for (PaymentApplicationKXDetailV voucherPushBillDetailZJ : voucherPushBillDetailZJPage.getRecords()) {
                voucherPushBillDetailZJ.setAmount(voucherPushBillDetailZJ.getAmount().setScale(2, RoundingMode.HALF_UP));
                voucherPushBillDetailZJ.setWriteOffInfoAmount(voucherPushBillDetailZJ.getWriteOffInfoAmount().setScale(2, RoundingMode.HALF_UP));
            }
        }
        return RepositoryUtil.convertMoneyPage(voucherPushBillDetailZJPage, PaymentApplicationKXDetailV.class);
    }

    public PageV<PaymentApplicationZFDetailV> queryPaymentDetails(PageF<SearchF<?>> form) {
        Page<PaymentApplicationZFDetailV> voucherPushBillDetailZJPage = paymentApplicationPayMxMapper.selectZFBySearch(RepositoryUtil.convertMPPage(form),
                form.getConditions().getQueryModel().orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(voucherPushBillDetailZJPage.getRecords())) {
            for (PaymentApplicationZFDetailV voucherPushBillDetailZJ : voucherPushBillDetailZJPage.getRecords()) {
                voucherPushBillDetailZJ.setTransactionAmount(voucherPushBillDetailZJ.getTransactionAmount().setScale(2, RoundingMode.HALF_UP));
                voucherPushBillDetailZJ.setTransactionHL(voucherPushBillDetailZJ.getTransactionHL().setScale(6, RoundingMode.HALF_UP));
            }
        }
        return RepositoryUtil.convertMoneyPage(voucherPushBillDetailZJPage, PaymentApplicationZFDetailV.class);
    }

    public PaymentApplicationFormDetailV preGenerateDetail(PreGenerateDetailF preGenerateDetailF) {
        if (CollectionUtils.isEmpty(preGenerateDetailF.getSettlementIdList())) {
            throw new OwlBizException("结算id不能为空");
        }
        PaymentApplicationFormDetailV paymentApplicationFormDetailV = new PaymentApplicationFormDetailV();
        List<PushZJBusinessBillForSettlement> paymentBillList = generateOnSettlement(preGenerateDetailF);
        if (CollectionUtils.isEmpty(paymentBillList)) {
            return paymentApplicationFormDetailV;
        }
        List<PaymentApplicationKXDetailV> kxmxDetailList = generateKXDetail(paymentBillList);
        paymentApplicationFormDetailV.setPaymentApplicationKXDetailVS(kxmxDetailList);
        List<PaymentApplicationZFDetailV> paymentApplicationZFDetailVS = generateZFDetail(kxmxDetailList);
        paymentApplicationFormDetailV.setPaymentApplicationZFDetailVS(paymentApplicationZFDetailVS);
        return paymentApplicationFormDetailV;
    }

    private List<PaymentApplicationZFDetailV> generateZFDetail(List<PaymentApplicationKXDetailV> kxmxDetailList) {
        List<PaymentApplicationZFDetailV> paymentApplicationZFDetailVS = new ArrayList<>();
        PaymentApplicationZFDetailV paymentApplicationZFDetailV = new PaymentApplicationZFDetailV();
        paymentApplicationZFDetailV.setCurrency("CNY-人民币");
        paymentApplicationZFDetailV.setTransactionAmount(kxmxDetailList.stream().map(PaymentApplicationKXDetailV::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        paymentApplicationZFDetailV.setTransactionHL(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.HALF_UP));
        paymentApplicationZFDetailVS.add(paymentApplicationZFDetailV);
        return paymentApplicationZFDetailVS;
    }

    private List<PaymentApplicationKXDetailV> generateKXDetail(List<PushZJBusinessBillForSettlement> paymentBillList) {
        Map<String,List<PushZJBusinessBillForSettlement>> stringListMap =
                paymentBillList.stream().collect(Collectors.groupingBy(p->p.getSubjectExtId()+"-"+p.getSettlementId()));
        List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS = new ArrayList<>();
        List<String> matchFtList = Lists.newArrayList();
        stringListMap.forEach((mergeKey,list)->{
            PushZJBusinessBillForSettlement one = list.get(0);
            PaymentApplicationKXDetailV paymentApplicationKXDetailV = new PaymentApplicationKXDetailV();
            BigDecimal totalAmount = list.stream()
                    .map(PushZJBusinessBillForSettlement::getTaxIncludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalAmount = totalAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            List<PaymentApplicationKXDetailV> matchedList =
                    mdm63Service.matchForPaySheet(totalAmount, one.getSettlementId(),
                            one.getSubjectExtId(),one.getSubjectName(), matchFtList);
            if (CollectionUtils.isNotEmpty(matchedList)){
                paymentApplicationKXDetailVS.addAll(matchedList);
            } else {
                paymentApplicationKXDetailV.setSubjectCode(one.getSubjectExtId());
                paymentApplicationKXDetailV.setSubjectName(one.getSubjectName());

                paymentApplicationKXDetailV.setWriteOffInfoAmount(one.getWriteOffInfoAmount());
                paymentApplicationKXDetailV.setAmount(totalAmount);
                paymentApplicationKXDetailVS.add(paymentApplicationKXDetailV);
            }


        });
        return paymentApplicationKXDetailVS;
    }

    public List<PushZJBusinessBillForSettlement> generateOnSettlement(PreGenerateDetailF preGenerateDetailF) {
        VoucherBillGenerateForContractSettlementContext context = new VoucherBillGenerateForContractSettlementContext();
        context.setCommunityIdList(com.google.common.collect.Lists.newArrayList(preGenerateDetailF.getCommunityId()));
        context.setSettlementIdList(preGenerateDetailF.getSettlementIdList());
        context.setEventType(9);
        return billRuleDomainService.autoExecute(context);
    }

    public PaymentApplicationBasicV queryBasic(AppBasicF appBasicF) {
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", appBasicF.getCommunityId());
        String xzzz = null;
        String xzzzName = null;
        String xzbm = null;
        String xzbmName = null;
        List<String> collect = community.stream().map(CfgExternalDataV::getExternalDataType).collect(Collectors.toList());
        if (!collect.contains("department")){
            throw new BizException(400, "未维护行政部门");
        }
        if (!collect.contains("org")){
            throw new BizException(400, "未维护行政组织");
        }
        for (CfgExternalDataV cfgExternalDataV : community) {
            if("org".equals(cfgExternalDataV.getExternalDataType())){
                xzzz = cfgExternalDataV.getDataCode();
                xzzzName = cfgExternalDataV.getDataName();
            }
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                xzbm = cfgExternalDataV.getDataCode();
                xzbmName =  cfgExternalDataV.getDataName();
            }
        }
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(xzzz);
        ExternalMaindataCalmappingListV list1 = externalClient.list(externalMaindataCalmappingListF);
        PaymentApplicationBasicV paymentApplicationBasicV = new PaymentApplicationBasicV();
        if (null != list1 && list1.getInfoList().size() > 0) {
            paymentApplicationBasicV.setOrg(list1.getInfoList().get(0).getZaorgno());
        }
        paymentApplicationBasicV.setUnitCode(xzzz);
        paymentApplicationBasicV.setUnitName(xzzzName);
        paymentApplicationBasicV.setDepartName(xzbmName);
        //TODO
        paymentApplicationBasicV.setBillDate(null);

        //TODO
        paymentApplicationBasicV.setHandledBy(null);
        paymentApplicationBasicV.setDepartmentList(configClient.getDeportList(Collections.singletonList(appBasicF.getCommunityId())));
        if(CollectionUtils.isNotEmpty(paymentApplicationBasicV.getDepartmentList()) && paymentApplicationBasicV.getDepartmentList().size() == 1){
            xzbm = paymentApplicationBasicV.getDepartmentList().get(0).getDataCode();
            xzbmName =  paymentApplicationBasicV.getDepartmentList().get(0).getDataName();
            paymentApplicationBasicV.setDepartName(xzbmName);
        }
        return paymentApplicationBasicV;
    }

    //根据工作台bizId获取支付申请单详情
    public PaymentApplicationFormZJV getPaymentByBizId(String bizId) {
        //根据信息台ID获取数据
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.getDetailByBpmProcessId(bizId);
        PaymentApplicationFormZJV payDetail = Global.mapperFacade.map(paymentApplicationFormZJ, PaymentApplicationFormZJV.class);
        List<ContractPayPlanInnerInfoV> contractIdOnIncomes = contractClient.getInnerInfoByContractIdOnPayApp(Arrays.asList(payDetail.getContractId()));
        payDetail.setApprovalStatusName(ApproveStatusEnum.valueOfByCode(payDetail.getApprovalStatus()).getValue());
        payDetail.setPayStatusName(PayStatusEnum.valueOfByCode(payDetail.getPayStatus()).getValue());
        payDetail.setContractServeType(contractIdOnIncomes.get(0).getContractServeType());
        return payDetail;
    }

    //根据业务支付申请单ID删除对应业务支付申请数据
    public Boolean deletePaymentById(Long id){
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.queryById(id.toString());
        if(Objects.isNull(paymentApplicationFormZJ)){
            throw BizException.throw300("该申请单不存在，请输入正确申请单ID");
        }
        //删除数据
        paymentApplicationFormRepository.removeById(id);
        voucherBillFileZJRepository.removeByBillId(id);
        paymentApplicationFormKXMXRepository.removeByAppId(id);
        paymentApplicationPayMxRepository.removeByAppId(id);
        //删除核销锁定数据
        log.info("删除支付申请单,删除核销锁定数据：{}",paymentApplicationFormZJ.getPayApplyCode());
        mdm63LockMapper.deleteByVoucherBillNo(paymentApplicationFormZJ.getPayApplyCode());
        //删除退款管理数据
        RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                ApproveStatusEnum.审批驳回.getCode(), ApproveStatusEnum.审批驳回.getValue());
        log.info("删除支付申请单,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
        chargeClient.refundManageCallBack(refundManageDTO);
        log.info("删除支付申请单,删除退款管理数据入参：{}",JSONArray.toJSON(refundManageDTO));
        chargeClient.removeRefundManage(refundManageDTO);
        return Boolean.TRUE;
    }

}
