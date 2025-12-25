package com.wishare.contract.apis.revision.projectInitiation;

import com.wishare.component.imports.ImportStandardV;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectInitiationPageF;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectInitiationSaveF;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectInitiationUpdateF;
import com.wishare.contract.apps.service.revision.export.ContractProjectOrderExportService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectInitiationAppService;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectInitiationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.DeliverReq;
import com.wishare.contract.domains.vo.revision.projectInitiation.ReceiveOrderResp;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.*;
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

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "立项管理")
@RestController
@RequestMapping("/projectInitiation")
@RequiredArgsConstructor
public class ContractProjectInitiationApi {

    private final ContractProjectInitiationAppService contractProjectInitiationAppService;

    private final ContractProjectOrderExportService contractProjectOrderExportService;

    @ApiOperation("分页查询立项")
    @PostMapping("/pageFront")
    public PageV<ContractProjectInitiationV> frontPage(@RequestBody PageF<SearchF<ContractProjectInitiationPageF>> pageF) {
        return contractProjectInitiationAppService.frontPage(pageF);
    }

    @ApiOperation("立项导出")
    @PostMapping("/pageFrontForExport")
    public PageV<ContractProjectInitiationV> frontPageForExport(@RequestBody PageF<SearchF<ContractProjectInitiationPageF>> pageF) {
        return contractProjectInitiationAppService.frontPageForExport(pageF);
    }

    @ApiOperation("查询立项详情")
    @GetMapping("/getDetail")
    public ContractProjectInitiationV getDetail(@RequestParam String id) {
        return contractProjectInitiationAppService.getDetail(id, true);
    }

    @ApiOperation("新增立项")
    @PostMapping
    public String save(@RequestBody @Validated ContractProjectInitiationSaveF saveF) {
        return contractProjectInitiationAppService.save(saveF);
    }

    @ApiOperation("修改立项")
    @PutMapping
    public String update(@RequestBody @Validated ContractProjectInitiationUpdateF updateF) {
        return contractProjectInitiationAppService.update(updateF);
    }

    @ApiOperation("删除立项")
    @DeleteMapping("/deleteById")
    public boolean delete(@RequestParam String id) {
        return contractProjectInitiationAppService.delete(id);
    }

    @ApiOperation("删除订单")
    @DeleteMapping("/deleteOrderById")
    public boolean deleteOrderById(@RequestParam String id) {
        return contractProjectInitiationAppService.deleteOrderById(id);
    }

    @ApiOperation(value = "立项订单导入", notes = "立项订单导入")
    @PostMapping("/orderImport")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public ImportStandardV orderImport(@RequestParam(value = "file") MultipartFile file, @RequestParam String projectInitiationId) {
        return contractProjectOrderExportService.orderImport(file, projectInitiationId);
    }

    @ApiOperation(value = "立项订单导入模板文件下载链接", notes = "立项订单导入模板文件下载链接")
    @GetMapping("/orderImportTemplateDownload")
    public FileVo orderImportTemplateDownload() {
        return contractProjectOrderExportService.orderImportTemplateDownload();
    }

    @ApiOperation(value = "发起审批")
    @PostMapping("/startApproval")
    public String startApproval(@RequestParam("id") @NotBlank(message = "立项ID不可为空") String id) {
        return contractProjectInitiationAppService.startApproval(id).trim();
    }

    @ApiOperation(value = "泛微审批意见获取接口", response = OpinionApprovalV.class)
    @PostMapping("/opinionApproval")
    public OpinionApprovalV opinionApproval(@RequestParam("id") String id) {
        return contractProjectInitiationAppService.opinionApproval(id);
    }

    @ApiOperation(value = "下单-京东慧采")
    @PostMapping("/startOrderForJD")
    public String startOrderForJD(@RequestBody @Validated StartOrderForJDReqF startOrderForJDReqF) {
        return contractProjectInitiationAppService.startOrderForJD(startOrderForJDReqF);
    }

    @ApiOperation(value = "立项下单校验")
    @PostMapping("/projectInitiationOrderVerification")
    public Boolean projectInitiationOrderVerification(@RequestBody ReceiveOrderResp receiveOrderResp) {
        return contractProjectInitiationAppService.projectInitiationOrderVerification(receiveOrderResp);
    }

    @ApiOperation(value = "订单状态更新")
    @PostMapping("/syncOrderStatus")
    public Boolean syncOrderStatus(@RequestBody DeliverReq deliverReq) {
        return contractProjectInitiationAppService.syncOrderStatus(deliverReq);
    }

    @ResponseBody
    @ApiOperation(value = "获取费项映射关系树", notes = "获取费项映射关系树")
    @RequestMapping(value = "/getCodeMappingTree", method = {RequestMethod.GET})
    public CostBaseResponse<List<CostItemNode>> getCodeMappingTree(@RequestParam(defaultValue = "") String zjCode) {
        return contractProjectInitiationAppService.getCodeMappingTree(zjCode);
    }

    @ResponseBody
    @ApiOperation(value = "查询项目下费项可用金额明细", notes = "查询项目下费项可用金额明细")
    @RequestMapping(value = "/getDynamicCostSurplusProp", method = {RequestMethod.POST})
    public CostBaseResponse<DynamicCostSurplusInfo> getDynamicCostSurplusProp(@RequestBody @Validated DynamicCostSurplusPropReqF dynamicCostSurplusPropReqF) {
        return contractProjectInitiationAppService.getDynamicCostSurplusProp(dynamicCostSurplusPropReqF);
    }

    @ResponseBody
    @ApiOperation(value = "查询月度分摊明细", notes = "查询月度分摊明细")
    @RequestMapping(value = "/getMonthlyAllocationDetail", method = {RequestMethod.POST})
    public List<ContractProjectPlanMonthlyAllocationV> getMonthlyAllocationDetail(@RequestBody @Validated(DynamicCostSurplusPropReqF.monthlyAllocationReq.class) DynamicCostSurplusPropReqF dynamicCostSurplusPropReqF) {
        return contractProjectInitiationAppService.getMonthlyAllocationDetail(dynamicCostSurplusPropReqF);
    }

    @ResponseBody
    @ApiOperation(value = "合约规划成本确认", notes = "合约规划成本确认")
    @RequestMapping(value = "/contractPlanConfirm", method = {RequestMethod.POST})
    public Boolean contractPlanConfirm(@RequestBody ContractProjectInitiationUpdateF req) {
        return contractProjectInitiationAppService.contractPlanConfirm(req);
    }
}