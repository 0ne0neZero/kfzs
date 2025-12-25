package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceRealEstateLeaseInfoF;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 开票记录表(InvoicingRecord)实体类
 *
 * @author makejava
 * @since 2022-09-20 19:58:17
 */
@Getter
@Setter
@TableName(value = "invoice", autoResultMap = true)
public class InvoiceE {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 开票类型：1:蓝票;2:红票
     */
    private Integer invoiceType;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;

    /**
     * 原蓝票发票主表id,可为多个,红票时必填
     */
    private Long blueInvoiceReceiptId;
    /**
     * 发票抬头类型：1 个人 2 企业
     */
    private Integer invoiceTitleType;

    /**
     * 购方名称
     */
    private String buyerName;
    /**
     * 购方税号
     */
    private String buyerTaxNum;
    /**
     * 购方电话
     */
    private String buyerTel;
    /**
     * 购方地址
     */
    private String buyerAddress;
    /**
     * 购方银行开户行及账号
     */
    private String buyerAccount;
    /**
     * 纳税人类别：1小规模纳税人；2一般纳税人；3简易征收纳税人；4政府机关
     */
    @TableField(exist = false)
    private Integer taxpayerType;;
    /**
     * 销方税号
     */
    private String salerTaxNum;
    /**
     * 销方名称
     */
    private String salerName;
    /**
     * 销方电话
     */
    private String salerTel;
    /**
     * 销方地址
     */
    private String salerAddress;
    /**
     * 销方银行开户行及账号
     */
    private String salerAccount;
    /**
     * 机器编码
     */
    private String machineCode;
    /**
     * 分机号
     */
    private Long extensionNumber;
    /**
     * 终端号
     */
    private Long terminalNumber;
    /**
     * 终端代码
     */
    private String terminalCode;
    /**
     * 用户代码
     */
    private String userCode;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;
    /**
     * 发票流水号
     */
    private String invoiceSerialNum;
    /**
     * 发票url地址
     */
    private String invoiceUrl;
    /**
     * 诺诺url地址
     */
    private String nuonuoUrl;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 第三方反参
     */
    private String thridReturnParameter;
    /**
     * 发票明细数据
     */
    private String invoiceDetails;
    /**
     * 推送方式：-1,不推送（默认）,0,邮箱;1,手机;2,邮箱、手机
     */
    private String pushMode;
    /**
     * 推送状态：0和空未推送  1以推送
     */
    private Integer pushState;
    /**
     * 购方手机（pushMode为1或2时，此项为
     必填）
     */
    private String buyerPhone;
    /**
     * 推送邮箱（pushMode为0或2时，此项为
     必填，）
     */
    private String email;
    /**
     * 是否免税：0不免税，1免税， 默认不免税
     */
    private Integer freeTax;
    /**
     * 税额
     */
    private Long taxAmount;
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

    /**
     * 诺诺经营租赁租赁费 开票 不动产的 详细地址
     */
    private String addressInfo;

    /**
     * 建筑服务信息
     */
    @TableField(typeHandler = JSONTypeHandler.class)
    private InvoiceBuildingServiceInfoF buildingServiceInfo;

    /**
     * 不动产经营租赁服务
     */
    @TableField(typeHandler = JSONTypeHandler.class)
    private InvoiceRealEstateLeaseInfoF realEstateLeaseInfo;

    public InvoiceE() {
        setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_id"));
    }

    /**
     * 是否免税：0不免税，1免税， 默认不免税
     */
    public Integer getFreeTax() {
        return Objects.nonNull(freeTax)?freeTax:0;
    }
}

