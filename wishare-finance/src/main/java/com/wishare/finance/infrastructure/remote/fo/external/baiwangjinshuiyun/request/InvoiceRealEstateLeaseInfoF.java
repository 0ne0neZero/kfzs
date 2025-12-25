package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 不动产经营租赁服务  特定约束类型代码为06时必填
 * @author dongpeng
 * @date 2023/10/25 20:12
 */
@Data
@ApiModel("不动产经营租赁服务")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRealEstateLeaseInfoF {

    @ApiModelProperty(value = "不动产地址",required = true)
    private String bdcdz;

    @ApiModelProperty(value = "不动产详细地址",required = true)
    private String fulladdress;

    @ApiModelProperty(value = "租赁起止日期(2022-03-10 2022-04-07)",required = true)
    private String zlqqz;

    @ApiModelProperty(value = "跨地市标识(N 否 Y 是)",required = true)
    private String kdsbz;

    @ApiModelProperty(value = "产权证书号",required = true)
    private String cqzsh;

    @ApiModelProperty(value = "单位(平方千米\n" +
            "平方米" +
            "公顷" +
            "亩" +
            "h㎡" +
            "k㎡" +
            "㎡)",required = true)
    private String dw;
}
