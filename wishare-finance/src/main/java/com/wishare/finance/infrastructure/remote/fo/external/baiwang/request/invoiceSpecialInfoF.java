package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Linitly
 * @date: 2023/6/19 20:54
 * @descrption:
 */
@Data
@ApiModel(value = "")
public class invoiceSpecialInfoF {

    // 必填，长度60
    @ApiModelProperty(value = "建筑服务特定要素-建筑服务发生地，按照省、市、区/县三级传值 \n" +
            "，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县 \n" +
            "”(建筑服务发生地和详细地址之和为120)")
    private String buildingLocalAddress;

    // 必填，长度60
    @ApiModelProperty(value = "建筑服务特定要素-建筑服务详细地址，举例“北京市海淀区清华 \n" +
            "东路17号”(建筑服务发生地和详细地址之和为120)")
    private String buildingDetailAddress;

    // 必填，长度80
    @ApiModelProperty(value = "建筑服务特定要素-建筑项目名称")
    private String buildingName;

    // 非必填，长度40
    @ApiModelProperty(value = "建筑服务特定要素-土地增值税项目编号")
    private String buildingLandTaxNo;

    // 必填，长度8
    @ApiModelProperty(value = "建筑服务特定要素-跨地（市）标志；标志：Y：是；N：否")
    private String buildingCrossSign;

    // 必填，长度80
    @ApiModelProperty(value = "货物运输特定要素-启运地")
    private String transportDeparture;

    // 必填，长度80
    @ApiModelProperty(value = "货物运输特定要素-到达地")
    private String transportArrive;

    // 必填，长度40
    @ApiModelProperty(value = "货物运输特定要素-运输工具种类，铁路运输、公路运输、水路运 \n" +
            "输、航空运输、管道运输 货物运输特定业务")
    private String transportToolType;

    // 必填，长度40
    @ApiModelProperty(value = "货物运输特定要素-货物运输特定业务：运输工具牌号")
    private String transportToolNum;

    // 必填，长度80
    @ApiModelProperty(value = "货物运输特定要素-货物运输特定业务：运输货物名称")
    private String transportGoodsName;

    // 非必填，长度40
    @ApiModelProperty(value = "不动产销售服务-房屋产权证书/不动产权证号码")
    private String propertyPropertyNo;

    // 必填，长度60
    @ApiModelProperty(value = "不动产销售服务-不动产地址，按照省、市、区/县三级传值，以 \n" +
            "&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”(不 \n" +
            "动产地址和详细地址之和为120)")
    private String propertyAddress;

    // 必填，长度60
    @ApiModelProperty(value = "不动产销售服务-详细地址，举例“北京市海淀区清华东路17号 \n" +
            "”(不动产地址和详细地址之和为120)")
    private String propertyDetailAddress;

    // 非必填，长度28
    @ApiModelProperty(value = "不动产销售服务-不动产单元代码/网签合同备案编码")
    private String propertyContractNo;

    // 非必填，长度16
    @ApiModelProperty(value = "不动产销售服务-土地增值税项目编号")
    private String propertyLandTaxNo;

    // 必填，长度8
    @ApiModelProperty(value = "不动产销售服务-跨地（市）标志")
    private String propertyCrossSign;

    // 必填，长度16
    @ApiModelProperty(value = "不动产销售服务-面积单位")
    private String propertyAreaUnit;

    // 非必填
    @ApiModelProperty(value = "不动产销售服务-核定计税价格")
    private BigDecimal propertyApprovedPrice;

    // 非必填
    @ApiModelProperty(value = "不动产销售服务-实际成交含税金额")
    private BigDecimal propertyDealPrice;

    // 必填，长度40
    @ApiModelProperty(value = "不动产租赁-房屋产权证书/不动产权证号码")
    private String leasePropertyNo;

    // 必填，长度60
    @ApiModelProperty(value = "不动产租赁-不动产地址，按照省、市、区/县三级传值，以&符间 \n" +
            "隔，举例“北京市&东城区、河北省&石家庄市&正定县”（不动产 \n" +
            "地址和详细地址之和为120）")
    private String leaseAddress;

    // 必填，长度60
    @ApiModelProperty(value = "不动产租赁-详细地址，举例“北京市海淀区清华东路17号”（不 \n" +
            "动产地址和详细地址之和为120）")
    private String leaseDetailAddress;

    // 必填，长度8
    @ApiModelProperty(value = "不动产租赁-跨地（市）标志")
    private String leaseCrossSign;

    // 必填，长度16
    @ApiModelProperty(value = "不动产租赁-面积单位")
    private String leaseAreaUnit;

    // 必填，长度20
    @ApiModelProperty(value = "不动产租赁-租赁期起；yyyy-MM-dd")
    private String leaseHoldDateStart;

    // 必填，长度20
    @ApiModelProperty(value = "不动产租赁-租赁期止；yyyy-MM-dd")
    private String leaseHoldDateEnd;
}
