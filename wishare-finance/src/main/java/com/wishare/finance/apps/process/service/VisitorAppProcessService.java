package com.wishare.finance.apps.process.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.model.configure.chargeitem.fo.VisitorApprovalProcessF;
import com.wishare.finance.apps.process.GenericProcessOperate;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.FinanceProcessRecordZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.PassClient;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class VisitorAppProcessService extends GenericProcessOperate<VisitorApprovalProcessF> {
    @Autowired
    private FinanceProcessRecordZJRepository financeProcessRecordZJRepository;
    @Autowired
    private ExternalClient externalClient;
    @Autowired
    private PassClient passClient;

    public ProcessCreateV createProcessVistor(VisitorApprovalProcessF dto, BusinessProcessType processType) {
        FinanceProcessRecordZJ record = financeProcessRecordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getMainDataId, dto.getMainDataId())
                .eq(FinanceProcessRecordZJ::getType, processType.getCode()));
        String requestId = null;
        /* 更新流程 */
        if(Objects.nonNull(record) && StrUtil.isNotBlank(record.getProcessId())) {
            BusinessInfoF businessInfoF = this.buildBusinessInfoF(dto);
            businessInfoF.setProcessId(record.getProcessId());
            log.info("访客邀约流程创建-表单数据:{}", JSON.toJSONString(businessInfoF));
            ProcessCreateV processCreateV = externalClient.wfApproveCreate(businessInfoF);
            log.info("访客邀约流程更新-返回得到的ProcessCreateV:{}", JSON.toJSONString(processCreateV));
            requestId = processCreateV.getET_RESULT().getRequestid();
            checkOAResult(processCreateV, requestId);
            record.setReviewStatus(ApproveStatusEnum.OA审批中.getCode());
            financeProcessRecordZJRepository.updateById(record);
            return processCreateV;
        }

        /* 新增流程 */
        BusinessInfoF businessInfoF = this.buildBusinessInfoF(dto);
        log.info("访客邀约流程创建-表单数据:{}", JSON.toJSONString(businessInfoF));
        ProcessCreateV processCreateV = externalClient.wfApproveCreate(businessInfoF);
        log.info("访客邀约流程创建-返回得到的ProcessCreateV:{}", JSON.toJSONString(processCreateV));
        requestId = processCreateV.getET_RESULT().getRequestid();
        checkOAResult(processCreateV, requestId);
        FinanceProcessRecordZJ processRecord = new FinanceProcessRecordZJ();
        processRecord.setProcessId(requestId);
        processRecord.setMainDataId(String.valueOf(dto.getMainDataId()));
        processRecord.setType(processType.getCode());
        processRecord.setReviewStatus(ApproveStatusEnum.OA审批中.getCode());
        FinanceProcessRecordZJ existRecord = financeProcessRecordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getProcessId, requestId));
        if (ObjectUtils.isNotEmpty(existRecord)) {
            log.info("流程已存在,进行流程数据更新");
            processRecord.setId(existRecord.getId());
            financeProcessRecordZJRepository.updateById(processRecord);
        } else {
            log.info("流程不存在,进行流程数据新增");
            financeProcessRecordZJRepository.save(processRecord);
        }
        return processCreateV;
    }

    private static void checkOAResult(ProcessCreateV processCreateV, String requestId) {
        if (ObjectUtils.isEmpty(processCreateV)) {
            throw new OwlBizException("创建流程数据为空,创建流程失败");
        }
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("访客邀约流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            throw new OwlBizException(processCreateV.getES_RETURN().getZZMSG());
        }
        if (StringUtils.isBlank(requestId)) {
            throw new OwlBizException("获取到的流程id为空");
        }
    }

    @Override
    public BusinessInfoF buildBusinessInfoF(VisitorApprovalProcessF mainDataId) {
        BusinessInfoF businessInfoF = new BusinessInfoF();
        BeanUtil.copyProperties(mainDataId, businessInfoF);
        businessInfoF.setFlowType("10680836");
        businessInfoF.setFormDataId(mainDataId.getMainDataId());
        businessInfoF.setFormType(12);
        businessInfoF.setContractName("关于"+mainDataId.getSqrName()+"访客邀约的申请");
        businessInfoF.setIsNextFlow("1");
        return businessInfoF;
    }

    @Override
    public void reject(VisitorApprovalProcessF mainDataId) {
        /* 调用接口透传不做业务处理 */
        mainDataId.setMainDataId(mainDataId.getMainDataId());
        mainDataId.setReviewStatus(0);
        passClient.visitorApprovalCallback(mainDataId);
    }

    @Override
    public void approved(VisitorApprovalProcessF mainDataId) {
        mainDataId.setMainDataId(mainDataId.getMainDataId());
        mainDataId.setReviewStatus(2);
        passClient.visitorApprovalCallback(mainDataId);
    }

    @Override
    public void approving(VisitorApprovalProcessF mainDataId) {
        mainDataId.setMainDataId(mainDataId.getMainDataId());
        mainDataId.setReviewStatus(1);
        passClient.visitorApprovalCallback(mainDataId);
    }
}
