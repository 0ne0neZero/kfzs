package com.wishare.finance.apps.service.pushbill;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.pushbill.fo.ContractInvoiceInfoF;
import com.wishare.finance.apps.pushbill.fo.InvoiceZJF;
import com.wishare.finance.apps.pushbill.fo.MeasurementDetailF;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.pushbill.enums.VoucherInvoiceZJEnums;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractMeasurementDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractMeasurementDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.finance.domains.voucher.consts.enums.VoucherApproveStateEnum;
import com.wishare.finance.infrastructure.remote.clients.base.BpmClient;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractVoucherService {

    private final BpmClient bpmClient;
    private final SubjectRepository subjectRepository;

    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;

    private final VoucherPushBillZJRepository voucherPushBillZJRepository;

    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    private final VoucherContractInvoiceZJRepository voucherContractInvoiceZJRepository;
    private final VoucherInvoiceZJRepository voucherInvoiceZJRepository;
    private final VoucherContractMeasurementDetailZJRepository voucherContractMeasurementDetailZJRepository;
    @Transactional(rollbackFor = Exception.class)
    public Long acceptContractInvoice(ContractInvoiceInfoF contractInvoiceInfoF) {
        log.info("合同系统推送收票信息且生成对下结算单-请求数据：{}", JSONObject.toJSONString(contractInvoiceInfoF));
        // 1. 校验

        // 2. 封装汇总单据实体
        List<MeasurementDetailF> measurementDetailFList = contractInvoiceInfoF.getDetailFList();
        List<InvoiceZJF> invoiceZJFList = contractInvoiceInfoF.getInvoiceZJFList();
        VoucherBillZJ voucherBillZJ = voucherPushBill(contractInvoiceInfoF);

        // 3. 封装报账明细实体
        List<VoucherPushBillDetailZJ>billDetailZJList=new ArrayList<>();

        // 循环计量明细，每一个费项封装一个报账明细
        for (MeasurementDetailF detailF : measurementDetailFList) {
            VoucherPushBillDetailZJ pushBillDetailZJ = new VoucherPushBillDetailZJ();
            buildSubjectAndCashFlowBillDetail( detailF.getChargeItemId(), pushBillDetailZJ);

            pushBillDetailZJ.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL_ZJ));
            pushBillDetailZJ.setVoucherBillNo(voucherBillZJ.getVoucherBillNo());
            pushBillDetailZJ.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            pushBillDetailZJ.setCommunityId(contractInvoiceInfoF.getCommunityId());
            pushBillDetailZJ.setCommunityName(contractInvoiceInfoF.getCommunityName());
            pushBillDetailZJ.setTaxRate(detailF.getTaxRate());
            pushBillDetailZJ.setChargeItemId(detailF.getChargeItemId());
            pushBillDetailZJ.setChargeItemName(detailF.getChargeItemName());
            pushBillDetailZJ.setTaxExcludAmount(detailF.getTaxExcludedAmount());
            pushBillDetailZJ.setTaxIncludAmount(detailF.getTaxIncludedAmount());
            pushBillDetailZJ.setTaxAmount(detailF.getTaxIncludedAmount() - detailF.getTaxExcludedAmount());
            pushBillDetailZJ.setBillEventType(3);
            pushBillDetailZJ.setContractId(contractInvoiceInfoF.getContractId());
            billDetailZJList.add(pushBillDetailZJ);
        }
        voucherPushBillZJRepository.save(voucherBillZJ);
        voucherBillDetailZJRepository.saveBatch(billDetailZJList);

        // 4. 对下结算单
        VoucherContractInvoiceZJ contractInvoiceZJ = Global.mapperFacade.map(contractInvoiceInfoF, VoucherContractInvoiceZJ.class);
        contractInvoiceZJ.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_CONTRACT_INVOICE_ZJ));
        contractInvoiceZJ.setVoucherBillId(voucherBillZJ.getId());
        // 计量明细
        List<VoucherContractMeasurementDetailZJ> measurementDetailZJS = Global.mapperFacade.mapAsList(measurementDetailFList, VoucherContractMeasurementDetailZJ.class);
        for (VoucherContractMeasurementDetailZJ detailZJ : measurementDetailZJS) {
            detailZJ.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_CONTRACT_MEASURE_DETAIL_ZJ));
            detailZJ.setContractInvoiceId(contractInvoiceZJ.getId());
            detailZJ.setVoucherBillId(voucherBillZJ.getId());
        }

        // 发票信息
        List<VoucherInvoiceZJ> invoiceZJS = Global.mapperFacade.mapAsList(invoiceZJFList, VoucherInvoiceZJ.class);
        for (VoucherInvoiceZJ invoiceZJ : invoiceZJS) {
            invoiceZJ.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_INVOICE_ZJ));
            invoiceZJ.setContractInvoiceId(contractInvoiceZJ.getId());
            invoiceZJ.setVoucherBillId(voucherBillZJ.getId());
            invoiceZJ.setInvoiceType(VoucherInvoiceZJEnums.valueOfByCode(invoiceZJ.getInvoiceType()).getValue());
        }
        voucherContractInvoiceZJRepository.save(contractInvoiceZJ);
        voucherContractMeasurementDetailZJRepository.saveBatch(measurementDetailZJS);
        voucherInvoiceZJRepository.saveBatch(invoiceZJS);

        // 5. 根据审批状态判断是否审批
        if (!VoucherApproveStateEnum.无需审批.equalsByCode(contractInvoiceInfoF.getApproveState())) {
            // 发起审批
            String s = initiateApprove(contractInvoiceInfoF, voucherBillZJ.getId());
            voucherBillZJ.setProcInstId(s);
        } else {
            voucherBillZJ.setApproveState(0);
        }
        voucherPushBillZJRepository.updateById(voucherBillZJ);
        return voucherBillZJ.getId();
    }

    /**
     * 为报账明细实体封装业务科目和现金流量
     *
     * @param chargeItemId     费项id
     * @param pushBillDetailZJ 报账明细实体
     */
    private void buildSubjectAndCashFlowBillDetail(Long chargeItemId, VoucherPushBillDetailZJ pushBillDetailZJ) {

        // 3.1 业务科目
//        SubjectE subjectByCode = subjectRepository.getSubjectByCode("03008");
//        SubjectE subject = subjectMapUnitDetailRepository.getSubject(subjectByCode.getId(), chargeItemId, 1);
//        if (Objects.nonNull(subject)) {
//            pushBillDetailZJ.setSubjectId(subject.getId());
//            pushBillDetailZJ.setSubjectName(subject.getSubjectName());
//            pushBillDetailZJ.setSubjectExtId(subject.getIdExt());
//        }
        SubjectE subjectByCode1 = subjectRepository.getSubjectByCode("02011");
        SubjectE subjectByCode2 = subjectRepository.getSubjectByCode("02020");
        SubjectE subjectByCode3 = subjectRepository.getSubjectByCode("02021");
        SubjectE subjectByCode4 = subjectRepository.getSubjectByCode("02022");

        SubjectE subject1 = subjectMapUnitDetailRepository.getSubject(subjectByCode1.getId(), chargeItemId, 1);
        SubjectE subject2 = subjectMapUnitDetailRepository.getSubject(subjectByCode2.getId(), chargeItemId, 1);
        SubjectE subject3 = subjectMapUnitDetailRepository.getSubject(subjectByCode3.getId(), chargeItemId, 1);
        SubjectE subject4 = subjectMapUnitDetailRepository.getSubject(subjectByCode4.getId(), chargeItemId, 1);

        if (Objects.nonNull(subject1)) {
            pushBillDetailZJ.setSubjectId(subject1.getId());
            pushBillDetailZJ.setSubjectName(subject1.getSubjectName());
            pushBillDetailZJ.setSubjectExtId(subject1.getIdExt());
        } else if (Objects.nonNull(subject2)){
            pushBillDetailZJ.setSubjectId(subject2.getId());
            pushBillDetailZJ.setSubjectName(subject2.getSubjectName());
            pushBillDetailZJ.setSubjectExtId(subject2.getIdExt());
        } else if (Objects.nonNull(subject3)){
            pushBillDetailZJ.setSubjectId(subject3.getId());
            pushBillDetailZJ.setSubjectName(subject3.getSubjectName());
            pushBillDetailZJ.setSubjectExtId(subject3.getIdExt());
        } else if (Objects.nonNull(subject4)) {
            pushBillDetailZJ.setSubjectId(subject4.getId());
            pushBillDetailZJ.setSubjectName(subject4.getSubjectName());
            pushBillDetailZJ.setSubjectExtId(subject4.getIdExt());
        }
        // 现金流量项目
        SubjectMapUnitDetailE unitDetailE = subjectMapUnitDetailRepository.getByUnitId(chargeItemId, SubjectRuleMapTypeEnum.现金流量.getCode());
        if (Objects.nonNull(unitDetailE)) {
            pushBillDetailZJ.setCashFlowItem(unitDetailE.getSubjectLevelLastName());
        }
    }

    /** 封装汇总单据实体
     * @param contractInvoiceInfoF 入参
     * @return
     */
    public VoucherBillZJ voucherPushBill( ContractInvoiceInfoF contractInvoiceInfoF) {
        VoucherBillZJ voucherBill = new VoucherBillZJ();
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_ZJ));
        voucherBill.setRuleId(null);
        voucherBill.setRuleName("对下结算单");
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(contractInvoiceInfoF.getCostCenterId());
        voucherBill.setCostCenterName(contractInvoiceInfoF.getCostCenterName());
        voucherBill.setBillEventType(3);
        voucherBill.setApproveState(PushBillApproveStateEnum.审核中.getCode());
        voucherBill.setTotalAmount(contractInvoiceInfoF.getInvoiceZJFList().stream().mapToLong(InvoiceZJF::getPayAmount).sum());
        return voucherBill;
    }


    /**
     * 发起审批
     */
    public String initiateApprove(ContractInvoiceInfoF contractInvoiceInfoF, Long voucherBillId) {
        try {
            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(contractInvoiceInfoF.getApproveRule());
            log.info("ChargeApproveAppService.batchBpmAdjust获取审批流程入参,wflowModelHistorysV:{},结果:{}", contractInvoiceInfoF.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }
            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", OperateTypeEnum.资金收款.getDes());
            formData.put("flowId", voucherBillId);
            processStartF.setFormData(formData);
            processStartF.setBusinessKey(String.valueOf(voucherBillId));
            processStartF.setBusinessType(OperateTypeEnum.资金收款.getDes());
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(contractInvoiceInfoF.getCommunityId());

            return bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
        } catch (BizException e) {
            log.info("流程发起异常：{}",e);
            log.error("流程发起异常：{}",e);
            throw new OwlBizException("流程发起超时，请稍后重试！");
        }
    }
}
