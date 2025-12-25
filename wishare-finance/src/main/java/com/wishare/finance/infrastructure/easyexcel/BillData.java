package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import lombok.*;

/**
 * @author szh
 * @date 2024/5/7 15:04
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ColumnWidth(25)
public class BillData {

    @ExcelIgnore
    private Long billId;
    @ExcelIgnore
    private Integer billType;
    @ExcelIgnore
    private String communityId;


    @ExcelProperty(value = "单号")
    private String billNo;

    /**
     账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     {@linkplain BillTypeEnum}
    */
    @ExcelProperty(value = "账单类型")
    private String billTypeStr;

    @ExcelProperty(value = "费项")
    private String chargeItemName;

    /**
     * 账单金额
     */
    @ExcelProperty(value = "账单金额（元）")
    private String totalAmountStr;

    @ExcelProperty(value = "实收金额（元）")
    private String actualPayAmountStr;


    @ExcelProperty(value = "开票金额（元）")
    private String invoiceAmountStr;

    /**
     {@linkplain BillSettleStateEnum}
     */
    @ExcelProperty(value = "结算状态")
    private String settleStateStr;

    /**
     {@linkplain BillStateEnum}
     */
    @ExcelProperty(value = "账单状态")
    private String stateStr;


}
