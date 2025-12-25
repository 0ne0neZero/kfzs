package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.consts.contractset.ContractProfitLossBillFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 损益账单关联表
 * </p>
 *
 * @author ljx
 * @since 2022-10-17
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_profit_loss_bill")
public class ContractProfitLossBillE{

    /**
     * id
     */
    @TableId(value = ContractProfitLossBillFieldConst.ID)
    private Long id;

    /**
     * 收款计划id
     */
    private Long collectionPlanId;

    /**
     * 损益计划id
     */
    private Long profitLossPlanId;

    /**
     * 中台账单id
     */
    private Long billId;

}
