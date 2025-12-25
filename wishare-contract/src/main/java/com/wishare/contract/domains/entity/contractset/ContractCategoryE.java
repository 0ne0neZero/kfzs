package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 合同分类实体
 *
 * @author yancao
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contract_category")
public class ContractCategoryE {

    private static final long serialVersionUID = 1L;

    /**
     * 合同分类id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 合同分类名称
     */
    private String name;

    /**
     * 父合同分类id
     */
    private Long parentId;

    /**
     * 合同分类id路径
     */
    private String path;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 是否删除：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;


    /**
     * 中台业务类型id
     */
    private Long natureTypeId;

    /**
     * 中台业务类型code
     */
    private String natureTypeCode;

    /**
     * 中台业务类型名称
     */
    private String natureTypeName;

    /**
     * 中交合同业务类型编码
     */
    private String bizCode;

    @ApiModelProperty("是否具有采购事项，1是，0否")
    private Integer isBuy;

    /**
     * 简称编码
     **/
    private String abbrCode;

}
