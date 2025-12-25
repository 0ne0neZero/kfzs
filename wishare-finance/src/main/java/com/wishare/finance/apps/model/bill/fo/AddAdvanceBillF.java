package com.wishare.finance.apps.model.bill.fo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建预收账单请求信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建预收账单请求信息")
@Valid
public class AddAdvanceBillF extends AddBillF{

    @ApiModelProperty(value = "计费方式")
    private Integer billMethod;

    @ApiModelProperty(value = "计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积")
    private Integer billArea;

    @ApiModelProperty("计费数额")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "预收时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "渠道交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "行号")
    private Long rowNumber;

    @ApiModelProperty("优惠金额")
    private Long preferentialAmount = 0L;

    @ApiModelProperty("赠送金额")
    private Long presentAmount = 0L;

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")
    private Integer paySource;

    @ApiModelProperty("空间路径")
    private String path;

    @ApiModelProperty(value = "组合支付信息 方圆定制")
    private List<BillSettleChannelInfo> settleChannelInfos;
}
