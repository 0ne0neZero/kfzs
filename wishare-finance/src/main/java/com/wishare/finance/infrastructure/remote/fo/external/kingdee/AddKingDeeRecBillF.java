package com.wishare.finance.infrastructure.remote.fo.external.kingdee;


import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddKingDeeRecBillF {

    /**
     * 第三方单据编号
     */
    private String mybillno;
    /**
     * 业务日期
     */
    private String bizdate;
    /**
     * 业务日期（取推送日期）
     */
    private String billdate;
    /**
     * 结转日期（取推送时间）
     */
    private String jzdate;
    /**
     * 客户
     */
    private String customer;
    /**
     * 结算方式
     */
    private String settletype;
    /**
     * 摘要
     */
    private String description;
    /**
     * 组织
     */
    private String org;
    /**
     * 分录
     */
    private List<KingDeeRecBillEntry> entrys;
}
