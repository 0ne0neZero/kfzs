package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 合同空间资源信息
* </p>
*
* @author wangrui
* @since 2022-12-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_space_resources", description = "合同空间资源信息")
public class ContractSpaceResourcesF {

    @ApiModelProperty("主键")
    @NotNull(message = "主键不可为空")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("资源编码")
    private String code;
    @ApiModelProperty("资源名称")
    private String name;
    @ApiModelProperty("资源分类")
    private String category;
    @ApiModelProperty("资源位置")
    private String position;
    @ApiModelProperty("资源业态")
    private String businessType;
    @ApiModelProperty("定价标准")
    private String pricingStandard;
    @ApiModelProperty("资源单价")
    private String resourceRates;
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @ApiModelProperty("资源状态")
    private String state;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;
    @ApiModelProperty("数量/面积")
    private String quantityArea;
    @ApiModelProperty("总价（元）")
    private BigDecimal totalPrice;
}
