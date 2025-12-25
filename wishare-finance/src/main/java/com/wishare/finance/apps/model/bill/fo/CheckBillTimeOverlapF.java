package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fxl
 * @describe 检查应收账单时间是否重叠form
 * @date 2024/8/2
 */
@Getter
@Setter
@ApiModel("检查应收账单时间是否重叠form")
public class CheckBillTimeOverlapF {

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "费项id")
    @NotNull(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "账单开始时间")
    @NotNull(message = "账单开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    @NotNull(message = "账单结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "房号ID")
    private List<String> roomIds;
}
