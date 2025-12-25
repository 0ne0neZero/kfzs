package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询预收率统计表报表返回数据
 *
 * @author yancao
 */
@Setter
@Getter
public class AdvanceRateReportPageDto {

    @ApiModelProperty("组织id")
    private Long costCenterId;

    @ApiModelProperty("组织名称")
    private String costCenterName;

    @ApiModelProperty("楼盘名称")
    private String communityName;

    @ApiModelProperty("房屋编码")
    private String roomCode;

    @ApiModelProperty("房屋id")
    private Long roomId;

    @ApiModelProperty("房屋名称")
    private String roomName;

    @ApiModelProperty("开票类别")
    private String invoiceType;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("饱和应收")
    private Long totalAmount;

    @ApiModelProperty("实收")
    private Long actualPayAmount;

}
