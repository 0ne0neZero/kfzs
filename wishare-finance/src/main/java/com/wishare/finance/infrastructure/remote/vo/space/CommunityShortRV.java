package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class CommunityShortRV {
    @ApiModelProperty("项目id")
    private String id;
    @ApiModelProperty("项目名")
    private String name;
    @ApiModelProperty("邮编")
    private String code;
    @ApiModelProperty("占地面积")
    private String cover;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区")
    private String area;
    @ApiModelProperty("社区")
    private String shequ;
    @ApiModelProperty("街道")
    private String street;
    @ApiModelProperty("经度")
    private String longitude;
    @ApiModelProperty("纬度")
    private String latitude;
    @ApiModelProperty("项目类型")
    private Long type;
    @ApiModelProperty("项目阶段，1意向项目、2运行项目、3退出项目、4签约项目")
    private String stage;
    @ApiModelProperty("所有权：0 集体共有，1：私人产权")
    private Byte ownership;
    @ApiModelProperty("建筑面积")
    private String buildArea;
    @ApiModelProperty("联系人")
    private String contacts;
    @ApiModelProperty("联系人电话")
    private String contactsPhone;
    @ApiModelProperty("开发商，用户中心的企业信息")
    private String developer;
    @ApiModelProperty("项目状态：0正常，1关闭")
    private Byte state;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否禁用：0：否，1：是")
    private Byte disabled;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("最后一次修改时间")
    private LocalDateTime gmtModify;
    @ApiModelProperty("组织id")
    private Long orgId;
    @ApiModelProperty("类型名称标识")
    private String typeNameFlag;
    @ApiModelProperty("接管日期")
    private LocalDateTime takeOverDate;
}
