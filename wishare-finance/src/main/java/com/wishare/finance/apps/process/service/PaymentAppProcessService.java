package com.wishare.finance.apps.process.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.process.GenericProcessOperate;
import com.wishare.finance.apps.process.enums.BelongRegionEnum;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.pushbill.fo.SettlementF;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDxZJV;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.repository.ReceivableAndTemporaryBillRepository;
import com.wishare.finance.domains.refund.RefundManageDTO;
import com.wishare.finance.domains.refund.RefundManagementDetailDTO;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeF;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeV;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentAppProcessService extends GenericProcessOperate<Long> {

    @Value("${process.create.bizCode:9999}")
    private String bizCode;

    @Autowired
    private PaymentApplicationFormRepository paymentAppFormRepository;

    @Autowired
    private ContractClient contractClient;

    @Autowired
    private BillRuleDomainService billRuleDomainService;
    @Autowired
    private PaymentApplicationFormRepository paymentApplicationFormRepository;
    @Autowired
    private ChargeClient chargeClient;
    @Autowired
    private ReceivableAndTemporaryBillRepository billRepository;

    /**
     * 创建OA流程表单信息
     *
     * @param mainDataId
     * @return
     */
    @Override
    public BusinessInfoF buildBusinessInfoF(Long mainDataId) {
        //查业务支付申请单信息
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentAppFormRepository.getById(mainDataId);
        //创建表单对象
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //基础字段（注意：业务支付申请单的editFlag必须传1）
        businessInfoF.setFormDataId(String.valueOf(mainDataId));
        businessInfoF.setEditFlag(1);
        businessInfoF.setFormType(BusinessProcessType.PAYMENT_APP_FORM.getCode());
        businessInfoF.setFlowType(bizCode);
        businessInfoF.setContractName(paymentApplicationFormZJ.getBusinessReasons());
        if (StrUtil.equals( "9999999999" ,paymentApplicationFormZJ.getContractId())){
            String region = paymentApplicationFormZJ.getRegion();
            businessInfoF.setSsqy(BelongRegionEnum.getCodeByName(region));
        }else{
            //查合同信息
            ContractPayConcludeF contractPayConcludeF = new ContractPayConcludeF();
            contractPayConcludeF.setId(paymentApplicationFormZJ.getContractId());
            ContractPayConcludeV contractPayConcludeV = contractClient.get(contractPayConcludeF);
            //表单字段
            businessInfoF.setSsqy(BelongRegionEnum.getCodeByName(contractPayConcludeV.getRegion()));
        }
        businessInfoF.setSpsxsm(paymentApplicationFormZJ.getBusinessReasons());
        businessInfoF.setZfje(paymentApplicationFormZJ.getTotalPaymentAmount());
        //返回
        return businessInfoF;
    }

    /**
     * 审批驳回回调
     *
     * @param mainDataId
     */
    @Override
    public void reject(Long mainDataId) {
        PaymentApplicationFormZJ form = new PaymentApplicationFormZJ();
        form.setId(mainDataId);
        form.setApprovalStatus(ApproveStatusEnum.审批驳回.getCode());
        paymentAppFormRepository.updateById(form);
        PaymentApplicationFormZJ bean = paymentApplicationFormRepository.queryById(String.valueOf(form.getId()));
        RefundManageDTO refundManageDTO = new RefundManageDTO(bean.getCommunityId(),
                bean.getPayApplyCode(), bean.getId(),
                ApproveStatusEnum.审批驳回.getCode(), ApproveStatusEnum.审批驳回.getValue());
        log.info("审批驳回,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
        chargeClient.syncRefundManageStatus(refundManageDTO);
        //chargeClient.refundManageCallBack(refundManageDTO);
    }

    /**
     * 审批通过回调
     *
     * @param mainDataId
     */
    @Override
    public void approved(Long mainDataId) {
        //按照原逻辑处理审批通过后的流程
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentAppFormRepository.getById(mainDataId);
        VoucherBillGenerateForContractSettlementContext context = new VoucherBillGenerateForContractSettlementContext();
        context.setCommunityIdList(Lists.newArrayList(paymentApplicationFormZJ.getCommunityId()));
        List<SettlementF> settlementFList = JSON.parseArray(paymentApplicationFormZJ.getSettlementInfo(), SettlementF.class);
        context.setSettlementIdList(settlementFList.stream().map(SettlementF::getSettlementId).collect(Collectors.toList()));
        context.setEventType(9);
        context.setSaveVoucher(true);
        context.setBizId(String.valueOf(paymentApplicationFormZJ.getId()));
        context.setExternalDepartmentCode(paymentApplicationFormZJ.getExternalDepartmentCode());

        //业务事由
        context.setReceiptRemark(paymentApplicationFormZJ.getBusinessReasons());
        //备注
        context.setRemark(paymentApplicationFormZJ.getRemarks());
        //附件张数
        context.setUploadNum(paymentApplicationFormZJ.getAttachmentNum());
        //收费系统生成业务申请单
        if("9999999999".equals(paymentApplicationFormZJ.getContractId())){
            RefundManagementDetailDTO detailDTO = new RefundManagementDetailDTO();
            detailDTO.setPayApplyId(paymentApplicationFormZJ.getId());
            detailDTO.setCommunityId(paymentApplicationFormZJ.getCommunityId());
            List<RefundManagementDetailDTO> refundList = chargeClient.queryRefundManageDetail(detailDTO);
            if(CollectionUtils.isNotEmpty(refundList)){
                QueryWrapper<?> receivableBillQueryWrapper = new QueryWrapper<>();
                receivableBillQueryWrapper.eq("deleted",0);
                receivableBillQueryWrapper.in("bill_no",refundList.stream().map(RefundManagementDetailDTO::getBillNo).collect(Collectors.toList()));
                receivableBillQueryWrapper.eq("sup_cp_unit_id", paymentApplicationFormZJ.getCommunityId());
                List<Long> receivableBillIds = billRepository.getReceivableBillIds(receivableBillQueryWrapper);
                if(CollectionUtils.isNotEmpty(receivableBillIds)){
                    context.setBillIdList(receivableBillIds.stream()
                            .map(l -> l != null ? l.toString() : "null")
                            .collect(Collectors.toList()));
                }
            }
        }

        billRuleDomainService.autoExecute(context);
        //更新审批状态为"审批通过"
        PaymentApplicationFormZJ updateForm = new PaymentApplicationFormZJ();
        updateForm.setId(mainDataId);
        updateForm.setApprovalStatus(ApproveStatusEnum.审批通过.getCode());
        paymentAppFormRepository.updateById(updateForm);

        RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationFormZJ.getCommunityId(),
                paymentApplicationFormZJ.getPayApplyCode(), paymentApplicationFormZJ.getId(),
                ApproveStatusEnum.审批通过.getCode(), ApproveStatusEnum.审批通过.getValue());
        log.info("审批通过,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
        chargeClient.syncRefundManageStatus(refundManageDTO);
        chargeClient.refundManageCallBack(refundManageDTO);
    }

    /**
     * 审批中回调
     *
     * @param mainDataId
     */
    @Override
    public void approving(Long mainDataId) {
        PaymentApplicationFormZJ form = new PaymentApplicationFormZJ();
        form.setId(mainDataId);
        form.setApprovalStatus(ApproveStatusEnum.OA审批中.getCode());
        paymentAppFormRepository.updateById(form);
        PaymentApplicationFormZJ bean = paymentApplicationFormRepository.queryById(String.valueOf(form.getId()));
        RefundManageDTO refundManageDTO = new RefundManageDTO(bean.getCommunityId(),
                bean.getPayApplyCode(), bean.getId(),
                ApproveStatusEnum.OA审批中.getCode(), ApproveStatusEnum.OA审批中.getValue());
        log.info("OA审批中回调,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
        chargeClient.syncRefundManageStatus(refundManageDTO);
    }

    //根据业务支付申请单号，生成合同报账单
    public String refreshHtbzdByYwzfsq (Long id){
        log.info("根据业务支付申请单号，生成合同报账单ID:{}", id);
        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentAppFormRepository.getById(id);
        VoucherBillGenerateForContractSettlementContext context = new VoucherBillGenerateForContractSettlementContext();
        context.setCommunityIdList(Lists.newArrayList(paymentApplicationFormZJ.getCommunityId()));
        List<SettlementF> settlementFList = JSON.parseArray(paymentApplicationFormZJ.getSettlementInfo(), SettlementF.class);
        context.setSettlementIdList(settlementFList.stream().map(SettlementF::getSettlementId).collect(Collectors.toList()));
        context.setEventType(9);
        context.setSaveVoucher(true);
        context.setBizId(String.valueOf(paymentApplicationFormZJ.getId()));
        context.setExternalDepartmentCode(paymentApplicationFormZJ.getExternalDepartmentCode());
        //收费系统生成业务申请单
        if("9999999999".equals(paymentApplicationFormZJ.getContractId())){
            log.info("根据业务支付申请单号，生成合同报账单，收费系统来源数据");
            RefundManagementDetailDTO detailDTO = new RefundManagementDetailDTO();
            detailDTO.setPayApplyId(paymentApplicationFormZJ.getId());
            detailDTO.setCommunityId(paymentApplicationFormZJ.getCommunityId());
            List<RefundManagementDetailDTO> refundList = chargeClient.queryRefundManageDetail(detailDTO);
            if(CollectionUtils.isNotEmpty(refundList)){
                QueryWrapper<?> receivableBillQueryWrapper = new QueryWrapper<>();
                receivableBillQueryWrapper.eq("deleted",0);
                receivableBillQueryWrapper.in("bill_no",refundList.stream().map(RefundManagementDetailDTO::getBillNo).collect(Collectors.toList()));
                receivableBillQueryWrapper.eq("sup_cp_unit_id", paymentApplicationFormZJ.getCommunityId());
                List<Long> receivableBillIds = billRepository.getReceivableBillIds(receivableBillQueryWrapper);
                if(CollectionUtils.isNotEmpty(receivableBillIds)){
                    context.setBillIdList(receivableBillIds.stream()
                            .map(l -> l != null ? l.toString() : "null")
                            .collect(Collectors.toList()));
                }
            }
        }
        log.info("根据业务支付申请单号，生成合同报账单,参数：{}", JSONArray.toJSONString(context));
        //AbstractPushBillZJStrategy.doExecuteForPaymentApplicationFormSave
        billRuleDomainService.autoExecute(context);
        return "成功";
    }
}
