package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/1
 */
@Getter
@Setter
@ApiModel("BPM凭证流程统一响应")
public class ProcessVoucherResultV {

    @ApiModelProperty(value = "交易订单信息", required = true)
    List<TransactInfoV> transactInfos;

    @ApiModelProperty(value = "BPM业务系统交易单号", required = true)
    private String businessId;

    @ApiModelProperty(value = "推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败", required = true)
    private Integer voucherState;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;

    @ApiModelProperty(value = "状态码：0成功，1失败")
    private String state;

}
