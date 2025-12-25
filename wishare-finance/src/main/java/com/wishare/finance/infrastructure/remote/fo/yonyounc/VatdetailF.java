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
@ApiModel("欧盟vat导入")
public class VatdetailF {

    @ApiModelProperty("交易代码,最大长度为64,类型为:String")
    private String businesscode;

    @ApiModelProperty("收货国家,最大长度为64,类型为:String")
    private String pk_receivecountry;

    @ApiModelProperty("供应商VAT码,最大长度为64,类型为:String")
    private String pk_suppliervatcode;

    @ApiModelProperty("税码,最大长度为64,类型为:String")
    private String pk_taxcode;

    @ApiModelProperty("客户VAT码,最大长度为64,类型为:String")
    private String pk_clientvatcode;

    @ApiModelProperty("方向,最大长度为64,类型为:String")
    private String direction;

    @ApiModelProperty("税额,最大长度为64,类型为:Double")
    private BigDecimal moneyamount;

    @ApiModelProperty("报税国家,最大长度为64,类型为:String")
    private String pk_vatcountry;

    @ApiModelProperty("税额,最大长度为64,类型为:Double")
    private BigDecimal taxamount;
}
