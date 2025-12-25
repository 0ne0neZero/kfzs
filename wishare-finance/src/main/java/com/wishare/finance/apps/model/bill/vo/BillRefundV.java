package com.wishare.finance.apps.model.bill.vo;

import com.alibaba.fastjson.JSON;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author xujian
 * @date 2022/9/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("退款信息")
public class BillRefundV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("申请退款时间")
    private LocalDateTime approveTime;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("退款状态：0待退款，1退款中，2已退款，3未生效")
    private Integer state;

    @ApiModelProperty("交易流水号")
    private String tradeNo;

    /**
     * 外部退款编号（支付宝退款单号，银行流水号等）
     */
    private String outRefundNo;

    @ApiModelProperty("附件")
    private String fileInfo;

    // 转化json附件
    public List<FileVo> getFileVos() {
        return Optional.ofNullable(fileInfo)
                .map(info -> JSON.parseArray(info, FileVo.class))
                .orElse(null);
    }

}
