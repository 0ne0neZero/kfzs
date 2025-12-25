package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 审核账单命令
 * @Author dxclay
 * @Date 2022/8/25
 * @Version 1.0
 */
@Getter
@Setter
public class ApproveBillCommand {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    private Integer approveState;

    /**
     * 驳回理由
     */
    private String rejectReason;

    /**
     * 审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款
     */
    private Integer operateType;


}
