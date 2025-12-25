package com.wishare.finance.domains.invoicereceipt.d;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.base.FinanceBaseEntity;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
 *
 *
 *
 *
 * @see com.wishare.finance.domains.invoicereceipt.d.InvoiceReceiptDo;
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Data
public class InvoiceReceiptDo extends FinanceBaseEntity {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 票据编号
     */
    private String invoiceReceiptNo;
    /**
     * 票据类型
          1: 增值税普通发票
          2: 增值税专用发票
          3: 增值税电子发票
          4: 增值税电子专票
          5: 收据
          6：电子收据
          7：纸质收据
     */
    private Integer type;
    /**
     * 账单类型 0-非账单 1-应收账单， 2-预收账单， 3-临时收费账单
     */
    private Integer billType;
    /**
     * 项目ID
     */
    private String communityId;
    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;
    /**
     * 成本中心
     */
    private Long costCenterId;
    /**
     * 成本中心名称
     */
    private String costCenterName;
    /**
     * 费项id
     */
    private Long chargeItemId;
    /**
     * 费项名称
     */
    private String chargeItemName;
    /**
     * 开票单元id
     */
    private String invRecUnitId;
    /**
     * 开票单元名称
     */
    private String invRecUnitName;
    /**
     * 缴费后开具：0:直接开具;1:缴费后开具;
     */
    private Integer afterPayment;
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String customerPhone;
    /**
     * 业务类型编码
     */
    private String businessCode;
    /**
     * 业务类型名称
     */
    private String businessName;
    /**
     * 申请开票时间
     */
    private LocalDateTime applyTime;
    /**
     * 开具发票时间
     */
    private LocalDateTime billingTime;
    /**
     * 推送方式：-1,不推送（默认）,0,邮箱;1,手机;
     */
    private String pushMode;
    /**
     * 推送状态：0和空未推送  1以推送
     */
    private Integer pushState;
    /**
     * 购方手机（pushMode为1时，此项为必填）
     */
    private String buyerPhone;
    /**
     * 推送邮箱（pushMode为0时，此项为必填）
     */
    private String email;
    /**
     * 税率id
     */
    private Long taxRateId;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 价税合计金额
     */
    private Long priceTaxAmount;
    /**
     * 红冲金额
     */
    private Long redTaxAmount;
    /**
     * 开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废
     */
    private Integer state;
    /**
     * 开票员
     */
    private String clerk;
    /**
     * 备注信息
     */
    private String remark;

    /**
     * 系统来源：1 收费系统 2合同系统
     */
    private Integer sysSource;
    /**
     * 发票来源：1.开具的发票 2.收入的发票
     */
    private Integer invSource;
    /**
     * 认领状态（0 未认领 1 已经认领）
     */
    private Integer claimStatus;

    /**
     * 扩展字段：合同系统（合同名称），收费系统（暂定）
     */
    private String extendFieldOne;
    /**
     * 回调地址
     */
    private String callBackUrl;
    /**
     * 收款单id
     */
    private Long gatherBillId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;
    /**
     * 票据模板ID
     */
    private Long receiptTemplateId;
    /**
     * 票据模板名称
     */
    private String receiptTemplateName;

    /**
     * 收款人
     */
    private String payeeName;

    /**
     * 收款方式
     */
    private String payChannel;







}

