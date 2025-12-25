package com.wishare.finance.domains.bill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apis.invoicereceipt.ReceiptApi;
import com.wishare.finance.apps.model.bill.fo.CanInvoiceInfoF;
import com.wishare.finance.apps.model.bill.fo.GatherInvoiceF;
import com.wishare.finance.apps.model.bill.vo.BillSimpleInfoV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchF;
import com.wishare.finance.apps.model.invoice.invoice.fo.ReceiptBatchF;
import com.wishare.finance.apps.service.bill.AllBillAppService;
import com.wishare.finance.apps.service.bill.GatherBillAppService;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.apps.service.invoicereceipt.ReceiptAppService;
import com.wishare.finance.domains.bill.consts.enums.InvoiceFlowMonitorStepTypeEnum;
import com.wishare.finance.domains.bill.entity.InvoiceFlowMonitorE;
import com.wishare.finance.domains.bill.repository.mapper.InvoiceFlowMonitorMapper;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.starter.Global;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ahchoo
 * @Description:
 * @Date: Create in 17:17 2023/12/25
 * @Modified By:
 */
@Slf4j
@Service
public class InvoiceFlowMonitorService extends ServiceImpl<InvoiceFlowMonitorMapper, InvoiceFlowMonitorE> {

    @Autowired
    private AllBillAppService allBillAppService;
    @Autowired
    private GatherBillAppService gatherBillAppService;



    // /**
    //  * 中交定制 开启e签宝，先走开收据再作废，再开票
    //  * D6236 流程,不再走eSign 只需要保留一份默认的收据 2024-04-18
    //  * @param form
    //  */
    // @Deprecated
    // public Long doFlow(InvoiceBatchF form) {
    //     //【{doFlow}】：【{初步(开票、开具)参数留存}】->发起e签宝开具->(定时任务)轮寻签署结果->(定时任务内置)进行发起e签宝作废->(定时任务)轮寻作废结果->(定时任务内置)唤起开票
    //     ReceiptBatchF receiptBatchF = this.getReceiptBatchF(form);
    //     InvoiceFlowMonitorE build = InvoiceFlowMonitorE.builder()
    //             .invoiceParameters(JSON.toJSONString(form))
    //             .receiptParameters(JSON.toJSONString(receiptBatchF))
    //             .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_INIT.getCode())
    //             .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_INIT.getValue())
    //             .build();
    //     //节点记录 【invoice_flow_monitor】
    //     // this.save(build);
    //     // /** 增加流程id */
    //     // receiptBatchF.setInvoiceFlowMonitorId(build.getId());
    //     //【{doFlow}】：【{发起e签宝开具}】->(定时任务)轮寻签署结果->(定时任务内置)进行发起e签宝作废->(定时任务)轮寻作废结果->(定时任务内置)唤起开票
    //     Long receiptId = Global.ac.getBean(ReceiptAppService.class).invoiceBatch(receiptBatchF);
    //     //节点记录 [invoice_flow_monitor]
    //     this.updateById(InvoiceFlowMonitorE.builder().id(build.getId())
    //             .stepType(InvoiceFlowMonitorStepTypeEnum.STEP_SIGN.getCode())
    //             .stepDescription(InvoiceFlowMonitorStepTypeEnum.STEP_SIGN.getValue())
    //             .receiptId(receiptId).build());
    //     return receiptId;
    // }

    /**
     * 中交定制 开启e签宝，先走开收据再作废，再开票
     * D6236 流程,不再走eSign 只需要保留一份默认的收据 2024-04-18
     * @param form
     */
    public Long doFlow(final InvoiceBatchF form) {
        //组装开收据参数
        ReceiptBatchF receiptBatchF = this.getReceiptBatchF(form);
        Long receiptId = Global.ac.getBean(ReceiptAppService.class).invoiceBatch(receiptBatchF);
        //发起开票
        Global.ac.getBean(InvoiceAppService.class).invoiceBatch(form);
        return receiptId;
    }





    /**
     * 进行发起收据请求参数组装
     * @param form
     * @return
     */
    private ReceiptBatchF getReceiptBatchF(InvoiceBatchF form){
        Long canInvoiceAmount;
        if (Objects.isNull(form.getGatherBillType())) {
            CanInvoiceInfoF canInvoiceInfoF = new CanInvoiceInfoF();
            canInvoiceInfoF.setBillIds(form.getBillIds());
            canInvoiceInfoF.setBillType(form.getBillType());
            canInvoiceInfoF.setSupCpUnitId(form.getSupCpUnitId());
            canInvoiceInfoF.setInvoiceType(InvoiceLineEnum.电子收据.getCode());
            BillSimpleInfoV billSimpleInfoV = allBillAppService.getBillSimpleInfoV(canInvoiceInfoF);
            canInvoiceAmount = billSimpleInfoV.getCanInvoiceAmount();
        } else {
            GatherInvoiceF gatherInvoiceF = getGatherInvoiceF(form);
            BillSimpleInfoV billSimpleInfoV = gatherBillAppService.canInvoiceInfo(gatherInvoiceF);
            canInvoiceAmount = billSimpleInfoV.getCanInvoiceAmount();
        }
        ReceiptBatchF receiptBatchF = Global.mapperFacade.map(form, ReceiptBatchF.class);
        /**中交流程不再走eSign 标识需要开票 开收据不对任何数据产生变动只会生产收据信息*/
        receiptBatchF.setInvoiceFlowMonitorId(0L);
        receiptBatchF.setPriceTaxAmount(canInvoiceAmount);
        receiptBatchF.setType(InvoiceLineEnum.电子收据.getCode());
        /**是否签章 0是 1否 */
        receiptBatchF.setSignStatus(1);
        /**不推送*/
        receiptBatchF.setPushMode(List.of(-1));
        /**是否跳过 中交有当前标识 则只从数据源获取数据不对数据源做任何变动改动*/
        receiptBatchF.setSkip(true);
        return receiptBatchF;
    }

    @NotNull
    private static GatherInvoiceF getGatherInvoiceF(InvoiceBatchF form) {
        GatherInvoiceF gatherInvoiceF = new GatherInvoiceF();
        gatherInvoiceF.setGatherBillType(form.getGatherBillType());
        if (form.getGatherBillType().equals(0)) {
            gatherInvoiceF.setGatherBillId(form.getGatherBillIds().get(0));
        } else {
            gatherInvoiceF.setGatherDetailId(form.getGatherDetailBillIds().get(0));
        }
        gatherInvoiceF.setSupCpUnitId(form.getSupCpUnitId());
        gatherInvoiceF.setInvoiceType(InvoiceLineEnum.电子收据.getCode());
        return gatherInvoiceF;
    }


}
