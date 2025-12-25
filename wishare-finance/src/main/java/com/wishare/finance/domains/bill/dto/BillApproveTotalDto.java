package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * 账单类型审核合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单类型审核合计信息")
public class BillApproveTotalDto {

    @ApiModelProperty(value = "审核总数", required = true)
    private Long total;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单）", required = true)
    private Integer type;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer operateType;

    @ApiModelProperty(value = "审核状态（0未审核，1审核中，2已审核，3未通过）")
    private Integer approvedState;

    public Long getTotal() {
       return Optional.ofNullable(this.total).orElse(0L);
    }
}
