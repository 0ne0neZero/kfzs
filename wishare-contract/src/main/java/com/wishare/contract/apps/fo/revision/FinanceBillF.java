package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "财务账单同步空间资源实体类")
public class FinanceBillF {
    @ApiModelProperty(value = "操作类型 1： 新增 2：变更 3: 废除")
    private Integer operator;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同 ID")
    private String agreementId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同编码")
    private String agreementNo;

    /**
     * 财务账单 ID
     */
    @ApiModelProperty(value = "财务账单 ID")
    private String agreementFinBillId;
    /**
     * 必填
     */
    @ApiModelProperty(value = "项目 pj 码")
    private String projectCode;

    /**
     * 必填
     */
    @ApiModelProperty(value = "应收金额（含税）,以分计数")
    private BigDecimal amount;

    /**
     * 必填
     */
    @ApiModelProperty(value = "我司分成比例，以 0.01%计数")
    private BigDecimal companyRate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "业主分层比例")
    private BigDecimal ownerRate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "费用名称")
    private String feeName;

    /**
     * 必填
     */
    @ApiModelProperty(value = "费用名称配置 id")
    private String feeNameConfigInfoId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "计划收款日期")
    private LocalDate firstStartDate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "计费开始日期")
    private LocalDate startDate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "计费结束日期")
    private LocalDate endDate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "收款周期类型：0、按年，1、按半年，2、按季度，3、按月，4、按单次")
    private Integer fundPeriodType;

    /**
     * 必填
     */
    @ApiModelProperty(value = "收入关系：0、计收入类，1、不计入收入类")
    private Integer incomeType;

    /**
     * 必填
     */
    @ApiModelProperty(value = "分成类型：0、按收入，1、按受益，2、不分成")
    private Integer percentType;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 必填
     */
    @ApiModelProperty(value = "税率,以 0.01%计数")
    private BigDecimal taxRate;
}
