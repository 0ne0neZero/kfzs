package com.wishare.contract.domains.entity.revision.income;

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
@TableName("contract_income_conclude_settlement_period")
public class ContractIncomeConcludeSettlementPeriodE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private Long id;

    private String settlementId;

    private Date startDate;

    private Date  endDate;

    public static final String ID = "id";
}
