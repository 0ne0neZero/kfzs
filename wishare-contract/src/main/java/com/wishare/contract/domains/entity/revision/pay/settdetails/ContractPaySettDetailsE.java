package com.wishare.contract.domains.entity.revision.pay.settdetails;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_settdetails")
public class ContractPaySettDetailsE{

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 关联结算单ID
     */
    private String settlementId;

    /**
     * 款项名称
     */
    private String name;

    /**
     * 款项类型ID
     */
    private String typeId;

    /**
     * 款项类型
     */
    private String type;

    /**
     * 结算金额
     */
    private BigDecimal amount;
    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 费项ID
     */
    private Long chargeItemId;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 税率ID
     */
    private String taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 付费类型ID
     */
    private String payTypeId;

    /**
     * 付费类型
     */
    private String payType;

    /**
     * 付费方式ID
     */
    private String payWayId;

    /**
     * 付费方式
     */
    private String payWay;


    /**
     * 收费标准ID
     */
    private String standardId;

    /**
     * 收费标准
     */
    private String standard;

    private BigDecimal standAmount;

    private BigDecimal taxRateAmount;

    private BigDecimal amountWithOutRate;

    private Integer num;

    private BigDecimal amountNum;

    /**
     * 扩展字段
     */
    private String extField;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合同清单ID
     **/
    private String payFundId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    //成本-合约规划可用金额
    private BigDecimal summarySurplusAmount;
    //成本-分摊明细ID
    private String cbApportionId;
    //计量周期开始时间
    private LocalDate startDate;
    //计量周期结束时间
    private LocalDate endDate;

    /**
     * 合同id
     */
    public static final String ID = "id";
}
