package com.wishare.finance.domains.invoicereceipt.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: zhengHui
 * @createTime: 2022-09-22  14:07
 * @description:数据库基础字段
 */
@Getter
@Setter
@Accessors(chain = true)
public class FinanceBaseEntity {


    @ApiModelProperty("删除状态：0 未删除，1 已删除")
    protected Byte deleted;

    @ApiModelProperty("租户id")
    @TableField(fill = FieldFill.INSERT)
    protected String tenantId;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    protected String creator;

    @ApiModelProperty("创建人名字")
    @TableField(fill = FieldFill.INSERT)
    protected String creatorName;

    @ApiModelProperty("操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String operator;

    @ApiModelProperty("操作人姓名")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String operatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime gmtCreate;
    @ApiModelProperty("最后修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime gmtModify;

}
