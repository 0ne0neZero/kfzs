package com.wishare.finance.apps.model.signature;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* <p>
* 中交核算机构映射表视图对象
* </p>
*
* @author zhangfy
* @since 2023-12-11
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "中交核算机构映射表视图对象", description = "中交核算机构映射表视图对象")
public class ExternalMaindataCalmappingV {

    /**
    * 主数据主键
    */
    @ApiModelProperty("主数据主键")
    private Long id;
    /**
    * 映射表ID
    */
    @ApiModelProperty("映射表ID")
    private String zid;
    /**
    * 发送记录ID
    */
    @ApiModelProperty("发送记录ID")
    private String zzserial;
    /**
    * 核算组织ID
    */
    @ApiModelProperty("核算组织ID")
    private String zaid;
    /**
    * 核算组织编码
    */
    @ApiModelProperty("核算组织编码")
    private String zaorgno;
    /**
    * 行政组织ID
    */
    @ApiModelProperty("行政组织ID")
    private String zorgid;
    /**
    * 行政组织编码
    */
    @ApiModelProperty("行政组织编码")
    private String zorgcode;
    /**
    * 录入时间
    */
    @ApiModelProperty("录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * 0 删除 1 未删除
    */
    @ApiModelProperty("0 删除 1 未删除")
    private Integer zdelete;

}
