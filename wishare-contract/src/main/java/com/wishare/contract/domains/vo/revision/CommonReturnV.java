package com.wishare.contract.domains.vo.revision;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 通用输出参数
 *
 * @author zhangfuyu
 * @Date 2023/6/12/11:28
 */
@Data
@ApiModel(value = "请求响应信息")
public class CommonReturnV {
    /**
     * 目标系统直接返回通用输入参数中请求头部信息同名参数对应的值。
     */
    @ApiModelProperty(value = "接口请求唯一ID")
    @NotBlank(message = "接口请求唯一ID不能为空")
    private String ZINSTID;

    /**
     * 响应请求的系统时间
     * 格式：(yyyyMMddHHmmss)
     */
    @ApiModelProperty(value = "响应时间")
    @NotNull(message = "请求时间不能为空")
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime ZZRESTIME;

    /**
     * 状态：S（成功）/E（错误）
     * 如果整批数据处理成功，则返回成功；
     * 如果部分数据处理成功，则返回错误。
     */
    @ApiModelProperty(value = "请求状态")
    @NotBlank(message = "请求状态不能为空")
    private String ZZSTAT;

    /**
     * 描述请求调用的情况，可以返回发送成功或错误数据的数量，也可以返回整批请求数据的错误描述。
     */
    @ApiModelProperty(value = "请求响应消息")
    private String ZZMSG;

    @ApiModelProperty(value = "备用字段1")
    private String ZZATTR1;

    @ApiModelProperty(value = "备用字段2")
    private String ZZATTR2;

    @ApiModelProperty(value = "备用字段3")
    private String ZZATTR3;
}
