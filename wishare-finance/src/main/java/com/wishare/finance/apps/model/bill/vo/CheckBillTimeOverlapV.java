package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author fxl
 * @describe
 * @date 2024/8/2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("检查应收账单时间是否重叠VO")
public class CheckBillTimeOverlapV {

    @ApiModelProperty(value = "是否与已有的账单重叠 true ：存在重叠账单， false : 不存在重叠账单")
    private Boolean isOverlap;

    @ApiModelProperty(value = "重叠账单id")
    private Long id;

    @ApiModelProperty(value = "重叠账单编号")
    private String billNo;

    @ApiModelProperty(value = "roomId")
    private String roomId;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    public static CheckBillTimeOverlapV noOverlap() {
        CheckBillTimeOverlapV checkBillTimeOverlapV = new CheckBillTimeOverlapV();
        checkBillTimeOverlapV.setIsOverlap(false);
        return checkBillTimeOverlapV;
    }
}
