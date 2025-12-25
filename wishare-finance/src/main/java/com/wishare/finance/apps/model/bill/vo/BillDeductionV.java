package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yangzhi
 * @date 2023/05/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("扣款信息")
public class BillDeductionV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("扣款金额")
    private Long deductionAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID", required = true)
    private String creator;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private String creatorName;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID", required = true)
    private String operator;

    @ApiModelProperty(value = "修改人姓名", required = true)
    private String operatorName;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime gmtModify;


}
