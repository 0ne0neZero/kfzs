package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 凭证核算方案财务组织信息
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@ApiModel(value="凭证核算方案财务组织信息")
public class VoucherSchemeOrgOBV {

    @ApiModelProperty(value = "核算方案id")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "财务组织id")
    @NotNull(message = "财务组织id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "财务组织编码")
    private String orgCode;

    @ApiModelProperty(value = "财务组织名称")
    @NotBlank(message = "财务组织编码不能为空")
    private String orgName;

    @ApiModelProperty(value = "关联类型： 1法定单位，2成本中心")
    @NotNull(message = "关联类型不能为空")
    private Integer orgType;

}
