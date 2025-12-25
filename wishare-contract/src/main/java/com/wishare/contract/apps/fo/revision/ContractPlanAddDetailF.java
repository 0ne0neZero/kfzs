package com.wishare.contract.apps.fo.revision;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 该父类仅做属性复用，不能承载具体的数据传输功能，若要使用对应的属性，请使用子类
 *
 * @author 龙江锋
 * @date 2023/8/17 14:02
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPlanAddDetailF {
    @ApiModelProperty("期数")
    @NotNull(message = "期数不能为空")
    private Integer termDate;

    @ApiModelProperty("计划收付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate planTime;

    @ApiModelProperty("金额比例")
    private String ratio;

    @ApiModelProperty("计划收付款金额(元)")
    private BigDecimal planAmount;

    @ApiModelProperty("服务类型")
    private String serviceType;

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("税率")
    @NotNull(message = "税率不能为空")
    private BigDecimal taxRate;

    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty("税额")
    private BigDecimal taxAmount;

    @ApiModelProperty("备注")
    private String remark;
}
