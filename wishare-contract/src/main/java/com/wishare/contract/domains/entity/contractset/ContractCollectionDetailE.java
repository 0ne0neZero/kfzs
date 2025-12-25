package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractCollectionDetailFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同收款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_collection_detail")
public class ContractCollectionDetailE {

    /**
     * id
     */
    @TableId(value = ContractCollectionDetailFieldConst.ID, type = IdType.AUTO)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 收款计划id
     */
    private Long collectionPlanId;

    /**
     * 开票信息
     */
    private String invoice;

    /**
     * 收款编号
     */
    private String receiptNumber;

    /**
     * 收款流水号
     */
    private String serialNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 本次收款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 收款方式   0现金  1网上转账  2支付宝  3微信
     */
    private Integer collectionType;

    /**
     * 收款凭证文件集
     */
    private String receiptVoucher;

    /**
     * 收款凭证文件名集
     */
    private String receiptVoucherName;

    /**
     * 收款人id
     */
    private String userId;

    /**
     * 收款人姓名
     */
    private String userName;

    /**
     * 收款时间
     */
    private LocalDateTime collectionTime;

    /**
     * 租户id
     */
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
     * 创建时间
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
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;


}
