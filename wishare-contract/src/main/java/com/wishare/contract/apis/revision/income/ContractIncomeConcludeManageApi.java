package com.wishare.contract.apis.revision.income;


import com.wishare.component.imports.ImportStandardV;
import com.wishare.contract.apps.fo.revision.ContractNoGenerateF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeAddF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludePageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeEditF;
import com.wishare.contract.apps.fo.revision.ContractRevF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.fo.revision.remote.BondRelationF;
import com.wishare.contract.apps.remote.vo.ContractBasePullV;
import com.wishare.contract.apps.remote.vo.SpaceCommunityRv;
import com.wishare.contract.apps.service.revision.export.IncomeExportService;
import com.wishare.contract.apps.service.revision.income.ContractIncomeBusinessService;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.vo.contractset.ContractDetailV;
import com.wishare.contract.domains.vo.revision.ContractInfoV;
import com.wishare.contract.domains.vo.revision.ContractNoInfoV;
import com.wishare.contract.domains.vo.revision.ContractNumShow;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeDetailFjxxV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeDetailV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.income.IncomeCorrectionHistoryV;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
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
import org.apache.commons.lang.time.DateUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  10:52
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同订立信息管理表"})
@RequestMapping("/manage/contractIncome")
public class ContractIncomeConcludeManageApi {

    private final ContractIncomeBusinessService contractIncomeBusinessService;

    private final IncomeExportService incomeExportService;

    @ApiOperation(value = "根据合同id批量获取合同信息", notes = "根据合同id批量获取合同信息")
    @PostMapping("/getInfoListByIds")
    public List<ContractIncomeConcludeDetailV> getInfoListByIds(@RequestBody List<String> ids){
        return contractIncomeBusinessService.getInfoListByIds(ids);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public ContractInfoV add(@Validated @RequestBody ContractIncomeAddF addF){
        return contractIncomeBusinessService.addIncomeContract(addF);
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @PostMapping("/edit")
    public ContractInfoV edit(@Validated @RequestBody ContractIncomeEditF editF) {
        return contractIncomeBusinessService.editIncomeContract(editF);
    }
    @ApiOperation(value = "修正", notes = "修正")
    @PostMapping("/correctionIncome")
    public ContractInfoV correctionIncome(@Validated @RequestBody ContractIncomeEditF editF) {
        return contractIncomeBusinessService.correctionIncome(editF);
    }


    @ApiOperation(value = "修改签约日期", notes = "修改签约日期")
    @PostMapping("/modifySignDate")
    public Boolean modifySignDate(@Validated @RequestBody ContractIncomeEditF editF) {
        return contractIncomeBusinessService.modifySignDate(editF);
    }


    @ApiOperation(value = "编辑", notes = "编辑")
    @PostMapping("/modify")
    public ContractInfoV modify(@Validated @RequestBody ContractIncomeAddF editF) {
        return contractIncomeBusinessService.modifyIncomeContract(editF);
    }

    /**
     * 根据合同ID获取合同详情
     * @return ContractIncomeConcludeDetailV
     */
    @ApiOperation(value = "根据合同ID获取合同详情", notes = "根据合同ID获取合同详情")
    @GetMapping("/detail")
    public ContractIncomeConcludeDetailV getDetailV(@Valid ContractDetailV param) {
        return contractIncomeBusinessService.detail(param);
    }

    /**
     * 根据合同ID获取合同详情，市拓免鉴权
     * @param id 合同 ID
     * @return ContractIncomeConcludeDetailV
     */
    @ApiOperation(value = "根据合同ID获取合同详情，市拓免鉴权", notes = "根据合同ID获取合同详情，市拓免鉴权")
    @GetMapping("/detailInfo")
    public ContractIncomeConcludeDetailV getDetailInfoV(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        ContractDetailV detailV = new ContractDetailV();
        detailV.setId(id);
        return contractIncomeBusinessService.detail(detailV);
    }

    /**
     * 根据合同ID获取合同附件列表详情
     * @return ContractIncomeConcludeDetailV
     */
    @ApiOperation(value = "根据合同ID获取合同详情", notes = "根据合同ID获取合同详情")
    @PostMapping("/fjxx")
    public ContractIncomeConcludeDetailFjxxV getFjxxDetailV(@RequestBody PageF<SearchF<ContractIncomeFjxxF>> contractIncomeFjxxF) {
        return contractIncomeBusinessService.getFjxxDetailV(contractIncomeFjxxF);
    }

    @ApiOperation(value = "提交合同", notes = "提交合同")
    @PostMapping("/post")
    public String post(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeBusinessService.postContract(id).trim();
    }

    @ApiOperation(value = "下拉选择主合同", notes = "下拉选择主合同")
    @PostMapping("/getMainContractIncome")
    public PageV<ContractIncomeConcludeV> getMainContractIncome(@RequestBody PageF<SearchF<ContractIncomeConcludeE>> request) {
        return contractIncomeBusinessService.getMainContractIncome(request);
    }

    /*@ApiOperation(value = "获取履约中合同数量", notes = "获取履约中合同数量")
    @PostMapping("/getPageShowNum")
    public Integer getPageShowNum() {
        return contractIncomeBusinessService.getPageShowNum();
    }*/

    @ApiOperation(value = "终止合同", notes = "终止合同")
    @PostMapping("/endContract")
    public Boolean endContract(@RequestBody ContractIncomeConcludeF form) {
        contractIncomeBusinessService.endContract(form);
        return true;
    }

    @ApiOperation(value = "收入合同导入模板文件下载链接", notes = "收入合同导入模板文件下载链接")
    @GetMapping("/download")
    public FileVo download() {
        return incomeExportService.download();
    }

    @ApiOperation(value = "收入合同Excel导入", notes = "收入合同Excel导入")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public ImportStandardV customerImport(@RequestParam(value = "file") MultipartFile file) {
        return incomeExportService.customerImport(file);
    }

    @ApiOperation(value = "获取变更记录", notes = "获取变更记录")
    @GetMapping("/changeRecord")
    public RelationRecordDetailV changeRecord(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeBusinessService.changeRecord(id);
    }

    @ApiOperation(value = "反审", notes = "反审")
    @GetMapping("/backReview")
    public Boolean backReview(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeBusinessService.backReview(id);
    }

    @ApiOperation(value = "新增缴纳保证金根据客户ID获取合同数据", notes = "新增缴纳保证金根据客户ID获取合同数据")
    @PostMapping("/getContractForBond")
    public List<ContractIncomeConcludeV> getContractForBond(@Validated @RequestBody BondRelationF form) {
        return contractIncomeBusinessService.getContractForBond(form);
    }

    @ApiOperation(value = "根据成本中心ID获取关联项目", notes = "根据成本中心ID获取关联项目")
    @GetMapping("/getComByCostCenterId")
    public SpaceCommunityRv getComByCostCenterId(@RequestParam("id") @NotBlank(message = "成本中心ID不可为空") String id) {
        return contractIncomeBusinessService.getCommunityByCostCenterId(id);
    }

    @ApiOperation(value = "获取合同页面金额数量展示数据", response = ContractNumShow.class)
    @PostMapping("/getPageShowNum")
    public ContractNumShow getPageShowNum(@RequestBody PageF<SearchF<ContractIncomeConcludeQuery>> request) {
        return contractIncomeBusinessService.getPageShowNum(request);
    }

    @ApiOperation(value = "选择合同（已筛选状态）", response = ContractIncomeConcludeV.class)
    @PostMapping("/pageForSelect")
    public PageV<ContractIncomeConcludeV> pageForSelect(@RequestBody PageF<ContractRevF> request) {
        return contractIncomeBusinessService.pageForSelect(request);
    }


    @ApiOperation(value = "根据id获取推送中交报文信息", notes = "根据id获取推送中交报文信息")
    @GetMapping("/getZjContractPullBody")
    public String getZjContractPullBody(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        return contractIncomeBusinessService.getZjContractPullBody(id);
    }

    @ApiOperation(value = "根据id推送中交信息接口", notes = "根据id推送中交信息接口")
    @GetMapping("/getZjContractPullInfo")
    public String getZjContractPullInfo(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
         return contractIncomeBusinessService.pullContract(id);
    }

    @ApiOperation(value = "根据id校验推送中交信息接口", notes = "根据id校验推送中交信息接口")
    @GetMapping("/getZjVerifyContractInfo")
    public ContractBasePullV getZjVerifyContractInfo(@RequestParam("id")  @NotBlank(message = "合同主键ID不可为空")  String id ){
        return contractIncomeBusinessService.verifyContract(id);
    }

    @ApiOperation(value = "泛微流程状态查询接口", response = ProcessQueryV.class)
    @PostMapping("/queryStatus")
    public ProcessQueryV queryStatus(@RequestParam("id") String id) {
        return contractIncomeBusinessService.queryStatus(id);
    }

    @ApiOperation(value = "泛微审批意见获取接口", response = OpinionApprovalV.class)
    @PostMapping("/opinionApproval")
    public OpinionApprovalV opinionApproval(@RequestParam("id") String id) {
        return contractIncomeBusinessService.opinionApproval(id);
    }
    @ApiOperation(value = "收入合同修正", notes = "收入合同修正")
    @PostMapping("/incomeCorrection")
    public String incomeCorrection(@Validated @RequestBody ContractIncomeEditF editF) {
        return contractIncomeBusinessService.incomeCorrection(editF);
    }
    @ApiOperation(value = "根据合同ID/修正记录ID获取合同详情", notes = "根据合同ID获取合同详情")
    @GetMapping("/getIncomeCorrectionDetail")
    public ContractIncomeConcludeDetailV getIncomeCorrectionDetail(@RequestParam("id") String id) {
        return contractIncomeBusinessService.getIncomeCorrectionDetail(id);
    }
    @ApiOperation(value = "根据合同ID查询修正记录列表", notes = "根据合同ID查询修正记录列表")
    @GetMapping("/getIncomeCorrectionList")
    public List<IncomeCorrectionHistoryV> getIncomeCorrectionList(@RequestParam("id") String id) {
        return contractIncomeBusinessService.getIncomeCorrectionList(id);
    }
    @ApiOperation(value = "根据修正记录ID发起BPM审批", notes = "根据修正记录ID发起BPM审批")
    @GetMapping("/incomeCorrectionApproval")
    public Boolean incomeCorrectionApproval(@RequestParam("id") String id) {
        return contractIncomeBusinessService.incomeCorrectionApproval(id);
    }
    @ApiOperation(value = "根据修正记录ID删除数据", notes = "根据修正记录ID删除数据")
    @GetMapping("/deleteIcomeCorrectionById")
    public Boolean deleteIcomeCorrectionById(@RequestParam("id") String id) {
        return contractIncomeBusinessService.deleteIcomeCorrectionById(id);
    }

}
