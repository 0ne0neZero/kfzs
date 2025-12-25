package com.wishare.finance.apps.service.strategy.receipt;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.SignExternalSealVo;
import com.wishare.finance.apps.model.signature.EsignResult;
import com.wishare.finance.apps.service.strategy.ReceiptTenant;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.repository.mapper.GatherDetailMapper;
import com.wishare.finance.domains.invoicereceipt.aggregate.ReceiptA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptMapper;
import com.wishare.finance.domains.invoicereceipt.repository.mapper.ReceiptTemplateMapper;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


public abstract class AbReceiptTenant implements ReceiptTenant {

    protected static final String SKIP = "skip";


    @Override
    public String getReceiptTemplatePath(ReceiptTemplateE templateE) {
        // 模板路径，默认为之前的一个路径
        String templatePath = null;
        if (Objects.nonNull(templateE)) {
            ReceiptTemplateStyleEnum styleEnum = ReceiptTemplateStyleEnum.valueOfCode(templateE.getTemplateStyle());
            templatePath = Objects.isNull(styleEnum) ? templatePath : styleEnum.getTemplatePath();
        }
        return StringUtils.isEmpty(templatePath)?ReceiptTemplateStyleEnum.样式一.getTemplatePath():templatePath;
    }


    /**
     * 根据ID获取票据模板信息如果没有就塞空
     *
     * @param command
     * @return
     */
    @Override
    public void verifyTemplate(AddReceiptCommand command) {
        ReceiptTemplateE receiptTemplateE = Global.ac.getBean(ReceiptTemplateMapper.class).selectById(command.getReceiptTemplateId());
        //是否配置模板 false:无
        boolean b = Objects.nonNull(receiptTemplateE);
        /** 对command 发起变更(模板信息) */
        command.setReceiptTemplateName(b?receiptTemplateE.getTemplateName():"");
        command.setReceiptTemplateId(b?receiptTemplateE.getId():null);

    }


    /**
     * @param command
     * @param receiptA
     */
    @Override
    public void increaseInvoiceReceiptE(AddReceiptCommand command, ReceiptA receiptA,List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map) {
        InvoiceReceiptE invoiceReceiptE = receiptA.getInvoiceReceiptE();
        //变更收款人以及收款方式
        this.updatePayMsg(invoiceReceiptE, command);
        //remark需要updatePayMsg方法塞的payChannel
        invoiceReceiptE.setRemark(receiptA.handleRemarkOpen(billDetailMoreVList,null,invoiceReceiptE.getPayChannel()));
    }



    /**
     * 校检该收据号是否被使用
     *
     * @param receiptNo
     */
    @Override
    public void verifyReceiptNoCanUse(Long receiptNo) {
        if (Objects.isNull(receiptNo)) {
            return;
        }
        // receipt optimize[receipt_no]
        List<ReceiptE> receiptES = Global.ac.getBean(ReceiptMapper.class).selectList(
                new LambdaQueryWrapper<ReceiptE>().eq(ReceiptE::getReceiptNo, receiptNo));
        ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(receiptES), "该收据号已被使用");
    }


    /**
     * 校验一致性
     * 法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)
     *
     * @param billOjvs
     */
    @Override
    public void verifyConsistency(List<BillDetailMoreV> billOjvs) {
        //校检账单中法定单位是否相同
        List<Long> statutoryBodyIds = billOjvs.stream().map(BillDetailMoreV::getStatutoryBodyId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(statutoryBodyIds) && statutoryBodyIds.size() != 1, "该批次账单法定单位不一致");
        //校检账单中收费对象是否相同
        if (!EnvConst.FANGYUAN.equals(EnvData.config)) {
            List<String> payerIds = billOjvs.stream().map(BillDetailMoreV::getPayerId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(payerIds) && payerIds.size() != 1, "该批次账单收费对象不一致");
        }
        //校检账单中账单来源是否相同
        List<Integer> sourceIds = billOjvs.stream().map(BillDetailMoreV::getSysSource).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(sourceIds) && sourceIds.size() != 1, "该批次账单来源不一致");
        //校检账单中项目是否相同
        List<String> communityIds = billOjvs.stream().map(BillDetailMoreV::getCommunityId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(communityIds) && communityIds.size() != 1, "该批次账单项目不一致");
        //校检账单中成本中心是否相同
        List<Long> costCenterIds = billOjvs.stream().map(BillDetailMoreV::getCostCenterId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        ErrorAssertUtils.isFalseThrow400(CollectionUtils.isNotEmpty(costCenterIds) && costCenterIds.size() != 1, "该批次账单成本中心不一致");
    }


    /**
     * 校检是否存在已经开票账单
     *
     * @param billDetailMoreVList
     * @param map
     */
    @Override
    public void verifyInvoiceState(List<BillDetailMoreV> billDetailMoreVList, Map<String, Object> map) {
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            //'开票状态：0未开票，1开票中，2部分开票，3已开票
            ErrorAssertUtils.isFalseThrow400(BillInvoiceStateEnum.已开票.getCode().equals(billDetailMoreV.getInvoiceState()), "该批次账单存在已开票状态，请重新选择");
            ErrorAssertUtils.isFalseThrow400(BillInvoiceStateEnum.开票中.getCode().equals(billDetailMoreV.getInvoiceState()), "该批次账单存在开票中状态，请重新选择");
        }
    }


    /**
     * 变更收款人以及收款方式
     *
     * @param command
     * @param invoiceReceiptE
     */
    private void updatePayMsg(InvoiceReceiptE invoiceReceiptE, AddReceiptCommand command) {
        List<Long> billIds = command.getBillIds();
        if (CollectionUtils.isEmpty(billIds)) {
            return;
        }
        //取账单最新的收款人以及收款方式 收款单明细(gather_detail)
        GatherDetail gatherDetail = Global.ac.getBean(GatherDetailMapper.class).selectOne(
                new LambdaQueryWrapper<GatherDetail>()
                        .in(GatherDetail::getRecBillId, billIds)
                        .eq(GatherDetail::getDeleted, 0)
                        .eq(GatherDetail::getSupCpUnitId, command.getSupCpUnitId())
                        .isNotNull(GatherDetail::getPayChannel)
                        .isNotNull(GatherDetail::getPayeeName)
                        .orderByDesc(GatherDetail::getPayTime)
                        .last("limit 1")
        );
        if (Objects.nonNull(gatherDetail)) {
            invoiceReceiptE.setPayChannel(SettleChannelEnum.valueOfByCode(gatherDetail.getPayChannel()).getValue());
            invoiceReceiptE.setPayeeName(gatherDetail.getPayeeName());

        }
    }

    @Override
    public void signOnPdf(ReceiptTemplateE receiptTemplateE, Integer signStatus,Map<String, Object> map) {
    }

    @Override
    public void pdfLogo(String path, HashMap<String, Object> templateData) {
    }

    @Override
    public boolean signExternalSeal(SignExternalSealVo vo) {
        return true;
    }

    /**
     * 付款方信息推送
     *
     * @param receiptVDto 数据对象集
     */
    @Override
    public int receiptSend(ReceiptVDto receiptVDto) {
        return Global.ac.getBean(ReceiptDomainService.class).receiptSend(receiptVDto.getPushMode(), receiptVDto, new HashMap<>() {{
            put("email", receiptVDto.getEmail());
        }});
    }

    /**
     * 发送信息给付款人
     */
    @Override
    public void afterReceiptSend(ReceiptVDto receiptVDto, Integer way) {
        Global.ac.getBean(ReceiptDomainService.class).afterReceiptSend(receiptVDto.getId(), way, receiptVDto);
    }


    /**
     * 开具成功\开票调用财务中心完成开票
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param status 开票\开具状态 true：成功 false：失败
     * @param invoiceFlowMonitorId 流程id
     * @param signStatus
     * BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), invoiceReceiptDetailEList, true, invoiceReceiptE.getCommunityId()
     */
    @Override
    public void handleBillStateFinishInvoice(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, boolean status,
            Long invoiceFlowMonitorId,Map<String, Object> map,Integer signStatus){
        //完成开票
        Global.ac.getBean(BillFacade.class).handleBillStateFinishInvoice(invoiceReceiptDetailEList,status,invoiceReceiptE.getCommunityId());
    }


    /**
     * 修改账单状态为开票中
     * @param invoiceReceiptE
     * @param invoiceReceiptDetailEList
     * @param command
     */
    @Override
    public void invoiceBatch(InvoiceReceiptE invoiceReceiptE,
            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList, AddReceiptCommand command,Map<String, Map<Long, Integer>> stringMapMap) {
        //
    }

    /**
     * 收据作废
     * @param receiptVDto
     * @return
     */
    @Override
    public boolean voidReceiptV(ReceiptVDto receiptVDto) {
        return false;
    }

    /**
     * 获取签署结果
     */
    @Override
    public EsignResult signResult(String signFlowId) {
        return null;
    }

    @Override
    public EsignResult voidResult(String signFlowId) {
        return null;
    }

    @Override
    public ReceiptE signResultAfter(Long receiptEId, EsignResult esignResult) {
        return null;
    }

    @Override
    public ReceiptE voidResultAfter(Long receiptEId, EsignResult esignResult) {
        return null;
    }

    @Override
    public void replaceAgainInvoiceState(List<InvoiceReceiptDetailE> invoiceReceiptDetailEList,
            AddReceiptCommand command) {
    }

    @Override
    public void flowRecordSignSuccAfter(ReceiptVDto receiptVDto){}

    /**
     * 提供链接
     * 针对eSign远洋需要特殊处理 不再直接提供文件地址 而是进行代理 再收据回收的时候提示收据已失效
     * @param no
     * @return
     */
    public String eSignUrl(String no){
        return null;
    }

    /**
     * 签署失败
     * @param invoiceReceiptId
     * @param errorMsg
     */
    public void signError(Long invoiceReceiptId,String errorMsg){

    }
}
