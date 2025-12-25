package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("银行托收请求参数")
public class BankSettleF {

    @ApiModelProperty(value = "托收账单id列表，区间为[0, 1000]")
    @Size(max = 1000, message = "账单id列表大小不正确，区间为[0, 1000]")
    private List<Long> billIds = new ArrayList<>();

    @ApiModelProperty(value = "托收失败账单，区间为[0, 1000]")
    @Size(max = 1000, message = "账单id列表大小不正确，区间为[0, 1000]")
    private List<Long> errorBillIds = new ArrayList<>();

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty("回盘时间")
    private String counterofferTime;

    public LocalDateTime getCounterofferTime() {
        if (StringUtils.isBlank(counterofferTime)) {
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(this.counterofferTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
