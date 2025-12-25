package com.wishare.finance.domains.bill.aggregate.approve;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustWayEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;

/**
 * 作废审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class InvalidApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        bill.invalid();
        bill.offAccount();
        // 将账单关联的已收款单状态置为删除
        invalidGatherInfos(bill);

        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容：作废账单", true)))
                        .option(new ContentOption(new PlainTextDataItem("作废原因：" + billApprove.getReason(), true))));
    }

    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
    }

    /**
     * 将账单名下的收款单和明细进行删除
     */
    private void invalidGatherInfos(B bill) {
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bill.getId(), bill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(gatherDetails)){
            gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().eq("rec_bill_no",bill.getBillNo()).set("deleted",1)
                    .eq("sup_cp_unit_id",bill.getSupCpUnitId()));
            List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).distinct().collect(
                    Collectors.toList());
            List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds, bill.getSupCpUnitId());
            for (GatherBill gatherBill1 : gatherBillList) {
                List<GatherDetail> gatherDetail1s = gatherDetailRepository.getByGatherBillId(gatherBill1.getId(), gatherBill1.getSupCpUnitId());
                if (CollectionUtils.isEmpty(gatherDetail1s)){
                    gatherBillRepository.update(new UpdateWrapper<GatherBill>().eq("id",gatherBill1.getId()).set("deleted",1)
                            .eq("sup_cp_unit_id",gatherBill1.getSupCpUnitId()));
                }
            }
        }
    }
}
