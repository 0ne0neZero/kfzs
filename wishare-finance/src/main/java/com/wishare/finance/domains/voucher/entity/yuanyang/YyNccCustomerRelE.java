package com.wishare.finance.domains.voucher.entity.yuanyang;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 远洋主数据与ncc客商对照表
 */
@Getter
@Setter
@ApiModel(value = "远洋主数据与ncc客商对照表")
@TableName(value = TableNames.YY_NCC_CUSTOMER_REL, autoResultMap = true)
public class YyNccCustomerRelE {

    @TableId
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("客商（NCC原名)")
    private String customerName;

    @ApiModelProperty("社会信用代码")
    private String creditCode;

    @ApiModelProperty("NCC客户编码")
    private String nccCustomerCode;

    @ApiModelProperty("租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}
