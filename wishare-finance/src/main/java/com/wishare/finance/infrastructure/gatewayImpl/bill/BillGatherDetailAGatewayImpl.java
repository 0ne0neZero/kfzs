package com.wishare.finance.infrastructure.gatewayImpl.bill;

import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.domains.bill.aggregate.BillCarryoverA;
import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.consts.enums.CarryoverTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.gateway.bill.BillGatherDetailAGateway;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * BillGatherDetailA领域网关实现
 *
 * @author fengxiaolin
 * @date 2023/4/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BillGatherDetailAGatewayImpl<B extends Bill> implements BillGatherDetailAGateway<B> {

    private final BillCarryoverRepository billCarryoverRepository;

    private final AdvanceBillRepository advanceBillRepository;

    private final GatherDetailRepository gatherDetailRepository;

    private final BillCarryoverDetailRepository billCarryoverDetailRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBillGatherDetailA(BillGatherDetailA<B> billGatherDetailA) {
        if (Objects.isNull(billGatherDetailA)) {
            return false;

        }
        Long carryoverId = null;
        Content advanceContent = null;
//        Content carryoverContent = null;
        Content carryDeductionContent = null;
        if (Objects.nonNull(billGatherDetailA.getDiscountBillCarryoverA())) {
            // 抵扣逻辑
            advanceBillRepository.updateBatchById(Lists.newArrayList(billGatherDetailA.getUpdateAdvanceBill()));
            if (billGatherDetailA.getDiscountBillCarryoverA().getCarryoverAmount()>0L){
                billCarryoverRepository.save(billGatherDetailA.getDiscountBillCarryoverA());
                // 添加结转详情信息
                saveCarryoverDetail(billGatherDetailA.getDiscountBillCarryoverA(),billCarryoverDetailRepository);
                carryDeductionContent = new Content().option(new ContentOption(new PlainTextDataItem("账单结转", true)))
                        .option(new ContentOption(new PlainTextDataItem("结转方式为：" + CarryoverTypeEnum.抵扣, true)))
                        .option(new ContentOption(new PlainTextDataItem(CarryoverTypeEnum.抵扣 + "金额为：", false)))
                        .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(
                                billGatherDetailA.getDiscountBillCarryoverA().getCarryoverAmount()), false),
                                OptionStyle.normal()))
                        .option(new ContentOption(new PlainTextDataItem("元", false)));
            }
            carryoverId = billGatherDetailA.getDiscountBillCarryoverA().getId();
        }else {
            // 支付逻辑
            if (Objects.nonNull(billGatherDetailA.getCarryoverAdvanceBillCarryoverA())) {
                BillCarryoverA<B, AdvanceBill> carryoverA = billGatherDetailA.getCarryoverAdvanceBillCarryoverA();
                 // 支付转预收逻辑
                advanceBillRepository.save(billGatherDetailA.getSaveAdvanceBill());
//                billCarryoverRepository.save(carryoverA);
//                carryoverId = billGatherDetailA.getCarryoverAdvanceBillCarryoverA().getId();
                // 添加结转详情信息
//                saveCarryoverDetail(carryoverA,billCarryoverDetailRepository);
//                carryoverContent =  new Content()
//                        .option(new ContentOption(
//                                new PlainTextDataItem("结转方式为：" + CarryoverTypeEnum.结转预收, true))
//                        )
//                        .option(new ContentOption(
//                                new PlainTextDataItem(CarryoverTypeEnum.结转预收 + "金额为：", false))
//                        )
//                        .option(new ContentOption(
//                                        new PlainTextDataItem(
//                                                AmountUtils.toStringAmount(
//                                                        carryoverA.getCarryoverAmount()), false
//                                        ),
//                                        OptionStyle.normal()
//                                )
//                        )
//                        .option(new ContentOption(new PlainTextDataItem("元", false)));

                advanceContent = new Content()
                        .option(new ContentOption(new PlainTextDataItem("多缴费生成", true)))
                        .option(
                                new ContentOption(new PlainTextDataItem("收款金额为：", false))
                        )
                        .option(
                                new ContentOption(
                                        new PlainTextDataItem(
                                                AmountUtils.toStringAmount(carryoverA.getCarryoverAmount()),
                                                false
                                        ),
                                        OptionStyle.normal()
                                )
                        )
                        .option(new ContentOption(new PlainTextDataItem("元", true)));
            }
        }
        boolean saveResult = true;
        // 保存收款详情信息
        GatherDetail gatherDetail = billGatherDetailA.getGatherDetail();
        if (Objects.nonNull(gatherDetail)){
            gatherDetail.setBillCarryoverId(carryoverId);
        }
        // 保存转预收的gatherDetail
        BillCarryoverA<B, AdvanceBill> carryoverAdvanceBillCarryoverA =
                billGatherDetailA.getCarryoverAdvanceBillCarryoverA();
        if (Objects.nonNull(carryoverAdvanceBillCarryoverA) && Objects.nonNull(carryoverAdvanceBillCarryoverA.getAdvanceBillSettle())) {
            if (TenantUtil.bf2()){
                // 中交环境多缴转预收为预收账户费项
                carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setChargeItemId(168197507609306L);
                carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setChargeItemName("预收账户");
            }
            gatherDetailRepository.save(carryoverAdvanceBillCarryoverA.getAdvanceBillSettle());
        }

        if (Objects.nonNull(gatherDetail) && gatherDetail.getPayAmount() != 0) {
             gatherDetailRepository.save(gatherDetail);
        }

        if (Objects.nonNull(gatherDetail) && Objects.nonNull(gatherDetail.getRecBillId()) && gatherDetail.getPayAmount() != 0) {
            SettleChannelEnum settleChannelEnum = SettleChannelEnum.valueOfByCode(gatherDetail.getPayChannel());
            Content content = new Content().option(new ContentOption(new PlainTextDataItem("收款方式： " +
                    settleChannelEnum.getValue(), true)));
            if (SettleChannelEnum.结转.equals(settleChannelEnum)) {
                content.option(new ContentOption(new PlainTextDataItem("结转账单编码：", false)))
                        .option(new ContentOption(
                                new PlainTextDataItem(billGatherDetailA.getUpdateAdvanceBill().getBillNo(), true)
                        ));
            }
            content.option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
                    .option(new ContentOption(
                            new PlainTextDataItem(
                                    AmountUtils.toStringAmount(gatherDetail.getPayAmount()), false),
                            OptionStyle.normal()
                    ))
                    .option(new ContentOption(new PlainTextDataItem("元", false)));
            BizLog.initiate(String.valueOf(gatherDetail.getRecBillId()),
                    LogContext.getOperator(), LogObject.账单, LogAction.收款, content);
        }

        if (Objects.nonNull(advanceContent)) {
            BizLog.normal(String.valueOf(billGatherDetailA.getSaveAdvanceBill().getId()), LogContext.getOperator(),
                LogObject.账单, LogAction.生成, advanceContent);
        }

//        if (Objects.nonNull(carryoverContent)) {
//            // 主账单操作
//            BizLog.initiate(String.valueOf(billGatherDetailA.getBill().getId()),
//                    LogContext.getOperator(), LogObject.账单, LogAction.结转, carryoverContent);
//        }

        if (Objects.nonNull(carryDeductionContent)) {
            BizLog.normal(String.valueOf(billGatherDetailA.getUpdateAdvanceBill().getId()), LogContext.getOperator(),
                    LogObject.账单, LogAction.结转, carryDeductionContent);
        }

        return true;
    }

    /**
     * 添加结转详情数据
     *
     * @param billCarryoverA            结转单
     * @param carryoverDetailRepository carryoverDetailRepository
     */
    private void saveCarryoverDetail(BillCarryoverE billCarryoverA, BillCarryoverDetailRepository carryoverDetailRepository) {
        List<BillCarryoverDetailE> resultList = Lists.newArrayList();
        for (CarryoverDetail carryoverDetail : billCarryoverA.getCarryoverDetail()) {
            BillCarryoverDetailE billCarryoverDetailE = new BillCarryoverDetailE();
            billCarryoverDetailE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CARRYOVER_DETAIL));
            billCarryoverDetailE.setBillType(billCarryoverA.getBillType());
            billCarryoverDetailE.setCarriedBillId(billCarryoverA.getCarriedBillId());
            billCarryoverDetailE.setCarriedBillNo(billCarryoverA.getCarriedBillNo());
            billCarryoverDetailE.setTargetBillId(carryoverDetail.getTargetBillId());
            billCarryoverDetailE.setTargetBillNo(carryoverDetail.getTargetBillNo());
            billCarryoverDetailE.setCarryoverType(billCarryoverA.getCarryoverType());
            billCarryoverDetailE.setCarryoverAmount(carryoverDetail.getActualCarryoverAmount());
            billCarryoverDetailE.setCarryoverTime(billCarryoverA.getCarryoverTime());
            billCarryoverDetailE.setBillCarryoverId(billCarryoverA.getId());
            billCarryoverDetailE.setRemark(billCarryoverA.getRemark());
            resultList.add(billCarryoverDetailE);
        }
        carryoverDetailRepository.saveBatch(resultList);
    }
}
