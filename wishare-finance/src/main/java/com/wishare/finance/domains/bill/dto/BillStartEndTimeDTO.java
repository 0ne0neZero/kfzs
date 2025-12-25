package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.consts.enums.BillMethodEnum;
import com.wishare.finance.infrastructure.utils.StartEndTimeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author luzhonghe
 * @date 2023/3/24
 * @Description:
 */
@Getter
@Setter
public class BillStartEndTimeDTO extends StartEndTimeDTO {

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    /**
     * 计费方式是否包含时间
     * @return
     */
    public Boolean hasTimeBilling() {
        if (billMethod != null) {
            return billMethod >= BillMethodEnum.PRICE_AREA_MONTH.getType() && billMethod <= BillMethodEnum.PRICE_DAY.getType();
        } else {
            return false;
        }
    }

}
