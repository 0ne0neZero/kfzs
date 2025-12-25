package com.wishare.finance.domains.configure.arrears.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("arrears_category")
public class ArrearsCategoryE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 欠费类别名称
     */
    private String arrearsCategoryName;

    /**
     * 父类别id
     */
    private Long parentId;


    /**
     * 父类别名称
     */
    private String parentName;

    /**
     * 0-禁用;1-启用;
     */
    private Integer status;

    /**
     * 是否末级：0否,1是
     */
    private Integer lastLevel;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
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
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;


    public ArrearsCategoryE() {
        IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
        this.operator = identityInfo.getUserId();
        this.operatorName = identityInfo.getUserName();
        this.gmtModify = LocalDateTime.now();
        this.tenantId = identityInfo.getTenantId();
    }

}
