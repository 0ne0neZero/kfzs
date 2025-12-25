package com.wishare.finance.domains.invoicereceipt.entity.nuonuo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * (NuonuoToken)实体类
 *
 * @author makejava
 * @since 2022-11-01 16:48:12
 */
@Getter
@Setter
@TableName("nuonuo_token")
public class NuonuoTokenE  {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 销方税号
     */
    private String salerTaxNum;
    /**
     * 诺诺token
     */
    private String token;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}

