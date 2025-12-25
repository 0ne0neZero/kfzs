package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_settlement_period")
public class ContractPayConcludeSettlementPeriodE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private Long id;

    @TableField("settlement_id")
    private String settlementId;

    @TableField("start_date")
    private Date startDate;

    @TableField("end_date")
    private Date  endDate;

    public static final String ID = "id";
    public static final String SETTLEMENT_ID = "settlement_id";
}
