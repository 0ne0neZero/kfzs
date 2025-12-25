package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/29 19:09
 * @descrption:
 */
@Data
@ApiModel(value = "监控信息返回实体")
public class MonitorResV {

    @ApiModelProperty(value = "发票类型代码")
    private String invoiceTypeCode;

    @ApiModelProperty(value = "开票截止日期")
    private String billingDeadline;

    @ApiModelProperty(value = "数据报送起始日期")
    private String dataSubmitStartDate;

    @ApiModelProperty(value = "数据报送终止日期")
    private String dataSubmitEndDate;

    @ApiModelProperty(value = "单张发票正数开具金额限额")
    private String oneQuota;

    @ApiModelProperty(value = "正数发票开具合计限额")
    private String positiveTotalQuota;

    @ApiModelProperty(value = "负数发票开具合计限额")
    private String negativeTotalQuota;

    @ApiModelProperty(value = "负数发票标志")
    private String negativeInvoiceFalg;

    @ApiModelProperty(value = "负数发票限定天数")
    private String negativeLimitDay;

    @ApiModelProperty(value = "数据报送最新日期")
    private String dataSubmitRecentDate;

    @ApiModelProperty(value = "当前时钟")
    private String currentClock;

    @ApiModelProperty(value = "服务器的工作状态")
    private String usingStatu;

    @ApiModelProperty(value = "上传截止日期")
    private String uploadDeadline;

    @ApiModelProperty(value = "离线限定功能标识")
    private String offlineFlag;

    @ApiModelProperty(value = "开票离线开票的时长(单位:小时)")
    private String offlineBillingTime;

    @ApiModelProperty(value = "离线开票张数")
    private String offlineBillingQuantity;

    @ApiModelProperty(value = "离线开具正数发票合计限额")
    private String offlinePositiveTotalQuota;

    @ApiModelProperty(value = "离线开具负数发票合计限额")
    private String offlineNegativeTotalQuota;

    @ApiModelProperty(value = "机器编号")
    private String machineNo;

    @ApiModelProperty(value = "离线扩展信息")
    private String offlineExtendInfo;
}
