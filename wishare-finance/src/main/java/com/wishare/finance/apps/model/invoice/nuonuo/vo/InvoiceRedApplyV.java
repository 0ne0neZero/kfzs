package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字专用发票信息表申请反参")
public class InvoiceRedApplyV {

    @ApiModelProperty("24位申请单号")
    private String billNo;

    @ApiModelProperty("信息表编号")
    private String billInfoNo;

    @ApiModelProperty("信息表状态(0:申请中 1:审核成功 2:审核失败 3:申请成功 4:申请失败 6:撤销中 7:撤销失败 8:已撤销)")
    private String billStatus;

    @ApiModelProperty("[信息表状态代码]信息表状态描述")
    private String billMessage;

}
