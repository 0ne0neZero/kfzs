package com.wishare.finance.domains.bill.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 结转明细
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class CarryoverDetail {

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单）")
    private Integer billType;

    @ApiModelProperty(value = "被结转账单id")
    private Long targetBillId;

    @ApiModelProperty(value = "被结转账单编号")
    private String targetBillNo;

    @ApiModelProperty(value = "实际结转金额")
    private Long actualCarryoverAmount;

    @ApiModelProperty(value = "结转金额")
    private Long carryoverAmount;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;
}
