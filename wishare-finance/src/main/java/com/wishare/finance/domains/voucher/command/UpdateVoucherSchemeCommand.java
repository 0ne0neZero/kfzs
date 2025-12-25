package com.wishare.finance.domains.voucher.command;

import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 更新凭证核算方案命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Getter
@Setter
@ApiModel("更新凭证核算方案命令")
public class UpdateVoucherSchemeCommand {

    @ApiModelProperty(value = "核算方案id")
    @NotNull(message = "核算方案信息")
    private Long id;

    @ApiModelProperty(value = "方案名称")
    private String name;

    @ApiModelProperty(value = "是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty(value = "新增的财务组织信息")
    private List<VoucherSchemeOrgOBV> orgs;

    @ApiModelProperty(value = "新增的财务组织信息")
    private List<VoucherSchemeRuleOBV> rules;


}
