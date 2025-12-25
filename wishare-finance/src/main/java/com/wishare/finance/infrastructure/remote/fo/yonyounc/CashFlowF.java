package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("现金流量")
public class CashFlowF {

    @ApiModelProperty("币种,最大长度为64,类型为:String")
    private String m_pk_currtype;

    @ApiModelProperty("原币,最大长度为64,类型为:Double")
    private BigDecimal money;

    @ApiModelProperty("全局本币,最大长度为64,类型为:Double")
    private BigDecimal moneyglobal;

    @ApiModelProperty("集团本币,最大长度为64,类型为:Double")
    private BigDecimal moneygroup;

    @ApiModelProperty("本币,最大长度为64,类型为:Double")
    private BigDecimal moneymain;

    @ApiModelProperty("现金主键,最大长度为64,类型为:String")
    private String pk_cashflow;

    @ApiModelProperty("内部单位主键,最大长度为64,类型为:String")
    private String pk_innercorp;
}
