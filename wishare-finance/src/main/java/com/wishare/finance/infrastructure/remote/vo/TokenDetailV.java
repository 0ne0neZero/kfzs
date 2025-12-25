package com.wishare.finance.infrastructure.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDetailV implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "错误码")
  private String errcode;

  @ApiModelProperty(value = "错误信息")
  private String errmsg;

  @ApiModelProperty(value = "token")
  private String access_token;

  @ApiModelProperty(value = "token有效时间")
  private String expires_in;

}