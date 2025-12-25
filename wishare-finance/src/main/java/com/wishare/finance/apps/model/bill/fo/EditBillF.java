package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/2/10
 * @Description:
 */
@Getter
@Setter
@ApiModel("编辑应收账单")
public class EditBillF {


    @ApiModelProperty(value = "应收账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty("账单金额(单位：分)")
    private Long totalAmount;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;
}