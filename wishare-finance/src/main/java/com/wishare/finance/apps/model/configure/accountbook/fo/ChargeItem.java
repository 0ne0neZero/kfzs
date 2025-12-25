package com.wishare.finance.apps.model.configure.accountbook.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("费项")
public class ChargeItem {

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    public ChargeItem() {
    }

    public ChargeItem(Long chargeItemId, String chargeItemName) {
        this.chargeItemId = chargeItemId;
        this.chargeItemName = chargeItemName;
    }
}
