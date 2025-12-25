package com.wishare.finance.domains.voucher.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/15 14:36
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("凭证明细")
public class VoucherDetail {

    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要", required = true)
    private String ruleRemark;

    /**
     * 科目Id
     */
    @ApiModelProperty(value = "科目Id", required = true)
    private Long subjectId;

    /**
     * 科目编码
     */
    @ApiModelProperty(value = "科目编码", required = true)
    private String subjectCode;

    /**
     * 科目名称
     */
    @ApiModelProperty(value = "科目名称", required = true)
    private String subjectName;

    /**
     * 借贷方向 debit 借  credit 贷
     */
    @ApiModelProperty("借贷方向 debit 借  credit 贷")
    private String type;

    /**
     * 借款金额
     */
    @ApiModelProperty("借款金额")
    private BigDecimal debitAmount = BigDecimal.ZERO;

    /**
     * 贷款金额
     */
    @ApiModelProperty("贷款金额")
    private BigDecimal creditAmount = BigDecimal.ZERO;

    /**
     * 辅助核算名称
     */
    @ApiModelProperty(value = "辅助核算名称", required = true)
    private List<SupItem> supItems;
}
