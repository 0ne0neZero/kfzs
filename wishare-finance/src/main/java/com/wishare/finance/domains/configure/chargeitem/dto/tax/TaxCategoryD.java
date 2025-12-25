package com.wishare.finance.domains.configure.chargeitem.dto.tax;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaxCategoryD {

    /**
     * 税种id
     */
    @TableField("id")
    private Long id;

    /**
     * 税种编码
     */
    @TableField("code")
    private String code;
    /**
     * 税种名称
     */
    @TableField("name")
    private String name;
    /**
     * 父税种id
     */
    @TableField("parent_id")
    private Long parentId;
    /**
     * 税种id路径
     */
    @TableField("path")
    private String path;
    /**
     * 是否禁用：0启用，1禁用
     */
    @TableField("disabled")
    private Integer disabled;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建人ID
     */
    @TableField("creator")
    private String creator;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField("gmtCreate")
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField("operator")
    private String operator;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField("gmtModify")
    private LocalDateTime gmtModify;


    /**
     * 税率信息
     */
    @TableField(exist = false)
    private List<TaxRateD> taxRateS;
}
