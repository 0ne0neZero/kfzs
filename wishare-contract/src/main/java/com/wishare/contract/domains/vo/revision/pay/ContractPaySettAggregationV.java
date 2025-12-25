package com.wishare.contract.domains.vo.revision.pay;

import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ContractPaySettAggregationV {

    private ContractPaySettlementConcludeE settlement;

    private List<ContractPayFundE> pidPayFunds;

    private List<ContractPaySettlementConcludeE> preSettlements;

    private Map<String, List<ContractPayFundE>> subPayFundMap;

    private List<ContractPaySettDetailsE> currPaySettDetails;

    private Map<String, List<ContractPaySettDetailsE>> currPaySettDetailMap;

    private Map<String, List<ContractPaySettDetailsE>> currPaySettDetailExtMap;

    private List<ContractPaySettDetailsE> prePaySettDetails;

    private Map<String, List<ContractPaySettDetailsE>> prePaySettDetailMap;

    private Map<String, List<ContractPaySettDetailsE>> prePaySettDetailExtMap;

    public void init() {
        this.pidPayFunds = new ArrayList<>();
        this.preSettlements = new ArrayList<>();
        this.subPayFundMap = new HashMap<>();
        this.currPaySettDetails = new ArrayList<>();
        this.currPaySettDetailMap = new HashMap<>();
        this.currPaySettDetailExtMap = new HashMap<>();
        this.prePaySettDetails = new ArrayList<>();
        this.prePaySettDetailMap = new HashMap<>();
        this.prePaySettDetailExtMap = new HashMap<>();
    }

}
