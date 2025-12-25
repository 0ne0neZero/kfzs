package com.wishare.contract.apps.service.contractset;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.CollectionPlanPaymentInvoiceF;
import com.wishare.contract.apps.remote.finance.facade.FinanceFacade;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.wishare.contract.domains.entity.contractset.ContractReceiveInvoiceDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractBpmProcessRecordMapper;
import com.wishare.contract.domains.mapper.contractset.ContractConcludeMapper;
import com.wishare.contract.domains.mapper.contractset.ContractPaymentDetailMapper;
import com.wishare.contract.domains.mapper.contractset.ContractReceiveInvoiceDetailMapper;
import com.wishare.contract.domains.service.contractset.ContractBpmProcessRecordService;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.owl.enhance.IOwlApiBase;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Objects;


@Service
@Slf4j
public class ContractBpmAppService implements IOwlApiBase{

    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeAppService contractConcludeAppService;

    @Setter(onMethod_ = {@Autowired})
    private ContractBpmProcessRecordMapper contractBpmProcessRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractBpmProcessRecordService contractBpmProcessRecordService;

    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeMapper contractConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractPaymentDetailMapper contractPaymentDetailMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractReceiveInvoiceDetailMapper contractReceiveInvoiceDetailMapper;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFacade financeFacade;


    @Transactional
    public boolean dealBpmExpendApply(String bpmResId,Boolean checkFlag,String errorMsg) throws ParseException {
        LambdaQueryWrapper<ContractBpmProcessRecordE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBpmProcessRecordE::getProcessId,bpmResId).eq(ContractBpmProcessRecordE::getDeleted,0);
        ContractBpmProcessRecordE contractBpmProcessRecordE = contractBpmProcessRecordMapper.selectOne(queryWrapper);
        if (Objects.isNull(contractBpmProcessRecordE)){
            log.error("external合同订立bpm回调无法找到流程记录,bpmResId:"+bpmResId);
            return false;
        }
        Long contractId = contractConcludeMapper.selectOneByBpmRecordId(contractBpmProcessRecordE.getId());
        if (Objects.isNull(contractId)){
            log.error("external合同订立bpm回调无法找到流程id对应合同,contractId:"+contractId);
            return false;
        }
        Integer reviewStatus = checkFlag?ContractSetConst.POSS:ContractSetConst.REJECT;
        contractBpmProcessRecordE.setReviewStatus(reviewStatus);
        if (!checkFlag) {
            contractBpmProcessRecordE.setRejectReason(errorMsg);
        }
        contractBpmProcessRecordMapper.updateById(contractBpmProcessRecordE);
        //如果审核通过，则变更合同状态
        if(checkFlag){
            contractConcludeAppService.contractState(contractId, ContractSetConst.PERFORM, ContractSetConst.POSS);
        }else {//如果审核不通过，则更新合同审核状态
            ContractConcludeE contractConcludeE = new ContractConcludeE();
            contractConcludeE.setId(contractId);
            contractConcludeE.setReviewStatus(reviewStatus);
            contractConcludeMapper.updateById(contractConcludeE);
        }
        return true;
    }

    @Transactional
    public boolean dealBpmExpendPayApply(String bpmResId,Boolean checkFlag,String errorMsg) {
        LambdaQueryWrapper<ContractBpmProcessRecordE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBpmProcessRecordE::getProcessId,bpmResId).eq(ContractBpmProcessRecordE::getDeleted,0);
        ContractBpmProcessRecordE contractBpmProcessRecordE = contractBpmProcessRecordMapper.selectOne(queryWrapper);
        if (Objects.isNull(contractBpmProcessRecordE)){
            log.error("external合同支付付款bpm回调无法找到流程记录,bpmResId:"+bpmResId);
            return false;
        }
        //查询付款明细信息
        LambdaQueryWrapper<ContractPaymentDetailE> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(ContractPaymentDetailE::getProcId,contractBpmProcessRecordE.getId()).eq(ContractPaymentDetailE::getDeleted,0);
        ContractPaymentDetailE pay = contractPaymentDetailMapper.selectOne(payQueryWrapper);
        if (Objects.isNull(pay)){//付款明细为空。查询收票明细信息
            //查询收票明细信息
            LambdaQueryWrapper<ContractReceiveInvoiceDetailE> invoiceQueryWrapper = new LambdaQueryWrapper<>();
            invoiceQueryWrapper.eq(ContractReceiveInvoiceDetailE::getProcId,contractBpmProcessRecordE.getId()).eq(ContractReceiveInvoiceDetailE::getDeleted,0);
            ContractReceiveInvoiceDetailE invoice = contractReceiveInvoiceDetailMapper.selectOne(invoiceQueryWrapper);
            if (Objects.isNull(invoice)){
                log.error("external合同支付收票bpm回调无法找到流程id对应收票明细,procId:"+contractBpmProcessRecordE.getId());
                return false;
            }
            Integer reviewStatus = checkFlag?ContractSetConst.POSS:ContractSetConst.REJECT;
            contractBpmProcessRecordE.setReviewStatus(reviewStatus);
            if (!checkFlag) {
                contractBpmProcessRecordE.setRejectReason(errorMsg);
            }
            contractBpmProcessRecordMapper.updateById(contractBpmProcessRecordE);
            Integer auditStatus = checkFlag?ContractSetConst.PAYMENT_POSS:ContractSetConst.PAYMENT_REJECT;
            ContractReceiveInvoiceDetailE contractPaymentDetailE = new ContractReceiveInvoiceDetailE();
            contractPaymentDetailE.setId(invoice.getId());
            contractPaymentDetailE.setAuditStatus(auditStatus);
            contractReceiveInvoiceDetailMapper.updateById(contractPaymentDetailE);
        }else {
            Integer reviewStatus = checkFlag?ContractSetConst.POSS:ContractSetConst.REJECT;
            contractBpmProcessRecordE.setReviewStatus(reviewStatus);
            if (!checkFlag) {
                contractBpmProcessRecordE.setRejectReason(errorMsg);
            }
            contractBpmProcessRecordMapper.updateById(contractBpmProcessRecordE);
            if(checkFlag){//审核通过需要生成付款单
                CollectionPlanPaymentInvoiceF item = new CollectionPlanPaymentInvoiceF();
                item.setContractId(pay.getContractId());
                item.setCollectionPlanId(pay.getCollectionPlanId());
                item.setPaymentMethod(pay.getPaymentMethod());
                item.setPaymentAmount(pay.getPaymentAmount());
                if (item.getPaymentAmount() != null && item.getPaymentAmount() != BigDecimal.ZERO) {
                    financeFacade.addPayBill(item);
                }
            }
            Integer auditStatus = checkFlag?ContractSetConst.PAYMENT_POSS:ContractSetConst.PAYMENT_REJECT;
            ContractPaymentDetailE contractPaymentDetailE = new ContractPaymentDetailE();
            contractPaymentDetailE.setId(pay.getId());
            contractPaymentDetailE.setAuditStatus(auditStatus);
            contractPaymentDetailMapper.updateById(contractPaymentDetailE);
        }
        return true;
    }

    @Transactional
    public boolean dealBpmExpendInvoiceApply(String bpmResId,Boolean checkFlag,String errorMsg) {
        LambdaQueryWrapper<ContractBpmProcessRecordE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBpmProcessRecordE::getProcessId,bpmResId).eq(ContractBpmProcessRecordE::getDeleted,0);
        ContractBpmProcessRecordE contractBpmProcessRecordE = contractBpmProcessRecordMapper.selectOne(queryWrapper);
        if (Objects.isNull(contractBpmProcessRecordE)){
            log.error("external合同支付收票bpm回调无法找到流程记录,bpmResId:"+bpmResId);
            return false;
        }
        //查询收票明细信息
        LambdaQueryWrapper<ContractReceiveInvoiceDetailE> payQueryWrapper = new LambdaQueryWrapper<>();
        payQueryWrapper.eq(ContractReceiveInvoiceDetailE::getProcId,contractBpmProcessRecordE.getId()).eq(ContractReceiveInvoiceDetailE::getDeleted,0);
        ContractReceiveInvoiceDetailE pay = contractReceiveInvoiceDetailMapper.selectOne(payQueryWrapper);
        if (Objects.isNull(pay)){
            log.error("external合同支付收票bpm回调无法找到流程id对应收票明细,procId:"+contractBpmProcessRecordE.getId());
            return false;
        }
        Integer reviewStatus = checkFlag?ContractSetConst.POSS:ContractSetConst.REJECT;
        contractBpmProcessRecordE.setReviewStatus(reviewStatus);
        if (!checkFlag) {
            contractBpmProcessRecordE.setRejectReason(errorMsg);
        }
        contractBpmProcessRecordMapper.updateById(contractBpmProcessRecordE);
        Integer auditStatus = checkFlag?ContractSetConst.PAYMENT_POSS:ContractSetConst.PAYMENT_REJECT;
        ContractReceiveInvoiceDetailE contractPaymentDetailE = new ContractReceiveInvoiceDetailE();
        contractPaymentDetailE.setId(pay.getId());
        contractPaymentDetailE.setAuditStatus(auditStatus);
        contractReceiveInvoiceDetailMapper.updateById(contractPaymentDetailE);
        return true;
    }
}
