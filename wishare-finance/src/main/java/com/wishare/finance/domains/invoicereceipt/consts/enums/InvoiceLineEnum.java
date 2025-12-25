package com.wishare.finance.domains.invoicereceipt.consts.enums;

import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.EnvConstEnum;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 发票种类 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据  7：纸质收据
 *
 * p,普通发票(电票)(默认);c,普通
 * 发票(纸票);s,专用发票;e,收购发票(电票);f,
 * 收购发票(纸质);r,普通发票(卷式);b,增值税
 * 电子专用发票;j,机动车销售统一发票;u,二手
 * 车销售统一发票;bs:电子发票(增值税专用
 * 发票)-即全电专票,pc:电子发票(普通发票)-
 * 即全电普票
 */
public enum InvoiceLineEnum {

    增值税普通发票(1, "增值税普通发票","c"),
    增值税专用发票(2, "增值税专用发票","s"),
    增值税电子发票(3, "增值税电子发票","p"),
    增值税电子专票(4, "增值税电子专票","b"),
    收据(5, "收据",""),
    电子收据(6, "电子收据",""),
    纸质收据(7, "纸质收据",""),
    全电普票(8, "全电普票","pc"),
    全电专票(9, "全电专票","bs"),
    定额发票(10, "定额发票",""),


    ;

    private Integer code;

    private String des;

    private String nuonuoCode;

    public static InvoiceLineEnum valueOfByCode(Integer code) {
        InvoiceLineEnum e = null;
        for (InvoiceLineEnum ee : InvoiceLineEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public String getNuonuoCode() {
        return nuonuoCode;
    }

    InvoiceLineEnum(Integer code, String des, String nuonuoCode) {
        this.code = code;
        this.des = des;
        this.nuonuoCode = nuonuoCode;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    /**
     * 根据编号来进行路由开票方式
     * @param type
     * @return
     */
    static public boolean getWayStatus(Integer type) {
        if (InvoiceLineEnum.增值税电子发票.getCode().equals(type)
                || InvoiceLineEnum.全电普票.getCode().equals(type)
                || InvoiceLineEnum.全电专票.getCode().equals(type)
                || (InvoiceLineEnum.增值税专用发票.getCode().equals(type) && EnvConst.FANGYUAN.equals(EnvData.config))) {
            return true;
        }
        return false;
    }

}
