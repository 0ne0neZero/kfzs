package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.fo.ReduceApprovalProcessF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.VisitorApprovalProcessF;
import com.wishare.finance.apps.model.reconciliation.fo.external.OpinionApprovalDataF;
import com.wishare.finance.apps.model.reconciliation.fo.external.OpinionApprovalF;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.ApprovalQueryF;
import com.wishare.finance.apps.process.fo.ProcessAdjustCallBackF;
import com.wishare.finance.apps.process.service.DxJsBillProcessService;
import com.wishare.finance.apps.process.service.PaymentAppProcessService;
import com.wishare.finance.apps.process.service.ReductionAppProcessService;
import com.wishare.finance.apps.process.service.VisitorAppProcessService;
import com.wishare.finance.apps.process.vo.OpinionApprovalV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.FinanceProcessRecordZJMapper;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalV2;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@Service
public class FinanceProcessRecordZJRepository extends ServiceImpl<FinanceProcessRecordZJMapper, FinanceProcessRecordZJ> {

    @Autowired
    private PaymentAppProcessService paymentAppProcessService;

    @Autowired
    @Lazy
    private DxJsBillProcessService dxJsBillProcessService;

    @Autowired
    private ExternalClient externalClient;

    @Autowired
    private VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;

    @Autowired
    private FinanceProcessRecordZJRepository recordZJRepository;
    @Autowired
    private ReductionAppProcessService reductionAppProcessService;
    @Autowired
    private VisitorAppProcessService visitorAppProcessService;

    /**
     * 审批回调处理
     *
     * @param paCallBackF
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean dealFinanceProcessRecord(ProcessAdjustCallBackF paCallBackF, Integer type, Integer skip) {
        if (ObjectUtils.isEmpty(paCallBackF) || ObjectUtils.isEmpty(paCallBackF.getFormData())) {
            log.info("审批回调的核心参数为空");
            return Boolean.FALSE;
        }
        String processId = paCallBackF.getProcessId();
        String mainDataId = paCallBackF.getFormData().getFormDataId();
        Integer reviewStatus = paCallBackF.getReviewStatus();
        log.info("审批回调逻辑处理接收到的核心参数,processId:{},mainDataId:{},reviewStatus:{},type:{}", processId, mainDataId, reviewStatus, type);
        if (StringUtils.isBlank(processId)) {
            log.error("external流程回调没有没有传流程id");
            throw new OwlBizException("审批回调流程id为空");
        }
        if (StringUtils.isBlank(mainDataId)) {
            log.error("external流程回调没有没有传业务id");
            throw new OwlBizException("审批回调业务id为空");
        }
        if (ObjectUtils.isEmpty(reviewStatus)) {
            log.error("external流程回调没有没有传结果状态");
            throw new OwlBizException("审批回调审批状态为空");
        }
        //37696962
        FinanceProcessRecordZJ recordE = this.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getProcessId, processId));

        if (ObjectUtils.isEmpty(recordE)) {
            log.error("external流程创建回调无法找到流程记录,processId:" + processId);
            throw new OwlBizException("审批回调无法找到流程记录");
        }
        //若skip为空（正常回调） 且 重复调用，则进行拦截；若skip不为空（手动回调），那么不进行校验
        if (ObjectUtils.isEmpty(skip) && reviewStatus.equals(recordE.getReviewStatus())) {
            log.info("external流程创建回调状态重复次本次跳过");
            throw new OwlBizException("external流程创建回调状态重复次本次跳过");
        }
        type = recordE.getType();
        log.info("流程id查询得到的回调类型:{}", type);
        if (BusinessProcessType.Reduction_FORM.getCode().equals(type)){
            // 驳回状态
            boolean flagStatus0 = 0 == reviewStatus && reviewStatus.equals(recordE.getReviewStatus());
            // 通过状态
            boolean flagStatus2 = 2 ==reviewStatus && reviewStatus.equals(recordE.getReviewStatus());
            if (flagStatus0 || flagStatus2) {
                log.info("减免审批驳回/通过状态重复");
                throw new OwlBizException("减免审批驳回/通过状态重复");
            }
        }
        //更新流程记录表中的审批状态
        recordE.setReviewStatus(reviewStatus);
        recordZJRepository.updateById(recordE);

        if (BusinessProcessType.PAYMENT_APP_FORM.getCode().equals(type)) {
            return paymentAppProcessService.handleReviewStatus(Long.valueOf(mainDataId), reviewStatus);
        } else if (BusinessProcessType.DX_JS_FORM.getCode().equals(type) || BusinessProcessType.SRQR_SQ_FORM.getCode().equals(type)) {
            return dxJsBillProcessService.handleReviewStatus(Long.valueOf(mainDataId), reviewStatus);
        }
        else if (BusinessProcessType.Reduction_FORM.getCode().equals(type)){
            ReduceApprovalProcessF reduceApprovalProcessF = new ReduceApprovalProcessF();
            reduceApprovalProcessF.setMainDataId(mainDataId);
            return  reductionAppProcessService.handleReviewStatus(reduceApprovalProcessF, reviewStatus);
        } else if (BusinessProcessType.VISITOR_FORM.getCode().equals(type)){
            VisitorApprovalProcessF visitorApprovalProcessF = new VisitorApprovalProcessF();
            visitorApprovalProcessF.setMainDataId(mainDataId);
            return  visitorAppProcessService.handleReviewStatus(visitorApprovalProcessF, reviewStatus);
        }
        return Boolean.TRUE;
    }

    /**
     * 查询oa审批信息
     * 业务支付申请单和对下结算单-实签，点详情进去的详情接口传的都是对下结算主表的id
     * 所以对于对下结算单-实签，id就是自己的id，能够根据id查询到流程信息
     * 但是对于业务申请单，需要根据id换业务支付申请的id再查询流程信息
     *
     * @param approvalQueryF
     * @return
     */
    public OpinionApprovalV opinionApproval(ApprovalQueryF approvalQueryF) {
        OpinionApprovalF opinionApprovalF = new OpinionApprovalF();
        OpinionApprovalDataF opinionApprovalDataF = new OpinionApprovalDataF();
        Long mainDataId = approvalQueryF.getId();
        if (BusinessProcessType.PAYMENT_APP_FORM.equals(approvalQueryF.getType())) {
            VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.getById(approvalQueryF.getId());
            if (Objects.isNull(voucherBillDxZJ)){
                throw new OwlBizException("报账单不存在,请检查!");
            }
            mainDataId = Long.valueOf(voucherBillDxZJ.getPayAppId());
        }

        FinanceProcessRecordZJ record = recordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getMainDataId, mainDataId)
                .eq(FinanceProcessRecordZJ::getType, approvalQueryF.getType().getCode()));
        if (ObjectUtils.isEmpty(record) || StringUtils.isBlank(record.getProcessId())) {
            log.info("当前业务id没有流程信息");
            return new OpinionApprovalV();
        }
        opinionApprovalDataF.setFormdataid(record.getMainDataId());
        opinionApprovalDataF.setRequestId(record.getProcessId());
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalClient.opinionApproval(opinionApprovalF);
    }

    public OpinionApprovalV2 opinionApprovalV2(String processId) {
        OpinionApprovalF opinionApprovalF = new OpinionApprovalF();
        OpinionApprovalDataF opinionApprovalDataF = new OpinionApprovalDataF();
        FinanceProcessRecordZJ record = recordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                        .eq(FinanceProcessRecordZJ::getProcessId, processId));
        opinionApprovalDataF.setFormdataid(record.getMainDataId());
        opinionApprovalDataF.setRequestId(processId);
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalClient.opinionApprovalV2(opinionApprovalF);
    }


}
