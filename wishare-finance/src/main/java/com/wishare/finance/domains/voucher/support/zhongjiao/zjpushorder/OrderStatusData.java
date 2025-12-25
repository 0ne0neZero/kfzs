package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusData {

    @ApiModelProperty("审批时间")
    private String SPSJ;
    @ApiModelProperty("业务系统单据内码  对应查询参数：djnm")
    private String LYDJNM;
    @ApiModelProperty("审批人4A人员编码")
    private String SPR;
    @ApiModelProperty("财务云单据内码  注意这个非对应查询参数:djnm")
    private String DJNM;
    @ApiModelProperty("当前环节名称")
    private String DQHJMC;
    @ApiModelProperty("是否需要签退（退回纸质单据）1：是，0：否")
    private String XYQT;
    @ApiModelProperty("审批意见")
    private String SPYJ;
    @ApiModelProperty("财务云单据编号 注意这个非对应查询参数:djbh")
    private String DJBH;
    @ApiModelProperty("当前环节名称")
    private String DQHJ;
}
