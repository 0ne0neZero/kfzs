package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.starter.beans.IdentityInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BPM部门实体类
 */
@Getter
@Setter
@TableName("bpm_dept")
public class BPMDeptE {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 部门code
     */
    private String deptCode;
    /**
     * 部门名称
     */
    private String deptName;



    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;
    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人名称
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
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 删除动作
     */
    public void delete() {
        deleted = DataDeletedEnum.DELETED.getCode();
    }

    /**
     * 更新操作人动作
     *
     * @param identityInfo
     */
    public void updateOperator(IdentityInfo identityInfo) {
        operator = identityInfo.getUserId();
        operatorName = identityInfo.getUserName();
        gmtModify = LocalDateTime.now();
    }


    public void updateOrInsert(){
        if(id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.TAX_RATE);
        }
        if (creator == null){
            creator = ApiData.API.getUserId().orElse("administrator");
        }
        if (creatorName == null){
            creatorName = ApiData.API.getUserName().orElse("系统默认");
        }
        if (gmtCreate == null){
            gmtCreate = LocalDateTime.now();
        }
        if (operator == null){
            operator =ApiData.API.getUserId().orElse("administrator");
        }
        if (operatorName == null){
            operatorName = ApiData.API.getUserName().orElse("系统默认");
        }
        if (gmtModify == null){
            gmtModify = LocalDateTime.now();
        }
        if (tenantId == null){
            tenantId = ApiData.API.getTenantId().get();
        }
    }

}

