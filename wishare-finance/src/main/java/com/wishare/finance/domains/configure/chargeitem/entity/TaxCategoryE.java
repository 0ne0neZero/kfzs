package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.starter.beans.IdentityInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 税种表(TaxCategory)实体类
 *
 * @author makejava
 * @since 2022-07-19 16:37:59
 */
@Getter
@Setter
@TableName(TableNames.TAX_CATEGORY)
public class TaxCategoryE   {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 税种编码
     */
    private String code;
    /**
     * 税种名称
     */
    private String name;
    /**
     * 父税种id
     */
    private Long parentId;
    /**
     * 税种id路径
     */
    private String path;
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
     * 创建人名称
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
     * 操作人名称
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;

    /**
     * 删除动作
     */
    public void delete(){
        deleted = DataDeletedEnum.DELETED.getCode();
    }

    /**
     * 更新操作人
     */
    public void updateOperator(IdentityInfo identityInfo){
        operator = identityInfo.getUserId();
        operatorName = identityInfo.getUserName();
        gmtModify = LocalDateTime.now();
    }

    public void updateOrInsert(){
        if(id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.TAX_CATEGORY);
        }
        if (disabled == null){
            disabled = 0;
        }
        if (parentId == null){
            parentId = 0L;
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

