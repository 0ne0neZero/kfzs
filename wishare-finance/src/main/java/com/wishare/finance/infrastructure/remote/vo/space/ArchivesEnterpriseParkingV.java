package com.wishare.finance.infrastructure.remote.vo.space;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author fengxiaolin
 * @date 2023/6/21
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "archives_enterprise_parking请求对象", description = "企业档案-车位信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivesEnterpriseParkingV {
    private Long id;
    @ApiModelProperty("企业档案archives_enterprise_base的主键ID")
    private Long archivesEnterpriseId;
    @ApiModelProperty("1 租赁 2 自购")
    private Integer parkingSpaceType;
    @ApiModelProperty("车位位置")
    private String parkingSpacePosition;
    @ApiModelProperty("车位证明")
    private FileVo parkingSpaceConfirmation;
    @ApiModelProperty("车位生效时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtParkingSpaceEffectiveTime;
    @ApiModelProperty("车位失效时间 yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtParkingSpaceExpireTime;
}
