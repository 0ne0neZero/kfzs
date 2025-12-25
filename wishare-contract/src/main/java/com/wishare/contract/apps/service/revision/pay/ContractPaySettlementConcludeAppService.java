package com.wishare.contract.apps.service.revision.pay;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.contract.apis.file.enums.FileBusinessTypeEnum;
import com.wishare.contract.apis.file.service.FileService;
import com.wishare.contract.apis.file.utils.FileUtil;
import com.wishare.contract.apps.fo.revision.pay.ContractPaySettlementAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPaySettlementConcludeListF;
import com.wishare.contract.apps.fo.revision.pay.ContractPaySettlementConcludePageF;
import com.wishare.contract.apps.fo.revision.pay.ContractPaySettlementConcludeUpdateF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceDetailF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceSaveF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementF;
import com.wishare.contract.apps.fo.revision.pay.settlement.PaySettlementQueryF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementInvoiceDetailE;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.pay.ContractPaySettlementConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractSettlementInvoiceAttachmentService;
import com.wishare.contract.domains.service.revision.pay.ContractSettlementInvoiceDetailService;
import com.wishare.contract.domains.vo.revision.pay.*;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanPeriodV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPaySettlementFileInfoV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPaySettlementPageV2;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.wishare.starter.utils.ThreadLocalUtil.curIdentityInfo;

/**
 * @description:合同付款计划
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:49
 */
@Service
@Slf4j
public class ContractPaySettlementConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPaySettlementConcludeService contractPaySettlementConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementInvoiceAttachmentService  contractSettlementInvoiceAttachmentService;

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementInvoiceDetailService contractSettlementInvoiceDetailService;
    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;
    @Setter(onMethod_ = {@Autowired})
    private FileService fileService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Value("${wishare.file.host:}")
    private String fileHost;
    @Value("${contract.devFlag:0}")
    private Integer devFlag;

    public ContractPaySettlementDetailsV getDetailsById(String id){
        return contractPaySettlementConcludeService.getDetailsById(id);
    }

    public PageV<ContractPaySettlementConcludeV> page(PageF<SearchF<ContractPaySettlementConcludePageF>> request) {
        return contractPaySettlementConcludeService.page(request);
    }

    public PageV<ContractPaySettlementPageV2> pageV2(PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeService.pageV2(request);
    }

    /**
     * 查询支出合同结算明细分页列表
     * @param req
     * @return
     */
    public PageV<ContractPaySettlementPageV2> exportDetailsPage(PageF<SearchF<?>> req) {
        return contractPaySettlementConcludeService.exportDetailsPage(req);
    }

    public PageV<ContractPaySettlementConcludeInfoV> pageInfo(PageF<SearchF<ContractPaySettlementConcludePageF>> request) {
        IPage<ContractPaySettlementConcludeInfoV> pageList = contractPaySettlementConcludeService.pageInfo(request);
        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    public ContractPaySettlementConcludeEditV list(ContractPaySettlementConcludeListF contractPayPlanConcludeListF){
        return contractPaySettlementConcludeService.list(contractPayPlanConcludeListF);
    }

    public ContractPaySettlementConcludeSumV accountAmountSum(PageF<SearchF<?>> request) {
//        return contractPaySettlementConcludeService.accountAmountSum(request);
        return contractPaySettlementConcludeService.accountAmountSumV2(request);
    }

    public String save(ContractPaySettlementAddF addF){
        return contractPaySettlementConcludeService.save(addF);
    }

    public PageV<ContractSettdeductionDetailV> contractSettdeductionDetailPage(PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeService.contractSettdeductionDetailPage(request);
    }


    public void update(ContractPaySettlementConcludeUpdateF contractPayConcludeF){
        contractPaySettlementConcludeService.update(contractPayConcludeF);
    }


    public boolean removeById(String id){
        return contractPaySettlementConcludeService.removeById(id);
    }

    public ContractPayProcessV sumbitId(String id){
        return contractPaySettlementConcludeService.sumbitId(id);
    }

    public void returnId(String id){
        contractPaySettlementConcludeService.returnId(id);
    }

    public String invoice(ContractSettlementsBillF contractSettlementsBillF){
        return contractPaySettlementConcludeService.invoice(contractSettlementsBillF);
    }

    public String setFund(ContractSettlementsFundF contractSettlementsFundF){
        return contractPaySettlementConcludeService.setFund(contractSettlementsFundF);
    }

    public ContractPayPlanPeriodV getPlanPeriod(String contractId) {
        return contractPaySettlementConcludeService.getPlanPeriod(contractId);
    }

    public PageV<ContractPaySettlementPageV2> pageMockV2(PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeService.pageMockV2(request);
    }

    public ContractPaySettlementFileInfoV fileInfo(String settlementId) {
        return contractPaySettlementConcludeService.fileInfo(settlementId);
    }

    public String updateSettlementStep(String settlementId, Integer step) {
        return contractPaySettlementConcludeService.updateSettlementStep(settlementId, step);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean invoiceSave(ContractSettlementInvoiceSaveF contractSettlementInvoiceSaveF) {
        contractSettlementInvoiceAttachmentService.batchSaveAttachments(contractSettlementInvoiceSaveF);
        contractSettlementInvoiceDetailService.batchSaveDetails(contractSettlementInvoiceSaveF);
        if(Objects.nonNull(contractSettlementInvoiceSaveF.getOtherFile())){
            //其他附件-业务事由
            if(StringUtils.isNotBlank(contractSettlementInvoiceSaveF.getOtherFile().getOtherBusinessReasons())){
                //更新结算单中其他附件-业务事由
                contractPaySettlementConcludeService.updateOtherBusinessReasons(contractSettlementInvoiceSaveF.getSettlementId(),
                        contractSettlementInvoiceSaveF.getOtherFile().getOtherBusinessReasons(),
                        contractSettlementInvoiceSaveF.getOtherFile().getExternalDepartmentCode(),
                        contractSettlementInvoiceSaveF.getOtherFile().getCalculationMethod());
            }
            //其他附件-附件列表
            if(CollectionUtils.isNotEmpty(contractSettlementInvoiceSaveF.getOtherFile().getFileList())){
                IdentityInfo identityInfo = curIdentityInfo();
                List<AttachmentE> fileList = contractSettlementInvoiceSaveF.getOtherFile().getFileList();
                //获取已有附件数据
                List<String> isHaveFileList = fileList.stream().filter(x-> org.apache.commons.lang3.StringUtils.isNotBlank(x.getId())).map(AttachmentE:: getId).collect(Collectors.toList());
                //根据现有数据删除其余无效数据
                attachmentService.deleteInvalidFileById(FileBusinessTypeEnum.FINANCE_OTHER_FILE.getCode(), contractSettlementInvoiceSaveF.getSettlementId(), isHaveFileList);
                //获取需要保存的附件数据
                List<AttachmentE> isSaveFileList = fileList.stream().filter(x-> org.apache.commons.lang3.StringUtils.isBlank(x.getId())).collect(Collectors.toList());
                if(ObjectUtils.isNotEmpty(isSaveFileList)){
                    isSaveFileList.forEach(x->{
                        x.setId(UUID.randomUUID().toString());
                        x.setBusinessId(contractSettlementInvoiceSaveF.getSettlementId());
                        x.setBusinessType(FileBusinessTypeEnum.FINANCE_OTHER_FILE.getCode());
                        x.setTenantId(identityInfo.getTenantId());
                        x.setCreator(identityInfo.getUserId());
                        x.setCreatorName(identityInfo.getUserName());
                        x.setGmtCreate(LocalDateTime.now());
                        x.setDeleted(Boolean.FALSE);
                        if (devFlag != 1){
                            MultipartFile multipartFile = FileUtil.getMultipartFile(org.apache.commons.lang3.StringUtils.isNotBlank(x.getFileKey()) ? fileHost + x.getFileKey() : null);
                            String fileId = fileService.getZjFileId(multipartFile, false);
                            x.setFileuuid(fileId);
                        }
                    });
                    attachmentService.saveBatch(isSaveFileList);
                }
            }else{
                //根据现有数据删除其余无效数据
                attachmentService.deleteInvalidFileById(FileBusinessTypeEnum.FINANCE_OTHER_FILE.getCode(), contractSettlementInvoiceSaveF.getSettlementId(), new ArrayList<>());
            }
            //[合同报账单]更改对下实签来自合同数据
            financeFeignClient.updateBilDxZjFromContract(contractSettlementInvoiceSaveF.getSettlementId(),
                    contractSettlementInvoiceSaveF.getOtherFile().getOtherBusinessReasons(),
                    contractSettlementInvoiceSaveF.getOtherFile().getExternalDepartmentCode(),
                    contractSettlementInvoiceSaveF.getOtherFile().getCalculationMethod());
        }
        return Boolean.TRUE;
    }

    public List<ContractSettlementInvoiceDetailF> getInvoice(String id) {
        LambdaQueryWrapper<ContractSettlementInvoiceDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractSettlementInvoiceDetailE::getSettlementId, id)
                .eq(ContractSettlementInvoiceDetailE::getDeleted, 0);
        List<ContractSettlementInvoiceDetailE> list = contractSettlementInvoiceDetailService.list(queryWrapper);

        return Global.mapperFacade.mapAsList(list, ContractSettlementInvoiceDetailF.class);
    }

    public List<ContractPaySettlementF> listSettleByCondition(PaySettlementQueryF queryF) {
        if (StringUtils.isEmpty(queryF.getContractId())) {
            throw new OwlBizException("合同id不能为空");
        }
        return contractPaySettlementConcludeService.listSettleByCondition(queryF);
    }

    public boolean refresh3(PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeService.refresh3(request);
    }

    public boolean refresh4(PageF<SearchF<?>> request) {
        return contractPaySettlementConcludeService.refresh4(request);
    }

    //根据id删除结算审批
    public Boolean deletedPaySettlement(String id) {
        ContractPaySettlementConcludeE paySettlement = contractPaySettlementConcludeService.getById(id);
        if (Objects.isNull(paySettlement)) {
            throw new OwlBizException("请输入正确结算单ID");
        }


        return Boolean.TRUE;
    }
}
