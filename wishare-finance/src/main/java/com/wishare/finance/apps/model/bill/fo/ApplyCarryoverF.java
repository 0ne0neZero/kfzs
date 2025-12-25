package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单转结请求信息
 *
 * @Author dxclay
 * @Date 2022/9/9
 * @Version 1.0
 */
@Getter
@Setter
public class ApplyCarryoverF {

    @ApiModelProperty(value = "结转账单id", required = true)
    @NotNull(message = "结转账单id不能为空")
    private Long carriedBillId;

    @ApiModelProperty(value = "结转额度", required = true)
    @NotNull(message = "结转额度金额不能为空")
    private Long carriedAmount;

    @ApiModelProperty(value = "被结转账单列表", required = true)
    @Valid
    @Size(min = 1, message = "被结转账单列表不能为空")
    private List<CarryInfo> targetBills;

    @ApiModelProperty(value = "结转方式：1抵扣，2结转预收", required = true)
    @NotNull(message = "结转方式不能为空")
    private Integer carryoverType;

    @ApiModelProperty(value = "结转附件文件路径")
    private List<String> fileUrl;

    @ApiModelProperty(value = "申请结转原因")
    private String applyReason;

    @ApiModelProperty(value = "是否结转预收： 0不转预收，1转预收")
    private Integer advanceCarried;

    @Setter
    @Getter
    public static class CarryInfo {

        @ApiModelProperty(value = "被结转账单id（结转方式为抵扣时必传）", required = true)
        private Long targetBillId;

        @ApiModelProperty(value = "结转金额（单位：分）", required = true)
        @NotNull(message = "结转金额不能为空")
        @Min(value = 1, message = "结转金额不能小于1")
        private Long carryoverAmount;

        @ApiModelProperty(value = "费项id")
        private Long chargeItemId;

        @ApiModelProperty(value = "费项名称")
        private String chargeItemName;

        @ApiModelProperty(value = "收费开始时间 (可选字段，存在收费周期的账单必传)")
        private LocalDateTime chargeStartTime;

        @ApiModelProperty(value = "收费结束时间 (可选字段，存在收费周期的账单必传)")
        private LocalDateTime chargeEndTime;

    }

}
