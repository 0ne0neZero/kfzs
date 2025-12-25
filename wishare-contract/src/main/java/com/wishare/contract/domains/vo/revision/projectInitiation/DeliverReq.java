package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class  DeliverReq {

    @ApiModelProperty(value = "妥投头部字段")
    private DeliverHeaderReq header;

    @ApiModelProperty(value = "妥投订单商品列表信息")
    private List<DeliverEntityReq> entitys;

    @Data
    public static class DeliverHeaderReq {

        @ApiModelProperty(value = "父订单号（可以重复）")
        private String parentJdOrderNum;

        @ApiModelProperty(value = "子订单号（订单妥投方式下全局唯一，运单妥投方式下可以重复）")
        private String jdOrderNum;

        @ApiModelProperty(value = "快递包裹号（运单妥投方式下全局唯一）")
        private String deliveryNumber;

        @ApiModelProperty(value = "订单完成时间")
        private Date completeTime;

        @ApiModelProperty(value = "客户系统订单号")
        private String poNum;

        @ApiModelProperty(value = "收获人姓名")
        private String shipToUser;

        @ApiModelProperty(value = "收获人手机号")
        private String shipToUserPhone;

        @ApiModelProperty(value = "收获地址")
        private String shipToAddress;

        @ApiModelProperty(value = "京东账号")
        private String pin;

    }

    @Data
    public class DeliverEntityReq {

        @ApiModelProperty(value = "商品编号")
        private String skuId;

        @ApiModelProperty(value = "商品名称")
        private String skuName;

        @ApiModelProperty(value = "商品购买数量（京东子单商品数量）")
        private Integer quantity;

        @ApiModelProperty(value = "商品含税单价")
        private BigDecimal salesPrice;

        @ApiModelProperty(value = "客户PO行，没有数据时为空")
        private String lineNumber;

        @ApiModelProperty(value = "税率，示例：13")
        private BigDecimal taxCode;

        @ApiModelProperty(value = "京东三级目录名称")
        private String categoryName;

        @ApiModelProperty(value = "京东三级目录id")
        private String categoryId;

        @ApiModelProperty(value = "商品单位")
        private String unit;

        @ApiModelProperty(value = "物料编码；只有当前订单商品已维护物料映射，才会有传值，如果商品没维护物料，该字段不会传输（报文中无该字段）")
        private String materialCode;

    }

}
