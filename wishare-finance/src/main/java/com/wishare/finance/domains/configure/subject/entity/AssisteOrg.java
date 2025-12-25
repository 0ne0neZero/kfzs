package com.wishare.finance.domains.configure.subject.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 辅助核算（业务单元）
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Getter
@Setter
@TableName(value = TableNames.ASSISTE_ORG)
@ApiModel(value="AssisteOrg对象", description="辅助核算（业务单元）")
public class AssisteOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "辅助核算部门编码")
    private String code;

    @ApiModelProperty(value = "辅助核算部门名称")
    private String name;

    @ApiModelProperty(value = "辅助核算编码")
    private String ascCode;

    @ApiModelProperty(value = "辅助核算名称")
    private String ascName;

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "上级部门编码")
    private String supCode;

    @ApiModelProperty(value = "上级部门名称")
    private String supName;

    @ApiModelProperty(value = "凭证系统 ：1用友NCC")
    private Integer syncSystem;

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

    public void updateOrInsert(){
        if(id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.ASSISTE_ORG);
        }
        if (creator == null){
            creator = ApiData.API.getUserId().orElse("administrator");
        }
        if (creatorName == null){
            creatorName = ApiData.API.getUserName().orElse("系统默认");
        }
        if (gmtCreate == null){
            gmtCreate = LocalDateTime.now();
        }
        if (operator == null){
            operator =ApiData.API.getUserId().orElse("administrator");
        }
        if (operatorName == null){
            operatorName = ApiData.API.getUserName().orElse("系统默认");
        }
        if (gmtModify == null){
            gmtModify = LocalDateTime.now();
        }
        if (tenantId == null){
            tenantId = ApiData.API.getTenantId().get();
        }
    }

}
