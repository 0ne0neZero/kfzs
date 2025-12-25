package com.wishare.contract.apps.fo.revision;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 计算收付款计划所要的数据
 *
 * @author 龙江锋
 * @date 2023/8/18 14:37
 */
@ApiModel("计算收付款计划所要的数据")
@Data
public class ContractPlanDateF {
    @ApiModelProperty("合同ID")
    @NotNull(message = "合同ID不能为空")
    private String contractId;

    /**
     * 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
     */
    @ApiModelProperty("拆分方式")
    @NotNull(message = "拆分方式不可为空")
    private Integer splitMode;

    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "合同开始日期不可为空")
    private LocalDate contractStartTime;

    @ApiModelProperty("合同到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "合同到期时间不可为空")
    private LocalDate contractEndTime;

    @ApiModelProperty("计划总金额")
    @NotNull(message = "计划总金额不可为空")
    private BigDecimal planAllAmount;

    @ApiModelProperty("第几批")
    @NotNull(message = "第几批")
    private Integer howOrder;

}
