package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "手机号参数")
public class PhoneParamF {

    @ApiModelProperty(value = "手机号列表")
    private List<String> phones;

}
