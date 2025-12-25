package com.wishare.finance.apps.model.invoice.invoice.fo;

import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author xujian
 * @date 2023/2/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票（无校检）")
public class InvoiceBatchBlueF {

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "购方税号，抬头类型为企业时必填")
    private String buyerTaxNum;

    @ApiModelProperty(value = "购方电话，抬头类型为企业时必填")
    private String buyerTel;

    @ApiModelProperty(value = "购方地址，抬头类型为企业时必填")
    private String buyerAddress;

    @ApiModelProperty("购方银行开户行及账号")
    private String buyerAccount;

    @ApiModelProperty(value = "销方税号", required = true)
    @NotBlank(message = "销方税号不能为空")
    private String salerTaxNum;

    @ApiModelProperty(value = "销方地址",required = true)
    @NotBlank(message = "销方地址不能为空")
    private String salerAddress;

    @ApiModelProperty(value = "销方电话",required = true)
    @NotBlank(message = "销方电话不能为空")
    private String salerTel;

    @ApiModelProperty("销方银行开户行及账号")
    private String salerAccount;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "发票来源：1.开具的发票 2.收入的发票", required = true)
    @NotNull(message = "发票来源不能为空")
    private Integer invSource;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;

    @ApiModelProperty(value = "购方手机（pushMode为1或2时，此项为必填）")
    private String buyerPhone;

    @ApiModelProperty(value = "推送邮箱（pushMode为0或2时，此项为必填）")
    private String email;

    @ApiModelProperty(value = "抬头类型：1个人，2企业", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "开票人")
    @NotBlank(message = "开票人不能为空")
    private String clerk;

    @ApiModelProperty("扩展字段：合同系统（合同名称）")
    private String extendFieldOne;

    @ApiModelProperty(value = "账单类型 0-无账单 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @Valid
    @ApiModelProperty("发票明细")
    @NotNull(message = "发票明细不能为空")
    private List<InvoiceBatchDetailBlueF> invoiceBatchDetailBlueFList;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("成本中心")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("客户id")
    private String customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty(value = "开票单元id",required = true)
    @NotBlank(message = "开票单元id不能为空")
    private String invRecUnitId;

    @ApiModelProperty(value = "开票单元名称",required = true)
    @NotBlank(message = "开票单元名称不能为空")
    private String invRecUnitName;

    @ApiModelProperty("开票金额")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty("是否免税：0不免税，1免税， 默认不免税")
    private Integer freeTax;

    public void checkParam() {
        checkBillType();
    }

    /**
     * 校检账单类型
     */
    private void checkBillType() {
        if (billType != 0) {
            throw BizException.throw400("暂不支持该类型账单开票");
        }
    }
}
