package com.wishare.finance.apis.reconciliation;

import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import com.wishare.finance.apps.model.reconciliation.fo.*;
import com.wishare.finance.apps.model.reconciliation.vo.*;
import com.wishare.finance.apps.service.reconciliation.FlowClaimAppService;
import com.wishare.finance.apps.service.reconciliation.FlowTempAppService;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流水领用记录
 *
 * @author yancao
 */
@Api(tags = {"流水认领接口"})
@Validated
@RestController
@RequestMapping("/flowClaim")
@RequiredArgsConstructor
public class FlowClaimApi {

    private final FlowClaimAppService flowClaimAppService;
    private final FlowDetailRepository flowDetailRepository;
    private final FlowTempAppService flowTempAppService;

    @PostMapping("/page/record")
    @ApiOperation(value = "分页查询领用记录", notes = "分页查询领用记录")
    public PageV<FlowClaimRecordPageV> pageRecord(@Validated @RequestBody PageF<SearchF<FlowClaimRecordE>> queryF) {
        return flowClaimAppService.pageRecord(queryF);
    }

    @PostMapping("/claim")
    @ApiOperation(value = "认领流水", notes = "认领流水")
    public Boolean claim(@Validated @RequestBody ClaimFlowF claimFlowF) {
         flowClaimAppService.claim(claimFlowF);
        return true;
    }

    @PostMapping("/claim/zj")
    @ApiOperation(value = "认领流水-中交专用", notes = "认领流水-中交专用")
    public Boolean claimForZJ(@Validated @RequestBody ClaimFlowZJF claimFlowZJF) {
        return flowClaimAppService.claimForZJ(claimFlowZJF);
    }

    @PostMapping("/queryOids")
    @ApiOperation(value = "查询4A组织id", notes = "查询4A组织id")
    public OrgOidV queryOids(@RequestBody OrgOidQueryF orgOidQueryF) {
        return flowClaimAppService.queryOids(orgOidQueryF);
    }

    @PostMapping("/queryBankAccounts")
    @ApiOperation(value = "查询银行账户", notes = "查询银行账户")
    public BankAccountV queryBankAccounts(@RequestBody BankAccountQueryF bankAccountQueryF) {
        return flowClaimAppService.queryBankAccounts(bankAccountQueryF);
    }

    @PostMapping("/revoked")
    @ApiOperation(value = "批量撤销认领", notes = "批量撤销认领")
    public FlowDetailRevokedV revoked(@Validated @RequestBody RevokedFlowF revokedFlowF) {
        return flowClaimAppService.revoked(revokedFlowF);
    }

//    @GetMapping("/query/detail/{recordId}")
//    @ApiOperation(value = "根据流水认领记录id获取认领详情", notes = "根据流水认领记录id获取认领详情")
//    public FlowClaimRecordDetailV queryDetailByRecordId(@PathVariable("recordId") Long recordId) {
//        return flowClaimAppService.queryDetailByRecordId(recordId);
//    }

    @GetMapping("/claim/detail/{flowId}")
    @ApiOperation(value = "根据流水记录id获取认领详情", notes = "根据流水记录id获取认领详情")
    public FlowClaimRecordDetailV claimDetail(@PathVariable Long flowId,
        @ApiParam("上级收费单元id")  String supCpUnitId) {
        return flowClaimAppService.claimDetail(flowId, supCpUnitId);
    }

    // 根据流水id 获取认领单据详情

    @PostMapping("/claim/gatherInfo")
    @ApiOperation(value = "根据流水id 获取认领单据详情", notes = "根据流水id 获取认领单据详情")
    public PageV<ContractFlowBillV> queryByFlowDetail(PageF<SearchF<?>> queryF){
        return null;
    }


    @PostMapping("/claim/gather/detail")
    @ApiOperation(value = "根据收款单ID列表获取关联流水认领明细", notes = "根据收款单ID列表获取关联流水认领明细")
    public List<FlowClaimDetailV> getRecGatherIdFlowClaimRecord(@Validated @RequestBody @NotEmpty(message = "收款单id列表不能为空")List<Long> gatherIds,
                                                                @RequestParam("supCpUnitId")@NotBlank(message = "上级单元id不能为空")String supCpUnitId){
        return flowClaimAppService.getRecGatherIdFlowClaimRecord(gatherIds,supCpUnitId);
    }

    @PostMapping("/add/detail")
    @ApiOperation(value = "新增流水明细", notes = "新增流水明细")
    public Boolean addDetail(@RequestBody @Validated AddFlowDetailF addFlowDetailF) {
        return flowClaimAppService.addDetail(addFlowDetailF);
    }

    @PostMapping("/page/detail")
    @ApiOperation(value = "分页查询流水明细", notes = "分页查询流水明细")
    public PageV<FlowDetailPageV> pageDetail(@Validated @RequestBody PageF<SearchF<FlowDetailE>> queryF) {
        return flowClaimAppService.pageDetail(queryF);
    }

    @PostMapping("/remit/page/detail")
    @ApiOperation(value = "分页查询流水明细(用于汇款认领)", notes = "分页查询流水明细(用于汇款认领)")
    public PageV<FlowDetailPageV> remitPageDetail(@Validated @RequestBody PageF<SearchF<FlowDetailE>> queryF) {
        return flowClaimAppService.remitPageDetail(queryF);
    }

    @PostMapping("/list/detail")
    @ApiOperation(value = "查询流水明细", notes = "查询流水明细")
    public List<FlowDetailPageV> listDetail(@RequestParam("id") Long id) {
        return flowClaimAppService.listDetail(id);
    }

    /**
     * 提供一个其他服务访问的查询流水明细接口
     * @param queryF
     * @return
     */
    @PostMapping("/page/outDetail")
    public PageV<FlowDetailPageRV> pageOutDetail(@Validated @RequestBody PageF<SearchF<FlowDetailE>> queryF) {
        return flowClaimAppService.pageOutDetail(queryF);
    }

    @ApiOperation(value = "导入流水明细", notes = "导入流水明细")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    @PostMapping("/import")
    public FlowImportV importFlow(@RequestParam("file") MultipartFile file){
        return flowClaimAppService.importFlow(file);
    }

    @PostMapping("/statistics/amount")
    @ApiOperation(value = "查询流水明细合计金额", notes = "查询流水明细合计金额")
    public FlowStatisticsV statisticsAmount(@Validated @RequestBody PageF<SearchF<FlowDetailE>> queryF){
        return flowClaimAppService.statisticsAmount(queryF);
    }

    @PostMapping("/statistics/claim/info")
    @ApiOperation(value = "统计已勾选流水信息", notes = "统计已勾选流水信息")
    public FlowStatisticsClaimInfoV statisticsClaimInfo(@Valid @RequestBody @ApiParam("流水明细id") @NotEmpty(message = "流水明细id不能为空")  List<Long> flowIdList){
        return flowClaimAppService.statisticsClaimInfo(flowIdList);
    }

    @GetMapping("/query/file")
    @ApiOperation(value = "获取流水导入模板文件", notes = "获取流水导入模板文件")
    public String queryFile(){
        return flowClaimAppService.queryFile();
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载流水导入模板", notes = "下载流水导入模板")
    public void download( HttpServletResponse response) {
        flowClaimAppService.download(response);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation(value = "批量删除流水", notes = "批量删除流水")
    public Boolean deleteBatch(@Valid @RequestBody @ApiParam("流水明细id") @NotEmpty(message = "流水明细id不能为空")  List<Long> flowIdList) {
        return flowClaimAppService.deleteBatch(flowIdList);
    }

    @PutMapping("/groupinfo/delete/batch")
    @ApiOperation(value = "统计批量删除流水信息", notes = "统计批量删除流水信息")
    public Map<String,Object> groupInfoDeleteBatch(@Valid @RequestBody @ApiParam("流水明细id") @NotEmpty(message = "流水明细id不能为空")  List<Long> flowIdList) {
        return flowClaimAppService.groupInfoDeleteBatch(flowIdList);
    }

    @PostMapping("/suspend/{flowId}")
    @ApiOperation(value = "挂起流水", notes = "挂起流水")
    public Boolean suspend(@PathVariable Long flowId){
        return flowClaimAppService.suspend(flowId);
    }

    @PostMapping("/unsuspend/{flowId}")
    @ApiOperation(value = "解除挂起", notes = "解除挂起")
    public Boolean unsuspend(@PathVariable Long flowId){
        return flowClaimAppService.unsuspend(flowId);
    }

    /**
     * 流水认领获取当前登录用户项目权限
     */
    @GetMapping("/getCommunityInfo")
    @ApiOperation(value = "流水认领获取当前登录人所属权限项目", notes = "流水认领获取当前登录人所属权限项目")
    public List<SpaceCommunityShortV> getCommunityInfo(){
        return flowClaimAppService.getCommunityInfo();
    }

    @GetMapping("/getFlowClaimRecord/{flowId}")
    @ApiOperation(value = "流水认领获取所属项目Id", notes = "流水认领获取所属项目Id")
    public  FlowClaimRecordE getFlowClaimRecord(@PathVariable String flowId){
        return flowClaimAppService.getFlowClaimRecord(flowId);
    }

    @GetMapping("/getFlowClaimSerialNumbers")
    @ApiOperation(value = "流水导入获取流水号", notes = "流水导入获取流水号")
    public Set<String> querySerialNumbers(){
       return flowClaimAppService.querySerialNumbers();
    }

    @PostMapping ("/importFlowClaim")
    @ApiOperation(value = "流水明细导入", notes = "流水明细导入")
    public Boolean importFlowClaim(@RequestBody List<FlowClaimDetailImportT> list){
        return flowClaimAppService.importFlowClaim(list);
    }

    /**
     * 中交同步流水
     * WishareExternal 通过定时任务调用到改接口
     * 其他业务慎用！！！！
     * @param list
     * @return
     */
    @PostMapping ("/syncFlowClaim")
    @ApiOperation(value = "流水明细同步", notes = "流水明细同步")
    public Boolean syncFlowClaim(@RequestBody List<FlowDetailV> list){
        return flowClaimAppService.syncFlowClaim(list);
    }

    @PostMapping ("/page/flowRecord")
    @ApiOperation(value = "查询流水认领记录", notes = "查询流水认领记录")
    public PageV<FlowRecordPageV> getPageRecord(@RequestBody PageF<SearchF<FlowRecordPageV>> queryF) {
        return flowClaimAppService.getPageRecord(queryF);
    }

    @GetMapping("/claim/detailByRecordId/{id}")
    @ApiOperation(value = "根据流水流水认领记录表id获取认领详情", notes = "根据流水流水认领记录表id获取认领详情")
    public FlowClaimRecordDetailV getClaimDetailByRecordId(@PathVariable Long id,
                                              @ApiParam("上级收费单元id")  String supCpUnitId) {
        return flowClaimAppService.getClaimDetailByRecordId(id, supCpUnitId);
    }


    @GetMapping("/claim/detailBySerialNumber/{serialNumber}")
    @ApiOperation(value = "根据流水认领记录批次号获取认领详情", notes = "根据流水认领记录批次号获取认领详情")
    public FlowClaimRecordE getClaimDetailBySerialNumber(@PathVariable String serialNumber) {
        return flowClaimAppService.getClaimDetailBySerialNumber(serialNumber);
    }

    @PostMapping("/claim/getFlowRecordStatistics")
    @ApiOperation(value = "流水认领记录统计详情", notes = "流水认领记录统计详情")
    public FlowRecordStatisticsV getFlowRecordStatistics(@RequestBody PageF<SearchF<?>> queryF){
        return flowClaimAppService.getFlowRecordStatistics(queryF);
    }


    @PostMapping("/updateFlowByApprove")
    @ApiOperation(value = "根据审核状态更新流水记录、流水", notes = "根据审核状态更新流水记录、流水")
    public Boolean updateFlowByApprove(@RequestBody UpdateFlowF updateFlowF){
        return flowClaimAppService.updateFlowByApprove(updateFlowF);
    }


    // 新增暂存数据账单
    @PostMapping("/addFlowTemp")
    @ApiOperation(value = "新增暂存记录", notes = "新增暂存记录")
    public Boolean addFlowTemp(@RequestBody AddFlowTempF addFlowTempF){

        return flowTempAppService.addFlowTemp(addFlowTempF);
    }

    // 查询暂存数据

    @PostMapping("/queryFlowTemp")
    @ApiOperation(value = "根据流水、用户查询暂存记录", notes = "根据流水、用户查询暂存记录")
    public List<FlowTempRecordV> queryFlowTemp(@RequestBody @ApiParam("流水id") @NotEmpty(message = "勾选流水不能为空")   List<Long> flowIds){
        return flowTempAppService.queryFlowTemp(flowIds);
    }

    @GetMapping("/queryFlowTempDetail/{flowTempRecordId}")
    @ApiOperation(value = "根据暂存记录查询账单", notes = "根据暂存记录查询账单")
    public List<FlowBillPageDto> queryFlowTempDetail(@PathVariable  Long flowTempRecordId){
        return flowTempAppService.queryFlowTempDetail(flowTempRecordId);
    }

    // 删除暂存数据

    @GetMapping("/deleteFlowTemp/{flowTempId}")
    @ApiOperation(value = "删除暂存记录", notes = "删除暂存记录")
    public Boolean deleteFlowTemp(@PathVariable Long flowTempId){
        return flowTempAppService.deleteFlowTemp(flowTempId);
    }

}
