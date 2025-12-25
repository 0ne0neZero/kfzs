package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZJRequestBody {
    /**
     * 接口标识 默认值：CCCCInterface
     */
    private String taskType = "CCCCInterface";
    private Object context;
    /**
     * 回传标识 固定值，false
     */
    private String requireCallback = "false";
}
