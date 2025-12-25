package com.wishare.contract.domains.vo.revision.projectInitiation;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReceiveOrderResp {

    @JSONField(name = "NEW_ITEM-LONGTEXT_n:132[]")
    @JsonProperty("NEW_ITEM-LONGTEXT_n:132[]")
    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @JSONField(name = "NEW_ITEM-ORDER_AMOUNT")
    @JsonProperty("NEW_ITEM-ORDER_AMOUNT")
    @ApiModelProperty(value = "订单应付金额")
    private BigDecimal orderAmount;

    @JSONField(name = "NEW_ITEM-ORDER-TIME")
    @JsonProperty("NEW_ITEM-ORDER-TIME")
    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime orderCreateTime;

    @JSONField(name = "NEW_ITEM-SLD_SYS_NAME")
    @JsonProperty("NEW_ITEM-SLD_SYS_NAME")
    @ApiModelProperty(value = "下单账号")
    private String orderAccount;

    // 其他字段
    @JSONField(name = "NEW_ITEM-PO")
    @JsonProperty("NEW_ITEM-PO")
    @ApiModelProperty(value = "PO号")
    private String po;

    @JSONField(name = "NEW_ITEM-PR")
    @JsonProperty("NEW_ITEM-PR")
    @ApiModelProperty(value = "PR号")
    private String pr;

    @JSONField(name = "NEW_ITEM-BATCH_NO")
    @JsonProperty("NEW_ITEM-BATCH_NO")
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @JSONField(name = "NEW_ITEM-ORDER_FREIGHT")
    @JsonProperty("NEW_ITEM-ORDER_FREIGHT")
    @ApiModelProperty(value = "运费")
    private BigDecimal orderFreight;

    /**
     * 关于PR号的说明：
     * 慧采外接PR号传输有新旧两套方案，旧版本（5.0商城）使用旧方案，启用PR号控制下单后，登录接口PR字段传输的内容将与京东订单一对一映射，无pr号、重复将不能下单，订单回传时，使用NEW_ITEM-THIRD_ORDER_ID回传PR号，PC端回传值会拼接下单PIN（回传值为PIN+PR，H5端回传不会拼接PIN）
     * 新版本商城（6.0）使用新方案，启用PR号控制下单后，登录接口PR字段传输的内容将与京东订单一对一映射，无pr号、重复将不能下单，订单回传时，使用NEW_ITEM-PR回传PR号，传值=登录接口客户入参PR值
     */
    @JSONField(name = "NEW_ITEM-THIRD_ORDER_ID")
    @JsonProperty("NEW_ITEM-THIRD_ORDER_ID")
    @ApiModelProperty(value = "第三方订单ID")
    private String prCode;

    @JSONField(name = "NEW_ITEM-UNIQUE_NO")
    @JsonProperty("NEW_ITEM-UNIQUE_NO")
    @ApiModelProperty(value = "唯一编号")
    private String uniqueNo;

    @JSONField(name = "NEW_ITEM-CUSTOMIZE")
    @JsonProperty("NEW_ITEM-CUSTOMIZE")
    @ApiModelProperty(value = "自定义字段")
    private String customize;

    @JSONField(name = "NEW_ITEM-REMARK")
    @JsonProperty("NEW_ITEM-REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @JSONField(name = "sign")
    @JsonProperty("sign")
    @ApiModelProperty(value = "签名")
    private String sign;

    @JSONField(name = "sku")
    @JsonProperty("sku")
    @ApiModelProperty(value = "商品信息")
    private List<SkuDTO> sku;


    @Data
    @Accessors(chain = true)
    @ApiModel(description = "商品信息")
    public static class SkuDTO {

        @JSONField(name = "NEW_ITEM-SKUBRAND")
        @JsonProperty("NEW_ITEM-SKUBRAND")
        @ApiModelProperty(value = "商品品牌")
        private String brand;

        @JSONField(name = "NEW_ITEM-DESCRIPTION")
        @JsonProperty("NEW_ITEM-DESCRIPTION")
        @ApiModelProperty(value = "商品描述")
        private String description;

        @JSONField(name = "NEW_ITEM-EXT_SECOND_CATEGORY_NAME")
        @JsonProperty("NEW_ITEM-EXT_SECOND_CATEGORY_NAME")
        @ApiModelProperty(value = "二级分类名称")
        private String extSecondCategoryName;

        @JSONField(name = "NEW_ITEM-PRICEUNIT")
        @JsonProperty("NEW_ITEM-PRICEUNIT")
        @ApiModelProperty(value = "商品税额(仅供参考，通过税率计算得出，与发票税金有差异)")
        private BigDecimal priceUnit;

        @JSONField(name = "NEW_ITEM-RTPRICE")
        @JsonProperty("NEW_ITEM-RTPRICE")
        @ApiModelProperty(value = "商品实时价")
        private BigDecimal rtPrice;

        @JSONField(name = "NEW_ITEM-EXT_SECOND_CATEGORY_ID")
        @JsonProperty("NEW_ITEM-EXT_SECOND_CATEGORY_ID")
        @ApiModelProperty(value = "二级分类ID")
        private String extSecondCategoryId;

        @JSONField(name = "NEW_ITEM-LONGTEXT")
        @JsonProperty("NEW_ITEM-LONGTEXT")
        @ApiModelProperty(value = "商品图片")
        private String longText;

        @JSONField(name = "NEW_ITEM-SKUBMODEL")
        @JsonProperty("NEW_ITEM-SKUBMODEL")
        @ApiModelProperty(value = "商品型号")
        private String productModel;

        @JSONField(name = "NEW_ITEM-SKUTYPE")
        @JsonProperty("NEW_ITEM-SKUTYPE")
        @ApiModelProperty(value = "SKU类型 0:普通sku;1:附件sku;2:赠品sku;3:延保sku")
        private String skuType;

        @JSONField(name = "NEW_ITEM-CLASSIFICATION")
        @JsonProperty("NEW_ITEM-CLASSIFICATION")
        @ApiModelProperty(value = "物料组编码 品类维度映射的客户侧物料号")
        private String classification;

        @JSONField(name = "NEW_ITEM-QUANTITY")
        @JsonProperty("NEW_ITEM-QUANTITY")
        @ApiModelProperty(value = "商品数量")
        private Integer quantity;

        @JSONField(name = "NEW_ITEM-EXT_CATEGORY_ID")
        @JsonProperty("NEW_ITEM-EXT_CATEGORY_ID")
        @ApiModelProperty(value = "分类ID")
        private String extCategoryId;

        @JSONField(name = "NEW_ITEM-VENDORMAT")
        @JsonProperty("NEW_ITEM-VENDORMAT")
        @ApiModelProperty(value = "商品编号")
        private String vendorMat;

        @JSONField(name = "NEW_ITEM-PARENTSKUID")
        @JsonProperty("NEW_ITEM-PARENTSKUID")
        @ApiModelProperty(value = "所属skuId 当商品类型为1:附件;2:赠品有值")
        private String parentSkuId;

        @JSONField(name = "NEW_ITEM-MATNR")
        @JsonProperty("NEW_ITEM-MATNR")
        @ApiModelProperty(value = "商品税率")
        private String taxRate;

        @JSONField(name = "NEW_ITEM-TAXCODE")
        @JsonProperty("NEW_ITEM-TAXCODE")
        @ApiModelProperty(value = "商品税收分类编码")
        private String taxCode;

        @JSONField(name = "NEW_ITEM-EXT_QUOTE_ID")
        @JsonProperty("NEW_ITEM-EXT_QUOTE_ID")
        @ApiModelProperty(value = "商品一级分类ID")
        private String extQuoteId;

        @JSONField(name = "NEW_ITEM-EXT_CATEGORY_NAME")
        @JsonProperty("NEW_ITEM-EXT_CATEGORY_NAME")
        @ApiModelProperty(value = "商品三级分类名称")
        private String extCategoryName;

        @JSONField(name = "NEW_ITEM-MATERIAL_CODE")
        @JsonProperty("NEW_ITEM-MATERIAL_CODE")
        @ApiModelProperty(value = "维度映射的客户侧物料号")
        private String materialCode;

        @JSONField(name = "NEW_ITEM-EXT_QUOTE_ITEM")
        @JsonProperty("NEW_ITEM-EXT_QUOTE_ITEM")
        @ApiModelProperty(value = "商品一级分类名称")
        private String extQuoteItem;

        @JSONField(name = "NEW_ITEM-UNIT")
        @JsonProperty("NEW_ITEM-UNIT")
        @ApiModelProperty(value = "销售单位")
        private String unit;

        @JSONField(name = "NEW_ITEM-PRICE")
        @JsonProperty("NEW_ITEM-PRICE")
        @ApiModelProperty(value = "商品单价(默认含税价)")
        private BigDecimal price;

        // @JSONField(name = "NEW_ITEM-SERVICE-DATA")
        // @JsonProperty("NEW_ITEM-SERVICE-DATA")
        // @ApiModelProperty(value = "服务数据")
        // private String serviceData;

        @JSONField(name = "NEW_ITEM-SKUBTYPE")
        @JsonProperty("NEW_ITEM-SKUBTYPE")
        @ApiModelProperty(value = "商品类型 null：普通商品；生鲜：生鲜商品；2：京东精选信创商品（已适配检验）；3：信创商品（未适配检验）")
        private String skuBType;

    }

}
