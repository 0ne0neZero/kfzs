package com.wishare.finance.domains.configure.cashflow.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 9:23
 * @version: 1.0.0
 */
@Getter
@Setter
@TableName(value = TableNames.CASH_FLOW, autoResultMap = true)
public class CashFlowE {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 父编码
     */
    private String fCode;

    /**
     * 父名称
     */
    private String fName;

    /**
     * 全路径名称
     */
    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<Long> path;

    /**
     * 现金流量项目类型
     * 1=现金流入;
     * 2=现金流出;
     */
    private String itemType;

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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
    /**
     * 数据来源id
     */
    private String idExt;

    public void init(){
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.CASH_FLOW);
        }
    }

}
