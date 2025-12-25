package com.wishare.finance.apps.model.bill.fo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangfulong
 * @date 2023/10/30
 * @Description:
 */
@Getter
@Setter
@ApiModel("创建收款单入参")
public class UpdateGatherBillF {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("账单ID")
    private Long id;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("账单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "账单金额（单位：分）",required = true)
    @NotNull(message = "账单金额不能为空")
    private Long totalAmount;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元不能为空")
    private String supCpUnitId;
}
