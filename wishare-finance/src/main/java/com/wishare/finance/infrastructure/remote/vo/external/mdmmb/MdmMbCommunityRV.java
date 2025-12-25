package com.wishare.finance.infrastructure.remote.vo.external.mdmmb;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@ApiModel(value = "实体项目信息映射表视图对象", description = "实体项目信息映射表")
public class MdmMbCommunityRV {

    private Integer id;
    @ApiModelProperty("主数据自增ID")
    private String mdmId;
    @ApiModelProperty("主数据编码")
    private String mdmCode;
    @ApiModelProperty("远程系统业务数据ID")
    private String businessId;
    @ApiModelProperty("远程系统ID")
    private Long remoteSystemId;
    @ApiModelProperty("业务系统值")
    private String businessValue;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}

