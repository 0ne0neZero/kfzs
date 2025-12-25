package com.wishare.finance.apps.process.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.model.bill.fo.ApproveReceivableBillF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.ApproveReceivableBatchBillF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.ReduceApprovalProcessF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ApproveTemporaryBillF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.RemissionManagementDTO;
import com.wishare.finance.apps.model.configure.chargeitem.vo.RemissionManagementDetailDTO;
import com.wishare.finance.apps.process.GenericProcessOperate;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.process.vo.ProcessCreateResultV;
import com.wishare.finance.apps.process.vo.ProcessCreateReturnV;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.FinanceProcessRecordZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReductionAppProcessService extends GenericProcessOperate<ReduceApprovalProcessF> {

    @Autowired
    private FinanceProcessRecordZJRepository financeProcessRecordZJRepository;
    @Autowired
    private ExternalClient externalClient;

    // 1:开启 0:关闭
    @Value("${reduction.oa.process.enable}")
    private Integer reductionOaProcessEnable;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcess(ReduceApprovalProcessF dto, BusinessProcessType processType) {
        FinanceProcessRecordZJ record = financeProcessRecordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getMainDataId, dto.getMainDataId())
                .eq(FinanceProcessRecordZJ::getType, processType.getCode()));
        String requestId = null;
        try {
            if (ObjectUtils.isNotEmpty(record) && StringUtils.isNotBlank(record.getProcessId())) {
                BusinessInfoF businessInfoF = this.buildBusinessInfoF(dto);
                businessInfoF.setProcessId(record.getProcessId());
                log.info("流程更新-表单数据:{}", JSON.toJSONString(businessInfoF));
                ProcessCreateV processCreateV = new ProcessCreateV();
                if (1==reductionOaProcessEnable){
                    processCreateV = externalClient.wfApproveCreate(businessInfoF);
                }else if (0 == reductionOaProcessEnable){
                    processCreateV = mock();
                }
                log.info("流程更新-返回得到的ProcessCreateV:{}", JSON.toJSONString(processCreateV));
                if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                    log.error("流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                    throw new OwlBizException("流程更新失败");
                }
                log.info("当前业务单已经存在流程数据:{}", JSON.toJSONString(record));
                //流程更新成功，审批状态设置为审批中
                record.setReviewStatus(ApproveStatusEnum.OA审批中.getCode());
                financeProcessRecordZJRepository.updateById(record);
                String s = validateFw(record.getProcessId());
                requestId = processCreateV.getET_RESULT().getRequestid();
                return requestId+","+s;
            }

            BusinessInfoF businessInfoF = this.buildBusinessInfoF(dto);
            log.info("流程创建-表单数据:{}", JSON.toJSONString(businessInfoF));
            ProcessCreateV processCreateV = new ProcessCreateV();
            if (1==reductionOaProcessEnable){
                processCreateV = externalClient.wfApproveCreate(businessInfoF);
            }else if(0 == reductionOaProcessEnable){
                processCreateV = mock();
            }
            log.info("流程创建-返回得到的ProcessCreateV:{}", JSON.toJSONString(processCreateV));

            if (ObjectUtils.isEmpty(processCreateV)) {
                throw new OwlBizException("创建流程数据为空,创建流程失败");
            }
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("支出合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                throw new OwlBizException(processCreateV.getES_RETURN().getZZMSG());
            }
            requestId = processCreateV.getET_RESULT().getRequestid();
            if (StringUtils.isBlank(requestId)) {
                throw new OwlBizException("获取到的流程id为空");
            }

            FinanceProcessRecordZJ processRecord = new FinanceProcessRecordZJ();
            processRecord.setProcessId(requestId);
            processRecord.setMainDataId(String.valueOf(dto.getMainDataId()));
            processRecord.setType(processType.getCode());
            //流程记录表中增加审批状态字段
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
        } catch (OwlBizException e) {
            log.info("发起异常：{}",e);
            throw new OwlBizException("OA流程发起超时，请稍后重试！");
        }

        String s = validateFw(requestId);
        return requestId+","+s;


    }

    @Autowired
    private ChargeClient chargeClient;

    @Override
    public void reject(ReduceApprovalProcessF mainDataId) {
        // 驳回
        // 0.获取账单信息 by 流程id
        List<RemissionManagementDetailDTO> remissionManagementDetail = chargeClient.getRemissionManagementDetail(mainDataId.getMainDataId());
        if(ObjectUtils.isEmpty(remissionManagementDetail)){return;}
        ;
        // 1.调用原审批接口
        int billType = remissionManagementDetail.get(0).getBillType();
        switch (billType){
            case 1 :
                for (RemissionManagementDetailDTO rmd : remissionManagementDetail) {
                    ApproveReceivableBillF dto = new ApproveReceivableBillF();
                    dto.setApproveState(3);
                    dto.setBillId(rmd.getBillid());
                    dto.setOperateType(6);
                    dto.setSupCpUnitId(rmd.getCommunityId());
                    //dto.setRejectReason("999111555");
                    try {
                        chargeClient.approveChangeBatch(dto);
                    } catch (Exception e) {
                        log.error("ReductionAppProcessService.reject() chargeClient.approveChangeBatch() called with exception => 【mainDataId = {}, dto = {}】", JSON.toJSONString(mainDataId), JSON.toJSONString(dto), e);
                    }
                }
                break;
            case 3 :
                for (RemissionManagementDetailDTO rmd : remissionManagementDetail) {
                    ApproveTemporaryBillF dto = new ApproveTemporaryBillF();
                    dto.setApproveState(3);
                    dto.setBillId(rmd.getBillid());
                    dto.setOperateType(6);
                    dto.setSupCpUnitId(rmd.getCommunityId());
                    //dto.setRejectReason("999111555");
                    try {
                        chargeClient.approveBatch(dto);
                    } catch (Exception e) {
                        log.error("ReductionAppProcessService.reject() chargeClient.approveBatch() called with exception => 【mainDataId = {}, dto = {}】", JSON.toJSONString(mainDataId), JSON.toJSONString(dto), e);
                    }
                }
                break;
        }
        // 2.把减免管理状态改为驳回
        RemissionManagementDTO dto = new RemissionManagementDTO();
        dto.setBatchNumber(remissionManagementDetail.get(0).getBatchNumber());
        dto.setCommunityId(remissionManagementDetail.get(0).getCommunityId());
        dto.setApprovedState(7);
        chargeClient.updateRemissionManage(dto);
    }

    @Override
    public void approving(ReduceApprovalProcessF mainDataId) {
        return;
    }

    @Override
    public void approved(ReduceApprovalProcessF mainDataId) {
        // 0.获取账单信息 by 流程id
        List<RemissionManagementDetailDTO> remissionManagementDetail = chargeClient.getRemissionManagementDetail(mainDataId.getMainDataId());
        if(ObjectUtils.isEmpty(remissionManagementDetail)){return;}
        ;
        // 1.调用原审批接口
        int billType = remissionManagementDetail.get(0).getBillType();
        switch (billType){
            case 1 :
                for (RemissionManagementDetailDTO rmd : remissionManagementDetail) {
                    ApproveReceivableBillF dto = new ApproveReceivableBillF();
                    dto.setApproveState(2);
                    dto.setBillId(rmd.getBillid());
                    dto.setOperateType(6);
                    dto.setSupCpUnitId(rmd.getCommunityId());
                    try {
                        chargeClient.approveChangeBatch(dto);
                    } catch (Exception e) {
                        log.error("ReductionAppProcessService.approved() chargeClient.approveChangeBatch() called with exception => 【mainDataId = {}, dto = {}】", JSON.toJSONString(mainDataId), JSON.toJSONString(dto), e);
                    }
                }
                break;
            case 3 :
                for (RemissionManagementDetailDTO rmd : remissionManagementDetail) {
                    ApproveTemporaryBillF dto = new ApproveTemporaryBillF();
                    dto.setApproveState(2);
                    dto.setBillId(rmd.getBillid());
                    dto.setOperateType(6);
                    dto.setSupCpUnitId(rmd.getCommunityId());
                    try {
                        chargeClient.approveBatch(dto);
                    } catch (Exception e) {
                        log.error("ReductionAppProcessService.approved() chargeClient.approveBatch() called with exception => 【mainDataId = {}, dto = {}】", JSON.toJSONString(mainDataId), JSON.toJSONString(dto), e);
                    }
                }
                break;
        }
        // 2.把减免管理状态改为驳回
        RemissionManagementDTO dto = new RemissionManagementDTO();
        dto.setBatchNumber(remissionManagementDetail.get(0).getBatchNumber());
        dto.setCommunityId(remissionManagementDetail.get(0).getCommunityId());
        dto.setApprovedState(6);
        chargeClient.updateRemissionManage(dto);
    }

    @Override
    public BusinessInfoF buildBusinessInfoF(ReduceApprovalProcessF dto) {
        BusinessInfoF businessInfoF = new BusinessInfoF();
        // 减免原因
        businessInfoF.setJmyy(dto.getReason());
        // 申请减免金额
        BigDecimal sqjmje = new BigDecimal(dto.getReductionTotalAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        businessInfoF.setSqjmje(sqjmje.toString());
        // 减免比例
        businessInfoF.setJmzkbl(dto.getAdjustRatio());
        // 项目名称
        businessInfoF.setXmmc(dto.getCommunityName());

        businessInfoF.setFlowType("0915");
        businessInfoF.setFormType(9);
        businessInfoF.setJmyy(dto.getReason());

        Integer ssdw = 0;
        String areaName = dto.getAreaName();
        if (StrUtil.contains(areaName,"西部")){
            ssdw=4;
        }else if (StrUtil.contains(areaName,"华南")){
            ssdw=3;
        }else if (StrUtil.contains(areaName,"华东")){
            ssdw=2;
        }else if (StrUtil.contains(areaName,"华北")){
            ssdw=1;
        }
        // 区域
        businessInfoF.setSsdw(ssdw);
        businessInfoF.setSsqy(ssdw);
        businessInfoF.setContractName(dto.getCommunityName() + "-账单减免申请");
        businessInfoF.setFormDataId(dto.getMainDataId());
        return businessInfoF;
    }

    private ProcessCreateV mock(){
        ProcessCreateReturnV ES_RETURN = new ProcessCreateReturnV();
        LocalDateTime localDateTime = LocalDateTimeUtil.of(20250614180102L);
        ES_RETURN.setZZRESTIME(localDateTime);
        ES_RETURN.setZZSTAT("S");
        ES_RETURN.setZZMSG("成功");
        ProcessCreateResultV processCreateResultV = new ProcessCreateResultV();
        processCreateResultV.setMsg("成功");
        processCreateResultV.setCode("0");
        processCreateResultV.setRequestid("33754872");
        ProcessCreateV pojo = new ProcessCreateV();
        pojo.setES_RETURN(ES_RETURN);
        pojo.setET_RESULT(processCreateResultV);
        return pojo;

    }
}
