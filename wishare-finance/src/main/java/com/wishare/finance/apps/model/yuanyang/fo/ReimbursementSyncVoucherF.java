package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 同步报销凭证信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/5/30
 */
@Getter
@Setter
@ApiModel("同步报销凭证信息")
public class ReimbursementSyncVoucherF {

    @ApiModelProperty(value = "业务系统交易单号", required = true)
    @Length(max = 64, message = "业务系统交易单号格式不正确")
    private String bizTransactionNo;

}
