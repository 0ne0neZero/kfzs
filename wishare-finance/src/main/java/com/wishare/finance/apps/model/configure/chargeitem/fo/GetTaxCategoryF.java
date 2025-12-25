package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.finance.domains.configure.chargeitem.command.tax.GetTaxCategoryCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("查询税种税率信息")
public class GetTaxCategoryF {

    @ApiModelProperty("税种id")
    private Long id;

    @ApiModelProperty("税种名称")
    private String name;

    public GetTaxCategoryCommand getTaxCategoryCommand(IdentityInfo identityInfo){
        GetTaxCategoryCommand command = Global.mapperFacade.map(this, GetTaxCategoryCommand.class);
        command.setTenantId(identityInfo.getTenantId());
        return command;
    }
}
