package com.wishare.finance.apps.model.configure.organization.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("银行账户反参")
public class StatutoryBodyAccountV {

    @ApiModelProperty("银行账户id")
    private Long id;

    @ApiModelProperty("账户名称")
    private String name;

    @ApiModelProperty("账户类型：1.基本账户，2一般账户，3专用账户")
    private Integer type;

    @ApiModelProperty("开户行名称")
    private String bankName;

    @ApiModelProperty("开户行账号")
    private String bankAccount;

    @ApiModelProperty("收款付款类型：1.收款付款，2.收款，3.付款")
    private Integer recPayType;

    @ApiModelProperty("法定单位Id")
    private Long statutoryBodyId;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;
}
