package com.wishare.contract.domains.vo.revision.projectInitiation.cost;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 动态成本分摊可用余额
 */
@Data
@Accessors(chain = true)
public class DynamicCostSurplusPropReqF {

    @ApiModelProperty("成本费项编码")
    @NotBlank(message = "成本费项编码不能为空")
    private String cbCode;

    @ApiModelProperty("成本费项名称")
    private String cbName;

    @ApiModelProperty("项目id")
    @NotBlank(message = "项目id不能为空")
    private String mdmId;

    @ApiModelProperty("年份")
    private String years;

    @ApiModelProperty("月份")
    private Integer month;

    @ApiModelProperty("数据来源（1立项2合同）")
    @NotNull(message = "数据来源不能为空")
    private Integer sourceType;

    @ApiModelProperty(value = "分摊金额(不含税)")
    @NotNull(message = "分摊金额(不含税)不能为空", groups = {monthlyAllocationReq.class})
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "计划开始时间")
    @NotNull(message = "计划开始时间不能为空")
    private LocalDate planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    @NotNull(message = "计划结束时间不能为空")
    private LocalDate planEndTime;

    @ApiModelProperty(value = "是否校验")
    private Boolean verificationFlag = false;

    public interface DynamicCostSurplusPropReq {

    }
    public interface monthlyAllocationReq {

    }
}
