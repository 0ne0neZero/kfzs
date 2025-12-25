package com.wishare.finance.apps.pushbill.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
public class PayPlanCodeReqV implements Serializable {

    @ApiModelProperty(value = "必传，单据类型，参考基础数据查询2.5.13传递")
    @JsonProperty("DJLX")
    private String DJLX;

    @ApiModelProperty(value = "必传，编制单位，4A组织OID")
    @JsonProperty("BZDW")
    private String BZDW;

    @ApiModelProperty(value = "必传，期间类型，资金计划有效时间，例20210923")
    @JsonProperty("QJNM")
    private String QJNM;

    @ApiModelProperty(value = "必传，来源系统同业务单据类型来源系统一致")
    @JsonProperty("LYXT")
    private String LYXT;

    @ApiModelProperty(value = "非必填，合同编号，主数据合同编号 QUERY")
    @JsonProperty("type")
    private String type;

    @ApiModelProperty(value = "项目id")
    private String community;

    @ApiModelProperty(value = "项目id")
    private Boolean testOrNot = false;

    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;


}
