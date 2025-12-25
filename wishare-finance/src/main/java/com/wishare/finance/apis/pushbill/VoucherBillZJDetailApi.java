package com.wishare.finance.apis.pushbill;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.apps.pushbill.fo.UpLoadFileF;
import com.wishare.finance.apps.pushbill.fo.VoucherBillRecMdm63F;
import com.wishare.finance.apps.pushbill.fo.VoucherBillZJRecDetailF;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63Response;
import com.wishare.finance.apps.service.pushbill.VoucherBillDetailAppService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.wishare.starter.utils.ThreadLocalUtil.curIdentityInfo;

@Api(tags = {"推单明细中交"})
@RestController
@RequestMapping("/pushbillZJ")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillZJDetailApi {

    private final VoucherBillDetailZJRepository voucherBillDetailRepository;
    private final FileStorage fileStorage;
    private final ContractClient contractClient;
    private final VoucherBillDetailAppService voucherBillDetailAppService;
    private final VoucherPushBillZJRepository voucherPushBillZJRepository;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;

    @PostMapping("/page")
    @ApiOperation(value = "获取推单(分页)", notes = "获取推单(分页)")
    public PageV<VoucherBillZJDetailV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherBillDetailRepository.pageBySearch(form), VoucherBillZJDetailV.class);
    }

    @PostMapping("/queryMoney")
    @ApiOperation(value = "获取推单明细金额", notes = "获取推单明细金额")
    public VoucherBillZJDetailMoneyV queryMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryMoneyByGroup(form);
    }


    @PostMapping("/convertDetail/queryMoney")
    @ApiOperation(value = "获取推单明细金额", notes = "获取推单明细金额")
    public VoucherBillZJConvertMoneyV queryConvertDetailMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryConvertDetailMoney(form);
    }

    // 收入确认单下影像资料
    @PostMapping("/queryFileForRec")
    @ApiOperation(value = "收入确认单下影像资料 ", notes = "收入确认单下影像资料 ")
    public List<VoucherBillZJFileSV> queryFileForRec(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryFileForRec(form);
    }


    // 资金收款单下的 应收款明细
    @PostMapping("/page/recDetail")
    @ApiOperation(value = "资金收款单下的 应收款明细", notes = "资金收款单下的 应收款明细")
    public PageV<VoucherBillZJRecDetailV> queryRecDetailPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryRecDetailPage(form);
    }

    @GetMapping("/recDetail/v2")
    @ApiOperation(value = "资金收款单下的 应收款明细-V2", notes = "资金收款单下的 应收款明细-V2")
    public List<VoucherBillZJRecDetailV2> queryRecDetailPageV2(@RequestParam("voucherBillNo") String voucherBillNo) {
        return voucherBillDetailAppService.queryRecDetailPageV2(voucherBillNo);
    }

    @GetMapping("/communityInfo")
    @ApiOperation(value = "根据报账单号查询项目信息", notes = "根据报账单号查询项目信息")
    public SpaceCommunityShortV queryCommunityByVoucherBillNo(@RequestParam("voucherBillNo") String voucherBillNo){
        return voucherBillDetailAppService.queryCommunityByVoucherBillNo(voucherBillNo);
    }

    @PostMapping("/recDetail/saveOrUpdate")
    @ApiOperation(value = "资金收款单下的 应收款明细-新增或修改-V2", notes = "资金收款单下的 应收款明细-新增或修改-V2")
    public String saveOrUpdateRecDetail(@RequestBody VoucherBillZJRecDetailF recDetailF) {
        return voucherBillDetailAppService.saveOrUpdateRecDetail(recDetailF);
    }

    @PostMapping("/recDetail/mdm63/page")
    @ApiOperation(value = "资金收款单-应收款明细V2-应收应付筛选", notes = "资金收款单-应收款明细V2-应收应付筛选")
    public PageV<Mdm63FrontV> queryMdm63Page(@RequestBody PageF<VoucherBillRecMdm63F> pageF){
        return voucherBillDetailAppService.queryMdm63Page(pageF);
    }

    // 资金收款单下的 认领明细
    @PostMapping("/page/flowDetail")
    @ApiOperation(value = "资金收款单下的 认领明细", notes = "资金收款单下的 认领明细")
    public PageV<VoucherBillZJFlowDetailV> queryFlowDetailPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherBillDetailAppService.queryFlowDetailPage(form), VoucherBillZJFlowDetailV.class);
    }

    @PostMapping("/page/convertDetail")
    @ApiOperation(value = "资金收款单下的 报账明细下的视图转换", notes = "资金收款单下的 报账明细下的视图转换")
    public PageV<VoucherBillZJConvertDetailV> queryConvertDetailPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherBillDetailAppService.queryConvertDetailPage(form), VoucherBillZJConvertDetailV.class);
    }


    // 资金收款单下的 现金留下
    @PostMapping("/page/CashFlow")
    @ApiOperation(value = "资金收款单下的 现金流", notes = "资金收款单下的 现金流")
    public PageV<VoucherBillZJCashFlowV> queryCashFlowPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryCashFlowPage(form);
    }

    // 资金收款单下的  影像附件
    @PostMapping("/page/FileS")
    @ApiOperation(value = "资金收款单下的  影像附件", notes = "资金收款单下的  影像附件")
    public List<VoucherBillZJFileSV> queryFileSPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryFileSPage(form);
    }


    // 对下结算单下的 发票明细
    @PostMapping("/queryInvoiceInfo")
    @ApiOperation(value = "对下结算单下的 发票明细", notes = "对下结算单下的 发票明细")
    public PageV<VoucherBillInvoiceV> queryInvoiceInfo(@RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryInvoiceInfo(form);
    }

    // 对下结算单下的 计量清单
    @PostMapping("/queryMeasurementDetail")
    @ApiOperation(value = "对下结算单下的 计量清单", notes = "对下结算单下的 计量清单")
    public PageV<VoucherBillMeasurementDetailV> queryMeasurementDetail(@RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryMeasurementDetail(form);
    }

    // 对下结算单下的 款项明细
    @PostMapping("/queryPaymentDetails")
    @ApiOperation(value = "对下结算单下的 款项明细", notes = "对下结算单下的 款项明细")
    public PageV<VoucherBillPaymentDetailsV> queryPaymentDetails(@RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryPaymentDetails(form);
    }

    // 对下结算单下的 成本明细
    @PostMapping("/queryCostInfoV")
    @ApiOperation(value = "对下结算单下的 成本明细(根据报账单号查询)", notes = "对下结算单下的 成本明细(根据报账单号查询)")
    public PageV<VoucherBillCostInfoV> queryCostInfoV(@RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryCostInfoV(form);
    }

    // 对下结算单下的 影像附件
    @PostMapping("/queryFileForSettle")
    @ApiOperation(value = "对下结算单下的 影像附件", notes = "对下结算单下的 影像附件")
    public List<VoucherBillZJFileSV> queryFileForSettle(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailAppService.queryFileForSettle(form);
    }

    @ApiOperation(value = "中交文件上传", notes = "中交文件上传")
    @PostMapping("/upload/fileZJ")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public FileV formalSave(@RequestParam("file") MultipartFile file, String billNo) {
        ZJFileVo zjFileVo = contractClient.zjUpload(file, billNo);
        FileV fileV = new FileV();
        if (Objects.nonNull(zjFileVo)) {
            FileVo fileVo = fileStorage.formalSave(file, FormalF.builder().tenantId(curIdentityInfo().getTenantId())
                    .serverName(this.getClass().getSimpleName())
                    .clazz(this.getClass())
                    .businessId(UUID.randomUUID().toString()).build());
            fileV.setFileVo(fileVo);
            fileV.setZjFileVo(zjFileVo);
            fileV.setBillNo(billNo);
            VoucherBillZJ voucherBillNo = voucherPushBillZJRepository.getOne(new QueryWrapper<VoucherBillZJ>()
                    .eq("voucher_bill_no", billNo));
            if (voucherBillNo.getBillEventType().equals(1)){
                UpLoadFileF upLoadFileF = new UpLoadFileF();
                upLoadFileF.setVoucherBillId(voucherBillNo.getId());
                upLoadFileF.setVoucherBillNo(billNo);
                upLoadFileF.setFiles(Arrays.asList(fileVo));
                upLoadFileF.setUploadFlag(1);
                voucherBillFileZJRepository.addFileInfo(upLoadFileF);
            }
            return fileV;
        }
        return fileV;
    }

    @PostMapping("/getPushDetailsByBillId")
    @ApiOperation(value = "根据账单id获取推单明细", notes = "根据账单id获取推单明细")
    public List<VoucherPushBillDetailZJ> getPushDetails(@Validated @RequestBody List<Long> ids) {
        return voucherBillDetailRepository.getPushDetails(ids);
    }

    @GetMapping("/refresh/mdm63/getProjectId")
    @ApiOperation(value = "根据项目ID重新获取MDM63数据", notes = "根据项目ID重新获取MDM63数据")
    public List<Mdm63Response> getProjectIdRefresh(@RequestBody @RequestParam("projectId") String projectId){
        return voucherBillDetailAppService.getProjectIdRefresh(projectId);
    }
}
