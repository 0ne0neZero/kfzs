package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yyx
 * @date 2023/5/25 16:52
 */
@Getter
@Setter
@ApiModel(value = "导入应收补录账单请求信息")
public class ImportRecordBillF {

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "房号")
    private String roomName;

    @ApiModelProperty(value = "房号Id")
    private String roomId;

    @ApiModelProperty(value = "收费费项ID")
    private Long chargeItemId;

    @ApiModelProperty(value = "收费费项")
    private String chargeItemName;

    @ApiModelProperty(value = "账单开始日期")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束日期")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "收款金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "收款方式")
    private String payChannel;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "收费对象")
    private String spaceChargeObj;

    @ApiModelProperty
    private String payeeId;

    @ApiModelProperty(value = "收款人")
    private String payeeName;

    @ApiModelProperty(value = "应收减免金额(分)")
    private Long deductibleAmount;

    @ApiModelProperty(value = "实收减免金额(分)")
    private Long discountAmount;

    @ApiModelProperty(value = "行号标识", required = true)
    private Long index;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;


}
