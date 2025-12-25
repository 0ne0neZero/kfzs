package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 账单详情信息
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class BillDetailDto<B extends Bill> {

    /**
     * 账单信息
     */
    private B bill;
    /**
     * 调整信息记录
     */
    private List<BillAdjustE> billAdjustDtos;
    /**
     * 结算信息记录
     */
    private List<BillSettleDto> billSettleDtos;
    /**
     * 结转信息记录
     */
    private List<BillCarryoverE> billCarryoverDtos;
    /**
     * 申请信息记录
     */
    private List<BillApproveE> approves;
    /**
     * 退款信息记录
     */
    private List<BillRefundE> billRefundDtos;
//    /**
//     * 冻结类型（0：无类型，1：通联银行代扣）
//     */
//    private Integer freezeType;

    /**
     * 应收违约金, (单位： 分)
     */
    private Long receivableOverdueAmount = 0L;

    /**
     * 实收违约金 (单位： 分)
     */
    private Long actualPayOverdueAmount = 0L;

    /**
     * 减免违约金 (单位： 分)
     */
    private Long deductionOverdueAmount = 0L;

    /**
     * 未收违约金 (单位： 分)
     */
    private Long notReceivedOverdueAmount = 0L;


}
