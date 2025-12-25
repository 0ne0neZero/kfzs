package com.wishare.finance.infrastructure.remote.vo.space;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
* <p>
* 企业档案-房产信息
* </p>
*
* @author wishare
* @since 2022-09-08
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "archives_enterprise_asset请求对象", description = "企业档案-房产信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivesEnterpriseAssetV {

    private Long id;
    @ApiModelProperty("企业档案archives_enterprise_base的主键ID")
    private Long archivesEnterpriseId;
    @ApiModelProperty("项目ID")
    private String communityId;
    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty("房号ID")
    private Long spaceId;
    @ApiModelProperty("房号名称")
    private String spaceName;
    @ApiModelProperty("加入时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtJoinTime;
    @ApiModelProperty("入住时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtCheckInTime;
    @ApiModelProperty("装修时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtDecorateTime;
    @ApiModelProperty("1 租赁 2 自购")
    private Integer assetRelation;
    @ApiModelProperty("租赁生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtAssetEffectiveTime;
    @ApiModelProperty("租赁失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtAssetExpireTime;


}
