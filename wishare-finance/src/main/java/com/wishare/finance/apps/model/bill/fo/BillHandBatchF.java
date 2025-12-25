package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/29 16:38
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiOperation("交账请求")
public class BillHandBatchF {

    @ApiModelProperty("账单id列表（二选一条件，检索条件为空时使用， 两个条件都不为空时默认使用账单id列表）")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty("账单对应的交账票据列表map数据\n"
        + "{\n"
        + "  \"12225441\": [{\n"
        + "   \"invoiceReceiptId\": \"票据id\",\n"
        + "   \"billId\": \"账单id\",\n"
        + "   \"amount\": 100, //交账金额（分）\n"
        + "   \"description\": \"交账说明\" \n"
        + "  }] \n"
        + "}")
    private String invoiceReceipts;
}
