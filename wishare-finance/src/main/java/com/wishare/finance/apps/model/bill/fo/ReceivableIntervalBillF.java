package com.wishare.finance.apps.model.bill.fo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhenghui
 * @date 2023/4/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("应收账单查询区间范围内账单信息")
@AllArgsConstructor
@NoArgsConstructor
public class ReceivableIntervalBillF {

    @ApiModelProperty(value = "上级收费单元id（如:项目）", required = true)
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "房号id")
    private Long roomId;

    @ApiModelProperty(value = "房号id")
    private List<Long> roomIds;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private String chargeItemId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("limitSize")
    private Long limitSize;

    public Long getLimitSize() {
        return CollUtil.isEmpty(roomIds) ? null : roomIds.size() * 100L;
    }
}
