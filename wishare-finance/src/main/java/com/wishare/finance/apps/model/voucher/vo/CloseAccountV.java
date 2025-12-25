package com.wishare.finance.apps.model.voucher.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CloseAccountV {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "账簿id")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "关账状态：0，未关账， 1，已关账")
    private Integer states;

    @ApiModelProperty(value = "关账列表按钮展示：0，展示，1，不展示")
    private Integer deleted;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;
}
