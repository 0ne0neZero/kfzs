package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBondCollectionDetailFieldConst;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
/**
 * <p>
 * 保证金计划收款明细
 * </p>
 *
 * @author ljx
 * @since 2022-10-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bond_collection_detail")
public class ContractBondCollectionDetailE{

    /**
     * id
     */
    @TableId(value = ContractBondCollectionDetailFieldConst.ID)
    private Long id;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 保证金计划id
     */
    private Long bondPlanId;

    /**
     * 收款金额
     */
    private BigDecimal collectionAmount;

    /**
     * 收款方式  0现金  1银行转帐  2汇款  3支票
     */
    private Integer collectionMethod;

    /**
     * 收款流水号
     */
    private String collectionNumber;

    /**
     * 收款凭证文件集
     */
    private String receiptVoucher;

    /**
     * 收款凭证文件名称
     */
    private String receiptVoucherName;

    /**
     * 收款时间
     */
    private LocalDateTime collectionTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
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

    /**
     * 收款编码
     */
    private String collectionCode;
}
