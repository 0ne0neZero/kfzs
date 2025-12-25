package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Objects;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "关账记录表")
@TableName(value = TableNames.CLOSE_ACCOUNT, autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CloseAccount {

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

   public void generateIdentifier() {
       if (java.util.Objects.isNull(getId())){
           setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.CLOSE_ACCOUNT));
       }
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CloseAccount that = (CloseAccount) o;
        return Objects.equal(accountBookId, that.accountBookId)
                && Objects.equal(accountBookCode, that.accountBookCode)
                && Objects.equal(accountingPeriod, that.accountingPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountBookId, accountBookCode, accountingPeriod);
    }
}
