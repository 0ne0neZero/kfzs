package com.wishare.finance.apps.process;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.process.fo.FwSSoBaseInfoF;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.FinanceProcessRecordZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.vo.user.UserInfoRv;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.wishare.finance.apps.process.enums.BusinessProcessType.Reduction_FORM;

@Slf4j
@Service
public class GenericProcessOperate<T> implements IProcessOperate<T> {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ExternalClient externalClient;

    @Autowired
    private FinanceProcessRecordZJRepository financeProcessRecordZJRepository;

    /**
     * 创建流程，返回OA审批页面地址
     *
     * @param mainDataId
     * @param processType
     * @return
     */
    @Override
    public String createProcess(T mainDataId, BusinessProcessType processType) {
        String requestId = null;
        try {
            if (ObjectUtils.isEmpty(mainDataId) || ObjectUtils.isEmpty(processType)) {
                throw new OwlBizException("流程主业务id或流程类型不能为空");
            }
            log.info("开始创建OA审批流程,主id:{},流程类型:{}", mainDataId, processType.getDesc());
            //查流程记录
            FinanceProcessRecordZJ record = financeProcessRecordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                    .eq(FinanceProcessRecordZJ::getMainDataId, mainDataId)
                    .eq(FinanceProcessRecordZJ::getType, processType.getCode()));
            log.info("流程记录:{}", JSON.toJSONString(record));
            if (ObjectUtils.isNotEmpty(record) && StringUtils.isNotBlank(record.getProcessId())) {
                BusinessInfoF businessInfoF = this.buildBusinessInfoF(mainDataId);
                businessInfoF.setProcessId(record.getProcessId());
                businessInfoF.setIsJsdProcess(record.getIsJsdProcess());
                businessInfoF.setFormType(processType.getCode());
                log.info("流程更新-表单数据:{}", JSON.toJSONString(businessInfoF));
                ProcessCreateV processCreateV = externalClient.wfApproveCreate(businessInfoF);
                log.info("流程更新-返回得到的ProcessCreateV:{}", JSON.toJSONString(processCreateV));
                if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                    log.error("流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                    throw new OwlBizException("流程更新失败");
                }
                log.info("当前业务单已经存在流程数据:{}", JSON.toJSONString(record));
                //流程更新成功，审批状态设置为审批中
                record.setReviewStatus(ApproveStatusEnum.OA审批中.getCode());
                financeProcessRecordZJRepository.updateById(record);
                return validateFw(record.getProcessId());
            }
            BusinessInfoF businessInfoF = this.buildBusinessInfoF(mainDataId);
            businessInfoF.setFormType(processType.getCode());
            log.info("流程创建-表单数据:{}", JSON.toJSONString(businessInfoF));
            ProcessCreateV processCreateV = externalClient.wfApproveCreate(businessInfoF);
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
            processRecord.setMainDataId(String.valueOf(mainDataId));
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

        return validateFw(requestId);
    }

    /**
     * 创建OA审批页面地址
     *
     * @param requestId
     * @return
     */
    @Override
    public String validateFw(String requestId) {
        FwSSoBaseInfoF fwSSoBaseInfoF = new FwSSoBaseInfoF();
        UserInfoRv s = userClient.getUserInfoByUserId(ThreadLocalUtil.curIdentityInfo().getUserId());
        if (ObjectUtils.isNotEmpty(s)) {
            fwSSoBaseInfoF.setUSERCODE(externalClient.getIphoneByEmpCode(s.getMobileNum()));
            fwSSoBaseInfoF.setDEPTCODE(externalClient.getDepetByEmpCode(s.getMobileNum()));
        }
        fwSSoBaseInfoF.setRequestId(requestId);
        return externalClient.validateFw(fwSSoBaseInfoF);
    }

    /**
     * 处理审批回到的状态
     *
     * @param mainDataId
     * @param reviewStatus
     * @return
     */
    @Override
    public Boolean handleReviewStatus(T mainDataId, Integer reviewStatus) {
        switch (reviewStatus) {
            case 0:
                this.reject(mainDataId);
                break;
            case 1:
                this.approving(mainDataId);
                break;
            case 2:
                this.approved(mainDataId);
                break;
            default:
                log.error("无效审核状态:{}", reviewStatus);
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
