package com.wishare.contract.domains.vo.revision.pay.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "周期对象")
public class PayPlanPeriodV {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "结算类型")
    private Integer type;
    private String typeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
