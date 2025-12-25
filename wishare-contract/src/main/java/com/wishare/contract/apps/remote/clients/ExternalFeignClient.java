package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.fo.contractset.blacklist.BlackListInfoF;
import com.wishare.contract.apps.fo.remind.PhoneParamF;
import com.wishare.contract.apps.fo.revision.income.ParamCallBackInfoF;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.fo.ContractPlanRf;
import com.wishare.contract.apps.remote.fo.InvoiceBaseInfoF;
import com.wishare.contract.apps.remote.fo.ZjOrganizationInfoListF;
import com.wishare.contract.apps.remote.fo.bpm.BpmContractExpendF;
import com.wishare.contract.apps.remote.fo.bpm.BpmContractNoExpendF;
import com.wishare.contract.apps.remote.fo.image.ImageFileDeleteF;
import com.wishare.contract.apps.remote.fo.image.ImageFileF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.fo.proquery.ProcessQueryF;
import com.wishare.contract.apps.remote.vo.ContractBasePullV;
import com.wishare.contract.apps.remote.vo.ContractPlanRv;
import com.wishare.contract.apps.remote.vo.EsbRetrunInfoV;
import com.wishare.contract.apps.remote.vo.MainDataOrganizationInfoV;
import com.wishare.contract.apps.remote.vo.blacklist.BlackUserV;
import com.wishare.contract.apps.remote.vo.bpm.BpmBillApplyV;
import com.wishare.contract.apps.remote.vo.bpm.BpmContractReturnV;
import com.wishare.contract.apps.remote.vo.imagefile.ImageFileDownV;
import com.wishare.contract.apps.remote.vo.imagefile.ImageFileV;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.*;
import com.wishare.contract.domains.vo.revision.proquery.ProcessQueryV;
import com.wishare.contract.domains.vo.revision.remind.PhoneThirdPartyIdV;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 第三方调用中心
 *
 * @author wangrui
 */
@OpenFeignClient(name = "wishare-external", serverName = "第三方调用中心", path = "/external")
public interface ExternalFeignClient {

    @PostMapping("/zjMasterData/getOrganizationInfoByTypeExt")
    List<MainDataOrganizationInfoV> getOrganizationInfoByTypeExt(@RequestBody ZjOrganizationInfoListF request);


        /**
         * 中交影像文件上传
         *
         * @param imageFileF
         */
    @PostMapping("/zj/image/upload")
    String zjUpload(@RequestBody ImageFileF imageFileF);

    @PostMapping("/zj/image/v2/upload")
    ImageFileV zjUploadV2(@RequestBody ImageFileF imageFileF);

    /**
     * 中交影像文件上传
     *
     * @param imageFileF
     */
    @PostMapping("/zj/image/down")
    ImageFileDownV downImageFile(@RequestBody ImageFileDeleteF imageFileF);


    /**
     * 中交合同数据推送
     *
     * @param contractBasePullF
     */
    @PostMapping("/zj/contract/pull")
    ContractBasePullV contractPull(@RequestBody ContractBasePullF contractBasePullF);

    /**
     * 发票验重校验
     *
     * @param invoiceBaseInfoF
     */
    @PostMapping("/zj/contract/checkInvoice")
    EsbRetrunInfoV checkInvoice(@RequestBody InvoiceBaseInfoF invoiceBaseInfoF);

    /**
     * 中交合同数据校验
     *
     * @param contractBaseInfoF
     */
    @PostMapping("/zj/contract/verify")
    ContractBasePullV contractVerify(@RequestBody ContractBasePullF contractBaseInfoF);

    /**
     * 中交合同数据停用
     *
     * @param contractBaseInfoF
     */
    @PostMapping("/zj/contract/noused")
    ContractBasePullV noused(@RequestBody ContractBasePullF contractBaseInfoF);

    /**
     * 通过手机号查员工编码
     *
     * @param iphone
     */
    @GetMapping("/zjMasterData/getIphoneByEmpCode")
    String getIphoneByEmpCode(@RequestParam(value = "iphone") String iphone) ;

    /**
     * 根据手机号批量获取员工三方id
     *
     * @param phoneParamF
     * @return
     */
    @PostMapping("/zjMasterData/getUserThirdPartyIdByPhone")
    List<PhoneThirdPartyIdV> getUserThirdPartyIdByPhone(@RequestBody PhoneParamF phoneParamF);

    /**
     * 通过款项code查员工名称
     *
     * @param code
     */
    @GetMapping("/zjDictData/mdm17query")
    String mdm17query(@RequestParam(value = "code") String code) ;

    /**
     * 通过款项code查员工名称
     *
     * @param code
     */
    @GetMapping("/zjDictData/mdm16query")
    String mdm16query(@RequestParam(value = "code") String code) ;

    /**
     * 通过手机号查员工部门
     *
     * @param iphone
     */
    @GetMapping("/zjMasterData/getDepetByEmpCode")
    String getDepetByEmpCode(@RequestParam(value = "iphone") String iphone) ;

    /**
     * 合同收款和保证金计划信息推送
     *
     * @param contractPlanRf
     * @return ContractPlanRv
     */
    @PostMapping("/space/addAgreement")
    ContractPlanRv contractPlan(@RequestBody List<ContractPlanRf> contractPlanRf);


    @PostMapping(value = "/bpm/contract/expend")
    @ApiOperation(value = "bpm合同订立支出类发起申请", notes = "bpm合同订立支出类发起申请")
    BpmContractReturnV expendApply(@RequestBody @Validated BpmContractExpendF f);

    @PostMapping(value = "/bpm/contract/noExpend")
    @ApiOperation(value = "bpm合同订立非支出类发起申请", notes = "bpm合同订立非支出类发起申请")
    BpmContractReturnV noExpendApply(@RequestBody @Validated BpmContractNoExpendF f);

    @PostMapping(value = "/bpm/contract/expendPay")
    @ApiOperation(value = "bpm合同订立支出类付款发起申请", notes = "bpm合同订立支出类付款发起申请")
    BpmContractReturnV expendPayApply(@RequestBody @Validated BpmContractExpendF f);

    // type 1：减免调整  2：退款
    @PostMapping(value = "/bpm/attach/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "bpm上传附件", notes = "bpm上传附件", response = BpmBillApplyV.class)
    Boolean upload(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                   @RequestPart(value = "callFiles", required = false) List<MultipartFile> callFiles,
                   @RequestPart(value = "mitigateFiles", required = false) List<MultipartFile> mitigateFiles,
                   @RequestParam("uuid") String uuid,
                   @RequestParam("bpmProcessInstResponseId") String bpmProcessInstResponseId,
                   @RequestParam("type") Integer type,
                   @RequestParam("userId") String userId);

    /**
     * 合同收款和保证金计划信息推送
     *
     * @param paramCallBackInfoF
     * @return ContractPlanRv
     */
    @PostMapping("/zjParam/ceCallBack")
    boolean ceCallBack(@RequestBody ParamCallBackInfoF paramCallBackInfoF);

    /**
     * 中交合同审批状态回调
     *
     * @param paramCallBackInfoF
     * @return ContractPlanRv
     */
    @PostMapping("/zjParam/approveContactCallback")
    boolean approveContactCallback(@RequestBody ParamCallBackInfoF paramCallBackInfoF);

    /**
     * 中交市拓登录
     */
    @GetMapping("/zjParam/ceLogin")
    String ceLogin();

//    @PostMapping("/zj/wfPayCreate")
//    @ApiOperation(value = "支付合同审批流程创建接口", notes = "支付合同审批流程创建接口")
//    ProcessCreateV wfPayCreate(@RequestBody ContractPayConcludeV processCreateF);
//
//    @PostMapping("/zj/wfIncomeCreate")
//    @ApiOperation(value = "收入合同审批流程创建接口", notes = "收入合同审批流程创建接口")
//    ProcessCreateV wfIncomeCreate(@RequestBody ContractIncomeConcludeV contractIncomeConcludeV);

    @PostMapping("/zj-apprpval/wfProcessCreate")
    @ApiOperation(value = "审批流程创建接口", notes = "审批流程创建接口")
    ProcessCreateV wfApproveCreate(@RequestBody BusinessInfoF businessInfoF);

    @PostMapping("/zj-apprpval/queryStatus")
    @ApiOperation(value = "流程状态查询接口", notes = "流程状态查询接口")
    ProcessQueryV queryStatus(@RequestBody ProcessQueryF processQueryF);

    @PostMapping("/zj-apprpval/opinionApproval")
    @ApiOperation(value = "审批意见获取接口", notes = "审批意见获取接口")
    OpinionApprovalV opinionApproval(@RequestBody OpinionApprovalF opinionApprovalF);

    /**
     * 黑名单信息获取服务
     *
     * @param blackListF 需要查询是否是黑名单的用户数据
     * @return 调用响应，含是否是黑名单数据
     */
    @ApiOperation(value = "中交获取黑名单数据接口", notes = "中交获取黑名单数据接口")
    @GetMapping("/zj/blackList/get")
    BlackUserV get(BlackListInfoF blackListF);

//    /**
//     * 用户在业务系统PC上传附件时，业务系统将附件通过该接口推送到影像系统进行保存，
//     * 进行发票OCR识别、核验，并将附件ID、发票信息回传给业务系统。
//     *
//     * @param imageFileF 附件信息
//     * @return String 文件唯一ID fileuuid
//     */
//    @ApiOperation(value = "接收附件接口", notes = "接收附件接口")
//    @PostMapping("/zj/image/upload")
//    String uploadImageFile(ImageFileF imageFileF);

    @ApiOperation("在线编辑")
    @PostMapping("/zjWps/online/edit")
    public String tempUpload(@RequestBody ContractRecordInfoV contractRecordInfoV);


    @ApiOperation(value = "泛微单点登录", notes = "泛微单点登录")
    @PostMapping("/zjSso/validateFw")
        public String validateFw(@RequestBody FwSSoBaseInfoF fwSSoInfoV) ;

    @ApiOperation(value = "枫行梦新建合同同步到枫行梦", notes = "枫行梦新建合同同步到枫行梦")
    @PostMapping("/zj/contractInfo/push")
    Boolean contractInfoPull(ContractBasePullF contractBasePullF);

    @ApiOperation(value = "合同状态同步枫行梦", notes = "合同状态同步枫行梦")
    @PostMapping("/zj/contractInfo/status")
    Boolean contractStatus(ContractBasePullF contractBasePullF);

    @ApiOperation(value = "枫行梦应收账单接口", notes = "枫行梦应收账单接口")
    @PostMapping("/zj/contractInfo/receivableBill")
    Boolean contractReceivableBill(ContractBasePullF contractBasePullF);

    @ApiOperation(value = "枫行梦财务账单的接口", notes = "枫行梦财务账单的接口")
    @PostMapping("/zj/contractInfo/financeBill")
    Boolean contractFinanceBill(ContractBasePullF contractBasePullF);

    @ApiOperation(value = "枫行梦收款接口", notes = "枫行梦收款接口")
    @PostMapping("/zj/contractInfo/receiptRecord")
    Boolean contractReceiptRecord(ContractBasePullF contractBasePullF);

    @GetMapping("/cost/getCodeMappingTree")
    @ApiOperation(value = "获取费项映射关系树", notes = "获取费项映射关系树", response = CostItemNode.class)
    CostBaseResponse<List<CostItemNode>> getCodeMappingTree(@RequestParam String zjCode);

    @PostMapping("/cost/getDynamicCostSurplusProp")
    @ApiOperation(value = "查询项目下费项可用金额明细", notes = "查询项目下费项可用金额明细", response = DynamicCostSurplusInfo.class)
    CostBaseResponse<DynamicCostSurplusInfo> getDynamicCostSurplusProp(@RequestBody DynamicCostSurplusPropReqF dynamicCostSurplusPropReqF);

    @PostMapping("/cost/getDynamicCostIncurred")
    @ApiOperation(value = "更新合约规划占用", notes = "更新合约规划占用", response = DynamicCostIncurredRespF.class)
    CostBaseResponse<List<DynamicCostIncurredRespF>> getDynamicCostIncurred(@RequestBody DynamicCostIncurredReqF dynamicCostIncurredReqF);

    @PostMapping("/jdHuiCai/trustLogin")
    @ApiOperation(value = "京东慧采信任登录", notes = "京东慧采信任登录")
    String trustLogin(@RequestBody StartOrderForJDReqF startOrderForJDReqF);

    @PostMapping("/jdHuiCai/confirmOrder")
    @ApiOperation(value = "订单信息确认", notes = "订单信息确认")
    Boolean confirmOrder(@RequestBody StartOrderForJDReqF startOrderForJDReqF);

    @PostMapping("/jdHuiCai/cancelOrder")
    @ApiOperation(value = "订单信息取消", notes = "订单信息取消")
    Boolean cancelOrder(@RequestBody StartOrderForJDReqF startOrderForJDReqF);
}
