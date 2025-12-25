package com.wishare.finance.infrastructure.remote.clients.base;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.wishare.finance.apps.model.configure.accountbook.fo.ExternalAccountingbookF;
import com.wishare.finance.apps.model.reconciliation.fo.PhoneParamF;
import com.wishare.finance.apps.model.reconciliation.fo.external.OpinionApprovalF;
import com.wishare.finance.apps.model.reconciliation.vo.PhoneThirdPartyIdV;
import com.wishare.finance.apps.model.signature.ESignF;
import com.wishare.finance.apps.model.signature.ESignResultYyV;
import com.wishare.finance.apps.model.signature.ESignResultZjV;
import com.wishare.finance.apps.model.signature.ElectronStampYyF;
import com.wishare.finance.apps.model.signature.ElectronStampZjF;
import com.wishare.finance.apps.model.signature.EsignResultV;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.finance.apps.process.fo.FwSSoBaseInfoF;
import com.wishare.finance.apps.process.vo.OpinionApprovalV;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import com.wishare.finance.infrastructure.remote.fo.external.kingdee.AddKingDeeRecBillF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongContentRF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongLoginRf;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbProjectRF;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbQueryRF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.BillingNewF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.QueryInvoiceResultF;
import com.wishare.finance.infrastructure.remote.fo.external.nuonuo.ReInvoiceF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankAccF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankDocF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankTypeF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddCustomerF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddPersonF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddProjectF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddSupplierF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.ListAccount;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractBasePullF;
import com.wishare.finance.infrastructure.remote.vo.external.kingdee.KingDeeResultRV;
import com.wishare.finance.infrastructure.remote.vo.external.mdmmb.MdmMbCommunityRV;
import com.wishare.finance.infrastructure.remote.vo.external.mdmmb.MdmMbProjectRV;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.BillingNewRv;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.QueryInvoiceResultRV;
import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalV2;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.AccessItemRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.AccountRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.BusinessTypeRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.CashFlowRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.RateRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.Result;
import com.wishare.finance.infrastructure.remote.vo.external.zhongjiao.ImageFileDeleteF;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import com.wishare.finance.infrastructure.remote.vo.zj.MDM16V;
import com.wishare.finance.infrastructure.remote.vo.zj.MDM17V;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xujian
 * @date 2022/9/18
 * @Description:
 */
@OpenFeignClient(name = "${wishare.feignClients.external.name:wishare-external}", serverName = "第三方external服务", path = "${wishare.feignClients.external.context-path:/external}")
public interface ExternalClient {

    @PostMapping(value = "/nuonuo/reInvoice", name = "开票重试接口")
    String reInvoice(@RequestBody @Validated ReInvoiceF form);

    @PostMapping(value = "/yonyounc/vouchers/add", name = "用友nc推凭")
    SendresultV addVouchers(@RequestBody @Validated UfinterfaceF form);


    @ApiOperation(value = "枫行梦应收账单的接口", notes = "枫行梦应收账单的接口")
    @PostMapping("/zj/contractInfo/receivableBill")
    Boolean contractReceivableBill(ContractBasePullF contractBaseInfoF);

    /*灵税通开放平台对接*/
    @PostMapping(value = "/lingshuitong/receivable/login",name = "灵税通单点集成页面接口")
    String receivableLogin(@RequestBody @Validated LingshuitongLoginRf form) ;

    @PostMapping(value = "/lingshuitong/invoice/voucherInvoice",name = "灵税通入账凭证接口 3.1.4")
    String invoiceVoucherInvoice(@RequestBody @Validated LingshuitongContentRF form) ;

    @GetMapping(value = "/cbs/flow/history", name = "获取历史流水")
    JSONObject getFlowDatas(@RequestParam("beginDate") String beginDate, @RequestParam("endDate") String endDate);

    @GetMapping(value = "/cbs/receipt/down", name = "下载回单")
    JSONObject downReceipt(@RequestParam("accnbr") String accnbr, @RequestParam("istnbr") String istnbr, @RequestParam("sqrnb1") String sqrnb1);

    @PostMapping(value = "/mdm/query",name = "映射关系查询接口")
    List<MdmMbProjectRV> mdmQuery(@Validated @RequestBody MdmMbQueryRF mdmMbProjectF);

    @PostMapping(value = "/mdm", name = "保存映射关系")
    Integer mdmSave(@RequestBody MdmMbProjectRF mdmMbProjectF);

    @PostMapping(value = "/yonyounc/list/businesstype", name = "获取业务类型视图")
    List<BusinessTypeRv> listBusinessType();

    @PostMapping(value = "/yonyounc/list/account", name = "获取辅助核算列表视图")
    List<AccountRv> listAccount();

    @PostMapping(value = "/yonyounc/list/account/codes", name = "获取辅助核算列表视图")
    List<AccountRv> listAccountByCodes(@RequestBody ListAccount listAccount);

    @PostMapping(value = "/yonyounc/list/cashflow", name = "获取辅助核算列表视图")
    List<CashFlowRv> listCashFlow();

    @PostMapping(value = "/yonyounc/list/accessitem", name = "获取辅助核算列表视图")
    List<AccessItemRv> listAccassitem();

    @PostMapping(value = "/yonyounc/banktype/add", name = "新增银行类别")
    Result addBankType(@RequestBody AddBankTypeF bankType);

    @PostMapping(value = "/yonyounc/bankdoc/add", name = "新增银行档案")
    Result addBankDoc(@RequestBody AddBankDocF bankDoc);

    @PostMapping(value = "/yonyounc/bankacc/add", name = "新增银行账户")
    Result addBankAcc(@RequestBody AddBankAccF bankAcc);

    @PostMapping(value = "/yonyounc/supplier/add", name = "新增供应商")
    Result addSupplier(@RequestBody AddSupplierF supplier);

    @PostMapping(value = "/yonyounc/customer/add", name = "新增客户")
    Result addCustomer(@RequestBody AddCustomerF customer);

    @PostMapping(value = "/yonyounc/person/add", name = "新增人员档案")
    Result addPerson(@RequestBody AddPersonF person);

    @PostMapping(value = "/yonyounc/project/add", name = "新增项目（成本中心）")
    Result addProject(@RequestBody AddProjectF project);

    @PostMapping(value = "/yonyounc/list/rate", name = "获取辅助核算税率列表视图")
    List<RateRv> listRate();

    @PostMapping(value = "/yonyounc/accountingbook/page", name = "分页获取账簿（账套）信息视图")
    PageV<ExternalAccountingbookF> getAccountingbookPage(@RequestBody PageF<?> pageF) ;
    @GetMapping(value = "/fy/fy/getAccessToken",name = "获取方圆token")
    String getAccessToken() ;
    @PostMapping(value = "/mdm/code/query",name = "映射关系查询接口")
    List<MdmMbCommunityRV> queryCodeInfo(@Validated @RequestBody MdmMbQueryRF mdmMbProjectF);

    @GetMapping(value = "/mdm/code/queryByBusinessId",name = "根据mdmId查询businessId")
    String queryByBusinessId(@RequestParam("mdmId") @NotBlank(message = "mdmId不能为空") String mdmId) ;

    @GetMapping(value = "/zjMasterData/getIphoneByEmpCode",name = "根据iphone查询EmpCode")
    String getIphoneByEmpCode(@RequestParam("iphone") @NotBlank(message = "iphone不能为空") String iphone) ;

    @GetMapping("/zjMasterData/getDepetByEmpCode")
    String getDepetByEmpCode(@RequestParam(value = "iphone") String iphone);

    @PostMapping("/zjSso/validateFw")
    String validateFw(@RequestBody FwSSoBaseInfoF fwSSoInfoV);

    @PostMapping("/zj-apprpval/wfProcessCreate")
    ProcessCreateV wfApproveCreate(@RequestBody BusinessInfoF businessInfoF);

    @PostMapping("/zj-apprpval/opinionApproval")
    OpinionApprovalV opinionApproval(@RequestBody OpinionApprovalF opinionApprovalF);

    @PostMapping(value = "/eSign/stamp", name = "e签宝电子签章")
    String stamp(ESignF signF);

    @GetMapping(value = "/eSign/queryEsignResult",name = "(e签宝)查询签署结果[入参e签宝返回当次id]")
    EsignResultV queryEsignResult(@RequestParam("signFlowId") @NotBlank(message = "signFlowId不能为空") String signFlowId) ;

    @PostMapping(value = "/zj/image/delete", name = "删除附件接口")
    boolean deleteImageFile(@RequestBody ImageFileDeleteF imageFileF);

    /**
     * 中交专用 签署作废
     * @param electronStampF
     * @return
     */
    @PostMapping(value = "/jz-eSign/stamp", name = "中交e签宝电子签章")
    String stampZj(ElectronStampZjF electronStampF);

    /**
     * 中交专用获取结果
     * @param signFlowId
     * @return
     */
    @GetMapping(value = "/jz-eSign/queryEsignResult",name = "(e签宝)查询签署结果[入参e签宝返回当次id]")
    ESignResultZjV queryEsignZjResult(@RequestParam("signFlowId") @NotBlank(message = "signFlowId不能为空") String signFlowId) ;

    /**
     * 远洋专用 签署作废
     * @param electronStampF
     * @return
     */
    @PostMapping(value = "/yy-eSign/stamp", name = "远洋e签宝电子签章")
    String stampYy(ElectronStampYyF electronStampF);

    /**
     * 远洋专用获取结果
     * @param signFlowId
     * @return
     */
    @GetMapping(value = "/yy-eSign/queryEsignResult",name = "(e签宝)查询签署结果[入参e签宝返回当次id]")
    ESignResultYyV queryEsignYyResult(@RequestParam("signFlowId") @NotBlank(message = "signFlowId不能为空") String signFlowId) ;

    @PostMapping(value = "/externalMaindataCalmapping/list", name = "下拉列表，默认数量20")
    ExternalMaindataCalmappingListV list(@Validated @RequestBody ExternalMaindataCalmappingListF externalMaindataCalmappingListF);

    @GetMapping(value = "/zjDictData/mdm16/queryByCode", name ="通过code查询MDM16信息")
    MDM16V queryByCode(@RequestParam("code") String code);


    @GetMapping(value = "/zjDictData/mdm17/queryByCode" , name = "通过code查询MDM17信息")
    MDM17V queryByCodeForMDM17(@RequestParam("code") String code);

    @PostMapping(value = "/jinDie/addReceivableBill", name = "新增应收单")
    KingDeeResultRV addKingDeeRecBill(@RequestBody AddKingDeeRecBillF addKingDeeRecBillF);

    @PostMapping(value = "/jinDie/addReceiptBill", name = "新增收款单")
    KingDeeResultRV addKingDeeGatherBill();

    /**
     * 根据手机号批量获取员工三方id
     *
     * @param phoneParamF
     * @return
     */
    @PostMapping("/zjMasterData/getUserThirdPartyIdByPhone")
    List<PhoneThirdPartyIdV> getUserThirdPartyIdByPhone(@RequestBody PhoneParamF phoneParamF);

    /**
     * 获取OA审批信息
     **/
    @PostMapping("/zj-apprpval/opinionApproval/v2")
    OpinionApprovalV2 opinionApprovalV2(@RequestBody OpinionApprovalF opinionApprovalF);
}
