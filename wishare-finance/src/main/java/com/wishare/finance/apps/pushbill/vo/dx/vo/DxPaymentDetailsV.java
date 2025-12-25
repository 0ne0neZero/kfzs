package com.wishare.finance.apps.pushbill.vo.dx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author longhuadmin
 * DO
 */
@Data
public class DxPaymentDetailsV {

    private String billId;

    private String voucherBillNo;
    /**
     * 用来做分组
     **/
    private String changeName;

    private String changeCode;

    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date accountDate;

    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date payTime;

    private String provisionStatus;

    private String settlementStatus;

    private String paymentId;

    private String paymentCode;

    private String paymentName;

    private String signedPaymentId;

    private String signedPaymentCode;

    private String signedPaymentName;

    private BigDecimal notSettlementAmount;

    private BigDecimal taxExcludAmount;

    private BigDecimal taxIncludAmount;

    private String communityId;

    private String communityName;


    private String contractId;

    private String contractName;

    /**
     * 这个是cfg配置的字典值,和tax_rate表无关
     **/
    private String taxRateId;

    private String taxType;

    private BigDecimal taxRate;
    /**
     * 税额
     */
    private BigDecimal taxAmount;

    private String subjectId;

    private String subjectName;




    /**
     * 根据传入的
     **/
    public void buildChangeName(Integer eventType){
        if (eventType == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
                eventType == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
            this.changeName = "实际应付款";
            this.changeCode = "04";
        }
        if (eventType == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
            if (Objects.nonNull(payTime) &&
                    Objects.nonNull(accountDate) &&
                    payTime.before(accountDate)) {
                this.changeName = "核销预付款";
                this.changeCode = "02";
            } else {
                this.changeName = "实际应付款";
                this.changeCode = "04";
            }
        }
        if (eventType == ZJTriggerEventBillTypeEnum.收入确认计提.getCode() ||
                eventType == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
            this.changeName = "实际应收款";
            this.changeCode = "03";
        }
        if (eventType == ZJTriggerEventBillTypeEnum.收入确认实签.getCode()){
            if (Objects.nonNull(payTime) &&
                    Objects.nonNull(accountDate) &&
                    payTime.before(accountDate)) {
                this.changeName = "核销预收款";
                this.changeCode = "02";
            } else {
                this.changeName = "实际应收款";
                this.changeCode = "03";
            }
        }
    }

    public void buildPaymentInfo(Integer eventType){
        if (eventType == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
            this.paymentId = signedPaymentId;
            this.paymentCode = signedPaymentCode;
            this.paymentName = signedPaymentName;
            if (StringUtils.equals("核销预付款",changeName)){
                this.paymentName = null;
            }
        }
        if (eventType == ZJTriggerEventBillTypeEnum.收入确认实签.getCode() &&
                StringUtils.equals("核销应收款",changeName)){
            this.paymentId = signedPaymentId;
            this.paymentCode = signedPaymentCode;
            this.paymentName = signedPaymentName;
            if (StringUtils.equals("核销应收款",changeName)){
                this.paymentName = null;
            }
        }
    }


    /**
     * 收入确认-分组key 宽项明细
     **/
    public String groupKeyForIncome(){
        return changeName+"_"+paymentName+"_"+taxRateId+"_"+contractId;
    }

    /**
     * 收入确认-分组key-通用确认明细
     **/
    public String groupKeyForIncomeGeneralForJT(){
        return contractId+"_"+ paymentId +"_"+ taxRate;
    }

    /**
     * 收入确认-分组key-通用确认明细
     **/
    public String groupKeyForIncomeGeneralForSQ(){
        return contractId+"_"+ signedPaymentId +"_"+ taxRate;
    }
}
