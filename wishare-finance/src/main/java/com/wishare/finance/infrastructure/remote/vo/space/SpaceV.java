package com.wishare.finance.infrastructure.remote.vo.space;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("空间请求信息")
public class SpaceV {

    private Long id;
    @ApiModelProperty("空间类型")
    private Long type;
    @ApiModelProperty("项目id")
    private String communityId;
    @ApiModelProperty("父级id，为0为顶级")
    private String pid;
    @ApiModelProperty("空间名称")
    private String name;
    @ApiModelProperty("空间路径id")
    private List<String> path;
    @ApiModelProperty("标准空间名称")
    private String standardSpaceName;
    @ApiModelProperty("所有权：0 集体共有，1：私人产权")
    private Byte ownership;
    @ApiModelProperty("是否可以是资产：0：否，1是")
    private Byte isAsset;
    @ApiModelProperty("类型名称标识")
    private String typeNameFlag;
    @ApiModelProperty("业主、产权人")
    private String owner;
    @ApiModelProperty("总楼层")
    private Integer totalFloor;
    @ApiModelProperty("建筑面积")
    private BigDecimal buildArea;
    @ApiModelProperty("所在楼层")
    private String floor;
    @ApiModelProperty("房间状态：1空置(未出售)、2未领、3空关、4装修、5入住、6入伙、10可售，11已售， 12已预定，13限制房产")
    private Byte houseStatus;
    @ApiModelProperty("计费面积")
    private BigDecimal billableArea;
    @ApiModelProperty("租赁面积")
    private BigDecimal leasedArea;
    @ApiModelProperty("物业面积")
    private BigDecimal propertyArea;
    @ApiModelProperty("花园面积")
    private BigDecimal gardenArea;
    @ApiModelProperty("套内面积")
    private BigDecimal innerArea;
    @ApiModelProperty("户型")
    private String houseType;
    @ApiModelProperty("车位数")
    private Integer carNum;
    @ApiModelProperty("总户数")
    private Integer totalHouses;
    @ApiModelProperty("交付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtDeliverDate;

    @ApiModelProperty("是否有子空间")
    private Boolean haveChild = false;

    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("最后一次修改时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("0启用，1禁用")
    private Byte disabled;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String district;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("简介")
    private String remark;


    /**
     * 开发商，用户中心的企业信息
     */
    @ApiModelProperty("开发商id，逗号分隔字符串")
    private String developer;

    @ApiModelProperty("开发商名称")
    private String developerName;

    /**
     * 空间操作状态：-1合并已还原，-2拆分已还原，-3被合并已还原，-4被拆分已还原，0原始，1合并，2拆分，3被合并，4被拆分
     */
    @ApiModelProperty("空间操作状态")
    private Byte operationState;



    /**
     * 车位销售状态
     */
    @ApiModelProperty("车位销售状态")
    private String parkSalesState;
    /**
     * 车位交付状态
     */
    @ApiModelProperty("车位交付状态")
    private String parkDeliveryState;

    /**
     * 车位销售状态名称
     */
    @ApiModelProperty("车位销售状态名称")
    private String parkSalesName;
    /**
     * 车位交付状态名称
     */
    @ApiModelProperty("车位交付状态名称")
    private String parkDeliveryName;

    /**
     * 房屋销售状态
     */
    @ApiModelProperty("房屋销售状态")
    private String houseSalesState;
    /**
     * 房屋交付状态
     */
    @ApiModelProperty("房屋交付状态")
    private String houseDeliveryState;
    /**
     * 房屋销售状态
     */
    @ApiModelProperty("房屋装修状态")
    private String houseDecoratState;
    /**
     * 房屋使用状态
     */
    @ApiModelProperty("房屋使用状态")
    private String houseUsageState;
    /**
     * 房屋销售状态名称
     */
    @ApiModelProperty("房屋销售状态名称")
    private String houseSalesName;
    /**
     * 房屋交付状态名称
     */
    @ApiModelProperty("房屋交付状态名称")
    private String houseDeliveryName;
    /**
     * 房屋装修状态名称
     */
    @ApiModelProperty("房屋装修态名称")
    private String houseDecoratName;
    /**
     * 房屋使用状态名称
     */
    @ApiModelProperty("房屋使用状态名称")
    private String houseUsageName;

    @ApiModelProperty("首次交付日期")
    private LocalDateTime deliveryDate;
}
