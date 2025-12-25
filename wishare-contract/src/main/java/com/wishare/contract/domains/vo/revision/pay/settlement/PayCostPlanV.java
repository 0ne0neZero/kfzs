package com.wishare.contract.domains.vo.revision.pay.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "支出合同结算单-详情-成本计划信息")
public class PayCostPlanV {

    @ApiModelProperty("成本计划id")
    private String id;

    @ApiModelProperty("成本计划编码")
    private String costPlanCode;

    @ApiModelProperty("应结算金额")
    private BigDecimal plannedSettlementAmount;

    @ApiModelProperty("核销金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("费用开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costStartTime;

    @ApiModelProperty("费用结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costEndTime;

    @ApiModelProperty("合同清单项id")
    private String typeId;

    @ApiModelProperty("合同清单项名称")
    private String type;

    @ApiModelProperty("费项id")
    private String chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItem;

    @ApiModelProperty("归属月")
    @JsonFormat(pattern = "yyyy-MM",timezone = "GMT+8")
    private Date belongMonth;

}


