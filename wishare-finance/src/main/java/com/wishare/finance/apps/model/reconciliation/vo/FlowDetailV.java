package com.wishare.finance.apps.model.reconciliation.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流水明细表
 *
 * @author yancao
 */
@Getter
@Setter
public class FlowDetailV {


    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 缴费金额
     */
    private Long settleAmount;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    /**
     * 对方账户
     */
    private String oppositeAccount;

    /**
     * 对方名称
     */
    private String oppositeName;

    /**
     * 对方开户行
     */
    private String oppositeBank;

    /**
     * 本方账户
     */
    private String ourAccount;

    /**
     * 本方名称
     */
    private String ourName;

    /**
     * 本方开户行
     */
    private String ourBank;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 资金用途
     */
    private String fundPurpose;

    /**
     * 交易平台
     */
    private String tradingPlatform;

    /**
     * 交易方式
     */
    private String transactionMode;

    /**
     * 认领状态：0未认领，1已认领
     */
    private Integer claimStatus;

    /**
     * 流水类型：1收入 2退款
     */
    private Integer type;

    /**
     * 是否为同步数据（0否，1是）
     */
    private Integer syncData;


    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


    /**
     * 数据来源id
     */
    private String idExt;

    /**
     * 结算方式编号
     */
    private String jsfsbh;






}
