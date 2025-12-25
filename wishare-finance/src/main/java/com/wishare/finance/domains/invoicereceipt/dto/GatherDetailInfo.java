package com.wishare.finance.domains.invoicereceipt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright @ 2023 慧享科技 Co. Ltd.
 * All right reserved.
 * <p>
 * 收款单子表
 * </p>
 *
 * @author:jiangfulong
 * @since:2023-8-17
 * @description:
 **/
@Data
public class GatherDetailInfo {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("nc收款单明细表ID")
    private String ncId;
    @ApiModelProperty("nc收款单主表ID")
    private String hncId;
    @ApiModelProperty("支付时间亦或收款单创建时间")
    private String payDate;
    @ApiModelProperty("付款单编号")
    private String payCode;
    @ApiModelProperty("收费清单NcId")
    private String chargeNcId;
    @ApiModelProperty("是否是预收款单 0 否  1 是")
    private Integer preFlag;
    @ApiModelProperty("款项类型名称")
    private String payTypeName;
    @ApiModelProperty("楼栋编码")
    private String buildNo;
    @ApiModelProperty("楼栋名称")
    private String buildName;
    @ApiModelProperty("房产编号")
    private String houseCode;
    @ApiModelProperty("房产名称")
    private String houseName;
    @ApiModelProperty("房号")
    private String houseNo;
    @ApiModelProperty("收费年月")
    private String chargeDate;
    @ApiModelProperty("收费项目")
    private String itemCode;
    @ApiModelProperty("收费项目")
    private String itemName;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("应收本金")
    private Double shoAmount;
    @ApiModelProperty("实收本金")
    private Double amount;
    @ApiModelProperty("应收违约金")
    private Integer shoPenalSum;
    @ApiModelProperty("实收违约金")
    private Integer penalSum;
    @ApiModelProperty("结算方式")
    private String clearingForm;
    @ApiModelProperty("应缴款日期")
    private String payLastDate;
    @ApiModelProperty("费用开始日期")
    private String payStartDate;
    @ApiModelProperty("费用截止日期")
    private String payEndDate;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("收费清单应收金额")
    private Double sfqdAmount;
    @ApiModelProperty("成本中心id")
    private Long costCenterId;
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;
    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;
    @ApiModelProperty("应收单id")
    private Long recBillId;
    @ApiModelProperty("应收单编号")
    private String recBillNo;
}
