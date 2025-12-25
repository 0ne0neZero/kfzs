package com.wishare.finance.apps.service.event.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimRecordApproveStateEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.BpmClient;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.enums.ApproveProcessCompleteEnum;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.model.ApproveProcessCompleteMsg;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProcessProgressV;
import com.wishare.finance.infrastructure.remote.vo.bpm.Progress;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BPM流程合同报账单收入确认
 * description 旧收入确认handler
 * @author Yuting.Wang
 * @since 2024/12/18 15:44
 */
@Component
@Slf4j
public class IncomeRecognitionHandler extends AbustractPushBillHandler{

    @Resource
    PushBillZJDomainService pushBillZJDomainService;
    @Value("${wishare.file.host:}")
    private String fileHost;
    @Autowired
    VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    @Autowired
    private  VoucherPushBillZJRepository voucherPushBillRepository;
    @Autowired
    private  ContractClient contractClient;
    @Autowired
    private  OrgClient orgClient;
    @Autowired
    private  ConfigClient configClient;
    @Autowired
    private  BpmClient bpmClient;
    @Autowired
    private  VoucherBillFileZJRepository voucherBillFileZJRepository;

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
        return OperateTypeEnum.收入确认.getDes().equals(param.getType());
    }

    @Override
    @Transactional
    public void approveAgree(Long voucherBillId, String procInstId) {
        log.info("收入确认审批通过!");

        VoucherBillZJ voucherBillZJ = pushBillZJDomainService.getById(voucherBillId);
        log.info("voucherBillZJ:"+ JSON.toJSON(voucherBillZJ));
        // 主表状态刷新
        pushBillZJDomainService.approveAgree(voucherBillId);

        // 收入确认单处理
        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBillZJ.getVoucherBillNo())
                .eq(VoucherPushBillDetailZJ::getDeleted, 0));


        // 获取影像资料
        List<ZJFileVo> zjFileVos = new ArrayList<>();
        List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(voucherBillZJ.getId());
        for (VoucherBillFileZJ voucherBillFileZJ : voucherBillFileZJS) {
            String files = voucherBillFileZJ.getFiles();
            JSONObject jsonObject = JSONObject.parseObject(files) ;
            String s = fileHost + jsonObject.getString("fileKey");
            MultipartFile multipartFile = FileUtil.getMultipartFile(s);
            ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
            zjFileVo.setName(jsonObject.getString("name"));
            zjFileVo.setFileKey(jsonObject.getString("fileKey"));
            zjFileVos.add(zjFileVo);
        }
        for (ZJFileVo zjFileVo : zjFileVos) {
            UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
            uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
            uploadLinkZJF.setName(zjFileVo.getName());
            uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
            uploadLinkZJF.setBillNo(voucherBillZJ.getVoucherBillNo());
            voucherPushBillRepository.addLinkZJ(uploadLinkZJF);
        }
        // 自动推送至财务云
        SyncBatchPushZJBillF syncBatchPushZJBillF = new SyncBatchPushZJBillF();
        syncBatchPushZJBillF.setVoucherIds(Arrays.asList(voucherBillId));
        syncBatchPushZJBillF.setVoucherSystem(2);
        // 调用成本中心 获取项目接口
        OrgFinanceCostRv orgFinanceCostById = orgClient.getOrgFinanceCostById(voucherBillZJ.getCostCenterId());
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", orgFinanceCostById.getCommunityId());
        // 调用外部数据映射接口
        // 调用根据行政组织获取核算组织接口
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                syncBatchPushZJBillF.setXZBM(cfgExternalDataV.getDataCode());
            } else if ("org".equals(cfgExternalDataV.getExternalDataType())){
                syncBatchPushZJBillF.setXZZZ(cfgExternalDataV.getDataCode());
            }
        }
        // set业务类型值
        syncBatchPushZJBillF.setYWLX(voucherBillZJ.getBusinessTypeId().trim());
        IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
        identityInfoDefault.setTenantId("13554968497211");
        identityInfoDefault.setUserId(voucherBillZJ.getCreator());
        identityInfoDefault.setUserName("BPM-内部审批");
        identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);
        ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(procInstId, "");
        log.info("调用bpm详情信息接口:" + JSON.toJSON(processFormAndInstanceProgress));

        List<Progress> progress = processFormAndInstanceProgress.getProgress();
        for (Progress progress1 : progress) {
            if ( null != progress1.getResult() && ("ROOT").equals(progress1.getNodeType().toString())){
                syncBatchPushZJBillF.setUserId(progress1.getUser().getId());
                break;
            }
        }
        syncBatchPushZJBillF.setApproveFlag(1);
        pushBillZJDomainService.syncBatchPushBill(syncBatchPushZJBillF);
    }

    @Override
    @Transactional
    public void approveRefuse(Long voucherBillId, String procInstId) {
        log.info("收入确认审批驳回!");

        VoucherBillZJ voucherBillZJ = pushBillZJDomainService.getById(voucherBillId);
        // 主表子表删除
        IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
        identityInfoDefault.setTenantId("13554968497211");
        identityInfoDefault.setUserId(voucherBillZJ.getCreator());
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
                    voucherBillZJ.setRemark(textValue);
                } else {
                    voucherBillZJ.setRemark("");
                }
                break;
            }
        }
        voucherBillZJ.setApproveState(2);
        voucherBillZJ.setPushState(5);
        pushBillZJDomainService.delete(voucherBillZJ);
    }

    @Override
    public void approveAgree(String voucherBillId, String procInstId) {

    }

    @Override
    public void approveRefuse(String voucherBillId, String procInstId) {

    }
}
