package com.wishare.finance.apps.model.configure.businessunit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cuipengfei
 * @date 2023-06-06- 15:31:11
 * @Description
 */
@Getter
@Setter
@ApiModel("业务单元关联的银行账户信息")
public class BusinessUnitAccountV {

    @ApiModelProperty(value = "业务单元Id")
    private Long businessUnitId;

    @ApiModelProperty(value = "业务单元编码")
    private String businessUnitCode;

    @ApiModelProperty(value = "业务单元名称")
    private String businessUnitName;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "账户类型")
    private Integer accountType;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "银行账户id")
    private Long id;

}
