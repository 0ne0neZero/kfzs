package com.wishare.finance.apps.model.voucher.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
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
@ApiModel("推凭记录")
public class VoucherRuleDTO {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 费项id
     */
    @ApiModelProperty("费项id")
    private Long chargeItemId;

    /**
     * 触发事件类型
     */
    @ApiModelProperty("触发事件类型")
    private Integer eventType;

    /**
     * 方案名称
     */
    @ApiModelProperty("方案名称")
    private String ruleName;

    /**
     * 过滤条件
     * [{
     *  "type": "法定单位",
     *  "compare": "eq", //相等
     *  "data": [xxx,xx] // 内容
     * }]
     */
    @ApiModelProperty("过滤条件\n"
        + "[{\n"
        + " \"type\": \"法定单位\",\n"
        + " \"compare\": \"eq\", //相等\n"
        + " \"data\": [xxx,xx] // 内容\n"
        + "}]")
    private String conditions;

    /**
     * 凭证摘要
     */
    @ApiModelProperty("凭证摘要")
    private String ruleRemark;

    /**
     * 是否启用：0启用，1禁用
     */
    @ApiModelProperty("是否启用：0启用，1禁用")
    private Integer disabled;

    /**
     * 是否红字凭证： 0否  1是
     */
    @ApiModelProperty("是否红字凭证： 0否  1是")
    private Integer red;

    /**
     * 是否含税：0否 1是
     */
    @ApiModelProperty("是否含税：0否 1是")
    private Integer taxIncluded;


    /**
     * 选中的过滤条件
     */
    @ApiModelProperty("选中的过滤条件")
    private String filterConditions;

    /**
     * 借贷分录科目id
     */
    @ApiModelProperty("借贷分录科目id")
    private Long subjectSystemId;

    /**
     * 借贷分录
     * [{
     *  "type": "debit/icredit", // 借/贷方向
     *  "subjectId": "xxx", //分类科目id
     *  "content": "xxx" // 辅助核算内容
     * }]
     */
    @ApiModelProperty("借贷分录\n"
        + "[{\n"
        + " \"type\": \"debit/icredit\", // 借/贷方向\n"
        + " \"subjectId\": \"xxx\", //分类科目id\n"
        + " \"content\": \"xxx\" // 辅助核算内容\n"
        + "}]")
    private String entries;

    /**
     * 排序 从小到大排序
     */
    @ApiModelProperty("排序 从小到大排序")
    private Integer sortNum;

    /**
     * 即时推送/定时推送
     */
    @ApiModelProperty("0-即时推送/1-定时推送")
    private Integer executeType;

    /**
     * 推凭时间
     * {
     *   "frequency": "day/month/year",
     *   "time": "xxx"
     * }
     */
    @ApiModelProperty(" 推凭时间\n"
        + " {\n"
        + " \"frequency\": \"day/month/year\",\n"
        + " \"time\": \"xxx\"\n"
        + " }")
    private String executeTime;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private String creator;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private String operator;

    /**
     * 操作人姓名
     */
    @ApiModelProperty("操作人姓名")
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("修改时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
