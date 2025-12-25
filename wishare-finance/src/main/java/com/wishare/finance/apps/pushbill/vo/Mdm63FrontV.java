package com.wishare.finance.apps.pushbill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "mdm63-资金收款单-应收应付筛选对象")
public class Mdm63FrontV {

    @ApiModelProperty(value = "应收应id")
    private String ftId;

    @ApiModelProperty(value = "应收应付标记 AR应收 AP应付")
    private String arapModule;

    @ApiModelProperty(value = "应收应付编号")
    private String billNum;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date bizDate;

    @ApiModelProperty(value = "往来单位编码")
    private String partnerCode;

    @ApiModelProperty(value = "往来单位名称")
    private String partnerName;

    @ApiModelProperty(value = "待核销金额")
    private BigDecimal dhxJe;

    @ApiModelProperty(value = "合同编码-CT码")
    private String contractNo;

    @ApiModelProperty(value = "项目编码-PJ码")
    private String projectInfoCode;

    @ApiModelProperty(value = "项目名称")
    private String projectInfoName;

    @ApiModelProperty(value = "业务科目id")
    private String fundsPropId;

    @ApiModelProperty(value = "业务科目名称")
    private String fundsPropName;

    @ApiModelProperty(value = "应收应付摘要")
    private String summary;

}
