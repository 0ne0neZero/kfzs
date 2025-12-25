package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
@ApiModel("编辑成本中心关联银行入参")
public class BankAccountCostOrgF {

    @ApiModelProperty(value = "成本中心Id", required = true)
    @NotNull(message = "成本中心Id不能为空")
    private Long costOrgId;

    @ApiModelProperty(value = "新增银行账户Id")
    private List<Long> addAccountList;

    @ApiModelProperty(value = "删除银行账户Id")
    private List<Long> deleteAccountList;


}
