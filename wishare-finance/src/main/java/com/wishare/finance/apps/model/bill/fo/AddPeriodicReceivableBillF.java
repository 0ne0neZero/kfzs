package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("创建周期性应收账单请求信息")
public class AddPeriodicReceivableBillF {

    @ApiModelProperty("法定单位id")
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty(value = "项目ID", required = true)
    @NotBlank(message = "项目ID不能为空")
    private String communityId;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank(message = "项目名称不能为空")
    private String communityName;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "房号ID", required = true)
    @NotBlank(message = "房号ID不能为空")
    private String roomId;

    @ApiModelProperty(value = "房号名称", required = true)
    @NotBlank(message = "房号名称不能为空")
    private String roomName;

    @ApiModelProperty(value = "周期开始时间 格式：yyyy-mm-dd HH:mm:ss", required = true)
    @NotNull(message = "周期开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "周期开始时间 格式：yyyy-mm-dd HH:mm:ss", required = true)
    @NotNull(message = "周期结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @Valid
    @ApiModelProperty(value = "指定周期的账单列表")
    private List<AddReceivableBillF> receivableBills;

}
