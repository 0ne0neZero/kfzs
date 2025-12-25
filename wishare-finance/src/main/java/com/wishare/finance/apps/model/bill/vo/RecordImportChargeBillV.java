package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.dto.ImportBillDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yyx
 * @project wishare-finance
 * @title RecordImportChargeBillV
 * @date 2023.10.12  11:43
 * @description
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "导入补录应收账单结果信息", parent = RecordImportChargeBillV.class)
public class RecordImportChargeBillV  {

    @ApiModelProperty(value = "账单id",required = true)
    private Long id;

    @ApiModelProperty("扩展字段4(是否暂估收入)")
    private String extField4;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty(value = "行号下标，导入的行号标识")
    private Integer index;

    @ApiModelProperty("失败原因")
    private String errorMessage;

    @ApiModelProperty("实收金额")
    private Long actualSettleAmount;

    @ApiModelProperty(value = "导入结果", required = true)
    private boolean result;

    private String errMsg = StringUtils.EMPTY;

    protected Long rowNumber;

}
