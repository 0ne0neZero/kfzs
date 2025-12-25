package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDate;
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
* 合同空间资源信息 保存请求参数
* </p>
*
* @author wangrui
* @since 2022-12-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractSpaceResourcesSaveF {

    @ApiModelProperty("主键")
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
    @ApiModelProperty("资源单位")
    private String resourceUnit;
    @ApiModelProperty("数量/面积")
    private String quantityArea;
    @ApiModelProperty("总价（元）")
    private BigDecimal totalPrice;
    @ApiModelProperty("开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startTime;
    @ApiModelProperty("结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endTime;
    @ApiModelProperty("资源状态")
    private String state;

}
