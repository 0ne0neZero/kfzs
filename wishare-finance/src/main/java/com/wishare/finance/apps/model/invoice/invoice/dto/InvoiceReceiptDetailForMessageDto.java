package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Getter
@Setter
public class InvoiceReceiptDetailForMessageDto {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 账单编号
     */
    private String billNo;
    /**
     * 房号ID
     */
    private String roomId;
    /**
     * 房号名称
     */
    private String roomName;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private String num;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单价含税标志：0:不含税,1:含税
     */
    private Integer withTaxFlag;
    /**
     * 单价
     */
    private String price;
    /**
     * 规格型号
     */
    private String spectype;

    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 账单开始时间
     */
    private LocalDateTime billStartTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime billEndTime;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 账单结算金额
     */
    private Long settleAmount;

    /**
     * 账单的开票金额
     */
    private Long invoiceAmount;

    /**
     * 账单所处开票的价税合计
     */
    private Long priceTaxAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    private String tenantId;

}
