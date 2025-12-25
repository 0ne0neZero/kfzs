package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BILLDATA {
    private String code;
    /**
     * 是否是主表
     */
    private String isMain;
    /**
     * 数据
     */
    private Object data;
}
