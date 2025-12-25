package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.model.VoucherDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
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
public class PrefabricationVoucherSaveF {

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
    @ApiModelProperty(value = "凭证类别", required = false)
    private String voucherType;

    /**
     * 账簿id
     */
    @ApiModelProperty(value = "账簿id", required = true)
    private Long accountBookId;

    /**
     * 制单日期
     */
    @ApiModelProperty(value = "制单日期", required = true)
    private LocalDate time;

    /**
     * 分录详情
     */
    private List<VoucherDetail> details;

    /**
     * 制单人
     */
    @ApiModelProperty(value = "制单人", required = true)
    private String maker;

    /**
     * 制单人Id
     */
    @ApiModelProperty(value = "制单人Id", required = true)
    private String makerId;

    /**
     * 推凭系统
     */
    @ApiModelProperty(value = "推凭系统", required = false)
    private int systemCode;

    /**
     * 科目体系id
     */
    @ApiModelProperty(value = "科目体系id", required = true)
    private Long subjectSystemId;

}
