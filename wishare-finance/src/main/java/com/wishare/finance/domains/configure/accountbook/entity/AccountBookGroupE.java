package com.wishare.finance.domains.configure.accountbook.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import com.wishare.finance.apps.model.configure.accountbook.fo.StatutoryBody;
import com.wishare.finance.domains.configure.accountbook.support.ListChargeItemTypeHandler;
import com.wishare.finance.domains.configure.accountbook.support.ListCostCenterTypeHandler;
import com.wishare.finance.domains.configure.accountbook.support.ListStatutoryBodyTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 账簿所属表(AccountBookGroup)实体类
 *
 * @author makejava
 * @since 2022-10-14 09:35:36
 */
@Getter
@Setter
@TableName(value = "account_book_group", autoResultMap = true)
public class AccountBookGroupE  {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 账簿主表id
     */
    private Long accountBookId;
    /**
     * 成本中心json
        {
            "costCenterId":"",
            "costCenterName":""
        }
     */
    @TableField(typeHandler = ListCostCenterTypeHandler.class)
    private List<CostCenter> costCenter;

    /**
     * 成本中心路径
     */
    private String costCenterPath;
    /**
     * 费项json
        {
            "chargeItemId":"",
            "chargeItemName":""
        }
     */
    @TableField(typeHandler = ListChargeItemTypeHandler.class)
    private List<ChargeItem> chargeItem;

    /**
     * 法定单位路径
     */
    private String statutoryBodyPath;
    /**
     * 法定单位json
     {
     "statutoryBodyId":"",
     "statutoryBodyName":""
     }
     */
    @TableField(typeHandler = ListStatutoryBodyTypeHandler.class)
    private List<StatutoryBody> statutoryBody;

    /**
     * 费项路径
     */
    private String chargeItemPath;
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


}

