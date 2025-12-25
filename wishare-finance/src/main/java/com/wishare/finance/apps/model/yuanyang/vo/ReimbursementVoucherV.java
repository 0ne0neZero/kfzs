package com.wishare.finance.apps.model.yuanyang.vo;

import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报销凭证信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/19
 */
@Getter
@Setter
public class ReimbursementVoucherV {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "报账凭证编号")
    private String voucherNo;

    @ApiModelProperty(value = "同步系统凭证编号")
    private String syncSystemVoucherNo;

    @ApiModelProperty(value = "凭证类别： 1记账凭证")
    private Integer voucherType;

    @ApiModelProperty(value = "账簿id")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码")
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "推凭金额 单位：分")
    private Long amount;

    @ApiModelProperty(value = "分录详情")
    private List<VoucherDetailOBV> details;

    @ApiModelProperty(value = "会计期间")
    private LocalDate fiscalPeriod;

    @ApiModelProperty(value = "同步系统: 1用友NCC")
    private Integer syncSystem;

    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "制单人id")
    private String makerId;

    @ApiModelProperty(value = "制单人名称")
    private String makerName;

    @ApiModelProperty(value = "推凭失败原因")
    private String errorReason;

}
