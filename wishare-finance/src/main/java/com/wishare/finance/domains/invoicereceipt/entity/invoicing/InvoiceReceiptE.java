package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.expensereport.enums.KingDeePushStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 发票收据主表(InvoiceReceipt)实体类
 * 发票收据主表(invoice_receipt)
 * @author makejava
 * @since 2022-09-23 11:37:05
 * {@link com.wishare.finance.domains.invoicereceipt.d.InvoiceReceiptDo;}
 */
@Getter
@Setter
@TableName("invoice_receipt")
public class InvoiceReceiptE  {
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
          8：全电普票
          9：全电专票
          10：定额发票
     */
    private Integer type;
    /**
     * 票据类型中文
     */
    @TableField(exist = false)
    private String typeName = "";
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
     * 推送状态：0和空未推送  1以推送
     */
    private Integer voidPushState;

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
    @TableField(exist = false)
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
     * 系统来源：0 未知系统 1 收费系统 2合同系统
     */
    private Integer sysSource;
    /**
     * 来源：1 系统生成 2 外部导入
     */
    private Integer source = 1;
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
     * 扩展字段：推凭/报账 状态（拈花湾金蝶报账）
     */
    private String extendFieldTwo = KingDeePushStateEnum.未推送.getCode();

    /**
     * 扩展字段：推凭/报账 错误信息（拈花湾金蝶报账）
     */
    private String extendFieldThree;

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

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    public InvoiceReceiptE() {
        generateIdentifier();
    }

    public static List<InvoiceReceiptStateEnum> succeedStates(){
        return List.of(InvoiceReceiptStateEnum.开票成功, InvoiceReceiptStateEnum.已红冲, InvoiceReceiptStateEnum.部分红冲);
    }

    public static List<Integer> succeedStateCodes(){
        return succeedStates().stream().map(InvoiceReceiptStateEnum::getCode).collect(Collectors.toList());
    }

    /**
     * 构造id和编号
     */
    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_receipt_id"));
        }
    }

    /**
     * 生成开票时间
     */
    public void generateInvoiceTime(IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        setCreator(identityInfo.getUserId());
        setCreatorName(identityInfo.getUserName());
        setOperator(identityInfo.getUserId());
        setOperatorName(identityInfo.getUserName());
        setGmtModify(now);
        setGmtCreate(now);
        setTenantId(identityInfo.getTenantId());
        setBillingTime(now);
    }

    public void verify(List<InvoiceReceiptDetailE> invoiceReceiptDetailEList) {
        if (CollectionUtils.isNotEmpty(invoiceReceiptDetailEList)) {
            Long totalAmount = invoiceReceiptDetailEList.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
            if (totalAmount.compareTo(priceTaxAmount) != 0) {
                throw BizException.throw400("开票金额" + priceTaxAmount +
                        "错误，可开票金额为" + totalAmount);
            }
        }
    }
}

