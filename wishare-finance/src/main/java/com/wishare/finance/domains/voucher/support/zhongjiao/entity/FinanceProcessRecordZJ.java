package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName(value = TableNames.FINANCE_PROCESS_RECORD_ZJ, autoResultMap = true)
public class FinanceProcessRecordZJ extends BaseEntity {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 所属业务单id
     */
    private String mainDataId;

    /**
     * 流程所属类型 1:业务支付申请单 2:对下结算单(待定)
     */
    private Integer type;

    /**
     * 审批状态
     */
    @TableField("reviewStatus")
    private Integer reviewStatus;

    private Integer isJsdProcess;

}

