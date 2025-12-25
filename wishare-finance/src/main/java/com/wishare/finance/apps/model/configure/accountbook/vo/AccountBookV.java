package com.wishare.finance.apps.model.configure.accountbook.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Getter
@Setter
@ApiModel("账簿反参")
public class AccountBookV {

    @ApiModelProperty("账簿id")
    private Long id;

    @ApiModelProperty("账簿编码")
    private String code;

    @ApiModelProperty("账簿名称")
    private String name;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("法定单位id路径")
    private List<Long> statutoryBodyIdPath;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("成本中心id路径")
    private List<Long> costCenterIdPath;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项id路径")
    private List<Long> chargeItemIdPath;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("费项属性类型：0 收人,1 支出")
    private Integer chargeItemAttribute;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否总账：0不是，1是")
    private Integer isGeneralLedger;

    @ApiModelProperty("资产金额")
    private BigDecimal assetsAmount;

    @ApiModelProperty("费用金额")
    private BigDecimal costAmount;

    @ApiModelProperty("负责金额")
    private BigDecimal liabilitiesAmount;

    @ApiModelProperty("所有者权益金额")
    private BigDecimal equitiesAmount;

    @ApiModelProperty("收入金额")
    private BigDecimal incomeAmount;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;
}
