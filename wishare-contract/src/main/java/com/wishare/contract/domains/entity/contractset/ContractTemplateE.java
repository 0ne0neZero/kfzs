package com.wishare.contract.domains.entity.contractset;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合同范本实体
 * @author ljx
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contract_template")
public class ContractTemplateE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同范本id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 合同范本名称
     */
    private String name;

    /**
     * 合同分类id
     */
    private Long categoryId;

    /**
     * 父合同范本id
     */
    private Long parentId;

    /**
     * 范本版本
     */
    private BigDecimal version;

    /**
     * 引用次数
     */
    private Integer useCount;

    /**
     * 引用状态：0未被引用  1被引用
     */
    private Integer useStatus;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0启用，1草稿，2禁用
     */
    private Integer status;

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
     * 创建人id
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
     * 操作人id
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
     * 文件名
     */
    private String fileName;

    /**
     * 合同范本id路径
     */
    private String path;

    /**
     * 合同分类路径名
     */
    private String categoryPathName;

}
