package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDealData {
    @ApiModelProperty("业务系统-单据ID 对应查询参数：djnm")
    private String DOCID;
    @ApiModelProperty("财务云单据ID")
    private String CWYDICID;
    @ApiModelProperty("业务系统-单据编号  对应查询参数：djbh")
    private String DOCCODE;
    @ApiModelProperty("财务云单据编号")
    private String CWYDOCCODE;
    @ApiModelProperty("描述")
    private String DESCRIPTION;
    @ApiModelProperty("调用状态码")
    private String STATUSCODE;
    @ApiModelProperty("来源系统    对应查询参数：lyxt")
    private String LYXT;
    @ApiModelProperty("该单据是否最近一次调用，0是 1否")
    private String ISNEWINVOKE;
    @ApiModelProperty(" ID")
    private String ID;
    @ApiModelProperty("是否成功，1失败 0 成功")
    private String RESULT;
    @ApiModelProperty("调用时间")
    private String INVOKETIME;

}
