package com.wishare.contract.domains.vo.revision.remind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(value = "手机号和员工三方id")
@AllArgsConstructor
@NoArgsConstructor
public class PhoneThirdPartyIdV implements Serializable {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "三方id")
    private String thirdPartyId;

}
