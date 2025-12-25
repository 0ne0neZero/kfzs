package com.wishare.finance.apps.process.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "请求响应信息")
public class CommonReturnV {

    @ApiModelProperty(value = "接口请求唯一ID")
    @NotBlank(message = "接口请求唯一ID不能为空")
    private String ZINSTID;

    @ApiModelProperty(value = "响应时间")
    @NotNull(message = "请求时间不能为空")
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime ZZRESTIME;

    @ApiModelProperty(value = "请求状态")
    @NotBlank(message = "请求状态不能为空")
    private String ZZSTAT;

    @ApiModelProperty(value = "请求响应消息")
    private String ZZMSG;

    @ApiModelProperty(value = "备用字段1")
    private String ZZATTR1;

    @ApiModelProperty(value = "备用字段2")
    private String ZZATTR2;

    @ApiModelProperty(value = "备用字段3")
    private String ZZATTR3;
}
