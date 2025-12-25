package com.wishare.finance.domains.voucher.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/15 14:22
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("预制凭证")
public class PrefabricationVoucher {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = false)
    private Long id;

    /**
     * 账单编号
     */
    @ApiModelProperty(value = "账单编号", required = false)
    private String billNo;

    /**
     * 账单id
     */
    @ApiModelProperty(value = "账单id 默认0", required = false)
    private Long billId = 0L;

    /**
     * 账单类型
     */
    @ApiModelProperty(value = "账单类型 默认0", required = false)
    private Integer billType = 0;

    /**
     * 凭证类别
     */
    @ApiModelProperty(value = "凭证类别", required = true)
    private String voucherType;

    /**
     * 账簿id
     */
    @ApiModelProperty(value = "账簿id", required = true)
    private Long accountBookId;


    /**
     * 账簿code
     */
    @ApiModelProperty(value = "账簿code", required = true)
    private String accountBookCode;

    /**
     * 制单日期
     */
    @ApiModelProperty(value = "制单日期", required = true)
    private String time;

    /**
     * 分录详情
     */
    @ApiModelProperty(value = "分录详情", required = true)
    private List<VoucherDetail> details;

    /**
     * 制单人
     */
    @ApiModelProperty(value = "制单人", required = true)
    private String maker;

}
