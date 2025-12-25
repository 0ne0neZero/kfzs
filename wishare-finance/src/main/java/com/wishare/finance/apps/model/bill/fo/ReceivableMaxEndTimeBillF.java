package com.wishare.finance.apps.model.bill.fo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhenghui
 * @date 2023/4/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("应收账单查询最大账单结束时间入参")
public class ReceivableMaxEndTimeBillF {

    @ApiModelProperty(value = "上级收费单元id（如:项目）", required = true)
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "收费单元id（如:房号）")
    private String cpUnitId;

    @ApiModelProperty(value = "房号id集合")
    private List<Long> roomIdList;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private String chargeItemId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;
}
