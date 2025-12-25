package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author fengxiaolin
 * @date 2023/6/5
 */
@Setter
@Getter
@ApiModel("查询预收账单最大结束时间")
public class AdvanceMaxEndTimeV {

    @ApiModelProperty("费项收费结束时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
