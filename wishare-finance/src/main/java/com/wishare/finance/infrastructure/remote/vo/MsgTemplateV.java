package com.wishare.finance.infrastructure.remote.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MsgTemplateV implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("模板id")
  private Long id;

  @ApiModelProperty("模板名称")
  private String name;

  @ApiModelProperty("模板内容")
  private String content;

  @ApiModelProperty("模板类型:0站内信 1短信")
  private Integer type;

  @ApiModelProperty("是否删除：0否 1是")
  private Integer deleted;

  @ApiModelProperty("所属渠道：0站内信 1短信 2极光 3钉钉 4企微 5邮件")
  private Integer channel;

  @ApiModelProperty("页面跳转url,需要从消息跳转到其他页面时必填")
  private String jumpUrl;

  @ApiModelProperty("创建用户")
  private String creator;

  @ApiModelProperty("修改用户")
  private String operator;

  @ApiModelProperty("创建人名称")
  private String creatorName;

  @ApiModelProperty("操作人名称")
  private String operatorName;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  private LocalDateTime gmtCreate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty("修改时间")
  private LocalDateTime gmtModify;

}
