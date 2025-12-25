package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

/**
 * 不动产销售服务  特定约束类型代码为05时必填
 * @author dongpeng
 * @date 2023/10/25 20:10
 */
@Data
@ApiModel("不动产销售服务")
public class InvoiceRealEstateSalesInfoF {

    @ApiModelProperty(value = "网签合同备案号",required = true)
    private String wqhtbabh;

    @ApiModelProperty(value = "不动产地址",required = true)
    private String bdcdz;

    @ApiModelProperty(value = "不动产详细地址",required = true)
    private String fulladdress;

    @ApiModelProperty(value = "跨地市标识(N 否 Y 是)",required = true)
    private String kdsbz;

    @ApiModelProperty(value = "土地增值税项目编号",required = true)
    private String tdzzsxmbh;

    @ApiModelProperty(value = "核定计税价格",required = true)
    private Decimal hdjsjg;

    @ApiModelProperty(value = "实际成交含税金额",required = true)
    private Decimal sjcjhsje;

    @ApiModelProperty(value = "产权证书号",required = true)
    private String cqzsh;

    @ApiModelProperty(value = "单位(平方千米\n" +
            "平方米\n" +
            "公顷\n" +
            "亩\n" +
            "h㎡\n" +
            "k㎡\n" +
            "㎡)",required = true)
    private String dw;

}
