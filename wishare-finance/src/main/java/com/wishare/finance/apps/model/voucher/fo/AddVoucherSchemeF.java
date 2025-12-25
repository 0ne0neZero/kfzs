package com.wishare.finance.apps.model.voucher.fo;

import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * 新增凭证核算方案信息
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@ApiModel(value="新增凭证核算方案信息")
public class AddVoucherSchemeF {

    @ApiModelProperty(value = "方案名称")
    private String name;

    @ApiModelProperty(value = "是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty(value = "财务组织信息")
    @Valid
    private List<VoucherSchemeOrgOBV> orgs;

    @ApiModelProperty(value = "关联凭证规则")
    @Valid
    private List<VoucherSchemeRuleOBV> rules;

}
