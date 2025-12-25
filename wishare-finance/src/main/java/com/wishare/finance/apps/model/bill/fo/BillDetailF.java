package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取账单详情")
public class BillDetailF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单 4.应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    /**
     * 校检账单类型
     */
    public void checkBillType() {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(this.billType);
        if (null == billTypeEnum) {
            throw BizException.throw400("账单类型异常");
        }
        if (billTypeEnum == BillTypeEnum.默认) {
            throw BizException.throw400("暂不支持此类账单查看详情");
        }
    }
}
