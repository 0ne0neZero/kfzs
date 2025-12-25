package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
@TableName("contract_pay_conclude_settlement_contract_snapshot")
public class ContractPayConcludeSettlementContractSnapshotE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private Long id;

    private String settlementId;

    private String contractId;

    private String contractNo;

    private String contractName;

    private String conmaincode;

    private String communityId;

    private String communityName;

    private BigDecimal contractAmountOriginalRate;

    private BigDecimal contractAmount;

    private BigDecimal changContractAmount;

    private String ourPartyId;

    private String ourParty;

    private String oppositeOneId;

    private String oppositeOne;

    private String oppositeTwoId;

    private String oppositeTwo;

    private String qydws;

    public static final String ID = "id";
}
