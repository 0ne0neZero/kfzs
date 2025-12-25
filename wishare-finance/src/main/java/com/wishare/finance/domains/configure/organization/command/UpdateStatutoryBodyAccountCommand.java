package com.wishare.finance.domains.configure.organization.command;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description: 修改银行账户command
 */
@Getter
@Setter
public class UpdateStatutoryBodyAccountCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 账户名称
     */
    private String name;
    /**
     * 账户类型：1.基本账户，2一般账户，3专用账户
     */
    private Integer type;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 开户行账号
     */
    private String bankAccount;
    /**
     * 收款付款类型：1.收款付款，2.收款，3.付款
     */
    private Integer recPayType;
    /**
     * 法定单位Id
     */
    private Long statutoryBodyId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 创建人ID
     */
    private String creator;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;
}
