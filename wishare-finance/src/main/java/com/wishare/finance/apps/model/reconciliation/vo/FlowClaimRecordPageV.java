package com.wishare.finance.apps.model.reconciliation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 分页获取流水领用记录返回数据
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("分页获取流水领用记录返回数据")
public class FlowClaimRecordPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("流水批次号")
    private String serialNumber;

    @ApiModelProperty("认领金额")
    private Long claimAmount;

    @ApiModelProperty("认领日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime claimDate;

    @ApiModelProperty("系统来源:1收费系统，2合同系统")
    private Integer sysSource;

    @ApiModelProperty("本方账户")
    private String ourAccount;

    @ApiModelProperty("本方名称")
    private String ourName;

    @ApiModelProperty("本方开户行")
    private String ourBank;

    @ApiModelProperty("交易平台")
    private String tradingPlatform;

    @ApiModelProperty("认领人")
    private String creatorName;

}
