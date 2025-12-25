package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("刷新信息")
public class RefreshStateV {

    private String communityId;

    /**
     * 状态:0 未刷新 1 刷新成功 2 刷新失败
     */
    private Integer state = 0;

}
