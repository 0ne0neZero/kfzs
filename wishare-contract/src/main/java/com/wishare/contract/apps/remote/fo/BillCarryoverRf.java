package com.wishare.contract.apps.remote.fo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("账单结转入参")
public class BillCarryoverRf {

    @ApiModelProperty(value = "结转账单id", required = true)
    @NotNull(message = "结转账单id不能为空")
    private Long carriedBillId;

    @ApiModelProperty(value = "结转金额", required = true)
    @NotNull(message = "结转金额不能为空")
    private Long carryoverAmount;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty(value = "结转方式：1抵扣，2结转预收", required = true)
    @NotNull(message = "结转方式不能为空")
    private Integer carryoverType;

    @ApiModelProperty(value = "结转附件文件路径")
    private List<String> fileUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否结转预收： 0不转预收，1转预收")
    private Integer advanceCarried;

    @ApiModelProperty(value = "结转详情")
    private List<CarryoverDetail> carryoverDetail;

    @Setter
    @Getter
    public static class CarryoverDetail {

        @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单）")
        private Integer billType;

        @ApiModelProperty(value = "被结转账单id")
        private Long targetBillId;

        @ApiModelProperty(value = "被结转账单编号")
        private String targetBillNo;

        @ApiModelProperty(value = "结转金额")
        private Long carryoverAmount;

        @ApiModelProperty(value = "费项id")
        private Long chargeItemId;

        @ApiModelProperty(value = "费项名称")
        private String chargeItemName;

        @ApiModelProperty(value = "收费开始时间")
        private LocalDateTime chargeStartTime;

        @ApiModelProperty(value = "收费结束时间")
        private LocalDateTime chargeEndTime;

    }
}
