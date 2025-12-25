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
 * 查询最大结束时间账单信息
 *
 * @author zhenghui
 */
@Setter
@Getter
@ApiModel("查询最大结束时间账单信息")
public class ReceivableMaxEndTimeV {


    @ApiModelProperty("费项收费结束时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("房号id")
    private String roomId;

}
