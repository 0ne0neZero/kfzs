package com.wishare.finance.apps.pushbill.vo;

import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基本信息
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@ApiModel(value="支付申请单-创建-基本信息")
public class PaymentApplicationBasicV {

    /**-----------------------------基本信息---------------------------------*/

    @ApiModelProperty("单位code")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门名称")
    private String departName;

    @ApiModelProperty("单据日期")
    private LocalDateTime billDate;

    @ApiModelProperty("核算组织")
    private String org;

    @ApiModelProperty("经办人")
    private String handledBy;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
}
