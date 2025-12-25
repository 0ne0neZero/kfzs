package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hhb
 * @describe
 * @date 2025/10/16 16:58
 */
@Data
public class PayNkSettlementV {
    @ApiModelProperty(value = "结算单ID")
    private String id;
    @ApiModelProperty(value = "结算单标题")
    private String title;
    @ApiModelProperty(value = "结算单编号")
    private String payFundNumber;
    @ApiModelProperty(value = "应结算金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty(value = "实际结算金额")
    private BigDecimal actualSettlementAmount;
    @ApiModelProperty(value = "计量周期")
    private String periodStr;
    @ApiModelProperty(value = "序号")
    private Integer sort;
    @ApiModelProperty(value = "与上期实际结算金额差额")
    private BigDecimal differenceSettlementAmount;
    @ApiModelProperty(value = "与上期实际结算金额差额是否大于10%")
    private Boolean isLessThanTenPercentOfThe = Boolean.FALSE;
}
