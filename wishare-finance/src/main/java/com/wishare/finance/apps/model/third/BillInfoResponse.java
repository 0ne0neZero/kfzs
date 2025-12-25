package com.wishare.finance.apps.model.third;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description: 账单列表信息
 * @author: luguilin
 * @date: 2025-02-05 10:40
 **/
@Data
public class BillInfoResponse {

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目PJ码")
    private String communityCode;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "单元ID")
    private String unitId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "付款方ID")
    private String payerId;

    @ApiModelProperty("收费单元id")
    private String cpUnitId;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("费项收费开始时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("费项收费结束时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("账单归属月")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime accountDate;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("违约金金额")
    private Long overdueAmount;

    @ApiModelProperty("实收减免金额")
    private Long discountAmount;

    @ApiModelProperty("结算金额")
    private Long settleAmount;

    @ApiModelProperty("退款金额 (单位： 分)")
    private Long refundAmount;

    @ApiModelProperty("结转金额 (单位： 分)")
    private Long carriedAmount;

    @ApiModelProperty("账单状态（0正常，1作废，2冻结，3挂账）")
    private Integer state;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty("是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty("结转状态：0未结转，1待结转，2部分结转，3已结转")
    private Integer carriedState;

    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty("账单标识（空.无标识 1.冲销标识）")
    private Integer billLabel;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private Integer taxRate;

    @ApiModelProperty(value = "账单创建")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("是否可催缴：0不可催缴，1可催缴")
    private Integer reminder;

    @ApiModelProperty(value = "自定义项8")
    private String extField8;

    @ApiModelProperty(value = "税额-新")
    private BigDecimal taxAmountNew;

}
