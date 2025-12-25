package com.wishare.finance.apps.model.voucher.vo;

import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 新版规则展示列表
 * @author: pgq
 * @since: 2023/1/2 18:47
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("新版规则展示列表")
public class VoucherRuleEventV {

    @ApiModelProperty("事件名称")
    private String eventName;

    @ApiModelProperty("事件编码")
    private Integer eventCode;

    @ApiModelProperty("该事件下的规则")
    private List<VoucherRuleE> list;
}
