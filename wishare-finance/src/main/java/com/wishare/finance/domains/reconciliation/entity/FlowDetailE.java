package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 流水明细表
 *
 * @author yancao
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flow_detail")
@ToString
public class FlowDetailE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

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
     * 认领状态：0未认领，1已认领 2 挂起 3 审核中
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
     * 回单地址
     */
    private String receiptUrl;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否已使用（用于判断流水金额和开票金额明细）
     */
    @TableField(exist = false)
    private Boolean dealFlag = false;

    /**
     * 成本中心id
     */
    @TableField(exist = false)
    private Long costCenterId;


    /**
     * 数据来源id
     */
    private String idExt;

    /**
     * 结算方式编号
     */
    private String jsfsbh;

    public void init(String englishName) {
        if (Objects.isNull(id)){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("flow_detail"));
        }
        if(StringUtils.isBlank(serialNumber)){
            setSerialNumber(IdentifierFactory.getInstance().serialNumber("flow_detail_serial_number","LS", 20));
        }
    }

}
