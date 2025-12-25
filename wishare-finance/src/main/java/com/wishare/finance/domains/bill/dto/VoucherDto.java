package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证通知参数
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
public class VoucherDto {

    @ApiModelProperty("凭证id")
    private Long voucherId;

    @ApiModelProperty("报账凭证编号")
    private String voucherNo;

    @ApiModelProperty("凭证类别， 默认：记账凭证")
    private String voucherType;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("推凭金额 单位：分")
    private Long amount;

    @ApiModelProperty("推凭状态：0待同步，1成功，2失败")
    private Integer inferenceState;

    @ApiModelProperty("分录详情")
    private List<VoucherEntryDetailDto> details;

    @ApiModelProperty("触发事件类型")
    private Integer evenType;

    @ApiModelProperty("凭证创建时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("制单方式： 自动录制， 手工录制")
    private String madeType;

    @ApiModelProperty("原因")
    private String reason;

}
