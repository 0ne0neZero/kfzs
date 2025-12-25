package com.wishare.finance.domains.voucher.consts.enums.bpm;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * BPM 凭证流程业务类型
 */
public enum BusinessBillTypeEnum {

    借款申请("001", "借款申请"),
    部门费用报销("003", "部门费用报销"),
    福利费报销("030", "福利费报销"),
    七项费用报销("002", "七项费用报销"),
    工会经费报销("006", "工会经费报销"),
    职工教育经费报销("007", "职工教育经费报销"),
    员工存档费报销("008", "员工存档费报销"),
    差旅费报销("010", "差旅费报销"),
    营业外支出("5502", "营业外支出"),
    预付款("YFK", "预付款"),
    能源费分摊("NYFFT", "能源费分摊"),
    资金上缴("ZJSJ", "资金上缴"),
    资金下拨("ZJXB", "资金下拨"),
    资金下拨付款("ZJXBFK", "资金下拨付款"),
    资产采买("CGDD", "资产采买"),
    携程对账("XCDZ", "携程对账"),
    工资支付("GZZF", "工资支付"),
    零星服务("LXFW", "零星服务"),
    退还已收款("THYSH", "退还已收款"),

    工资计提("GZJT", "工资计提"),
    工资发放("GZFF", "工资发放"),
    工资冲回("GZCH", "工资冲回"),

    社保计提("SBJT", "社保计提"),
    社保发放("SBFF", "社保发放"),
    社保冲回("SBCH", "社保冲回"),
    ;

    private String code;
    private String value;

    public static final List<String > WAGE_SET;
    public static final List<String > SOCIAL_SET;
    static {
        WAGE_SET = Arrays.asList(工资计提.getCode(), 工资发放.getCode(), 工资冲回.getCode());
        SOCIAL_SET = Arrays.asList(社保计提.getCode(), 社保发放.getCode(), 社保冲回.getCode());
    }
    BusinessBillTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return StringUtils.equals(code, this.code);
    }
}
