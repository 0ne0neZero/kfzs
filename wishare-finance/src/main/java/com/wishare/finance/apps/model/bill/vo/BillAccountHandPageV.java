package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.entity.PayWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交账信息表
 *
 * @author dxclay
 * @since 2023-01-11
 */
@Getter
@Setter
@ApiModel("交账信息分页信息")
public class BillAccountHandPageV {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "费项类型：1常规收费类型 2临时收费类型 3押金收费类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单")
    private Integer billType;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "实际应收款金额")
    private Long payableAmount;

    @ApiModelProperty(value = "收款金额")
    private Long payAmount;

    @ApiModelProperty(value = "收费时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "票据编号列表")
    private List<String> invoiceNos;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount;

    @ApiModelProperty(value = "票据总额")
    private Long invoiceTotalAmount;

    @ApiModelProperty(value = "增值税普通发票	1: 增值税普通发票	2: 增值税专用发票	3: 增值税电子发票	4: 增值税电子专票	5: 收据	6：电子收据	7:纸质收据	8：全电普票")
    private List<Integer> invoiceTypes;

    @ApiModelProperty(value = "开票时间")
    private LocalDateTime invoiceTime;

    @ApiModelProperty(value = "是否挂账：0未挂账，1已挂账，2已销账")
    private Integer onAccount;

    @ApiModelProperty(value = "交账状态：0未交账，1部分交账，2已交账")
    private Integer accountHanded;

    @ApiModelProperty(value = "交账日期")
    private LocalDateTime handTime;

    @ApiModelProperty(value = "收款方式 [{\"payWay\": 1,\"payChannel\": \"ALIPAY\"}]")
    private List<PayWay> payWays;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

}
