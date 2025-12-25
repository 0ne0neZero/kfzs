package com.wishare.finance.infrastructure.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRespV implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "错误码")
  private String errcode;

  @ApiModelProperty(value = "错误信息")
  private String errmsg;

  @ApiModelProperty(value = "无效用户")
  private String invaliduser;

  @ApiModelProperty(value = "无效组织")
  private String invalidparty;

  @ApiModelProperty(value = "jobid")
  private String jobid;

}






