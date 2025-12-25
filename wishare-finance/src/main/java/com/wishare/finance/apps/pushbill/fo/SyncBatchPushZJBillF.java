package com.wishare.finance.apps.pushbill.fo;

import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillSysEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量同步请求参数
 * @author dxclay
 * @since  2023/3/10
 * @version 1.0
 */
@Setter
@Getter
@ApiModel("批量同步请求参数")
public class SyncBatchPushZJBillF {

    @ApiModelProperty(value = "推单id列表")
    @Size(min = 1, max = 10, message = "推单同步条数仅允许1~10条")
    private List<Long> voucherIds;

    private Integer voucherSystem = PushBillSysEnum.中交系统.getCode();
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 业务类型ID
     */
    @ApiModelProperty(value = "业务类型ID")
    private String YWLX;
    /**
     * 行政组织OID
     */
    @ApiModelProperty(value = "行政组织OID")
    private String XZZZ;
    /**
     * 行政部门OID
     */
    @ApiModelProperty(value = "行政部门OID")
    private String XZBM;

    // 1 已审批  否则未审批
    private Integer approveFlag;
}
