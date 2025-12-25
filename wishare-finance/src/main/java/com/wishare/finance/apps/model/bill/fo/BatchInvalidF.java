package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量作废
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("批量作废")
public class BatchInvalidF {

    @ApiModelProperty("账单id集合")
    @NotEmpty(message = "账单id集合不能为空")
    private List<Long> billIdList;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

}
