package com.wishare.finance.apps.model.voucher.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import com.wishare.finance.infrastructure.configs.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证核算方案表
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@ApiModel(value="凭证核算方案信息")
public class VoucherSchemeV {

    @ApiModelProperty(value = "核算方案id")
    @JsonSerialize(using = LongToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "方案名称")
    private String name;

    @ApiModelProperty(value = "是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty(value = "财务组织信息")
    private List<VoucherSchemeOrgOBV> orgs;

    @ApiModelProperty(value = "财务组织信息")
    private List<VoucherSchemeRuleOBV> rules;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;


}
