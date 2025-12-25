package com.wishare.finance.domains.bill.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
 * @date 2022/9/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("应收账单退款信息")
public class ReceivableBillRefundDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("退款记录id")
    private Long id;

    @ApiModelProperty("退款编号")
    private String refundNo;

    @ApiModelProperty("第三方流水号")
    private String outRefundNo;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("账单来源")
    private String source;

    @ApiModelProperty("附件")
    private String fileInfo;

    @ApiModelProperty("附件")
    private List<FileVo> fileVos;

    @ApiModelProperty("付款方ID")
    private String payerId;

    @ApiModelProperty("付款方名称")
    private String payerName;

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty("退款原因")
    private String remark;

    @ApiModelProperty("退款操作人")
    private String creatorName;

    // 转化json附件
    public List<FileVo> getFileVos() {
        return Optional.ofNullable(fileInfo)
                .map(info -> JSON.parseArray(info, FileVo.class))
                .orElse(null);
    }
}
