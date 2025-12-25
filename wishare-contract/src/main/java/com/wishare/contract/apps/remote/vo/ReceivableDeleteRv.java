package com.wishare.contract.apps.remote.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wangrui
 */
@Getter
@Setter
@ApiModel("删除中台账单")
public class ReceivableDeleteRv {

    @ApiModelProperty("失败数量")
    private Integer failCount;

    @ApiModelProperty("成功数量")
    private Integer successCount;


}
