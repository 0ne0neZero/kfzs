package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/22 9:18
 * @descrption:
 */
@Data
@ApiModel(value = "特定业务信息")
public class InvoiceSpecialInfoResV {

    @ApiModelProperty(value = "建筑服务特定要素-建筑服务发生地(按照省、市、区/县三级传值，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”)和详细地址相加共占120")
    private String buildingLocalAddress;

    @ApiModelProperty(value = "建筑服务特定要素-详细地址（和建筑服务发生地相加共占120）")
    private String buildingDetailAddress;

    @ApiModelProperty(value = "建筑服务特定要素-建筑项目名称")
    private String buildingName;

    @ApiModelProperty(value = "建筑服务特定要素-土地增值税项目编号")
    private String buildingLandTaxNo;

    @ApiModelProperty(value = "建筑服务特定要素-跨地（市）标志（标志：Y：是；N：否）")
    private String buildingCrossSign;

    @ApiModelProperty(value = "货物运输特定要素-启运地（按照省、市、区/县三级传值，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”）")
    private String transportDeparture;

    @ApiModelProperty(value = "货物运输特定要素-到达地（按照省、市、区/县三级传值，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”）")
    private String transportArrive;

    @ApiModelProperty(value = "货物运输特定要素-运输工具种类（铁路运输、公路运输、水路运输、航空运输、管道运输货物运输特定业务）")
    private String transportToolType;

    @ApiModelProperty(value = "货物运输特定要素-运输工具牌号")
    private String transportToolNum;

    @ApiModelProperty(value = "货物运输特定要素-运输货物名称")
    private String transportGoodsName;

    @ApiModelProperty(value = "不动产销售服务-房屋产权证书/不动产权证号码")
    private String propertyPropertyNo;

    @ApiModelProperty(value = "不动产销售服务-不动产地址和详细地址相加共占120（按照省、市、区/县三级传值，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”）")
    private String propertyAddress;

    @ApiModelProperty(value = "不动产销售服务-详细地址（和不动产地址相加共占120）")
    private String propertyDetailAddress;

    @ApiModelProperty(value = "不动产销售服务-不动产单元代码/网签合同备案编码")
    private String propertyContractNo;

    @ApiModelProperty(value = "不动产销售服务-土地增值税项目编号")
    private String propertyLandTaxNo;

    @ApiModelProperty(value = "不动产销售服务-跨地（市）标志")
    private String propertyCrossSign;

    @ApiModelProperty(value = "不动产销售服务-面积单位")
    private String propertyAreaUnit;

    @ApiModelProperty(value = "不动产销售服务-核定计税价格")
    private String propertyApprovedPrice;

    @ApiModelProperty(value = "不动产销售服务-实际成交含税金额")
    private String propertyDealPrice;

    @ApiModelProperty(value = "不动产租赁-房屋产权证书/不动产权证号码")
    private String leasePropertyNo;

    @ApiModelProperty(value = "不动产租赁-不动产地址和详细地址相加共占120（按照省、市、区/县三级传值，以&符间隔，举例“北京市&东城区、河北省&石家庄市&正定县”）")
    private String leaseAddress;

    @ApiModelProperty(value = "不动产租赁-详细地址（和不动产地址相加共占120）")
    private String leaseDetailAddress;

    @ApiModelProperty(value = "不动产租赁-跨地（市）标志")
    private String leaseCrossSign;

    @ApiModelProperty(value = "不动产租赁-面积单位")
    private String leaseAreaUnit;

    @ApiModelProperty(value = "不动产租赁-租赁期起(yyyy-MM-dd)")
    private String leaseHoldDateStart;

    @ApiModelProperty(value = "不动产租赁-租赁期止(yyyy-MM-dd)")
    private String leaseHoldDateEnd;
}
