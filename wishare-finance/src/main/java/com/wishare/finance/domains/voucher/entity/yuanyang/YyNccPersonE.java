package com.wishare.finance.domains.voucher.entity.yuanyang;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ApiModel(value = "ncc人员表")
@TableName(value = TableNames.YY_NCC_PERSON, autoResultMap = true)
public class YyNccPersonE {

    @TableId
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("人员编码")
    private String code;

    @ApiModelProperty("姓名")
    private String name;
//
//    @ApiModelProperty("业务单元")
//    private String businessUnit;

    @ApiModelProperty("业务单元编码")
    private String businessUnitCode;

//    @ApiModelProperty("所在部门")
//    private String dept;

    @ApiModelProperty("所在部门编码")
    private String deptCode;

    @ApiModelProperty("租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    public YyNccPersonE() {
        generateIdentifier();
    }

    public void generateIdentifier() {
        if (Objects.isNull(getId())) {
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.YY_NCC_PERSON));
        }
    }

}
