package com.wishare.finance.infrastructure.remote.vo.space;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@Setter
@Accessors(chain = true)
@ApiModel(value = "项目信息", description = "项目信息")
public class SpaceCommunityV2V {

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
    @ApiModelProperty("编号")
    private String serialNumber;
    @ApiModelProperty("项目图片")
    private String pic;
    @ApiModelProperty("项目介绍")
    private String remark;
    @ApiModelProperty("项目详细地址")
    private String address;
    @ApiModelProperty("扩展信息，json数组")
    private Map<String, Object> expandInfo;
    @ApiModelProperty("项目状态：0正常，1关闭")
    private Byte state;
    @ApiModelProperty("是否禁用：0：否，1：是")
    private Byte disabled;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("最后一次修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtModify;

    @ApiModelProperty("组织id")
    private Long orgId;
    @ApiModelProperty("组织id")
    private List<Long> orgIds;

    @ApiModelProperty("类型名称标识")
    private String typeNameFlag;
    @ApiModelProperty("开发商，用户中心的企业信息")
    private String developer;

    @ApiModelProperty("开发商，用户中心的企业信息")
    private List<Long> developers;
    @ApiModelProperty("开发商名称")
    private String developerName;
    @ApiModelProperty("是否有子空间")
    private Boolean haveChild = false;
    @ApiModelProperty("接管日期")

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime takeOverDate;

    @ApiModelProperty("总户数")
    private Integer totalHouses;

    @ApiModelProperty("项目id")
    private String projectId;
    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("附件")
    private List<FileVo> annexes;

    private String annexUrls;

    @ApiModelProperty("项目简称编码")
    private String projectNameNumber;

    public List<FileVo> getAnnexes() {
        if (StringUtils.isNotBlank(annexUrls)) {
            return JSON.parseArray(annexUrls, FileVo.class);
        }
        return annexes;
    }
}
