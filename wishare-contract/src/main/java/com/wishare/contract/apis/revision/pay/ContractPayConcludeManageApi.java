package com.wishare.contract.apis.revision.pay;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.component.imports.ImportStandardV;
import com.wishare.contract.apps.fo.revision.ContractRevF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeFjxxF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludeF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayEditF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.fo.revision.remote.BondRelationF;
import com.wishare.contract.apps.remote.vo.ContractBasePullV;
import com.wishare.contract.apps.service.revision.export.PayExportService;
import com.wishare.contract.apps.service.revision.pay.ContractPayBusinessService;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.vo.contractset.ContractDetailV;
import com.wishare.contract.domains.vo.contractset.ContractIdV;
import com.wishare.contract.domains.vo.contractset.ContractPayCostPlanReqV;
import com.wishare.contract.domains.vo.revision.ContractInfoV;
import com.wishare.contract.domains.vo.revision.ContractNumShow;
import com.wishare.contract.domains.vo.revision.MockJson;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeDetailFjxxV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.proquery.ProcessQueryV;
import com.wishare.contract.domains.vo.revision.relation.RelationRecordDetailV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0.0
 * @Description： 支出合同管理表
 * @Author： chenglong
 * @since： 2023/6/25  13:55
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同订立信息管理表"})
@RequestMapping("/manage/contractPayConclude")
public class ContractPayConcludeManageApi {

    private final ContractPayBusinessService contractPayBusinessService;

    private final PayExportService payExportService;

    @ApiOperation(value = "根据合同id批量获取合同信息", notes = "根据合同id批量获取合同信息")
    @PostMapping("/getInfoListByIds")
    public List<ContractPayConcludeDetailV> getInfoListByIds(@RequestBody List<String> ids){
        return contractPayBusinessService.getInfoListByIds(ids);
    }


    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public ContractInfoV add(@Validated @RequestBody ContractPayAddF addF){
        return contractPayBusinessService.addPayContract(addF);
    }


    @ApiOperation(value = "编辑", notes = "编辑")
    @PostMapping("/edit")
    public ContractInfoV edit(@Validated @RequestBody ContractPayEditF editF) {
        return contractPayBusinessService.editPayContract(editF);
    }

    @ApiOperation(value = "修正", notes = "修正")
    @PostMapping("/correctionPay")
    public ContractInfoV correctionPay(@Validated @RequestBody ContractPayEditF editF) {
        return contractPayBusinessService.correctionPay(editF);
    }

    @ApiOperation(value = "修改签约日期", notes = "修改签约日期")
    @PostMapping("/modifySignDate")
    public Boolean modifySignDate(@Validated @RequestBody ContractPayEditF editF) {
        return contractPayBusinessService.modifySignDate(editF);
    }

    @ApiOperation(value = "修改", notes = "修改")
    @PostMapping("/modify")
    public ContractInfoV modify(@Validated @RequestBody ContractPayAddF editF) {
        return contractPayBusinessService.modifyPayContract(editF);
    }

    /**
     * 根据合同ID获取合同详情
     * @param id 合同 ID
     * @return ContractPayConcludeDetailV
     */
    @ApiOperation(value = "根据合同ID获取合同详情", notes = "根据合同ID获取合同详情")
    @GetMapping("/detail")
    public ContractPayConcludeDetailV getDetailV(@Valid ContractDetailV param) {
        return contractPayBusinessService.detail(param);
    }

    /**
     * 根据合同ID获取合同附件列表详情
     * @return ContractIncomeConcludeDetailV
     */
    @ApiOperation(value = "根据合同ID获取合同详情", notes = "根据合同ID获取合同详情")
    @PostMapping("/fjxx")
    public ContractIncomeConcludeDetailFjxxV getFjxxDetailV(@RequestBody PageF<SearchF<ContractIncomeFjxxF>> contractIncomeFjxxF) {
        return contractPayBusinessService.getFjxxDetailV(contractIncomeFjxxF);
    }

    @ApiOperation(value = "提交合同", notes = "提交合同")
    @PostMapping("/post")
    public String post(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayBusinessService.postContract(id).trim();
    }

    @ApiOperation(value = "下拉选择主合同", notes = "下拉选择主合同")
    @PostMapping("/getMainContractPay")
    public PageV<ContractPayConcludeV> getMainContractPay(@RequestBody PageF<SearchF<ContractPayConcludeE>> request) {
        return contractPayBusinessService.getMainContractPay(request);
    }

    @ApiOperation(value = "终止合同", notes = "终止合同")
    @PostMapping("/endContract")
    public Boolean endContract(@RequestBody ContractPayConcludeF form) {
        contractPayBusinessService.endContract(form);
        return true;
    }

    @ApiOperation(value = "支出合同Excel导入", notes = "支出合同Excel导入")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public ImportStandardV customerImport(@RequestParam(value = "file") MultipartFile file) {
        return payExportService.customerImport(file);
    }

    @ApiOperation(value = "支出合同导入模板文件下载链接", notes = "支出合同导入模板文件下载链接")
    @GetMapping("/download")
    public FileVo download() {
        return payExportService.download();
    }

    @ApiOperation(value = "新增收取保证金根据供应商ID获取合同数据", notes = "新增收取保证金根据供应商ID获取合同数据")
    @PostMapping("/getContractForBond")
    public List<ContractPayConcludeV> getContractForBond(@Validated @RequestBody BondRelationF form) {
        return contractPayBusinessService.getContractForBond(form);
    }

    @ApiOperation(value = "获取变更记录", notes = "获取变更记录")
    @GetMapping("/changeRecord")
    public RelationRecordDetailV changeRecord(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayBusinessService.changeRecord(id);
    }

    @ApiOperation(value = "反审", notes = "反审")
    @GetMapping("/backReview")
    public Boolean backReview(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayBusinessService.backReview(id);
    }

    @ApiOperation(value = "获取合同页面金额数量展示数据", response = ContractNumShow.class)
    @PostMapping("/getPageShowNum")
    public ContractNumShow getPageShowNum(@RequestBody PageF<SearchF<ContractPayConcludeQuery>> request) {
        return contractPayBusinessService.getPageShowNum(request);
    }

    @ApiOperation(value = "选择合同（已筛选状态）", response = ContractPayConcludeV.class)
    @PostMapping("/pageForSelect")
    public PageV<ContractPayConcludeV> pageForSelect(@RequestBody PageF<ContractRevF> request) {
        return contractPayBusinessService.pageForSelect(request);
    }

    @ApiOperation(value = "根据id获取推送中交报文信息", notes = "根据id获取推送中交报文信息")
    @GetMapping("/getZjContractPullBody")
    public String getZjContractPullBody(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        return contractPayBusinessService.getZjContractPullBody(id);
    }

    @ApiOperation(value = "根据id推送中交信息接口", notes = "根据id推送中交信息接口")
    @GetMapping("/getZjContractPullInfo")
    public String getZjContractPullInfo(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        return contractPayBusinessService.pullContract(id);
    }

    @ApiOperation(value = "根据id推送中交信息接口", notes = "根据id推送中交信息接口")
    @GetMapping("/getZjVerifyContractInfo")
    public ContractBasePullV getZjVerifyContractInfo(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        return contractPayBusinessService.verifyContract(id);
    }

    @ApiOperation(value = "泛微流程状态查询接口", response = ProcessQueryV.class)
    @PostMapping("/queryStatus")
    public ProcessQueryV queryStatus(@RequestParam("id") String id) {
        return contractPayBusinessService.queryStatus(id);
    }

    @ApiOperation(value = "泛微审批意见获取接口", response = OpinionApprovalV.class)
    @PostMapping("/opinionApproval")
    public OpinionApprovalV opinionApproval(@RequestParam("id") String id) {
        return contractPayBusinessService.opinionApproval(id);
    }

    @ApiOperation(value = "成本合同台账Mock数据", response = ContractNumShow.class)
    @GetMapping("/mockJson")
    public JSONObject getUserMessage(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam (name = "pageSize", defaultValue = "10") Integer pageSize,
                                     @RequestParam (name="printAll", required = false) String printall){
        IPage<MockJson> all = contractPayBusinessService.mockJson(pageNo, pageSize);
        JSONObject object = new JSONObject();
        object.put("data" , all.getRecords());
        object.put("total", all.getTotal());
        object.put ("count", all.getSize());
        return object;
    }

    /*@ApiOperation(value = "根据合同ID发起NK", notes = "根据合同ID发起NK")
    @GetMapping("/startNK")
    public void startNK(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        contractPayBusinessService.startNK(id);
    }
*/
    @ApiOperation(value = "根据合同ID获取结算单列表", notes = "根据合同ID获取结算单列表")
    @GetMapping("/getPayNkSettlementList")
    public List<PayNkSettlementV> getPayNkSettlementList(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ) {
        return contractPayBusinessService.getPayNkSettlementList(id);
    }

    @ApiOperation(value = "开始NK", notes = "开始NK")
    @PostMapping("/startNK")
    public void startNK(@Validated @RequestBody ContractIdV vo){
        contractPayBusinessService.startNK(vo);
    }
    @ApiOperation(value = "结束NK", notes = "开始NK")
    @GetMapping("/endNK")
    public void endNK(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        contractPayBusinessService.endNK(id);
    }

    @ApiOperation(value = "HY合同列表清单", notes = "HY合同列表清单")
    @GetMapping("/getHyContractList")
    public List<HyContractListV> getHyContractList() {
        return contractPayBusinessService.getHyContractList();
    }

    @ApiOperation(value = "根据费项ID获取合约规划信息")
    @PostMapping("/getCostPlanByChargeItemId")
    public ContractPayCostPlanV getCostPlanByChargeItemId(@Validated @RequestBody ContractPayCostPlanReqV reqVo) {

        return contractPayBusinessService.getCostPlanByChargeItemId(reqVo);
    }

    @ApiOperation(value = "根据条件获取成本分摊明细")
    @PostMapping("/getCostApportionDetail")
    public ContractPayCostApportionV getCostApportionDetail(@Validated @RequestBody ContractPayCostPlanReqV reqVo) {
        ContractPayCostApportionV result = contractPayBusinessService.getCostApportionDetail(reqVo);
        if(Objects.isNull(result)){
            result = new ContractPayCostApportionV();
        }
        return result;
    }

    @ApiOperation(value = "结算单根据条件查询成本费项分摊明细")
    @PostMapping("/getSettlementCostPlan")
    public ContractPayCostApportionV getSettlementCostPlan(@Validated @RequestBody ContractPayCostPlanReqV reqVo) {
        ContractPayCostApportionV result = contractPayBusinessService.getSettlementCostPlan(reqVo);
        if(Objects.isNull(result)){
            result = new ContractPayCostApportionV();
        }
        return result;
    }

}
