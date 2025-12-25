package com.wishare.contract.apps.fo.revision.pay.report;

import com.wishare.contract.apps.fo.revision.pay.ContractQydwsF;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/6/3 11:56
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPayDataListD {
    //合同ID
    private String contractId;
    //合同名称
    private String contractName;
    //区域
    private String region;
    //合同服务类型：1.四保一服
    private Integer contractServeType;
    //项目ID
    private String communityId;
    //项目名称
    private String communityName;
    //签约单位
    private String qydws;
    //计划结算金额
    private BigDecimal plannedCollectionAmount;
    //应结算金额
    private BigDecimal shouldPlannedCollectionAmount;
    //补充协议未拆分计划数据
    private Integer bcwcfNum;
    //已结算期数
    private Integer oldNum = 0;
    //应结算期数
    private Integer shouldNum;
    //计划ID集合
    private String planIdList;

    //------后端逻辑计算------
    //private List<ContractQydwsF> qydwsList;
    //未结算金额
    private BigDecimal noSettlementAmount;
    //未结算期数
    private Integer noSettlementNum = 0;
    //YJ合同对应的原合同
    private String yjPidContractId;
    //Nk状态（0.未开启；1.已开启;2.已关闭；3.关闭中）
    private Integer nkStatus;
}
