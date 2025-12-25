package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间应收账单返回信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("房间应收账单返回信息")
public class ReceivableBillsV {

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty("费项收费开始时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("费项收费结束时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount;

    @ApiModelProperty("实收减免金额  (单位： 分)")
    private Long discountAmount;

    @ApiModelProperty("已结转金额 (单位： 分)")
    private Long carriedAmount;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("账单信息")
    private List<ReceivableBillV> billList;

    @ApiModelProperty(value = "bpm跳收状态：0正常，1跳收审核中，2跳收审核通过3跳收拒绝")
    private Integer jumpState;

    @Getter
    @Setter
    @ApiModel("账单信息")
    public static class ReceivableBillV{

        @ApiModelProperty("账单id")
        private Long id;

        @ApiModelProperty("账单编号")
        private String billNo;

        @ApiModelProperty("法定单位id")
        private Long statutoryBodyId;

        @ApiModelProperty("法定单位名称")
        private String statutoryBodyName;

        @ApiModelProperty(value = "项目ID")
        private String communityId;

        @ApiModelProperty(value = "项目名称")
        private String communityName;

        @ApiModelProperty(value = "房号ID")
        private String roomId;

        @ApiModelProperty(value = "房号名称")
        private String roomName;

        @ApiModelProperty(value = "费项id")
        private Long chargeItemId;

        @ApiModelProperty(value = "费项名称")
        private String chargeItemName;

        @ApiModelProperty(value = "收费对象id")
        private String targetObjId;

        @ApiModelProperty(value = "收费对象名字")
        private String targetObjName;

        @ApiModelProperty(value = "收费对象电话")
        private String phone;

        @ApiModelProperty(value = "支付状态（0未支付，1部分支付，2已支付）")
        private String payState;

        @ApiModelProperty("费项收费开始时间 格式：yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startTime;

        @ApiModelProperty("费项收费结束时间 格式：yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endTime;

        @ApiModelProperty("币种(货币代码)（CNY:人民币）")
        private String currency;

        @ApiModelProperty("账单金额")
        private Long totalAmount;

        @ApiModelProperty("应收金额  (单位： 分)")
        private Long receivableAmount;

        @ApiModelProperty("应收减免金额  (单位： 分)")
        private Long deductibleAmount;

        @ApiModelProperty("实收减免金额  (单位： 分)")
        private Long discountAmount;

        @ApiModelProperty("已结转金额 (单位： 分)")
        private Long carriedAmount;

        @ApiModelProperty("实际缴费金额")
        private Long actualPayAmount;

        @ApiModelProperty("实际应缴金额")
        private Long actualUnpayAmount;

        @ApiModelProperty("账单已缴时间 格式：yyyy-MM-dd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
        private LocalDateTime chargeTime;

        @ApiModelProperty("计费方式(1:单价面积/月，2:单价/月，3:单价面积/天，4:单价/天)")
        private Integer billMethod;

        @ApiModelProperty("计费面积(单位：m²)")
        private BigDecimal chargingArea;

        @ApiModelProperty("单价（单位：分）")
        private BigDecimal unitPrice;

        @ApiModelProperty("退款金额(单位： 分)")
        private Long refundAmount;

        @ApiModelProperty("退款状态 （0未退款，1退款中，2部分退款，3已退款）")
        private Integer refundState;

        @ApiModelProperty(value = "bpm跳收状态：0正常，1跳收审核中，2跳收审核通过3跳收拒绝")
        private Integer jumpState;

        @ApiModelProperty("付款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
        private Integer payerType;

        @ApiModelProperty("收费对象属性（1个人，2企业）")
        private Integer payerLabel;

    }

}
