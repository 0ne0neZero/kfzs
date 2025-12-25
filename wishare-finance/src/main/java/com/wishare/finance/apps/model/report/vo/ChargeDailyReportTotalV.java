package com.wishare.finance.apps.model.report.vo;

import com.wishare.finance.domains.report.dto.ChargeDailyReportTotalDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收费日报统计收费项目返回参数
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收费日报统计返回参数")
public class ChargeDailyReportTotalV {

    @ApiModelProperty("收费项目合计")
    private List<ChargeDailyReportTotalDto> chargeItemTotalList;

    @ApiModelProperty("收款方式合计")
    private List<ChargeDailyReportTotalDto> paymentTotalList;
}
