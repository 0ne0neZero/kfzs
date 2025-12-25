package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author zyj
 * @date 2023/7/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("银行签约对应编辑应收账单")
public class EditBillForBankSignF {


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

    @ApiModelProperty("账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty(value = "扩展字段1(违约金支付状态)")
    private String extField1;

    @ApiModelProperty(value = "扩展字段2(违约金开始时间)")
    private String extField2;

    @ApiModelProperty(value = "扩展字段5(违约金比率)")
    private String extField5;

    /**
     * 是否是违约金：0-否/1-是
     */
    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue;

    /**
     * 冻结类型（0：无类型，1：通联银行代扣）
     */
    @ApiModelProperty(value = "冻结类型（0：无类型，1：通联银行代扣）")
    private Integer freezeType;
}