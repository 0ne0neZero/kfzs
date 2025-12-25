package com.wishare.finance.apps.model.yuanyang.fo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.infrastructure.support.ApiData;

/**
 * 远洋报销回调数据
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/5/4
 */
public class ReimbursementCallbackF {

    /**
     * 报销类型 ： 1支付报销
     */
    private String type;

    /**
     * 报销原数据
     */
    private String metaData;

    /**
     * 租户id
     */
    private String tenantId;

    public ReimbursementCallbackF() {
    }

    public ReimbursementCallbackF(String type, String metaData) {
        this.type = type;
        this.metaData = metaData;
        this.tenantId = ApiData.API.getTenantId().orElse(null);
    }

    public static ReimbursementCallbackF formatReimburse(ReimburseCompensateF reimburseCompensateF){
        return new ReimbursementCallbackF(ReimburseType.REIMBURSE.name(), JSON.toJSONString(reimburseCompensateF));
    }

    public static ReimbursementCallbackF formatTravelReimburse(TravelReimburseCompensateF travelReimburseCompensateF){
        return new ReimbursementCallbackF(ReimburseType.TRAVEL.name(), JSON.toJSONString(travelReimburseCompensateF));
    }

    public static ReimbursementCallbackF formatTurnoverFunds(TurnoverFundsCompensateF turnoverFundsCompensateF){
        return new ReimbursementCallbackF(ReimburseType.TURNOVER.name(), JSON.toJSONString(turnoverFundsCompensateF));
    }
    public static ReimbursementCallbackF formatAllocateFunds(AllocateFundsCompensateF allocateFundsCompensateF){
        return new ReimbursementCallbackF(ReimburseType.ALLOCATE.name(), JSON.toJSONString(allocateFundsCompensateF));
    }

    public static ReimbursementCallbackF formatBusinessFunds(BusinessFundsCompensateF businessFundsCompensateF){
        return new ReimbursementCallbackF(ReimburseType.GENERAL.name(), JSON.toJSONString(businessFundsCompensateF));
    }

    public ReimburseCompensateF parseReimbursement(){
        return JSON.parseObject(metaData, ReimburseCompensateF.class);
    }

    public TravelReimburseCompensateF parseTravelReimburse(){
        return JSON.parseObject(metaData, TravelReimburseCompensateF.class);
    }
    public TurnoverFundsCompensateF parseTurnoverFunds(){
        return JSON.parseObject(metaData, TurnoverFundsCompensateF.class);
    }
    public AllocateFundsCompensateF parseAllocateFunds(){
        return JSON.parseObject(metaData, AllocateFundsCompensateF.class);
    }

    public BusinessFundsCompensateF parseBusinessFunds() {
        return JSON.parseObject(metaData, BusinessFundsCompensateF.class);
    }
    public String getType() {
        return type;
    }

    public ReimbursementCallbackF setType(String type) {
        this.type = type;
        return this;
    }

    public String getMetaData() {
        return metaData;
    }

    public ReimbursementCallbackF setMetaData(String metaData) {
        this.metaData = metaData;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ReimbursementCallbackF setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
}
