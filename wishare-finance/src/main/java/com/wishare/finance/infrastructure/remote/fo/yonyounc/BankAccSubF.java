package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/12/9 17:31
 * @version: 1.0.0
 */
@ApiModel("银行账户新增类")
@Valid
@Getter
@Setter
public class BankAccSubF {

    /**
     * 户名
     */
    @ApiModelProperty("户名")
    private String accName;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String accNum;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 传递时间 默认当前系统时间
     */
    @ApiModelProperty("传递时间 默认当前系统时间")
    private String creationTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creator;

    /**
     * 分布式
     */
    @ApiModelProperty("分布式")
    private Integer dataOriginFlag = 0;

    /**
     * 冻结状态
     */
    @ApiModelProperty("冻结状态")
    private String fronzenState;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String name;

    /**
     * 银行账户主键
     */
    @ApiModelProperty("银行账户主键")
    private String pkBankAccbas;

    /**
     * 银行账户子户主键
     */
    @ApiModelProperty("银行账户子户主键")
    private String pkBankAccsub;

    /**
     * 币种
     */
    @ApiModelProperty("币种")
    private String pkCurrtype;

    /**
     * 账户类型 0=活期;1=定期;2=通知;3=票据;4=保证金户;
     */
    @ApiModelProperty("账户类型 0=活期;1=定期;2=通知;3=票据;4=保证金户;")
    private Integer accType;

    /**
     * 交易账户 Y N 默认Y
     */
    @ApiModelProperty("交易账户 Y N 默认Y")
    private char isTrade = 'Y';

    /**
     * 透支额度
     */
    @ApiModelProperty("透支额度")
    private Double overDraftmny;

    /**
     * 透支控制方式 0=控制; 1=提示; 2=不控制; 默认 2
     */
    @ApiModelProperty("透支控制方式 "
        + "0=控制; 1=提示; 2=不控制;"
        + "默认 2")
    private String overDraftType;

    /**
     * 付款范围 0=不限制; 1=全局内; 2=集团内; 默认 0
     */
    @ApiModelProperty("付款范围"
        + "0=不限制; 1=全局内; 2=集团内; "
        + "默认 0")
    private String payArea;

}
