package com.wishare.finance.apps.model.configure.chargeitem.vo;

import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateD;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("税种树形反参")
public class TaxCategoryTree extends Tree<TaxCategoryTree,Long> {

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("税的类型")
    private String taxTypeStr = "税种";

    @ApiModelProperty("税率")
    private List<TaxRateD> taxRateS;

}
