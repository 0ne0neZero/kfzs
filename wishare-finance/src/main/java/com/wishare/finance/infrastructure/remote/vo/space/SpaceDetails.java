package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 空间详细信息
 *
 * @author yancao
 */
@Data
public class SpaceDetails {

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

    @ApiModelProperty("户型")
    private String houseType;

    @ApiModelProperty("车位数")
    private Integer carNum;

    @ApiModelProperty("总户数")
    private Integer totalHouses;

    @ApiModelProperty("交付时间")
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

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("简介")
    private String remark;
    /**
     * 车位产权编码
     */
    @ApiModelProperty("车位产权编码")
    private String parkingProperty;
    /**
     * 房产产权
     */
    @ApiModelProperty("房产产权")
    private String estateProperty;
}
