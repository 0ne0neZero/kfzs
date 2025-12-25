package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@TableName("voucher_bill_file_zj")
public class VoucherBillFileZJ {

    @ApiModelProperty(value = "id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "报账单id")
    private Long voucherBillId;

    @ApiModelProperty(value = "报账单号")
    private String voucherBillNo;

    @ApiModelProperty("附件信息")
    private String files;

    /**
     * 是否上传到财务云， 默认没有 1标识已经上传  0 标识没有上传
     */
    private Integer uploadFlag;

    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @ApiModelProperty(value = "租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
}
