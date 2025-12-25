package com.wishare.finance.infrastructure.remote.fo;

import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/10 14:32
 * @version: 1.0.0
 */
@Getter
@Setter
public class VoucherRuleF {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 触发事件类型
     */
    private Integer eventType;

    /**
     * 方案名称
     */
    private String ruleName;

    /**
     * 过滤条件
     * [{
     *  "type": "法定单位",
     *  "compare": "eq", //相等
     *  "data": [xxx,xx] // 内容
     * }]
     */
    private String conditions;

    /**
     * 凭证摘要
     */
    private String ruleRemark;

    /**
     * 是否启用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 是否红字凭证： 0否  1是
     */
    private Integer red;

    /**
     * 是否含税：0否 1是
     */
    private Integer taxIncluded;

    /**
     * 选中的过滤条件
     */
    private String filterConditions;

    /**
     * 借贷分录科目id
     */
    private Long subjectSystemId;

    /**
     * 借贷分录
     * [{
     *  "type": "debit/icredit", // 借/贷方向
     *  "subjectId": "xxx", //分类科目id
     *  "content": "xxx" // 辅助核算内容
     * }]
     */
    private String entries;

    /**
     * 排序 从小到大排序
     */
    private Integer sortNum;

    /**
     * 1-即时推送/0-定时推送/2-手动推送
     */
    private Integer executeType;

    /**
     * 推凭时间
     * {
     *   "frequency": "day/month/year",
     *   "time": "xxx"
     * }
     */
    private String executeTime;

    /**
     * 租户id
     */
    private String tenantId;

}
