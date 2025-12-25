package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/1/12
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单已经生成月份数据")
public class ChargeGenerateDto {

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("房间id")
    private Long roomId;

    @ApiModelProperty("房间名称")
    private String roomName;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("账单计费开始时间")
    private String startTime;
}
