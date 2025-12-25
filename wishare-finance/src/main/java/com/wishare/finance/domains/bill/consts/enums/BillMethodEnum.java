package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 计费方式枚举
 */
@Slf4j
public enum BillMethodEnum {

    PRICE_AREA_MONTH(1, "单价*面积*月"),
    PRICE_MONTH(2, "单价*月"),
    PRICE_AREA_DAY(3, "单价*面积*天"),
    PRICE_DAY(4, "单价*天"),
    PRICE_DEGREE(5, "单价*度"),
    PRICE_CUBIC_METER(6, "单价*m³"),
    PRICE_TON(7, "单价*吨"),
    PRICE_FIXED(8, "固定金额"),

    PRICE_OVERDUE(9,"违约单价*用量*违约比率*天"),
    PRICE_COUNT(10,"单价*数量"),
    PRICE_KWH(11, "单价*千瓦时"),
    PRICE_AREA(12, "单价*m²")
    ;

    private Integer type;
    private String message;

    BillMethodEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public final static Set<BillMethodEnum> INSTRUMENT_METHOD =
            Set.of(PRICE_DEGREE, PRICE_TON, PRICE_CUBIC_METER);

    public final static Predicate<BillMethodEnum> IS_INSTRUMENT_METHOD = INSTRUMENT_METHOD::contains;

    /**
     * 构造计量描述
     *
     * @param billMethod
     * @param bills
     * @param chargingAreaSum
     * @return
     */
    public static String handleNumStr(Integer billMethod, List<BillDetailMoreV> bills, BigDecimal chargingAreaSum, Integer chargeCount) {
        String numStr = "";
        if (null == billMethod) {
            return numStr;
        }
        if (chargingAreaSum != null) {
            chargingAreaSum = chargingAreaSum.setScale(2, RoundingMode.HALF_UP);
        }
        BillMethodEnum billMethodEnum = valueOfByCode(billMethod);
        switch (billMethodEnum) {
            //单价*天
            case PRICE_DAY:
            //单价*月
            case PRICE_MONTH:
            //固定金额
            case PRICE_FIXED:
                break;
            //单价*吨
            case PRICE_TON:
                numStr = chargingAreaSum.toString() + "吨" + handleMeterReading(bills);
                break;
            //单价*度
            case PRICE_DEGREE:
                numStr = chargingAreaSum.toString()+"度" + handleMeterReading(bills);
                break;
            //单价*千瓦时
            case PRICE_KWH:
                numStr = chargingAreaSum.toString()+"度" + handleMeterReading(bills);
                break;
            //单价*面积*天
            case PRICE_AREA_DAY:
            //单价*面积*月
            case PRICE_AREA_MONTH:
                numStr = chargingAreaSum + "m²";
                break;
            //单价*m³
            case PRICE_CUBIC_METER:
                numStr = chargingAreaSum.toString() +"m³" + handleMeterReading(bills);
                break;
            case PRICE_OVERDUE: //违约单价*用量*违约比率*天
                //numStr = "1*" + size + "";
                break;
            case PRICE_COUNT: //单价*数量
                numStr = chargeCount + "个";
                break;
        }
        return numStr;
    }

    /**
     * 处理账单中的表读数信息
     * @param bills
     * @return
     */
    public static String handleMeterReading(List<BillDetailMoreV> bills) {
        String reading = "";
        try {
            BigDecimal min = null;
            BigDecimal max = null;
            for (BillDetailMoreV billDetailMoreV : bills) {
                String extField3 = billDetailMoreV.getExtField3();
                String extField4 = billDetailMoreV.getExtField4();
                if (StringUtils.isNotBlank(extField3)) {
                    BigDecimal tempMin = new BigDecimal(extField3);
                    if (min == null || min.compareTo(tempMin) > 0) {
                        min = tempMin;
                    }
                    BigDecimal temMax = new BigDecimal(extField3);
                    if (max == null || max.compareTo(temMax) < 0) {
                        max = temMax;
                    }
                }
                if (StringUtils.isNotBlank(extField4)) {
                    BigDecimal tempMin = new BigDecimal(extField4);
                    if (min == null || min.compareTo(tempMin) > 0) {
                        min = tempMin;
                    }
                    BigDecimal temMax = new BigDecimal(extField4);
                    if (max == null || max.compareTo(temMax) < 0) {
                        max = temMax;
                    }
                }
            }
            if (min != null && max != null) {
                //当前2个字段可能存在读出处理反的情况
                reading = "(" + min.min(max) + "-" + min.max(max) + ")";
            }
        } catch (Exception e) {
            log.error("处理表读数失败", e);
        }
        return reading;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static BillMethodEnum valueOfByCode(int code) {
        for (BillMethodEnum ee : BillMethodEnum.values()) {
            if (ee.getType() == code) {
                return ee;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_METHOD_NOT_SUPPORT.msg());
    }

    public static Boolean needArea(Integer type) {
        if (BillMethodEnum.PRICE_AREA_MONTH.getType().equals(type)
                || BillMethodEnum.PRICE_AREA_DAY.getType().equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean needPanel(Integer type) {
        if (BillMethodEnum.PRICE_DEGREE.getType().equals(type) ||  BillMethodEnum.PRICE_KWH.getType().equals(type)
                || BillMethodEnum.PRICE_CUBIC_METER.getType().equals(type)
                || BillMethodEnum.PRICE_TON.getType().equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static BillMethodEnum getBillMethodEnumByName(String name) {
        for (BillMethodEnum objectEnum : BillMethodEnum.values()) {
            if (name.equals(objectEnum.getMessage())) {
                return objectEnum;
            }
        }
        return null;
    }

    public static BillMethodEnum getBillMethodEnumByCode(Integer code) {
        for (BillMethodEnum objectEnum : BillMethodEnum.values()) {
            if (code.equals(objectEnum.getType())) {
                return objectEnum;
            }
        }
        return null;
    }


    public static String getBillMethodName(Integer billMethod, BigDecimal unitPrice, BigDecimal chargingArea) {
        String billMethodName = "";
        if (billMethod == null) {
            billMethodName = "";
        } else if (PRICE_AREA_MONTH.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*" + chargingArea + "㎡" + "*月";
        } else if (PRICE_MONTH.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*月";
        } else if (PRICE_AREA_DAY.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*" + chargingArea + "㎡" + "*天";
        } else if (PRICE_DAY.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*天";
        } else if (PRICE_DEGREE.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*度";
        } else if (PRICE_KWH.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*千瓦时";
        }else if (PRICE_CUBIC_METER.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*m³";
        } else if (PRICE_TON.getType().equals(billMethod)) {
            billMethodName = unitPrice + "元*吨";
        } else if (PRICE_FIXED.getType().equals(billMethod)) {
            billMethodName = "固定金额";
        }
        return billMethodName;
    }

    /**
     * 构造计量描述
     *
     * @param billMethod
     * @param chargingAreaSum
     * @param chargeCount
     * @return
     */
    public static String huixiangHandleNumStr(Integer billMethod,
                                              List<BillDetailMoreV> bills,
                                              BigDecimal chargingAreaSum,
                                              Integer chargeCount) {
        String numStr = "";
        if (null == billMethod) {
            return numStr;
        }
        int cycle = bills.size();
        BigDecimal chargingArea = bills.get(0).getChargingArea() == null ? BigDecimal.ZERO
                : bills.get(0).getChargingArea().setScale(2, RoundingMode.HALF_UP);
        chargingAreaSum = chargingAreaSum.setScale(2, RoundingMode.HALF_UP);
        BillMethodEnum billMethodEnum = valueOfByCode(billMethod);
        switch (billMethodEnum) {
            //单价*天
            case PRICE_DAY:
            //单价*月
            case PRICE_MONTH:
                numStr = 1 + "*" + cycle;
                break;
            //固定金额
            case PRICE_FIXED:
                numStr = "1";
                break;
            //单价*吨
            case PRICE_TON:
                numStr = chargingAreaSum.toString() + "吨" + handleMeterReading(bills);
                break;
            //单价*度
            case PRICE_DEGREE:
                numStr = chargingAreaSum.toString()+"度" + handleMeterReading(bills);
                break;
            //单价*面积*天
            case PRICE_AREA_DAY:
            //单价*面积*月
            case PRICE_AREA_MONTH:
                numStr = chargingArea + "m²" + "*" + cycle;
                break;
            //单价*m³
            case PRICE_CUBIC_METER:
                numStr = chargingAreaSum.toString() +"m³" + handleMeterReading(bills);
                break;
            case PRICE_OVERDUE: //违约单价*用量*违约比率*天
                //numStr = "1*" + size + "";
                break;
            //单价*数量
            case PRICE_COUNT:
                //拈花湾
                if(TenantUtil.bf64()){
                    numStr = chargeCount + "*" + cycle;
                }else{
                    numStr = chargeCount + "个" + "*" + cycle;
                }

                break;
            //单价*面积
            case PRICE_AREA:
                numStr = chargingArea + "m²" + "*" + chargeCount;
                break;
        }
        return numStr;
    }

    /**
     * 中交定制构造计量描述
     *
     * @param billMethod
     * @param bills
     * @param chargingAreaSum
     * @return
     */
    public static String zhongjiaoHandleNumStr(Integer billMethod, List<BillDetailMoreV> bills, BigDecimal chargingAreaSum, Integer chargeCount) {
        String numStr = "";
        if (null == billMethod) {
            return numStr;
        }
        BigDecimal chargingArea = bills.get(0).getChargingArea() == null ? BigDecimal.ZERO
                : bills.get(0).getChargingArea().setScale(2, RoundingMode.HALF_UP);
        BillMethodEnum billMethodEnum = valueOfByCode(billMethod);
        switch (billMethodEnum) {
            //单价*天
            case PRICE_DAY:
                //单价*月
            case PRICE_MONTH:
                //固定金额
            case PRICE_FIXED:
                break;
            //单价*吨
            case PRICE_TON:
                numStr = chargingArea + "吨" + handleMeterReading(bills);
                break;
            //单价*度
            case PRICE_DEGREE:
                //单价*千瓦时
            case PRICE_KWH:
                numStr = chargingArea+"度" + handleMeterReading(bills);
                break;
            //单价*面积*天
            case PRICE_AREA_DAY:
                //单价*面积*月
            case PRICE_AREA_MONTH:
                numStr = chargingArea + "m²";
                break;
            //单价*m³
            case PRICE_CUBIC_METER:
                numStr = chargingArea +"m³" + handleMeterReading(bills);
                break;
            case PRICE_OVERDUE: //违约单价*用量*违约比率*天
                //numStr = "1*" + size + "";
                break;
            case PRICE_COUNT: //单价*数量
                numStr = chargeCount + "个";
                break;
        }
        return numStr;
    }

}
