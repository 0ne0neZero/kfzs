
package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "导入结果信息")
public class ImportBillDto {

    @ApiModelProperty(value = "账单id",required = true)
    private Long id;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount;

    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmount;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmount;

    @ApiModelProperty("实收金额（实收金额 = 应收金额金额 + 违约金金额 - 优惠总额） (单位： 分)")
    private Long settleAmount;

    @ApiModelProperty(value = "行号下标，导入的行号标识")
    private Integer index;

    @ApiModelProperty("失败原因")
    private String errorMessage;

    @ApiModelProperty(value = "导入结果", required = true)
    private boolean result;

}
