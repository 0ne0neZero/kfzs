package com.wishare.finance.apps.service.event.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDxZJV;
import com.wishare.finance.apps.service.pushbill.PushBillDxZJAppService;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.BpmClient;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.enums.ApproveProcessCompleteEnum;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProcessProgressV;
import com.wishare.finance.infrastructure.remote.vo.bpm.Progress;
import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum.APPROVEING_DX_BPM_DES;

/**
 * BPM流程合同报账单对下结算单
 * description 对下结算【计提、实签】handler
 * @author Yuting.Wang
 * @since 2024/12/18 15:44
 */
@Component
@Slf4j
public class DownSettlementHandler extends AbustractPushBillHandler{

    @Resource
    private PushBillDxZJAppService pushBillDxZJAppService;

    @Resource
    private VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;

    @Resource
    private TemporaryChargeBillRepository temporaryChargeBillRepository;

    @Resource
    private VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    @Resource
    private  BpmClient bpmClient;

    @Autowired
    private VoucherBillFileZJRepository voucherBillFileZJRepository;

    @Autowired
    private ContractClient contractClient;

    @Value("${wishare.file.host:}")
    private String fileHost;
    @Override
    public void handle(ApproveProcessCompleteMsg param) {
        boolean pass = ApproveProcessCompleteEnum.通过.getValue()
                .equals(param.getBusinessStatus());
        if (pass) {
            approveAgree(Long.valueOf(param.getBusinessKey()), param.getProcInstId());
        } else {
            approveRefuse(Long.valueOf(param.getBusinessKey()), param.getProcInstId());
        }
    }

    @Override
    public boolean support(ApproveProcessCompleteMsg param) {
        return APPROVEING_DX_BPM_DES.contains(param.getType());
    }

    //计提单审批通过
    @Override
    @Transactional
    public void approveAgree(Long voucherBillId, String procInstId) {
        log.info("推送财务云前置dx类报账单审批通过!");

        VoucherBillDxZJV voucherBillDxZJV = pushBillDxZJAppService.getById(voucherBillId, true);
        if(Objects.isNull(voucherBillDxZJV)){
            log.error("推送财务云前置dx类报账单审批通过-报账单不存在");
            return;
        }
        log.info("推送财务云前置voucherBillDxZJV:"+ JSON.toJSON(voucherBillDxZJV));
        // 主表状态刷新
        pushBillDxZJAppService.approveAgree(voucherBillId);
        // 账单状态修改
        List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJV.getVoucherBillNo());
        if(CollectionUtils.isNotEmpty(detailDxZJS)){
            String communityId = detailDxZJS.get(0).getCommunityId();
            List<Long> billIds = detailDxZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
            //flag = 1 审批通过
            temporaryChargeBillRepository.updateBatchStatus(billIds, communityId, voucherBillDxZJV.getBillEventType(), 1);
        }
        log.info("推送财务云前置对下结算审批通过，触发自动推送逻辑{}",voucherBillId);
        //自动推送至财务云
        SyncBatchPushZJBillF pushZJBillF = new SyncBatchPushZJBillF();
        pushZJBillF.setVoucherIds(Arrays.asList(voucherBillId));
        pushZJBillF.setUserId(voucherBillDxZJV.getCreator());
        pushBillDxZJAppService.syncBatchPushBillForSettlement(pushZJBillF);
    }

    @Override
    @Transactional
    public void approveRefuse(Long voucherBillId, String procInstId) {
        log.info("对下结算审批驳回!");

        VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.getById(voucherBillId);
        // 主表子表删除
        IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
        identityInfoDefault.setTenantId("13554968497211");
        identityInfoDefault.setUserId(voucherBillDxZJ.getCreator());
        identityInfoDefault.setUserName("BPM-内部审批");
        identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);

        ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(procInstId, "");
        log.info("调用bpm详情信息接口:" + JSON.toJSON(processFormAndInstanceProgress));

        List<Progress> progress = processFormAndInstanceProgress.getProgress();
        for (Progress progress1 : progress) {
            // 遍历到拒绝节点
            if ( null != progress1.getResult() && ("refuse").equals(progress1.getResult().toString())){
                String string = progress1.getComment().toString();
                log.info("progress1.getComment().toString():" + string);
                Pattern pattern = Pattern.compile("text=([^,}]*)");
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    String textValue = matcher.group(1);
                    // 移除前导空格
                    textValue = textValue.trim();
                    voucherBillDxZJ.setRemark(textValue);
                } else {
                    voucherBillDxZJ.setRemark("");
                }
                break;
            }
        }
        voucherBillDxZJ.setApproveState(2);
        voucherBillDxZJ.setPushState(5);
        voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);

        List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJ.getVoucherBillNo());
        if(CollectionUtils.isNotEmpty(detailDxZJS)){
            String communityId = detailDxZJS.get(0).getCommunityId();
            List<Long> billIds = detailDxZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
            //flag = 2 审批驳回
            temporaryChargeBillRepository.updateBatchStatus(billIds, communityId, voucherBillDxZJ.getBillEventType(), 2);
        }
    }

    @Override
    public void approveAgree(String voucherBillId, String procInstId) {

    }

    @Override
    public void approveRefuse(String voucherBillId, String procInstId) {

    }
}
