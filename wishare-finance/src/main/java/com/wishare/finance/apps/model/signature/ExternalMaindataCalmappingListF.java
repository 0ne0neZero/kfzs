package com.wishare.finance.apps.model.signature;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* <p>
* 中交核算机构映射表 更新请求参数
* </p>
*
* @author zhangfy
* @since 2023-12-11
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "中交核算机构映射表下拉列表请求参数", description = "中交核算机构映射表")
public class ExternalMaindataCalmappingListF {

    /**
    * zid
    */
    @ApiModelProperty("映射表ID")
    @Length(message = "映射表ID不可超过 100 个字符",max = 100)
    private String zid;
    /**
    * zzserial
    */
    @ApiModelProperty("发送记录ID")
    @Length(message = "发送记录ID不可超过 12 个字符",max = 12)
    private String zzserial;
    /**
    * zaid
    */
    @ApiModelProperty("核算组织ID")
    @Length(message = "核算组织ID不可超过 50 个字符",max = 50)
    private String zaid;
    /**
    * zaorgno
    */
    @ApiModelProperty("核算组织编码")
    @Length(message = "核算组织编码不可超过 50 个字符",max = 50)
    private String zaorgno;
    /**
    * zorgid
    */
    @ApiModelProperty("行政组织ID")
    @Length(message = "行政组织ID不可超过 50 个字符",max = 50)
    private String zorgid;
    /**
    * zorgcode
    */
    @ApiModelProperty("行政组织编码")
    @Length(message = "行政组织编码不可超过 50 个字符",max = 50)
    private String zorgcode;
    /**
    * gmtCreate
    */
    @ApiModelProperty("录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * gmtModify
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * zdelete
    */
    @ApiModelProperty("0 删除 1 未删除")
    private Integer zdelete;
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private Long indexId;
}
