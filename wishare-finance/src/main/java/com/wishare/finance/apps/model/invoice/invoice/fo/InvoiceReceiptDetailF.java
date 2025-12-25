package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票明细")
public class InvoiceReceiptDetailF {

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty("商品编码（商品税收分类编码开发者自行填写）")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty(value = "单价含税标志：0:不含税,1:含税", required = true)
    @NotNull(message = "单价含税标志不能为空")
    private Integer withTaxFlag;

    @ApiModelProperty("数量")
    private String num;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("单价")
    private String price;

    @ApiModelProperty("规格型号")
    private String spectype;

    @ApiModelProperty("不含税金额")
    private Long taxExcludedAmount;

    @ApiModelProperty(value = "含税金额",required = true)
    @NotNull(message = "账单的含税金额不能为空")
    private Long taxIncludedAmount;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单编号", required = true)
    @NotBlank(message = "账单编号不能为空")
    private String billNo;

    @ApiModelProperty(value = "账单的结算金额",required = true)
    @NotNull(message = "账单的结算金额不能为空")
    private Long settleAmount;

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime billStartTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime billEndTime;
}
