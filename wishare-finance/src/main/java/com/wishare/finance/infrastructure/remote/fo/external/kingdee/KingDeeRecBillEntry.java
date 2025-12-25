package com.wishare.finance.infrastructure.remote.fo.external.kingdee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KingDeeRecBillEntry {

    /**
     * 含税单价
     */
    private String taxprice;
    /**
     * 不含税单价
     */
    private String price;
    /**
     * 价税合计
     */
    private String totamount;
    /**
     * 不含税金额
     */
    private String amount;
    /**
     * 税率（%）
     */
    private String taxrate;
    /**
     * 税额
     */
    private String taxamount;
    /**
     * 项目
     */
    private String item;
    /**
     * 业务版块
     */
    private String yewubk;
    /**
     * 收费系统费项
     */
    private String feeitem;
}
