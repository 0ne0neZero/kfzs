package com.wishare.finance.apps.process.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.process.GenericProcessOperate;
import com.wishare.finance.apps.process.enums.BelongRegionEnum;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDxZJV;
import com.wishare.finance.apps.service.pushbill.PushBillDxZJAppService;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.FinanceProcessRecordZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeF;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeV;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对下结算单审批状态 0：默认值 1：审核中 2：已完成 3：驳回
 */
@Slf4j
@Service
public class DxJsBillProcessService extends GenericProcessOperate<Long> {

    @Value("${process.create.bizCode:9999}")
    private String bizCode;

    @Autowired
    private VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;

    @Autowired
    private VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;

    @Autowired
    private ContractClient contractClient;

    @Autowired
    private FinanceProcessRecordZJMapper financeProcessRecordZJMapper;

    @Autowired
    private ApplicationContext applicationContext;
    @Resource
    private TemporaryChargeBillRepository temporaryChargeBillRepository;
    @Resource
    @Lazy
    private PushBillDxZJAppService pushBillDxZJAppService;

    /**
     * 创建OA流程表单信息
     *
     * @param mainDataId
     * @return
     */
    @Override
    public BusinessInfoF buildBusinessInfoF(Long mainDataId) {
        VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.getById(mainDataId);
        //创建表单对象
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //基础字段
        businessInfoF.setFormDataId(String.valueOf(mainDataId));
        businessInfoF.setEditFlag(1); //todo 待前端确定传0还是1
        businessInfoF.setFormType(BusinessProcessType.DX_JS_FORM.getCode());
        businessInfoF.setFlowType(bizCode);
        businessInfoF.setContractName(voucherBillDxZJ.getReceiptRemark());

        List<VoucherPushBillDetailDxZJ> detailDxs = voucherBillDetailDxZJRepository.list(Wrappers.<VoucherPushBillDetailDxZJ>lambdaQuery()
                .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBillDxZJ.getVoucherBillNo()));
        ContractPayConcludeF contractPayConcludeF = new ContractPayConcludeF();
        contractPayConcludeF.setId(detailDxs.get(0).getContractId());
        ContractPayConcludeV contractPayConcludeV = contractClient.get(contractPayConcludeF);

        //表单字段
        businessInfoF.setSsqy(BelongRegionEnum.getCodeByName(contractPayConcludeV.getRegion()));
        businessInfoF.setSpsxsm(voucherBillDxZJ.getReceiptRemark());
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
        //将审批状态改成驳回，推送状态改成待推送
        VoucherBillDxZJ voucherBillDxZJ = new VoucherBillDxZJ();
        voucherBillDxZJ.setId(mainDataId);
        voucherBillDxZJ.setApproveState(3);
        voucherBillDxZJ.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);
    }

    /**
     * 审批通过回调
     *
     * @param mainDataId
     */
    @Override
    public void approved(Long mainDataId) {
        //从流程记录中查询流程的创建人信息
        FinanceProcessRecordZJ record = financeProcessRecordZJMapper.selectOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getMainDataId, mainDataId));
        //避免循环引用，从ioc中手动个获取bean
        PushBillZJDomainService pushBillZJDomainService = applicationContext.getBean(PushBillZJDomainService.class);
        //合同报账单单-对下结算单实签
        if(BusinessProcessType.DX_JS_FORM.getCode().equals(record.getType())){
            pushBillZJDomainService.approveAgreeOnOa(mainDataId, record.getCreator());
        }else{
            VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.getById(mainDataId);
            if(Objects.nonNull(voucherBillDxZJ)){
                //合同报账单单-收入确认单实签
                voucherPushBillDxZJRepository.update(new LambdaUpdateWrapper<VoucherBillDxZJ>()
                        .set(VoucherBillDxZJ::getGmtApprove, LocalDateTime.now())
                        .set(VoucherBillDxZJ::getApproveState, PushBillApproveStateEnum.已审核.getCode())
                        .set(VoucherBillDxZJ::getPushState, PushBillTypeEnum.已推送.getCode())
                        .eq(VoucherBillDxZJ::getId, mainDataId));
                // 账单状态修改
                List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJ.getVoucherBillNo());
                if(CollectionUtils.isNotEmpty(detailDxZJS)){
                    String communityId = detailDxZJS.get(0).getCommunityId();
                    List<Long> billIds = detailDxZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
                    //flag = 1 审批通过
                    temporaryChargeBillRepository.updateBatchStatus(billIds, communityId, voucherBillDxZJ.getBillEventType(), 1);
                }
                log.info("推送财务云前置对下结算审批通过，触发自动推送逻辑{}",mainDataId);
                //自动推送至财务云
                SyncBatchPushZJBillF pushZJBillF = new SyncBatchPushZJBillF();
                pushZJBillF.setVoucherIds(Arrays.asList(mainDataId));
                pushZJBillF.setUserId(voucherBillDxZJ.getCreator());
                pushBillDxZJAppService.syncBatchPushBillForSettlement(pushZJBillF);
            }
        }
    }

    /**
     * 审批中回调
     *
     * @param mainDataId
     */
    @Override
    public void approving(Long mainDataId) {
        //将审批状态修改为审批中
        VoucherBillDxZJ voucherBillDxZJ = new VoucherBillDxZJ();
        voucherBillDxZJ.setId(mainDataId);
        voucherBillDxZJ.setApproveState(1);
        voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);
    }

}
